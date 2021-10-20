package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AuditLog {
    private int auditLogId;
    private String auditLogOrder;//审核的订单ID
    private String auditFlowName;//审核节点名称
    private String auditCondition;//审核节点意见
    private String auditOpinion;//审核节点备注
    private String auditUser;//操作人
    private User user;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditLogTime;//操作时间

}
