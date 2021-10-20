package com.ybau.transaction.service.serviceImpl;

import com.ybau.transaction.domain.Permission;
import com.ybau.transaction.mapper.PermissionMapper;
import com.ybau.transaction.service.PermissionService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionMapper permissionMapper;



    /**
     * 查询所有权限
     *
     * @return
     */
    @Override
    public ResponseData findAll() {
        ResponseData result = new ResponseData();
        List<Permission> permissions = permissionMapper.findAll();
        result.setCode(200);
        result.setMsg("查询成功");
        result.setData(permissions);
        return result;
    }


}
