package com.work.shop.oms.dao.define;

import com.work.shop.invocation.Writer;

public interface OrderToHistoryMapper {
	
	@Writer
	public void masOrdInfoToHistory(String masterOrderSn);
	@Writer
	public void masOrdAddInfoToHistory(String masterOrderSn);
	@Writer
	public void masOrdPayToHistory(String masterOrderSn);
	@Writer
	public void masOrdGoodsToHistory(String masterOrderSn);
	@Writer
	public void masOrdActionToHistory(String masterOrderSn);
	@Writer
	public void orderDistributeToHistory(String masterOrderSn);
	@Writer
	public void distributeActionToHistory(String orderSn);
	@Writer
	public void orderDepotShipToHistory(String orderSn);
	
	@Writer
	public void deleteMasOrdInfo(String masterOrderSn);
	@Writer
	public void deleteMasOrdAddInfo(String masterOrderSn);
	@Writer
	public void deleteMasOrdPay(String masterOrderSn);
	@Writer
	public void deleteMasOrdGoods(String masterOrderSn);
	@Writer
	public void deleteMasOrdAction(String masterOrderSn);
	@Writer
	public void deleteOrderDistribute(String masterOrderSn);
	@Writer
	public void deleteDistributeAction(String orderSn);
	@Writer
	public void deleteOrderDepotShip(String orderSn);

}
