package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Organization;
import com.ybau.transaction.domain.Role;
import com.ybau.transaction.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {


    //根据账号和密码查询是否有这个用户

    User selectByUserNameAndPassword(@Param(value = "map") Map<String, String> map);


    /**
     * 插入新用户
     *
     * @param map
     * @throws Exception
     */

    void saveUser(@Param(value = "map") Map<String, Object> map) throws Exception;

    /**
     * 查询用户是否重复
     *
     * @param userName
     * @return
     */

    int findByUsername(@Param(value = "userName") String userName) throws Exception;

    /**
     * 查询公司列表所有用户
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "username", property = "userName"),
            @Result(column = "email", property = "email"),
            @Result(column = "Flag", property = "flag"),
            @Result(column = "addTime", property = "addTime"),
            @Result(column = "auUserName", property = "addUser"),
            @Result(column = "auName", property = "addName"),
            @Result(column = "updateTime", property = "updateTime"),
            @Result(column = "uuUserName", property = "updateUser"),
            @Result(column = "uuName", property = "updateName"),
            @Result(column = "name", property = "name"),
            @Result(column = "userPhone", property = "userPhone"),
            @Result(column = "organizationId", property = "organizationId"),
            @Result(column = "uId", property = "user", one = @One(select = "com.ybau.transaction.mapper.UserMapper.findByUid")),
            @Result(column = "organizationId", property = "organization", one = @One(select = "com.ybau.transaction.mapper.OrganizationMapper.findByOId")),
            @Result(column = "departmentId", property = "department", one = @One(select = "com.ybau.transaction.mapper.DepartmentMapper.findByDId")),
            @Result(column = "groupingId", property = "grouping", one = @One(select = "com.ybau.transaction.mapper.GroupingMapper.findByGId")),
    })
    @Select("<script> SELECT u.departmentId,u.groupingId,u.id,u.userPhone,u.username,u.email,u.flag,u.addTime,u.uId,u.updateTime,u.organizationId,u.name,au.username auUserName,au.name auName,uu.username uuUserName,uu.name uuName FROM user u " +
            "  LEFT JOIN user au ON u.addUser=au.id" +
            "  LEFT JOIN user uu ON u.updateUser=uu.id" +
            " where 1=1 and u.sign = 1 " +
            " <if test='map.username!=null and map.username!=\"\"'>and u.username like CONCAT('%',#{map.username},'%')</if>" +
            " <if test='map.name!=null and map.name!=\"\"'>and u.name like CONCAT('%',#{map.name},'%')</if>" +
            " <if test='map.userPhone!=null and map.userPhone!=\"\"'>and u.userPhone like CONCAT('%',#{map.userPhone},'%')</if>" +
            " <if test='map.organizationId!=null and map.organizationId!= \"\"'>and u.organizationId =#{map.organizationId}</if>" +
            " ORDER BY addTime desc" +
            " </script>")
    List<User> findByUser(@Param(value = "map") Map<String, Object> map) throws Exception;

    @Select("select id,username,name,organizationId from user where id=#{uId} and sign=1")
    User findByUid(@Param(value = "uId") String uId);


    /**
     * 更新关联关系
     *
     * @param id
     */
    @Update("update user SET uId =NULL WHERE uId=#{id}")
    void updateByuId(@Param(value = "id") String id);

    /**
     * 修改密码
     *
     * @param map
     */

    void updatePwd(@Param(value = "map") Map<String, String> map) throws Exception;

    /**
     * 修改用户
     *
     * @param map
     */
    void updateUser(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询所有用户
     *
     * @return
     */
    @Select("<script> " +
            "select * from user where  1=1 and organizationId=#{organizationId} " +
            " <if test='id!=\"null\"'>and not id= #{id} </if> " +
            " <if test='id!=\"null\"'>and not uid=#{id} </if>" +
            "</script>")
    List<User> findAll(@Param(value = "id") String id, @Param(value = "organizationId") Integer organizationId);

    /**
     * 根据ID查询用户下所有权限
     *
     * @param uId
     * @return
     */
    List<Role> findByRole(@Param(value = "uId") String uId);

    /**
     * 根据ID查询该用户可以添加的权限
     *
     * @param uId
     * @return
     */
    List<Role> findOtherRole(@Param(value = "uId") String uId);

    /**
     * 根据ID查询用户信息
     *
     * @param uId
     * @return
     */
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "username", property = "userName"),
            @Result(column = "email", property = "email"),
            @Result(column = "Flag", property = "flag"),
            @Result(column = "addTime", property = "addTime"),
            @Result(column = "addUser", property = "addUser"),
            @Result(column = "updateTime", property = "updateTime"),
            @Result(column = "updateUser", property = "updateUser"),
            @Result(column = "name", property = "name"),
            @Result(column = "userPhone", property = "userPhone"),
            @Result(column = "uid", property = "uId"),
            @Result(column = "organizationId", property = "organizationId"),
            @Result(column = "organizationId", property = "organization", one = @One(select = "com.ybau.transaction.mapper.OrganizationMapper.findByOId")),
    })
    @Select("select id,username,email,Flag,addTime,uId,addUser,updateTime,updateUser,organizationId,name,userPhone,departmentId,groupingId from user where id=#{uId}")
    User findById(@Param(value = "uId") String uId);

    @Update("update user set flag=#{i} where username=#{username} ")
    void updateFlag(@Param(value = "i") int i, @Param(value = "username") String username);

    /**
     * 更改错误次数
     *
     * @param username
     */
    void updateByUserCount(@Param(value = "username") String username);

    /**
     * 根据用户名查询该用户密码错误次数
     *
     * @param username
     * @return
     */
    User findByUserCount(@Param(value = "username") String username);

    /**
     * 更新用户密码错误次数（为0）
     *
     * @param username
     */
    void updateByCount(@Param(value = "username") String username);

    /**
     * 根据公司ID查询是否有下属用户
     *
     * @param id
     * @return
     */
    int findByOrganizationId(@Param(value = "id") int id);


    /**
     * 根据用户ID修改用户邮箱手机号
     *
     * @param map
     */
    void updateEmailByPhone(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询部门下是否存在用户
     *
     * @param departmentId
     * @return
     */
    int findByDepartmentId(@Param(value = "departmentId") int departmentId);

    /**
     * 查询分组是否存在用户
     *
     * @param groupingId
     * @return
     */
    int findByGroupingId(@Param(value = "groupingId") int groupingId);

    /**
     * 根据公司ID查询
     *
     * @param
     * @return
     */
    List<Organization> findByOId(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据公司ID查询所有用户
     *
     * @param organizationId
     * @return
     */
    List<User> findOId(@Param(value = "organizationId") Integer organizationId);

    /**
     * 根据订单ID查询该订单是否已经被该用户审核过
     *
     * @param orderId
     * @param id
     * @return
     */
    int findUserByOrd(@Param(value = "orderId") String orderId, @Param(value = "id") String id);

    /**
     * 插入用户审核的订单
     *
     * @param orderId
     * @param id
     */
    void saveUserByOrder(@Param(value = "orderId") String orderId, @Param(value = "id") String id);

    /**
     * 查询拥有定制订单新增操作记录的用户
     *
     * @param url
     * @return
     */
    List<Map> findSaveCoLog(@Param(value = "url") String url);

    /**
     * 根据定制订单ID查询跟进人信息
     *
     * @param coId
     * @return
     */
    List<User> findByCoId(@Param(value = "coId") int coId);

    /**
     * 分页查询供应商用户信息
     *
     * @param map
     * @return
     */
    List<User> supplierByUser(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询供应商是否存在下属用户
     *
     * @param supplierId
     * @return
     */
    int findBySupplierId(@Param(value = "supplierId") int supplierId);

    /**
     * 查询拥有向供应商下单权限的用户
     *
     * @param map
     * @param url
     * @return
     */
    List<Map> supplierUser(@Param(value = "map") Map<String, Object> map, @Param(value = "url") String url, @Param(value = "organizationId") int organizationId, @Param(value = "sign") int sign);

    /**
     * 根据用户名查询用户属于供应商||公司
     *
     * @param username
     * @return
     */
    String findUserName(@Param(value = "username") String username);

    /**
     * 供应商用户登录
     *
     * @param user_message
     * @return
     */
    User findSupplierUser(@Param(value = "map") Map<String, String> user_message);

    /**
     * 根据用户名查询用户ID
     *
     * @param aUser
     * @return
     */
    Integer findUserByName(@Param(value = "aUser") String aUser);
}
