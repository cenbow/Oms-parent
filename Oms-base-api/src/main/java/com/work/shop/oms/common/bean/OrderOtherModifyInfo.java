package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class OrderOtherModifyInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String invType;												//发票类型
	private String invPayee;											//发票抬头 
	private String invContent;											//发票内容
	private String postscript;											//客户给商家留言
	private String toBuyer;												//商家给客户的留言
	private String howOos;												//缺货方式

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public String getInvPayee() {
		return invPayee;
	}

	public void setInvPayee(String invPayee) {
		this.invPayee = invPayee;
	}

	public String getInvContent() {
		return invContent;
	}

	public void setInvContent(String invContent) {
		this.invContent = invContent;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public String getToBuyer() {
		return toBuyer;
	}

	public void setToBuyer(String toBuyer) {
		this.toBuyer = toBuyer;
	}

	public String getHowOos() {
		return howOos;
	}

	public void setHowOos(String howOos) {
		this.howOos = howOos;
	}
	
	
	

}
