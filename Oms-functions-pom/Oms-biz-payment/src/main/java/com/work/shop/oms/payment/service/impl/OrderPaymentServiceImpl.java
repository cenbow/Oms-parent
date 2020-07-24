package com.work.shop.oms.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.bean.OrderPayInfo;
import com.work.shop.oms.api.param.bean.PayBackInfo;
import com.work.shop.oms.api.param.bean.PayReturnInfo;
import com.work.shop.oms.api.payment.service.OrderPaymentService;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.config.service.SystemPaymentService;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.exception.OrderException;
import com.work.shop.oms.order.feign.OrderManagementService;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.response.OrderManagementResponse;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.payment.service.PayService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * 订单支付业务逻辑
 * @author QuYachu
 */
@Service
public class OrderPaymentServiceImpl implements OrderPaymentService {
	
	private Logger logger = Logger.getLogger(OrderPaymentServiceImpl.class);

	@Resource
	private RedisClient redisClient;

	@Resource
	private PayService payService;

	@Resource
	private MasterOrderActionService masterOrderActionService;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource
	private MergeOrderPayMapper mergeOrderPayMapper;

	//@Resource
//	private OrderDistributeOpService orderDistributeOpService;
	
	@Resource
	private OrderManagementService orderManagementService;

    /*@Resource
    private OrderConfirmService orderConfirmService;*/

    @Resource
	private SystemPaymentService systemPaymentService;

    @Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

    /**
     * 获取指定的支付总金额
     * @param masterOrderSnList
     * @return
     */
    @Override
	public ApiReturnData<OrderPayInfo> getOrderPayMoneyInfo(List<String> masterOrderSnList) {
		ApiReturnData<OrderPayInfo> apiReturnData = new ApiReturnData<OrderPayInfo>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);

		try {
			if (masterOrderSnList == null || masterOrderSnList.size() == 0) {
                apiReturnData.setMessage("订单号为空");
                return apiReturnData;
            }

            OrderPayInfo orderPayInfo = new OrderPayInfo();
			BigDecimal totalMoney = BigDecimal.valueOf(0);
            for (String masterOrderSn : masterOrderSnList) {
                MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
                if (null == masterOrderInfo) {
                    apiReturnData.setMessage("无效的订单号");
                    return apiReturnData;
                }
                //设置支付信息 价格状态
				if( orderPayInfo.getOrderPayPriceNo() == null
					&& masterOrderInfo.getGoodsSaleType() != null
					&& masterOrderInfo.getPriceChangeStatus() != null
					&& masterOrderInfo.getGoodsSaleType() != Constant.GOODS_SALE_TYPE_STANDARD
					&& masterOrderInfo.getPriceChangeStatus() < Constant.PRICE_CHANGE_AFFIRM_2 ){

					orderPayInfo.setOrderPayPriceNo(1);
				}
                // 支付总费用
                totalMoney = totalMoney.add(masterOrderInfo.getPayTotalFee());
            }

            orderPayInfo.setPayTotalfee(totalMoney.doubleValue());

            apiReturnData.setMessage("成功");
            apiReturnData.setData(orderPayInfo);
            apiReturnData.setIsOk(Constant.OS_STR_YES);
		} catch (Exception e) {
		    logger.error("获取订单: " + JSONObject.toJSONString(masterOrderSnList) + "支付信息异常", e);
            apiReturnData.setMessage("查询异常");
        }

        return apiReturnData;
	}

	/**
	 * 获取订单支付信息
	 * @param paySn 支付单号
	 * @param masterOrderSnList 订单编码列表
	 * @return ApiReturnData<OrderPayInfo>
	 */
	@Override
	public ApiReturnData<OrderPayInfo> getOrderPayInfo(String paySn, List<String> masterOrderSnList) {
		logger.info("支付查询支付消息：paySn=" + paySn + ",masterOrderSnList=" + masterOrderSnList);
		ApiReturnData<OrderPayInfo> apiReturnData = new ApiReturnData<OrderPayInfo>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		try {
			OrderPayInfo orderPayInfo = new OrderPayInfo();
			if (StringUtil.isNotNull(paySn)) {
				if (paySn.startsWith(Constant.ORDER_PAY_MFK)) {
					MergeOrderPay mergeOrderPay = mergeOrderPayMapper.selectByPrimaryKey(paySn);
					if (null == mergeOrderPay) {
						throw new Exception("未找到对应支付单！");
					}
					orderPayInfo.setPaySn(mergeOrderPay.getMergePaySn());
					orderPayInfo.setPayId(mergeOrderPay.getPayId());
					orderPayInfo.setPayName(mergeOrderPay.getPayName());
					orderPayInfo.setPayLastTime(TimeUtil.formatDate(mergeOrderPay.getPayLasttime()));
					orderPayInfo.setPayStatus(mergeOrderPay.getPayStatus());
					orderPayInfo.setPayTotalfee(mergeOrderPay.getMergePayFee().doubleValue());

					HashSet<String> userSet = new HashSet<String>();
					String[] paySnData = mergeOrderPay.getMasterPaySn().split(Constant.STRING_SPLIT_COMMA);
					for (String masterPaySn : paySnData) {
						MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByPrimaryKey(masterPaySn);
						MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderPay.getMasterOrderSn());
						String userId = masterOrderInfo.getUserId();
						if (masterOrderInfo.getOrderStatus() > 0) {
							orderPayInfo.setOrderStatus(masterOrderInfo.getOrderStatus());
						}
						userSet.add(userId);
					}
					if (null == userSet || userSet.size() != 1) {
						throw new Exception("userId异常！");
					}
					orderPayInfo.setUserId(userSet.iterator().next());
				} else {
					MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByPrimaryKey(paySn);
					if (null == masterOrderPay){
						throw new Exception("未找到对应支付单！");
					}
					orderPayInfo.setPaySn(masterOrderPay.getMasterPaySn());
					orderPayInfo.setPayId(masterOrderPay.getPayId());
					orderPayInfo.setPayName(masterOrderPay.getPayName());
					orderPayInfo.setPayLastTime(TimeUtil.formatDate(masterOrderPay.getPayLasttime()));
					orderPayInfo.setPayStatus(masterOrderPay.getPayStatus());
					orderPayInfo.setPayTotalfee(masterOrderPay.getPayTotalfee().doubleValue());
					MasterOrderInfo masterOrderInfo =  masterOrderInfoMapper.selectByPrimaryKey(masterOrderPay.getMasterOrderSn());
					String userId = masterOrderInfo.getUserId();
					if (masterOrderInfo.getOrderStatus() > 0) {
						orderPayInfo.setOrderStatus(masterOrderInfo.getOrderStatus());
					}
					orderPayInfo.setUserId(userId);
					//判断非标、改价的订单 设置支付信息 价格状态
					if( masterOrderInfo.getGoodsSaleType() != null
							&& masterOrderInfo.getPriceChangeStatus() != null
							&& masterOrderInfo.getGoodsSaleType() != Constant.GOODS_SALE_TYPE_STANDARD
							&& masterOrderInfo.getPriceChangeStatus() < Constant.PRICE_CHANGE_AFFIRM_2 ){
						orderPayInfo.setOrderPayPriceNo(1);
					}
				}
			} else {
				if (null != masterOrderSnList && masterOrderSnList.size() > 0) {
					for (String masterOrderSn : masterOrderSnList) {
						MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
						if (null == masterOrderInfo) {
							throw new Exception("未找到对应订单！");
						}
					}
				} else {
					throw new Exception("参数不能都为空！");
				}
				//单个订单支付查询
				if (masterOrderSnList.size() == 1){
					MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
					masterOrderPayExample.or().andMasterOrderSnEqualTo(masterOrderSnList.get(0)).andPayStatusEqualTo((byte)Constant.OP_PAY_STATUS_UNPAYED);
					List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
					if (null == masterOrderPayList || masterOrderPayList.size() == 0) {
						apiReturnData.setIsOk("0");
						apiReturnData.setMessage("未找到需要支付的支付单！");
						return apiReturnData;
					} else if(masterOrderPayList.size() > 1) {
						throw new Exception("支付单数据异常！");
					}
					orderPayInfo.setPaySn(masterOrderPayList!=null?masterOrderPayList.get(0).getMasterPaySn():"");
					orderPayInfo.setPayId(masterOrderPayList!=null?masterOrderPayList.get(0).getPayId():(byte)0);
					orderPayInfo.setPayName(masterOrderPayList.get(0).getPayName());
					orderPayInfo.setPayLastTime(masterOrderPayList!=null?TimeUtil.formatDate(masterOrderPayList.get(0).getPayLasttime()):"");
					orderPayInfo.setPayStatus(masterOrderPayList!=null?masterOrderPayList.get(0).getPayStatus():(byte)-1);
					orderPayInfo.setPayTotalfee(masterOrderPayList!=null?masterOrderPayList.get(0).getPayTotalfee().doubleValue():0D);
					MasterOrderInfo masterOrderInfo =  masterOrderInfoMapper.selectByPrimaryKey(masterOrderSnList.get(0));
					String userId =masterOrderInfo.getUserId();
					if(masterOrderInfo.getOrderStatus()>0){
						orderPayInfo.setOrderStatus(masterOrderInfo.getOrderStatus());
					}
					orderPayInfo.setUserId(userId);
					//判断非标、改价的订单 设置支付信息 价格状态
					if( masterOrderInfo.getGoodsSaleType() != null
							&& masterOrderInfo.getPriceChangeStatus() != null
							&& masterOrderInfo.getGoodsSaleType() != Constant.GOODS_SALE_TYPE_STANDARD
							&& masterOrderInfo.getPriceChangeStatus() < Constant.PRICE_CHANGE_AFFIRM_2 ){
						orderPayInfo.setOrderPayPriceNo(1);
					}
					//判断是不是买的店铺商品
					if (!Constant.DEFAULT_SHOP.equals(masterOrderInfo.getOrderFrom())) {
						orderPayInfo.setSpecialType(Constant.SPECIAL_TYPE_ORDER_BUY_STORE);
					}
					//判断是不是外部买家用铁信支付的类型，目前只有在买自营这一个场景，这个场景不允许前端确认支付
					//获取铁信支付的payId
					SystemPayment systemPayment = systemPaymentService.selectSystemPayByCode(Constant.PAY_TIEXIN);
					MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSnList.get(0));
					if (orderPayInfo.getPayId() == systemPayment.getPayId()) {
						if (null != masterOrderInfoExtend && Constant.OUTSIDE_COMPANY.equals(masterOrderInfoExtend.getCompanyType())) {
							orderPayInfo.setSpecialType(Constant.SPECIAL_TYPE_OUTSIDE_COMPANY_TIEXIN);
						}
					}
					//特殊情况 对应需求1351 子公司买店铺商品用铁信支付时 specialType = 3
					if (!Constant.DEFAULT_SHOP.equals(masterOrderInfo.getOrderFrom()) && orderPayInfo.getPayId() == systemPayment.getPayId()
							&& null != masterOrderInfoExtend && Constant.INTERNAL_COMPANY.equals(masterOrderInfoExtend.getCompanyType())) {
						orderPayInfo.setSpecialType(Constant.SPECIAL_TYPE_INTERNAL_COMPANY_BUY_STORE_TIEXIN);
					}
				} else {
					//合并支付查询
					ReturnInfo<MergeOrderPay> reInfo = payService.createMergePay(masterOrderSnList);
					if (reInfo.getIsOk() == 0 ) {
						throw new Exception("创建合并支付单数据异常！" + reInfo.getMessage());
					}
					MergeOrderPay mergeOrderPay = reInfo.getData();
					if (null == mergeOrderPay) {
						throw new Exception("合并支付单数据异常！");
					}
					orderPayInfo.setPaySn(mergeOrderPay.getMergePaySn());
					orderPayInfo.setPayId(mergeOrderPay.getPayId());
					orderPayInfo.setPayName(mergeOrderPay.getPayName());
					orderPayInfo.setPayLastTime(TimeUtil.formatDate(mergeOrderPay.getPayLasttime()));
					orderPayInfo.setPayStatus(mergeOrderPay.getPayStatus());
					orderPayInfo.setPayTotalfee(mergeOrderPay.getMergePayFee().doubleValue());
					HashSet<String> userSet = new HashSet<String>();
					for (String masterPaySn : mergeOrderPay.getMasterPaySn().split(Constant.STRING_SPLIT_COMMA)) {
						MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByPrimaryKey(masterPaySn);
						MasterOrderInfo masterOrderInfo =  masterOrderInfoMapper.selectByPrimaryKey(masterOrderPay.getMasterOrderSn());
						String userId = masterOrderInfo.getUserId();
						if (masterOrderInfo.getOrderStatus() > 0) {
							orderPayInfo.setOrderStatus(masterOrderInfo.getOrderStatus());
						}
						//判断非标、改价的订单 设置支付信息 价格状态
						if( orderPayInfo.getOrderPayPriceNo() == null
								&& masterOrderInfo.getGoodsSaleType() != null
								&& masterOrderInfo.getPriceChangeStatus() != null
								&& masterOrderInfo.getGoodsSaleType() != Constant.GOODS_SALE_TYPE_STANDARD
								&& masterOrderInfo.getPriceChangeStatus() < Constant.PRICE_CHANGE_AFFIRM_2 ){
							orderPayInfo.setOrderPayPriceNo(1);
						}
						userSet.add(userId);
					}
					if (null == userSet || userSet.size() != 1) {
						throw new Exception("userId异常！");
					}
					orderPayInfo.setUserId(userSet.iterator().next());
				}
			}
			apiReturnData.setData(orderPayInfo);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
		} catch (Exception e) {
			apiReturnData.setIsOk(Constant.OS_STR_NO);
			apiReturnData.setMessage(e.getMessage());
			logger.info("支付查询异常：",e);
			logger.error("支付查询异常：",e);
		}
		return apiReturnData;
	}

	/**
	 * 变更支付方式
	 * @param paySn 支付单号
	 * @param newPayId  支付方式id
	 * @return ApiReturnData
	 */
	@Override
	public ApiReturnData changeOrderPayMethod(String paySn, Integer newPayId) {
		logger.info("changeOrderPayMethod.begin....paySn=" + paySn + ",newPayId" + newPayId);
		ApiReturnData apiReturnData = new ApiReturnData();
		ReturnInfo returnInfo = payService.changeOrderPayMethod(paySn, newPayId, null);
		apiReturnData.setIsOk(returnInfo.getIsOk() + "");
		apiReturnData.setMessage(returnInfo.getMessage());
		logger.info("changeOrderPayMethod.end...." + JSON.toJSONString(returnInfo));
		return apiReturnData;
	}

	/**
	 * 支付前锁定支付单
	 * @param paySn 支付单号
	 * @param actionUser 操作用户
	 * @return ApiReturnData
	 */
	@Override
	public ApiReturnData lockOrderBeforePayment(String paySn, String actionUser) {
		logger.info("lockOrderBeforePayment.begin....paySn=" + paySn + ",adminUser" + actionUser);
		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		ReturnInfo returnInfo = new ReturnInfo();

		try {
			if (StringUtil.isNull(paySn) || StringUtil.isNull(actionUser)) {
				apiReturnData.setMessage("参数不能为空！");
				return apiReturnData;
			}

			List<String> masterPaySnList = new ArrayList<String>();
			if (paySn.startsWith(Constant.OP_BEGIN_WITH_MFK)) {
				MergeOrderPay mergeOrderPay = mergeOrderPayMapper.selectByPrimaryKey(paySn);
				if (null == mergeOrderPay) {
					apiReturnData.setMessage("未找到对应支付单！");
					return apiReturnData;
				}
				String[] paySns = mergeOrderPay.getMasterPaySn().split(Constant.STRING_SPLIT_COMMA);
				masterPaySnList = Arrays.asList(paySns);
			} else {
				masterPaySnList.add(paySn);
			}
			String message="";
			for (String masterPaySn : masterPaySnList) {
				MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByPrimaryKey(masterPaySn);
				if (null == masterOrderPay) {
					apiReturnData.setMessage("支付单数据异常！");
					return apiReturnData;
				}

				OrderManagementRequest request = new OrderManagementRequest();
				request.setSource(ConstantValues.METHOD_SOURCE_TYPE.FRONT);
				request.setActionUser(actionUser);
				request.setActionUserId(Constant.CUSTOMER_PAY_LOCK_ID + "");
				request.setMasterOrderSn(masterOrderPay.getMasterOrderSn());
				request.setMessage("用户支付前锁定订单！");
				OrderManagementResponse response = orderManagementService.orderLock(request);
				message += response.getMessage();
			}
			apiReturnData.setIsOk(Constant.OS_STR_YES);
			apiReturnData.setMessage(message);
		} catch (OrderException e) {
			logger.info("lockOrderBeforePayment.异常....masterOrderSn=" + paySn, e);
			apiReturnData.setIsOk(Constant.OS_STR_NO);
			apiReturnData.setMessage(e.getMessage());
		}
		logger.info("lockOrderBeforePayment.end....masterOrderSn=" + JSON.toJSONString(returnInfo));
		return apiReturnData;
	}

	/**
	 * 前端支付成功回调
	 * @param payBackInfo 支付成功信息
	 * @return PayReturnInfo
	 */
	@Override
	public PayReturnInfo payStChClient(PayBackInfo payBackInfo) {
		logger.info("payStChClient.begin...." + JSON.toJSONString(payBackInfo));

		// 支付单号
		String paySn = payBackInfo.getPaySn();
		String actionNoteIn = payBackInfo.getActionNoteIn();
		// 支付流水号
		String payNote = payBackInfo.getPayNote();
		// 支付id
		String payId = payBackInfo.getPayId();
		// 支付金额
		double amountToPay = payBackInfo.getAmountToPay();
		if (actionNoteIn == null) {
			actionNoteIn = "";
		}
		StringBuilder actionNote = new StringBuilder(actionNoteIn + payNote);
		PayReturnInfo payReturn = new PayReturnInfo();

		String mergePaySn = null;
		try {
			try {
				String cacheResult = redisClient.get(paySn);
				if (StringUtil.isNotNull(cacheResult)) {
					payReturn.setMessage("5秒内重复提交！");
					return payReturn;
				}
				redisClient.set(paySn, paySn);
				redisClient.expire(paySn, 5);
			} catch (Exception e1) {
				logger.info("支付回调redis验证短时间重复提交异常：", e1);
				logger.error("支付回调redis验证短时间重复提交异常：", e1);
			}
			payReturn.setIsOk(Constant.YESORNO_YES);
			List<String> masterPaySnList = new ArrayList<String>();
			if (paySn.startsWith(Constant.OP_BEGIN_WITH_MFK)) {
				mergePaySn = paySn;
				MergeOrderPay op = mergeOrderPayMapper.selectByPrimaryKey(paySn);
				if (null == op) {
					payReturn.setIsOk(Constant.YESORNO_NO);
					payReturn.setMessage("合并付款单" + paySn+ "不存在，不能进行付款操作！");
				}

				op.setPayFee(new BigDecimal(amountToPay));
				op.setPayNote(actionNote.toString());
				op.setUserPayTime(new Date());
				op.setUpdateTime(new Date());
				op.setPayStatus((byte)Constant.OP_PAY_STATUS_PAYED);
				mergeOrderPayMapper.updateByPrimaryKeySelective(op);
				String[] masterPaySnStr = op.getMasterPaySn().split(Constant.STRING_SPLIT_COMMA);
				for (String masterPaySn : masterPaySnStr) {
					if (null != masterPaySn && masterPaySn.length() > 0) {
						masterPaySnList.add(masterPaySn);
					}
				}
				if (op.getMergePayFee().doubleValue() < amountToPay) {
					payReturn.setIsOk(Constant.YESORNO_NO);
				}
			} else {
				masterPaySnList.add(paySn);
			}

			MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
			masterOrderPayExample.or().andMasterPaySnIn(masterPaySnList);
			List<MasterOrderPay> payMasterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);

			List<String> orderSnList = new ArrayList<String>();
			for (MasterOrderPay masterOrderPay : payMasterOrderPayList) {
				orderSnList.add(masterOrderPay.getMasterOrderSn());
			}
			for (String orderSn : orderSnList) {
				try {
					MasterOrderInfo oInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn.trim());
					checkParam(payReturn, orderSn, paySn, payNote, amountToPay, oInfo);
					logger.info("payStChClient.checkParam.orderSn:"+orderSn+",paySn:"+paySn+",payReturn:"+JSON.toJSONString(payReturn));
					if (payReturn.getIsOk() == Constant.YESORNO_YES) {
						MasterOrderPayExample masterOrderUnPayExample = new MasterOrderPayExample();
						masterOrderUnPayExample.or().andMasterOrderSnEqualTo(orderSn).andPayStatusEqualTo((byte)Constant.OP_PAY_STATUS_UNPAYED);
						List<MasterOrderPay> unPayMasterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderUnPayExample);
						MasterOrderPay unPayMasterOrderPay = unPayMasterOrderPayList.get(0);
						double payFee = 0.00;
						if (paySn.startsWith(Constant.OP_BEGIN_WITH_MFK)) {
							payFee = unPayMasterOrderPay.getPayTotalfee().doubleValue();
						} else {
							payFee = amountToPay;
						}
						if (masterPaySnList.contains(unPayMasterOrderPay.getMasterPaySn())) {
							masterOrderPay(unPayMasterOrderPay.getMasterPaySn(), orderSn.trim(), mergePaySn, payId, payFee, actionNote);
							masterOrderActionService.insertOrderActionBySn(orderSn,"前台付款操作：付款单" + unPayMasterOrderPay.getMasterPaySn() + "付款操作;"+ actionNote.toString(), Constant.OS_STRING_SYSTEM);

							OrderManagementRequest request = new OrderManagementRequest();
							request.setSource(ConstantValues.METHOD_SOURCE_TYPE.FRONT);
							request.setActionUser(Constant.OS_STRING_SYSTEM);
							request.setActionUserId(Constant.CUSTOMER_PAY_LOCK_ID + "");
							request.setMasterOrderSn(orderSn);
							request.setMessage("用户支付后解锁！");
							orderManagementService.orderLock(request);

						} else {
							masterOrderActionService.insertOrderActionBySn(orderSn,"前台付款操作异常：未找到有效支付单，付款单" + unPayMasterOrderPay.getMasterPaySn() + "付款操作;"+ actionNote.toString(), Constant.OS_STRING_SYSTEM);
						}
					} else {
						masterOrderActionService.insertOrderActionBySn(orderSn,"前台付款操作异常：数据校验失败，付款单" + paySn + "付款操作;"+ actionNote.toString()+payReturn.getMessage(), Constant.OS_STRING_SYSTEM);
					}
				} catch (Exception e) {
					masterOrderActionService.insertOrderActionBySn(orderSn,"前台付款操作异常：" + paySn + "付款操作;"+ actionNote.toString()+e.toString(), Constant.OS_STRING_SYSTEM);
					logger.info("支付回调异常："+JSON.toJSONString(payBackInfo), e);
				}
			}
		} catch (Exception e) {
			logger.info("支付回调异常：" + JSON.toJSONString(payBackInfo), e);
		}
		return payReturn;
	}
	
	private void masterOrderPay(String masterPaySn, String masterOrderSn, String mergePaySn, String payId, double amountToPay, StringBuilder actionNote) {
		//实际付款金额 小于 应付款金额  
		MasterOrderPay op = masterOrderPayMapper.selectByPrimaryKey(masterPaySn);
		if (StringUtil.isNotEmpty(payId) && !payId.equals(op.getPayId() + "")) {
			payService.changeOrderPayMethod(masterPaySn, Integer.valueOf(payId), Constant.OS_STRING_SYSTEM);
		}
		if (op.getPayTotalfee().doubleValue() - amountToPay > 0) {
			logger.info("payStChClient.processPayClient.未付款.实际金额小于应付款金额.start.orderSn:" + masterOrderSn + ".panSn:" + masterPaySn + ",amountToPay:" + amountToPay + ",payTotalfee:" + op.getPayTotalfee());
			// 补款金额
			double fillingMoney = op.getPayTotalfee().doubleValue() - amountToPay;
			//创建已支付付款单和补款单
			List<MasterPay> masterPayList = new ArrayList<MasterPay>();
			MasterPay payInfo = new MasterPay();
			payInfo.setPayId(Integer.parseInt(payId));
			payInfo.setPayStatus(Constant.OP_PAY_STATUS_PAYED);
			payInfo.setPayTotalFee(amountToPay);
			payInfo.setPayTime(new Date());
			MasterPay nupayInfo = new MasterPay();
			nupayInfo.setPayId(Integer.parseInt(payId));
			nupayInfo.setPayTotalFee(fillingMoney);
			masterPayList.add(nupayInfo);
			masterPayList.add(payInfo);
			payService.createMasterPay(masterOrderSn, masterPayList);
			actionNote.append("<br/><font color='red'>实际付款金额不足,生成补付款付款单;补付款金额为" + fillingMoney + "</font>");
			// 更新订单已支付金额和订单待支付金额及订单支付状态
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn); 
			masterOrderInfo.setTotalPayable(masterOrderInfo.getTotalPayable().subtract(new BigDecimal(amountToPay)));
			masterOrderInfo.setMoneyPaid(masterOrderInfo.getMoneyPaid().add(new BigDecimal(amountToPay)));
			masterOrderInfo.setPayStatus(Constant.OI_PAY_STATUS_PARTPAYED);
			masterOrderInfoMapper.updateByPrimaryKeySelective(masterOrderInfo);
		} else {
			logger.info("payStChClient.processPayClient.未付款.实际金额大于等于应付款金额.start.orderSn:"+masterOrderSn+",panSn:"+masterPaySn+",amountToPay:"+amountToPay+",payTotalfee:"+op.getPayTotalfee());
			//实际支付金额 大于等于 应付金额
			if (op.getPayTotalfee().doubleValue() - amountToPay < 0) {
				// 原付款单的金额改变
				actionNote.append("<br/><font color='red'>多付款，" +masterPaySn + "的支付总金额" + op.getPayTotalfee() + "更为" + amountToPay + "</font>");
			}
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setAdminUser(ConstantValues.ACTION_USER_SYSTEM);
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setPaySn(masterPaySn);
			orderStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.FRONT);
			orderStatus.setMessage(actionNote.toString());
			orderStatus.setPayFee(amountToPay);
			orderStatus.setMergePaySn(mergePaySn);
			payService.payStCh(orderStatus);
		}
	}
	
	private PayReturnInfo checkParam(PayReturnInfo ri, String orderSn, String paySn, String payNote, double amountToPay, MasterOrderInfo oInfo) {
		if (StringUtil.isNull(orderSn)) {
			ri.setIsOk(Constant.YESORNO_NO);
			ri.setMessage(ri.getMessage() + "传入订单编号参数为空！");
			return ri;
		}
		if (StringUtil.isNull(paySn)) {
			ri.setIsOk(Constant.YESORNO_NO);
			ri.setMessage(ri.getMessage() + "传入付款单编号参数为空！");
			return ri;
		}
		if (amountToPay <= 0.00) {
			ri.setIsOk(Constant.YESORNO_NO);
			ri.setMessage(ri.getMessage() + "传入付款金额为" + amountToPay);
			return ri;
		}
		if (null == oInfo) {
			ri.setIsOk(Constant.YESORNO_NO);
			ri.setMessage(ri.getMessage() + "订单不存在!");
			return ri;
		}
		int os = oInfo.getOrderStatus();
		if (os == Constant.OI_ORDER_STATUS_CANCLED) {
			ri.setIsOk(Constant.YESORNO_NO);
			ri.setMessage(ri.getMessage() + "订单" + orderSn + "已取消，不能进行付款操作！");
			return ri;
		}
		if (os == Constant.OI_ORDER_STATUS_FINISHED) {
			ri.setIsOk(Constant.YESORNO_NO);
			ri.setMessage(ri.getMessage()+"订单" + orderSn + "已完成，不能进行付款操作！");
			return ri;
		}
		if (oInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED
				|| oInfo.getPayStatus() == Constant.OI_PAY_STATUS_SETTLED) {
			ri.setIsOk(Constant.YESORNO_NO);
			ri.setMessage(ri.getMessage()+"订单" + orderSn + "已付款,不能进行付款操作！");
			return ri;
		}
		if (oInfo.getTransType() == Constant.OI_TRANS_TYPE_PRESHIP) {
			if (os != Constant.OI_ORDER_STATUS_CONFIRMED) {
				ri.setIsOk(Constant.YESORNO_NO);
				ri.setMessage(ri.getMessage()+"订单" + orderSn+ "的交易类型为货到付款，同时不处于确定状态，所以不能进行付款操作！");
				return ri;
			}
		}
		MasterOrderGoodsExample example = new MasterOrderGoodsExample();
		example.or().andMasterOrderSnEqualTo(oInfo.getMasterOrderSn());
		List<MasterOrderGoods> osList = masterOrderGoodsMapper.selectByExample(example);
		if (null == osList || osList.size() == 0) {
			ri.setIsOk(Constant.YESORNO_NO);
			ri.setMessage(" 订单" + orderSn + "商品不存在，不能进行付款操作！");
			return ri;
		}
		
		return ri;
	}

    /**
     * 根据支付单号获取对应的订单号
     * @param paySn 支付单号
     * @return ApiReturnData<List<String>>
     */
    @Override
    public ApiReturnData<List<MasterOrderPay>> getOrderPaySnByMergePaySn(String paySn) {
        logger.info("根据支付单号查询订单号：paySn=" + paySn);
        ApiReturnData<List<MasterOrderPay>> data = new ApiReturnData<List<MasterOrderPay>>();
        data.setIsOk(Constant.OS_STR_NO);

        try {
            if (StringUtils.isBlank(paySn)) {
                data.setMessage("参数错误！");
                return data;
            } else {

                List<MasterOrderPay> orderPayList = new ArrayList<MasterOrderPay>(Constant.DEFAULT_LIST_SIZE);

                // 合并付款单编号-> 获取对于的付款单编号
                MergeOrderPay mergePay = mergeOrderPayMapper.selectByPrimaryKey(paySn);
                String[] masterPaySns = mergePay.getMasterPaySn().split(Constant.STRING_SPLIT_COMMA);
                for (String masterPaySn : masterPaySns) {
                    MasterOrderPay masterPay = masterOrderPayMapper.selectByPrimaryKey(masterPaySn);
                    orderPayList.add(masterPay);
                }

                data.setData(orderPayList);
                data.setIsOk(Constant.OS_STR_YES);
                data.setMessage("查询成功！");
            }
        } catch (Exception e) {
            logger.error("查询支付单异常,paySn:" + paySn,e);
            data.setMessage("查询支付单异常,paySn:" + paySn);
        }

        return data;
    }

	@Override
	public ApiReturnData<MasterOrderPay> getGroupBuyOrderPay(String masterOrderSn) {
		ApiReturnData<MasterOrderPay> returnData = new ApiReturnData<>();
		returnData.setIsOk("0");
		if (masterOrderSn.indexOf(",") > 0) {
			String[] masterOrderSns = masterOrderSn.split(",");
			MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByMasterOrderSn(masterOrderSns[0]);
			if (masterOrderPay == null || masterOrderPay.getMergePaySn() == null) {
				returnData.setMessage("合并支付单为空");
				return returnData;
			}
			MergeOrderPay mergeOrderPay = mergeOrderPayMapper.selectByPrimaryKey(masterOrderPay.getMergePaySn());
			if (masterOrderPay == null) {
				returnData.setMessage("合并支付单为空");
				return returnData;
			}
			masterOrderPay.setPrepayments(mergeOrderPay.getMergePayFee());
			masterOrderPay.setIsOperationConfirmPay((byte)-1);

			returnData.setData(masterOrderPay);
			returnData.setIsOk("1");
			return returnData;
		} else {

			MasterOrderInfoExtend infoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			if (infoExtend == null) {
				returnData.setMessage("订单查询有误");
				return returnData;
			}
			MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByMasterOrderSn(masterOrderSn);
			if (masterOrderPay != null) {
				masterOrderPay.setIsOperationConfirmPay(infoExtend.getIsOperationConfirmPay());
				returnData.setData(masterOrderPay);
				returnData.setIsOk("1");
				return returnData;
			}
		}
		return returnData;
	}

	/**
	 * 根据支付单号获取对应的订单号
	 * @param paySn 支付单号
	 * @return ApiReturnData<List<String>>
	 */
	@Override
	public ApiReturnData<List<String>> getOrderSnByPaySn(String paySn) {
		logger.info("根据支付单号查询订单号：paySn=" + paySn);
		ApiReturnData<List<String>> data = new ApiReturnData<List<String>>();
		data.setIsOk(Constant.OS_STR_NO);

		try {
			if (StringUtils.isBlank(paySn)) {
				data.setMessage("参数错误！");
				return data;
			} else {

				List<String> orderSns = new ArrayList<String>(Constant.DEFAULT_LIST_SIZE);
				if (paySn.startsWith(Constant.ORDER_PAY_MFK)) {
					// 合并付款单编号-> 获取对于的付款单编号
					MergeOrderPay mergePay = mergeOrderPayMapper.selectByPrimaryKey(paySn);
					String[] masterPaySns = mergePay.getMasterPaySn().split(Constant.STRING_SPLIT_COMMA);
					for (String masterPaySn : masterPaySns) {
						MasterOrderPay masterPay = masterOrderPayMapper.selectByPrimaryKey(masterPaySn);
						orderSns.add(masterPay.getMasterOrderSn());
					}
				} else {
					// 付款单编号
					MasterOrderPay masterPay = masterOrderPayMapper.selectByPrimaryKey(paySn);
				    orderSns.add(masterPay.getMasterOrderSn());
				}
				data.setData(orderSns);
				data.setIsOk(Constant.OS_STR_YES);
				data.setMessage("查询成功！");
			}
		} catch (Exception e) {
			logger.error("查询商品发生异常,paySn:" + paySn,e);
			data.setMessage("查询商品发生异常,paySn:" + paySn);
		}
		
		return data;
	}

}
