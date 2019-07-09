package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

public class CreateOrderRefund implements Serializable{

	private static final long serialVersionUID = 1L;
	
	 private String returnPaySn;

	 private String relatingReturnSn;
	 
	 private Short returnPay;
	 
	 private Double returnFee = 0D;

	public String getReturnPaySn() {
		return returnPaySn;
	}

	public void setReturnPaySn(String returnPaySn) {
		this.returnPaySn = returnPaySn;
	}

	public String getRelatingReturnSn() {
		return relatingReturnSn;
	}

	public void setRelatingReturnSn(String relatingReturnSn) {
		this.relatingReturnSn = relatingReturnSn;
	}

	public Short getReturnPay() {
		return returnPay;
	}

	public void setReturnPay(Short returnPay) {
		this.returnPay = returnPay;
	}

	public Double getReturnFee() {
		return returnFee;
	}

	public void setReturnFee(Double returnFee) {
		this.returnFee = returnFee;
	}
	
	public static String copyObj(Object srcObj,Object distObj){
		Field[] fields = srcObj.getClass().getDeclaredFields();
		for (Field field : fields) {
			System.out.println(field.getType());
		}
		return "";
	}
}
