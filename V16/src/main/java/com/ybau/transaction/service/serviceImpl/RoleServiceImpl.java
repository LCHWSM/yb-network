package com.ybau.transaction.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ybau.transaction.domain.Permission;
import com.ybau.transaction.domain.Role;
import com.ybau.transaction.mapper.PermissionMapper;
import com.ybau.transaction.mapper.RoleMapper;
import com.ybau.transaction.service.RoleService;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    JwtUtil jwtUtil;


    /**
     * 删除用户关联权限
     *
     * @param id
     */
    @Override
    public void deleteByRole(String id) {
        roleMapper.deleteRole(id);
    }

    /**
     * 给用户添加权限
     *
     * @param map
     * @return
     */
    @Override
    public ResponseData saveRoleByUId(Map<String, Object> map) {
        ResponseData result = new ResponseData();
        List<Integer> roles = (List<Integer>) map.get("roles");
        roleMapper.deleteRole(map.get("uId").toString());
        for (Integer role : roles) {
            roleMapper.saveRoleByUId(map.get("uId").toString(), role);
        }
        result.setCode(200);
        result.setMsg("添加成功");
        return result;
    }

    /**
     * 根据角色ID删除角色
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData deleteRoleById(int id) {
        ResponseData result = new ResponseData();
        if (id == 1) {
            return new ResponseData(400, "超级管理员角色不可删除", null);
        }
        roleMapper.deleteRoleById(id);
        permissionMapper.deleteByRId(id);//删除角色下属的权限
        result.setCode(200);
        result.setMsg("删除成功");
        return result;
    }

    /**
     * 查询用户列表（分页）
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public ResponseData findAll(int pageSize, int pageNum, String roleName) {
        ResponseData result = new ResponseData();
        PageHelper.startPage(pageNum, pageSize);
        List<Role> roles = roleMapper.findAll(roleName);
        PageInfo pageInfo = new PageInfo(roles);
        result.setCode(200);
        result.setMsg("查询成功");
        result.setData(pageInfo);
        return result;
    }

    /**
     * 新增角色
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData saveRole(Map<String, Object> map, HttpServletRequest request) {
        int count = roleMapper.findByName((String) map.get("name"));
        if (count > 0) {
            return new ResponseData(400, "角色名字已存在，新增失败", null);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);//获取用户名
        map.put("addUser", id);

        try {
            map.put("addTime", df.parse(df.format(new Date())));
        } catch (Exception e) {
            log.error("{}", e);
        }
        roleMapper.saveRole(map);
        List<Integer> pIds = (List<Integer>) map.get("pIds");
        for (Integer pId : pIds) {
            permissionMapper.savePermissionByRId(pId, Integer.parseInt(map.get("id").toString()));//插入角色关联权限
        }
        return new ResponseData(200, "插入成功", null);
    }

    /**
     * 修改角色
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResponseData updateRole(Map<String, Object> map, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String token = request.getHeader("token");//获取请求头token
        String id = jwtUtil.getId(token);//获取用户名
        map.put("updateUser", id);
        try {
            map.put("updateTime", df.parse(df.format(new Date())));
            roleMapper.updateRole(map);
            permissionMapper.deleteByRId((Integer) map.get("id"));//删除角色下属的权限
            List<Integer> pIds = (List<Integer>) map.get("pIds");
            for (Integer pId : pIds) {
                permissionMapper.savePermissionByRId(pId, (Integer) map.get("id"));//插入角色关联权限
            }
            return new ResponseData(200,"修改成功",null);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseData(400,"该角色名已存在",null);
        }

    }

    /**
     * 根据角色ID查询角色拥有的权限
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData findByRId(Integer id) {
        List<Permission> permissions = roleMapper.findByRId(id);
        return new ResponseData(200,"查询成功",permissions);
    }

    /**
     * 根据角色Id查询角色没有拥有的权限
     *
     * @param id
     * @return
     */
    @Override
    public ResponseData findOtherByRId(Integer id) {
        List<Permission> permissions = roleMapper.findOtherByRId(id);
        return new ResponseData(200,"查询成功",permissions);
    }

}
