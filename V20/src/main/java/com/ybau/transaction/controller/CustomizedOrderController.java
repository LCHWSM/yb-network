package com.ybau.transaction.controller;

import com.ybau.transaction.service.CustomizedOrderService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * 定制服务WEB层
 */
@RestController
@RequestMapping("/customizedOrder")
public class  CustomizedOrderController {

    @Autowired
    CustomizedOrderService customizedOrderService;


    /**
     * 新增用户定制需求
     *
     * @param map
     * @return
     */
    @PostMapping("/saveCo")
    public ResponseData saveCo(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return customizedOrderService.saveCo(map, request);
    }

    /**
     * 修改定制订单信息
     *
     * @return
     */
    @PostMapping("/updateFlag")
    public ResponseData updateFlag(@RequestBody Map<String,Object> map,HttpServletRequest request) throws ParseException {
        return customizedOrderService.updateFlag(map,request);
    }

    /**
     * 查询所有用户定制需求(管理端)
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String,Object> map){
        return customizedOrderService.findAll(map);
    }

    /**
     * 根据用户手机号查询用户定制需求（客户端）
     * @param map
     * @return
     */
    @PostMapping("/findClient")
    public ResponseData findClient(@RequestBody Map<String,Object> map,HttpServletRequest request){
        return customizedOrderService.findClient(map,request);
    }

    /**
     * 修改定制订单信息
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateOrder")
    public ResponseData updateOrder(@RequestBody Map<String,Object> map,HttpServletRequest request) throws ParseException {
        return customizedOrderService.updateOrder(map,request);
    }

    /**
     * 查询登录用户是否是此订单跟进人
     * @param coId 定制订单ID
     * @param request 获取用户ID
     * @return 返回是否是跟进人
     */
    @GetMapping("/findUser")
    public ResponseData findUser(int coId,HttpServletRequest request){
        return customizedOrderService.findUser(coId,request);
    }

    /**
     * 根据用户ID查询用户跟进的订单
     * @param map
     * @param request
     * @return
     */
    @PostMapping ("/findByUserId")
    public ResponseData findByUserId(@RequestBody Map<String,Object> map,HttpServletRequest request){
        return customizedOrderService.findByUserId(map,request);
    }

    /**
     * 根据定制订单ID查询定制订单信息
     * @param coId
     * @return
     */
    @GetMapping("/findById")
    public ResponseData findById(int coId){
        return customizedOrderService.findById(coId);
    }
}
