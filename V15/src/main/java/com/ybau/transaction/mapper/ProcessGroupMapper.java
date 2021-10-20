package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.AuditFlow;
import com.ybau.transaction.domain.ProcessGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sun.awt.SunHints;

import java.util.List;
import java.util.Map;

@Repository
public interface ProcessGroupMapper {

    /**
     * 插入分组信息
     * @param map
     */
    void saveAuditFlow(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据公司ID查询所有流程组
     * @param map
     * @return
     */
    List<ProcessGroup> findAll(@Param(value = "map") Map<String, Object> map);

    /**
     * 切换公司所用的流程组
     * @param organizationId
     * @param processGroupId
     */
    void saveOrganization(@Param(value = "organizationId") Integer organizationId,@Param(value = "processGroupId") Integer processGroupId);

    /**
     * 查询公司是否已创建流程
     * @param organizationId
     * @return
     */
    List<ProcessGroup> findByOId(@Param(value = "organizationId") int organizationId,@Param(value = "processGroupId") Integer processGroupId);

    /**
     * 根据流程组ID  修改流程组名字
     * @param processGroupId
     * @param processGroupName
     */
    void updateName(@Param(value = "processGroupId") Integer processGroupId, @Param(value = "processGroupName") String processGroupName);

}
