package com.work.shop.oms.common.bean;

import com.work.shop.oms.common.bean.C2bGood;

public class C2bGood {
	
	private Integer b2cCount;
	
	private String b2cGoodsId;
	
	private String b2cGoodsSku;

	public Integer getB2cCount() {
		return b2cCount;
	}

	public void setB2cCount(Integer b2cCount) {
		this.b2cCount = b2cCount;
	}

	public String getB2cGoodsId() {
		return b2cGoodsId;
	}

	public void setB2cGoodsId(String b2cGoodsId) {
		this.b2cGoodsId = b2cGoodsId;
	}

	public String getB2cGoodsSku() {
		return b2cGoodsSku;
	}

	public void setB2cGoodsSku(String b2cGoodsSku) {
		this.b2cGoodsSku = b2cGoodsSku;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((b2cGoodsId == null) ? 0 : b2cGoodsId.hashCode());
		result = prime * result
				+ ((b2cGoodsSku == null) ? 0 : b2cGoodsSku.hashCode());
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
		C2bGood other = (C2bGood) obj;
		if (b2cGoodsId == null) {
			if (other.b2cGoodsId != null)
				return false;
		} else if (!b2cGoodsId.equals(other.b2cGoodsId))
			return false;
		if (b2cGoodsSku == null) {
			if (other.b2cGoodsSku != null)
				return false;
		} else if (!b2cGoodsSku.equals(other.b2cGoodsSku))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "C2bGood [b2cCount=" + b2cCount + ", b2cGoodsId=" + b2cGoodsId
				+ ", b2cGoodsSku=" + b2cGoodsSku + "]";
	}
}
