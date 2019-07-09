package com.work.shop.oms.common.bean;

import java.io.Serializable;
/**
 * 支付方式管理VO
 * @author lyh
 *
 */
public class PaymentManagementVO implements Serializable{

	private static final long serialVersionUID = -8858486589557292392L;
	
	private String payId;//支付方式ID
	private String payCode;//支付方式编码
	private String payName;//支付方式名称
	private String payFee;//支付费用
	private String payDesc;//支付方式描述
	private String payOrder;//支付方式在页面的显示顺序
	private String payConfig;//支付方式的配置信息，包括商户号和密钥什么的
	private String enabled;//是否可用，0，否；1，是
	private String isCod;//是否货到付款，0，否；1，是
	private String isOnline;//是否在线支付，0，否；1，是
	private String isMobile;//是否手机渠道使用
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public String getPayFee() {
		return payFee;
	}
	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}
	public String getPayDesc() {
		return payDesc;
	}
	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}
	public String getPayOrder() {
		return payOrder;
	}
	public void setPayOrder(String payOrder) {
		this.payOrder = payOrder;
	}
	public String getPayConfig() {
		return payConfig;
	}
	public void setPayConfig(String payConfig) {
		this.payConfig = payConfig;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getIsCod() {
		return isCod;
	}
	public void setIsCod(String isCod) {
		this.isCod = isCod;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public String getIsMobile() {
		return isMobile;
	}
	public void setIsMobile(String isMobile) {
		this.isMobile = isMobile;
	}
	
	

}
