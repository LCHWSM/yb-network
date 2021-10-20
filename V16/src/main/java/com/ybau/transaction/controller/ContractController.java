package com.ybau.transaction.controller;

import com.ybau.transaction.service.ContractService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 合同web层
 */

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    ContractService contractService;

    /**
     * 查询合同列表（分页）
     *
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String, Object> map) {
        return contractService.findAll(map);
    }

    /**
     * 插入合同信息
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveContract")
    public ResponseData saveContract(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return contractService.saveContract(map, request);
    }

    /**
     * 根据合同ID删除该合同
     *
     * @param contractId
     * @return
     */
    @GetMapping("/deleteContract")
    public ResponseData deleteContract(int contractId) {
        return contractService.deleteContract(contractId);
    }

    /**
     * 查询未关联合同订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findNotContract")
    public ResponseData findNotContract(@RequestBody Map<String, Object> map) {
        return contractService.findNotContract(map);
    }

    /**
     * 合同关联订单
     *
     * @param map
     * @return
     */
    @PostMapping("/saveContractId")
    public ResponseData saveContractId(@RequestBody Map<String, Object> map) {
        return contractService.saveContractId(map);
    }

    /**
     * 修改合同信息
     *
     * @param request
     * @return
     */
    @PostMapping("/updateContract")
    public ResponseData updateContract(HttpServletRequest request, @RequestBody Map<String, Object> map) {
        return contractService.updateContract(request, map);
    }

    /**
     * 取消订单关联合同
     *
     * @param orderId
     * @return
     */
    @GetMapping("/updateConByOrderId")
    public ResponseData updateConByOrderId(String orderId) {
        return contractService.updateConByOrderId(orderId);
    }

    /**
     * 根据订单ID查看订单合同信息
     *
     * @param orderId 订单ID
     * @return
     */
    @GetMapping("/findByOrderId")
    public ResponseData findByOrderId(String orderId) {
        return contractService.findByOrderId(orderId);
    }


    /**
     * 所有订单新增合同
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveContractAll")
    public ResponseData saveContractAll(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return contractService.saveContractAll(map, request);
    }

}
