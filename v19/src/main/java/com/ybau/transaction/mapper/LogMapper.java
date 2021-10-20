package com.ybau.transaction.mapper;


import com.ybau.transaction.domain.CoLog;
import com.ybau.transaction.domain.Log;
import com.ybau.transaction.domain.SupplierOrderLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sun.awt.SunHints;

import java.util.List;
import java.util.Map;

@Repository
public interface LogMapper {

    /**
     * 存入操作记录
     * @param map
     */
    void saveProductLog(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询日志（分页，条件筛选）
     * @param map
     * @return
     */
    List<Log> findAll(@Param(value = "map")Map<String, Object> map);

    /**
     * 插入定制订单操作记录
     * @param map
     */
    void saveCoLog(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询定制订单操作日志
     * @param map
     * @return
     */
    List<CoLog> findCoLog(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据定制订单ID查询订单订单操作备注
     * @param coId
     * @return
     */
    List<String> findByCoId(@Param(value = "coId") int coId);

    /**
     * 插入供应商订单相关日志
     * @param map
     */
    void saveSupplierLog(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询向供应商订单操作日志
     * @param map
     * @return
     */
    List<SupplierOrderLog> findSupplierLog(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据用户ID查询导入用户记录
     * @param startTime
     * @param endTime
     * @param id
     * @return
     */
    List<Map<String,Object>> findLeadOrder(@Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime,@Param(value = "id") String id);

    /**
     * 根据日志ID删除日志
     * @param logId
     */
    void deleteClearing(@Param(value = "logId") String logId);
}
