package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Role;
import com.ybau.transaction.domain.User;
import com.ybau.transaction.mapper.RoleMapper;
import com.ybau.transaction.mapper.UserMapper;
import com.ybau.transaction.service.SupplierUserService;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import com.ybau.transaction.util.SHAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Slf4j
@Service
@Transactional
public class SupplierUserServiceImpl implements SupplierUserService {

    @Autowired
    JwtUtil jwtUtil;

    //邮箱正则表达式
    @Value("${user.RULE_EMAIL}")
    String RULE_EMAIL;
    //手机号码正则表达式
    @Value("${user.userPhone}")
    String userPhone;

    @Autowired
    SHAUtil shaUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    /**
     * 新增供应商用户信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveSupplierUser(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        map.put("addUser", id);
        map.put("addTime", df.parse(df.format(new Date())));
        if (map.get("userPhone") != null) {
            //如果输入了用户手机号则验证手机号 是否正确
            if (!Pattern.compile(userPhone).matcher(map.get("userPhone").toString()).matches()) {
                return new ResponseData(400, "手机号格式错误", null);
            }
        } else if (map.get("email") != null) {
            //如果邮箱号不为空，验证邮箱格式是否正确
            if (!Pattern.compile(userPhone).matcher(map.get("email").toString()).matches()) {
                return new ResponseData(400, "邮箱格式错误", null);
            }
        }
        if (map.get("passWord") == null && map.get("passWord").toString() == "") {
            //密码不可为空
            return new ResponseData(400, "密码不可为空", null);
        } else if (map.get("username") == null && map.get("username").toString() == "") {
            return new ResponseData(400, "用户名不可为空", null);
        }
        map.put("password", shaUtil.getResult(map.get("passWord").toString()));//密码加密
        map.put("id", System.currentTimeMillis() + "");//生成用户ID
        try {
            userMapper.saveUser(map);
            return new ResponseData(200, "新增成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "用户名已存在", null);
        }
    }

    /**
     * 分页查询供应商用户信息
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData supplierByUser(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<User> users = userMapper.supplierByUser(map);
        PageInfo pageInfo = new PageInfo(users);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 修改供应商用户信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateSByUser(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String id = jwtUtil.getId(request.getHeader("token"));
        map.put("updateUser", id);
        map.put("updateTime", df.parse(df.format(new Date())));
        if (map.get("userPhone") != null) {
            //如果输入了用户手机号则验证手机号 是否正确
            if (!Pattern.compile(userPhone).matcher(map.get("userPhone").toString()).matches()) {
                return new ResponseData(400, "手机号格式错误", null);
            }
        } else if (map.get("email") != null) {
            //如果邮箱号不为空，验证邮箱格式是否正确
            if (!Pattern.compile(userPhone).matcher(map.get("userPhone").toString()).matches()) {
                return new ResponseData(400, "邮箱格式错误", null);
            }
        }
        if (map.get("password") != null && map.get("password").toString() != "") {
            //如果修改了密码 则对密码加密
            map.put("password", shaUtil.getResult(map.get("password").toString()));//密码加密
        }
        try {
            userMapper.updateUser(map);
            return new ResponseData(200, "修稿成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "用户名已存在", null);
        }
    }

    /**
     * 查询用户拥有的角色
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseData findByRole(String userId) {
        List<Role> roles = userMapper.findByRole(userId);
        return new ResponseData(200, "查询成功", roles);
    }

    /**
     * 修改供应商用户角色
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData updateUidByRid(Map<String, Object> map) {
        List<Integer> roles = (List<Integer>) map.get("roles");
        roleMapper.deleteRole(map.get("uId").toString());
        for (Integer role : roles) {
            roleMapper.saveRoleByUId(map.get("uId").toString(), role);
        }
        return new ResponseData(200, "修改成功", null);
    }
}
