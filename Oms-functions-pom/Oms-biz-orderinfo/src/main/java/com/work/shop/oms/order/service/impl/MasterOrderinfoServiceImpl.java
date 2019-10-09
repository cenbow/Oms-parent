package com.work.shop.oms.order.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.cardAPI.api.CardCartSearchServiceApi;
import com.work.shop.cardAPI.bean.APIBackMsgBean;
import com.work.shop.cardAPI.bean.ParaUserCardStatus;
import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.bean.bgchanneldb.ChannelShopExample;
import com.work.shop.oms.common.bean.AsynProcessOrderBean;
import com.work.shop.oms.common.bean.DepotInfo;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.common.bean.OcpbStatus;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.OrdersCreateReturnInfo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.common.bean.ValidateOrder;
import com.work.shop.oms.dao.ChannelShopMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.distribute.service.OrderDistributeService;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderAddressInfoService;
import com.work.shop.oms.order.service.MasterOrderGoodsService;
import com.work.shop.oms.order.service.MasterOrderInfoExtendService;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.order.service.MasterOrderPayService;
import com.work.shop.oms.order.service.OrderValidateService;
import com.work.shop.oms.order.service.SystemOrderSnService;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import com.work.shop.oms.payment.feign.PayService;
import com.work.shop.oms.shoppay.service.ShopPayService;
import com.work.shop.oms.stock.service.ChannelStockService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.NumberUtil;
import com.work.shop.oms.utils.StringUtil;

/**
 * 主订单服务
 * @author lemon
 */
@Service
public class MasterOrderinfoServiceImpl implements MasterOrderInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private SystemOrderSnService systemOrderSnService;

	@Resource
	private MasterOrderAddressInfoService addressInfoService;

	@Resource
	private MasterOrderGoodsService masterOrderGoodsService;

	@Resource
	private MasterOrderPayService masterOrderPayService;

	@Resource
	private MasterOrderActionService masterOrderActionService;

	@Resource
	private OrderValidateService orderValidateService;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private MasterOrderInfoExtendService orderInfoExtendService;
	
	@Resource(name = "asynProcessOrderProducerJmsTemplate")
	private JmsTemplate asynProcessOrderJmsTemplate;
	
	@Resource(name="orderConfirmService")
	private OrderConfirmService orderConfirmService;

	//@Resource(name = "cardCartSearchServiceApi")
	private CardCartSearchServiceApi cardCartSearchServiceApi;

	@Resource
	private PayService payService;

	@Resource(name="orderQuestionService")
	private OrderQuestionService orderQuestionService;

	@Resource
	private ChannelShopMapper channelShopMapper;

	@Resource
	private ShopPayService shopPayService;

	@Resource
	private ChannelStockService channelStockService;

	@Resource
	private MasterOrderInfoExtendService masterOrderInfoExtendService;

	@Resource(name="orderAccountPeriodJmsTemplate")
	private JmsTemplate orderAccountPeriodJmsTemplate;

	/**
	 * 问题单类型
	 */
	public static ThreadLocal<Integer> questionType = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return QUESTION_TYPE_NONE;
		}
	};

	/**
	 * 没有问题
	 */
	public static Integer QUESTION_TYPE_NONE = 0;

	/**
	 * 订单部分商品低于保底价或者限定折扣价
	 */
	public static Integer QUESTION_TYPE_DIYUZKJ = 1;

	/**
	 * 已付款订单商品成交价与已付款有差异
	 */
	public static Integer QUESTION_TYPE_PAYCY = 2;

	/**
	 * 创建订单方法，生成订单号，插入订单数据，生成付款单，生成发货单等操作
	 * 
	 * @param masterOrder 订单接口传入order对象
	 * @return OrderCreateReturnInfo 订单主表信息
	 */
	@Override
	public OrderCreateReturnInfo createOrder(MasterOrder masterOrder) {
		
		if (masterOrder == null) {
			return new OrderCreateReturnInfo("[masterOrder]参数不能为空", null);
		}
		
		logger.info("CreateOrderJson.userId:" + masterOrder.getUserId() + ",orderOutSn:" + masterOrder.getOrderOutSn()
				+ ",masterOrder:" + JSON.toJSONString(masterOrder));
		
		questionType.set(QUESTION_TYPE_NONE);
		
		// 验证传入参数是否正确
		ServiceReturnInfo<MasterOrder> validateInfo = orderValidateService.validate(masterOrder);
		if (!validateInfo.isIsok()) {
			return new OrderCreateReturnInfo(validateInfo.getMessage(), masterOrder.getOrderFrom());
		}
		return createMasterOrder(masterOrder);
	}

	/**
	 * 批量创建订单
	 * @param masterOrders 订单接口传入order对象
	 * @return OrdersCreateReturnInfo
	 */
	@Override
	public OrdersCreateReturnInfo createOrders(List<MasterOrder> masterOrders) {
		questionType.set(QUESTION_TYPE_NONE);
		if (StringUtil.isListNull(masterOrders)) {
			return new OrdersCreateReturnInfo("[masterOrders]参数不能为空");
		}
		logger.info("CreateOrderJson.userId:" + masterOrders.get(0).getUserId() + ",masterOrders:" + JSON.toJSONString(masterOrders));
		// 验证传入参数是否正确
		ServiceReturnInfo<List<MasterOrder>> validateInfo = orderValidateService.validateOrders(masterOrders);
		if (!validateInfo.isIsok()) {
			return new OrdersCreateReturnInfo(validateInfo.getMessage());
		}
		List<String> orderSns = new ArrayList<String>();
		List<OrderCreateReturnInfo> returnInfos = new ArrayList<OrderCreateReturnInfo>();
		int successCount = 0;
		for (MasterOrder masterOrder : masterOrders) {
			OrderCreateReturnInfo createReturnInfo = createMasterOrder(masterOrder);
			if (createReturnInfo.getIsOk() == Constant.OS_YES) {
				orderSns.add(createReturnInfo.getMasterOrderSn());
				successCount++;
			}
			returnInfos.add(createReturnInfo);
		}
		if (successCount == 0) {
			return new OrdersCreateReturnInfo("创建订单失败", returnInfos);
		}
		String mergePaySn = "";
		try {
			if (returnInfos.size() == 1) {
				if (StringUtil.isListNotNull(returnInfos.get(0).getPaySn())) {
					mergePaySn = returnInfos.get(0).getPaySn().get(0);
				}
			} else {
				ReturnInfo<MergeOrderPay> info = payService.createMergePay(orderSns);
				if (info.getIsOk() == Constant.OS_NO || info.getData() == null) {
					logger.error("创建合并支付单失败" + info.getMessage());
					return new OrdersCreateReturnInfo("", "创建订单成功！", returnInfos);
				}
				mergePaySn = info.getData().getMergePaySn();
			}
		} catch (Exception e) {
			logger.error("创建合并支付单异常：" + e.getMessage(), e);
			return new OrdersCreateReturnInfo("", "创建订单成功！", returnInfos);
		}
		OrdersCreateReturnInfo createReturnInfo = new OrdersCreateReturnInfo(mergePaySn, "创建订单成功！", returnInfos);

		logger.info("批量创建订单:" + JSONObject.toJSONString(createReturnInfo));
		return createReturnInfo;
	}

    /**
     * 设置渠道信息
     * @param masterOrder
     */
	private void setChannelInfo(MasterOrder masterOrder) {
        ChannelShopExample shopExample = new ChannelShopExample();
        shopExample.or().andShopCodeEqualTo(masterOrder.getOrderFrom());
        List<ChannelShop> channelShops = channelShopMapper.selectByExample(shopExample);

        if (StringUtil.isListNotNull(channelShops)) {
            ChannelShop channelShop = channelShops.get(0);
            masterOrder.setChannelShopName(channelShop.getShopTitle());
            masterOrder.setSiteCode(channelShop.getChannelCode());
        }
    }

	/**
	 * 创建主订单
	 * @param masterOrder 订单信息
	 * @return OrderCreateReturnInfo
	 */
	private OrderCreateReturnInfo createMasterOrder(MasterOrder masterOrder) {
		String errorMsg = "";
		boolean error = false;
		OrderCreateReturnInfo orderCreateReturnInfo = null;
		MasterOrderInfo masterOrderInfo = null;
		String masterOrderSn = "";
		// 验证邦购币
		ServiceReturnInfo<OcpbStatus> validate = null;
		try {
			validate = shopPayService.validateShopPay(masterOrder);
			if (!validate.isIsok()) {
				// 冻结失败
				logger.error("occupation shoppay error" + validate.getMessage());
				return new OrderCreateReturnInfo(validate.getMessage(), masterOrder.getOrderFrom());
			}
			// 将商品拆分到行
			/*ServiceReturnInfo<MasterOrder> splitInfo = orderValidateService.splitGoods(masterOrder);
			if (!splitInfo.isIsok()) {
				error = true;
				return new OrderCreateReturnInfo(splitInfo.getMessage(), masterOrder.getOrderFrom());
			}*/

            setChannelInfo(masterOrder);
			// 创建订单
			ServiceReturnInfo<MasterOrderInfo> moi = insertMasterOrderInfo(masterOrder);
			if (!moi.isIsok()) {
				error = true;
				return new OrderCreateReturnInfo(moi.getMessage(), masterOrder.getOrderFrom());
			}
			masterOrderInfo = moi.getResult();
			masterOrderSn = masterOrderInfo.getMasterOrderSn();
			
			// 发货信息
			MasterShip masterShip = masterOrder.getShipList().get(0);
			// 保存地址信息
			addressInfoService.insertMasterOrderAddressInfo(masterShip, masterOrderSn);
			// 保存订单商品
			ReturnInfo<List<MasterOrderGoods>> goodsInfo = masterOrderGoodsService.insertMasterOrderGoods(masterOrderSn, masterOrder, masterOrderInfo);
			if (goodsInfo.getIsOk() == Constant.OS_NO || StringUtil.isListNull(goodsInfo.getData())) {
				error = true;
				return new OrderCreateReturnInfo(goodsInfo.getMessage(), masterOrder.getOrderFrom());
			}
			// 处理支付单
			preparOrderPay(masterOrder);
			// 保存支付单
			ServiceReturnInfo<List<String>> returnInfo = masterOrderPayService.insertMasterOrderPay(masterOrderSn, masterOrder.getPayList());
			if (!returnInfo.isIsok()) {
				error = true;
				logger.error("生成付款单失败 : " + returnInfo.getMessage());
				return new OrderCreateReturnInfo(errorMsg = "生成付款单失败 : " + returnInfo.getMessage(), masterOrder.getOrderFrom());
			}

			orderCreateReturnInfo = new OrderCreateReturnInfo(masterOrderSn, "", returnInfo.getResult(),
					 "生成订单成功", masterOrder.getOrderFrom());

			orderCreateReturnInfo.setOrderSn(masterOrderSn);
			// 如果是问题单的情况
			if (masterOrderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_QUESTION) {
				orderCreateReturnInfo.setMessage(orderCreateReturnInfo.getMessage() + "并且生成了一张问题单！");
			}

			// 红包打折券使用通知
			if (StringUtil.isNotEmpty(masterOrder.getBonusId())
					|| StringUtil.isNotEmpty(masterOrder.getUseCards())
					|| StringUtil.isNotEmpty(masterOrder.getFreePostCard())) {
				ParaUserCardStatus paraBean = new ParaUserCardStatus();
				if (StringUtil.isNotEmpty(masterOrder.getUseCards())) {
					// 打折券，多个以,分割
					paraBean.setCouponNo(masterOrder.getUseCards());
				}
				if (StringUtil.isNotEmpty(masterOrder.getBonusId())) {
					// 红包，多个以,分割
					paraBean.setPackageNo(masterOrder.getBonusId());
				}
				if (StringUtil.isNotEmpty(masterOrder.getFreePostCard())) {
					paraBean.setFreeNo(masterOrder.getFreePostCard());
				}
				// 订单号
				paraBean.setOrderNo(masterOrderSn);
				// 状态，3作废、4使用
				paraBean.setStatus(Constant.CARD_USERED);
				// 下单人
				paraBean.setUserId(masterOrder.getUserId());
				try {
					logger.info("订单[" + masterOrderSn + "] 调用红包券卡通知接口：paraBean = " + JSON.toJSONString(paraBean));
					APIBackMsgBean<Integer> result = cardCartSearchServiceApi.updateUserCard(paraBean);
					logger.info("订单[" + masterOrderSn + "] 调用红包券卡通知接口：result = " + JSON.toJSONString(result));
					if (result == null || result.getSoaStateBean() == null || Constant.OS_STR_NO.equals(result.getSoaStateBean().getIsOk())) {
						String errorMSg = result == null ? "通知券卡中心使用券卡异常:接口返回结果为空" : result.getSoaStateBean().getMsg();
						orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "用户" + masterOrder.getUserId()
								+ errorMSg, "9976"));
					}
					masterOrderActionService.insertOrderAction(masterOrderSn, "通知使用红包券卡完成" + masterOrderSn, masterOrderInfo.getPayStatus());
				} catch (Exception e) {
					logger.error("订单[" + masterOrderSn + "]通知券卡中心使用券卡异常：" + e.getMessage(), e);
					orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "用户" + masterOrder.getUserId()
							+ "通知券卡中心使用券卡异常：" + e.getMessage(), "9976"));
				}
			}

			//无需审批订单直接更新账期最后支付时间
            if (masterOrderInfo.getNeedAudit() == Constant.OS_NO) {
                //orderInfoExtendService.fillPayLastDate(masterOrderSn, masterOrderInfo.getAddTime());
            }
		} catch (Exception e) {
			error = true;
			logger.error("订单[" + masterOrderSn + "]创建失败：系统异常-" + e.getMessage(), e);
			return new OrderCreateReturnInfo(errorMsg = "创建订单失败：系统异常-" + e.getMessage(), masterOrder.getOrderFrom());
		} finally {
			if (error) {
				logger.error("orderInfoServiceImpl.createOrder error " + masterOrder.getOrderFrom() + errorMsg);
				shopPayService.releaseShopPay(masterOrder, validate.getResult());
				return new OrderCreateReturnInfo(errorMsg, masterOrder.getOrderFrom());
			}
		}
		
		try {
			ValidateOrder validateOrder = buildValidateOrder(masterOrder);
			asynProcessOrder(masterOrderSn, validateOrder, validate.getResult(), questionType.get());
		} catch (Exception e) {
			logger.error(masterOrderSn + " 订单生成后校验订单信息异常", e);
		} finally {
			questionType.set(null);
		}

		logger.info("orderCreateReturnInfo:" + JSONObject.toJSONString(orderCreateReturnInfo));
		return orderCreateReturnInfo;
	}

	/**
	 * 订单的后续操作
	 * 
	 * @param masterOrderSn 订单号
	 * @param validateOrder 校验订单信息
	 * @param ocpbStatus 平台币占用情况
	 * @param qt 问题单状态
	 */
	@Override
	public void dealOther(String masterOrderSn, ValidateOrder validateOrder, OcpbStatus ocpbStatus, int qt) {
		try {
			// 通知平台库存预占
			ReturnInfo info = channelStockService.preOccupy(masterOrderSn);
			ReturnInfo returnInfo = orderValidateService.validateOrderInfo(masterOrderSn, validateOrder, ocpbStatus, qt);
			logger.debug(masterOrderSn + "检查是否可以确认：" + JSON.toJSONString(returnInfo));
			/*// 线下支付订单并且通知拆单
			if (validateOrder.getIsXianxia()) {
				// 设置线下支付问题单
				orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "线下支付问题单", "16"));
				// 确认
				MasterOrderInfo updateMaster = new MasterOrderInfo();
				updateMaster.setOrderStatus((byte)Constant.OI_ORDER_STATUS_CONFIRMED); // 确认
				updateMaster.setConfirmTime(new Date()); // 确认时间
				updateMaster.setMasterOrderSn(masterOrderSn);
				masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
				// 拆单处理
				orderDistributeService.orderDistribute(masterOrderSn);
			}*/
			if (returnInfo != null && returnInfo.getIsOk() == Constant.OS_YES) {
				orderConfirmService.confirmOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "自动确认", null));
			}
		} catch (Exception e) {
			logger.error("订单[" + masterOrderSn + "]的后续操作异常" + e.getMessage(), e);
		} finally {
			orderInfoExtendService.masterOrderFinished(masterOrderSn);
		}
	}

	/**
	 * 发送异步确认到MQ
	 * @param masterOrderSn 订单编码
	 * @param validateOrder 校验订单信息
	 * @param ocpbStatus 平台币状态
	 * @param integer 问题单
	 */
	public void asynProcessOrder(String masterOrderSn, ValidateOrder validateOrder, OcpbStatus ocpbStatus, Integer integer) {
		AsynProcessOrderBean bean = new AsynProcessOrderBean(masterOrderSn, ocpbStatus, validateOrder, integer);
		String json = JSON.toJSONString(bean);
		logger.debug("订单[" + masterOrderSn + "]下单成功后异步处理:" + json);
		asynProcessOrderJmsTemplate.send(new TextMessageCreator(json));
	}

	/**
	 * 根据订单号查询订单信息
	 * @param masterOrderSn 订单编码
	 * @return MasterOrderInfo
	 */
	@Override
	public MasterOrderInfo getOrderInfoBySn(String masterOrderSn) {
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			return null;
		}
		return masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
	}
	
	/**
	 * 通过外部交易号, 获取对应的订单信息
	 * @param outOrderSn 外部交易号
	 * @return MasterOrderInfo
	 */
	@Override
	public MasterOrderInfo getOrderInfoByOutOrderSn(String outOrderSn) {
		if (StringUtil.isTrimEmpty(outOrderSn)) {
			return null;
		}
		
		MasterOrderInfoExample example = new MasterOrderInfoExample();
		example.or().andOrderOutSnEqualTo(outOrderSn);
		
		List<MasterOrderInfo> list = masterOrderInfoMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}

	/**
	 * 填充订单数据
	 * @param orderInfo 订单
	 * @param masterOrder 订单数据
	 */
	private void fillOrderInfo(MasterOrderInfo orderInfo, MasterOrder masterOrder) {
		// 下单人id
		orderInfo.setUserId(masterOrder.getUserId());
		// 代下单人id
		orderInfo.setInsteadUserId(masterOrder.getInsteadUserId());
		// 交易类型
		orderInfo.setTransType(Byte.parseByte("" + masterOrder.getTransType()));
		// 订单状态
		orderInfo.setOrderStatus((byte) Constant.OI_ORDER_STATUS_UNCONFIRMED);
		orderInfo.setPayStatus(Byte.parseByte("" + masterOrder.getPayStatus()));
		// 如果有使用余额积分付款，并且余额积分付款小于实际应付金额
		if (masterOrder.getSurplus() > 0 && NumberUtil.sub(masterOrder.getTotalFee(),
				masterOrder.getBonus()) - masterOrder.getSurplus() - masterOrder.getIntegralMoney() >= 0.01) {
			orderInfo.setPayStatus(Constant.OI_PAY_STATUS_PARTPAYED);
		}

		// 订单来源
		orderInfo.setOrderFrom(masterOrder.getOrderFrom());
		// 渠道店铺名称
		orderInfo.setShopName(masterOrder.getChannelShopName());
		// 渠道站点编码
		orderInfo.setChannelCode(masterOrder.getSiteCode());
		// 受益人ID，如果开启提成功能
		orderInfo.setBeneficiaryId(masterOrder.getBeneficiaryId());
		// 订单由某广告带来的广告id
		orderInfo.setFromAd(Short.parseShort(masterOrder.getFromAd().toString()));
		// 缺货处理方式
		orderInfo.setHowOos(masterOrder.getHowOss());
		// 订单设备的来源
		orderInfo.setReferer(masterOrder.getReferer());
		// 推荐分成的用户id
		orderInfo.setParentId(masterOrder.getParentId());

		// 代销类型(0无;1是代销;2是CPS)
		orderInfo.setOutletType(masterOrder.getOutletType());
		// CPS来源
		orderInfo.setSourceCode(masterOrder.getSourceCode());
		// 配送总费用
		orderInfo.setShippingTotalFee(BigDecimal.valueOf(masterOrder.getShippingTotalFee()));
		// 保价总费用
		orderInfo.setInsureTotalFee(BigDecimal.valueOf(masterOrder.getInsureTotalFee()));
		// 支付总费用
		orderInfo.setPayTotalFee(BigDecimal.valueOf(masterOrder.getPayTotalFee()));
		// 订单附言，由用户提交订单前填写
		orderInfo.setPostscript(masterOrder.getPostscript());
		// 应付款总金额
		orderInfo.setTotalPayable(BigDecimal.valueOf(masterOrder.getTotalPayable()));
		// 商品总金额
		orderInfo.setGoodsAmount(BigDecimal.valueOf(masterOrder.getGoodsAmount()));
		// 订单总金额
		orderInfo.setTotalFee(BigDecimal.valueOf(masterOrder.getTotalFee()));
		// 已付款金额
		orderInfo.setMoneyPaid(BigDecimal.valueOf(masterOrder.getMoneyPaid()));
		// 余额支付金额
		orderInfo.setSurplus(BigDecimal.valueOf(masterOrder.getSurplus()));
		// 使用红包金额
		orderInfo.setBonus(BigDecimal.valueOf(masterOrder.getBonus()));
		// 使用红包ID
		orderInfo.setBonusId(masterOrder.getBonusId());
		// 订单商品折扣
		orderInfo.setDiscount(BigDecimal.valueOf(masterOrder.getDiscount()));
		// 商品数量
		orderInfo.setGoodsCount(statisticsGoodsCount(masterOrder));
		// 下单时间
		orderInfo.setAddTime(masterOrder.getAddTime() == null ? new Date() : masterOrder.getAddTime());
		// 商家给客户的留言
		orderInfo.setToBuyer(masterOrder.getToBuyer());
		// 订单类型 0，正常订单 1，补发订单  2，换货订单
		orderInfo.setOrderType((int) masterOrder.getOrderType());
		// 是否立即下发ERP（0否，1是）default 0
		orderInfo.setIsnow(0);
		// 是否需要审核
        orderInfo.setNeedAudit(masterOrder.getNeedAudit());
        //是否需要合同签章
        orderInfo.setNeedSign(masterOrder.getNeedSign());
	}

	/**
	 * 保存Orderinfo进数据库
	 * 
	 * @param masterOrder 订单信息
	 * @return ServiceReturnInfo<MasterOrderInfo>
	 */
	private ServiceReturnInfo<MasterOrderInfo> insertMasterOrderInfo(MasterOrder masterOrder) {
		// 生成订单号
		ServiceReturnInfo<String> siSn = systemOrderSnService.createMasterOrderSn();
		// 生成订单号失败
		if (!siSn.isIsok()) {
			logger.error("生成订单号失败" + siSn.getMessage());
			return new ServiceReturnInfo<MasterOrderInfo>(siSn.getMessage());
		}
		final String masterOrderSn = siSn.getResult();
		logger.debug("生成订单的订单号:" + masterOrderSn);
		MasterOrderInfo orderInfo = new MasterOrderInfo();
		orderInfo.setOrderOutSn(masterOrder.getOrderOutSn());
		orderInfo.setMasterOrderSn(masterOrderSn);

		// 填充订单数据
		fillOrderInfo(orderInfo, masterOrder);

		List<Integer> promotion = masterOrder.getPromotionSummry();
		String proIds = StringUtil.join(promotion, ",");
		proIds = proIds == null ? "" : proIds;
		// 设置参加了那些促销
		orderInfo.setPrIds(proIds);
		orderInfo.setPrName(masterOrder.getPrName());
		orderInfo.setNoticeStatus(0);
		// 下单人
		orderInfo.setUserName(masterOrder.getUserId());
		// 订单锁定状态（0，未锁定；1，已锁定）
		orderInfo.setLockStatus(0);
		// 发货总状态 0，未发货；1，备货中；2，部分发货；3，已发货；4，部分收货；5，客户已收货）
		orderInfo.setShipStatus((byte) 0);
		// 问题单状态 （0 否  1 是）
		orderInfo.setQuestionStatus(0);
		// 使用积分金额
		orderInfo.setIntegralMoney(BigDecimal.valueOf(masterOrder.getIntegralMoney()));
		// 使用积分数量
		orderInfo.setIntegral(masterOrder.getIntegral());
		orderInfo.setCancelCode("");
		orderInfo.setCancelReason("");

		// 订单类型
		masterOrder.setOrderCategory(Constant.OI_ORDER_CATEGORY_SALE);
		// 订单类型
		masterOrder.setOrderCategory(masterOrder.getOrderCategory());

		masterOrder.setInvoicesOrganization("");
		// 单据组织：物资领用时，成本中心编码；其它出库时，承运商编码
		orderInfo.setInvoicesOrganization(masterOrder.getInvoicesOrganization());
		orderInfo.setSource(getSource(masterOrder.getReferer(), masterOrder.getSource()));
		// 拆单状态 0：未拆单；1：拆单中；2：已拆单；3：重新拆单
		orderInfo.setSplitStatus(Constant.SPLIT_STATUS_UNSPLITED);
		// 注册手机号码
		orderInfo.setRegisterMobile(masterOrder.getRegisterMobile());
		// 点数
		orderInfo.setPoints(BigDecimal.valueOf(masterOrder.getPoints()));
		// 订单bv
		orderInfo.setBvValue(masterOrder.getBvValue());
		// 订单基础bv
		orderInfo.setBaseBvValue(masterOrder.getBaseBvValue());
		// 预计发货时间
		orderInfo.setExpectedShipDate(masterOrder.getExpectedShipDate());
		// 综合税费
		orderInfo.setTax(BigDecimal.valueOf(masterOrder.getTax()));
		// 是否需要审核 0不需要、1需要
		orderInfo.setNeedAudit(masterOrder.getNeedAudit());
		// 是否需要合同签章
		orderInfo.setNeedSign(masterOrder.getNeedSign());
		masterOrderInfoMapper.insertSelective(orderInfo);
		byte payStatus = orderInfo.getPayStatus() == 2 ? orderInfo.getPayStatus() : 0;
		// 写入订单日志
		masterOrderActionService.insertOrderAction(masterOrderSn, "通过接口创建订单：" + masterOrderSn, payStatus);
		// 订单扩展信息
		orderInfoExtendService.insertOrderInfoExtend(masterOrderSn, masterOrder);
		return new ServiceReturnInfo<MasterOrderInfo>(orderInfo);
	}

	/**
	 * 统计订单商品实际数量
	 * @param masterOrder 订单数据
	 * @return Integer
	 */
	private Integer statisticsGoodsCount(MasterOrder masterOrder) {
		Integer count = 0;
		if (masterOrder != null) {
			if (masterOrder.getShipList() != null && masterOrder.getShipList().size() > 0) {
				List<MasterGoods> goodsLists = masterOrder.getShipList().get(0).getGoodsList();
				if (goodsLists != null && goodsLists.size() > 0) {
					for (MasterGoods masterGoods : goodsLists) {
						count += masterGoods.getGoodsNumber();
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * source类型
	 * @param referer 来源设备
	 * @param source 来源
	 * @return Integer
	 */
	private Integer getSource(String referer, Integer source) {
		if (StringUtil.equalsIgnoreCase(Constant.OS_POS, referer)) {
			return 1;
		}
		if (StringUtil.equalsIgnoreCase(Constant.OS_REFERER_YHJ, referer)) {
			return 2;
		}
		return source;
	}

	/**
	 * 预设支付单信息
	 * @param masterOrder 订单
	 */
	private void preparOrderPay(MasterOrder masterOrder) {
		List<MasterPay> masterPays = masterOrder.getPayList();
		if (StringUtil.isListNull(masterPays)) {
			return;
		}
		for (MasterPay masterPay : masterPays) {
			if (masterPay.getSurplus() <= 0) {
				continue;
			}
			masterPay.setPayStatus(Constant.OP_PAY_STATUS_UNPAYED);
			masterPay.setSurplus(0d);
		}
	}

	/**
	 * 构建检验订单信息
	 * @param masterOrder 订单信息
	 * @return ValidateOrder
	 */
	private ValidateOrder buildValidateOrder(MasterOrder masterOrder) {
		ValidateOrder validateOrder = new ValidateOrder();
		validateOrder.setGoodsQuestionCode(masterOrder.getGoodsQuestionCode());
		validateOrder.setGoodsSettlementPrice(masterOrder.getGoodsSettlementPrice());
		validateOrder.setIntegral(masterOrder.getIntegral());
		validateOrder.setIntegralMoney(masterOrder.getIntegralMoney());
		validateOrder.setIsAdvance(masterOrder.getIsAdvance());
		validateOrder.setIsGroup(masterOrder.getIsGroup());
		validateOrder.setOrderSettlementPrice(masterOrder.getOrderSettlementPrice());
		validateOrder.setPaySettlementPrice(masterOrder.getPaySettlementPrice());
		validateOrder.setQuestionCode(masterOrder.getQuestionCode());
		validateOrder.setSmsCode(masterOrder.getSmsCode());
		validateOrder.setSmsFlag(masterOrder.getSmsFlag());
		validateOrder.setReferer(masterOrder.getReferer());
		validateOrder.setOrderFrom(masterOrder.getOrderFrom());
		validateOrder.setPoints(masterOrder.getPoints());
		validateOrder.setSurplus(masterOrder.getSurplus());
		validateOrder.setIsXianxia(false);
		if (masterOrder.getPayStatus().byteValue() == Constant.OI_PAY_STATUS_PAYED 
				&& Constant.OP_PAY_WAY_PAYCODE_XIANXIA.equals(masterOrder.getPayList().get(0).getPayCode())) {
			validateOrder.setIsXianxia(true);
		}
		return validateOrder;
	}

	/**
	 * 定时任务处理到期账期支付扣款
	 * @param orderAccountPeriod
	 * @return
	 */
	@Override
	public ReturnInfo<Boolean> processOrderPayPeriod(OrderAccountPeriod orderAccountPeriod) {
		ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
		returnInfo.setIsOk(Constant.OS_NO);

		try {
			String masterOrderSn = orderAccountPeriod.getMasterOrderSn();
			List<MasterOrderPay> masterOrderPayList = masterOrderPayService.getMasterOrderPayList(masterOrderSn);
			if (masterOrderPayList == null || masterOrderPayList.size() == 0) {
				returnInfo.setMessage("订单:" + masterOrderSn + "支付单信息不存在");
				return returnInfo;
			}

			MasterOrderPay masterOrderPay = masterOrderPayList.get(0);
			orderAccountPeriod.setOrderMoney(masterOrderPay.getPayTotalfee());
			orderAccountPeriod.setPaymentPeriod(masterOrderPay.getPaymentPeriod());
			orderAccountPeriod.setPaymentRate(masterOrderPay.getPaymentRate());
			orderAccountPeriod.setType(1);
			orderAccountPeriodJmsTemplate.send(new TextMessageCreator(JSONObject.toJSONString(orderAccountPeriod)));
		} catch (Exception e) {
			logger.error("处理订单账期支付推送问题");
		}

		return returnInfo;
	}

    /**
     * 设置账期支付支付时间和扣款
     * @param masterOrderInfo
     * @return
     */
    @Override
	public ReturnInfo<Boolean> processOrderPayPeriod(MasterOrderInfo masterOrderInfo) {
		ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
		returnInfo.setIsOk(Constant.OS_YES);

		try {
			String masterOrderSn = masterOrderInfo.getMasterOrderSn();
			List<MasterOrderPay> masterOrderPayList = masterOrderPayService.getMasterOrderPayList(masterOrderSn);
			if (masterOrderPayList == null || masterOrderPayList.size() == 0) {
				returnInfo.setMessage("订单:" + masterOrderSn + "支付单信息不存在");
				return returnInfo;
			}

			MasterOrderPay masterOrderPay = masterOrderPayList.get(0);
			int payId = masterOrderPay.getPayId();
			if (Constant.PAYMENT_ZHANGQI_ID  != payId) {
				returnInfo.setMessage("订单:" + masterOrderSn + "不是账期支付");
				return returnInfo;
			}

			// 判断是否已设置
            MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendService.getMasterOrderInfoExtendById(masterOrderSn);
			if (masterOrderInfoExtend == null) {
                returnInfo.setMessage("订单:" + masterOrderSn + "扩展信息不存在");
                return returnInfo;
            }
            int payPeriodStatus = masterOrderInfoExtend.getPayPeriodStatus();
			if (payPeriodStatus == 1) {
                returnInfo.setMessage("订单:" + masterOrderSn + "账期已扣款");
                return returnInfo;
            }
            processOrderAccountPay(masterOrderPay);
			Date lastPayDate = masterOrderInfoExtend.getLastPayDate();
			if (lastPayDate != null) {
                returnInfo.setMessage("订单:" + masterOrderSn + "账期支付时间已设置");
                return returnInfo;
            }
            //账期支付填充最后支付时间
            masterOrderInfoExtendService.fillPayLastDate(masterOrderSn, new Date());

		} catch (Exception e) {
			logger.error("处理订单账期支付推送问题");
			returnInfo.setIsOk(Constant.OS_NO);
		}


		return returnInfo;
	}

    /**
     * 处理订单账期支付
     * @param masterOrderSn
     * @return ReturnInfo<Boolean>
     */
    @Override
	public ReturnInfo<Boolean> processOrderPayPeriod(String masterOrderSn) {
		ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
		returnInfo.setIsOk(Constant.OS_NO);

		try {
			List<MasterOrderPay> masterOrderPayList = masterOrderPayService.getMasterOrderPayList(masterOrderSn);
			if (masterOrderPayList == null || masterOrderPayList.size() == 0) {
				returnInfo.setMessage("订单:" + masterOrderSn + "支付单信息不存在");
				return returnInfo;
			}

			MasterOrderPay masterOrderPay = masterOrderPayList.get(0);
            processOrderAccountPay(masterOrderPay);
		} catch (Exception e) {
			logger.error("处理订单账期支付推送问题");
		}

		return returnInfo;
	}

    /**
     * 处理订单账期支付信息
     * @param masterOrderPay
     */
	private void processOrderAccountPay(MasterOrderPay masterOrderPay) {
        int payId = masterOrderPay.getPayId();
        if (Constant.PAYMENT_ZHANGQI_ID  != payId) {
            return;
        }
        // 期数
        int paymentPeriod = masterOrderPay.getPaymentPeriod();
        if (paymentPeriod == 0) {
            String masterOrderSn = masterOrderPay.getMasterOrderSn();
            MasterOrderInfo masterOrderInfo = getOrderInfoBySn(masterOrderSn);
            MasterOrderInfoExtend masterOrderInfoExtend = orderInfoExtendService.getMasterOrderInfoExtendById(masterOrderSn);

            int payPeriodStatus = masterOrderInfoExtend.getPayPeriodStatus();
            if (payPeriodStatus != 0) {
                return;
            }
            // 下发账期扣款
            OrderAccountPeriod orderAccountPeriod = fillOrderAccountPeriod(masterOrderInfo, masterOrderInfoExtend, masterOrderPay);
            orderAccountPeriodJmsTemplate.send(new TextMessageCreator(JSONObject.toJSONString(orderAccountPeriod)));
        }

    }

    /**
     * 填充订单账期支付信息
     * @param masterOrderInfo
     * @param masterOrderInfoExtend
     * @param masterOrderPay
     * @return OrderAccountPeriod
     */
    private OrderAccountPeriod fillOrderAccountPeriod(MasterOrderInfo masterOrderInfo, MasterOrderInfoExtend masterOrderInfoExtend, MasterOrderPay masterOrderPay) {
        OrderAccountPeriod orderAccountPeriod = new OrderAccountPeriod();
        orderAccountPeriod.setMasterOrderSn(masterOrderInfo.getMasterOrderSn());
        orderAccountPeriod.setAddTime(masterOrderInfo.getAddTime());
        orderAccountPeriod.setUserId(masterOrderInfo.getUserId());
        orderAccountPeriod.setCompanyId(masterOrderInfoExtend.getCompanyCode());
        orderAccountPeriod.setOrderMoney(masterOrderPay.getPayTotalfee());
        orderAccountPeriod.setPaymentPeriod(masterOrderPay.getPaymentPeriod());
        orderAccountPeriod.setPaymentRate(masterOrderPay.getPaymentRate());
        orderAccountPeriod.setType(1);

        return orderAccountPeriod;
    }
}
