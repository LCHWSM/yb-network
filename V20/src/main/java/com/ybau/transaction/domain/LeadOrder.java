package com.ybau.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class LeadOrder {
    private String orderId;//订单
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime; //订单创建时间
    private String customerName;//用户姓名
    private String customerMobile;//用户手机号
    private String receiverName;//收货人姓名
    private String receiverMobile;//收货人手机号
    private String receiverProvince;//收货人地址（省）
    private String receiverCity;//市
    private String receiverDistrict;//区（县）
    private String receiverStreet;//乡镇（街道）
    private String receiverAddress;//收货人地址详细信息
    private String receiverPostcode;//邮编
    private int expressOnly;//发货标识
    private String expressOnlyStr;
    private List<Goods> goodsDetail;//商品实体类
    private Express express;//快递实体类
    private String site;//用户地址（省市县详细地址）
    private User user;//订单创建人
    private Organization organization;//订单所属公司
    private int audit;//审核状态（2审核不通过  1审核通过   3 审核中 4 审核撤销 5 订单已取消）
    private String auditStr;
    private double sumMoney;//订单总金额
    private int contractId;//合同ID
    private Contract contract;//合同实体类
    private int paymentStatus;//付款状态（1：未付款，2：已付款，3：紧急发货，4：未归还，5：已归还，6：退款中,7：已退款，8部分结算）
    private String paymentStatusStr;//结算状态 中文
    private int paymentMethod;//付款方式（1：额度下单，2：借样，3：直接下单）
    private String paymentMethodStr;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date returnTime;//借样归还时间
    private int addCompany;//公司ID
    private String addUser;//创建订单用户ID
    private List<Map> auditFlowList;//正在使用的流程节点信息
    private Audit auditNode;
    private int auditFlowGrade;//该订单所在顺序
    private int processGroupId;//该订单所在分组
    private double actualMoney;//订单实收金额
    private double freight;//订单运费
    private int retreatCargo;//退货标识（1，退货中2，已退货）
    private String retreatCargoStr;
    private String orderRemark;//订单备注
    private LeadInvoice invoice;//发票实体类
    private double settlementAmount;//已结算金额
    private String orderSignStr;
    private int orderSign;
    private double serveCost;
    private String proposer; //申请人字段
    private String dingNumber;//钉钉编号字段

    public int getOrderSign() {
        if (orderSignStr.equals("否")) {
            orderSign = 0;
        } else if (orderSignStr.equals("是")) {
            orderSign = 1;
        }
        return orderSign;
    }

    public void setOrderSign(int orderSign) {
        this.orderSign = orderSign;
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
            paymentStatusStr = "额度下单";
        } else if (paymentMethod == 2) {
            paymentStatusStr = "借样下单";
        } else if (paymentMethod == 3) {
            paymentStatusStr = "直接购买";
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
        }
        return retreatCargoStr;
    }

    public void setRetreatCargoStr(String retreatCargoStr) {
        this.retreatCargoStr = retreatCargoStr;
    }


    public int getExpressOnly() {
        if (expressOnlyStr.equals("已发货")) {
            expressOnly = 1;
        } else if (expressOnlyStr.equals("未发货")) {
            expressOnly = 0;
        }
        return expressOnly;
    }

    public void setExpressOnly(int expressOnly) {
        this.expressOnly = expressOnly;
    }

    public int getAudit() {
        if (auditStr.equals("审核通过")) {
            audit = 1;
        } else if (auditStr.equals("审核不通过")) {
            audit = 2;
        } else if (auditStr.equals("审核中")) {
            audit = 3;
        } else if (auditStr.equals("审核撤销")) {
            audit = 4;
        } else if (auditStr.equals("订单取消")) {
            audit = 5;
        }
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public int getPaymentStatus() {
        if (paymentStatusStr.equals("未结算/不可发货")) {
            paymentStatus = 1;
        } else if (paymentStatusStr.equals("已结算")) {
            paymentStatus = 2;
        } else if (paymentStatusStr.equals("未结算/可发货")) {
            paymentStatus = 3;
        } else if (paymentStatusStr.equals("未归还（借样）")) {
            paymentStatus = 4;
        } else if (paymentStatusStr.equals("已归还（借样）")) {
            paymentStatus = 5;
        } else if (paymentStatusStr.equals("退款中")) {
            paymentStatus = 6;
        } else if (paymentStatusStr.equals("已退款")) {
            paymentStatus = 7;
        } else if (paymentStatusStr.equals("部分结算")) {
            paymentStatus = 8;
        }
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentMethod() {
        if (paymentMethodStr.equals("额度下单")) {
            paymentMethod = 1;
        } else if (paymentMethodStr.equals("借样下单")) {
            paymentMethod = 2;
        } else if (paymentMethodStr.equals("直接购买")) {
            paymentMethod = 3;
        }
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getRetreatCargo() {
        if (retreatCargoStr.equals("退货中")) {
            retreatCargo = 1;
        } else if (retreatCargoStr.equals("已退货")) {
            retreatCargo = 2;
        }
        return retreatCargo;
    }

    public void setRetreatCargo(int retreatCargo) {
        this.retreatCargo = retreatCargo;
    }

    @Override
    public String toString() {
        return "LeadOrder{" +
                "orderId='" + orderId + '\'' +
                ", orderTime=" + orderTime +
                ", customerName='" + customerName + '\'' +
                ", customerMobile='" + customerMobile + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverMobile='" + receiverMobile + '\'' +
                ", receiverProvince='" + receiverProvince + '\'' +
                ", receiverCity='" + receiverCity + '\'' +
                ", receiverDistrict='" + receiverDistrict + '\'' +
                ", receiverStreet='" + receiverStreet + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", receiverPostcode='" + receiverPostcode + '\'' +
                ", expressOnly=" + expressOnly +
                ", expressOnlyStr='" + expressOnlyStr + '\'' +
                ", goodsDetail=" + goodsDetail +
                ", express=" + express +
                ", site='" + site + '\'' +
                ", user=" + user +
                ", organization=" + organization +
                ", audit=" + audit +
                ", auditStr='" + auditStr + '\'' +
                ", sumMoney=" + sumMoney +
                ", contractId=" + contractId +
                ", contract=" + contract +
                ", paymentStatus=" + paymentStatus +
                ", paymentStatusStr='" + paymentStatusStr + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", paymentMethodStr='" + paymentMethodStr + '\'' +
                ", returnTime=" + returnTime +
                ", addCompany=" + addCompany +
                ", addUser='" + addUser + '\'' +
                ", auditFlowList=" + auditFlowList +
                ", auditNode=" + auditNode +
                ", auditFlowGrade=" + auditFlowGrade +
                ", processGroupId=" + processGroupId +
                ", actualMoney=" + actualMoney +
                ", freight=" + freight +
                ", retreatCargo=" + retreatCargo +
                ", retreatCargoStr='" + retreatCargoStr + '\'' +
                ", orderRemark='" + orderRemark + '\'' +
                ", invoice=" + invoice +
                ", settlementAmount=" + settlementAmount +
                '}';
    }
}
