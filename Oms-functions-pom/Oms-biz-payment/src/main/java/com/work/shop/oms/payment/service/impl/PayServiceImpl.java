package com.work.shop.oms.payment.service.impl;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.config.service.OrderPeriodDetailService;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.feign.OrderManagementService;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.response.OrderManagementResponse;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.payment.service.PayService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 后台支付服务接口
 * @author QuYachu
 */
@Service
public class PayServiceImpl implements PayService {
	
	Logger logger = Logger.getLogger(PayServiceImpl.class);

	@Resource
	SystemPaymentMapper  systemPaymentMapper;
	@Resource
	MasterOrderPayMapper masterOrderPayMapper;
	@Resource
	MergeOrderPayMapper mergeOrderPayMapper;
	@Resource
	MasterOrderInfoMapper masterOrderInfoMapper;
	//@Resource
//	OrderConfirmService orderConfirmService;
	@Resource
	MasterOrderPayBackMapper masterOrderPayBackMapper; 
	@Resource
	private RedisClient redisClient;
	@Resource
	private OrderPeriodDetailService orderPeriodDetailService;
	@Resource
	private MasterOrderActionService masterOrderActionService;
	@Resource(name = "orderStockProviderJmsTemplate")
	private JmsTemplate orderStockJmsTemplate;
	@Resource
	OrderManagementService orderManagementService;

	public PayServiceImpl() {
		System.out.println("PayServiceImpl init....");
	}

	/**
	 * 创建支付单
	 */
	public ReturnInfo<List<String>> createMasterPay(String masterOrderSn, List<MasterPay> masterPayList) {
		logger.info("开始创建订单支付单：" + masterOrderSn + "，支付单" + JSON.toJSONString(masterPayList));
		ReturnInfo<List<String>> returnInfo = new ReturnInfo<List<String>>(); 
		List<String> orderPays = new ArrayList<String>();
		try {
			if (StringUtil.isNull(masterOrderSn)) {
				throw new Exception("创建支付单masterOrderSn为null！");
			}
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn); 
			if (masterOrderInfo == null){
				throw new Exception("没有找到对应订单！");
			}
			if (null == masterPayList || masterPayList.size() == 0) {
				throw new Exception("创建支付单masterPayParamList为null！");
			}
			int i = 1;
			for (MasterPay masterPay : masterPayList) {
				//默认支付方式支付宝
				if (StringUtil.isNull(masterPay.getPayCode()) && masterPay.getPayId() == 0) {
					masterPay.setPayCode(Constant.OP_PAY_CODE_ALIPAY);
				}
				SystemPayment sp = new SystemPayment();
				if (masterPay.getPayId() > 0) {
					sp = systemPaymentMapper.selectByPrimaryKey(masterPay.getPayId().byteValue());
				} else {
					sp = selectSystemPaymentByCode(masterPay.getPayCode());
				}
				if (null == sp) {
					throw new Exception("创建支付单未找到支付方式：PayCode" + masterPay.getPayCode());
				}
				MasterOrderPay masterOrderPay = new MasterOrderPay();
				masterOrderPay.setCreateTime(new Date());
				if (masterPay.getPayTime() != null){
					masterOrderPay.setPayTime(masterPay.getPayTime());
				}
				masterOrderPay.setMasterOrderSn(masterOrderSn);
				// 支付单金额
				masterOrderPay.setPayTotalfee(new BigDecimal(masterPay.getPayTotalFee()));
				if (null != masterPay.getPayStatus()) {
					masterOrderPay.setPayStatus(masterPay.getPayStatus().byteValue());
				} else {
					masterOrderPay.setPayStatus((byte)Constant.OP_PAY_STATUS_UNPAYED);
				}
				if (StringUtil.isTrimEmpty(masterPay.getPayNote())) {
					masterOrderPay.setPayNote("");
				} else {
					masterOrderPay.setPayNote(masterPay.getPayNote());
				}
				masterOrderPay.setPayId(sp.getPayId());
				masterOrderPay.setPayName(sp.getPayName());
				String maxPaySn = getMasterOrderPayMaxSn(masterOrderSn);
				if (null == maxPaySn) {
					String paySn = Constant.OP_BEGIN_WITH_FK + masterOrderSn.trim() + genCode(i++, 2);
					masterOrderPay.setMasterPaySn(paySn);
				} else {
					Long maxPaySnNum = Long.parseLong(maxPaySn.substring(2));
					maxPaySnNum = maxPaySnNum + 1;
					String newPaySn = Constant.OP_BEGIN_WITH_FK + maxPaySnNum.toString();
					masterOrderPay.setMasterPaySn(newPaySn);
				}
				// 付款最后期限设置
				int sec_p = 0;
				String secpValue = null;
				int pe = 1;
				try {
					secpValue = redisClient.get("OS_PAYMENT_PERIOD_" + pe);
				} catch (Throwable e) {
					logger.error("redis is null error ," + e);
				}
				if (secpValue != null) {
					sec_p = new Integer(secpValue);
				} else {
					sec_p = getPeriodDetailValue(pe, Constant.OS_PAYMENT_PERIOD);
					try {
						redisClient.set("OS_PAYMENT_PERIOD_" + pe, sec_p + "");
					} catch (Throwable e) {
						logger.error("redis is null error ," + e);
					}
				}
				if (sec_p != 0) {
					masterOrderPay.setPayLasttime(getDate(new Date(), sec_p));									// 付款最后期限
				}
				//如果是创建未支付支付单则备份并删除未支付支付单
				if (masterPay.getPayStatus() == Constant.OP_PAY_STATUS_UNPAYED) {
					MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
					masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn).andPayStatusEqualTo((byte)Constant.OP_PAY_STATUS_UNPAYED);
					List<MasterOrderPay> unPayMasterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
					if (unPayMasterOrderPayList != null && unPayMasterOrderPayList.size() >= 1) {
						for (MasterOrderPay unPayMasterOrderPay : unPayMasterOrderPayList) {
							MasterOrderPayBack masterOrderPayBack = new MasterOrderPayBack();
							PropertyUtils.copyProperties(masterOrderPayBack, unPayMasterOrderPay);
							masterOrderPayBackMapper.deleteByPrimaryKey(unPayMasterOrderPay.getMasterPaySn());
							masterOrderPayBackMapper.insertSelective(masterOrderPayBack);
							masterOrderPayMapper.deleteByPrimaryKey(unPayMasterOrderPay.getMasterPaySn());
							masterOrderActionService.insertOrderActionBySn(masterOrderSn, "创建支付单时备份删除未支付支付单" + unPayMasterOrderPay.getMasterPaySn(), Constant.OS_STRING_SYSTEM);
						}
					}
				} 
				//如果是创建未确认支付单则备份并删除未确认支付单
				if (masterPay.getPayStatus() == Constant.OP_PAY_STATUS_COMFIRM) {
					MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
					masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn).andPayStatusEqualTo((byte)Constant.OP_PAY_STATUS_COMFIRM);
					List<MasterOrderPay> unPayMasterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
					if (unPayMasterOrderPayList != null && unPayMasterOrderPayList.size() >= 1) {
						for (MasterOrderPay unPayMasterOrderPay : unPayMasterOrderPayList) {
							MasterOrderPayBack masterOrderPayBack = new MasterOrderPayBack();
							PropertyUtils.copyProperties(masterOrderPayBack, unPayMasterOrderPay);
							masterOrderPayBackMapper.deleteByPrimaryKey(unPayMasterOrderPay.getMasterPaySn());
							masterOrderPayBackMapper.insertSelective(masterOrderPayBack);
							masterOrderPayMapper.deleteByPrimaryKey(unPayMasterOrderPay.getMasterPaySn());
							masterOrderActionService.insertOrderActionBySn(masterOrderSn, "创建支付单时备份删除未确认支付单"+unPayMasterOrderPay.getMasterPaySn(),Constant.OS_STRING_SYSTEM);
						}
					}
				} 
				masterOrderPayMapper.insertSelective(masterOrderPay);
				masterOrderActionService.insertOrderActionBySn(masterOrderSn, "创建支付单成功" + masterOrderPay.getMasterPaySn(), Constant.OS_STRING_SYSTEM);
				orderPays.add(masterOrderPay.getMasterPaySn());
				logger.info("创建订单支付单：" + masterOrderPay.getMasterPaySn());
			}
//			masterOrderInfo.setUpdateTime(new Date());
//			masterOrderInfo.setPayStatus(changeOrderInfoPay(masterOrderSn));
//			masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfo);
			returnInfo.setData(orderPays);
			returnInfo.setIsOk(Constant.OS_YES);
		} catch (Exception e) {
			logger.info("创建订单支付单异常：" + masterOrderSn + "，支付单数量" + masterPayList.size(),e);
			return new ReturnInfo<List<String>>(Constant.OS_NO,"创建付款单信息" + masterOrderSn + "失败" + e.getMessage());
		}
		return returnInfo;
	}

	/**
	 * 创建多订单合并支付
	 * @param masterOrderSnList
	 * @return
	 */
	@Override
	public ReturnInfo<MergeOrderPay> createMergePay(List<String> masterOrderSnList){
		logger.info("开始创建合并支付单:"+masterOrderSnList.toString());
		BigDecimal totalFee = new BigDecimal(0.000);
		String masterOrderPaySnStr = "";
		ReturnInfo<MergeOrderPay> returnInfo = new ReturnInfo<MergeOrderPay>(); 
		try {
            if (null == masterOrderSnList || masterOrderSnList.size() == 0) {
                throw new Exception("创建合并支付单，订单号参数为空！");
            }
            byte payId = 1;
            byte payStatus = 0;
            List<Date> payLastTimeList = new ArrayList<Date>();
			for (String masterOrderSn : masterOrderSnList) {
				MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
				//masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn).andPayStatusEqualTo((byte) Constant.OP_PAY_STATUS_UNPAYED);
                // 不查询未付款
                masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn);
				List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
				if (null == masterOrderPayList || masterOrderPayList.size() != 1) {
					throw new Exception("订单" + masterOrderSn + "的支付单数据异常！");
				}
				MasterOrderPay masterOrderPay = masterOrderPayList.get(0);
                totalFee = totalFee.add(masterOrderPay.getPayTotalfee());
				masterOrderPaySnStr += masterOrderPay.getMasterPaySn() + ",";
				payLastTimeList.add(masterOrderPay.getPayLasttime());
				payId = masterOrderPay.getPayId();
                payStatus = masterOrderPay.getPayStatus();
			}
			MergeOrderPay mergeOrderPay = new MergeOrderPay();
			mergeOrderPay.setCreatTime(new Date());
			mergeOrderPay.setMasterPaySn(masterOrderPaySnStr.substring(0, masterOrderPaySnStr.length() - 1));
			mergeOrderPay.setMergePayFee(totalFee.setScale(2, BigDecimal.ROUND_HALF_UP));
			mergeOrderPay.setPayId(payId);
			mergeOrderPay.setPayName("支付宝");
			mergeOrderPay.setPayStatus(payStatus);
			InetAddress ia = InetAddress.getLocalHost();
			String localIp = ia.getHostAddress();
			if (StringUtil.isNull(localIp)) {
				localIp = "00";
			}
			String mergePaySn = Constant.OP_BEGIN_WITH_MFK + System.currentTimeMillis() + localIp.substring(localIp.length() - 1);
			mergeOrderPay.setMergePaySn(mergePaySn);
			// 付款最后期限
			mergeOrderPay.setPayLasttime(payLastTimeList.size() == 1 ? payLastTimeList.get(0) : getMaxDate(payLastTimeList));
			mergeOrderPayMapper.insertSelective(mergeOrderPay);
			logger.info("创建合并支付单："+mergePaySn);
			returnInfo.setIsOk(Constant.OS_YES);
			returnInfo.setData(mergeOrderPay);
			return returnInfo;
		} catch (Exception e) {
			logger.info("创建合并支付单异常：",e);
			return new ReturnInfo<MergeOrderPay>(Constant.OS_NO,"创建合并支付单信息" + masterOrderSnList.toString() + "失败" + e.getMessage());
		}
	}

	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

//	@Resource
//	private OrderQuestionService orderQuestionService;

//	@Resource(name = "groupBuyMessageSummaryJmsTemplate")
//	private JmsTemplate groupBuyMessageSummaryJmsTemplate;

	/**
	 * 订单支付成功
	 * @param orderStatus
	 * @return
	 */
	@Override
	public ReturnInfo payStCh(OrderStatus orderStatus) {
		logger.info("支付支付单：" + JSON.toJSONString(orderStatus));
		try {
			// 支付单号
			String paySn = orderStatus.getPaySn();
			MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByPrimaryKey(paySn);
			if (null == masterOrderPay) {
				throw new Exception("没有找到支付单：" + paySn);
			}
			
			// 订单号
			String masterOrderSn = orderStatus.getMasterOrderSn();
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn); 
			if (null == masterOrderInfo) {
				throw new Exception("没有找到订单：" + masterOrderSn);
			}
			//判断是否为团购单
			MasterOrderInfoExtendExample example = new MasterOrderInfoExtendExample();
			example.createCriteria().andMasterOrderSnEqualTo(masterOrderSn);
			List<MasterOrderInfoExtend> masterOrderInfoExtends = masterOrderInfoExtendMapper.selectByExample(example);
			MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtends.get(0);
			if(masterOrderInfoExtend.getGroupId() != null){
				//判断是预付款还是尾款 -1为未确认，0为预付款，1为尾款 ，前端只显示一个按钮，必须先确认预付款才有尾款
				if(masterOrderInfoExtend.getIsOperationConfirmPay() == -1) {
					//设置支付单为部分付款，并填充付款金额
					MasterOrderPay record = new MasterOrderPay();
					record.setMasterPaySn(paySn);
					record.setPayStatus(Byte.valueOf("1"));
					masterOrderPayMapper.updateByPrimaryKeySelective(record);

					//设置团购问题单
					orderStatus = new OrderStatus();
					orderStatus.setCode(Constant.QUESTION_CODE_TEN_THOUSAND);
				//	orderQuestionService.questionOrderByMasterSn(masterOrderInfo.getMasterOrderSn(), orderStatus);

//					String addRewardPointChangeLogMQ = JSONObject.toJSONString();
//					logger.info("团购信息:" + addRewardPointChangeLogMQ);
//					groupBuyMessageSummaryJmsTemplate.send(new TextMessageCreator(addRewardPointChangeLogMQ)););
					//下发团购mq
					return new ReturnInfo(Constant.OS_YES);
				}else if(masterOrderInfoExtend.getIsOperationConfirmPay() == 0){
					//尾款，团购订单成功，单子设置为正常单，填充对应金额
					MasterOrderPay record = new MasterOrderPay();
					record.setMasterPaySn(paySn);
					record.setPayStatus(Byte.valueOf("2"));
					masterOrderPayMapper.updateByPrimaryKeySelective(record);

					//订单改为正常单
					MasterOrderInfo masterOrderInfoParam = new MasterOrderInfo();
					masterOrderInfoParam.setMasterOrderSn(masterOrderInfo.getMasterOrderSn());
					masterOrderInfoParam.setQuestionStatus(0);
					masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfoParam);
				}
			}

			if (masterOrderPay.getPayStatus() == Constant.OP_PAY_STATUS_PAYED) {
				throw new Exception("支付单：" + paySn + "已支付，不能进行支付操作！");
			}
			// 判断是否是问题单
			if (Constant.OD_QUESTION_STATUS_YES.equals(masterOrderInfo.getQuestionStatus())) {
				throw new Exception("订单：" + masterOrderSn + "要处于正常单状态！");
			}
			// 判断是否被非admin的当前人锁定
			if (orderStatus.getAdminUser() != null && !orderStatus.getAdminUser().trim().equals("admin") 
					&& !StringUtils.equalsIgnoreCase(ConstantValues.ACTION_USER_SYSTEM, orderStatus.getAdminUser())) {
				if (Constant.OI_LOCK_STATUS_UNLOCKED == masterOrderInfo.getLockStatus()) {
					//未锁定报错
					throw new Exception("请锁定后，再进行支付操作！");
				}
				if (!orderStatus.getUserId().equals(masterOrderInfo.getLockStatus())) {
					throw new Exception("订单非本人锁定，不能进行支付操作！");
				}
			}

            //更新支付方式
            String payCode = orderStatus.getPayCode();
            logger.error("订单支付 支付编码：" + payCode);
            if (StringUtils.isNotBlank(payCode)) {
                SystemPaymentExample paymentExample = new SystemPaymentExample();
                paymentExample.or().andPayCodeEqualTo(payCode).andEnabledEqualTo(1);
                List<SystemPayment> systemPayments = systemPaymentMapper.selectByExample(paymentExample);
                if (StringUtil.isListNotNull(systemPayments)) {
                    SystemPayment systemPayment = systemPayments.get(0);
                    masterOrderPay.setPayId(systemPayment.getPayId());
                    masterOrderPay.setPayName(systemPayment.getPayName());
                }
                logger.error("订单支付 支付信息：" + JSONObject.toJSONString(systemPayments));
            }
            
			masterOrderPay.setPayNote(orderStatus.getMessage());
			masterOrderPay.setPayStatus((byte)Constant.OP_PAY_STATUS_PAYED);
			masterOrderPay.setPayTime(new Date());
			masterOrderPay.setUpdateTime(new Date());
			masterOrderPay.setMergePaySn(orderStatus.getMergePaySn());
			masterOrderPayMapper.updateByPrimaryKeySelective(masterOrderPay);
			
			MasterOrderInfo masterOrderInfoUpdate = new MasterOrderInfo();
			masterOrderInfoUpdate.setMasterOrderSn(masterOrderSn);
			if (orderStatus.getPayFee() > 0) {
				masterOrderInfoUpdate.setTotalPayable(masterOrderInfo.getTotalPayable().subtract(new BigDecimal(orderStatus.getPayFee())));
				masterOrderInfoUpdate.setMoneyPaid(masterOrderInfo.getMoneyPaid().add(new BigDecimal(orderStatus.getPayFee())));
			} else {
				masterOrderInfoUpdate.setTotalPayable(masterOrderInfo.getTotalPayable().subtract(masterOrderPay.getPayTotalfee()));
				masterOrderInfoUpdate.setMoneyPaid(masterOrderInfo.getMoneyPaid().add(masterOrderPay.getPayTotalfee()));	
			}
			masterOrderInfoUpdate.setUpdateTime(new Date());
			masterOrderInfoUpdate.setPayStatus(changeOrderInfoPay(masterOrderSn));
			masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfoUpdate);
			
			try {
				// 支付释放占用量
				OrderStatus orderStatusStock= new OrderStatus();
				orderStatusStock.setMasterOrderSn(masterOrderSn);
				orderStatusStock.setType("1");
				orderStockJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(orderStatusStock)));
			} catch (Exception e) {
				logger.info("支付支付单库存占用异常：",e);
			}
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, "支付单已支付：" + paySn, orderStatus.getAdminUser());
			//pos不确认
			if (!orderStatus.getSource().equals(ConstantValues.METHOD_SOURCE_TYPE.POS) && masterOrderInfoUpdate.getPayStatus() == Constant.OI_PAY_STATUS_PAYED) {

				OrderManagementRequest request = new OrderManagementRequest();
				request.setMasterOrderSn(masterOrderSn);
				request.setActionUser(orderStatus.getAdminUser());
				request.setMessage(orderStatus.getSource() + "支付确认！");
				OrderManagementResponse response = orderManagementService.orderConfirm(request);
				if (response == null || !response.getSuccess()) {
					throw new Exception("已支付时订单确认失败：" + response ==null ? "返回结果为空" : response.getMessage());
				}
			}
			return new ReturnInfo(Constant.OS_YES);
		} catch (Exception e) {
			logger.info("支付支付单异常：" + orderStatus.getPaySn(), e);
			return new ReturnInfo(Constant.OS_NO, "支付支付单异常：" + e.getMessage());
		}
	}
	
	/**
	 * 换货退单入库确认
	 */
	public ReturnInfo orderReturnPayStCh(OrderStatus orderStatus) {
		logger.info("退单入库确认支付单：" + JSON.toJSONString(orderStatus));
		try {
			// 支付单号
			String paySn = orderStatus.getPaySn();
			// 订单号
			String masterOrderSn = orderStatus.getMasterOrderSn();
			MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByPrimaryKey(paySn);
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn); 
			if (null == masterOrderPay) {
				throw new Exception("没有找到支付单：" + paySn);
			}
			if (null == masterOrderInfo){
				throw new Exception("没有找到订单：" + masterOrderSn);
			}
			if (masterOrderPay.getPayStatus() == Constant.OP_PAY_STATUS_PAYED) {
				throw new Exception("支付单：" + orderStatus.getPaySn() + "已支付，不能进行支付操作！");
			}
			masterOrderPay.setPayNote(orderStatus.getMessage());
			masterOrderPay.setPayStatus((byte)Constant.OP_PAY_STATUS_PAYED);
			masterOrderPay.setPayTime(new Date());
			masterOrderPay.setUpdateTime(new Date());
			masterOrderPayMapper.updateByPrimaryKeySelective(masterOrderPay);
			masterOrderInfo.setPayStatus(changeOrderInfoPay(orderStatus.getMasterOrderSn()));
			masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfo);
			try {
				OrderStatus orderStatusStock= new OrderStatus();
				orderStatusStock.setMasterOrderSn(orderStatus.getMasterOrderSn());
				orderStatusStock.setType("1");
				orderStockJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(orderStatusStock)));
			} catch (Exception e) {
				logger.info("支付支付单库存占用异常：",e);
			}
			orderStatus.setMessage(orderStatus.getSource() + "支付确认！");
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, "退货入库，退单转入款支付：" + paySn, Constant.OS_STRING_SYSTEM);
//			ReturnInfo reInfo = orderConfirmService.confirmOrderByMasterSn(orderStatus.getMasterOrderSn(), orderStatus);
			OrderManagementRequest request = new OrderManagementRequest();
			request.setMasterOrderSn(masterOrderSn);
			request.setActionUser(orderStatus.getAdminUser());
			request.setMessage(orderStatus.getSource() + "支付确认！");
			OrderManagementResponse response = orderManagementService.orderConfirm(request);
			if (response == null || !response.getSuccess()) {
				throw new Exception("已支付时订单确认失败：" + response ==null ? "返回结果为空" : response.getMessage());
			}
			return new ReturnInfo(Constant.OS_YES);
		} catch (Exception e) {
			logger.info("支付支付单异常：" + orderStatus.getPaySn(), e);
			return new ReturnInfo(Constant.OS_NO, "支付支付单异常：" + e.getMessage());
		}
	}
	
	/**
	 * 退单入库未确认
	 * @param orderStatus
	 */
	@Override
	public ReturnInfo orderReturnUnPayStCh(OrderStatus orderStatus) {
		logger.info("退单入库未确认未支付：" + JSON.toJSONString(orderStatus));
		String paySn = orderStatus.getPaySn();
		String masterOrderSn = orderStatus.getMasterOrderSn();
		try {
			MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByPrimaryKey(paySn);
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn); 
			if (null == masterOrderPay) {
				throw new Exception("没有找到支付单：" + paySn);
			}
			if (null == masterOrderInfo) {
				throw new Exception("没有找到订单：" + masterOrderSn);
			}
			if (masterOrderPay.getPayStatus() == Constant.OP_PAY_STATUS_COMFIRM) {
				throw new Exception("支付单：" + paySn + "未确认，不能进行未确认操作！");
			}
			if (masterOrderPay.getPayId() != Byte.parseByte(ConstantValues.EXCHANGE_ORDER_RETURN_2_PAY_ID)){
				throw new Exception("支付单：" + paySn + "不是退单转入款，不能进行未确认操作！");
			}
			masterOrderPay.setPayStatus((byte)Constant.OP_PAY_STATUS_COMFIRM);
			masterOrderPay.setUpdateTime(new Date());
			masterOrderPay.setPayNote("");
			masterOrderPay.setPayTime(null);
			masterOrderPayMapper.updateByPrimaryKeySelective(masterOrderPay);
			masterOrderInfo.setPayStatus(changeOrderInfoPay(masterOrderSn));
			masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfo);
			orderStatus.setMessage("退单入库未确认挂起！");
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, "退货未入库，退单转入款待确认：" + paySn, Constant.OS_STRING_SYSTEM);
//			ReturnInfo reInfo = orderConfirmService.unConfirmOrderByMasterSn(masterOrderSn, orderStatus);
//			if (reInfo.getIsOk() <= 0) {
//				throw new Exception("退单入库未确认订单未确认失败："+reInfo.getMessage());
//			}
			OrderManagementRequest request = new OrderManagementRequest();
			request.setMasterOrderSn(masterOrderSn);
			request.setActionUser(orderStatus.getAdminUser());
			request.setMessage("退单入库未确认挂起！");
			OrderManagementResponse response = orderManagementService.orderUnConfirm(request);
			if (response == null || !response.getSuccess()) {
				throw new Exception("退单入库未确认订单未确认失败："+ response ==null ? "返回结果为空" : response.getMessage());
			}
			return new ReturnInfo(Constant.OS_YES);
		} catch (Exception e) {
			logger.info("退单入库未确认异常："+orderStatus.getPaySn(),e);
			return new ReturnInfo(Constant.OS_NO,"退单入库未确认异常："+e.getMessage());
		}
	}
	
	/**
	 * 未支付
	 */
	public ReturnInfo unPayStCh(OrderStatus orderStatus) {
		logger.info("未支付支付单：" + JSON.toJSONString(orderStatus));
		// 支付单号
		String paySn = orderStatus.getPaySn();
		// 订单号
		String masterOrderSn = orderStatus.getMasterOrderSn();
		try {
			MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByPrimaryKey(paySn);
			if (null == masterOrderPay) {
				throw new Exception("没有找到支付单：" + paySn);
			}

			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (null == masterOrderInfo){
				throw new Exception("没有找到订单：" + masterOrderSn);
			}

			if (masterOrderPay.getPayStatus() == Constant.OP_PAY_STATUS_UNPAYED) {
				throw new Exception("支付单："+ paySn + "未支付，不能进行未支付操作！");
			}
			masterOrderPay.setPayStatus((byte)Constant.OP_PAY_STATUS_UNPAYED);
			masterOrderPay.setUpdateTime(new Date());
			masterOrderPayMapper.updateByPrimaryKeySelective(masterOrderPay);

			// 应付款总金额 = 应付款总金额 + 支付总费用
			masterOrderInfo.setTotalPayable(masterOrderInfo.getTotalPayable().add(masterOrderPay.getPayTotalfee()));
			// 已付款金额 = 已付款金额 - 支付总费用
			masterOrderInfo.setMoneyPaid(masterOrderInfo.getMoneyPaid().subtract(masterOrderPay.getPayTotalfee()));
			// 付款状态
			masterOrderInfo.setPayStatus(changeOrderInfoPay(masterOrderSn));
			masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfo);
			
			orderStatus.setMessage("未支付挂起！");
			masterOrderActionService.insertOrderActionBySn(masterOrderSn, "支付单未支付：" + paySn, orderStatus.getAdminUser());

			// 订单未确认
//			ReturnInfo reInfo = orderConfirmService.unConfirmOrderByMasterSn(masterOrderSn, orderStatus);
//			if (reInfo.getIsOk() <= 0) {
//				throw new Exception("已支付时订单确认失败：" + reInfo.getMessage());
//			}
			OrderManagementRequest request = new OrderManagementRequest();
			request.setMasterOrderSn(masterOrderSn);
			request.setActionUser(orderStatus.getAdminUser());
			request.setMessage("未支付挂起！");
			OrderManagementResponse response = orderManagementService.orderUnConfirm(request);
			if (response == null || !response.getSuccess()) {
				throw new Exception("已支付时订单确认失败："+ response ==null ? "返回结果为空" : response.getMessage());
			}
			return new ReturnInfo(Constant.OS_YES);
		} catch (Exception e) {
			logger.info("后台未支付支付单异常：" + paySn, e);
			return new ReturnInfo(Constant.OS_NO, "未支付支付单异常：" + e.getMessage());
		}
	}
	
	/**
	 * 根据支付单号或订单号修改支付方式
	 * @param paySn
	 * @param newPayId
	 * @param actionUser
	 * @return
	 */
	@Override
	public ReturnInfo changeOrderPayMethod(String paySn, Integer newPayId, String actionUser) {
		logger.info("修改支付方式：paySn=" + paySn + ",newPayId=" + newPayId);
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			if (StringUtils.isBlank(paySn) || null == newPayId) {
				returnInfo.setIsOk(0);
				returnInfo.setMessage("参数错误！");
				return returnInfo;
			} else {
				if (paySn.indexOf("MFK") >= 0) {
					MergeOrderPay mergePay = mergeOrderPayMapper.selectByPrimaryKey(paySn);
					SystemPayment sp = systemPaymentMapper.selectByPrimaryKey(newPayId.byteValue());
					mergePay.setPayId(sp.getPayId());
					mergePay.setPayName(sp.getPayName());
					mergePay.setUpdateTime(new Date());
					mergeOrderPayMapper.updateByPrimaryKeySelective(mergePay);
					
					String[] masterPaySnData = mergePay.getMasterPaySn().split(",");
					for (String masterPaySn : masterPaySnData) {
						if (StringUtils.isNotBlank(masterPaySn)) {
							MasterOrderPay masterPay = masterOrderPayMapper.selectByPrimaryKey(masterPaySn);
							String oldPayName = masterPay.getPayName();
							masterPay.setPayId(sp.getPayId());
							masterPay.setPayName(sp.getPayName());
							masterPay.setUpdateTime(new Date());
							masterOrderPayMapper.updateByPrimaryKeySelective(masterPay);
							masterOrderActionService.insertOrderActionBySn(masterPay.getMasterOrderSn(), "[合并支付]修改支付方式" + oldPayName + "->" + sp.getPayName(), actionUser == null ? Constant.OS_STRING_SYSTEM : actionUser);
						}
					}
				} else {
					MasterOrderPay masterPay = masterOrderPayMapper.selectByPrimaryKey(paySn);
					SystemPayment sp = systemPaymentMapper.selectByPrimaryKey(newPayId.byteValue());
					String oldPayName = masterPay.getPayName();
					masterPay.setPayId(newPayId.byteValue());
					masterPay.setPayName(sp.getPayName());
					masterPay.setUpdateTime(new Date());
					masterOrderPayMapper.updateByPrimaryKeySelective(masterPay);
					masterOrderActionService.insertOrderActionBySn(masterPay.getMasterOrderSn(), "修改支付方式" + oldPayName + "->" + sp.getPayName(), actionUser == null ? Constant.OS_STRING_SYSTEM : actionUser);
				}
			}
			returnInfo.setIsOk(1);
			returnInfo.setMessage("支付方式修改成功！");
		} catch (NumberFormatException e) {
			logger.error("修改支付方式异常：paySn=" + paySn + ",newPayId=" + newPayId, e);
			logger.info("修改支付方式异常：paySn=" + paySn + ",newPayId=" + newPayId, e);
			returnInfo.setIsOk(0);
			returnInfo.setMessage("修改支付方式异常！");
		}
		return returnInfo;
	}

	/**
	 * 付款单状态变更后，查询订单支付状态
	 * @param masterOrderSn 订单编码
	 * @return
	 * @throws Exception
	 */
	private byte changeOrderInfoPay(String masterOrderSn) throws Exception {
		MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
		masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);

		// 未付款标志
		boolean unpayFlag = false;
		// 已付款标志
		boolean payFlag = false;
		for (MasterOrderPay masterOrderPay : masterOrderPayList) {
			// 未付款、待确认
			if (masterOrderPay.getPayStatus() == Constant.OP_PAY_STATUS_UNPAYED || masterOrderPay.getPayStatus() == Constant.OP_PAY_STATUS_COMFIRM) {
				unpayFlag = true;
			}
			// 已付款
			if (masterOrderPay.getPayStatus() == Constant.OP_PAY_STATUS_PAYED) {
				payFlag = true;
			}
		}
		// 已支付
		if (payFlag && !unpayFlag) {
			return Constant.OI_PAY_STATUS_PAYED;
		} else if(!payFlag && unpayFlag) {
			//未支付
			return Constant.OI_PAY_STATUS_UNPAYED;
		} else if(payFlag && unpayFlag) {
			//部分支付
			return Constant.OI_PAY_STATUS_PARTPAYED;
		} else {
			throw new Exception("支付单数据异常！");
		}
	}
	
	
	/**
	 * 通过支付编码，查找支付名字
	 * @param payCode 支付编码
	 * @return SystemPayment
	 */
	private SystemPayment selectSystemPaymentByCode(String payCode) {
		if (StringUtils.isEmpty(payCode)) {
			return null;
		}
		SystemPaymentExample example = new SystemPaymentExample();
		example.or().andPayCodeEqualTo(payCode);
		List<SystemPayment> payments = systemPaymentMapper.selectByExample(example);
		if (null == payments || payments.isEmpty()) {
			return null;
		}
		return payments.get(0);
	}
	
	//获取最大的付款单号
	private String getMasterOrderPayMaxSn(String masterOrderSn) {
		String maxPaySn = null;
		MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
		masterOrderPayExample.setOrderByClause(" master_pay_sn desc ");
		masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn).limit(1);
		List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
		if (CollectionUtils.isNotEmpty(masterOrderPayList)) {
			maxPaySn = masterOrderPayList.get(0).getMasterPaySn();
		}
		return maxPaySn;
	}

	/**
	 * 判断此订单是否被当前操作者锁定
	 * @param lockStatus
	 * @param userId
	 * @return String
	 */
	public static String judgeSelfLock(Integer lockStatus, Integer userId) {
		if(null == userId) {
			return "没有此用户名的信息";
		} else if (null == lockStatus || lockStatus.intValue() == Constant.OI_LOCK_STATUS_UNLOCKED) {
			return "此订单未被锁定";
		} else {
			if (lockStatus.equals(userId)) {
				// 此订单被此操作者锁定
				return "";
			} else {
				return "没有被此用户绑定,返回此订单被绑定用户的ID为:" + lockStatus;
			}
		}
	}
		 
	private int getPeriodDetailValue(int period, String type) {
		OrderPeriodDetail detail = orderPeriodDetailService.selectByPeriodAndType(period, type);
		if (detail != null) {
			return detail.getPeriodValue();
		}
		return 0;
	}

	public Date getDate(Date t, int sec) {
		long milliseconds = t.getTime();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds + sec * 1000);
		Date f = c.getTime();
		return f;
	}
	
	public Date getMaxDate(List<Date> dateList) {
		Date maxDate = new Date();
		for (int i = 0; i < dateList.size() - 1; i++) {
			if (dateList.get(i).before(dateList.get(i + 1))) {
				maxDate = dateList.get(i);
			} else {
				maxDate = dateList.get(i + 1);
			}
		}
		return maxDate;
	}
	
	/**
	 * @param i
	 * @param size
	 * @return
	 */
	private String genCode(Integer i, Integer size) {
		String num = i.toString();
		StringBuilder code = new StringBuilder("");
		for (int j = 0; j < size - num.length(); j++) {
			code.append("0");
		}
		return code + num;
	}

}
