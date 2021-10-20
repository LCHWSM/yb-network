package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.SupplierOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Repository
public interface SupplierOrderMapper {
    /**
     * 插入供应商订单
     * @param map
     */
    void saveOrder(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询是否已存在记录
     * @param map
     * @return
     */
    int findSORecord(@Param(value = "map") Map<String, Object> map);

    /**
     * 存入订单记录信息
     * @param map
     */
    void saveSORecord(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据用户ID 查询用户所下供应商订单
     * @param map
     * @param id
     * @return
     */
    List<SupplierOrder> findUIdOrder(@Param(value = "map") Map<String, Object> map,@Param(value = "id") String id);

    /**
     * 修改订单信息
     * @param map
     */
    void updateOrderById(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据登录用户的供应商ID查询该供应商的所有订单
     * @param map
     * @param supplierId
     * @return
     */
    List<SupplierOrder> findBySupplier(@Param(value = "map") Map<String, Object> map,@Param(value = "supplierId") int supplierId);

    /**
     * 根据订单ID查询订单相关信息
     * @param supplierOrderId
     * @return
     */
    SupplierOrder findById(@Param(value = "supplierOrderId") String supplierOrderId);

    /**
     * 修改报价
     * @param div
     * @param supplierOrderId
     */
    void updateQuotation(@Param(value = "div") double div,@Param(value = "supplierOrderId") String supplierOrderId,@Param(value = "settlement") int settlement);

    /**
     * 执行修改操作
     * @param supplierOrderId
     * @param flag
     */
    void lockQuotation(@Param(value = "supplierOrderId") String supplierOrderId,@Param(value = "flag") int flag);

    /**
     * 修改供应商意见
     * @param supplierOrderId
     * @param dispose
     */
    void updateDispose(@Param(value = "supplierOrderId") String supplierOrderId,@Param(value = "dispose") int dispose,@Param(value = "disposeLog") String disposeLog);

    /**
     * 根据订单ID修改已结算金额 和结算状态
     * @param supplierOrderId
     * @param moneyDiv
     * @param settlement
     */
    void closeSum(@Param(value = "supplierOrderId") String supplierOrderId,@Param(value = "moneyDiv") double moneyDiv,@Param(value = "settlement") int settlement);

    /**
     * 查询公司内供应商订单
     * @param map
     * @return
     */
    List<SupplierOrder> findByOrganId(@Param(value = "map") Map<String, Object> map);

    /**
     *  查询所有供应商订单
     * @param map
     * @return
     */
    List<SupplierOrder> findAll(@Param(value = "map") Map<String, Object> map);

    /**
     * 订单关联合同
     * @param supplierOrderId
     * @param intValue
     */
    void updateContractId(@Param(value = "supplierOrderId") String supplierOrderId,@Param(value = "supplierContractId") int intValue);

    /**
     * 根据合同id解除与订单的关联
     * @param supplierContractId
     */
    void deleteContractId(@Param(value = "supplierContractId") int supplierContractId);

    /**
     * 修改供应商合同信息  （用户）
     * @param map
     */
    void updateContract(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询供应商订单为关联合同订单  （用户）
     * @param map
     * @param id
     * @return
     */
    List<SupplierOrder> findSROrder(@Param(value = "map") Map<String, Object> map,@Param(value = "id") String id);

    /**
     * 订单关联合同
     * @param orderId
     * @param supplierContractId
     */
    void saveContractId(@Param(value = "orderId") String orderId,@Param(value = "supplierContractId") int supplierContractId);

    /**
     * 解除订单关联
     * @param srOrderId
     */
    void deleteOrderId(@Param(value = "srOrderId") String srOrderId);

    /**
     * 根据合同ID查询相关联的订单
     * @param map
     * @return
     */
    List<SupplierOrder> findContractId(@Param(value = "map") Map<String, Object> map);

    /**
     * 订单关联发票附件
     * @param supplierOrderId
     * @param supplierInvoiceId
     */
    void updateInvoice(@Param(value = "supplierOrderId") String supplierOrderId,@Param(value = "supplierInvoiceId") int supplierInvoiceId);


    /**
     * 根据用户Id查询该用户未关联发票附件的订单
     * @param map
     * @param userId
     * @return
     */
    List<SupplierOrder> findNotOrderUser(@Param(value = "map") Map<String, Object> map,@Param(value = "userId") String userId);

    /**
     * 根据发票id解除所有关联
     * @param supplierInvoiceId
     */
    void deleteInvoice(@Param(value = "supplierInvoiceId") int supplierInvoiceId);

    /**
     * 根据发票Id查询已关联订单
     * @param map
     * @return
     */
    List<SupplierOrder> relevanceInvoiceUser(@Param(value = "map") Map<String, Object> map);
}
