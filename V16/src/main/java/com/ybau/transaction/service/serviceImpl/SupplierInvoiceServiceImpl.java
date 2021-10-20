package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.domain.OrderInvoice;
import com.ybau.transaction.domain.SupplierInvoice;
import com.ybau.transaction.domain.SupplierOrder;
import com.ybau.transaction.mapper.SupplierInvoiceMapper;
import com.ybau.transaction.mapper.SupplierOrderMapper;
import com.ybau.transaction.service.SupplierInvoiceService;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class SupplierInvoiceServiceImpl implements SupplierInvoiceService {


    @Autowired
    SupplierInvoiceMapper supplierInvoiceMapper;

    @Value("${ftp.visitUrl}")
    String prefixUrl;

    @Value("${ftp.bastPath}")
    String bastPath;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    SupplierOrderMapper supplierOrderMapper;

    @Autowired
    JsonUtil jsonUtil;


    /**
     * 根据订单Id查询关联发票附件
     *
     * @param supplierOrderId
     * @return
     */
    @Override
    public ResponseData findOrderId(String supplierOrderId) {
        OrderInvoice orderInvoice = supplierInvoiceMapper.findOrderId(supplierOrderId);
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
     * 新增发票附件信息 （ 供应商用户）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveInvoiceUser(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        SupplierOrder supplierOrder = supplierOrderMapper.findById((String) map.get("supplierOrderId"));
        if (!supplierOrder.getUserId().equals(id)) {
            return new ResponseData(400, "不可关联他人订单", null);
        }
        map.put("supplierInvoiceSite", JsonUtil.listToJson(jsonUtil.replaceUrl((List<Map<String, Object>>) map.get("flies"))));
        map.put("addUser", id);//存入操作人
        map.put("addTime", df.parse(df.format(new Date())));

        try {
            supplierInvoiceMapper.saveInvoiceUser(map);//插入发票信息
            BigInteger supplierInvoiceId = (BigInteger) map.get("supplierInvoiceId");
            supplierOrderMapper.updateInvoice((String) map.get("supplierOrderId"), supplierInvoiceId.intValue());//订单关联发票附件
            return new ResponseData(200, "上传成功", null);
        } catch (Exception e) {
            log.error("时间转换异常 {}", e);
            return new ResponseData(400, "发票名称或编号重复", null);
        }

    }

    /**
     * 修改供应商发票附件信息 （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateInvoiceUser(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        SupplierInvoice supplierInvoice = supplierInvoiceMapper.findSupplierInvoice((int) map.get("supplierInvoiceId"));
        if (!supplierInvoice.getUserId().equals(id)) {
            return new ResponseData(400, "不可修改他人发票附件", null);
        }
        map.put("orderInvoiceSite", JsonUtil.listToJson(jsonUtil.replaceUrl((List<Map<String, Object>>) map.get("flies"))));
        map.put("updateUser", id);
        map.put("updateTime", df.parse(df.format(new Date())));
        try {
            supplierInvoiceMapper.updateInvoiceUser(map);//执行修改操作
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "发票名称编号已存在，修改失败", null);
        }
    }

    /**
     * 取消发票与订单的关联
     *
     * @param supplierOrderId
     * @return
     */
    @Override
    public ResponseData cancelReleUser(String supplierOrderId, HttpServletRequest request) {
        SupplierOrder supplierOrder = supplierOrderMapper.findById(supplierOrderId);
        if (!supplierOrder.getUserId().equals(jwtUtil.getId(request.getHeader("token")))) {
            return new ResponseData(400, "不可操作他人订单", null);
        }
        supplierOrderMapper.updateInvoice(supplierOrderId, 0);
        return new ResponseData(200, "取消关联成功", null);
    }

    /**
     * 查询所有未关联发票附件的 订单  （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findNotOrderUser(Map<String, Object> map, HttpServletRequest request) {
        PageHelper.startPage( (int) map.get("pageNum"),(int) map.get("pageSize"));
        List<SupplierOrder> supplierOrder = supplierOrderMapper.findNotOrderUser(map, jwtUtil.getId(request.getHeader("token")));
        PageInfo pageInfo = new PageInfo(supplierOrder);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据发票ID查询该发票关联的订单信息  （用户）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceInvoiceUser(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrder> supplierOrder = supplierOrderMapper.relevanceInvoiceUser(map);
        PageInfo pageInfo = new PageInfo(supplierOrder);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 订单关联发票 （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData relevanceOrderUser(Map<String, Object> map, HttpServletRequest request) {
        List<String> supplierOrderIds = (List<String>) map.get("supplierOrderIds");
        for (String supplierOrderId : supplierOrderIds) {
            SupplierOrder supplierOrder = supplierOrderMapper.findById(supplierOrderId);
            if (!supplierOrder.getUserId().equals(jwtUtil.getId(request.getHeader("token")))) {
                return new ResponseData(400, "不可操作他人订单", null);
            }
        }
        for (String supplierOrderId : supplierOrderIds) {
            supplierOrderMapper.updateInvoice(supplierOrderId, (int) map.get("supplierInvoiceId"));//订单关联发票附件
        }
        return new ResponseData(200, "关联成功", null);
    }

    /**
     * 新增发票附件  （所有订单）
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveInvoice(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        map.put("supplierInvoiceSite", JsonUtil.listToJson(jsonUtil.replaceUrl((List<Map<String, Object>>) map.get("flies"))));
        map.put("addUser", jwtUtil.getId(request.getHeader("token")));//存入操作人
        map.put("addTime", df.parse(df.format(new Date())));
        try {
            supplierInvoiceMapper.saveInvoiceUser(map);//插入发票信息
            BigInteger supplierInvoiceId = (BigInteger) map.get("supplierInvoiceId");
            supplierOrderMapper.updateInvoice((String) map.get("supplierOrderId"), supplierInvoiceId.intValue());//订单关联发票附件
            return new ResponseData(200, "上传成功", null);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "发票名称或编号重复", null);
        }
    }

    /**
     * 修改发票附件信息 （所有订单）
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
            supplierInvoiceMapper.updateInvoiceUser(map);//执行修改操作
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "发票名称编号已存在，修改失败", null);
        }
    }

    /**
     * 取消发票附件与订单关联
     * @param supplierOrderId
     * @return
     */
    @Override
    public ResponseData cancelRelevance(String supplierOrderId) {
        supplierOrderMapper.updateInvoice(supplierOrderId, 0);
        return new ResponseData(200, "取消关联成功", null);
    }

    /**
     * 查询所有未关联发票的订单  （所有订单）
     * @param map
     * @return
     */
    @Override
    public ResponseData findNotOrder(Map<String, Object> map) {
        PageHelper.startPage( (int) map.get("pageNum"),(int) map.get("pageSize"));
        map.put("supplierInvoiceId",0);
        List<SupplierOrder> supplierOrder = supplierOrderMapper.findNotOrderUser(map, null);
        PageInfo pageInfo = new PageInfo(supplierOrder);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据发票ID查看已关联的订单 （所有订单）
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceInvoiceId(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrder> supplierOrder = supplierOrderMapper.relevanceInvoiceUser(map);
        PageInfo pageInfo = new PageInfo(supplierOrder);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 订单关联发票
     * @param map
     * @return
     */
    @Override
    public ResponseData relevanceOrderId(Map<String, Object> map) {
        List<String> supplierOrderIds = (List<String>) map.get("supplierOrderIds");
        for (String supplierOrderId : supplierOrderIds) {
            supplierOrderMapper.updateInvoice(supplierOrderId, (int) map.get("supplierInvoiceId"));//订单关联发票附件
        }
        return new ResponseData(200, "关联成功", null);
    }

    /**
     *  删除发票  （所有订单）
     * @param supplierInvoiceId
     * @return
     */
    @Override
    public ResponseData deleteInvoice(int supplierInvoiceId) {
        supplierOrderMapper.deleteInvoice(supplierInvoiceId);//根据发票Id解除所有关联信息
        supplierInvoiceMapper.deleteInvoice(supplierInvoiceId);//根据发票ID删除发票
        return new ResponseData(200,"删除成功",null);
    }

    /**
     * 判断发票附件是不是登录用户上传的
     * @param supplierInvoiceId
     * @param request
     * @return
     */
    @Override
    public ResponseData ifUserId(int supplierInvoiceId, HttpServletRequest request) {
        SupplierInvoice supplierInvoice = supplierInvoiceMapper.findSupplierInvoice(supplierInvoiceId);
        if (supplierInvoice.getUserId().equals(jwtUtil.getId(request.getHeader("token")))) {
            return new ResponseData(200, "查询成功", true);
        }
        return new ResponseData(200, "查询成功", false);
    }
}
