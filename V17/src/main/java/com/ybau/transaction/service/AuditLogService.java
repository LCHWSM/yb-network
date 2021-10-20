package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import java.util.Map;

public interface AuditLogService {
    ResponseData findAuditLog(Map<String, Object> map);

}
