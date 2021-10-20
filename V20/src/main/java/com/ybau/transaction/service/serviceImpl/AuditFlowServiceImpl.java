package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.*;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.AuditFlowService;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.MakeOrderNumUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@Transactional
public class AuditFlowServiceImpl implements AuditFlowService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuditFlowMapper auditFlowMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ProcessGroupMapper processGroupMapper;

    @Autowired
    NodeDraftMapper nodeDraftMapper;

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    private RedissonClient client;

    /**
     * 发布流程
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveAuditFlow(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        List<NodeDraft> nodeDrafts = nodeDraftMapper.findAll(map);
        if (nodeDrafts == null || nodeDrafts.size() < 1) {
            return new ResponseData(400, "未创建审批节点草稿，无法发布", null);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("processGroupUser", id);
        map.put("processGroupTime", df.parse(df.format(new Date())));
        map.put("processGroupId", (int) ((Math.random() * 9 + 1) * 10000000));
        processGroupMapper.saveAuditFlow(map);//插入分组信息
        for (NodeDraft nodeDraft : nodeDrafts) {
            auditFlowMapper.saveAuditFlow(nodeDraft, (Integer) map.get("processGroupId"));
            if (nodeDraft.getNodeDraftWay() == 2 && nodeDraft.getNodeDraftCourse() == 4) {
                List<Map> nodeDraftUserList = nodeDraft.getNodeDraftList();
                if (nodeDraftUserList != null && nodeDraftUserList.size() > 0) {
                    for (Map<String, Object> auditUserMap : nodeDraftUserList) {
                        auditFlowMapper.saveAuditflowByGrouping(nodeDraft.getAuditFlowId(), (String) auditUserMap.get("id"), Integer.parseInt(auditUserMap.get("groupingId").toString()));//如果为分支判断，插入该审批人所管理组ID
                    }
                }
            }
        }
        List<Integer> nodeDraftIds = nodeDraftMapper.findByNodeDraftId((Integer) map.get("organizationId"));
        //删除分支判断信息
        for (Integer nodeDraftId : nodeDraftIds) {
            nodeDraftMapper.deleteAuditFlowByGrouping(nodeDraftId);
        }
        nodeDraftMapper.deleteByOId((Integer) map.get("organizationId"));//根据公司ID删除所有节点
        return new ResponseData(200, "发布成功", null);
    }


    /**
     * 查询公司是否已经创建节点，未创建节点无法下单
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findAuditFlow(HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        List<Integer> list = new ArrayList<>();
        User user = userMapper.findById(id);//根据ID查询用户
        Organization organization = organizationMapper.findByOId(user.getOrganizationId());
        if (organization == null) {
            return new ResponseData(400, "未查询到所属公司", null);
        }
        List<ProcessGroup> auditFlows = processGroupMapper.findByOId(user.getOrganizationId(), organization.getProcessGroupId());//根据公司ID查询公司节点
        if (auditFlows != null && auditFlows.size() > 0) {
            RBucket<Object> clientBucket = client.getBucket("data_ybnetwork.permission" + id);
            String permissionStr = (String) clientBucket.get();
            if (permissionStr != null) {
                List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
                for (Permission permission : permissions) {
                    switch (permission.getUrl()) {
                        case "updatePrice":
                            list.add(1);
                            break;
                        case "externalShow":
                            list.add(2);
                            break;
                        case "externalNoShow":
                            list.add(3);
                            break;
                    }
                }
                return new ResponseData(200, "查询成功", list);
            }
        }
        return new ResponseData(400, "所属公司未创建审核节点，无法下单", null);
    }
}
