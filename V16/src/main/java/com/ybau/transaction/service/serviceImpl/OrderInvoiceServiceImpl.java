package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.domain.OrderInvoice;
import com.ybau.transaction.mapper.OrderInvoiceMapper;
import com.ybau.transaction.mapper.OrderMapper;
import com.ybau.transaction.service.OrderInvoiceService;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class OrderInvoiceServiceImpl implements OrderInvoiceService {

    @Autowired
    OrderInvoiceMapper orderInvoiceMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    OrderMapper orderMapper;

    @Value("${ftp.visitUrl}")
    String prefixUrl;

    @Value("${ftp.bastPath}")
    String bastPath;

    @Autowired
    JsonUtil jsonUtil;

    /**
     * 根据订单ID查询发票附件信息
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseData findOrderId(String orderId) {
        OrderInvoice orderInvoice = orderInvoiceMapper.findOrderId(orderId);
        if (orderInvoice != null) {
            boolean json = JsonUtil.isJson(orderInvoice.getOrderInvoiceSite());
            //判断类型是否为json
            if (json) {
                List<Map> list = JsonUtil.jsonToList(orderInvoice.getOrderInvoiceSite(), Map.class);
                for (Map<String, Object> fileMap : list) {
                    String path = fileMap.get("path").toString().replace("[]", prefixUrl + bastPath + "/");
                    fileMap.put("path", path);
                }
                orderInvoice.setOrderInvoiceSite(JsonUtil.listToJson(list));
            }
        }
        return new ResponseData(200, "查询成功", orderInvoice);
    }

    /**
     * 新增合同附件  （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveInvoiceUser(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        Order order = orderMapper.findOrderId((String) map.get("orderId"));
        if (!order.getAddUser().equals(id)) {
            return new ResponseData(400, "不可关联他人订单", null);
        }
        map.put("orderInvoiceSite", JsonUtil.listToJson(jsonUtil.replaceUrl((List<Map<String, Object>>) map.get("flies"))));
        map.put("addUser", id);//存入操作人
        try {
            map.put("addTime", df.parse(df.format(new Date())));
        } catch (Exception e) {
            log.error("时间转换异常 {}", e);
        }
        try {
            orderInvoiceMapper.saveInvoiceUser(map);//插入发票附件
            BigInteger orderInvoiceId = (BigInteger) map.get("orderInvoiceId");
            orderMapper.updateInvoice((String) map.get("orderId"), orderInvoiceId.intValue());//订单关联发票附件
            return new ResponseData(200, "新增成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "发票名字和编号已存在", null);
        }
    }

    /**
     * 修改发票信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateInvoiceUser(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        OrderInvoice orderInvoice = orderInvoiceMapper.findById((int) map.get("orderInvoiceId"));
        if (orderInvoice.getUserId()==null){
            return new ResponseData(400,"该发票信息已被删除",null);
        }
        if (!orderInvoice.getUserId().equals(id)) {
            return new ResponseData(400, "不可修改他人发票信息", null);
        }
        map.put("orderInvoiceSite", JsonUtil.listToJson(jsonUtil.replaceUrl((List<Map<String, Object>>) map.get("flies"))));
        map.put("updateUser", id);
        map.put("updateTime", df.parse(df.format(new Date())));
        try {
            orderInvoiceMapper.updateInvoiceUser(map);//执行修改操作
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "发票名称编号已存在，修改失败", null);
        }
    }

    /**
     * 取消关联订单（用户）
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseData cancelReleUser(String orderId, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        Order order = orderMapper.findOrderId(orderId);
        if (!order.getAddUser().equals(id)) {
            return new ResponseData(400, "不可操作他人订单", null);
        }
        orderMapper.updateInvoice(orderId, 0);//解除关联
        return new ResponseData(200, "取消关联成功", null);
    }

    /**
     * 根据用户ID查询未关联发票附件的订单
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findNotByUser(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findNotByUser(map, id);
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据发票ID查询已关联的订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceInvoice(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.relevanceInvoice(map);
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 订单关联发票
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceOrder(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        List<String> orders = (List<String>) map.get("orders");
        for (String orderId : orders) {
            Order order = orderMapper.findOrderId(orderId);
            if (!order.getAddUser().equals(id)) {
                return new ResponseData(200, "订单中包含他人订单，关联失败", null);
            }
        }
        for (String orderId : orders) {
            orderMapper.updateInvoice(orderId, (int) map.get("orderInvoiceId"));//订单关联发票
        }
        return new ResponseData(200, "关联成功", null);
    }

    /**
     * 新增合同附件 （所有订单）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveInvoice(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        map.put("orderInvoiceSite", JsonUtil.listToJson(jsonUtil.replaceUrl((List<Map<String, Object>>) map.get("flies"))));
        map.put("addUser", jwtUtil.getId(request.getHeader("token")));//存入操作人
        try {
            map.put("addTime", df.parse(df.format(new Date())));
        } catch (Exception e) {
            log.error("时间转换异常 {}", e);
        }
        try {
            orderInvoiceMapper.saveInvoiceUser(map);//插入发票附件
            BigInteger orderInvoiceId = (BigInteger) map.get("orderInvoiceId");
            orderMapper.updateInvoice((String) map.get("orderId"), orderInvoiceId.intValue());//订单关联发票附件
            return new ResponseData(200, "新增成功", null);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "发票名字和编号已存在", null);
        }

    }

    /**
     * 修改合同附件 （所有订单）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateInvoice(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        map.put("orderInvoiceSite", JsonUtil.listToJson(jsonUtil.replaceUrl((List<Map<String, Object>>) map.get("flies"))));
        map.put("updateUser", jwtUtil.getId(request.getHeader("token")));
        map.put("updateTime", df.parse(df.format(new Date())));
        try {
            orderInvoiceMapper.updateInvoiceUser(map);//执行修改操作
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "发票名称编号已存在，修改失败", null);
        }
    }

    /**
     * 取消关联  （所有订单）
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseData cancelRelevance(String orderId) {
        orderMapper.updateInvoice(orderId, 0);//解除关联
        return new ResponseData(200, "取消关联成功", null);
    }

    /**
     * 查询所有未关联合同附件的订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findNotOrder(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findNotByUser(map, null);
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据合同ID查看已关联的订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceInvoiceId(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.relevanceInvoice(map);
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 订单关联发票 （所有订单）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceOrderId(Map<String, Object> map) {
        List<String> orders = (List<String>) map.get("orders");
        for (String orderId : orders) {
            orderMapper.updateInvoice(orderId, (int) map.get("orderInvoiceId"));//订单关联发票
        }
        return new ResponseData(200, "关联成功", null);
    }

    /**
     * 根据发票附件ID删除发票附件 并解除所有关联 （所有订单）
     *
     * @param invoiceId
     * @return
     */
    @Override
    public ResponseData deleteInvoice(int invoiceId) {
        orderMapper.updateInvoiceId(invoiceId);//解除发票与订单关联
        orderInvoiceMapper.deleteInvoiceId(invoiceId);//删除发票附件
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 判断该合同是否是该用户上传
     *
     * @param invoiceId
     * @param request
     * @return
     */
    @Override
    public ResponseData ifUserId(int invoiceId, HttpServletRequest request) {
        OrderInvoice orderInvoice = orderInvoiceMapper.findById(invoiceId);
        if (orderInvoice.getUserId().equals(jwtUtil.getId(request.getHeader("token")))) {
            return new ResponseData(200, "查询成功", true);
        }
        return new ResponseData(200, "查询成功", false);
    }


}
