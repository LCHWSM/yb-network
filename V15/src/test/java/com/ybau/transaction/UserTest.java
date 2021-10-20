package com.ybau.transaction;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.domain.Product;
import com.ybau.transaction.mapper.OrganizationMapper;
import com.ybau.transaction.mapper.ProductMapper;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.SHAUtil;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootTest
public class UserTest {
//    private static int counts = 0;
//    private static Object lock = 0;
//    @Autowired
//    SHAUtil shaUtil;
//    @Autowired
//    OrganizationMapper organizationMapper;
//
//    @Value("${userPhone}")
//    String userPhone;
//
//    @Autowired
//    ProductMapper productMapper;
//
//    @Autowired
//    JwtUtil jwtUtil;
//
//
//    @Autowired
//    private RedissonClient client;
//    @Value("${ftp.visitUrl}")
//    String prefixUrl;
//
//    @Value("${ftp.bastPath}")
//    String bastPath;
//
//    String RULE_EMAIL = "^(\\w+([-.][A-Z-a-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
//
//
//    //    private static final String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
//    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//
//    @Test
//    public void findPwd() {
//        Map map = new HashMap();
//        List<Product> products = productMapper.findAll(map);
//        for (Product product : products) {
//            String replace = product.getDescription().replace("http://127.0.0.1/image-server/yb_network6", "http://172.16.68.199:8087/yb_network_ftp/v5-1");
//            productMapper.updateDescriptionUrl(replace, product.getId());
//        }
//    }
//
//
//    @Test
//    public void testDouble() {
//        List<Map> list = new ArrayList<>();
//        int count = list.stream()
//                .collect(Collectors.groupingBy(a -> a.get("groupingId"), Collectors.counting()))
//                .entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
//                .collect(Collectors.toList()).size();
//    }
//
//
//    @Test
//    public void subInt() {
//        Order order = new Order();
//        List<Order> list = new ArrayList<>();
//        order.setOrderId("9999");
////        HashMap<String,Object> map=new HashMap<>();
//        list.add(order);
////        map.put("1",order);
//        order = null;
//
//        System.out.println(list.get(0));  //测试6
//    }
//
//    @Test
//    public void Int() {
//
//    }
//

}


