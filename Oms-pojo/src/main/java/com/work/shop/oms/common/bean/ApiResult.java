package com.work.shop.oms.common.bean;

public class ApiResult {
	/**
	 * 接口调用成功 
	 */
	public static final int SUCCESS = 1;
	
	/**
	 * 接口调用失败
	 */
	public static final int ERROR = -1;
	
	/**
	 * 返回调用方缺货
	 */
	public static final int OUT_STOCK = 2;
	
	/**
	 * 返回调用方已占用
	 */
	public static final int HAS_STOCK = 3;
	
	
	/**
	 * 返回调用方已释放
	 */
	public static final int HAS_RELEASE = 3;
	
	
	/**
	 * 返回调用方异常
	 */
	public static final int OUT_ERROR = 2;
	
	/**
	 * 接口调用返回缺货
	 */
	public static final int STOCK_NO_FULL = 0;
	
	/**
	 * 接口调用，发生异常
	 */
	public static final int EXCEPTION = -500;
	/**
	 * hessian调用发生异常
	 */
	public static final int HESSIAN_ERROR = -400;
	/**
	 * 验证错误
	 */
	public static final int AUTH_ERROR = -3;
	/**
	 * 没有传入sku
	 */
	public static final int NONE_SKU = -2;

}
