package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.NodeDraft;
import com.ybau.transaction.mapper.NodeDraftMapper;
import com.ybau.transaction.service.NodeDraftService;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class NodeDraftServiceImpl implements NodeDraftService {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    NodeDraftMapper nodeDraftMapper;

    /**
     * 新增审核节点草稿
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveNodeDraft(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("addUser", id);
        map.put("addTime", df.parse(df.format(new Date())));
        List<NodeDraft> nodeDrafts = nodeDraftMapper.findByOId((int) map.get("organizationId"));//查询出该公司节点数
        if (nodeDrafts.size() > 0 && (int) map.get("nodeDraftWay") == 3) {
            return new ResponseData(400, "该公司已经创建审批节点，无法设置为不需要审核", null);
        }
        if (nodeDrafts != null && nodeDrafts.size() > 0) {
            if (nodeDrafts.get(0).getNodeDraftWay() == 3) {
                return new ResponseData(400, "该公司根节点为不需要审核，无法继续创建审批流程", null);
            }
        }
        map.put("nodeDraftGrade", nodeDrafts.size() + 1);//在已有的节点数量后加1
        if (map.get("nodeDraftUser") != null) {
            //审核用户列表转为JSON字符串存储
            List<Map> auditUser = (List<Map>) map.get("nodeDraftUser");
            map.put("nodeDraftUser", JsonUtil.listToJson(auditUser));
        }
        nodeDraftMapper.saveAuditFlow(map);
        if ((int) map.get("nodeDraftWay") == 2 && (int) map.get("nodeDraftCourse") == 4) {
            List<Map<String, Object>> nodeDraftUserList = (List<Map<String, Object>>) map.get("nodeDraftUserList");
            //判断nodeDraftUserList中是否有重复的组ID
            int count = nodeDraftUserList.stream()
                    .collect(Collectors.groupingBy(a -> a.get("groupingId"), Collectors.counting()))
                    .entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
                    .collect(Collectors.toList()).size();
            if (count < 1) {
                for (Map<String, Object> stringObjectMap : nodeDraftUserList) {
                    nodeDraftMapper.saveAuditFlowByGrouping(Integer.parseInt(map.get("nodeDraftId").toString()), (String) stringObjectMap.get("nodedraftUser"), Integer.valueOf(stringObjectMap.get("groupingId").toString()));//如果为分支判断，插入该审批人所管理组ID
                }
            } else {
                return new ResponseData(400, "不可添加重复的组", null);
            }
        }
        return new ResponseData(200, "新增成功", null);
    }

    /**
     * 查询所有草稿箱内的节点
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        int pageSize = (int) map.get("pageSize");
        int pageNum = (int) map.get("pageNum");
        PageHelper.startPage(pageNum, pageSize);
        List<NodeDraft> nodeDrafts = nodeDraftMapper.findAll(map);
        if (nodeDrafts != null && nodeDrafts.size() > 0) {
            for (NodeDraft auditFlow : nodeDrafts) {
                String auditUser = auditFlow.getNodeDraftUser();
                if (auditUser != null) {
                    //审核用户JSON字符串转换为list
                    auditFlow.setNodeDraftUserList(JsonUtil.jsonToList(auditUser, Map.class));
                }
            }
        }
        PageInfo pageInfo = new PageInfo(nodeDrafts);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 修改节点审核信息
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData updateNodeDraft(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        Integer nodeDraftWay = (Integer) map.get("nodeDraftWay");
        if (nodeDraftWay != null && nodeDraftWay == 3) {
            //如果节点设置为不需要审核，删除所有节点调用插入方法重新插入
            List<Integer> nodeDraftIds = nodeDraftMapper.findByNodeDraftId((Integer) map.get("organizationId"));
            //删除分支判断信息
            for (Integer nodeDraftId : nodeDraftIds) {
                nodeDraftMapper.deleteAuditFlowByGrouping(nodeDraftId);
            }
            nodeDraftMapper.deleteByOId((Integer) map.get("organizationId"));//根据公司ID删除所有节点
            saveNodeDraft(map, request);
        }
        if (map.get("nodeDraftUser") != null) {
            //审核用户列表转为JSON字符串存储
            List<Map> auditUser = (List<Map>) map.get("nodeDraftUser");
            map.put("nodeDraftUser", JsonUtil.listToJson(auditUser));
        }
        nodeDraftMapper.updateNodeDraft(map);//执行修改操作
        nodeDraftMapper.deleteAuditFlowByGrouping((int) map.get("nodeDraftId"));//删除该节点所有分支判断选项
        if ((int) map.get("nodeDraftWay") == 2 && (int) map.get("nodeDraftCourse") == 4) {
            List<Map<String, Object>> nodeDraftUserList = (List<Map<String, Object>>) map.get("nodeDraftUserList");
            for (Map<String, Object> auditUserMap : nodeDraftUserList) {
                nodeDraftMapper.saveAuditFlowByGrouping(Integer.parseInt(map.get("nodeDraftId").toString()), (String) auditUserMap.get("nodedraftUser"), Integer.parseInt(auditUserMap.get("groupingId").toString()));//如果为分支判断，插入该审批人所管理组ID
            }
        }
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 根据ID删除审核节点草稿
     *
     * @param nodeDraftId
     * @return
     */
    @Override
    public ResponseData deleteByNodeDraftId(int nodeDraftId, int organizationId) {
        List<NodeDraft> nodeDrafts = nodeDraftMapper.findByOId(organizationId);//查询出该公司节点数
        for (int i = nodeDrafts.size() - 1; i < nodeDrafts.size(); i++) {
            if (nodeDrafts.get(i).getNodeDraftId() != nodeDraftId) {
                return new ResponseData(400, "仅支持删除最后一个节点", null);
            }
        }
        nodeDraftMapper.deleteByNodeDraftId(nodeDraftId);//删除节点相关信息
        nodeDraftMapper.deleteAuditFlowByGrouping(nodeDraftId);//删除该节点所有分支判断选项
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 根据节点ID查询节点相关信息
     *
     * @param nodeDraftId
     * @return
     */
    @Override
    public ResponseData findById(int nodeDraftId) {
        NodeDraft nodeDraft = nodeDraftMapper.findById(nodeDraftId);
        return new ResponseData(200, "查询成功", nodeDraft);
    }
}
