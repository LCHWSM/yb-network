package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Audit;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.domain.SponsorAudit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface SponsorAuditMapper {
    /**
     * 发起审核
     *
     */
     void saveSponsorAudit(@Param(value = "map")Map<String,Object>map);

    /**
     * 根据发起ID查询相关信息
     * @param sponsorAuditId
     * @return
     */
    SponsorAudit findBySId(@Param(value = "sponsorAuditId") int sponsorAuditId);

    /**
     * 修改节点
     * @param sponsorAuditId
     * @param auditNode
     */
    void updateAuditNode(@Param(value = "sponsorAuditId") int sponsorAuditId,@Param(value = "auditNode") int auditNode);

    /**
     * 更改发起消息审核状态为审核通过
     * @param sponsorAuditId 发起ID
     * @param audit 审核状态
     */
    void updateAuditOpinion(@Param(value = "sponsorAuditId") int sponsorAuditId,@Param(value = "audit") int audit);

    /**
     * 根据用户ID查询用户发起的审核
     * @param map
     * @return
     */
    List<Order> findAll(@Param(value = "map") Map<String,Object> map);

    /**
     * 根据Id查询该发起消息包含的信息
     * @param sponsorAuditId
     * @return
     */
    SponsorAudit findBySAId(@Param(value = "sponsorAuditId") int sponsorAuditId);


    /**
     * 根据订单ID查询发起审核信息
     * @param orderId
     * @return
     */
    SponsorAudit findByOrderId(@Param(value = "orderId") String orderId);

    /**
     * 根据订单ID删除审核信息
     * @param orderId
     */
    void deleteOrderId(@Param(value = "orderId") String orderId);
}
