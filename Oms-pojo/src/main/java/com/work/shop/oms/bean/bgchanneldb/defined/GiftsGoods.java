package com.work.shop.oms.bean.bgchanneldb.defined;


public class GiftsGoods {
    private Integer id;
    
    private String promCode;

    private String promDetailsCode;
    
    private Integer giftsPriority;
    
    private String giftsGoodsSn;

    private Integer giftsGoodsCount; //赠送量

    private Integer limitCount; //赠送量
    
    private String  limitGoodsSn;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPromCode() {
		return promCode;
	}

	public void setPromCode(String promCode) {
		this.promCode = promCode;
	}

	public String getPromDetailsCode() {
		return promDetailsCode;
	}

	public void setPromDetailsCode(String promDetailsCode) {
		this.promDetailsCode = promDetailsCode;
	}

	public String getGiftsGoodsSn() {
		return giftsGoodsSn;
	}

	public void setGiftsGoodsSn(String giftsGoodsSn) {
		this.giftsGoodsSn = giftsGoodsSn;
	}

	public Integer getGiftsGoodsCount() {
		return giftsGoodsCount;
	}

	public void setGiftsGoodsCount(Integer giftsGoodsCount) {
		this.giftsGoodsCount = giftsGoodsCount;
	}

	public Integer getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}

	public String getLimitGoodsSn() {
		return limitGoodsSn;
	}

	public void setLimitGoodsSn(String limitGoodsSn) {
		this.limitGoodsSn = limitGoodsSn;
	}

	public Integer getGiftsPriority() {
		return giftsPriority;
	}

	public void setGiftsPriority(Integer giftsPriority) {
		this.giftsPriority = giftsPriority;
	}
    
}
