package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户地址实体类
 */
@Data
public class UserSite {
    private int userSiteId;//用户地址ID
    private String receiverName;//收件人姓名
    private String receiverMobile;//收件人电话
    private String receiverProvince;//收货人地址（省）
    private String receiverCity;//市
    private String receiverDistrict;//区（县）
    private String receiverStreet;//收件人街道
    private String receiverAddress;//收货人地址详细信息
    private String site;//用户地址汇总
    private String userId;//所属用户ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//最后修改时间

    public String getReceiverMobile() {
        if (this.receiverMobile == null) {
            receiverMobile = "";
        }
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }
}
