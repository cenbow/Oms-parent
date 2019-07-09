package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OtherTradeParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2015842823669618790L;
	
	
    private Integer id;

    private String orderId;//订单编号  *
    
    private String province;//地区省 *

    private String city;//地区市 *

    private String county;//地区区 *

    private String district;//地区街道

    private String address;//地区详细地址 *

    private String goodsNum;//订单商品数量 *

    private String orderStatus;//订单状态

    private String payStatus;//支付状态

    private String totalFee;//订单总金额

    private String shippingFee;//运费 *

    private String payment;//订单支付金额 *

    private String invoiceType;//发票类型

    private String invoiceName;//发票抬头

    private String sellerRemark;//卖家留言

    private String remark;//买家留言

    private String receiverName;//收货人 *

    private String receiverPhone;//收货人电话 *

    private String receiverAddress;//收货人完整地址

    private Date createTime;//订单创建时间*

    private Date payTime;//支付时间

    private Date modifiedTime;//订单更新时间

    private Integer isOs;

    private Date addTime;//订单添加时间
    
    
    private String ordersDiscount;//订单


    private String channelCode;//渠道code*

    private String channelName;//渠道名称*

    private Integer downloadFinish;

    private String outerOrderSn;

    private String orderErrorMsg;
    
    private String billNo;
    
    private String actionUser;
    
    private List<OtherOrderParam>  otherOrderList;//订单商品

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(String shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getSellerRemark() {
		return sellerRemark;
	}

	public void setSellerRemark(String sellerRemark) {
		this.sellerRemark = sellerRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Integer getIsOs() {
		return isOs;
	}

	public void setIsOs(Integer isOs) {
		this.isOs = isOs;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getDownloadFinish() {
		return downloadFinish;
	}

	public void setDownloadFinish(Integer downloadFinish) {
		this.downloadFinish = downloadFinish;
	}

	public String getOuterOrderSn() {
		return outerOrderSn;
	}

	public void setOuterOrderSn(String outerOrderSn) {
		this.outerOrderSn = outerOrderSn;
	}

	public String getOrderErrorMsg() {
		return orderErrorMsg;
	}

	public void setOrderErrorMsg(String orderErrorMsg) {
		this.orderErrorMsg = orderErrorMsg;
	}

	public String getOrdersDiscount() {
		return ordersDiscount;
	}

	public void setOrdersDiscount(String ordersDiscount) {
		this.ordersDiscount = ordersDiscount;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public List<OtherOrderParam> getOtherOrderList() {
		return otherOrderList;
	}

	public void setOtherOrderList(List<OtherOrderParam> otherOrderList) {
		this.otherOrderList = otherOrderList;
	}
    
    

}
