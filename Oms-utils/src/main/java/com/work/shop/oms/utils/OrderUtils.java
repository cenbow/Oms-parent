package com.work.shop.oms.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.work.shop.oms.common.bean.ConstantValues;


/**
 * 订单相关工具类
 * @author 吴健 HQ01U8435
 *
 */
public class OrderUtils {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyMMdds");
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00000");

	/**
	 * 根据自增id，生成定长的退单单号
	 * @param autoId
	 * @return
	 */
	public static String mergeAutoId(Integer autoId) {
		String returnSn = "";
		
		/*String currentDay = DATE_FORMAT.format(new Date());
		
		returnSn = currentDay + DECIMAL_FORMAT.format(autoId % 100000);
		
		returnSn = ConstantValues.GENERATE_PRE_ORDER_RETURN + returnSn;*/
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyMMdd");
		DecimalFormat decimalFromat = new DecimalFormat("00000");
		String currentDay = df.format(date);
		String ncode = "";
		int odd = 0;
		int even = 0;
		returnSn = currentDay + decimalFromat.format(autoId % 100000);
		ncode = "0" + returnSn;
		for (int i = 0; i < 12; i++) {
			if (1 == i % 2) {
				odd += ncode.charAt(i);
			} else {
				even += ncode.charAt(i);
			}
		}
		returnSn = ConstantValues.GENERATE_PRE_ORDER_RETURN + returnSn + (10 - ((odd * 3 + even) % 10)) % 10;
		
		return returnSn;
	}

	public static String generateRefundSn(String orderReturnSn,int payCount) {
		try {
			return ConstantValues.GENERATE_PRE_ORDER_REFUND + orderReturnSn.substring(2) +  DECIMAL_FORMAT.format(payCount+1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
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
	public static int payIdToReturnPay(int payId) {
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
		case 16: return 16;	// 退款到京东渠道
		case 17: return 17; // 退款到苏宁支付
		case 18: return 18; // 退款到LC风格网
		case 19: return 19; // 退款到微信支付
		default: return 0;   //其余 ---> 0 退款到个人账户
		}
	}
	
	/**
	 * 随机一个数字字符串，最大10位
	 * @param len
	 * @return
	 */
	public static String randomStrNumber(int len) {
		Random r = new Random();
		
		String temp = "0000000000" + r.nextInt(999999999);
		
		return temp.substring(temp.length() - len);
	}

	public static void main(String[] args) {
		System.out.println(randomStrNumber(10));
	}
}
