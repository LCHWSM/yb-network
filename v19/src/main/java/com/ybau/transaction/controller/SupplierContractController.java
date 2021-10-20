package com.ybau.transaction.controller;


import com.ybau.transaction.service.SupplierContractService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/supplierContract")
public class SupplierContractController {

    @Autowired
    SupplierContractService supplierContractService;


    /**
     * 根据订单ID查询订单关联的合同 (用户)
     *
     * @param srId
     * @return
     */
    @GetMapping("/findBySrId")
    public ResponseData findBySrId(String srId) {
        return supplierContractService.findBySrId(srId);
    }

    /**
     * 新增供应商合同   （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveContr")
    public ResponseData saveContr(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return supplierContractService.saveContr(map, request);
    }


    /**
     * 根据合同ID删除合同 并解除所有关联   （用户）
     *
     * @param supplierOrderId
     * @return
     */
    @GetMapping("/cancelContr")
    public ResponseData cancelContr(String supplierOrderId, HttpServletRequest request) {
        return supplierContractService.cancelContr(supplierOrderId, request);
    }

    /**
     * 修改合同订单（用户）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateContr")
    public ResponseData updateContr(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return supplierContractService.updateContr(map, request);
    }

    /**
     * 查询未关联合同订单 （用户）
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/findSROrder")
    public ResponseData findSROrder(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return supplierContractService.findSROrder(map, request);
    }

    /**
     * 订单关联合同  （用户）
     *
     * @param map
     * @return
     */
    @PostMapping("/relevanceSROrder")
    public ResponseData relevanceSROrder(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return supplierContractService.relevanceSROrder(map,request);
    }

    /**
     * 新增供应商合同  （所有）
     *
     * @param map
     * @return
     */
    @PostMapping("/saveContract")
    public ResponseData saveContract(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierContractService.saveContract(map, request);
    }

    /**
     * 根据合同ID删除合同 （所有订单）
     *
     * @param supplierContractId
     * @return
     */
    @GetMapping("/deleteContract")
    public ResponseData deleteContract(int supplierContractId) {
        return supplierContractService.deleteContract(supplierContractId);
    }

    /**
     * 修改合同信息  （所有订单）
     *
     * @param map
     * @return
     */
    @PostMapping("/updateContract")
    public ResponseData updateContract(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return supplierContractService.updateContract(map, request);
    }

    /**
     * 查询所有本公司未关联合同订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findSROrderAll")
    public ResponseData findSROrderAll(@RequestBody Map<String, Object> map) {
        return supplierContractService.findSROrderAll(map);
    }


    /**
     * 订单关联合同  （所有订单）
     *
     * @param map
     * @return
     */
    @PostMapping("/relevanceSRAll")
    public ResponseData relevanceSRAll(@RequestBody Map<String, Object> map) {
        return supplierContractService.relevanceSRAll(map);
    }

    /**
     * 判断该合同是否是该用户上传的
     *
     * @param supplierContractId
     * @param request
     * @return
     */
    @GetMapping("/ifPermission")
    public ResponseData ifPermission(int supplierContractId, HttpServletRequest request) {
        return supplierContractService.ifPermission(supplierContractId, request);
    }

    /**
     * 根据合同ID查询合同相关联的订单
     *
     * @param map
     * @return
     */
    @PostMapping("/findContractId")
    public ResponseData findContractId(@RequestBody Map<String, Object> map) {
        return supplierContractService.findContractId(map);
    }

    /**
     * 根据供应商订单ID取消与合同的关联 (所有订单)
     * @param supplierOrderId
     * @return
     */
    @GetMapping("/updateSOId")
    public ResponseData updateSOId(String supplierOrderId){
        return supplierContractService.updateSOId(supplierOrderId);
    }

    /**
     * 根据合同ID查询该合同关联的订单 （所有订单）
     * @param map
     * @return
     */
    @PostMapping("/findByCId")
    public ResponseData findByCId(@RequestBody Map<String,Object> map){
        return supplierContractService.findContractId(map);
    }

}
