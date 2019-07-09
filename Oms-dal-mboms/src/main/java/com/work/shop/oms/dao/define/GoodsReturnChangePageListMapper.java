package com.work.shop.oms.dao.define;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.oms.api.bean.GoodsReturnPageInfo;
import com.work.shop.oms.api.bean.OrderGoodsInfo;
import com.work.shop.oms.api.bean.OrderStatusTypeBean;
import com.work.shop.oms.bean.GoodsReturnChangePageListExample;
import com.work.shop.oms.common.bean.GoodsReturnChangeBean;

import java.util.List;
import java.util.Map;

public interface GoodsReturnChangePageListMapper {
    @ReadOnly
    int countByExample(GoodsReturnChangePageListExample example);
    @ReadOnly
    List<GoodsReturnChangeBean> selectByExample(GoodsReturnChangePageListExample example);
    
    
    @ReadOnly
    int countByExampleBG(Map<String, Object> params);
    
    @ReadOnly
    List<GoodsReturnPageInfo> selectByExampleBG(Map<String, Object> params);

    List<OrderGoodsInfo> selectOrderGoods(Map<String, Object> params);
    
    @ReadOnly
    List<GoodsReturnPageInfo> selectByExampleMall(Map<String, Object> params);
    
    @ReadOnly
    int countByExampleMall(Map<String, Object> params);

    @ReadOnly
    List<OrderStatusTypeBean> getChangeOrderTypeNum(Map<String, Object> paramMap);
}
