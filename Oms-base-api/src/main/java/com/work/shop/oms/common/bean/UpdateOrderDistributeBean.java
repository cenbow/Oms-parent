package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.OrderDistributeBean;

public class UpdateOrderDistributeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8006818838327723263L;

	private OrderDistributeBean distribute;
	
	private List<MasterOrderGoods> addList = new ArrayList<MasterOrderGoods>();
	
	private List<MasterOrderGoods> deleteList = new ArrayList<MasterOrderGoods>();
	
	private List<MasterOrderGoods> updateList = new ArrayList<MasterOrderGoods>();

	private List<MasterOrderGoods> notUpdateList = new ArrayList<MasterOrderGoods>();
	
	private Set<String> deleteDepots = new HashSet<String>();
	
	private Set<String> notUpdateDepots = new HashSet<String>();
	
	private String supplierCode;

	public OrderDistributeBean getDistribute() {
		return distribute;
	}

	public void setDistribute(OrderDistributeBean distribute) {
		this.distribute = distribute;
	}

	public List<MasterOrderGoods> getAddList() {
		return addList;
	}

	public void setAddList(List<MasterOrderGoods> addList) {
		this.addList = addList;
	}

	public List<MasterOrderGoods> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<MasterOrderGoods> deleteList) {
		this.deleteList = deleteList;
	}

	public List<MasterOrderGoods> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<MasterOrderGoods> updateList) {
		this.updateList = updateList;
	}

	public List<MasterOrderGoods> getNotUpdateList() {
		return notUpdateList;
	}

	public void setNotUpdateList(List<MasterOrderGoods> notUpdateList) {
		this.notUpdateList = notUpdateList;
	}

	public Set<String> getDeleteDepots() {
		return deleteDepots;
	}

	public void setDeleteDepots(Set<String> deleteDepots) {
		this.deleteDepots = deleteDepots;
	}

	public Set<String> getNotUpdateDepots() {
		return notUpdateDepots;
	}

	public void setNotUpdateDepots(Set<String> notUpdateDepots) {
		this.notUpdateDepots = notUpdateDepots;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
}
