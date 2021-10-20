package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface UserService {


    ResponseData saveUser(Map<String, Object> map, HttpServletRequest request);

    ResponseData findByUser(Map<String, Object> map);

//    ResponseData deleteById(String id,String username);

    ResponseData updatePwd(Map<String, String> map,HttpServletRequest request);

    ResponseData login(Map<String, Object> params) throws ParseException;

    ResponseData updateUser(Map<String, Object> map, HttpServletRequest request);

    ResponseData findAll(String id, Integer organizationId);

    ResponseData findByRole(String uId);

    ResponseData findOtherRole(String uId);

    ResponseData findById(String uId);

    int findByOrganizationId(int id);


    ResponseData updateEmailByPhone(Map<String, Object> map, HttpServletRequest request);


    ResponseData findByOId(Map<String,Object> map);

    ResponseData findSaveCoLog();

    ResponseData supplierUser(Map<String, Object> map);
}
