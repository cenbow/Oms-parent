package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退单商品信息
 * @author QuYachu
 */
public class ReturnGoods implements Serializable {

	private static final long serialVersionUID = -611201999104911769L;

	/**
	 * 商品id
	 */
	private Long id;

	/**
	 * 商品名称
	 */
	private String goodsName;

    /**
     * 退货金额(财务价格)
     */
	private Double settlementPrice;

    /**
     * 成本价
     */
	private BigDecimal costPrice;

    /**
     * 进项税
     */
	private BigDecimal inputTax;

	/**
	 * 商品sku编码
	 */
	private String customCode;

	/**
	 * 商家货号
	 */
	private String goodsSn;

	/**
	 * 商品数量
	 */
	private int goodsReturnNumber;

	/**
	 * 商品图片
	 */
	private String goodsThumb;

	/**
	 * 颜色
	 */
	private String goodsColorName;

	/**
	 * 尺码
	 */
	private String goodsSizeName;

	/**
	 * 是否收到货 （0 否  1 是）
	 */
	private Integer isGoodReceived;

	/**
	 * 是否入库 （0未入库 1已入库 2待入库）
	 */
	private Integer checkinStatus;

	/**
	 * 退单质检状态 （0质检不通过、1质检通过）
	 */
	private Integer qualityStatus;

	/**
	 * 国标码（条形码，扫描出库时使用）
	 */
	private String barcode;

	/**
	 * 商家货号SKU
	 */
	private String businessBarcode;
	
	public String getBusinessBarcode() {
		return businessBarcode;
	}

	public void setBusinessBarcode(String businessBarcode) {
		this.businessBarcode = businessBarcode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getIsGoodReceived() {
		return isGoodReceived;
	}

	public void setIsGoodReceived(Integer isGoodReceived) {
		this.isGoodReceived = isGoodReceived;
	}

	public Integer getCheckinStatus() {
		return checkinStatus;
	}

	public void setCheckinStatus(Integer checkinStatus) {
		this.checkinStatus = checkinStatus;
	}

	public Integer getQualityStatus() {
		return qualityStatus;
	}

	public void setQualityStatus(Integer qualityStatus) {
		this.qualityStatus = qualityStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGoodsThumb() {
		return goodsThumb;
	}

	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb;
	}

	public String getGoodsColorName() {
		return goodsColorName;
	}

	public void setGoodsColorName(String goodsColorName) {
		this.goodsColorName = goodsColorName;
	}

	public String getGoodsSizeName() {
		return goodsSizeName;
	}

	public void setGoodsSizeName(String goodsSizeName) {
		this.goodsSizeName = goodsSizeName;
	}

	public int getGoodsReturnNumber() {
		return goodsReturnNumber;
	}

	public void setGoodsReturnNumber(int goodsReturnNumber) {
		this.goodsReturnNumber = goodsReturnNumber;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Double getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(Double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getInputTax() {
        return inputTax;
    }

    public void setInputTax(BigDecimal inputTax) {
        this.inputTax = inputTax;
    }
}
