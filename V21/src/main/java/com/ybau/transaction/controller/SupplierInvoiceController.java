package com.ybau.transaction.controller;


import com.ybau.transaction.service.SupplierInvoiceService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;


@RestController
@RequestMapping("/supplierInvoice")
public class SupplierInvoiceController {

    @Autowired
    SupplierInvoiceService supplierInvoiceService;

    /**
     * 根据订单ID查询发票附件
     *
     * @param supplierOrderId
     * @return
     */
    @GetMapping("/findOrderId")
    public ResponseData findOrderId(String supplierOrderId) {
        return supplierInvoiceService.findOrderId(supplierOrderId);
    }

    /**
     * 插入发票附件信息 （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveInvoiceUser")
    public ResponseData saveInvoiceUser(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierInvoiceService.saveInvoiceUser(map, request);
    }

    /**
     * 修改供应商发票信息（用户）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateInvoiceUser")
    public ResponseData updateInvoiceUser(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierInvoiceService.updateInvoiceUser(map, request);
    }

    /**
     * 取消发票与订单 关联 （用户）
     *
     * @param supplierOrderId
     * @return
     */
    @GetMapping("/cancelReleUser")
    public ResponseData cancelReleUser(String supplierOrderId, HttpServletRequest request) {
        return supplierInvoiceService.cancelReleUser(supplierOrderId, request);
    }

    /**
     * 查询所有未关联发票附件的订单 （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/findNotOrderUser")
    public ResponseData findNotOrderUser(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return supplierInvoiceService.findNotOrderUser(map, request);
    }

    /**
     * 根据发票ID查询已关联的订单信息  （用户）
     *
     * @param map
     * @return
     */
    @PostMapping("/relevanceInvoiceUser")
    public ResponseData relevanceInvoiceUser(@RequestBody Map<String, Object> map) {
        return supplierInvoiceService.relevanceInvoiceUser(map);
    }

    /**
     * 订单关联发票附件 （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/relevanceOrderUser")
    public ResponseData relevanceOrderUser(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return supplierInvoiceService.relevanceOrderUser(map, request);
    }

    /**
     * 新增发票附件 （所有订单）
     *
     * @return
     */
    @PostMapping("/saveInvoice")
    public ResponseData saveInvoice(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierInvoiceService.saveInvoice(map, request);
    }

    /**
     * 修改发票附件信息 （所有订单）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateInvoice")
    public ResponseData updateInvoice(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierInvoiceService.updateInvoice(map, request);
    }

    /**
     * 取消发票附件与订单关联
     *
     * @param supplierOrderId
     * @return
     */
    @GetMapping("/cancelRelevance")
    public ResponseData cancelRelevance(String supplierOrderId) {
        return supplierInvoiceService.cancelRelevance(supplierOrderId);
    }

    /**
     * 查询所有未关联发票的订单 (所有订单)
     *
     * @param map
     * @return
     */
    @PostMapping("/findNotOrder")
    public ResponseData findNotOrder(@RequestBody Map<String, Object> map) {
        return supplierInvoiceService.findNotOrder(map);
    }

    /**
     * 根据发票ID查看已关联的订单 （所有订单）
     *
     * @param map
     * @return
     */
    @PostMapping("/relevanceInvoiceId")
    public ResponseData relevanceInvoiceId(@RequestBody Map<String, Object> map) {
        return supplierInvoiceService.relevanceInvoiceId(map);
    }

    /**
     * 订单关联发票
     * @param map
     * @return
     */
    @PostMapping("/relevanceOrderId")
    public ResponseData relevanceOrderId(@RequestBody Map<String,Object> map){
        return supplierInvoiceService.relevanceOrderId(map);
    }

    /**
     * 删除发票附件信息
     * @param supplierInvoiceId
     * @return
     */
    @GetMapping("/deleteInvoice")
    public ResponseData deleteInvoice(int supplierInvoiceId){
        return supplierInvoiceService.deleteInvoice(supplierInvoiceId);
    }

    /**
     * 判断该发票附件是不是登录用户上传
     * @param supplierInvoiceId
     * @return
     */
    @GetMapping("/ifUserId")
    public ResponseData ifUserId(int supplierInvoiceId,HttpServletRequest request){
        return supplierInvoiceService.ifUserId(supplierInvoiceId,request);
    }

}
