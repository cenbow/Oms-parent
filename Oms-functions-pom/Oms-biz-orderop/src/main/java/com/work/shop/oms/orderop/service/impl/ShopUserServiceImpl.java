package com.work.shop.oms.orderop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.SystemPayment;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.bean.SystemRegionAreaExample;
import com.work.shop.oms.common.bean.DistributeGoods;
import com.work.shop.oms.common.bean.DistributeOrder;
import com.work.shop.oms.common.bean.DistributePay;
import com.work.shop.oms.common.bean.OrderDepotResult;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ShopUserInfo;
import com.work.shop.oms.dao.MasterOrderActionMapper;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderGoodsDetailMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.SystemPaymentMapper;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.orderop.service.JmsSendQueueService;
import com.work.shop.oms.orderop.service.ShopUserService;
import com.work.shop.oms.orderop.service.UserPointsService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.HttpClientUtil;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;

@Service
public class ShopUserServiceImpl implements ShopUserService{

	private static Logger logger = Logger.getLogger(ShopUserServiceImpl.class);
	private static String SHOP_USER_SERVICE_URL = ConfigCenter.getProperty("shopuserservice.address");
	

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
	@Resource(name = "noticePickingProducerJmsTemplate")
	private JmsTemplate noticePickingJmsTemplate;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
    @Resource
    private SystemPaymentMapper systemPaymentMapper;
    @Resource
    private MasterOrderGoodsDetailMapper masterOrderGoodsDetailMapper;
    //@Resource
    // private UserPointsService userPointsService;
    @Resource
    private JmsSendQueueService jmsSendQueueService;
	
	@Override
	public ReturnInfo<ShopUserInfo> getUserCreateOrderInfo(String userId) {
		logger.info("[获取创建订单用户信息]start.shopCode:" + userId);
		ReturnInfo<ShopUserInfo> response = new ReturnInfo<ShopUserInfo>(Constant.OS_NO);
		try {
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			NameValuePair param = null;
			param = new BasicNameValuePair("shopCode", userId);
			paramsList.add(param);
//			param = new BasicNameValuePair("userId", "userId");
//			paramsList.add(param);
			String url = SHOP_USER_SERVICE_URL +"/custom/api/getUserCreateOrderInfo.do";
			String back = HttpClientUtil.post(url,paramsList);
			logger.info("[获取创建订单用户信息接口调用]shopCode:" + userId+",reponse:" + back);
			if (StringUtil.isEmpty(back)) {
				logger.error("[获取创建订单用户信息接口调用]FAILURE,shopCode:" + userId +",reponse:返回结果为空！");
				response.setMessage("[获取创建订单用户信息接口调用]返回结果为空！");
				return response;
			}
			ShopUserInfo info = JSON.parseObject(back, ShopUserInfo.class);
			if (info.getIsOk() && info.getUserModel() != null) {
				response.setData(info);
				response.setIsOk(Constant.OS_YES);
				response.setMessage("success");
			} else {
				response.setMessage("[获取创建订单用户信息接口调用]FAILURE,shopCode:" + userId +",reponse:用户信息为空！");
				logger.error("[获取创建订单用户信息接口调用]FAILURE,shopCode:" + userId +",reponse:用户信息为空！");
			}
		
		} catch (Exception e) {
			response.setMessage("获取创建订单用户信息接口调用失败！错误信息:"+e.getMessage());
			logger.error("[获取创建订单用户信息接口调用]Failed,shopCode:" + userId + ",msg:"+e.getMessage(),e);
		}
		return response;
	}

	@Override
	public ReturnInfo<String> pushOrdreShop(Integer pushType,
			String masterOrderSn, MasterOrderInfo master) {
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		try {
			if (master == null) {
				master = this.masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			}
			if (!master.getChannelCode().equals(Constant.Chlitina)) {
				info.setMessage("不是店长站点");
				return info;
			}
			if (master.getOrderType().intValue() == Constant.OI_ORDER_TYPE_CHANGE) {
				info.setMessage("换单不推送消息");
				return info;
			}
			MasterOrderPayExample orderPayExample = new MasterOrderPayExample();
			orderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<MasterOrderPay> orderPays = this.masterOrderPayMapper.selectByExample(orderPayExample);
			String shipSn = null;
			String depotCode = Constant.DETAILS_DEPOT_CODE;
			pushXomsCommon(master, masterOrderSn, shipSn, orderPays, depotCode, pushType);
			info.setMessage("success");
			info.setIsOk(Constant.OS_YES);
		} catch (Exception e) {
			logger.error("[" + masterOrderSn + "]下发长平台系统异常" + e.getMessage(), e);
			info.setMessage("[" + masterOrderSn + "]下发长平台系统异常" + e.getMessage());
		}
		return info;
	}
	
	private OrderDepotResult pushXomsCommon(MasterOrderInfo master, String masterOrderSn,
			String shipSn, List<MasterOrderPay> orderPays, String depotCode, Integer pushType) {
		OrderDepotResult depotResult = new OrderDepotResult(-1, "初始默认错误");
		try {
			String queueName = "push_order_shop";
			MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			MasterOrderAddressInfo address = this.masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
			List<SystemRegionArea> regions = getSystemRegion(address);
			DistributeOrder distributeOrder = new DistributeOrder();
			// 订单信息
			distributeOrder.setMasterOrderSn(masterOrderSn);
			distributeOrder.setShipSn(shipSn);
			distributeOrder.setOrderType(master.getOrderType());
			distributeOrder.setRelatingShipSn(master.getRelatingOriginalSn()); // 关联交货单
			distributeOrder.setRelatingReturnSn(master.getRelatingReturnSn()); // 关联退货单
			if (pushType.intValue() == 2) {
				distributeOrder.setOrderStatus(Constant.OI_ORDER_STATUS_CANCLED);
			} else {
				distributeOrder.setOrderStatus(master.getOrderStatus().intValue());
			}
			distributeOrder.setDiscount(setScale(master.getDiscount()));
			distributeOrder.setGoodsAmount(setScale(master.getGoodsAmount()));
			distributeOrder.setGoodsCount(master.getGoodsCount());
			distributeOrder.setTotalFee(setScale(master.getTotalFee()));
			distributeOrder.setMoneyPaid(setScale(master.getMoneyPaid().add(master.getSurplus())));
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
			distributeOrder.setAreaCode(address.getAreaCode());
			distributeOrder.setReferer(master.getReferer());
			distributeOrder.setPushType(pushType);
			distributeOrder.setShippingAddress(address.getShippingAddress());
			distributeOrder.setShippingTotalFee(master.getShippingTotalFee().doubleValue());
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
			criteria.andMasterOrderSnEqualTo(masterOrderSn).andIsDelEqualTo(0);
			List<MasterOrderGoods> goodsList = this.masterOrderGoodsDetailMapper.getXOMSMasterOrderGoods(masterOrderSn);
			if (StringUtil.isListNotNull(goodsList)) {
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
				String data = JSON.toJSONString(distributeOrder);
				logger.info("queueName:" + queueName + ";订单数据下发店长平台:" + data);
				if (StringUtil.isNotEmpty(queueName)) {
					jmsSendQueueService.sendQueueMessage(queueName, data);
					depotResult.setMessage("[" + masterOrderSn + "]下发XOMS系统成功");
					depotResult.setResult(1);
				}
			} else {
				logger.error("[" + masterOrderSn + "]下发长平台系统，有效订单商品为空！");
				depotResult.setMessage("[" + masterOrderSn + "]下发长平台系统，有效订单商品为空！");
			}
		} catch (Exception e) {
			logger.error("[" + masterOrderSn + "]下发长平台系统异常" + e.getMessage(), e);
			depotResult.setMessage("[" + masterOrderSn + "]下发长平台系统异常" + e.getMessage());
		}
		return depotResult;
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
}