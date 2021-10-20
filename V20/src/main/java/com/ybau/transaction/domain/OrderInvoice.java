package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 订单发票管理实体类
 */
@Data
public class OrderInvoice {
    private int orderInvoiceId;//发票Id
    private String orderInvoiceName;//发票名称
    private String orderInvoiceNumber;//发票编号
    private User addUser;//添加人实体类
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//添加时间
    private User updateUser;//修改人实体类
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//修改时间
    private String orderInvoiceSite;//发票附件地址
    private String remarks;//发票备注
    private String userId;


}
