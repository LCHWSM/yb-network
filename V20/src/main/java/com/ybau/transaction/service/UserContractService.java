package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserContractService {
    ResponseData findAll(Map<String, Object> map, HttpServletRequest request);

    ResponseData saveContract(Map<String, Object> map, HttpServletRequest request);

    ResponseData cancelContract(String  orderId,HttpServletRequest request);

    ResponseData updateContract(Map<String, Object> map, HttpServletRequest request);

    ResponseData findOrder(Map<String, Object> map, HttpServletRequest request);

    ResponseData relevanceOrder(Map<String, Object> map,HttpServletRequest request);

    ResponseData ifPermission(int contractId, HttpServletRequest request);

    ResponseData findByContract(Map<String,Object> map);

}
