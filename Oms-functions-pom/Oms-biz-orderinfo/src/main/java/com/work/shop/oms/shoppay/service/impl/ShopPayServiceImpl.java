package com.work.shop.oms.shoppay.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.OcpbStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.common.bean.ShopPayCashBean;
import com.work.shop.oms.common.bean.ShopPayResultBean;
import com.work.shop.oms.common.bean.ShopPaySearchBean;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.shoppay.service.ShopPayService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.pay.hessian.client.UserHessianClient;
import com.work.shop.pay.util.GetProperties;

@Service
public class ShopPayServiceImpl implements ShopPayService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Float searchUserPayService(String userName) {
		String json = null;
		try {
			json = UserHessianClient.sent("searchUserPayService", userName);
		} catch (Exception e) {
			logger.error(userName + "查询邦付宝信息异常", e);
		}
		if (null == json) {
			return null;
		} else {
			ShopPaySearchBean bean = null;
			try {
				bean = JSON.parseObject(json, ShopPaySearchBean.class);
			} catch (Exception e) {
				logger.error(userName + "查询结果JSON转对象异常", e);
			}
			if (null != bean.getResult()) {
				return bean.getResult().getUseMoney();
			} else {
				return null;
			}
		}
	
	}

	@Override
	public ServiceReturnInfo<OcpbStatus> validateShopPay(MasterOrder masterOrder) {
		// 首先检验余额,余额大于0,且开通与邦付宝连接，先冻结此笔款项
		if (masterOrder.getSurplus() > 0) {
			// 如果没有余额验证码，则设置不需要验证码
			if (!StringUtil.isBlank(masterOrder.getSmsCode())) {
				masterOrder.setSmsFlag("1");
			}
			String userId = masterOrder.getUserId();
			// 冻结款项,如果冻结时，余额不够，则订单不能生成
			ShopPayResultBean apiBack = null;
			String message = "";
			try {
				ShopPayCashBean shopPayCashBean = new ShopPayCashBean();
				shopPayCashBean.setUserName(masterOrder.getUserId());
				shopPayCashBean.setMoney(masterOrder.getSurplus().floatValue());
				shopPayCashBean.setType(4);
				shopPayCashBean.setOrderType(6);
				shopPayCashBean.setOrderSource("13");
				shopPayCashBean.setSmsFlag(masterOrder.getSmsFlag());
				shopPayCashBean.setSmsCode(masterOrder.getSmsFlag());
				apiBack = payService(masterOrder.getUserId(), "", shopPayCashBean);
				logger.debug("bangFuBaoBack:" + JSON.toJSONString(apiBack));
			} catch (Exception e) {
				logger.error("用户ID：" + userId + "支付余额时调用接口异常", e);
				return new ServiceReturnInfo<OcpbStatus>("用户ID：" + userId + "支付余额时调用接口出错！");
			}
			if (apiBack == null) {
				logger.error("用户ID：" + userId + "支付余额时调用接口返回信息为空！");
				return new ServiceReturnInfo<OcpbStatus>("用户ID：" + userId + "支付余额时调用接口返回信息为空！");
			}
			if (apiBack.getStatus() == 2) {
				logger.error("用户ID：" + userId + apiBack.getMsg());
				return new ServiceReturnInfo<OcpbStatus>(OcpbStatus.lock);
			}
			if (apiBack != null && apiBack.getStatus() == 1) {
				String payMsg = apiBack.getMsg();
				logger.debug("bangFuBaoBack:" + payMsg);
				if (payMsg == null || payMsg.length() < 1) {
					message = "用户ID：" + userId + "账户余额不够支付！";
					return new ServiceReturnInfo<OcpbStatus>(message);
				}
				if (payMsg.contains("lock")) {
					// 余额占用失败原因是由于余额用户账户锁定导致后续会根据这个状态将订单设置为问题单
					masterOrder.setPayStatus(Short.valueOf(Constant.OI_PAY_STATUS_UNPAYED + ""));
					masterOrder.setSurplus(0.0);
					// setFreezeFlag(true);
					// 余额占用失败但是原因是由于余额用户账户锁定导致
					return new ServiceReturnInfo<OcpbStatus>(OcpbStatus.lock);
				} else {
					return new ServiceReturnInfo<OcpbStatus>("用户ID：" + userId + " 账户余额不够支付！");
				}
			}
			if (apiBack != null && apiBack.getStatus() == 0) {
				// surplusSucces = 1;
				// 余额占用成功
				return new ServiceReturnInfo<OcpbStatus>(OcpbStatus.success);
			}
		}
		// 余额没有占用成功 也不会影响后续流程
		return new ServiceReturnInfo<OcpbStatus>(OcpbStatus.ignore);
	}

	@Override
	public ShopPayResultBean payService(String userName, String masterOrderSn,
			ShopPayCashBean payCashBean) {
		if (null != userName && !StringUtil.isEmpty(userName)) {
			String json = null;
			try {
				logger.info("bgpay_url=" + GetProperties.getProperty("bgpay_url"));
				logger.info("邦付宝扣减余额：masterOrderSn:" + masterOrderSn + ";userName:" + userName
						+ ";bangFuBaoCashBean:" + JSON.toJSONString(payCashBean));
				json = UserHessianClient.sent("payService", JSON.toJSONString(payCashBean));
			} catch (Exception e) {
				logger.error("订单[" + masterOrderSn + "]调用消费余额接口异常：", e);
				ShopPayResultBean resultBean = new ShopPayResultBean();
				resultBean.setStatus(2);
				resultBean.setMsg("订单[" + masterOrderSn + "]调用消费余额接口异常：" + e.getMessage());
				return resultBean;
			}
			if (null == json) {
				ShopPayResultBean resultBean = new ShopPayResultBean();
				resultBean.setStatus(2);
				resultBean.setMsg("订单[" + masterOrderSn + "]调用消费余额接口失败：接口返回为空 ！");
				return resultBean;
			} else {
				ShopPayResultBean result = null;
				try {
					result = JSON.parseObject(json, ShopPayResultBean.class);
				} catch (Exception e) {
					logger.error("订单[" + masterOrderSn + "]调用消费余额接口返回结果转对象异常：", e);
				}
				return result;
			}
		} else {
			return null;
		}
	}

	@Override
	public ServiceReturnInfo<OcpbStatus> releaseShopPay(
			MasterOrder masterOrder, OcpbStatus ocpbStatus) {
		// 首先检验余额,余额大于0, 订单创建失败后释放冻结余额
		if (masterOrder.getSurplus() > 0) {
			// 如果没有余额验证码，则设置不需要验证码
			if (!StringUtil.isBlank(masterOrder.getSmsCode())) {
				masterOrder.setSmsFlag("1");
			}
			String userId = masterOrder.getUserId();
			// 冻结款项,如果冻结时，余额不够，则订单不能生成
			ShopPayResultBean apiBack = null;
			try {
				ShopPayCashBean shopPayCashBean = new ShopPayCashBean();
				shopPayCashBean.setUserName(masterOrder.getUserId());
				shopPayCashBean.setMoney(masterOrder.getSurplus().floatValue());
				shopPayCashBean.setType(5);
				shopPayCashBean.setOrderType(6);
				shopPayCashBean.setOrderSource("13");
				shopPayCashBean.setSmsFlag(masterOrder.getSmsFlag());
				shopPayCashBean.setSmsCode(masterOrder.getSmsFlag());
				apiBack = payService(masterOrder.getUserId(), "", shopPayCashBean);
				logger.info("ShopPayBack:" + JSON.toJSONString(apiBack));
			} catch (Exception e) {
				logger.error("用户ID：" + userId + "支付余额时调用接口异常", e);
				return new ServiceReturnInfo<OcpbStatus>("用户ID：" + userId + "支付余额时调用接口出错！");
			}
		}
		// 余额没有占用成功 也不会影响后续流程
		return new ServiceReturnInfo<OcpbStatus>(OcpbStatus.ignore);
	}

	@Override
	public ReturnInfo<String> backSurplus(String userId, float surplus,
			String returnSn, String memo) {
		logger.info("退还余额 userId：" + userId + ";returnSn:" + returnSn + ";surplus: " + surplus);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		ShopPayCashBean cash = new ShopPayCashBean();
		cash.setOrderNo(returnSn);
		cash.setUserName(userId);
		cash.setMoney(surplus);
		cash.setType(1);
		cash.setOrderType(6);
		cash.setOrderSource("13");
		cash.setSmsFlag("");
		cash.setSmsCode("");
		cash.setMemo(memo);
		ShopPayResultBean payResultBean = payService(userId, returnSn, cash);
		logger.info("backBalance.orderSurplusApiService.payService.returnSn:"+ returnSn +",surplusReturn:" + JSON.toJSONString(payResultBean) + "退surplus:" + surplus);
		if(payResultBean.getStatus().intValue() > 0){
			//失败
			info.setMessage(payResultBean.getMsg());
		}else{
			//成功
			info.setMessage("退余额成功！");
			info.setIsOk(Constant.OS_YES);
			info.setData(payResultBean.getResult());
//			OrderRefund updateRefund = new OrderRefund();
//			updateRefund.setReturnPaySn(orderRefund.getReturnPaySn());
//			updateRefund.setBackbalance(ConstantValues.YESORNO_YES.byteValue());
//			orderRefundMapper.updateByPrimaryKeySelective(updateRefund);
		}
		return info;
	}
}
