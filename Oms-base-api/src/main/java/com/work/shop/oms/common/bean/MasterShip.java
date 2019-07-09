package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单配送信息
 * @author QuYachu
 */
public class MasterShip implements Serializable{

	private static final long serialVersionUID = -6853836239893462460L;

	/**
	 * 配送方式(配送编码)
	 */
	private String shippingCode;

	/**
	 * 收货人姓名
	 */
	private String consignee;

	/**
	 * 收货人国家
	 */
	private String country;

	/**
	 * 收货人省份
	 */
	private String province;

	/**
	 * 收货人城市
	 */
	private String city;

	/**
	 * 收货人区县
	 */
	private String district;
	
	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 收货人详细地址
	 */
	private String address;

	/**
	 * 收货人电话
	 */
	private String tel;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 收货人电子邮件
	 */
	private String email;

	/**
	 * 收货人邮政编码
	 */
	private String zipcode;

	/**
	 * 最佳送货时间
	 */
	private String bestTime;

	/**
	 * 标志性建筑
	 */
	private String signBuilding;
	
	private String cardMessage;

	private Byte deliveryType;
	
	private Short shippingDays;

	private Byte directRange;
	
	private String shippingRequire;

	/**
	 * 支付方式  0. 不限 1.POS机支付; 2.现金支付;
	 */
	private Byte chargeType;

	/**
	 * 自提点代码
	 */
	private String cacCode;

	/**
	 * 发货仓编码
	 */
	private String depotCode;

	/**
	 * 发货仓库地区ID
	 */
	private Short regionId;

	/**
	 * 配送时间
	 */
	private Date rangeTime;

	/**
	 * 购买商品信息(数组单个元素结构参照：g_list定义)
	 */
	private List<MasterGoods> goodsList = new ArrayList<MasterGoods>();

	/**
	 * 购买商品信息(数组单个元素结构参照：g_list定义)
	 */
	private List<GoodsCardInfo> cardInfos = new ArrayList<GoodsCardInfo>();

	/**
	 * 运费付款方式 01:到付;02:自付 默认02
	 */
	private String wayPaymentFreight;

	/**
	 * 用户身份证号码
	 */
	private String userCardNo;

	/**
	 * 用户身份证姓名
	 */
	private String userCardName;

	/**
	 * 店长省份编码
	 */
	private String provinceCode;

	/**
	 * 店长市编码
	 */
	private String cityCode;

	/**
	 * 店长区县编码
	 */
	private String districtCode;

	/**
	 * 店长地区编码
	 */
	private String areaCode;

	/**
	 * 店长收货地址
	 */
	private String shippingAddress;

	/**
	 * 经度
	 */
	private Double longitude;

	/**
	 * 纬度
	 */
	private Double latitude;

	/**
	 * 配送时间范围
	 */
	private String deliveryTime;

	/**
	 * 发票收货人姓名
	 */
	private String invConsignee;

	/**
	 * 发票收货人手机号码
	 */
	private String invMobile;

	/**
	 * 发票地址
	 */
	private String invAddress;

	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getCountry() {
		return country == null ? "1" : country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel==null?"":tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile==null?"":mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getZipcode() {
		return zipcode==null?"":zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getEmail() {
		return email==null?"":email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBestTime() {
		return bestTime==null?"":bestTime;
	}

	public void setBestTime(String bestTime) {
		this.bestTime = bestTime;
	}

	public String getSignBuilding() {
		return signBuilding==null?"":signBuilding;
	}

	public void setSignBuilding(String signBuilding) {
		this.signBuilding = signBuilding;
	}

	public List<MasterGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<MasterGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public String getDistrict() {
		return district==null?"0":district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCacCode() {
		return cacCode==null?"":cacCode;
	}

	public void setCacCode(String cacCode) {
		this.cacCode = cacCode;
	}

	public Byte getChargeType() {
		return chargeType==null?0:chargeType;
	}

	public void setChargeType(Byte chargeType) {
		this.chargeType = chargeType;
	}

	public Byte getDeliveryType() {
		return deliveryType==null?0:deliveryType;
	}

	public void setDeliveryType(Byte deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Byte getDirectRange() {
		return directRange==null?0:directRange;
	}

	public void setDirectRange(Byte directRange) {
		this.directRange = directRange;
	}

	public String getShippingRequire() {
		return shippingRequire==null?"":shippingRequire;
	}

	public void setShippingRequire(String shippingRequire) {
		this.shippingRequire = shippingRequire;
	}

	public Short getShippingDays() {
		return shippingDays==null?0:shippingDays;
	}

	public void setShippingDays(Short shippingDays) {
		this.shippingDays = shippingDays;
	}

	public String getCardMessage() {
		return cardMessage==null?"":cardMessage;
	}

	public void setCardMessage(String cardMessage) {
		this.cardMessage = cardMessage;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public Short getRegionId() {
		return regionId;
	}

	public void setRegionId(Short regionId) {
		this.regionId = regionId;
	}

	public Date getRangeTime() {
		return rangeTime;
	}

	public void setRangeTime(Date rangeTime) {
		this.rangeTime = rangeTime;
	}

	public String getWayPaymentFreight() {
		return wayPaymentFreight == null ? "02" : wayPaymentFreight;
	}

	public void setWayPaymentFreight(String wayPaymentFreight) {
		this.wayPaymentFreight = wayPaymentFreight;
	}

	public List<GoodsCardInfo> getCardInfos() {
		return cardInfos;
	}

	public void setCardInfos(List<GoodsCardInfo> cardInfos) {
		this.cardInfos = cardInfos;
	}

	public String getUserCardNo() {
		return userCardNo;
	}

	public void setUserCardNo(String userCardNo) {
		this.userCardNo = userCardNo;
	}

	public String getUserCardName() {
		return userCardName;
	}

	public void setUserCardName(String userCardName) {
		this.userCardName = userCardName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

    public String getInvConsignee() {
        return invConsignee;
    }

    public void setInvConsignee(String invConsignee) {
        this.invConsignee = invConsignee;
    }

    public String getInvMobile() {
        return invMobile;
    }

    public void setInvMobile(String invMobile) {
        this.invMobile = invMobile;
    }

    public String getInvAddress() {
        return invAddress;
    }

    public void setInvAddress(String invAddress) {
        this.invAddress = invAddress;
    }
}
