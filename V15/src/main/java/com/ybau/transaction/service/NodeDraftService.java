package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface NodeDraftService {
    /**
     * 新增审核节点草稿
     * @param map
     * @param request
     * @return
     */
    ResponseData saveNodeDraft(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 查询所有草稿箱内的节点
     * @param map
     * @return
     */
    ResponseData findAll(Map<String, Object> map);

    /**
     * 修改节点审核信息
     * @param map
     * @return
     */
    ResponseData updateNodeDraft(Map<String, Object> map,HttpServletRequest request) throws ParseException;

    /**
     * 根据ID删除审核节点
     * @param nodeDraftId
     * @return
     */
    ResponseData deleteByNodeDraftId(int nodeDraftId,int organizationId);

    /**
     * 根据节点ID查询节点相关信息
     * @param nodeDraftId
     * @return
     */
    ResponseData findById(int nodeDraftId);
}
