package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class StockGoods implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 734906930391211627L;

	private String orderFrom;
	
	private String referer;
	
	private String depotCode;
	
	private String customCode;
	
	private String extensionCode;
	
	private String extensionId;
	
	private Integer goodsNumber;
	
	private Integer sendNumber;

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getExtensionCode() {
		return extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

	public String getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Integer getSendNumber() {
		return sendNumber;
	}

	public void setSendNumber(Integer sendNumber) {
		this.sendNumber = sendNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customCode == null) ? 0 : customCode.hashCode());
		result = prime * result
				+ ((depotCode == null) ? 0 : depotCode.hashCode());
		result = prime * result
				+ ((extensionCode == null) ? 0 : extensionCode.hashCode());
		result = prime * result
				+ ((extensionId == null) ? 0 : extensionId.hashCode());
		result = prime * result
				+ ((goodsNumber == null) ? 0 : goodsNumber.hashCode());
		result = prime * result
				+ ((orderFrom == null) ? 0 : orderFrom.hashCode());
		result = prime * result + ((referer == null) ? 0 : referer.hashCode());
		result = prime * result
				+ ((sendNumber == null) ? 0 : sendNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockGoods other = (StockGoods) obj;
		if (customCode == null) {
			if (other.customCode != null)
				return false;
		} else if (!customCode.equals(other.customCode))
			return false;
		if (depotCode == null) {
			if (other.depotCode != null)
				return false;
		} else if (!depotCode.equals(other.depotCode))
			return false;
		if (extensionCode == null) {
			if (other.extensionCode != null)
				return false;
		} else if (!extensionCode.equals(other.extensionCode))
			return false;
		if (extensionId == null) {
			if (other.extensionId != null)
				return false;
		} else if (!extensionId.equals(other.extensionId))
			return false;
		if (goodsNumber == null) {
			if (other.goodsNumber != null)
				return false;
		} else if (!goodsNumber.equals(other.goodsNumber))
			return false;
		if (orderFrom == null) {
			if (other.orderFrom != null)
				return false;
		} else if (!orderFrom.equals(other.orderFrom))
			return false;
		if (referer == null) {
			if (other.referer != null)
				return false;
		} else if (!referer.equals(other.referer))
			return false;
		if (sendNumber == null) {
			if (other.sendNumber != null)
				return false;
		} else if (!sendNumber.equals(other.sendNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StockGoods [orderFrom=" + orderFrom + ", referer=" + referer
				+ ", depotCode=" + depotCode + ", customCode=" + customCode
				+ ", extensionCode=" + extensionCode + ", extensionId="
				+ extensionId + ", goodsNumber=" + goodsNumber
				+ ", sendNumber=" + sendNumber + "]";
	}
}
