package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Role {
    private int id;//角色ID
    private String name;//角色名称
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//添加时间
    private String roleDesc;//备注
    private User addUser;//添加人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//修改时间
    private User updateUser;//修改人
    private List<Permission> permissions;//权限实体类
}
