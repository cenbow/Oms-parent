package com.work.shop.oms.vo;

import java.io.Serializable;


public class ExpressInfo implements Serializable, Comparable<ExpressInfo> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4846081708447528851L;

	private String time;
	
	private String context;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	public int compareTo(ExpressInfo o) {
		return this.time.compareTo(o.getTime());
	}
}
