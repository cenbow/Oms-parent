package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CardPackageUpdateInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6538239374611058450L;

	private String cardNo;										// 卡号

	private String cardLn;										// 卡批次

	private Float cardMoney;									// 卡面值

	private Float cardLimitMoney;								// 消费额度

	private Integer cardType;									// 卡类型
	
	private String cardLnName;									// 卡券名称

	private Float newCardMoney;									// 卡新面值
	
	private String rangeCode;									// 使用范围 0全场 1 品牌 2 单品
	
	private String rangeValue;									// 范围值
	
	private String rangeName;									// 使用范围 0全场 1 品牌 2 单品
	
	private String status;										// 券卡状态
	
	private String effectDate;									// 卡生效时间

	private String expireTime;									// 卡过期时间

	private String activeTime;									// 用户激活卡时间
	
	private Boolean selected;									// 是否选中

	private Boolean initSelected;								// 是否选中

	
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardLn() {
		return cardLn;
	}

	public void setCardLn(String cardLn) {
		this.cardLn = cardLn;
	}

	public Float getCardMoney() {
		return cardMoney;
	}

	public void setCardMoney(Float cardMoney) {
		this.cardMoney = cardMoney;
	}

	public Float getCardLimitMoney() {
		return cardLimitMoney;
	}

	public void setCardLimitMoney(Float cardLimitMoney) {
		this.cardLimitMoney = cardLimitMoney;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCardLnName() {
		return cardLnName;
	}

	public void setCardLnName(String cardLnName) {
		this.cardLnName = cardLnName;
	}

	public Float getNewCardMoney() {
		return newCardMoney;
	}

	public void setNewCardMoney(Float newCardMoney) {
		this.newCardMoney = newCardMoney;
	}

	public String getRangeCode() {
		return rangeCode;
	}

	public void setRangeCode(String rangeCode) {
		this.rangeCode = rangeCode;
	}

	public String getRangeValue() {
		return rangeValue;
	}

	public void setRangeValue(String rangeValue) {
		this.rangeValue = rangeValue;
	}
	
	public String getRangeName() {
		return rangeName;
	}

	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(String effectDate) {
		this.effectDate = effectDate;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}
	
	public Boolean getSelected() {
		if (selected != null) {
			selected = false;
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String nowdate = df.format(date);
			if (nowdate.compareTo(effectDate) >= 0 &&  nowdate.compareTo(expireTime) <= 0) {
				selected = true;
			}
		}
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	
	public Boolean getInitSelected() {
		return initSelected;
	}

	public void setInitSelected(Boolean initSelected) {
		this.initSelected = initSelected;
	}

	@Override
	public String toString() {
		return "CardPackageUpdateInfo [cardNo=" + cardNo + ", cardLn=" + cardLn
				+ ", cardMoney=" + cardMoney + ", cardLimitMoney="
				+ cardLimitMoney + ", cardType=" + cardType + ", cardLnName="
				+ cardLnName + ", newCardMoney=" + newCardMoney
				+ ", rangeCode=" + rangeCode + ", rangeValue=" + rangeValue
				+ ", rangeName=" + rangeName + ", status=" + status
				+ ", effectDate=" + effectDate + ", expireTime=" + expireTime
				+ ", activeTime=" + activeTime + ", selected=" + selected + "]";
	}
}
