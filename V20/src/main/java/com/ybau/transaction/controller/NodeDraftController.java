package com.ybau.transaction.controller;

import com.ybau.transaction.service.NodeDraftService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * 审核节点草稿箱
 */
@RestController
@RequestMapping("/nodeDraft")
public class NodeDraftController {

    @Autowired
    NodeDraftService nodeDraftService;

    /**
     * 新增审核节点草稿
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveNodeDraft")
    public ResponseData saveNodeDraft(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return nodeDraftService.saveNodeDraft(map, request);
    }

    /**
     * 查询所有草稿箱内的节点
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String,Object> map){
        return nodeDraftService.findAll(map);
    }

    /**
     * 修改节点审核信息
     * @param map
     * @return
     */
    @PostMapping("/updateNodeDraft")
    public ResponseData updateNodeDraft(@RequestBody Map<String,Object> map,HttpServletRequest request) throws ParseException {
        return nodeDraftService.updateNodeDraft(map,request);
    }

    /**
     * 根据ID删除审核节点
     * @param nodeDraftId
     * @return
     */
    @GetMapping("/deleteByNodeDraftId")
    public ResponseData deleteByNodeDraftId(int nodeDraftId,int organizationId){
        return nodeDraftService.deleteByNodeDraftId(nodeDraftId,organizationId);
    }

    /**
     * 根据节点ID查询节点相关信息
     * @param nodeDraftId
     * @return
     */
    @GetMapping("/findById")
    public ResponseData findById(int nodeDraftId){
        return nodeDraftService.findById(nodeDraftId);
    }

}
