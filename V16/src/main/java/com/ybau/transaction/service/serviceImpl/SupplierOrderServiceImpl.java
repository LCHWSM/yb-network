package com.ybau.transaction.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Invoice;
import com.ybau.transaction.domain.SupplierOrder;
import com.ybau.transaction.domain.User;
import com.ybau.transaction.mapper.InvoiceMapper;
import com.ybau.transaction.mapper.LogMapper;
import com.ybau.transaction.mapper.SupplierOrderMapper;
import com.ybau.transaction.mapper.UserMapper;
import com.ybau.transaction.service.InvoiceService;
import com.ybau.transaction.service.SupplierOrderService;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class SupplierOrderServiceImpl implements SupplierOrderService {

    @Autowired
    SupplierOrderMapper supplierOrderMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    LogMapper logMapper;

    @Autowired
    InvoiceMapper invoiceMapper;

    @Autowired
    UserMapper userMapper;


    /**
     * 向供应商下单
     *
     * @param map
     * @param request
     * @return
     * @throws ParseException
     */
    @Override
    public ResponseData saveOrder(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        User user = userMapper.findById(id);
        map.put("organizationId", user.getOrganizationId());
        map.put("addUser", id);
        map.put("addTime", df.parse(df.format(new Date())));
        map.put("supplierOrderId", "SR" + MakeOrderNumUtil.makeOrderNum());
        if (map.get("productNum") == null && map.get("productName") == null && map.get("amount") == null) {
            return new ResponseData(400, "商品信息不可为空", null);
        }
        if (map.get("supplierId") == null) {
            return new ResponseData(400, "请选择供应商下单", null);
        }
        try {
            int amount = (int) map.get("amount");
            if (amount < 1) {
                return new ResponseData(400, "商品数量不可小于1", null);
            }
        } catch (Exception e) {
            return new ResponseData(400, "商品数量填写错误", null);
        }
        int count = supplierOrderMapper.findSORecord(map);//查询是否已存在记录
        if (count < 1) {
            supplierOrderMapper.saveSORecord(map);//存入供应商记录信息
        }
        if (map.get("invoiceFlag") != null && map.get("invoiceFlag").toString().equals("1")) {
            Invoice invoice = JSON.parseObject(JSON.toJSONString(map.get("invoice")), Invoice.class);
            if (invoice.getInvoiceType() == 0) {
                return new ResponseData(400, "发票类型不可为空", null);
            } else if (invoice.getUnitName() == null || invoice.getUnitName() == "" || invoice.getFlag() == 0) {
                return new ResponseData(400, "发票抬头或抬头类型不可为空", null);
            }
            //如果invoiceFlag为2为需要上传发票存入发票信息
            Map<String, Object> InvoiceMap = invoiceService.saveInvoice(invoice, map.get("supplierOrderId").toString(), id);//调用插入方法插入数据库
            if ((int) InvoiceMap.get("flag") == 1) {
                //如果flag为1数据有问题，回滚数据，提示错误
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, InvoiceMap.get("name").toString(), null);
            }
        }
        supplierOrderMapper.saveOrder(map);
        return new ResponseData(200, "下单成功", null);
    }

    /**
     * 查询用户向供应商所下订单
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findUIdOrder(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrder> supplierOrders = supplierOrderMapper.findUIdOrder(map, id);
        PageInfo pageInfo = new PageInfo(supplierOrders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 修改供应商订单信息
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData updateOrderById(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        map.put("logUser", jwtUtil.getId(request.getHeader("token")));
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("classify", 1);
        map.put("logOrderId", map.get("supplierOrderId"));
        try {
            int amount = (int) map.get("amount");
            if (amount < 1) {
                return new ResponseData(400, "商品数量不可小于1", null);
            }
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "商品数量填写错误", null);
        }
        if (map.get("invoiceFlag").equals("1")) {
            if (map.get("invoiceType") == null || map.get("invoiceType") == "") {
                return new ResponseData(400, "发票类型不可为空", null);
            } else if (map.get("unitName") == null || map.get("unitName") == "" || map.get("flag") == null || map.get("flag") == "") {
                return new ResponseData(400, "发票抬头或抬头类型不可为空", null);
            }
        }
        if (map.get("addUser") == null) {
            return new ResponseData(400, "跟进人不可为空", null);
        }
        User user = userMapper.findById((String) map.get("addUser"));
        if (user == null) {
            return new ResponseData(400, "无此用户", null);
        }
        if (map.get("invoiceFlag").equals("2")) {
            map.put("logName", "商品编号：" + map.get("productNum") + ",商品名称：" + map.get("productName") + ",规格：" + map.get("specification") + ",数量：" + map.get("amount") + ",跟进人:" + user.getName() + ",备注：" + map.get("remarks")
                    + ",发票抬头:" + ",纳税人识别号:" + ",地址、电话:" + ",开户行及账号 :" + ",邮箱:" + ",发票邮寄地址(专票必填):");
        } else {
            map.put("logName", "商品编号：" + map.get("productNum") + ",商品名称：" + map.get("productName") + ",规格：" + map.get("specification") + ",数量：" + map.get("amount") + ",跟进人:" + user.getName() + ",备注：" + map.get("remarks")
                    + ",发票抬头:" + map.get("unitName") + ",纳税人识别号:" + map.get("invoiceNumber") + ",地址、电话:" + map.get("unitSite") + map.get("unitPhone") + ",开户行及账号 :" + map.get("bankDeposit") + map.get("bankAccount") + ",邮箱:" + map.get("email") + ",发票邮寄地址(专票必填):" + map.get("ticketSite"));
        }
        //执行修改操作
        supplierOrderMapper.updateOrderById(map);
        logMapper.saveSupplierLog(map);//插入日志信息
        //判断是否修改了发票信息
        map.put("orderId", map.get("supplierOrderId"));
        if (invoiceMapper.findByOrderId(map.get("supplierOrderId").toString()) > 0) {
            //如果大于0则已存在发票信息
            if (map.get("invoiceFlag").equals("2")) {
                //如果有发票信息传入不需要则删除发票信息
                invoiceMapper.deleteByOrderId(map.get("supplierOrderId").toString());
            } else {
                invoiceMapper.updateByOrderId(map);
            }
        } else {
            //如果没有发票信息传入需要发票信息 则新增发票信息
            if (map.get("invoiceFlag").equals("1")) {
                invoiceMapper.saveInvoiceMap(map);
            }
        }
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 根据登录用户的供应商ID查询该供应商的所有订单
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findBySupplier(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        User user = userMapper.findById(id);//根据用户ID查询用户相关信息
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrder> supplierOrders = supplierOrderMapper.findBySupplier(map, user.getOrganizationId());
        PageInfo pageInfo = new PageInfo(supplierOrders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 修改报价
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateQuotation(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        String id = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        map.put("logUser", id);
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("logOrderId", map.get("supplierOrderId"));
        map.put("classify", 2);
        double div = 0;
        SupplierOrder supplierOrder = supplierOrderMapper.findById((String) map.get("supplierOrderId"));
        User user = userMapper.findById(id);
        if (supplierOrder.getSupplierId() != user.getOrganizationId()) {
            return new ResponseData(400, "无权对其他供应商订单进行操作", null);
        }
        if (supplierOrder.getDispose() == 3) {
            return new ResponseData(400, "供应商已拒绝交易，不可更改", null);
        }
        if (supplierOrder.getFlag() == 2) {
            return new ResponseData(400, "报价已锁定，不可更改", null);
        }
        try {
            int price = (int) map.get("price");
            if (price < 1) {
                return new ResponseData(400, "报价金额不可低于1元", null);
            }
            div = BigDecimalUtil.div(price + "", "100", 2);
        } catch (Exception e) {
            return new ResponseData(400, "报价金额有误", null);
        }
        map.put("logName", "修改报价为：￥" + DateUtli.displayWithComma(String.valueOf(div)) + "元");
        if (supplierOrder.getSettlementAmount() == 0) {
            supplierOrderMapper.updateQuotation(div, (String) map.get("supplierOrderId"), 1);
        } else if (div > supplierOrder.getSettlementAmount()) {
            supplierOrderMapper.updateQuotation(div, (String) map.get("supplierOrderId"), 2);
        } else {
            supplierOrderMapper.updateQuotation(div, (String) map.get("supplierOrderId"), 3);
        }
        logMapper.saveSupplierLog(map);//插入日志信息
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 报价锁定状态修改
     *
     * @param supplierOrderId
     * @param flag
     * @return
     */
    @Override
    public ResponseData lockQuotation(String supplierOrderId, int flag, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SupplierOrder supplierOrder = supplierOrderMapper.findById(supplierOrderId);
        String id = jwtUtil.getId(request.getHeader("token"));
        if (!id.equals(supplierOrder.getUserId())) {
            return new ResponseData(400, "您无权操作其他用户订单", null);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("logUser", id);
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("logOrderId", supplierOrderId);
        map.put("classify", 2);
        String flagStr = flag == 1 ? "未锁定" : "锁定";
        map.put("logName", "修改报价状态为：" + flagStr);
        supplierOrderMapper.lockQuotation(supplierOrderId, flag);//执行修改操作
        logMapper.saveSupplierLog(map);//插入日志信息
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 修改供应商反馈
     *
     * @param supplierOrderId
     * @param dispose
     * @param request
     * @return
     */
    @Override
    public ResponseData updateDispose(String supplierOrderId, int dispose, String disposeStr, HttpServletRequest request, String disposeLog) throws ParseException {
        String id = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SupplierOrder supplierOrder = supplierOrderMapper.findById(supplierOrderId);
        User user = userMapper.findById(id);
        if (user.getOrganizationId() != supplierOrder.getSupplierId()) {
            return new ResponseData(400, "无权对其他供应商订单进行操作", null);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("logUser", id);
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("logOrderId", supplierOrderId);
        map.put("classify", 3);
        map.put("logName", "修改供应商反馈为：" + disposeStr);
        map.put("logRemarks", disposeLog);
        supplierOrderMapper.updateDispose(supplierOrderId, dispose, disposeLog);//执行修改操作
        logMapper.saveSupplierLog(map);
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 本人结算金额
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData closeSum(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        String id = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        double moneyDiv;
        SupplierOrder supplierOrder = supplierOrderMapper.findById(map.get("supplierOrderId").toString());
        if (map.get("operationTime") == null || map.get("operationTime") == "") {
            return new ResponseData(400, "结算时间不可为空", null);
        }
        if (!supplierOrder.getUserId().equals(id)) {
            return new ResponseData(400, "您无权操作其他用户订单", null);
        }
        try {
            if (BigDecimalUtil.calcJulianDays(sdf.parse(sdf.format(new Date())), sdf.parse((String) map.get("operationTime"))) < 0) {
                return new ResponseData(400, "结算时间选择错误", null);
            } else if (supplierOrder.getQuotation() <= 0) {
                return new ResponseData(400, "该订单还未报价，无法结算", null);
            }
        } catch (Exception e) {
            return new ResponseData(400, "结算时间选择错误", null);
        }
        try {
            int money = (int) map.get("money");
            moneyDiv = BigDecimalUtil.div(money + "", "100", 2);//计算要结算金额
        } catch (Exception e) {
            return new ResponseData(400, "金额输入错误", null);
        }
        map.put("logUser", id);
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("logOrderId", map.get("supplierOrderId"));
        map.put("classify", 4);
        double add = BigDecimalUtil.add(moneyDiv, supplierOrder.getSettlementAmount());//累计结算金额
        map.put("logName", "本次结算金额为：￥" + DateUtli.displayWithComma(String.valueOf(moneyDiv)) + "元");
        if (supplierOrder.getQuotation() <= add) {
            //如果累计结算金额大于或等于报价则为已结算状态，如果小于报价则为部分结算
            supplierOrderMapper.closeSum(map.get("supplierOrderId").toString(), add, 3);
        } else {
            supplierOrderMapper.closeSum(map.get("supplierOrderId").toString(), add, 2);
        }
        logMapper.saveSupplierLog(map);//存入日志
        return new ResponseData(200, "结算成功", null);
    }

    /**
     * 修改已结算金额
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateAmount(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        String id = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SupplierOrder supplierOrder = supplierOrderMapper.findById(map.get("supplierOrderId").toString());
        double moneyDiv;
        if (supplierOrder.getQuotation() <= 0) {
            return new ResponseData(400, "该订单还未报价，无法修改结算金额", null);
        }
        try {
            int money = (int) map.get("money");
            if (money < 0) {
                return new ResponseData(400, "金额不可为负数", null);
            }
            moneyDiv = BigDecimalUtil.div(money + "", "100", 2);
        } catch (Exception e) {
            return new ResponseData(400, "金额输入有误", null);
        }
        map.put("logUser", id);
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("logOrderId", map.get("supplierOrderId"));
        map.put("classify", 5);
        map.put("logName", "修改结算金额为：￥" + DateUtli.displayWithComma(String.valueOf(moneyDiv)) + "元");
        if (moneyDiv == 0) {
            supplierOrderMapper.closeSum(map.get("supplierOrderId").toString(), moneyDiv, 1);
        } else if (moneyDiv >= supplierOrder.getQuotation()) {
            //如果修改的结算金额大于或等于报价 则为已结算
            supplierOrderMapper.closeSum(map.get("supplierOrderId").toString(), moneyDiv, 3);
        } else {
            supplierOrderMapper.closeSum(map.get("supplierOrderId").toString(), moneyDiv, 2);
        }
        logMapper.saveSupplierLog(map);//存入日志
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 查询公司内供应商订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findByOrganId(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        User user = userMapper.findById(id);
        map.put("organizationId", user.getOrganizationId());
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrder> supplierOrders = supplierOrderMapper.findByOrganId(map);
        PageInfo pageInfo = new PageInfo(supplierOrders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 公司结算订单
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData companyCloseSum(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        User user = userMapper.findById(id);
        double moneyDiv;
        SupplierOrder supplierOrder = supplierOrderMapper.findById(map.get("supplierOrderId").toString());
        if (user.getOrganizationId() != supplierOrder.getOrganizationId()) {
            return new ResponseData(400, "您无权操作其他公司订单", null);
        }
        if (map.get("operationTime") == null || map.get("operationTime") == "") {
            return new ResponseData(400, "结算时间不可为空", null);
        }
        try {
            if (BigDecimalUtil.calcJulianDays(sdf.parse(sdf.format(new Date())), sdf.parse((String) map.get("operationTime"))) < 0) {
                return new ResponseData(400, "结算时间选择错误", null);
            } else if (supplierOrder.getQuotation() <= 0) {
                return new ResponseData(400, "该订单还未报价，无法结算", null);
            }
        } catch (Exception e) {
            return new ResponseData(400, "结算时间选择错误", null);
        }
        try {
            int money = (int) map.get("money");
            moneyDiv = BigDecimalUtil.div(money + "", "100", 2);//计算要结算金额
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "金额输入错误", null);
        }
        map.put("logOrderId", map.get("supplierOrderId"));
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("logUser", id);
        map.put("classify", 4);
        double add = BigDecimalUtil.add(moneyDiv, supplierOrder.getSettlementAmount());//累计结算金额
        map.put("logName", "本次结算金额为：￥" + DateUtli.displayWithComma(String.valueOf(moneyDiv)) + "元");
        if (supplierOrder.getQuotation() <= add) {
            //如果累计结算金额大于或等于报价则为已结算状态，如果小于报价则为部分结算
            supplierOrderMapper.closeSum(map.get("supplierOrderId").toString(), add, 3);
        } else {
            supplierOrderMapper.closeSum(map.get("supplierOrderId").toString(), add, 2);
        }
        logMapper.saveSupplierLog(map);//存入日志
        return new ResponseData(200, "结算成功", null);
    }

    /**
     * 查询所有订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<SupplierOrder> supplierOrders = supplierOrderMapper.findAll(map);
        PageInfo pageInfo = new PageInfo(supplierOrders);
        return new ResponseData(200, "查询成功", pageInfo);
    }


}
