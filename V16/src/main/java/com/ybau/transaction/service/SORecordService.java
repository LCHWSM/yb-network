package com.ybau.transaction.service;


import com.ybau.transaction.util.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface SORecordService {

    /**
     * 根据用户ID查询所有用户填写的供应商商品信息
     * @param map
     * @param request
     * @return
     */
    ResponseData findAllById(Map<String, Object> map, HttpServletRequest request);

    /**
     * 修改记录的商品信息
     * @param map
     * @return
     */
    ResponseData updateRecord(Map<String, Object> map);

    /**
     * 删除已添加商品记录
     * @param soRecordId
     * @return
     */
    ResponseData deleteRecord(int soRecordId);
}
