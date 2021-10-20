package com.ybau.transaction.controller;

import com.ybau.transaction.service.PermissionService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限WEB层
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 查询所有权限
     *
     * @return
     */
    @GetMapping("/findAll")
    public ResponseData findAll() {
        return permissionService.findAll();
    }


}
