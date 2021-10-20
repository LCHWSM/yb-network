package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 结算记录实体类
 */
@Data
public class ClearingLog {
    private int clearingLogId;//结算记录ID
    private String clearingLogName;//具体操作
    private User clearingLogUser;//操作人实体类
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date clearingLogTime;//操作时间
    private String clearingRemark;//操作备注
    private String clearingLogOrderId;//结算订单ID
    private String consignee;//订单收货人信息

    public String getConsignee() {
        if (consignee != null) {
            String aNull = consignee.replace("null", "");
            return aNull;
        }
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    private int classify;//订单结算 修改分类信息


}
