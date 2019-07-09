package com.work.shop.oms.common.bean;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 公共方法类
 * @author zcl
 *
 */
public class CommonUtil {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(CommonUtil.class);
	/**
	 * 字符串转化为int型，为空或含有非数字的字符串转换为0
	 * @param value
	 * @return
	 */
	public static long StrToLong(String value) {
		if(value == null || value.equals("")) {
			return 0;
		} else {
			boolean b = true;
			if(b == false) {
				return 0;
			} else {
				return Long.valueOf(value);
			}
		}
	}

	/**
	 * 日期转化为字符串型，NULL转换为""
	 * @param Date 日期
	 * @return String 日期字符串（"yyyy-MM-dd HH:mm:ss"）
	 */
	public static String DateToStr(Date date) {
		if(date == null ) {
			return "";
		} else {
			java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(date);
		}
	}

	/**
	 * 数字转为字符串型，NULL转换为"0"
	 * @param  Object 数字
	 * @return String 数字字符串
	 */
	public static String NumToStr(Object obj) {

		if(obj == null ) {
			return "0";
		} else if (obj instanceof Short || obj instanceof Integer 
				|| obj instanceof Long || obj instanceof Float 
				|| obj instanceof Double || obj instanceof Byte){
               return obj.toString();
		} else {
			return "";
			
		}
		
	}

	/**
	 * 取得updateType
	 * @param action
	 */
	public static String getOrderUpdateType(String action) {
		if(action.equals("confirm")) {
			return "0;";
		} else if(action.equals("pay")) {
			return "0;";
		} else if(action.equals("consignee")) {
			return "1;";
		} else if(action.equals("shipping")) {
			return "2;";
		} else if(action.equals("addGoods")) {
			return "3;";
		} else if(action.equals("updateGoods")) {
			return "3;";
		} else if(action.equals("deleteGoods")) {
			return "3;";
		} else if(action.equals("money")) {
			return "4;";
		} else if(action.equals("suspend")) {
			return "5;";
		} else if(action.equals("removeSuspnd")) {
			return "0;";
		} else if(action.equals("question")) {
			return "5;";
		} else if(action.equals("removeQuestion")) {
			return "0;";
		} else if(action.equals("cancel")) {
			return "6;";
		} else if(action.equals("invalid")) {
			return "6;";
		} else if(action.equals("payFinish")) {
			return "7;";
		} else {
			return "";
		}
	}
	
	/**
	 * 从字符串中取得排在前面的数字,列如“中介费的额4865438DAET468435”，会取得"4865438".
	 * 
	 * @param str 字符串
	 * @return 数字字符串
	 */
	public static String getNumInStr(String str) {
		int begin = -1;
		int end = -1;
		StringBuffer numSb = new StringBuffer("");
		if(str != null && !str.trim().equals("")) {
			byte[] bytes = str.getBytes();
			for(int i=0; i<bytes.length; ++i) {
				if(bytes[i] >= 48 && bytes[i] <= 57) { //48-->0 ... 57-->9
					if(begin < 0) {
						begin = i;
					} 
					end = i;
				} else if(begin > 0) {
					break;
				}
					
			}
			if(begin >= 0 && end > begin) {
				for(int b = 0; b<(end - begin+1); b++) {
					numSb.append((char)bytes[begin+b]);
				}
			}
		}
		
		return numSb.toString();
	}
	
	/**
	 * 根据订单来源和用户下单时的用户等级取得渠道会员等级的汉字表示
	 * @param orderFrom 订单来源
	 * @param levelId 下单时会员的等级
	 * @return 渠道会员等级的汉字表示
	 */
	public static String getChannelUserLevel(String orderFrom, int levelId) {
		if(orderFrom == null || orderFrom.equals("")) {
			return "";
		} else if(orderFrom.equals("平台官方网店")) {
			switch(levelId) {
			case 0: return "普通会员";
			case 11: return "VIP";
			case 21: return "SVIP";
			case 99: return "内部员工";
			case 100: return "内部员工VIP";
			case 101: return "内部员工SVIP";
			default : return "";
			}
		} else if(orderFrom.equals("美特斯邦威淘宝官方网店") 
				|| orderFrom.equals("淘宝AMPM店")
				|| orderFrom.equals("淘宝MC店")
				|| orderFrom.equals("米喜迪淘宝旗舰店")
				|| orderFrom.equals("chin祺旗舰店")) {
			switch(levelId) {
			case 1: return "普通会员";
			case 2: return "高级会员";
			case 3: return "VIP会员";
			case 4: return "至尊vip会员";
			default : return "";
			}
		} else if(orderFrom.equals("美特斯邦威拍拍官方网店")) {
			switch(levelId) {
			default: return "";
			}
		} else {
			return "";
		}
	}
	
	/**
	 * 物流问题单解决方装换。汉字转换成整型
	 * 
	 * @param solverStr
	 * @return
	 */
	public static int solverStrToint(String solverStr) {
		
		if(solverStr == null || "".equals(solverStr) || solverStr.equals("未指定处理者")) {
			return 0;
		} else if(solverStr.equals("IT")) {
			return 1;
		} else if(solverStr.equals("物流")) {
			return 2;
		} else if(solverStr.equals("客服")) {
			return 3;
		} else {
			return 0;
		}
		
	}
	
	/**
	 * 物流问题单解决方装换。整型转换成汉字
	 * 
	 * @param solverStr
	 * @return
	 */
	public static String solverIntToStr(int solverInt) {
		switch(solverInt) {
		case 0: return "未指定处理者";
		case 1: return "IT";
		case 2: return "物流";
		case 3: return "客服";
		default: return "";
		}
	}
	
	/**
	 * 物流问题单客服处理结果
	 * @param num
	 * @return
	 */
	public static String resultDoWithIntToStr(int num) {
		switch(num) {
		case 0: return "未处理";
		case 1: return "订单取消";
		case 2: return "取消无货发有货";
		case 3: return "未联系上";
		case 4: return "无人接听";
		case 5: return "关机";
		case 6: return "拒听";
		case 7: return "非本人接听";
		case 8: return "客户未确认";
		case 9: return "投诉升级";
		case 10: return "修改商品,发货";
		case 11: return "订单已发货";
		case 12:  return "返回正常单";
		default: return "未处理";
		}
	}
	
	/**
	 * 物流问题单处理状态
	 * @param num
	 * @return
	 */
	public static String processStatusIntToStr(int num) {
		switch(num) {
		case 0: return "待处理";
		case 1: return "已处理";
		case 2: return "待跟进";
		default: return "待处理";
		}
	}
	
	/**
	 * 四舍五入算法。
	 * @param rate 保留小数点后几位
	 * @param num 要计算的数字
	 * @return
	 */
	public static double roundingOff(int rate, double num) {
		BigDecimal   b1   =   new   BigDecimal(num);
		double   d   =   b1.setScale(rate,   BigDecimal.ROUND_HALF_UP).doubleValue();
		return d;
	}
	
	/**
	 * 付款方式转与退款方式的转变
	 * 
	 * @param payId 付款方式
	 * @return 退款方式
	 * 
	 * 
	 * 与constant.js 363行
	 * editExtraReturnOrder.jsp  67行
	 * OrderRefundInfo.js 113行
	 * addExtraReturnOrder.jsp  58
	 * addReturnOrder.jsp  64
	 * editExtraReturnOrder.jsp  77
	 * editReturnOrder.jsp  81
	 * exchangeOrderInfo.jsp  438 465 474 1431
	 * orderRefundList.jsp 110
	 * 
	 */
	/*public static int payIdToReturnPay(int payId) {
		switch(payId) {
		case 1: return 1;   //支付宝---> 1 退款到支付宝
//		case 2: return 5;    //网银在线 --->
//		case 4: return 0;    //货到付款--->0 退款到个人账户
		case 4: return 2;    //货到付款--->0 退款到银行账户  2012.10.22 ORDER-537
		case 5: return 4;    //银联电子支付 ---> 4 退到银联支付
		case 6: return 3;    //财付通（财付通建行支付100%送Q币）--> 3退款到财付通
		case 7: return 8;   //快钱人民币网关--->8 快钱人民币网关
		case 8: return 2;   //银行汇款/转帐--->2 退款到银行账户
		case 11: return 7;   //贝宝支付 ---> 7 退款到贝宝支付
		case 12: return 6;   //招商银行 ---> 6 退款到招商银行  (为招商银行。
//		case 13: return 6;   //招商银行 ---> 6 退款到招商银行  (130数据库中system_payment中13为招商银行。101中却是12
		case 13: return 9;	// 一号店 ---> 9 一号店
		case 14: return 14; // 退款到汇付天下
		case 15: return 15; // 退款到手机银联
		case 16: return 16; // 退款到手机银联
		default: return 0;   //其余 ---> 0 退款到个人账户
		}
	}*/
	
	/**
	 * 退款方式转换成相应的汉词
	 * 
	 * @param returnPay  退款方式
	 * @return  
	 */
	public static String returnPayToStr(int returnPay) {
		switch(returnPay) {
		case 0: return "退款到个人账户";  
		case 1: return "退款到支付宝";
		case 2: return "退款到银行账户";
		case 3: return "退款到财付通"; 
		case 4: return "退到银联支付";   
		case 6: return "为招商银行";
		case 7: return "退款到贝宝支付";
		case 8: return "退款到快钱人民币网关";
		case 9: return "退款到一号店";
		case 14: return "退款到汇付天下";
		case 10: return "无须退款";
		case 15: return "退款到手机银联";
		case 16: return "退款到京东平台";
		default : return "未知退款方式";
		}
	}
	
	/**
	 * 渠道code转换为name
	 * 
	 * @param code
	 * @return
	 */
	public static String channelCodeToName(String code) {
		if(code == null || "".equals(code.trim())) {
			return "未知渠道";
		} else if(code.equals("A02588S001")) {
			return "LC风格网";
		} else if(code.equals("A02588S003")) {
			return "美特斯邦威淘宝官方网店";
		} else if(code.equals("A02588S004")) {
			return "美特斯邦威拍拍官方网店";
		} else if(code.equals("A02588S002")) {
			return "EBAY官方旗舰店";
		} else if(code.equals("A02588S005")) {
			return "淘宝AMPM店";
		} else if(code.equals("A02588S006")) {
			return "淘宝MC店";
		} else if(code.equals("A02588S007")) {
			return "手机应用渠道";
		} else if(code.equals("A02588S008")) {
			return "米喜迪淘宝旗舰店";
		} else if(code.equals("A02588S009")) {
			return "chin祺旗舰店";
		} else if(code.equals("A02588S010")) {
			return "美特斯邦威拍拍女装旗舰店";
		}else if(code.equals("A02588S018")) {
			return "京东ME&CITY官方旗舰店";
//		}else if(code.equals(Constant.OS_CHANNEL_LC)) {
//			return "LC风格网";
		} else {
			return "未知渠道";
		}
	}
	
	/**
	 * 渠道name转换成code
	 * @param name
	 * @return
	 */
	public static String channelNameToCode(String name) {
		if(name == null || "".equals(name.trim())) {
			return "";
		} else if(name.equals("LC风格网")) {
			return "A02588S001";
		} else if(name.equals("美特斯邦威淘宝官方网店")) {
			return "A02588S003";
		} else if(name.equals("美特斯邦威拍拍官方网店")) {
			return "A02588S004";
		} else if(name.equals("EBAY官方旗舰店")) {
			return "A02588S002";
		} else if(name.equals("淘宝AMPM店")) {
			return "A02588S005";
		} else if(name.equals("淘宝MC店")) {
			return "A02588S006";
		} else if(name.equals("淘宝MC店")) {
			return "A02588S007";
		} else if(name.equals("米喜迪淘宝旗舰店")) {
			return "A02588S008";
		} else if(name.equals("chin祺旗舰店")) {
			return "A02588S008";
		} else if(name.equals("美特斯邦威拍拍女装旗舰店")) {
			return "A02588S010";
		}else if(name.equals("京东ME&CITY官方旗舰店")) {
			return "A02588S018";
		}else if(name.equals("LC风格网")) {
			return "A02588S001";
		}  else {
			return "";
		}
	}
	
	/**
	 * 订单状态int值转换成字符串
	 * @param orderStatus
	 * @return
	 */
	public static String orderStatusForIntToStr(int orderStatus) {
		switch(orderStatus) {
		case 0: return "未确认";
		case 1: return "已确认";
		case 2: return "已取消";
		case 3: return "无效";
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
	 * 退单的结算类型状态值转换成字符串
	 * @param type
	 * @return
	 */
	public static String returnSettlementTypeForIntToStr(int type) {
		switch(type) {
		case 0: return "";
		case 1: return "预付款";
		case 2: return "保证金";
		default: return "未知";
		}
	}
	
	
	
	/**
	 * linux系统获取本机内网地址
	 */
	public static String getLocalIP() {
		String ip = "";
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				if (ni.getName().equals("eth0")) {
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = ips.nextElement().getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return ip;
	}
	
	/**
	 * 将判断是否被当前用户绑定的状态转变成信息
	 * @param judgeFlag
	 * @return
	 */
	public static String getJudgeLockStatusMeg(int judgeFlag) {
		switch(judgeFlag) {
		case -2: return "没有此用户名的信息";
		case -1: return "当期数据表中没有找到此订单";
		case 0: return "此订单未绑定";
		case 2: return "此订单被此操作者锁定";
		case 3: return "判断此订单是否被操作者锁定时异常";
	    default: return "没有被此用户绑定,返回此订单被绑定用户的ID为:"+judgeFlag;
		}
	}	
	
	/**
	* md5加密方法
	* @param plainText 加密字符串
	* @return String 返回32位md5加密字符串(16位加密取substring(8,24))
	*/
	public final static String md5(String plainText) {
		// 返回字符串
		String md5Str = null;
		
		try {
		    // 操作字符串
		    StringBuffer buf = new StringBuffer();

		   /**
		    * MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
		    * 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
		    * 
		    * MessageDigest 对象开始被初始化。
		    * 该对象通过使用 update()方法处理数据。
		    * 任何时候都可以调用 reset()方法重置摘要。
		    * 一旦所有需要更新的数据都已经被更新了，应该调用digest()方法之一完成哈希计算。 
		    * 
		    * 对于给定数量的更新数据，digest 方法只能被调用一次。
		    * 在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。
		    */ 
		    MessageDigest md = MessageDigest.getInstance("MD5");
		   
		    // 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
		    md.update(plainText.getBytes());
	
		    // 计算出摘要,完成哈希计算。
		    byte b[] = md.digest();
		    int i;
	
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
	
			    if (i < 0) {
			    	i += 256;
			    }
	
			    if (i < 16) {
			    	buf.append("0");
			    }
	
			    // 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
			    buf.append(Integer.toHexString(i));
			}
	
			// 16位的加密
			//md5Str = buf.toString().substring(8,24);
			// 32位的加密
			md5Str = buf.toString();


		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return md5Str;
	}
	
	/**
	 * 订单支付方式 
	 * @param int orderPayStatus 订单支付状态
	 * @return String
	 ***/
	public static String orderPayStatusForIntToStr(int orderPayStatus) {
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
	public static String shipStatusForIntToStr(int shipStatus) {
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
	 * 财务状态 （0，未结算；1，已结算；2，待结算）
	 ***/
	public static String payStatus(int shipStatus) {
		
		switch(shipStatus) {
			case 0: return "未结算";
			case 1: return "已结算";
			case 2: return "待结算";
			default:return "未知";
		}
	}

	/**
	 * 物流状态 （0，未收货；1，已收货,未入库；2，已入库；3，可入库；4，快递收货,仓库未收货）
	 ***/
	public static String shipStatus(int shipStatus) {
		
	/*	if(null ==shipStatus){
			return "";
		}*/

		switch(shipStatus) {
	   	    case 0: return "未收货";
			case 1: return "已入库";
			case 2: return "待入库";
			case 3: return "可入库";
			case 4: return "快递收货,仓库未收货";
			default:return "未知";
		}
	}
	
	//是否入库:0未入库 1已入库 2待入库）',
	public static String checkinStatus(Byte checkinStatus) {
		
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

	//是否收货:0.未收货，1.已收货
	public static String isGoodReceived(Byte isGoodReceived) {
		
		if(null == isGoodReceived){
			return "";
		}

		switch(isGoodReceived) {
	   	    case 0: return "未收货";
			case 1: return "已收货";
		
			default:return "未知";
		}
	}
	
	//'退单质检状态 （0质检不通过、1质检通过）',
	public static String qualityStatus(Integer isGoodReceived) {
			
			if(null == isGoodReceived){
				return "";
			}

			switch(isGoodReceived) {
		   	    case 0: return "质检不通过";
				case 1: return "质检通过";
			
				default:return "未知";
			}
	}
	
	//是否退款(0无需退款，1需要退款)
	public static String haveRefund(Byte haveRefund) {
			
			if(null == haveRefund){
				return "";
			}

			switch(haveRefund) {
		   	    case 0: return "无须退款";
				case 1: return "需要退款";
			
				default:return "未知";
			}
	}
	
}
