package com.work.shop.oms.rider.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.logistics.api.OrderTmsServiceAPI;
import com.work.shop.logistics.api.bean.OrderBean;
import com.work.shop.logistics.api.bean.ResultBean;
import com.work.shop.logistics.api.bean.ReturnBean;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.OrderItem;
import com.work.shop.oms.bean.OrderRiderDistributeLog;
import com.work.shop.oms.bean.OrderRiderDistributeLogExample;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.common.bean.TimeUtil;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.OrderRiderDistributeLogMapper;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.order.response.OrderQueryResponse;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderAddressInfoService;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.rider.service.RiderDistributeService;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 骑手平台派单服务
 * @author QuYachu
 *
 */
@Service("riderDistributeService")
public class RiderDistributeServiceImpl implements RiderDistributeService {
	
	private static final Logger logger = Logger.getLogger(RiderDistributeServiceImpl.class);
	
	@Resource(name="orderRiderDistributeLogMapper")
	private OrderRiderDistributeLogMapper orderRiderDistributeLogMapper;
	
	@Resource
	private MasterOrderInfoService masterOrderInfoService;
	
	@Resource
	private MasterOrderAddressInfoService masterOrderAddressInfoService;
	
	@Resource(name = "riderDistributeProcessJmsTemplate")
	private JmsTemplate riderDistributeProcessJmsTemplate;
	
	//@Resource
	private OrderTmsServiceAPI orderTmsServiceAPI;

	@Resource(name = "distributeShipService")
	private DistributeShipService distributeShipService;

	@Resource
	private MasterOrderActionService masterOrderActionService;
	
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;

	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
	
	/**
	 * 保存到骑手平台派单信息表中
	 * @param orderSnList
	 * @param orderType 1订单号、2外部交易号
	 * @return
	 */
	@Override
	public ServiceReturnInfo<Boolean> saveRiderDistributeInfoList(List<String> orderSnList, int orderType) {
		ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>(false);
		
		if (orderSnList == null || orderSnList.size() == 0) {
			serviceReturnInfo.setIsok(false);
			serviceReturnInfo.setMessage("参数列表为空");
			return serviceReturnInfo;
		}
		
		try {
			List<OrderRiderDistributeLog> logList = new ArrayList<OrderRiderDistributeLog>();
			Date nowDate = new Date();
			for (String orderSn : orderSnList) {
				OrderRiderDistributeLog model = new OrderRiderDistributeLog();
				OrderRiderDistributeLogExample distributeLogExample = new OrderRiderDistributeLogExample();
				OrderRiderDistributeLogExample.Criteria criteria = distributeLogExample.or();
				model.setAddTime(nowDate);
				model.setOrderStatus(Constant.OI_ORDER_DIS_DEFAULT);
				if (orderType == 1) {
					model.setMasterOrderSn(orderSn);
					criteria.andMasterOrderSnEqualTo(orderSn);
				} else if (orderType == 2) {
					model.setOrderOutSn(orderSn);
					criteria.andOrderOutSnEqualTo(orderSn);
				}
				// 新增或者修改
				List<OrderRiderDistributeLog> list = orderRiderDistributeLogMapper.selectByExampleWithBLOBs(distributeLogExample);
				if (list != null && list.size() > 0) {
					// 修改
					orderRiderDistributeLogMapper.updateByExampleSelective(model, distributeLogExample);
					
					OrderRiderDistributeLog searchModel = list.get(0);
					// 判断是否需要走重发接口
					Integer orderStatus = searchModel.getOrderStatus();
					if (orderStatus != null && orderStatus.intValue() == Constant.OI_ORDER_DIS_CANCEL) {
						// 订单取消状态，需要走订单重发接口
						model.setAddType(1);
					}
				} else {
					// 新增
					orderRiderDistributeLogMapper.insertSelective(model);
				}
				logList.add(model);
			}

			serviceReturnInfo.setIsok(true);
			serviceReturnInfo.setMessage("成功");
			serviceReturnInfo.setResult(true);
			
			// jms下发处理
			sendRiderDistributeListToJms(logList);
			
		} catch (Exception e) {
			logger.error("保存:" + JSONObject.toJSONString(orderSnList) + ",到骑手平台派单信息表中异常!" + e);
			serviceReturnInfo.setMessage("处理异常");
		}
		
		return serviceReturnInfo;
	}
	
	/**
	 * 将订单下发到骑手平台处理
	 * @param logList
	 */
	private void sendRiderDistributeListToJms(List<OrderRiderDistributeLog> logList) {
		for (OrderRiderDistributeLog logModel : logList) {
			try {
				sendRiderDistributeToJms(logModel);
			} catch (Exception e) {
				logger.error("下发骑手平台JMS异常!" + e + ",OrderRiderDistributeLog:" + JSONObject.toJSONString(logModel));
			}
		}
	}
	
	/**
	 * 将订单下发到骑手平台处理
	 * @param logModel
	 */
	private void sendRiderDistributeToJms(OrderRiderDistributeLog logModel) {
		String json = JSONObject.toJSONString(logModel);
		riderDistributeProcessJmsTemplate.send(new TextMessageCreator(json));
	}
	
	/**
	 * 下发订单到骑手平台
	 * @param logModel
	 * @return
	 */
	@Override
	public ServiceReturnInfo<Boolean> sendRiderDistributeByUser(OrderRiderDistributeLog logModel) {
		ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>();
		serviceReturnInfo.setIsok(false);
		if (logModel == null) {
			serviceReturnInfo.setMessage("配送订单信息为空");
			return serviceReturnInfo;
		}
		
		if (StringUtils.isBlank(logModel.getMasterOrderSn())) {
			serviceReturnInfo.setMessage("配送订单编码为空");
			return serviceReturnInfo;
		}
		
		OrderRiderDistributeLog selectModel = getModel(logModel);
		if (selectModel != null) {
			Integer orderStatus = selectModel.getOrderStatus();
			if (orderStatus != null) {
				if (orderStatus != Constant.OI_ORDER_DIS_DEFAULT && orderStatus != Constant.OI_ORDER_DIS_CANCEL 
						&& orderStatus != Constant.OI_ORDER_PLATFORM_CANCEL) {
					serviceReturnInfo.setMessage("订单已下发配送,不能再次下发");
					return serviceReturnInfo;
				}
			}
			
			// 重新配送
			logModel.setAddType(1);
		}
		
		selectModel.setActionUser(logModel.getActionUser());
		return sendRiderDistribute(selectModel);
	}
	
	/**
	 * 下发订单到骑手平台
	 * @param logModel
	 * @return
	 */
	@Override
	public ServiceReturnInfo<Boolean> sendRiderDistribute(OrderRiderDistributeLog logModel) {
		ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>();
		serviceReturnInfo.setIsok(false);
		if (logModel == null) {
			serviceReturnInfo.setMessage("配送订单信息为空");
			return serviceReturnInfo;
		}
		OrderRiderDistributeLog updateLogModel = getRiderDistOrder(logModel);
		ServiceReturnInfo<ResultBean> result = sendRiderDistributeData(updateLogModel);
		
		String message = "配送下发失败";
		if (result != null) {
			boolean isOk = result.isIsok();
			if (isOk) {
				message = "配送下发成功";

				// 订单状态 骑手平台接单
				updateLogModel.setOrderStatus(Constant.OI_ORDER_PLATFORM_ACCEPT);
				updateLogModel.setDistributeMsg("配送下发成功");
				
				ResultBean resultBean = result.getResult();
				// 发单时间
				updateLogModel.setCreateTime(new Date());
				// 配送距离
				updateLogModel.setDistance(resultBean.getDistance());
				// 运费
				updateLogModel.setDeliveryFee(resultBean.getDeliverFee());
				// 优惠券
				updateLogModel.setCouponFee(resultBean.getCouponFee());
				// 实际支付运费
				updateLogModel.setActualFee(resultBean.getFee());
				// 骑手平台编码
				updateLogModel.setTmsCode(resultBean.getTmsCode());
				// 骑手平台名称
				updateLogModel.setTmsName(resultBean.getTmsName());
			} else {
				updateLogModel.setDistributeMsg(result.getMessage());
				if (updateLogModel.getOrderStatus() != null) {
					// 发单时间
					updateLogModel.setCreateTime(new Date());
				}
				message = "配送下发失败:" + result.getMessage();
			}
			try {
				OrderRiderDistributeLogExample distributeLogExample = new OrderRiderDistributeLogExample();
				OrderRiderDistributeLogExample.Criteria criteria = distributeLogExample.or();
				if (StringUtil.isNotEmpty(logModel.getMasterOrderSn())) {
					criteria.andMasterOrderSnEqualTo(logModel.getMasterOrderSn());
				} else if (StringUtil.isNotEmpty(logModel.getOrderOutSn())) {
					criteria.andOrderOutSnEqualTo(logModel.getOrderOutSn());
				}
				orderRiderDistributeLogMapper.updateByExampleWithBLOBs(updateLogModel, distributeLogExample);
				serviceReturnInfo.setIsok(true);
				serviceReturnInfo.setMessage("下发成功");
			} catch (Exception e) {
				logger.error("更新配送日志失败!" + e.getMessage(), e);
				serviceReturnInfo.setMessage("更新配送日志失败!");
			}
		} 
		
		masterOrderActionService.insertOrderActionBySn(logModel.getMasterOrderSn(), message, logModel.getActionUser());
		return serviceReturnInfo;
	}
	
	/**
	 * 订单配送取消
	 * @param logModel
	 * @return
	 */
	@Override
	public ServiceReturnInfo<Boolean> sendOrderRiderCancel(OrderRiderDistributeLog logModel) {
		ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>();
		serviceReturnInfo.setIsok(false);
		if (logModel == null) {
			serviceReturnInfo.setMessage("配送订单信息为空");
			return serviceReturnInfo;
		}
		
		String masterOrderSn = logModel.getMasterOrderSn();
		if (StringUtils.isBlank(masterOrderSn)) {
			serviceReturnInfo.setMessage("订单编码为空");
			return serviceReturnInfo;
		}
		
		// 操作用户
		String actionUser = logModel.getActionUser();
		
		OrderRiderDistributeLog selectModel = getModel(logModel);
		if (selectModel == null) {
			serviceReturnInfo.setMessage("订单:" + masterOrderSn + "没有配送记录");
			return serviceReturnInfo;
		}
		
		Integer orderStatus = selectModel.getOrderStatus();
		if (orderStatus != null) {
			
			if (orderStatus == 1) {
				serviceReturnInfo.setMessage("订单:" + masterOrderSn + "配送中");
				return serviceReturnInfo;
			}
			
			if (orderStatus == 2) {
				serviceReturnInfo.setMessage("订单:" + masterOrderSn + "配送完成");
				return serviceReturnInfo;
			}
			
			if (orderStatus == 3) {
				serviceReturnInfo.setMessage("订单:" + masterOrderSn + "配送平台已取消");
				return serviceReturnInfo;
			}
			
			if (orderStatus == 6) {
				serviceReturnInfo.setMessage("订单:" + masterOrderSn + "配送已通知取消");
				return serviceReturnInfo;
			}
		}
		
		MasterOrderInfo masterOrderInfo = masterOrderInfoService.getOrderInfoBySn(masterOrderSn);
		if (masterOrderInfo == null) {
			serviceReturnInfo.setMessage("订单:" + masterOrderSn + "不存在");
			return serviceReturnInfo;
		}
		
		String channelCode = masterOrderInfo.getChannelCode();
		String shopCode = masterOrderInfo.getOrderFrom();
		OrderBean orderBean = new OrderBean();
		orderBean.setOrderNo(masterOrderSn);
		orderBean.setCancelReason(logModel.getStatusMsg());
		orderBean.setChannelCode(channelCode);
		orderBean.setShopCode(shopCode);
		
		String message = "取消配送失败";
		try {
			ReturnBean<ResultBean> returnBean = cancelTmsOrder(orderBean);
			if (returnBean == null) {
				serviceReturnInfo.setMessage("订单:" + masterOrderSn + "取消配送下发失败");
				return serviceReturnInfo;
			}
			
			if ("1".equals(returnBean.getCode())) {
				// 配送单状态变更
				OrderRiderDistributeLog updateDistLog = new OrderRiderDistributeLog();
				updateDistLog.setLogId(selectModel.getLogId());
				updateDistLog.setOrderStatus(Constant.OI_ORDER_PLATFORM_CANCEL);
				updateDistLog.setCancelTime(new Date());
				orderRiderDistributeLogMapper.updateByPrimaryKeySelective(updateDistLog);
				
				serviceReturnInfo.setIsok(true);
				serviceReturnInfo.setResult(true);
				serviceReturnInfo.setMessage("订单:" + masterOrderSn + "取消配送成功");
				message = "取消配送成功";
			} else {
				serviceReturnInfo.setIsok(false);
				serviceReturnInfo.setResult(false);
				serviceReturnInfo.setMessage("订单:" + masterOrderSn + "取消配送失败," + returnBean.getMsg());
				message = "," + returnBean.getMsg();
			}
		} catch (Exception e) {
			logger.error("订单:" + masterOrderSn + "取消配送异常", e);
			message = "取消配送异常:" + e.getMessage();
		} finally {
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, message, actionUser);
		}
		
		return serviceReturnInfo;
	}
	
	/**
	 * 处理订单数据并下发到骑手平台
	 * @param logModel
	 * @return
	 */
	public ServiceReturnInfo<ResultBean> sendRiderDistributeData(OrderRiderDistributeLog logModel) {
		ServiceReturnInfo<ResultBean> serviceReturnInfo = new ServiceReturnInfo<ResultBean>();
		serviceReturnInfo.setIsok(false);
		try {
			// 查询订单信息
			String masterOrderSn = logModel.getMasterOrderSn();
			String orderOutSn = logModel.getOrderOutSn();
			
			MasterOrderInfo masterOrderInfo = null;
			if (StringUtils.isNotBlank(masterOrderSn)) {
				masterOrderInfo = masterOrderInfoService.getOrderInfoBySn(masterOrderSn);
			} else if (StringUtils.isNotBlank(orderOutSn)) {
				masterOrderInfo = masterOrderInfoService.getOrderInfoByOutOrderSn(orderOutSn);
			}
			
			if (masterOrderInfo == null) {
				serviceReturnInfo.setMessage("通过订单号:" + masterOrderSn + "或者外部交易号:" + orderOutSn + "没有查询到对应的订单信息");
				return serviceReturnInfo;
			}
			
			// 判断订单的状态、订单是否已付款、是否已发货
			String orderSn = masterOrderInfo.getMasterOrderSn();
			String outSn = masterOrderInfo.getOrderOutSn();
			// 订单总金额
			Double orderMoney = masterOrderInfo.getTotalFee().doubleValue();
			String channelCode = masterOrderInfo.getChannelCode();
			String shopCode = masterOrderInfo.getOrderFrom();
			
			// 用户地址信息
			MasterOrderAddressInfo masterOrderAddressInfo = masterOrderAddressInfoService.selectAddressInfo(orderSn);
			if (masterOrderAddressInfo == null) {
				serviceReturnInfo.setMessage("通过订单号:" + orderSn + "没有查询到对应的订单地址信息");
				return serviceReturnInfo;
			}
			MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			if (extend == null) {
				serviceReturnInfo.setMessage("通过订单号:" + orderSn + "没有查询到对应的订单扩展信息");
				return serviceReturnInfo;
			}

			logModel.setMasterOrderSn(orderSn);
			logModel.setOrderOutSn(outSn);
			// 渠道店铺编码
			logModel.setChannelCode(shopCode);

			// 收货人
			String userName = masterOrderAddressInfo.getConsignee();
			// 手机号码
			String mobile = masterOrderAddressInfo.getMobile();
			// 电话号码
			String tel = masterOrderAddressInfo.getTel();
			// 用户地址
			String userAddress = masterOrderAddressInfo.getAddress();
			
			OrderBean orderBean = new OrderBean();
			// 订单编码
			orderBean.setOrderNo(orderSn);
			// 订单金额
			orderBean.setOrderPrice(orderMoney);
			// 渠道编码
			orderBean.setChannelCode(channelCode);
			// 店铺编码
			orderBean.setShopCode(shopCode);
			// 用户名
			orderBean.setUserName(userName);
			// 用户地址
			orderBean.setUserAddress(userAddress);
			// 手机号码
			orderBean.setUserMobile(mobile);
			// 电话号码
			orderBean.setUserTel(tel);
			orderBean.setLatitude(masterOrderAddressInfo.getLatitude());
			orderBean.setLongitude(masterOrderAddressInfo.getLongitude());
			// 配送类型 0新增订单/1重新配送订单
			orderBean.setAddType(logModel.getAddType());
			
			ReturnBean<ResultBean> returnBean = sendToTmsService(orderBean);
			
			if (returnBean != null) {
				String code = returnBean.getCode();
				if (Constant.RIDER_CODE_SUCESS.equals(code)) {
					serviceReturnInfo.setIsok(true);
					serviceReturnInfo.setResult(returnBean.getResult());
				} else if (Constant.RIDER_CODE_FAIL.equals(code)) {
					serviceReturnInfo.setMessage(returnBean.getMsg());
				} else if (Constant.RIDER_CODE_ERROR.equals(code)) {
					// 下发到了骑手平台，返回有问题
					serviceReturnInfo.setMessage(returnBean.getMsg());
					// 使用配送取消状态
					logModel.setOrderStatus(Constant.OI_ORDER_DIS_CANCEL); 
				}
			}
		} catch (Exception e) {
			serviceReturnInfo.setMessage("数据处理异常");
			logger.error("处理:" + JSONObject.toJSONString(logModel) + "异常!" + e);
		}
		
		return serviceReturnInfo;
	}
	
	@Override
	public OrderQueryResponse riderDistGet(OrderQueryRequest request) {
		logger.info("骑手配送订单查询 logModel：" + JSON.toJSONString(request));
		OrderQueryResponse response = new OrderQueryResponse();
		response.setSuccess(false);
		response.setMessage("骑手配送订单查询失败");
		try {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			// 自提时间范围
			String startTime = request.getStartTime();
			String endTime = request.getEndTime();
			
			if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
				Date date = com.work.shop.oms.utils.TimeUtil.getBeforeDay(-1);
				String dateTime = TimeUtil.format2Date(date);
				startTime = dateTime + " 00:00:00";
				endTime = dateTime + " 23:59:59";
			}
			
			if (StringUtils.isNotBlank(startTime)) {
				queryMap.put("startTime", startTime);
			}
			
			if (StringUtils.isNotBlank(endTime)) {
				queryMap.put("endTime", endTime);
			}
			// 线下店铺编码
			String storeCode = request.getStoreCode();
			if (StringUtils.isNotBlank(storeCode)) {
				queryMap.put("storeCode", storeCode);
			}
			
			List<OrderItem> queryOrderItems = orderDistributeDefineMapper.getRiderDistOrder(queryMap);
			List<OrderItem> orderItems = new ArrayList<OrderItem>();
			if (StringUtil.isListNotNull(queryOrderItems)) {
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				String systemTime = df.format(new Date());
				for (OrderItem item : queryOrderItems) {
					String deliveryTime = item.getDeliveryTime();
					if (StringUtil.isTrimEmpty(deliveryTime)) {
						//continue;
						// 没有选择配送时间，默认直接配送
						orderItems.add(item);
					} else {
						try {
							// 根据时段筛选
							String[] timeStr = deliveryTime.split("-");
							String minTime = timeStr[0];
							String maxTime = timeStr[1];
							if (systemTime.compareTo(minTime) >= 0 && systemTime.compareTo(maxTime) <= 0) {
								// 配送时间在改范围内
								orderItems.add(item);
							}
						} catch (Exception e) {
							logger.error("比对配送时间异常" + e.getMessage(), e);
						}
					}
				}
			}
			
			response.setSuccess(true);
			response.setMessage("骑手配送订单查询成功");
			response.setOrderItems(orderItems);
		} catch (Exception e) {
			logger.error("骑手配送订单查询失败" + e.getMessage(), e);
			response.setMessage("骑手配送订单查询失败" + e.getMessage());
		}
		return response;
	}
	
	/**
	 * 配送单发货接单(骑手配送接单)
	 * @param logModel
	 * @return
	 */
	@Override
	public ServiceReturnInfo<Boolean> riderDistAcceptOrders(OrderRiderDistributeLog logModel) {
		logger.info("骑手配送接单回写logModel：" + JSON.toJSONString(logModel));
		ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>();
		serviceReturnInfo.setIsok(false);
		if (logModel == null) {
			serviceReturnInfo.setMessage("骑手配送接单回写失败");
			return serviceReturnInfo;
		}
		String orderSn = logModel.getMasterOrderSn();
		String orderOutSn = logModel.getOrderOutSn();
		if (StringUtil.isTrimEmpty(orderSn) && StringUtil.isTrimEmpty(orderOutSn)) {
			serviceReturnInfo.setMessage("骑手配送接单回写失败:订单号或外部订单号不能都为空");
			return serviceReturnInfo;
		}
		try {
			OrderRiderDistributeLogExample queryExample = new OrderRiderDistributeLogExample();
			OrderRiderDistributeLogExample.Criteria criteria = queryExample.or();
			if (StringUtil.isNotEmpty(orderSn)) {
				criteria.andMasterOrderSnEqualTo(orderSn);
			} else if (StringUtil.isNotEmpty(orderOutSn)) {
				criteria.andOrderOutSnEqualTo(orderOutSn);
			}
			List<OrderRiderDistributeLog> distributeLogs = orderRiderDistributeLogMapper.selectByExample(queryExample);
			if (StringUtil.isListNull(distributeLogs)) {
				serviceReturnInfo.setMessage("骑手配送接单回写失败:订单号" + orderSn + "不能查找到配送单");
				return serviceReturnInfo;
			}
			// 配送单状态变更
			OrderRiderDistributeLog updateDistLog = new OrderRiderDistributeLog();
			updateDistLog.setLogId(distributeLogs.get(0).getLogId());
			updateDistLog.setAcceptTime(logModel.getAcceptTime());
			updateDistLog.setOrderStatus(Constant.OI_ORDER_DIS_ACCEPT);
			if (StringUtil.isNotEmpty(logModel.getDistributeMsg())) {
				updateDistLog.setDistributeMsg(logModel.getDistributeMsg());
			}
			// 骑手订单号
			if (StringUtils.isNotBlank(logModel.getClientOrderSn())) {
				updateDistLog.setClientOrderSn(logModel.getClientOrderSn());
			}
			orderRiderDistributeLogMapper.updateByPrimaryKeySelective(updateDistLog);
			serviceReturnInfo.setMessage("骑手配送接单回写成功");
			serviceReturnInfo.setIsok(true);
		} catch (Exception e) {
			logger.error("骑手配送接单回写异常" + e.getMessage(), e);
			serviceReturnInfo.setMessage("骑手配送接单回写异常" + e.getMessage());
		} finally {
			logger.info("骑手配送接单回写serviceReturnInfo：" + JSON.toJSONString(serviceReturnInfo));
		}
		return serviceReturnInfo;
	}
	
	/**
	 * 配送单取单
	 * @param logModel
	 * @return
	 */
	@Override
	public ServiceReturnInfo<Boolean> riderDistTakeOrders(OrderRiderDistributeLog logModel) {
		logger.info("骑手配送取单回写logModel：" + JSON.toJSONString(logModel));
		ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>();
		serviceReturnInfo.setIsok(false);
		if (logModel == null) {
			serviceReturnInfo.setMessage("骑手配送取单回写失败");
			return serviceReturnInfo;
		}
		String orderSn = logModel.getMasterOrderSn();
		String orderOutSn = logModel.getOrderOutSn();
		if (StringUtil.isTrimEmpty(orderSn) && StringUtil.isTrimEmpty(orderOutSn)) {
			serviceReturnInfo.setMessage("骑手配送取单回写失败:订单号或外部订单号不能都为空");
			return serviceReturnInfo;
		}
		try {
			OrderRiderDistributeLogExample queryExample = new OrderRiderDistributeLogExample();
			OrderRiderDistributeLogExample.Criteria criteria = queryExample.or();
			if (StringUtil.isNotEmpty(orderSn)) {
				criteria.andMasterOrderSnEqualTo(orderSn);
			} else if (StringUtil.isNotEmpty(orderOutSn)) {
				criteria.andOrderOutSnEqualTo(orderOutSn);
			}
			List<OrderRiderDistributeLog> distributeLogs = orderRiderDistributeLogMapper.selectByExample(queryExample);
			if (StringUtil.isListNull(distributeLogs)) {
				serviceReturnInfo.setMessage("骑手配送取单回写失败:订单号" + orderSn + "不能查找到配送单");
				return serviceReturnInfo;
			}
			// 配送单状态变更
			OrderRiderDistributeLog updateDistLog = new OrderRiderDistributeLog();
			updateDistLog.setLogId(distributeLogs.get(0).getLogId());
			updateDistLog.setOrderStatus(Constant.OI_ORDER_DIS_DOING);
			updateDistLog.setFetchTime(logModel.getFetchTime());
			if (StringUtil.isNotEmpty(logModel.getDistributeMsg())) {
				updateDistLog.setDistributeMsg(logModel.getDistributeMsg());
			}
			// 骑手订单号
			if (StringUtils.isNotBlank(logModel.getClientOrderSn())) {
				updateDistLog.setClientOrderSn(logModel.getClientOrderSn());
			}
			orderRiderDistributeLogMapper.updateByPrimaryKeySelective(updateDistLog);
			// 订单发货状态变更
			DistributeShippingBean distributeShipBean = new DistributeShippingBean();
			distributeShipBean.setOrderSn(orderSn);
			distributeShipBean.setShipDate(new Date());
			distributeShipBean.setShippingCode(Constant.TASK_OUT);
			// 配送订单号
			distributeShipBean.setInvoiceNo(logModel.getClientOrderSn());
			ReturnInfo<String> shipInfo = distributeShipService.processShip(distributeShipBean, false);
			if (shipInfo.getIsOk() == Constant.OS_NO) {
				serviceReturnInfo.setMessage("骑手配送取单回写异常:" + shipInfo.getMessage());
			} else {
				serviceReturnInfo.setMessage("骑手配送取单回写成功");
				serviceReturnInfo.setIsok(true);
			}
		} catch (Exception e) {
			logger.error("骑手配送取单回写异常" + e.getMessage(), e);
			serviceReturnInfo.setMessage("骑手配送取单回写异常" + e.getMessage());
		} finally {
			logger.info("骑手配送取单回写serviceReturnInfo：" + JSON.toJSONString(serviceReturnInfo));
		}
		return serviceReturnInfo;
	}

	/**
	 * 骑手配送完成
	 * @param logModel
	 * @return
	 */
	@Override
	public ServiceReturnInfo<Boolean> riderDistCompleted(OrderRiderDistributeLog logModel) {
		logger.info("骑手配送完成回写logModel：" + JSON.toJSONString(logModel));
		ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>();
		serviceReturnInfo.setIsok(false);
		if (logModel == null) {
			serviceReturnInfo.setMessage("骑手配送完成回写失败");
			return serviceReturnInfo;
		}
		String orderSn = logModel.getMasterOrderSn();
		String orderOutSn = logModel.getOrderOutSn();
		if (StringUtil.isTrimEmpty(orderSn) && StringUtil.isTrimEmpty(orderOutSn)) {
			serviceReturnInfo.setMessage("骑手配送完成回写失败:订单号或外部订单号不能都为空");
			return serviceReturnInfo;
		}
		try {
			OrderRiderDistributeLogExample queryExample = new OrderRiderDistributeLogExample();
			OrderRiderDistributeLogExample.Criteria criteria = queryExample.or();
			if (StringUtil.isNotEmpty(orderSn)) {
				criteria.andMasterOrderSnEqualTo(orderSn);
			} else if (StringUtil.isNotEmpty(orderOutSn)) {
				criteria.andOrderOutSnEqualTo(orderOutSn);
			}
			List<OrderRiderDistributeLog> distributeLogs = orderRiderDistributeLogMapper.selectByExample(queryExample);
			if (StringUtil.isListNull(distributeLogs)) {
				serviceReturnInfo.setMessage("骑手配送完成回写失败:订单号" + orderSn + "不能查找到配送单");
				return serviceReturnInfo;
			}
			// 配送单状态变更
			OrderRiderDistributeLog updateDistLog = new OrderRiderDistributeLog();
			updateDistLog.setLogId(distributeLogs.get(0).getLogId());
			updateDistLog.setOrderStatus(Constant.OI_ORDER_DIS_DONE);
			updateDistLog.setFinishTime(logModel.getFinishTime());
			if (StringUtil.isNotEmpty(logModel.getDistributeMsg())) {
				updateDistLog.setDistributeMsg(logModel.getDistributeMsg());
			}
			if (StringUtils.isNotBlank(logModel.getClientOrderSn())) {
				updateDistLog.setClientOrderSn(logModel.getClientOrderSn());
			}
			orderRiderDistributeLogMapper.updateByPrimaryKeySelective(updateDistLog);
			DistributeShippingBean distributeShipBean = new DistributeShippingBean();
			distributeShipBean.setOrderSn(orderSn);
			distributeShipBean.setShipDate(new Date());
			distributeShipBean.setShippingCode(Constant.TASK_OUT);
			distributeShipBean.setInvoiceNo(logModel.getClientOrderSn());
			ReturnInfo<String> shipInfo = distributeShipService.distOrderConfirm(distributeShipBean);
			if (shipInfo.getIsOk() == Constant.OS_NO) {
				serviceReturnInfo.setMessage("骑手配送完成回写异常:" + shipInfo.getMessage());
			} else {
				serviceReturnInfo.setMessage("骑手配送完成回写成功");
				serviceReturnInfo.setIsok(true);
			}
		} catch (Exception e) {
			logger.error("骑手配送完成回写异常" + e.getMessage(), e);
			serviceReturnInfo.setMessage("骑手配送取单回写异常" + e.getMessage());
		} finally {
			logger.info("骑手配送完成回写serviceReturnInfo：" + JSON.toJSONString(serviceReturnInfo));
		}
		return serviceReturnInfo;
	}
	
	/**
	 * 骑手配送取消
	 * @param logModel
	 * @return
	 */
	@Override
	public ServiceReturnInfo<Boolean> riderDistCancel(OrderRiderDistributeLog logModel) {
		logger.info("骑手配送取消回写logModel：" + JSON.toJSONString(logModel));
		ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>();
		serviceReturnInfo.setIsok(false);
		if (logModel == null) {
			serviceReturnInfo.setMessage("骑手配送取消回写失败");
			return serviceReturnInfo;
		}
		String orderSn = logModel.getMasterOrderSn();
		String orderOutSn = logModel.getOrderOutSn();
		if (StringUtil.isTrimEmpty(orderSn) && StringUtil.isTrimEmpty(orderOutSn)) {
			serviceReturnInfo.setMessage("骑手配送取消回写失败:订单号或外部订单号不能都为空");
			return serviceReturnInfo;
		}
		try {
			OrderRiderDistributeLogExample queryExample = new OrderRiderDistributeLogExample();
			OrderRiderDistributeLogExample.Criteria criteria = queryExample.or();
			if (StringUtil.isNotEmpty(orderSn)) {
				criteria.andMasterOrderSnEqualTo(orderSn);
			} else if (StringUtil.isNotEmpty(orderOutSn)) {
				criteria.andOrderOutSnEqualTo(orderOutSn);
			}
			List<OrderRiderDistributeLog> distributeLogs = orderRiderDistributeLogMapper.selectByExample(queryExample);
			if (StringUtil.isListNull(distributeLogs)) {
				serviceReturnInfo.setMessage("骑手配送取消回写失败:订单号" + orderSn + "不能查找到配送单");
				return serviceReturnInfo;
			}
			// 配送单状态变更
			OrderRiderDistributeLog updateDistLog = new OrderRiderDistributeLog();
			updateDistLog.setLogId(distributeLogs.get(0).getLogId());
			// 配送取消
			updateDistLog.setOrderStatus(Constant.OI_ORDER_DIS_CANCEL);
			// 取消时间
			updateDistLog.setCancelTime(logModel.getCancelTime());
			if (StringUtil.isNotEmpty(logModel.getStatusMsg())) {
				// 订单状态(异常单)
				updateDistLog.setStatusMsg(logModel.getStatusMsg());
			}
			if (StringUtil.isNotEmpty(logModel.getDistributeMsg())) {
				updateDistLog.setDistributeMsg(logModel.getDistributeMsg());
			}
			// 骑手平台订单号
			if (StringUtils.isNotBlank(logModel.getClientOrderSn())) {
				updateDistLog.setClientOrderSn(logModel.getClientOrderSn());
			}
			orderRiderDistributeLogMapper.updateByPrimaryKeySelective(updateDistLog);
			
			serviceReturnInfo.setMessage("骑手配送取消回写成功");
			serviceReturnInfo.setIsok(true);
		} catch (Exception e) {
			logger.error("骑手配送取消回写异常" + e.getMessage(), e);
			serviceReturnInfo.setMessage("骑手配送取消回写异常" + e.getMessage());
		} finally {
			logger.info("骑手配送取消回写serviceReturnInfo：" + JSON.toJSONString(serviceReturnInfo));
		}
		return serviceReturnInfo;
	}

	/**
	 * 订单下发到tms服务
	 * @param orderBean
	 * @return
	 */
	private ReturnBean<ResultBean> sendToTmsService(OrderBean orderBean) {
		ReturnBean<ResultBean> returnBean = orderTmsServiceAPI.addOrder(orderBean);
		return returnBean;
	}
	
	/**
	 * 订单取消tms
	 * @param orderBean
	 * @return
	 */
	private ReturnBean<ResultBean> cancelTmsOrder(OrderBean orderBean) {
		ReturnBean<ResultBean> returnBean = orderTmsServiceAPI.cancelOrder(orderBean);
		return returnBean;
	}
	
	/**
	 * 获取骑手配送日志
	 * @param logModel
	 * @return
	 */
	@Override
	public OrderRiderDistributeLog getModel(OrderRiderDistributeLog logModel) {
		OrderRiderDistributeLogExample distributeLogExample = new OrderRiderDistributeLogExample();
		OrderRiderDistributeLogExample.Criteria criteria = distributeLogExample.or();
		criteria.andMasterOrderSnEqualTo(logModel.getMasterOrderSn());
		
		List<OrderRiderDistributeLog> list = orderRiderDistributeLogMapper.selectByExampleWithBLOBs(distributeLogExample);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	private OrderRiderDistributeLog getRiderDistOrder(OrderRiderDistributeLog logModel) {
		OrderRiderDistributeLog distributeLog = new OrderRiderDistributeLog();
		Date newDate = new Date();
		OrderRiderDistributeLogExample distributeLogExample = new OrderRiderDistributeLogExample();
		OrderRiderDistributeLogExample.Criteria criteria = distributeLogExample.or();
		OrderRiderDistributeLog insertLog = new OrderRiderDistributeLog();
		insertLog.setAddTime(newDate);
		insertLog.setOrderStatus(Constant.OI_ORDER_DIS_DEFAULT);
		if (StringUtil.isNotEmpty(logModel.getMasterOrderSn())) {
			insertLog.setMasterOrderSn(logModel.getMasterOrderSn());
			criteria.andMasterOrderSnEqualTo(logModel.getMasterOrderSn());
		} else if (StringUtil.isNotEmpty(logModel.getOrderOutSn())) {
			insertLog.setOrderOutSn(logModel.getOrderOutSn());
			criteria.andOrderOutSnEqualTo(logModel.getOrderOutSn());
		}
		// 新增
		List<OrderRiderDistributeLog> list = orderRiderDistributeLogMapper.selectByExampleWithBLOBs(distributeLogExample);
		if (StringUtil.isListNull(list)) {
			orderRiderDistributeLogMapper.insertSelective(insertLog);
			list = orderRiderDistributeLogMapper.selectByExampleWithBLOBs(distributeLogExample);
			if (StringUtil.isListNotNull(list)) {
				distributeLog = list.get(0);
			}
		} else {
			distributeLog = list.get(0);
		}
		return distributeLog;
	}
	
	/**
	 * 查询配送订单记录总数
	 * @param param
	 * @return
	 */
	@Override
	public int getRiderDistributeCount(Map<String, Object> param) {
		return orderRiderDistributeLogMapper.getOrderRiderCount(param);
	}
	
	/**
	 * 查询配送订单记录列表
	 * @param param
	 * @return
	 */
	@Override
	public List<OrderRiderDistributeLog> getOrderRiderList(Map<String, Object> param) {
		return orderRiderDistributeLogMapper.getOrderRiderList(param);
	}
}
