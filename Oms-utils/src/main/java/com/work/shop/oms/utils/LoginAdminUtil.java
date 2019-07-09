package com.work.shop.oms.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台登录用户，cookie操作类
 * @author
 *
 */
public class LoginAdminUtil {

	/**
	 * 获取登录用户的sessionKey
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getSesseionKey(HttpServletRequest request, HttpServletResponse response){
		try {
			return SecurityCookie.getCookieByName(request, response, Constant.COOKIE_ID_KEY);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 往cookie中写入登录用户的sessionKey
	 * @param response
	 * @param sessionKey
	 * @return
	 */
	public static boolean setSessionKey(HttpServletResponse response, String sessionKey) {
		try {
			SecurityCookie.addCookie(response, Constant.COOKIE_ID_KEY, sessionKey);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 删除用户的sessionKey
	 * @param response
	 * @return
	 */
	public static boolean deleteSessionKey(HttpServletResponse response){
		try {
			SecurityCookie.addCookie(response, Constant.COOKIE_ID_KEY, "0");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
