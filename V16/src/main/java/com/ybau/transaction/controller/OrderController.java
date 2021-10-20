package com.ybau.transaction.controller;


import com.ybau.transaction.domain.Order;
import com.ybau.transaction.service.OrderService;
import com.ybau.transaction.util.ResponseData;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private Environment environment;


    ResponseData result = new ResponseData();


    /**
     * 查询时间段
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findOrderDate", method = RequestMethod.POST)
    public ResponseData findOrderDate(@RequestBody Map<String, Object> params) throws Exception {
        String authCode = (String) params.get("authCode");
        if (authCode.equals(environment.getProperty("order.authCode"))) {
            List<Order> orders = orderService.findOrderDate(params);
            result.setCode(200);
            result.setData(orders);
            return result;
        }
        result.setCode(400);
        result.setData("authCode有误");
        return result;
    }

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/findAll")
    public ResponseData findOrderAll(@RequestBody Map<String, Object> map, HttpServletRequest request) throws Exception {
        return orderService.findOrderAll(map, request);

    }

    /**
     * 根据时间调用接口查询
     *
     * @param map
     * @return
     */
    @RequestMapping("/findOrderByDate")
    public ResponseData findOrderByDate(@RequestBody Map<String, Object> map) throws Exception {
        return orderService.findOrderByDate(map);
    }

    /**
     * 下单
     *
     * @param map
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/saveOrder")
    public ResponseData saveOrder(@RequestBody Map<String, Object> map, HttpServletRequest request) throws Exception {
        return orderService.saveOrder(map,request);
    }


    /**
     * 查询已审核，未发货订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findByAudit")
    public ResponseData findByAudit(@RequestBody Map<String, Object> map,HttpServletRequest request) {
        return orderService.findByAudit(map,request);
    }

    /**
     * 查询合同关联订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findByOrder")
    public ResponseData findByOrder(@RequestBody Map<String, Object> map) {
        return orderService.findByOrder(map);
    }

    /**
     * 查询待核算订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findByUnpaid")
    public ResponseData findByUnpaid(@RequestBody Map<String, Object> map) {
        return orderService.findByUnpaid(map);
    }


    /**
     * 查询用户借样待归还订单明细
     *
     * @param request
     * @return
     */
    @GetMapping("/findSample")
    public ResponseData findSample(int pageSize, int pageNum, HttpServletRequest request) throws ParseException {
        return orderService.findSample(pageSize, pageNum, request);
    }


    /**
     * 查询直接付款未结算订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findOrder")
    public ResponseData findOrder(@RequestBody Map<String, Object> map,HttpServletRequest request) {
        return orderService.findOrder(map,request);
    }

    /**
     * 校验额度库存是否充足
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/findInventory")
    public ResponseData findInventory(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return orderService.findInventory(map, request);
    }

    /**
     * 取消订单操作
     *
     * @param orderId
     * @return
     */
    @GetMapping("/cancelOrder")
    public ResponseData cancelOrder(String orderId, HttpServletRequest request) throws ParseException {
        return orderService.cancelOrder(orderId, request);
    }


    /**
     * 查询已取消的订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findCancelOrder")
    public ResponseData findCancelOrder(@RequestBody Map<String, Object> map,HttpServletRequest request) {
        return orderService.findCancelOrder(map,request);
    }

    /**
     * 查询本公司订单
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/findCompanyOrder")
    public ResponseData findCompanyOrder(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return orderService.findCompanyOrder(map, request);
    }


    /**
     * 查询已发货订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findShipments")
    public ResponseData findShipments(@RequestBody Map<String, Object> map,HttpServletRequest request) {
        return orderService.findShipments(map,request);
    }

    /**
     * 查询借样待归还订单
     *
     * @param map 查询订单筛选条件
     * @return 查询出的借样待归还订单
     */
    @PostMapping("/findBorrow")
    public ResponseData findBorrow(@RequestBody Map<String, Object> map,HttpServletRequest request) {
        return orderService.findBorrow(map,request);
    }

    /**
     * 订单退款操作
     *
     * @param orderId 订单ID
     * @param request 获取操作人ID
     * @return 成功或失败
     */
    @PostMapping("/refund")
    public ResponseData refund(@RequestBody List<String> orderId, HttpServletRequest request) throws ParseException {
        return orderService.refund(orderId, request);
    }

    /**
     * 修改订单信息
     *
     * @param map     需要修改的订单信息
     * @param request 修改人的用户ID
     * @return 是否修改成功
     */
    @PostMapping("/updateOrder")
    public ResponseData updateOrder(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return orderService.updateOrder(map, request);
    }


    /**
     * 根据订单ID查询订单信息
     *
     * @param orderId 订单ID
     * @return 订单信息
     */
    @GetMapping("/findByOrderId")
    public ResponseData findByOrderId(String orderId,HttpServletRequest request) {
        return orderService.findOrderId(orderId,request);
    }

    /**
     * 删除订单
     *
     * @param
     * @return
     */
    @PostMapping("/deleteOrder")
    public ResponseData deleteOrder(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return orderService.deleteOrder(map, request);
    }

    /**
     * 删除导入的历史数据
     * @param orderIds
     * @return
     */
    @GetMapping ("/deleteLeadOrder")
    public ResponseData deleteLeadOrder( String orderIds,String logId) {
        return orderService.deleteLeadOrder(orderIds,logId);
    }


    /**
     * 服务费管理
     * @param orderId
     * @param serveCost
     * @return
     */
    @GetMapping("/updateCost")
    public ResponseData updateCost(String orderId,int serveCost,HttpServletRequest request){
        return orderService.updateCost(orderId,serveCost,request);
    }


}