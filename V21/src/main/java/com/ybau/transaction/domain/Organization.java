package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * 机构实体类
 */
@Data
public class Organization {

    private int organizationId;//机构id
    private String organizationName;//机构名字
    private double organizationLimit;//机构额度
    private String organizationLimitStr;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;//添加时间
    private User addUser;//添加人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;//最后修改时间
    private User updateUser;//最后修改人
    private List<User> users;//公司下属用户
    private double useBalance;//可用额度
    private String useBalanceStr;
    private double availableBalance;//已使用额度
    private String availableBalanceStr;
    private int processGroupId;//标记该公司目前使用的哪一组流程
    private int settingTime;//设置该公司借样订单预期归还时间
    private String way;//设定该公司的下单方式(1为额度下单，2为借样3为直接购买)
    private List<Supplier> supplier;//供应商实体类
    private List<DataStatistics> dataStatistics;



}
