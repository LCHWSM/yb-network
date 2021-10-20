package com.ybau.transaction.controller;

import com.ybau.transaction.service.AuditService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    AuditService auditService;

    /**
     * 根据用户ID查询待该用户审核的信息
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String,Object>map, HttpServletRequest request){
        return auditService.findAll(map,request);
    }

    /**
     * 审核
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/auditResult")
    public ResponseData auditResult(@RequestBody Map<String,Object> map,HttpServletRequest request) throws Exception {
        return auditService.auditResult(map,request);
    }

    /**
     * 查询已操作审核信息
     * @param map
     * @return
     */
    @PostMapping("/findByOperation")
    public ResponseData findByOperation(@RequestBody Map<String,Object> map,HttpServletRequest request){
        return auditService.findByOperation(map,request);
    }


}
