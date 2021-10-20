package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.OrderInvoice;
import com.ybau.transaction.domain.SupplierInvoice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Repository
public interface SupplierInvoiceMapper {
    /**
     * 根据订单ID查询发票附件信息
     * @param supplierOrderId
     * @return
     */
    OrderInvoice findOrderId(@Param(value = "supplierOrderId") String supplierOrderId);

    /**
     * 新增发票附件
     * @param map
     */
    void saveInvoiceUser(Map<String, Object> map);

    /**
     * 根据发票附件ID查询发票信息
     * @param supplierInvoiceId
     * @return
     */
    SupplierInvoice findSupplierInvoice(@Param(value = "supplierInvoiceId") int supplierInvoiceId);

    /**
     * 修改供应商发票操作
     * @param map
     */
    void updateInvoiceUser(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据发票附件ID删除发票附件
     * @param supplierInvoiceId
     */
    void deleteInvoice(@Param(value = "supplierInvoiceId") int supplierInvoiceId);
}
