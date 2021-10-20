package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.CustomizedOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustomizedOrderMapper {
    /**
     * 插入用户定制需求
     * @param map
     */
    void saveCo(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询所有用户定制需求
     * @param map
     * @return
     */
    List<CustomizedOrder> findAll(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据用户手机号查询用户定制需求（客户端)
     * @param map
     * @return
     */
    List<CustomizedOrder> findClient(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据ID查询定制订单的信息
     * @param coId
     * @return
     */
    CustomizedOrder findById(@Param(value ="coId") int coId);

    /**
     * 插入中间表信息
     * @param coId
     * @param user
     */
    void userCustomizedorder(@Param(value = "coId") int coId,@Param(value = "user") String user);

    /**
     * 修改订单信息
     * @param map
     */
    void updateFlag(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据定制订单ID删除所有跟进人信息
     * @param coId
     */
    void deleteByCoId(@Param(value = "coId") int coId);

    /**
     * 修改订单信息
     * @param map
     */
    void updateOrder(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据订单ID 和用户ID查询是否是此订单的跟进人
     * @param coId
     * @param id
     * @return
     */
    int findByUser(@Param(value = "coId") int coId,@Param(value = "id") String id);

    /**
     * 根据用户ID查询用户跟进的订单
     * @param map
     * @return
     */
    List<CustomizedOrder> findByUserId(@Param(value = "map") Map<String, Object> map);
}
