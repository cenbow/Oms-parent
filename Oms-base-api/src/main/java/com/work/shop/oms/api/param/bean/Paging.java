package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.List;

/**
 * extjs Json格式的分页用数据
 * @author lhj
 *
 */
public class Paging<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4482135784490070177L;

	/**总记录数*/
	private int totalProperty;
	
	/** 当前页数*/
	private List<T> root;
	
	/** 提示信息*/
	private String message;
	
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Paging() {
	}

	public Paging(int totalProperty, List<T> root) {
		super();
		this.totalProperty = totalProperty;
		this.root = root;
	}

	/**
	 * @return the totalProperty
	 */
	public int getTotalProperty() {
		return totalProperty;
	}

	/**
	 * @param totalProperty the totalProperty to set
	 */
	public void setTotalProperty(int totalProperty) {
		this.totalProperty = totalProperty;
	}

	/**
	 * @return the root
	 */
	public List<T> getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(List<T> root) {
		this.root = root;
	}
	
	
}
