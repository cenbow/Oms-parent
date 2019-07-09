package com.work.shop.oms.service;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.MasterOrderGoodsDetail;
import com.work.shop.oms.bean.ProductBarcodeList;
import com.work.shop.oms.common.bean.OrderInfoUpdateInfo;
import com.work.shop.oms.vo.AdminUser;

public interface OrderGoodsEditService {
	
	public List<Map<String,String>> getOrderSnCombStore(String masterOrderSn);
	
	public List<Map<String,String>> getSupplierCodeCombStore(String masterOrderSn);
	
	public List<ProductBarcodeList> getProductBarcodeListByGoodsSn(String goodsSn,String type);
	
	public Map getOrderGoodsEdit(String masterOrderSn,String orderSn);
	
	public Map doSaveOrderGoodsEdit(AdminUser adminUser,String masterOrderSn,String orderSn,OrderInfoUpdateInfo orderInfoUpdateInfo);
	
	public Map searchGoods(String goodsSn,String channelCode, String userId);
	
	public Map getSizeListByColorCode(String goodSn,String colorCode);
	
	public Map getColorListBySizeCode(String goodSn,String sizeCode);
	
	public Map getCustomStock(ProductBarcodeList barcodeList, String shopCode, int type);
	
	public Map addOrderGoods(String masterOrderSn,
			String channelCode,MasterOrderGoodsDetail masterOrderGoodsDetail);
	
	public Map recalculate(OrderInfoUpdateInfo infoUpdateInfo , String[] bonusIds);
	
	public Map getIsVerifyStock();
}
