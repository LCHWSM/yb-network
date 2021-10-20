package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface UserSiteService {

    /**
     * 查询用户已存地址 （分页，筛选）
     *
     * @param map
     * @return
     */
    ResponseData findUserSite(Map<String, Object> map, HttpServletRequest request);

    /**
     * 新增用户地址
     *
     * @param map
     * @param request
     * @return
     * @throws ParseException
     */

    void saveUserSite(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    /**
     * 修改用户地址
     *
     * @param map
     * @return
     */
    ResponseData updateUserSite(Map<String, Object> map) throws ParseException;

    /**
     * 根据地址ID删除地址
     *
     * @param userSiteId
     * @return
     */
    ResponseData deleteUserSite(int userSiteId);

    /**
     * 根据收件人名字查询地址
     *
     * @param receiverName
     * @param request
     * @return
     */
    ResponseData findByName(String receiverName, HttpServletRequest request);
}
