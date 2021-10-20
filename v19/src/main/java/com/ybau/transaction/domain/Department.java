package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 部门实体类
 */
@Data
public class Department {
    private int departmentId;//部门ID唯一标识自动生成
    private String departmentName;//部门名字
    private Organization organization;//所属公司信息
    private String departmentNumber;//部门编号，手动生成，不可重复
    private User addUser;//添加人信息
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//添加时间
    private User updateUser;//修改人信息
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//修改时间
    private List<Grouping> groupings;//部门分组情况
}
