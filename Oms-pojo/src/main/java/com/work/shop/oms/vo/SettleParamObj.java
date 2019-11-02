package com.work.shop.oms.vo;

import java.io.Serializable;
import java.util.List;


/**
 * 订单结算bean
 * @author cage
 *
 */
public class SettleParamObj implements Serializable {	
	
	private static final long serialVersionUID = 1L;

	/**
	 * 单据号
	 */
	private String dealCode;

	/**
	 * 业务类型:0入库  1结算 9入库撤销
	 */
	private Integer bussType;

	/**
	 * 工具推送true
	 */
	private boolean tools;

	/**
	 * 操作人
	 */
	private String userId;

	/**
	 * 推送erp商品
	 */
	private List<StorageGoods> storageGoods;
	
	public List<StorageGoods> getStorageGoods() {
		return storageGoods;
	}

	public void setStorageGoods(List<StorageGoods> storageGoods) {
		this.storageGoods = storageGoods;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDealCode() {
		return dealCode;
	}

	public void setDealCode(String dealCode) {
		this.dealCode = dealCode;
	}

	public Integer getBussType() {
		return bussType;
	}

	public void setBussType(Integer bussType) {
		this.bussType = bussType;
	}

	public boolean isTools() {
		return tools;
	}

	public void setTools(boolean tools) {
		this.tools = tools;
	}

	
}
