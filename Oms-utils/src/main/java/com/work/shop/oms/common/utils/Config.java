/*
 * 上海坦思计算机系统有限公司
 * 
 * -----------+----------+------------------------------------------
 * 	更新时间       |  更新者      |  				备注
 * -----------+----------+------------------------------------------
 * 	2010-07-12        张瑞雨              					创建
 */

package com.work.shop.oms.common.utils;

public interface Config {

	/**
	 * 随机验证码的图片的宽度
	 */
	int IMAGE_WIDTH = 65;

	/**
	 * 随机验证码的图片的高度
	 */
	int IMAGE_HEIGHT = 20;

	/**
	 * 随机验证码的图片的元素
	 */
	char[] CODE = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
			'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	/**
	 * 最大投票数
	 */
	int MAX_VOTE_COUNT = 5;

	/**
	 * cookie失效时间30分钟
	 */
	Long cookie_timeout = 60L * 60L * 10;

	/**
	 * 员工所在地域订单权限用到
	 */
	String ADD_HQL_WHERE_KEY = "ads";
 
	
	/** 员工COOKIE 中的 ID*/
	String USER_COOKIE_ID = "uid";

	/** 订单锁定时间超时 30分钟 */
	int lockTimeout = 30 * 60;
	
	/** 订单默认配送地 */
	String DEFAULT_AREA_CODE = "0";
}
