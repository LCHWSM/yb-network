package com.ybau.transaction.service.serviceImpl;


import com.ybau.transaction.domain.Goods;
import com.ybau.transaction.mapper.ProductMapper;
import com.ybau.transaction.service.CollectService;
import com.ybau.transaction.util.JsonUtil;
import com.ybau.transaction.util.JwtUtil;
import com.ybau.transaction.util.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 购物车逻辑层
 */

@Service
@Slf4j
public class CollectServiceImpl implements CollectService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private RedissonClient client;
    @Autowired
    ProductMapper productMapper;
    @Value("${ftp.visitUrl}")
    String prefixUrl;

    @Value("${ftp.bastPath}")
    String bastPath;

    /**
     * 添加商品到购物车
     *
     * @param
     * @param request
     * @return
     */

    @Override
    public ResponseData saveCollect(List<Goods> list, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        RBucket<Object> bucket = client.getBucket("data_ybnetwork." + id);
        String goodsStr = (String) bucket.get();
        for (Goods goods : list) {
            if (goods.getGoodsNumber() < 1) {
                return new ResponseData(400, "商品数量必须为正数", null);
            }
        }
        boolean flag = false;
        if (goodsStr == null) {
            //用户此前没有购物车直接储存
            bucket.set(JsonUtil.listToJson(list));
        } else {
            List<Goods> goodsList = JsonUtil.jsonToList(goodsStr, Goods.class);
            for (int i = 0; i < goodsList.size(); i++) {
                for (Goods goods : list) {
                    if (goodsList.get(i).getGoodsId().equals(goods.getGoodsId())) {
                        //判断redis中该商品是否已经存在
                        goodsList.get(i).setGoodsNumber((Integer) goodsList.get(i).getGoodsNumber() + (Integer) goods.getGoodsNumber());
                        flag = true;
                    }
                }
            }
            if (!flag) {
                for (Goods goods : list) {
                    goodsList.add(goods);
                }
            }
            //重新存储到Redis
            bucket.set(JsonUtil.listToJson(goodsList));
        }
        return new ResponseData(200, "添加成功", null);
    }

    /**
     * 查询用户购物车中商品
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData findCollect(HttpServletRequest request) {
        String token = request.getHeader("token");
        List<Goods> list = new ArrayList<>();
        String id = jwtUtil.getId(token);
        RBucket<Object> bucket = client.getBucket("data_ybnetwork." + id);
        String goodsList = (String) bucket.get();
        if (goodsList != null) {
            List<Goods> goods = JsonUtil.jsonToList(goodsList, Goods.class);
            for (Goods good : goods) {
                //根据存储的商品ID查询商品信息
                Goods goodsOne = productMapper.finGoodId(Integer.parseInt(good.getGoodsId()));
                if (goodsOne != null) {
                    goodsOne.setGoodsNumber(good.getGoodsNumber());//赋值数量
                    goodsOne.setImages(prefixUrl + bastPath + "/" + goodsOne.getImages());//拼接图片地址
                    list.add(goodsOne);
                }
            }
        }
        return new ResponseData(200, "查询成功", list);
    }

    /**
     * 修改购物车内容
     *
     * @param list
     * @return
     */
    @Override
    public ResponseData updateCollect(List<Map<String, Object>> list, HttpServletRequest request) {
        String token = request.getHeader("token");
        String id = jwtUtil.getId(token);
        for (Map<String, Object> map : list) {
            try {
                if ((Integer) map.get("goodsNumber") < 1) {
                    return new ResponseData(400, "商品数量必须为正数", null);
                }
            } catch (Exception e) {
                log.error("类型转换异常{}", e);
                return new ResponseData(400, "商品数量不可为小数", null);
            }
        }
        RBucket<Object> bucket = client.getBucket("data_ybnetwork." + id);
        bucket.set(JsonUtil.listToJson(list));
        return new ResponseData(200, "修改成功", null);
    }
}
