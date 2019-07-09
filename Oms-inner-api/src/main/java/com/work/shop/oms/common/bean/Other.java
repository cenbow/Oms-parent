/*
 * 上海坦思计算机系统有限公司
 * 
 * -------------+-----------+-----------------------------
 * 更新时间			更新者			更新内容
 * -------------+-----------+-----------------------------
 * 2011/06/21		吴健				创建
 */
package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.common.utils.ObjectUtil;


/**
 * 订单信息中其他信息
 * @author wujian
 *
 */
public class Other extends ObjectUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4007034618404384118L;
	/**
	 * 发票类型
	 */
	private String invType;
	/**
	 * 发票抬头
	 */
	private String invPayee;
	/**
	 * 发票内容
	 */
	private String invCon;
	/**
	 * 客户给商家的留言
	 */
	private String userScript;
	/**
	 * 缺货处理
	 */
	private String howOos;
	/**
	 * 商家备注
	 */
	private String toUser;
	/**
	 * 发票税额
	 */
	private float invAmt;
	/**
	 * 配送费
	 */
	private float shipAmt;
	/**
	 * 报价费
	 */
	private float insureAmt;
	/**
	 * 支付额
	 */
	private float payAmt;
	/**
	 * 包装费
	 */
	private float packAmt;
	
	/**
	 * 商品总金额
	 */
	private float goodsAmount;
	/**
	 * 订单总金额
	 */
	private float totalFee;
	/**
	 * 已付款金额
	 */
	private float moneyPaid;
	/**
	 * 已付款金额，根据已付款的付款单划分
	 */
	private List<Double> payedMoneyList;
	/**
	 * 换货时退货入款
	 */
	private float exchangeReturnFee;
	/**
	 * 折扣金额
	 */
	private float discount;
	/**
	 * 订单使用余额
	 */
	private float surplus;
	/**
	 * 使用积分
	 */
	private float integralMoney;
	/**
	 * 使用红包
	 */
	private float bonus;
	/*
	 * 使用红包名称
	 */
	private String bonusName = "";
	
	/**
	 * 应付款总金额
	 */
	private float totalPayable;
	/**
	 * 贺卡总费用
	 */
	private float cardTotalFee;
	/**
	 * 订单类型 0，正常订单 1，补发订单 2，换货订单
	 */
	private int orderType;
	
	public Other() {
	}

	public Other(Object[] o, int off) {
		setInvAmt(otf(o[off+0]));
		setShipAmt(otf(o[off+1]));
		setInsureAmt(otf(o[off+2]));
		setPayAmt(otf(o[off+3]));
		setPackAmt(otf(o[off+4]));
		setInvType(ots(o[off+5]));
		setInvPayee(ots(o[off+6]));			/*发票抬头*/
		setInvCon(ots(o[off+7]));			/*发票内容*/
		setUserScript(ots(o[off+8]));		/*客户留言*/
		setHowOos(ots(o[off+9]));			/*缺货处理*/
		setToUser(ots(o[off+10]));			/*商家备注*/ 
		setGoodsAmount(otf(o[off+11]));		
		setTotalFee(otf(o[off+12]));
		setMoneyPaid(otf(o[off+13]));	
		setDiscount(otf(o[off+14]));
		setSurplus(otf(o[off+15]));
		setBonus(otf(o[off+16]));
		setIntegralMoney(otf(o[off+17]));
		setTotalPayable(otf(o[off+18]));
		setOrderType(oti(o[off+19]));
		setCardTotalFee(otf(o[off+20]));
	}
	public String getInvType() {
		return invType;
	}
	public void setInvType(String invType) {
		this.invType = invType;
	}
	public String getInvPayee() {
		return invPayee;
	}
	public void setInvPayee(String invPayee) {
		this.invPayee = invPayee;
	}
	public String getInvCon() {
		return invCon;
	}
	public void setInvCon(String invCon) {
		this.invCon = invCon;
	}
	public String getUserScript() {
		return userScript;
	}
	public void setUserScript(String userScript) {
		this.userScript = userScript;
	}
	public String getHowOos() {
		return howOos;
	}
	public void setHowOos(String howOos) {
		this.howOos = howOos;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public float getInvAmt() {
		return invAmt;
	}
	public void setInvAmt(float invAmt) {
		this.invAmt = invAmt;
	}
	public float getShipAmt() {
		return shipAmt;
	}
	public void setShipAmt(float shipAmt) {
		this.shipAmt = shipAmt;
	}
	public float getInsureAmt() {
		return insureAmt;
	}
	public void setInsureAmt(float insureAmt) {
		this.insureAmt = insureAmt;
	}
	public float getPayAmt() {
		return payAmt;
	}
	public void setPayAmt(float payAmt) {
		this.payAmt = payAmt;
	}
	public float getPackAmt() {
		return packAmt;
	}
	public void setPackAmt(float packAmt) {
		this.packAmt = packAmt;
	}

	public float getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(float goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public float getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(float totalFee) {
		this.totalFee = totalFee;
	}

	public float getMoneyPaid() {
		return moneyPaid;
	}

	public void setMoneyPaid(float moneyPaid) {
		this.moneyPaid = moneyPaid;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public float getSurplus() {
		return surplus;
	}

	public void setSurplus(float surplus) {
		this.surplus = surplus;
	}

	public float getIntegralMoney() {
		return integralMoney;
	}

	public void setIntegralMoney(float integralMoney) {
		this.integralMoney = integralMoney;
	}

	public float getBonus() {
		return bonus;
	}

	public void setBonus(float bonus) {
		this.bonus = bonus;
	}

	public String getBonusName() {
		return bonusName;
	}

	public void setBonusName(String bonusName) {
		this.bonusName = bonusName;
	}

	public float getTotalPayable() {
		return totalPayable;
	}

	public void setTotalPayable(float totalPayable) {
		this.totalPayable = totalPayable;
	}

	public float getCardTotalFee() {
		return cardTotalFee;
	}

	public void setCardTotalFee(float cardTotalFee) {
		this.cardTotalFee = cardTotalFee;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public List<Double> getPayedMoneyList() {
		return payedMoneyList;
	}

	public void setPayedMoneyList(List<Double> payedMoneyList) {
		this.payedMoneyList = payedMoneyList;
	}

	public float getExchangeReturnFee() {
		return exchangeReturnFee;
	}

	public void setExchangeReturnFee(float exchangeReturnFee) {
		this.exchangeReturnFee = exchangeReturnFee;
	}
	
}
