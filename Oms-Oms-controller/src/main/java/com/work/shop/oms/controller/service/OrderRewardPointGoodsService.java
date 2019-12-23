package com.work.shop.oms.controller.service;

import com.work.shop.oms.bean.OrderRewardPointGoodsDetailBean;
import com.work.shop.oms.bean.OrderRewardPointGoodsMasterBean;
import com.work.shop.oms.param.bean.ParamOrderRewardPointGoods;

import java.util.List;

public interface OrderRewardPointGoodsService {

    //创建积分订单
    void createOrderRewardPoint(ParamOrderRewardPointGoods param);

    //取消积分订单
    void cancelOrderRewardPoint(ParamOrderRewardPointGoods param);

    int getCountOfOrderRewardPointGoodsMaster(ParamOrderRewardPointGoods param);

    List<OrderRewardPointGoodsMasterBean> getOrderRewardPointGoodsMaster(ParamOrderRewardPointGoods param);

    List<OrderRewardPointGoodsDetailBean> getOrderRewardPointGoodsDetail(String orderSN);

}
