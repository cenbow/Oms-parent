package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class ReturnShipUpdateParam implements Serializable{

    
    /** @Fields serialVersionUID: */
      	
    private static final long serialVersionUID = -18747575731592224L;
    
    private String orderOutSn;
    
    private String returnSn;
    
	private String returnInvoiceNo;//快递单号
    
    private String expressCompany;
    
    private String customCode;

    public String getOrderOutSn() {
        return orderOutSn;
    }

    public void setOrderOutSn(String orderOutSn) {
        this.orderOutSn = orderOutSn;
    }

    public String getReturnInvoiceNo() {
        return returnInvoiceNo;
    }

    public void setReturnInvoiceNo(String returnInvoiceNo) {
        this.returnInvoiceNo = returnInvoiceNo;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getReturnSn() {
  		return returnSn;
  	}

  	public void setReturnSn(String returnSn) {
  		this.returnSn = returnSn;
  	}

    
    

}
