package com.ybau.transaction.domain;

import lombok.Data;

import java.util.List;

/**
 * 分组实体类
 */
@Data
public class Grouping {
    private int groupingId;//分组ID 自动生成
    private String groupingName;//分组名字
    private int departmentId;//部门ID
    private List<User> users;//分组包含的用户列表
}
