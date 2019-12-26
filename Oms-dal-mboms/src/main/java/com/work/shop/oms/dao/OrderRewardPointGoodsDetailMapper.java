package com.work.shop.oms.dao;

import com.work.shop.oms.bean.OrderRewardPointGoodsDetailBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("orderRewardPointGoodsDetailMapper")
public interface OrderRewardPointGoodsDetailMapper {

    List<OrderRewardPointGoodsDetailBean> getOrderRewardPointGoodsDetail(List<String> orderSNLIst);

    void createOrderDetail(List<OrderRewardPointGoodsDetailBean> detailBeanList);
}