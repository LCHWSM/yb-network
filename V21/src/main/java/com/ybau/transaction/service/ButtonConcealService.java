package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;

public interface ButtonConcealService {
    /**
     * 判断订单中心按钮是否展示
     * @param request
     * @return
     */
    ResponseData orderCenter(HttpServletRequest request);

    /**
     * 判断目录是否展示
     * @param request
     * @return
     */
    ResponseData findCatalog(HttpServletRequest request);

    /**
     * 查看是否有新增修改用户权限
     * @param request
     * @return
     */
    ResponseData findByUser(HttpServletRequest request);

    /**
     * 查询是否有新增 修改角色权限
     * @param request
     * @return
     */
    ResponseData findByRole(HttpServletRequest request);

    /**
     * 额度相关按钮控制
     * @param request
     * @return
     */
    ResponseData findQuota(HttpServletRequest request);

    /**
     * 公司相关按钮控制
     * @param request
     * @return
     */
    ResponseData findOrganization(HttpServletRequest request);

    /**
     * 分类相关按钮控制
     * @param request
     * @return
     */
    ResponseData findClassify(HttpServletRequest request);

    /**
     * 商品按钮控制
     * @param request
     * @return
     */
    ResponseData findProduct(HttpServletRequest request);

    /**
     * 库存按钮控制
     * @param request
     * @return
     */
    ResponseData findInventory(HttpServletRequest request);

    /**
     * 合同相关按钮控制
     * @param request
     * @return
     */
    ResponseData findContract(HttpServletRequest request);

    /**
     * 流层相关按钮控制
     * @param request
     * @return
     */
    ResponseData findFlow(HttpServletRequest request);

    /**
     * 供应商相关按钮控制
     * @param request
     * @return
     */
    ResponseData findSupplier(HttpServletRequest request);

    /**
     * 供应商用户相关按钮控制
     * @param request
     * @return
     */
    ResponseData findSupplierUser(HttpServletRequest request);
}
