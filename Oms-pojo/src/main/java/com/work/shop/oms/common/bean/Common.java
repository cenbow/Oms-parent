package com.work.shop.oms.common.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import com.work.shop.oms.bean.MasterOrderInfo;



/**
 * 基本信息
 * @author
 *
 */
public class Common extends MasterOrderInfo implements Serializable {
	
/*	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimits() {
		return limits;
	}
	public void setLimits(int limits) {
		this.limits = limits;
	}*/
	private String deliveryType;//发货：1仓库，2工厂，3第三方发货
		
	public String orderSnArr;
	
	private List<String> sites;// 用户有权限站点
	
	public String getOrderSnArr() {
		return orderSnArr;
	}
	public void setOrderSnArr(String orderSnArr) {
		this.orderSnArr = orderSnArr;
	}
	private boolean  isNotQuanQiuTong;
	

	public boolean getIsNotQuanQiuTong() {
		return isNotQuanQiuTong;
	}
	public void setIsNotQuanQiuTong(boolean isNotQuanQiuTong) {
		this.isNotQuanQiuTong = isNotQuanQiuTong;
	}
	private List<String>  addList;
	
	public List<String> getAddList() {
		return addList;
	}
	public void setAddList(List<String> addList) {
		this.addList = addList;
	}
	private BigDecimal  payTotalfee;
	  
	  private Date  deliveryTime;
	
	
	public BigDecimal getPayTotalfee() {
		return payTotalfee;
	}
	public void setPayTotalfee(BigDecimal payTotalfee) {
		this.payTotalfee = payTotalfee;
	}
	private String relatingExchangeSn;
	
	public String getRelatingExchangeSn() {
		return relatingExchangeSn;
	}
	public void setRelatingExchangeSn(String relatingExchangeSn) {
		this.relatingExchangeSn = relatingExchangeSn;
	}
	private List<String> orderSnList;

	
/*	private int start = 0;												//分页 起始页
	
	private int limits = 20;	*/
	
	private List<String> orderOutSnList;
	
	public List<String> getOrderOutSnList() {
		return orderOutSnList;
	}
	public void setOrderOutSnList(List<String> orderOutSnList) {
		this.orderOutSnList = orderOutSnList;
	}
	public List<String> getOrderSnList() {
		return orderSnList;
	}
	public void setOrderSnList(List<String> orderSnList) {
		this.orderSnList = orderSnList;
	}
	private Byte shippingStatus;

	public Byte getShippingStatus() {
		return shippingStatus;
	}
	public void setShippingStatus(Byte shippingStatus) {
		this.shippingStatus = shippingStatus;
	}
	private String questionReason;

	public String getQuestionReason() {
		return questionReason;
	}
	public void setQuestionReason(String questionReason) {
		this.questionReason = questionReason;
	}
	public String getDistWarehCode() {
		return distWarehCode;
	}
	public void setDistWarehCode(String distWarehCode) {
		this.distWarehCode = distWarehCode;
	}
	private static final long serialVersionUID = -3274509214057973121L;
	
	private String bgUserLevel;
	
	private String useLevelStr = "";
	
	private String distWarehCode;  //'仓库code'
	
	private String shipCode;  //''承运商',
	

	public String getShipCode() {
		return shipCode;
	}
	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}
	private Date payTime;		 					//支付时间
	
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	private String payName;							// 支付方式
	
	private String payNote;							//付款备注
	
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public String getPayNote() {
		return payNote;
	}
	public void setPayNote(String payNote) {
		this.payNote = payNote;
	}
	private String	exportTemplateType;

	private BigDecimal settlementPrice;   //财务价格
	
	public BigDecimal getSettlementPrice() {
		return settlementPrice;
	}
	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}
	private BigDecimal shippingFee;					//配送费用
	
	private String cardNo;							// 卡号

	private BigDecimal cardMoney;					// 卡面值

	private int erpStatus;							// ERP订单状态

	private String dearTime;						// 结算时间

	private String conser;							// 购货人（下单人）

	private String userLevelName;					// 会员等级名称
	
	private String userTypeName;					// 会员类型名称

	private int userLevelDiscount;					// 会员商品折扣率

	private BigDecimal payFee;
	
	private String sort;							//排序的字段
	
	private String dir;								//升序或降序
	
	private String [] orderFroms;
	
	private String bonusId;
	
	private int isAllowCancel = 0;

	private Integer isGroup;						// 团购订单

	private Integer isAdvance;						// 预售商品

	private String shopId;							// 门店ID
	
	private String shopName;						// 门店名称
	
	private String shopTime;						// 门店接单时间

	private String channelName;						// 店铺名称

	private Short activityType;						// 活动类型
	
	private String activityTypeDesc;				// 活动类型描述
	
	private String c2bImageHearder;					// 图片库域名
	
	private String timeoutRsn;						// 订单超时原因

	private BigDecimal totalBonus;					// 订单中使用红包总金额

	private String bonusName = "";					// 使用红包名称

	private int usePartBonus = 0;					// 红包金额是否全部使用   0：是、1：否

	private BigDecimal exchangeReturnFee;			// 换货时退货入款

	private String ordertotalstatusStr;				// 订单总状态

	private String orderStatusStr;					// 订单状态
	
	private String payStatusStr;					// 支付状态
	
	private String shipStatusStr;					// 发货状态
	
	private String orderCategoryStr;				// 订单种类
	
	private String transTypeStr;					// 交易类型
	
	private String consigneeStr;					// 收货人信息
	
	private String orderTypeStr;					// 订单类型
	
	private String questionStatusStr;				// 问题单状态
	
	private String startTime;						// 开始时间
	
	private String endTime;							// 结束时间
	
	private String selectTimeType;					// 时间查询类型
	
	private Integer orderView;						// 订单是否有效
	
	private Integer  payId;							// 支付类型
	
	private String address;							// 收货详细地址
	
	private String fullAddress;						// 收货地址全称
	
	private Integer shippingId;						// 承运商
	
	private Date shippingTime;						// 发货时间
	
	private String skuSn;							// sku11位码
	
	private String goodsSn;							// 商品款号 
	
	private String listDataType;					//数据类型   newData-近三月数据   historyData-历史数据
	
	private String country;							// 国家
	
	private String province;						// 省
	
	private String city;							// 市
	
	private String district;						// 区
	
	private String street;							// 街道
	
	private String zipcode;							// 收货人的邮政编码
	
	private String orderFromFirst;					// 订单来源第一级选择
	
	private String orderFromSec;					// 订单来源第二级选择
	
	private String lockUserName;					// 订单锁定人
	
	private Byte reviveStt;							// 是否是复活订单 0 不是 1 是
	
	private Integer isShop;							// 是否可被门店获取 0 不可 1 可被获取
	
	private int osqStatus;							// 问题单状态
	
	private int logqStatus;							// 物流问题单状态
	
	private String logqDesc;						// 物流问题单原因
	
	private String returnSn;						// 订单关联所有退单列表
	
	private String invoiceNo;						// 快递单号
	
	private String bestTime;

	private String signBuilding;
	
	private String shippingName;

	private Integer goodsNum;						// 商品数量
	
	public String getSignBuilding() {
		return signBuilding;
	}
	public void setSignBuilding(String signBuilding) {
		this.signBuilding = signBuilding;
	}
	public String getBestTime() {
		return bestTime;
	}
	public void setBestTime(String bestTime) {
		this.bestTime = bestTime;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public int getErpStatus() {
		return erpStatus;
	}
	public void setErpStatus(int erpStatus) {
		this.erpStatus = erpStatus;
	}

	public String getDearTime() {
		return dearTime;
	}
	public void setDearTime(String dearTime) {
		this.dearTime = dearTime;
	}
	public String getConser() {
		return conser;
	}
	public void setConser(String conser) {
		this.conser = conser;
	}
	public String getUserLevelName() {
		return userLevelName;
	}
	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}
	public String getUserTypeName() {
		return userTypeName;
	}
	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}
	public int getUserLevelDiscount() {
		return userLevelDiscount;
	}
	public void setUserLevelDiscount(int userLevelDiscount) {
		this.userLevelDiscount = userLevelDiscount;
	}
	public int getIsAllowCancel() {
		return isAllowCancel;
	}
	public void setIsAllowCancel(int isAllowCancel) {
		this.isAllowCancel = isAllowCancel;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopTime() {
		return shopTime;
	}
	public void setShopTime(String shopTime) {
		this.shopTime = shopTime;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Short getActivityType() {
		return activityType;
	}
	public void setActivityType(Short activityType) {
		this.activityType = activityType;
	}
	public String getActivityTypeDesc() {
		return activityTypeDesc;
	}
	public void setActivityTypeDesc(String activityTypeDesc) {
		this.activityTypeDesc = activityTypeDesc;
	}
	public String getC2bImageHearder() {
		return c2bImageHearder;
	}
	public void setC2bImageHearder(String c2bImageHearder) {
		this.c2bImageHearder = c2bImageHearder;
	}
	public String getTimeoutRsn() {
		return timeoutRsn;
	}
	public void setTimeoutRsn(String timeoutRsn) {
		this.timeoutRsn = timeoutRsn;
	}
	public BigDecimal getTotalBonus() {
		return totalBonus;
	}
	public void setTotalBonus(BigDecimal totalBonus) {
		this.totalBonus = totalBonus;
	}
	public String getBonusName() {
		return bonusName;
	}
	public void setBonusName(String bonusName) {
		this.bonusName = bonusName;
	}
	public int getUsePartBonus() {
		return usePartBonus;
	}
	public void setUsePartBonus(int usePartBonus) {
		this.usePartBonus = usePartBonus;
	}
	public BigDecimal getExchangeReturnFee() {
		return exchangeReturnFee;
	}
	public void setExchangeReturnFee(BigDecimal exchangeReturnFee) {
		this.exchangeReturnFee = exchangeReturnFee;
	}
	public String getQuestionStatusStr(){
		if(null==this.getQuestionStatus()){return "";}
		StringBuffer str=new StringBuffer();
		switch (this.getQuestionStatus()) {
		case 0:
			str.append("否");
			break;
		case 1:
			str.append("是");
			break;
		default:
			str.append(this.getQuestionStatus().toString());
			break;
		};
		return str.toString();
	}
	

	
	public String getOrderTypeStr(){
		if(null==this.getOrderType()){return "";}
		StringBuffer str=new StringBuffer();
		switch (this.getOrderType()) {
		case 0:
			str.append("正常订单");
			break;
		case 1:
			str.append("补发订单");
			break;
		case 2:
			str.append("换货订单");
			break;
		default:
			str.append(this.getOrderType().toString());
			break;
		};
		return str.toString();
		
	}
	
	public String getTransTypeStr() {
		if(null==this.getTransType()){return "";}
		StringBuffer str=new StringBuffer();
		switch (this.getTransType()) {
		case 1:
			str.append("款到发货");
			break;
		case 2:
			str.append("货到付款");
			break;
		case 3:
			str.append("担保交易");
			break;
		default:
			str.append(this.getTransType().toString());
			break;
		};
		return str.toString();
	}

	public String getOrdertotalstatusStr() {
		StringBuffer str=new StringBuffer();
		str.append(this.getOrderStatusStr());
		str.append(",");
		str.append(this.getPayStatusStr());
		str.append(",");
		str.append(this.getShipStatusStr());
		return str.toString();
	}

	public String getOrderStatusStr() {
		if(null==this.getOrderStatus()){return "";}
		StringBuffer str=new StringBuffer();
		switch (this.getOrderStatus()) {
		case 0:
			str.append("未确认");
			break;
		case 1:
			str.append("已确认");
			break;
		case 2:
			str.append("已取消");
			break;
		case 3:
			str.append("完成");
			break;
/*		case 4:
			str.append("退货");
			break;
		case 5:
			str.append("锁定");
			break;
		case 6:
			str.append("解锁");
			break;
		case 7:
			str.append("完成");
			break;
		case 8:
			str.append("拒收");
			break;
		case 9:
			str.append("已合并");
			break;
		case 10:
			str.append("已拆分");
			break;*/
		default:
			str.append(this.getOrderStatus().toString());
			break;
		};
		return str.toString();
	}

	public String getPayStatusStr() {
		if(null==this.getPayStatus()){return "";}
		StringBuffer str=new StringBuffer();
		switch (this.getPayStatus()) {
		case 0:
			str.append("未付款");
			break;
		case 1:
			str.append("部分付款");
			break;
		case 2:
			str.append("已付款");
			break;
		case 3:
			str.append("已结算");
			break;
		default:
			str.append(this.getPayStatus().toString());
			break;
		};
		return str.toString();
	}

	public String getShipStatusStr() {
		StringBuffer str=new StringBuffer();
		if(null==this.getShipStatus()){return "";}
		switch (this.getShipStatus()) {
		case 0:
			str.append("未发货");
			break;
		case 1:
			str.append("备货中");
			break;
		case 2:
			str.append("部分发货");
			break;
		case 3:
			str.append("已发货");
			break;
		case 4:
			str.append("部分收货");
			break;
		case 5:
			str.append("客户已收货");
			break;
		case 6:
			str.append("门店部分收货");
			break;
		case 7:
			str.append("门店收货");
			break;
		default:
			str.append(this.getShipStatus().toString());
			break;
		};
		return str.toString();
	}
	
	
	public String getShippingStatusStr() {
		StringBuffer str=new StringBuffer();
		if(null==this.getShippingStatus()){
			return "";
		}
		switch (this.getShippingStatus()) {

	/*	0，未发货；1，已发货；2，已收货；3，备货中；6,门店收货10，快递取件；11，运输中；12，派件中；13，客

		户签收；14，客户拒签；15，货物遗失；16，货物损毁*/

		case 0:
			str.append("未发货");
			break;
		case 1:
			str.append("已发货");
			break;
		case 2:
			str.append("已收货");
			break;
		case 3:
			str.append("备货中");
			break;
		/*case 4:
			str.append("部分收货");
			break;*/
	/*	case 5:
			str.append("客户已收货");
			break;*/
		case 6:
			str.append("门店收货");
			break;
	/*	case 7:
			str.append("");
			break;*/
/*		case 8:
			str.append("");
			break;
		case 9:
			str.append("");
			break;*/
		case 10:
			str.append("快递取件");
			break;
		case 11:
			str.append("运输中");
			break;
		case 12:
			str.append("派件中");
			break;
		case 13:
			str.append("客户签收");
			break;
		case 14:
			str.append("客户拒签");
			break;
		case 15:
			str.append("货物遗失");
			break;
		case 16:
			str.append("货物损毁");
			break;
		default:
			str.append(this.getShippingStatus().toString());
			break;
		};
		return str.toString();
	}

	
	public String getOrderCategoryStr() {
		StringBuffer str=new StringBuffer();
		if(null==this.getOrderCategory()){return "";}
		switch (this.getOrderCategory()) {
		case 1:
			str.append("零售");
			break;
		case 2:
			str.append("物资领用");
			break;
		case 3:
			str.append("其它出库");
			break;
		case 4:
			str.append("C2b定制");
			break;
		default:
			str.append(this.getOrderCategory().toString());
			break;
	}
		return str.toString();
    }

/*	public String getConsigneeStr() {
		return this.getConsignee()+" TEL:"+this.getMobile();
		
		
	}*/
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSelectTimeType() {
		return selectTimeType;
	}
	public void setSelectTimeType(String selectTimeType) {
		this.selectTimeType = selectTimeType;
	}
	public Integer getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(Integer isGroup) {
		this.isGroup = isGroup;
	}
	public Integer getIsAdvance() {
		return isAdvance;
	}
	public void setIsAdvance(Integer isAdvance) {
		this.isAdvance = isAdvance;
	}
	public Integer getOrderView() {
		return orderView;
	}
	public void setOrderView(Integer orderView) {
		this.orderView = orderView;
	}
	public Integer getPayId() {
		return payId;
	}
	public void setPayId(Integer payId) {
		this.payId = payId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getShippingId() {
		return shippingId;
	}
	public void setShippingId(Integer shippingId) {
		this.shippingId = shippingId;
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
	public String getListDataType() {
		return listDataType;
	}
	public void setListDataType(String listDataType) {
		this.listDataType = listDataType;
	}
	public Date getShippingTime() {
		return shippingTime;
	}
	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getOrderFromSec() {
		return orderFromSec;
	}
	public void setOrderFromSec(String orderFromSec) {
		this.orderFromSec = orderFromSec;
	}
	public String getOrderFromFirst() {
		return orderFromFirst;
	}
	public void setOrderFromFirst(String orderFromFirst) {
		this.orderFromFirst = orderFromFirst;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public BigDecimal getCardMoney() {
		return cardMoney;
	}
	public void setCardMoney(BigDecimal cardMoney) {
		this.cardMoney = cardMoney;
	}
	public String getLockUserName() {
		return lockUserName;
	}
	public void setLockUserName(String lockUserName) {
		this.lockUserName = lockUserName;
	}
	public Byte getReviveStt() {
		return reviveStt;
	}
	public void setReviveStt(Byte reviveStt) {
		this.reviveStt = reviveStt;
	}
	public Integer getIsShop() {
		return isShop;
	}
	public void setIsShop(Integer isShop) {
		this.isShop = isShop;
	}
	public int getOsqStatus() {
		return osqStatus;
	}
	public void setOsqStatus(int osqStatus) {
		this.osqStatus = osqStatus;
	}
	public int getLogqStatus() {
		return logqStatus;
	}
	public void setLogqStatus(int logqStatus) {
		this.logqStatus = logqStatus;
	}
	public String getLogqDesc() {
		return logqDesc;
	}
	public void setLogqDesc(String logqDesc) {
		this.logqDesc = logqDesc;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}
	public String getBgUserLevel() {
		return bgUserLevel;
	}

	public void setBgUserLevel(String bgUserLevel) {
		this.bgUserLevel = bgUserLevel;
	}

/*	public String getUseLevelStr() {
		// 判断是否是平台订单
		if ("HQ01S116".equals(getOrderFrom())) {
			useLevelStr = getBgUserLevel();
		} else {
			int userLevel = 0;
			if (null == getChannelUserLevel()) {
				return "";
			}
			userLevel = Integer.valueOf(getChannelUserLevel());
			switch (userLevel) {
				case 0:
					useLevelStr = "普通会员";
					break;
				case 1:
					useLevelStr = "高级会员";
					break;
				case 2:
					useLevelStr = "vip会员";
					break;
				case 3:
					useLevelStr = "至尊vip会员";
					break;
			}
		}
		return useLevelStr;
	}*/

	public void setUseLevelStr(String useLevelStr) {
		this.useLevelStr = useLevelStr;
	}
	
	public BigDecimal getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	//计算折扣率
	public String getCalculateDiscount() {
		BigDecimal shippingTotalFee  =  (BigDecimal) (this.getShippingFee()==null? new BigDecimal(0.00)   : this.getShippingFee());	
		BigDecimal totalFee  =  (BigDecimal) (this.getTotalFee()==null? new BigDecimal(0.00)   : this.getTotalFee());	
		BigDecimal surplus  =  (BigDecimal) (this.getSurplus()==null? new BigDecimal(0.00)   : this.getSurplus());	
		BigDecimal moneyPaid  =  (BigDecimal) (this.getMoneyPaid()==null? new BigDecimal(0.00)   : this.getMoneyPaid());
		if(totalFee.doubleValue() - shippingTotalFee.doubleValue() <= 0) {
			return "";
		} else {
			double a = moneyPaid.doubleValue() + surplus.doubleValue()- shippingTotalFee.doubleValue();
			double b = totalFee.doubleValue() - shippingTotalFee.doubleValue();
			double re = (a/b)*100;
			re =re*100/100;
			BigDecimal discountMoney_b = new BigDecimal(re);
			re = discountMoney_b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			String sRe = String.valueOf(re);
			return sRe + "%";
		}
	}
	public String[] getOrderFroms() {
		return orderFroms;
	}
	
	public void setOrderFroms(String[] orderFroms) {
		this.orderFroms = orderFroms;
	}
	
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public BigDecimal getPayFee() {
		return payFee;
	}
	public void setPayFee(BigDecimal payFee) {
		this.payFee = payFee;
	}
	public String getBonusId() {
		return bonusId;
	}
	public void setBonusId(String bonusId) {
		this.bonusId = bonusId;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getExportTemplateType() {
		return exportTemplateType;
	}
	public void setExportTemplateType(String exportTemplateType) {
		this.exportTemplateType = exportTemplateType;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	private String mainOrChild; //主订单或子订单

	public String getMainOrChild() {
		return mainOrChild;
	}
	public void setMainOrChild(String mainOrChild) {
		this.mainOrChild = mainOrChild;
	}	
	
	private String orderSn; //子订单号

	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	
	private Byte depotStatus;  //'分仓发货状态（0，未分仓 1，已分仓未通知 2，已分仓已通知）',

	public Byte getDepotStatus() {
		return depotStatus;
	}
	public void setDepotStatus(Byte depotStatus) {
		this.depotStatus = depotStatus;
	}
	
	
	private List<String> masterOrderSnList;

	public List<String> getMasterOrderSnList() {
		return masterOrderSnList;
	}
	public void setMasterOrderSnList(List<String> masterOrderSnList) {
		this.masterOrderSnList = masterOrderSnList;
	}
	
	private String consignee; //收货人

	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	private String mobile; //手机

	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
		
	private String tel; //收货人电话
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	private String mergePaySn; //合并付款单编号
	
	private BigDecimal mergePayFee; //合并付款单金额

	public String getMergePaySn() {
		return mergePaySn;
	}
	public void setMergePaySn(String mergePaySn) {
		this.mergePaySn = mergePaySn;
	}
	public BigDecimal getMergePayFee() {
		return mergePayFee;
	}
	public void setMergePayFee(BigDecimal mergePayFee) {
		this.mergePayFee = mergePayFee;
	}

	private String  masterPaySn; //主付款单编号

	public String getMasterPaySn() {
		return masterPaySn;
	}
	public void setMasterPaySn(String masterPaySn) {
		this.masterPaySn = masterPaySn;
	}
	
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<String> getSites() {
		return sites;
	}
	public void setSites(List<String> sites) {
		this.sites = sites;
	}
	public Integer getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}
}