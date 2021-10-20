package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface OrderInvoiceService {
    ResponseData findOrderId(String orderId);

    ResponseData saveInvoiceUser(Map<String, Object> map, HttpServletRequest request);

    ResponseData updateInvoiceUser(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData cancelReleUser(String orderId,HttpServletRequest request);

    ResponseData findNotByUser(Map<String, Object> map, HttpServletRequest request);

    ResponseData relevanceInvoice(Map<String, Object> map);

    ResponseData relevanceOrder(Map<String, Object> map,HttpServletRequest request);

    ResponseData saveInvoice(Map<String, Object> map, HttpServletRequest request);

    ResponseData updateInvoice(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData cancelRelevance(String orderId);

    ResponseData findNotOrder(Map<String, Object> map);

    ResponseData relevanceInvoiceId(Map<String, Object> map);

    ResponseData relevanceOrderId(Map<String, Object> map);

    ResponseData deleteInvoice(int invoiceId);

    ResponseData ifUserId(int invoiceId, HttpServletRequest request);

}
