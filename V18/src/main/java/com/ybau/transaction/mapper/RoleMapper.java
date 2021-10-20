package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Permission;
import com.ybau.transaction.domain.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMapper {


    /**
     * 删除用户——角色中间表信息
     *
     * @param id
     */
    void deleteRole(@Param(value = "id") String id);

    /**
     * 用户添加权限
     *
     * @param uId
     * @param role
     */

    void saveRoleByUId(@Param(value = "uId") String uId, @Param(value = "role") Integer role);

    /**
     * 删除角色
     *
     * @param id
     */
    void deleteRoleById(@Param(value = "id") int id);

    /**
     * 查询角色列表
     *
     * @return
     */
    List<Role> findAll(@Param(value = "roleName") String roleName);

    /**
     * 新增角色
     *
     * @param map
     * @return
     */
    int saveRole(@Param(value = "map") Map<String, Object> map);

    /**
     * 更新角色
     *
     * @param map
     */
    void updateRole(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据角色ID查询角色拥有的权限
     *
     * @param id
     * @return
     */
    List<Permission> findByRId(@Param(value = "id") Integer id);

    /**
     * 查询角色没有的权限
     *
     * @param id
     * @return
     */
    List<Permission> findOtherByRId(@Param(value = "id") Integer id);

    /**
     * 根据角色名字查询是否重复
     *
     * @param name
     * @return
     */
    int findByName(@Param(value = "name") String name);
}
