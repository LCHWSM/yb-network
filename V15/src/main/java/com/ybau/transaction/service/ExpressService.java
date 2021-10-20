package com.ybau.transaction.service;

import com.ybau.transaction.domain.Company;
import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface ExpressService {


    List<Company> findAll();

    ResponseData saveExpress(Map<String, Object> map);

    ResponseData saveByExpress(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData refundCargo(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData updateExpress(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData updateRefundCargo(Map<String, Object> map, HttpServletRequest request) throws ParseException;

    ResponseData refundCargoSucceed(Map<String,Object> map,HttpServletRequest request) throws ParseException;
}
