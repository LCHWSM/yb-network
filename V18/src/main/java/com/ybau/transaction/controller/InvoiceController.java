package com.ybau.transaction.controller;


import com.ybau.transaction.service.InvoiceService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    /**
     * 查询已增加的发票地址
     *
     * @return
     */
    @PostMapping("/findInvoice")
    public ResponseData findInvoice(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return invoiceService.findInvoice(map, request);
    }

    /**
     * 根据记录ID删除发票记录
     *
     * @param invoicerecordId
     * @return
     */
    @GetMapping("/deleteInvoice")
    public ResponseData deleteInvoice(Integer invoicerecordId, HttpServletRequest request) {
        return invoiceService.deleteInvoice(invoicerecordId, request);
    }

    /**
     * 修改发票记录信息
     *
     * @param map
     * @return
     */
    @PostMapping("/updateInvoice")
    public ResponseData updateInvoice(@RequestBody Map<String, Object> map) {
        return invoiceService.updateInvoice(map);
    }

    /**
     * 根据名字查询发票信息
     *
     * @param unitName
     * @return
     */
    @GetMapping("/findByUnitName")
    public ResponseData findByUnitName(String unitName,Integer flag,HttpServletRequest request) {
        return invoiceService.findByUnitName(unitName,flag,request);
    }
}
