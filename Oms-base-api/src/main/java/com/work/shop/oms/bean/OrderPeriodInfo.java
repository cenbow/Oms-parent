package com.work.shop.oms.bean;

import java.io.Serializable;

/**
 * 订单周期明细
 * @author QuYachu
 *
 */
public class OrderPeriodInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * key
	 */
	private String periodId;
	
	/**
	 * id
	 */
	private Integer periodSeries;
	
	/**
	 * 周期时间秒
	 */
	private Integer periodValue;
	
	/**
	 * 周期时间
	 */
	private String periodValueName;
	
	private String desc;
    
	/**
	 * 名称
	 */
    private String periodName;

	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}

	public Integer getPeriodSeries() {
		return periodSeries;
	}

	public void setPeriodSeries(Integer periodSeries) {
		this.periodSeries = periodSeries;
	}

	public Integer getPeriodValue() {
		return periodValue;
	}

	public void setPeriodValue(Integer periodValue) {
		this.periodValue = periodValue;
	}

	public String getPeriodValueName() {
		return periodValueName;
	}

	public void setPeriodValueName(String periodValueName) {
		/*String name = "";
		if (periodValue != null) {
			if (periodValue < 60) {
				name = periodValue + "秒";
			} else {
				int secrem = periodValue % 60;
				int min = periodValue / 60;
			}
		}*/
		this.periodValueName = periodValueName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
    
}
