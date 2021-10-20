package com.ybau.transaction.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 快递实体类
 */
@Data
public class Express {
    private int expressId;//快递Id
    private String expressName;//快递名字
    private String expressNumbers;//快递单号
    private String expressSign;//快递标识
    private String orderId;
    private String successTime;//传入成功时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date practicalTime;//借样订单实际归还时间
    private String retreatName;//退货公司名字
    private String retreatNumbers;//退货单号
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date retreatTime;//退货时间

}
