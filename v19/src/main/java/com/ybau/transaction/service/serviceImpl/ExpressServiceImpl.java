package com.ybau.transaction.service.serviceImpl;

import com.ybau.transaction.domain.Company;
import com.ybau.transaction.domain.Goods;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.*;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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


/**
 * 快递service
 */
@Service
@Transactional
@Slf4j
public class ExpressServiceImpl implements ExpressService {


    @Autowired
    private ExpressMapper expressMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    private Environment environment;

    @Autowired
    private RestURLUtil restURLUtil;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    LogService logService;

//    //配置访问路径
//    @Value("${yb-express-url}")
//    private String url;

    @Autowired
    LogMapper logMapper;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    GoodsMapper goodsMapper;


    /**
     * 查询所有快递公司信息
     *
     * @return
     */

    @Override
    public List<Company> findAll() {
        return expressMapper.findAll();
    }


    /**
     * 调用订单快递数据
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData saveExpress(Map<String, Object> map) {
        Map<String, Object> map1 = new HashMap<>();
        String authCode = (String) map.get("authCode");
        ResponseData result = new ResponseData();
        if (map != null && map.size() > 0) {
            //验证acthCode是否正确 不正确返回错误码
            if (authCode.equals(environment.getProperty("order.authCode"))) {
                //查询是否是要更改快递数据
                int count = expressMapper.findCounttest((String) map.get("orderId"));
                //大于0有数据修改 小于0无数据新增
                if (count > 0) {
                    try {
                        //插入成功返回SUC其他返回FAIL
                        int i = expressMapper.updateExpresstest(map);
                        if (i > 0) {
                            map1.put("orderId", map.get("orderId"));
                            map1.put("status", "SUC");
                            result.setCode(200);
                            result.setData(map1);
                            return result;
                        }
                        result.setCode(406);
                        map1.put("orderId", map.get("orderId"));
                        map1.put("status", "FAIL");
                        return result;

                    } catch (Exception e) {
                        log.error("{}", e);
                        result.setCode(406);
                        map1.put("orderId", map.get("orderId"));
                        map1.put("status", "FAIL");
                        result.setData(map1);
                        return result;
                    }
                }
                //无该订单号，新发货订单成功返回SUC其他返回FAIL
                try {
                    int i = expressMapper.saveExpresstest(map);
                    if (i > 0) {
                        map1.put("orderId", map.get("orderId"));
                        map1.put("status", "SUC");
                        result.setCode(200);
                        result.setData(map1);
                        return result;
                    }
                    result.setCode(406);
                    map1.put("orderId", map.get("orderId"));
                    map1.put("status", "FAIL");
                    return result;
                } catch (Exception e) {
                    log.error("{}", e);
                    result.setCode(406);
                    map1.put("orderId", map.get("orderId"));
                    map1.put("status", "FAIL");
                    result.setData(map1);
                    return result;
                }
            }
            return new ResponseData(400, "authCode验证错误，请传入正确的authCode", null);
        }
        return new ResponseData(400, "请传入参数", null);
    }


    /**
     * 发货按钮
     *
     * @param map
     * @param request
     * @return
     * @throws ParseException
     */
    @Override
    public ResponseData saveByExpress(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        Order order = orderMapper.findByOrderId((String) map.get("orderId"));//根据订单ID查询订单信息
        if (order.getExpressOnly() == 1) {
            //已发货订单不可进行二次发货
            return new ResponseData(400, "该订单已发货，不可进行二次发货", null);
        }
        if (order.getPaymentMethod() != 0) {
            //不等于0为本系统下单
            if (order.getPaymentMethod() == 2 || order.getPaymentMethod() == 1 && order.getAudit() == 1) {
                //为借样 额度 下单 审核通过即可发货
                return addExpress(map, request);
            }
            if (order.getPaymentMethod() == 3 && order.getAudit() == 1 && order.getPaymentStatus() == 3 || order.getPaymentStatus() == 2 || order.getPaymentStatus() == 8) {
                //为直接购买，审核通过，或紧急发货或部分结算状态即可发货
                return addExpress(map, request);
            }
            return new ResponseData(400, "该订单审核未通过或未结算，发货失败", null);
        }
        return new ResponseData(400,"发货失败",null);
//        else {
////            //等于0为拉取订单 需调用粤宝网络接口
////            //添加authCode
////            map.put("authCode", environment.getProperty("order.authCode"));
////            //调用工具类发送请求接收参数
////            JSONObject jsonObject = restURLUtil.doPost(JSONObject.fromObject(map), url);
////            if (jsonObject != null) {
////                //把Json数据转换成MAP
////                Map<String, Object> mapAll = jsonUtil.parseJSON2Map(jsonObject);
////                //取出返回数据
////                Map<String, Object> data = (Map<String, Object>) mapAll.get("data");
////                if (data != null && data.size() > 0) {
////                    String status = (String) data.get("status");
////                    //判断是否等于SUC， 等于SUC证明成功
////                    if (status.equals("SUC")) {
////                        //成功调用发货接口
////                        return addExpress(map, request);
////                    }
////                }
////            }
////            return new ResponseData(400, "发货失败，请稍后再试", null);
//        }
    }

    /**
     * 修改快递单号
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateExpress(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("logUser", id);//存入操作人
        map.put("logSubject", map.get("orderId"));//存入操作订单
        map.put("logTime", df.parse(df.format(new Date())));//存入操作时间
        map.put("logName", map.get("expressName") + " " + map.get("expressNumber"));
        map.put("logRemarks", "修改发货信息");
        expressMapper.updateExpress(map);
        logService.saveProductLog(map);//调用日志service
        return new ResponseData(200, "修改发货信息", null);
    }

    /**
     * 修改退货信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateRefundCargo(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        Order order = orderMapper.findOrderId(map.get("orderId").toString());
        if (order.getRetreatCargo() == 0) {
            return new ResponseData(400, "该订单未填写退货信息，无法修改退货信息", null);
        }
        map.put("logName", map.get("retreatName") + " " + map.get("retreatNumbers"));
        map.put("logUser", id);
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("logRemarks", "修改退货信息");
        map.put("logSubject", map.get("orderId"));
        map.put("logClassify", 7);
        expressMapper.updateRetreatCargo(map);//录入退货信息
        logMapper.saveProductLog(map);//录入退货日志信息
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 退货成功按钮
     *
     * @param map 订单ID
     * @return 返回退货是否成功
     */
    @Override
    public ResponseData refundCargoSucceed(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        String id = jwtUtil.getId(request.getHeader("token"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Order order = orderMapper.findOrderId(map.get("orderId").toString());
        if (order.getRetreatCargo() != 1) {
            return new ResponseData(400, "该订单已完成退货", null);
        }
        map.put("logUser", id);
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("logSubject", map.get("orderId"));
        map.put("logClassify", 7);
        if (order.getPaymentMethod() == 2) {
            //如果为借样订单  更改借样订单为已归还  存入实际归还时间
            return organizationService.borrowSample(map, request);
        }
        map.put("logRemarks", "退货");
        orderMapper.updateRetreatCargo(order.getOrderId(), 2);
        goodsService.updateWarehouse(order.getGoodsDetail());//回滚库存
        return new ResponseData(200, "退货成功", null);
    }

    /**
     * 发货
     *
     * @param map
     * @param request
     * @return
     * @throws ParseException
     */
    public ResponseData addExpress(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        //生成成功时间时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df.format(new Date());
        String expressName = (String) map.get("expressName");
        String expressNumbers = (String) map.get("expressNumber");
        String orderId = (String) map.get("orderId");
        String expressSign = (String) map.get("expressSign");
        map.put("logUser", id);//存入操作人
        map.put("logSubject", orderId);//存入操作订单
        map.put("logTime", df.parse(df.format(new Date())));//存入操作时间
        //未发货 发货操作
        orderService.updateExpress((String) map.get("orderId"));
        int i = expressMapper.saveExpress(format, orderId, expressName, expressNumbers, expressSign);
        if (i > 0) {
            map.put("logName", expressName + " " + expressNumbers);
            map.put("logRemarks", "发货");
            logService.saveProductLog(map);//调用日志service
            return new ResponseData(200, "发货成功", null);
        }
        return new ResponseData(400, "发货失败", null);
    }

    /**
     * 填写退货信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData refundCargo(Map<String, Object> map, HttpServletRequest request) throws ParseException, IllegalAccessException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        List<Map<String, Object>> goodsDetail = (List<Map<String, Object>>) map.get("goodsDetail");
        if (goodsDetail.size() < 1) {
            return new ResponseData(400, "未选择商品,无法操作", null);
        }
        Order order = orderMapper.findOrderId(map.get("orderId").toString());
        if (order.getExpressOnly() != 1) {
            return new ResponseData(400, "该订单未发货，无法退货", null);
        }
//        if (order.getRetreatCargo() != 0) {
//            return new ResponseData(400, "该订单已录入退货信息，无法重复录入", null);
//        }
        double now = 0;
        try {
            now = BigDecimalUtil.div((int) map.get("now"), 100, 2);
        } catch (Exception e) {
            return new ResponseData(400, "退款金额输入有误", null);
        }

        double add = BigDecimalUtil.add(order.getActualMoney(), order.getFreight());//订单总金额
        double sub = BigDecimalUtil.sub(add, order.getSettlementAmount());//订单总金额-已恢复额度  = 待结算额度
        if (order.getPaymentMethod() != 2 && now > BigDecimalUtil.sub(add, order.getRefundAmount())) {
            //本次退款金额不可大于（订单总金额-已退款金额）
            return new ResponseData(400, "本次退款金额不可大于未退款金额", null);
        }
        map.put("now", sub);
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("logName", map.get("retreatName") + " " + map.get("retreatNumbers"));
        if (order.getPaymentMethod() == 2) {
            map.put("logRemarks", "归还（借样）");
            for (Map<String, Object> goodMap : goodsDetail) {
                map.put("logOrderId", map.get("logOrderId") + "商品编号:" + goodMap.get("goodsCore") + "，商品名字:" + goodMap.get("goodsName") + "，归还商品数量:" + goodMap.get("goodsNumber") + ";");
            }
        } else {
            map.put("logRemarks", "退货");
            for (Map<String, Object> goodMap : goodsDetail) {
                map.put("logOrderId", map.get("logOrderId") + "商品编号:" + goodMap.get("goodsCore") + "，商品名字:" + goodMap.get("goodsName") + "，退货商品数量:" + goodMap.get("goodsNumber") + ";");
            }
        }
        map.put("logUser", id);
        map.put("logTime", df.parse(df.format(new Date())));
        map.put("logSubject", map.get("orderId"));
        map.put("logClassify", 7);
        map.put("logCore", now);

        if (order.getPaymentMethod() == 1) {
            //如果该订单为额度下单  并且是未结算状态  退货成功的时候恢复额度
            //恢复的额度应为  实际金额+运费-已结算金额（未结算时应为0）-退款金额
            if (order.getPaymentStatus() == 1 || order.getPaymentStatus() == 8) {
                //计算出剩余可恢复额度
                //计算用户退款金额
                if (now > BigDecimalUtil.sub(sub, order.getRefundAmount())) {
                    //如果用户退款金额大于可恢复额度则恢复部分额度剩余标识退款中
                    orderMapper.updatePaymentStatus(map.get("orderId").toString(), 6, null);
                    map.put("now", BigDecimalUtil.sub(sub, order.getRefundAmount()));
                } else if (now == BigDecimalUtil.sub(sub, order.getRefundAmount())) {
                    //如果退款金额等于可恢复额度 则标记退款完成
                    orderMapper.updatePaymentStatus(map.get("orderId").toString(), 7, null);
                } else {
                    //如果用户退款金额小于可恢复额度则恢复额度，剩余未结算金额可继续结算
                    map.put("now", now);
                }
                map.put("id", order.getAddCompany());
                organizationMapper.updateUsable(map);
                orderMapper.updateRefundAmount(map.get("orderId").toString(), BigDecimalUtil.add((double) map.get("now"), order.getRefundAmount()));//更改退款金额为（本次退款金额+已退款金额）
            }
        } else if (order.getPaymentStatus() == 2) {
            //如果已全部结算则直接标记订单退款中
            orderMapper.updatePaymentStatus(map.get("orderId").toString(), 6, null);
        }
        if (order.getPaymentMethod() == 3) {
            double add1 = BigDecimalUtil.add(BigDecimalUtil.add(now, order.getSettlementAmount()), order.getRefundAmount());//计算本次退款金额+已退款金额+已结算金额
            //如果为直接购买
            if (order.getPaymentStatus() == 2 || order.getPaymentStatus() == 8) {
                //已结算金额+本次退款金额+已退款金额 >订单总金额 则标记退款中，需退款
                if (add1 > add) {
                    //如果用户退款金额+已结算金额大于订单总金额 则订单标识改为退款中  如果不大于则需要继续结算
                    orderMapper.updatePaymentStatus(map.get("orderId").toString(), 6, null);
                } else if (add1 == add) {
                    //如果用户退款金额+已结算金额等于订单总金额 则标记退款成功
                    orderMapper.updatePaymentStatus(map.get("orderId").toString(), 7, null);
                }
                //用户退款+已结算金小于订单总金额 则需继续结算
            } else if (order.getPaymentStatus() == 3) {
                if (add1 > add) {
                    //如果用户退款金额+已结算金额大于订单总金额 则订单标识改为退款中  如果不大于则需要继续结算
                    orderMapper.updatePaymentStatus(map.get("orderId").toString(), 6, null);
                } else if (add1 == add) {
                    //如果用户退款金额+已结算金额等于订单总金额 则标记退款成功
                    orderMapper.updatePaymentStatus(map.get("orderId").toString(), 7, null);
                }
            }
        }
        orderMapper.updateRefundAmount(map.get("orderId").toString(), BigDecimalUtil.add(now, order.getRefundAmount()));//更改退款金额
        orderMapper.updateRetreatCargo(map.get("orderId").toString(), 1);//更改订单退货信息为退货中
        try {
            int count = goodsService.updateReturnNumber(goodsDetail);//更改商品退货数量
            if (count == 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseData(400, "退货商品数量超过可操作商品数量", null);
            }
        }catch (Exception e){
            return new ResponseData(400,"退货商品数量输入有误",null);
        }
        expressMapper.updateRetreatCargo(map);//录入退货信息
        logMapper.saveProductLog(map);//录入退货日志信息
        return new ResponseData(200, "录入退货信息成功", null);
    }
}
