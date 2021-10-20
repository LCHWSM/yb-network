package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ProductService {
    ResponseData saveProduct(Map<String, Object> map, HttpServletRequest request);

    ResponseData deleteProduct(int id);

    ResponseData updateProduct(Map<String, Object> map, HttpServletRequest request);

    ResponseData findAll(Map<String, Object> map);

    ResponseData findById(int id);

    ResponseData findProduct(Map<String, Object> map,HttpServletRequest request);

    ResponseData updateWarehouse(Map<String, Object> map, HttpServletRequest request);

    ResponseData updateEditor(String oldUrl, String newUrl);
}
