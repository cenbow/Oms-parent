package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsDetail;
import com.work.shop.oms.common.bean.OrderItemGoodsDetail;
import com.work.shop.oms.common.bean.ReturnOrderGoods;

public interface MasterOrderGoodsDetailMapper {
	
	public List<MasterOrderGoodsDetail> getMasterOrderGoodsDetail(Map paramMap);
	
	public List<MasterOrderGoodsDetail> getMergedMasOrdGoodsDetail(Map paramMap);

	public List<OrderItemGoodsDetail> getOrderGoodsDetail(String masterOrderSn);
	
	/**
	 * 获取订单商品数据（相同价格合并）
	 * @param masterOrderSn
	 * @return
	 */
	public List<MasterOrderGoods> getXOMSMasterOrderGoods(String masterOrderSn);

	/**
	 * 获取退单单商品数据（相同价格合并）
	 * @param returnSn
	 * @return
	 */
	public List<ReturnOrderGoods> getXOMSOrderReturnGoods(String returnSn);
	
	@ReadOnly
	public List<MasterOrderGoods> getMasterOrderGoodsByDepot(MasterOrderGoods record);

    /**
     * 根据交货单号
     * @param orderSns
     * @return
     */
    public List<OrderItemGoodsDetail> getOrderGoodsDetailByOrder(List<String> list);
}
