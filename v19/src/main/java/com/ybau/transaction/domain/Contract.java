package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合同实体类
 */
@Data
public class Contract {
    private int contractId;//合同ID
    private String contractName;//合同名字
    private String contractNumber;//合同编号
    private User user;//上传人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//上传时间
    private String contractSite;//文件路径
    private List<Order> orders;//订单列表
    private String remark;//合同备注
    private User updateUser;//
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//修改时间
    private Organization organization;//公司实体类
    private int organizationId;//公司ID
    private List<Map> fileMap;
    private String addUser;

}
