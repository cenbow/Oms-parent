package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsReturnChangeDetailInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 11位码
     */
    private String customCode;

    /**
     * 商品的扩展属性，common：正品；gift：赠品；group：套装；
     prize：奖品；c2m：C2M订制，c2b：C2B订制
     */
    private String extensionCode;

    /**
     * 换购选择类型，1：换购本商品其他尺码或颜色；2：换购其他商品
     */
    private Byte redemption;

    /**
     * 吊牌情况，1：吊牌完好；2：吊牌破损；3：无吊牌
     */
    private Integer tagType = 0;

    /**
     * 外观情况，1：外观完好；2：外观有破损；3：外观有污渍
     */
    private Integer exteriorType = 0;

    /**
     * 赠品情况，1：赠品完好；2：赠品破损；3：赠品不全；4：未收到赠品
     */
    private Integer giftType = 0;

    /**
     *
     */
    private String remark;

    /**
     * 退换数量
     */
    private Integer returnSum;

    /**
     * 商品打折券,":"分割
     */
    private String useCard = "";

    /**
     * 商品分摊红包金额
     */
    private Double shareBonus = 0.00;

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public String getExtensionCode() {
        return extensionCode;
    }

    public void setExtensionCode(String extensionCode) {
        this.extensionCode = extensionCode;
    }

    public Byte getRedemption() {
        return redemption;
    }

    public void setRedemption(Byte redemption) {
        this.redemption = redemption;
    }

    public Integer getGiftType() {
        return giftType;
    }

    public void setGiftType(Integer giftType) {
        this.giftType = giftType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getReturnSum() {
        return returnSum;
    }

    public void setReturnSum(Integer returnSum) {
        this.returnSum = returnSum;
    }

    public String getUseCard() {
        return useCard;
    }

    public void setUseCard(String useCard) {
        this.useCard = useCard;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    public Integer getExteriorType() {
        return exteriorType;
    }

    public void setExteriorType(Integer exteriorType) {
        this.exteriorType = exteriorType;
    }

    public Double getShareBonus() {
        return shareBonus;
    }

    public void setShareBonus(Double shareBonus) {
        this.shareBonus = shareBonus;
    }
}
