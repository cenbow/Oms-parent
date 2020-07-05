package com.work.shop.oms.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.GoodsCardInfo;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.common.bean.OcpbStatus;
import com.work.shop.oms.common.bean.OptionPointsBean;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.common.bean.ShopPayCashBean;
import com.work.shop.oms.common.bean.ShopPayResultBean;
import com.work.shop.oms.common.bean.SystemInfo;
import com.work.shop.oms.common.bean.ValidateOrder;
import com.work.shop.oms.common.utils.NumberUtil;
import com.work.shop.oms.config.service.SystemPaymentService;
import com.work.shop.oms.config.service.SystemShippingService;
import com.work.shop.oms.dao.BoSupplierContractMapper;
import com.work.shop.oms.dao.BoSupplierOrderMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.SystemConfigMapper;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderAddressInfoService;
import com.work.shop.oms.order.service.MasterOrderInfoExtendService;
import com.work.shop.oms.order.service.OrderValidateService;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import com.work.shop.oms.orderop.service.UserPointsService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.shoppay.service.ShopPayService;
import com.work.shop.oms.stock.service.ChannelStockService;
import com.work.shop.oms.user.account.UserAccountService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.OrderAttributeUtil;
import com.work.shop.oms.utils.StringUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单校验服务
 * @author QuYachu
 */
@Service
public class OrderValidateServiceImpl implements OrderValidateService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "redisClient")
	private RedisClient redisClient;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private SystemShippingService systemShippingService;
	@Resource
	private SystemPaymentService systemPaymentService;

	@Resource(name="orderQuestionService")
	private OrderQuestionService orderQuestionService;

	@Resource
	private UserPointsService userPointsService;
	@Resource(name="masterOrderActionServiceImpl")
	private MasterOrderActionService masterOrderActionService;
	@Resource
	private SystemConfigMapper systemConfigMapper;
	@Resource
	private MasterOrderAddressInfoService addressInfoService;

	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	@Resource(name = "shopPayServiceImpl")
	private ShopPayService shopPayService;

	@Resource
	private ChannelStockService channelStockService;

	@Resource
	private UserAccountService userAccountService;
	@Resource
	private MasterOrderInfoExtendService orderInfoExtendService;
	@Resource
	private BoSupplierOrderMapper boSupplierOrderMapper;
	@Resource
	private BoSupplierContractMapper boSupplierContractMapper;
	// 没有问题
	public static Integer QUESTION_TYPE_NONE = 0;
	// 订单部分商品低于保底价或者限定折扣价
	public static Integer QUESTION_TYPE_DIYUZKJ = 1;
	// 已付款订单商品成交价与已付款有差异
	public static Integer QUESTION_TYPE_PAYCY = 2;

	public static String filterProvinceIds = "710000|810000|820000|990000";

	/**
	 * 验证订单数据
	 * @param masterOrder 订单信息
	 * @return ServiceReturnInfo<MasterOrder>
	 */
	@Override
	public ServiceReturnInfo<MasterOrder> validate(MasterOrder masterOrder) {
		ServiceReturnInfo<MasterOrder> valiIdateInfo = null;
		if (StringUtil.isNotEmpty(masterOrder.getOrderOutSn())) {
			long result = -1;
			String orderOutSnKey = "order_out_sn_" + masterOrder.getOrderFrom() + "_" + masterOrder.getOrderOutSn();
			try {
				result = redisClient.setnx(orderOutSnKey, masterOrder.getOrderOutSn());
				// redis 超时时间有10秒
				redisClient.expire(orderOutSnKey, 10);
			} catch (Throwable e) {
				logger.error(masterOrder.getOrderOutSn() + "判断外部交易号重复,读取redis异常:" + e.getMessage(), e);
			}
			if (result == 0) {
				valiIdateInfo = new ServiceReturnInfo<MasterOrder>("短时间内同一交易号" + masterOrder.getOrderOutSn() + "进入转单接口！");
				return valiIdateInfo;
			}
			MasterOrderInfo oi = ckeckOrderOutSnIsExsitString(masterOrder.getOrderOutSn(), masterOrder.getOrderFrom());
			if (oi != null) {
				valiIdateInfo = new ServiceReturnInfo<MasterOrder>(oi.getOrderFrom() + " :" + oi.getOrderOutSn() + "交易号已转过OS订单！");
				return valiIdateInfo;
			}
		}
		// 验证用户
		if (StringUtil.isEmpty(masterOrder.getUserId())) {
			return new ServiceReturnInfo<MasterOrder>("下单人ID不能为空！");
		}

		if (masterOrder.getTransType() == null) {
			return new ServiceReturnInfo<MasterOrder>("交易类型不能为空！");
		}

		if (masterOrder.getTransType() < 1 || masterOrder.getTransType() > 3) {
			return new ServiceReturnInfo<MasterOrder>("交易类型不存在！1~3");
		}

		if (masterOrder.getOrderFrom() == null) {
			return new ServiceReturnInfo<MasterOrder>("订单来源不能为空！");
		}

		List<MasterShip> ship = masterOrder.getShipList();

		if (ship == null || ship.isEmpty()) {
			return new ServiceReturnInfo<MasterOrder>("没有配送信息");
		}

		/** 检查发货单参数是否为空 */
		for (MasterShip masterShip : ship) {
			valiIdateInfo = checkShip(masterShip, masterOrder);
			if (!valiIdateInfo.isIsok()) {
				return valiIdateInfo;
			}
		}
		List<MasterPay> pay = masterOrder.getPayList();
		valiIdateInfo = checkPayList(pay, masterOrder);
		if (valiIdateInfo != null && !valiIdateInfo.isIsok()) {
			return valiIdateInfo;
		}
		return new ServiceReturnInfo<MasterOrder>(masterOrder);
	}

	@Override
	public ServiceReturnInfo<List<MasterOrder>> validateOrders(List<MasterOrder> masterOrders) {
		logger.info("CreateOrderJson:" + JSON.toJSONString(masterOrders));
		if (StringUtil.isListNull(masterOrders)) {
			return new ServiceReturnInfo<List<MasterOrder>>("提交订单数据为空");
		}
		for (MasterOrder masterOrder : masterOrders) {
			ServiceReturnInfo<MasterOrder> returnInfo = validate(masterOrder);
			if (!returnInfo.isIsok()) {
				return new ServiceReturnInfo<List<MasterOrder>>("店铺[" + masterOrder.getOrderFrom() + "]" + returnInfo.getMessage());
			}
		}
		return new ServiceReturnInfo<List<MasterOrder>>(masterOrders);

	}

	@Override
	public ServiceReturnInfo<MasterOrder> orderFormat(String orderInfoStr) {
		ServiceReturnInfo<MasterOrder> validateinfo = null;
		// 如果传入订单信息为空
		if (StringUtil.isBlank(orderInfoStr)) {
			validateinfo = new ServiceReturnInfo<MasterOrder>("订单参数不能为空");
			return validateinfo;
		}
		// 反序列化订单
		MasterOrder masterOrder = formatJson(orderInfoStr);
		// 反序列化订单失败
		if (masterOrder == null) {
			validateinfo = new ServiceReturnInfo<MasterOrder>("传入订单参数有误，无法正确转化订单参数");
			return validateinfo;
		}
		return new ServiceReturnInfo<MasterOrder>(masterOrder);
	}

	@Override
	public ServiceReturnInfo<List<MasterOrder>> orderListFormat(String orderInfoStr) {
		ServiceReturnInfo<List<MasterOrder>> validateinfo = null;
		// 如果传入订单信息为空
		if (StringUtil.isBlank(orderInfoStr)) {
			validateinfo = new ServiceReturnInfo<List<MasterOrder>>("订单参数不能为空");
			return validateinfo;
		}
		// 反序列化订单
		List<MasterOrder> masterOrders = formatJsonList(orderInfoStr);
		// 反序列化订单失败
		if (StringUtil.isListNull(masterOrders)) {
			validateinfo = new ServiceReturnInfo<List<MasterOrder>>("传入订单参数有误，无法正确转化订单参数");
			return validateinfo;
		}
		return new ServiceReturnInfo<List<MasterOrder>>(masterOrders);
	}

	@Override
	public ReturnInfo validateOrderInfo(String masterOrderSn, ValidateOrder validateOrder ,OcpbStatus ocpbStatus, int qt) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		MasterOrderInfo orderInfo = this.masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (orderInfo == null) {
			info.setMessage("订单[" + masterOrderSn + "]不存在");
			return info;
		}
		List<MasterOrderInfoExtend> masterOrderInfoExtendByOrder = orderInfoExtendService.getMasterOrderInfoExtendByOrder(masterOrderSn);
		if (CollectionUtils.isEmpty(masterOrderInfoExtendByOrder)) {
			info.setMessage("订单[" + masterOrderSn + "]的扩展信息不存在");
			return info;
		}
		MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendByOrder.get(0);
		// 余额锁定判断
		if (!ocpbStatus.equals(OcpbStatus.lock)) {
			// 使用余额支付
			if (validateOrder.getSurplus().floatValue() > 0) {
				ShopPayCashBean cash = new ShopPayCashBean();
				cash.setOrderNo(masterOrderSn);
				cash.setUserName(orderInfo.getUserId());
				cash.setMoney(validateOrder.getSurplus().floatValue());
				cash.setType(2);
				cash.setOrderType(6);
				cash.setOrderSource("13");
				cash.setSmsFlag(validateOrder.getSmsFlag());
				cash.setSmsCode(validateOrder.getSmsFlag());
				cash.setMemo("订单[" + masterOrderSn + "]下单成功后扣减冻结余额：" + validateOrder.getSurplus());
				ShopPayResultBean apiBack = shopPayService.payService(orderInfo.getUserId(), masterOrderSn, cash);
				logger.info("余额扣减：masterOrderSn:" + masterOrderSn + ";userId:" + orderInfo.getUserId()
						+ " apiBack:" + JSON.toJSONString(apiBack));
				if (apiBack == null || apiBack.getStatus() == 1) {
					String payMsg = apiBack.getMsg();
					if (payMsg.contains("lock")) {
						logger.error(masterOrderSn + "余额账户：" + orderInfo.getUserId() + "账户锁定");
						ocpbStatus = OcpbStatus.lock;
					}
				} else {
					MasterOrderAction orderAction = masterOrderActionService.createOrderAction(orderInfo);
					orderAction.setActionNote("余额[" + validateOrder.getSurplus() + "]扣减成功,流水号[" + apiBack.getResult() + "]");
					masterOrderActionService.insertOrderActionByObj(orderAction);
					MasterOrderPayExample example = new MasterOrderPayExample();
					example.or().andPayIdEqualTo((byte)3).andMasterOrderSnEqualTo(masterOrderSn)
						.andPayStatusNotEqualTo((byte)Constant.OP_PAY_STATUS_COMFIRM);
					MasterOrderPay updatePay = new MasterOrderPay();
					updatePay.setPayNote(apiBack.getResult());
					masterOrderPayMapper.updateByExampleSelective(updatePay, example);
				}
			}
		} else {
			logger.debug(masterOrderSn + "余额账户锁定");
		}
		// 余额锁定问题单
		if (ocpbStatus.equals(OcpbStatus.lock)) {
			orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "用户" + orderInfo.getUserId()
					+ "余额账户锁定问题单", "9974"));
			orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
		}
		//外部公司购买自营商品如果使用的铁信支付的话要转成问题单去改价
		//这里并没有判断商品是否是自营的，靠的是前端等限制
		MasterOrderPay masterOrderPay = masterOrderPayMapper.selectByMasterOrderSn(masterOrderSn);
		if (masterOrderPay == null) {
			info.setMessage("订单[" + masterOrderSn + "]的支付单不存在");
			return info;
		}
		//盈合问题单
		if(StringUtils.isNotBlank(masterOrderInfoExtend.getBoId())) {
			//设置盈合问题单
			orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "盈合问题单", "123"));
			orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
			//设置营销合伙人系统的子项目
			BoSupplierOrder  boSupplierOrder = new BoSupplierOrder();
			boSupplierOrder.setMasterOrderSn(masterOrderSn);
			boSupplierOrder.setBoId(masterOrderInfoExtend.getBoId());
			boSupplierOrder.setCompanyCode(masterOrderInfoExtend.getCompanyCode());
			boSupplierOrder.setCompanyName(masterOrderInfoExtend.getCompanyName());
			boSupplierOrder.setPayId(Integer.valueOf(masterOrderPay.getPayId().toString()));
			boSupplierOrder.setPayName(masterOrderPay.getPayName());
			boSupplierOrder.setCreateUser(Constant.OS_STRING_SYSTEM);
			boSupplierOrder.setCreateTime(new Date());
			boSupplierOrder.setUpdateUser(Constant.OS_STRING_SYSTEM);
			boSupplierOrder.setUpdateTime(new Date());
			//查询商业合伙人供应商合同  如果只有一个默认写入对应子公司id
			List<BoSupplierContract> boSupplierContracts = boSupplierContractMapper.selectByBoId(masterOrderInfoExtend.getBoId());
			if(boSupplierContracts != null && boSupplierContracts.size()==1){
				boSupplierOrder.setChildCompanyId(boSupplierContracts.get(0).getChildCompanyId());
			}
			boSupplierOrderMapper.insertSelective(boSupplierOrder);
		}
		//获取铁信支付的payId
		SystemPayment systemPayment = systemPaymentService.selectSystemPayByCode(Constant.PAY_TIEXIN);
		if (masterOrderPay.getPayId().equals(systemPayment.getPayId())) {
			//是铁信支付，获取订单扩展信息判断是否外部公司
			if (Constant.OUTSIDE_COMPANY.equals(masterOrderInfoExtendByOrder.get(0).getCompanyType())) {
				//是外部买家创建问题单
				orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "铁信支付改价问题单", "122"));
				orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
			}
		}
		//盈合支付的问题单判断  暂时只处理设置了盈合id的
		//,,
		//待询价 或者改价问题单 或者 是盈合商品
		if(orderInfo.getGoodsSaleType() != null && orderInfo.getGoodsSaleType() != 0){
			switch (orderInfo.getGoodsSaleType()){
				case Constant.GOODS_SALE_TYPE_CUSTOMIZATION :
					orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "待询价问题单", "120"));
					orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
					break;
				case Constant.GOODS_SALE_TYPE_CHANGE_PRICE :
					orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "改价问题单", "121"));
					orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
					break;
				case Constant.GOODS_SALE_TYPE_BO :
					//暂时只处理设置了盈合id的
					/*orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "盈合问题单", "123"));
					orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
					//盈合商品需要处理 盈合子项目
					if(StringUtils.isBlank(masterOrderInfoExtendByOrder.get(0).getBoId())) {
						info.setMessage("订单[" + masterOrderSn + "]的盈合ID不存在");
						return info;
					}*/
					break;
			}
		}

		// 订单创建成功后积分扣减
		if (orderInfo.getIntegral().intValue() > 0) {
			Integer integral = orderInfo.getIntegral();
			// 冻结款项,如果冻结时，余额不够，则订单不能生成
			boolean process = false;
			try {
				ReturnInfo returnInfo = userPointsService.deductionsPoints(orderInfo.getUserId(), integral, masterOrderSn);
				String pointsNote = null;
				if(returnInfo.getIsOk() > 0){
					process = true;
					logger.debug("订单创建-扣减时CAS积分("+integral+")扣减成功！orderSn:" + masterOrderSn +",userId:"+orderInfo.getUserId());
					pointsNote = "订单创建-扣减时CAS积分("+ integral +")扣减成功！";
				} else {
					logger.error("订单创建-扣减时CAS积分("+integral+")扣减失败！orderSn:"+ masterOrderSn +",userId:" + orderInfo.getUserId());
					pointsNote = "订单创建-扣减时CAS积分("+integral+")扣减失败！错误信息:"+returnInfo.getMessage();
				}
				MasterOrderAction orderAction = masterOrderActionService.createOrderAction(orderInfo);
				orderAction.setActionNote(pointsNote);
				masterOrderActionService.insertOrderActionByObj(orderAction);
			} catch (Exception e) {
				logger.error(masterOrderSn + "用户ID：" + orderInfo.getUserId() + "订单创建-扣减时CAS积分("+integral+")扣减失败", e);
			}
			if (!process) {
				// 处理失败，设置问题单
				orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "用户" + orderInfo.getUserId()
						+ "积分账户扣减异常问题单", "9975"));
				orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
			}
		}
		// 订单创建成功后扣减点数
		if (validateOrder.getPoints().doubleValue() > 0.0) {
			Double points = validateOrder.getPoints();
			String pointsNote = null;
			try {
				OptionPointsBean pointsBean = new OptionPointsBean(orderInfo.getUserId(), orderInfo.getTotalFee().doubleValue(),
						masterOrderSn, points, orderInfo.getBvValue(), 1);
				ReturnInfo<Integer> returnInfo = userPointsService.optionPoints(pointsBean);
				if(returnInfo.getIsOk() == Constant.OS_YES){
					logger.debug("订单创建-点数("+ points + ")冻结成功！orderSn:" + masterOrderSn +",userId:"+orderInfo.getUserId());
					pointsNote = "订单创建-点数("+ points + ")冻结成功！流水号:" + returnInfo.getOrderOutSn();
					masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
					pointsBean.setOrderType(3);
					ReturnInfo<Integer> userPointsInfo = userPointsService.optionPoints(pointsBean);
					if(userPointsInfo.getIsOk() == Constant.OS_YES){
						MasterOrderPay updatePay = new MasterOrderPay();
						updatePay.setPayNote(userPointsInfo.getOrderOutSn());
						MasterOrderPayExample payExample = new MasterOrderPayExample();
						SystemPayment payment = systemPaymentService.selectSystemPayByCode("points");
						if (payment != null) {
							payExample.or().andMasterOrderSnEqualTo(masterOrderSn).andPayIdEqualTo(payment.getPayId());
							masterOrderPayMapper.updateByExampleSelective(updatePay, payExample);
						}
						logger.debug("订单创建-点数("+ points + ")消费成功！orderSn:" + masterOrderSn +",userId:"+orderInfo.getUserId());
						pointsNote = "订单创建-点数("+ points + ")消费成功！流水号:" + userPointsInfo.getOrderOutSn();
						masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
					} else {
						logger.error("订单创建-点数(" + points + ")消费失败！orderSn:"+ masterOrderSn +",userId:" + orderInfo.getUserId());
						pointsNote = "订单创建-点数(" + points + ")消费失败！错误信息:"+ userPointsInfo.getMessage();
						masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
						orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "用户" + orderInfo.getUserId()
								+ "点数消费异常", "9978"));
						orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
					}
				} else {
					logger.error("订单创建-点数(" + points + ")冻结失败！orderSn:"+ masterOrderSn +",userId:" + orderInfo.getUserId());
					pointsNote = "订单创建-点数(" + points + ")冻结失败！错误信息:"+returnInfo.getMessage();
					masterOrderActionService.insertOrderActionBySn(masterOrderSn, pointsNote, Constant.OS_STRING_SYSTEM);
					// 处理失败，设置问题单
					orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "用户" + orderInfo.getUserId()
							+ "点数冻结异常", "9977"));
					orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
				}
			} catch (Exception e) {
				logger.error(masterOrderSn + "用户ID：" + orderInfo.getUserId() + "订单创建-点数("+points+")失败" + e.getMessage(), e);
				pointsNote = "订单创建-点数(" + points + ")失败！错误信息:" + e.getMessage();
			}
		}

		// 拆单后占用库存标志
		boolean confirm = false;
		// 团购订单下单占用库存
//		if (validateOrder.getIsGroup() == 1) {
			// 如果是预售商品则设置问题单
//			if (validateOrder.getIsAdvance() == 1) {
//				orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "团购订单含预售商品问题单", "998"));
//				orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
//			}
//		}

		boolean flag = true;
		// 商品数量大于100 金额大于10000 问题单
		/*boolean flag = orderInfo.getTotalFee().doubleValue() < 10000 && orderInfo.getGoodsCount().doubleValue() < 100;
		if (orderInfo.getChannelCode().equals(Constant.Chlitina)) {
			flag = true;
		}
		if (!flag) {
			orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "大额待确认问题单", "886"));
			orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
		}*/
		// 差异超过一分钱
		if (orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED) {
			// 已支付订单通知支付预占
			channelStockService.payOccupy(masterOrderSn);
			if (Math.abs(this.subPrice(validateOrder.getOrderSettlementPrice(), validateOrder.getPaySettlementPrice())) > 0.01
					|| Math.abs(this.subPrice(validateOrder.getOrderSettlementPrice(),
							(validateOrder.getGoodsSettlementPrice() + orderInfo.getShippingTotalFee().doubleValue()))) > 0.01) {

				logger.info("差异超过一分钱:" + JSONObject.toJSONString(validateOrder));
				orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "已付款订单商品成交价与已付款有差异", "9973"));
				orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
			}
		}

		// 信用支付
		/*MasterOrderPayExample payExample = new MasterOrderPayExample();
		payExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(payExample);
		if (masterOrderPayList != null && masterOrderPayList.size() > 0) {
			MasterOrderPay masterOrderPay = masterOrderPayList.get(0);

			int payId = masterOrderPay.getPayId().intValue();
			//if (payId == Constant.PAYMENT_XINYONG_ID || payId == Constant.PAYMENT_BAOHAN_ID) {
			if (payId == Constant.PAYMENT_BAOHAN_ID) {

				BigDecimal payTotalFee = masterOrderPay.getPayTotalfee();
				// 信用支付、保函支付
				UserAccountBean userAccountBean = new UserAccountBean();
				userAccountBean.setOrderNo(masterOrderSn);
				userAccountBean.setUserId(orderInfo.getUserId());
				userAccountBean.setType(2);
				userAccountBean.setActionUser("oms");
				userAccountBean.setMoney(payTotalFee);

				if (payId == Constant.PAYMENT_BAOHAN_ID) {
					userAccountBean.setAccountType(1);
				}
				ReturnInfo<Boolean> accountReturnInfo = userAccountService.doReduceUserAccount(userAccountBean);

				String accountText = "";
				if (payId == Constant.PAYMENT_XINYONG_ID) {
                    accountText = "铁付通";
                } else if (payId == Constant.PAYMENT_BAOHAN_ID) {
                    accountText = "银行保函";
                }
				if (accountReturnInfo != null && accountReturnInfo.getIsOk() == Constant.OS_YES) {
					String note = "订单创建-" + accountText + "("+ payTotalFee + ")消费成功！";
					masterOrderActionService.insertOrderActionBySn(masterOrderSn, note, Constant.OS_STRING_SYSTEM);
				} else {
                    String note = "订单创建-" + accountText + "("+ payTotalFee + ")消费失败！错误信息:" + accountReturnInfo.getMessage();
                    masterOrderActionService.insertOrderActionBySn(masterOrderSn, note, Constant.OS_STRING_SYSTEM);
                    // 处理失败，设置问题单
                    orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "用户" + orderInfo.getUserId()
                            + "扣减" + accountText + "异常", "9979"));
                    orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
				}
			}
		}*/

		// 正常订单
		if (orderInfo.getOrderType() == Constant.OI_ORDER_TYPE_NORMAL_ORDER) {
			// 担保交易
			if (orderInfo.getTransType() == Constant.OI_TRANS_TYPE_GUARANTEE) {
				// 已付款
				if (orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED) {
					confirm = true;
				} else {
					orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "渠道订单付款金额与订单金额不符！", "9971"));
					orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
				}
			}
			// 如果是款到发货，且已付款，满足条件，则自动确认
			// 款到发货
			if (orderInfo.getTransType() == Constant.OI_TRANS_TYPE_PREPAY) {
				if (flag && orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED ) {
					if (orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED) {
						confirm = true;
					}
				}
			}
			MasterOrderAddressInfo addressInfo = this.addressInfoService.selectAddressInfo(masterOrderSn);
			if (addressInfo != null) {
				if (StringUtil.isTrimEmpty(addressInfo.getCountry()) || "0".equals(addressInfo.getCountry())
						|| StringUtil.isTrimEmpty(addressInfo.getProvince()) || "0".equals(addressInfo.getProvince())
						|| StringUtil.isTrimEmpty(addressInfo.getCity()) || "0".equals(addressInfo.getCity())
						) {
					confirm = false;
					orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "收货地址不全问题单", "993"));
					orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
				} else if ("1".equals(addressInfo.getCountry())
						&&addressInfo.getProvince().matches(filterProvinceIds)) {
					confirm = false;
					orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "港澳台收货地址问题单", "9931"));
					orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
				} else if (!"1".equals(addressInfo.getCountry())) {
					confirm = false;
					orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "国外收货地址问题单", "9931"));
					orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
				}
			}
		}

		// 订单是正常单且订单符合确认条件
		if (confirm && orderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
			info.setIsOk(Constant.OS_YES);
			info.setMessage("订单符合确认条件");
		} else {
			info.setMessage("订单不符合确认条件");
		}
		return info;
	}

    /**
     * 将商品拆分到行
     * @param masterOrder 订单信息
     * @return ServiceReturnInfo<MasterOrder>
     */
	public ServiceReturnInfo<MasterOrder> splitGoods(MasterOrder masterOrder) {
		logger.debug("将多件一行商品拆分到一件一行 start");
		int index = 1;
		String useCards = "";

		for (MasterShip masterShip : masterOrder.getShipList()) {
			List<MasterGoods> goodsList = new ArrayList<MasterGoods>();
			Map<String, List<GoodsCardInfo>> cardInfoMap = null;
			if (StringUtil.isListNotNull(masterShip.getCardInfos())) {
				cardInfoMap = new HashMap<String, List<GoodsCardInfo>>();
				for (GoodsCardInfo cardInfo : masterShip.getCardInfos()) {
					List<GoodsCardInfo> tempCardInfos = cardInfoMap.get(cardInfo.getCustomCode());
					if (StringUtil.isListNull(tempCardInfos)) {
						tempCardInfos = new ArrayList<GoodsCardInfo>();
					}
					tempCardInfos.add(cardInfo);
					cardInfoMap.put(cardInfo.getCustomCode(), tempCardInfos);
				}
			}

			// 红包金额
			Double bonus = masterOrder.getBonus();
			int bonusGoodsNum = 0;
			if (bonus != null && bonus > 0) {
				for (MasterGoods goods : masterShip.getGoodsList()) {
					Double shareBonus = goods.getShareBonus();
					if (shareBonus != null && shareBonus > 0) {
						bonusGoodsNum += goods.getGoodsNumber();
					}
				}
			}

			for (MasterGoods goods : masterShip.getGoodsList()) {
				int goodsNumber = goods.getGoodsNumber();
				if (goodsNumber > 1) {
					// 商品总折扣
					Double totalDisCount = NumberUtil.getDoubleByValue(goods.getDisCount(), 6);

					Double goodsDisCount = 0d;
					for (int i = 0; i < goodsNumber; i++) {
						MasterGoods newGoods = new MasterGoods();
						try {
							PropertyUtils.copyProperties(newGoods, goods);
						} catch (Exception e) {
							logger.error("复制MasterGoods属性异常" + e.getMessage(), e);
						}
						if (cardInfoMap != null) {
							List<GoodsCardInfo> cardInfos = cardInfoMap.get(goods.getCustomCode());
							if (StringUtil.isListNotNull(cardInfos)) {
								GoodsCardInfo cardInfo = cardInfos.get(0);
//								newGoods.setMarketPrice(cardInfo.getMarketPrice());
//								newGoods.setGoodsPrice(cardInfo.getGoodsPrice());
//								newGoods.setTransactionPrice(cardInfo.getTransactionPrice());
								newGoods.setUseCard(cardInfo.getCardCouponNo());
								cardInfos.remove(0);
								cardInfoMap.put(goods.getCustomCode(), cardInfos);
								if (!StringUtil.isTrimEmpty(cardInfo.getCardCouponNo())) {
									useCards += cardInfo.getCardCouponNo() + Constant.STRING_SPLIT_COMMA;
								}
							}
						}
						if (goods.getSendNumber().intValue() > 0) {
							newGoods.setSendNumber(1);
						}
						newGoods.setGoodsNumber(1);
						newGoods.setExtensionId(index + "");

						// 折扣处理,一分钱问题
						if (totalDisCount != null && totalDisCount > 0) {
							if (i + 1 == goodsNumber) {
								// 最后一项
								double newDiscount = NumberUtil.getDoubleByValue(totalDisCount - goodsDisCount, 6);
								if (newDiscount < 0) {
									newDiscount = 0;
								}
								newGoods.setDisCount(newDiscount);
								if (newDiscount > 0) {
									// 成交价
									double transactionPrice = NumberUtil.getDoubleByValue(newGoods.getGoodsPrice() - newDiscount, 6);
									newGoods.setTransactionPrice(transactionPrice);
								}

								//logger.info("newGoods:" + JSONObject.toJSONString(newGoods));
							} else {
								double newDiscount = NumberUtil.getDoubleByValue(newGoods.getGoodsPrice() - newGoods.getTransactionPrice(), 6);
								newGoods.setDisCount(newDiscount);
								goodsDisCount += newDiscount;

								//logger.info("newGoods:" + JSONObject.toJSONString(newGoods));
							}
						}

						// 红包平摊,一分钱问题
						Double shareBonus = newGoods.getShareBonus();
						if (shareBonus != null && shareBonus > 0) {
							if (1 == bonusGoodsNum) {
								// 最后一项
								newGoods.setShareBonus(bonus);
							} else {
								bonus = NumberUtil.getDoubleByValue(bonus - shareBonus, 6);
								bonusGoodsNum--;
							}
						}
						goodsList.add(newGoods);
						index++;
					}
				} else {
					if (cardInfoMap != null) {
						List<GoodsCardInfo> cardInfos = cardInfoMap.get(goods.getCustomCode());
						if (StringUtil.isListNotNull(cardInfos)) {
							GoodsCardInfo cardInfo = cardInfos.get(0);
							goods.setMarketPrice(cardInfo.getMarketPrice());
							goods.setGoodsPrice(cardInfo.getGoodsPrice());
							goods.setTransactionPrice(cardInfo.getTransactionPrice());
							goods.setUseCard(cardInfo.getCardCouponNo());
							cardInfos.remove(0);
							cardInfoMap.put(goods.getCustomCode(), cardInfos);
							if (!StringUtil.isTrimEmpty(cardInfo.getCardCouponNo())) {
								useCards += cardInfo.getCardCouponNo() + Constant.STRING_SPLIT_COMMA;
							}
						}
					}
					goods.setExtensionId(index + "");
					goodsList.add(goods);
					index++;

					// 红包平摊,一分钱问题
					Double shareBonus = goods.getShareBonus();
					if (shareBonus != null && shareBonus > 0) {
						if (1 == bonusGoodsNum) {
							// 最后一项
							goods.setShareBonus(bonus);
						} else {
							bonus = NumberUtil.getDoubleByValue(bonus - shareBonus, 6);
							bonusGoodsNum--;
						}
					}
				}
			}
			masterShip.setGoodsList(goodsList);
			//logger.info("goodsList:" + JSONObject.toJSONString(goodsList));
		}
		logger.debug("将多件一行商品拆分到一件一行 end");
		masterOrder.setUseCards(useCards);
		return new ServiceReturnInfo<MasterOrder>(masterOrder);
	}

	public void setQuestionOrder(MasterOrderInfo master) {
		try {
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionNote("订单[" + master.getMasterOrderSn() + "]生成问题单，可能是预售商品或者是没有该商品");
			masterOrderActionService.insertOrderActionByObj(orderAction);
			orderQuestionService.questionOrderByMasterSn(master.getMasterOrderSn(), new OrderStatus(master.getMasterOrderSn(), "预售商品或者是没有该商品", "20"));
		} catch (Throwable t) {
			logger.error("订单[" + master.getMasterOrderSn() + "]生成问题单异常" + t.getMessage(), t);
		}
	}

	/**
	 * 获取系统配置变量
	 *
	 * @param code
	 *            代码
	 * @return
	 */
	private SystemConfig getSystemConfig(String code) {
		try {
			SystemConfigExample example = new SystemConfigExample();
			example.or().andCodeEqualTo(code);
			List<SystemConfig> systemConfigs = systemConfigMapper.selectByExampleWithBLOBs(example);
			if (systemConfigs == null || systemConfigs.isEmpty())
				return null;
			return systemConfigs.get(0);
		} catch (Exception e) {
			logger.error("获取系统配置变量异常", e);
		}
		return null;
	}

	private boolean getPriceSwitch(SystemConfig systemConfig) {
		if (systemConfig == null)
			return false;
		if (StringUtil.isEmpty(systemConfig.getValue()))
			return false;
		if (!"0".equals(systemConfig.getValue().trim())) {
			return true;
		}
		return false;
	}

	@Override
	public String errorMessage(SystemInfo systemInfo, ServiceReturnInfo<?> serviceReturnInfo) {
		OrderCreateReturnInfo returninfo = new OrderCreateReturnInfo();
		returninfo.setIsOk(0);
		returninfo.setMessage(serviceReturnInfo.getMessage());
		/*if (returninfo.getDepotInfos().isEmpty()) {
			returninfo.setDepotInfos(null);
		}*/
		String s = JSON.toJSONString(serviceReturnInfo.getResult());
		logger.error("订单生成：" + s);
		try {
			return URLEncoder.encode(s, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String successMessage(SystemInfo systemInfo, ServiceReturnInfo<?> serviceReturnInfo) {
		String s = JSON.toJSONString(serviceReturnInfo.getResult());
		logger.info("订单生成：" + s);
		try {
			return URLEncoder.encode(s, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 反序列化Order Json字符串
	 * @param orderParam 订单JSON字符串
	 * @return Order Object
	 */
	public MasterOrder formatJson(String orderParam) {
		try {
			String str = URLDecoder.decode(orderParam, "UTF-8");
//			str = new String(str.getBytes("iso8859-1"), "UTF-8");
			MasterOrder o = JSON.parseObject(str, MasterOrder.class);
			return o;
		} catch (Exception e) {
			logger.error("反序列化Order Json字符串异常" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 反序列化Order Json字符串
	 * @param orderParam 订单JSON字符串
	 * @return Order Object
	 */
	public List<MasterOrder> formatJsonList(String orderParam) {
		try {
			String str = URLDecoder.decode(orderParam, "UTF-8");
//			str = new String(str.getBytes("iso8859-1"), "UTF-8");
			List<MasterOrder> o = JSON.parseArray(str, MasterOrder.class);
			return o;
		} catch (Exception e) {
			logger.error("反序列化Order Json字符串异常" + e.getMessage(), e);
		}
		return null;
	}

	private MasterOrderInfo ckeckOrderOutSnIsExsitString(String orderOutSn, String orderFrom) {
		MasterOrderInfoExample example = new MasterOrderInfoExample();
		example.or().andOrderOutSnEqualTo(orderOutSn).andOrderFromEqualTo(orderFrom);
		List<MasterOrderInfo> list = masterOrderInfoMapper.selectByExample(example);
		if (!StringUtil.isListNotNull(list))
			return null;
		return list.get(0);
	}

	/**
	 * 验证发货单参数是否正确，包括发货单中商品信息
	 *
	 * @param masterShip
	 * @param masterOrder
	 * @return
	 */
	private ServiceReturnInfo<MasterOrder> checkShip(MasterShip masterShip, MasterOrder masterOrder) {
		if (masterShip == null) {
			return new ServiceReturnInfo<MasterOrder>("配送方式不能为空！");
		}
		if (StringUtil.isBlank(masterShip.getShippingCode()) && OrderAttributeUtil.isPosOrder(masterOrder.getSource())) {
			return new ServiceReturnInfo<MasterOrder>("配送方式不能为空！");
		}

		if (!shippingCodeExists(masterShip.getShippingCode())) {
			return new ServiceReturnInfo<MasterOrder>("配送方式不存在！ shippingcode : " + masterShip.getShippingCode());
		}
		if (StringUtil.isTrimEmpty(masterShip.getConsignee())) {
			return new ServiceReturnInfo<MasterOrder>("收货人姓名不能为空！");
		}
		if (masterShip.getCountry() == null) {
			return new ServiceReturnInfo<MasterOrder>("收货人国家不能为空！");
		}
		if (masterShip.getProvince() == null) {
			return new ServiceReturnInfo<MasterOrder>("收货人省份不能为空！");
		}
		if (masterShip.getCity() == null) {
			return new ServiceReturnInfo<MasterOrder>("收货人城市不能为空！");
		}
		if (masterShip.getAddress() == null) {
			return new ServiceReturnInfo<MasterOrder>("收货人详细地址不能为空！");
		}
		return checkOrderGoods(masterShip, masterOrder);
	}

	/**
	 * 校验商品传入参数，验证商品价格是否低于保护价
	 *
	 * @param p
	 * @param order
	 * @return
	 */
	private ServiceReturnInfo<MasterOrder> checkOrderGoods(MasterShip masterShip, MasterOrder masterOrder) {
		if (masterShip.getGoodsList() == null || masterShip.getGoodsList().isEmpty()) {
			return new ServiceReturnInfo<MasterOrder>("订单商品不能为空或格式不正确！");
		}
		for (MasterGoods masterGoods : masterShip.getGoodsList()) {
			if (masterGoods == null) {
				return new ServiceReturnInfo<MasterOrder>("系统异常：订单商品解析出错");
			}
			if (StringUtil.isEmpty(masterGoods.getCustomCode())) {
				return new ServiceReturnInfo<MasterOrder>("订单商品明细中没有传入customCode参数");
			}
			if (masterGoods.getGoodsNumber() == null) {
				return new ServiceReturnInfo<MasterOrder>("订单商品明细中没有传入goodsNumber参数");
			}
			if (masterGoods.getTransactionPrice() == null) {
				return new ServiceReturnInfo<MasterOrder>("订单商品明细中没有传入transactionPrice参数");
			}
			if (StringUtil.isNotEmpty(masterShip.getDepotCode())
					&& StringUtil.isTrimEmpty(masterGoods.getDepotCode())) {
				masterGoods.setDepotCode(masterShip.getDepotCode());
			}
		}
		return new ServiceReturnInfo<MasterOrder>((MasterOrder) null);
	}

	/**
	 * 判断shippingCode是否存在
	 *
	 * @param shippingCode
	 * @return
	 */
	private boolean shippingCodeExists(String shippingCode) {
		try {
			logger.info("shippingCode:" + shippingCode);
			// shippingCode为99即为自提 现不用
			if (StringUtil.isNotBlank(shippingCode) && !"99".equals(shippingCode)) {
				if (redisClient.exists("shippingCode_" + shippingCode)) {
					return true;
				}
				// 如果没有在缓存中找到，再去数据库里面找
				SystemShipping returnBean = systemShippingService.getSystemShipByShipCode(shippingCode);
				logger.info("returnBean:" + returnBean);
				if (returnBean == null) {
					return false;
				}
				logger.info("returnBean:" + JSONObject.toJSONString(returnBean));
				// 找到之后再放进缓存中
				redisClient.set("shippingCode_" + shippingCode, "shippingCode_" + returnBean.getShippingCode());
				return true;
			}
			return true;
		} catch (Exception e) {
			logger.error("function shippingCodeExists", e);
		}
		return false;
	}

	/**
	 * 验证支付方式参数是否正确
	 *
	 * @param masterPays 支付单传入参数
	 * @param masterOrder 订单传入参数
	 * @return
	 */
	private ServiceReturnInfo<MasterOrder> checkPayList(List<MasterPay> masterPays, MasterOrder masterOrder) {
		if (StringUtil.isListNull(masterPays)) {
			return new ServiceReturnInfo<MasterOrder>("付款单信息不能为空！");
		}
		double payTotalFee = 0;
		for (MasterPay masterPay : masterPays) {
			if (masterPay.getPayCode() == null || "".equals(masterPay.getPayCode())) {
				return new ServiceReturnInfo<MasterOrder>("支付方式不能为空");
			}
			// REDIS读取 后面需要做一个缓存管理系统统一管理系统基础信息
			if (!checkPayCodeExists(masterPay.getPayCode())) {
				return new ServiceReturnInfo<MasterOrder>("支付方式不存在！");
			}
			payTotalFee += masterPay.getPayTotalFee();
		}
		// 订单支付财务价
		masterOrder.setPaySettlementPrice(payTotalFee);
		List<MasterShip> shipLists = masterOrder.getShipList();
		double goodsTranPrice = getGoodsPrice(shipLists);
		if (masterOrder.getPayStatus() == Constant.OI_PAY_STATUS_PAYED) {
			// 付款单为已付款
			// 订单财务价 = 商品成交价*数量 + 邮费  -红包 - 积分
			double orderSettlementPrice = this.addPrice(goodsTranPrice, masterOrder.getShippingTotalFee());
			orderSettlementPrice = this.addPrice(orderSettlementPrice, masterOrder.getTax());
			orderSettlementPrice = this.subPrice(orderSettlementPrice, masterOrder.getBonus());
			orderSettlementPrice = this.subPrice(orderSettlementPrice, masterOrder.getIntegralMoney());
			masterOrder.setOrderSettlementPrice(orderSettlementPrice);
		}

		return null;
	}

	/**
	 * 判断支付方式ID 是否存在
	 *
	 * @param payCode
	 *            支付方式code
	 * @return
	 */
	private boolean checkPayCodeExists(String payCode) {
		try {
			if (StringUtil.isNotBlank(payCode)) {
				if (redisClient.exists("payCode_" + payCode))
					return true;
				SystemPayment returnBean = systemPaymentService.selectSystemPayByCode(payCode);
				if (returnBean == null) {
					return false;
				}
				redisClient.set("payCode_" + payCode, "payCode_" + returnBean.getPayCode());
				return true;
			}
		} catch (Exception e) {
			logger.error("function checkPayCodeExists", e);
		}
		return false;
	}

	/**
	 * 商品财务价
	 * @param shipLists 商品配送列表
	 * @return double
	 */
	private double getGoodsPrice(List<MasterShip> shipLists) {
		double goodsTranPrice = 0;
		for (int i = 0; i < shipLists.size(); i++) {
			MasterShip shipList = shipLists.get(i);
			for (MasterGoods masterGoods : shipList.getGoodsList()) {
				// sum商品成交价
				BigDecimal tr = new BigDecimal(masterGoods.getTransactionPrice().toString());
				// 商品数量
				BigDecimal num = new BigDecimal(masterGoods.getGoodsNumber());
				goodsTranPrice = addPrice(goodsTranPrice, tr.multiply(num).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				// 小数数量
				BigDecimal goodsDecimalNumber = masterGoods.getGoodsDecimals();
				if (goodsDecimalNumber != null && goodsDecimalNumber.doubleValue() > 0) {
					goodsTranPrice += NumberUtil.getDoubleByDecimal(goodsDecimalNumber.multiply(tr), 2);
				}
			}
		}
		return goodsTranPrice;
	}

	public double addPrice(double price1, double price2) {
		BigDecimal p1 = new BigDecimal(price1);
		BigDecimal p2 = new BigDecimal(price2);
		return p1.add(p2).doubleValue();
	}

	public double subPrice(double price1, double price2) {
		BigDecimal p1 = new BigDecimal(price1);
		BigDecimal p2 = new BigDecimal(price2);
		return p1.subtract(p2).doubleValue();
	}
}
