package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Permission;
import com.ybau.transaction.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PermissionMapper {
   /**
    * 查询用户下属权限
    * @param uid
    * @return
    */
   List<Permission> findByUserId(@Param(value = "uid") String uid);

   /**
    * 删除角色——权限关联信息
    * @param id
    */
   void deleteByRId(@Param(value = "rid")int id);

   /**
    * 为角色新增权限
    * @param pId
    * @param rId
    */
   void savePermissionByRId(@Param(value = "pId") Integer pId,@Param(value = "rId") int rId);

   /**
    * 查询所有权限
    * @return
    */
   List<Permission> findAll();

   /**
    * 根据审核状态查询此状态下拥有权限的人
    * @param audit
    * @return
    */
   List<User> findByAudit(@Param(value = "audit")String audit);
}
