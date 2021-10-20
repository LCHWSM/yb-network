package com.ybau.transaction.domain;

import lombok.Data;

@Data
public class Money {
    private double sumMoney;
    private double actualMoney;
    private double freight;
    private double settlementAmount;
}
