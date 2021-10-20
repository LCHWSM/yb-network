package com.ybau.transaction.service;


import com.ybau.transaction.domain.Goods;
import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface CollectService {
    ResponseData saveCollect(List<Goods> list, HttpServletRequest request);

    ResponseData findCollect(HttpServletRequest request);

    ResponseData updateCollect(List<Map<String, Object>> list, HttpServletRequest request);
}
