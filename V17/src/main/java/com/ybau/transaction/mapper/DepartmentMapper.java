package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Department;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 部门持久层
 */
@Repository
public interface DepartmentMapper {
    /**
     * 根据公司ID查询该公司所有部门信息（分页）
     *
     * @param map
     * @return
     */
    List<Department> findByOId(@Param(value = "map") Map<String, Object> map);

    /**
     * 插入公司部门
     *
     * @param map
     */
    void saveDepartment(@Param(value = "map") Map<String, Object> map);

    /**
     * 修改公司部门
     *
     * @param map
     */
    void updateDepartment(@Param(value = "map") Map<String, Object> map);

    /**
     * 删除公司
     * @param departmentId
     */
    void deleteDepartment(@Param(value = "departmentId") int departmentId);

    /**
     * 根据ID查询部门信息
     * @param departmentId
     * @return
     */
    Department findByDId(@Param(value = "departmentId") int departmentId);

    /**
     * 根据公司ID查询公司部门分组信息
     * @param organizationId
     * @return
     */
    List<Department> findDepartment(@Param(value = "organizationId") int organizationId);

    /**
     * 查询公司是否存在部门
     * @param id
     * @return
     */
    int findByCount(@Param(value = "id") int id);
}
