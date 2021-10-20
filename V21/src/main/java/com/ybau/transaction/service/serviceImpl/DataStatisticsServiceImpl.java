package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.domain.Organization;
import com.ybau.transaction.mapper.DataStatisticsMapper;
import com.ybau.transaction.service.DataStatisticsService;
import com.ybau.transaction.util.BigDecimalUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@Transactional
public class DataStatisticsServiceImpl implements DataStatisticsService {

    @Autowired
    DataStatisticsMapper dataStatisticsMapper;


    /**
     * 按公司查询所有下单方式数据
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Organization> organizations = dataStatisticsMapper.findAll(map);
        PageInfo pageInfo = new PageInfo(organizations);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据公司查询
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findTime(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Map<String, Object>> maps = dataStatisticsMapper.findTime(map);
        PageInfo pageInfo = new PageInfo(maps);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 据公司ID 和时间查询订单具体信息
     * @param map
     * @return
     */
    @Override
    public ResponseData findByOrder(Map<String, Object> map) throws ParseException {
        PageHelper.startPage((int)map.get("pageNum"),(int)map.get("pageSize"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String startTime = (String) map.get("startTime");//取出选择时间
        String nextTime = BigDecimalUtil.subMonth(startTime);//当前月份+一个月
        List<Order> orders=dataStatisticsMapper.findByOrder(df.parse(startTime),df.parse(nextTime),(int) map.get("organizationId"));
        PageInfo pageInfo=new PageInfo(orders);
        return new ResponseData(200,"查询成功",pageInfo);
    }
}
