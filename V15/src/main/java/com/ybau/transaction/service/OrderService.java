package com.ybau.transaction.service;

import com.ybau.transaction.domain.LeadOrder;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> findOrderDate(Map<String, Object> params) throws Exception;

    ResponseData findOrderAll(Map<String, Object> map, HttpServletRequest request) throws Exception;

    ResponseData findOrderByDate(Map<String, Object> map) throws Exception;

    void updateExpress(String orderId);

    ResponseData saveOrder(Map<String, Object> map, HttpServletRequest request) throws Exception;



    Order findByOrderId(String logSubject);

    ResponseData findByAudit(Map<String, Object> map,HttpServletRequest request);

    ResponseData findByOrder(Map<String, Object> map);

    ResponseData findByUnpaid(Map<String, Object> map);


    ResponseData findSample(int pageSize, int pageNum, HttpServletRequest request) throws ParseException;

    ResponseData findOrder(Map<String, Object> map,HttpServletRequest request);

    ResponseData findInventory(Map<String, Object> map, HttpServletRequest request);


    ResponseData cancelOrder(String orderId,HttpServletRequest request) throws ParseException;

    ResponseData findCancelOrder(Map<String, Object> map,HttpServletRequest request);

    ResponseData findCompanyOrder(Map<String, Object> map, HttpServletRequest request);

    ResponseData findShipments(Map<String, Object> map,HttpServletRequest request);

    ResponseData findBorrow(Map<String, Object> map,HttpServletRequest request);

    ResponseData refund(List<String> orderId, HttpServletRequest request) throws ParseException;

    ResponseData updateOrder(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData findOrderId(String orderId,HttpServletRequest request);

    ResponseData leadOrder(List<LeadOrder> orders,String uId,String substring);

    ResponseData deleteOrder(Map<String,Object> map,HttpServletRequest request) throws ParseException;

    ResponseData deleteLeadOrder(String orderIds,String logId);

    ResponseData updateCost(String orderId, int serveCost,HttpServletRequest request);

}

