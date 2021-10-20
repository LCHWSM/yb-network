package com.ybau.transaction.domain;

import lombok.Data;

@Data
public class TimeDataSta {
    private int dataStatisticsId;
    private String datatime;//时间
    private String paymentMethod;//下单方式
    private double companyOrderSum;//下单方式订单总金额
    private double companyOrderQuit;//下单方式订单退款总金额
    private double companyOrderTrun;//下单方式订单转销售金额
    private long orderSumNumber;//订单总数量
    private long orderQuitNumber;//订单退款数量
    private long orderTrunNumber;//订单转销售数量
}
