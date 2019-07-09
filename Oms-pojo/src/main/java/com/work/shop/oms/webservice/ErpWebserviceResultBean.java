package com.work.shop.oms.webservice;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author lemon
 *
 */
public class ErpWebserviceResultBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2034816661612033538L;
	private int code;
	private String message;
	private static HashMap<Integer,String> msgMap=new HashMap<Integer,String>();
	static{
		msgMap.put(2, "ERP商品最新状态和OS可能未同步,请稍后再试");
	}
	public ErpWebserviceResultBean() {
		code=-1;
		message="初始化";
	}
	public ErpWebserviceResultBean(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		String msg="";
		if(code>1){
			msg= msgMap.get(code)==null?message:msgMap.get(code);
		}else{
			msg=message;
		}
		return "ERP返回信息:"+msg;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setCode_Message(int code, String message){
		this.code = code;
		this.message = message;
	}
	@Override
	public String toString() {
		String erpMsg=new String("");
		if (code>1){
			erpMsg="ERP Message:"+message;
		}
		return "ErpWebserviceResultBean [code=" + code + ", OS message=" + getMessage()
				+ "]"+erpMsg;
	}
}
