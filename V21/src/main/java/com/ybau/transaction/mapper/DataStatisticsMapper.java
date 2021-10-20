package com.ybau.transaction.mapper;


import com.ybau.transaction.domain.Order;
import com.ybau.transaction.domain.Organization;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface DataStatisticsMapper {
    /**
     * 修改数据统计
     *
     * @param paymentMethod
     * @param companyOrderSum
     * @param companyOrderQuit
     * @param companyOrderTrun
     * @param orderSumNumber
     * @param orderQuitNumber
     * @param orderTrunNumber
     * @param organizationId
     * @return
     */
    int updateDataStatistics(@Param(value = "paymentMethod") String paymentMethod, @Param(value = "companyOrderSum") double companyOrderSum,
                             @Param(value = "companyOrderQuit") double companyOrderQuit, @Param(value = "companyOrderTrun") double companyOrderTrun,
                             @Param(value = "orderSumNumber") int orderSumNumber, @Param(value = "orderQuitNumber") int orderQuitNumber,
                             @Param(value = "orderTrunNumber") int orderTrunNumber, @Param(value = "organizationId") String organizationId);


    /**
     * 新增数据统计
     *
     * @param paymentMethod
     * @param companyOrderSum
     * @param companyOrderQuit
     * @param companyOrderTrun
     * @param orderSumNumber
     * @param orderQuitNumber
     * @param orderTrunNumber
     * @param organizationId
     */
    void insertDataStatistics(@Param(value = "paymentMethod") String paymentMethod, @Param(value = "companyOrderSum") double companyOrderSum,
                              @Param(value = "companyOrderQuit") double companyOrderQuit, @Param(value = "companyOrderTrun") double companyOrderTrun,
                              @Param(value = "orderSumNumber") int orderSumNumber, @Param(value = "orderQuitNumber") int orderQuitNumber,
                              @Param(value = "orderTrunNumber") int orderTrunNumber, @Param(value = "organizationId") String organizationId);

    /**
     * 更改数据信息 （时间）
     *
     * @param paymentMethod
     * @param companyOrderSum
     * @param companyOrderQuit
     * @param companyOrderTrun
     * @param orderSumNumber
     * @param orderQuitNumber
     * @param orderTrunNumber
     * @param organizationId
     * @param time
     * @return
     */
    int updateTimeDataStatistics(@Param(value = "paymentMethod") String paymentMethod, @Param(value = "companyOrderSum") double companyOrderSum,
                                 @Param(value = "companyOrderQuit") double companyOrderQuit, @Param(value = "companyOrderTrun") double companyOrderTrun,
                                 @Param(value = "orderSumNumber") int orderSumNumber, @Param(value = "orderQuitNumber") int orderQuitNumber,
                                 @Param(value = "orderTrunNumber") int orderTrunNumber, @Param(value = "organizationId") String organizationId, @Param(value = "time") String time);

    /**
     * 插入数据信息（根据时间）
     *
     * @param paymentMethod    下单方式
     * @param companyOrderSum  订单总金额
     * @param companyOrderQuit 订单退款金额
     * @param companyOrderTrun 订单转销售金额
     * @param orderSumNumber   订单总数量
     * @param orderQuitNumber  订单退款数量
     * @param orderTrunNumber  订单转销售数量
     * @param organizationId   公司ID
     * @param time             时间
     */
    void insertTimeDataStatistics(@Param(value = "paymentMethod") String paymentMethod, @Param(value = "companyOrderSum") double companyOrderSum,
                                  @Param(value = "companyOrderQuit") double companyOrderQuit, @Param(value = "companyOrderTrun") double companyOrderTrun,
                                  @Param(value = "orderSumNumber") int orderSumNumber, @Param(value = "orderQuitNumber") int orderQuitNumber,
                                  @Param(value = "orderTrunNumber") int orderTrunNumber, @Param(value = "organizationId") String organizationId, @Param(value = "time") String time);

    /**
     * 查询所有公司所有下单方式 数据统计
     *
     * @param map
     * @return 所有公司数据统计
     */
    List<Organization> findAll(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询公司按时间统计数据
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> findTime(@Param(value = "map") Map<String, Object> map);

    /**
     * 按公司ID 和时间查询具体订单
     *
     * @param startTime
     * @param nextTime
     * @param organizationId
     * @return
     */
    List<Order> findByOrder(@Param(value = "startTime") Date startTime, @Param(value = "nextTime") Date nextTime, @Param(value = "organizationId") int organizationId);
}
