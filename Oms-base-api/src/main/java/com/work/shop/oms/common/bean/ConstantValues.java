package com.work.shop.oms.common.bean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;



/**
 * 常量类
 * 
 * 
 */
public class ConstantValues {
	
	
	/**系统用户*/
	public static final String ACTION_USER_SYSTEM = "system";
	
	/** YES OR NO*/
	public static final Integer YESORNO_YES = 1;
	public static final Integer YESORNO_YES_YES = 2;
	public static final Integer YESORNO_NO = 0;
	public static final Integer YESORNO_FAIL = -1;

	/**
	 * 邦购客服电话
	 */
	public static final String BANGGO_CUST_TEL = "400-821-9988";
	/**
	 * 渠道授权KEY
	 */
	public static final String CH_SC_AU_ = "CH_SC_AU_";
	
	/**
	 * 淘宝预售状态
	 */
	public static final String TOP_TRADE_TYPE = "step";
	/**
	 * 淘宝预售、大促时拉入的部分订单type可能等于step，step_trade_status！=FRONT_PAID_FINAL_PAID的话 就禁止转入
	 */
	public static final String FRONT_PAID_FINAL_PAID = "FRONT_PAID_FINAL_PAID";
	
	
	
	/**京东转单转入状态*/
	public static String JD_OS_ORDER_STATA = "WAIT_SELLER_STOCK_OUT";
	/**淘宝转单转入状态*/
	public static String TOP_OS_ORDER_STATA = "WAIT_SELLER_SEND_GOODS";
	/**一号店转单转入状态*/
	public static String YHD_OS_ORDER_STATA_TODO = "ORDER_TRUNED_TO_DO";
	/**苏宁店转单转入状态*/
	public static String SUNING_OS_ORDER_STATA_TOSHIP = "10";
	/**苏宁分销装入状态*/
	public static String SNFX_OS_ORDER_STATA_TOSHIP = "10";
	/**
	 * 有赞订单装入状态
	 */
	 public static String  YZ_OS_ORDER_STATA_TOSHIP = "WAIT_SELLER_SEND_GOODS";
	/**贝贝网转单转入状态*/
	public static String BB_OS_ORDER_STATA = "1";
	/**蘑菇街网转单转入状态*/
	public static String MGJ_OS_ORDER_STATA = "ORDER_PAID";  //已付款
	
	/**楚楚街拉单状态*/
	public static Integer CHUCHUJIE_OS_ORDER_STATA = 2;  //已付款
	/**蚂蚁手店拉单转入状态*/
	public static String MAYI_TO_SEND_GOODS = "2";  //已代发货
	
	public static Integer MEILISHUO_OS_ORDER_STATA = 2;  //已付款
	
	public static String MGJ_OS_TRADE_STATUS = "ORDER_PAID";  //已付款
	/**
	 *蚂蚁订单状态 
	 ***/
	public static Integer mayi_OS_TO_ORDER = 1;  //订单处理状态（0，未确认；1，已确认；2，交易未付款关闭；4：交易未发货关闭；5，交易正常完成；）
	/**
	 *蚂蚁订单状态 
	 ***/
	public static Integer MAYI_OS_PAY_STATUS = 2; //支付状态(0，未付款；2，已付款；3，待结算；4，已结算)
	
	public static final String ORDER_GOODS_GIFT = "gift";
	public static final String ORDER_GOODS_FD = "bag";
	public static String YHD_OS_ORDER_STATA_PAYED = "ORDER_PAYED";
	
	public static String  MEILISHUO_OS_TO_ORDER = "ORDER_PAID";  //订单状态，包括：ORDER_CREATED": "已下单",ORDER_CANCELLED": "订单取消",ORDER_PAID":
 //   "已支付",ORDER_SHIPPED": "已发货",ORDER_RECEIVED": "已收货",ORDER_COMPLETED": "订单完成",ORDER_CLOSED":
//    "订单关闭"

	public static String TOP_UID(String n) {
		return "tb_" + n;
	}
	public static String JD_UID(String n) {
		return "jd_" + n;
	}

	public static String SN_UID(String n) {
		return "sn_" + n;
	}

	public static String DD_UID(String n) {
		return "dd_" + n;
	}
	public static String TOP_FX_UID(String n) {
		return "top_fx_" + n;
	}
	public static String WEIXIN_UID(String n) {
		return "weixin_" + n;
	}
	public static String SUNING_UID(String n) {
		return "suning_" + n;
	}
	
	public static String YZ_UID(String n) {
		return "yz_" + n;
	}
	
	public static String SUNINGFX_UID(String n) {
		return "snfx_" + n;
	}
	
	public static String yike_UID(String n){
		return "yike_"+n;
	}
	
	public static String Other_UID(String n){
		return "other_"+n;
	}
	
	public static String MoGuJie_UID(String n){
		return "mgj_"+n;
	}
	
	public static String MaYi_UID(String n){
		return "mayi_"+n;
	}
	
	public static String MeiLiShuo_UID(String n){
		return "meilishuo_"+n;
	}
	
	public static String Chuchujie_UID(String n){
		return "chuchujie_"+n;
	}
	
	public static String Pdd_UID(String n){
		return "Pdd_"+n;
	}
	

	public static BigDecimal toBigDecimal(String f) {
		try {
			if(StringUtils.isNotBlank(f)) {
				return new BigDecimal(f);
			}
			return new BigDecimal(0);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	public static BigDecimal toBigDecimal(double d) {
		try {
			return new BigDecimal(d);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	public static String pickNum(String v) {
		int index = 0;
		if((index = v.indexOf("-")) > 0) {
			return v.substring(0, index);
		}
		return v;
	}

	public static String notNull(String s) {
		return s == null ? "" : s;
	}
	
	/***
	 * 问题单拼接处理
	 * @param questionCode
	 */
	public static void setQuestionCode(MasterOrder masterOrder,String questionCode){
		if(StringUtils.isBlank(questionCode)){
			return ;
		}
		StringBuilder questionSb = new StringBuilder();
		if(StringUtils.isNotBlank(masterOrder.getQuestionCode())){
			questionSb.append(masterOrder.getQuestionCode()).append(",");
		}
		questionSb.append(questionCode).append(",");
		questionSb.deleteCharAt(questionSb.length() - 1);
		masterOrder.setQuestionCode(questionSb.toString());
	}
	
	/**
	 * 淘宝退单转问题单逻辑
	 */
	public static final String QUESTION_REFUND_CODE = "19";
	public static final String QUESTION_REFUND_NAME = "淘宝退款转问题单";
	
	//manual  Converter
	public static String MANUAL_CONVERTER_HAVE_TO_PAY = "1";
		
	public static String MANUAL_CONVERTER_DID_NOT_PAY = "0";
	
	/**
	 * 地区级别
	 */
	public static final String CHANNEL_AREA_TYPE_COUNTRY = "0";//国家
	public static final String CHANNEL_AREA_TYPE_PROVICE = "1";//省份
	public static final String CHANNEL_AREA_TYPE_CITY = "2";//城市
	public static final String CHANNEL_AREA_TYPE_DISTINCT = "3";//区县
	
	
	/**
	 * 淘宝退单记录-状态标记
	 * -1:不需要设问题单或者进行ag自动退款 、
	 * 0：未设问题单或自动生成退款单 
	 * 1：设置问题单成功
	 *  2:设置问题单失败 
	 *  3:删除商品失败，尝试设置问题单
	 *   4:删除商品成功，请求ag接口失败  
	 *    5:请求ag退款成功 
	 *     6:ag退款成功
	 *      7:退单完成结算
	 */
	
	/***
	 * 退单设置-过期时间
	 */
	public static Integer TOP_REFUND_OVERDATE = 2;
	public static String TOP_REFUND_TIMEOUT = "超时未设置";
	
	public static final byte TOP_REFUND_FAILED = -1;//处理失败
	public static final byte TOP_REFUND_NOPROCESS = 0;//未处理
	public static final byte TOP_REFUND_PROCESSED = 1;//已处理
	
	public static final byte TOP_REFUND_2 = 2;//后续处理2
	public static final byte TOP_REFUND_3 = 3;//后续处理3
	public static final byte TOP_REFUND_4 = 4;//后续处理4
	
	
	public static final String TOP_REFUND_NULL = "-1";//无需处理
	public static final String TOP_REFUND_QUESTION = "0";//设置问题单
	public static final String TOP_REFUND_REFUND= "1";//设置退款单
	public static final String TOP_REFUND_RETURN= "2";//设置退货单
	public static final String TOP_REFUND_SUCCESS= "SUCCESS";//设置退货单
	public static final String TOP_REFUND_EXTRAREFUND= "3";//设置额外退款单
	
	
	
	public static final String FULL_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
	public static final String TOP_FENXIAO_CHANNEL_CODE = "HQ01S999";
	public static final String TOP_FENXIAO_DEFAULT_CHANNEL_CODE = "HQ01S219";
	public static final String TOP_FENXIAO_DEFAULT_CHANNEL_NAME = "分销默认渠道";

	//渠道前缀
	public static final String REFUND_JOB_PRE = "ref_"; //退单前缀
	public static final String DOWNLOAD_JOB_PRE = "dl_"; //拉单前缀
	public static final String CONVERT_JOB_PRE = "convert_"; //转单前缀
	public static final String DELIVERY_JOB_PRE = "delivery_"; //发货前缀
	public static final String WAREHOUSE_JOB_PRE="warehouse_";//分配前缀
	public static final String QUESTION_SHORTNAME = "question_";//设置问题单前缀
	public static final String REFUND_SHORTNAME="refund_";//设置退款单单前缀
	public static final String RETURN_SHORTNAME="return_";//设置退货单前缀
	public static final String EXTRAREFUND_SHORTNAME="extrarefund_";//设置额外退款单前缀
	public static final String SETTLEMENT_SHORTNAME="settlement_";//结算
	
	
	//渠道简称
	public static final String JD_SHORTNAME="jd";
	public static final String FENXIAO_SHORTNAME="fx";
	public static final String TAOBAO_SHORTNAME="tb";
	public static final String YIHAODIAN_SHORTNAME = "yi";
	public static final String WEIXIN_SHORTNAME = "wx";
	public static final String DANGDANG_SHORTNAME = "dd";
	public static final String SUNING_SHORTNAME = "sn";
	public static final String BEIBEI_SHORTNAME = "bb";
	public static final String MOGUJIE_SHORTNAME = "mgj";
	public static final String OTHER_SHORTNAME = "other";
	public static final String MAYI_SHORTNAME = "mayi";	
	public static final String MEILISHUO_SHORTNAME = "meilishuo";
	public static final String CHUCHUJIE_SHORTNAME = "chuchujie";
	public static final String VIP_SHORTNAME = "vip";
	public static final String MBGW_SHORTNAME = "mbgw";
	public static final String SNFX_SHORTNAME = "snfx";
	public static final String PDD_SHORTNAME = "pdd";
	public static final String YZ_SHORTNAME = "yz";
	
	public static final String YK_SHORTNAME = "yk";
	
	
	
	//渠道下载简称
	public static final String JD_DL_SHORTNAME=DOWNLOAD_JOB_PRE+JD_SHORTNAME;
	public static final String FENXIAO_DL_SHORTNAME=DOWNLOAD_JOB_PRE+FENXIAO_SHORTNAME;
	public static final String TAOBAO_DL_SHORTNAME=DOWNLOAD_JOB_PRE+TAOBAO_SHORTNAME;
	public static final String YIHAODIAN_DL_SHORTNAME =DOWNLOAD_JOB_PRE+YIHAODIAN_SHORTNAME;
	public static final String WEIXIN_DL_SHORTNAME = DOWNLOAD_JOB_PRE+WEIXIN_SHORTNAME;
	public static final String DANGDANG_DL_SHORTNAME = DOWNLOAD_JOB_PRE+DANGDANG_SHORTNAME;
	public static final String SUNING_DL_SHORTNAME = DOWNLOAD_JOB_PRE+SUNING_SHORTNAME;
	public static final String BEIBEI_DL_SHORTNAME = DOWNLOAD_JOB_PRE+BEIBEI_SHORTNAME;
	public static final String MOGUJIE_DL_SHORTNAME = DOWNLOAD_JOB_PRE+MOGUJIE_SHORTNAME;
	public static final String MAYI_DL_SHORTNAME = DOWNLOAD_JOB_PRE+MAYI_SHORTNAME;
	public static final String MEILISHUO_DL_SHORTNAME = DOWNLOAD_JOB_PRE+MEILISHUO_SHORTNAME;
	public static final String CHUCHUJIE_DL_SHORTNAME = DOWNLOAD_JOB_PRE+CHUCHUJIE_SHORTNAME;
	public static final String VIP_DL_SHORTNAME = DOWNLOAD_JOB_PRE+VIP_SHORTNAME;
	public static final String MBGW_DL_SHORTNAME = DOWNLOAD_JOB_PRE+MBGW_SHORTNAME;
	public static final String SNFX_DL_SHORTNAME = DOWNLOAD_JOB_PRE+SNFX_SHORTNAME;
	public static final String PDD_DL_SHORTNAME = DOWNLOAD_JOB_PRE+PDD_SHORTNAME;
	public static final String YZ_DL_SHORTNAME = DOWNLOAD_JOB_PRE+YZ_SHORTNAME;
	
	public static final String YK_DL_SHORTNAME = DOWNLOAD_JOB_PRE+YK_SHORTNAME;

	//渠道退单简称
	public static final String SNFX_REF_SHORTNAME=REFUND_JOB_PRE+SNFX_SHORTNAME;
	public static final String FENXIAO_REF_SHORTNAME=REFUND_JOB_PRE+FENXIAO_SHORTNAME;
	public static final String TAOBAO_REF_SHORTNAME=REFUND_JOB_PRE+TAOBAO_SHORTNAME;
	public static final String FENXIAO_QUESTION_SHORTNAME=QUESTION_SHORTNAME+FENXIAO_SHORTNAME;
	public static final String SNFENXIAO_QUESTION_SHORTNAME=QUESTION_SHORTNAME+SNFX_SHORTNAME;
	public static final String PROCESS_SHORTNAME=REFUND_JOB_PRE+"process";
	public static final String TAOBAO_QUESTION_SHORTNAME=QUESTION_SHORTNAME+TAOBAO_SHORTNAME;
	public static final String TAOBAO_TAOBAO_QUESTION_SHORTNAME=QUESTION_SHORTNAME+TAOBAO_SHORTNAME+TAOBAO_SHORTNAME;
	public static final String TAOBAO_REFUND_SHORTNAME=REFUND_SHORTNAME+TAOBAO_SHORTNAME;
	public static final String TAOBAO_RETURN_SHORTNAME=RETURN_SHORTNAME+TAOBAO_SHORTNAME;
	public static final String TAOBAO_TAOBAO_RETURN_SHORTNAME=RETURN_SHORTNAME+TAOBAO_SHORTNAME+TAOBAO_SHORTNAME;
	public static final String TAOBAO_EXTRAREFUND_SHORTNAME=EXTRAREFUND_SHORTNAME+TAOBAO_SHORTNAME;
	public static final String TAOBAO_REFUND_SETTLEMENT_SHORTNAME=REFUND_SHORTNAME+SETTLEMENT_SHORTNAME+TAOBAO_SHORTNAME;
	/**
	 * 转单Task类型
	 */
	public static String CONVERT_JOB_TYPE_TAOBAO = CONVERT_JOB_PRE + TAOBAO_SHORTNAME;
	public static String CONVERT_JOB_TYPE_DANGDANG = CONVERT_JOB_PRE + DANGDANG_SHORTNAME;
	public static String CONVERT_JOB_TYPE_JD = CONVERT_JOB_PRE + JD_SHORTNAME;
	public static String CONVERT_JOB_TYPE_TOPFX = CONVERT_JOB_PRE + FENXIAO_SHORTNAME;
	public static String CONVERT_JOB_TYPE_WEIXIN = CONVERT_JOB_PRE + WEIXIN_SHORTNAME;
	public static String CONVERT_JOB_TYPE_YHD = CONVERT_JOB_PRE + YIHAODIAN_SHORTNAME;
	public static String CONVERT_JOB_TYPE_SUNING = CONVERT_JOB_PRE + SUNING_SHORTNAME;
	public static String CONVERT_JOB_TYPE_SNFX = CONVERT_JOB_PRE + SNFX_SHORTNAME;      //自己加的，一个字段
	public static String CONVERT_JOB_TYPE_BEIBEI = CONVERT_JOB_PRE + BEIBEI_SHORTNAME;
	public static String CONVERT_JOB_TYPE_MOGUJIE = CONVERT_JOB_PRE + MOGUJIE_SHORTNAME; //蘑菇街
	public static String CONVERT_JOB_TYPE_OTHER = CONVERT_JOB_PRE + OTHER_SHORTNAME;
	public static String CONVERT_JOB_TYPE_MAYI = CONVERT_JOB_PRE + MAYI_SHORTNAME; //蚂蚁手店 
	public static String CONVERT_JOB_TYPE_MEILISHUO = CONVERT_JOB_PRE + MEILISHUO_SHORTNAME; // 美丽说
	public static String CONVERT_JOB_TYPE_CHUCHUJIE = CONVERT_JOB_PRE +CHUCHUJIE_SHORTNAME ; // 楚楚街
	public static String CONVERT_JOB_TYPE_VIP = CONVERT_JOB_PRE +VIP_SHORTNAME ; // 唯品会
	public static String CONVERT_JOB_TYPE_MBGW = CONVERT_JOB_PRE +MBGW_SHORTNAME ;
	public static String CONVERT_JOB_TYPE_PDD = CONVERT_JOB_PRE +PDD_SHORTNAME ;
	public static String CONVERT_JOB_TYPE_YZ = CONVERT_JOB_PRE +YZ_SHORTNAME ;
	
	/**
	 * 发货Task类型
	 */
	public static String DELIVERY_JOB_TYPE_TAOBAO = DELIVERY_JOB_PRE + TAOBAO_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_DANGDANG = DELIVERY_JOB_PRE + DANGDANG_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_JD = DELIVERY_JOB_PRE + JD_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_TOPFX = DELIVERY_JOB_PRE + FENXIAO_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_WEIXIN = DELIVERY_JOB_PRE + WEIXIN_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_YHD = DELIVERY_JOB_PRE + YIHAODIAN_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_SUNING = DELIVERY_JOB_PRE + SUNING_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_BB = DELIVERY_JOB_PRE + BEIBEI_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_SNFX = DELIVERY_JOB_PRE + SNFX_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_MOGUJIE = DELIVERY_JOB_PRE + MOGUJIE_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_MAYI = DELIVERY_JOB_PRE + MAYI_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_MEILISHUO = DELIVERY_JOB_PRE + MEILISHUO_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_CHUCHUJIE = DELIVERY_JOB_PRE + CHUCHUJIE_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_MBGW = DELIVERY_JOB_PRE + MBGW_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_PDD = DELIVERY_JOB_PRE + PDD_SHORTNAME;
	public static String DELIVERY_JOB_TYPE_YZ = DELIVERY_JOB_PRE + YZ_SHORTNAME;
	/**
	 * 分仓Task类型
	 */
	public static String WAREHOUSE_JOB_TYPE_TAOBAO = WAREHOUSE_JOB_PRE + TAOBAO_SHORTNAME;

	

	

	/**默认订单号回写模板*/
	public static String CALL_OS_DFAULT_NOTE = "| 此交易在%s已关联OS[待发货] 订单号:%s";

	/***
	 * 业务监控-拉单
	 */
	
	/** 京东渠道Code */
	public final static String JD_CHANNEL_CODE = "JD_CHANNEL_CODE";

	/** 淘宝渠道Code */
	public final static String TB_CHANNEL_CODE = "TB_CHANNEL_CODE";

	/** 淘宝天猫渠道Code */
	public final static String TMALL_CHANNEL_CODE = "TMALL_CHANNEL_CODE";

	/** 淘宝分销渠道Code */
	public final static String TBFX_CHANNEL_CODE = "TBFX_CHANNEL_CODE";

	/** 淘宝分销 父店铺ＣＯＤＥ */
	public final static String TB_FX_PARENT_CODE = "HQ01S999";

	/** 一号店渠道Code */
	public final static String YHD_CHANNEL_CODE = "YHD_CHANNEL_CODE";

	/** 微购物渠道Code */
	public final static String WGW_CHANNEL_CODE = "WGW_CHANNEL_CODE";

	/** 拍拍渠道Code */
	public final static String PP_CHANNEL_CODE = "PP_CHANNEL_CODE";

	/** 苏宁渠道Code */
	public final static String SN_CHANNEL_CODE = "SN_CHANNEL_CODE";

	/** 当当网渠道Code */
	public final static String DD_CHANNEL_CODE = "DD_CHANNEL_CODE";

	/** 微信渠道Code */
	public final static String WX_CHANNEL_CODE = "WX_CHANNEL_CODE";

	/** 邦购渠道Code */
	public final static String BG_CHANNEL_CODE = "BG_CHANNEL_CODE";


	/** 贝贝渠道Code */
	public final static String BB_CHANNEL_CODE = "BB_CHANNEL_CODE";

	/** 蘑菇街渠道Code */
	public final static String MGJ_CHANNEL_CODE = "MGJ_CHANNEL_CODE";
	
	/** 手工单渠道Code */
	public final static String OTHER_CHANNEL_CODE = "OTHER_CHANNEL_CODE";
	
	/** 蚂蚁手店渠道Code */
	public final static String MAYI_CHANNEL_CODE = "MAYI_CHANNEL_CODE";
	
	/** 美丽说渠道Code */
	public final static String MEILISHUO_CHANNEL_CODE = "MLS_CHANNEL_CODE";
	
	/**
	 * 楚楚街渠道
	 */
	public final static String CHUCHUJIE_CHANNEL_CODE = "CCJ_CHANNEL_CODE";
	
	public final static String WPH_CHANNEL_CODE = "WPH_CHANNEL_CODE";
	
	public final static String GW_CHANNEL_CODE = "GW_CHANNEL_CODE";
	
	public final static String PDD_CHANNEL_CODE = "PDD_CHANNEL_CODE";
	
	public final static String SNFX_CHANNEL_CODE = "SNFX_CHANNEL_CODE";
	
	public final static String YZ_CHANNEL_CODE = "YZ_CHANNEL_CODE";
	public final static String YK_CHANNEL_CODE = "YIKE";
	
	/**
	 * MCK店铺号
	 */
	public final static String MCK_SHOP_CODE = "HQ01S293";
	
	/**
	 * MM店铺号
	 */
	public final static String MM_SHOP_CODE = "HQ01S292";
	
	/**
	 * CHIN店铺号
	 */
	public final static String CHIN_SHOP_CODE = "HQ01S300";
	
	
	
	/**
	 * 渠道判定
	 */
	
	public static final List<String> TAOBAO_CHANNEL_LIST = Arrays.asList(new String[] { TB_CHANNEL_CODE, TMALL_CHANNEL_CODE });
	public static final List<String> JD_CHANNEL_LIST = Arrays.asList(new String[] { JD_CHANNEL_CODE });
	public static final List<String> YHD_CHANNEL_LIST = Arrays.asList(new String[] { YHD_CHANNEL_CODE });
	public static final List<String> DANGDANG_CHANNEL_LIST = Arrays.asList(new String[] { DD_CHANNEL_CODE });
	public static final List<String> PP_CHANNEL_LIST = Arrays.asList(new String[] { PP_CHANNEL_CODE });
	public static final List<String> WEIXIN_CHANNEL_LIST = Arrays.asList(new String[] { WX_CHANNEL_CODE });
	public static final List<String> TOPFX_CHANNEL_LIST = Arrays.asList(new String[] { TBFX_CHANNEL_CODE, TB_FX_PARENT_CODE });
	public static final List<String> SN_CHANNEL_LIST = Arrays.asList(new String[] { SN_CHANNEL_CODE });
	public static final List<String> BB_CHANNEL_LIST = Arrays.asList(new String[] { BB_CHANNEL_CODE });
	public static final List<String> MGJ_CHANNEL_LIST = Arrays.asList(new String[] { MGJ_CHANNEL_CODE });
	public static final List<String> MAYI_CHANNEL_LIST = Arrays.asList(new String[] { MAYI_CHANNEL_CODE });
	public static final List<String> MEILISHUO_CHANNEL_LIST = Arrays.asList(new String[] { MEILISHUO_CHANNEL_CODE });
	public static final List<String> CHUCHUJIE_CHANNEL_LIST = Arrays.asList(new String[] { CHUCHUJIE_CHANNEL_CODE });
	public static final List<String> WPH_CHANNEL_LIST = Arrays.asList(new String[] { WPH_CHANNEL_CODE });
	public static final List<String> GW_CHANNEL_LIST = Arrays.asList(new String[] { GW_CHANNEL_CODE });
	public static final List<String> PDD_CHANNEL_LIST = Arrays.asList(new String[] { PDD_CHANNEL_CODE });
	public static final List<String> SNFX_CHANNEL_LIST = Arrays.asList(new String[] { SNFX_CHANNEL_CODE });
	public static final List<String> YZ_CHANNEL_LIST = Arrays.asList(new String[] { YZ_CHANNEL_CODE });
	public static final List<String> YK_CHANNEL_LIST = Arrays.asList(new String[] { YK_CHANNEL_CODE });
	
   /**
    * 店铺判定
    */
	
	public static final List<String> GW_SHOP_LIST = Arrays.asList(new String[] { MCK_SHOP_CODE, MM_SHOP_CODE, CHIN_SHOP_CODE});
	
	public static final String USER_SYSTEM = "system";

	/**
	 * 关联订单类型（ 0，正常订单；1，补发订单；2，换货订单）
	 */
	public static class ORDERRETURN_RELATIN_ORDER_TYPE {
		/** 退单关联正常订单 */
		public static final Integer NORMAL = 0;
		/** 退单关联补发订单 */
		public static final Integer ADDITIONAL = 1;
		/** 退单关联换货订单 */
		public static final Integer EXCHANGE = 2;
	}

	/**
	 * 退单状态：0未确定、1已确认、4无效、10已完成
	 * 
	 * @author 吴健 HQ01U8435
	 * 
	 */
	public static class ORDERRETURN_STATUS {
		/** 0未确定 */
		public static final Byte UN_CONFIRM = 0;
		/** 1已确认 */
		public static final Byte CONFIRMED = 1;
		/** 4无效 */
		public static final Byte INVALIDITY = 4;
		/** 10已完成 */
		public static final Byte COMPLETE = 10;
	}

	/**
	 * 退单类型：1退货单、2拒收入库单、3退款单、4额外退款单、5失货退货单
	 * 
	 * @author 吴健 HQ01U8435
	 * 
	 */
	public static class ORDERRETURN_TYPE {
		/** 1退货单 */
		public static final Byte RETURN_GOODS = 1;
		/** 2拒收入库单 */
		public static final Byte REJECTION_AND_WAREHOUSE = 2;
		/** 3普通退款单 */
		public static final Byte RETURN_PAY = 3;
		/** 4额外退款单 */
		public static final Byte RETURN_EXTRA_PAY = 4;
		/** 5失货退货单**/
		public static final Byte RETURN_LOSE_GOODS = 5;
	}
	/**
	 * 退款类型：0：未处理  1.删除商品  2取消订单  3已发货
	 * 
	 * @author 吴健 HQ01U8435
	 * 
	 */
	public static class ORDERRETURN_REFUND_TYPE {
		/** 未处理 */
		public static final Byte NO_PRO = 0;
		/** 删除商品 */
		public static final Byte DELETE_GOODS = 1;
		/** 取消订单 */
		public static final Byte CANCEL_ORDER = 2;
		/** 已发货 */
		public static final Byte SHIP_ORDER = 3;
		/** AG生成退款单 */
		public static final Byte AG_RETURN = 4;
	}
	/**
	 * `order_return`.`return_settlement_type` tinyint(3) NOT NULL DEFAULT '0'
	 * COMMENT '1,预付款，2保证金'
	 * 
	 * @author 吴健 HQ01U8435
	 * 
	 */
	public static class ORDERRETURN_SETTLMENT_TYPE {
		/**   */
		public static final Byte DEFAULT = 0;
		/** 1,预付款 */
		public static final Byte PRE_PAY = 1;
		/** 2保证金 */
		public static final Byte BAIL = 2;

	}

	/**
	 * 退单的退款总状态
	 * 
	 * @author 吴健 HQ01U8435
	 * 
	 */
	public static class ORDERRETURN_PAY_STATUS {
		/** 0，未结算 */
		public static final Byte UNSETTLED = 0;
		/** 1，已结算 */
		public static final Byte SETTLED = 1;
		/** 2，待结算 */
		public static final Byte WAITSETTLE = 2;
	}

	/**
	 * 退单质检状态：0质检通过 1质检失败 2部分质检通过
	 */
	public static class ORDERRETURN_ISPASS_STATUS {
		/** 0，质检通过 */
		public static final Byte PASS = 0;
		/** 1，质检失败 */
		public static final Byte UNPASS = 1;
		/** 2,部分质检通过*/
		public static final Byte PARTPASS = 2;
	}
	
	/**
	 * order_return_ship质检状态：0质检不通过 1质检通过 2部分质检通过
	 */
	public static class ORDERRETURNSHIP_ISPASS_STATUS {
		/** 0，质检不通过 */
		public static final Byte UNPASS = 0;
		/** 1，质检通过 */
		public static final Byte PASS = 1;
		/** 2,部分质检通过*/
		public static final Byte PARTPASS = 2;
	}
	
	/**
	 * 退单收货状态:0、未收货；1、已收货；2、部分收货;
	 */
	public static class ORDERRETURN_GOODS_RECEIVED{
		
		public static final Byte UNRECEIVED = 0;
		
		public static final Byte RECEIVED = 1;
		
		public static final Byte PARTRECEIVED = 2;
		
	}

	/**
	 * order_return.process_type 处理状态
	 */
	public static class ORDERRETURN_PROCESS_TYPE {

		/** 0无操作 */
		public static final Byte NOOP = 0;
		/** 1退货 */
		public static final Byte RETURN = 1;
		/** 2修补 */
		public static final Byte REPAIR = 2;
		/** 3销毁 */
		public static final Byte DESTROY = 3;
		/** 4换货 */
		public static final Byte EXCHANGE = 4;
	}

	/**
	 * order_return.timeout_status 是否超时单
	 */
	public static class ORDERRETURN_TIMEOUT_STATUS {

		public static final int NORMAL = 0; /* 0 正常 */
		public static final int TIMEOUT = 1; /* 1 超时 */
	}

	/**
	 * order_return.ship_status 退货总状态
	 * 
	 * 退货总状态（0未收货;1已收货未质检;2.质检通过待入库;3已入库;4，部分入库）
	 * 
	 * @author 吴健 HQ01U8435
	 * 
	 */
	public static class ORDERRETURN_SHIP_STATUSS {
		/** 0，未收货 */
		public static final Byte UNRECEIVED = 0;
		/** 1，已收货未质检 */
		public static final Byte UNPASSCHECK = 1;
		/** 2，质检通过待入库 */
		public static final Byte WAITSTORAGE = 2;
		/** 3，已入库 */
		public static final Byte STORAG = 3;
		/** 4，部分入库 */
		public static final Byte PARTSTORAG = 4;
	}
	
	/**
	 * 
	 * @author cage
	 *order_return_ship  checkin_status 
	 *是否入库 （0未入库 1已入库 2待入库 3部分入库）
	 */
	public static class ORDERRETURNSHIP_CHECKIN_STATUS{
		public static final Integer UNSTORAGE = 0;
		public static final Integer STORAGE = 1;
		public static final Integer WAITSTORAGE = 2;
		public static final Integer PARTSTORAGE = 3;
		
	
	}

	/**
	 * 是否是换货时产生的退货、退款单 1是 0否 2额外退款单
	 * 
	 * @author 吴健 HQ01U8435
	 * 
	 */
	public static class ORDERRETURN_ORDER_TYPE {
		public static final Byte YES = 1;
		public static final Byte NOT = 0;
		public static final Byte ADD_RETURN_PAY = 2;
	}

	/**
	 * 成为为1，失败为零
	 */
	public static class YEANDNO {
		public static final Integer YES = 1;
		public static final Integer NO = 0;
	}

	public static final Integer ORDER_TIMEOUT_PROCESS_STATUS_9 = 9;

	/**
	 * 是否超时单 （0 正常 1 超时）
	 */
	public static class ORDERTIMEOUT_STATUS {
		public static final Integer NORMAL = 0;
		public static final Integer TIMEOUT = 1;
	}

	/**
	 * 问题单状态 （0 否 1 是）
	 */
	public static class ORDERQUESTION_STATUS {
		public static final Integer NORMAL = 0;
		public static final Integer QUESTION = 1;
	}

	/**
	 * 处理状态 （0 正常 1 挂起）
	 */
	public static class ORDER_PROCESS_STATUS {
		public static final Integer NORMAL = 0;
		public static final Integer SUSPEND = 1;
	}

	/**
	 * 订单支付总状态（0，未付款；1，部分付款；2，已付款；3，已结算）
	 */
	public static class ORDER_PAY_STATUS {
		/** 0，未付款 */
		public static final Integer UNPAYED = 0;
		/** 1，部分付款 */
		public static final Integer PARTPAY = 1;
		/** 2，已付款 */
		public static final Integer PAYED = 2;
		/** 3，已结算 */
		public static final Integer SETTLEMENT = 3;
	}

	/**
	 * 付款单支付状态（0，未付款；1，付款中；2，已付款；3，已结算）
	 */
	public static class OP_ORDER_PAY_STATUS {
		/** 0，未付款 */
		public static final Integer UNPAYED = 0;
		/** 1，付款中 */
		public static final Integer PAYING = 1;
		/** 2，已付款 */
		public static final Integer PAYED = 2;
		/** 3，已结算 */
		public static final Integer SETTLEMENT = 3;
		/** 4，待确认 */
		public static final Integer WAIT_CONFRIM= 4;
	}

	/**
	 * 订单-发货状态（发货总状态（0，未发货；1，备货中；2，部分发货；3，已发货；4，部分收货；5，客户已收货，6门店部分收货 7门店收货） ）
	 */
	public static class ORDERINFO_SHIP_STATUS {
		/** 0，未发货 */
		public static final Integer UNSHIP = 0;
		/** 1，备货中 */
		public static final Integer STOCKING = 1;
		/** 2，部分发货 */
		public static final Integer PART_SHIPED = 2;
		/** 3，已发货 */
		public static final Integer SHIPED = 3;
		/** 4，部分收货 */
		public static final Integer PART_TAKEED = 4;
		/** 5，客户已收货 */
		public static final Integer TAKEED = 5;
		/** 6，门店部分收货 */
		public static final Integer SHOP_PART_TAKEED = 6;
		/** 7，门店收货 */
		public static final Integer SHOP_TAKEED = 7;
	}

	/**
	 * 订单配送总状态(
	 * 0，未发货；1，已发货；2，已收货；3，备货中；10，快递取件；11，运输中；12，派件中；13，客户签收；14，客户拒签；15，
	 * 货物遗失；16，货物损毁 )
	 */
	public static class ORDER_SHIP_STATUS {
		/** 0，未发货 */
		public static final Integer NO_SHIP = 0;
		/** 1，已发货 */
		public static final Integer SHIPED = 1;
		/** 2，已收货 */
		public static final Integer TAKEED = 2;
		/** 3，备货中 */
		public static final Integer STOCKING = 3;
		/** 10，快递取件 */
		public static final Integer EXPRESS = 10;
		/** 11，运输中 */
		public static final Integer TRANSPORTING = 11;
		/** 12，派件中 */
		public static final Integer PACKAGE = 12;
		/** 13，客户签收 */
		public static final Integer SIGNED = 13;
		/** 14，客户拒签 */
		public static final Integer NOSIGN = 14;
		/** 15，货物遗失 */
		public static final Integer LOSS = 15;
		/** 16，货物损毁 */
		public static final Integer GARBAGE = 16;
	}

	/**
	 * 订单状态 （0，未确认；1，已确认；2，已取消；3，无效；4，退货；5，锁定；6，解锁；7，完成；8，拒收；9，已合并；10，已拆分；）
	 */
	public static class ORDER_STATUS {
		/** 0，未确认 */
		public static final Byte UNCONFIRM = 0;
		/** 1，已确认 */
		public static final Byte CONFIRMED = 1;
		/** 2，已取消 */
		public static final Byte CANCELED = 2;
		/** 3，完成 */
		public static final Byte INVALIDITY = 3;
		/** 4，退货 */
		public static final Byte RETURN_GOODS = 4;
		/** 5，锁定 */
		public static final Byte LOCKED = 5;
		/** 6，解锁 */
		public static final Byte UNLOCK = 6;
		/** 7，完成 */
		public static final Byte COMPLETE = 7;
		/** 8，拒收 */
		public static final Byte REFUSED = 8;
		/** 9，已合并 */
		public static final Byte MERGED = 9;
		/** 10，已拆分 */
		public static final Byte SPLIT = 10;
	}

	/**
	 * order_refund.return_pay_status 退单财务状态
	 * */
	public static class ORDERREFUND_RETURN_PAY_STATUS {
		/** 0，未结算 */
		public static final Byte UNSETTLED = 0;
		/** 1，已结算 */
		public static final Byte SETTLED = 1;
		/** 2，待结算 */
		public static final Byte WAITSETTLE = 2;
	}

	public static class ORDER_PROCESS_PERIOD_CODE {
		/**
		 * 周期ID 付款提醒周期 下单->用户付款提醒
		 */
		public static final String OS_PAYMENT_ALARM_PERIOD = "6101";

		/**
		 * 付款周期 下单->用户付款
		 */
		public static final String OS_PAYMENT_PERIOD = "6102";

		/** 快递收款周期 */
		public static final String OS_EXPRESS_RECEIPT_PERIOD = "6103";

		/**
		 * 付款确认周期 付款->确认
		 */
		public static final String OS_PAYMENT_CONFIRM_PERIOD = "6104";

		/** 订单全程处理周期 */
		public static final String OS_ORDER_PROCESS_PERIOD = "6201";

		/** 订单确认周期 */
		public static final String OS_ORDER_CONFIRM_PERIOD = "6202";

		/** 订单完成周期 */
		public static final String OS_ORDER_FINISH_PERIOD = "6203";

		/** 订单结算周期 */
		public static final String OS_ORDER_SETTLE_PERIOD = "6204";

		/** 发货周期 */
		public static final String OS_DELIVERY_PERIOD = "6301";

		/** 客户确认收货周期 */
		public static final String OS_CUSTOMER_CONFIRM_RECEIVE_PERIOD = "6302";

		/** 退款周期 */
		public static final String OS_REFUND_PERIOD = "6401";

		/** 退款确认周期 */
		public static final String OS_REFUDN_CONFIRM_PERIOD = "6402";

		/** 追单周期 */
		public static final String OS_CHASE_PERIOD = "6501";

		/** 快递收货提醒周期 */
		public static final String OS_EXPRESS_RECIEVE_ALARM_PERIOD = "6502";

		/** 快递收货周期 */
		public static final String OS_EXPRESS_RECIEVE_PERIOD = "6503";

		/** 仓库确认收货周期 */
		public static final String OS_WAREHOUSE_CONFIRM_RECIEVE_PERIOD = "6504";

		/** 入库质检周期 */
		public static final String OS_INCOMING_QUALITY_INSPECTION_PERIOD = "6505";

		/** 收货入库周期 */
		public static final String OS_RECEIVING_WAREHOUSING_PERIOD = "6506";

		/**
		 * 退单全程处理周期 生成退单->退单终了（作废/完成）
		 */
		public static final String OS_ORDER_RETURN_PROCESS_PERIOD = "6601";

		/** 退货单完成周期 */
		public static final String OS_ORDER_RETURN_SHIP_FINISH_PERIOD = "6602";

		/** 退货单结算周期 */
		public static final String OS_ORDER_RETURN_SHIP_SETTLE_PERIOD = "6603";

		/** 补款周期 */
		public static final String OS_ADDITION_PAYMENT_PERIOD = "6701";

		/** 补款提醒周期 */
		public static final String OS_ADDITION_PAYMENT_ALARM_PERIOD = "6702";

		/** 补款确认周期 */
		public static final String OS_ADDITION_PAYMENT_CONFIRM_PERIOD = "6703";

		/** 换单全程处理周期 */
		public static final String OS_ORDER_EXCHANGE_PROCESS_PERIOD = "6801";

		/** 换货确认周期 */
		public static final String OS_ORDER_EXCHANGE_CONFIRM_PERIOD = "6802";

		/** 换货完成周期 */
		public static final String OS_ORDER_EXCHANGE_FINISH_PERIOD = "6803";

		/** 换货结算周期 */
		public static final String OS_ORDER_EXCHANGE_SETTLE_PERIOD = "6804";
	}

	public static class ORDER_LOCK_STATUS {
		/** 已锁定 */
		public static final int LOCKED = 1;
		/** 未锁定 */
		public static final int UN_LOCKED = 0;
	}

	public static final BigDecimal ZERO = new BigDecimal("0.00");

	public static final String ORDER_CANCEL_CODE_1 = "8019";
	public static final String ORDER_CANCEL_NOTE_1 = "团购订单30分钟未付款取消";

	public static final Integer ORDER_TIMEOUT_RELATING_TYPE_ORDER = 0;
	public static final Integer ORDER_TIMEOUT_RELATING_TYPE_RETURNORDER = 1;


	/**
	 * ERP通知OS缺货时，非缺货原因处理
	 */
	public static final String ERP_OS_SHORT_REASON_NO1 = "跨仓个数受限";
	public static final String ERP_OS_SHORT_REASON_NO2 = "运输线路未维护";
	
	
	/**
	 * 福袋商品前缀
	 * 
	 */
	public static final String FDBAG_GOODS = "866";
	

	/**
	 * 退单编号前缀
	 */
	public static final String GENERATE_PRE_ORDER_RETURN = "TD";
	/**
	 * 退款单编号前缀
	 */
	public static final String GENERATE_PRE_ORDER_REFUND = "TK";

	/**
	 * 查询接口最大数据量返回限制
	 */
	public static final int MAX_SEARCH_ORDER_RESULT_COUNT = 20;

	public static String PROTECT_PRICE(String sn) {
		if (StringUtils.isBlank(sn)) {
			return "null";
		}
		return "GOODS_PROTECT_PRICE_SN_" + sn;
	}

	public static final String ORDER_PROMOTION_ID_ARRAY = "ORDER_PROMOTION_ID_ARRAY";

	/**
	 * 全短配问题单
	 */
	public static final String SHORT_ORDER_QUESTION_MSG = "ERP整单缺货：<br/>&nbsp;&nbsp;&nbsp;<font color='red'>问题单code：995;问题单原因：仓库全缺货问题单</font>";

	/**
	 * 统一订单方法调用者来源类型 POS:POS端;FRONT:前端;OS:后台
	 */
	public static class METHOD_SOURCE_TYPE {
		/** POS:POS端调用; */
		public static final String POS = "POS";
		/** FRONT:前端调用; */
		public static final String FRONT = "FRONT";
		/** ERP:ERP调用; */
		public static final String ERP = "ERP";
		/** OS:后台调用 */
		public static final String OMS = "OMS";
	}

	/**
	 * 取消订单是创建退单 2: 不创建;1: 创建;
	 */
	public static class CREATE_RETURN {
		/** 0: 不创建; */
		public static final String NO = "2";
		/** 1: 创建; */
		public static final String YES = "1";
	}

	/**
	 * 订单结算时业务销售类型（ TL:退货||HH:换货||RL:零售 ）
	 */
	public static class ORDER_SELLTE_SALEMODE {
		/** TL:退货 */
		public static final String RETURN = "TL";
		/** HH:换货 */
		public static final String CHANGE = "HH";
		/** RL:零售 */
		public static final String SALE = "RL";
	}

	/**
	 * 订单结算时业务类型（ 0:入库 1:结算）
	 */
	public static class ORDER_SELLTE_BUSSMODE {
		/** 0:入库 */
		public static final Integer INPUT = 0;
		/** 1:结算 */
		public static final Integer SELLTE = 1;
		/** 2:积分调整*/
		public static final Integer POINTS = 2;
		/** 3:批量创建退单 */
		public static final Integer BATCH_RETURN_CREATE = 3;
	}
	
	/**
	 * 退单入库操作（ 0:未入库 1:已入库  2:待入库  3：部分入库）
	 */
	public static class ORDER_RETURN_CHECKINSTATUS {
		/** 0:未入库 */
		public static final Integer UNINPUT = 0;
		/** 1:已入库 */
		public static final Integer INPUTED = 1;
		/** 2:待入库 */
		public static final Integer WAITINPUT = 2;
		/** 3：部分入库 */
		public static final Integer PARTINPUT = 3;
	}

	/**
	 * 订单结算时业务类型（ 0:入库 1:结算）
	 */
	public static class ORDER_SOURCE_ERP {
		/** 0:未处理为解析 */
		public static final Integer NO = 0;

		/** 1:线上订单(手机,手机Wap,Ipad移动商城) */
		public static final Integer ONLINE = 1;

		/** 2:Pos(全流通) */
		public static final Integer POS = 2;

		/** 云货架(YHJ) */
		public static final Integer YHJ = 3;

	}

	
	/*订单、退单同步*/
	public static Integer SYNC_SUCCESS = 1;//同步成功
	public static Integer SYNC_SUCCESS_PARTIAL = 3;//部分成功
	public static Integer SYNC_FAIL = 4;//同步失败

	/** 邦购官方网店 HQ01S116 */
	public static final String HQ01S116 = "HQ01S116";
	
												 

	// 默认扩展属性ID
	public static final String DEFAULT_EXTENSION_ID = "";
	

	
	/**
	 * Oms短信发送-业务类型汇总定义
	 */
	//
	public final static String MOBILE_MESSAGE_SENDTYPE_209 = "209";
	public final static String MOBILE_MESSAGE_SENDTYPE_211 = "211";
	/**
	 * 退单转入款
	 */
	//
	public final static String EXCHANGE_ORDER_RETURN_2_PAY_ID = "26";
	public final static String EXCHANGE_ORDER_RETURN_2_PAY_CODE = "return2pay";
	
	/**
	 * 号码类型
	 * ***/
	public final static Integer ORDER_CODE_ORDER_SN = 1;
	public final static Integer ORDER_CODE_RETURN_SN = 2;
	
	/**
	 * 无货退款单 来源类型（ 删除商品,订单发货，订单取消）
	 */
	public static class ORDERRETURN_REFUND_SOURCE {
		/** 删除商品 */
		public static final String DELETE_GOODS = "deleteGoods";
		/** 订单发货 */
		public static final String ORDER_ALLSHIP = "orderAllShip";
		/** 订单取消 */
		public static final String ORDER_CANCEL = "orderCancel";
		/** AG生成退款单 */
		public static final String AG_RETURN = "agReturn";
	}
	
	/**
	 * 退款单 退单理由（ 取消生成退单,发货退单-缺货，删除商品生成退单）
	 */
	public static class ORDERRETURN_REASON {
		//取消生成退单
		public static final String RETURN_PAY_REASON_CANCEL = "001";
		//发货退单-缺货
		public static final String RETURN_PAY_REASON_ALLSHIP = "46";
		//删除商品生成退单
		public static final String RETURN_PAY_REASON_DELETE_GOODS = "004";
		//AG生成退单
		public static final String RETURN_PAY_REASON_AG = "54";
	}
	
	
	/**
	 * 订单结算调整单据-单据类型
	 */
	public static class ORDER_SETTLE_BILL_TYPE {
		/** 未处理 */
		public static final Integer NOPROCESS = 0;
		/** 订单结算 */
		public static final Integer ORDER_SETTLE = 1;
		/** 订单货到付款结算 */
		public static final Integer ORDER_DELIVERY_PAY = 2;
		/** 退单结算 */
		public static final Integer RETURN_SETTLE = 3;
		/**保证金**/
		public static final Integer DEPOSIT_SETTLE = 4;
		/**日志**/
		public static final Integer ORDER_LOG_SETTLE = 5;
	}
	
	/**
	 * 订单结算调整单据-结算结果
	 */
	public static class ORDER_SETTLE_BILL_RESULT {
		/** 未处理 */
		public static final Integer NOPROCESS = 0;
		/** 结算成功 */
		public static final Integer SUCCESS = 1;
		/** 结算失败 */
		public static final Integer FAILED = 2;
	}

	/*
	 * 订单交易类型
	 */
	public static class ORDER_INFO_TRANS_TYPE{
		
		public static final Integer PAYMENT_BEFORE_DELIVERY = 1;//款到发货
		
		public static final Integer CASH_ON_DELIVERY = 2;//货到付款
		
		public static final Integer SECURIED_TRANSACTION = 3; //担保交易
	}
	
	/**
	 * 邦购app订单总状态
	 */
	public static class NEW_TOTAL_ORDER_STATUS{
		
		public static final Integer RETURN_GOODS_PROGRESS = 1;//退货中
		
		public static final Integer EXCHANGE_GOODS_PROGRESS = 2;//换货中
		
		public static final Integer RETURN_GOODS_FINISH = 3; //退货完成
		
		public static final Integer EXCHANGE_GOODS_FINISH = 4; //换货完成
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
