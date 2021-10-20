package com.ybau.transaction.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ProcessGroup {
    private int processGroupId;//流程组ID
    private String processGroupName;//流程组名字
    private User processGroupUser;//发布人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date processGroupTime;//发布时间
    private int organizationId;//所属公司ID
    private List<AuditFlow> auditFlows;//流程节点详细信息
    private Organization organization;

}
