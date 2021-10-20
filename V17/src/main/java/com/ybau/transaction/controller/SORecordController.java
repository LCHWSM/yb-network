package com.ybau.transaction.controller;


import com.ybau.transaction.service.SORecordService;
import com.ybau.transaction.util.MakeOrderNumUtil;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/soRecord")
public class SORecordController {

    @Autowired
    SORecordService soRecordService;

    /**
     * 根据用户ID查询所有用户填写的供应商商品信息
     *
     * @param map
     * @return
     */
    @PostMapping("/findAllById")
    public ResponseData findAllById(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return soRecordService.findAllById(map, request);
    }

    /**
     * 修改记录的商品信息
     *
     * @param map
     * @return
     */
    @PostMapping("/updateRecord")
    public ResponseData updateRecord(@RequestBody Map<String, Object> map) {
        return soRecordService.updateRecord(map);
    }

    /**
     * 删除已添加商品记录
     *
     * @param soRecordId
     * @return
     */
    @GetMapping("/deleteRecord")
    public ResponseData deleteRecord(int soRecordId) {
        return soRecordService.deleteRecord(soRecordId);
    }



}
