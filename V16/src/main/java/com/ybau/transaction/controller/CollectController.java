package com.ybau.transaction.controller;


import com.ybau.transaction.domain.Goods;
import com.ybau.transaction.service.CollectService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 购物车功能控制层
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    CollectService collectService;

    /**
     * 为用户新增购物车
     *
     * @param
     * @param request
     * @return
     */
    @PostMapping("/saveCollect")
    public ResponseData saveCollect(@RequestBody List<Goods> list, HttpServletRequest request) {
        return collectService.saveCollect(list, request);
    }

    /**
     * 查询用户购物车数据
     *
     * @param request
     * @return
     */
    @GetMapping("/findCollect")
    public ResponseData findCollect(HttpServletRequest request) {
        return collectService.findCollect(request);
    }

    /**
     * 修改购物车内容
     *
     * @param list
     * @return
     */
    @PostMapping("/updateCollect")
    public ResponseData updateCollect(@RequestBody List<Map<String, Object>> list, HttpServletRequest request) {
        return collectService.updateCollect(list, request);
    }
}
