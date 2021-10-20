package com.ybau.transaction.util;

import com.ybau.transaction.domain.Goods;
import com.ybau.transaction.domain.Order;
import com.ybau.transaction.mapper.DataStatisticsMapper;
import com.ybau.transaction.mapper.OrderMapper;
import com.ybau.transaction.mapper.OrganizationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OrderStatistics {

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    DataStatisticsMapper dataStatisticsMapper;


    /**
     * 订单数据统计 一分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    private void calculateOrderData() {
        List<String> organizationIds = organizationMapper.findById();//查询所有公司信息
        for (String organizationId : organizationIds) {
            int limit = 0;//额度下单数量
            int limitRefund = 0;//额度下单退款订单数量
            int borrow = 0;//借样下单数量
            int borrowRefund = 0;//借样退款订单数量
            int borrowMarket = 0;//借样订单转销售订单数量
            int direct = 0;//直接购买数量数量
            int directRefund = 0;//直接购买退款订单数量
            double limitAggregate = 0;//额度下单总金额
            double limitRefundAggregate = 0;//额度下单退款总金额
            double borrowAggregate = 0;//借样下单总金额
            double borrowRefundAggregate = 0;//借样归还金额
            double borrowMarketAggregate = 0;//借样转销售金额
            double directAggregate = 0;//直接购买总金额
            double directRefundAggregate = 0;//直接购买退款总金额
            List<Order> orders = orderMapper.findOrganizationId(organizationId);//根据公司ID查询该公司所有有效订单
            for (Order order : orders) {
                if (order.getPaymentMethod() == 1) {
                    //额度下单计算总金额
                    limit++;
                    limitAggregate = BigDecimalUtil.add(limitAggregate, BigDecimalUtil.add(order.getActualMoney(), order.getFreight()));//计算该订单总金额
                    List<Goods> goodsDetail = order.getGoodsDetail();
                    for (Goods goods : goodsDetail) {
                        if (goods.getReturnNumber() > 0) {
                            //判断此订单是否有退款
                            limitRefund++;
                        }
                    }
                    limitRefundAggregate = order.getRefundAmount();//取出退款金额
                } else if (order.getPaymentMethod() == 2) {
                    //如果是借样订单
                    borrow++;//借样订单数量增加
                    borrowAggregate = order.getSumMoney();//赋值订单总金额
                    List<Goods> goodsDetail = order.getGoodsDetail();
                    for (Goods goods : goodsDetail) {
                        if (goods.getReturnNumber() > 0) {
                            //如果有退款商品
                            borrowRefund++;
                            borrowRefundAggregate = BigDecimalUtil.add(BigDecimalUtil.mul(goods.getReturnNumber(), goods.getGoodsPrice()), borrowRefundAggregate);//计算借样归还金额
                        }
                        if (goods.getSellNumber() > 0) {
                            //如果有转销售
                            borrowMarket++;
                            borrowMarketAggregate = BigDecimalUtil.add(borrowMarketAggregate, BigDecimalUtil.mul(goods.getSellNumber(), goods.getGoodsPrice()));//计算转销售金额
                        }
                    }
                } else if (order.getPaymentMethod() == 3) {
                    //如果是直接购买
                    direct++;
                    directAggregate = BigDecimalUtil.add(directAggregate, BigDecimalUtil.add(order.getActualMoney(), order.getFreight()));//计算该订单总金额
                    List<Goods> goodsDetail = order.getGoodsDetail();
                    for (Goods goods : goodsDetail) {
                        if (goods.getReturnNumber() > 0) {
                            directRefund++;
                        }
                    }
                    directRefundAggregate = order.getRefundAmount();
                }
            }
            int count = dataStatisticsMapper.updateDataStatistics("额度下单", limitAggregate, limitRefundAggregate, 0, limit, limitRefund, 0, organizationId);
            if (count < 1) {
                //小于1新增
                dataStatisticsMapper.insertDataStatistics("额度下单", limitAggregate, limitRefundAggregate, 0, limit, limitRefund, 0, organizationId);
            }
            int countBorrow = dataStatisticsMapper.updateDataStatistics("借样下单", borrowAggregate, borrowRefundAggregate, borrowMarketAggregate, borrow, borrowRefund, borrowMarket, organizationId);
            if (countBorrow < 1) {
                //小于1新增
                dataStatisticsMapper.insertDataStatistics("借样下单", borrowAggregate, borrowRefundAggregate, borrowMarketAggregate, borrow, borrowRefund, borrowMarket, organizationId);
            }
            int countDirect = dataStatisticsMapper.updateDataStatistics("直接购买", directAggregate, directRefundAggregate, 0, direct, directRefund, 0, organizationId);
            if (countDirect < 1) {
                //小于1新增
                dataStatisticsMapper.insertDataStatistics("直接购买", directAggregate, directRefundAggregate, 0, direct, directRefund, 0, organizationId);
            }
        }
    }


}
