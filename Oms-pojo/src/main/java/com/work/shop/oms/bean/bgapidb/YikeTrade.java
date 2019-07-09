package com.work.shop.oms.bean.bgapidb;

import java.math.BigDecimal;
import java.util.Date;

import com.ibm.icu.text.SimpleDateFormat;
import com.work.shop.oms.bean.bgapidb.defined.BaseTrade;

public class YikeTrade extends BaseTrade{
    private String code;

    private String shopcode;

    private Integer totalQty;

    private BigDecimal totalMoney;

    private BigDecimal discountMoney;

    private BigDecimal discountAmount;

    private Integer returnQuantity;

    private String orderPreShiptime;

    private BigDecimal payAmount;

    private BigDecimal usecouponValue;

    private BigDecimal rewardFee;

    private Integer useBonus;

    private String isCod;

    private BigDecimal codServiceFee;

    private String buyerCode;

    private String storeCode;

    private String buyerEmail;

    private String recvConsignee;

    private String recvMobile;

    private String recvTel;

    private String province;

    private String city;

    private String county;

    private String address;

    private String orderStatus;

    private String buyerRemark;

    private String orderRemark;

    private String serviceSalercode;

    private String serviceSalername;

    private String serviceChannelcode;

    private String sellerCode;

    private String sellerSysShopcode;

    private String invoiceType;

    private String invoiceName;

    private Date createTime;

    private Date payTime;

    private Date shipTime;

    private Date endTime;

    private Date modifiedTime;

    private Integer isOs;

    private Date addTime;

    private String channelCode;

    private String channelName;

    private Integer downloadFinish;

    private String outerOrderSn;

    private String orderErrorMsg;

    private Byte isOccupy;

    private String orderType;

    private Date orderTime;

    private Date orderPreShipTime;

    private String isPayed;

    private Date finishTime;

    private BigDecimal expressFee;

    private BigDecimal expressCost;

    private String buyerOldCode;

    private String buyerNick;

    private String expressFeeType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getShopcode() {
        return shopcode;
    }

    public void setShopcode(String shopcode) {
        this.shopcode = shopcode == null ? null : shopcode.trim();
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(BigDecimal discountMoney) {
        this.discountMoney = discountMoney;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(Integer returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public String getOrderPreShiptime() {
        return orderPreShiptime;
    }

    public void setOrderPreShiptime(String orderPreShiptime) {
        this.orderPreShiptime = orderPreShiptime == null ? null : orderPreShiptime.trim();
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getUsecouponValue() {
        return usecouponValue;
    }

    public void setUsecouponValue(BigDecimal usecouponValue) {
        this.usecouponValue = usecouponValue;
    }

    public BigDecimal getRewardFee() {
        return rewardFee;
    }

    public void setRewardFee(BigDecimal rewardFee) {
        this.rewardFee = rewardFee;
    }

    public Integer getUseBonus() {
        return useBonus;
    }

    public void setUseBonus(Integer useBonus) {
        this.useBonus = useBonus;
    }

    public String getIsCod() {
        return isCod;
    }

    public void setIsCod(String isCod) {
        this.isCod = isCod == null ? null : isCod.trim();
    }

    public BigDecimal getCodServiceFee() {
        return codServiceFee;
    }

    public void setCodServiceFee(BigDecimal codServiceFee) {
        this.codServiceFee = codServiceFee;
    }

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode == null ? null : buyerCode.trim();
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode == null ? null : storeCode.trim();
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail == null ? null : buyerEmail.trim();
    }

    public String getRecvConsignee() {
        return recvConsignee;
    }

    public void setRecvConsignee(String recvConsignee) {
        this.recvConsignee = recvConsignee == null ? null : recvConsignee.trim();
    }

    public String getRecvMobile() {
        return recvMobile;
    }

    public void setRecvMobile(String recvMobile) {
        this.recvMobile = recvMobile == null ? null : recvMobile.trim();
    }

    public String getRecvTel() {
        return recvTel;
    }

    public void setRecvTel(String recvTel) {
        this.recvTel = recvTel == null ? null : recvTel.trim();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark == null ? null : buyerRemark.trim();
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark == null ? null : orderRemark.trim();
    }

    public String getServiceSalercode() {
        return serviceSalercode;
    }

    public void setServiceSalercode(String serviceSalercode) {
        this.serviceSalercode = serviceSalercode == null ? null : serviceSalercode.trim();
    }

    public String getServiceSalername() {
        return serviceSalername;
    }

    public void setServiceSalername(String serviceSalername) {
        this.serviceSalername = serviceSalername == null ? null : serviceSalername.trim();
    }

    public String getServiceChannelcode() {
        return serviceChannelcode;
    }

    public void setServiceChannelcode(String serviceChannelcode) {
        this.serviceChannelcode = serviceChannelcode == null ? null : serviceChannelcode.trim();
    }

    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode == null ? null : sellerCode.trim();
    }

    public String getSellerSysShopcode() {
        return sellerSysShopcode;
    }

    public void setSellerSysShopcode(String sellerSysShopcode) {
        this.sellerSysShopcode = sellerSysShopcode == null ? null : sellerSysShopcode.trim();
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

    public Date getShipTime() {
        return shipTime;
    }

    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public Byte getIsOccupy() {
        return isOccupy;
    }

    public void setIsOccupy(Byte isOccupy) {
        this.isOccupy = isOccupy;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getOrderPreShipTime() {
        return orderPreShipTime;
    }

    public void setOrderPreShipTime(Date orderPreShipTime) {
        this.orderPreShipTime = orderPreShipTime;
    }

    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed == null ? null : isPayed.trim();
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(BigDecimal expressFee) {
        this.expressFee = expressFee;
    }

    public BigDecimal getExpressCost() {
        return expressCost;
    }

    public void setExpressCost(BigDecimal expressCost) {
        this.expressCost = expressCost;
    }

    public String getBuyerOldCode() {
        return buyerOldCode;
    }

    public void setBuyerOldCode(String buyerOldCode) {
        this.buyerOldCode = buyerOldCode == null ? null : buyerOldCode.trim();
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick == null ? null : buyerNick.trim();
    }

    public String getExpressFeeType() {
        return expressFeeType;
    }

    public void setExpressFeeType(String expressFeeType) {
        this.expressFeeType = expressFeeType == null ? null : expressFeeType.trim();
    }
	@Override
	public String tradeId() {
		return code;
	}

	@Override
	public String getLastModefiedTimeForManager() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTime);
	}
}