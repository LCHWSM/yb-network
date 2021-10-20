package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Company;
import com.ybau.transaction.domain.Express;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 快递持久成
 */
@Repository
public interface ExpressMapper {

    /**
     * 查询快递信息测试
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    @Select("select * from expresstest where orderId=#{orderId}")
    Express findByOrdersIdtest(@Param(value = "orderId") String orderId) throws Exception;

    /**
     * 根据ID查询快递信息
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    @Select("select * from express where orderId=#{orderId}")
    Express findByOrdersId(@Param(value = "orderId") String orderId) throws Exception;


    /**
     * 查询该订单是否已发货
     *
     * @param orderId
     * @return
     */
    @Select("SELECT COUNT(expressId) FROM express WHERE orderId=#{orderId}")
    int findCount(@Param(value = "orderId") String orderId);

    /**
     * 查询快递公司所有信息
     *
     * @return
     */
    @Select("SELECT * FROM company ORDER BY CONVERT(NAME USING gbk)")
    List<Company> findAll();

    /**
     * 查询是否已经发货测试
     *
     * @param orderId
     * @return
     */
    @Select("SELECT COUNT(expressId) FROM expresstest WHERE orderId=#{orderId}")
    int findCounttest(@Param(value = "orderId") String orderId);

    /**
     * 插入快递测试
     *
     * @param map
     */
    @Insert("INSERT INTO expresstest (expressName,expressNumber,orderId) VALUES (#{map.expressId},#{map.expressNumber},#{map.orderId})")
    int saveExpresstest(@Param(value = "map") Map<String, Object> map) throws Exception;

    /**
     * 修改快递测试
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Update("<script> " +
            "update expresstest set " +
            "<if test='map.expressId!=null'> expressName = #{map.expressId},</if>" +
            "<if test='map.expressNumber!=null'> expressNumber=#{map.expressNumber} </if> where orderId = #{map.orderId}" +
            " </script>")
    int updateExpresstest(@Param(value = "map") Map<String, Object> map) throws Exception;


    /**
     * 修改快递参数
     *
     * @param
     * @return
     */
    @Update("update express set expressName = #{map.expressName},expressSign=#{map.expressSign},expressNumbers=#{map.expressNumber} where orderId = #{map.orderId}")
    int updateExpress(@Param(value = "map") Map<String, Object> map);


    /**
     * 新增发货信息
     *
     * @param
     * @param
     * @return
     */
    @Insert("INSERT INTO express (expressName,expressNumbers,orderId,successTime,expressSign) VALUES (#{expressName},#{expressNumbers},#{orderId},#{format},#{expressSign})")
    int saveExpress(@Param(value = "format") String format, @Param(value = "orderId") String orderId, @Param(value = "expressName") String expressName, @Param(value = "expressNumbers") String expressNumbers, @Param(value = "expressSign") String expressSign);

    /**
     * 插入借样订单实际归还时间
     *
     * @param practicalTime
     * @param orderId
     */
    @Update("update express set practicalTime = #{practicalTime} where orderId = #{orderId}")
    void updateTime(@Param(value = "practicalTime") Date practicalTime, @Param(value = "orderId") String orderId);

    /**
     * 录入订单退货信息
     *
     * @param map
     */
    @Update("update express set retreatName=#{map.retreatName},retreatNumbers=#{map.retreatNumbers},retreatTime=#{map.logTime} where orderId=#{map.orderId}")
    void updateRetreatCargo(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据订单ID删除快递信息
     * @param orderId
     */
    @Delete("delete from express where orderId=#{orderId} ")
    void deleteOrderId(@Param(value = "orderId") String orderId);

    /**
     * 插入发货订单
     * @param express
     * @param orderId
     */

    @Insert("insert into express (orderId,expressName,expressNumbers) values (#{orderId},#{express.expressName},#{express.expressNumbers})")
    void deliverGoods(@Param(value = "express") Express express,@Param(value = "orderId") String orderId);
}
