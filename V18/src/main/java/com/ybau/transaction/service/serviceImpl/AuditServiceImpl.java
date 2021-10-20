package com.ybau.transaction.service.serviceImpl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.ybau.transaction.domain.*;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.AuditService;
import com.ybau.transaction.service.GoodsService;
import com.ybau.transaction.service.SponsorAuditService;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@Transactional
public class AuditServiceImpl implements AuditService {

    @Autowired
    AuditMapper auditMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    SponsorAuditMapper sponsorAuditMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    AuditFlowMapper auditFlowMapper;

    @Autowired
    SponsorAuditService sponsorAuditService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuditLogMapper auditLogMapper;

    @Autowired
    PermissionUtil permissionUtil;


//TODO:批量审核

    /**
     * 根据用户ID查询待该用户审核的信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        map.put("userId", id);
        List<Order> audits = auditMapper.findAll(map);
        String name = permissionUtil.findByShow(id);
        if (audits != null && audits.size() > 0) {
            Iterator<Order> iterator = audits.iterator();
            while (iterator.hasNext()) {
                Order order = iterator.next();
                order.setOrderSignNameStr(name);
                if (order.getOrderId() == null) {
                    iterator.remove();
                }
            }
        }
        PageInfo pageInfo = new PageInfo(audits);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 审核
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData auditResult(Map<String, Object> map, HttpServletRequest request) throws Exception {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String token = request.getHeader("token");
            String id = jwtUtil.getId(token);
            Audit audit = auditMapper.findByAuditId((Integer) map.get("auditId"));//根据Id查询审核信息
            if (audit.getAuditOpinion() != 4) {
                //被审核的消息必须是待审核状态
                return new ResponseData(400, "该订单不在审核中，审核失败", null);
            }
            SponsorAudit sponsorAudit = sponsorAuditMapper.findBySId(audit.getSponsorAuditId());
            Order order = orderMapper.findOrderId(sponsorAudit.getSponsorAuditOrder());
            int count = userMapper.findUserByOrd(order.getOrderId(), id);//查询是否记录里是否已经存在
            if (count < 1) {
                //不存在 插入
                userMapper.saveUserByOrder(order.getOrderId(), id);//插入用户审核记录
            }
            if (!audit.getAuditUser().equals(id)) {
                //如果可审核用户 跟审核用户不是同一个人返回错误
                return new ResponseData(400, "该信息不在您审核权限范围内", null);
            }
            AuditFlow auditFlow = auditFlowMapper.findAuditFlow(audit.getAuditflow());
            map.put("auditLogOrder", sponsorAudit.getSponsorAuditOrder());
            map.put("auditFlowName", auditFlow.getAuditFlowName());
            map.put("auditOpinion", map.get("auditRemarks"));
            map.put("auditUser", id);
            map.put("auditLogTime", df.parse(df.format(new Date())));
            if (audit.getAuditOpinion() == 4 && (Integer) map.get("AuditOpinion") == 1) {
                map.put("auditCondition", "审核通过");
                //被审核的消息必须是待审核状态  如果auditOpinion=1 则审核通过
                //根据Id更改该信息为审核通过
                auditMapper.updateAudit((Integer) map.get("auditId"), 1, map.get("auditRemarks").toString(), df.parse(df.format(new Date())));
                if (auditFlow.getAuditFlowWay() == 1 && auditFlow.getAuditFlowCourse() == 5) {
                    //如果该节点为直接主管审核 则直接审核通过节点顺序+1进入下一个节点
                    sponsorAuditMapper.updateAuditNode(audit.getSponsorAuditId(), sponsorAudit.getAuditNode() + 1);
                    //修改后重新走流程
                    sponsorAuditService.saveAudit(order.getAddCompany(), sponsorAudit.getSponsorAuditId(), order.getAddUser(), sponsorAudit.getProcessGroupId());
                }
                if (auditFlow.getAuditFlowWay() == 1 && auditFlow.getAuditFlowCourse() == 6) {
                    //如果是分级主管审批   不递增节点  寻找此审核人的上级
                    sponsorAuditService.saveAudit(order.getAddCompany(), sponsorAudit.getSponsorAuditId(), order.getAddUser(), sponsorAudit.getProcessGroupId());
                }
                if (auditFlow.getAuditFlowWay() == 2 && auditFlow.getAuditFlowCourse() == 1) {
                    //如果是依次审批
                    String auditUserStr = auditFlow.getAuditUser();
                    Integer flag = 0;
                    List<Map> auditUserList = JsonUtil.jsonToList(auditUserStr, Map.class);
                    for (Map auditUser : auditUserList) {
                        if (auditUser.get("id").equals(audit.getAuditUser())) {
                            //找到此审核人的位置  在此审核人位置+1为下一个审核人
                            flag = Integer.parseInt(auditUser.get("flag").toString()) + 1;
                        }
                        if (flag > auditUserList.size()) {
                            //如果下一个审核人的位置大于整个数组则上一个审核人为最后以为，此节点通过
                            sponsorAuditMapper.updateAuditNode(audit.getSponsorAuditId(), sponsorAudit.getAuditNode() + 1);
                            sponsorAuditService.saveAudit(order.getAddCompany(), sponsorAudit.getSponsorAuditId(), order.getAddUser(), sponsorAudit.getProcessGroupId());
                        } else {
                            //如果下一个审核人位置不大于整个数组 则存在下以为审核人  为下个审批人创建审批消息
                            if (auditUser.get("flag") == flag) {
                                //取出顺序相同的用户ID
                                auditMapper.saveAudit(auditUser.get("id").toString(), sponsorAudit.getSponsorAuditId(), 4, auditFlow.getAuditFlowId(), sponsorAudit.getProcessGroupId(), df.parse(df.format(new Date())));
                            }
                        }
                    }
                }
                if (auditFlow.getAuditFlowWay() == 2 && auditFlow.getAuditFlowCourse() == 2) {
                    //如果采用会签的方式  查询所有的审核消息是否全部通过
                    List<Audit> audits = auditMapper.findIfAuditAll(audit.getSponsorAuditId(), audit.getAuditflow());
                    if (audits.size() < 1) {
                        //如果小于1则全部审核通过  进入下一个节点
                        sponsorAuditMapper.updateAuditNode(audit.getSponsorAuditId(), sponsorAudit.getAuditNode() + 1);
                        sponsorAuditService.saveAudit(order.getAddCompany(), sponsorAudit.getSponsorAuditId(), order.getAddUser(), sponsorAudit.getProcessGroupId());
                    }
                }
                if (auditFlow.getAuditFlowWay() == 2 && auditFlow.getAuditFlowCourse() == 3) {
                    //如果采用或签的方式  审核通过即进入下一个节点
                    //更改所有审核节点为审核通过
                    auditMapper.updateSponsorAudit(audit.getSponsorAuditId(), 5, null, df.parse(df.format(new Date())), audit.getAuditflow(), audit.getAuditId());
                    sponsorAuditMapper.updateAuditNode(audit.getSponsorAuditId(), sponsorAudit.getAuditNode() + 1);
                    sponsorAuditService.saveAudit(order.getAddCompany(), sponsorAudit.getSponsorAuditId(), order.getAddUser(), sponsorAudit.getProcessGroupId());
                }
                if (auditFlow.getAuditFlowWay() == 2 && auditFlow.getAuditFlowCourse() == 4) {
                    //如果采用分支判断的方式  审核通过 即进入下一个节点
                    sponsorAuditMapper.updateAuditNode(audit.getSponsorAuditId(), sponsorAudit.getAuditNode() + 1);
                    sponsorAuditService.saveAudit(order.getAddCompany(), sponsorAudit.getSponsorAuditId(), order.getAddUser(), sponsorAudit.getProcessGroupId());
                }
            }
            if (audit.getAuditOpinion() == 4 && (Integer) map.get("AuditOpinion") == 2) {
                map.put("auditCondition", "审核不通过");
                //审核不通过 回滚库存 额度
                if (order.getPaymentMethod() != 1) {
                    //非额度下单只回滚库存
                    goodsService.updateWarehouse(order.getGoodsDetail());
                } else {
                    //如果为额度下单，则回滚库存和额度
                    //回滚额度 为实收金额+订单运费-已结算金额（未结算时 未结算金额默认为0）
                    double sub = BigDecimalUtil.sub(BigDecimalUtil.add(order.getActualMoney(), order.getFreight()), order.getSettlementAmount());
                    Map nowMap = new HashMap<>();
                    nowMap.put("id", order.getAddCompany());
                    nowMap.put("now", sub);
                    organizationMapper.updateUsable(nowMap);//回滚额度
                    goodsService.updateWarehouse(order.getGoodsDetail());//回滚库存
                }
                //审核不通过更改用户发起审核的所有信息为审核不通过
                auditMapper.updateAudit((Integer) map.get("auditId"), 2, map.get("auditRemarks").toString(), df.parse(df.format(new Date())));
                //设置本节点所有审核消息为不通过
                auditMapper.updateSponsorAudit(audit.getSponsorAuditId(), 2, null, df.parse(df.format(new Date())), audit.getAuditflow(), audit.getAuditId());
                //更改用户发起信息为审核不通过
                sponsorAuditMapper.updateAuditOpinion(audit.getSponsorAuditId(), 2);
                //更改订单审核状态为审核不通过
                orderMapper.updateAudit(sponsorAudit.getSponsorAuditOrder(), 2);
            }
            auditLogMapper.saveAuditLog(map);//插入审核记录
            return new ResponseData(200, "审核成功", null);
        } catch (Exception e) {
            log.error("{}", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseData(400, "公司所用审核节点不完善，无法审核", null);
        }
    }

    /**
     * 查询已审核接口信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findByOperation(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        map.put("auditUser", id);
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> audits = auditMapper.findByOperation(map);
        String name = permissionUtil.findByShow(id);
        if (audits != null && audits.size() > 0) {
            Iterator<Order> iterator = audits.iterator();
            while (iterator.hasNext()) {
                Order order = iterator.next();
                order.setOrderSignNameStr(name);
                if (order.getOrderId() == null) {
                    iterator.remove();
                }

            }
        }
        PageInfo pageInfo = new PageInfo(audits);
        return new ResponseData(200, "查询成功", pageInfo);
    }
}
