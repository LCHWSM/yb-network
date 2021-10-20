package com.ybau.transaction.controller;

import com.ybau.transaction.service.SupplierService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    SupplierService supplierService;


    /**
     * 新增供应商
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveSupplier")
    public ResponseData saveSupplier(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierService.saveSupplier(map, request);
    }

    /**
     * 查询所有供应商 （分页）
     *
     * @param map
     * @return
     */
    @PostMapping("/findAll")
    public ResponseData findAll(@RequestBody Map<String, Object> map) {
        return supplierService.findAll(map);
    }

    /**
     * 修改供应商信息
     *
     * @param map
     * @param request
     * @return
     * @throws ParseException
     */
    @PostMapping("/updateSupplier")
    public ResponseData updateSupplier(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return supplierService.updateSupplier(map, request);
    }

    /**
     * 根据ID删除供应商信息
     *
     * @param supplierId
     * @return
     */
    @GetMapping("/deleteById")
    public ResponseData deleteById(int supplierId) {
        return supplierService.deleteById(supplierId);
    }

    /**
     * 查询供应商信息
     *
     * @return
     */
    @GetMapping("/findSupplier")
    public ResponseData findSupplier(String supplierName, Integer pageSize, Integer pageNum, HttpServletRequest request) {
        return supplierService.findSupplier(supplierName, pageSize, pageNum, request);
    }

    /**
     * 根据供应商名查询
     * @param supplierName
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/findBySupplier")
    public ResponseData findBySupplier(String supplierName, Integer pageSize, Integer pageNum) {
        return supplierService.findBySupplier(supplierName,pageSize,pageNum);
    }

}
