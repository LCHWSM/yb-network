package com.ybau.transaction.controller;

import com.ybau.transaction.service.SupplierUserService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * 供应商用户
 */
@RestController
@RequestMapping("/supplierUser")
public class SupplierUserController {

    @Autowired
    SupplierUserService supplierUserService;

    /**
     * 新增供应商用户
     *
     * @return
     */
    @PostMapping("/saveSupplierUser")
    public ResponseData saveSupplierUser(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierUserService.saveSupplierUser(map, request);
    }


    /**
     * 分页查询供应商用户信息
     *
     * @param map 筛选条件
     * @return
     */
    @PostMapping("/supplierByUser")
    public ResponseData supplierByUser(@RequestBody Map<String, Object> map) {
        return supplierUserService.supplierByUser(map);
    }

    /**
     * 修改供应商用户信息
     */
    @PostMapping("/updateSByUser")
    public ResponseData updateSByUser(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierUserService.updateSByUser(map, request);
    }

    /**
     * 查询用户拥有的角色
     *
     * @param userId  供应商用户ID
     * @return  查询结果
     */
    @GetMapping("/findByRole")
    public ResponseData findByRole(String userId) {
        return supplierUserService.findByRole(userId);
    }

    /**
     * 修改用户角色
     * @param map
     * @return
     */
    @PostMapping("/updateUidByRid")
    public ResponseData updateUidByRid(@RequestBody Map<String,Object> map){
        return supplierUserService.updateUidByRid(map);
    }
}
