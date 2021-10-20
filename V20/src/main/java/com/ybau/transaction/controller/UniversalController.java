package com.ybau.transaction.controller;

import com.ybau.transaction.service.UniversalService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/universal")
public class UniversalController {

    @Autowired
    UniversalService universalService;

    /**
     * 修改通用库存预警
     *
     * @param red
     * @param yellow
     * @return
     */
    @GetMapping("/updateWarning")
    public ResponseData updateWarning(int red, int yellow) {
        return universalService.updateWarning(red, yellow);
    }

    /**
     * 查询通用库存预警
     *
     * @return
     */
    @GetMapping("/findAll")
    public ResponseData findAll() {
        return universalService.findAll();
    }
}
