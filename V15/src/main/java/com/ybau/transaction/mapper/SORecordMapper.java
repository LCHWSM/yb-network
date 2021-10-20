package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.SORecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SORecordMapper {

    /**
     * 根据用户ID查询所有用户填写的供应商商品信息
     * @param map
     * @param id
     * @return
     */
    List<SORecord> findAllById(@Param(value = "map") Map<String, Object> map,@Param(value = "id") String id);

    /**
     * 修改存入的商品信息
     * @param map
     */
    void updateRecord(@Param(value = "map") Map<String, Object> map);

    /**
     * 删除已添加商品记录
     * @param soRecordId
     */
    void deleteRecord(@Param(value = "soRecordId") int soRecordId);
}
