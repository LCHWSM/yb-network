package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.ExcelProduct;
import com.ybau.transaction.domain.Goods;
import com.ybau.transaction.domain.Product;
import com.ybau.transaction.domain.Universal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductMapper {


    /**
     * 查询商品库存数量
     * @param goodsId
     * @return
     */
    int findByPId(@Param(value = "goodsId") String goodsId);


    /**
     * 新增商品
     *
     * @param map
     */
    void saveProduct(@Param(value = "map") Map<String, Object> map);

    /**
     * 删除商品
     *
     * @param id
     */
    void deleteProduct(@Param(value = "id")int id);

    /**
     * 修改商品
     *
     * @param map
     */
    void updateProduct(@Param(value = "map")Map<String, Object> map);

    /**
     * 条件查询所有
     *
     * @param map
     * @return
     */
    List<Product> findAll(@Param(value = "map")Map<String, Object> map);

    /**
     * 根据ID查询商品
     *
     * @param id
     * @return
     */
    Product findById(@Param(value = "id")int id);

    /**
     * 查询分类下是否存在商品
     * @param id
     * @return
     */
    int findByCid(@Param(value = "id")int id);

    /**
     * 查新所有商品（分类前台展示）
     * @param map
     * @return
     */
    List<Product> findProduct(@Param(value = "map")Map<String, Object> map);

    /**
     * 增加库存
     * @param map
     */
    void updateAddWarehouse(@Param(value = "map")Map<String, Object> map);

    /**
     * 根据名字查询商品
     * @param name
     * @return
     */
    int findByName(@Param(value = "name")String name,@Param(value = "core") String core,@Param(value = "id") Integer id);

    /**
     * 减少库存
     * @param map
     */
    int updateSubWarehouse(@Param(value = "map")Map<String, Object> map);


    /**
     * 增加库存
     * @param map
     */
    void updateWarehouse(@Param(value = "map")Map<String, Object> map);


    @Select("select id from product")
    List<Integer> selectProductIdList();

    @Select("select description from product where id= #{id}")
    String selectDescriptionById(@Param(value = "id") int id);

    @Update("update product set description=#{des} where id = #{id}")
    int updateDescriptionUrl(@Param("des")String des, @Param("id")int id);

    /**
     * 根据购物车存储的商品ID查询商品信息
     * @param parseInt
     * @return
     */
    Goods finGoodId(@Param(value = "parseInt") int parseInt);

    /**
     * 查询商品预警数量
     * @return
     */
    Universal findUniversal();

    /**
     * 根据商品名字查询是否存在商品
     * @param gName
     * @return
     */
    Product findByPName(@Param(value = "gName") String gName);

    /**
     * 根据商品名字和编码查询
     * @param gName
     * @param gCore
     * @return
     */
    List<String> findNameBygCore(@Param(value = "gName") String gName,@Param(value = "gCore") String gCore);

    /**
     * 根据商品Id查询商品价格
     * @param goodsId
     * @return
     */
    double findByPrive(@Param(value = "goodsId") String goodsId);

    /**
     * 查询所有商品信息
     * @return
     */
    List<Product> getAll();

    /**
     * 插入导入商品
     * @param excelProduct
     */
    void leadProduct(@Param(value = "excelProduct") ExcelProduct excelProduct);


    /**
     * 导出数据查询
     * @param classifyId
     * @return
     */
    List<ExcelProduct> exportProduct(@Param(value = "classifyId") String classifyId);
}

