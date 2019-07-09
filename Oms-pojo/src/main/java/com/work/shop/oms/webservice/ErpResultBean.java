package com.work.shop.oms.webservice;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author zhaiyongding@metersbonwe.com
 *
 * created at 2014-7-17
 */
public class ErpResultBean implements Serializable{
	public static final int SUCCESS_CODE=0;
	private int Code;
	private String Message;
	private static HashMap<Integer,String> msgMap=new HashMap<Integer,String>();
	static{
		msgMap.put(1, "调用ERP接口成功!");
		msgMap.put(2, "ERP商品最新状态和OS可能未同步,请稍后再试");
		msgMap.put(3, "ERP商品仓库状态和OS可能未同步,请稍后再试");
	}
	public static ErpResultBean getNewInstance(){
		return new ErpResultBean();
	}
	public ErpResultBean() {
		Code=-1;
		Message="初始化";
	}
	public ErpResultBean(int code, String message) {
		super();
		Code = code;
		Message = message;
	}
	public int getCode() {
		return Code;
	}
	public void setCode(int code) {
		Code = code;
	}
	public String getMessage() {
		String msg="";
		if(Code>=1){
			msg= msgMap.get(Code)==null?Message:msgMap.get(Code);
		}else{
			msg=Message;
		}
		return msg;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public void setCode_Message(int code, String message){
		Code = code;
		Message = message;
	}
	@Override
	public String toString() {
		String erpMsg=new String("");
		if (Code>1){
			erpMsg="ERP Message:"+Message;
		}
		return "ErpWebserviceResultBean [Code=" + Code + ", OS Message=" + getMessage()
				+ "]"+erpMsg;
	}
}
