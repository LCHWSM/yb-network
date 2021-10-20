package com.ybau.transaction.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class ExcelOrder implements Serializable {

    @Excel(name = "订单编号", needMerge = true, width = 30)
    private String orderId;//订单
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "下单时间", format = "yyyy-MM-dd HH:mm:ss", needMerge = true, width = 20)
    private Date orderTime; //订单创建时间
    @Excel(name = "收货人姓名", needMerge = true, width = 10)
    private String receiverName;//收货人姓名
    @Excel(name = "收货人手机号", needMerge = true, width = 20)
    private String receiverMobile;//收货人手机号
    @Excel(name = "收货人地址（省）", needMerge = true, width = 20)
    private String receiverProvince;//收货人地址（省）
    @Excel(name = "收货人地址(市)", needMerge = true, width = 20)
    private String receiverCity;//市
    @Excel(name = "收货人地址(区/县)", needMerge = true, width = 20)
    private String receiverDistrict;//区（县）
    @Excel(name = "收货人地址(乡镇/街道)", needMerge = true, width = 20)
    private String receiverStreet;//乡镇（街道）
    @Excel(name = "收货人地址(其它，例如门牌等)", needMerge = true, width = 20)
    private String receiverAddress;//收货人地址详细信息
    @Excel(name = "下单方式", needMerge = true, width = 10)
    private String paymentMethodStr;
    @Excel(name = "审核状态", needMerge = true, width = 10)
    private String auditStr;
    @Excel(name = "结算状态", needMerge = true, width = 15)
    private String paymentStatusStr;//结算状态 中文
    private int expressOnly;//发货标识
    @Excel(name = "发货状态", needMerge = true, width = 10)
    private String expressOnlyStr;
    private int audit;//审核状态（2审核不通过  1审核通过   3 审核中 4 审核撤销 5 订单已取消）
    @Excel(name = "退货状态", needMerge = true, width = 10)
    private String retreatCargoStr;
    @Excel(name = "是否采集到数据仪表盘", needMerge = true, width = 10)
    private String orderSignStr;
    private int orderSign;
    private int paymentStatus;//付款状态（1：未付款，2：已付款，3：紧急发货，4：未归还，5：已归还，6：退款中,7：已退款，8部分结算）
    private int paymentMethod;//付款方式（1：额度下单，2：借样，3：直接下单）
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "借样订单预计归还时间", format = "yyyy-MM-dd", needMerge = true, width = 20)
    private Date returnTime;//借样归还时间
    private int addCompany;//公司ID
    @Excel(name = "公司名字", needMerge = true, width = 20)
    private String organizationName;
    @Excel(name = "下单人", needMerge = true, width = 20)
    private String addUser;//创建订单用户用户名
    private String name;
    @Excel(name = "订单总金额", needMerge = true, width = 10)
    private double sumMoney;//订单总金额
    @Excel(name = "订单实收金额", needMerge = true, width = 10)
    private double actualMoney;//订单实收金额
    @Excel(name = "订单其他金额", needMerge = true, width = 10)
    private double freight;//订单运费
    private int retreatCargo;//退货标识（1，退货中2，已退货）
    @Excel(name = "已结算金额", needMerge = true, width = 10)
    private double settlementAmount;//已结算金额
    @Excel(name = "退款金额", needMerge = true, width = 10)
    private double refundAmount;//已结算金额
    @Excel(name = "结算服务费", needMerge = true, width = 10)
    private double serveCost;
    @Excel(name = "订单备注", needMerge = true, width = 30)
    private String orderRemark;//订单备注
    @Excel(name = "发票抬头", needMerge = true, width = 20)
    private String unitName;//公司或个人姓名
    @Excel(name = "发票抬头类型", needMerge = true, width = 10)
    private String flagStr;
    @Excel(name = "发票-纳税人识别号", needMerge = true, width = 20)
    private String invoiceNumber;//税号
    @Excel(name = "发票-地址/电话", needMerge = true, width = 20)
    private String unitSite;//地址
    @Excel(name = "发票-开户行及账号", needMerge = true, width = 20)
    private String bankDeposit;//开户行
    private int flag;//1：个人，2：公司
    @Excel(name = "发票邮寄地址（专票必填)", needMerge = true, width = 20)
    private String ticketSite;//收票地址
    @Excel(name = "发票-电子邮箱（电子发票必填）", needMerge = true, width = 20)
    private String email;//邮箱
    private int invoiceType;//发票类型 1：普票  2：专票
    @Excel(name = "发票类型", needMerge = true, width = 10)
    private String invoiceTypeStr;
    @Excel(name = "快递公司", needMerge = true, width = 20)
    private String expressName;
    @Excel(name = "快递单号", needMerge = true, width = 20)
    private String expressNumbers;
    @ExcelCollection(name = "")
    private List<Goods> goodsDetail;//商品实体类

    public String getOrderSignStr() {
        if (orderSign == 1) {
            orderSignStr = "是";
        } else if (orderSign == 0) {
            orderSignStr = "否";
        }
        return orderSignStr;
    }

    public void setOrderSignStr(String orderSignStr) {
        this.orderSignStr = orderSignStr;
    }

    public String getUnitName() {
        if (unitName == null) {
            unitName = "";
        }
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getInvoiceNumber() {
        if (invoiceNumber == null) {
            invoiceNumber = "";
        }
        return invoiceNumber;
    }


    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getUnitSite() {
        if (unitSite == null) {
            unitSite = "";
        }
        return unitSite;
    }

    public void setUnitSite(String unitSite) {
        this.unitSite = unitSite;
    }


    public String getBankDeposit() {
        if (bankDeposit == null) {
            bankDeposit = "";
        }
        return bankDeposit;
    }

    public void setBankDeposit(String bankDeposit) {
        this.bankDeposit = bankDeposit;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getTicketSite() {
        if (ticketSite == null) {
            ticketSite = "";
        }
        return ticketSite;
    }

    public void setTicketSite(String ticketSite) {
        this.ticketSite = ticketSite;
    }


    public String getFlagStr() {
        if (flag == 1) {
            flagStr = "个人";
        } else if (flag == 2) {
            flagStr = "公司";
        }
        return flagStr;
    }

    public void setFlagStr(String flagStr) {
        this.flagStr = flagStr;
    }

    public String getInvoiceTypeStr() {
        if (invoiceType == 1) {
            invoiceTypeStr = "普票";
        } else if (invoiceType == 2) {
            invoiceTypeStr = "专票";
        }
        return invoiceTypeStr;
    }

    public void setInvoiceTypeStr(String invoiceTypeStr) {
        this.invoiceTypeStr = invoiceTypeStr;
    }


    public String getPaymentStatusStr() {

        if (paymentStatus == 1 && paymentMethod == 3) {
            paymentStatusStr = "未结算/不可发货";
        } else if (paymentStatus == 1 && paymentMethod == 1) {
            paymentStatusStr = "未结算/可发货";
        } else if (paymentStatus == 2) {
            paymentStatusStr = "已结算/可发货";
        } else if (paymentStatus == 3) {
            paymentStatusStr = "未结算/可发货";
        } else if (paymentStatus == 4) {
            paymentStatusStr = "未归还";
        } else if (paymentStatus == 5) {
            paymentStatusStr = "已归还";
        } else if (paymentStatus == 6) {
            paymentStatusStr = "退款中";
        } else if (paymentStatus == 7) {
            paymentStatusStr = "已退款";
        } else if (paymentStatus == 8 && expressOnly == 2) {
            paymentStatusStr = "部分结算/不可发货";
        } else if (paymentStatus == 8 && expressOnly == 0 || expressOnly == 1) {
            paymentStatusStr = "部分结算/可发货";
        }

        return paymentStatusStr;
    }

    public void setPaymentStatusStr(String paymentStatusStr) {
        this.paymentStatusStr = paymentStatusStr;
    }

    public String getOrderRemark() {
        if (this.orderRemark == null) {
            orderRemark = "";
        }
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getExpressOnlyStr() {
        if (expressOnly == 0 || expressOnly == 2) {
            expressOnlyStr = "未发货";
        } else if (expressOnly == 1) {
            expressOnlyStr = "已发货";
        }
        return expressOnlyStr;
    }

    public void setExpressOnlyStr(String expressOnlyStr) {
        this.expressOnlyStr = expressOnlyStr;
    }

    public String getAuditStr() {
        if (audit == 1) {
            auditStr = "审核通过";
        } else if (audit == 2) {
            auditStr = "审核不通过";
        } else if (audit == 3) {
            auditStr = "审核中";
        } else if (audit == 4) {
            auditStr = "审核撤销";
        } else if (audit == 5) {
            auditStr = "订单取消";
        }
        return auditStr;
    }

    public void setAuditStr(String auditStr) {
        this.auditStr = auditStr;
    }

    public String getPaymentMethodStr() {
        if (paymentMethod == 1) {
            paymentMethodStr = "额度下单";
        } else if (paymentMethod == 2) {
            paymentMethodStr = "借样下单";
        } else if (paymentMethod == 3) {
            paymentMethodStr = "直接购买";
        }
        return paymentMethodStr;
    }

    public void setPaymentMethodStr(String paymentMethodStr) {
        this.paymentMethodStr = paymentMethodStr;
    }

    public String getRetreatCargoStr() {
        if (retreatCargo == 1) {
            retreatCargoStr = "退货中";
        } else if (retreatCargo == 2) {
            retreatCargoStr = "已退货";
        } else if (retreatCargo == 0) {
            retreatCargoStr = "未退货";
        }
        return retreatCargoStr;
    }

    public void setRetreatCargoStr(String retreatCargoStr) {
        this.retreatCargoStr = retreatCargoStr;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }


    public String getReceiverName() {
        if (receiverName == null) {
            receiverName = "";
        }
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        if (receiverMobile == null) {
            receiverMobile = "";
        }
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverProvince() {
        if (receiverProvince == null) {
            receiverProvince = "";
        }
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        if (receiverCity == null) {
            receiverCity = "";
        }
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        if (receiverDistrict == null) {
            receiverDistrict = "";
        }
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverStreet() {
        if (receiverStreet == null) {
            receiverStreet = "";
        }
        return receiverStreet;
    }

    public void setReceiverStreet(String receiverStreet) {
        this.receiverStreet = receiverStreet;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }


    public int getExpressOnly() {
        return expressOnly;
    }

    public void setExpressOnly(int expressOnly) {
        this.expressOnly = expressOnly;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(double sumMoney) {
        this.sumMoney = sumMoney;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public int getAddCompany() {
        return addCompany;
    }

    public void setAddCompany(int addCompany) {
        this.addCompany = addCompany;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAddUser() {

        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(double actualMoney) {
        this.actualMoney = actualMoney;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public int getRetreatCargo() {
        return retreatCargo;
    }

    public void setRetreatCargo(int retreatCargo) {
        this.retreatCargo = retreatCargo;
    }

    public double getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(double settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public List<Goods> getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(List<Goods> goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
}
