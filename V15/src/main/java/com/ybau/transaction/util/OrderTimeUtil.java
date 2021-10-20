package com.ybau.transaction.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 定时任务工具类
 */
@Component
@Slf4j
public class OrderTimeUtil {

    Map<String, Object> map = new HashMap<>();

    int x = 0;

    @Autowired
    private PullOrder pullOrder;

    /**
     * 定时任务  每间隔一个小时执行一次
     */
   //@Scheduled(cron = "0 0 0-23 * * ? ")
    private void cancelTimeOutOrder() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = "2015-02-04 17:53:55";
        x++;
        if (x == 1) {
            //首次运行拉取15年-当前时间订单
            try {
                map.put("startTime", time);
                map.put("endTime", df.format(new Date()));
                pullOrder.findOrderByDate(map);
            } catch (Exception e) {
                log.info("定时任务OrderTimeOutCancelTask拉取失败，失败原因: {}", e);
            }
        } else {
            try {
                //后每次运行拉取上一次拉取时间-当前时间订单
                map.put("startTime", map.get("endTime"));
                map.put("endTime", df.format(new Date()));
                pullOrder.findOrderByDate(map);
                log.info("定时任务OrderTimeOutCancelTask拉取了{}次", x);
            } catch (Exception e) {
                log.info("定时任务OrderTimeOutCancelTask拉取失败，失败原因: {}", e);
            }
        }
    }
}

