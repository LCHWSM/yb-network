package com.ybau.transaction.controller;

import com.ybau.transaction.service.DepartmentService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

/**
 * 部门控制层
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    /**
     * 根据公司ID查询公司所有部门（分页）
     *
     * @param map(pageSize pageNum organizationId(公司ID) departmentName（筛选条件）departmentNumber（筛选条件）   )
     * @return
     */
    @PostMapping("/findByOId")
    public ResponseData findByOId(@RequestBody Map<String, Object> map) {
        return departmentService.findByOId(map);
    }

    /**
     * 为公司新增部门
     *
     * @param map
     * @return
     */
    @PostMapping("/saveDepartment")
    public ResponseData saveDepartment(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return departmentService.saveDepartment(map, request);
    }

    /**
     * 修改公司部门
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateDepartment")
    public ResponseData updateDepartment(@RequestBody Map<String, Object> map, HttpServletRequest request) throws ParseException {
        return departmentService.updateDepartment(map, request);
    }

    /**
     * 根据部门ID删除部门
     *
     * @param departmentId
     * @return
     */
    @GetMapping("/deleteDepartment")
    public ResponseData deleteDepartment(int departmentId) {
        return departmentService.deleteDepartment(departmentId);
    }

    /**
     * 根据公司ID查询公司下属部门分组信息
     *
     * @param organizationId
     * @return
     */
    @GetMapping("/findDepartment")
    public ResponseData findDepartment(int organizationId) {
        return departmentService.findDepartment(organizationId);
    }
}
