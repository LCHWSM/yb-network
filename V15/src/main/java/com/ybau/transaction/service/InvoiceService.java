package com.ybau.transaction.service;

import com.ybau.transaction.domain.Invoice;
import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface InvoiceService {
    /**
     * 插入发票消息
     * @param invoice
     * @param orderId
     * @return
     */
    Map<String, Object> saveInvoice(Invoice invoice, String orderId,String id);

    /**
     * 查询已插入的发票记录
     * @param map
     * @return
     */
    ResponseData findInvoice(Map<String, Object> map, HttpServletRequest request);

    /**
     *
     * @param invoicerecordId
     * @return
     */
    ResponseData deleteInvoice(Integer invoicerecordId,HttpServletRequest request);

    /**
     * 修改发票记录信息
     * @param map
     * @return
     */
    ResponseData updateInvoice(Map<String, Object> map);

    /**
     * 根据发票名字查询发票信息
     * @param unitName
     * @return
     */
    ResponseData findByUnitName(String unitName,Integer flag,HttpServletRequest request);
}
