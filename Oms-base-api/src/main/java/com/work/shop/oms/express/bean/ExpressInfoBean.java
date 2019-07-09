package com.work.shop.oms.express.bean;

import java.util.ArrayList;
import java.util.List;

public class ExpressInfoBean {
	
	private ErpStatusInfoRO erp;
	
	private List<ExpressInfo> express = new ArrayList<ExpressInfo>();
	
	private String from;

	public ErpStatusInfoRO getErp() {
		return erp;
	}

	public void setErp(ErpStatusInfoRO erp) {
		this.erp = erp;
	}

	public List<ExpressInfo> getExpress() {
		return express;
	}

	public void setExpress(List<ExpressInfo> express) {
		this.express = express;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
}
