package com.work.shop.oms.controller.service;

import com.work.shop.oms.bean.ResultRewardPointGoodsBean;
import com.work.shop.oms.param.bean.ParamOrderRewardPointGoods;

import java.util.List;

public interface OrderRewardPointGoodsService {

    //创建积分订单
    void createOrderRewardPoint(ParamOrderRewardPointGoods param);

    //取消积分订单
    void cancelOrderRewardPoint(ParamOrderRewardPointGoods param);

    //根据订单号查询订单
    List<ResultRewardPointGoodsBean> getOrderRewardPointGoodsByOrderSN(String orderSN);

    int getCountOfOrderRewardPointGoods(ParamOrderRewardPointGoods param);

    List<ResultRewardPointGoodsBean> getOrderRewardPointGoods(ParamOrderRewardPointGoods param);
}
