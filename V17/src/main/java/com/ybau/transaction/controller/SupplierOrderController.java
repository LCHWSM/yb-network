package com.ybau.transaction.controller;


import com.ybau.transaction.service.SupplierOrderService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/supplierOrder")
public class SupplierOrderController {

    @Autowired
    SupplierOrderService supplierOrderService;

    /**
     * 新增供应商订单
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveOrder")
    public ResponseData saveOrder(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierOrderService.saveOrder(map, request);
    }

    /**
     * 查询用户向供应商下单订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findUIdOrder")
    public ResponseData findUIdOrder(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return supplierOrderService.findUIdOrder(map, request);
    }


    /**
     * 修改供应商订单信息
     *
     * @param map
     * @return
     */
    @PostMapping("/updateOrderById")
    public ResponseData updateOrderById(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierOrderService.updateOrderById(map, request);
    }

    /**
     * 根据登录用户的供应商ID查看该供应商下所有订单
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/findBySupplier")
    public ResponseData findBySupplier(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return supplierOrderService.findBySupplier(map, request);
    }

    /**
     * 修改报价
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateQuotation")
    public ResponseData updateQuotation(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierOrderService.updateQuotation(map, request);
    }

    /**
     * 报价锁定状态修改
     *
     * @param supplierOrderId
     * @param flag
     * @return
     */
    @GetMapping("/lockQuotation")
    public ResponseData lockQuotation(String supplierOrderId, int flag, HttpServletRequest request) throws ParseException {
        return supplierOrderService.lockQuotation(supplierOrderId, flag, request);
    }

    /**
     * 修改供应商反馈
     *
     * @param supplierOrderId
     * @param dispose
     * @param request
     * @return
     */
    @GetMapping("/updateDispose")
    public ResponseData updateDispose(String supplierOrderId, int dispose, String disposeStr, HttpServletRequest request,String disposeLog) throws ParseException {
        return supplierOrderService.updateDispose(supplierOrderId, dispose, disposeStr, request,disposeLog);
    }

    /**
     * 结算金额
     *
     * @param map
     * @return
     */
    @PostMapping("/closeSum")
    public ResponseData closeSum(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierOrderService.closeSum(map, request);
    }

    /**
     * 修改已结算金额
     *
     * @param map
     * @return
     */
    @PostMapping("/updateAmount")
    public ResponseData updateAmount(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierOrderService.updateAmount(map, request);
    }

    /**
     * 查询公司内供应商订单
     * @param map
     * @return
     */
    @PostMapping("/findByOrganId")
    public ResponseData findByOrganId(@RequestBody Map<String,Object> map,HttpServletRequest request){
        return supplierOrderService.findByOrganId(map,request);
    }

    /**
     * 结算公司订单
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/companyCloseSum")
    public ResponseData companyCloseSum(@RequestBody Map<String,Object> map,HttpServletRequest request) throws ParseException {
        return supplierOrderService.companyCloseSum(map,request);
    }

    /**
     * 查询所有订单
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String,Object> map){
        return supplierOrderService.findAll(map);
    }
}
