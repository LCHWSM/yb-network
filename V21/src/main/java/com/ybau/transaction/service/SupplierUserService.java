package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface SupplierUserService {
    /**
     * 新增供应商信用户信息
     * @param map
     * @param request
     * @return
     */
    ResponseData saveSupplierUser(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 分页查询供应商用户信息
     * @param map
     * @return
     */
    ResponseData supplierByUser(Map<String, Object> map);

    /**
     * 修改供应商用户信息
     * @param map
     * @param request
     * @return
     */
    ResponseData updateSByUser(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 查询用户拥有的角色
     * @param userId
     * @return
     */
    ResponseData findByRole(String userId);

    /**
     * 修改供应商用户角色
     * @param map
     * @return
     */
    ResponseData updateUidByRid(Map<String,Object> map);
}
