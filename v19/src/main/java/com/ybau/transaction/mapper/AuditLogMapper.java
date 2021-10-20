package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.AuditLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuditLogMapper {
    /**
     * 插入审核记录
     * @param map
     */
    void saveAuditLog(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询审核记录
     * @param map
     * @return
     */
    List<AuditLog> findAuditLog(@Param(value = "map") Map<String, Object> map);
}
