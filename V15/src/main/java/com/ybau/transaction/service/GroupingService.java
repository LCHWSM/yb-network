package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

import java.util.Map;

public interface GroupingService {
    /**
     * 新增部门分组
     * @param map
     * @return
     */
    ResponseData saveGrouping(Map<String, Object> map);

    /**
     * 修改部门分组
     * @param map
     * @return
     */
    ResponseData updateGrouping(Map<String, Object> map);

    /**
     * 根据分组ID删除分组
     * @param groupingId
     * @return
     */
    ResponseData deleteGrouping(int groupingId);

    /**
     * 查询分组用户
     * @return
     */
    ResponseData findByUser();

}
