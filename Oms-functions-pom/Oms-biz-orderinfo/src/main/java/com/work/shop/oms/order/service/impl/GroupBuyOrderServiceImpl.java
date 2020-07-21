package com.work.shop.oms.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.order.service.GroupBuyOrderService;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupBuyOrderServiceImpl implements GroupBuyOrderService {

    @Resource
    private OrderCancelService orderCancelService;

    @Resource
    private MasterOrderInfoMapper masterOrderInfoMapper;
    @Resource
    private MasterOrderPayMapper masterOrderPayMapper;
    @Resource
    private MasterOrderGoodsMapper masterOrderGoodsMapper;
    @Resource
    private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
    @Override
    public void processGroupByResult(ProductGroupBuyBean groupBuyBean) {

        //团购id
        Integer groupId = groupBuyBean.getId();

        List<MasterOrderInfoExtend> extendList=masterOrderInfoExtendMapper.selectOrderSnByGroupId(groupId);
        if (CollectionUtils.isEmpty(extendList)) {
            log.error("未查询到该团购下的非取消订单,团购id:{}",groupId);
            return;
        }
        List<String> orderSnList = extendList.stream().map(x -> x.getMasterOrderSn()).collect(Collectors.toList());

        String priceRuleStr = groupBuyBean.getPriceRule();

        if (StringUtils.isBlank(priceRuleStr)) {
            log.error("团购规则配置有误");
            return;
        }
        //获取最低规则折扣
        List<Map> maps = JSON.parseArray(priceRuleStr, Map.class);
        BigDecimal minDiscount = BigDecimal.valueOf(1);//最小折扣
        for (Map map : maps) {
            Set<String> keySet = map.keySet();
            String next = keySet.iterator().next();
            if (groupBuyBean.getOrderMoney().compareTo(new BigDecimal(next)) > 0) {
                if (minDiscount.compareTo(new BigDecimal(map.get(next).toString())) > 0) {
                    minDiscount = new BigDecimal(map.get(next).toString());
                }
            }
        }


        if ("3".equals(groupBuyBean.getGroupBuyStatus().toString())) {
            //团购失败
            //取消订单
            for (String masterOrderSn : orderSnList) {
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setAdminUser(Constant.OS_STRING_SYSTEM);
                orderStatus.setType(ConstantValues.CREATE_RETURN.YES);
                orderStatus.setCode("10002");
                orderCancelService.cancelOrderByMasterSn(masterOrderSn, orderStatus);
            }

        } else if ("2".equals(groupBuyBean.getGroupBuyStatus().toString())) {
            //团购成功
            if (groupBuyBean.getParticipateGroupType() == 1 || groupBuyBean.getPayRate().compareTo(BigDecimal.ZERO) == 0) {
                //下单参与团购


                for (String orderSn : orderSnList) {
                    processTotalFee(minDiscount, orderSn);
                }

            } else {
                //付预付款参与团购
                for (String orderSn : orderSnList) {
                    MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
                    if (masterOrderInfo.getPayStatus() != 1) {
                        OrderStatus orderStatus = new OrderStatus();
                        orderStatus.setAdminUser(Constant.OS_STRING_SYSTEM);
                        orderStatus.setType(ConstantValues.CREATE_RETURN.YES);
                        orderStatus.setCode("10002");
                        orderCancelService.cancelOrderByMasterSn(orderSn, orderStatus);
                        continue;
                    }
                    processTotalFee(minDiscount, orderSn);
                }
            }

        } else {
            log.error("团购状态错误:{}", JSON.toJSONString(groupBuyBean));
        }
    }

    private void processTotalFee(BigDecimal minDiscount, String orderSn) {
        MasterOrderGoodsExample e = new MasterOrderGoodsExample();
        e.createCriteria().andMasterOrderSnEqualTo(orderSn);
        List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(e);
        BigDecimal totalFee = BigDecimal.ZERO;
        for (MasterOrderGoods masterOrderGoods : goodsList) {
            //计算商品单价
            BigDecimal unitPrice = masterOrderGoods.getGoodsPrice().subtract(masterOrderGoods.getGoodsAddPrice());
            totalFee.add(unitPrice.multiply(BigDecimal.valueOf(masterOrderGoods.getGoodsNumber())));
        }
        //计算加价
        MasterOrderPay orderPay = masterOrderPayMapper.selectByMasterOrderSn(orderSn);
        BigDecimal paymentRate = orderPay.getPaymentRate().add(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(100));
        totalFee = totalFee.multiply(paymentRate);
        //添加运费
        MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
        BigDecimal shippingTotalFee = orderInfo.getShippingTotalFee();
        totalFee = totalFee.add(shippingTotalFee);

        //最终成交价
        totalFee=totalFee.multiply(minDiscount);

        MasterOrderInfo masterOrderInfo = new MasterOrderInfo();
        masterOrderInfo.setMasterOrderSn(orderSn);
        masterOrderInfo.setPayTotalFee(totalFee);
        masterOrderInfo.setTotalPayable(totalFee);
        masterOrderInfo.setBalanceAmount(totalFee.subtract(orderInfo.getPrepayments()));
        masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfo);
    }
}
