package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface LogService {
    void saveProductLog(Map<String, Object> map);

    ResponseData findAll(Map<String, Object> map);

    ResponseData findOrderByLog(Map<String, Object> map);

    ResponseData findCoLog(Map<String, Object> map);

    ResponseData saveCoLog(Map<String, Object> map,HttpServletRequest request) throws ParseException;

    ResponseData findSupplierLog(Map<String, Object> map);

    ResponseData findLeadOrder(String startTime, String endTime, HttpServletRequest request,int pageSize,int pageNum);

    ResponseData findLeadOrderAll(String startTime, String endTime, int pageNum, int pageSize);

}
