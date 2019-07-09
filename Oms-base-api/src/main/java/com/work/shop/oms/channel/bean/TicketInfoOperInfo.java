package com.work.shop.oms.channel.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 调整单信息
 * @author QuYachu
 *
 */
public class TicketInfoOperInfo implements Serializable {

	private static final long serialVersionUID = 8631880036175322912L;

	private Integer id;
	
	private String ticketCode; // 调整单号
	
	private String goodsSn; // 商品款号
	
	private String channelGoodssn; // 渠道商品款号
	
	private String goodsTitle; // 商品名称
	
	private BigDecimal safePrice; // 商品保护价格
	
	private BigDecimal newPrice; // 商品新价格
	
	private String sellingInfo; // 卖点信息
	
	private String onSellStatus; // '0:下架，1上架'
	
	private String hasDiscount; // 支持会员打折。可选值:true,false
	
	private String freightPayer; // 运费承担方式。运费承担方式。可选值:seller（卖家承担）,buyer(买家承担)
	
	private String isOffline; // 是否是线下商品。1：线上商品, 2：线上或线下商品, 3：线下商品
	
	private Byte isExcuted; // 是否执行成功。0成功，1 不成功
	
	private String isSyncStock;
	
	private String urlWords;
	
	private String url;
	
	private Byte isOnlineOffline;
	
	private String showName;

	public Integer getId() {
		return id;
	}

	public String getTicketCode() {
		return ticketCode;
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

	public BigDecimal getSafePrice() {
		return safePrice;
	}

	public BigDecimal getNewPrice() {
		return newPrice;
	}

	public String getSellingInfo() {
		return sellingInfo;
	}

	public String getOnSellStatus() {
		return onSellStatus;
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

	public Byte getIsExcuted() {
		return isExcuted;
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

	public String getShowName() {
		return showName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
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

	public void setSafePrice(BigDecimal safePrice) {
		this.safePrice = safePrice;
	}

	public void setNewPrice(BigDecimal newPrice) {
		this.newPrice = newPrice;
	}

	public void setSellingInfo(String sellingInfo) {
		this.sellingInfo = sellingInfo;
	}

	public void setOnSellStatus(String onSellStatus) {
		this.onSellStatus = onSellStatus;
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

	public void setIsExcuted(Byte isExcuted) {
		this.isExcuted = isExcuted;
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

	public void setShowName(String showName) {
		this.showName = showName;
	}
}
