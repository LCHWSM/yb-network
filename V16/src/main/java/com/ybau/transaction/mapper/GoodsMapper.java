package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Goods;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * 商品持久层
 */
@Repository
public interface GoodsMapper {
    /**
     * 根据订单ID查询商品数据测试
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    @Select("select * from goodstest where goodsId in (select goodsId from order_goodstest where orderId=#{ordersId})")
    List<Goods> findByOrdersIdtest(@Param(value = "orderId") String orderId) throws Exception;

    /**
     * 查询订单下属商品
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    @Select("select * from goods where id in (select goodsId from order_goods where orderId=#{ordersId})")
    List<Goods> findByOrdersId(@Param(value = "orderId") String orderId) throws Exception;


    /**
     * 插入查询的商品数据
     *
     * @param map
     */
    @Insert("INSERT INTO goods (goodsId,goodsName,goodsPrice,goodsNumber,goodsCore,goodsImages,goodsDescription) " +
            "VALUES " +
            "(#{map.goodsId},#{map.goodsName},#{map.goodsPrice},#{map.goodsNumber},#{map.goodsCore},#{map.goodsImages},#{map.goodsDescription})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void saveGood(@Param(value = "map") Map<String, Object> map);

    /**
     * 维护中间表数据
     *
     * @param goodsId
     * @param orderId
     */
    @Insert("insert into order_goods(orderId,goodsId) VALUES (#{orderId},#{goodsId}) ")
    void saveOrder_Goods(@Param(value = "goodsId") String goodsId, @Param(value = "orderId") String orderId);

    /**
     * 查询是否有商品已下单
     *
     * @param id
     * @return
     */
    @Select("select count(1) from goods where goodsId=#{id}")
    int findByPId(@Param(value = "id") int id);

    /**
     * 导入商品
     *
     * @param goods
     */
    @Insert("insert into goods(goodsId,goodsName,goodsPrice,goodsNumber,goodsCore) VALUES (#{goods.goodsId},#{goods.goodsName},#{goods.goodsPrice},#{goods.goodsNumber},#{goods.goodsCore})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addGoods(@Param(value = "goods") Goods goods);

    /**
     * 导入中间表
     *
     * @param id
     * @param orderId
     */
    @Insert("insert into order_goods (orderId,goodsId) values(#{orderId},#{id})")
    void addGoodsByOrder(@Param(value = "id") int id, @Param(value = "orderId") String orderId);

    /**
     * 根据ID删除商品信息
     *
     * @param orderId
     */
    @Delete("delete from goods where goodsId in (select goodsId from order_goods where orderId=#{orderId})")
    void deleteOrderId(@Param(value = "orderId") String orderId);

    //
//    /**
//     * 根据订单ID删除中间表信息
//     * @param orderId
//     */
    @Delete("delete from order_goods where orderId=#{orderId}")
    void deleteByOrder_Goods(@Param(value = "orderId") String orderId);

    /**
     * 修改商品信息
     * @param goods
     */
    @Update("update goods set goodsPrice=#{goods.goodsPrice},goodsNumber=#{goods.goodsNumber} where id=#{goods.id} ")
    void updateGoods(@Param(value = "goods") Map<String,Object> goods);

}
