package com.ybau.transaction.controller;

import com.ybau.transaction.service.AuditFlowService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * 审核节点web层
 */
@RestController
@RequestMapping("/auditFlow")
public class AuditFlowController {

    @Autowired
    AuditFlowService auditFlowService;

    /**
     * 发布公司审批节点
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveAuditFlow")
    public ResponseData saveAuditFlow(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return auditFlowService.saveAuditFlow(map, request);
    }



    /**
     * 查询公司是否已经设置审批节点
     * @param request
     * @return
     */
    @RequestMapping("/findAuditFlow")
    public ResponseData findAuditFlow(HttpServletRequest request) {
        return auditFlowService.findAuditFlow(request);
    }


}
