package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ClassifyService {
    ResponseData findAll(int pageSize, int pageNum, String classifyName);

    ResponseData saveClassify(Map<String, Object> map, HttpServletRequest request);

    ResponseData deleteById(int id);

    ResponseData updateClassify(Map<String, Object> map, HttpServletRequest request);

    ResponseData updateSort(Map<String, Object> map);

}
