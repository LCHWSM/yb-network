package com.ybau.transaction.mapper;


import com.ybau.transaction.domain.ClearingLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClearingLogMapper {


    /**
     * 存入订单操作日志
     * @param map
     */
    void saveClearingLog(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询订单结算日志
     * @param map
     * @return
     */
    List<ClearingLog> findOrderByLog(@Param(value = "map") Map<String, Object> map);

    /**
     * 增加借样订单转销售订单新订单日志
     * @param map
     */
    void saveLog(@Param(value = "map") Map<String, Object> map);
}
