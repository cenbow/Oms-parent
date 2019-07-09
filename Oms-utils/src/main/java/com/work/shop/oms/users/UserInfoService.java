package com.work.shop.oms.users;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.common.bean.UserUsers;

/**
 * 用户相关服务
 * @author
 *
 */
public class UserInfoService {

	private static Logger logger = Logger.getLogger(UserInfoService.class);

	
	/**
	 * 根据UID获取用户信息
	 * @param uid 用户Id区别于CAS Nid
	 * @return
	 */
	public static UserUsers getUserInfo(String uid) {
		UserUsers user = null;
		String u = UserApiService.getUserInfo(uid);
		try {
			logger.info("CAS_BACK_STR_getUserInfo:"+u);
			if (!StringUtils.isBlank(u)) {
				JSONObject object = JSON.parseObject(u);
				boolean ok = (Boolean) object.get("isOk");
				if (ok) {
					JSONObject result = object.getJSONObject("UserInfo");
					if (result != null) {
						user = new UserUsers();
						user.setUserId(uid);
						user.setUserType(result.getInteger("userType"));
						user.setLevelId(result.getInteger("levelId"));
						user.setMobile(result.getString("mobile"));
						user.setEmail(result.getString("email"));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return user;
	}
	
	/**
	 * 根据UID获取用户信息
	 * @param uid 用户Id区别于CAS Nid
	 * @return
	 */
	public static UserUsers getBaseUserInfo(String uid) {
		UserUsers user = null;
		String u = UserApiService.getBaseUserInfo(uid);
		try {
			logger.info("CAS_BACK_STR_getUserInfo:"+u);
			if (!StringUtils.isBlank(u)) {
				JSONObject object = JSON.parseObject(u);
				boolean ok = (Boolean) object.get("isOk");
				if (ok) {
					JSONObject result = object.getJSONObject("UserInfo");
					if (result != null) {
						user = new UserUsers();
						user.setUserId(uid);
						user.setUserType(result.getInteger("userType"));
						user.setLevelId(result.getInteger("levelId"));
						user.setMobile(result.getString("mobile"));
						user.setEmail(result.getString("email"));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return user;
	}

	/**
	 * 获取用户积分
	 * @param uid
	 * @return
	 */
	public static Integer getUserRank(String uid) {
		Integer r = -11111;
		String rank = UserApiService.getUserRank(uid);
		try {
			logger.info("CAS_BACK_STR_getUserInfo:"+rank);
			if (!StringUtils.isBlank(rank)) {
				JSONObject object = JSON.parseObject(rank);
				boolean ok = (Boolean) object.get("isOk");
				if (ok) {
					JSONObject result = object.getJSONObject("result");
					if (result != null) {
						r = result.getInteger("rankPoints");
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return r;
	}
	
	
	/**
	 * 插入用户
	 * @param uid
	 * @return
	 */
	public static boolean insertUser(String uid) {
		boolean r = false;
		String rank = UserApiService.insertUser(uid);
		logger.info("CAS_BACK_STR_getUserInfo:"+rank);
		try {
			if (!StringUtils.isBlank(rank)) {
				JSONObject object = JSON.parseObject(rank);
				boolean ok = (Boolean) object.get("isOk");
				if (ok) {
					r = true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return r;
	}
	
}
