package com.ybau.transaction.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class ExcelProduct {


    private int id;//商品id

    private String name;//商品名字

    private String core;//商品编码

    private double price;//销售价格
    private double cost;//成本价格
    private String param;//商品类型

    private int status;//是否启用商品
    private String statusStr;
    private String description;//商品描述
    private String images;//商品主图
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//添加时间
    private String addUser;//添加人

    private int warehouse;//库存

    private int classify_id;//分类id
    private String classifyStr;


    public int getStatus() {
        if (statusStr.equals("启用")) {
            status = 1;
        } else {
            status = 0;
        }
        return status;
    }

    public void setStatus(int status) {

        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }


}
