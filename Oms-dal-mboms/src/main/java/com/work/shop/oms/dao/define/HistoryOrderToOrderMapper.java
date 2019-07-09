package com.work.shop.oms.dao.define;

import java.util.List;

import com.work.shop.invocation.Writer;

public interface HistoryOrderToOrderMapper {
	
	@Writer
	public void historyMasOrdInfoToMasOrdInfo(String historyMasterOrderSn);//主单表
	@Writer
	public void historyMasOrdAddInfoToMasOrdAddInfo(String historyMasterOrderSn);//主单地址信息表
	@Writer
	public void historyMasOrdPayToMasOrdPay(String historyMasterOrderSn);//主单支付表
	@Writer
	public void historyMasOrdGoodsToMasOrdGoods(String historyMasterOrderSn);//主单商品表
	@Writer
	public void historyMasOrdActionToMasOrdAction(String historyMasterOrderSn);//主单日志表
	@Writer
	public void historyOrderDistributeToOrderDistribute(String historyMasterOrderSn);//交货单表
	@Writer
	public void historyDistributeActionToDistributeAction(String orderSn);//交货单日志表
	@Writer
	public void historyOrderDepotShipToOrderDepotShip(String orderSn);//配送表
	
	@Writer
	public void deleteHistoryMasOrdInfo(String historyMasterOrderSn);
	@Writer
	public void deleteHistoryMasOrdAddInfo(String historyMasterOrderSn);
	@Writer
	public void deleteHistoryMasOrdPay(String historyMasterOrderSn);
	@Writer
	public void deleteHistoryMasOrdGoods(String historyMasterOrderSn);
	@Writer
	public void deleteHistoryMasOrdAction(String historyMasterOrderSn);
	@Writer
	public void deleteHistoryOrderDistribute(String historyMasterOrderSn);
	@Writer
	public void deleteHistoryDistributeAction(String orderSn);
	@Writer
	public void deleteHistoryOrderDepotShip(String orderSn);

}
