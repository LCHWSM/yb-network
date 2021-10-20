package com.ybau.transaction.controller;


import com.ybau.transaction.service.AuditLogService;
import com.ybau.transaction.util.ResponseData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auditLog")
public class AuditLogController {

    @Autowired
    AuditLogService auditLogService;

    /**
     * 查询审核记录
     * @param map
     * @return
     */
    @PostMapping("/findAuditLog")
    public ResponseData findAuditLog(@RequestBody Map<String,Object> map){
       return auditLogService.findAuditLog(map);
    }
}
