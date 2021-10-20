package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.*;
import com.ybau.transaction.mapper.*;
import com.ybau.transaction.service.OrderService;
import com.ybau.transaction.service.RoleService;
import com.ybau.transaction.service.UserService;
import com.ybau.transaction.util.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper mapper;

    @Autowired
    SHAUtil shaUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedissonClient client;

    @Autowired
    RoleService roleService;

    @Autowired
    OrderService orderService;

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    OrganizationMapper organizationMapper;


    @Autowired
    OrderMapper orderMapper;

    //规定借样时间低于该值进行提醒
    @Value("${order.RemindTime}")
    int RemindTime;

    //规定可用额度低于该值提醒
    @Value("${order.availableBalanceRemind}")
    String availableBalanceRemind;

    //规定错误账户几次锁定
    @Value("${pwd.lock}")
    int lock;
    //邮箱正则表达式
    @Value("${user.RULE_EMAIL}")
    String RULE_EMAIL;
    //手机号码正则表达式
    @Value("${user.userPhone}")
    String userPhone;

    @Autowired
    SupplierOrderMapper supplierOrderMapper;


    /**
     * 新增用户
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData saveUser(Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("token");//获取token
        String id = jwtUtil.getId(token);//获取添加人的用户名
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String Phone = (String) map.get("userPhone");//验证手机号是否符合要求
        if (Phone != null && Phone != "") {
            if (!Pattern.compile(userPhone).matcher(Phone).matches()) {
                return new ResponseData(400, "手机号码格式错误，请重新输入", null);
            }
        }
        if (map.get("email") != null && map.get("email") != "") {
            //如果邮箱不为空验证邮箱
            if (!Pattern.compile(RULE_EMAIL).matcher((String) map.get("email")).matches()) {
                return new ResponseData(400, "邮箱格式错误，请重新输入", null);
            }
        }
        try {
            String passWord = (String) map.get("password");
            String pwd = shaUtil.getResult(passWord);//密码加密
            map.put("password", pwd);
            map.put("id", System.currentTimeMillis() + "");//生成用户ID
            map.put("addTime", df.parse(df.format(new Date())));
            map.put("addUser", id);
            int count = mapper.findByUsername((String) map.get("username"));
            if (count == 0) {
                mapper.saveUser(map);
                return new ResponseData(200, "新增成功", null);
            } else {
                return new ResponseData(400, "新增失败，该用户名已存在", null);
            }
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "参数有误，新增用户失败", null);
        }
    }

    /**
     * 分页查询用户列表
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findByUser(Map<String, Object> map) {
        try {
            int pageNum = (int) map.get("pageNum");
            int pageSize = (int) map.get("pageSize");
            PageHelper.startPage(pageNum, pageSize);
            List<User> users = mapper.findByUser(map);
            PageInfo<User> pageInfo = new PageInfo<>(users);
            return new ResponseData(200, "查询成功", pageInfo);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "参数有误，查询失败", null);
        }

    }

//    /**
//     * 根据ID删除用户
//     *
//     * @param id
//     * @return
//     */
//    @Override
//    public ResponseData deleteById(String id, String username) {
//        ResponseData result = new ResponseData();
//        try {
//            int count = orderService.findOrderByUser(id);//根据用户名查询该用户是否存在订单
//            if (count > 0) {
//                return new ResponseData(400, "该用户存在已下订单,删除失败", null);
//            }
//            //更新上级关系
//            mapper.updateByuId(id);
//            mapper.deleteById(id);
//            roleService.deleteByRole(id);//删除用户下所有的权限
//
//            result.setCode(200);
//            result.setData("删除成功");
//            return result;
//        } catch (Exception e) {
//            log.error("{}", e);
//            result.setCode(400);
//            result.setMsg("参数有误删除失败");
//            return result;
//        }
//    }

    /**
     * 修改密码
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData updatePwd(Map<String, String> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwtUtil.getUsername(token);
        String id = jwtUtil.getId(token);
        map.put("id", id);
        map.put("username", username);
        String pwd = map.get("passWord");
        if (pwd != null && pwd != "") {
            String passWord = shaUtil.getResult(pwd);
            map.put("password", passWord);
            User user = mapper.selectByUserNameAndPassword(map);
            if (user == null) {
                return new ResponseData(400, "原密码错误", null);
            }
        }
        try {
            String newPassWord = map.get("newPassWord");
            String newPwd = shaUtil.getResult(newPassWord);
            map.put("newPwd", newPwd);
            mapper.updatePwd(map);
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(200, "参数有误，修改失败", null);
        }

    }


    /**
     * 登陆
     *
     * @param params
     * @return
     */
    @Override
    public ResponseData login(Map<String, Object> params) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        User user = new User();
        ResponseData result = new ResponseData();
        if (params.containsKey("username") && !StringUtils.isEmpty(params.get("username"))
                && params.containsKey("password") && !StringUtils.isEmpty(params.get("password"))) {
            //校验登录账号密码
            Map<String, String> user_message = new HashMap<>();
            User loginUser = null;
            String username = (String) params.get("username");
            String password = (String) params.get("password");
            user_message.put("username", username);
            user_message.put("password", shaUtil.getResult(password));
            String upUserName = (String) params.get("upUserName");//上次登陆的用户名
            String count = mapper.findUserName(username);
            if (count != null && count.equals("1")) {
                loginUser = mapper.selectByUserNameAndPassword(user_message);
            } else if (count != null && count .equals("2")) {
                loginUser = mapper.findSupplierUser(user_message);
            }
            //判断账户是否冻结
            //根据账号密码查询用户是否存在
            if (loginUser != null) {
                if (loginUser.getFlag() == 0) {
                    //登陆成功重新赋值
                    mapper.updateByCount(username);
                    int randomNum = (int) ((Math.random() * 9 + 1) * 100000);//生成随机码存入token
                    //登录成功，签发token
                    String id = loginUser.getId();
                    String token = jwtUtil.createJWT(id, loginUser.getUserName(), randomNum);
                    //生成token
                    Map<String, Object> data = new HashMap<>();
                    data.put("token", token);
                    data.put("username", loginUser.getUserName());
                    data.put("email", loginUser.getEmail());
                    data.put("name", loginUser.getName());
                    data.put("userPhone", loginUser.getUserPhone());
                    data.put("organizationName", loginUser.getOrganizationName());
                    List<Permission> permissions = permissionMapper.findByUserId(id);//查询用户权限
                    boolean flag = false;
                    String limitRemind = "";
                    for (Permission permission : permissions) {
                        //循环判断用户是否有某个权限
                        if (permission.getUrl().equals("returnRemind")) {
                            //有权限公司借样订单都会提醒
                            flag = true;
                        }
                        if (permission.getUrl().equals("limitRemind")) {
                            //有权限公司额度不足提醒
                            String availableBalance = organizationMapper.findAvailableBalance(loginUser.getOrganizationId());//根据公司ID查询该公司可用额度
                            if (availableBalance != null && availableBalance != "") {
                                if (BigDecimalUtil.sub(availableBalance, availableBalanceRemind) < 1) {
                                    limitRemind = "您公司的可用额度已不足" + availableBalanceRemind + "，请尽快结算订单";
                                }
                            }
                        }
                    }
                    data.put("limitRemind", limitRemind);
                    String remind = "";
                    if (flag) {
                        //为true整个公司借样订单都会提醒
                        List<Order> orders = orderMapper.findByOitId(loginUser.getOrganizationId());//根据公司ID查询出该公司订单
                        for (Order order : orders) {
                            int i = BigDecimalUtil.calcJulianDays(order.getReturnTime(), df.parse(df.format(new Date())));
                            if (i < RemindTime) {
                                remind = "您有借样订单还未归还，请尽快归还";
                                //查询到有订单后结束循环
                                break;
                            }
                        }
                        data.put("remind", remind);
                    } else {
                        //无权限根据用户ID查询
                        List<Order> orders = orderMapper.findByUserId(loginUser.getId());
                        for (Order order : orders) {
                            int i = BigDecimalUtil.calcJulianDays(order.getReturnTime(), df.parse(df.format(new Date())));
                            if (i < RemindTime) {
                                remind = "您有借样订单还未归还，请尽快归还";
                                //查询到有订单后结束循环
                                break;
                            }
                        }
                        data.put("remind", remind);
                    }
                    RBucket<Object> bucket1 = client.getBucket("data_ybnetwork.permission" + id);
                    bucket1.set(JsonUtil.listToJson(permissions));//权限存入redis
                    //存入Redis
                    RBucket<Object> bucket = client.getBucket("data_ybnetwork." + loginUser.getUserName() + id + randomNum);
                    bucket.set(token, 60, TimeUnit.MINUTES);
                    result.setCode(200);
                    result.setData(data);
                    return result;
                }
                result.setCode(400);
                result.setMsg("账户已冻结，请联系管理员激活");
                return result;
            } else {
                //lock 小于1时 账户不锁定
                if (lock < 1) {
                    result.setCode(400);
                    result.setMsg("账号或密码输入错误");
                    return result;
                } else {
                    if (upUserName != null && username.equals(upUserName)) {
                        mapper.updateByUserCount(username);
                        user = mapper.findByUserCount(username);
                        //超过设置次数更改状态冻结账户
                        if (user == null) {
                            result.setCode(400);
                            result.setMsg("此账户不存在");
                            result.setData(username);
                            return result;
                        }
                        if (user.getUserCount() > lock) {
                            mapper.updateFlag(2, username);
                            result.setCode(400);
                            result.setMsg("账户已冻结，请联系管理员激活");
                            result.setData(username);
                            return result;
                        }
                        result.setCode(400);
                        result.setMsg("您密码已连续输错" + user.getUserCount() + "次，超过" + lock + "次将会锁定");
                        result.setData(username);
                        return result;
                    }
                    result.setCode(400);
                    result.setMsg("账号或密码输入错误");
                    result.setData(username);
                    return result;
                }
            }
        }
        result.setCode(400);
        result.setMsg("账号或者密码为空");
        return result;
    }

    /**
     * 修改用户信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateUser(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");//获取token
        String id = jwtUtil.getId(token);//获取用户名
        map.put("updateUser", id);//设置修改用户名
        String Phone = (String) map.get("userPhone");//验证手机号是否符合要求
        if (Phone != null && Phone != "") {
            Pattern compile = Pattern.compile(userPhone);
            Matcher userPhone = compile.matcher(Phone);
            boolean matches = userPhone.matches();
            if (!matches) {
                return new ResponseData(400, "手机号码格式错误，请重新输入", null);
            }
        }
        if ((String) map.get("email") != null && (String) map.get("email") != "") {
            //如果邮箱不为空验证邮箱
            Pattern compile = Pattern.compile(RULE_EMAIL);
            Matcher email = compile.matcher((String) map.get("email"));
            boolean matches = email.matches();
            if (!matches)
                return new ResponseData(400, "邮箱格式错误，请重新输入", null);
        }
        String pwd = (String) map.get("password");
        if (pwd != null && pwd != "") {
            String password = shaUtil.getResult(pwd);
            map.put("password", password);
        }
        try {
            map.put("updateTime", df.parse(df.format(new Date())));//设置修改日期
            mapper.updateUser(map);
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "用户名重复", null);
        }
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @Override
    public ResponseData findAll(String id, Integer organizationId) {
        ResponseData result = new ResponseData();
        List<User> users = mapper.findAll(id, organizationId);
        result.setCode(200);
        result.setMsg("查询成功");
        result.setData(users);
        return result;
    }

    /**
     * 根据ID查询用户下所有的角色
     *
     * @param uId
     * @return
     */
    @Override
    public ResponseData findByRole(String uId) {
        List<Role> roles = mapper.findByRole(uId);
        return new ResponseData(200, "查询成功", roles);

    }

    /**
     * 根据ID查询用户没有的角色
     *
     * @param uId
     * @return
     */
    @Override
    public ResponseData findOtherRole(String uId) {
        List<Role> roles = mapper.findOtherRole(uId);
        return new ResponseData(200, "查询成功", roles);
    }

    /**
     * 根据ID查询用户
     *
     * @param uId
     * @return
     */
    @Override
    public ResponseData findById(String uId) {
        User user = mapper.findById(uId);
        return new ResponseData(200, "查询成功", user);
    }

    /**
     * 根据公司ID查询该公司是否存在用户
     *
     * @param id
     * @return
     */
    @Override
    public int findByOrganizationId(int id) {
        return mapper.findByOrganizationId(id);
    }

    /**
     * 修改用户个人信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateEmailByPhone(Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);//获取用户ID
        map.put("id", id);
        String phone = map.get("userPhone").toString();
        if (map.get("email").toString() != null && map.get("email").toString() != "") {
            //如果邮箱不为空验证邮箱
            Pattern compile = Pattern.compile(RULE_EMAIL);
            Matcher email = compile.matcher((String) map.get("email"));
            if (!email.matches()) {
                return new ResponseData(400, "邮箱格式错误，请重新输入", null);
            }
        }
        if (phone != null && phone != "") {
            Pattern compile = Pattern.compile(userPhone);
            Matcher userPhone = compile.matcher(phone);
            if (!userPhone.matches()) {
                return new ResponseData(400, "手机号码格式错误，请重新输入", null);
            }
        }
        mapper.updateEmailByPhone(map);
        User user = mapper.findById((String) map.get("id"));
        return new ResponseData(200, "修改成功", user);
    }

    /**
     * 根据公司ID查询下属用户
     *
     * @param
     * @return
     */
    @Override
    public ResponseData findByOId(Map<String, Object> map) {
        List<Organization> organizations = mapper.findByOId(map);
        return new ResponseData(200, "查询成功", organizations);
    }

    /**
     * 查询拥有定制订单新增操作记录权限的人
     *
     * @return
     */
    @Override
    public ResponseData findSaveCoLog() {
        List<Map> maps = mapper.findSaveCoLog("/log/saveCoLog");
        return new ResponseData(200, "查询成功", maps);
    }

    /**
     * 查询拥有向供应商下单权限的用户
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData supplierUser(Map<String, Object> map) {
        SupplierOrder supplierOrder = supplierOrderMapper.findById(map.get("supplierOrderId").toString());
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Map> maps = mapper.supplierUser(map, "/supplierOrder/saveOrder", supplierOrder.getOrganizationId(), 1);
        PageInfo pageInfo = new PageInfo(maps);
        return new ResponseData(200, "查询成功", pageInfo);
    }


}
