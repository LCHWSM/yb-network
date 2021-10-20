package com.ybau.transaction.mapper;


import com.ybau.transaction.domain.OrderInvoice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface OrderInvoiceMapper {

    /**
     * 根据订单ID查询发票附件信息
     * @param orderId
     * @return
     */
    OrderInvoice findOrderId(@Param(value = "orderId") String orderId);

    /**
     * 插入发票附件  （用户）
     * @param map
     */
    void saveInvoiceUser(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据发票ID查询发票信息
     * @param orderInvoiceId
     * @return
     */
    OrderInvoice findById(@Param(value = "orderInvoiceId") int orderInvoiceId);

    /**
     * 修改操作
     * @param map
     */
    void updateInvoiceUser(@Param(value = "map") Map<String, Object> map);

    /**
     * 删除发票附件
     * @param invoiceId
     */
    void deleteInvoiceId(@Param(value = "invoiceId") int invoiceId);
}
