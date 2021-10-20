package com.ybau.transaction.controller;

import com.ybau.transaction.service.UserContractService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户合同WEB层
 */
@RestController
@RequestMapping("/userContract")
public class UserContractController {
    @Autowired
    UserContractService userContractService;

    /**
     * 根据用户ID查询添加的合同列表 （筛选、排序）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return userContractService.findAll(map, request);
    }

    /**
     * 新增用户合同
     *
     * @param map
     * @return
     */
    @PostMapping("/saveContract")
    public ResponseData saveContract(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return userContractService.saveContract(map, request);
    }

    /**
     *
     * 根据订单ID取消合同关联
     *
     * @param orderId
     * @return
     */
    @GetMapping("/cancelContract")
    public ResponseData cancelContract(String orderId,HttpServletRequest request) {
        return userContractService.cancelContract(orderId,request);
    }


    /**
     * 根据合同iD修改合同信息
     *
     * @param map
     * @return
     */
    @PostMapping("/updateContract")
    public ResponseData updateContract(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return userContractService.updateContract(map, request);
    }


    /**
     * 根据用户ID查询该用户下的订单
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/findOrder")
    public ResponseData findOrder(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return userContractService.findOrder(map, request);
    }

    /**
     * 合同关联订单
     *
     * @param map
     * @return
     */
    @PostMapping("/relevanceOrder")
    public ResponseData relevanceOrder(@RequestBody Map<String, Object> map,HttpServletRequest request) {
        return userContractService.relevanceOrder(map,request);
    }

    /**
     * 查询该合同是否是登录用户上传的
     *
     * @param contractId
     * @param request
     * @return
     */
    @GetMapping("/ifPermission")
    public ResponseData ifPermission(int contractId, HttpServletRequest request) {
        return userContractService.ifPermission(contractId, request);
    }

    /**
     * 根据合同ID查询 该合同关联的订单
     *
     * @param map
     * @return
     */
    @PostMapping ("/findByContract")
    public ResponseData findByContract(@RequestBody Map<String,Object> map) {
        return userContractService.findByContract(map);
    }


}
