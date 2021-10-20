package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class User {

    private String id;

    private String userName;

    private String passWord;

    private String email;

    private int flag;//0启用，1人工冻结，2系统冻结
    private String uId;//用户上级ID
    private User user;//用户实体类
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//添加时间
    private String addUser;//添加人
    private String addName;//添加人姓名
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//最后修改时间
    private String updateUser;//最后修改人
    private String updateName;//最后修改人姓名
    private List<Role> roles;//角色实体类
    private int userCount;//账户错误次数
    private int organizationId;//公司ID
    private Organization organization;//公司信息
    private String name;//用户姓名
    private String userPhone;//用户手机号
    private String organizationName;//公司名字
    private Department department;//用户所属部门
    private Grouping grouping;//用户所属分组
    private int groupingId;//用户分组ID
    private int departmentId;//用户部门ID
    private Supplier supplier;//供应商信息


}
