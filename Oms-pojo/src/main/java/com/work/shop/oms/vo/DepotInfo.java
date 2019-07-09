package com.work.shop.oms.vo;

import java.io.Serializable;


/**
 * 立即分仓信息 OS创建订单接口返回
 * 
 * @author tony
 * 
 */
public class DepotInfo implements Serializable {
	private static final long serialVersionUID = 4757419756004124686L;
	// 实际出货仓 （2014-12版本新增）
	private String rcv_warehcode;
	// 11位码（2014-12版本新增）
	private String customCode;
	// 商品数量（2014-12版本新增）
	private String goodsCount;
	// 配发时间yyyy-MM-dd HH:mm:ss（2014-12版本新增）
	private String allocDate;
	// 虚拟发货仓（2014-12版本新增）
	private String dist_wareh_code;
	// 实际出货仓名称（2014-12版本新增）
	private String rcv_warehcode_name;

	public String getRcv_warehcode_name() {
		return rcv_warehcode_name;
	}

	public void setRcv_warehcode_name(String rcv_warehcode_name) {
		this.rcv_warehcode_name = rcv_warehcode_name;
	}

	public String getRcv_warehcode() {
		return rcv_warehcode;
	}

	public void setRcv_warehcode(String rcv_warehcode) {
		this.rcv_warehcode = rcv_warehcode;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getAllocDate() {
		return allocDate;
	}

	public void setAllocDate(String allocDate) {
		this.allocDate = allocDate;
	}

	public String getDist_wareh_code() {
		return dist_wareh_code;
	}

	public void setDist_wareh_code(String dist_wareh_code) {
		this.dist_wareh_code = dist_wareh_code;
	}

	@Override
	public String toString() {
		return "DepotInfo [rcv_warehcode=" + rcv_warehcode + ", customCode=" + customCode + ", goodsCount=" + goodsCount + ", allocDate=" + allocDate + ", dist_wareh_code=" + dist_wareh_code + "]";
	}
}