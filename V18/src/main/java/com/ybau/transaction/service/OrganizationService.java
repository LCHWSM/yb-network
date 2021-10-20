package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;
import org.apache.http.protocol.ResponseDate;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface OrganizationService {
    ResponseData findAll(Map<String, Object> map);

    ResponseData findByUId(HttpServletRequest request);

    ResponseData saveOrganization(Map<String, Object> map, HttpServletRequest request);

    ResponseData deleteById(int id, String name);

    ResponseData updateOrganization(String name, HttpServletRequest request, int id,Integer settingTime,String way);

    ResponseData findOrganization();

    ResponseData updateLimit(Map<String, Object> map, HttpServletRequest request);

    ResponseData updateUsable(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData verifyLimit(int organizationId);

    ResponseData paymentStatus(Map<String, Object> map,HttpServletRequest request) throws ParseException, IllegalAccessException;

    ResponseData borrowSample(Map<String, Object> map,HttpServletRequest request) throws ParseException;

    ResponseData findLimit(String organizationName, Integer pageNum, Integer pageSize);

    ResponseData saveSupplier(Map<String, Object> map);
}
