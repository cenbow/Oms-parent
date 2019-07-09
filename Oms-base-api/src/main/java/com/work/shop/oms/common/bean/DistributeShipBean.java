package com.work.shop.oms.common.bean;

import java.io.Serializable;



public class DistributeShipBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 576965025144712546L;
	
	private String orderSn;									// 订单号

    private String code;									// 出库单编号

    private Integer bfOrgWarehId;							// 仓库id

    private String distWarehCode;							// 虚拟仓库code

    private Short regionId;									// 地区id

    private Integer ttlQty;									// 总数量

    private Integer bfOrgTspComId;							// 承运商id

    private String shipCode;								// 承运商

    private String csbNum;									// 托运单号

    private String shipDate;								// 出库时间

    private String createDate;								// 创建时间

    private String lastModifiedDate;						// 最后更新时间

    private String majorSendWarehCode;						// 实际出货仓

    private Integer shipType;								// 配送类型 1:门店发货单;2:工厂发货单;3:第三方仓库发货单

    private Integer synFlag;								// 同步标志:0自动,1:手工
    
    private String customCode;								// 商品编码
    
    private String invoiceNo;								// 快递单号
    
    private String shipSn;									// 交货单号
    
    private String shippingCode;
    
    private String shippingName;
    
    private String depotCode;
    
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public Integer getBfOrgWarehId() {
		return bfOrgWarehId == null ? 0 : bfOrgWarehId;
	}
	public void setBfOrgWarehId(Integer bfOrgWarehId) {
		this.bfOrgWarehId = bfOrgWarehId;
	}
	public String getDistWarehCode() {
		return distWarehCode;
	}
	public void setDistWarehCode(String distWarehCode) {
		this.distWarehCode = distWarehCode;
	}
	public Short getRegionId() {
		return regionId == null ? (short)0 : regionId;
	}
	public void setRegionId(Short regionId) {
		this.regionId = regionId;
	}
	public Integer getTtlQty() {
		return ttlQty == null ? 0 : ttlQty;
	}
	public void setTtlQty(Integer ttlQty) {
		this.ttlQty = ttlQty;
	}
	public Integer getBfOrgTspComId() {
		return bfOrgTspComId == null ? 0 : bfOrgTspComId;
	}
	public void setBfOrgTspComId(Integer bfOrgTspComId) {
		this.bfOrgTspComId = bfOrgTspComId;
	}
	public String getShipCode() {
		return shipCode;
	}
	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}
	public String getCsbNum() {
		return csbNum;
	}
	public void setCsbNum(String csbNum) {
		this.csbNum = csbNum;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getMajorSendWarehCode() {
		return majorSendWarehCode;
	}
	public void setMajorSendWarehCode(String majorSendWarehCode) {
		this.majorSendWarehCode = majorSendWarehCode;
	}
	public Integer getShipType() {
		return shipType;
	}
	public void setShipType(Integer shipType) {
		this.shipType = shipType;
	}
	public Integer getSynFlag() {
		return synFlag;
	}
	public void setSynFlag(Integer synFlag) {
		this.synFlag = synFlag;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCustomCode() {
		return customCode;
	}
	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getShipSn() {
		return shipSn;
	}
	public void setShipSn(String shipSn) {
		this.shipSn = shipSn;
	}
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((majorSendWarehCode == null) ? 0 : majorSendWarehCode.hashCode());
		result = prime * result + ((orderSn == null) ? 0 : orderSn.hashCode());
		result = prime * result + ((shipCode == null) ? 0 : shipCode.hashCode());
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
		DistributeShipBean other = (DistributeShipBean) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (majorSendWarehCode == null) {
			if (other.majorSendWarehCode != null)
				return false;
		} else if (!majorSendWarehCode.equals(other.majorSendWarehCode))
			return false;
		if (orderSn == null) {
			if (other.orderSn != null)
				return false;
		} else if (!orderSn.equals(other.orderSn))
			return false;
		if (shipCode == null) {
			if (other.shipCode != null)
				return false;
		} else if (!shipCode.equals(other.shipCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DistributeShipBean [orderSn=" + orderSn + ", code=" + code
				+ ", bfOrgWarehId=" + bfOrgWarehId + ", distWarehCode="
				+ distWarehCode + ", regionId=" + regionId + ", ttlQty="
				+ ttlQty + ", bfOrgTspComId=" + bfOrgTspComId + ", shipCode="
				+ shipCode + ", csbNum=" + csbNum + ", shipDate=" + shipDate
				+ ", createDate=" + createDate + ", lastModifiedDate="
				+ lastModifiedDate + ", majorSendWarehCode="
				+ majorSendWarehCode + ", shipType=" + shipType + ", synFlag="
				+ synFlag + ", customCode=" + customCode + "]";
	}

}