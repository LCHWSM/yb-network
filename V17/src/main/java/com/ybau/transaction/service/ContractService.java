package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ContractService {
    ResponseData findAll(Map<String, Object> map);

    ResponseData saveContract(Map<String, Object> map, HttpServletRequest request);

    ResponseData deleteContract(int contractId);

    ResponseData findNotContract(Map<String, Object> map);

    ResponseData saveContractId(Map<String, Object> map);

    ResponseData updateConByOrderId(String orderId);

    ResponseData updateContract(HttpServletRequest request, Map<String, Object> map);

    ResponseData findByOrderId(String orderId);

    ResponseData saveContractAll(Map<String, Object> map, HttpServletRequest request);
}
