package com.work.shop.oms.vo;

public class ErpStatus {
	
	private String docType;
	
	private int index;
	
	private String shipSn;
	
	private String depotCode;
	
	private String statusInfo;
	
	private String time;
	
	private String warehouseCode;

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getShipSn() {
		return shipSn;
	}

	public void setShipSn(String shipSn) {
		this.shipSn = shipSn;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
}
