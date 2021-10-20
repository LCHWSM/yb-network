package com.ybau.transaction.domain;

import lombok.Data;

/**
 * 通用预警实体类
 */
@Data
public class Universal {
    private int universalId;
    private int red;//红色预警
    private int yellow;//黄色预警
}
