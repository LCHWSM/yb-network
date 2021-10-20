package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import java.util.Map;

public interface TemplateService {
    ResponseData saveTemplate(Map<String,Object> map);


    ResponseData findAll();

}
