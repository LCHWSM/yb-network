package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Money;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.domain.Organization;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.GoodsService;
import com.ybau.transaction.service.LogService;
import com.ybau.transaction.service.OrganizationService;
import com.ybau.transaction.service.UserService;
import com.ybau.transaction.util.BigDecimalUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ExpressMapper expressMapper;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ClearingLogMapper clearingLogMapper;

    @Autowired
    LogMapper logMapper;

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    GroupingMapper groupingMapper;

    /**
     * 查询所有公司机构（分页）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        int pageNum = (int) map.get("pageNum");
        int pageSize = (int) map.get("pageSize");
        PageHelper.startPage(pageNum, pageSize);
        List<Organization> organizations = organizationMapper.findAll(map);
        PageInfo pageInfo = new PageInfo(organizations);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据用户ID查询公司信息
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findByUId(HttpServletRequest request) {
        String token = request.getHeader("token");//获取token
        String id = jwtUtil.getId(token);//获取用户ID
        Organization organization = organizationMapper.findByUId(id);
        return new ResponseData(200, "查询成功", organization);
    }

    /**
     * 新增公司机构
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveOrganization(Map<String, Object> map, HttpServletRequest request) {
        try {
            int settingTime = (int) map.get("settingTime");
            if (settingTime < 1) {
                return new ResponseData(400, "归还天数不可为负数", null);
            }
        } catch (Exception e) {
            log.error("类型转换异常 {}", e);
            return new ResponseData(400, "归还天数只可为整数", null);
        }
        Double organizationLimit = Double.valueOf(map.get("organizationLimit").toString());
        if (organizationLimit < 0) {
            return new ResponseData(400, "公司额度不可为负数", null);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("addUser", id);
        try {
            map.put("addTime", df.parse(df.format(new Date())));
        } catch (Exception e) {
            log.error("转换时间异常" + e);
        }
        //判断公司名字是否重复
        int count = organizationMapper.findByName((String) map.get("organizationName"));
        if (count < 1) {
            organizationMapper.saveOrganization(map);
            return new ResponseData(200, "新增成功", null);
        }
        return new ResponseData(400, "该公司已存在，新增失败", null);
    }

    /**
     * 删除公司
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData deleteById(int id, String name) {
        int count = userService.findByOrganizationId(id);//查询该公司是否有用户
        int flag = orderMapper.findByOName(id);//根据公司名字判断该公司下是否有订单
        int countTment = departmentMapper.findByCount(id);
        if (flag > 0) {
            return new ResponseData(400, "该公司下存在订单，无法删除", null);
        }
        if (countTment > 0) {
            return new ResponseData(400, "该公司存在下属部门，无法删除", null);
        }
        if (count < 1) {
            //小于1无用户执行删除操作
            organizationMapper.deleteById(id);
            return new ResponseData(200, "删除成功", null);
        }
        //大于1存在用户，返回提示信息
        return new ResponseData(400, "该公司存在下属账户，无法删除", null);
    }

    /**
     * 更新公司
     *
     * @param name
     * @return
     */
    @Override
    public ResponseData updateOrganization(String name, HttpServletRequest request, int id, Integer settingTime, String way) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (settingTime < 1) {
                return new ResponseData(400, "归还天数只可为正整数", null);
            }
        } catch (Exception e) {
            log.error("类型转换异常{}", e);
            return new ResponseData(400, "归还天数只可为正整数", null);
        }
        Date updateTime = null;
        String token = request.getHeader("token");
        String userId = jwtUtil.getId(token);//根据token获取用户名
        try {
            updateTime = df.parse(df.format(new Date()));
        } catch (Exception e) {
            log.error("时间转换异常{}", e);
        }
        //判断公司名字是否重复
        Organization organization = organizationMapper.findByOId(id);//根据ID查询公司
        if (organization.getOrganizationName().equals(name)) {
            organizationMapper.updateOrganization(name, userId, updateTime, id, settingTime, way);
            return new ResponseData(200, "修改成功", null);
        }
        int count = organizationMapper.findByName(name);
        if (count < 1) {
            organizationMapper.updateOrganization(name, userId, updateTime, id, settingTime, way);
            return new ResponseData(200, "修改成功", null);
        }
        return new ResponseData(400, "该公司已存在，修改失败", null);
    }

    /**
     * 查询所有公司（未分页）
     *
     * @return
     */
    @Override
    public ResponseData findOrganization() {
        List<Organization> organizations = organizationMapper.findOrganization();
        return new ResponseData(200, "查询成功", organizations);
    }

    /**
     * 更新额度
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateLimit(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);//根据Token获取操作人用户名
        int flag = (int) map.get("flag");
        String now = (String) map.get("now");
        if (now.indexOf(".") < 1) {
            now = now + ".00";
        }
        int length = now.substring(now.indexOf(".") + 1).length();
        if (length > 2) {
            return new ResponseData(200, "额度只可以精确到分", null);
        }
        map.put("logSubject", map.get("organizationName"));
        try {
            map.put("logTime", df.parse(df.format(new Date())));
            if (Double.valueOf(now) < 1) {
                //增加额度需大于0否则返回
                return new ResponseData(400, "调整的额度需大于0", null);
            }
        } catch (Exception e) {
            log.error("时间转换异常");
            return new ResponseData(400, "传入参数有误", null);
        }
        if (1 == flag) {
            //等于1增加额度
            organizationMapper.updateAddLimit(map);
            //增加操作日志
            map.put("logName", "+" + map.get("now"));
            map.put("logUser", id);
            logService.saveProductLog(map);//调用日志service
            return new ResponseData(200, "增加额度成功", null);
        } else {
            //否则降低额度
            organizationMapper.updateSubLimit(map);
            map.put("logName", "-" + map.get("now"));
            map.put("logUser", id);
            logService.saveProductLog(map);//调用日志service
            return new ResponseData(200, "减少额度成功", null);
        }

    }


    /**
     * 额度核销
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateUsable(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        String id = jwtUtil.getId(request.getHeader("token"));//根据Token获取操作人用户名
        String orderId = (String) map.get("orderId");
        map.put("clearingLogUser", id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("clearingLogTime", df.parse(df.format(new Date())));
        map.put("clearingRemark", map.get("logRemarks"));
        map.put("classify", 2);
        int now = 0;
        double div = 0;
        Order order = orderMapper.findByOrderId(orderId);
        if (order.getPaymentMethod() != 1) {
            return new ResponseData(400, "该订单不是额度购买订单，无法结算", null);
        }
        if (order.getAudit() != 1) {
            return new ResponseData(400, "该订单还未审核通过，结算失败", null);
        }
        if (order.getPaymentStatus() != 1 && order.getPaymentStatus() != 8) {
            return new ResponseData(400, "需选择可核销订单", null);
        }
        try {
            now = (int) map.get("now");
            //把分转为元
            div = BigDecimalUtil.div(String.valueOf(now), "100", 2);
            map.put("clearingLogName", "本次核销了：" + div + "元");
        } catch (Exception e) {
            return new ResponseData(400, "输入金额有误", null);
        }
        if (div < 1) {
            //如果结算金额小于1
            return new ResponseData(400, "结算金额需大于0", null);
        }
        //计算订单总金额
        double add = BigDecimalUtil.add(order.getActualMoney(), order.getFreight());
        //本次核销+历史核销+已退款金额不可大于订单总金额
        double add1 = BigDecimalUtil.add(div, order.getSettlementAmount());
        double add2 = BigDecimalUtil.add(BigDecimalUtil.add(div, order.getSettlementAmount()), order.getRefundAmount());
        if (add < add2) {
            return new ResponseData(400, "核销金额加退款金额不可大于订单总金额", null);
        }
        map.put("now", div);//本次核销金额存入map
        map.put("id", order.getAddCompany());//存入此订单公司ID
        int i = organizationMapper.updateUsable(map);//判断是否核销成功
        if (i < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseData(400, "核销金额不可大于已用金额", null);
        }
        if (BigDecimalUtil.sub(add, add2) == 0) {
            //如果此次结算已全部结算金额
            orderMapper.updatePaymentStatus(orderId, 2, add1);//更改付款状态
        } else {
            //否则为部分结算订单
            orderMapper.updatePaymentStatus(orderId, 8, add1);
        }
        map.put("clearingLogOrderId", orderId);
        clearingLogMapper.saveClearingLog(map);
        return new ResponseData(200, "核销成功", null);
    }


    /**
     * 校验额度金额
     *
     * @return
     */
    @Override
    public ResponseData verifyLimit(int organizationId) {
        String sum = "0";
        //查询除公司额度下单未结算订单总金额
        List<Money> monies = orderMapper.findBySumMoney(organizationId);
        Organization organization = organizationMapper.findByOId(organizationId);//根据公司ID查询公司信息
        if (monies != null && monies.size() > 0) {
            for (Money money : monies) {
                //实际金额+运费等于订单实收金额
                //实收金额-已结算金额-退款金额 等于须恢复金额
                double add1 = BigDecimalUtil.add(money.getActualMoney(), money.getFreight());
                double add2 = BigDecimalUtil.add(money.getSettlementAmount(), +money.getRefundAmount());
                if (add1 > add2) {
                    //如果退款金额+已结算金额大于或等于订单总金额则进行计算核对
                    double add = BigDecimalUtil.sub(add1, add2);
                    sum = BigDecimalUtil.add(add + "", sum) + "";
                }
            }
        }
        if (sum.equals(organization.getUseBalance() + "")) {
            return new ResponseData(200, "校正成功", null);
        }
        double sub = BigDecimalUtil.sub(sum, organization.getUseBalance() + "");//计算差额数值
        organizationMapper.verifyLimit(sub, organizationId);//校验额度
        return new ResponseData(200, "校正成功", null);
    }

    /**
     * 直接购买 结算接口
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData paymentStatus(Map<String, Object> map, HttpServletRequest request) throws ParseException, IllegalAccessException {
        String id = jwtUtil.getId(request.getHeader("token"));
        String orderId = map.get("orderId").toString();
        map.put("clearingLogUser", id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("clearingLogTime", df.parse(df.format(new Date())));
        map.put("classify", 2);
        Order order = orderMapper.findByOrderId(orderId);
        if (order.getPaymentMethod() != 3) {
            return new ResponseData(400, "订单中存在非直接购买订单，无法结算", null);
        }
        if (order.getAudit() != 1) {
            return new ResponseData(400, "订单中存在还未审核通过订单，结算失败", null);
        }
        if (order.getPaymentStatus() != 1 && order.getPaymentStatus() != 3 && order.getPaymentStatus() != 8) {
            return new ResponseData(400, "需选择可结算订单结算", null);
        }
        try {
            int now = (int) map.get("now");
        } catch (Exception e) {
            return new ResponseData(400, "输入金额有误", null);
        }
        double div = BigDecimalUtil.div(map.get("now").toString(), "100", 2);//计算结算金额
        if (div < 0) {
            return new ResponseData(400, "结算金额不可为负数", null);
        }
        if (!map.get("urgency").equals("1") && div == 0) {
            return new ResponseData(400, "未进行操作", null);
        }
        if (map.get("urgency").equals("1") && div == 0) {
            if (order.getExpressOnly() == 0 || order.getExpressOnly() == 1) {
                return new ResponseData(400, "未进行操作", null);
            }
        }
        double add = BigDecimalUtil.add(order.getActualMoney(), order.getFreight());//计算出订单总金额
        //本次结算金额+历史结算金额+已退款金额不可大于订单总金额
        double add1 = BigDecimalUtil.add(div, order.getSettlementAmount());
        double add2 = BigDecimalUtil.add(order.getRefundAmount(), add1);//计算退款金额+历史结算金额+本次结算金额
        double sub = BigDecimalUtil.sub(add, add2);
        if (sub < 0) {
            //如果订单总金额小于结算金额则返回错误
            return new ResponseData(400, "结算金额加退款金额不可大于订单总金额", null);
        }
        int urgency = Integer.parseInt((String) map.get("urgency"));
        String str = urgency == 1 ? "是" : "否";
        map.put("clearingLogName", "本次结算了：" + div + "元" + "（是否可发货：" + str + ")");
        map.put("clearingLogOrderId", orderId);
        if (order.getExpressOnly() == 1 || order.getExpressOnly() == 0) {
            //如果已发货或者为可发货状态 更改付款状态为已结算  更改已结算金额
            if (sub == 0) {
                orderMapper.updatePaymentStatus(orderId, 2, add1);//更改付款状态
            } else {
                //如果已发货 更改付款状态为已结算  更改已结算金额
                orderMapper.updatePaymentStatus(orderId, 8, add1);//更改付款状态
            }
        } else {
            //如果还未发货或还不为可发货状态
            if (div != 0) {
                //结算了金额在进行下一步判断
                if (sub != 0) {
                    //如果还未发货 并且部分结算金额 有结算金额 判断该订单是否设置成为可发货状态
                    if (map.get("urgency").equals("1")) {
                        //如果本次结算设置为可发货  则该订单为可发货
                        orderMapper.updatePaymentByExpress(orderId, 8, 0, add1);//更改直接购买订单付款状态和发货信息
                    } else {
                        //如果为设置未设置可发货 则此订单 虽结算 但还不可发货
                        orderMapper.updatePaymentByExpress(orderId, 8, 2, add1);//更改直接购买订单付款状态和发货信息
                    }
                } else {
                    //如果还未发货 并且全部结算金额 则订单设置为可以发货状态 更改付款状态为已结算
                    orderMapper.updatePaymentByExpress(orderId, 2, 0, add1);//更改直接购买订单付款状态和发货信息
                }
            }
            if (map.get("urgency").equals("1") && div == 0 && order.getSettlementAmount() == 0) {
                //如果只设置了可发货  并且未结算金额 则更改订单为 未结算/可发货
                map.put("clearingLogName", "设置订单为可发货");
                orderMapper.updatePaymentByExpress(orderId, 3, 0, null);//更改直接购买订单付款状态和发货信息
                map.put("clearingLogOrderId", orderId);
            }
            if (map.get("urgency").equals("1") && div == 0 && order.getSettlementAmount() != 0) {
                //如果该订单已经结算 则只更改发货状态
                orderMapper.updatePaymentByExpress(orderId, 8, 0, null);//更改直接购买订单付款状态和发货信息
            }
        }
        clearingLogMapper.saveClearingLog(map);//存入订单结算日志
        return new ResponseData(200, "结算成功", null);

    }

    /**
     * 设定借样订单归还时间
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData borrowSample(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("logUser", id);
        map.put("logTime", sdf.parse(sdf.format(new Date())));
        map.put("logSubject", map.get("orderId"));
        map.put("logClassify", 7);
        map.put("logRemarks", "已归还（借样）");
        map.put("logName", "实际归还时间：" + map.get("practicalTime"));
        Order order = orderMapper.findOrderId(map.get("orderId").toString());
        if (order.getPaymentMethod() != 2) {
            return new ResponseData(400, "订单中存在非借样订单，无法归还", null);
        }
        if (order.getPaymentStatus() != 4) {
            return new ResponseData(400, "订单无法重复归还", null);
        }
        if (order.getExpressOnly() == 0) {
            //未发货无法选择归还时间
            return new ResponseData(400, "订单未发货，无法归还", null);
        }
        int i = BigDecimalUtil.calcJulianDays(order.getOrderTime(), df.parse((String) map.get("practicalTime")));
        if (i > 0) {
            return new ResponseData(400, "归还时间不可在下单时间之前", null);
        }
        int practicalTime = BigDecimalUtil.calcJulianDays(df.parse((String) map.get("practicalTime")), df.parse(df.format(new Date())));
        if (practicalTime > 0) {
            return new ResponseData(400, "归还时间不可在当前时间之后", null);
        }
        orderMapper.updateRetreatCargo(order.getOrderId(), 2);
        logMapper.saveProductLog(map);//录入归还日志信息
        orderMapper.updatePaymentStatus(order.getOrderId(), 5, null);
        expressMapper.updateTime(df.parse((String) map.get("practicalTime")), order.getOrderId());//存入实际归还时间
        goodsService.updateWarehouse(order.getGoodsDetail());//回滚库存
        return new ResponseData(200, "归还成功", null);
    }

    /**
     * 查询公司额度  分页  （筛选）
     *
     * @param organizationName (公司名字筛选 条件)
     * @param pageNum          （分页）
     * @param pageSize         （分页）
     * @return
     */
    @Override
    public ResponseData findLimit(String organizationName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Organization> organizations = organizationMapper.findLimit(organizationName);
        PageInfo pageInfo = new PageInfo(organizations);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 公司关联可下单的供应商
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData saveSupplier(Map<String, Object> map) {
        List<Integer> supplierIds = (List<Integer>) map.get("supplierIds");
        Integer organizationId = (Integer) map.get("organizationId");
        organizationMapper.deleteSupplier(organizationId);//插入前删除之前数据
        if (supplierIds != null && organizationId != null) {
            for (Integer supplierId : supplierIds) {
                organizationMapper.saveSupplier(supplierId, organizationId);
            }
            return new ResponseData(200, "配置成功", null);
        }
        return new ResponseData(400, "配置失败", null);
    }
}