package com.ybau.transaction;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import com.ybau.transaction.domain.*;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.*;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@SpringBootTest
@Slf4j
public class UserTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    PullOrder pullOrder;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    GoodsService goodsService;


    @Autowired
    UserMapper userMapper;

    @Autowired
    RedissonClient client;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ExpressMapper expressMapper;

    //规定借样时间低于该值进行提醒
    @Value("${order.RemindTime}")
    int RemindTime;

    @Autowired
    UserSiteService userSiteService;

    @Autowired
    SponsorAuditService sponsorAuditService;

    @Autowired
    InvoiceMapper invoiceMapper;

    @Autowired
    ProcessGroupMapper processGroupMapper;

    @Autowired
    ClearingLogMapper clearingLogMapper;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    UserSiteMapper userSiteMapper;

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    SponsorAuditMapper sponsorAuditMapper;

    @Autowired
    LogMapper logMapper;

    //邮箱正则表达式
    @Value("${user.RULE_EMAIL}")
    String RULE_EMAIL;
    //手机号码正则表达式
    @Value("${user.userPhone}")
    String userPhone;

    @Autowired
    AuditLogMapper auditLogMapper;

    @Autowired
    AuditMapper auditMapper;

    @Autowired
    PermissionUtil permissionUtil;

    @Test
    public void orderTest() throws Exception {
//        Map map = new HashMap();
//        Map map1 = new HashMap();
//        List<Map<String, Object>> list = new ArrayList();
//        map.put("clearingRemark", "");
//        map.put("orderSign", 0);
//        map.put("paymentMethod", 3);
//        map.put("receiverAddress", "");
//        map.put("receiverCity", "");
//        map.put("receiverDistrict", "");
//        map.put("receiverMobile", "");
//        map.put("receiverName", "李怡");
//        map.put("receiverProvince", "");
//        map.put("receiverStreet", "");
//        map.put("actualMoney", 378000);
//        map.put("serveCost", 0);
//        map.put("ticketSite", "");
//        map.put("invoiceFlag", "1");
//        map.put("orderId", "20201124143937364002");
//        map.put("orderRemark", "");
//        map.put("bankDeposit", "");
//        map.put("email", "");
//        map.put("flag", 2);
//        map.put("freight", 0);
//        map.put("invoiceNumber", "");
//        map.put("invoiceType", "2");
//        map.put("unitName", "广东粤宝黄金珠宝有限公司");
//        map.put("unitSite", "");
//        map1.put("goodsCore", "G00185");
//        map1.put("goodsId", "503");
//        map1.put("goodsImages", "http://192.168.3.11:8087/yb_network_ftp/v5-1/image/4/1.jpg");
//        map1.put("goodsName", "2020鼠年流通纪念币（金典封装版）");
//        map1.put("goodsNumber", 200);
//        map1.put("goodsPrice", 6800);
//        map1.put("id",450);
//        map1.put("warehouse",0);
//        map.put("userId", "3800");
//        list.add(map1);
//        map.put("goodsDetail", list);
//        int z = 1;
//        while (z < 1000) {
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//
//            String id = (String) map.get("userId");
//            Order order = orderMapper.findOrderId(map.get("orderId").toString());
//            double actualDiv = 0;
//            double freightDiv = 0;
//            try {
//                int actualMoney = (int) map.get("actualMoney");
//                int freight = (int) map.get("freight");
//                if (actualMoney < 0 || freight < 0) {
//
//                }
//                actualDiv = BigDecimalUtil.div(map.get("actualMoney").toString(), "100", 2);
//                map.put("actualMoney", actualDiv);//对实收金额重新赋值
//                freightDiv = BigDecimalUtil.div(map.get("freight").toString(), "100", 2);
//                map.put("freight", freightDiv);//对运费重新赋值
//                double serveCost = BigDecimalUtil.div((int) map.get("serveCost"), 100, 2);
//                map.put("serveCost", serveCost);//对服务费重新赋值
//            } catch (Exception e) {
//
//            }
//            if (map.get("invoiceFlag").equals("1")) {
//                if (map.get("invoiceType") == null || map.get("invoiceType") == "") {
//
//                } else if (map.get("unitName") == null || map.get("unitName") == "" || map.get("flag") == null || map.get("flag") == "") {
//
//                }
//            }
//            if (map.get("receiverMobile") != null && map.get("unitPhone") != null && map.get("receiverMobile") != "" && map.get("unitPhone") != "") {
//                if (!Pattern.compile(userPhone).matcher((String) map.get("receiverMobile")).matches() && Pattern.compile(userPhone).matcher((String) map.get("unitPhone")).matches()) {
//
//                }
//            }
//            if (map.get("email") != null && map.get("email") != "") {
//                //如果邮箱不为空验证邮箱
//                if (!Pattern.compile(RULE_EMAIL).matcher((String) map.get("email")).matches()) {
//
//                }
//            }
//            double now = BigDecimalUtil.add(actualDiv, freightDiv);
//            double add = BigDecimalUtil.add(order.getActualMoney(), order.getFreight());
//            map.put("clearingLogName", "修改订单信息");
//            map.put("clearingLogUser", id);
//            map.put("clearingLogTime", df.parse(df.format(new Date())));
//            map.put("clearingLogOrderId", map.get("orderId"));
//            map.put("classify", 1);
//            if (order.getPaymentMethod() == 1 && order.getRetreatCargo() == 0 && order.getAudit() == 1 || order.getAudit() == 3) {
//                //如果为额度订单  并且未退货 已审核通过或审核中  扣除或增加相应额度
//                //如果为额度下单并且未付款 并且未退货 则修改金额时扣除相关额度
//                if (order.getPaymentStatus() == 1 || order.getPaymentStatus() == 8) {
//                    //如果是为结算 或部分结算 则去扣除或增加相应额度
//                    if (!String.valueOf(now).equals(String.valueOf(add))) {
//                        if (now < order.getSettlementAmount()) {
//
//                        }
//                        //判断 是否修改了金额
//                        double sub = BigDecimalUtil.sub(String.valueOf(add), String.valueOf(now));
//                        //根据公司ID更新  修改后的订单额度信息
//                        organizationMapper.updateLimit(sub, order.getAddCompany());
//                    }
//                }
//            }
//            map.put("site", map.get("receiverProvince").toString() + map.get("receiverCity").toString() + map.get("receiverDistrict").toString() + map.get("receiverStreet").toString() + map.get("receiverAddress").toString());
//            if (map.get("invoiceFlag").equals("2")) {
//                map.put("consignee", "姓名:" + map.get("receiverName").toString() + "，手机号:" + map.get("receiverMobile").toString() + "，地址:" + map.get("site").toString() + "，实收金额:" + map.get("actualMoney") + "，其他费用:" + map.get("freight") + "，订单备注:" + map.get("orderRemark")
//                        + "，发票抬头:" + "，纳税人识别号:" + "，地址、电话:" + "，开户行及账号 :" + "，邮箱:" + "，发票邮寄地址(专票必填):");
//            } else {
//                map.put("consignee", "姓名:" + map.get("receiverName").toString() + "，手机号:" + map.get("receiverMobile").toString() + "，地址:" + map.get("site").toString() + "，实收金额:" + map.get("actualMoney") + "，其他费用:" + map.get("freight") + "，订单备注:" + map.get("orderRemark")
//                        + "，发票抬头:" + map.get("unitName") + "，纳税人识别号:" + map.get("invoiceNumber") + "，地址、电话:" + map.get("unitSite") + map.get("unitPhone") + "，开户行及账号 :" + map.get("bankDeposit") + map.get("bankAccount") + "，邮箱:" + map.get("email") + "，发票邮寄地址(专票必填):" + map.get("ticketSite"));
//            }
//            List<Map<String, Object>> goodsDetail = (List<Map<String, Object>>) map.get("goodsDetail");
//            double sumMoney = 0;
//            for (Map<String, Object> goods : goodsDetail) {
//                try {
//                    double price = productMapper.findByPrive(goods.get("goodsId").toString());
//                    double div = BigDecimalUtil.div((Integer) goods.get("goodsPrice"), 100, 2);//计算用户修改的价格
//                    goods.put("goodsPrice", div);
//                    double mul = BigDecimalUtil.mul(price, (int) goods.get("goodsNumber"));//计算出单个商品价格
//                    sumMoney = BigDecimalUtil.add(mul, sumMoney);//计算出订单总金额
//                    map.put("consignee", map.get("consignee") + "，商品编号:" + goods.get("goodsCore") + "，商品名字:" + goods.get("goodsName") + "，商品价格:" + div + "，商品数量:" + goods.get("goodsNumber"));
//                } catch (Exception e) {
//                }
//            }
//            map.put("sumMoney", sumMoney);//订单总金额赋值
//            goodsService.updateGoods(goodsDetail);
//            //执行修改订单操作
//            orderMapper.updateOrder(map);
//            //执行修改发票信息操作
//            int count = invoiceMapper.findByOrderId(map.get("orderId").toString());
//            if (count > 0) {
//                if (map.get("invoiceFlag").equals("2")) {
//                    //如果有发票信息传入不需要则删除发票信息
//                    invoiceMapper.deleteByOrderId(map.get("orderId").toString());
//                } else {
//                    invoiceMapper.updateByOrderId(map);
//                }
//            } else {
//                //如果没有发票信息传入需要发票信息 则新增发票信息
//                if (map.get("invoiceFlag").equals("1")) {
//                    invoiceMapper.saveInvoiceMap(map);
//                }
//            }
//            //执行订单日志插入操作
//            clearingLogMapper.saveClearingLog(map);
//            z++;
//        }
//    }
    }
}

