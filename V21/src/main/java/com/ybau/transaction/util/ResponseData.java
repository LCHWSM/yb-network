package com.ybau.transaction.util;

import lombok.Data;

@Data
public class ResponseData {
    private int code;//错误提示码
    private String msg;//错误提示信息
    private Object data;//返回数据

    public ResponseData() {
    }

    public ResponseData(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
