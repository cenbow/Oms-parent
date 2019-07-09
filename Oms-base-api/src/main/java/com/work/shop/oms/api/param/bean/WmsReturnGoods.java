package com.work.shop.oms.api.param.bean;

import java.io.Serializable;

public class WmsReturnGoods implements Serializable {

    private static final long serialVersionUID = -7361803583888829862L;
    
    private Long id ;//商品id
    
    private String customCode;//商品11位码
    
    private String barcode;//商品13位码（条形码）
    
    private int goodsReturnNumber;//退单商品数量
    
    private int prodScanNum;//实际扫描数量
    
    private String remark;//备注（次品，污损品）
    
    private String relatingReturnSn;//关联退单SN
    
    private Double settlementPrice;//财务价格
    
    private String seller;//供应商编码
    
    private int goodsMark;//商品标示：0、美邦商品；1、第三方商品
    
    public Double getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(Double settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public String getRelatingReturnSn() {
        return relatingReturnSn;
    }

    public void setRelatingReturnSn(String relatingReturnSn) {
        this.relatingReturnSn = relatingReturnSn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getProdScanNum() {
        return prodScanNum;
    }

    public void setProdScanNum(int prodScanNum) {
        this.prodScanNum = prodScanNum;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getGoodsReturnNumber() {
        return goodsReturnNumber;
    }

    public void setGoodsReturnNumber(int goodsReturnNumber) {
        this.goodsReturnNumber = goodsReturnNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getGoodsMark() {
        return goodsMark;
    }

    public void setGoodsMark(int goodsMark) {
        this.goodsMark = goodsMark;
    }
    
    
    
}
