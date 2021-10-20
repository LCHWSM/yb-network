package com.ybau.transaction.domain;

import lombok.Data;

import java.util.Date;

@Data
public class SupplierContract {

    private int supplierContractId;
    private String supplierContractName;
    private String supplierContractNumber;
    private User addUser;
    private Date addTime;
    private String supplierContractSite;
    private String remark;
    private User updateUser;
    private Date updateTime;
    private Organization organization;

}
