package com.ybau.transaction.service;

import com.ybau.transaction.domain.AuditFlow;
import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface AuditFlowService {
    /**
     * 新增公司审批节点
     * @param map
     * @param request
     * @return
     */
    ResponseData saveAuditFlow(Map<String, Object> map, HttpServletRequest request) throws ParseException;



    /**
     * 查询公司是否已经设置审批流程
     * @param request
     * @return
     */
    ResponseData findAuditFlow(HttpServletRequest request);
}
