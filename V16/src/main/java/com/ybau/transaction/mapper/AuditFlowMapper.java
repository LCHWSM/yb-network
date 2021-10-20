package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.AuditFlow;
import com.ybau.transaction.domain.NodeDraft;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuditFlowMapper {
    /**
     * 新增审批节点
     *
     * @param nodeDraft
     */
    void saveAuditFlow(@Param(value = "nodeDraft") NodeDraft nodeDraft, @Param(value = "processGroupId") int processGroupId);

    /**
     * 根据公司ID查询该公司已有审批节点
     *
     * @param organizationId
     * @return
     */
    List<AuditFlow> findByOId(@Param(value = "organizationId") int organizationId, @Param(value = "processGroupId") int processGroupId);


    /**
     * 为分支判断时存入可审批的组
     *
     * @param auditFlowId
     * @param auditUserId
     * @param groupingId
     */
    void saveAuditflowByGrouping(@Param(value = "auditFlowId") int auditFlowId, @Param(value = "auditUserId") String auditUserId, @Param(value = "groupingId") int groupingId);

    /**
     * 根据用户分组ID和目前审核所在节点查询可审核人ID
     *
     * @param grouping
     * @param auditNode
     * @return
     */
    String findByGrouping(@Param(value = "grouping") int grouping, @Param(value = "auditNode") int auditNode);


    /**
     * 根据分组ID查询流程组包含节点信息
     *
     * @param processGroupId
     * @return
     */
    List<Map> findByProcessGroupId(@Param(value = "processGroupId") int processGroupId);

    /**
     * 根据节点ID查询分支判断信息
     *
     * @param auditFlowId
     * @return
     */
    List<Map> findByAG(@Param(value = "auditFlowId") int auditFlowId);


    /**
     *根据ID查看节点信息
     * @param auditflow
     * @return
     */
    AuditFlow findAuditFlow(@Param(value = "auditflow") int auditflow);

    /**
     * 根据订单目前所在审核顺序，和所在分组查询该节点信息
     * @param auditNode
     * @param processGroupId
     * @return
     */
    AuditFlow findGradeByGroupId(@Param(value = "auditNode") int auditNode,@Param(value = "processGroupId") int processGroupId);

}
