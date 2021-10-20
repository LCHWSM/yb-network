package com.ybau.transaction.controller;


import com.ybau.transaction.service.OrderInvoiceService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/orderInvoice")
public class OrderInvoiceController {

    @Autowired
    OrderInvoiceService orderInvoiceService;


    /**
     * 根据订单ID查询发票附件信息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/findOrderId")
    public ResponseData findOrderId(String orderId) {
        return orderInvoiceService.findOrderId(orderId);
    }

    /**
     * 新增合同附件  （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveInvoiceUser")
    public ResponseData saveInvoiceUser(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return orderInvoiceService.saveInvoiceUser(map, request);
    }

    /**
     * 修改发票信息 （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateInvoiceUser")
    public ResponseData updateInvoiceUser(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return orderInvoiceService.updateInvoiceUser(map, request);
    }


    /**
     * 取消关联  (用户)
     *
     * @param orderId
     * @return
     */
    @GetMapping("/cancelReleUser")
    public ResponseData cancelReleUser(String orderId, HttpServletRequest request) {
        return orderInvoiceService.cancelReleUser(orderId, request);
    }

    /**
     * 根据用户ID查询该用户未关联发票附件的订单
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/findNotByUser")
    public ResponseData findNotByUser(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return orderInvoiceService.findNotByUser(map, request);
    }

    /**
     * 根据合同Id查询已关联的订单
     *
     * @param map
     * @return
     */
    @PostMapping("/relevanceInvoice")
    public ResponseData relevanceInvoice(@RequestBody Map<String, Object> map) {
        return orderInvoiceService.relevanceInvoice(map);
    }

    /**
     * 订单关联发票
     *
     * @param map
     * @return
     */
    @PostMapping("/relevanceOrder")
    public ResponseData relevanceOrder(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return orderInvoiceService.relevanceOrder(map, request);
    }


    /**
     * 新增发票附件 （所有订单）
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveInvoice")
    public ResponseData saveInvoice(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return orderInvoiceService.saveInvoice(map, request);
    }

    /**
     * 修改发票信息 （所有订单）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateInvoice")
    public ResponseData updateInvoice(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return orderInvoiceService.updateInvoice(map, request);
    }

    /**
     * 取消关联  (所有订单)
     *
     * @param orderId
     * @return
     */
    @GetMapping("/cancelRelevance")
    public ResponseData cancelRelevance(String orderId) {
        return orderInvoiceService.cancelRelevance(orderId);
    }


    /**
     * 查询所有未关联发票的订单 （所有订单）
     *
     * @param map
     * @return
     */
    @PostMapping("/findNotOrder")
    public ResponseData findNotOrder(@RequestBody Map<String, Object> map ) {
        return orderInvoiceService.findNotOrder(map);
    }

    /**
     * 根据发票ID查看已关联的订单 （所有订单）
     * @param map
     * @return
     */
    @PostMapping("/relevanceInvoiceId")
    public ResponseData relevanceInvoiceId(@RequestBody Map<String, Object> map) {
        return orderInvoiceService.relevanceInvoiceId(map);
    }


    /**
     * 订单关联发票
     *
     * @param map
     * @return
     */
    @PostMapping("/relevanceOrderId")
    public ResponseData relevanceOrderId(@RequestBody Map<String, Object> map ) {
        return orderInvoiceService.relevanceOrderId(map);
    }

    /**
     * 根据发票附件ID删除发票  并解除所有关联   (所有订单)
     * @param invoiceId
     * @return
     */
    @GetMapping("/deleteInvoice")
    public ResponseData deleteInvoice(int invoiceId){
        return orderInvoiceService.deleteInvoice(invoiceId);
    }

    /**
     * 判断该合同是否是该用户上传的  （用户）
     * @param invoiceId
     * @return
     */
    @GetMapping("/ifUserId")
    public ResponseData ifUserId(int invoiceId,HttpServletRequest request){
        return orderInvoiceService.ifUserId(invoiceId,request);
    }


}
