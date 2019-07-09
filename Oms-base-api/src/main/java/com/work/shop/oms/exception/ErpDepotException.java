package com.work.shop.oms.exception;

public class ErpDepotException extends Exception {

	private static final long serialVersionUID = -5452990304815405380L;

	public ErpDepotException(String message) {
		super(message);
	}

	public ErpDepotException(String message, Throwable e) {
		super(message, e);
	}
}
