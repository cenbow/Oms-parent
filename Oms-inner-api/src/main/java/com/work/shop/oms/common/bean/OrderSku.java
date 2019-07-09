package com.work.shop.oms.common.bean;

import java.io.Serializable;

public class OrderSku implements Serializable{

	/**外部渠道补全sku bean
	 * 
	 */
	private static final long serialVersionUID = -8336995760777764262L;
	
	//外部订单号
	private String orderOutSn;
	//外部商品id
	private String outSkuSn;
	//内部商品11位码
	private String skuSn;
	//外部商品名称
	private String outSkuName;
	//商品数量
	private int goodsNum;
	//平台类型（仅用于查询）
	private String channelType;
    //开始条数
	private int pageStart;
	//返回条数
	private int PageSize;
	public String getOrderOutSn() {
		return orderOutSn;
	}
	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}
	public String getOutSkuSn() {
		return outSkuSn;
	}
	public void setOutSkuSn(String outSkuSn) {
		this.outSkuSn = outSkuSn;
	}
	public String getSkuSn() {
		return skuSn;
	}
	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}
	public String getOutSkuName() {
		return outSkuName;
	}
	public void setOutSkuName(String outSkuName) {
		this.outSkuName = outSkuName;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public int getPageStart() {
		return pageStart;
	}
	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}
	public int getPageSize() {
		return PageSize;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}

}
