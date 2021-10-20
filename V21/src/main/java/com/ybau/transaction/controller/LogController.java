package com.ybau.transaction.controller;


import com.ybau.transaction.service.LogService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    LogService logService;

    /**
     * 查询所有日志
     *
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String, Object> map) {
        return logService.findAll(map);
    }


    /**
     * 查询订单结算记录
     *
     * @param map
     * @return
     */
    @PostMapping("/findOrderByLog")
    public ResponseData findOrderByLog(@RequestBody Map<String, Object> map) {
        return logService.findOrderByLog(map);
    }

    /**
     * 查询定制订单操作记录
     *
     * @param map
     * @return
     */
    @PostMapping("/findCoLog")
    public ResponseData findCoLog(@RequestBody Map<String, Object> map) {
        return logService.findCoLog(map);
    }

    /**
     * 新增定制订单操作记录
     *
     * @param map
     * @return
     */
    @PostMapping("/saveCoLog")
    public ResponseData saveCoLog(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return logService.saveCoLog(map, request);
    }

    /**
     * 查询供应商订单相关操作日志
     *
     * @param map
     * @return
     */
    @PostMapping("/findSupplierLog")
    public ResponseData findSupplierLog(@RequestBody Map<String, Object> map) {
        return logService.findSupplierLog(map);
    }

    /**
     * @param startTime 开始时间 （筛选条件）
     * @param endTime   结束时间（筛选条件）
     * @return
     */
    @GetMapping("/findLeadOrder")
    public ResponseData findLeadOrder(String startTime, String endTime, HttpServletRequest request, int pageSize, int pageNum) {
        return logService.findLeadOrder(startTime, endTime, request, pageSize, pageNum);
    }

    /**
     * 查询所有导入订单记录
     *
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/findLeadOrderAll")
    public ResponseData findLeadOrderAll(String startTime, String endTime, int pageSize, int pageNum) {
        return logService.findLeadOrderAll(startTime, endTime, pageNum, pageSize);
    }

}
