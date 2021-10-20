package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Audit {
    private int auditId;//创建消息ID
    private String auditUser;//可以审核该消息用户
    private User user;
    private int sponsorAuditId;//发起消息Id
    private SponsorAudit sponsorAudit;//发起审核实体类
    private int auditOpinion;//审核意见（0待审核，1审核通过2，审核不通过）
    private String auditRemarks;//审核备注
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;//审核时间
    private int auditflow;//节点ID
    private AuditFlow auditFlow;//审核节点实体类
    private int processGroupId;//目前公司所使用流程组信息
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;//审核创建时间
    private String auditFlowName;//节点名字
    private String sponsorAuditOrder;//审核订单
}
