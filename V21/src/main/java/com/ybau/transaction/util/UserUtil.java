package com.ybau.transaction.util;

import com.ybau.transaction.domain.User;
import com.ybau.transaction.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户工具类
 */
@Component
public class UserUtil {

    @Autowired
    UserMapper userMapper;

    public String findSuperior(String uId) {
        User user = userMapper.findById(uId);
        User superiorUser = userMapper.findById(user.getUId());//此用户上级信息
        if (superiorUser != null && superiorUser.getDepartmentId() == user.getDepartmentId()) {
            //判断两个用户是否是在同一个部门 如果在同一个部门继续查询
            String id=findSuperior(superiorUser.getId());
            return id;
        }
        //查询到不是一个部门的 返回上一个ID
      return uId;
    }
}

