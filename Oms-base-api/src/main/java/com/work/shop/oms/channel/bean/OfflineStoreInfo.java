package com.work.shop.oms.channel.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OfflineStoreInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7115261779726715323L;
	
	private String shopCode;
	
	private String storeCode;
	
	private List<String> shopCodeList;

	private List<String> storeCodeList;
	
	private String storeId;
	
	private String storeName;
	
	private int storeType;
	
	private String address;

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	
	public List<String> getShopCodeList() {
		return shopCodeList;
	}

	public void setShopCodeList(List<String> shopCodeList) {
		this.shopCodeList = shopCodeList;
	}

	public List<String> getStoreCodeList() {
		return storeCodeList;
	}

	public void setStoreCodeList(List<String> storeCodeList) {
		this.storeCodeList = storeCodeList;
	}

	public void setShopCodesByStr(String shopCodesStr) {
		if (shopCodesStr != null && !"".equals(shopCodesStr.trim())) {
			String arr[] = shopCodesStr.split(",");
			List<String> list = new ArrayList<String>();
			for (String str : arr) {
				list.add(str);
			}
			this.shopCodeList = list;
		} else {
			this.shopCodeList = null;
		}
	}
	
	public void setStoreCodesByStr(String storeCodesStr) {
		if (storeCodesStr != null && !"".equals(storeCodesStr.trim())) {
			String arr[] = storeCodesStr.split(",");
			List<String> list = new ArrayList<String>();
			for (String str : arr) {
				list.add(str);
			}
			this.storeCodeList = list;
		} else {
			this.storeCodeList = null;
		}
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getStoreType() {
		return storeType;
	}

	public void setStoreType(int storeType) {
		this.storeType = storeType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}