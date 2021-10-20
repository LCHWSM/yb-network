package com.ybau.transaction.domain;

import lombok.Data;

/**
 * 快递公司实体类
 */

@Data
public class Company {

    private int id;
    //快递中文名字
    private String name;
    //快递唯一标识
    private String companyOnly;

}
