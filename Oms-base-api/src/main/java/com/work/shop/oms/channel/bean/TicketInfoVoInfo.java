package com.work.shop.oms.channel.bean;

import java.io.Serializable;
import java.util.Date;

public class TicketInfoVoInfo implements Serializable {

	private static final long serialVersionUID = 5187674381469170307L;

	private int id;
	
	private String ticketCode;// 调整单编号
	
	private String channelCode; // 渠道编码
	
	private Byte ticketType = -1;// 单据类型,默认为-1,条件会排除-1
	
	private String shopCode;// 经营店铺名称
	
	private String goodsSn;// 商品款号
	
	private String channelGoodssn;// 渠道商品款号
	
	private String goodsTitle;// 商品名称
	
	private String onSellStatus;// 上下架状态
	
	private String safePrice;// 商品保护价格
	
	private String newPrice;// 商品新价格
	
	private String sellingInfo;// 卖点信息
	
	private String hasDiscount; // 支持会员打折
	
	private String freightPayer;// 运费承担方式
	
	private String isOffline; // 是否是线下商品
	
	private String isSyncStock; //是否需要同步库存 0不需要，1需要
	
	private String urlWords;  //带链接的广告词
	
	private String url; //广告词链接地址
	
	private Byte isOnlineOffline;  //线上线下款商品状态。1：线上款（默认值）；2：线下款；3：线上线下同款
	
	private String returnMessage;
	
	private Date requestTime;
	
	private String sku; // 商品11位码
	
	private String channelStock; // 商品渠道库存
	
	private String showName;//2017-7-4  天猫渠道  新增   商品展示标题

	public int getId() {
		return id;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public Byte getTicketType() {
		return ticketType;
	}

	public String getShopCode() {
		return shopCode;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public String getChannelGoodssn() {
		return channelGoodssn;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public String getOnSellStatus() {
		return onSellStatus;
	}

	public String getSafePrice() {
		return safePrice;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public String getSellingInfo() {
		return sellingInfo;
	}

	public String getHasDiscount() {
		return hasDiscount;
	}

	public String getFreightPayer() {
		return freightPayer;
	}

	public String getIsOffline() {
		return isOffline;
	}

	public String getIsSyncStock() {
		return isSyncStock;
	}

	public String getUrlWords() {
		return urlWords;
	}

	public String getUrl() {
		return url;
	}

	public Byte getIsOnlineOffline() {
		return isOnlineOffline;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public String getSku() {
		return sku;
	}

	public String getChannelStock() {
		return channelStock;
	}

	public String getShowName() {
		return showName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public void setTicketType(Byte ticketType) {
		this.ticketType = ticketType;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public void setChannelGoodssn(String channelGoodssn) {
		this.channelGoodssn = channelGoodssn;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public void setOnSellStatus(String onSellStatus) {
		this.onSellStatus = onSellStatus;
	}

	public void setSafePrice(String safePrice) {
		this.safePrice = safePrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public void setSellingInfo(String sellingInfo) {
		this.sellingInfo = sellingInfo;
	}

	public void setHasDiscount(String hasDiscount) {
		this.hasDiscount = hasDiscount;
	}

	public void setFreightPayer(String freightPayer) {
		this.freightPayer = freightPayer;
	}

	public void setIsOffline(String isOffline) {
		this.isOffline = isOffline;
	}

	public void setIsSyncStock(String isSyncStock) {
		this.isSyncStock = isSyncStock;
	}

	public void setUrlWords(String urlWords) {
		this.urlWords = urlWords;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setIsOnlineOffline(Byte isOnlineOffline) {
		this.isOnlineOffline = isOnlineOffline;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public void setChannelStock(String channelStock) {
		this.channelStock = channelStock;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
}
