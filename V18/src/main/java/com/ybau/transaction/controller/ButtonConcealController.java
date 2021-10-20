package com.ybau.transaction.controller;

import com.ybau.transaction.service.ButtonConcealService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 按钮隐藏WEB层
 */

@RestController
@RequestMapping("/buttonConceal")
public class ButtonConcealController {
    @Autowired
    ButtonConcealService buttonConcealService;

    /**
     * 判断订单中心按钮是否展示
     *
     * @param request
     * @return
     */
    @GetMapping("/orderCenter")
    public ResponseData orderCenter(HttpServletRequest request) {
        return buttonConcealService.orderCenter(request);
    }

    /**
     * 查询一级目录
     *
     * @param request
     * @return
     */
    @GetMapping("/findCatalog")
    public ResponseData findCatalog(HttpServletRequest request) {
        return buttonConcealService.findCatalog(request);
    }

    /**
     * 查询是否有新增修改等用户权限
     *
     * @param request
     * @return
     */
    @GetMapping("/findByUser")
    public ResponseData findByUser(HttpServletRequest request) {
        return buttonConcealService.findByUser(request);
    }


    /**
     * 查询用户是否有新增修改角色权限
     *
     * @param request
     * @return
     */
    @GetMapping("/findByRole")
    public ResponseData findByRole(HttpServletRequest request) {
        return buttonConcealService.findByRole(request);
    }

    /**
     * 额度相关按钮控制
     *
     * @param request
     * @return
     */
    @GetMapping("/findQuota")
    public ResponseData findQuota(HttpServletRequest request) {
        return buttonConcealService.findQuota(request);
    }

    /**
     * 公司相关按钮控制
     *
     * @param request
     * @return
     */
    @GetMapping("/findOrganization")
    public ResponseData findOrganization(HttpServletRequest request) {
        return buttonConcealService.findOrganization(request);
    }

    /**
     * 分类按钮控制
     *
     * @param request
     * @return
     */
    @GetMapping("/findClassify")
    public ResponseData findClassify(HttpServletRequest request) {
        return buttonConcealService.findClassify(request);
    }

    /**
     * 商品按钮控制
     *
     * @param request
     * @return
     */
    @GetMapping("/findProduct")
    public ResponseData findProduct(HttpServletRequest request) {
        return buttonConcealService.findProduct(request);
    }

    /**
     * 库存按钮控制
     *
     * @param request
     * @return
     */
    @GetMapping("/findInventory")
    public ResponseData findInventory(HttpServletRequest request) {
        return buttonConcealService.findInventory(request);
    }


    /**
     * 合同相关按钮控制
     *
     * @param request
     * @return
     */
    @GetMapping("/findContract")
    public ResponseData findContract(HttpServletRequest request) {
        return buttonConcealService.findContract(request);
    }

    /**
     * 流程相关按钮控制
     * @param request
     * @return
     */
    @GetMapping("/findFlow")
    public ResponseData findFlow(HttpServletRequest request){
        return buttonConcealService.findFlow(request);
    }

    /**
     * 供应商相关按钮控制
     * @param request
     * @return
     */
    @GetMapping("/findSupplier")
    public ResponseData findSupplier(HttpServletRequest request){
        return buttonConcealService.findSupplier(request);
    }

    /**
     * 供应商用户相关按钮控制
     * @param request
     * @return
     */
    @GetMapping("/findSupplierUser")
    public ResponseData findSupplierUser(HttpServletRequest request){
        return buttonConcealService.findSupplierUser(request);
    }

}
