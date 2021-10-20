package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface SupplierInvoiceService {
    ResponseData findOrderId(String supplierOrderId);

    ResponseData saveInvoiceUser(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData updateInvoiceUser(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData cancelReleUser(String supplierOrderId,HttpServletRequest request);

    ResponseData findNotOrderUser(Map<String, Object> map, HttpServletRequest request);


    ResponseData relevanceInvoiceUser(Map<String, Object> map);

    ResponseData relevanceOrderUser(Map<String, Object> map, HttpServletRequest request);

    ResponseData saveInvoice(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData updateInvoice(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData cancelRelevance(String supplierOrderId);

    ResponseData findNotOrder(Map<String, Object> map);

    ResponseData relevanceInvoiceId(Map<String, Object> map);

    ResponseData relevanceOrderId(Map<String, Object> map);

    ResponseData deleteInvoice(int supplierInvoiceId);

    ResponseData ifUserId(int supplierInvoiceId,HttpServletRequest request);

}
