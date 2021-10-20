package com.ybau.transaction.service.serviceImpl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.*;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.*;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

//    @Autowired
//    PullOrder pullOrder;

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

    /**
     * 根据时间拉取订单
     *
     * @param params
     * @return
     */
    @Override
    public List<Order> findOrderDate(Map<String, Object> params) throws Exception {
        return orderMapper.findOrderDate(params);
    }

    /**
     * 查询所有订单（分页）
     *
     * @return
     */
    @Override
    public ResponseData findOrderAll(Map<String, Object> map, HttpServletRequest request) {
        try {
            String name = permissionUtil.findByShow(jwtUtil.getId(request.getHeader("token")));
            int pageSize = (int) map.get("pageSize");
            int pageNum = (int) map.get("pageNum");
            PageHelper.startPage(pageNum, pageSize);
            List<Order> orderList = orderMapper.findOrderAll(map);
            for (Order order : orderList) {
                order.setOrderSignNameStr(name);
            }
            PageInfo<Order> pageInfo = new PageInfo<>(orderList);
            return new ResponseData(200, "查询成功", pageInfo);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "传入参数有误", null);
        }
    }

    /**
     * 根据时间调用粤宝网络接口
     *
     * @param map
     * @return
     * @throws Exception
     */

//    @Override
//    public ResponseData findOrderByDate(Map<String, Object> map) throws Exception {
//        pullOrder.findOrderByDate(map);//异步拉取订单
//        return new ResponseData(200, "已提交拉取，请到订单中心查看", null);
//
//    }

    /**
     * 更改发货标识
     *
     * @param
     */
    @Override
    public void updateExpress(String orderId) {
        orderMapper.updateExpress(orderId);
    }

    /**
     * 创建订单
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveOrder(Map<String, Object> map, HttpServletRequest request) throws Exception {
        Map<String, Object> goodsMap = new HashMap<>();
        String now = "0";
        double sumMoney = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("addUser", id);//存入订单创建人
        Organization organization = organizationMapper.findByUId(jwtUtil.getId(token));
        //从Redis中取出用户权限
        RBucket<Object> clientBucket = client.getBucket("data_ybnetwork.permission" + id);
        String permissionStr = (String) clientBucket.get();
        List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
        boolean flag = false;
        for (Permission permission : permissions) {
            //判断用户是否有修改商品价格的权限
            if (permission.getUrl().equals("updatePrice")) {
                flag = true;
            }
        }
        int paymentMethod = (int) map.get("paymentMethod");
        if (!organization.getWay().contains(paymentMethod + "")) {
            return new ResponseData(400, "您所属公司不支持此种方式下单", null);
        }
        List<ProcessGroup> auditFlows = processGroupMapper.findByOId(organization.getOrganizationId(), organization.getProcessGroupId());
        if (auditFlows == null || auditFlows.size() < 1) {
            return new ResponseData(400, "您所属公司还未创建审批流程，无法下单", null);
        }
        map.put("addCompany", organization.getOrganizationId());//存入订单所属人公司ID
        map.put("orderId", MakeOrderNumUtil.makeOrderNum());//存入随机订单号
        map.put("organizationId", organization.getOrganizationId());//存入公司ID
        map.put("customerName", map.get("receiverName"));//赋值姓名
        map.put("customerMobile", map.get("receiverMobile"));//赋值手机号
        map.put("paymentStatus", 1);//赋值付款状态
        map.put("audit", 3);//赋值审核状态为审核中
        // 拼接收货地址
        map.put("site", map.get("receiverProvince").toString() +
                map.get("receiverCity").toString() +
                map.get("receiverDistrict").toString() +
                map.get("receiverStreet").toString() +
                map.get("receiverAddress").toString());
        List<Map<String, Object>> goodsDetail = (List<Map<String, Object>>) map.get("goodsDetail");
        if (goodsDetail == null || goodsDetail.size() < 1) {
            //如果下单商品列表无数据返回错误
            return new ResponseData(400, "须选择商品下单", null);
        }
        try {
            int serveCost = (int) map.get("serveCost");
            double div = BigDecimalUtil.div(serveCost, 100, 2);
            map.put("serveCost", div);//存入服务费
        } catch (Exception e) {
            return new ResponseData(400, "服务费金额输入错误", null);
        }
        try {
            map.put("orderTime", df.parse(df.format(new Date())));//存入订单创建时间
            double mul = 0;
            double sumMoneySingle = 0;
            for (int i = 0; i < goodsDetail.size(); i++) {
                goodsMap = goodsDetail.get(i);
                int goodsNumber = (int) goodsMap.get("goodsNumber");
                if (goodsNumber < 1) {
                    return new ResponseData(400, "下单数量须大于0", null);
                }
                double goodsPrice = BigDecimalUtil.div(goodsMap.get("goodsPrice").toString(), "100", 2);
                goodsMap.put("goodsPrice", goodsPrice);
                if (goodsPrice < 0) {
                    return new ResponseData(400, "商品金额不可低于0", null);
                }
                if (flag) {
                    //有修改价格的权限  使用用户输入的价格
                    double price = productMapper.findByPrive(goodsMap.get("goodsId").toString());
                    sumMoneySingle = BigDecimalUtil.mul(String.valueOf(price), goodsMap.get("goodsNumber").toString());//计算商品总金额
                    mul = BigDecimalUtil.mul(String.valueOf(goodsPrice), goodsMap.get("goodsNumber").toString());
                } else {
                    //无修改价格的权限  根据商品价格查找商品价格
                    double price = productMapper.findByPrive(goodsMap.get("goodsId").toString());
                    mul = BigDecimalUtil.mul(String.valueOf(price), goodsMap.get("goodsNumber").toString());
                    sumMoneySingle = mul;//赋值给商品总金额
                }
                sumMoney = BigDecimalUtil.add(sumMoneySingle, sumMoney);
                now = BigDecimalUtil.add(mul + "", now) + "";
            }
            map.put("sumMoney", sumMoney);//添加商品总金额
            map.put("now", now);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "下单数量或金额输入有误", null);
        }
        if (paymentMethod == 1) {
            //等于1为额度下单
            map.put("expressOnly", 0);
            int i = organizationMapper.subLimit(map);//更改额度
            if (i < 1) {
                //小于1额度不足直接返回
                return new ResponseData(400, "额度不足", null);
            }
            //额度充足查看库存是否足够
            try {
                goodsService.addGoods(goodsDetail, (String) map.get("orderId"));
            } catch (Exception e) {
                //回滚额度
                log.error("{}", e);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, "库存不足", null);
            }
        } else {
            //不等于1为直接购买或借样检查库存是否充足
            String returnTime = (String) map.get("returnTime");//取出用户设定的归还时间
            if ((int) map.get("paymentMethod") == 2) {
                //借样下单 付款状态赋值为为归还
                map.put("paymentStatus", 4);//赋值付款状态
                map.put("expressOnly", 0);
            }
            if ((int) map.get("paymentMethod") == 3) {
                //等于3为直接购买下单
                map.put("expressOnly", 2);
            }
            if (returnTime != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                Date currentTime = sdf.parse(sdf.format(new Date()));
                int i = BigDecimalUtil.calcJulianDays(sdf.parse(returnTime), currentTime);
                if (i > organization.getSettingTime()) {
                    //用户借样归还时间不可大于设定的归还时间
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return new ResponseData(400, "预计归还时间不可大于" + organization.getSettingTime() + "天", null);
                }
            }
            try {
                //修改商品库存数量
                goodsService.addGoods(goodsDetail, (String) map.get("orderId"));
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("{}", e);
                return new ResponseData(400, "库存不足", null);
            }
        }
        int count = userSiteMapper.findByCount(map);
        if (count < 1) {
            //如果不存在则插入，如果存在则忽略
            userSiteService.saveUserSite(map, request);//持久化用户地址
        }
        if ((map.get("invoiceFlag") != null && map.get("invoiceFlag").toString().equals("1"))) {
            Invoice invoice = JSON.parseObject(JSON.toJSONString(map.get("invoice")), Invoice.class);
            if (invoice.getInvoiceType() == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, "发票类型不可为空", null);
            } else if (invoice.getUnitName() == null || invoice.getUnitName() == "" || invoice.getFlag() == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, "发票抬头或抬头类型不可为空", null);
            }
            //如果invoiceFlag为2为需要上传发票存入发票信息
            Map<String, Object> InvoiceMap = invoiceService.saveInvoice(invoice, map.get("orderId").toString(), id);//调用插入方法插入数据库
            if ((int) InvoiceMap.get("flag") == 1) {
                //如果flag为1数据有问题，回滚数据，提示错误
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, InvoiceMap.get("name").toString(), null);
            }
        }
        RBucket<Object> bucket = client.getBucket("data_ybnetwork." + id);//更新用户购物车
        String goodsStr = (String) bucket.get();
        if (goodsStr != null) {
            List<Goods> goods = JsonUtil.jsonToList(goodsStr, Goods.class);
            for (int x = 0; x < goods.size(); x++) {
                for (int i = 0; i < goodsDetail.size(); i++) {
                    if (goods.get(x).getGoodsId().equals(goodsDetail.get(i).get("goodsId")) && goods.get(x).getGoodsNumber() == (int) goodsDetail.get(i).get("goodsNumber")) {
                        goods.remove(x);
                    }
                }
            }
            bucket.set(JsonUtil.listToJson(goods));
        }
        try {
            orderMapper.saveOrder(map);
            sponsorAuditService.saveSponsorAudit(id, (String) map.get("orderId"));//下单即自动发起审核
            return new ResponseData(200, "下单成功", null);
        } catch (Exception e) {
            log.error("{}", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseData(400, "公司所用节点不完善，无发下单", null);
        }
    }

    /**
     * 根据订单ID查询订单
     *
     * @param logSubject
     * @return
     */
    @Override
    public Order findByOrderId(String logSubject) {
        return orderMapper.findByOrderId(logSubject);
    }

    /**
     * 查询已审核未发货订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findByAudit(Map<String, Object> map, HttpServletRequest request) {
        String name = permissionUtil.findByShow(jwtUtil.getId(request.getHeader("token")));
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orderList = orderMapper.findByAudit(map);
        for (Order order : orderList) {
            order.setOrderSignNameStr(name);
        }
        PageInfo pageInfo = new PageInfo(orderList);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 查询合同订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findByOrder(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findIdByOrder(map);
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 查询待核算订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findByUnpaid(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findByUnpaid(map);
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }


    /**
     * 借样订单待归还明细
     *
     * @param pageSize
     * @param pageNum
     * @param request
     * @return
     */
    @Override
    public ResponseData findSample(int pageSize, int pageNum, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        List<Order> orders = new ArrayList<>();
        RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);//取出用户权限
        String permissionStr = (String) bucket1.get();
        boolean flag = false;
        if (permissionStr != null) {
            List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);//json转换成List
            for (Permission permission : permissions) {
                //循环判断用户是否有某个权限
                if (permission.getUrl().equals("returnRemind")) {
                    flag = true;
                }
            }
        }
        if (flag) {
            //有权限公司借样订单都会提醒
            //为true整个公司借样订单都会提醒
            Organization organization = organizationMapper.findByUId(id);
            PageHelper.startPage(pageNum, pageSize);
            orders = orderMapper.findByOitId(organization.getOrganizationId());//根据公司ID查询出该公司订单
        } else {
            //无权限根据用户ID查询
            PageHelper.startPage(pageNum, pageSize);
            orders = orderMapper.findByUserId(id);
        }
        String name = permissionUtil.findByShow(id);
        Iterator<Order> iterator = orders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            int i = BigDecimalUtil.calcJulianDays(order.getReturnTime(), df.parse(df.format(new Date())));
            //预计归还时间减当前时间大于规定时间移除该订单
            if (i > RemindTime) {
                iterator.remove();
                break;
            }
            List<Goods> goodsDetail = order.getGoodsDetail();
            for (Goods goods : goodsDetail) {
                //判断下单商品总数量减去归还数量+转销售数量数量是否小于1  小于1 全部商品已归还 移除订单
                if (goods.getGoodsNumber() - (goods.getSellNumber() + goods.getReturnNumber()) < 1) {
                    iterator.remove();
                    break;
                }
            }
            order.setOrderSignNameStr(name);
        }
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 查看直接付款未结算订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findOrder(Map<String, Object> map, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        String name = permissionUtil.findByShow(id);
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findOrder(map);
        for (Order order : orders) {
            order.setOrderSignNameStr(name);
        }
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 校验额度库存是否充足
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findInventory(Map<String, Object> map, HttpServletRequest request) {
        List<Map<String, Object>> goods = (List<Map<String, Object>>) map.get("goods");
        double div = 0;
        for (Map<String, Object> good : goods) {
            int count = productMapper.findByPId((String) good.get("goodsId"));
            if (count < (int) good.get("goodsNumber")) {
                return new ResponseData(400, "库存不足", null);
            }
        }
        try {
            int now = (int) map.get("now");
            div = BigDecimalUtil.div(now, 100, 2);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "金额输入有误", null);
        }
        if ((int) map.get("way") == 1) {
            String token = request.getHeader("token");
            String id = jwtUtil.getId(token);
            Organization organization = organizationMapper.findByUId(id);
            double now = BigDecimalUtil.sub(organization.getAvailableBalance() + "", div + "");
            if (now < 0) {
                return new ResponseData(400, "额度不足", null);
            }
        }
        return new ResponseData(200, "额度库存充足", null);
    }


    /**
     * 取消订单操作
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseData cancelOrder(String orderId, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Map<String, Object> map = new HashMap<>();
        String id = jwtUtil.getId(request.getHeader("token"));
        map.put("auditUser", id);
        map.put("auditLogTime", df.parse(df.format(new Date())));
        map.put("auditCondition", "订单取消");
        map.put("auditLogOrder", orderId);
        map.put("auditFlowName", "");
        Order order = orderMapper.findOrderId(orderId);
        if (order.getAudit() != 1) {
            return new ResponseData(400, "未审核通过，无法取消订单", null);
        }
        if (order.getExpressOnly() == 1) {
            return new ResponseData(400, "该订单已发货，无法取消订单", null);
        }
        if (order.getPaymentMethod() == 1) {
            //如果为额度下单 则判断是否结算，如已结算则 结算状态更改为退款中，如未结算，则回滚额度
            if (order.getPaymentStatus() == 1 || order.getPaymentStatus() == 8) {
                //如果为未结算 或部分结算 则回滚未结算额度
                //回滚额度为 订单实收金额+订单运费-已结算金额（未结算时应为0）
                map.put("now", BigDecimalUtil.sub(BigDecimalUtil.add(order.getActualMoney(), order.getFreight()), order.getSettlementAmount()));
                map.put("id", order.getAddCompany());
                organizationMapper.updateUsable(map);//回滚额度操作
                //更改审核为已取消订单 不更改结算状态
                orderMapper.updateAudit(orderId, 5);
            }
            if (order.getPaymentStatus() == 2 || order.getPaymentStatus() == 8) {
                //如果额度已结算，不回滚额度，更改审核状态为已取消，更改结算状态为退款中
                //或者为部分结算   更改订单状态为退款中
                orderMapper.updateAuditPayment(orderId, 5, 6);
            }
        }
        if (order.getPaymentMethod() == 2) {
            //如果为借样下单
            //更改审核状态为取消订单
            orderMapper.updateAudit(orderId, 5);
        }
        if (order.getPaymentMethod() == 3) {
            //如果未结算则只更改审核状态为订单取消
            orderMapper.updateAudit(orderId, 5);
            //等于3则为直接购买订单  如果已结算 则更改审核状态为已取消，更改结算状态为退款中
            if (order.getPaymentStatus() == 2 || order.getPaymentStatus() == 8) {
                orderMapper.updateAuditPayment(orderId, 5, 6);
            }
        }
        goodsService.updateWarehouse(order.getGoodsDetail());//回滚库存
        auditLogMapper.saveAuditLog(map);//插入审核记录
        return new ResponseData(200, "取消成功", null);
    }

    /**
     * 查询已取消的订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findCancelOrder(Map<String, Object> map, HttpServletRequest request) {
        String name = permissionUtil.findByShow(jwtUtil.getId(request.getHeader("token")));
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findCancelOrder(map);
        for (Order order : orders) {
            order.setOrderSignNameStr(name);
        }
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 查询本公司订单
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findCompanyOrder(Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        User user = userMapper.findById(id);
        String byShow = permissionUtil.findByShow(id);
        map.put("organizationId", user.getOrganizationId());//根据用户ID
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findCompanyOrder(map);
        for (Order order : orders) {
            order.setOrderSignNameStr(byShow);
        }
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 查询已发货订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findShipments(Map<String, Object> map, HttpServletRequest request) {
        String name = permissionUtil.findByShow(jwtUtil.getId(request.getHeader("token")));
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findShipments(map);
        for (Order order : orders) {
            order.setOrderSignNameStr(name);
        }
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 查询借样待归还订单
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findBorrow(Map<String, Object> map, HttpServletRequest request) {
        String name = permissionUtil.findByShow(jwtUtil.getId(request.getHeader("token")));
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Order> orders = orderMapper.findBorrow(map);
        for (Order order : orders) {
            order.setOrderSignNameStr(name);
        }
        PageInfo pageInfo = new PageInfo(orders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 订单退款操作
     *
     * @param orderIds 订单ID
     * @param request  获取操作人ID
     * @return 成功或失败
     */
    @Override
    public ResponseData refund(List<String> orderIds, HttpServletRequest request) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("clearingLogName", "订单退款");
        map.put("clearingLogUser", id);
        map.put("clearingLogTime", df.parse(df.format(new Date())));
        map.put("classify", 2);
        for (String orderId : orderIds) {
            Order order = orderMapper.findOrderId(orderId);
            if (order.getPaymentStatus() != 6) {
                return new ResponseData(400, "该订单未结算，无法退款", null);
            }
        }
        for (String orderId : orderIds) {
            map.put("clearingLogOrderId", orderId);
            orderMapper.updatePaymentStatus(orderId, 7, null);//更改订单付款状态为退款中
            clearingLogMapper.saveClearingLog(map);//调用日志Mapper 存入日志信息
        }
        return new ResponseData(200, "退款成功", null);
    }

    /**
     * 修改订单信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateOrder(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        Order order = orderMapper.findOrderId(map.get("orderId").toString());
        double actualDiv = 0;
        double freightDiv = 0;
        try {
            int actualMoney = (int) map.get("actualMoney");
            int freight = (int) map.get("freight");
            if (actualMoney < 0 || freight < 0) {
                return new ResponseData(400, "金额不可低于0", null);
            }
            actualDiv = BigDecimalUtil.div(map.get("actualMoney").toString(), "100", 2);
            map.put("actualMoney", actualDiv);//对实收金额重新赋值
            freightDiv = BigDecimalUtil.div(map.get("freight").toString(), "100", 2);
            map.put("freight", freightDiv);//对运费重新赋值
            double serveCost = BigDecimalUtil.div((int) map.get("serveCost"), 100, 2);
            map.put("serveCost", serveCost);//对服务费重新赋值
        } catch (Exception e) {
            return new ResponseData(400, "输入金额有误，只保留两位小数", null);
        }
        if (map.get("invoiceFlag").equals("1")) {
            if (map.get("invoiceType") == null || map.get("invoiceType") == "") {
                return new ResponseData(400, "发票类型不可为空", null);
            } else if (map.get("unitName") == null || map.get("unitName") == "" || map.get("flag") == null || map.get("flag") == "") {
                return new ResponseData(400, "发票抬头或抬头类型不可为空", null);
            }
        }
        if (map.get("receiverMobile") != null && map.get("unitPhone") != null && map.get("receiverMobile") != "" && map.get("unitPhone") != "") {
            if (!Pattern.compile(userPhone).matcher((String) map.get("receiverMobile")).matches() && Pattern.compile(userPhone).matcher((String) map.get("unitPhone")).matches()) {
                return new ResponseData(400, "手机号码格式错误，请重新输入", null);
            }
        }
        if (map.get("email") != null && map.get("email") != "") {
            //如果邮箱不为空验证邮箱
            if (!Pattern.compile(RULE_EMAIL).matcher((String) map.get("email")).matches()) {
                return new ResponseData(400, "邮箱格式错误，请重新输入", null);
            }
        }
        double now = BigDecimalUtil.add(actualDiv, freightDiv);
        double add = BigDecimalUtil.add(order.getActualMoney(), order.getFreight());
        map.put("clearingLogName", "修改订单信息");
        map.put("clearingLogUser", id);
        map.put("clearingLogTime", df.parse(df.format(new Date())));
        map.put("clearingLogOrderId", map.get("orderId"));
        map.put("classify", 1);
        if (order.getPaymentMethod() == 1 && order.getRetreatCargo() == 0) {
            if (order.getAudit() == 1 || order.getAudit() == 3) {
                //如果为额度订单  并且未退货 已审核通过或审核中  扣除或增加相应额度
                //如果为额度下单并且未付款 并且未退货 则修改金额时扣除相关额度
                if (order.getPaymentStatus() == 1 || order.getPaymentStatus() == 8) {
                    //如果是为结算 或部分结算 则去扣除或增加相应额度
                    if (!String.valueOf(now).equals(String.valueOf(add))) {
                        if (now < order.getSettlementAmount()) {
                            return new ResponseData(400, "修改金额不可小于已结算金额", null);
                        }
                        //判断 是否修改了金额
                        double sub = BigDecimalUtil.sub(String.valueOf(add), String.valueOf(now));
                        //根据公司ID更新  修改后的订单额度信息
                        organizationMapper.updateLimit(sub, order.getAddCompany());
                    }
                }
            }
        }
        map.put("site", map.get("receiverProvince").toString() + map.get("receiverCity").toString() + map.get("receiverDistrict").toString() + map.get("receiverStreet").toString() + map.get("receiverAddress").toString());
        if (map.get("invoiceFlag").equals("2")) {
            map.put("consignee", "姓名:" + map.get("receiverName").toString() + "，手机号:" + map.get("receiverMobile").toString() + "，地址:" + map.get("site").toString() + "，实收金额:" + map.get("actualMoney") + "，其他费用:" + map.get("freight") + "，订单备注:" + map.get("orderRemark") + "，申请人" + map.get("proposer") + "，钉钉单号" + map.get("dingNumber")
                    + "，发票抬头:" + "，纳税人识别号:" + "，地址、电话:" + "，开户行及账号 :" + "，邮箱:" + "，发票邮寄地址(专票必填):");
        } else {
            map.put("consignee", "姓名:" + map.get("receiverName").toString() + "，手机号:" + map.get("receiverMobile").toString() + "，地址:" + map.get("site").toString() + "，实收金额:" + map.get("actualMoney") + "，其他费用:" + map.get("freight") + "，订单备注:" + map.get("orderRemark") + "，申请人" + map.get("proposer") + "，钉钉单号" + map.get("dingNumber")
                    + "，发票抬头:" + map.get("unitName") + "，纳税人识别号:" + map.get("invoiceNumber") + "，地址、电话:" + map.get("unitSite") + map.get("unitPhone") + "，开户行及账号 :" + map.get("bankDeposit") + map.get("bankAccount") + "，邮箱:" + map.get("email") + "，发票邮寄地址(专票必填):" + map.get("ticketSite"));
        }
        List<Map<String, Object>> goodsDetail = (List<Map<String, Object>>) map.get("goodsDetail");
        double sumMoney = 0;
        for (Map<String, Object> goods : goodsDetail) {
            try {
                double price = productMapper.findByPrive(goods.get("goodsId").toString());
                double div = BigDecimalUtil.div((Integer) goods.get("goodsPrice"), 100, 2);//计算用户修改的价格
                goods.put("goodsPrice", div);
                double mul = BigDecimalUtil.mul(price, (int) goods.get("goodsNumber"));//计算出单个商品价格
                sumMoney = BigDecimalUtil.add(mul, sumMoney);//计算出订单总金额
                map.put("consignee", map.get("consignee") + "，商品编号:" + goods.get("goodsCore") + "，商品名字:" + goods.get("goodsName") + "，商品价格:" + div + "，商品数量:" + goods.get("goodsNumber"));
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, "输入金额有误，只保留两位小数", null);
            }
        }
        map.put("sumMoney", sumMoney);//订单总金额赋值
        goodsService.updateGoods(goodsDetail);
        //执行修改订单操作
        orderMapper.updateOrder(map);
        int countSite = userSiteMapper.findByCount(map);
        if (countSite < 1) {
            //如果不存在则插入，如果存在则忽略
            userSiteService.saveUserSite(map, request);//持久化用户地址
        }
        //执行修改发票信息操作
        int count = invoiceMapper.findByOrderId(map.get("orderId").toString());
        if (count > 0) {
            if (map.get("invoiceFlag").equals("2")) {
                //如果有发票信息传入不需要则删除发票信息
                invoiceMapper.deleteByOrderId(map.get("orderId").toString());
            } else {
                invoiceMapper.updateByOrderId(map);
                int invoiceCount = invoiceMapper.findCoun(map, id);
                if (invoiceCount < 1) {
                    invoiceMapper.saveinvoicerecord(map, id);
                }
            }
        } else {
            //如果没有发票信息传入需要发票信息 则新增发票信息
            if (map.get("invoiceFlag").equals("1")) {
                invoiceMapper.saveInvoiceMap(map);
                int invoiceCount = invoiceMapper.findCoun(map, id);
                if (invoiceCount < 1) {
                    invoiceMapper.saveinvoicerecord(map, id);
                }
            }
        }
        //执行订单日志插入操作
        clearingLogMapper.saveClearingLog(map);
        return new ResponseData(200, "修改成功", null);

    }

    /**
     * 根据ID查询订单信息
     *
     * @param orderId
     * @return
     */
    @Override
    public ResponseData findOrderId(String orderId, HttpServletRequest request) {
        Order order = orderMapper.findByOrder(orderId);
        order.setOrderSignNameStr(permissionUtil.findByShow(jwtUtil.getId(request.getHeader("token"))));
        return new ResponseData(200, "查询成功", order);
    }

    /**
     * 导入订单方法
     *
     * @param
     * @return
     */
    @Override
    public ResponseData leadOrder(List<LeadOrder> orders, String uId, String substring) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        try {
            for (LeadOrder order : orders) {
                order.setOrderId(MakeOrderNumUtil.makeOrderNum());//存入随机订单号
                orderMapper.addOrder(order);
                list.add(order.getOrderId());//把订单ID存入list
                if (order.getInvoice().getInvoiceFlag().equals("需要")) {
                    //等于1需要发票
                    invoiceMapper.saveInvoice(order.getInvoice(), order.getOrderId());
                    int count = invoiceMapper.findCount(order.getInvoice(), order.getAddUser());//查询发票记录是否已经插入
                    if (count < 1) {
                        //如果不存在并且为公司开具发票时 则插入
                        invoiceMapper.saveInvoiceRecord(order.getInvoice(), order.getAddUser());//存入发票记录
                    }
                }
                int count = userSiteMapper.findCount(order.getReceiverName(), order.getReceiverMobile(), order.getReceiverProvince(), order.getReceiverCity(), order.getReceiverDistrict(), order.getReceiverStreet(), order.getReceiverAddress(), order.getSite(), order.getAddUser());
                if (count < 1) {
                    //如果不存在则插入，如果存在则忽略
                    userSiteMapper.saveSite(order.getReceiverName(), order.getReceiverMobile(), order.getReceiverProvince(), order.getReceiverCity(), order.getReceiverDistrict(), order.getReceiverStreet(), order.getReceiverAddress(), order.getSite(), order.getAddUser());
                }
                goodsService.leadGoods(order.getOrderId(), order.getGoodsDetail());
                if (order.getExpressOnly() == 1) {
                    expressMapper.deliverGoods(order.getExpress(), order.getOrderId());//插入发货信息
                }
            }
            //存入日志信息
            map.put("clearingLogName", substring);
            map.put("clearingLogUser", uId);
            map.put("clearingLogTime", df.parse(df.format(new Date())));
            map.put("classify", 9);
            map.put("consignee", JSONUtils.toJSONString(list));
            clearingLogMapper.saveClearingLog(map);//插入日志
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "参数有误，导入失败", null);
        }
        return new ResponseData(200, "导入成功", null);
    }


    /**
     * 删除订单信息(支持多个订单一起删除)
     *
     * @param
     * @return
     */

    @Override
    public ResponseData deleteOrder(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        List<String> ordersIds = (List<String>) map.get("orderIds");
        //循环取出每个订单ID
        for (String orderId : ordersIds) {
            expressMapper.deleteOrderId(orderId);//根据ID删除快递信息
            goodsMapper.deleteOrderId(orderId);//根据ID删除商品信息
            goodsMapper.deleteByOrder_Goods(orderId);//根据订单ID删除中间表信息
            sponsorAuditMapper.deleteOrderId(orderId);//根据ID删除商品信息
            auditMapper.deleteOrderId(orderId);//根据ID删除用户审核记录
            invoiceMapper.deleteByOrderId(orderId);//根据订单ID删除发票信息
            orderMapper.deleteOrderId(orderId);//删除发票信息
        }
        map.put("clearingLogTime", df.parse(df.format(new Date())));
        map.put("clearingLogUser", id);
        map.put("clearingLogName", "本次共删除了" + ordersIds.size() + "条订单");
        map.put("classify", 8);
        clearingLogMapper.saveClearingLog(map);
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 删除导入的数据
     *
     * @param orderIds
     * @return
     */
    @Override
    public ResponseData deleteLeadOrder(String orderIds, String logId) {
        List<String> parse = (List<String>) JSONUtils.parse(orderIds);
        for (String orderId : parse) {
            expressMapper.deleteOrderId(orderId);//根据ID删除快递信息
            goodsMapper.deleteOrderId(orderId);//根据ID删除商品信息
            goodsMapper.deleteByOrder_Goods(orderId);//根据订单ID删除中间表信息
            sponsorAuditMapper.deleteOrderId(orderId);//根据ID删除商品信息
            auditMapper.deleteOrderId(orderId);//根据ID删除用户审核记录
            invoiceMapper.deleteByOrderId(orderId);//根据订单ID删除发票信息
            orderMapper.deleteOrderId(orderId);//删除发票信息
        }
        logMapper.deleteClearing(logId);//删除此条日志
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 服务费管理
     *
     * @param orderId
     * @param serveCost
     * @return
     */
    @Override
    public ResponseData updateCost(String orderId, int serveCost, HttpServletRequest request) {
        String id = jwtUtil.getId(request.getHeader("token"));
        Order order = orderMapper.findOrderId(orderId);
        if (!order.getAddUser().equals(id)) {
            return new ResponseData(400, "不可修改他人订单", null);
        }
        try {
            double serveCostMoney = BigDecimalUtil.div(serveCost, 100, 2);
            orderMapper.updateCost(orderId, serveCostMoney);//修改
        } catch (Exception e) {
            return new ResponseData(400, "金额输入有误", null);
        }

        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 借样转销售订单  (可转多次)
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData turnSell(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        Order order = orderMapper.findOrderId((String) map.get("orderId"));
        if (order.getPaymentMethod() != 2) {
            return new ResponseData(400, "只可选择借样订单", null);
        }
        String now = "0";
        double sumMoney = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        map.put("addUser", order.getAddUser());//存入订单创建人
        Organization organization = organizationMapper.findByUId(id);
        //从Redis中取出用户权限
        RBucket<Object> clientBucket = client.getBucket("data_ybnetwork.permission" + id);
        String permissionStr = (String) clientBucket.get();
        List<Permission> permissions = JsonUtil.jsonToList(permissionStr, Permission.class);
        boolean flag = false;
        map.put("clearingLogName", "借样订单转销售");
        map.put("clearingLogUser", id);
        map.put("clearingLogTime", df.parse(df.format(new Date())));
        map.put("clearingLogOrderId", map.get("orderId"));
        map.put("classify", 10);
        for (Permission permission : permissions) {
            //判断用户是否有修改商品价格的权限
            if (permission.getUrl().equals("updatePrice")) {
                flag = true;
            }
        }
        String paymentMethod = (String) map.get("paymentMethod");
        if (!organization.getWay().contains(paymentMethod + "")) {
            return new ResponseData(400, "您所属公司不支持此种方式下单", null);
        }
        map.put("addCompany", organization.getOrganizationId());//存入订单所属人公司ID
        map.put("orderId", MakeOrderNumUtil.makeOrderNum());//存入随机订单号
        map.put("organizationId", organization.getOrganizationId());//存入公司ID
        map.put("paymentStatus", 1);//赋值付款状态
        map.put("audit", 1);//赋值审核状态为审核通过

        // 拼接收货地址
        map.put("site", map.get("receiverProvince").toString() +
                map.get("receiverCity").toString() +
                map.get("receiverDistrict").toString() +
                map.get("receiverStreet").toString() +
                map.get("receiverAddress").toString());
        List<Map<String, Object>> goodsDetail = (List<Map<String, Object>>) map.get("goodsDetail");
        if (goodsDetail == null || goodsDetail.size() < 1) {
            //如果下单商品列表无数据返回错误
            return new ResponseData(400, "须选择商品转为销售订单", null);
        }
        try {
            int serveCost = (int) map.get("serveCost");
            double div = BigDecimalUtil.div(serveCost, 100, 2);
            map.put("serveCost", div);//存入服务费
        } catch (Exception e) {
            return new ResponseData(400, "服务费金额输入错误", null);
        }
        try {
            map.put("orderTime", df.parse(df.format(new Date())));//存入订单创建时间
            double mul = 0;
            double sumMoneySingle = 0;
            for (int i = 0; i < goodsDetail.size(); i++) {
                Map<String, Object> goodsMap = goodsDetail.get(i);
                int goodsNumber = (int) goodsMap.get("goodsNumber");
                if (goodsNumber < 1) {
                    return new ResponseData(400, "选择商品数量须大于0", null);
                }
                double goodsPrice = BigDecimalUtil.div(goodsMap.get("goodsPrice").toString(), "100", 2);
                map.put("consignee", map.get("consignee") + "商品编号:" + goodsMap.get("goodsCore") + "，商品名字:" + goodsMap.get("goodsName") + "，商品价格:" + goodsPrice + "，转为销售订单商品数量:" + goodsMap.get("goodsNumber") + "，");
                goodsMap.put("goodsPrice", goodsPrice);
                if (goodsPrice < 0) {
                    return new ResponseData(400, "商品金额不可低于0", null);
                }
                if (flag) {
                    //有修改价格的权限  使用用户输入的价格
                    double price = productMapper.findByPrive(goodsMap.get("goodsId").toString());
                    sumMoneySingle = BigDecimalUtil.mul(String.valueOf(price), goodsMap.get("goodsNumber").toString());//计算商品总金额
                    mul = BigDecimalUtil.mul(String.valueOf(goodsPrice), goodsMap.get("goodsNumber").toString());
                } else {
                    //无修改价格的权限  根据商品价格查找商品价格
                    double price = productMapper.findByPrive(goodsMap.get("goodsId").toString());
                    mul = BigDecimalUtil.mul(String.valueOf(price), goodsMap.get("goodsNumber").toString());
                    sumMoneySingle = mul;//赋值给商品总金额
                }
                sumMoney = BigDecimalUtil.add(sumMoneySingle, sumMoney);
                now = BigDecimalUtil.add(mul + "", now) + "";
            }
            map.put("sumMoney", sumMoney);//添加商品总金额
            map.put("now", now);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "下单数量或金额输入有误", null);
        }
        if ((int) map.get("expressOnly") == 1) {
            //如果该订单已发货，则转换的订单也为已发货
            map.put("expressOnly", 1);
            //存入发货单号
            if (paymentMethod.equals("1")) {
                //等于1为额度下单
                int i = organizationMapper.subLimit(map);//更改额度
                if (i < 1) {
                    //小于1额度不足直接返回
                    return new ResponseData(400, "额度不足", null);
                }
            }
            //存入发货单号
            expressMapper.saveExpress(df.format(new Date()), (String) map.get("orderId"), (String) map.get("expressName"), (String) map.get("expressNumbers"), null);
        } else {
            //如果未发货则根据不同下单方式赋值
            if (paymentMethod.equals("1")) {
                //等于1为额度下单
                map.put("expressOnly", 0);
                int i = organizationMapper.subLimit(map);//更改额度
                if (i < 1) {
                    //小于1额度不足直接返回
                    return new ResponseData(400, "额度不足", null);
                }
            } else {
                //如果未直接购买 发货状态赋值未2
                map.put("expressOnly", 2);
            }
        }
        int count = userSiteMapper.findByCount(map);
        if (count < 1) {
            //如果不存在则插入，如果存在则忽略
            userSiteService.saveUserSite(map, request);//持久化用户地址
        }
        int goodCount = goodsService.updateSellNumber((List<Map<String, Object>>) map.get("goodsDetail"));//更改原有订单商品转销售数量
        if (goodCount == 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseData(400, "选择商品数量与已归还数量不可超过商品总数量", null);
        }
        //插入商品和中间表信息
        goodsService.addGoods(goodsDetail, (String) map.get("orderId"));
        if ((map.get("invoiceFlag") != null && map.get("invoiceFlag").toString().equals("1"))) {
            Invoice invoice = JSON.parseObject(JSON.toJSONString(map.get("invoice")), Invoice.class);
            if (invoice.getInvoiceType() == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, "发票类型不可为空", null);
            } else if (invoice.getUnitName() == null || invoice.getUnitName() == "" || invoice.getFlag() == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, "发票抬头或抬头类型不可为空", null);
            }
            //如果invoiceFlag为2为需要上传发票存入发票信息
            Map<String, Object> InvoiceMap = invoiceService.saveInvoice(invoice, map.get("orderId").toString(), id);//调用插入方法插入数据库
            if ((int) InvoiceMap.get("flag") == 1) {
                //如果flag为1数据有问题，回滚数据，提示错误
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, InvoiceMap.get("name").toString(), null);
            }
        }
        //执行订单日志插入操作
        clearingLogMapper.saveClearingLog(map);
        clearingLogMapper.saveLog(map);
        try {
            orderMapper.saveOrder(map);//插入订单
            return new ResponseData(200, "转销售订单成功", null);
        } catch (Exception e) {
            log.error("{}", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseData(400, "转销售订单失败", null);
        }

    }

    /**
     * 查询申请人金额统计
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData proposerMoney(Map<String, Object> map) {
        String proposer = (String) map.get("proposer");
        if (proposer == null) {
            return new ResponseData(400, "申请人不可为空", null);
        }
        PageHelper.startPage((int)map.get("pageNum"),(int)map.get("pageSize"));
        List<Order> orderList = orderMapper.proposerMoneyPaging(map);
        List<Order> orders = orderMapper.proposerMoney(map);
        Map<String, Object> returnMap = new HashMap<>();
        double aggregate = 0;
        double refundMoney = 0;
        double sellMoney = 0;
        double orderAggregate = 0;
        double orderRefundMoney = 0;
        double orderSellMoney = 0;
        if (orders != null && orders.size() > 0) {
            for (Order order : orders) {
                List<Goods> goodsDetail = order.getGoodsDetail();
                for (Goods goods : goodsDetail) {
                    //计算出单个商品-退款数量-转销售订单数量后的金额
                    double mul = BigDecimalUtil.mul(goods.getGoodsPrice(), (goods.getGoodsNumber() - goods.getReturnNumber() - goods.getSellNumber()));//单个商品
                    double mul1 = BigDecimalUtil.mul(goods.getGoodsPrice(), goods.getReturnNumber());//计算出归还商品价格
                    double mul2 = BigDecimalUtil.mul(goods.getGoodsPrice(), goods.getSellNumber());//计算出转销售订单价格
                    aggregate = BigDecimalUtil.add(aggregate, mul);//计算订单-退款转销售订单后剩余金额
                    refundMoney = BigDecimalUtil.add(refundMoney, mul1);//计算退款总金额
                    sellMoney = BigDecimalUtil.add(sellMoney, mul2);//计算出转销售订单总金额
                }
                order.setActualMoney(aggregate);
                aggregate = 0;
                order.setSettlementAmount(sellMoney);
                sellMoney = 0;
                order.setRefundAmount(refundMoney);
                refundMoney = 0;
                orderAggregate = BigDecimalUtil.add(orderAggregate, order.getActualMoney());
                orderRefundMoney = BigDecimalUtil.add(orderRefundMoney, order.getRefundAmount());
                orderSellMoney = BigDecimalUtil.add(orderSellMoney, order.getSettlementAmount());
            }
            for (Order order : orderList) {
                List<Goods> goodsDetail = order.getGoodsDetail();
                for (Goods goods : goodsDetail) {
                    //计算出单个商品-退款数量-转销售订单数量后的金额
                    double mul = BigDecimalUtil.mul(goods.getGoodsPrice(), (goods.getGoodsNumber() - goods.getReturnNumber() - goods.getSellNumber()));//单个商品
                    double mul1 = BigDecimalUtil.mul(goods.getGoodsPrice(), goods.getReturnNumber());//计算出归还商品价格
                    double mul2 = BigDecimalUtil.mul(goods.getGoodsPrice(), goods.getSellNumber());//计算出转销售订单价格
                    aggregate = BigDecimalUtil.add(aggregate, mul);//计算订单-退款转销售订单后剩余金额
                    refundMoney = BigDecimalUtil.add(refundMoney, mul1);//计算退款总金额
                    sellMoney = BigDecimalUtil.add(sellMoney, mul2);//计算出转销售订单总金额
                }
                order.setActualMoney(aggregate);
                aggregate = 0;
                order.setSettlementAmount(sellMoney);
                sellMoney = 0;
                order.setRefundAmount(refundMoney);
                refundMoney = 0;
            }
        } else {
            return new ResponseData(400, "未查询到订单", null);
        }
        PageInfo pageInfo=new PageInfo(orderList);
        returnMap.put("orders", pageInfo);
        returnMap.put("orderAggregate", orderAggregate);
        returnMap.put("orderRefundMoney", orderRefundMoney);
        returnMap.put("orderSellMoney", orderSellMoney);
        return new ResponseData(200, "查询成功", returnMap);
    }

}


