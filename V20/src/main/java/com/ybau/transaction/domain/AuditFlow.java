package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 审批流程实体类
 */

@Data
public class AuditFlow {
    private int auditFlowId;//审批流程ID
    private String auditFlowName;//审批节点名字
    private int auditFlowGrade;//审批节点等级
    private int auditFlowWay;//审批方式（1:主管审核,2:为正常审核流程,0:为不需要审核）
    private int auditFlowCourse;//审批过程（1:依次审批,2:会签,3:或签,4:分支判断,5:主管审核,6:分级主管审核）
    private String auditUser;//可审批人
    private int organizationId;//公司ID
    private User addUser;//添加人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//添加时间
    private List<Map>auditUserList;
}
