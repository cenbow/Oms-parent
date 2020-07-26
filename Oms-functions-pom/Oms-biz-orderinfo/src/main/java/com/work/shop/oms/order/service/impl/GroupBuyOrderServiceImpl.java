package com.work.shop.oms.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.PayReturnInfo;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.service.GroupBuyOrderService;
import com.work.shop.oms.order.service.OrderManagementService;
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
    private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

    @Resource
    private OrderManagementService orderManagementService;

    @Override
    public void processGroupByResult(ProductGroupBuyBean groupBuyBean) {
        //团购id
        Integer groupId = groupBuyBean.getId();
        MasterOrderInfoExtend record = new MasterOrderInfoExtend();
        record.setGroupId(groupId);
        List<MasterOrderInfoExtend> extendList = masterOrderInfoExtendMapper.selectOrderSnByGroupId(record);
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
        BigDecimal minDiscount = null;//最小折扣
        for (Map map : maps) {
            Set<String> keySet = map.keySet();
            String next = keySet.iterator().next();
            if (groupBuyBean.getOrderMoney().compareTo(new BigDecimal(next)) > -1) {
                if(minDiscount == null){
                    minDiscount = new BigDecimal(map.get(next).toString());
                }else{
                    if (minDiscount.compareTo(new BigDecimal(map.get(next).toString())) > -1) {
                        minDiscount = new BigDecimal(map.get(next).toString());
                    }
                }
            }
        }

        if(minDiscount == null){
            minDiscount = new BigDecimal(10);
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
//            if (groupBuyBean.getParticipateGroupType() == 1 || groupBuyBean.getPayRate().compareTo(BigDecimal.ZERO) == 0) {
//                //下单参与团购
//                for (String orderSn : orderSnList) {
//                    processTotalFee(minDiscount, orderSn);
//                }
//            } else {
                //付预付款参与团购
                for (String orderSn : orderSnList) {
                    MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
                    if (masterOrderInfo.getPayStatus() != 1) {
                        OrderStatus orderStatus = new OrderStatus();
                        orderStatus.setAdminUser(Constant.OS_STRING_SYSTEM);
                        orderStatus.setType(ConstantValues.CREATE_RETURN.NO);
                        orderStatus.setCode("10002");
                        orderCancelService.cancelOrderByMasterSn(orderSn, orderStatus);
                        continue;
                    }
                    OrderManagementRequest request = new OrderManagementRequest();
                    request.setMasterOrderSn(orderSn);
                    request.setDiscount(minDiscount);
                    orderManagementService.groupBuySuccess(request);
                }
        } else {
            log.error("团购状态错误:{}", JSON.toJSONString(groupBuyBean));
        }
    }
}
