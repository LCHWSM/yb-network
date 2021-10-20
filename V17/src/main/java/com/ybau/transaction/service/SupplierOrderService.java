package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface SupplierOrderService {
    /**
     * 新增
     * @param map
     * @param request
     * @return
     */
    ResponseData saveOrder(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 查询用户向供应商下的订单
     * @param map
     * @param request
     * @return
     */
    ResponseData findUIdOrder(Map<String, Object> map, HttpServletRequest request);

    /**
     * 修改供应商订单信息
     * @param map
     * @return
     */
    ResponseData updateOrderById( Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 根据登录用户的供应商ID查询该供应商的所有订单
     * @param map
     * @param request
     * @return
     */
    ResponseData findBySupplier(Map<String, Object> map, HttpServletRequest request);

    /**
     * 修改报价
     * @param map
     * @param request
     * @return
     */
    ResponseData updateQuotation(Map<String, Object> map, HttpServletRequest request)throws ParseException;

    /**
     * 报价锁定状态修改
     * @param supplierOrderId
     * @param flag
     * @return
     */
    ResponseData lockQuotation(String supplierOrderId, int flag,HttpServletRequest request) throws ParseException;

    /**
     * 修改供应商反馈
     * @param supplierOrderId
     * @param dispose
     * @param request
     * @return
     */
    ResponseData updateDispose(String supplierOrderId, int dispose,String disposeStr, HttpServletRequest request,String disposeLog) throws ParseException;

    /**
     * 结算金额
     * @param map
     * @param request
     * @return
     */
    ResponseData closeSum(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 修改已结算金额
     * @param map
     * @param request
     * @return
     */
    ResponseData updateAmount(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 查询公司内供应商订单
     * @param map
     * @return
     */
    ResponseData findByOrganId(Map<String, Object> map,HttpServletRequest request);

    /**
     * 公司结算订单
     * @param map
     * @param request
     * @return
     */
    ResponseData companyCloseSum(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 查询所有订单
     * @param map
     * @return
     */
    ResponseData findAll(Map<String, Object> map);
}
