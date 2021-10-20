package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
public class NodeDraft {
    private int nodeDraftId;//草稿节点ID
    private String nodeDraftName;//草稿节点名字
    private int nodeDraftGrade;//节点草稿排序顺序
    private int nodeDraftWay;//审批方式（1:主管审核,2:为正常审核流程,3:为不需要审核）
    private int nodeDraftCourse;//审批过程（1:依次审批,2:会签,3:或签,4:分支判断,5:主管审核,6:分级主管审核）
    private String nodeDraftUser;//审核人信息字符串
    private List<Map> nodeDraftUserList;//审核人信息集合
    private int organizationId;//公司ID
    private User addUser;//添加人用户信息
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date addTime;//添加时间
    private List<Map>nodeDraftList;//存储分支判断信息
    private int auditFlowId;


}
