package com.work.shop.oms.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.common.bean.TimeUtil;

/**
 * 退单信息
 * @author QuYachu
 */
public class OrderReturnListVO extends OrderReturn {
	
	private static final long serialVersionUID = -5427783822434109483L;
	
	/**
	 * 商品11位码
	 */
	private String customCode;
	
	private String returnPays;
	
	/**
	 * 退货快递公司编码
	 */
	private String returnExpress;
	
	/**
	 * 退货数量
	 */
	private Short prodscannum;
	
	/**
	 * 用户权限站点 
	 */
	private List<String> sites;
	
	/**
	 * 查询类型 0退单列表、1退单商品列表
	 */
	private int searchType;

	/**
	 * 付款备注
	 */
	private String payNote;
	
	private BigDecimal financialPrices;

	/**
	 * 交易类型 1：款到发货 2：货到付款 3：担保交易
	 */
	private Byte transType;

	/**
	 * 导出模版类型
	 */
	private String exportTemplateType;

	/**
	 * 折扣金额
	 */
	private BigDecimal discount;

	/**
	 * 退红包金额
	 */
	private BigDecimal returnBonusMoney;

	/**
	 * 退款方式名称
	 */
	private String returnPayName;
	
	/**
	 * 退金额状态：0未退、1已退
	 */
	private int backBalance;
	
	private String skuSns;
	private String goodsSns;
	private String goodsNames;
	private String custumCodes;
	private Integer orderView;
	private String consignee;

	/**
	 * 升序或降序
	 */
	private String dir;

	/**
	 * 排序的字段
	 */
	private String sort;

	/**
	 * 仓库编码
	 */
	private String depotCode;

	/**
	 * 退货人手机号码
	 */
	private String returnMobile;

	private String warehouseName;

	private String noOrderGoods; // 导出不包含商品信息

	private String depotName;

	/**
	 * 退款银行
	 */
	private String bank;

	/**
	 * 退款支行
	 */
	private String subsidiaryBank;

	/**
	 * 退款帐号
	 */
	private String accountNumber;

	/**
	 * 退款账户
	 */
	private String account;

	private String warehouseCode;

	/**
	 * 关联退单SN
	 */
	private String returnShippingSn;

	/**
	 * 商品编号
	 */
	private String goodsSn;

	/**
	 * 商品sku码
	 */
	private String skuSn;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 退单商品数量
	 */
	private Short goodsReturnNumber;

	/**
	 * 商品购买量
	 */
	private Short goodsBuyNumber;

	/**
	 * 商品成交价
	 */
	private BigDecimal marketPrice;

	/**
	 * 商品财务价
	 */
	private BigDecimal goodsPrice;

	private BigDecimal totalFee; // 成交价格

	private BigDecimal goodsAmount;// 财务价格

	/**
	 * 红包金额
	 */
	private BigDecimal bonus;

	private String payName;

	private String returnReasonName; // 退单原因

	// 外部交易号
	private String orderOutSn; // order_info

	// referer
	private String referer;// order_info

	private Byte orderOrderStatus; // order_info

/*	public Double getReturnTotalFee() {
		return returnTotalFee;
	}*/

	private Byte orderPayStatus; // order_info

	private Byte orderShipStatus; // order_info
	// 退款方式
	private Integer returnPay;// order_refund
	// 下单人
	private String userName;// order_info
	// 退款金额
//	private Double returnTotalFee;// order_refund

	// 退货快递单号
	private String returnInvoiceNo;// order_return_ship

	// 财务状态
	private Integer returnPayStatus;// order_refund

	// 入库时间
	private Date checkinTime;// order_return_ship
	// 退单商品数量
	private int returnGoodsCount;// order_return_ship
	// 退单入库状态
	private Integer returnShippingStatus;// order_return_ship
	// 退款金额区间查询
	private Double stReturnTotalFee;
	private Double enReturnTotalFee;
	// 时间查询类型标志
	private String selectTimeType;
	// 时间查询
	private String startTime;
	private String endTime;
	// 退单状态
	private String returnOrderStatusStr;
	// 订单来源
	private String channelName;
	// 退单类型
	private String returnTypeStr;
	// 退款方式Str
	private String returnPayStr;
	// 退单入库状态
	private String returnShippingStatusStr;
	// 数据类型 newDate-近三月数据 historyDate-历史数据
	private String listDataType;
	// 来源
	private String orderFrom;
	private String[] orderFroms;
	private String orderFromFirst;
	private String orderFromSec;
	
	private String addTimeStr;
	
	/**
	 * 是否入库(0未入库 1已入库 2待入库)
	 */
	private Byte checkinStatus;
	 
	private Byte isGoodReceived;
	
	/**
	 * 箱规
	 */
	private Integer boxGauge;

	/**
	 * 渠道类型 0店铺、1自营
	 */
	private Integer channelType;
	
	public Short getProdscannum() {
		return prodscannum;
	}

	public void setProdscannum(Short prodscannum) {
		this.prodscannum = prodscannum;
	}

	/***
	 * 承运商
	 * 
	 * **/
	public String getReturnExpressStr() {
		
		if(StringUtils.isBlank(this.getReturnExpress())){
			return "";
		}
		
		if("cac".equals(this.getReturnExpress())){
			return "自提";
		}else if("sto".equals(this.getReturnExpress())){
			return "申通快递";
		}else if("zjs".equals(this.getReturnExpress())){
			return "宅急送";
		}else if("htky".equals(this.getReturnExpress())){
			return "汇通快运";
		}else if("ems".equals(this.getReturnExpress())){
			return "EMS 国内邮政特快专递";
		}else if("yto".equals(this.getReturnExpress())){
			return "圆通速递";
		}else if("postbag".equals(this.getReturnExpress())){
			return "邮政小包";
		}else if("phx".equals(this.getReturnExpress())){
			return "4PX联邮通";
		}else if("yanwen".equals(this.getReturnExpress())){
			return "燕文专线";
		}else if("hdnl".equals(this.getReturnExpress())){
			return "燕文专线HDNL";
		}else if("eyb".equals(this.getReturnExpress())){
			return "E邮宝";
		}else if("zjb".equals(this.getReturnExpress())){
			return "雅玛多";
		}else if("zmkm".equals(this.getReturnExpress())){
			return "芝麻开门";
		}else if("sf_express".equals(this.getReturnExpress())){
			return "顺丰速运";
		}else if("shcod".equals(this.getReturnExpress())){
			return "赛澳递";
		}
		else if("zto".equals(this.getReturnExpress())){
			return "中通速递";
		}
		else if("yunda".equals(this.getReturnExpress())){
			return "韵达快运";
		}
		else if("bswl".equals(this.getReturnExpress())){
			return "邦送物流";
		}
		else if("qfkd".equals(this.getReturnExpress())){
			return "全峰快递";
		}
		else if("qfkdcod".equals(this.getReturnExpress())){
			return "全峰快递到付";
		}
		else if("stars_express".equals(this.getReturnExpress())){
			return "星辰急便";
		}
		else if("post_mail".equals(this.getReturnExpress())){
			return "邮局平邮";
		}
		else if("jdkd".equals(this.getReturnExpress())){
			return "京东快递";
		}
		else if("ttkd".equals(this.getReturnExpress())){
			return "天天快递";
		}
		return "";
		
	}
	
	public String getReturnExpress() {
		return returnExpress;
	}
	public void setReturnExpress(String returnExpress) {
		this.returnExpress = returnExpress;
	}

	private List<String>  addList;
	
	public String getCustomCode() {
		return customCode;
	}
	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}
	public List<String> getAddList() {
		return addList;
	}
	public void setAddList(List<String> addList) {
		this.addList = addList;
	}
	
	private boolean  isNotQuanQiuTong;  //
	
	public boolean getIsNotQuanQiuTong() {
		return isNotQuanQiuTong;
	}
	public void setIsNotQuanQiuTong(boolean isNotQuanQiuTong) {
		this.isNotQuanQiuTong = isNotQuanQiuTong;
	}
	
	public String getReturnPays() {
		return returnPays;
	}
	
	public String getReturnPaysStr() {
	
		if (null == this.getReturnPays()) {
			return "";
		}
		
		String [] strArray = this.getReturnPays().split(",");
		
		StringBuffer sb = new StringBuffer();
		
		for(String str :strArray ){
			
			if(StringUtils.isNotBlank(str)){
				switch (Integer.valueOf(str)) {
		
				case 1:
					sb.append("支付宝,");
					break;
				case 2:
					sb.append("网银在线,");
					break;
				case 3:
					sb.append("全部余额支付,");
					break;
				case 4:
					sb.append("货到付款");
					break;
				case 5:
					sb.append( "银联在线支付");
					break;
				case 6:
					sb.append( "财付通");
					break;
				case 7:
					sb.append( "财付通");
					break;
				case 8:
					sb.append( "银行汇款/转帐");
					break;
				case 9:
					sb.append("邮局汇款");
					break;
					
				case 10:
					sb.append("财付通中介担保接口");
					break;
					
				case 11:
					sb.append("贝宝支付");
					break;
					
				case 12:
					sb.append("招商银行");
					break;
					
				case 13:
					sb.append("一号店");
					break;
						
				case 14:
					sb.append("汇付天下");
					break;
				case 15:
					sb.append("手机银联支付");
					break;
				case 16:
					sb.append("京东支付");
					break;
				case 17:
					sb.append("苏宁支付");
					break;
				case 18:
					sb.append("LC风格网");
					break;
				case 19:
					sb.append("微信支付");
					
				case 20:
					sb.append("1号店");
					
				case 21:
					sb.append("门店支付");
					break;
				case 22:
					sb.append("当当支付");
					break;
				case 23:
					sb.append("聚美支付");
					break;
				case 24:
					sb.append("爱奇艺支付");
					break;
				case 25:
					sb.append("京东优惠");
					break;
				case 26:
					sb.append("退单转入款");
					break;
		
				default:
					sb.append(str+",");
					break;
				}
			}
			
			
		}
		
	/*	switch (this.getReturnPay()) {
		case 0:
			return "退款到个人账户";
		case 1:
			return "退款到支付宝";
		case 2:
			return "退款到银行账户";
		case 3:
			return "退款到财付通";
		case 4:
			return "退款到银联支付";
		case 6:
			return "退款到招商银行";
		case 7:
			return "退款到贝宝支付";
		case 8:
			return "退款到快钱人民币网关";
		case 9:
			return "退款到一号店";
		case 14:
			return "退款到汇付天下";
		case 15:
			return "退款到手机银联";
		case 16:
			return "至京东平台退款";
		case 17:
			return "退款到苏宁支付";
		case 18:
			return "退款到LC风格网";
		case 19:
			return "退款到微信支付";
		case 22:
			return "退款到当当支付";
		case 23:
			return "退款到聚美支付";
		case 10:
			return "无须退款";
		default:
			return this.getReturnPay().toString();
		}*/
		
		return sb.toString();
	}
	
	

	public void setReturnPays(String returnPays) {
		this.returnPays = returnPays;
	}

	public String getReturnReasonStr() {
		
	/*	
        {v : '21', n : '质量问题'},
        {v : '22', n : '尺寸不合适'},
        {v : '23', n : '颜色不满意'},
        {v : '24', n : '款式、设计不满意'},
        {v : '25', n : '色差'},
        {v : '26', n : '材质不满意'},
        {v : '27', n : '实物与描述不符'},
        {v : '28', n : '吊牌错'},
        {v : '29', n : '网站信息（图片等）出错'},
        {v : '30', n : '价格问题（其他渠道便宜、降价）'},
        {v : '31', n : '快递问题（服务态度、送货慢等）'},
        {v : '32', n : '市场活动不满'},
        {v : '33', n : '其他（无理由退货等）'},
        {v : '34', n : '错漏发货'}
		*/
		
		
		
		
		
		if (null == this.getReturnReason()) {
			return "";
		}
		
		if("21".equals(this.getReturnReason())){
			return "质量问题";
		}else if("22".equals(this.getReturnReason())){
			return "尺寸不合适";
		}else if("23".equals(this.getReturnReason())){
			return "颜色不满意";
		}else if("24".equals(this.getReturnReason())){
			return "款式、设计不满意";
		}else if("25".equals(this.getReturnReason())){
			return "色差";
		}else if("26".equals(this.getReturnReason())){
			return "材质不满意";
		}else if("27".equals(this.getReturnReason())){
			return "实物与描述不符";
		}else if("28".equals(this.getReturnReason())){
			return "吊牌错";
		}else if("29".equals(this.getReturnReason())){
			return "网站信息（图片等）出错";
		}else if("30".equals(this.getReturnReason())){
			return "价格问题（其他渠道便宜、降价）";
		}else if("31".equals(this.getReturnReason())){
			return "快递问题（服务态度、送货慢等）";
		}else if("32".equals(this.getReturnReason())){
			return "市场活动不满";
		}else if("33".equals(this.getReturnReason())){
			return "其他（无理由退货等）";
		}else if("34".equals(this.getReturnReason())){
			return "错漏发货";
		}
		return "";
		
	/*	switch (this.getReturnReason()) {
		case 0:
			return "无操作";
		case 1:
			return "退货";
		case 2:
			return "修补";
		case 3:
			return "销毁";
		case 4:
			return "换货";
		default:
			return "无操作";
		}*/
		
		//return returnReasonStr;
	}

	/*public void setReturnReasonStr(String returnReasonStr) {
		this.returnReasonStr = returnReasonStr;
	}*/

//	private String returnReasonStr;  //退单原因
	
	

	private BigDecimal settlementPrice;   //财务价格
	
	public BigDecimal getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public String getPayNote() {
		return payNote;
	}
	
	private Integer qualityStatus;
	
	public Integer getQualityStatus() {
		return qualityStatus;
	}

	public void setQualityStatus(Integer qualityStatus) {
		this.qualityStatus = qualityStatus;
	}

	public Byte getCheckinStatus() {
		return checkinStatus;
	}

	public void setCheckinStatus(Byte checkinStatus) {
		this.checkinStatus = checkinStatus;
	}

	public Byte getIsGoodReceived() {
		return isGoodReceived;
	}

	public void setIsGoodReceived(Byte isGoodReceived) {
		this.isGoodReceived = isGoodReceived;
	}

	public void setPayNote(String payNote) {
		this.payNote = payNote;
	}

	public BigDecimal getFinancialPrices() {
		return financialPrices;
	}

	public void setFinancialPrices(BigDecimal financialPrices) {
		this.financialPrices = financialPrices;
	}

	public Byte getTransType() {
		return transType;
	}

	public void setTransType(Byte transType) {
		this.transType = transType;
	}

	public String getExportTemplateType() {
		return exportTemplateType;
	}

	public void setExportTemplateType(String exportTemplateType) {
		this.exportTemplateType = exportTemplateType;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getReturnBonusMoney() {
		return returnBonusMoney;
	}

	public void setReturnBonusMoney(BigDecimal returnBonusMoney) {
		this.returnBonusMoney = returnBonusMoney;
	}

	public String getReturnPayName() {
		return returnPayName;
	}

	public void setReturnPayName(String returnPayName) {
		this.returnPayName = returnPayName;
	}

	public String getSkuSns() {
		return skuSns;
	}

	public void setSkuSns(String skuSns) {
		this.skuSns = skuSns;
	}

	public String getGoodsSns() {
		return goodsSns;
	}

	public void setGoodsSns(String goodsSns) {
		this.goodsSns = goodsSns;
	}

	public String getGoodsNames() {
		return goodsNames;
	}

	public void setGoodsNames(String goodsNames) {
		this.goodsNames = goodsNames;
	}

	public String getCustumCodes() {
		return custumCodes;
	}

	public void setCustumCodes(String custumCodes) {
		this.custumCodes = custumCodes;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getOrderView() {
		return orderView;
	}

	public void setOrderView(Integer orderView) {
		this.orderView = orderView;
	}

	public String getNoOrderGoods() {
		return noOrderGoods;
	}

	public void setNoOrderGoods(String noOrderGoods) {
		this.noOrderGoods = noOrderGoods;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getReturnMobile() {
		return returnMobile;
	}

	public void setReturnMobile(String returnMobile) {
		this.returnMobile = returnMobile;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getSubsidiaryBank() {
		return subsidiaryBank;
	}

	public void setSubsidiaryBank(String subsidiaryBank) {
		this.subsidiaryBank = subsidiaryBank;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getReturnShippingSn() {
		return returnShippingSn;
	}

	public void setReturnShippingSn(String returnShippingSn) {
		this.returnShippingSn = returnShippingSn;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getSkuSn() {
		return skuSn;
	}

	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Short getGoodsReturnNumber() {
		return goodsReturnNumber;
	}

	public void setGoodsReturnNumber(Short goodsReturnNumber) {
		this.goodsReturnNumber = goodsReturnNumber;
	}

	public Short getGoodsBuyNumber() {
		return goodsBuyNumber;
	}

	public void setGoodsBuyNumber(Short goodsBuyNumber) {
		this.goodsBuyNumber = goodsBuyNumber;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getReturnReasonName() {
		return returnReasonName;
	}

	public void setReturnReasonName(String returnReasonName) {
		this.returnReasonName = returnReasonName;
	}

	// 处理方式
	public String getProcessTypeStr() {
		if (null == this.getProcessType()) {
			return "无操作";
		}

		switch (this.getProcessType()) {
		case 0:
			return "无操作";
		case 1:
			return "退货";
		case 2:
			return "修补";
		case 3:
			return "销毁";
		case 4:
			return "换货";
		default:
			return "无操作";
		}
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String[] getOrderFroms() {
		return orderFroms;
	}

	public void setOrderFroms(String[] orderFroms) {
		this.orderFroms = orderFroms;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	public String getOrderOutSn() {
		return orderOutSn;
	}

	public void setOrderOutSn(String orderOutSn) {
		this.orderOutSn = orderOutSn;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public Integer getReturnPay() {
		return returnPay;
	}

	public void setReturnPay(Integer returnPay) {
		this.returnPay = returnPay;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}

	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}

	public Integer getReturnPayStatus() {
		return returnPayStatus;
	}

	public void setReturnPayStatus(Integer returnPayStatus) {
		this.returnPayStatus = returnPayStatus;
	}

	public Date getCheckinTime() {
		return checkinTime;
	}

	public void setCheckinTime(Date checkinTime) {
		this.checkinTime = checkinTime;
	}

	public int getReturnGoodsCount() {
		return returnGoodsCount;
	}

	public void setReturnGoodsCount(int returnGoodsCount) {
		this.returnGoodsCount = returnGoodsCount;
	}

	public Integer getReturnShippingStatus() {
		return returnShippingStatus;
	}

	public void setReturnShippingStatus(Integer returnShippingStatus) {
		this.returnShippingStatus = returnShippingStatus;
	}


	/*public void setReturnTotalFee(Double returnTotalFee) {
		this.returnTotalFee = returnTotalFee;
	}*/

	public Double getStReturnTotalFee() {
		return stReturnTotalFee;
	}

	public void setStReturnTotalFee(Double stReturnTotalFee) {
		this.stReturnTotalFee = stReturnTotalFee;
	}

	public Double getEnReturnTotalFee() {
		return enReturnTotalFee;
	}

	public void setEnReturnTotalFee(Double enReturnTotalFee) {
		this.enReturnTotalFee = enReturnTotalFee;
	}

	public String getSelectTimeType() {
		return selectTimeType;
	}

	public void setSelectTimeType(String selectTimeType) {
		this.selectTimeType = selectTimeType;
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

	public String getReturnOrderStatusStr() {
		if (null == this.getReturnOrderStatus()) {
			return "";
		}
		switch (this.getReturnOrderStatus()) {
		case 0:
			return "未确认";
		case 1:
			return "已确认";
		case 2:
			return "质检通过";
		case 3:
			return "质检不通过";
		case 4:
			return "无效";
		case 5:
			return "已提交处理意见";
		case 10:
			return "已完成";
		default:
			return this.getReturnOrderStatus().toString();
		}

	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getReturnTypeStr() {
		if (null == this.getReturnType()) {
			return "";
		}
		switch (this.getReturnType()) {
		case 1:
			return "退货单";
		case 2:
			return "拒收入库单";
		case 3:
			return "退款单";
		case 4:
			return "额外退款单";	
		case 5:
			 return "承运商失货单";
			
		default:
			return this.getReturnType().toString();
		}
	}


	/**
	 * 订单状态
	 ***/
	public String getOrderStatusStr() {
		if (this.getOrderOrderStatus() == null) {
			return "";
		}
		String returnOrderStatusStr = this.orderStatusForIntToStr(this.getOrderOrderStatus()) + ","
				+ this.orderPayStatusForIntToStr(this.getOrderPayStatus()) + "," + this.shipStatusForIntToStr(this.getOrderShipStatus());
		return returnOrderStatusStr;
	}
	
	/**
     * 订单状态int值转换成字符串
     * @param orderStatus
     * @return
     */
    public String orderStatusForIntToStr(int orderStatus) {
        switch(orderStatus) {
        case 0: return "未确认";
        case 1: return "已确认";
        case 2: return "已取消";
        case 3: return "已完成";
        case 4: return "退货";
        case 5: return "锁定";
        case 6: return "解锁";
        case 7: return "完成";
        case 8: return "拒收";
        case 9: return "已合并";
        case 10: return "已拆分";
        case 11: return "已关闭";
        default:
            return "未知";
        }
    }
    
    /**
     * 订单支付方式 
     * @param int orderPayStatus 订单支付状态
     * @return String
     ***/
    public String orderPayStatusForIntToStr(int orderPayStatus) {
        switch(orderPayStatus) {
            case 0: return "未付款";
            case 1: return "部分付款";
            case 2: return "已付款";
            case 3: return "已结算";
            default:return "未知";
        }
    }
    
    /**
     * 订单的发货状态-发货状态发 货总状态（0，未发货；1，备货中；2，部分发货；3，已发货；4，部分收货；5，客户已收货）
     * @param int shipStatus 订单的发货状态
     * @return String
     * **/
    public String shipStatusForIntToStr(int shipStatus) {
        switch(shipStatus) {
            case 0: return "未发货";
            case 1: return "备货中";
            case 2: return "部分发货";
            case 3: return "已发货";
            case 4: return "部分收货";
            case 5: return "客户已收货";
            default:return "未知";
        }
    }
	
	/**
	 * 退单状态;
	 **/
	public String getReturnStatusStr() {
		if (this.getPayStatus() == null) {
			return "";
		}

        //退款单
        if (this.getReturnType() == 3) {
            return this.getReturnOrderStatusStr();
        }

		String returnStatusStr = this.getReturnOrderStatusStr() + "," + this.payStatus(this.getPayStatus()) + ","
				+ this.checkinStatus(this.getCheckinStatus());
		return returnStatusStr;
	}
	
	/**
     * 财务状态 （0，未结算；1，已结算；2，待结算）
     ***/
    public String payStatus(int shipStatus) {
        switch(shipStatus) {
            case 0: return "未结算";
            case 1: return "已结算";
            case 2: return "待结算";
            default:return "未知";
        }
    }
    
  //是否入库:0未入库 1已入库 2待入库）',
    public String checkinStatus(Byte checkinStatus) {
        
        if(null == checkinStatus){
            return "";
        }

        switch(checkinStatus) {
            case 0: return "未入库";
            case 1: return "已入库";
            case 2: return "待入库";
            default:return "未知";
        }
    }

	public String getListDataType() {
		return listDataType;
	}

	public void setListDataType(String listDataType) {
		this.listDataType = listDataType;
	}

	public String getOrderFromSec() {
		return orderFromSec;
	}

	public void setOrderFromSec(String orderFromSec) {
		this.orderFromSec = orderFromSec;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getOrderFromFirst() {
		return orderFromFirst;
	}

	public void setOrderFromFirst(String orderFromFirst) {
		this.orderFromFirst = orderFromFirst;
	}

	public Byte getOrderOrderStatus() {
		return orderOrderStatus;
	}

	public void setOrderOrderStatus(Byte orderOrderStatus) {
		this.orderOrderStatus = orderOrderStatus;
	}

	public Byte getOrderPayStatus() {
		return orderPayStatus;
	}

	public void setOrderPayStatus(Byte orderPayStatus) {
		this.orderPayStatus = orderPayStatus;
	}

	public Byte getOrderShipStatus() {
		return orderShipStatus;
	}

	public void setOrderShipStatus(Byte orderShipStatus) {
		this.orderShipStatus = orderShipStatus;
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
	
	
	private String orderSn;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	
	//入库时间
	private Date orgCheckinTime;// order_return_goods

	public Date getOrgCheckinTime() {
		return orgCheckinTime;
	}

	public void setOrgCheckinTime(Date orgCheckinTime) {
		this.orgCheckinTime = orgCheckinTime;
	}
	
	public List<String> getSites() {
		return sites;
	}
	public void setSites(List<String> sites) {
		this.sites = sites;
	}

	public String getAddTimeStr() {
		if (this.getAddTime() == null) {
			return "";
		}
		return addTimeStr = TimeUtil.formatDate(this.getAddTime());
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public Integer getBoxGauge() {
		return boxGauge;
	}

	public void setBoxGauge(Integer boxGauge) {
		this.boxGauge = boxGauge;
	}

	public int getBackBalance() {
		return backBalance;
	}

	public void setBackBalance(int backBalance) {
		this.backBalance = backBalance;
	}

    public String getReturnPayStr() {
        return returnPayStr;
    }

    public void setReturnPayStr(String returnPayStr) {
        this.returnPayStr = returnPayStr;
    }

	public Integer getChannelType() {
		return channelType;
	}

	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}
}
