package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.*;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.GoodsService;
import com.ybau.transaction.service.OrderService;
import com.ybau.transaction.service.SponsorAuditService;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class SponsorAuditServiceImpl implements SponsorAuditService {


    @Autowired
    SponsorAuditMapper sponsorAuditMapper;

    @Autowired
    OrderService orderService;

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuditFlowMapper auditFlowMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuditMapper auditMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UserUtil userUtil;

    @Autowired
    GoodsService goodsService;

    @Autowired
    AuditLogMapper auditLogMapper;

    @Autowired
    PermissionUtil permissionUtil ;

    /**
     * 发起审核
     */
    @Override
    public void saveSponsorAudit(String id, String orderId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Organization organization = organizationMapper.findByUId(id);
        map.put("sponsorAuditUser", id);
        map.put("sponsorAuditTime", df.parse(df.format(new Date())));
        map.put("audit", 3);
        map.put("sponsorAuditOrder", orderId);
        List<AuditFlow> auditFlows = auditFlowMapper.findByOId(organization.getOrganizationId(), organization.getProcessGroupId());
        if (auditFlows != null && auditFlows.size() > 0) {
            //赋值目前在第几个节点（发起审核默认在第一个节点）
            map.put("auditNode", auditFlows.get(0).getAuditFlowGrade());
        }
        map.put("processGroupId", organization.getProcessGroupId());//赋值目前公司所使用的流程
        //如果下面创建成功则创建消息返回成功
        sponsorAuditMapper.saveSponsorAudit(map);
        saveAudit(organization.getOrganizationId(), Integer.parseInt(map.get("sponsorAuditId").toString()), id, organization.getProcessGroupId());
    }

    /**
     * 根据用户ID查询用户发起的审核
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("id", id);
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> sponsorAudits = sponsorAuditMapper.findAll(map);
        String name = permissionUtil.findByShow(id);
        for (Order sponsorAudit : sponsorAudits) {
            sponsorAudit.setOrderSignNameStr(name);
        }
        PageInfo pageInfo = new PageInfo(sponsorAudits);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 创建审核
     *
     * @param organizationId 公司ID
     * @param sponsorAuditId 发起审核消息ID
     */
    public void saveAudit(int organizationId, int sponsorAuditId, String sponsorUserId, int processGroupId) throws
            Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Map<String, Object> map = new HashMap<>();
        //根据公司ID和目前公司使用的流程组查询该公司审核节点
        List<AuditFlow> auditFlows = auditFlowMapper.findByOId(organizationId, processGroupId);
        //根据发起审核ID查询该审核目前在第几个节点
        SponsorAudit sponsorAudit = sponsorAuditMapper.findBySId(sponsorAuditId);
        for (AuditFlow auditFlow : auditFlows) {
            if (auditFlow.getAuditFlowGrade() == sponsorAudit.getAuditNode()) {
                //用户发起的节点跟公司审核节点对应
                map.put("auditLogOrder", sponsorAudit.getSponsorAuditOrder());
                map.put("auditFlowName", auditFlow.getAuditFlowName());
                map.put("auditLogTime", df.parse(df.format(new Date())));
                map.put("auditCondition", "系统自动审核通过");
                if (auditFlow.getAuditFlowWay() == 1 && auditFlow.getAuditFlowCourse() == 6) {
                    //如果是主管审批并且是分级主管审批
                    User user = userMapper.findById(sponsorUserId);
                    User superiorUser = userMapper.findById(user.getUId());
                    if (superiorUser != null) {
                        if (user.getDepartmentId() == superiorUser.getDepartmentId()) {
                            //相同部门一致创建上级审批消息
                            auditMapper.saveAudit(superiorUser.getId(), sponsorAuditId, 4, auditFlow.getAuditFlowId(), sponsorAudit.getProcessGroupId(), df.parse(df.format(new Date())));
                        } else {
                            //不一致设置该节点直接通过
                            //创建系统自动审核消息
                            int count = userMapper.findUserByOrd(sponsorAudit.getSponsorAuditOrder(), user.getId());//查询是否记录里是否已经存在
                            if (count < 1) {
                                //不存在 插入
                                userMapper.saveUserByOrder(sponsorAudit.getSponsorAuditOrder(), user.getId());//插入用户审核记录
                            }
                            int flag = auditMapper.findUserFlowSponsor(user.getId(), sponsorAuditId, auditFlow.getAuditFlowId());//判断此节点是否已经创建审核消息
                            if (flag < 1) {
                                auditMapper.saveAudit(user.getId(), sponsorAuditId, 5, auditFlow.getAuditFlowId(), sponsorAudit.getProcessGroupId(), df.parse(df.format(new Date())));
                            }
                            map.put("auditUser", user.getId());
                            auditLogMapper.saveAuditLog(map);
                            if (auditFlows.size() > sponsorAudit.getAuditNode()) {
                                //如果该公司的审核节点大于目前审核节点可继续审核赋值用户发起的信息为下一个节点
                                sponsorAuditMapper.updateAuditNode(sponsorAuditId, sponsorAudit.getAuditNode() + 1);
                                //修改后重新走流程
                                saveAudit(organizationId, sponsorAuditId, sponsorAudit.getSponsorAuditUser(), sponsorAudit.getProcessGroupId());
                            } else {
                                //如果不大于则审核全部通过更改发起的审核状态为通过，更改订单审核状态为通过
                                sponsorAuditMapper.updateAuditOpinion(sponsorAuditId, 1);//更改用户发起消息为审核通过
                                //更改订单审核状态为审核通过
                                orderMapper.updateAudit(sponsorAudit.getSponsorAuditOrder(), 1);
                            }
                        }
                    } else {
                        //如果等于NUll则无上级此节点通过
                        int count = userMapper.findUserByOrd(sponsorAudit.getSponsorAuditOrder(), user.getId());//查询是否记录里是否已经存在
                        if (count < 1) {
                            //不存在 插入
                            userMapper.saveUserByOrder(sponsorAudit.getSponsorAuditOrder(), user.getId());//插入用户审核记录
                        }
                        int flag = auditMapper.findUserFlowSponsor(user.getId(), sponsorAuditId, auditFlow.getAuditFlowId());//判断此节点是否已经创建审核消息
                        if (flag < 1) {
                            //小于1则是第一次创建
                            auditMapper.saveAuditPass(user.getId(), sponsorAuditId, 5, auditFlow.getAuditFlowId(), sponsorAudit.getProcessGroupId(), df.parse(df.format(new Date())), df.parse(df.format(new Date())));
                        }
                        map.put("auditUser", user.getId());
                        auditLogMapper.saveAuditLog(map);
                        if (auditFlows.size() > sponsorAudit.getAuditNode()) {
                            //如果该公司的审核节点大于目前审核节点可继续审核赋值用户发起的信息为下一个节点
                            sponsorAuditMapper.updateAuditNode(sponsorAuditId, sponsorAudit.getAuditNode() + 1);
                            //修改后重新走流程
                            saveAudit(organizationId, sponsorAuditId, sponsorAudit.getSponsorAuditUser(), sponsorAudit.getProcessGroupId());
                        } else {
                            //如果不大于则审核全部通过更改发起的审核状态为通过，更改订单审核状态为通过
                            sponsorAuditMapper.updateAuditOpinion(sponsorAuditId, 1);//更改用户发起消息为审核通过
                            //更改订单审核状态为审核通过
                            orderMapper.updateAudit(sponsorAudit.getSponsorAuditOrder(), 1);

                        }
                    }
                } else if (auditFlow.getAuditFlowWay() == 1 && auditFlow.getAuditFlowCourse() == 5) {
                    //如果为直接部门主管审核为其创建
                    //调用工具类查询到发起用户整个部门上级
                    String superior = userUtil.findSuperior(sponsorUserId);
                    if (superior.equals(sponsorUserId)) {
                        //如果上级跟发起人是同一人 则直接通过
                        //一致设置该节点直接通过
                        int count = userMapper.findUserByOrd(sponsorAudit.getSponsorAuditOrder(), superior);//查询是否记录里是否已经存在
                        if (count < 1) {
                            //不存在 插入
                            userMapper.saveUserByOrder(sponsorAudit.getSponsorAuditOrder(), superior);//插入用户审核记录
                        }
                        auditMapper.saveAuditPass(superior, sponsorAuditId, 5, auditFlow.getAuditFlowId(), sponsorAudit.getProcessGroupId(), df.parse(df.format(new Date())), df.parse(df.format(new Date())));
                        map.put("auditUser", superior);
                        auditLogMapper.saveAuditLog(map);
                        if (auditFlows.size() > sponsorAudit.getAuditNode()) {
                            //如果该公司的审核节点大于目前审核节点可继续审核赋值用户发起的信息为下一个节点
                            sponsorAuditMapper.updateAuditNode(sponsorAuditId, sponsorAudit.getAuditNode() + 1);
                            //修改后重新走流程
                            saveAudit(organizationId, sponsorAuditId, sponsorAudit.getSponsorAuditUser(), sponsorAudit.getProcessGroupId());
                        } else {
                            //如果不大于则审核全部通过更改发起的审核状态为通过，更改订单审核状态为通过
                            sponsorAuditMapper.updateAuditOpinion(sponsorAuditId, 1);//更改用户发起消息为审核通过
                            //更改订单审核状态为审核通过
                            orderMapper.updateAudit(sponsorAudit.getSponsorAuditOrder(), 1);
                        }
                    } else {
                        //如果不一致则创建审核消息
                        auditMapper.saveAudit(superior, sponsorAuditId, 4, auditFlow.getAuditFlowId(), sponsorAudit.getProcessGroupId(), df.parse(df.format(new Date())));
                    }
                }
                if (auditFlow.getAuditFlowWay() == 2 && auditFlow.getAuditFlowCourse() == 1) {
                    //如果审批方式为依次审批
                    String auditUserStr = auditFlow.getAuditUser();//取出审核人列表
                    List<Map> auditUsers = JsonUtil.jsonToList(auditUserStr, Map.class);//审核人列表转为list集合
                    if (auditUsers == null || auditUsers.size() < 1) {
                        //如果审批人为空，则回滚数据，提示发起失败
                        throw new Exception();
                    }
                    for (Map auditUser : auditUsers) {
                        if ((int) auditUser.get("flag") == 1) {
                            //找到排在第一位的用户  获取用户ID插入消息
                            auditMapper.saveAudit((String) auditUser.get("id"), sponsorAuditId, 4, auditFlow.getAuditFlowId(), sponsorAudit.getProcessGroupId(), df.parse(df.format(new Date())));
                        }
                    }
                }
                if (auditFlow.getAuditFlowWay() == 2 && auditFlow.getAuditFlowCourse() == 2 || auditFlow.getAuditFlowCourse() == 3) {
                    //如果AuditFlowCourse是2或者是3则采用会签||或签的方式
                    String auditUserStr = auditFlow.getAuditUser();
                    List<Map> auditUsers = JsonUtil.jsonToList(auditUserStr, Map.class);
                    for (Map auditUser : auditUsers) {
                        //为所有用户创建审批消息
                        auditMapper.saveAudit((String) auditUser.get("id"), sponsorAuditId, 4, auditFlow.getAuditFlowId(), sponsorAudit.getProcessGroupId(), df.parse(df.format(new Date())));
                    }
                }
                if (auditFlow.getAuditFlowWay() == 2 && auditFlow.getAuditFlowCourse() == 4) {
                    //等于4为分支判断
                    User user = userMapper.findById(sponsorAudit.getSponsorAuditUser());//根据用户id查询出用户所属分组
                    String userId = auditFlowMapper.findByGrouping(user.getGroupingId(), auditFlow.getAuditFlowId());//根据分组和目前审核所在节点查询审批人
                    if (userId == null) {
                        throw new Exception();
                    }
                    auditMapper.saveAudit(userId, sponsorAuditId, 4, auditFlow.getAuditFlowId(), sponsorAudit.getProcessGroupId(), df.parse(df.format(new Date())));
                }
                if (auditFlow.getAuditFlowWay() == 3) {
                    //如果为不需要审核 流程顺序 +1 直接通过
                    sponsorAuditMapper.updateAuditNode(sponsorAuditId, sponsorAudit.getAuditNode() + 1);
                    //等于3不需要审核 直接审核通过
                    sponsorAuditMapper.updateAuditOpinion(sponsorAuditId, 1);//更改用户发起消息为审核通过
                    //更改订单审核状态为审核通过
                    orderMapper.updateAudit(sponsorAudit.getSponsorAuditOrder(), 1);
                }
            }
        }
        //如果审核节点所在位数已经大于节点总数量则已全部审核通过
        if (sponsorAudit.getAuditNode() > auditFlows.size()) {
            sponsorAuditMapper.updateAuditOpinion(sponsorAuditId, 1);//更改用户发起消息为审核通过
            //更改订单审核状态为审核通过
            orderMapper.updateAudit(sponsorAudit.getSponsorAuditOrder(), 1);
        }
    }


    /**
     * 审核撤销
     *
     * @param orderId
     * @param request
     * @return
     */
    @Override
    public ResponseData repealAudit(String orderId, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Map<String, Object> auditLog = new HashMap();
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        Order order = orderMapper.findOrderId(orderId);//根据ID插叙出订单信息
        if (!id.equals(order.getAddUser())) {
            //如果撤销审核人和下单人不一致返回错误
            return new ResponseData(400, "您不是此订单的下单人，无法撤销审核", null);
        }
        if (order.getAudit() != 3) {
            //如果审核状态不在审核中
            return new ResponseData(400, "该订单审核状态已不在审核中，无法撤销审核", null);
        }
        SponsorAudit sponsorAudit = sponsorAuditMapper.findByOrderId(orderId);//查询出发起审核信息
        //根据分组ID和该审核所在顺序查询审核节点名字
        AuditFlow auditFlow = auditFlowMapper.findGradeByGroupId(sponsorAudit.getAuditNode(), sponsorAudit.getProcessGroupId());
        auditLog.put("auditLogOrder", sponsorAudit.getSponsorAuditOrder());
        auditLog.put("auditFlowName", auditFlow.getAuditFlowName());
        auditLog.put("auditCondition", "审核撤销");
        auditLog.put("auditUser", id);
        auditLog.put("auditLogTime", df.parse(df.format(new Date())));
        auditLogMapper.saveAuditLog(auditLog);
        sponsorAuditMapper.updateAuditOpinion(sponsorAudit.getSponsorAuditId(), 4);//更改发起消息为审核撤销
        //更改所有待用户审核信息为撤销审核
        auditMapper.updateBySponsorAudit(sponsorAudit.getSponsorAuditId(), 3, 4, df.parse(df.format(new Date())));//更改跟此订单相关联的消息为审核撤销(只更改审核中)
        orderMapper.updateAudit(orderId, 4);
        //回滚额度和库存
        if (order.getPaymentMethod() != 1) {
            //非额度下单只回滚库存
            goodsService.updateWarehouse(order.getGoodsDetail());
        } else {
            //如果为额度下单，则回滚库存和额度
            //回滚额度 为实收金额+订单运费-已结算金额（未结算时 未结算金额默认为0）
            double sub = BigDecimalUtil.sub(BigDecimalUtil.add(order.getActualMoney(), order.getFreight()), order.getSettlementAmount());
            Map<String, Object> map = new HashMap<>();
            map.put("id", order.getAddCompany());
            map.put("now", sub);
            organizationMapper.updateUsable(map);//回滚额度
            goodsService.updateWarehouse(order.getGoodsDetail());//回滚库存
        }
        return new ResponseData(200, "撤销成功", null);
    }
}
