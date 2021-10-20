package com.ybau.transaction.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 供应商订单实体类
 */
@Data
public class SupplierOrder {
    private String supplierOrderId;//供应商订单ID、
    private String productNum;//商品编码
    private String productName;//商品名称
    private String specification;//规格
    private int amount;//数量
    private String remarks;//备注
    private int supplierId;//供应商信息
    private Supplier supplier;
    private User addUser;//创建人实体类
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//创建时间
    private int dispose;//供应商反馈（1待处理 2处理中  3不受理）
    private String disposeStr;//供应商反馈字符串
    private double quotation;//报价
    private int flag;//是否锁定报价1未锁定 2锁定报价
    private String flagStr;//锁定报价字符串
    private int settlement;//结算状态（1未结算，2已结算）
    private String settlementStr;//结算状态字符串
    private Invoice invoice;//发票相关信息
    private double settlementAmount;//已结算金额
    private double paymentStatus;//结算状态
    private String paymentStatusStr;//结算状态字符串
    private String userId;
    private Organization organization;
    private int organizationId;//公司ID
    private String disposeLog;//供应商反馈备注


    public String getDisposeStr() {
        if (this.dispose == 1) {
            this.disposeStr = "待处理";
        } else if (this.dispose == 2) {
            this.disposeStr = "处理中";
        } else if (this.dispose == 3) {
            this.disposeStr = "拒绝交易";
        } else if (this.dispose == 4) {
            this.disposeStr = "已完成";
        }
        return disposeStr;
    }

    public void setDisposeStr(String disposeStr) {
        this.disposeStr = disposeStr;
    }

    public String getFlagStr() {
        if (this.flag == 1) {
            this.flagStr = "未锁定";
        } else if (this.flag == 2) {
            this.flagStr = "已锁定";
        }
        return flagStr;
    }

    public void setFlagStr(String flagStr) {
        this.flagStr = flagStr;
    }

    public String getSettlementStr() {
        if (this.settlement == 1) {
            this.settlementStr = "未结算";
        } else if (this.settlement == 2) {
            this.settlementStr = "部分结算";
        }else if(this.settlement==3){
            this.settlementStr="已结算";
        }
        return settlementStr;
    }

    public void setSettlementStr(String settlementStr) {
        this.settlementStr = settlementStr;
    }


}
