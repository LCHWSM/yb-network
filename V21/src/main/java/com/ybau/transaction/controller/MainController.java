package com.ybau.transaction.controller;


import com.ybau.transaction.service.UserService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    UserService service;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData login(@RequestBody Map<String, Object> params) throws ParseException {

        return service.login(params);
    }

    /**
     * 创建新用户
     *
     * @param map
     * @return
     */
    @PostMapping("/saveUser")
    public ResponseData saveUser(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        return service.saveUser(map, request);
    }

    /**
     * 查询所有用户列表（分页）
     *
     * @param map
     * @return
     */
    @PostMapping("/findByUser")
    public ResponseData findByUser(@RequestBody Map<String, Object> map) {
        return service.findByUser(map);
    }


    /**
     * 修改密码
     *
     * @param map
     * @return
     */
    @PostMapping("/updatePwd")
    public ResponseData updatePwd(@RequestBody Map<String, String> map, HttpServletRequest request) {
        return service.updatePwd(map, request);
    }

    /**
     * 修改用户信息
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateUser")
    public ResponseData updateUser(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return service.updateUser(map, request);
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @GetMapping("/findAll")
    public ResponseData findAll(String id, Integer organizationId) {
        return service.findAll(id, organizationId);
    }

    /**
     * 查询用户下拥有所有角色
     *
     * @param uId
     * @return
     */
    @GetMapping("/findByRole")
    public ResponseData findByRole(String uId) {
        return service.findByRole(uId);
    }

    /**
     * 查询用户没有的角色
     *
     * @param uId
     * @return
     */
    @GetMapping("/findOtherRole")
    public ResponseData findOtherRole(String uId) {

        return service.findOtherRole(uId);
    }

    /**
     * 根据ID查询用户
     *
     * @param uId
     * @return
     */
    @GetMapping("/findById")
    public ResponseData findById(String uId) {
        return service.findById(uId);
    }

    /**
     * 修改用户个人信息
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateEmailByPhone")
    public ResponseData updateEmailByPhone(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return service.updateEmailByPhone(map, request);
    }

    /**
     * 查询所有用户
     *
     * @param
     * @return
     */
    @PostMapping("/findByOId")
    public ResponseData findByOId(@RequestBody Map<String, Object> map) {
        return service.findByOId(map);
    }

    /**
     * 查询拥有新增定制订单操作记录权限的人
     *
     * @return
     */
    @GetMapping("/findSaveCoLog")
    public ResponseData findSaveCoLog() {
        return service.findSaveCoLog();
    }


    /**
     * 查询拥有向供应商下单权限的用户
     * @param map
     * @return
     */
    @PostMapping("/supplierUser")
    public ResponseData supplierUser(@RequestBody Map<String,Object> map){
        return service.supplierUser(map);
    }


}
