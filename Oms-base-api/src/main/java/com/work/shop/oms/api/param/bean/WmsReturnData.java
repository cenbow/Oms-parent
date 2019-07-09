package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.List;

public class WmsReturnData implements Serializable {

	private static final long serialVersionUID = 4845316220900257256L;
	
	private String returnSn;//退单号
	
	private List<WmsReturnGoods> wmsReturnGoods;//商品信息

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public List<WmsReturnGoods> getWmsReturnGoods() {
		return wmsReturnGoods;
	}

	public void setWmsReturnGoods(List<WmsReturnGoods> wmsReturnGoods) {
		this.wmsReturnGoods = wmsReturnGoods;
	}

	
}
