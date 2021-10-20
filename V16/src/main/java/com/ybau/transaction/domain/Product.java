package com.ybau.transaction.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品实体类
 */
@Data
public class Product {
    private int id;//商品id
    private String name;//商品名字
    private String core;//商品编码
    private double price;//销售价格
    private double cost;//成本价格
    private String param;//商品类型
    private int status;//是否启用商品
    private String description;//商品描述
    private String images;//商品主图
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//添加时间
    private User addUser;//添加人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//修改时间
    private User updateUser;//修改人
    private int warehouse;//库存
    private String classifyName;//分类名字
    private int red;//库存红色预警
    private int yellow;//库存黄色预警
    private int universal;//是否使用通用属性  0使用独有，1通用
    private Universal universals;//通用属性实体类
}
