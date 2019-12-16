package com.work.shop.oms.order.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.work.shop.oms.api.param.bean.CreateOrderRefund;
import com.work.shop.oms.api.param.bean.CreateOrderReturn;
import com.work.shop.oms.api.param.bean.CreateOrderReturnGoods;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.config.service.OrderCustomDefineService;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.dao.define.OrderReturnSearchMapper;
import com.work.shop.oms.order.service.MasterOrderInfoExtendService;
import com.work.shop.oms.order.service.MasterOrderPayService;
import com.work.shop.oms.utils.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.action.service.OrderActionService;
import com.work.shop.oms.api.bean.OrderReturnAction;
import com.work.shop.oms.api.param.bean.CreateOrderReturnBean;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.channel.service.ChannelInfoService;
import com.work.shop.oms.order.request.ReturnManagementRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.ReturnManagementResponse;
import com.work.shop.oms.order.service.ReturnManagementService;
import com.work.shop.oms.orderReturn.service.OrderReturnService;
import com.work.shop.oms.orderReturn.service.OrderReturnStService;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.vo.ReturnAccountVO;
import com.work.shop.oms.vo.ReturnCommonVO;
import com.work.shop.oms.vo.ReturnGoodsVO;
import com.work.shop.oms.vo.ReturnOrderParam;
import com.work.shop.oms.vo.ReturnOrderVO;
import com.work.shop.oms.vo.ReturnPaymentVO;

/**
 * 退单管理服务
 * @author lemon
 */
@Service
public class ReturnManagementServiceImpl implements ReturnManagementService {
	
	private static final Logger logger = Logger.getLogger(ReturnManagementServiceImpl.class);
	
	@Resource
	private MasterOrderInfoDetailMapper masterOrderInfoDetailMapper;

	@Resource
	private MasterOrderPayTypeDetailMapper masterOrderPayTypeDetailMapper;

	@Resource
	private OrderDistributeMapper orderDistributeMapper;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private OrderReturnMapper orderReturnMapper;

	@Resource(name = "orderReturnService")
	private OrderReturnService orderReturnService;

	@Resource
	private OrderReturnStService orderReturnStService;

	@Resource(name = "channelInfoServiceImpl")
	private ChannelInfoService channelInfoService;

	@Resource
	private OrderReturnGoodsMapper orderReturnGoodsMapper;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;

	@Resource
	private OrderReturnShipMapper orderReturnShipMapper;

	@Resource
	private OrderRefundMapper orderRefundMapper;

	@Resource
	private SystemPaymentMapper systemPaymentMapper;

	@Resource
	private OrderReturnActionMapper orderReturnActionMapper;

	@Resource
	private OrderActionService orderActionService;
	
	@Resource
	private OrderCancelService orderCancelService;

	@Resource
	private GoodsReturnChangeMapper goodsReturnChangeMapper;

	@Resource
	private GoodsReturnChangeDetailMapper goodsReturnChangeDetailMapper;

	@Resource
	private OrderCustomDefineService orderCustomDefineService;

	@Resource
	private OrderReturnSearchMapper orderReturnSearchMapper;

	@Resource
	private MasterOrderInfoExtendService masterOrderInfoExtendService;

	@Resource
	private MasterOrderPayService masterOrderPayService;

	/**
	 * 退单详情
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemGet(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单详情查询失败");
		response.setSuccess(false);
		ReturnOrderVO returnOrderVO = new ReturnOrderVO();

		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		String relOrderSn = request.getMasterOrderSn();
		OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
		if(orderReturn == null){
			response.setMessage("退单数据不存在");
			return response;
		}
		Map<String, Object> paramItemMap = new HashMap<String, Object>(4);
		paramItemMap.put("masterOrderSn", orderReturn.getMasterOrderSn());
		paramItemMap.put("isHistory", 0);
		//查询主单信息（主单表、扩展表、地址信息表）
		MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramItemMap);
		if (master == null) {
			response.setMessage("关联原订单" + relOrderSn + " 不存在");
			return response;
		}

		ChannelShop channelInfo = new ChannelShop();
		channelInfo.setShopCode(master.getStoreCode());
		channelInfo.setShopTitle(master.getStoreName());
		//配送信息（配送方式，快递号）
		OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
		if (orderReturnShip == null) {
			response.setMessage("退单发货扩展表不存在");
			return response;
		}
		relOrderSn = orderReturn.getMasterOrderSn();
		if(StringUtils.isBlank(relOrderSn)){
			response.setMessage("关联原订单为空无法加载退单数据");
			return response;
		}

		ReturnCommonVO returnCommon = fullApplyReturnInfo(orderReturn, orderReturnShip, channelInfo);
		//商品数据
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("returnSn", returnSn);
		List<OrderReturnGoods> orderReturnGoodsList = orderReturnSearchMapper.getReturnGoodsByReturnSn(param);
		List<ReturnGoodsVO> returnGoodsVOList = new ArrayList<ReturnGoodsVO>();
		if (CollectionUtils.isNotEmpty(orderReturnGoodsList)) {
			for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
				ReturnGoodsVO returnGoodsVO = fullReturnGoods(orderReturn, orderReturnGoods);
				returnGoodsVOList.add(returnGoodsVO);
			}
		}
		//付款信息（支付方式+金额）
		OrderRefundExample orfEx = new OrderRefundExample();
		orfEx.or().andRelatingReturnSnEqualTo(returnSn);
		List<OrderRefund> oRefundList = orderRefundMapper.selectByExample(orfEx);
		// 账目信息
		ReturnAccountVO returnAccount = new ReturnAccountVO();
		// 原订单红包
		returnAccount.setBonus(master.getBonus().doubleValue()); 
		// 原订单配送费用
		returnAccount.setShippingTotalFee(master.getShippingTotalFee().doubleValue());
		// 退款总金额
		returnAccount.setReturnTotalFee(orderReturn.getReturnTotalFee().doubleValue());
		// 退款商品金额
		returnAccount.setReturnGoodsMoney(orderReturn.getReturnGoodsMoney().doubleValue());
		// 退单运费
		returnAccount.setReturnShipping(orderReturn.getReturnShipping().doubleValue());
		// 退红包金额
		returnAccount.setReturnBonusMoney(orderReturn.getReturnBonusMoney().doubleValue());
		// 退其它费用
		returnAccount.setReturnOtherMoney(orderReturn.getReturnOtherMoney().doubleValue());
		// 积分金额
		returnAccount.setTotalIntegralMoney(orderReturn.getIntegralMoney().doubleValue());

		if (master != null) {
			OrderItemDetail itemDetail = new OrderItemDetail();
			cloneBean(itemDetail, master);
			response.setItemDetail(itemDetail);
		}
		List<OrderItemPayDetail> payDetails = masterOrderPayTypeDetailMapper.getOrderItemPayDetail(relOrderSn);
		if (StringUtil.isListNotNull(payDetails)) {
			response.setPayDetails(payDetails);
		}
		OrderReturnActionExample actionExample = new OrderReturnActionExample();
		actionExample.setOrderByClause("action_id desc");
		actionExample.or().andReturnSnEqualTo(returnSn);
		List<OrderReturnAction> actions = orderReturnActionMapper.selectByExample(actionExample);
		returnOrderVO.setReturnCommon(returnCommon);
		returnOrderVO.setReturnGoodsList(returnGoodsVOList);
		returnOrderVO.setReturnAccount(returnAccount);
		returnOrderVO.setReturnActionList(actions);
		if (StringUtil.isListNotNull(oRefundList)) {
			List<OrderRefundBean> returnRefundBeanList = new ArrayList<OrderRefundBean>();
			for (OrderRefund refund : oRefundList) {
				OrderRefundBean refundBean = new OrderRefundBean();
				cloneBean(refundBean, refund);
				SystemPayment payment = systemPaymentMapper.selectByPrimaryKey(refund.getReturnPay().byteValue());
				if (payment != null) {
					refundBean.setReturnPayName(payment.getPayName());
				}
				returnRefundBeanList.add(refundBean);
			}
			returnOrderVO.setReturnRefundBeanList(returnRefundBeanList);
		}
		AdminUser adminUser = new AdminUser();
		adminUser.setUserId(request.getActionUserId());
		adminUser.setUserName(request.getActionUser());
		ReturnItemStatusUtils statusUtils = new ReturnItemStatusUtils(returnCommon, adminUser);
		response.setStatusUtils(statusUtils);
		response.setReturnOrderVO(returnOrderVO);

		response.setSuccess(true);
		response.setMessage("成功");
		return response;
	}

	/**
	 * 退单创建初始化
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemCreateInit(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单创建初始化失败");
		response.setSuccess(false);

		String relOrderSn = request.getMasterOrderSn();
		if (StringUtils.isBlank(relOrderSn)) {
			response.setMessage("关联原订单为空无法加载退单数据");
			return response;
		}
		String returnType = request.getReturnType();
		if (StringUtils.isBlank(returnType)) {
			response.setMessage("退单申请时退单类型缺失");
			return response;
		}
		// 使用主单号创建退单
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("masterOrderSn", relOrderSn);
		paramMap.put("isHistory", 0);
		MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
		if (master == null) {
			response.setMessage("关联原订单" + relOrderSn + " 不存在");
			return response;
		}

		ReturnOrderVO returnOrderVO = new ReturnOrderVO();
		if (master.getShipStatus().intValue() >= Constant.OI_SHIP_STATUS_PARTSHIPPED) {
			returnOrderVO.setOrderShipType(1);
		}
		ReturnCommonVO returnCommon = fullReturnInfo(master, returnType);
		// 订单商品查询
		MasterOrderGoodsExample masterOrderGoodsExample = new MasterOrderGoodsExample();
		MasterOrderGoodsExample.Criteria criteria = masterOrderGoodsExample.or();
		criteria.andMasterOrderSnEqualTo(relOrderSn);
		criteria.andIsDelEqualTo(Constant.IS_DEL_NO);
		if (StringUtils.isNotBlank(request.getOrderSn())) {
            criteria.andOrderSnEqualTo(request.getOrderSn());
        }

		List<MasterOrderGoods> orderGoodsList = masterOrderGoodsMapper.selectByExample(masterOrderGoodsExample);
		if (CollectionUtils.isEmpty(orderGoodsList)) {
			response.setMessage("原订单已发货商品列表为空");
			return response;
		}
		//获取已退货量
		List<Byte> typeList = new ArrayList<Byte>();
		typeList.add(ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS);
		typeList.add(ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS);
		OrderReturnExample orderReturnExample = new OrderReturnExample();
		orderReturnExample.or().andReturnTypeIn(typeList)
		.andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY)
		.andRelatingOrderSnEqualTo(relOrderSn);
		List<OrderReturn> orderReturnList = orderReturnMapper.selectByExample(orderReturnExample);

		Double oldReturnBonusMoney = 0d;
		//已退货量汇总统计
		Map<String, Integer> haveReturnGoodsMap = new HashMap<String, Integer>(Constant.DEFAULT_MAP_SIZE);
		if (orderReturnList.size() > 0) {
			//根据查出来的退单sn从退单商品表获取所有关联商品的已退货商品数量
			List<String> returnSns = new ArrayList<String>();
			for (OrderReturn orderReturn : orderReturnList) {
				returnSns.add(orderReturn.getReturnSn());
				oldReturnBonusMoney += orderReturn.getReturnBonusMoney().doubleValue();
			}
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			orderReturnGoodsExample.or().andRelatingReturnSnIn(returnSns);
			List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			if (CollectionUtils.isNotEmpty(orderReturnGoodsList)) {
				for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
					Integer haveReturn = orderReturnGoods.getGoodsReturnNumber().intValue();
					Integer haveReturn2 = orderReturnGoods.getGoodsReturnNumber().intValue();
					String returnKey = orderReturnGoods.getCustomCode() + orderReturnGoods.getExtensionCode() + orderReturnGoods.getExtensionId() + orderReturnGoods.getOsDepotCode();
					String returnKey2 = orderReturnGoods.getCustomCode() + orderReturnGoods.getExtensionCode() + orderReturnGoods.getOsDepotCode();
					if (haveReturnGoodsMap.containsKey(returnKey)) {
						haveReturn = haveReturnGoodsMap.get(returnKey) + orderReturnGoods.getGoodsReturnNumber().intValue();
					}
					if (haveReturnGoodsMap.containsKey(returnKey2)) {
						haveReturn2 = haveReturnGoodsMap.get(returnKey2) + orderReturnGoods.getGoodsReturnNumber().intValue();
					}
					haveReturnGoodsMap.put(returnKey, haveReturn);
					haveReturnGoodsMap.put(returnKey2, haveReturn2);
				}
			}
		}

        //关联申请单
        GoodsReturnChangeExample goodsReturnChangeExample = new GoodsReturnChangeExample();
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(1);
        statusList.add(3);
        goodsReturnChangeExample.or().andOrderSnEqualTo(relOrderSn).andStatusIn(statusList);
        List<GoodsReturnChange> goodsReturnChanges = goodsReturnChangeMapper.selectByExample(goodsReturnChangeExample);
        List<GoodsReturnChangeDetailVo> detailVoList = null;
        if (StringUtil.isListNotNull(goodsReturnChanges)) {
            GoodsReturnChange goodsReturnChange = goodsReturnChanges.get(0);
            String returnchangeSn = goodsReturnChange.getReturnchangeSn();
            GoodsReturnChangeDetailExample goodsReturnChangeDetailExample = new GoodsReturnChangeDetailExample();
            goodsReturnChangeDetailExample.or().andReturnchangeSnEqualTo(returnchangeSn);
            List<GoodsReturnChangeDetail> details = goodsReturnChangeDetailMapper.selectByExample(goodsReturnChangeDetailExample);
            if (StringUtil.isListNotNull(details)) {
                detailVoList = new ArrayList<GoodsReturnChangeDetailVo>();
                for (GoodsReturnChangeDetail detail : details) {
                    GoodsReturnChangeDetailVo vo = new GoodsReturnChangeDetailVo();
                    cloneBean(vo, detail);
                    vo.setDownReturnSum(vo.getGoodsNumber() - vo.getReturnSum());
                    detailVoList.add(vo);
                }
                response.setReturnChangeGoods(detailVoList);
            }
        }
		
		//商品信息
		List<ReturnGoodsVO> returnGoodsVOList = new ArrayList<ReturnGoodsVO>();
		double totalGoodsMoney = 0D;
		double settlementPrice = 0D;
		for (MasterOrderGoods orderGoods : orderGoodsList) {
			ReturnGoodsVO returnGoodsVO = fullApplyReturnGoods(orderGoods, returnCommon, haveReturnGoodsMap, returnType);
			if (returnGoodsVO == null) {
				continue ;
			}
			returnGoodsVOList.add(returnGoodsVO);
			settlementPrice += orderGoods.getSettlementPrice().multiply(new BigDecimal(orderGoods.getGoodsNumber())).doubleValue();
			totalGoodsMoney += new BigDecimal(returnGoodsVO.getTransactionPrice()).multiply(new BigDecimal(returnGoodsVO.getCanReturnCount())).doubleValue();
		}
        returnGoodsVOList = mergeReturnGoods(returnGoodsVOList, detailVoList);

		//订单支付信息
		ReturnAccountVO returnAccount = new ReturnAccountVO();
		MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
		masterOrderPayExample.or().andMasterOrderSnEqualTo(relOrderSn);
		List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
		//支付方式
		List<PayType> createOrderPayList = new ArrayList<PayType>();
		Byte payId = 0;
		BigDecimal totalFee = new BigDecimal(0);
		for (MasterOrderPay masterOrderPay : masterOrderPayList) {
//			PayType payType = new PayType();
			if (payId == 0) {
				payId = masterOrderPay.getPayId();
			}
//			payType.setPId(masterOrderPay.getPayId());
//			payType.setPayFee(masterOrderPay.getPayTotalfee().doubleValue());
			totalFee = totalFee.add(masterOrderPay.getPayTotalfee());
		}
		PayType payType = new PayType();
		payType.setPId(payId);
		payType.setPayFee(totalFee.subtract(master.getShippingTotalFee()).doubleValue());
		createOrderPayList.add(payType);
		SystemPaymentExample paymentExample = new SystemPaymentExample();
		paymentExample.or();
		List<SystemPayment> payments = systemPaymentMapper.selectByExample(paymentExample);
		if (StringUtil.isListNotNull(payments)) {
			List<ReturnPaymentVO> paymentVOs = new ArrayList<ReturnPaymentVO>();
			for (SystemPayment payment : payments) {
				ReturnPaymentVO paymentVO = new ReturnPaymentVO();
				paymentVO.setPayCode(payment.getPayCode());
				paymentVO.setPayId(payment.getPayId());
				paymentVO.setPayName(payment.getPayName());
				paymentVO.setSelected(false);
				if (payment.getPayId() == payId) {
					paymentVO.setSelected(true);
				}
				paymentVOs.add(paymentVO);
			}
			response.setReturnPars(paymentVOs);
		}
		
		returnAccount.setReturnBonusMoney(master.getBonus().doubleValue());
//		returnAccount.setReturnShipping(master.getShippingTotalFee().doubleValue());
		returnAccount.setReturnShipping(0D);
		double shippingTotalFee = master.getShippingTotalFee().doubleValue();
		returnAccount.setTotalIntegralMoney(master.getIntegralMoney().doubleValue());//积分使用金额
		returnAccount.setReturnGoodsMoney(totalGoodsMoney);
		if(Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()
				|| Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){
			//退货单和失货退货单时才让积分展示
		}else{
			returnAccount.setTotalIntegralMoney(0d);//积分使用金额
		}
		//付款备注
//	  returnAccount.setOrderPayDesc(processOrderPayDesc(orderInfo.getOrderSn()));
		//汇总退单总金额 -初始值
		if(Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() 
				|| Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()
				|| Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()
                || Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY.intValue()){
			//总货款
            BigDecimal returnTotalFee = new BigDecimal(returnAccount.getReturnGoodsMoney()).subtract(new BigDecimal(returnAccount.getTotalIntegralMoney()))
                .subtract(new BigDecimal(returnAccount.getReturnBonusMoney()));
            returnAccount.setReturnTotalFee(returnTotalFee.doubleValue());
		}else{
			//运费
			returnAccount.setReturnTotalFee(shippingTotalFee);
		}
//		//退单申请 - 付款信息平摊
//		List<OrderPay> createOrderPayList = processOrderPayShareList(orderInfo.getOrderSn(), orderInfo.getTransType().intValue(), returnAccount.getReturnTotalFee().doubleValue());
//		logger.debug("getOrderReturnDetailVO.orderSn:"+orderInfo.getOrderSn()+",createOrderPayList:"+JSON.toJSONString(createOrderPayList));
		returnOrderVO.setReturnCommon(returnCommon);
		returnOrderVO.setReturnGoodsList(returnGoodsVOList);
		returnOrderVO.setReturnAccount(returnAccount);
		returnOrderVO.setOrderPayList(createOrderPayList);
		// 订单信息
		// 使用主单号创建退单
		if(master != null){
			OrderItemDetail itemDetail = new OrderItemDetail();
			cloneBean(itemDetail, master);
			response.setItemDetail(itemDetail);
		}
		List<OrderItemPayDetail> payDetails = masterOrderPayTypeDetailMapper.getOrderItemPayDetail(relOrderSn);
		if (StringUtil.isListNotNull(payDetails)) {
			response.setPayDetails(payDetails);
		}

        AdminUser adminUser = new AdminUser();
		adminUser.setUserId(request.getActionUserId());
		adminUser.setUserName(request.getActionUser());
		ReturnItemStatusUtils statusUtils = new ReturnItemStatusUtils(returnCommon, adminUser);
		response.setStatusUtils(statusUtils);
		response.setReturnOrderVO(returnOrderVO);
		response.setMessage("退单创建初始化成功");
		response.setSuccess(true);
		response.setReturnOrderVO(returnOrderVO);

		return response;
	}

    /**
     * 汇总退单商品
     * @param returnGoodsVOList
     * @return
     */
    private List<ReturnGoodsVO> mergeReturnGoods(List<ReturnGoodsVO> returnGoodsVOList, List<GoodsReturnChangeDetailVo> detailVoList) {
        if (StringUtil.isListNull(returnGoodsVOList)) {
            return returnGoodsVOList;
        }
        Map<String, GoodsReturnChangeDetailVo> changeMap = new HashMap<String, GoodsReturnChangeDetailVo>();
        if (StringUtil.isListNotNull(detailVoList)) {
            for (GoodsReturnChangeDetailVo vo : detailVoList) {
                changeMap.put(vo.getCustomCode(), vo);
            }
        }

        Map<String, List<ReturnGoodsVO>> map = new HashMap<String, List<ReturnGoodsVO>>();
        for (ReturnGoodsVO vo : returnGoodsVOList) {
            String key = vo.getCustomCode() + "_" + vo.getExtensionCode();
            List<ReturnGoodsVO> returnGoodsVOS = map.get(key);
            if (returnGoodsVOS == null) {
                returnGoodsVOS = new ArrayList<ReturnGoodsVO>();
            }
            returnGoodsVOS.add(vo);
            map.put(key, returnGoodsVOS);
        }

        List<ReturnGoodsVO> voList = new ArrayList<ReturnGoodsVO>();
        for (Map.Entry<String, List<ReturnGoodsVO>> entry : map.entrySet()) {
            List<ReturnGoodsVO> returnGoodsVOS = entry.getValue();
            if (StringUtil.isListNull(returnGoodsVOS)) {
                continue;
            }

            int goodsNum = 0;
            int haveReturnGoodsNum = 0;
            int canReturnGoodsNum = 0;
            int priceDifferNum = 0;
            BigDecimal disCountTotal = new BigDecimal(0);
            BigDecimal inMoney = new BigDecimal(0);
            BigDecimal bv = new BigDecimal(0);
            BigDecimal priceDiffTotal = new BigDecimal(0);
            for (ReturnGoodsVO vo : returnGoodsVOS) {
                Integer goodsBuyNumber = vo.getGoodsBuyNumber();
                goodsNum += goodsBuyNumber;
                //已退货量
                haveReturnGoodsNum += vo.getHavedReturnCount();
                //可退货量
                canReturnGoodsNum += vo.getCanReturnCount();
                //折扣
                Double discount = vo.getDiscount();
                if (discount != null) {
                    disCountTotal = disCountTotal.add(new BigDecimal(discount));
                }
                //积分
                Double integralMoney = vo.getIntegralMoney();
                if (integralMoney != null) {
                    inMoney = inMoney.add(new BigDecimal(integralMoney));
                }
                //bv
                Integer bvValue = vo.getBvValue();
                if (bvValue != null) {
                    bv = bv.add(new BigDecimal(bvValue));
                }

                //退差价数量
                if (vo.getPriceDifferNum() != null) {
                    priceDifferNum += vo.getPriceDifferNum();
                }
                //退差价小计
                if (vo.getPriceDiffTotal() != null) {
                    priceDiffTotal = priceDiffTotal.add(new BigDecimal(vo.getPriceDiffTotal()));
                }
            }

            ReturnGoodsVO vo = returnGoodsVOS.get(0);
            vo.setGoodsBuyNumber(goodsNum);
            vo.setHavedReturnCount(haveReturnGoodsNum);
            vo.setCanReturnCount(canReturnGoodsNum);
            vo.setDiscount(disCountTotal.doubleValue());
            vo.setIntegralMoney(inMoney.doubleValue());
            vo.setBvValue(bv.intValue());
            vo.setPriceDifferNum(priceDifferNum);
            vo.setPriceDiffTotal(priceDiffTotal.doubleValue());
            //填充申请信息
            String customCode = vo.getCustomCode();
            GoodsReturnChangeDetailVo detailVo = changeMap.get(customCode);
            if (detailVo != null) {
                vo.setChange(true);
                vo.setDealReturnNum(detailVo.getReturnSum());
            }

            voList.add(vo);
        }
        return voList;
    }

	/**
	 * 退单沟通
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemCommunicate(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单沟通失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单沟通失败");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		String actionUser = request.getActionUser();
		if (StringUtil.isTrimEmpty(actionUser)) {
			response.setMessage("操作人为空");
			return response;
		}
		try {
			ReturnInfo<String> info = orderReturnStService.returnOrderCommunicate(returnSn, request.getMessage(), actionUser);
			if (info == null) {
				response.setMessage("退单沟通失败");
				return response;
			}
			if (Constant.OS_NO == info.getIsOk()) {
				response.setMessage("退单沟通失败:" + info.getMessage());
				return response;
			}
			response.setMessage("退单沟通成功");
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error("退单沟通失败:" + e.getMessage(), e);
			response.setMessage("退单沟通失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 查询退单日志
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse getReturnItemLog(ReturnManagementRequest request) {

		return null;
	}

	/**
	 * 退单创建
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemCreate(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单创建失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单创建失败");
			return response;
		}
		CreateOrderReturnBean param = request.getReturnBean();
		if (param == null) {
			response.setMessage("退单创建失败");
			return response;
		}
		try {
		    //存在未确认退单不允许创建退单
			OrderReturnExample returnExample = new OrderReturnExample();
			returnExample.or().andMasterOrderSnEqualTo(param.getRelatingOrderSn())
                .andReturnOrderStatusEqualTo(ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM)
//				.andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY)
					.andReturnTypeEqualTo(param.getReturnType().byteValue());
			List<OrderReturn> orderReturns = orderReturnMapper.selectByExample(returnExample);
			if (StringUtil.isListNotNull(orderReturns)) {
				response.setMessage("订单" + param.getRelatingOrderSn() + "存在待确认退单");
				return response;
			}

			//校验退单商品信息
            ReturnInfo<Boolean> verifyInfo = verifyReturnOrderInfo(param);
			if (verifyInfo == null || verifyInfo.getIsOk() == 0) {
			    String message = "校验退单商品信息异常";
			    if (verifyInfo != null) {
                    message = verifyInfo.getMessage();
                }
                response.setMessage(message);
                response.setSuccess(false);
                return response;
            }

			ReturnInfo<String> info = orderReturnService.createOrderReturn(param);
			if (info == null) {
				response.setMessage("退单创建失败");
				return response;
			}
			if (Constant.OS_NO == info.getIsOk()) {
				response.setMessage("退单创建失败:" + info.getMessage());
				return response;
			}

			response.setReturnSn(info.getReturnSn());
			response.setMessage("退单创建成功");
			response.setSuccess(true);

            //退单成功，回写申请单状态为已完成
            GoodsReturnChangeExample goodsReturnChangeExample = new GoodsReturnChangeExample();
            List<Integer> statusList = new ArrayList<Integer>();
            statusList.add(1);
            statusList.add(3);
            goodsReturnChangeExample.or().andOrderSnEqualTo(param.getRelatingOrderSn()).andStatusIn(statusList);
            GoodsReturnChange change = new GoodsReturnChange();
            change.setStatus(2);
            goodsReturnChangeMapper.updateByExampleSelective(change, goodsReturnChangeExample);

            //退单确认
            if (request.getIsConfirm() == 1) {
                request.setReturnSn(info.getReturnSn());
                request.setActionUser(param.getActionUser());
                ReturnManagementResponse returnManagementResponse = returnItemConfirm(request);
                logger.info("退单确认结果：" + JSONObject.toJSONString(returnManagementResponse));
            }

		} catch (Exception e) {
			logger.error("退单创建失败:" + e.getMessage(), e);
			response.setMessage("退单创建失败:" + e.getMessage());
		}
		return response;
	}

    /**
	 * 退单锁定
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemLock(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单锁定失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单锁定失败");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		String userId = request.getActionUserId();
		if (StringUtil.isTrimEmpty(userId)) {
			response.setMessage("操作人为空");
			return response;
		}
		try {
			ReturnInfo<String> info = orderReturnStService.returnOrderLock(returnSn,
					request.getMessage(), request.getActionUser(), Integer.valueOf(userId));
			if (info == null) {
				response.setMessage("退单锁定失败");
				return response;
			}
			if (Constant.OS_NO == info.getIsOk()) {
				response.setMessage("退单锁定失败:" + info.getMessage());
				return response;
			}
			response.setMessage("退单锁定成功");
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error("退单锁定失败:" + e.getMessage(), e);
			response.setMessage("退单锁定失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单解锁
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemUnLock(ReturnManagementRequest request) {
	    logger.info("退单解锁 -> request : " + JSONObject.toJSONString(request));
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单解锁失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单解锁失败");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		String userId = request.getActionUserId();
		if (StringUtil.isTrimEmpty(userId)) {
			response.setMessage("操作人为空");
			return response;
		}
		try {
			ReturnInfo<String> info = orderReturnStService.returnOrderUnLock(returnSn,
					request.getMessage(), request.getActionUser(), Integer.valueOf(userId));
			if (info == null) {
				response.setMessage("退单解锁失败");
				return response;
			}
			if (Constant.OS_NO == info.getIsOk()) {
				response.setMessage(info.getMessage());
				return response;
			}
			response.setMessage("退单解锁成功");
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error("退单解锁失败:" + e.getMessage(), e);
			response.setMessage("退单解锁失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单确认
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemConfirm(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单确认失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单创建失败");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		try {
			ReturnInfo<String> info = orderReturnStService.returnOrderConfirm(returnSn, "退单确认", request.getActionUser());
			if (info == null) {
				response.setMessage("退单确认失败");
				return response;
			}
			if (Constant.OS_NO == info.getIsOk()) {
				response.setMessage("退单确认失败:" + info.getMessage());
				return response;
			}
			response.setMessage("退单确认成功");
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error("退单确认失败:" + e.getMessage(), e);
			response.setMessage("退单确认失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单完成
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemCompleted(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单完成失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单完成失败");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		try {
			OrderStatus orderStatus = new OrderStatus("退单完成", request.getActionUser());
			orderStatus.setCode("1");
			orderStatus.setReturnSn(returnSn);
			ReturnInfo<String> info = orderReturnService.orderReturnFinish(orderStatus);
			if (info == null) {
				response.setMessage("退单完成失败");
				return response;
			}
			if (Constant.OS_NO == info.getIsOk()) {
				response.setMessage("退单完成失败:" + info.getMessage());
				return response;
			}
			response.setMessage("退单完成成功");
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error("退单完成失败:" + e.getMessage(), e);
			response.setMessage("退单完成失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单作废
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemCancel(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单取消失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单取消失败");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		try {
			ReturnInfo<String> info = orderReturnStService.returnOrderInvalid(returnSn, request.getMessage(), request.getActionUser());
			if (info == null) {
				response.setMessage("退单取消失败");
				return response;
			}
			if (Constant.OS_NO == info.getIsOk()) {
				response.setMessage("退单取消失败:" + info.getMessage());
				return response;
			}
			response.setMessage("退单取消成功");
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error("退单取消失败:" + e.getMessage(), e);
			response.setMessage("退单取消失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单入库
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemStorage(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单入库失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单入库失败");
			return response;
		}
		// 退单号
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		try {
			OrderReturn orderReturn = null;
			// 判断是否是订单号
			if (!returnSn.startsWith(Constant.OR_BEGIN_WITH_TD)) {
				// 订单号
				OrderReturnExample returnExample = new OrderReturnExample();
				returnExample.or().andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY)
				.andMasterOrderSnEqualTo(returnSn);
				List<OrderReturn> orderReturns = orderReturnMapper.selectByExample(returnExample);
				if (StringUtil.isListNull(orderReturns)) {
					response.setMessage("订单[" + returnSn + "]不存在退单");
					return response;
				}
				orderReturn = orderReturns.get(0);
				returnSn = orderReturn.getReturnSn();
			} else {
				orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn.trim());
				if (orderReturn == null) {
					response.setMessage("退单[" + returnSn + "]不存在");
					return response;
				}
			}
			ReturnOrderParam returnOrderParam = new ReturnOrderParam();
			// 退单号
			returnOrderParam.setReturnSn(returnSn);
			// 退单商品
			returnOrderParam.setStorageGoods(request.getStorageGoods());
			// 操作人
			returnOrderParam.setUserName(request.getActionUser());
			returnOrderParam.setPullInAll(false);
			returnOrderParam.setActionNote("商品入库");
			returnOrderParam.setToERP(false);
			ReturnInfo<String> info = orderReturnStService.returnOrderStorage(returnOrderParam);
			if (info == null) {
				response.setMessage("退单入库失败");
				return response;
			}
			if (Constant.OS_NO == info.getIsOk()) {
				response.setMessage("退单入库失败:" + info.getMessage());
				return response;
			}
			
			// 退单入库成功,发起退款消息
			String masterOrderSn = orderReturn.getRelatingOrderSn();
			String siteCode = orderReturn.getSiteCode();
			String userId = orderReturn.getUserId();
			String returnOrderAmount = orderReturn.getReturnTotalFee().toString();
			
			OrderReturnMoneyBean orderReturnMoneyBean = new OrderReturnMoneyBean();
			orderReturnMoneyBean.setSiteCode(siteCode);
			orderReturnMoneyBean.setUserId(userId);
			orderReturnMoneyBean.setOrderSn(masterOrderSn);
			orderReturnMoneyBean.setReturnPaySn(returnSn);
			orderReturnMoneyBean.setReturnOrderAmount(returnOrderAmount);
			
			logger.info("退单入库完成,自动退款:" + JSONObject.toJSONString(orderReturnMoneyBean));
			orderCancelService.doOrderReturnMoney(orderReturnMoneyBean);
			
			response.setMessage("退单入库成功");
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error("退单入库失败:" + e.getMessage(), e);
			response.setMessage("退单入库失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单结算
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnItemSettlement(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单结算失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单结算失败");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		try {
			ReturnInfo<String> info = orderReturnStService.returnOrderSettle(returnSn, "退单结算", request.getActionUser());
			if (info == null) {
				response.setMessage("退单结算失败");
				return response;
			}
			if (Constant.OS_NO == info.getIsOk()) {
				response.setMessage("退单结算失败:" + info.getMessage());
				return response;
			}
			response.setMessage("退单结算成功");
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error(returnSn + "退单结算失败:" + e.getMessage(), e);
			response.setMessage("退单结算失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单待入库明细
	 * @param request 请求参数
	 * @return OmsBaseResponse<ReturnGoodsVO>
	 */
	@Override
	public OmsBaseResponse<ReturnGoodsVO> returnWaitStorageItem(ReturnManagementRequest request) {
		OmsBaseResponse<ReturnGoodsVO> response = new OmsBaseResponse<ReturnGoodsVO>();
		response.setMessage("查询退单待入库明细失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("查询退单待入库明细失败");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		List<ReturnGoodsVO> returnGoodsVOList = new ArrayList<ReturnGoodsVO>();
		try {
			if (!returnSn.trim().startsWith(Constant.OR_BEGIN_WITH_TD)) {
				// 订单号
				OrderReturnExample returnExample = new OrderReturnExample();
				returnExample.or().andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY)
				.andMasterOrderSnEqualTo(returnSn);
				List<OrderReturn> orderReturns = orderReturnMapper.selectByExample(returnExample);
				if (StringUtil.isListNull(orderReturns)) {
					response.setMessage("订单[" + returnSn + "]不存在退单");
					return response;
				}
				returnSn = orderReturns.get(0).getReturnSn();
			} else {
				OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn.trim());
				if (orderReturn == null) {
					response.setMessage("退单[" + returnSn + "]不存在");
					return response;
				}
			}
			//商品数据
			OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
			orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
			List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
			if (CollectionUtils.isNotEmpty(orderReturnGoodsList)) {
				for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
					if (orderReturnGoods.getGoodsReturnNumber().intValue() > orderReturnGoods.getProdscannum().intValue()) {
						ReturnGoodsVO returnGoodsVO = new ReturnGoodsVO();
						cloneBean(returnGoodsVO, orderReturnGoods);
						returnGoodsVO.setCanReturnCount(orderReturnGoods.getGoodsReturnNumber() - orderReturnGoods.getHaveReturnCount().intValue());
						returnGoodsVO.setProdScanNum(orderReturnGoods.getProdscannum().intValue());
						returnGoodsVOList.add(returnGoodsVO);
					}
				}
			}
			if (StringUtil.isListNull(returnGoodsVOList)) {
				response.setMessage("退单没有待入库明细");
				response.setSuccess(false);
			} else {
				response.setMessage("查询退单待入库明细成功");
				response.setList(returnGoodsVOList);
				response.setSuccess(true);
			}
		} catch (Exception e) {
			logger.error(returnSn + "退单待入库商品查询失败:" + e.getMessage(), e);
			response.setMessage("退单待入库商品查询失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单退款完成
	 * @param request 请求参数
	 * 退单退款完成  退单号、实际退款金额、操作人
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnRefundCompleted(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单退款完成失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("退单退款完成失败");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}
		if (request.getActualRefundAmount() == null) {
			response.setMessage("实际退款金额为空");
			return response;
		}

        String message = request.getMessage();
        if (StringUtils.isBlank(message)) {
            message = "普通退款：";
        }

		String actionNote = null;
		Integer returnOrderStatus = null;
		try {
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn.trim());
			if (orderReturn == null) {
				response.setMessage("退单[" + returnSn + "]不存在");
				return response;
			}
			if (request.getRefundResult()) {
				OrderRefundExample refundExample = new OrderRefundExample();
				refundExample.or().andRelatingReturnSnEqualTo(returnSn.trim());
				List<OrderRefund> orderRefunds = orderRefundMapper.selectByExample(refundExample);
				if (StringUtil.isListNull(orderRefunds)) {
					response.setMessage("退单[" + returnSn + "]退款单据不存在");
					return response;
				}
				OrderRefund orderRefund = orderRefunds.get(0);
				// 已退
				orderRefund.setBackbalance((byte) 1);
				orderRefund.setActualRefundAmount(request.getActualRefundAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
				orderRefund.setUpdateTime(new Date());
				orderRefundMapper.updateByPrimaryKeySelective(orderRefund);
				actionNote = message + "退单退款完成,退款金额" + request.getActualRefundAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
				//退款成功
                returnOrderStatus = 5;
			} else {
				actionNote = message + "退单退款失败,退款金额" + request.getActualRefundAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			response.setMessage("退单退款完成成功");
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error(returnSn + "退单退款完成失败:" + e.getMessage(), e);
			response.setMessage("退单退款完成失败:" + e.getMessage());
			actionNote = message + "退单退款完成失败:" + e.getMessage();
		} finally {
			//操作日志
			orderActionService.addOrderReturnAction(returnSn, actionNote, request.getActionUser(), returnOrderStatus);
		}
		return response;
	}

    /**
     * 手动退款
     * @param request
     * @return
     */
    @Override
    public ReturnManagementResponse manualRefund(ReturnManagementRequest request) {
        ReturnManagementResponse response = new ReturnManagementResponse();
        if (request == null) {
            response.setMessage("请求参数不能为空");
            return response;
        }

        try {
            String siteCode = request.getSiteCode();
            if (StringUtils.isBlank(siteCode)) {
                response.setMessage("站点不能为空");
                return response;
            }

            String masterOrderSn = request.getMasterOrderSn();
            if (StringUtils.isBlank(masterOrderSn)) {
                response.setMessage("订单号不能为空");
                return response;
            }

            String returnSn = request.getReturnSn();
            if (StringUtils.isBlank(returnSn)) {
                response.setMessage("退单号不能为空");
                return response;
            }

            String returnOrderAmount = request.getReturnOrderAmount();
            if (StringUtils.isBlank(returnOrderAmount) && Double.valueOf(returnOrderAmount) > 0) {
                response.setMessage("退单金额不能为空并且必须大于0");
                return response;
            }

            String userId = request.getUserId();
            if (StringUtils.isBlank(userId)) {
                response.setMessage("下单人不能为空");
                return response;
            }

            OrderReturnMoneyBean bean = new OrderReturnMoneyBean();
            bean.setOrderSn(masterOrderSn);
            bean.setReturnPaySn(returnSn);
            bean.setReturnOrderAmount(returnOrderAmount);
            bean.setUserId(userId);
            bean.setSiteCode(siteCode);
            orderCancelService.doOrderReturnMoney(bean);
            response.setSuccess(true);
            response.setMessage("发起退款成功");

        } catch (Exception e) {
            logger.error("手动退款异常" + JSONObject.toJSONString(request));
            response.setMessage("手动退款异常");
        }

        return response;
    }

	/**
	 * 退单对账单已生成
	 * @param request
	 * @return ReturnManagementResponse
	 */
	@Override
	public ReturnManagementResponse returnOrderBillCompleted(ReturnManagementRequest request) {
		ReturnManagementResponse response = new ReturnManagementResponse();
		response.setMessage("退单对账单生成状态失败");
		response.setSuccess(false);
		if (request == null) {
			response.setMessage("参数为空");
			return response;
		}
		String returnSn = request.getReturnSn();
		if (StringUtil.isTrimEmpty(returnSn)) {
			response.setMessage("退单号为空");
			return response;
		}

		String actionNote = "对账单生成状态处理成功";
		Integer returnOrderStatus = 7;
		try {
			OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn.trim());
			if (orderReturn == null) {
				response.setMessage("退单[" + returnSn + "]不存在");
				return response;
			}

			if (orderReturn.getSelltedBillStatus() != null && orderReturn.getSelltedBillStatus() == 1) {
				response.setMessage("对账单生成状态处理成功");
				response.setSuccess(true);
				return response;
			}

			OrderReturn updateOrderReturn = new OrderReturn();
			updateOrderReturn.setReturnSn(returnSn);
			updateOrderReturn.setSelltedBillStatus(1);
			updateOrderReturn.setUpdateTime(new Date());
			orderReturnMapper.updateByPrimaryKeySelective(updateOrderReturn);

			response.setMessage("对账单生成状态处理成功");
			response.setSuccess(true);

		} catch (Exception e) {
			logger.error(returnSn + "退单对账单生成状态处理失败:" + e.getMessage(), e);
			response.setMessage("退单对账单生成状态处理失败:" + e.getMessage());
			actionNote = "退单对账单生成状态处理失败:" + e.getMessage();
		} finally {
			//操作日志
			orderActionService.addOrderReturnAction(returnSn, actionNote, request.getActionUser(), returnOrderStatus);
		}

		return response;
	}

    /**
     * 订单退款操作
     * @param orderReturnBean
     */
    @Override
    public ReturnManagementResponse doOrderReturnMoneyByCommon(OrderReturnBean orderReturnBean) {

        ReturnManagementResponse response = new ReturnManagementResponse();
		response.setSuccess(false);
        response.setMessage("退款失败");
        //添加退款处理日志
        String returnSn = orderReturnBean.getReturnSn();
        OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
        if (orderReturn == null) {
            response.setMessage("退单不存在");
            return response;
        }
        String userId = orderReturnBean.getUserId();
        if (StringUtils.isBlank(userId)) {
            userId = Constant.OS_STRING_SYSTEM;
        }

        ReturnManagementResponse accountResponse = null;
		int returnType = orderReturnBean.getType();
		// 结算账户
		if (returnType == 3) {
            accountResponse = fillOrderReturnBeanBySettlementAccount(orderReturn, orderReturnBean);
		} else if (returnType == 1 || returnType == 4) {
            accountResponse = fillOrderReturnBeanByUserAccount(orderReturn, orderReturnBean);
		}

		if (accountResponse != null && !accountResponse.getSuccess()) {
            response.setMessage(accountResponse.getMessage());
            return response;
        }

        OrderReturnAction orderReturnAction = new OrderReturnAction();
        orderReturnAction.setActionUser(userId);
        orderReturnAction.setActionNote(returnSn + "退款已申请,请耐心等待");
        orderReturnAction.setReturnSn(returnSn);
        orderReturnAction.setLogTime(new Date());
        orderReturnAction.setReturnOrderStatus(orderReturn.getReturnOrderStatus()==null ? 0 : Integer.valueOf(orderReturn.getReturnOrderStatus()));
        orderReturnAction.setReturnShippingStatus(orderReturn.getShipStatus() ==null ?0:orderReturn.getShipStatus().intValue());
        orderReturnAction.setReturnPayStatus(orderReturn.getPayStatus()== null ?0:orderReturn.getPayStatus().intValue());
        orderReturnActionMapper.insertSelective(orderReturnAction);

        orderCancelService.doOrderReturnMoneyByCommon(orderReturnBean);

        response.setMessage("已提交退款,请稍后");
        response.setSuccess(true);
        return response;
    }

	/**
	 * 设置信用额度退款信息
	 * @param orderReturn
	 * @param orderReturnBean
	 */
	private ReturnManagementResponse fillOrderReturnBeanByUserAccount(OrderReturn orderReturn, OrderReturnBean orderReturnBean) {
        ReturnManagementResponse response = new ReturnManagementResponse();
		response.setSuccess(false);
		String masterOrderSn = orderReturnBean.getMasterOrderSn();
		MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendService.getMasterOrderInfoExtendById(masterOrderSn);

		if (masterOrderInfoExtend == null) {
            response.setMessage("订单不存在 ");
			return response;
		}

		orderReturnBean.setCompanyId(masterOrderInfoExtend.getCompanyCode());
        response.setSuccess(true);
        response.setMessage("成功");
        return response;
	}

    /**
     * 设置账期支付退款信息
     * @param orderReturn
     * @param orderReturnBean
     */
    private ReturnManagementResponse fillOrderReturnBeanBySettlementAccount(OrderReturn orderReturn, OrderReturnBean orderReturnBean) {

        ReturnManagementResponse response = new ReturnManagementResponse();
		response.setSuccess(false);
        String masterOrderSn = orderReturnBean.getMasterOrderSn();
        MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendService.getMasterOrderInfoExtendById(masterOrderSn);

        if (masterOrderInfoExtend == null) {
            response.setMessage("订单不存在");
            return response;
        }

        int payPeriodStatus = masterOrderInfoExtend.getPayPeriodStatus();
        if (payPeriodStatus == 0) {
            response.setMessage("订单未正常扣款,请扣款后再退款");
            return response;
        }

        orderReturnBean.setCompanyId(masterOrderInfoExtend.getCompanyCode());
        orderReturnBean.setSettlement(masterOrderInfoExtend.getSettlementAccount());

        List<MasterOrderPay> orderPayList = masterOrderPayService.getMasterOrderPayList(masterOrderSn);
        if (orderPayList == null || orderPayList.size() == 0) {
            response.setMessage("订单支付单不存在");
            return response;
        }

        MasterOrderPay masterOrderPay = orderPayList.get(0);
        orderReturnBean.setPayNo(masterOrderPay.getPayNote());

        response.setSuccess(true);
        response.setMessage("成功");
        return response;
    }

	/**
	 * 退单状态显示
	 * @param common 退单信息
	 * @return String
	 */
	private String displayReturnStatus(ReturnCommonVO common){
		//退单状态
		String orderStatus = StringUtils.EMPTY;
		if (common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM) {
			orderStatus = "未确认";
		} else if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.CONFIRMED) {
			orderStatus = "已确认";
		} else if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY) {
			orderStatus = "无效";
		} else if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE) {
			orderStatus = "已完成";
		} else {
			orderStatus = "异常";
		}
		//付款状态
		String payStatus = StringUtils.EMPTY;
		if (common.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED) {
			payStatus = "已结算";
		} else if(common.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE) {
			payStatus = "待结算";
		} else {
			payStatus = "未结算";
		}
		String passStatus = StringUtils.EMPTY;
		String checkInStatus = StringUtils.EMPTY;
		if (common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()
				|| common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()
				|| common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()) {
			//入库状态
			if (common.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE) {
				checkInStatus = "已入库";
			} else if (common.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.WAITSTORAGE) {
				checkInStatus = "待入库";
			} else if (common.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE) {
				checkInStatus = "部分入库";
			} else {
				checkInStatus = "未入库";
			}
		}

        if (common.getReturnType() == 3) {
            return orderStatus;
        }
		return orderStatus + "&nbsp;" + payStatus + "&nbsp;" + passStatus + "&nbsp;" + checkInStatus;
	}

	/**
	 * 填充退单信息
	 * @param master 主订单信息
	 * @param returnType 退单类型
	 * @return ReturnCommonVO
	 */
	private ReturnCommonVO fullReturnInfo(MasterOrderDetail master, String returnType) {
		ReturnCommonVO commonVO = new ReturnCommonVO();
		commonVO.setChannelName(master.getReferer());
		commonVO.setChannelCode(master.getOrderFrom());
		commonVO.setUserId(master.getUserId());
		// 订单已付款
		commonVO.setOrderPayedMoney(master.getMoneyPaid().doubleValue());
		// 主单号
		commonVO.setMasterOrderSn(master.getMasterOrderSn());
		commonVO.setSiteCode(master.getChannelCode());
		commonVO.setRelatingOrderSn(master.getMasterOrderSn());
		commonVO.setReturnType(Integer.valueOf(returnType));
		commonVO.setReturnStatusDisplay("申请中");
		commonVO.setDepotCode(Constant.DETAILS_DEPOT_CODE);
		return commonVO;
	}
	
	/**
	 * 填充退单申请信息
	 * @param orderReturn 退单信息
	 * @param orderReturnShip 退单快递信息
	 * @param channelInfo 店铺信息
	 * @return ReturnCommonVO
	 */
	private ReturnCommonVO fullApplyReturnInfo(OrderReturn orderReturn, OrderReturnShip orderReturnShip, ChannelShop channelInfo) {
		ReturnCommonVO returnCommon = new ReturnCommonVO();
		returnCommon.setReturnSn(orderReturn.getReturnSn());
		returnCommon.setRelatingOrderSn(orderReturn.getRelatingOrderSn());
		returnCommon.setReturnType(orderReturn.getReturnType().intValue());
		returnCommon.setReturnOrderStatus(orderReturn.getReturnOrderStatus().intValue());
		returnCommon.setPayStatus(orderReturn.getPayStatus().intValue());
		returnCommon.setShipsStatus(orderReturnShip.getCheckinStatus().intValue());
		returnCommon.setHaveRefund(orderReturn.getHaveRefund().intValue());
		returnCommon.setChannelName(channelInfo.getShopTitle());
		returnCommon.setChannelCode(channelInfo.getChannelCode());
		returnCommon.setUserId(orderReturn.getUserId());
		returnCommon.setReturnDesc(orderReturn.getReturnDesc());
		returnCommon.setIsGoodReceived(orderReturnShip.getIsGoodReceived().intValue());
		returnCommon.setChasedOrNot(orderReturnShip.getChasedOrNot().intValue());
		returnCommon.setReturnSettlementType(orderReturn.getReturnSettlementType().intValue());
		returnCommon.setProcessType(orderReturn.getProcessType().intValue());
		returnCommon.setReturnExpress(orderReturnShip.getReturnExpress());
		returnCommon.setReturnInvoiceNo(orderReturnShip.getReturnInvoiceNo());
		returnCommon.setNewOrderSn(orderReturn.getNewOrderSn());
		returnCommon.setDepotCode(orderReturnShip.getDepotCode());
		returnCommon.setAddTime(DateTimeUtils.format(orderReturn.getAddTime()));
		returnCommon.setCheckInTime(DateTimeUtils.format(orderReturnShip.getCheckinTime()));
		returnCommon.setClearTime(DateTimeUtils.format(orderReturn.getClearTime()));
		returnCommon.setReturnOrderIspass(orderReturnShip.getQualityStatus());
		returnCommon.setLockStatus(orderReturn.getLockStatus());
		returnCommon.setCheckinStatus(orderReturnShip.getCheckinStatus().intValue());
		returnCommon.setBackToCs(orderReturn.getBackToCs());
		returnCommon.setQualityStatus(orderReturnShip.getQualityStatus());
		returnCommon.setReturnStatusDisplay(displayReturnStatus(returnCommon));

        String returnReason = orderReturn.getReturnReason();
        returnCommon.setReturnReason(returnReason);
	/*	if (StringUtils.isNotBlank(returnReason)) {
            OrderCustomDefine orderCustomDefine = orderCustomDefineService.selectCustomDefineByCode(returnReason);
            if (orderCustomDefine != null) {
                returnCommon.setReturnReasonStr(orderCustomDefine.getName());
            }
        }*/

		returnCommon.setRefundType(orderReturn.getRefundType().intValue());
		//主单号
		returnCommon.setMasterOrderSn(orderReturn.getMasterOrderSn());
		returnCommon.setToErp(orderReturn.getToErp().intValue());
		returnCommon.setSiteCode(orderReturn.getSiteCode());

		OrderRefundExample example = new OrderRefundExample();
        example.or().andRelatingReturnSnEqualTo(orderReturn.getReturnSn());
        int backBalance = 0;
        List<OrderRefund> orderRefunds = orderRefundMapper.selectByExample(example);
        if (StringUtil.isListNotNull(orderRefunds)) {
            for (OrderRefund orderRefund : orderRefunds) {
                if (orderRefund.getBackbalance() == 1) {
                    backBalance = 1;
                    break;
                }
            }

            OrderRefund orderRefund = orderRefunds.get(0);
            Short returnPay = orderRefund.getReturnPay();
            SystemPaymentExample paymentExample = new SystemPaymentExample();
            paymentExample.or().andEnabledEqualTo(1).andPayIdEqualTo(returnPay.byteValue());
            List<SystemPayment> systemPaymentList = systemPaymentMapper.selectByExample(paymentExample);
            if (StringUtil.isListNotNull(systemPaymentList)) {
                SystemPayment payment = systemPaymentList.get(0);
                returnCommon.setPayCode(payment.getPayCode());
            }
        }
        returnCommon.setBackBalance(backBalance);

        return returnCommon;
	}

	/**
	 * 填充退单商品
	 * @param orderReturn 退单信息
	 * @param orderReturnGoods 退单商品信息
	 * @return ReturnGoodsVO
	 */
	private ReturnGoodsVO fullReturnGoods(OrderReturn orderReturn, OrderReturnGoods orderReturnGoods) {
		ReturnGoodsVO returnGoodsVO = new ReturnGoodsVO();
		returnGoodsVO.setId(orderReturnGoods.getId());
		returnGoodsVO.setIsGoodReceived(orderReturnGoods.getIsGoodReceived().intValue());
		returnGoodsVO.setQualityStatus(orderReturnGoods.getQualityStatus().intValue());
		returnGoodsVO.setCheckinStatus(orderReturnGoods.getCheckinStatus().intValue());
		
		returnGoodsVO.setExtensionId(orderReturnGoods.getExtensionId());
		returnGoodsVO.setShareSettle(orderReturnGoods.getShareSettle().doubleValue());
		returnGoodsVO.setGoodsName(orderReturnGoods.getGoodsName());
		returnGoodsVO.setExtensionCode(orderReturnGoods.getExtensionCode());
		if (null != orderReturnGoods.getCustomCode()) {
			String goodsSn = orderReturnGoods.getCustomCode().substring(0, 6);
			returnGoodsVO.setGoodsSn(goodsSn);
		}
		returnGoodsVO.setGoodsColorName(orderReturnGoods.getGoodsColorName());
		returnGoodsVO.setGoodsSizeName(orderReturnGoods.getGoodsSizeName());
		returnGoodsVO.setCustomCode(orderReturnGoods.getCustomCode());
		returnGoodsVO.setMarketPrice(orderReturnGoods.getMarketPrice().doubleValue());
		returnGoodsVO.setGoodsPrice(orderReturnGoods.getSalePrice().doubleValue());
        returnGoodsVO.setTransactionPrice(orderReturnGoods.getGoodsPrice().doubleValue());
		returnGoodsVO.setSettlementPrice(orderReturnGoods.getSettlementPrice().doubleValue());
		returnGoodsVO.setShareBonus(orderReturnGoods.getShareBonus().doubleValue());
		returnGoodsVO.setGoodsBuyNumber(orderReturnGoods.getGoodsBuyNumber().intValue());
//		returnGoodsVO.setDiscount((orderReturnGoods.getMarketPrice().doubleValue() - orderReturnGoods.getGoodsPrice().doubleValue()));
        returnGoodsVO.setDiscount(orderReturnGoods.getDiscount().doubleValue());
		//实际扫描数量
		returnGoodsVO.setProdScanNum(orderReturnGoods.getProdscannum().intValue());
		returnGoodsVO.setSalesMode(orderReturnGoods.getSalesMode().intValue());
		//退货单
		returnGoodsVO.setOsDepotCode(orderReturnGoods.getOsDepotCode());
		returnGoodsVO.setShopReturnCount(orderReturnGoods.getChargeBackCount());
		returnGoodsVO.setHavedReturnCount(orderReturnGoods.getHaveReturnCount());
		//可退货量（退货详情页面的可退货量直接从表里取字段就可以了）
		returnGoodsVO.setCanReturnCount(orderReturnGoods.getGoodsReturnNumber().intValue());
		returnGoodsVO.setReturnReason(orderReturnGoods.getReturnReason());
		//退款单
		//退差价数量
		returnGoodsVO.setPriceDifferNum(orderReturnGoods.getPriceDifferNum().intValue());
		//退差价单价
		returnGoodsVO.setPriceDifference(orderReturnGoods.getPriceDifference().doubleValue());
		if (orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY.intValue()) {
			returnGoodsVO.setReturnReason(orderReturnGoods.getPriceDifferReason());
		} else {
			returnGoodsVO.setReturnReason(orderReturnGoods.getReturnReason());
		}
		returnGoodsVO.setSeller(orderReturnGoods.getSeller());
		returnGoodsVO.setIntegralMoney(orderReturnGoods.getIntegralMoney().doubleValue());
		returnGoodsVO.setBoxGauge(orderReturnGoods.getBoxGauge());
		//成本价
        returnGoodsVO.setCostPrice(orderReturnGoods.getCostPrice().doubleValue());
		return returnGoodsVO;
	}
	
	/**
	 * 填充退单申请商品
	 * @param orderGoods 订单商品
	 * @param returnCommon 退单西悉尼
	 * @param haveReturnGoodsMap 可退商品
	 * @param returnType 退单类型
	 * @return ReturnGoodsVO
	 */
	private ReturnGoodsVO fullApplyReturnGoods(MasterOrderGoods orderGoods, ReturnCommonVO returnCommon, Map<String,Integer> haveReturnGoodsMap, String returnType) {
		ReturnGoodsVO returnGoodsVO = new ReturnGoodsVO();
		//已退货数量
		String returnKey2 = orderGoods.getCustomCode() + orderGoods.getExtensionCode() + orderGoods.getDepotCode();
        if(MapUtils.isNotEmpty(haveReturnGoodsMap) && haveReturnGoodsMap.containsKey(returnKey2)){
            returnGoodsVO.setHavedReturnCount(haveReturnGoodsMap.get(returnKey2));
        } else {
			returnGoodsVO.setHavedReturnCount(0);
		}
		//可退货量为零
		int canReturnCount = orderGoods.getGoodsNumber().intValue() - orderGoods.getChargeBackCount() - returnGoodsVO.getHavedReturnCount();
		/*if (canReturnCount < 1) {
			if (returnCommon.getReturnType() == Constant.OR_RETURN_TYPE_RETURN || returnCommon.getReturnType() == Constant.OR_RETURN_TYPE_REJECT
					|| returnCommon.getReturnType() == Constant.OR_RETURN_TYPE_LOSERETURN) {
				return  null;
			}
		}*/

		returnGoodsVO.setCanReturnCount(canReturnCount);
		returnGoodsVO.setSap(orderGoods.getSap());
		returnGoodsVO.setBvValue(StringUtil.isTrimEmpty(orderGoods.getBvValue()) ? 0 : Integer.valueOf(orderGoods.getBvValue()));
		returnGoodsVO.setCustomCode(orderGoods.getCustomCode());
		returnGoodsVO.setGoodsSizeName(orderGoods.getGoodsSizeName());
		returnGoodsVO.setGoodsColorName(orderGoods.getGoodsColorName());
		//图片url
		returnGoodsVO.setGoodsThumb(orderGoods.getGoodsThumb());
		returnGoodsVO.setGoodsName(orderGoods.getGoodsName());
		returnGoodsVO.setExtensionId(orderGoods.getExtensionId());
		returnGoodsVO.setExtensionCode(orderGoods.getExtensionCode());
		returnGoodsVO.setGoodsSn(orderGoods.getGoodsSn());
		returnGoodsVO.setMarketPrice(orderGoods.getMarketPrice().doubleValue());
		returnGoodsVO.setGoodsPrice(orderGoods.getGoodsPrice().doubleValue());
        returnGoodsVO.setTransactionPrice(orderGoods.getTransactionPrice().doubleValue());
		returnGoodsVO.setSettlementPrice(orderGoods.getSettlementPrice().doubleValue());
		returnGoodsVO.setShareBonus(orderGoods.getShareBonus().doubleValue());
		returnGoodsVO.setGoodsBuyNumber(orderGoods.getGoodsNumber().intValue());
		returnGoodsVO.setDiscount(new BigDecimal(String.valueOf(orderGoods.getDiscount())).doubleValue());
		returnGoodsVO.setOsDepotCode(orderGoods.getDepotCode());
		returnGoodsVO.setShopReturnCount(orderGoods.getChargeBackCount());
        returnGoodsVO.setCostPrice(orderGoods.getCostPrice().doubleValue());

		//商品销售模式：1为自营，2为买断，3为寄售，4为直发
		if (orderGoods.getSalesMode() == null) {
			returnGoodsVO.setSalesMode(1);
		} else {
			returnGoodsVO.setSalesMode(orderGoods.getSalesMode().intValue());
		}
		// //供销商编码
		returnGoodsVO.setSeller(orderGoods.getSupplierCode());
		//退货单时才让积分展示
		if (Integer.valueOf(returnType).intValue() == ConstantValues.YESORNO_YES.intValue() || Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){
			//使用积分金额
			returnGoodsVO.setIntegralMoney(orderGoods.getIntegralMoney().doubleValue());
		}
		//消费积分
		returnGoodsVO.setPayPoints(orderGoods.getPayPoints().doubleValue());
		//默认扫描数量为0
		returnGoodsVO.setProdScanNum(0);
		//退差价数量
		returnGoodsVO.setPriceDifferNum(orderGoods.getGoodsNumber().intValue());
		//退货单
		returnGoodsVO.setOsDepotCode(orderGoods.getDepotCode());
		returnGoodsVO.setShopReturnCount(orderGoods.getChargeBackCount());
		returnGoodsVO.setBvValue(StringUtil.isTrimEmpty(orderGoods.getBvValue()) ? 0 : Integer.valueOf(orderGoods.getBvValue()));
		returnGoodsVO.setBaseBvValue(orderGoods.getBaseBvValue());
		returnGoodsVO.setBoxGauge(orderGoods.getBoxGauge() == null ? null : orderGoods.getBoxGauge().intValue());
        returnGoodsVO.setSupplierName(orderGoods.getSupplierName());
		return returnGoodsVO;
	}
	
	private void cloneBean(Object destObj, Object origObj) {
		try {
			BeanUtils.copyProperties(destObj, origObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 退单页面按钮状态控制
	 * @param infoVO
	 * @return
	 */
	private Map<String,Integer> controlOrderReturnButton(ReturnOrderVO infoVO){
		Map<String,Integer> buttonStatusMap = new HashMap<String,Integer>();
		ReturnCommonVO common = infoVO.getReturnCommon();
		if(StringUtils.isEmpty(common.getReturnSn())){
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_LOCKRETURN, ConstantValues.YESORNO_NO);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNLOCKRETURN, ConstantValues.YESORNO_NO);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SAVERETURN, ConstantValues.YESORNO_YES);
		}else{
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_LOCKRETURN, ConstantValues.YESORNO_YES);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNLOCKRETURN, ConstantValues.YESORNO_YES);
		}
			
		if(common.getLockStatus() == 0){
			//未锁定
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_COMMUNICATERETURN, ConstantValues.YESORNO_NO);
		}else{
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_COMMUNICATERETURN, ConstantValues.YESORNO_YES);
			//回退客服和取消回退按钮权限放开
			/*//退货单 、拒收入库单 - 操作 - 启用回退客服
			if(common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS
					|| common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE){
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_YES);
			}   */
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RETURNFORWARD, ConstantValues.YESORNO_YES);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_YES);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_YES);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_MESSAGEEDIT, ConstantValues.YESORNO_YES);
			
			//已入库、部分入库的作废按钮不可用
			if(common.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED ||
					common.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.PARTINPUT){
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
			}
			
			//确认必须已锁定
			if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM){
				//未确定 -> 确定可用，不确定不可用
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_CONFIRMRETURN, ConstantValues.YESORNO_YES);
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_NO);
			}else if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE){
				buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
//			  buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_NO);
			}else{
				
				//确认-已入库
				if(common.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_CONFIRMRETURN, ConstantValues.YESORNO_NO);
					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_NO);
//					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_STORAGECANCLE, ConstantValues.YESORNO_YES);
					
				}else{
					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_CONFIRMRETURN, ConstantValues.YESORNO_NO);
					buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_YES);
				}
				
				if(common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY
						|| common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY){
					//退款单、额外退款单 - 操作
					//结算必须已确认
					if(common.getPayStatus().intValue() != 1){
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_YES);
					}else{
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_NO);
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
					}
				}else{
					//退货单 、拒收入库单 - 操作
					//收货必须已确认
					if(common.getIsGoodReceived().intValue() == 0){
						//未收货 -> 收货可用，未收货不可用
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RECEIVERETURN, ConstantValues.YESORNO_YES);
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNRECEIVERETURN, ConstantValues.YESORNO_NO);
					}else{
						
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RECEIVERETURN, ConstantValues.YESORNO_NO);
						//未收货-入库
						if(common.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNRECEIVERETURN, ConstantValues.YESORNO_YES);						 
						}
						
						//未确认 不可用
						buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_NO);
						
						//质检必须已收货
						if(common.getReturnOrderIspass().intValue() == 0){
							//未质检通过 -> 质检通过可用，未质检通过不可用
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_PASSRETURN, ConstantValues.YESORNO_YES);
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNPASSRETURN, ConstantValues.YESORNO_NO);
						}else{
							//质检通过未收货不能点击
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNRECEIVERETURN, ConstantValues.YESORNO_NO);
							
							buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_PASSRETURN, ConstantValues.YESORNO_NO);							   
							//质检不通过-入库
							if(common.getCheckinStatus().intValue() != ConstantValues.ORDER_RETURN_CHECKINSTATUS.INPUTED){
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNPASSRETURN, ConstantValues.YESORNO_YES);
							}
							
							//入库必须质检通过
							if(common.getCheckinStatus().intValue() == ConstantValues.ORDER_RETURN_CHECKINSTATUS.WAITINPUT){
								//待入库 -> 入库可用，结算不可用
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_STORAGERETURN, ConstantValues.YESORNO_YES);
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_VIRTUALSTORAGERETURN, ConstantValues.YESORNO_YES);
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_NO);
							}else{
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_STORAGERETURN, ConstantValues.YESORNO_NO);
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_YES);
								
								if(common.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE){
									buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNPASSRETURN,ConstantValues.YESORNO_NO);
								}
								//入库后不可作废
								buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
								
								//入库后不可回退客服
//							  buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_NO);
								
								//待结算 -> 结算可用
								if(common.getPayStatus().intValue() != 1 && common.getCheckinStatus().intValue() != ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE){
									buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_YES);
								}else{
									buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_NO);
//								  buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_NO);
								}
							}
						}
					}
				}
			}
		}
		if (common.getToErp() != null && common.getToErp().intValue() == 2) {
			banButtons(buttonStatusMap);
		}
		//删除商品产生的退款单在进入退单详情的时候不允许操作按钮
		if(StringUtil.isNotBlank(common.getReturnSn()) && common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_PAY && common.getRefundType().intValue() == ConstantValues.ORDERRETURN_REFUND_TYPE.DELETE_GOODS){
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_LOCKRETURN, ConstantValues.YESORNO_NO);
			buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNLOCKRETURN, ConstantValues.YESORNO_NO);
			banButtons(buttonStatusMap);
		}
		
		//当退单无效时，禁止操作其他按钮
		if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY){
			banButtons(buttonStatusMap);
		}
		
		return buttonStatusMap;
	}

	/**
	 * 设置面板按钮
	 * @param buttonStatusMap 按钮状态
	 */
	private void banButtons(Map<String, Integer> buttonStatusMap) {
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SAVERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_CONFIRMRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNCONFIRMRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RECEIVERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNRECEIVERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_PASSRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_UNPASSRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_STORAGERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_SETTLERETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_INVALIDRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_COMMUNICATERETURN, ConstantValues.YESORNO_YES);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_BACKTOCSRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_EDITRETURN, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_RETURNPAYEDIT, ConstantValues.YESORNO_NO);
		buttonStatusMap.put(Constants.RETURN_ORDER_CLICK_TYPE_MESSAGEEDIT, ConstantValues.YESORNO_NO);
	}

    /**
     * 创建退单校验退单商品
     * @param param
     * @return
     */
    private ReturnInfo<Boolean> verifyReturnOrderInfo(CreateOrderReturnBean param) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        returnInfo.setMessage("校验退单商品失败");

        //订单号
        String relatingOrderSn = param.getRelatingOrderSn();
        if (StringUtils.isBlank(relatingOrderSn)) {
            returnInfo.setMessage("订单号为空");
            return returnInfo;
        }

        returnInfo.setIsOk(1);
        //获取已退商品信息
        Map<String, ReturnGoodsVO> goodsVOMap = getReturnGoodsNum(relatingOrderSn, param.getReturnType());
        //无已退商品不校验
        if (goodsVOMap == null || goodsVOMap.size() < 1) {
            return returnInfo;
        }

        //比对已退商品信息和待退商品信息，无效商品自动过滤
        List<CreateOrderReturnGoods> orderReturnGoodsList = checkGoodsInfo(goodsVOMap, param.getCreateOrderReturnGoodsList());
        if (StringUtil.isListNull(orderReturnGoodsList)) {
            returnInfo.setIsOk(0);
            returnInfo.setMessage("无有效退单商品信息");
            return returnInfo;
        }

        //更新退单信息
        updateCreateOrderReturnInfo(param, orderReturnGoodsList);
        return returnInfo;
    }

    /**
     * 多退单创建更新退单商品信息，同时更新退单价格
     * @param param
     * @param orderReturnGoodsList
     */
    private void updateCreateOrderReturnInfo(CreateOrderReturnBean param, List<CreateOrderReturnGoods> orderReturnGoodsList) {
        //更新退单商品
        param.setCreateOrderReturnGoodsList(orderReturnGoodsList);

        //更新退单价格
        //退单商品售价总价
        BigDecimal totalGoodsPrice = new BigDecimal(0);
        //退单商品结算总价
        BigDecimal totalSettlementPrice = new BigDecimal(0);
        //退单总分摊红包价格
        BigDecimal totalShareBonus = new BigDecimal(0);

        for (CreateOrderReturnGoods goods : orderReturnGoodsList) {
            //退商品数量
            Integer goodsReturnNumber = goods.getGoodsReturnNumber();

            //商品售价
            Double goodsPrice = goods.getGoodsPrice();
            if (goodsPrice != null) {
                totalGoodsPrice = totalGoodsPrice.add(MathOperation.mulByBigDecimal(goodsPrice, goodsReturnNumber));
            }

            //商品财务价
            Double settlementPrice = goods.getSettlementPrice();
            if (settlementPrice != null) {
                totalSettlementPrice = totalSettlementPrice.add(MathOperation.mulByBigDecimal(settlementPrice, goodsReturnNumber));
            }


            //商品分摊红包金额
            Double shareBonus = goods.getShareBonus();
            if (shareBonus != null) {
                totalShareBonus = MathOperation.addByBigDecimal(totalShareBonus.doubleValue(), shareBonus);
            }

        }

        //更新退单信息
        CreateOrderReturn createOrderReturn = param.getCreateOrderReturn();
        if (createOrderReturn != null) {
            //更新商品总价
            createOrderReturn.setReturnGoodsMoney(totalGoodsPrice.doubleValue());
            //更新总结算价
            createOrderReturn.setReturnTotalFee(totalSettlementPrice.doubleValue());
            //更新总分摊红包金额
            createOrderReturn.setReturnBonusMoney(totalShareBonus.doubleValue());
        }

        //更新退款信息
        List<CreateOrderRefund> createOrderRefundList = param.getCreateOrderRefundList();
        if (StringUtil.isListNotNull(createOrderRefundList)) {
            CreateOrderRefund refund = createOrderRefundList.get(0);
            refund.setReturnFee(totalSettlementPrice.doubleValue());
        }
    }

    /**
     * 创建退单 比对已退商品信息和待退商品信息，无效商品自动过滤
     * @param goodsVOMap 已退商品信息
     * @param returnGoodsList 创建退单商品信息
     */
    private List<CreateOrderReturnGoods> checkGoodsInfo(Map<String, ReturnGoodsVO> goodsVOMap, List<CreateOrderReturnGoods> returnGoodsList) {
        if (goodsVOMap == null || goodsVOMap.size() < 1) {
            return null;
        }

        if (StringUtil.isListNull(returnGoodsList)) {
            return null;
        }
        //创建退单商品分组
        Map<String, List<CreateOrderReturnGoods>> createReturnGoodsMap = catCreateOrderReturnGoods(returnGoodsList);
        if (createReturnGoodsMap == null || createReturnGoodsMap.size() < 1) {
            return null;
        }

        //实际创建退单商品信息
        List<CreateOrderReturnGoods> createReturnGoodsList = new ArrayList<CreateOrderReturnGoods>();

        //比对创建退单商品和已退商品信息
        Set<String> keySet = goodsVOMap.keySet();
        for (String key : keySet) {
            //已退信息
            ReturnGoodsVO vo = goodsVOMap.get(key);
            if (vo == null) {
                continue;
            }

            //待退信息
            List<CreateOrderReturnGoods> orderReturnGoodsList = createReturnGoodsMap.get(key);
            if (StringUtil.isListNull(orderReturnGoodsList)) {
                continue;
            }

            //创建退数量
            int createReturnNum = 0;
            for (CreateOrderReturnGoods goods : orderReturnGoodsList) {
                Integer goodsReturnNumber = goods.getGoodsReturnNumber();
                if (goodsReturnNumber == null) {
                    goodsReturnNumber = 0;
                }
                createReturnNum += goodsReturnNumber;
            }

            if (createReturnNum < 1) {
                continue;
            }

            //该订单下商品可退数量,可退数量为0，过滤该商品
            Integer canReturnCount = vo.getCanReturnCount();
            if (canReturnCount == 0) {
                continue;
            }

            //创建退数量小于等于商品可退数量，直接退
            //创建退数量大于商品可退数量，只退可退数量
            if (createReturnNum > canReturnCount) {
                createReturnNum = canReturnCount;
            }

            //同sku,同扩展属性商品一条记录多数量计算
            CreateOrderReturnGoods createOrderReturnGoods = orderReturnGoodsList.get(0);
            createOrderReturnGoods.setGoodsReturnNumber(createReturnNum);
            createReturnGoodsList.add(createOrderReturnGoods);
        }

        return createReturnGoodsList;
    }

    /**
     * 获取订单关联已退单商品信息
     * @param orderSn
     * @return
     */
    private Map<String, ReturnGoodsVO> getReturnGoodsNum(String orderSn, Integer returnType) {
        //根据订单号查询关联退单
        OrderReturnExample returnExample = new OrderReturnExample();
        List<Byte> statusList = new ArrayList<>(2);
        statusList.add(ConstantValues.ORDERRETURN_STATUS.CONFIRMED);
        statusList.add(ConstantValues.ORDERRETURN_STATUS.COMPLETE);
        returnExample.or().andRelatingOrderSnEqualTo(orderSn).andReturnOrderStatusIn(statusList).andReturnTypeEqualTo(returnType.byteValue());
        List<OrderReturn> orderReturns = orderReturnMapper.selectByExample(returnExample);

        //无关联退单不校验，直接返回
        if (StringUtil.isListNull(orderReturns)) {
            return null;
        }

        List<String> snList = new ArrayList<String>();
        for (OrderReturn orderReturn : orderReturns) {
            snList.add(orderReturn.getReturnSn());
        }

        //获取退单商品
        OrderReturnGoodsExample goodsExample = new OrderReturnGoodsExample();
        goodsExample.or().andRelatingReturnSnIn(snList);
        List<OrderReturnGoods> returnGoodsList = orderReturnGoodsMapper.selectByExample(goodsExample);
        //商品分组
        Map<String, List<OrderReturnGoods>> goodsMap = catOrderReturnGoods(returnGoodsList);
        //无已退商品，不校验
        if (goodsMap == null || goodsMap.size() < 1) {
            return null;
        }

        //获取订单商品分组
        MasterOrderGoodsExample orderGoodsExample = new MasterOrderGoodsExample();
        orderGoodsExample.or().andMasterOrderSnEqualTo(orderSn);
        List<MasterOrderGoods> masterOrderGoodsList = masterOrderGoodsMapper.selectByExample(orderGoodsExample);
        if (StringUtil.isListNull(masterOrderGoodsList)) {
            return null;
        }
        Map<String, List<MasterOrderGoods>> masterOrderGoodsMap = catMasterOrderGoods(masterOrderGoodsList);
        if (masterOrderGoodsMap == null || masterOrderGoodsMap.size() < 1) {
            return null;
        }

        //获取已退商品信息
        Map<String, ReturnGoodsVO> goodsVoMap = new HashMap<String, ReturnGoodsVO>();
        for (Map.Entry<String, List<MasterOrderGoods>> entry : masterOrderGoodsMap.entrySet()) {
            List<MasterOrderGoods> goodsList = entry.getValue();
            if (StringUtil.isListNull(goodsList)) {
                continue;
            }
            ReturnGoodsVO vo = new ReturnGoodsVO();
            MasterOrderGoods masterOrderGoods = goodsList.get(0);
            CommonUtils.copyPropertiesNew(masterOrderGoods, vo);

            //订单购买量
            int goodsBuyNum = 0;
            for (MasterOrderGoods orderGoods : goodsList) {
                goodsBuyNum += orderGoods.getGoodsNumber();
            }
            vo.setGoodsBuyNumber(goodsBuyNum);

            //已退退单商品数量
            List<OrderReturnGoods> orderReturnGoodsList = goodsMap.get(entry.getKey());
            if (StringUtil.isListNull(orderReturnGoodsList)) {
                vo.setCanReturnCount(goodsBuyNum);
                vo.setGoodsReturnNumber(0);
                goodsVoMap.put(entry.getKey(), vo);
                continue;
            }
            int totalReturnNum = 0;
            for (OrderReturnGoods goods : orderReturnGoodsList) {
                totalReturnNum += goods.getGoodsReturnNumber();
            }

            //可退量
            int ableReturnGoodsNum = goodsBuyNum - totalReturnNum;
            if (ableReturnGoodsNum < 0) {
                ableReturnGoodsNum = 0;
            }

            //已退货量
            vo.setHavedReturnCount(totalReturnNum);
            //可退货量
            vo.setCanReturnCount(ableReturnGoodsNum);
            //退货量
            vo.setGoodsReturnNumber(totalReturnNum);

            goodsVoMap.put(entry.getKey(), vo);
        }

        return goodsVoMap;
    }

    /**
     * 退单商品按照sku、商品扩展属性分组
     * @param returnGoodsList
     * @return
     */
    private Map<String, List<OrderReturnGoods>> catOrderReturnGoods(List<OrderReturnGoods> returnGoodsList) {
        if (StringUtil.isListNull(returnGoodsList)) {
            return null;
        }

        Map<String, List<OrderReturnGoods>> goodsMap = new HashMap<String, List<OrderReturnGoods>>();
        for (OrderReturnGoods goods : returnGoodsList) {
            String key = goods.getCustomCode() + "-" + goods.getExtensionCode();
            List<OrderReturnGoods> goodsList = goodsMap.get(key);
            if (goodsList == null) {
                goodsList = new ArrayList<OrderReturnGoods>();
            }
            goodsList.add(goods);
            goodsMap.put(key, goodsList);
        }
        return goodsMap;
    }

    /**
     * 创建退单商品按照sku、商品扩展属性分组
     * @param returnGoodsList
     * @return
     */
    private Map<String, List<CreateOrderReturnGoods>> catCreateOrderReturnGoods(List<CreateOrderReturnGoods> returnGoodsList) {
        if (StringUtil.isListNull(returnGoodsList)) {
            return null;
        }

        Map<String, List<CreateOrderReturnGoods>> goodsMap = new HashMap<String, List<CreateOrderReturnGoods>>();
        for (CreateOrderReturnGoods goods : returnGoodsList) {
            String key = goods.getCustomCode() + "-" + goods.getExtensionCode();
            List<CreateOrderReturnGoods> goodsList = goodsMap.get(key);
            if (goodsList == null) {
                goodsList = new ArrayList<CreateOrderReturnGoods>();
            }
            goodsList.add(goods);
            goodsMap.put(key, goodsList);
        }
        return goodsMap;
    }

    /**
     * 创建订单商品按照sku、商品扩展属性分组
     * @param orderGoodsList
     * @return
     */
    private Map<String, List<MasterOrderGoods>> catMasterOrderGoods(List<MasterOrderGoods> orderGoodsList) {
        if (StringUtil.isListNull(orderGoodsList)) {
            return null;
        }

        Map<String, List<MasterOrderGoods>> goodsMap = new HashMap<String, List<MasterOrderGoods>>();
        for (MasterOrderGoods goods : orderGoodsList) {
            String key = goods.getCustomCode() + "-" + goods.getExtensionCode();
            List<MasterOrderGoods> goodsList = goodsMap.get(key);
            if (goodsList == null) {
                goodsList = new ArrayList<MasterOrderGoods>();
            }
            goodsList.add(goods);
            goodsMap.put(key, goodsList);
        }
        return goodsMap;
    }
}
