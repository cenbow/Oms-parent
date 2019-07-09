/*
 * 上海坦思计算机系统有限公司
 * 
 * -----------+----------+------------------------------------------
 * 	更新时间       |  更新者      |  				备注
 * -----------+----------+------------------------------------------
 * 	2010-09-26        张瑞雨              					创建
 */
package com.work.shop.oms.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.shop.oms.common.utils.Config;
import com.work.shop.oms.exception.CookieTimeoutExpcetion;

/**
 * 
 * @author zhangruiyu
 * 
 */
public final class SecurityCookie implements Config {

	private SecurityCookie() {
	}

	private static SecurityCookie	savecookie	= new SecurityCookie();

	private final SimpleDateFormat	format		= new SimpleDateFormat(
														"yyyyMMddHHmmss");
	private final static int		LENGTH		= 12;
	
	private final static int MAX_AGE = 60 * 60 * 5;

	private String randomKey() {
		String s = format.format(new Date());
		return Long.toHexString(Long.parseLong(s));
	}

	private Long pasreKey(String s) {
		return Long.parseLong(s, 16);
	}

	/**
	 * 添加cookie
	 * 
	 * @param response
	 * @param name
	 *            cookie的名称
	 * @param value
	 *            cookie的值
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Throwable
	 */
	public static void addCookie(HttpServletResponse response, String name,
			String value) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		String key = savecookie.randomKey();
		value = AESUtil.parseByte2HexStr(AESUtil.encrypt(value, key)) + key;
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(MAX_AGE);
		response.addCookie(cookie);
	}

	/**
	 * 获取cookie的值
	 * 
	 * @param request
	 * @param name
	 *            cookie的名称
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws CookieTimeoutExpcetion
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String getCookieByName(HttpServletRequest request,
			HttpServletResponse response, String name)
			throws UnsupportedEncodingException, CookieTimeoutExpcetion,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {

		Map<String, Cookie> cookieMap = readCookieMap(request);

		if (cookieMap.containsKey(name)) {

			Cookie cookie = (Cookie) cookieMap.get(name);

			if (cookie.getValue() == null || "".equals(cookie.getValue())) {
				return null;
			}

			String s = URLDecoder.decode(cookie.getValue(), "utf-8");

			if (s.length() - LENGTH <= 0) {
				return null;
			}

			String b = s.substring(0, s.length() - LENGTH);

			String e = s.substring(s.length() - LENGTH);

			Long a = savecookie.pasreKey(e);

			if (Long.valueOf(TimeUtil.format3Date(new Date())) >= (a + cookie_timeout)) {
				throw new CookieTimeoutExpcetion("cookie time out b:" + b + ", e:" + a);
			}

			String value = new String(AESUtil.decrypt(AESUtil
					.parseHexStr2Byte(b), "" + e), "utf-8");
			// 当response 为空时 不做将用户权限信息写入cookie中
			if ((Long.valueOf(TimeUtil.format3Date(new Date())) - a) >= 1 * 60L && response != null) {
				addCookie(response, name, value);
			}

			return value;
		}
		return null;
	}

	private static Map<String, Cookie> readCookieMap(
			HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				cookieMap.put(cookies[i].getName(), cookies[i]);
			}
		}
		return cookieMap;
	}
	
	public static void main(String[] args) throws Exception {
		String value2 = "8801759142C0457D7BA341142D32FD9391D95C0C03F7C1013E59382CC87C804653162C00B7EF40F14FE8A72FD013BEA812517e35c886";
		String s = URLDecoder.decode(value2, "utf-8");

		if (s.length() - LENGTH <= 0) {
		}

		String b = s.substring(0, s.length() - LENGTH);

		String e = s.substring(s.length() - LENGTH);

		Long a = savecookie.pasreKey(e);

		if (Long.valueOf(TimeUtil.format3Date(new Date())) >= (a + cookie_timeout)) {
			throw new CookieTimeoutExpcetion("cookie time out b:" + b + ", e:" + a);
		}

		String value = new String(AESUtil.decrypt(AESUtil
				.parseHexStr2Byte(b), "" + e), "utf-8");
		
		System.out.println(value);
	}
}
