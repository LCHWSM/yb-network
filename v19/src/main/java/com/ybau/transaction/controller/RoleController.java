package com.ybau.transaction.controller;

import com.ybau.transaction.service.RoleService;
import com.ybau.transaction.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 添加用户角色
     *
     * @param map
     * @return
     */
    @RequestMapping("/updateRoleByUId")
    public ResponseData updateRoleByUId(
            @RequestBody Map<String, Object> map) {
        return roleService.saveRoleByUId(map);
    }

    /**
     * 根据角色ID删除角色
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteRoleById")
    public ResponseData deleteRoleById(Integer id) {
        return roleService.deleteRoleById(id);
    }

    /**
     * 查询角色列表（分页）
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/findAll")
    public ResponseData findAll(int pageSize, int pageNum, String roleName) {
        return roleService.findAll(pageSize, pageNum, roleName);
    }

    /**
     * 新增角色
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/saveRole")
    public ResponseData saveRole(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return roleService.saveRole(map, request);
    }

    /**
     * 修改角色
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/updateRole")
    public ResponseData updateRole(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        return roleService.updateRole(map, request);
    }

    /**
     * 根据Id查询角色拥有的权限
     *
     * @param id
     * @return
     */
    @GetMapping("/findByRId")
    public ResponseData findByRId(Integer id) {
        return roleService.findByRId(id);
    }

    /**
     * 根据角色Id查询角色没有拥有的权限
     * @param id
     * @return
     */
    @GetMapping("/findOtherByRId")
    public ResponseData findOtherByRId(Integer id) {
        return roleService.findOtherByRId(id);
    }
}
