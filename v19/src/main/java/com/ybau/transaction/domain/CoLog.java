package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CoLog {
    private int coLogId;
    private String coLogName;//处理情况
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date coLogTime;//处理时间
    private User operationUser;//操作人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operationTime;//操作时间
    private String coRemark;//操作备注

}
