package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Department;
import com.ybau.transaction.mapper.DepartmentMapper;
import com.ybau.transaction.mapper.GroupingMapper;
import com.ybau.transaction.mapper.UserMapper;
import com.ybau.transaction.service.DepartmentService;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 部门逻辑层
 */
@Service
@Slf4j
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    GroupingMapper groupingMapper;

    /**
     * 根据公司ID查询该公司所有部门（分页）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findByOId(Map<String, Object> map) {
        PageHelper.startPage((int) map.get("pageNum"), (int) map.get("pageSize"));
        List<Department> departments = departmentMapper.findByOId(map);
        PageInfo pageInfo = new PageInfo(departments);
        return new ResponseData(200, "查询成功", pageInfo);
    }


    /**
     * 为公司新增部门
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveDepartment(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("addUser", id);
        map.put("addTime", df.parse(df.format(new Date())));
        try {
            departmentMapper.saveDepartment(map);
            return new ResponseData(200, "新增成功", null);
        } catch (Exception e) {
            return new ResponseData(400, "部门名称或部门编号重复，新增失败", null);
        }

    }

    /**
     * 修改公司部门
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateDepartment(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("updateUser", id);
        map.put("updateTime", df.parse(df.format(new Date())));
        try {
            departmentMapper.updateDepartment(map);
            return new ResponseData(200, "修改成功", null);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400, "部门名称或部门编号重复，修改失败", null);
        }
    }

    /**
     * 根据部门ID删除该部门
     *
     * @param departmentId
     * @return
     */
    @Override
    public ResponseData deleteDepartment(int departmentId) {
        int count = userMapper.findByDepartmentId(departmentId);//查询该部门是否存在用户
        int grouCount = groupingMapper.findBycount(departmentId);
        if (count > 0) {
            return new ResponseData(400, "该部门下存在用户，删除失败", null);
        }
        if (grouCount > 0) {
            return new ResponseData(400, "该部门存在下属分组，删除失败", null);
        }
        departmentMapper.deleteDepartment(departmentId);
        return new ResponseData(200, "删除成功", null);
    }

    /**
     * 根据公司ID查询公司下属部门分组信息
     *
     * @param organizationId
     * @return
     */
    @Override
    public ResponseData findDepartment(int organizationId) {
        List<Department> departments = departmentMapper.findDepartment(organizationId);
        return new ResponseData(200, "查询成功", departments);
    }
}
