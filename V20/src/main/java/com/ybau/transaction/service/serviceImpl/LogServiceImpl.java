package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.*;
import com.ybau.transaction.mapper.ClearingLogMapper;
import com.ybau.transaction.mapper.CustomizedOrderMapper;
import com.ybau.transaction.mapper.LogMapper;
import com.ybau.transaction.service.LogService;
import com.ybau.transaction.service.OrderService;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class LogServiceImpl implements LogService {


    @Autowired
    LogMapper logMapper;

    @Autowired
    OrderService orderService;

    @Autowired
    ClearingLogMapper clearingLogMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomizedOrderMapper customizedOrderMapper;


    /**
     * 新增库存操作日志
     *
     * @param map
     */
    @Override
    public void saveProductLog(Map<String, Object> map) {
        logMapper.saveProductLog(map);
    }

    /**
     * 查询所有日志
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        int pageSize = (int) map.get("pageSize");
        int pageNum = (int) map.get("pageNum");
        PageHelper.startPage(pageNum, pageSize);
        List<Log> logs = logMapper.findAll(map);
        String flag = (String) map.get("flag");
        if (flag != null && flag.equals("0")) {
            for (int i = 0; i < logs.size(); i++) {
                String logSubject = logs.get(i).getLogSubject();
                Order order = orderService.findByOrderId(logSubject);
                logs.get(i).setOrder(order);
                break;
            }
        }
        PageInfo pageInfo = new PageInfo(logs);
        return new ResponseData(200, "查询成功", pageInfo);
    }


    /**
     * 查询订单结算记录
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findOrderByLog(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<ClearingLog> clearingLogs = clearingLogMapper.findOrderByLog(map);
        PageInfo pageInfo = new PageInfo(clearingLogs);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 查询定制订单操作记录
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findCoLog(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<CoLog> coLogs = logMapper.findCoLog(map);
        PageInfo pageInfo = new PageInfo(coLogs);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 新增定制订单操作记录
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData saveCoLog(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        int count = customizedOrderMapper.findByUser((int) map.get("coId"), id);//根据订单ID 和用户ID查询是否是此订单的跟进人
        if (count < 1) {
            return new ResponseData(400, "你不是此订单的跟进人，无法进行操作", null);
        }
        String coLogTime = (String) map.get("coLogTime");
        if (coLogTime == null || coLogTime == "") {
            return new ResponseData(400, "处理时间不可为空", null);
        }
        if (map.get("coLogName").toString() == null || map.get("coLogName").toString() == "") {
            return new ResponseData(400, "处理情况不可为空", null);
        }
        map.put("updateUser", id);
        map.put("updateTime", df.parse(df.format(new Date())));
        map.put("coClassify", 3);
        logMapper.saveCoLog(map);
        return new ResponseData(200, "新增成功", null);
    }

    /**
     * 查询供应商订单相关操作日志
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findSupplierLog(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrderLog> supplierOrderLogs = logMapper.findSupplierLog(map);
        PageInfo pageInfo = new PageInfo(supplierOrderLogs);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * @param startTime 开始时间 （筛选条件）
     * @param endTime   结束时间（筛选条件）
     * @return
     */
    @Override
    public ResponseData findLeadOrder(String startTime, String endTime, HttpServletRequest request, int pageSize, int pageNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,Object>> logs = logMapper.findLeadOrder(startTime, endTime, id);
        PageInfo pageInfo = new PageInfo(logs);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 查询所有导入记录
     *
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseData findLeadOrderAll(String startTime, String endTime, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String,Object>> logs = logMapper.findLeadOrder(startTime, endTime, null);
        PageInfo pageInfo = new PageInfo(logs);
        return new ResponseData(200, "查询成功", pageInfo);
    }
}
