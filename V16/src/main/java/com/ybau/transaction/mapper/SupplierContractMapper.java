package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Contract;
import com.ybau.transaction.domain.SupplierContract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SupplierContractMapper {
    /**
     * 根据订单ID查询订单关联的合同
     * @param srId
     * @return
     */
    Contract findBySrId(@Param(value = "srId") String srId);

    /**
     * 插入用户上传合同信息
     * @param map
     */
    void saveContract(@Param(value = "map") Map<String, Object> map);

    /**
     * 根据合同ID删除合同
     * @param supplierContractId
     */
    void deleteContr(@Param(value = "supplierContractId") int supplierContractId);

    /**
     * 根据合同ID查询合同信息
     * @param supplierContractId
     * @return
     */
    Contract findById(@Param(value = "supplierContractId") Integer supplierContractId);

}
