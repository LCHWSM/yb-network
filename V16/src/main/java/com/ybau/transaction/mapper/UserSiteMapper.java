package com.ybau.transaction.mapper;


import com.ybau.transaction.domain.UserSite;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Repository
public interface UserSiteMapper {
    /**
     * 查询用户已存地址列表（分页，筛选）
     * @param map
     * @return
     */
    List<UserSite> findUserSite(@Param(value = "map") Map<String, Object> map);

    /**
     * 新增用户收货地址
     * @param map
     */
    void saveUserSite(@Param(value = "map") Map<String, Object> map);

    /**
     * 修改用户地址
     * @param map
     */
    void updateUserSite(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据ID删除地址
     * @param userSiteId
     */
    void deleteUserSite(@Param(value = "userSiteId") int userSiteId);

    /**
     * 根据名字查询用户地址
     * @param receiverName
     * @param id
     * @return
     */
    List<UserSite> findByName(@Param("receiverName") String receiverName, @Param(value = "id") String id);

    /**
     * 查询所有收货信息是否重复
     * @param map
     * @return
     */
    int findByCount(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询地址是否重复
     * @param receiverName
     * @param receiverMobile
     * @param receiverProvince
     * @param receiverCity
     * @param receiverDistrict
     * @param receiverStreet
     * @param receiverAddress
     * @param site
     * @param addUser
     * @return
     */
    int findCount(@Param(value = "receiverName") String receiverName,@Param(value = "receiverMobile") String receiverMobile,@Param(value = "receiverProvince") String receiverProvince,@Param(value = "receiverCity") String receiverCity,@Param(value = "receiverDistrict") String receiverDistrict,@Param(value = "receiverStreet") String receiverStreet,@Param(value = "receiverAddress") String receiverAddress,@Param(value = "site") String site,@Param(value = "addUser") String addUser);

    void saveSite(@Param(value = "receiverName") String receiverName,@Param(value = "receiverMobile") String receiverMobile,@Param(value = "receiverProvince") String receiverProvince,@Param(value = "receiverCity") String receiverCity,@Param(value = "receiverDistrict") String receiverDistrict,@Param(value = "receiverStreet") String receiverStreet,@Param("receiverAddress") String receiverAddress,@Param(value = "site") String site,@Param(value = "addUser") String addUser);
}
