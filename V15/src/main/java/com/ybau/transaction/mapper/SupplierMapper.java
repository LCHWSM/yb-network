package com.ybau.transaction.mapper;

import com.ybau.transaction.domain.Supplier;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sun.awt.SunHints;

import java.util.List;
import java.util.Map;

@Repository
public interface SupplierMapper {
    /**
     * 新增供应商信息
     * @param map
     */
    void saveSupplier(@Param(value = "map") Map<String, Object> map);

    /**
     * 查询所有供应商记录
     * @param map
     * @return
     */
    List<Supplier> findAll(@Param(value = "map") Map<String, Object> map);

    /**
     * 修改供应商信息
     * @param map
     */
    void updateSupplier(@Param(value = "map") Map<String, Object> map);

    /**
     * 删除成功
     * @param supplierId
     */
    void deleteById(@Param(value = "supplierId") int supplierId);

    /**
     * 查询供应商信息
     * @return
     */
    List<Supplier> findSupplier(@Param(value = "supplierName") String supplierName,@Param(value = "organizationId") int organizationId);

    /**
     * 查询供应商列表
     * @param supplierName
     * @return
     */
    List<Supplier> findBySupplier(String supplierName);

    /**
     * 根据供应商ID查询是否有下属订单
     * @param supplierId
     * @return
     */
    int findBySupplierId(@Param(value = "supplierId") int supplierId);
}
