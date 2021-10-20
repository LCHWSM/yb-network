package com.ybau.transaction;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import com.ybau.transaction.domain.*;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.*;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
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

    @Test
    public void orderTest() throws Exception {

        String date1 = DateUtli.stampToTime("1611221491");
        System.out.println(date1);

    }
}

