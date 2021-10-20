package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Grouping;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 分组持久层
 */

@Repository
public interface GroupingMapper {
    /**
     * 新增部门分组
     * @param map
     */
    void saveGrouping(@Param(value = "map") Map<String, Object> map);

    /**
     * 修改部门分组
     * @param map
     */
    void updateGrouping(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据ID查询分组信息
     * @param groupingId
     * @return
     */
    Grouping findByGId(@Param(value = "groupingId") int groupingId);

    /**
     * 根据ID删除分组信息
     * @param groupingId
     */
    void deleteGrouping(@Param(value = "groupingId") int groupingId);

    /**
     * 查询分组下用户列表
     * @return
     */
    List<Grouping> findByUser();

    /**
     * 根据部门ID查询是否有下属分组
     * @param departmentId
     * @return
     */
    int findBycount(@Param(value = "departmentId") int departmentId);
}
