package com.work.shop.oms.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.HttpClientUtil;

/**
 * 用户接口
 * @author
 *
 */
public class UserApiService {
	
	private static Logger log = Logger.getLogger(UserApiService.class);
	
	private static String CAS_GAVE_POINTS_URL = ConfigCenter.getProperty("userTaskPlan.givePoit.address");
	
//	private static String USER_INFO_URL =ConfigCenter.getProperty("user_info_api");
//	private static String BASE_USER_INFO_URL =ConfigCenter.getProperty("base_user_info_api");
	private static String USER_RANK_URL =ConfigCenter.getProperty("user_rank_api");
	private static String USER_INSERT_URL =ConfigCenter.getProperty("user_insert_api");
	
	public static String getUserInfo(String uid) {
		String back = null;
		String userInfoUurl = CAS_GAVE_POINTS_URL +"/custom/api/getUserInfo.do";
		try {
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("uid", uid));
			back = HttpClientUtil.post(userInfoUurl, valuePairs);
		} catch (Exception ex) {
			log.error(ex);
		}
		return back;
	}
	
	public static String getBaseUserInfo(String uid) {
		String back = null;
		String baseUserInfoUurl = CAS_GAVE_POINTS_URL +"/custom/api/getBaseSicUserInfo.do";
		try {
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("uid", uid));
			back = HttpClientUtil.post(baseUserInfoUurl, valuePairs);
		} catch (Exception ex) {
			log.error(ex);
		}
		return back;
	}
	
	public static String getUserRank(String uid) {
		String back = null;
		try {
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("uid", uid));
			back = HttpClientUtil.post(USER_RANK_URL, valuePairs);
		} catch (Exception ex) {
			log.error(ex);
		}
		return back;
	}
	
	
	public static String insertUser(String uid) {
		String back = null;
		try {
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("uid", uid));
			Random r = new Random(System.currentTimeMillis());
			int pwd = 100000+r.nextInt(99999);
			valuePairs.add(new BasicNameValuePair("pwd","bm"+pwd));
			valuePairs.add(new BasicNameValuePair("realName", "bm_"+uid));
			valuePairs.add(new BasicNameValuePair("nickName", "bm_"+uid));
			valuePairs.add(new BasicNameValuePair("comeFrom", "BgOrderManager"));
			back = HttpClientUtil.post(USER_INSERT_URL, valuePairs);
		} catch (Exception ex) {
			log.error(ex);
		}
		return back;
	}
	
	
	public static void main(String[] args) {
		String x = getUserRank("lekai8");
		JSONObject object = JSON.parseObject(x);
		boolean  ok = (Boolean) object.get("isOk");
		if(ok){
			JSONObject result = object.getJSONObject("result");
			System.out.println(result.getInteger("rankPoints")+" "+result.getInteger("points_banggo"));
		}
		String y = getUserInfo("lekai8");
		JSONObject object1 = JSON.parseObject(y);
		boolean  ok2 = (Boolean) object.get("isOk");
		if(ok2){
			JSONObject result1 = object1.getJSONObject("UserInfo");
			System.out.println(result1.getString("uid")+" "+result1.getString("userType"));
		}
	}
	
}
