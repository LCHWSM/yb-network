package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Classify;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClassifyMapper {
    /**
     * 查询分类
     * @return
     */
    List<Classify> findAll(@Param(value = "classifyName") String classifyName);

    /**
     * 根据名字查询分类是否存在
     * @param name
     * @return
     */
    int findByName(@Param(value = "name") String name);

    /**
     * 新增分类
     * @param map
     */
    void saveClassify(@Param(value = "map")Map<String, Object> map);

    /**
     * 根据ID删除分类
     * @param id
     */
    void deleteById(@Param(value = "id")int id);

    /**
     * 修改分类
     * @param map
     */
    void updateClassify(@Param(value = "map")Map<String, Object> map);



    /**
     * 更改分类排序顺序
     * @param upClassifyId
     * @param nextSort
     */
    void updateSort(@Param(value = "upClassifyId") int upClassifyId, @Param(value = "nextSort")int nextSort);

    /**
     * 查询所有分类
     * @return
     */
    List<Classify> findClassify();

    /**
     * 根据分类名字查询分类ID
     * @param classifyStr
     * @return
     */
    Integer findById(@Param(value = "classifyStr") String classifyStr);
}
