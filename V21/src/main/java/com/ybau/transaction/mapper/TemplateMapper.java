package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Template;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TemplateMapper {

    /**
     * 删除原有模板
     */
    void deleteTemplate();


    /**
     * 插入文件路径/名称
     */
    void saveTemplate(@Param(value = "map") Map<String,Object> map);


    /**
     * 查询模板
     * @return
     */
    List<Template> findAll();

}
