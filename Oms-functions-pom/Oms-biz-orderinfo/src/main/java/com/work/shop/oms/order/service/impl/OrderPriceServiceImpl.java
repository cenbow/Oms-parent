package com.work.shop.oms.order.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.utils.NumberUtil;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.OrderPriceService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.StringUtil;

@Service
public class OrderPriceServiceImpl implements OrderPriceService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource(name="masterOrderActionServiceImpl")
	private MasterOrderActionService masterOrderActionService;
	@Resource
	private RedisClient redisClient;
	
	private final static String extensionCodes = "gift,prize";
	
	/**
	 * 计算订单商品财务价格
	 */
	public void financialPrice(MasterOrderInfo masterOrderInfo, List<MasterOrderGoods> masterOrderGoods) {
		logger.debug("计算订单财务价格");
		if (masterOrderInfo == null || StringUtil.isListNull(masterOrderGoods))
			return;
		// 如果没有使用红包，则财务价格就是成交价格
		boolean bonusZero = false;
		if (masterOrderInfo.getBonus().doubleValue() == 0) {
			bonusZero = true;
		}
		// 如果没有使用余额，则分摊余额为0.
		boolean surplusZero = false;
		if (masterOrderInfo.getSurplus().doubleValue() <= 0) {
			surplusZero = true;
		}
		String masterOrderSn = masterOrderInfo.getMasterOrderSn();
		String actionNote = "红包,余额,积分平摊计算正确！";
		for (MasterOrderGoods orderGoods : masterOrderGoods) {
			String sku = orderGoods.getCustomCode();
			// 非赠品（ "gift" "prize"）商品需要验证保护价
			if (extensionCodes.indexOf(orderGoods.getExtensionCode()) == -1) {
				// 商品的保护价
				try {
					String doubleValue = getProtectedPrice(sku.substring(0, 6));
					double protectPrice = StringUtil.isEmpty(doubleValue) ? 0d : Double.parseDouble(doubleValue);
					if (subPrice(protectPrice, orderGoods.getTransactionPrice().doubleValue()) > 0d && protectPrice > 0) {
						// 商品价格低于保护价
						orderGoods.setProtectFalg(1);// 设置商品价格低于保护价标记
						MasterOrderinfoServiceImpl.questionType.set(MasterOrderinfoServiceImpl.QUESTION_TYPE_DIYUZKJ);
					}
				} catch (Throwable e) {
					logger.error("", e);
				}
			}
			try {
				// 成交价小于0报错
				if (orderGoods.getTransactionPrice().doubleValue() < 0d) {
					actionNote = "<font color=\"red\">" + orderGoods.getCustomCode() + "的成交价为负数;红包和余额平摊计算失败</font>";
					try {
						MasterOrderAction orderAction = masterOrderActionService.createOrderAction(masterOrderInfo);
						orderAction.setActionNote(actionNote);
						masterOrderActionService.insertOrderActionByObj(orderAction);
					} catch (Exception e) {
						logger.error(masterOrderSn + "红包和余额平摊计算失败。日志插入异常" , e);
					}
					return;
				}
				// 财务结算价格 = 商品成交价- 红包分摊 - 积分分摊
				Double settPrice = orderGoods.getTransactionPrice().doubleValue()
						- orderGoods.getShareBonus().doubleValue()
						- orderGoods.getIntegralMoney().doubleValue();
				orderGoods.setSettlementPrice(NumberUtil.getBigDecimalByDouble(settPrice, 6));// 财务结算价
//				orderGoods.setShareBonus(BigDecimal.valueOf(0d));// 商品分担红包金额
				orderGoods.setShareSurplus(BigDecimal.valueOf(0d));// 商品分担余额金额
				continue;
				
				// 使用积分
				// 平台订单下单时红包余额已经分摊到每行每件上
				/*if (Constant.REQ_TYPE_POST.equals("1")) {
					Double settPrice = orderGoods.getTransactionPrice().doubleValue()
							- orderGoods.getShareBonus().doubleValue()
							- orderGoods.getIntegralMoney().doubleValue();
					orderGoods.setSettlementPrice(BigDecimal.valueOf(settPrice));// 财务结算价
//					orderGoods.setShareBonus(BigDecimal.valueOf(0d));// 商品分担红包金额
					orderGoods.setShareSurplus(BigDecimal.valueOf(0d));// 商品分担余额金额
					continue;
				}*/
				
				/*if (bonusZero && surplusZero) { 
					// 没有使用红包同时没有使用余额
					// 财务结算价
					orderGoods.setSettlementPrice(orderGoods.getTransactionPrice());
					// 商品分担红包金额
					orderGoods.setShareBonus(BigDecimal.valueOf(0d));
					// 商品分担余额金额
					orderGoods.setShareSurplus(BigDecimal.valueOf(0d));
				}*/
				
				// 如果是平台渠道
				/*if ((masterOrderInfo.getOrderFrom() != null && Constant.OS_CHANNEL_BANGGO.equalsIgnoreCase(masterOrderInfo.getOrderFrom())
						|| Constant.OS_REFERER_YHJ.equals(masterOrderInfo.getReferer()))) {
					// 红包折算价/数量 = 红包折算单价
					BigDecimal temp = BigDecimal.valueOf(orderGoods.getShareBonus().doubleValue() / orderGoods.getGoodsNumber());
					// 设置红包分摊价格
					orderGoods.setShareBonus(temp);
					// 财务结算价 = 商品成交价 - 红包折算单价 
					orderGoods.setSettlementPrice(orderGoods.getTransactionPrice().subtract(temp));
					continue;
				} else {
					// 如果是不是平台订单 财务结算价 = 商品成交价
					orderGoods.setSettlementPrice(orderGoods.getTransactionPrice());
					continue;
				}*/
			} catch (Exception e) {
				logger.error(masterOrderSn + "红包和余额平摊计算异常", e);
			}
		}
	}

	/**
	 * 获取商品保护价
	 * 
	 * @param goodsSn
	 *            六位码
	 * @return
	 */
	private String getProtectedPrice(String goodsSn) {
		logger.debug("getProtectedPrice - goodssn :" + goodsSn);
		String resultStr = null;
		if (StringUtil.isNotBlank(goodsSn)) {
			resultStr = redisClient.get("productGoodsProtectedPrice_goodsSn_" + goodsSn);
		}
		logger.debug("getProtectedPrice - resultStr :" + resultStr);
		return resultStr;
	}

	public double subPrice(double price1, double price2) {
		BigDecimal p1 = new BigDecimal(price1);
		BigDecimal p2 = new BigDecimal(price2);
		return p1.subtract(p2, MathContext.DECIMAL32).doubleValue();
	}
}
