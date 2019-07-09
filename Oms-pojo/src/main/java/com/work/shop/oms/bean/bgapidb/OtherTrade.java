package com.work.shop.oms.bean.bgapidb;


import java.util.Date;
import java.text.SimpleDateFormat;

import com.work.shop.oms.bean.bgapidb.defined.BaseTrade;

public class OtherTrade extends BaseTrade {
    private String orderId;

    private String province;

    private String city;

    private String county;

    private String district;

    private String address;

    private String goodsNum;

    private String orderStatus;

    private String payStatus;

    private String totalFee;

    private String ordersDiscount;

    private String shippingFee;

    private String payment;

    private String invoiceType;

    private String invoiceName;

    private String sellerRemark;

    private String remark;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private Date createTime;

    private Date payTime;

    private Date modifiedTime;

    private Integer isOs;

    private Date addTime;

    private String channelCode;

    private String channelName;

    private Integer downloadFinish;

    private String outerOrderSn;

    private String orderErrorMsg;

    private String billNo;

    private String payWay;

    private String bgUserId;

    private Byte isOccupy;

    private String shippingCode;

    private String wayPaymentFreight;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum == null ? null : goodsNum.trim();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus == null ? null : payStatus.trim();
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee == null ? null : totalFee.trim();
    }

    public String getOrdersDiscount() {
        return ordersDiscount;
    }

    public void setOrdersDiscount(String ordersDiscount) {
        this.ordersDiscount = ordersDiscount == null ? null : ordersDiscount.trim();
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee == null ? null : shippingFee.trim();
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment == null ? null : payment.trim();
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType == null ? null : invoiceType.trim();
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName == null ? null : invoiceName.trim();
    }

    public String getSellerRemark() {
        return sellerRemark;
    }

    public void setSellerRemark(String sellerRemark) {
        this.sellerRemark = sellerRemark == null ? null : sellerRemark.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
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
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName == null ? null : channelName.trim();
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
        this.outerOrderSn = outerOrderSn == null ? null : outerOrderSn.trim();
    }

    public String getOrderErrorMsg() {
        return orderErrorMsg;
    }

    public void setOrderErrorMsg(String orderErrorMsg) {
        this.orderErrorMsg = orderErrorMsg == null ? null : orderErrorMsg.trim();
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay == null ? null : payWay.trim();
    }

    public String getBgUserId() {
        return bgUserId;
    }

    public void setBgUserId(String bgUserId) {
        this.bgUserId = bgUserId == null ? null : bgUserId.trim();
    }

    public Byte getIsOccupy() {
        return isOccupy;
    }

    public void setIsOccupy(Byte isOccupy) {
        this.isOccupy = isOccupy;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode == null ? null : shippingCode.trim();
    }

    public String getWayPaymentFreight() {
        return wayPaymentFreight;
    }

    public void setWayPaymentFreight(String wayPaymentFreight) {
        this.wayPaymentFreight = wayPaymentFreight == null ? null : wayPaymentFreight.trim();
    }
    
    @Override
	public String tradeId() {
		return orderId;
	}

	@Override
	public String getLastModefiedTimeForManager() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(modifiedTime);
	}
}