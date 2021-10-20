package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface SponsorAuditService {
    /**
     * 发起审核
     * @param
     * @param
     * @return
     */
    void saveSponsorAudit(String id,String orderId) throws Exception;

    /**
     * 根据用户ID查询用户发起的审核
     * @param request
     * @return
     */
    ResponseData findAll(Map<String,Object> map,HttpServletRequest request);

    /**
     * 创建审核信息
     * @param organizationId
     * @param sponsorAuditId
     * @param sponsorUserId
     * @param processGroupId
     * @throws Exception
     */
    void saveAudit(int organizationId, int sponsorAuditId, String sponsorUserId, int processGroupId) throws Exception ;



    /**
     * 审核撤销
     * @param orderId
     * @param request
     * @return
     */
    ResponseData repealAudit(String orderId, HttpServletRequest request) throws ParseException;
}
