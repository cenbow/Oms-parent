package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GoodsReturnPageInfo implements Serializable{
	
	/**用于平台前台退换货申请单列表
	 * 
	 */
	private static final long serialVersionUID = -2978204092592106678L;
	private String orderSn;
	private String goodsSn;
	private String skuSn;
	private int goodsNum;
	private String createTime;
	private String orderCreateTime;
	private String goodsName;
	private String goodsColor;
	private String goodsSize;
	private String goodsImgUrl;
	private String goodsReturnChangeSn;
	private double goodsPrice;
	private int returnType;
	private int status;
	private String channelCode;//店铺编码
 	private String channelName;//店铺名称
 	private int goodsReturnChangeStatus;//商品状态(1、退货中;2、换货中;3、退货完成;4、换货完成)申请单列表使用

    /**
     * 退换原因，1：商品质量不过关；2：商品在配送中损坏；3：商品与描述不符；4：尚未收到商品；5：其他（请具体说明）
     */
    private Integer reason;

    private String reasonStr;

    private List<GoodsReturnChangeDetailBean> details;
 	
	public int getGoodsReturnChangeStatus() {
		return goodsReturnChangeStatus;
	}
	public void setGoodsReturnChangeStatus(int goodsReturnChangeStatus) {
		this.goodsReturnChangeStatus = goodsReturnChangeStatus;
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
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsColor() {
		return goodsColor;
	}
	public void setGoodsColor(String goodsColor) {
		this.goodsColor = goodsColor;
	}
	public String getGoodsSize() {
		return goodsSize;
	}
	public void setGoodsSize(String goodsSize) {
		this.goodsSize = goodsSize;
	}
	public String getGoodsReturnChangeSn() {
		return goodsReturnChangeSn;
	}
	public void setGoodsReturnChangeSn(String goodsReturnChangeSn) {
		this.goodsReturnChangeSn = goodsReturnChangeSn;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getGoodsImgUrl() {
		return goodsImgUrl;
	}
	public void setGoodsImgUrl(String goodsImgUrl) {
		this.goodsImgUrl = goodsImgUrl;
	}
	public String getSkuSn() {
		return skuSn;
	}
	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}
	public String getGoodsSn() {
		return goodsSn;
	}
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public int getReturnType() {
		return returnType;
	}
	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

    public List<GoodsReturnChangeDetailBean> getDetails() {
        return details;
    }

    public void setDetails(List<GoodsReturnChangeDetailBean> details) {
        this.details = details;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public String getReasonStr() {
        if (getReason() == null) {
            return "";
        }
        switch (getReason()) {
            case 1: return "商品质量不过关";
            case 2: return "商品在配送中损坏";
            case 3: return "商品与描述不符";
            case 4: return "尚未收到商品";
            case 5: return "其他";
            default:return "";
        }
    }

    public void setReasonStr(String reasonStr) {
        this.reasonStr = reasonStr;
    }
}
