package com.ybau.transaction.controller;


import com.ybau.transaction.service.DataStatisticsService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/dataStatistics")
public class DataStatisticsController {
    @Autowired
    DataStatisticsService dataStatisticsService;


    /**
     * 按公司查询所有下单方式数据统计
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String,Object> map){
        return dataStatisticsService.findAll(map);
    }

    /**
     * 根据公司ID查询公司按时间统计信息
     * @param map
     * @return
     */
    @PostMapping("/findTime")
    public ResponseData findTime(@RequestBody Map<String,Object> map){
        return dataStatisticsService.findTime(map);
    }

    /**
     * 根据公司ID 和时间查询订单具体信息
     * @param map
     * @return
     */
    @PostMapping("/findByOrder")
    public ResponseData findByOrder(@RequestBody Map<String,Object> map) throws ParseException {
        return dataStatisticsService.findByOrder(map);
    }
}
