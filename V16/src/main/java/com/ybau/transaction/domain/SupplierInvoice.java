package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 供应商发票附件实体类
 */
@Data
public class SupplierInvoice {
    private int supplierInvoiceId;//供应商发票附件Id
    private String supplierInvoiceName;//供应商发票附件名字
    private String supplierInvoiceNum;//供应商发票附件编号
    private User addUser;//添加人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;
    private User updateUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private String supplierInvoiceSite;//附件地址
    private String remarks;//备注
    private String userId;
}
