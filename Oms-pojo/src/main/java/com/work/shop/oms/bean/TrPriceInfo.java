package com.work.shop.oms.bean;

public class TrPriceInfo {

    private String entityState;
    private boolean selected;
    private int id;
    private int trTransLineId;
    private int orgTransId;
    private String orgTransCode;
    private String orgTransName;
    private double enterPrice;
    private int transCycle;
    private boolean isDefault;
	public String getEntityState() {
		return entityState;
	}
	public void setEntityState(String entityState) {
		this.entityState = entityState;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTrTransLineId() {
		return trTransLineId;
	}
	public void setTrTransLineId(int trTransLineId) {
		this.trTransLineId = trTransLineId;
	}
	public int getOrgTransId() {
		return orgTransId;
	}
	public void setOrgTransId(int orgTransId) {
		this.orgTransId = orgTransId;
	}
	public String getOrgTransCode() {
		return orgTransCode;
	}
	public void setOrgTransCode(String orgTransCode) {
		this.orgTransCode = orgTransCode;
	}
	public String getOrgTransName() {
		return orgTransName;
	}
	public void setOrgTransName(String orgTransName) {
		this.orgTransName = orgTransName;
	}
	public double getEnterPrice() {
		return enterPrice;
	}
	public void setEnterPrice(double enterPrice) {
		this.enterPrice = enterPrice;
	}
	public int getTransCycle() {
		return transCycle;
	}
	public void setTransCycle(int transCycle) {
		this.transCycle = transCycle;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
    public TrPriceInfo() {
	}
}
