package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface SupplierService {


    /**
     * 新增供应商
     * @param map
     * @param request
     * @return
     */
    ResponseData saveSupplier(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 查询所有供应商 （分页 筛选）
     * @param map
     * @return
     */
    ResponseData findAll(Map<String, Object> map);

    /**
     * 修改 供应商信息
     * @param map
     * @param request
     * @return
     */
    ResponseData updateSupplier(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 根据供应商ID删除供应商信息
     * @param supplierId
     * @return
     */
    ResponseData deleteById(int supplierId);

    /**
     * 查询供应商信息
     * @return
     */
    ResponseData findSupplier(String supplierName,Integer pageSize,Integer pageNum,HttpServletRequest request);


    /**
     * 根据供应商名字查询
     * @param supplierName
     * @param pageSize
     * @param pageNum
     * @return
     */
    ResponseData findBySupplier(String supplierName, Integer pageSize, Integer pageNum);
}
