package com.work.shop.oms.orderop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.SettleOrderInfo;
import com.work.shop.oms.bean.UserPointsException;
import com.work.shop.oms.common.bean.OptionPointsBean;
import com.work.shop.oms.common.bean.OptionPointsResponse;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.UserUsers;
import com.work.shop.oms.dao.UserPointsExceptionMapper;
import com.work.shop.oms.orderop.service.UserPointsService;
import com.work.shop.oms.users.UserInfoService;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.HttpClientUtil;
import com.work.shop.oms.utils.StringUtil;

@Service
public class UserPointsServiceImpl implements UserPointsService{

	private static Logger logger = Logger.getLogger(UserPointsServiceImpl.class);
	private static String CAS_GAVE_POINTS_URL = ConfigCenter.getProperty("userTaskPlan.givePoit.address");
	
	private static String KT_CAS_GAVE_POINTS_URL = ConfigCenter.getProperty("KTuserTaskPlan.givePoit.address");
	
	private static String OS_SETTLE_POINTS_START = "OS_";
	private static String OS_SETTLE_DO_FROM = "orderservice";
	private static String OS_SETTLE_POINTS_TYPE = "banggo";
	
	@Resource
	private UserPointsExceptionMapper userPointsExceptionMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public ReturnInfo<String> processUserPoints(SettleOrderInfo settleOrderInfo) {
		ReturnInfo<String> response = new ReturnInfo<String>();
		settleOrderInfo.setSource(2);
		settleOrderInfo.setDoStatus("1");
		try {
			String param = JSON.toJSONString(settleOrderInfo);
			logger.info("[UserPointsService.giveUserPoints]start...SettleOrderInfo:"+param);
//			param = new String(param.getBytes("UTF-8"), "iso8859-1");
			UserUsers user = UserInfoService.getUserInfo(settleOrderInfo.getUvid());
			if (user == null) {
				throw new RuntimeException("用户ID[" + settleOrderInfo.getUvid() + "]无效，无法获取有效的用户对象信息");
			}
			List<NameValuePair> httpParam = joinParams(settleOrderInfo);
			String url = CAS_GAVE_POINTS_URL +"/custom/api/integralQueue.do";
			String back = HttpClientUtil.post(url,httpParam);
//			back = URLDecoder.decode(back, "UTF-8");
			if (StringUtil.isEmpty(back)) {
				logger.info("[CAS订单创建扣减接口调用]FAILURE,SettleOrderInfo:"+JSON.toJSONString(settleOrderInfo)+",reponse:返回结果为空！");
				response.setMessage("[CAS订单创建扣减接口调用]返回结果为空！");
				return response;
			}
			Map<String, Object> map = JSON.parseObject(back, Map.class);
			if (map != null && !map.isEmpty()) {
				logger.info("[CAS订单创建扣减接口调用]SUCCESS,SettleOrderInfo:"+JSON.toJSONString(settleOrderInfo)+",reponse:" + back);
				if ((Boolean)map.get("isOk")) {
					response.setIsOk(1);
					response.setMessage((String)map.get("message"));
				} else {
					response.setMessage((String)map.get("message"));
				}
			} else {
				response.setMessage("[CAS订单创建扣减接口调用]返回结果为空！");
				logger.error("[CAS订单创建扣减接口调用]FAILURE,SettleOrderInfo:"+JSON.toJSONString(settleOrderInfo)+",reponse:返回结果为空！");
			}
		} catch (Exception e) {
			response.setIsOk(Constant.YESORNO_NO);
			response.setMessage("CAS积分赠送接口调用失败！错误信息:"+e.getMessage());
			logger.error("[CAS积分赠送接口调用]Failed,SettleOrderInfo:"+JSON.toJSONString(settleOrderInfo)+",msg:"+e.getMessage(),e);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ReturnInfo<String> giveUserPoints(String uid, Integer points,String dealCode) {
		logger.debug("[UserPointsService.giveUserPoints]start.....uid:"+uid+",points:"+points+",dealCode:"+dealCode);
		// 设置参数
		ReturnInfo<String> response = new ReturnInfo<String>();
		String doEvidence = OS_SETTLE_POINTS_START + dealCode;
		String doFrom = OS_SETTLE_DO_FROM;// usertask
		String comment = "订单结算赠送积分";// 会员计划赠送积分
		String pointStatus = "0";// 0
		String pointsType = OS_SETTLE_POINTS_TYPE;
		String doStatus = "1";
		try {
			UserUsers user = UserInfoService.getUserInfo(uid);
			if (user == null) {
				throw new RuntimeException("用户ID[" + uid + "]无效，无法获取有效的用户对象信息");
			}
			String codeMD5 = mod5Code(uid, points, pointsType, pointStatus,
					doStatus, doFrom, doEvidence);
			List<NameValuePair> paramsList = joinParams(uid,points,pointStatus,codeMD5, doEvidence, doFrom, comment,doStatus,pointsType);
			
			// 取得赠送积分接口的地址
			String url = CAS_GAVE_POINTS_URL + "/custom/api/customerUsePoints.do";
			
			// 开始调用赠送积分接口发送数据
			String responseStr = HttpClientUtil.post(url, paramsList);
			if (StringUtil.isEmpty(responseStr)) {
				logger.debug("[CAS订单创建扣减接口调用]FAILURE,uid:"+uid+",points:"+points+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:返回结果为空！");
				response.setMessage("[CAS订单创建扣减接口调用]返回结果为空！");
				return response;
			}
			Map<String, Object> map = JSON.parseObject(responseStr, Map.class);
			if (map != null && !map.isEmpty()) {
				logger.debug("[CAS订单创建扣减接口调用]SUCCESS,uid:"+uid+",points:"+points+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:" + responseStr);
				if ((Boolean)map.get("isOk")) {
					response.setIsOk(1);
					response.setMessage((String)map.get("message"));
				} else {
					response.setMessage((String)map.get("message"));
				}
			} else {
				response.setMessage("[CAS订单创建扣减接口调用]返回结果为空！");
				logger.debug("[CAS订单创建扣减接口调用]FAILURE,uid:"+uid+",points:"+points+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:返回结果为空！");
			}
//			responseStr = responseStr.replaceAll("isOk", "isSucc");
//			logger.debug("[CAS积分赠送接口调用]SUCCESS,uid:"+uid+",points:"+points+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:" + responseStr);
//			response = JSON.parseObject(responseStr, ReturnInfo.class);
		} catch (Exception e) {
			response.setIsOk(Constant.YESORNO_NO);
			response.setMessage("CAS积分赠送接口调用失败！错误信息:"+e.getMessage());
			logger.error("[CAS积分赠送接口调用]Failed.uid:"+uid+",points:"+points+",msg:"+e.getMessage(),e);
		}
		return response;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public ReturnInfo<String> deductionsPoints(String uid, Integer points,
			String dealCode) {
		logger.debug("[UserPointsService.deductionsPoints]start.....uid:"+uid+",points:"+points+",dealCode:"+dealCode);
		// 设置参数
		ReturnInfo<String> response = new ReturnInfo<String>();
		String doEvidence = OS_SETTLE_POINTS_START + dealCode + "001";
		String doFrom = OS_SETTLE_DO_FROM;// usertask
		String comment = "订单创建扣减积分";// 订单创建扣减积分
		String pointStatus = "1"; // 扣减为1
		String pointsType = OS_SETTLE_POINTS_TYPE;
		String doStatus = "1";
		try {
			UserUsers user = UserInfoService.getUserInfo(uid);
			if (user == null) {
				throw new RuntimeException("用户ID[" + uid + "]无效，无法获取有效的用户对象信息");
			}
			String codeMD5 = mod5Code(uid, points, pointsType, pointStatus,
					doStatus, doFrom, doEvidence);
			List<NameValuePair> paramsList = deductionsPointsParams(uid,points, pointStatus,codeMD5, doEvidence,
					doFrom, comment,doStatus,pointsType);
			// 取得赠送积分接口的地址
			String url = CAS_GAVE_POINTS_URL + "/custom/api/customerUsePoints.do";
			logger.info("积分抵扣调用参数:"+JSON.toJSONString(paramsList));
			// 开始调用赠送积分接口发送数据
			String responseStr = HttpClientUtil.post(url, paramsList);
			if (StringUtil.isEmpty(responseStr)) {
				logger.error("[CAS订单创建扣减接口调用]FAILURE,uid:"+uid+",points:"+points+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:返回结果为空！");
				response.setMessage("[CAS订单创建扣减接口调用]返回结果为空！");
				return response;
			}
			Map<String, Object> map = JSON.parseObject(responseStr, Map.class);
			if (map != null && !map.isEmpty()) {
				logger.info("[CAS订单创建扣减接口调用]SUCCESS,uid:"+uid+",points:"+points+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:" + responseStr);
				if ((Boolean)map.get("isOk")) {
					response.setIsOk(1);
					response.setMessage((String)map.get("message"));
				} else {
					response.setMessage((String)map.get("message"));
				}
			} else {
				response.setMessage("[CAS订单创建扣减接口调用]返回结果为空！");
				logger.error("[CAS订单创建扣减接口调用]FAILURE,uid:"+uid+",points:"+points+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:返回结果为空！");
			}
		} catch (Exception e) {
			response.setMessage("CAS订单创建扣减接口调用失败！错误信息:"+e.getMessage());
			logger.error("[CAS订单创建扣减接口调用]Failed.uid:"+uid+",points:"+points+",msg:"+e.getMessage(),e);
		}
		return response;
	}

	// 调用积分接口前设置要传递的参数
	private List<NameValuePair> deductionsPointsParams(String uid,int points,String pointStatus,String codeMD5, String doEvidence, String doFrom,
			String comment,String doStatus,String pointsType) {

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		NameValuePair param = null;
		param = new BasicNameValuePair("uid", uid);
		paramsList.add(param);
		param = new BasicNameValuePair("points", points+"");
		paramsList.add(param);
		param = new BasicNameValuePair("pointsType", pointsType);
		paramsList.add(param);
		param = new BasicNameValuePair("pointStatus", pointStatus);
		paramsList.add(param);
		param = new BasicNameValuePair("doStatus", doStatus);
		paramsList.add(param);
		param = new BasicNameValuePair("doFrom", doFrom);
		paramsList.add(param);
		param = new BasicNameValuePair("doEvidence", doEvidence);
		paramsList.add(param);
		param = new BasicNameValuePair("comment", comment);
		paramsList.add(param);
		param = new BasicNameValuePair("code", codeMD5);
		paramsList.add(param);
		return paramsList;
	}

	private List<NameValuePair> joinParams(SettleOrderInfo settleOrderInfo) {
		List<SettleOrderInfo> infos = new ArrayList<SettleOrderInfo>();
		infos.add(settleOrderInfo);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		NameValuePair param = null;
		param = new BasicNameValuePair("json", JSON.toJSONString(infos));
		paramsList.add(param);
		param = new BasicNameValuePair("length", "1");
		paramsList.add(param);
//		param = new BasicNameValuePair("goodsinfos", JSON.toJSONString(settleOrderInfo.getGoodsinfos()));
//		paramsList.add(param);
//		param = new BasicNameValuePair("source", "2");
//		paramsList.add(param);
//		param = new BasicNameValuePair("settlement", settleOrderInfo.getSettlement()+"");
//		paramsList.add(param);
//		param = new BasicNameValuePair("shopid", settleOrderInfo.getShopid());
//		paramsList.add(param);
//		param = new BasicNameValuePair("guideid", settleOrderInfo.getGuideid());
//		paramsList.add(param);
//		param = new BasicNameValuePair("doStatus", "1");
//		paramsList.add(param);
//		param = new BasicNameValuePair("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(settleOrderInfo.getTime()));
//		paramsList.add(param);
		
		return paramsList;
	}

	// 调用积分接口前设置要传递的参数
	private List<NameValuePair> joinParams(String uid,int points, String pointStatus,String codeMD5, String doEvidence, String doFrom,
			String comment,String doStatus,String pointsType) {

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		NameValuePair param = null;
		param = new BasicNameValuePair("uid", uid);
		paramsList.add(param);
		param = new BasicNameValuePair("points", points+"");
		paramsList.add(param);
		param = new BasicNameValuePair("pointsType", pointsType);
		paramsList.add(param);
		param = new BasicNameValuePair("pointStatus", pointStatus);
		paramsList.add(param);
		param = new BasicNameValuePair("doStatus", doStatus);
		paramsList.add(param);
		param = new BasicNameValuePair("doFrom", doFrom);
		paramsList.add(param);
		param = new BasicNameValuePair("doEvidence", doEvidence);
		paramsList.add(param);
		param = new BasicNameValuePair("comment", comment);
		paramsList.add(param);
		param = new BasicNameValuePair("code", codeMD5);
		paramsList.add(param);
		return paramsList;
	}

	// 加密请求参数
	private String mod5Code(String uid, Integer points,String pointsType, String pointStatus, 
			String doStatus,String doFrom, String doEvidence) {
		StringBuffer paramsMD5 = new StringBuffer();
		paramsMD5.append("uid=" + uid);
		paramsMD5.append("&points=" + points);
		paramsMD5.append("&pointsType=" + pointsType);
		paramsMD5.append("&pointStatus=" + pointStatus);
		paramsMD5.append("&doStatus=" + doStatus);
		paramsMD5.append("&doFrom=" + doFrom);
		paramsMD5.append("&doEvidence=" + doEvidence);
		logger.info("mod5Code-before:"+paramsMD5.toString());
		String codeMD5 = DigestUtils.md5Hex(paramsMD5.toString());
		logger.info("mod5Code-after:"+paramsMD5.toString());
		return codeMD5;
	}

	@Override
	public Integer searchUserPoints(String userId, Double orderAmount) {
		return null;
	}

	@Override
	public ReturnInfo<Integer> optionPoints(OptionPointsBean pointsBean) {
		ReturnInfo<Integer> response = new ReturnInfo<Integer>(Constant.OS_NO);
		if (pointsBean == null) {
			response.setMessage("参数[pointsBean]不能为空");
			return response;
		}
		if (pointsBean.getOrderType() == 0) {
			response.setMessage("参数[pointsBean.orderType]不能为0");
			return response;
		}
		String orderType = "";
		if (pointsBean.getOrderType() == 1) {
			orderType = "冻结点数";
		} else if (pointsBean.getOrderType() == 2) {
			orderType = "释放点数";
		} else if (pointsBean.getOrderType() == 3) {
			orderType = "消费点数";
		} else if (pointsBean.getOrderType() == 4) {
			orderType = "退返点数";
		} else {
			response.setMessage("参数[pointsBean.orderType]不在有效范围内【1-4】");
			return response;
		}
		try {
			String param = JSON.toJSONString(pointsBean);
			logger.info("[UserPointsService.optionPoints]start...pointsBean:"+param);
			/*UserUsers user = UserInfoService.getUserInfo(pointsBean.getUserId());
			if (user == null) {
				throw new RuntimeException("用户ID[" + pointsBean.getUserId() + "]无效，无法获取有效的用户对象信息");
			}*/
			List<NameValuePair> httpParam = joinOptionPointsParams(pointsBean);
			String url = KT_CAS_GAVE_POINTS_URL +"/custom/api/doKLPointsPay.do";
			String back = HttpClientUtil.post(url,httpParam);
			if (StringUtil.isEmpty(back)) {
				logger.info("[订单创建" +orderType + "接口调用]FAILURE,pointsBean:"+JSON.toJSONString(pointsBean)+",reponse:返回结果为空！");
				response.setMessage("[订单创建" +orderType + "接口调用]返回结果为空！");
				return response;
			}
			OptionPointsResponse pointsResponse = JSON.parseObject(back, OptionPointsResponse.class);
			if (pointsResponse != null) {
				logger.info("[订单创建" +orderType + "接口调用]SUCCESS,pointsBean:"+JSON.toJSONString(pointsBean)+",reponse:" + back);
				if (pointsResponse.getIsOk().intValue() == 0) {
					response.setIsOk(1);
					response.setOrderOutSn(pointsResponse.getResult().getContent());
					response.setMessage(orderType + "成功");
				} else {
					response.setMessage("[订单创建" +orderType + "接口调用]失败" + pointsResponse.getMessage());
				}
			} else {
				response.setMessage("[订单创建" +orderType + "接口调用]返回结果为空！");
				logger.error("[订单创建" +orderType + "接口调用]FAILURE,pointsBean:"+JSON.toJSONString(pointsBean)+",reponse:返回结果为空！");
			}
		} catch (Exception e) {
			response.setIsOk(Constant.OS_NO);
			response.setMessage("订单创建" +orderType + "接口调用失败！错误信息:"+e.getMessage());
			logger.error("[订单创建" +orderType + "接口调用]Failed,pointsBean:"+JSON.toJSONString(pointsBean)+",msg:"+e.getMessage(), e);
		} finally {
			if (response.getIsOk() == Constant.OS_NO) {
				UserPointsException exception = new UserPointsException();
				exception.setCreateTime(new Date());
				exception.setOptionNote(response.getMessage());
				exception.setOptionType(pointsBean.getOrderType() + "");
				exception.setOrderSn(pointsBean.getOrderNo());
				exception.setSubmitParam(JSON.toJSONString(pointsBean));
				exception.setUserId(pointsBean.getUserId());
				exception.setPoints(BigDecimal.valueOf(pointsBean.getOrderPoints()));
				userPointsExceptionMapper.insertSelective(exception);
			}
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ReturnInfo<String> customerUseBv(String uid, Integer bvValue,String dealCode, String bvStatus) {
		logger.debug("[UserPointsService.customerUseBv]start...uid:"+uid+",points:"+bvValue+",dealCode:"+dealCode+",bvStatus="+ bvStatus);
		// 设置参数
		ReturnInfo<String> response = new ReturnInfo<String>();
		String doEvidence = OS_SETTLE_POINTS_START + dealCode;
		String doFrom = OS_SETTLE_DO_FROM;// usertask
		String comment = "订单结算赠送美力值";
		String pointsType = "NEWFORCE";
		String doStatus = "1";
		if (StringUtil.isTrimEmpty(bvStatus)) {
			response.setMessage("bvStatus is empty");
			return response;
		}
		if (bvStatus.equals("1")) {
			comment = "订单取消或退单扣减美力值";
		} else {
			comment = "订单结算赠送美力值";
		}
		try {
			String codeMD5 = mod5Code(uid, bvValue, pointsType, bvStatus, doStatus, doFrom, doEvidence);
			List<NameValuePair> paramsList = joinParams(uid,bvValue,bvStatus,codeMD5, doEvidence, doFrom, comment,doStatus,pointsType);
			String url = CAS_GAVE_POINTS_URL + "/custom/api/customerUseBv.do";
			// 开始调用BV接口发送数据
			String responseStr = HttpClientUtil.post(url, paramsList);
			if (StringUtil.isEmpty(responseStr)) {
				logger.debug("["+comment+"接口调用]FAILURE,uid:"+uid+",points:"+bvValue+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:返回结果为空！");
				response.setMessage("["+comment+"接口调用]返回结果为空！");
				return response;
			}
			Map<String, Object> map = JSON.parseObject(responseStr, Map.class);
			if (map != null && !map.isEmpty()) {
				logger.debug("["+comment+"接口调用]SUCCESS,uid:"+uid+",points:"+bvValue+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:" + responseStr);
				if ((Boolean)map.get("isOk")) {
					response.setIsOk(1);
					response.setMessage((String)map.get("message"));
				} else {
					response.setMessage((String)map.get("message"));
				}
			} else {
				response.setMessage("["+comment+"调用]返回结果为空！");
				logger.debug("["+comment+"接口调用]FAILURE,uid:"+uid+",points:"+bvValue+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:返回结果为空！");
			}
		} catch (Exception e) {
			response.setIsOk(Constant.YESORNO_NO);
			response.setMessage("["+comment+"]调用失败！错误信息:"+e.getMessage());
			logger.error("["+comment+"]Failed.uid:"+uid+",points:"+bvValue+",msg:"+e.getMessage(),e);
		}
		return response;
	}
	
	@Override
	public ReturnInfo<String> customerUseBvByKT(String uid, Integer bvValue,
			String dealCode, String bvStatus) {
		logger.debug("[UserPointsService.customerUseBv]start...uid:"+uid+",points:"+bvValue+",dealCode:"+dealCode+",bvStatus="+ bvStatus);
		// 设置参数
		ReturnInfo<String> response = new ReturnInfo<String>();
		String doEvidence = OS_SETTLE_POINTS_START + dealCode;
		String doFrom = OS_SETTLE_DO_FROM;// usertask
		String comment = "订单结算赠送美力值";
		String pointsType = OS_SETTLE_POINTS_TYPE;
		String doStatus = "1";
		if (StringUtil.isTrimEmpty(bvStatus)) {
			response.setMessage("bvStatus is empty");
			return response;
		}
		if (bvStatus.equals("1")) {
			comment = "订单取消或退单扣减美力值";
		} else {
			comment = "订单结算赠送美力值";
		}
		try {
			String codeMD5 = mod5Code(uid, bvValue, pointsType, bvStatus, doStatus, doFrom, doEvidence);
			List<NameValuePair> paramsList = joinParams(uid,bvValue,bvStatus,codeMD5, doEvidence, doFrom, comment,doStatus,pointsType);
			String url = KT_CAS_GAVE_POINTS_URL + "/custom/api/customerUseBv.do";
			// 开始调用BV接口发送数据
			String responseStr = HttpClientUtil.post(url, paramsList);
			if (StringUtil.isEmpty(responseStr)) {
				logger.debug("["+comment+"接口调用]FAILURE,uid:"+uid+",points:"+bvValue+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:返回结果为空！");
				response.setMessage("["+comment+"接口调用]返回结果为空！");
				return response;
			}
			Map<String, Object> map = JSON.parseObject(responseStr, Map.class);
			if (map != null && !map.isEmpty()) {
				logger.debug("["+comment+"接口调用]SUCCESS,uid:"+uid+",points:"+bvValue+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:" + responseStr);
				if ((Boolean)map.get("isOk")) {
					response.setIsOk(1);
					response.setMessage((String)map.get("message"));
				} else {
					response.setMessage((String)map.get("message"));
				}
			} else {
				response.setMessage("["+comment+"调用]返回结果为空！");
				logger.debug("["+comment+"接口调用]FAILURE,uid:"+uid+",points:"+bvValue+",url:"+url+",paramsList:"+JSON.toJSONString(paramsList)+",reponse:返回结果为空！");
			}
		} catch (Exception e) {
			response.setIsOk(Constant.YESORNO_NO);
			response.setMessage("["+comment+"]调用失败！错误信息:"+e.getMessage());
			logger.error("["+comment+"]Failed.uid:"+uid+",points:"+bvValue+",msg:"+e.getMessage(),e);
		}
		return response;
	}

	private List<NameValuePair> joinOptionPointsParams(OptionPointsBean pointsBean) {
		List<OptionPointsBean> infos = new ArrayList<OptionPointsBean>();
		infos.add(pointsBean);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		NameValuePair param = null;
		param = new BasicNameValuePair("userId", pointsBean.getUserId());
		paramsList.add(param);
		param = new BasicNameValuePair("orderAmount", pointsBean.getOrderAmount().toString());
		paramsList.add(param);
		param = new BasicNameValuePair("orderNo", pointsBean.getOrderNo());
		paramsList.add(param);
		param = new BasicNameValuePair("orderPoints", pointsBean.getOrderPoints() + "");
		paramsList.add(param);
		param = new BasicNameValuePair("orderBv", pointsBean.getOrderBv() + "");
		paramsList.add(param);
		param = new BasicNameValuePair("orderType", pointsBean.getOrderType() + "");
		paramsList.add(param);
		return paramsList;
	}
}
