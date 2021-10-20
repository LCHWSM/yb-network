package com.ybau.transaction.service.serviceImpl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.UserSite;
import com.ybau.transaction.mapper.UserSiteMapper;
import com.ybau.transaction.service.UserSiteService;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class UserSiteServiceImpl implements UserSiteService {

    @Autowired
    UserSiteMapper userSiteMapper;
    @Autowired
    JwtUtil jwtUtil;

    /**
     * 查询用户地址（分页，筛选）
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData findUserSite(Map<String, Object> map, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        map.put("userId", id);
        int pageSize = (int) map.get("pageSize");
        int pageNum = (int) map.get("pageNum");
        PageHelper.startPage(pageNum, pageSize);
        List<UserSite> userSites = userSiteMapper.findUserSite(map);
        PageInfo pageInfo = new PageInfo(userSites);
        return new ResponseData(200, "查询成功", pageInfo);
    }

    /**
     * 新增用户地址信息
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public void saveUserSite(Map<String, Object> map, HttpServletRequest request) throws ParseException {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("userId", id);
        map.put("updateTime", df.parse(df.format(new Date())));
        int count = userSiteMapper.findByCount(map);
        if (count < 1) {
            //如果不存在则插入，如果存在则忽略
            userSiteMapper.saveUserSite(map);
        }
    }

    /**
     * 修改用户地址
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData updateUserSite(Map<String, Object> map) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("updateTime", df.parse(df.format(new Date())));
        map.put("site", map.get("receiverProvince").toString() + map.get("receiverCity") + map.get("receiverDistrict") + map.get("receiverStreet") + map.get("receiverAddress"));
        userSiteMapper.updateUserSite(map);
        return new ResponseData(200, "修改成功", null);
    }

    /**
     * 根据地址ID删除地址
     *
     * @param userSiteId
     * @return
     */
    @Override
    public ResponseData deleteUserSite(int userSiteId) {
        userSiteMapper.deleteUserSite(userSiteId);
        return new ResponseData(200, "删除成功", null);
    }

    @Override
    public ResponseData findByName(String receiverName, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        List<UserSite> userSites = userSiteMapper.findByName(receiverName, id);
        return new ResponseData(200, "查询成功", userSites);
    }
}
