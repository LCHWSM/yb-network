package com.ybau.transaction.controller;


import com.ybau.transaction.service.UserSiteService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/userSite")
public class UserSiteController {

    @Autowired
    UserSiteService userSiteService;

    /**
     * 查询用户已存地址（分页，筛选）
     *
     * @param map
     * @return
     */
    @PostMapping("/findUserSite")
    public ResponseData findUserSite(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return userSiteService.findUserSite(map, request);
    }



    /**
     * 修改用户地址
     *
     * @param map
     * @return
     */
    @PostMapping("/updateUserSite")
    public ResponseData updateUserSite(@RequestBody Map<String, Object> map) throws ParseException {
        return userSiteService.updateUserSite(map);
    }

    /**
     * 根据地址ID删除地址
     *
     * @param userSiteId
     * @return
     */
    @GetMapping("/deleteUserSite")
    public ResponseData deleteUserSite(int userSiteId) {
        return userSiteService.deleteUserSite(userSiteId);
    }

    /**
     * 根据名字查询收货地址
     *
     * @return
     */
    @GetMapping("/findByName")
    public ResponseData findByName(String receiverName,HttpServletRequest request) {
        return userSiteService.findByName(receiverName,request);
    }


}


