package com.ybau.transaction.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SponsorAudit {
    private int sponsorAuditId;//发起审核ID
    private String sponsorAuditUser;//发起人ID
    private String sponsorAuditOrder;//发起的订单ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sponsorAuditTime;//发起时间
    private int audit;//发起审核的审核状态（2审核不通过  1审核通过,3 审核中 4 审核撤销）
    private int auditNode;//标志该发起的审核目前在第几步
    private User user;//用户实体类
    private Order order;//订单实体类
    private int processGroupId;//目前公司所使用流程组信息
    private AuditFlow auditFlow;//标记该发起信息为哪一个节点
    private List<AuditFlow> auditFlows;//标记该发起消息使用的是哪一个流程组的审核节点
}
