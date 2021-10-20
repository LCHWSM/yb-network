package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SupplierOrderLog {
    private int supplierLogId;//供应商订单日志ID
    private String logName;//具体操作
    private User logUser;//操作人实体类
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date logTime;//操作时间
    private String logRemarks;//操作备注
    private String logOrderId;//操作订单ID
    private int classify;//日志分类标识
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date operationTime;//结算时间

    public String getLogName() {
        String replace = logName.replace("null", "");
        return replace;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }
}
