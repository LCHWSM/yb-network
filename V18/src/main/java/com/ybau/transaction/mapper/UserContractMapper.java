package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Contract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserContractMapper {
    /**
     * 根据用户ID查询上传订单列表（筛选、排序）
     * @param map
     * @param id
     * @return
     */
    List<Contract> findAll(@Param(value = "map") Map<String, Object> map, @Param(value = "id") String id);

    /**
     * 新增用户发票
     * @param map
     */
    void saveContract(@Param(value = "map") Map<String, Object> map);
}
