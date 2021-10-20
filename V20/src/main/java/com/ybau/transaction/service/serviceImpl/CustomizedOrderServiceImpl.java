package com.ybau.transaction.service.serviceImpl;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.CustomizedOrder;
import com.ybau.transaction.domain.User;
import com.ybau.transaction.mapper.CustomizedOrderMapper;
import com.ybau.transaction.mapper.LogMapper;
import com.ybau.transaction.mapper.UserMapper;
import com.ybau.transaction.service.CustomizedOrderService;
import com.ybau.transaction.util.CodeUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@Transactional
public class CustomizedOrderServiceImpl implements CustomizedOrderService {
    @Autowired
    CustomizedOrderMapper customizedOrderMapper;

    @Autowired
    JwtUtil jwtUtil;

    //手机号码正则表达式
    @Value("${user.userPhone}")
    String userPhone;
    //邮箱正则表达式
    @Value("${user.RULE_EMAIL}")
    String email;

    @Autowired
    LogMapper logMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private RedissonClient client;


    /**
     * 用户定制需求
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveCo(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        RBucket<Object> clientBucket = client.getBucket((String) map.get("uuid"));
        String redisCode = (String) clientBucket.get();
        String code = map.get("code").toString();
        code = code.toLowerCase();
        if (StringUtils.isEmpty(redisCode)) {
            //如果验证码失效 重新生成验证码返回
            Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();//调用工具类获取验证码
            RBucket<Object> Bucket = client.getBucket((String) codeMap.get("uuid"));//用UUID作为redis的key
            Bucket.set(codeMap.get("code"), 5, TimeUnit.MINUTES);//把验证码的值存入到redis
            codeMap.remove("code");
            return new ResponseData(405, "验证码失效，请重新输入", codeMap);
        }
        String codeStr = redisCode.toLowerCase();
        if (!code.equals(codeStr)) {
            //校验验证码是否正确
            //如果验证码错误 重新生成验证码返回
            Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();//调用工具类获取验证码
            RBucket<Object> Bucket = client.getBucket((String) codeMap.get("uuid"));//用UUID作为redis的key
            Bucket.set(codeMap.get("code"), 5, TimeUnit.MINUTES);//把验证码的值存入到redis
            codeMap.remove("code");
            return new ResponseData(405, "验证码错误，请重新输入", codeMap);
        }
        if (map.get("coName").toString().length() < 2 || map.get("coName").toString().length() > 10) {
            return new ResponseData(400, "姓名输入有误，请重新输入", null);
        }
        //校验手机号是否正确
        Pattern compile = Pattern.compile(userPhone);
        Matcher userPhone = compile.matcher(map.get("coPhone").toString());
        if (!userPhone.matches()) {
            return new ResponseData(400, "手机号码格式错误，请重新输入", null);
        }
        //校验邮箱是否正确
        Pattern compile1 = Pattern.compile(email);
        Matcher email = compile1.matcher(map.get("coEmail").toString());
        if (!email.matches()) {
            return new ResponseData(400, "邮箱格式错误，请重新输入", null);
        }
        map.put("coAddTime", df.parse(df.format(new Date())));//存入创建时间
        customizedOrderMapper.saveCo(map);//执行插入方法
        clientBucket.delete();//验证码使用后即失效
        return new ResponseData(200, "提交成功", null);
    }

    /**
     * 反馈客户记录
     *
     * @param request
     * @param map
     * @return
     */
    @Override
    public ResponseData updateFlag(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("updateUser", id);//存入操作人
        map.put("updateTime", df.parse(df.format(new Date())));//存入操作时间
        map.put("coLogName", "");
        String coFlagStr = "";
        switch ((String) map.get("coFlag")) {
            case "1":
                coFlagStr = "待处理";
                break;
            case "2":
                coFlagStr = "处理中";
                break;
            case "3":
                coFlagStr = "已完成";
                break;
            case "4":
                coFlagStr = "已取消";
                break;
        }
        map.put("coLogName", "处理状态：" + coFlagStr);
        List<String> users = (List<String>) map.get("users");
        if (users != null && users.size() < 1) {
            //如果把跟进入全部取消 则清空跟进人信息
            customizedOrderMapper.deleteByCoId((int) map.get("coId"));//如果有修改则清空之前的跟进人 插入新的跟进人
        }
        if (users != null && users.size() > 0) {
            String name = "";
            //如果有指定跟进入，那么插入跟进入与用户中间表
            customizedOrderMapper.deleteByCoId((int) map.get("coId"));//如果有修改则清空之前的跟进人 插入新的跟进人
            for (String userId : users) {
                customizedOrderMapper.userCustomizedorder((int) map.get("coId"), userId);//插入中间表信息
                User user = userMapper.findById(userId);
                name = user.getName() + "," + name;
            }
            map.put("coLogName", map.get("coLogName") + " 跟进人：" + name);
        }
        customizedOrderMapper.updateFlag(map);//修改订单信息
        //插入操作记录
        map.put("coClassify", 1);
        logMapper.saveCoLog(map);
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 查询用户所有定制需求
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findAll(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<CustomizedOrder> customizedOrders = customizedOrderMapper.findAll(map);
        PageInfo pageInfo = new PageInfo(customizedOrders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据用户手机号查询用户定制需求（客户端）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findClient(Map<String, Object> map, HttpServletRequest request) {
        Map<String, Object> refreshMap = new HashMap<>();
        if (map.get("flag").toString().equals("2")) {
            //如果为第一次登录验证验证码并颁发token
            RBucket<Object> clientBucket = client.getBucket((String) map.get("uuid"));
            String redisCode = (String) clientBucket.get();
            String code = map.get("code").toString();
            code = code.toLowerCase();
            if (StringUtils.isEmpty(redisCode)) {
                //如果验证码失效 重新生成验证码返回
                Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();//调用工具类获取验证码
                RBucket<Object> Bucket = client.getBucket((String) codeMap.get("uuid"));//用UUID作为redis的key
                Bucket.set(codeMap.get("code"), 5, TimeUnit.MINUTES);//把验证码的值存入到redis
                codeMap.remove("code");
                return new ResponseData(405, "验证码失效，请重新输入", codeMap);
            }
            String codeStr = redisCode.toLowerCase();
            if (!code.equals(codeStr)) {
                //校验验证码是否正确
                //如果验证码错误 重新生成验证码返回
                Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();//调用工具类获取验证码
                RBucket<Object> Bucket = client.getBucket((String) codeMap.get("uuid"));//用UUID作为redis的key
                Bucket.set(codeMap.get("code"), 5, TimeUnit.MINUTES);//把验证码的值存入到redis
                codeMap.remove("code");
                return new ResponseData(405, "验证码错误，请重新输入", codeMap);
            }
            //生成无用户下token
            String uuId = UUID.randomUUID().toString();
            String refreshToken = jwtUtil.createRefreshJWT(map.get("coPhone").toString(), uuId);
            RBucket<Object> clientBucket1 = client.getBucket(uuId);
            //把生成的token存入Redis
            clientBucket1.set(refreshToken, 60, TimeUnit.MINUTES);
            clientBucket.delete();//验证码使用后即失效
            refreshMap.put("token",refreshToken);
        } else {
            //否则为刷新操作  验证token是否正确
            String token = request.getHeader("token");
            Claims claims = jwtUtil.parseJWT(token);
            if (claims == null) {
                return new ResponseData(409, "验证已过期，请重新访问", null);
            }
            String phone = jwtUtil.getPhone(token);
            if (!map.get("coPhone").toString().equals(phone)) {
                return new ResponseData(409, "验证已过期，请重新访问", null);
            }
            RBucket<Object> clientBucket = client.getBucket(jwtUtil.getUUID(token));
            String redisToken = (String) clientBucket.get();
            if (redisToken == null || !redisToken.equals(token)) {
                return new ResponseData(409, "验证已过期，请重新访问", null);
            }
        }
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<CustomizedOrder> customizedOrders = customizedOrderMapper.findClient(map);
        PageInfo pageInfo = new PageInfo(customizedOrders);
        refreshMap.put("pageInfo",pageInfo);
        return new ResponseData(200, "查询成功", refreshMap);
    }

    /**
     * 修改定制订单信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateOrder(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        map.put("updateUser", id);
        map.put("updateTime", df.parse(df.format(new Date())));
        if (map.get("coEmail").toString() != null && map.get("coEmail").toString() != "") {
            //如果邮箱不为空验证邮箱
            Pattern compile = Pattern.compile(email);
            Matcher email = compile.matcher((String) map.get("coEmail"));
            if (!email.matches()) {
                return new ResponseData(400, "电子邮箱格式错误，请重新输入", null);
            }
        }
        if (map.get("coPhone").toString() != null && map.get("coPhone").toString() != "") {
            Pattern compile = Pattern.compile(userPhone);
            Matcher phone = compile.matcher((String) map.get("coPhone"));
            if (!phone.matches()) {
                return new ResponseData(400, "手机号码格式错误，请重新输入", null);
            }
        }
        //拼接修改日志
        map.put("coClassify", 2);
        map.put("coLogName", "联系人姓名：" + map.get("coName").toString() + " 手机号：" + map.get("coPhone").toString() + " 电子邮箱：" + map.get("coEmail").toString() + " 定制需求：" + map.get("coDemand").toString());
        customizedOrderMapper.updateOrder(map);
        logMapper.saveCoLog(map);//存入日志信息
        return new ResponseData(200, "修改订单信息成功", null);
    }

    /**
     * 查询是否是定制订单跟进人
     *
     * @param coId
     * @param request
     * @return
     */
    @Override
    public ResponseData findUser(int coId, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        CustomizedOrder customizedOrder = customizedOrderMapper.findById(coId);
        List<User> users = customizedOrder.getUsers();
        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user.getId().equals(id)) {
                    return new ResponseData(200, null, 1);
                }
            }
        }
        return new ResponseData(200, null, 2);
    }

    /**
     * 根据用户ID查询用户跟进的订单
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData findByUserId(Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("userId", id);
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<CustomizedOrder> customizedOrders = customizedOrderMapper.findByUserId(map);
        PageInfo pageInfo = new PageInfo(customizedOrders);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 根据定制订单ID查询定制订单信息
     *
     * @param coId
     * @return
     */
    @Override
    public ResponseData findById(int coId) {
        CustomizedOrder customizedOrder = customizedOrderMapper.findById(coId);
        return new ResponseData(200, "查询成功", customizedOrder);
    }
}
