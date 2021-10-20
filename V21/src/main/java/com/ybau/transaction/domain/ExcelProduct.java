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

//    @Excel(name = "商品Id", needMerge = true, width = 10)
    private int id;//商品id
    @Excel(name = "商品编码", needMerge = true, width = 10)
    private String core;//商品编码
    @Excel(name = "商品名称", needMerge = true, width = 23)
    private String name;//商品名字
    private int classify_id;//分类id
    @Excel(name = "分类", needMerge = true, width = 10)
    private String classifyStr;
    @Excel(name = "库存", needMerge = true, width =10)
    private int warehouse;//库存
    @Excel(name = "价格", needMerge = true, width = 10)
    private double price;//销售价格

    private double cost;//成本价格
    private String param;//商品类型

    private int status;//是否启用商品
    @Excel(name = "商品状态", needMerge = true, width = 15)
    private String statusStr;
    private String description;//商品描述
    private String images;//商品主图
    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", needMerge = true, width = 20)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//添加时间
    @Excel(name = "创建人", needMerge = true, width = 10)
    private String addUser;//添加人
    @Excel(name = "最后修改时间", format = "yyyy-MM-dd HH:mm:ss", needMerge = true, width = 20)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//最后修改时间
    @Excel(name = "最后修改人", needMerge = true, width = 15)
    private String updateUserName;//最后修改人

    private String addUserName;//添加人名字


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
        if (status==1){
            statusStr="正常";
        }else {
            statusStr="不启用";
        }
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }


}
