package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.ExcelOrder;
import com.ybau.transaction.domain.LeadOrder;
import com.ybau.transaction.domain.Money;
import com.ybau.transaction.domain.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import sun.awt.SunHints;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderMapper {
    /**
     * 粤宝网络接口
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Select("select * from orderstest where orderTime BETWEEN #{map.startTime} AND #{map.endTime} ORDER BY orderTime DESC")
    @Results({
            @Result(id = true, column = "orderId", property = "orderId"),
            @Result(column = "orderTime", property = "orderTime"),
            @Result(column = "customerName", property = "customerName"),
            @Result(column = "customerMobile", property = "customerMobile"),
            @Result(column = "receiverName", property = "receiverName"),
            @Result(column = "receiverMobile", property = "receiverMobile"),
            @Result(column = "receiverProvince", property = "receiverProvince"),
            @Result(column = "receiverCity", property = "receiverCity"),
            @Result(column = "receiverDistrict", property = "receiverDistrict"),
            @Result(column = "receiverStreet", property = "receiverStreet"),
            @Result(column = "receiverAddress", property = "receiverAddress"),
            @Result(column = "receiverPostcode", property = "receiverPostcode"),
            @Result(column = "orderId", property = "goodsDetail", many = @Many(select = "com.ybau.transaction.mapper.GoodsMapper.findByOrdersIdtest")),
            @Result(column = "orderId", property = "express", one = @One(select = "com.ybau.transaction.mapper.ExpressMapper.findByOrdersIdtest"))
    })
    List<Order> findOrderDate(@Param(value = "params") Map<String, Object> params) throws Exception;


    /**
     * 根据订单ID查询订单信息
     *
     * @param orderId
     * @return
     */
    Order findByOrder(@Param(value = "orderId") String orderId);

    /**
     * 查询所有订单
     *
     * @return
     * @throws Exception
     */


    List<Order> findOrderAll(@Param(value = "map") Map<String, Object> map) throws Exception;

    /**
     * 插入订单
     *
     * @param map
     * @throws Exception
     */
    void saveOrder(@Param(value = "map") Map<String, Object> map) throws Exception;

    /**
     * 维护快递是否发货标识
     *
     * @param orderId
     */
    @Update("UPDATE orders SET expressOnly = 1 WHERE orderId = #{orderId}")
    void updateExpress(@Param(value = "orderId") String orderId);

    /**
     * 查询订单是否存在
     *
     * @param orderId
     * @return
     */
    @Select("SELECT COUNT(orderId) FROM orders WHERE orderId=#{orderId}")
    int findCount(@Param(value = "orderId") String orderId);


    /**
     * 根据公司ID查询是否有下属订单
     *
     * @param organizationId
     * @return
     */
    @Select("select count(orderId) FROM  orders where addCompany=#{organizationId}")
    int findByOName(@Param(value = "organizationId") int organizationId);


    /**
     * 根据订单ID查询订单
     *
     * @param logSubject
     * @return
     */
    @Select("select * from orders where orderId=#{logSubject}")
    Order findByOrderId(@Param(value = "logSubject") String logSubject);

    /**
     * 查询已审核未发货订单
     *
     * @param map
     * @return
     */

    List<Order> findByAudit(@Param(value = "map") Map<String, Object> map);


    /**
     * 查询未关联合同订单
     *
     * @param map
     * @return
     */
    @Select("<script>" +
            " SELECT retreatCargo,orderId,orderTime,actualMoney,freight,receiverName,receiverMobile,expressOnly,site,addUser,addCompany,audit,sumMoney,contractId,paymentStatus,paymentMethod,returnTime FROM orders WHERE 1=1  and contractId=0 and addCompany=#{map.organizationId} " +
            " <if test='map.startTime!=null'> AND orderTime &gt;= #{map.startTime} </if>" +
            " <if test='map.endTime!=null'> AND orderTime &lt;= #{map.endTime}</if>" +
            " <if test='map.receiverName!=null'> and receiverName like CONCAT('%',#{map.receiverName},'%') </if>" +
            " <if test='map.receiverMobile!=null'> and receiverMobile like CONCAT('%',#{map.receiverMobile},'%') </if>" +
            "<if test='map.orderId!=null'> and orderId like CONCAT('%',#{map.orderId},'%') </if>" +
            " <if test='map.site!=null'> and site like CONCAT('%',#{map.site},'%') </if>" +
            " order By orderTime DESC" +
            " </script>")
    @Results(id = "orderMap", value = {
            @Result(id = true, column = "orderId", property = "orderId"),
            @Result(column = "orderTime", property = "orderTime"),
            @Result(column = "receiverName", property = "receiverName"),
            @Result(column = "receiverMobile", property = "receiverMobile"),
            @Result(column = "paymentStatus", property = "paymentStatus"),
            @Result(column = "paymentMethod", property = "paymentMethod"),
            @Result(column = "returnTime", property = "returnTime"),
            @Result(column = "receiverPostcode", property = "receiverPostcode"),
            @Result(column = "expressOnly", property = "expressOnly"),
            @Result(column = "site", property = "site"),
            @Result(column = "addCompany", property = "addCompany"),
            @Result(column = "sumMoney", property = "sumMoney"),
            @Result(column = "actualMoney", property = "actualMoney"),
            @Result(column = "freight", property = "freight"),
            @Result(column = "orderId", property = "goodsDetail", many = @Many(select = "com.ybau.transaction.mapper.GoodsMapper.findByOrdersId")),
            @Result(column = "addUser", property = "user", one = @One(select = "com.ybau.transaction.mapper.UserMapper.findByUid")),
            @Result(column = "addCompany", property = "organization", one = @One(select = "com.ybau.transaction.mapper.OrganizationMapper.findByOId")),
            @Result(column = "contractId", property = "contract", one = @One(select = "com.ybau.transaction.mapper.ContractMapper.findByContractId"))
    })
    List<Order> findNotContract(@Param(value = "map") Map<String, Object> map);


    /**
     * 根据合同ID查询订单信息（分页，筛选）
     *
     * @param map
     * @return
     */
    @Select("<script>" +
            " SELECT orderId,retreatCargo,orderTime,actualMoney,freight,receiverName,receiverMobile,expressOnly,site,addUser,addCompany,audit,sumMoney,contractId,paymentStatus,paymentMethod,returnTime FROM orders WHERE contractId=#{map.contractId}  " +
            " <if test='map.receiverName!=null'> and receiverName like CONCAT('%',#{map.receiverName},'%') </if>" +
            " <if test='map.receiverMobile!=null'> and receiverMobile like CONCAT('%',#{map.receiverMobile},'%') </if>" +
            " <if test='map.orderId!=null'> and orderId  like CONCAT('%',#{map.orderId},'%') </if>" +
            " <if test='map.site!=null'> and site  like CONCAT('%',#{map.site},'%') </if>" +
            " order By orderTime DESC " +
            " </script>")
    @Results(id = "orderMap1", value = {
            @Result(id = true, column = "orderId", property = "orderId"),
            @Result(column = "orderTime", property = "orderTime"),
            @Result(column = "receiverName", property = "receiverName"),
            @Result(column = "receiverMobile", property = "receiverMobile"),
            @Result(column = "paymentStatus", property = "paymentStatus"),
            @Result(column = "paymentMethod", property = "paymentMethod"),
            @Result(column = "receiverMobile", property = "receiverMobile"),
            @Result(column = "returnTime", property = "returnTime"),
            @Result(column = "actualMoney", property = "actualMoney"),
            @Result(column = "freight", property = "freight"),
            @Result(column = "site", property = "site"),
            @Result(column = "sumMoney", property = "sumMoney"),
            @Result(column = "orderId", property = "goodsDetail", many = @Many(select = "com.ybau.transaction.mapper.GoodsMapper.findByOrdersId")),
    })
    List<Order> findIdByOrder(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询待核销订单
     *
     * @param map
     * @return
     */

    List<Order> findByUnpaid(@Param(value = "map") Map<String, Object> map);

    /**
     * 修改付款状态
     *
     * @param orderId
     */

    void updatePaymentStatus(@Param(value = "orderId") String orderId, @Param(value = "paymentStatus") int paymentStatus, @Param(value = "settlementAmount") Double settlementAmount);

    /**
     * 查询公司下额度下单未结算订单金额总和
     *
     * @param organizationId
     * @return
     */
    List<Money> findBySumMoney(@Param(value = "organizationId") int organizationId);

    /**
     * 查询公司ID借样商品
     *
     * @param organizationId
     * @return
     */

    List<Order> findByOitId(@Param(value = "organizationId") int organizationId);

    /**
     * 根据用户ID查询用户所属订单
     *
     * @param uId
     * @return
     */
    List<Order> findByUserId(@Param(value = "uId") String uId);

    /**
     * 查询直接购买未结算订单
     *
     * @param map
     * @return
     */


    List<Order> findOrder(@Param(value = "map") Map<String, Object> map);

    /**
     * 修改订单审核状态为审核中
     *
     * @param orderId 订单ID
     * @param audit   订单审核信息
     */
    void updateAudit(@Param(value = "orderId") String orderId, @Param(value = "audit") int audit);

    /**
     * 根据ID查询出订单信息
     *
     * @param orderId
     * @return
     */
    Order findOrderId(@Param(value = "orderId") String orderId);

    /**
     * 根据订单ID更改审核状态为订单取消，更改结算状态为 退款中
     *
     * @param orderId
     * @param audit
     * @param paymentStatus
     */
    void updateAuditPayment(@Param(value = "orderId") String orderId, @Param(value = "audit") Integer audit, @Param(value = "paymentStatus") Integer paymentStatus);


    /**
     * 查询已取消订单
     *
     * @param map
     * @return
     */
    List<Order> findCancelOrder(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询本公司订单
     *
     * @param map
     * @return
     */
    List<Order> findCompanyOrder(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询已发货订单
     *
     * @param map
     * @return
     */
    List<Order> findShipments(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询借样未归还订单
     *
     * @param map
     * @return
     */
    List<Order> findBorrow(@Param(value = "map") Map<String, Object> map);

    /**
     * 直接购买 结算或设置紧急发货后 更改发货状态
     *
     * @param orderId
     * @param paymentStatus
     * @param expressOnly
     */
    void updatePaymentByExpress(@Param(value = "orderId") String orderId, @Param(value = "paymentStatus") Integer paymentStatus, @Param(value = "expressOnly") Integer expressOnly, @Param(value = "settlementAmount") Double settlementAmount);

    /**
     * 根据订单ID修改订单信息
     *
     * @param map
     */
    void updateOrder(@Param(value = "map") Map<String, Object> map);

    /**
     * 更改订单退货信息为退货中
     *
     * @param orderId
     * @param retreatCargo
     */
    void updateRetreatCargo(@Param(value = "orderId") String orderId, @Param(value = "retreatCargo") int retreatCargo);

    /**
     * 导入订单
     * @param order
     */
    void addOrder(@Param(value = "orders") LeadOrder order);

    /**
     * 根据开始时间和结束时间导出订单
     * @param startDate
     * @param endDate
     * @return
     */
    List<ExcelOrder> findStartByEnd(@Param(value = "startDate") String startDate,@Param(value = "endDate") String endDate,@Param(value = "proposer") String proposer,@Param(value = "goodsName") String goodsName);

    /**
     * 删除订单信息
     * @param orderId
     */
    void deleteOrderId(@Param(value = "orderId") String orderId);

    /**
     * 根据合同ID修改所有与合同相关联的订单
     * @param orderId
     */
    void updateContract(@Param(value = "orderId") String orderId);

    /**
     * 根据用户Id查询用户所下的订单
     * @param map
     * @param id
     * @return
     */
    List<Order> findOrderByUser(@Param(value = "map") Map<String, Object> map,@Param(value = "id") String id);

    /**
     * 所有订单解除关联
     * @param contractId
     */
    void updateContractId(@Param(value = "contractId") int contractId);

    /**
     * 服务费管理
     * @param orderId
     * @param serveCost
     */
    void updateCost(@Param(value = "orderId") String orderId,@Param(value = "serveCost") double serveCost);

    /**
     * 订单关联发票附件
     * @param orderId
     * @param orderInvoiceId
     */
    void updateInvoice(@Param(value = "orderId") String orderId,@Param(value = "orderInvoiceId") int orderInvoiceId);

    /**
     * 根据用户Id查询未关联合同附件的订单
     * @param map
     * @param id
     * @return
     */
    List<Order> findNotByUser(@Param(value = "map") Map<String, Object> map, @Param("id") String id);

    /**
     * 根据发票ID查询已关联的订单
     * @param map
     * @return
     */
    List<Order> relevanceInvoice(@Param(value = "map") Map<String, Object> map);


    /**
     * 根据发票ID解除与订单关联
     * @param invoiceId
     */
    void updateInvoiceId(@Param(value = "invoiceId") int invoiceId);

    /**
     * 更改退款金额
     * @param orderId
     * @param now
     */
    void updateRefundAmount(@Param(value = "orderId") String orderId, @Param(value = "now") double now);

    /**
     * 查询申请人金额汇总
     * @param map
     * @return
     */
    List<Order> proposerMoney(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询该申请人订单
     * @param map
     * @return
     */
    List<Order> proposerMoneyPaging(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据公司ID查询该公司有效订单
     * @param organizationId
     * @return
     */
    List<Order> findOrganizationId(@Param(value = "organizationId") String organizationId);

    /**
     * 根据时间和公司ID查询该公司这个时间段的订单
     * @param organizationId
     * @param time
     * @param nextTime
     * @return
     */
    List<Order> findTimeOrganizationId(@Param(value = "organizationId") String organizationId, @Param(value = "time") Date time, @Param(value = "nextTime") Date nextTime);

    /**
     * 批量导入退货信息
     * @param order
     */
    void updateRetreat(@Param(value = "order") Map<String, Object> order);
}
