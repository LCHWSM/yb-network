package com.ybau.transaction.mapper;


import com.ybau.transaction.domain.Audit;
import com.ybau.transaction.domain.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface AuditMapper {
    /**
     * 插入消息
     * @param id
     * @param sponsorAuditId
     * @param auditOpinion
     * @param auditFlowId
     */
    void saveAudit(@Param("id") String id, @Param(value = "sponsorAuditId") int sponsorAuditId, @Param(value = "auditOpinion") int auditOpinion, @Param(value = "auditFlowId") int auditFlowId, @Param(value ="processGroupId") int processGroupId,@Param(value = "startTime") Date startTime);

    /**
     * 根据用户ID查询待该用户审核的信息
     * @param map
     * @return
     */
    List<Order> findAll(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据ID查询审核信息
     * @param auditId
     * @return
     */
    Audit findByAuditId(@Param(value = "auditId") int auditId);


    /**
     * 创建系统直接审核通过信息
     * @param id 可以审核该消息用户
     * @param sponsorAuditId 发起审批信息Id
     * @param auditOpinion 审核意见（4待审核，1审核通过2，审核不通过,3审核撤销，5，系统自动审核通过）
     * @param auditFlowId 审核节点ID
     * @param processGroupId 公司所使用流程组ID
     * @param auditTime  审核时间
     * @param startTime 审核创建时间
     */
    void saveAuditPass(@Param(value = "id") String id, @Param(value = "sponsorAuditId") int sponsorAuditId,@Param(value = "auditOpinion") int auditOpinion,@Param(value = "auditFlowId") int auditFlowId,@Param(value = "processGroupId") int processGroupId,@Param(value = "auditTime") Date auditTime,@Param(value = "startTime") Date startTime);

    /**
     * 更改跟本次相关审核信息
     * @param sponsorAuditId
     * @param auditOpinion
     * @param auditRemarks
     * @param auditTime
     */
    void updateSponsorAudit(@Param(value = "sponsorAuditId") int sponsorAuditId, @Param(value = "auditOpinion") int auditOpinion,@Param(value = "auditRemarks") String auditRemarks,@Param(value = "auditTime") Date auditTime,@Param(value = "auditFlow") Integer auditFlow,@Param(value = "auditId") Integer auditId);

    /**
     * 根据AuditID修改相关信息
     * @param auditId
     * @param auditOpinion
     * @param auditRemarks
     * @param auditTime
     */
    void updateAudit(@Param(value = "auditId") Integer auditId, @Param(value = "auditOpinion") int auditOpinion,@Param(value = "auditRemarks") String auditRemarks,@Param(value = "auditTime") Date auditTime);

    /**
     * 查询会签是否已全部审核完
     * @param sponsorAuditId
     * @param auditflow
     * @return
     */
    List<Audit> findIfAuditAll(@Param(value = "sponsorAuditId") int sponsorAuditId,@Param(value = "auditflow") int auditflow);

    /**
     * 查询已经审核过的消息
     * @param map
     * @return
     */
    List<Order> findByOperation(@Param(value = "map") Map<String, Object> map);

    /**
     * 更改审核信息 (只更改待审核状态中的信息)
     * @param sponsorAuditId
     * @param auditOpinion
     * @param audit
     */
    void updateBySponsorAudit(@Param(value = "sponsorAuditId") Integer sponsorAuditId,@Param(value = "auditOpinion") Integer auditOpinion,@Param(value = "audit") Integer audit,@Param(value = "auditTime") Date auditTime);

    /**
     * 查询是否已经创建审核消息
     * @param id
     * @param sponsorAuditId
     * @param auditFlowId
     * @return
     */
    int findUserFlowSponsor(@Param(value = "id") String id,@Param(value = "sponsorAuditId") int sponsorAuditId,@Param(value = "auditFlowId") int auditFlowId);

    /**
     * 根据ID删除用户审核记录
     * @param orderId
     */
    void deleteOrderId(@Param(value = "orderId") String orderId);
}
