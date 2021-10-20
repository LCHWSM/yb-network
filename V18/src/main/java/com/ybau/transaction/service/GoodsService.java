package com.ybau.transaction.service;

import com.ybau.transaction.domain.Goods;

import java.util.List;
import java.util.Map;

public interface GoodsService {
    void saveGoods(List<Goods> goodsDetail, String orderId);

    void addGoods(List<Map<String,Object>> goodsDetail, String orderId);


    void updateWarehouse(List<Goods> goodsDetail);

    void leadGoods(String orderId, List<Goods> goodsDetail);

    void updateGoods(List<Map<String,Object>> goodsDetail);

    int updateSellNumber(List<Map<String, Object>> goodsList);

    int updateReturnNumber(List<Map<String, Object>> goodsDetail);

}
