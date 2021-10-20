package com.ybau.transaction.service;


import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface RoleService {


    void deleteByRole(String id);


    ResponseData saveRoleByUId(Map<String, Object> map);

    ResponseData deleteRoleById(int id);

    ResponseData findAll(int pageSize, int pageNum, String roleName);

    ResponseData saveRole(Map<String, Object> map, HttpServletRequest request);

    ResponseData updateRole(Map<String, Object> map, HttpServletRequest request);

    ResponseData findByRId(Integer id);

    ResponseData findOtherByRId(Integer id);

}
