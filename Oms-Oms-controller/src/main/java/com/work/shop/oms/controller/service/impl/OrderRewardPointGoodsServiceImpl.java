package com.work.shop.oms.controller.service.impl;

import com.work.shop.oms.bean.OrderRewardPointGoodsMasterBean;
import com.work.shop.oms.bean.ResultRewardPointGoodsBean;
import com.work.shop.oms.controller.service.OrderRewardPointGoodsService;
import com.work.shop.oms.dao.OrderRewardPointGoodsDetailMapper;
import com.work.shop.oms.dao.OrderRewardPointGoodsMasterMapper;
import com.work.shop.oms.param.bean.ParamOrderRewardPointGoods;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/16 6:56 下午
 * @Version V1.0
 **/

@Service("orderRewardPointGoodsService")
public class OrderRewardPointGoodsServiceImpl implements OrderRewardPointGoodsService {

    @Resource
    private OrderRewardPointGoodsMasterMapper rewardPointGoodsMasterMapper;

    @Resource
    private OrderRewardPointGoodsDetailMapper rewardPointGoodsDetailMapper;

    @Override
    public void createOrderRewardPoint(ParamOrderRewardPointGoods param) {
        OrderRewardPointGoodsMasterBean masterBean = new OrderRewardPointGoodsMasterBean();
        masterBean.setOrderSN(param.getOrderSN());
        masterBean.setBuyerSN(param.getBuyerSN());
        masterBean.setTotalPoint(param.getTotalPoint());
        masterBean.setReceiverName(param.getReceiverName());
        masterBean.setReceiverTel(param.getReceiverTel());
        masterBean.setReceiverAddress(param.getReceiverAddress());
        rewardPointGoodsMasterMapper.createOrderMaster(masterBean);
        rewardPointGoodsDetailMapper.createOrderDetail(param.getDetailBeanList());
    }

    @Override
    public void cancelOrderRewardPoint(ParamOrderRewardPointGoods param) {
        rewardPointGoodsMasterMapper.cancelOrder(param);
    }

    @Override
    public List<ResultRewardPointGoodsBean> getOrderRewardPointGoodsByOrderSN(String orderSN) {
        return rewardPointGoodsMasterMapper.getOrderRewardPointGoodsByOrderSN(orderSN);
    }

    @Override
    public int getCountOfOrderRewardPointGoods(ParamOrderRewardPointGoods param) {
        return rewardPointGoodsMasterMapper.getCountOfOrderRewardPointGoods(param);
    }

    @Override
    public List<ResultRewardPointGoodsBean> getOrderRewardPointGoods(ParamOrderRewardPointGoods param) {
        return rewardPointGoodsMasterMapper.getOrderRewardPointGoods(param);
    }

}
