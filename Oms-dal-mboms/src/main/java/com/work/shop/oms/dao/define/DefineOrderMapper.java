package com.work.shop.oms.dao.define;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.api.bean.GoodsReturnChangeMobile;
import com.work.shop.oms.api.bean.OrderDetailInfo;
import com.work.shop.oms.api.bean.OrderExchangesDetailInfo;
import com.work.shop.oms.api.bean.OrderGoodsInfo;
import com.work.shop.oms.api.bean.OrderMobile;
import com.work.shop.oms.api.bean.OrderPageInfo;
import com.work.shop.oms.api.bean.OrderPayInfo;
import com.work.shop.oms.api.bean.OrderReturnDetailInfo;
import com.work.shop.oms.api.bean.OrderReturnGoodsInfo;
import com.work.shop.oms.api.bean.OrderReturnPageInfo;
import com.work.shop.oms.api.bean.OrderShipInfo;
import com.work.shop.oms.api.bean.ReturnGoodsStatus;
import com.work.shop.oms.api.bean.UserOrderInfo;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderAddressInfo;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDistribute;

public interface DefineOrderMapper {

    //支付前订单消息查询
    @Writer
    OrderPayInfo selectWaitPayInfo(Map<String, Object> params);

    @ReadOnly
    List<OrderPageInfo> selectUserOrderInfo(Map<String, Object> params);

    @ReadOnly
    OrderDetailInfo selectOrderDetailInfo(Map<String, Object> params);

    @ReadOnly
    OrderReturnDetailInfo selectOrderReturnDetailInfo(Map<String, Object> params);

    @ReadOnly
    OrderExchangesDetailInfo selectOrderExchangesDetailInfo(Map<String, Object> params);

    @Writer
    void updateOrderUser(Map<String, Object> params);

    @Writer
    void updateHistoryOrderUser(Map<String, Object> params);

    @ReadOnly
    List<OrderReturnPageInfo> selectUserOrderReturnInfo(Map<String, Object> params);


    @ReadOnly
    List<OrderShipInfo> selectOrderShipInfo(Map<String, Object> params);

    @ReadOnly
    List<OrderGoodsInfo> selectOrderGoodsInfo(Map<String, Object> params);

    @ReadOnly
    List<OrderReturnGoodsInfo> selectOrderReturnGoodsInfo(Map<String, Object> params);

    @ReadOnly
    List<String> selectUserId(Map<String, Object> params);

    @ReadOnly
    List<String> selectOrderSnByUser(Map<String, Object> params);

    @ReadOnly
    Integer selectUserOrderInfoCount(Map<String, Object> params);

    @ReadOnly
    Integer selectReturnGoodsType(Map<String, Object> params);

    @ReadOnly
    Integer selectGoodsType(Map<String, Object> params);

    @ReadOnly
    Integer selectUserIdCount(Map<String, Object> params);

    @ReadOnly
    Integer selectUserOrderReturnInfoCount(Map<String, Object> params);


    @ReadOnly
    Integer selectUserOrderCountByType(Map<String, Object> params);

    @ReadOnly
    OrderAddressInfo selectOrderAddressInfo(Map<String, Object> params);

    @Writer
    void updateOrderReturn(Map<String, Object> params);

    @Writer
    void updateOrderReturnChange(Map<String, Object> params);

    @Writer
    void updateOrderReturnChangeAction(Map<String, Object> params);


    /**
     * 平台手机端查询换单列表
     *
     * @param map
     * @return
     */
    @ReadOnly
    List<OrderMobile> getExchangeOrderList(Map<String, Object> map);


    @ReadOnly
    List<OrderGoodsInfo> selectByWhere(Map<String, Object> params);

    @ReadOnly
    List<ReturnGoodsStatus> selectReturnGoodsStatus(Map<String, Object> params);

    @ReadOnly
    List<OrderGoodsInfo> selectRestrictionGoodsByWhere(Map<String, Object> params);

    /**
     * 获取订单配送出库商品信息
     *
     * @param params
     * @return
     */
    @ReadOnly
    public List<MasterOrderGoods> getOrderDistributeGoods(Map<String, Object> params);

    //查询订单来源
    HashMap<String, Object> getOrderFromByOrderSN(String orderSN);
}