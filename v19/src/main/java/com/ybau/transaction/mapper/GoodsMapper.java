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

    List<Goods> findByOrdersIdtest(@Param(value = "orderId") String orderId) throws Exception;

    /**
     * 查询订单下属商品
     *
     * @param orderId
     * @return
     * @throws Exception
     */

    List<Goods> findByOrdersId(@Param(value = "orderId") String orderId) throws Exception;


    /**
     * 插入查询的商品数据
     *
     * @param map
     */

    void saveGood(@Param(value = "map") Map<String, Object> map);

    /**
     * 维护中间表数据
     *
     * @param goodsId
     * @param orderId
     */

    void saveOrder_Goods(@Param(value = "goodsId") String goodsId, @Param(value = "orderId") String orderId);

    /**
     * 查询是否有商品已下单
     *
     * @param id
     * @return
     */

    int findByPId(@Param(value = "id") int id);

    /**
     * 导入商品
     *
     * @param goods
     */

    void addGoods(@Param(value = "goods") Goods goods);

    /**
     * 导入中间表
     *
     * @param id
     * @param orderId
     */

    void addGoodsByOrder(@Param(value = "id") int id, @Param(value = "orderId") String orderId);

    /**
     * 根据ID删除商品信息
     *
     * @param orderId
     */

    void deleteOrderId(@Param(value = "orderId") String orderId);

    //
//    /**
//     * 根据订单ID删除中间表信息
//     * @param orderId
//     */

    void deleteByOrder_Goods(@Param(value = "orderId") String orderId);

    /**
     * 修改商品信息
     * @param goods
     */

    void updateGoods(@Param(value = "goods") Map<String,Object> goods);

    /**
     * 更改原有商品转销售数量
     * @param sellNumber
     * id
     */
    void updateSellNumber(@Param(value = "sellNumber") int sellNumber,@Param(value = "id") int id);

    /**
     * 根据商品ID查询商品信息
     * @param id
     * @return
     */
    Goods findById(@Param(value = "id") int id);


    /**
     * 修改退货件数
     * @param goodsNumber
     * @param id
     */
    void updateReturnNumber(@Param("returnNumber") int goodsNumber, @Param(value = "id") int id);
}
