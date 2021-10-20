//package com.ybau.transaction.util;
//
//
//import com.ybau.transaction.domain.Goods;
//import com.ybau.transaction.mapper.OrderMapper;
//import com.ybau.transaction.service.GoodsService;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@Component
//public class PullOrder {
//
//    @Autowired
//    private Environment environment;
//    @Autowired
//    private RestURLUtil restURLUtil;
//    @Autowired
//    private JsonUtil jsonUtil;
//    @Autowired
//    private GoodsService goodsService;
//    @Autowired
//    private OrderMapper orderMapper;
//
////    @Value("${yb-order-url}")
////    private String url;
////
////
////    @Value("${order.audit}")
////    private int audit;
//
//    @Async
//    public void findOrderByDate(Map<String, Object> map) throws Exception {
//        Map<String, Object> map1 = new HashMap();
//        map.put("authCode", environment.getProperty("order.authCode"));
//        //调用工具类访问远程接口
//        JSONObject jsonObject = restURLUtil.doPost(JSONObject.fromObject(map), url);
//        if (jsonObject != null) {
//            //把Json数据转换成MAP
//            Map<String, Object> map2 = jsonUtil.parseJSON2Map(jsonObject);
//            //取出返回数据
//            List<Map<String, Object>> list = (List<Map<String, Object>>) map2.get("data");
//            if (list != null && list.size() > 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    map1 = list.get(i);
//                    //查询后插入数据库
//                    //判断该订单是否存在
//                    int count = orderMapper.findCount((String) map1.get("orderId"));
//                    //小于0不存在，把该订单信息插入数据库，大于0已存在不做操作 直接返回
//                    if (count <= 0) {
//                        //赋值详细地址
//                        map1.put("site", map1.get("receiverProvince").toString() + map1.get("receiverCity").toString() + map1.get("receiverDistrict").toString() + map1.get("receiverStreet").toString() + map1.get("receiverAddress").toString());
//                        //赋值订单创建人
//                        map1.put("addUser", "粤宝网络（系统拉取");
//                        //赋值公司
//                        map1.put("addCompany", "粤宝网络");
//                        //赋值是否需要审核
//                        map1.put("audit", audit);
//                        Double sumMoney = 0.0;
//                        List<Goods> goodsDetail = (List<Goods>) map1.get("goodsDetail");
//                        if (goodsDetail != null) {
//                            for (int x = 0; x < goodsDetail.size(); x++) {
//                                map = (Map<String, Object>) goodsDetail.get(x);
//                                sumMoney = sumMoney + ((double) map.get("goodsPrice") * (int) map.get("goodsNumber"));
//                            }
//                            map1.put("now", sumMoney);
//                        }
//                        orderMapper.saveOrder(map1);
//                        //调用商品Service
//                        if (goodsDetail != null && goodsDetail.size() > 0) {
//                            goodsService.saveGoods(goodsDetail, (String) map1.get("orderId"));
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
