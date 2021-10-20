package com.ybau.transaction.service.serviceImpl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.AuditLog;
import com.ybau.transaction.mapper.AuditLogMapper;
import com.ybau.transaction.service.AuditLogService;
import com.ybau.transaction.service.AuditService;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {
    @Autowired
    AuditLogMapper auditLogMapper;


    /**
     * 查询审核记录
     * @param map
     */
    @Override
    public ResponseData findAuditLog(Map<String, Object> map) {
        int pageSize = (int) map.get("pageSize");
        int pageNum = (int) map.get("pageNum");
        PageHelper.startPage(pageNum,pageSize);
        List<AuditLog> auditLogs=auditLogMapper.findAuditLog(map);
        PageInfo pageInfo=new PageInfo(auditLogs);
        return new ResponseData(200,"查询成功",pageInfo);
    }
}
