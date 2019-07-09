/*
* 2014-7-28 上午11:00:26
* 吴健 HQ01U8435
*/

package com.work.shop.oms.orderop.service.impl;

import com.work.shop.oms.orderop.service.CardTypesService;

//@Service("cardTypesService")
public class CardTypesServiceImpl implements CardTypesService {

	/*@Resource
	private CardTypeMapper cardTypeMapper;

	@Resource
	private CardUserPackageMapper cardUserPackageMapper;
	@Resource
	private CardCouponMapper cardCouponMapper;
	@Resource
	private CardPackageMapper cardPackageMapper;
	@Resource
	private CardLogMapper cardLogMapper;
	
	*//**
	 * 
	 * @param typeCode
	 * @return
	 *//*
	@Override
	public String getCardTableName(String typeCode) {
		if(!StringUtils.isNumeric(typeCode)) {
			return null;
		}
		CardTypeExample cardTypeExample = new CardTypeExample();
		
		cardTypeExample.createCriteria().andTypeCodeEqualTo(Integer.parseInt(typeCode));
		
		List<CardType> cardTypes = cardTypeMapper.selectByExample(cardTypeExample);
		
		if(cardTypes != null && cardTypes.size() > 0) {
			String enName = cardTypes.get(0).getTypeEnName();
			
			return "card_" + enName;
		}
		
		return null;
	}

	@Override
	public CardInfo getCardInfo(String cardNo, String tableName) {
		String json = "";
		
		if (tableName.equalsIgnoreCase("card_coupon")) {
			CardCoupon cc = cardCouponMapper.selectByPrimaryKey(cardNo);
			json = JSON.toJSONString(cc);
//		} else if (tableName.equalsIgnoreCase("card_package")) {
//			CardPackage cp = cardPackageMapper.selectByPrimaryKey(cardNo);
//			json = JSON.toJSONString(cp);
		} else if (tableName.equalsIgnoreCase("card_package") || tableName.equalsIgnoreCase("card_user_package")) {
			CardUserPackage cup = cardUserPackageMapper.selectByPrimaryKey(cardNo);
			json = JSON.toJSONString(cup);
		}
		
		CardInfo rs = JSON.parseObject(json, CardInfo.class);
	
		return rs;
	}
	
	@Override
	public void saveCardInfo(CardInfo ci, String cardTable, String actionNote,
			String preCardNo) {
		String json = JSON.toJSONString(ci);
		if (cardTable.equalsIgnoreCase("card_coupon")) {
			CardCoupon cardCoupon = JSON.parseObject(json, CardCoupon.class);
			
			cardCouponMapper.insert(cardCoupon);
			
			CardCoupon oldCoupon = cardCouponMapper.selectByPrimaryKey(preCardNo);
			if(oldCoupon != null) {
				oldCoupon.setStatus(Byte.valueOf("3"));
				cardCouponMapper.updateByPrimaryKey(oldCoupon);
			}
		} else if (cardTable.equalsIgnoreCase("card_package") || cardTable.equalsIgnoreCase("card_user_package")) {
			CardUserPackage cardUserPackage = JSON.parseObject(json, CardUserPackage.class);

			cardUserPackageMapper.insert(cardUserPackage);
			
			CardUserPackage oldPackage = cardUserPackageMapper.selectByPrimaryKey(preCardNo);
			if(oldPackage != null) {
				oldPackage.setStatus(Byte.valueOf("3"));
				cardUserPackageMapper.updateByPrimaryKey(oldPackage);
			}
		}
		
		if(cardTable.equalsIgnoreCase("card_package")) {
			CardPackage cp = JSON.parseObject(json, CardPackage.class);
			cardPackageMapper.insert(cp);

			CardPackage oldPackage = cardPackageMapper.selectByPrimaryKey(preCardNo);
			if(oldPackage != null) {
				oldPackage.setStatus(Byte.valueOf("3"));
				cardPackageMapper.updateByPrimaryKey(oldPackage);
			}
		}
		
		//记录成功日志
		CardLog cl = new CardLog();
		cl.setClDesc(ci.getUserId() + actionNote+"重新分配券卡【" + ci.getCardNo() + "】");
		cl.setUserId(ci.getUserId());
		cl.setClType(ci.getStatus());
		cl.setCreateTime(ci.getCreateTime());
		cl.setCardNo(ci.getCardNo());
		cl.setCardTypeEnName(cardTable.substring(5));
		cl.setCardMoney((float)ci.getCardMoney());
		cardLogMapper.insert(cl);
	}
	
	@Override
	public String generateNewCardNo(String oldCardNo, String tableName) {
		
		String cardLn = oldCardNo.substring(oldCardNo.length() - 4, oldCardNo.length());
		boolean isCardPackage = false;
		if (tableName.equalsIgnoreCase("card_coupon")) {
			isCardPackage = false;
		} else if (tableName.equalsIgnoreCase("card_package") || tableName.equalsIgnoreCase("card_user_package")) {
			isCardPackage = true;
		}

		String newCardNo = null;
		
		while(true) {
			newCardNo = oldCardNo.substring(0, 3) + NumberUtil.randomStrNumber(7) + cardLn;
			
			if(isCardPackage) {
				if(cardUserPackageMapper.selectByPrimaryKey(newCardNo) == null) {
					return newCardNo;
				}
			} else {
				if(cardCouponMapper.selectByPrimaryKey(newCardNo) == null) {
					return newCardNo;
				}
			}
		}
	}
	
	@Override
	public void repealCard(String cardNo) {
		
		
	}*/
	
}
