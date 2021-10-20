package com.ybau.transaction.controller;

import com.ybau.transaction.domain.Company;
import com.ybau.transaction.service.ExpressService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/express")
public class ExpressController {


    @Autowired
    private ExpressService expressService;


    /**
     * 查询所有快递公司
     *
     * @return
     */
    @RequestMapping("/findAll")
    public ResponseData findAll() {
        List<Company> companies = expressService.findAll();
        return new ResponseData(200, "查询成功", companies);
    }

    /**
     * 快递单号传入接口
     *
     * @param map
     * @return
     */
    @RequestMapping("/saveExpress")
    public ResponseData saveExpress(@RequestBody Map<String, Object> map) {
        return expressService.saveExpress(map);

    }


    /**
     * 调用粤宝网络接口
     *
     * @param map
     * @return
     */
    @RequestMapping("/saveByExpress")
    public ResponseData saveByExpress(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return expressService.saveByExpress(map, request);
    }

    /**
     * 修改快递单号
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateExpress")
    public ResponseData updateExpress(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return expressService.updateExpress(map, request);
    }

    /**
     * 填写退货信息
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/refundCargo")
    public ResponseData refundCargo(@RequestBody Map<String,Object> map,HttpServletRequest request) throws ParseException {
        return expressService.refundCargo(map,request);
    }

    /**
     * 修改退货信息
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateRefundCargo")
    public ResponseData updateRefundCargo(@RequestBody Map<String,Object> map,HttpServletRequest request) throws ParseException {
        return expressService.updateRefundCargo(map,request);
    }

    /**
     * 退货成功按钮
     * @param map
     * @return
     */
    @PostMapping ("/refundCargoSucceed")
    public ResponseData refundCargoSucceed(@RequestBody Map<String,Object> map,HttpServletRequest request) throws ParseException {
        return expressService.refundCargoSucceed(map,request);
    }


}
