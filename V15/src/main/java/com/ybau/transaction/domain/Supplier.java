package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 供应商实体类
 */
@Data
public class Supplier {
    private int supplierId;//供应商ID
    private String supplierName;//供应商名字
    private User addUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//创建时间
    private User updateUser;//修改人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//修改时间
    private String supplierPhone;//供应商电话
    private String supplierSite;//供应商地址
}
