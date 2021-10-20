package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface SupplierContractService {
    ResponseData findBySrId(String srId);

    ResponseData saveContr(Map<String, Object> map, HttpServletRequest request);

    ResponseData cancelContr(String supplierOrderId,HttpServletRequest request);

    ResponseData updateContr(Map<String, Object> map, HttpServletRequest request);

    ResponseData findSROrder(Map<String, Object> map, HttpServletRequest request);

    ResponseData relevanceSROrder(Map<String, Object> map,HttpServletRequest request);

    ResponseData saveContract(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData deleteContract(int supplierContractId);

    ResponseData updateContract(Map<String, Object> map, HttpServletRequest request);

    ResponseData findSROrderAll(Map<String, Object> map);

    ResponseData relevanceSRAll(Map<String, Object> map);

    ResponseData ifPermission(int supplierContractId, HttpServletRequest request);

    ResponseData findContractId(Map<String, Object> map);

    ResponseData updateSOId(String supplierOrderId);

}
