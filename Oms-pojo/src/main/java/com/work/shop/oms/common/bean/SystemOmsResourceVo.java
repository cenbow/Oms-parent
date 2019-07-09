package com.work.shop.oms.common.bean;

import java.util.List;

import com.work.shop.oms.bean.SystemOmsResource;

public class SystemOmsResourceVo extends SystemOmsResource {
	
	/***叶节点****/
	private boolean leaf;
	
	/***选中****/
	private boolean selected;
	
    /*****/
	private List<SystemOmsResourceVo> list;
	
	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public List<SystemOmsResourceVo> getList() {
		return list;
	}

	public void setList(List<SystemOmsResourceVo> list) {
		this.list = list;
	}

}
