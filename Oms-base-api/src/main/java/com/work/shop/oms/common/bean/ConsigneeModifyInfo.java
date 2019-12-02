package com.work.shop.oms.common.bean;

import java.io.Serializable;

/**
 * 收货人信息修改信息Bean
 *
 */
public class ConsigneeModifyInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String orderSn;								// 订单号
	private String actionUser;							// 操作人
	private String consignee;							//收货人名称
	private String country;								//收货人国家
	private String province;							//收货人省
	private String city;								//收货人城市
	private String district;							//收货人区域
	private String street;								//收货人街道
	private String email;								//收货人email
	private String zipcode;								//收货人邮编
	private String address;								//收货人详细地址
	private String tel;									//收货人电话	
	private String mobile;								//收货人手机
	private String signBuilding;						//标志性建筑
	private String bestTime;							//最佳送货时间
	private String oldMobile;							// 原手机号码
	private String oldTel;								// 原电话号码
	private Byte shippingId;							// 承运商Id
	private String shippingCode;						// 承运商编码
	private String depotCode;							// 分配仓编码
	private String invoiceNo;							// 承运商单号
	private String postscript;							// 给商家留言
	private String InvPayee;							// 发票抬头
	private String invContent;							// 发票内容，可留空
	private String paySn;								// 付款单编号
	private String orderOutSn;							// 小票号
	private double shippingFee;							// 运费
	private Double payTotalFee = 0D;					// 支付费用
	private String payId;								// 支付方式Id
	private String source = "OMS";						// 方法调用来源 POS:POS端;FRONT:前端;OMS:后台取消;ERP:ERP端

    /*发票信息*/
    /**
     * 发票类型
     */
    private String invType;

    /**
     * 发票抬头
     */
    private String invPayee;

    /**
     * 纳税人识别号
     */
    private String invTaxer;

    /**
     * 发票公司地址
     */
    private String invCompanyAddress;

    /**
     * 发票注册电话
     */
    private String invPhone;

    /**
     * 发票开户银行
     */
    private String invBank;

    /**
     * 发票银行账号
     */
    private String invBankNo;

    /*发票收货地址信息*/

    /**
     * 发票收货人
     */
    private String invConsignee;

    /**
     * 发票收货人手机号
     */
    private String invMobile;

    /**
     * 发票收货人地址
     */
    private String invAddress;

	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSignBuilding() {
		return signBuilding;
	}
	public void setSignBuilding(String signBuilding) {
		this.signBuilding = signBuilding;
	}
	public String getBestTime() {
		return bestTime;
	}
	public void setBestTime(String bestTime) {
		this.bestTime = bestTime;
	}
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}

	public String getOldMobile() {
		return oldMobile;
	}
	public void setOldMobile(String oldMobile) {
		this.oldMobile = oldMobile;
	}
	public String getOldTel() {
		return oldTel;
	}
	public void setOldTel(String oldTel) {
		this.oldTel = oldTel;
	}
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Byte getShippingId() {
		return shippingId;
	}
	public void setShippingId(Byte shippingId) {
		this.shippingId = shippingId;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public String getInvPayee() {
		return InvPayee;
	}
	public void setInvPayee(String invPayee) {
		InvPayee = invPayee;
	}
	public String getInvContent() {
		return invContent;
	}
	public void setInvContent(String invContent) {
		this.invContent = invContent;
	}
	public String getPaySn() {
		return paySn;
	}
	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}
	public String getOrderOutSn() {
		return orderOutSn;
	}
	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}
	public double getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}
	public Double getPayTotalFee() {
		return payTotalFee;
	}
	public void setPayTotalFee(Double payTotalFee) {
		this.payTotalFee = payTotalFee;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getActionUser() {
		return actionUser;
	}
	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public String getInvTaxer() {
        return invTaxer;
    }

    public void setInvTaxer(String invTaxer) {
        this.invTaxer = invTaxer;
    }

    public String getInvCompanyAddress() {
        return invCompanyAddress;
    }

    public void setInvCompanyAddress(String invCompanyAddress) {
        this.invCompanyAddress = invCompanyAddress;
    }

    public String getInvPhone() {
        return invPhone;
    }

    public void setInvPhone(String invPhone) {
        this.invPhone = invPhone;
    }

    public String getInvBank() {
        return invBank;
    }

    public void setInvBank(String invBank) {
        this.invBank = invBank;
    }

    public String getInvBankNo() {
        return invBankNo;
    }

    public void setInvBankNo(String invBankNo) {
        this.invBankNo = invBankNo;
    }

    public String getInvConsignee() {
        return invConsignee;
    }

    public void setInvConsignee(String invConsignee) {
        this.invConsignee = invConsignee;
    }

    public String getInvMobile() {
        return invMobile;
    }

    public void setInvMobile(String invMobile) {
        this.invMobile = invMobile;
    }

    public String getInvAddress() {
        return invAddress;
    }

    public void setInvAddress(String invAddress) {
        this.invAddress = invAddress;
    }

    @Override
	public String toString() {
		return "ConsigneeModifyInfo [orderSn=" + orderSn + ", actionUser="
				+ actionUser + ", consignee=" + consignee + ", country="
				+ country + ", province=" + province + ", city=" + city
				+ ", district=" + district + ", street=" + street + ", email="
				+ email + ", zipcode=" + zipcode + ", address=" + address
				+ ", tel=" + tel + ", mobile=" + mobile + ", signBuilding="
				+ signBuilding + ", bestTime=" + bestTime + ", oldMobile="
				+ oldMobile + ", oldTel=" + oldTel + ", shippingId="
				+ shippingId + ", shippingCode=" + shippingCode
				+ ", depotCode=" + depotCode + ", invoiceNo=" + invoiceNo
				+ ", postscript=" + postscript + ", InvPayee=" + InvPayee
				+ ", invContent=" + invContent + ", paySn=" + paySn
				+ ", orderOutSn=" + orderOutSn + ", shippingFee=" + shippingFee
				+ ", payTotalFee=" + payTotalFee + ", payId=" + payId
				+ ", source=" + source + "]";
	}
}
