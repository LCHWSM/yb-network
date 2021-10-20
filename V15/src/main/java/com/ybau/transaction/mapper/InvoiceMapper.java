package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Invoice;
import com.ybau.transaction.domain.LeadInvoice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sun.awt.SunHints;

import java.util.List;
import java.util.Map;

@Repository
public interface InvoiceMapper {
    /**
     * 插入发票信息
     * @param invoice
     * @param orderId
     */
    void saveInvoice(@Param(value = "invoice") LeadInvoice invoice, @Param(value = "orderId") String orderId);

    /**
     * 存入发票信息记录
     * @param invoice
     * @param id
     */
    void saveInvoiceRecord(@Param(value = "invoice") LeadInvoice invoice,@Param(value = "id") String id);

    /**
     * 查询已插入发票记录地址
     * @param map
     * @param userId
     * @return
     */
    List<Invoice> findInvoice(@Param(value = "map") Map<String, Object> map,@Param(value = "userId") String userId);

    /**
     * 根据用户iD和登录用户iD查询是否是登录用户插入的
     * @param invoiceRecordId
     * @param id
     * @return
     */
    int findIdByUserId(@Param(value = "invoiceRecordId") int invoiceRecordId,@Param(value = "id") String id);


    /**
     * 根据发票记录ID删除信息
     * @param invoiceRecordId
     */
    void deleteInvoice(@Param(value = "invoiceRecordId") int invoiceRecordId);

    /**
     * 修改发票记录信息
     * @param map
     */
    void updateInvoice(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询发票记录是否已经插入
     * @param invoice
     * @return
     */
    int findCount(@Param(value = "invoice") LeadInvoice invoice,@Param(value = "id") String id);

    /**
     * 订单相关联发票信息修改
     * @param map
     */
    void updateByOrderId(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据订单ID查询是否有发票信息
     * @param orderId
     * @return
     */
    int findByOrderId(@Param(value = "orderId") String orderId);

    /**
     * 根据发票名字查询信息
     * @param unitName
     * @return
     */
    List<Invoice> findByUnitName(@Param(value = "unitName") String unitName,@Param(value = "flag") Integer flag,@Param(value = "userId") String userId);

    /**
     * 根据订单ID删除发票信息
     * @param orderId
     */
    void deleteByOrderId(@Param(value = "orderId") String orderId);

    /**
     * map参数新增发票信息
     * @param map
     */
    void saveInvoiceMap(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据订单ID查询发票信息
     * @param supplierOrderId
     * @return
     */
    Invoice findById(@Param(value = "supplierOrderId") String supplierOrderId);

    void insertInvoice(Invoice invoice, String orderId);

    int findByCount(Invoice invoice, String id);

    void saveInvoiceByRecord(Invoice invoice, String id);
}
