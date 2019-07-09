package com.work.shop.oms.channel.bean;

import java.io.Serializable;

public class SyncStockParam implements Serializable {

	private static final long serialVersionUID = 5343605423505252610L;

	private String shopCode;
	private String sku;
	private Integer stockCount;
	private Integer priority; // MQ优先级 为空 默认为 4.  0  是最低的优先级， 9  是最高的优先级。另外，客户端应当将0‐4  看作普通优先级，5‐9  看作加急优先级.
	/**
	 * 标识：全量更新、增量更新
	 * 一号店：1：全量更新，2：增量更新
	 * 淘宝：1为全量更新，2为增量更新
	 * 分销：1为全量更新，2为增量更新
	 */
	private String type;
	
	private boolean isUrgent = false;
	
	private String lastModifyDate;
	
	private Long reletionId;

	public SyncStockParam() {
		super();
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public boolean isUrgent() {
		return isUrgent;
	}

	public void setUrgent(boolean isUrgent) {
		this.isUrgent = isUrgent;
	}

	public String getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	

	public Long getReletionId() {
		return reletionId;
	}

	public void setReletionId(Long reletionId) {
		this.reletionId = reletionId;
	}

	@Override
	public String toString() {
		return "SyncStockParam [shopCode=" + shopCode + ", sku=" + sku
				+ ", stockCount=" + stockCount + ", priority=" + priority
				+ ", type=" + type + ", isUrgent=" + isUrgent
				+ ", lastModifyDate=" + lastModifyDate + ", reletionId="
				+ reletionId + "]";
	}

	
}