package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 日志实体类
 */
@Data
public class    Log {
    private int logId;
    private String logName;//具体操作
    private User logUser;//操作人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date logTime;//操作时间
    private String logRemarks;//备注
    private int logClassify;//操作分类
    private String logSubject;//操作主题
    private String logCore;//商品编码
    private Order order;
    private String organizationName;//公司名字
    private String logOrderId;//订单编号

}
