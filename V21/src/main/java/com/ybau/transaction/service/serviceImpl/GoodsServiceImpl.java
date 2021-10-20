package com.ybau.transaction.service.serviceImpl;

import com.ybau.transaction.domain.Goods;
import com.ybau.transaction.domain.Product;
import com.ybau.transaction.mapper.GoodsMapper;
import com.ybau.transaction.mapper.ProductMapper;
import com.ybau.transaction.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品Service层
 */
@Service
@Transactional
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    ProductMapper productMapper;


    /**
     * 插入商品方法
     *
     * @param goodsDetail
     * @param orderId
     */
    @Override
    public void saveGoods(List<Goods> goodsDetail, String orderId) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < goodsDetail.size(); i++) {
            map = (Map<String, Object>) goodsDetail.get(i);
            goodsMapper.saveGood(map);
            goodsMapper.saveOrder_Goods(map.get("id").toString(), orderId);
        }
    }

    /**
     * 创建订单修改库存
     *
     * @param goodsDetail
     * @param orderId
     * @return
     */
    @Override
    public void addGoods(List<Map<String, Object>> goodsDetail, String orderId) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < goodsDetail.size(); i++) {
            map = goodsDetail.get(i);
            map.put("now", map.get("goodsNumber"));
            map.put("id", map.get("goodsId"));
            Product product = productMapper.findById(Integer.parseInt(map.get("goodsId").toString()));
            map.put("goodsImages", product.getImages());
            map.put("goodsDescription", product.getDescription());
            goodsMapper.saveGood(map);
            goodsMapper.saveOrder_Goods((String) map.get("id"), orderId);
        }
    }

    /**
     * 增加库存
     *
     * @param goodsDetail
     */
    @Override
    public void updateWarehouse(List<Goods> goodsDetail) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < goodsDetail.size(); i++) {
            Goods goods =  goodsDetail.get(i);
            //回滚库存数量为 订单实际退还数量
            map.put("now", goods.getReturnNumber());
            map.put("id", goods.getGoodsId());
            productMapper.updateWarehouse(map);//增加库存库存
        }
    }

    /**
     * 导入商品
     *
     * @param orderId
     * @param goodsDetail
     */
    @Override
    public void leadGoods(String orderId, List<Goods> goodsDetail) {
        for (Goods goods : goodsDetail) {
            goodsMapper.addGoods(goods);
            goodsMapper.addGoodsByOrder(goods.getId(), orderId);
        }
    }

    /**
     * 修改商品信息
     *
     * @param goodsDetail
     */
    @Override
    public void updateGoods(List<Map<String, Object>> goodsDetail) {
        for (Map<String, Object> goods : goodsDetail) {
            goodsMapper.updateGoods(goods);
        }
    }

    /**
     * 更改原有商品转销售数量
     *
     * @param goodsList
     */
    @Override
    public int updateSellNumber(List<Map<String, Object>> goodsList) {
        //借样订单 可转多次为销售订单
        for (Map<String, Object> goodsMap : goodsList) {
            Goods goods = goodsMapper.findById((int) goodsMap.get("id"));
            int sellNumber = goods.getGoodsNumber() - ((int) goodsMap.get("goodsNumber") + goods.getSellNumber() + goods.getReturnNumber());//计算出转销售订单后剩余商品数量
            if (sellNumber < 0) {
                return 1;
            }
            goodsMapper.updateSellNumber(((int) goodsMap.get("goodsNumber") + goods.getSellNumber()), (int) goodsMap.get("id"));//赋值原订单商品转出数量
        }
        return 0;
    }

    /**
     * 更改商品退货数量
     *
     * @param goodsDetail
     * @return
     */
    @Override
    public int updateReturnNumber(List<Map<String, Object>> goodsDetail) {
        for (Map<String, Object> map : goodsDetail) {
            Goods goods = goodsMapper.findById((int) map.get("id"));
            int returnNumber = goods.getGoodsNumber() - ((int) map.get("goodsNumber") + goods.getSellNumber() + goods.getReturnNumber());
            if (returnNumber < 0) {
                return 1;
            }
            goodsMapper.updateReturnNumber(((int) map.get("goodsNumber") + goods.getReturnNumber()), (int) map.get("id"));
        }
        return 0;
    }
}


