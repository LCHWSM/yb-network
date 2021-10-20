package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Contract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ContractMapper {
    /**
     * 查询合同列表（分页）
     * @param map
     * @return
     * @Date 2018/7/10 13：19
     */
    List<Contract> findAll(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询合同名字或合同编号是否重复
     *
     * @param contractName
     * @param contractNumber
     * @return
     */
    int findContractName(@Param(value = "contractName") String contractName, @Param(value = "contractNumber") String contractNumber);

    /**
     * 新增合同信息
     *
     * @param map
     */
    void saveContract(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据合同ID查询该合同下是否存在订单
     *
     * @param contractId
     * @return
     */
    int findByOrder(@Param(value = "contractId") int contractId);

    /**
     * 根据合同ID删除合同
     *
     * @param contractId
     */
    void deleteByContractId(@Param(value = "contractId") int contractId);

    /**
     * 添加订单合同编号
     * @param orderId
     * @param contractId
     */
    void saveContractId(@Param(value = "orderId") String orderId,@Param(value = "contractId") int contractId);

    /**
     * 修改合同信息
     */
    void updateContract(@Param(value = "map") Map<String,Object> map);

    /**
     * 取消订单 关联
     * @param orderId
     */
    void updateConByOrderId(@Param(value = "orderId") String orderId);

    /**
     * 根据ID查询合同信息
     * @param id
     * @return
     */
    Contract findByContractId(@Param(value = "id") int id);

    /**
     * 根据合同ID查询合同信息
     * @param contractId
     * @return
     */
    Contract findById(@Param(value = "contractId") int contractId);

    /**
     * 根据订单ID 查询订单合同信息
     * @param orderId
     * @return
     */
    Contract findByOrderId(@Param(value = "orderId") String orderId);

    /**
     * 删除合同信息
     * @param contractId
     */
    void deleteContract(@Param(value = "contractId") int contractId);
}
