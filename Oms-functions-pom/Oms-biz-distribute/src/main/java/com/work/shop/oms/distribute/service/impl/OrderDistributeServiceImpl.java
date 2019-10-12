package com.work.shop.oms.distribute.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.config.service.SystemPaymentService;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.distribute.service.OrderDistributeService;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.order.service.PurchaseOrderService;
import com.work.shop.oms.orderReturn.service.OrderSettleService;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.stock.service.UniteStockService;
import com.work.shop.oms.utils.*;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.GoodsDistribute;
import com.work.shop.oms.vo.SettleParamObj;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 订单交货单服务
 * @author QuYachu
 */
@Service("orderDistributeService")
public class OrderDistributeServiceImpl implements OrderDistributeService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource
	private OrderDistributeMapper orderDistributeMapper;

	@Resource
	private MasterOrderActionMapper masterOrderActionMapper;
	@Resource
	private DistributeActionService distributeActionService;
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;

	@Resource(name = "redisClient")
	private RedisClient redisClient;

	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;

	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

    @Resource
    private MasterOrderGoodsDetailMapper masterOrderGoodsDetailMapper;
    @Resource(name = "uniteStockServiceImpl")
    private UniteStockService uniteStockService;
    @Resource
    private PurchaseOrderService purchaseOrderService;
	@Resource
	private SystemPaymentService systemPaymentService;
	@Resource(name="orderQuestionService")
	private OrderQuestionService orderQuestionService;

	@Resource
	private MasterOrderInfoService masterOrderInfoService;

	@Resource
	private OrderSettleService orderSettleService;

	/**
	 * 判断订单拆交货单信息
	 * @param masterOrderSn 订单号
	 * @param master 订单信息
	 * @return String
	 */
	private String checkOrderDistributeInfo(String masterOrderSn, MasterOrderInfo master) {
    	String msg = null;
		if (master == null) {
			msg = "订单[" + masterOrderSn + "]不存在";
			return msg;
		}
		if (Constant.SPLIT_STATUS_SPLITTING.equals(master.getSplitStatus())) {
			msg = "订单[" + masterOrderSn + "]处于拆单中";
			return msg;
		}
		if (Constant.SPLIT_STATUS_SPLITED.equals(master.getSplitStatus())) {
			msg = "订单[" + masterOrderSn + "]已经拆过单";
			return msg;
		}
		if (master.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			msg = "订单[" + masterOrderSn + "]未确认";
			return msg;
		}
		if (!OrderAttributeUtil.isUpdateModifyFlag((int) master.getTransType(), (int) master.getPayStatus())) {
			msg = "订单[" + masterOrderSn + "]要处于已付款状态！否则不能进行确认操作！";
			return msg;
		}

		return msg;
	}

    /**
     * 判断订单拆分状态
     * @param masterOrderSn
     * @return String
     */
	private String checkOrderSplit(String masterOrderSn) {

	    String msg = null;
        long result = -1;
        String orderOutSnKey = "order_split_" + masterOrderSn;
        try {
            result = redisClient.setnx(orderOutSnKey, masterOrderSn);
            // redis 超时时间有5秒钟改成1分钟 （60S）
            redisClient.expire(orderOutSnKey, 10);
        } catch (Throwable e) {
            logger.error("订单[" + masterOrderSn + "]判断订单是否拆单中,读取redis异常:" + e);
        }
        if (result == 0) {
            msg = "订单[" + masterOrderSn + "]已经拆单或者正在拆单中！";
            return msg;
        }

        return msg;
    }

    /**
     * 获取订单分配商品列表
     * @param master
     */
	private List<MasterOrderGoods> getOrderDistributeGoods(MasterOrderInfo master) {

		String masterOrderSn = master.getMasterOrderSn();

		List<MasterOrderGoods> orderGoodsList = null;
		// 新拆单
		if (Constant.SPLIT_STATUS_UNSPLITED.equals(master.getSplitStatus())) {
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
			criteria.andMasterOrderSnEqualTo(masterOrderSn).andIsDelEqualTo(0);
			orderGoodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
		} else if (Constant.SPLIT_STATUS_RESPLITED.equals(master.getSplitStatus())) {
			// 重新拆单
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
			criteria.andMasterOrderSnEqualTo(masterOrderSn);
			criteria.andOrderSnNotEqualTo("").andIsDelEqualTo(0);
			orderGoodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
		}

		return orderGoodsList;
	}

    /**
     * 联采订单自动结算完成
     * @param masterOrderSn
     */
	private void processOrderFinishByGroup(String masterOrderSn) {
        SettleParamObj paramObj = new SettleParamObj();
        paramObj.setBussType(1);
        paramObj.setDealCode(masterOrderSn);
        paramObj.setTools(false);
        paramObj.setUserId("系统结算");
        ReturnInfo<String> info = orderSettleService.masterOrderSettle(paramObj);
        logger.info("联采订单自动结算完成:" + JSONObject.toJSONString(paramObj));
    }

    /**
     * 处理订单分配后续处理
     * @param master 订单信息
     * @param orderSns 订单交货单列表
     * @param depotResult
     */
	private void processOrderDistributeFollow(MasterOrderInfo master, List<String> orderSns, OrderDepotResult depotResult) {

		String masterOrderSn = master.getMasterOrderSn();

		MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		Short orderType = masterOrderInfoExtend.getOrderType();

		if (orderType == null || orderType == 1) {
			// 如果是联采订单
            processOrderFinishByGroup(masterOrderSn);
			return;
		}

        // 需要审核订单设置待审核问题单
        if (master.getNeedAudit() == 1 && master.getAuditStatus() == 0) {
            orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "待审核问题单", Constant.QUESTION_CODE_REVIEW));
        } else {
            // 不需要审核正常订单 创建采购单
            if (master.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {

                //订单推送供应链
                logger.info("拆单完成后:" + masterOrderSn + "订单推送供应链");
                purchaseOrderService.pushJointPurchasing(masterOrderSn, master.getUserId(), "000000", null, 0);

                // 需要合同签章的，先设置问题单
                if (master.getNeedSign() == 1 && master.getSignStatus() == 0) {
                    orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "待签章问题单", Constant.QUESTION_CODE_SIGN));
                } else {
                    // 不需要审批的订单，下发账期支付扣款
                    // 是否账期支付, 0期立即扣款
                    //masterOrderInfoService.processOrderPayPeriod(masterOrderSn);
                    for (String orderSn : orderSns) {
                        OrderManagementRequest request = new OrderManagementRequest();
                        request.setShipSn(orderSn);
                        purchaseOrderService.purchaseOrderCreate(request);
                        distributeActionService.addOrderAction(orderSn, depotResult.getMessage());
                    }
                }
            }
        }

        if (orderType == null || orderType == 0) {
            // 通知统一库存分配库存
            uniteStockService.distOccupy(masterOrderSn);
        }
    }

    /**
     * 设置订单拆单状态
     * @param masterOrderSn
     */
    private void setMasterOrderSplitStatus(String masterOrderSn){
	    Date date = new Date();
        MasterOrderInfo info = new MasterOrderInfo();
        info.setSplitStatus(Constant.SPLIT_STATUS_SPLITED);
        info.setSplitTime(date);
        info.setUpdateTime(date);
        info.setMasterOrderSn(masterOrderSn);
        masterOrderInfoMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 创建订单交货单
     * @param master 订单信息
     * @param orderPays 支付单列表
     * @param supplierMap 供应商交货关系
     * @return ReturnInfo<List<String>>
     */
    private ReturnInfo<List<String>> createOrderDistributeInfo(MasterOrderInfo master, List<MasterOrderPay> orderPays, Map<String, GoodsDistribute> supplierMap) {

        ReturnInfo<List<String>> returnInfo = new ReturnInfo<List<String>>(Constant.OS_YES);
        List<String> orderSns = new ArrayList<String>();
        String masterOrderSn = master.getMasterOrderSn();
        // 创建交货单
        Integer index = 0;
        for (String supplierKey : supplierMap.keySet()) {
            GoodsDistribute goodsDistribute = supplierMap.get(supplierKey);
            final String orderSn = createOrderSn(masterOrderSn, index);
            logger.info("订单[" + masterOrderSn + "]创建子订单号:" + orderSn);
            if (StringUtil.isEmpty(orderSn)) {
                logger.error("订单[" + masterOrderSn + "]创建子订单号异常");
            }
            try {
                List<String> depotCodes = goodsDistribute.getDepotCodes();
                createOrderDistribute(master, orderSn, goodsDistribute, index, orderPays);
                for (String depot : depotCodes) {
                    OrderDepotShip depotShip = new OrderDepotShip();
                    depotShip.setOrderSn(orderSn);
                    // 发货仓
                    depotShip.setDepotCode(depot);
                    // 分配类型
                    depotShip.setDeliveryType(2);
                    // 发货状态 0 未发货
                    depotShip.setShippingStatus((byte)Constant.OS_SHIPPING_STATUS_UNSHIPPED);
                    depotShip.setCreatTime(new Date());
                    // 是否被删除0否1是
                    depotShip.setIsDel(0);
                    orderDepotShipMapper.insertSelective(depotShip);
                }
            } catch (Exception e) {
                logger.error("订单[" + masterOrderSn + "]创建分配单异常" + e.getMessage() , e);
                saveLogInfo("订单[" + masterOrderSn + "]创建分配单异常" + e.getMessage(), master);
                returnInfo.setIsOk(Constant.OS_NO);
                returnInfo.setMessage("订单[\" + masterOrderSn + \"]创建分配单异常");
                return returnInfo;
            }
            orderSns.add(orderSn);
        }

        returnInfo.setData(orderSns);
        return returnInfo;
    }

	/**
	 * 根据订单拆配送单
	 * @param masterOrderSn
	 * @return OrderDepotResult
	 */
	@Override
	public OrderDepotResult orderDistribute(String masterOrderSn) {
		logger.info("主订单[" + masterOrderSn + "]开始拆单" );
		OrderDepotResult depotResult = new OrderDepotResult(-1, "初始默认错误");
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		String msg = checkOrderDistributeInfo(masterOrderSn, master);
		if (StringUtils.isNotBlank(msg)) {
			depotResult.setMessage(msg);
			return depotResult;
		}

		MasterOrderPayExample orderPayExample = new MasterOrderPayExample();
		orderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		orderPayExample.setOrderByClause(" pay_time desc");
		List<MasterOrderPay> orderPays = masterOrderPayMapper.selectByExample(orderPayExample);
		if (StringUtil.isListNull(orderPays)) {
			logger.error("订单[" + masterOrderSn + "]支付单不存在");
			depotResult.setMessage("订单[" + masterOrderSn + "]支付单不存在");
			return depotResult;
		}
		// 线下支付问题单可以拆单
		/*SystemPayment payment = systemPaymentService.getSystemPaymentByPayId(orderPays.get(0).getPayId());
		if (payment != null && !Constant.OP_PAY_WAY_PAYCODE_XIANXIA.equals(payment.getPayCode())) {
			if (master.getQuestionStatus() == Constant.OI_QUESTION_STATUS_QUESTION) {
				depotResult.setMessage("订单[" + masterOrderSn + "]是问题单");
				return depotResult;
			}
		}*/

		// 订单交货单拆单状态判断
		msg = checkOrderSplit(masterOrderSn);
		if (StringUtils.isNotBlank(msg)) {
            depotResult.setMessage(msg);
            return depotResult;
        }

		// 获取交货单商品列表
		List<MasterOrderGoods> orderGoodsList = getOrderDistributeGoods(master);

		List<String> orderSns = new ArrayList<String>();
		try {
			Map<String, GoodsDistribute> supplierMap = buildGoodsDistribute(orderGoodsList, master);
			// 货到付款订单需检查拆单后数据
			if (master.getTransType().intValue() == Constant.OI_TRANS_TYPE_PRESHIP) {
				if (!supplierMap.isEmpty() && supplierMap.size() > 1) {
					// 拆单量大于1 设置问题单
					depotResult.setMessage("订单[" + masterOrderSn + "]货到付款拆单数大于1 ");
					return depotResult;
				}
			}

            ReturnInfo<List<String>> orderReturnInfo = createOrderDistributeInfo(master, orderPays, supplierMap);
			if (orderReturnInfo.getIsOk() == Constant.OS_NO) {
                depotResult.setMessage(orderReturnInfo.getMessage());
                return depotResult;
            }

            orderSns = orderReturnInfo.getData();
			// 设置订单已拆单
            setMasterOrderSplitStatus(masterOrderSn);
		} catch (Exception e) {
			logger.error("订单[" + masterOrderSn + "]创建分配单异常", e);
			depotResult.setMessage("订单[" + masterOrderSn + "]创建分配单异常" + e.getMessage());
			return depotResult;
		}

        processOrderDistributeFollow(master, orderSns, depotResult);
		return depotResult;
	}

	@Override
	public OrderDepotResult pushXoms(String masterOrderSn) {
		OrderDepotResult depotResult = new OrderDepotResult(-1, "初始默认错误");
		logger.info("订单[" + masterOrderSn + "]重新推送XOMS系统" );
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("参数[masterOrderSn]不能为空");
			depotResult.setMessage("参数[masterOrderSn]不能为空");
			return depotResult;
		}
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (master == null) {
			logger.error("订单[" + masterOrderSn + "]不存在");
			depotResult.setMessage("订单[" + masterOrderSn + "]不存在");
			return depotResult;
		}
		if (Constant.SPLIT_STATUS_SPLITED != master.getSplitStatus()) {
			depotResult.setMessage("订单[" + masterOrderSn + "]未拆过单");
			return depotResult;
		}
		if (master.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			depotResult.setMessage("订单[" + masterOrderSn + "]未确认");
			return depotResult;
		}
		if (!OrderAttributeUtil.isUpdateModifyFlag((int) master.getTransType(), (int) master.getPayStatus())) {
			depotResult.setMessage("订单[" + masterOrderSn + "]要处于已付款状态！否则不能进行确认操作！");
			return depotResult;
		}
		List<OrderDistribute> distributes = selectEffectiveDistributes(masterOrderSn);
		if (StringUtil.isListNull(distributes)) {
			depotResult.setMessage("订单[" + masterOrderSn + "] 下交货单为空！");
			return depotResult;
		}
		String shipSn = distributes.get(0).getOrderSn();
		double moneyPaid = master.getMoneyPaid().doubleValue() + master.getSurplus().doubleValue() + master.getPoints().doubleValue();
		
		MasterOrderPayExample orderPayExample = new MasterOrderPayExample();
		orderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		orderPayExample.setOrderByClause(" pay_time desc");
		List<MasterOrderPay> orderPays = this.masterOrderPayMapper.selectByExample(orderPayExample);
		if (StringUtil.isListNull(orderPays)) {
			logger.error("订单[" + masterOrderSn + "]支付单不存在");
			depotResult.setMessage("订单[" + masterOrderSn + "]支付单不存在");
			return depotResult;
		}
		MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
		MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
		criteria.andMasterOrderSnEqualTo(masterOrderSn).andOrderSnEqualTo(shipSn).andIsDelEqualTo(0);
		List<MasterOrderGoods> goodsList = this.masterOrderGoodsMapper.selectByExample(goodsExample);
		if (StringUtil.isListNull(orderPays)) {
			logger.error("订单[" + masterOrderSn + "]支付单不存在");
			depotResult.setMessage("订单[" + masterOrderSn + "]支付单不存在");
			return depotResult;
		}
		String depotCode = goodsList.get(0).getDepotCode();
		try {
			depotResult = occupyStock(master, masterOrderSn, shipSn, moneyPaid, orderPays, depotCode);
		} catch (Exception e) {
			logger.error("[" + masterOrderSn + "]下发XOMS系统异常" + e.getMessage(), e);
			depotResult.setMessage("[" + masterOrderSn + "]下发XOMS系统异常" + e.getMessage());
		}
		return depotResult;
	}
	
	private OrderDepotResult occupyStock(MasterOrderInfo master, String masterOrderSn,
			String shipSn, double moneyPaid, List<MasterOrderPay> orderPays, String depotCode) {
		OrderDepotResult depotResult = new OrderDepotResult(-1, "初始默认错误");
		try {
			MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			MasterOrderAddressInfo address = this.masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
			List<SystemRegionArea> regions = getSystemRegion(address);
			DistributeOrder distributeOrder = new DistributeOrder();
			// 订单信息
//			String shipSn = orderSns.get(0);
			distributeOrder.setMasterOrderSn(masterOrderSn);
			distributeOrder.setShipSn(shipSn);
			distributeOrder.setOrderType(master.getOrderType());
			distributeOrder.setRelatingShipSn(master.getRelatingOriginalSn()); // 关联交货单
			distributeOrder.setRelatingReturnSn(master.getRelatingReturnSn()); // 关联退货单
			distributeOrder.setOrderStatus(master.getOrderStatus().intValue());
			distributeOrder.setDiscount(setScale(master.getDiscount()));
			distributeOrder.setGoodsAmount(setScale(master.getGoodsAmount()));
			distributeOrder.setGoodsCount(master.getGoodsCount());
			distributeOrder.setTotalFee(setScale(master.getTotalFee()));
			distributeOrder.setMoneyPaid(setScale(moneyPaid));
			distributeOrder.setBonus(setScale(master.getBonus()));
			distributeOrder.setIntegralMoney(setScale(master.getIntegralMoney()));
			distributeOrder.setAddTime(TimeUtil.formatDate(master.getAddTime()));
			distributeOrder.setBestTime(address.getBestTime());
			distributeOrder.setUserId(master.getUserId());
			distributeOrder.setRegisterMobile(master.getRegisterMobile());
			distributeOrder.setPrName(master.getPrName());
			distributeOrder.setPrIds(master.getPrIds());
			distributeOrder.setPayTime(TimeUtil.formatDate(orderPays.get(0).getPayTime()));
			// 收货人信息
			distributeOrder.setConsignee(address.getConsignee());
			distributeOrder.setMobile(address.getMobile());
			distributeOrder.setTel(address.getTel());
			distributeOrder.setCountry(findRegionsNameByid(regions, address.getCountry()));
			distributeOrder.setProvince(findRegionsNameByid(regions, address.getProvince()));
			distributeOrder.setCity(findRegionsNameByid(regions, address.getCity()));
			distributeOrder.setDistrict(findRegionsNameByid(regions, address.getDistrict()));
			distributeOrder.setAddress(address.getAddress());
			distributeOrder.setEmail(address.getEmail());
			distributeOrder.setInvContent(extend == null ? "" : extend.getInvContent());
			distributeOrder.setInvPayee(extend == null ? "" : extend.getInvPayee());
			distributeOrder.setInvType(extend == null ? "" : extend.getInvType());
			distributeOrder.setDepotCode(depotCode);
			distributeOrder.setBvValue(master.getBvValue());
			distributeOrder.setPoints(setScale(master.getPoints()));
			distributeOrder.setExpectedShipDate(master.getExpectedShipDate());
			distributeOrder.setIsAdvance(extend.getIsAdvance());
			distributeOrder.setComplexTax(setScale(master.getTax()));
			distributeOrder.setSourceType(master.getSource());
			distributeOrder.setUserCardName(address.getUserCardName());
			distributeOrder.setUserCardNo(address.getUserCardNo());
			distributeOrder.setBaseBvValue(master.getBaseBvValue());
			distributeOrder.setIsCac(extend.getIsCac());
			distributeOrder.setProvinceCode(address.getProvinceCode());
			distributeOrder.setCityCode(address.getCityCode());
			distributeOrder.setDistrictCode(address.getDistrictCode());
			distributeOrder.setAreaCode(address.getAreaCode());
			distributeOrder.setReferer(master.getReferer());
			distributeOrder.setShippingAddress(address.getShippingAddress());
			distributeOrder.setShippingTotalFee(master.getShippingTotalFee().doubleValue());
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
			criteria.andMasterOrderSnEqualTo(masterOrderSn).andOrderSnEqualTo(shipSn).andIsDelEqualTo(0);
			List<MasterOrderGoods> goodsList = this.masterOrderGoodsDetailMapper.getXOMSMasterOrderGoods(masterOrderSn);
			if (StringUtil.isListNotNull(goodsList)) {
//				goodsList = distributeSupplierService.groupByOrderGoods(goodsList);
				List<DistributeGoods> distributeGoodsList = new ArrayList<DistributeGoods>();
				for (MasterOrderGoods goods : goodsList) {
					DistributeGoods distributeGoods = new DistributeGoods();
					distributeGoods.setCustomCode(goods.getCustomCode());
					distributeGoods.setTransactionPrice(setScale(goods.getTransactionPrice()));
					distributeGoods.setGoodsNumber(goods.getGoodsNumber().intValue());
					distributeGoods.setGoodsPrice(setScale(goods.getGoodsPrice()));
					distributeGoods.setSettlementPrice(setScale(goods.getSettlementPrice()));
					distributeGoods.setPrName(goods.getPromotionDesc());
					distributeGoods.setPrIds(goods.getPromotionId());
					distributeGoods.setShareBonus(setScale(goods.getShareBonus()));
					distributeGoods.setGoodsThumb(goods.getGoodsThumb());
					distributeGoods.setGoodsName(goods.getGoodsName());
					distributeGoods.setIntegralMoney(setScale(goods.getIntegralMoney()));
					distributeGoods.setColorName(goods.getGoodsColorName());
					distributeGoods.setSizeName(goods.getGoodsSizeName());
					distributeGoods.setExtensionCode(goods.getExtensionCode());
					distributeGoods.setSap(goods.getSap());
					distributeGoods.setDiscount(setScale(goods.getDiscount()));
					distributeGoods.setBvValue(StringUtil.isTrimEmpty(goods.getBvValue()) ? 0 : Integer.valueOf(goods.getBvValue()));
					distributeGoods.setExpectedShipDate(goods.getExpectedShipDate());
					distributeGoods.setComplexTax(setScale(goods.getTax()));
					distributeGoods.setComplexTaxRate(setScale(goods.getTaxRate()));
					distributeGoods.setBaseBvValue(goods.getBaseBvValue());
					distributeGoods.setDepotCode(goods.getDepotCode());
					distributeGoodsList.add(distributeGoods);
				}
				distributeOrder.setDistributeGoods(distributeGoodsList);
			} else {
				logger.error("[" + masterOrderSn + "]下发XOMS系统，有效订单商品为空！");
				depotResult.setMessage("[" + masterOrderSn + "]下发XOMS系统，有效订单商品为空！");
			}
		} catch (Exception e) {
			logger.error("[" + masterOrderSn + "]下发XOMS系统异常" + e.getMessage(), e);
			depotResult.setMessage("[" + masterOrderSn + "]下发XOMS系统异常" + e.getMessage());
		}
		return depotResult;
	}
	
	/*private OrderDepotResult pushXomsCommon(MasterOrderInfo master, String masterOrderSn,
			String shipSn, double moneyPaid, List<MasterOrderPay> orderPays, String depotCode) {
		OrderDepotResult depotResult = new OrderDepotResult(-1, "初始默认错误");
		try {
			String queueName = "";
			if (master.getChannelCode().equals(Constant.NEWFORCE)) {
				queueName = "notice_picking";
			} else if (master.getChannelCode().equals(Constant.KELTI)) {
				queueName = "notice_picking_kelti";
			} else if (master.getChannelCode().equals(Constant.Chlitina)) {
				queueName = "notice_shopkeeper";
			} else if (master.getChannelCode().equals(Constant.CONSULTANT)) {
				queueName = "notice_consultant";
			}
			MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			MasterOrderAddressInfo address = this.masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
			List<SystemRegionArea> regions = getSystemRegion(address);
			DistributeOrder distributeOrder = new DistributeOrder();
			// 订单信息
//			String shipSn = orderSns.get(0);
			distributeOrder.setMasterOrderSn(masterOrderSn);
			distributeOrder.setShipSn(shipSn);
			distributeOrder.setOrderType(master.getOrderType());
			distributeOrder.setRelatingShipSn(master.getRelatingOriginalSn()); // 关联交货单
			distributeOrder.setRelatingReturnSn(master.getRelatingReturnSn()); // 关联退货单
			distributeOrder.setOrderStatus(master.getOrderStatus().intValue());
			distributeOrder.setDiscount(setScale(master.getDiscount()));
			distributeOrder.setGoodsAmount(setScale(master.getGoodsAmount()));
			distributeOrder.setGoodsCount(master.getGoodsCount());
			distributeOrder.setTotalFee(setScale(master.getTotalFee()));
			distributeOrder.setMoneyPaid(setScale(moneyPaid));
			distributeOrder.setBonus(setScale(master.getBonus()));
			distributeOrder.setIntegralMoney(setScale(master.getIntegralMoney()));
			distributeOrder.setAddTime(TimeUtil.formatDate(master.getAddTime()));
			distributeOrder.setBestTime(address.getBestTime());
			distributeOrder.setUserId(master.getUserId());
			distributeOrder.setRegisterMobile(master.getRegisterMobile());
			distributeOrder.setPrName(master.getPrName());
			distributeOrder.setPrIds(master.getPrIds());
			distributeOrder.setPayTime(TimeUtil.formatDate(orderPays.get(0).getPayTime()));
			// 收货人信息
			distributeOrder.setConsignee(address.getConsignee());
			distributeOrder.setMobile(address.getMobile());
			distributeOrder.setTel(address.getTel());
			distributeOrder.setCountry(findRegionsNameByid(regions, address.getCountry()));
			distributeOrder.setProvince(findRegionsNameByid(regions, address.getProvince()));
			distributeOrder.setCity(findRegionsNameByid(regions, address.getCity()));
			distributeOrder.setDistrict(findRegionsNameByid(regions, address.getDistrict()));
			distributeOrder.setAddress(address.getAddress());
			distributeOrder.setEmail(address.getEmail());
			distributeOrder.setInvContent(extend == null ? "" : extend.getInvContent());
			distributeOrder.setInvPayee(extend == null ? "" : extend.getInvPayee());
			distributeOrder.setInvType(extend == null ? "" : extend.getInvType());
			distributeOrder.setDepotCode(depotCode);
			distributeOrder.setBvValue(master.getBvValue());
			distributeOrder.setPoints(setScale(master.getPoints()));
			distributeOrder.setExpectedShipDate(master.getExpectedShipDate());
			distributeOrder.setIsAdvance(extend.getIsAdvance());
			distributeOrder.setComplexTax(setScale(master.getTax()));
			distributeOrder.setSourceType(master.getSource());
			distributeOrder.setUserCardName(address.getUserCardName());
			distributeOrder.setUserCardNo(address.getUserCardNo());
			distributeOrder.setBaseBvValue(master.getBaseBvValue());
			distributeOrder.setIsCac(extend.getIsCac());
			distributeOrder.setProvinceCode(address.getProvinceCode());
			distributeOrder.setCityCode(address.getCityCode());
			distributeOrder.setDistrictCode(address.getDistrictCode());
			distributeOrder.setAreaCode(address.getAreaCode());
			distributeOrder.setReferer(master.getReferer());
			distributeOrder.setShippingAddress(address.getShippingAddress());
			distributeOrder.setShippingTotalFee(master.getShippingTotalFee().doubleValue());
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
			criteria.andMasterOrderSnEqualTo(masterOrderSn).andOrderSnEqualTo(shipSn).andIsDelEqualTo(0);
			List<MasterOrderGoods> goodsList = this.masterOrderGoodsDetailMapper.getXOMSMasterOrderGoods(masterOrderSn);
			if (StringUtil.isListNotNull(goodsList)) {
//				goodsList = distributeSupplierService.groupByOrderGoods(goodsList);
				List<DistributeGoods> distributeGoodsList = new ArrayList<DistributeGoods>();
				for (MasterOrderGoods goods : goodsList) {
					DistributeGoods distributeGoods = new DistributeGoods();
					distributeGoods.setCustomCode(goods.getCustomCode());
					distributeGoods.setTransactionPrice(setScale(goods.getTransactionPrice()));
					distributeGoods.setGoodsNumber(goods.getGoodsNumber().intValue());
					distributeGoods.setGoodsPrice(setScale(goods.getGoodsPrice()));
					distributeGoods.setSettlementPrice(setScale(goods.getSettlementPrice()));
					distributeGoods.setPrName(goods.getPromotionDesc());
					distributeGoods.setPrIds(goods.getPromotionId());
					distributeGoods.setShareBonus(setScale(goods.getShareBonus()));
					distributeGoods.setGoodsThumb(goods.getGoodsThumb());
					distributeGoods.setGoodsName(goods.getGoodsName());
					distributeGoods.setIntegralMoney(setScale(goods.getIntegralMoney()));
					distributeGoods.setColorName(goods.getGoodsColorName());
					distributeGoods.setSizeName(goods.getGoodsSizeName());
					distributeGoods.setExtensionCode(goods.getExtensionCode());
					distributeGoods.setSap(goods.getSap());
					distributeGoods.setDiscount(setScale(goods.getDiscount()));
					distributeGoods.setBvValue(StringUtil.isTrimEmpty(goods.getBvValue()) ? 0 : Integer.valueOf(goods.getBvValue()));
					distributeGoods.setExpectedShipDate(goods.getExpectedShipDate());
					distributeGoods.setComplexTax(setScale(goods.getTax()));
					distributeGoods.setComplexTaxRate(setScale(goods.getTaxRate()));
					distributeGoods.setBaseBvValue(goods.getBaseBvValue());
					distributeGoods.setDepotCode(goods.getDepotCode());
					distributeGoodsList.add(distributeGoods);
				}
				List<DistributePay> distributePays = new ArrayList<DistributePay>();
				for (MasterOrderPay orderPay : orderPays) {
					if (orderPay.getPayStatus().intValue() != Constant.OP_PAY_STATUS_PAYED) {
						continue;
					}
					if (orderPay.getPayTotalfee().doubleValue() == 0) {
						continue;
					}
					SystemPayment payment = systemPaymentMapper.selectByPrimaryKey(orderPay.getPayId());
					DistributePay distributePay = new DistributePay();
					distributePay.setPaySn(orderPay.getMasterPaySn());
					distributePay.setPayCode(payment.getPayCode());
					distributePay.setPayName(payment.getPayName());
					distributePay.setPayNote(orderPay.getPayNote());
					distributePay.setPayTotalFee(setScale(orderPay.getPayTotalfee()));
					distributePay.setCreateTime(TimeUtil.formatDate(orderPay.getCreateTime()));
					distributePay.setPayTime(TimeUtil.formatDate(orderPay.getPayTime()));
					distributePays.add(distributePay);
				}
				distributeOrder.setDistributePays(distributePays);
				distributeOrder.setDistributeGoods(distributeGoodsList);
//				noticePickingJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(distributeOrder)));
				String data = JSON.toJSONString(distributeOrder);
				logger.info("queueName:" + queueName + ";下发XOMS:" + data);
				if (StringUtil.isNotEmpty(queueName)) {
					jmsSendQueueService.sendQueueMessage(queueName, data);
					MasterOrderInfo updateMaster = new MasterOrderInfo();
					updateMaster.setMasterOrderSn(masterOrderSn);
					updateMaster.setUpdateTime(new Date());
					updateMaster.setIsnow(1);
					masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
					depotResult.setMessage("[" + masterOrderSn + "]下发XOMS系统成功");
					depotResult.setResult(1);
				}
			} else {
				logger.error("[" + masterOrderSn + "]下发XOMS系统，有效订单商品为空！");
				depotResult.setMessage("[" + masterOrderSn + "]下发XOMS系统，有效订单商品为空！");
			}
		} catch (Exception e) {
			logger.error("[" + masterOrderSn + "]下发XOMS系统异常" + e.getMessage(), e);
			depotResult.setMessage("[" + masterOrderSn + "]下发XOMS系统异常" + e.getMessage());
		}
		return depotResult;
	}*/

	private Double setScale(BigDecimal obj) {
		if (obj == null) {
			return 0D;
		}
		return obj.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private Double setScale(Double obj) {
		if (obj == null) {
			return 0D;
		}
		return BigDecimal.valueOf(obj).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private Double setScale(Float obj) {
		if (obj == null) {
			return 0D;
		}
		return BigDecimal.valueOf(obj).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	@Override
	public List<OrderDistribute> selectEffectiveDistributes(String masterOrderSn) {
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			return null;
		}
		OrderDistributeExample example = new OrderDistributeExample();
		example.or().andMasterOrderSnEqualTo(masterOrderSn).andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
		List<OrderDistribute> distributes = this.orderDistributeMapper.selectByExample(example);
		return distributes;
	}

    /**
     * 根据交货单号获取交货单信息
     * @param orderSn
     * @return
     */
    @Override
    public OrderDistribute selectDistributesByOrderSn(String orderSn) {
        if (StringUtil.isTrimEmpty(orderSn)) {
            return null;
        }
        return orderDistributeMapper.selectByPrimaryKey(orderSn);
    }

	@Override
	public List<OrderDepotShip> selectEffectiveShips(String orderSn) {
		if (StringUtil.isTrimEmpty(orderSn)) {
			return null;
		}
		OrderDepotShipExample example = new OrderDepotShipExample();
		example.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
		List<OrderDepotShip> depotShips = this.orderDepotShipMapper.selectByExample(example);
		return depotShips;
	}

	/**
	 * 指定分配商品 发货单
	 * @param goodsTemps
	 * @param masterOrderSn
	 * @return
	 */
	/*private Map<String, GoodsDistribute> buildGoodsDistribute(List<MasterOrderGoods> goodsList, MasterOrderInfo masterOrderInfo) {
		Map<String, List<MasterOrderGoods>> orderGoodsMap = new HashMap<String, List<MasterOrderGoods>>();
		// 根据订单号生产发货单号
		for (MasterOrderGoods orderGoods : goodsList) {
			String supplierKey = orderGoods.getSupplierCode();
			if (masterOrderInfo.getSource().intValue() == Constant.OD_SOURCE_C2M) {
				supplierKey = Constant.SUPPLIER_TYPE_MB;
			} else if (orderGoods.getSalesMode().intValue() == 4) {
				supplierKey = orderGoods.getSupplierCode();
				if (Constant.SUPPLIER_TYPE_MB.equals(supplierKey)) {
					supplierKey = "THIRD-MB";
				}
			} else if (orderGoods.getSalesMode().intValue() == 1 
					&& Constant.ORDER_TYPE_C2B.equals(orderGoods.getExtensionCode())) {
				supplierKey = "THIRD-MB";
			} else {
				supplierKey = Constant.SUPPLIER_TYPE_MB;
			}
			List<MasterOrderGoods> list = orderGoodsMap.get(supplierKey);
			if (list == null) {
				list = new ArrayList<MasterOrderGoods>();
				list.add(orderGoods);
				orderGoodsMap.put(supplierKey, list);
			} else {
				list.add(orderGoods);
				orderGoodsMap.put(supplierKey, list);
			}
		}
		Map<String, GoodsDistribute> distributeMap = new HashMap<String, GoodsDistribute>();
		for (String supplierKey : orderGoodsMap.keySet()) {
			GoodsDistribute distribute = new GoodsDistribute();
			List<MasterOrderGoods> goods = orderGoodsMap.get(supplierKey);
			distribute.setOrderGoods(goods);
			distribute.setSupplierCode(supplierKey);
			distributeMap.put(supplierKey, distribute);
		}
		return distributeMap;
	}*/

    /**
     * 指定分配商品 发货单
     * @param goodsList 商品信息
     * @param masterOrderInfo 订单信息
     * @return Map<String, GoodsDistribute>
     */
	private Map<String, GoodsDistribute> buildGoodsDistribute(List<MasterOrderGoods> goodsList, MasterOrderInfo masterOrderInfo) {

		// 供应商与商品关系
		Map<String, List<MasterOrderGoods>> orderGoodsMap = new HashMap<String, List<MasterOrderGoods>>(Constant.DEFAULT_MAP_SIZE);
		// 仓库
		Map<String, Object> map = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
		for (MasterOrderGoods orderGoods : goodsList) {
			// 按照商品供应商拆分订单商品
			String supplierKey = orderGoods.getSupplierCode();
			map.put(orderGoods.getDepotCode(), orderGoods.getDepotCode());
			List<MasterOrderGoods> list = orderGoodsMap.get(supplierKey);
			if (list == null) {
				list = new ArrayList<MasterOrderGoods>();
				list.add(orderGoods);
				orderGoodsMap.put(supplierKey, list);
			} else {
				list.add(orderGoods);
				orderGoodsMap.put(supplierKey, list);
			}
		}

		// 供应商交货信息
        Map<String, GoodsDistribute> distributeMap = new HashMap<String, GoodsDistribute>(Constant.DEFAULT_MAP_SIZE);

        // 处理供应商信息
		for (String distKey : orderGoodsMap.keySet()) {
			GoodsDistribute distribute = new GoodsDistribute();
			List<MasterOrderGoods> items = orderGoodsMap.get(distKey);

			// 仓库列表
			List<String> list = new ArrayList<String>();
			for (MasterOrderGoods item : items) {
				if (!list.contains(item.getDepotCode())) {
					list.add(item.getDepotCode());
				}
			}
			distribute.setDepotCodes(list);
			distribute.setOrderGoods(items);
			distribute.setDepotCode(items.get(0).getDepotCode());
			distribute.setSupplierCode(distKey);
            distribute.setSupplierName(items.get(0).getSupplierName());
			distributeMap.put(distKey, distribute);
		}
		return distributeMap;
	}
	
	public static void main(String[] args) {
		OrderDistributeServiceImpl impl = new OrderDistributeServiceImpl();
		System.out.println(impl.planDistTime(new Date(), 5));
	}
	
	private String planDistTime(Date date, int week) {
		if (week == 7) { // 周日7转换为0
			week = 0;
		}
		String distTime = null;
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w == week) {
			distTime = TimeUtil.format2Date(date);
		} else if (w > week){
			cal.add(Calendar.DAY_OF_YEAR, week - w + 7);
			distTime = TimeUtil.format2Date(cal.getTime());
		} else {
			cal.add(Calendar.DAY_OF_YEAR, week - w);
			distTime = TimeUtil.format2Date(cal.getTime());
		}
		return distTime;
	}

    /**
     * 创建交货单（子订单）
     * @param master 订单信息
     * @param orderSn 交货单号
     * @param goodsDistribute 商品分配信息
     * @param index
     * @param orderPays 支付单
     * @return OrderDistribute
     */
	private OrderDistribute createOrderDistribute(MasterOrderInfo master, String orderSn, GoodsDistribute goodsDistribute,
			Integer index, List<MasterOrderPay> orderPays) {

		MasterOrderAddressInfo address = this.masterOrderAddressInfoMapper.selectByPrimaryKey(master.getMasterOrderSn());
		// 自提时创建自提码
		String gotCode = null;
		if (address != null && address.getShippingId().intValue() == 66) {
			gotCode = getGotCode(orderSn);
			logger.info("订单：" + orderSn + ";自提码：" + gotCode);
		}
		
		// 生产新订单
		OrderDistribute distribute = fillOrderDistributeInfo(orderSn, master);

		// 填充交货单价格信息
        fillDistributePriceInfo(goodsDistribute, distribute);

        // 填充交货单支付单信息
        fillDistributePayInfo(master, distribute, orderPays);

		distribute.setGotCode(gotCode);
		distribute.setGotStatus(Constant.GOT_STATUS_NO);

        distribute.setSupplierCode(goodsDistribute.getSupplierCode());
        distribute.setSupplierName(goodsDistribute.getSupplierName());

		// 货到付款运费传值
		if (master.getTransType() == Constant.OI_TRANS_TYPE_PRESHIP) {
			distribute.setShippingTotalFee(master.getShippingTotalFee());
			distribute.setTotalPayable(master.getTotalPayable());
		}

		distribute.setDistTime(goodsDistribute.getDistTime());
		orderDistributeMapper.insertSelective(distribute);

        saveDistributeAction(distribute);
		return distribute;
	}

    /**
     * 填充交货单支付单信息
     * @param master
     * @param distribute
     * @param orderPays
     */
	private void fillDistributePayInfo(MasterOrderInfo master, OrderDistribute distribute, List<MasterOrderPay> orderPays) {

	    // 支付id
        int pId = 0;
        for (MasterOrderPay orderPay : orderPays) {
            if (orderPay.getPayId() != 3) {
                pId = orderPay.getPayId();
            }
        }

        // 订单总金额
        double settlementPrice = distribute.getTotalFee().doubleValue();

        List<PayType> payTypes = new ArrayList<PayType>();
        // moneyPaid surplus 存储订单支付信息
        double moneyPaid = master.getMoneyPaid().doubleValue();
        // 主订单余额
        double surplus = master.getSurplus().doubleValue();
        if (surplus > 0) {
            if (settlementPrice > surplus) {
                // 部分平台币
                distribute.setSurplus(new BigDecimal(surplus));
                distribute.setMoneyPaid(new BigDecimal(settlementPrice - surplus));
                master.setSurplus(new BigDecimal(0));
                payTypes.add(new PayType(3, surplus));
                double payFee = settlementPrice - surplus;
                payTypes.add(new PayType(pId, payFee));
                master.setMoneyPaid(new BigDecimal(moneyPaid - payFee));
            } else {
                // 全部平台币
                distribute.setSurplus(new BigDecimal(settlementPrice));
                distribute.setMoneyPaid(new BigDecimal(0.0));
                master.setSurplus(new BigDecimal(surplus - settlementPrice));
                payTypes.add(new PayType(3, moneyPaid));
            }
        } else {
            // 支付总金额
            BigDecimal payFee = new BigDecimal(settlementPrice);
            distribute.setMoneyPaid(payFee);
            distribute.setSurplus(new BigDecimal(0.0));
            payTypes.add(new PayType(pId, payFee.doubleValue()));
            master.setMoneyPaid(new BigDecimal(moneyPaid - settlementPrice));
        }

        distribute.setPayInfo(JSON.toJSONString(payTypes));
    }

    /**
     * 设置交货单价格信息
     * @param goodsDistribute
     * @param distribute
     */
	private void fillDistributePriceInfo(GoodsDistribute goodsDistribute, OrderDistribute distribute) {
        // 商品总金额
        double goodsAmount = 0.0d;
        // 商品总折扣
        double discount = 0.0d;
        // 商品总折扣
        int goodsCount = 0;
        // 商品积分金额
        double integralMoney = 0D;
        // 商品积分
        int integral = 0;
        // 商品总财务价
        double settlementPrice = 0.0d;
        // 红包
        double bonus = 0.0d;

        // 交货单号
        String orderSn = distribute.getOrderSn();
        for (MasterOrderGoods orderGoods : goodsDistribute.getOrderGoods()) {
            int goodsNumber = orderGoods.getGoodsNumber();

            // 商品总金额
            double totalGoodsAmount = NumberUtil.mul(orderGoods.getGoodsPrice().doubleValue(), goodsNumber);
            goodsAmount = NumberUtil.sum(goodsAmount, totalGoodsAmount);

            // 折扣
            double totalDiscount = NumberUtil.mul(orderGoods.getDiscount().doubleValue(), goodsNumber);
            discount = NumberUtil.sum(discount, totalDiscount);

            // 积分金额
            double totalIntegralMoney = NumberUtil.mul(orderGoods.getIntegralMoney().doubleValue(), goodsNumber);
            integralMoney = NumberUtil.sum(integralMoney, totalIntegralMoney);

            // 积分
            integral += orderGoods.getIntegral() * goodsNumber;
            // 商品数量
            goodsCount += goodsNumber;

            // 结算价
            double totalSettlementPrice = NumberUtil.mul(orderGoods.getSettlementPrice().doubleValue(), goodsNumber);
            settlementPrice = NumberUtil.sum(settlementPrice, totalSettlementPrice);

            // 平摊红包
            double totalBonus = NumberUtil.mul(orderGoods.getShareBonus().doubleValue(), goodsNumber);
            bonus = NumberUtil.sum(bonus, totalBonus);

            MasterOrderGoods updateGoods = new MasterOrderGoods();
            updateGoods.setId(orderGoods.getId());
            updateGoods.setOrderSn(orderSn);
            masterOrderGoodsMapper.updateByPrimaryKeySelective(updateGoods);
        }

        distribute.setGoodsCount(goodsCount);
        distribute.setDiscount(new BigDecimal(discount));
        distribute.setGoodsAmount(new BigDecimal(goodsAmount));
        distribute.setIntegral(integral);
        distribute.setIntegralMoney(new BigDecimal(integralMoney));
        distribute.setBonus(new BigDecimal(bonus));

        // 商品总金额： 215.00 元 - 折让： 0.00 元 + 发票税额： 0.00 元 + 配送费用： 0.00 元 + 保价费用： 0.00 元 + 支付费用： 0.00 元
        distribute.setTotalFee(new BigDecimal(settlementPrice));
        distribute.setPayTotalFee(new BigDecimal(0.0));
    }

    /**
     * 保存分配单日志
     * @param distribute
     */
	private void saveDistributeAction(OrderDistribute distribute) {
        DistributeAction action = distributeActionService.createQrderAction(distribute);
        action.setActionNote("创建分配单成功：" + distribute.getOrderSn());
        distributeActionService.saveOrderAction(action);
    }

    /**
     * 填充订单信息到交货单
     * @param orderSn 交货单号
     * @param master 订单信息
     * @return OrderDistribute
     */
    private OrderDistribute fillOrderDistributeInfo(String orderSn, MasterOrderInfo master) {
        OrderDistribute distribute = new OrderDistribute();
        distribute.setOrderSn(orderSn);
        distribute.setMasterOrderSn(master.getMasterOrderSn());
        distribute.setOrderType(master.getOrderType());
        distribute.setTransType(master.getTransType());
        distribute.setOrderOutSn(master.getOrderOutSn());
        distribute.setOrderStatus(master.getOrderStatus());
        distribute.setPayStatus(master.getPayStatus());
        distribute.setShipStatus(master.getShipStatus());
        // 0未分仓
        distribute.setDepotStatus((byte)Constant.OI_DEPOT_STATUS_UNDEPOTED);
        // 0正常单
        distribute.setQuestionStatus(Constant.OI_QUESTION_STATUS_NORMAL);
        distribute.setOrderFrom(master.getOrderFrom());
        distribute.setAddTime(master.getAddTime());
        distribute.setReferer(master.getReferer());

        distribute.setSource(master.getSource());
        distribute.setIsnow(master.getIsnow());
        distribute.setIsDel(0);
        distribute.setBvValue(master.getBvValue());
        distribute.setPoints(master.getPoints());

        return distribute;
    }

	private String getGotCode(String orderSn) {
		String gotCode = GotCodeUtil.createData(8);
		if (StringUtil.isTrimEmpty(gotCode)) {
			long result = -1;
			String gotCodeKey = "got_code_" + gotCode;
			try {
				result = redisClient.setnx(gotCodeKey, orderSn);
				// redis 超时时间有5秒钟改成1分钟 （60S）
				redisClient.expire(gotCodeKey, 60);
			} catch (Throwable e) {
				logger.error("订单[" + orderSn + "]判断是否被使用,读取redis异常:" + e);
			}
			// 已经使用重新获取
			if (result == 0) {
				gotCode = getGotCode(orderSn);
			}
			// 已经使用重新获取
			OrderDistributeExample example = new OrderDistributeExample();
			example.or().andGotCodeEqualTo(gotCode).andGotStatusEqualTo(Constant.GOT_STATUS_NO);
			int count = orderDistributeMapper.countByExample(example);
			if (count > 0) {
				gotCode = getGotCode(orderSn);
			}
		}
		return gotCode;
	}
	
	/**
	 * 创建子订单号
	 * @param masterOrderSn
	 * @param index
	 * @return
	 */
	private String createOrderSn(String masterOrderSn, Integer index) {
		String orderSn = null;
		if (index == 0) {
			String maxOrderSn = getMaxOrderSn(masterOrderSn);
			if (StringUtil.isEmpty(maxOrderSn)) {
				orderSn = getSpliceOrderSn(masterOrderSn, 0);
			} else {
				orderSn = getSpliceOrderSn(masterOrderSn, getOrderSnIndex(maxOrderSn));
			}
		} else {
			orderSn = getSpliceOrderSn(masterOrderSn, index);
		}
		return orderSn;
	}

	/**
	 * 
	 * @param masterOrderSn
	 * @return
	 */
	private String getMaxOrderSn(String masterOrderSn) {
		OrderDistributeExample distributeExample = new OrderDistributeExample();
		distributeExample.setOrderByClause(" order_sn desc");
		distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn).limit(1);
		List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
		if (StringUtil.isListNotNull((distributes))) {
			return distributes.get(0).getOrderSn();
		}
		return null;
	}
	
	public String getSpliceOrderSn(String masterOrderSn, Integer index){
		String orderSn = "";
		index +=1;
		String hexCode = Integer.toHexString(index);
		if(hexCode.length() == 1){
			orderSn= masterOrderSn + "S0"+hexCode;
		} else {
			orderSn="S"+index;
		}
		return orderSn;
	}
	
	public int getOrderSnIndex(String maxOrderSn){
		int sindex = maxOrderSn.indexOf("S");
		String code = maxOrderSn.substring(sindex + 1);
		if (StringUtil.isNotEmpty(code)) {
			return Integer.valueOf(code,16);
		}
		return 0;
	}
	private MasterOrderAction saveLogInfo(String message, MasterOrderInfo master) {
		MasterOrderAction action = new MasterOrderAction();
		action.setMasterOrderSn(master.getMasterOrderSn());
		action.setOrderStatus(master.getOrderStatus());
		action.setShippingStatus(master.getShipStatus());
		action.setPayStatus(master.getPayStatus());
		action.setActionUser(Constant.OS_STRING_SYSTEM);
		action.setActionNote(message);
		action.setLogTime(new Date());
		masterOrderActionMapper.insertSelective(action);
		return action;
	}
	
	private List<SystemRegionArea> getSystemRegion(final MasterOrderAddressInfo address) {
		String[] arr = new String[] { address.getCountry(), address.getProvince(), address.getCity(), address.getDistrict() };
		SystemRegionAreaExample example = new SystemRegionAreaExample();
		example.or().andRegionIdIn(Arrays.asList(arr));
		return systemRegionAreaMapper.selectByExample(example);
	}
	
	/**
	 * 根据地区ID查询地区名称
	 * 
	 * @param regions
	 *            地区数据源
	 * @param regionId
	 *            地区ID
	 * @return
	 */
	private String findRegionsNameByid(List<SystemRegionArea> regions, String regionId) {
		if (regions == null || regions.isEmpty())
			return "";
		for (int i = 0; i < regions.size(); i++) {
			SystemRegionArea sg = regions.get(i);
			if (sg == null)
				continue;
			if (sg.getRegionId() == null)
				continue;
			if (sg.getRegionId().equals(regionId)) {
				return sg.getRegionName();
			}
		}
		return "";
	}
}
