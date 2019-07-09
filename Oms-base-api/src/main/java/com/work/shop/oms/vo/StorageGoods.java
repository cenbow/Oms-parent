package com.work.shop.oms.vo;

import java.io.Serializable;

public class StorageGoods implements Serializable {

	private static final long serialVersionUID = 7526748509572665344L;
	
	private Long id;//商品id
	
	private int prodScanNum;//实际扫描数量

	private String remark;//商品备注
	
	private String customCode;//商品11位码
	
	private Double settlementPrice;//财务价格
	
	
	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public Double getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(Double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getProdScanNum() {
		return prodScanNum;
	}

	public void setProdScanNum(int prodScanNum) {
		this.prodScanNum = prodScanNum;
	}
}
