package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Organization;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface OrganizationMapper {

    /**
     * 根据用户ID查询所属公司信息
     *
     * @param id
     * @return
     */
    Organization findByUId(@Param(value = "id") String id);

    /**
     * 查询所有公司信息（筛选）
     *
     * @param map
     * @return
     */
    List<Organization> findAll(@Param(value = "map")Map<String, Object> map);

    /**
     * 新增公司
     *
     * @param map
     */
    void saveOrganization(@Param(value = "map")Map<String, Object> map);

    /**
     * 根据ID删除公司
     *
     * @param id
     */
    void deleteById(@Param(value = "id")int id);

    /**
     * 更新公司信息
     *
     * @param name
     * @param username
     * @param updateTime
     * @param id
     */
    void updateOrganization(@Param(value = "name") String name, @Param(value = "username")String username, @Param(value = "updateTime")Date updateTime, @Param(value = "id")int id,@Param(value = "settingTime") Integer settingTime,@Param(value = "way") String way);

    /**
     * 查询公司信息（未分页）
     *
     * @return
     */
    List<Organization> findOrganization();

    /**
     * 根据ID查询公司信息
     *
     * @param id
     * @return
     */
    Organization findByOId(@Param(value = "id") int id);

    /**
     * 根据公司名字查询是否存在公司
     *
     * @param organizationName
     * @return
     */
    int findByName(@Param(value = "organizationName") String organizationName);


    /**
     * 增加额度
     *
     * @param map
     */
    void updateAddLimit(@Param(value = "map") Map<String, Object> map);

    /**
     * 减少额度
     *
     * @param map
     */
    void updateSubLimit(@Param(value = "map") Map<String, Object> map);

    /**
     * 核销额度
     *
     * @param map
     */
    int updateUsable(@Param(value = "map") Map<String, Object> map);

    /**
     * 下单减少额度
     * @param map
     * @return
     */
    int subLimit(@Param(value = "map")Map<String, Object> map);

    /**
     * 根据公司名字查询公司信息
     * @param organizationName
     * @return
     */
    Organization findByOrgName(@Param(value = "organizationName")String organizationName);

    /**
     * 校验已用额度方法
     * @param sub
     * @param organizationId
     */
    void verifyLimit(@Param(value = "sub") double sub,@Param(value = "organizationId") int organizationId);

    /**
     * 根据公司ID查询该公司可用额度
     * @param organizationId
     * @return
     */
    String findAvailableBalance(@Param(value = "organizationId") int organizationId);


    /**
     * 根据公司使用的分组ID查询公司所使用的流程
     * @param processGroupId
     * @return
     */
    Organization findByProcessGroupId(@Param(value = "processGroupId") int processGroupId);

    /**
     * 根据ID更新订单修改金额后的额度信息
     * @param sub
     * @param addCompany
     */
    void updateLimit(@Param(value = "sub") double sub,@Param(value = "addCompany") int addCompany);

    /**
     * 查询公司额度  分页  （筛选）
     *
     * @param organizationName (公司名字筛选 条件)
     * @return
     */
    List<Organization> findLimit(@Param(value = "organizationName") String organizationName);

    /**
     * 插入公司可下单的供应商信息
     * @param supplierId
     * @param organizationId
     */
    void saveSupplier(@Param(value = "supplierId") Integer supplierId, @Param(value = "organizationId") Integer organizationId);

    /**
     * 根据公司ID删除与供应商的关联信息
     * @param organizationId
     */
    void deleteSupplier(@Param(value = "organizationId") Integer organizationId);

    /**
     * 查询所有公司ID
     * @return
     */
    List<String> findById();


}
