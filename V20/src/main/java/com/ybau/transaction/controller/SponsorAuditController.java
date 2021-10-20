package com.ybau.transaction.controller;


import com.ybau.transaction.service.SponsorAuditService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/sponsorAudit")
public class SponsorAuditController {

    @Autowired
    private SponsorAuditService sponsorAuditService;

    /**
     * 根据用户ID查询该用户发起的审核
     *
     * @param request
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return sponsorAuditService.findAll(map, request);
    }




    /**
     * 撤销审核
     * @param orderId
     * @param request
     * @return
     */
    @GetMapping("/repealAudit")
    public ResponseData repealAudit(String orderId,HttpServletRequest request) throws ParseException {
        return sponsorAuditService.repealAudit(orderId,request);
    }
}
