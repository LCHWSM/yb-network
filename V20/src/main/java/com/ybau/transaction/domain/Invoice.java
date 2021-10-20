package com.ybau.transaction.domain;

import lombok.Data;

@Data
public class Invoice {
    private int invoiceId;//发票Id
    private String unitName;//公司或个人姓名
    private String invoiceNumber;//税号
    private String unitSite;//地址
    private String unitPhone;//电话
    private String bankDeposit;//开户行
    private String bankAccount;//银行账号
    private String orderId;//订单Id
    private int flag;//1：个人，2：公司
    private String flagStr;
    private String email;//邮箱
    private int invoiceType;//发票类型 1：普票  2：专票
    private String invoiceTypeStr;
    private String ticketSite;//收票地址
    private int invoiceFlag;//判断是否需要发票

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getUnitName() {
        if (unitName == null) {
            unitName = "";
        }
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getInvoiceNumber() {
        if (invoiceNumber == null) {
            invoiceNumber = "";
        }
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getUnitSite() {
        if (unitSite == null) {
            unitSite = "";
        }
        return unitSite;
    }

    public void setUnitSite(String unitSite) {
        this.unitSite = unitSite;
    }

    public String getUnitPhone() {
        if (unitPhone == null) {
            unitPhone = "";
        }
        return unitPhone;
    }

    public void setUnitPhone(String unitPhone) {
        this.unitPhone = unitPhone;
    }

    public String getBankDeposit() {
        if (bankDeposit == null) {
            bankDeposit = "";
        }
        return bankDeposit;
    }

    public void setBankDeposit(String bankDeposit) {
        this.bankDeposit = bankDeposit;
    }

    public String getBankAccount() {
        if (bankAccount == null) {
            bankAccount = "";
        }
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getTicketSite() {
        if (ticketSite == null) {
            ticketSite = "";
        }
        return ticketSite;
    }

    public void setTicketSite(String ticketSite) {
        this.ticketSite = ticketSite;
    }

    public int getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(int invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public String getFlagStr() {
        if(flag==1){
            flagStr="个人";
        }else if (flag==2){
            flagStr="公司";
        }
        return flagStr;
    }

    public void setFlagStr(String flagStr) {
        this.flagStr = flagStr;
    }

    public String getInvoiceTypeStr() {
        if (invoiceFlag==1){
            invoiceTypeStr="普票";
        }else if (invoiceFlag==2){
            invoiceTypeStr="专票";
        }
        return invoiceTypeStr;
    }

    public void setInvoiceTypeStr(String invoiceTypeStr) {
        this.invoiceTypeStr = invoiceTypeStr;
    }
}
