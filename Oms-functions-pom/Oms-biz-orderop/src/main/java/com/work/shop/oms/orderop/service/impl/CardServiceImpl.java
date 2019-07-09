package com.work.shop.oms.orderop.service.impl;

import com.work.shop.oms.orderop.service.CardService;

//@Service
public class CardServiceImpl implements CardService {

	/*@Resource
	CardPackageMapper cardPackageMapper;
	@Resource
	CardUserPackageMapper cardUserPackageMapper;
	@Resource
	CardLogMapper cardLogMapper;
	@Resource
	CardCouponMapper cardCouponMapper;
	@Resource
	private OrderPayMapper orderPayMapper;
	@Resource
	private CardTypesService cardTypesService;
	@Resource
	private OrderGoodsMapper orderGoodsMapper;
	@Resource
	private OrderCardPackageMapper orderCardPackageMapper;

	Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

	@Override
	public void useCardPackage(String orderSn, String cardNo, String userId) {
		log.info("使用红包" + cardNo + ",用户：" + userId);
		try {
			if (StringUtils.isEmpty(cardNo) || StringUtils.isEmpty(userId))
				return;
			CardPackage cardpackage = cardPackageMapper.selectByPrimaryKey(cardNo);
			if (cardpackage == null) {
				log.error("orderSn=" + orderSn + ";cardNo=" + cardNo + "; 红包卡号不存在");
				return ;
			}
			CardUserPackage carduserpackage = cardUserPackageMapper.selectByPrimaryKey(cardNo);
			if (cardpackage != null) {
				cardpackage.setStatus((byte) 4);
				cardPackageMapper.updateByPrimaryKeySelective(cardpackage);
			}
			if (cardpackage != null) {
				carduserpackage.setStatus((byte) 4);
				cardUserPackageMapper.updateByPrimaryKeySelective(carduserpackage);
			}
			CardLog cardlog = new CardLog();
			cardlog.setCardNo(cardpackage.getCardNo());
			cardlog.setCardMoney(cardpackage.getCardMoney());
			cardlog.setCardTypeEnName("package");
			cardlog.setClDesc("用户：参数[userId:" + userId + "]，卡号：cardNo:" + cardNo);
			cardlog.setClType((byte) 4);
			cardlog.setComefrom("oms");
			cardlog.setCreateTime(new Date());
			cardlog.setUserId(userId);
			cardLogMapper.insertSelective(cardlog);
			// 将红包信息记录至OS order_card_package表中，以便后续变更红包面值使用
			try {
				if (cardpackage != null) {
					OrderCardPackage orderCardPackage = new OrderCardPackage();
					orderCardPackage.setOrderSn(orderSn);
					orderCardPackage.setCardNo(cardNo);
					orderCardPackage.setCardLn(cardpackage.getCardLn());
					orderCardPackage.setCardMoney(cardpackage.getCardMoney());
					orderCardPackage.setNewCardMoney(0.00F);
					orderCardPackage.setCardLimitMoney(cardpackage.getCardLimitMoney());
					orderCardPackage.setCreateTime(new Date());
					orderCardPackageMapper.insert(orderCardPackage);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("保存订单使用红包信息："+e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void userCardCoupon(String cardNo, String userId) {
		try {
			if (StringUtils.isEmpty(cardNo) || StringUtils.isEmpty(userId)) {
				return;
			}
			CardCoupon cardCoupon =  cardCouponMapper.selectByPrimaryKey(cardNo);
			if(cardCoupon == null){
				return ;
			}
			cardCoupon.setStatus((byte) 4);
			cardCouponMapper.updateByPrimaryKeySelective(cardCoupon);
			log.debug("打折卷使用成功:" + cardNo);
			
			CardLog cardlog = new CardLog();
			cardlog.setCardNo(cardCoupon.getCardNo());
			cardlog.setCardMoney(cardCoupon.getCardMoney());
			cardlog.setCardTypeEnName("coupon");
			cardlog.setClDesc("用户：参数[userId:" + userId + "]，卡号：cardNo:" + cardNo);
			cardlog.setClType((byte) 4);
			cardlog.setComefrom("oms");
			cardlog.setCreateTime(new Date());
			cardlog.setUserId(userId);
			log.debug("打折卷使用成功 插入使用日志:" + cardNo);

			cardLogMapper.insertSelective(cardlog);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void cancelCardForOrderCancel(String orderSn, String userId,
			double bonusMoney) {
		log.info("订单取消退卡 orderSn=" + orderSn + ";userId=" + userId + ";bonusMoney=" + bonusMoney);
		if(StringUtils.isBlank(orderSn) || StringUtils.isBlank(userId)) {
			return;
		}
		List<String> bonusIds = getBonusIdsByOrderSn(orderSn);
		if (StringUtil.isNotNullForList(bonusIds)) {
			CardUserPackageExample userPackageExample = new CardUserPackageExample();
			userPackageExample.createCriteria().andCardNoIn(bonusIds);
			List<CardUserPackage> userPackages = cardUserPackageMapper.selectByExample(userPackageExample);
			if(userPackages != null && userPackages.size() > 0) {
				double cardTotal = 0d;
				for(CardUserPackage card : userPackages) {
					cardTotal += card.getCardMoney();
				}
				if(cardTotal == bonusMoney) {//红包全部金额
					//订单取消时，红包或券的重新绑定
					rebindCardForOrderCancel(userId, bonusIds);
				} else {
					//订单使用了这些红包的部分金额，作废红包
					for(String cn : bonusIds) {
						cardTypesService.repealCard(cn);
					}
				}
			}
		}
		//退打折券
		OrderGoodsExample orderGoodsExample = new OrderGoodsExample();
		orderGoodsExample.createCriteria().andOrderSnEqualTo(orderSn);
		List<OrderGoods> orderGoods = orderGoodsMapper.selectByExample(orderGoodsExample);
		List<String> orderGoodsCardNo = new ArrayList<String>();
		if(orderGoods != null) {
			for(OrderGoods og : orderGoods) {
				if (StringUtil.isNotEmpty(og.getUseCard())) {
					String[] cardNos = og.getUseCard().split(":");
					for (String cardNo : cardNos) {
						if(StringUtils.isNotEmpty(cardNo)) {
							orderGoodsCardNo.add(cardNo);
						}
					}
				}
			}
			rebindCardForOrderCancel(userId, orderGoodsCardNo);
		}
	}
	
	@Override
	public List<String> getBonusIdsByOrderSn(String orderSn) {
		OrderPayExample queryBonus = new OrderPayExample();
		queryBonus.createCriteria().andOrderSnEqualTo(orderSn.trim());
		List<OrderPay> orderPays = orderPayMapper.selectByExample(queryBonus);
		if(StringUtil.isNotNullForList(orderPays)) {
			List<String> bonusIds = new ArrayList<String>();
			for(OrderPay p : orderPays) {
				if (StringUtil.isNotEmpty(p.getBonusId())) {
					// bonusIds.add(p.getBonusId());
					// 多个红包逗号分割
					String[] bonusIdsArr = p.getBonusId().split(",");
					for (String bonusId : bonusIdsArr) {
						if(StringUtils.isNotEmpty(bonusId)) {
							bonusIds.add(bonusId);
						}
					}
				}
			}
			return bonusIds;
		}
		return null;
	}
	
	*//**
	 * 订单取消时，红包或券的重新绑定
	 * @param userId
	 * @param bunosIds
	 *//*
	private void rebindCardForOrderCancel(String userId, List<String> bonusIds) {
		log.info("rebindCardForOrderCancel[userId={}, bonusIds={}]", userId, bonusIds);
		
		for(String cardNo : bonusIds) {
			if(cardNo.length() < 4) {
				continue;
			}

			String typeCode = cardNo.substring(0, 2);
			
			String tableName = null;
			if(StringUtils.isBlank(tableName = cardTypesService.getCardTableName(typeCode))) {
				log.debug("rebindCardForOrderCancel : get table name failed, userId={}, bonusId={}", userId, cardNo);
				//订单取消时，券卡操作日志
				logCardFailed(userId, cardNo, tableName, 0.00d);
				continue;
			}

			
			CardInfo cardInfo = cardTypesService.getCardInfo(cardNo, tableName);
			if(cardInfo == null) {
				log.debug("rebindCardForOrderCancel : get card info failed, userId={}, bonusId={}", userId, cardNo);
				logCardFailed(userId, cardNo, tableName, 0.0);
				return;
			}

			//判断是否已无效
			if(cardInfo.getStatus() == 3) {
				log.debug("rebindCardForOrderCancel : card status = {},红包已处于无效状态!", cardInfo.getStatus());
				return;
			}

			//判断是否过期
			if(cardInfo.getExpireTime().getTime()+24*60*60*1000<(new Date()).getTime()) {
				log.debug("rebindCardForOrderCancel : the card[cardNo-{}] is out of date[expireTime={}]",
						cardNo, cardInfo.getExpireTime());
				//作废红包券卡
//				this.invalidCard(cardNo, cardTable);
				return ;
			}

			cardInfo.setCardNo(cardTypesService.generateNewCardNo(cardNo, tableName));
			cardInfo.setStatus(Byte.valueOf("1"));
			cardInfo.setActiveTime(new Date());
			cardInfo.setCreateTime(new Date());
			cardTypesService.saveCardInfo(cardInfo, tableName, "取消订单", cardNo);
		}
	}
	
	@Override
	public void logCardFailed(String userId, String cardNo, String cardTable,
			double cardMoneyNew) {
		
		CardLog cl = new CardLog();
		cl.setClDesc(userId + "取消订单重新分配券卡时操作失败！没有能重新绑定新的券卡，原来的券卡是【" + cardNo + "】, ");
		cl.setUserId(userId);
		cl.setClType(Byte.valueOf("6"));
		cl.setCreateTime(new Date());
		cl.setCardNo(cardNo);
		cl.setCardTypeEnName(StringUtils.isBlank(cardTable) ?  "":  cardTable.substring(5));
		cl.setCardMoney((float)cardMoneyNew);
		
		cardLogMapper.insert(cl);
	}*/
}
