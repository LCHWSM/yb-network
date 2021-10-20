package com.ybau.transaction.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单实体类
 */
@Data
public class Order {
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
    private Invoice invoice;//发票实体类
    private double settlementAmount;//已结算金额
    private int orderSign;//为1标记为是否为机器销售
    private String orderSignStr;
    private String orderSignNameStr;//根据权限返回数字
    private String orderSignName;//不同权限显示不同字体
    private double serveCost;//服务费
    private int orderInvoiceId;
    private double refundAmount;//退款金额

    public String getOrderSignName() {
        if (orderSignNameStr == null) {
            orderSignName = "本订单是购金机等渠道销售的订单，数据仪表盘不要统计此订单数据:";
        } else {
            if (orderSignNameStr.contains("1")) {
                orderSignName = "本订单的的商品将在购金机上销售:";
            }
            if (orderSignNameStr.contains("2")) {
                orderSignName = "";
            }
            if (orderSignNameStr.contains("12") || orderSignNameStr.contains("21")) {
                orderSignName = "";
            }
        }

        return orderSignName;
    }

    public void setOrderSignName(String orderSignName) {
        this.orderSignName = orderSignName;
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

    public String getReceiverMobile() {
        if (receiverMobile == null) {
            receiverMobile = "";
        }
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
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

    public int getRetreatCargo() {
        return retreatCargo;
    }

    public void setRetreatCargo(int retreatCargo) {
        this.retreatCargo = retreatCargo;
    }

    public String getOrderSignStr() {
        if (orderSign == 1) {
            orderSignStr = "是";
        } else {
            orderSignStr = "否";
        }

        return orderSignStr;
    }

    public void setOrderSignStr(String orderSignStr) {
        this.orderSignStr = orderSignStr;
    }
}
