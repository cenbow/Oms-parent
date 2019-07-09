package com.work.shop.oms.express.bean;
/**
 * 韵达物流不同节点结果集
 * @author huangl
 *
 */
public class YDExpressStep {

	private String time;
	private String address;
	private String remark;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public YDExpressStep() {}
	
	public YDExpressStep(String time,String address) {
		this.time = time;
		this.address = address;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
