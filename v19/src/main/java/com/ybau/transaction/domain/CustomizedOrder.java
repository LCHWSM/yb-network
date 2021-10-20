package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 定制订单实体类
 */
@Data
public class CustomizedOrder {
    private int coId;//定制订单实体类
    private String coName;//定制人姓名
    private String coPhone;//定制人手机号
    private String coEmail;//定制人邮箱
    private String coDemand;//定制需求
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date coAddTime;//创建时间
    private int coFlag;//处理状态（1待处理，2处理中，3已完成，4已取消）
    private String coFlagStr;
    private User updateUser;//最后修改人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//最后修改时间
    private List<User> users;//跟进人信息
    private List<String> coRemarks;
    public String getCoFlagStr() {
        if (this.getCoFlag() == 1) {
            coFlagStr = "待处理";
        } else if (this.getCoFlag() == 2) {
            coFlagStr = "处理中";
        } else if (this.getCoFlag() == 3) {
            coFlagStr = "已完成";
        } else if (this.getCoFlag() == 4) {
            coFlagStr = "已取消";
        }
        return coFlagStr;
    }

    public void setCoFlagStr(String coFlagStr) {
        this.coFlagStr = coFlagStr;
    }
}
