package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.NodeDraft;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface NodeDraftMapper {

    /**
     * 根据公司ID查询该公司已创建的审核节点
     *
     * @param organizationId
     * @return
     */
    List<NodeDraft> findByOId(@Param(value = "organizationId") int organizationId);

    /**
     * 插入审核节点
     *
     * @param map
     */
    void saveAuditFlow(@Param(value = "map") Map<String, Object> map);

    /**
     * 插入分支判断条件
     *
     * @param nodeDraftId
     * @param nodeDraftUser
     * @param groupingId
     */
    void saveAuditFlowByGrouping(@Param(value = "nodeDraftId") Integer nodeDraftId, @Param(value = "nodeDraftUser") String nodeDraftUser, @Param(value = "groupingId") int groupingId);

    /**
     * 根据公司ID查询该公司草稿箱内的审核节点
     *
     * @param map
     * @return
     */
    List<NodeDraft>findAll(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询分支判断 审核人 分组信息
     *
     * @param nodeDraftId
     * @return
     */
    List<Map> findByGrouping(@Param(value = "nodeDraftId") int nodeDraftId);

    /**
     * 修改审核节点草稿
     *
     * @param map
     */
    void updateNodeDraft(@Param(value = "map") Map<String, Object> map);

    /**
     * 删除分支判断的相关信息
     *
     * @param nodeDraftId
     */
    void deleteAuditFlowByGrouping(@Param(value = "nodeDraftId") int nodeDraftId);

    /**
     * 删除节点相关信息
     *
     * @param nodeDraftId
     */
    void deleteByNodeDraftId(@Param(value = "nodeDraftId") int nodeDraftId);

    /**
     * 根据节点ID查询节点相关信息
     *
     * @param nodeDraftId
     * @return
     */
    NodeDraft findById(@Param(value = "nodeDraftId") int nodeDraftId);

    /**
     * 根据公司ID删除所有节点
     * @param organizationId
     */
    void deleteByOId(@Param(value = "organizationId") Integer organizationId);

    /**
     * 根据公司ID查询所有节点ID
     * @param organizationId
     * @return
     */
    List<Integer> findByNodeDraftId(@Param(value = "organizationId") Integer organizationId);
}
