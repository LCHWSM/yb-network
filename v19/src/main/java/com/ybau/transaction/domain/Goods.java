package com.ybau.transaction.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 订单商品实体类
 */

@Data
public class Goods {
    private String goodsId;//商品ID
    @Excel(name  = "商品名称", width = 45)
    private String goodsName;//商品名称
    @Excel(name = "商品价格", width = 10)
    private Double goodsPrice;//商品价格
    @Excel(name = "商品数量", width = 5)
    private int goodsNumber;//商品数量
    private String images;//商品主图
    private int warehouse;//商品库存
    @Excel(name = "商品编码", width = 10)
    private String goodsCore;//商品编码
    private String goodsDescription;//商品详情页
    private String goodsImages;//商品主图
    private int id;
    @Excel(name = "退货/借样归还数量", width = 5)
    private int returnNumber;//退货归还数量
    @Excel(name = "转为销售订单数量", width = 5)
    private int sellNumber;//转为销售订单数量

    public String getGoodsId() {
        if (goodsId==null){
            goodsId="";
        }
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        if (goodsName==null){
            goodsName="";
        }
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(int warehouse) {
        this.warehouse = warehouse;
    }

    public String getGoodsCore() {
        if (goodsCore==null){
            goodsCore="";
        }
        return goodsCore;
    }

    public void setGoodsCore(String goodsCore) {
        this.goodsCore = goodsCore;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public String getGoodsImages() {
        return goodsImages;
    }

    public void setGoodsImages(String goodsImages) {
        this.goodsImages = goodsImages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
