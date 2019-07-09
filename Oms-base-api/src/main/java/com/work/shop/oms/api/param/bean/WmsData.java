package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.List;

public class WmsData implements Serializable {

	private static final long serialVersionUID = 5098794521452717163L;
	
	private String returnInvoiceNo; //快递单号
	
	private String orderSn;//OS订单号
	
	private String userName;//操作人
	
	private String depotCode;//仓库
	
	private boolean partStorage;//是否支持部分入库
	
	private List<WmsReturnData> wmsReturnData;//退单信息

	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}

	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}

	public boolean isPartStorage() {
		return partStorage;
	}

	public void setPartStorage(boolean partStorage) {
		this.partStorage = partStorage;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public List<WmsReturnData> getWmsReturnData() {
		return wmsReturnData;
	}

	public void setWmsReturnData(List<WmsReturnData> wmsReturnData) {
		this.wmsReturnData = wmsReturnData;
	}

	
}
