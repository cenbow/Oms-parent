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

	private String dealCode; //单据号
	
	private Integer bussType;//业务类型:0入库  1结算 9入库撤销

	private boolean tools;//工具推送true
	
	private String userId;//操作人
	
	private List<StorageGoods> storageGoods;//推送erp商品
	
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
