package com.work.shop.oms.common.bean;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class OrderGoodsUpdateInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6358534589019316929L;
	private String keyword;  
	private Long id;								// order_goods表主键
	private String depotCode;						// 配货仓单编码
	private String extensionCode;					// 商品的扩展属性
	private String newGoodsName;					// 商品名称
	private String goodsSn;							// 商品的唯一货号
	private String colorCode;						// 颜色
	private String sizeCode;						// 尺码
	private Integer addNumber;						// 添加商品的数量
	private Double goodsPrice;						// 商品价格
	private Double transactionPrice;				// 商品成交价格
	private Integer goodsNumber;					// 商品的数量
	private Integer returnGoodsNumber;				// 退商品的数量
	private Double shareBonus;						// 分摊金额
	private Integer checked = 0;					// 是否选择了以修改的红包为准（0：不是；1：是）
	private Double settlementPrice;					// 财务价格
	private Double shareSurplus;					// 余额分摊金额
	private Double realPayFee;						// 商品实际支付金额
	private Long warehId;							// 商品仓库明细表ID
	private String rcvWarehcode;					// 商品实际出货仓子仓
	private String promtDesc;						// 促销信息描述
	
	private String cardNoUseList;
	private String customCode;
	//转换后的OrderGoodsWarehouse
//	private List<OrderGoodsWarehouse> orderGoodsWarehouseList = new ArrayList<OrderGoodsWarehouse>();

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getCardNoUseList() {
		return cardNoUseList;
	}

	public void setCardNoUseList(String cardNoUseList) {
		this.cardNoUseList = cardNoUseList;
	}

	public String getPromtDesc() {
		return promtDesc;
	}

	public void setPromtDesc(String promtDesc) {
		this.promtDesc = promtDesc;
	}

	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getExtensionCode() {
		return extensionCode == null ? "" : extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

	public String getNewGoodsName() {
		return newGoodsName == null ? "" : newGoodsName.replaceAll("\"|'", "");
	}
	
	public void setNewGoodsName(String newGoodsName) {
		if(!StringUtils.isBlank(newGoodsName)){
			newGoodsName = newGoodsName.replaceAll("\"|'", "");
		}
		this.newGoodsName = newGoodsName;
	}
	
	public String getGoodsSn() {
		return goodsSn;
	}
	
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	
	public String getColorCode() {
		return colorCode;
	}
	
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	public String getSizeCode() {
		return sizeCode;
	}
	
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}
	
	public Double getGoodsPrice() {
		return goodsPrice;
	}
	
	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	
	public Double getTransactionPrice() {
		return transactionPrice;
	}
	
	public void setTransactionPrice(Double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}
	
	public Integer getGoodsNumber() {
		return goodsNumber;
	}
	
	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Long getWarehId() {
		return warehId;
	}

	public void setWarehId(Long warehId) {
		this.warehId = warehId;
	}

	public String getRcvWarehcode() {
		return rcvWarehcode;
	}

	public void setRcvWarehcode(String rcvWarehcode) {
		this.rcvWarehcode = rcvWarehcode;
	}

	public Integer getReturnGoodsNumber() {
		return returnGoodsNumber;
	}

	public void setReturnGoodsNumber(Integer returnGoodsNumber) {
		this.returnGoodsNumber = returnGoodsNumber;
	}

	public Double getShareBonus() {
		return shareBonus;
	}

	public void setShareBonus(Double shareBonus) {
		this.shareBonus = shareBonus;
	}

	public Double getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(Double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}

	public Double getShareSurplus() {
		return shareSurplus;
	}

	public void setShareSurplus(Double shareSurplus) {
		this.shareSurplus = shareSurplus;
	}

	public Double getRealPayFee() {
		return realPayFee;
	}

	public void setRealPayFee(Double realPayFee) {
		this.realPayFee = realPayFee;
	}

	public void setAddNumber(Integer addNumber) {
		this.addNumber = addNumber;
	}

	public Integer getAddNumber() {
		return addNumber;
	}
	


}