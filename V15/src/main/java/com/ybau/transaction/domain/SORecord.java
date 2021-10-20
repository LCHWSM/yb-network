package com.ybau.transaction.domain;

import lombok.Data;

@Data
public class SORecord {
    private int SORecordId;//供应商下单记录ID
    private String SORecordNum;//商品编号
    private String SORecordName;//商品名称
    private String specification;//规格
    private int amount;//商品数量
    private String remarks;//备注
    private int supplierId;//供应商ID
    private String supplierName;//供应商名字
}
