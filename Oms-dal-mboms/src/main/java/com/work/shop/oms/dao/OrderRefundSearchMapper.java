package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.oms.vo.OrderRefundListVO;

public interface OrderRefundSearchMapper {
    @ReadOnly
    List<OrderRefundListVO> selectOrderRefundListByExample(Map<String,Object>map);
    @ReadOnly
    int countOrderRefundListByExample(Map<String,Object>map);
}