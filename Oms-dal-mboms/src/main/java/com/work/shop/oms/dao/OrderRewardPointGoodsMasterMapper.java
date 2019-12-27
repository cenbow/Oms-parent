package com.work.shop.oms.dao;

import com.work.shop.oms.bean.OrderRewardPointGoodsMasterBean;
import com.work.shop.oms.bean.ResultRewardPointGoodsBean;
import com.work.shop.oms.param.bean.ParamOrderRewardPointGoods;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("orderRewardPointGoodsMasterMapper")
public interface OrderRewardPointGoodsMasterMapper {

    //创建订单主表
    void createOrderMaster(OrderRewardPointGoodsMasterBean masterBean);

    //订单取消
    void cancelOrder(ParamOrderRewardPointGoods param);

    //根据订单号查询订单
    List<ResultRewardPointGoodsBean> getOrderRewardPointGoodsByOrderSN(String orderSN);

    int getCountOfOrderRewardPointGoods(ParamOrderRewardPointGoods param);

    List<ResultRewardPointGoodsBean> getOrderRewardPointGoods(ParamOrderRewardPointGoods param);
}