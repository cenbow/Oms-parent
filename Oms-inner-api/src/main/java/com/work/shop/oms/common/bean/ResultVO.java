package com.work.shop.oms.common.bean;


import java.io.Serializable;


/**
 * 用ajax时返回结果用， 大家自己的可以继承这个。
 * @author lhj
 */

public class ResultVO <T>  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4252624739225311546L;
	/**结果code*/
	private String code = "200";//200:成功
	/**结果返回消息，默认为空字符*/
	private String message ="";
	private String title="";
	private T obj;
	
	public ResultVO() {

	}
	
	
	/**
	 * @param code
	 */
	public ResultVO(String code) {
		super();
		this.code = code;
	}
	
	
	/**
	 * @param code
	 * @param message
	 */
	public ResultVO(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	
}
