package com.work.shop.oms.dao;

import com.work.shop.oms.bean.OrderRewardPointGoodsMasterBean;
import com.work.shop.oms.param.bean.ParamOrderRewardPointGoods;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("orderRewardPointGoodsMasterMapper")
public interface OrderRewardPointGoodsMasterMapper {

    int getCountByParam(ParamOrderRewardPointGoods param);

    List<OrderRewardPointGoodsMasterBean> getOrderRewardPointGoods(ParamOrderRewardPointGoods param);

    //创建订单主表
    void createOrderMaster(OrderRewardPointGoodsMasterBean masterBean);

    //订单取消
    void cancelOrder(ParamOrderRewardPointGoods param);
}