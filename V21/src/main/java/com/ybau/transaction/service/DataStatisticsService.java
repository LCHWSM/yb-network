package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import java.text.ParseException;
import java.util.Map;

public interface DataStatisticsService {
    ResponseData findAll(Map<String, Object> map);

    ResponseData findTime(Map<String, Object> map);

    ResponseData findByOrder(Map<String, Object> map) throws ParseException;
}
