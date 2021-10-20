package com.ybau.transaction.mapper;


import com.ybau.transaction.domain.Universal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversalMapper {
    /**
     * 更新通用库存
     *
     * @param red
     * @param yellow
     */
    void updateWarning(@Param(value = "red") int red, @Param(value = "yellow") int yellow);

    Universal findAll();

}
