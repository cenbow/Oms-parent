package com.work.shop.oms.erp.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.UpdateOrderInfo;
import com.work.shop.oms.common.bean.OrderCancelInfo;
import com.work.shop.oms.erp.service.ErpInterfaceProxy;
import com.work.shop.oms.webservice.ErpResultBean;
import com.work.shop.oms.webservice.ErpWebserviceResultBean;

@Service
public class ErpInterfaceProxyImpl implements ErpInterfaceProxy {

	@Override
	public ErpResultBean UpdateCancelIdt(OrderCancelInfo orderCancelInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpWebserviceResultBean UPdateReviceAddress(String ordersn,
			String pJsonData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpWebserviceResultBean UpdateShippingMethod(String ordersn,
			Map<String, Object> pJsonData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpWebserviceResultBean UpdateProductByIdtDtl(String ordersn,
			Map pJsonData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpResultBean UpdateProductByIdtDtl(UpdateOrderInfo updateOrderGoods) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpWebserviceResultBean UpdateIdtToQnOrToR(String ordersn,
			int optionType, Map<String, Object> pJsonData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpWebserviceResultBean UpdateIdtToHangUpOrToHangR(String ordersn,
			int optionType, Map<String, Object> pJsonData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpWebserviceResultBean UpdateOrderAmount(String ordersn,
			Map pJsonData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpWebserviceResultBean confirmOrder(String ordersn, int optionType,
			Map pJsonData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErpResultBean changeConsigneeInfo(String orderSn,
			ConsigneeModifyInfo consignInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
