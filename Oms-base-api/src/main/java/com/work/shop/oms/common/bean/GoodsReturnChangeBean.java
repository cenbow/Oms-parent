package com.work.shop.oms.common.bean;

import java.io.Serializable;

import com.work.shop.oms.bean.GoodsReturnChange;



public class GoodsReturnChangeBean extends GoodsReturnChange implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2928864060529284811L;
	private Integer stReturnSum;
	private Integer enReturnSum;
	private String startTime;
	private String endTime;

	/**
	 * newData三个月以内数据
	 * historyData历史数据
	 */
	private String listDataType;
	public Integer getStReturnSum() {
		return stReturnSum;
	}
	public void setStReturnSum(Integer stReturnSum) {
		this.stReturnSum = stReturnSum;
	}
	public Integer getEnReturnSum() {
		return enReturnSum;
	}
	public void setEnReturnSum(Integer enReturnSum) {
		this.enReturnSum = enReturnSum;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getListDataType() {
		return listDataType;
	}
	public void setListDataType(String listDataType) {
		this.listDataType = listDataType;
	}
	
	private String returnTypeStr;
	private String reasonStr;
	private String redemptionStr;
	private String tagTypeStr;
	private String exteriorTypeStr;
	private String giftTypeStr;
	private String mobileStr;
	private String statusStr;
	private String returnSn;
	private String returnPaySn;
	private Integer transType;
	private String transTypeStr;
	private String channelName;
	private String channelType;
	private Double totalFee;
	private Integer questionStatus;
	private String goodsName;
	private Double transactionPrice;
	
	
	public String getReturnTypeStr() {
		switch (getReturnType()) {
		case 1: return "退货";
		case 2: return "换货";
		case 3: return "退款";
		case 4: return "额外退款单";
		default:return "";
		}
	}
	public String getReasonStr() {
		switch (getReason()) {
		case 1: return "商品质量不过关";
		case 2: return "商品在配送中损坏";
		case 3: return "商品与描述不符";
		case 4: return "尚未收到商品";
		case 5: return "其他";
		default:return "";
		}
	}
	public String getRedemptionStr() {
		switch (getRedemption()) {
		case 1: return "换购本商品其他尺码或颜色";
		case 2: return "换购其他商品";
		default:return "";
		}
	}
	public String getTagTypeStr() {
		switch (getTagType()) {
		case 1: return "吊牌完好";
		case 2: return "吊牌破损";
		case 3: return "无吊牌";
		default:return "";
		}
	}
	public String getExteriorTypeStr() {
		switch (getExteriorType()) {
		case 1: return "外观完好";
		case 2: return "外观有破损";
		case 3: return "外观有污渍";
		default:return "";
		}
	}
	public String getGiftTypeStr() {
		switch (getGiftType()) {
		case 1: return "赠品完好";
		case 2: return "赠品破损";
		case 3: return "赠品不全";
		case 4: return "未收到赠品";
		default:return "";
		}
	}
	public String getMobileStr() {
		String Mobile="";
		if(null!=getContactMobile()){
			Mobile+=getContactMobile();
			if(null!=getContactTelephone()){
				Mobile+="/";
			}
			} 
		if(null!=getContactTelephoneArea())Mobile+=getContactTelephoneArea();
		if(null!=getContactTelephone())Mobile+=getContactTelephone();
		if(null!=getContactTelephoneBranch())Mobile+="-"+getContactTelephoneBranch();
		return Mobile;
	}
	public String getStatusStr() {
		switch (getStatus()) {
		case 0: return "已取消";
		case 1: return "待沟通";
		case 2: return "已完成";
		case 3: return "待处理";
		default:return "";
		}
	}
	public String getReturnSn() {
		return returnSn;
	}
	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}
	public String getReturnPaySn() {
		return returnPaySn;
	}
	public void setReturnPaySn(String returnPaySn) {
		this.returnPaySn = returnPaySn;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Double getTotalFee() 
	{
		return totalFee;
	}
	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
	public String getTransTypeStr() {
		switch (getTransType()) {
		case 1: return "款到发货";
		case 2: return "货到付款";
		case 3: return "担保交易";
		default:return "";
		}
	}
	public Integer getQuestionStatus() {
		return questionStatus;
	}
	public void setQuestionStatus(Integer questionStatus) {
		this.questionStatus = questionStatus;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Double getTransactionPrice() {
		return transactionPrice;
	}
	public void setTransactionPrice(Double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	
 

}
