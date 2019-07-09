package com.work.shop.oms.exception;

public class OrderException extends RuntimeException {
	private static final long serialVersionUID = 8804526464746213456L;

	public OrderException(){
		super();
	}
	
	public OrderException(String message){
		super(message);
	}
	
	public OrderException(String message,Throwable cause){
		super(message,cause);
	}
	
	public OrderException(Exception e) {
		super(e);
	}

}
