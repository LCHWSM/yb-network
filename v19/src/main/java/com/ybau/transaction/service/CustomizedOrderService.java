package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface CustomizedOrderService {
    /**
     * 用户定制需求
     * @param map
     * @param request
     * @return
     */
    ResponseData saveCo(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 修改定制订单信息
     * @param map
     * @param request
     * @return
     */
    ResponseData updateFlag(Map<String,Object> map,HttpServletRequest request) throws ParseException;

    /**
     * 查询所有用户定制需求
     * @param map
     * @return
     */
    ResponseData findAll(Map<String, Object> map);

    /**
     * 根据用户手机号查询用户定制需求（客户端）
     * @param map
     * @return
     */
    ResponseData findClient(Map<String, Object> map,HttpServletRequest request);

    /**
     * 修改定制订单信息
     * @param map
     * @param request
     * @return
     */
    ResponseData updateOrder(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 查询是否是定制订单跟进人
     * @param coId
     * @param request
     * @return
     */
    ResponseData findUser(int coId, HttpServletRequest request);

    /**
     * 根据用户ID查询用户跟进的订单
     * @param map
     * @param request
     * @return
     */
    ResponseData findByUserId(Map<String, Object> map, HttpServletRequest request);

    /**
     * 根据定制订单ID查询定制订单信息
     * @param coId
     * @return
     */
    ResponseData findById(int coId);
}
