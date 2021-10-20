package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface DepartmentService {
    /**
     * 根据公司ID查询该公司所有部门（分页）
     * @param map
     * @return
     */
    ResponseData findByOId(Map<String, Object> map);

    /**
     * 为公司新增部门
     * @param map
     * @param request
     * @return
     */
    ResponseData saveDepartment(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 修改公司部门
     * @param map
     * @param request
     * @return
     */
    ResponseData updateDepartment(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 根据部门ID删除该部门
     * @param departmentId
     * @return
     */
    ResponseData deleteDepartment(int departmentId);

    /**
     * 根据公司ID查询公司下属部门分组信息
     * @param organizationId
     * @return
     */
    ResponseData findDepartment(int organizationId);
}
