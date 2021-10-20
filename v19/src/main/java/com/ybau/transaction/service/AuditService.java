package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface AuditService {
    /**
     * 根据用户ID查询待该用户审核的信息
     * @param map
     * @param request
     * @return
     */
    ResponseData findAll(Map<String, Object> map, HttpServletRequest request);

    /**
     * 审核
     * @param map
     * @param request
     * @return
     */
    ResponseData auditResult(Map<String, Object> map, HttpServletRequest request) throws Exception;


    /**
     * 查询已审核订单接口
     * @param map
     * @param request
     * @return
     */
    ResponseData findByOperation(Map<String, Object> map, HttpServletRequest request);
}
