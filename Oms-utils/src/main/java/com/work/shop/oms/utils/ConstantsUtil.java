package com.work.shop.oms.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.work.shop.oms.bean.ChannelShop;

public class ConstantsUtil {

	/**
	 * Object 转换为 String
	 * @param obj
	 * @return
	 */
	public static String obj2Str(Object obj){

		return obj == null ? StringUtils.EMPTY : StringUtils.trimToEmpty(obj.toString());
	}
    
	
	/**
	 * Object 转换为 Integer
	 * @param obj
	 * @return
	 */
	public static Integer obj2Int(Object obj){

		return obj == null||StringUtils.isBlank(obj.toString()) ? Integer.valueOf(0) : Integer.valueOf(obj.toString());
	}
	
	
	/**
	 * Object 转换为 Double
	 * @param obj
	 * @return
	 */
	public static Double obj2Dbe(Object obj){
		
		return obj == null||StringUtils.isBlank(obj.toString()) ? Double.valueOf(0.0d) : Double.valueOf(obj.toString());
	}
	/**
	 * Double 转换为 BigDecimal
	 * @param obj
	 * @return
	 */
	public static BigDecimal obj2Bigdecimal(Double obj){
		
		return obj == null ? BigDecimal.valueOf(0.0d) : BigDecimal.valueOf(obj);
	}
	
	/**
	 * Object 转换为 Date
	 * @param obj
	 * @return
	 * @throws ParseException 
	 */
	public static Date obj2Date(Object obj) throws ParseException{

		return obj == null||StringUtils.isBlank(obj.toString()) ? null : DateUtils.parseDate(StringUtils.trim(obj.toString()),new String[]{ "yyyy-MM-dd HH:mm:ss"});
	}
	
	/**
	 * 获取退款到帐日期信息
	 * @param returnPay 退款方式
	 * @return
	 */
	public static String getRefundTime(int returnPay, int transType) {
		switch(returnPay) {
		case 0: return "1个工作日";		//退款到个人账户
		case 1: return "1-2个工作日"; 		//退款到支付宝
		case 2: if(transType == 2) return "2-7个工作日"; 		//退款到银行账户(货到付款)
		case 3: ; 		//退款到财付通
		case 4: ; 		//退款到银联支付
//		case 5: ; 		//
		case 6: ; 		//退款到招商银行
		case 7: ; 		//退款到贝宝支付
		case 8: ; 		//退款到快钱人民币网关
		case 9: ; 		//退款到一号店
//		case 10: ; 		//无须退款
		case 14: ; 		//退款到汇付天下
		case 15: ; 		//退款到手机银联
		case 16: return "1-2个工作日";  //京东平台退款
		default: return "银联借记卡支付:2-5个工作日;银联信用卡支付:7-15个工作日";
		}
	}
	
	// 默认仓库短信模板
		private static String SHANGHAI_NAME = "上海仓库";
		private static String GUANGZHOU_NAME = "广州仓库";
		private static String TIANJIN_NAME = "天津仓库";

		// private static String SHANGHAI_MSG =
		// "退换货地址:上海市浦东新区六灶镇鹿达路86号平台B2C物流管理部退货组。邮编:201323;收件人:徐祖广;电话:02138119999—3662 或3616。请放入发货清单,不支持平邮和到付.如有问题请联系客服4008219988,谢谢!";
		// private static String GUANGZHOU_MSG =
		// "退换货地址:广州增城市新塘镇巷口村广本东路广百骏盈现代物流园A6库。邮编:511300;联系人:何正辉;电话:020-32633999转8814或8818.请放入发货清单,不支持平邮和到付.如有问题请联系客服4008219988,谢谢!";

		private static String SHANGHAI_MSG = "退换货地址:上海市浦东新区六灶镇鹿达路86号平台B2C物流管理部退货组。邮编:201323;收件人:徐祖广;电话:021-38119999-3534或3533。请放入发货清单,不支持平邮和到付.如有问题请联系客服4008219988,谢谢!";
		private static String GUANGZHOU_MSG = "退换货地址:广州增城市新塘镇巷口村广本东路广百骏盈现代物流园A5-4库。邮编:511300;联系人:吕祥辉;电话:15918636908.请放入发货清单,不支持平邮和到付.如有问题请联系客服4008219988,谢谢!";
		private static String TIANJIN_MSG = "退换货地址:天津市东丽区空港物流加工区西六道普洛斯物流园美邦服饰。邮编:300300;联系人:张爱先;电话:15002212896.请放入发货清单,不支持平邮和到付.如有问题请联系客服4008219988,谢谢!";

		/**
		 * 格式化退货短信模板
		 * 
		 * @param shop
		 * @return
		 */
		public static String returnOrderMobileMsg(ChannelShop shop) {
			if (shop == null) {
				return StringUtils.EMPTY;
			}
			// 平台退换货地址:上海市浦东新区六灶镇鹿达路86号平台B2C物流管理部退货组。邮编:201323;收件人:徐祖广;电话:02138119999—3662
			// 或3616.请放入发货清单,不支持平邮和到付.如有问题请联系客服4008219988,谢谢!
			StringBuilder mobileMsg = new StringBuilder();
			mobileMsg.append("退换货地址:");
			if (StringUtils.isBlank(shop.getShopAddress())) {
				shop.setShopAddress(" ");
			}
			if (StringUtils.isBlank(shop.getShopLinkman())) {
				shop.setShopLinkman(" ");
			}
			if (StringUtils.isBlank(shop.getShopTel())) {
				shop.setShopTel(" ");
			}
			if (StringUtils.isNotBlank(shop.getShopCode())) {
				shop.setShopCode("(" + shop.getShopCode() + ")");
			}
			mobileMsg.append(shop.getShopAddress()).append(";");
			mobileMsg.append("收件人:").append(shop.getShopLinkman()).append(";");
			mobileMsg.append("电话号码:").append(shop.getShopTel()).append(".");
			mobileMsg.append("请放入发货清单,不支持平邮和到付.如有问题请联系客服4008219988,谢谢!");
			return mobileMsg.toString();
		}

		/**
		 * 默认加载短信模板
		 * 
		 * @param shopList
		 * @return
		 */
		public static List<ChannelShop> loadDefaultData(List<ChannelShop> shopList) {
			ChannelShop shop = new ChannelShop();
			shop.setShopTitle(SHANGHAI_NAME);
//			shop.setMobileMsg(SHANGHAI_MSG);
			shopList.add(shop);
			shop = new ChannelShop();
			shop.setShopTitle(GUANGZHOU_NAME);
//			shop.setMobileMsg(GUANGZHOU_MSG);
			shopList.add(shop);
			shop = new ChannelShop();
			shop.setShopTitle(TIANJIN_NAME);
//			shop.setMobileMsg(TIANJIN_MSG);
			shopList.add(shop);
			return shopList;
		}
		
		
		/**
		 * OS结算单据编码-日期+随机编码
		 */
		private static int systemCode = 0;
		public synchronized static String getSettleBillCode() {
			String str = "";
			systemCode += 1;
			if (systemCode > 0 && systemCode < 10) {
				str = "000" + systemCode;
			}
			if (systemCode >= 10 && systemCode < 100) {
				str = "00" + systemCode;
			}
			if (systemCode >= 100 && systemCode < 1000) {
				str = "0" + systemCode;
			}
			if (systemCode >= 1000 && systemCode < 10000) {
				str = "" + systemCode;
			}
			if (systemCode >= 10000) {
				systemCode = 0;
				str = "0001";
			}
			if (StringUtils.isNotBlank(str)) {
				str = TimeUtil.format3Date(new Date()) + str;
			}
			return str;
		}
}
