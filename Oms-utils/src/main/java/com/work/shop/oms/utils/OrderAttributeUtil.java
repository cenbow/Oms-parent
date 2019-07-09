package com.work.shop.oms.utils;

import java.net.InetAddress;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderDistribute;

/**
 * 订单店铺以及订单来源媒体判断
 * @author lemon
 *
 */
public class OrderAttributeUtil {

	private static String ip = "";
	
	/** ERP DOCSOURCE 全流通 */
	public static final String DOC_SOURCE_POS = "01";
	
	/** ERP DOCSOURCE 运货架 */
	public static final String DOC_SOURCE_YHJ = "02";
	
	/** ERP DOCSOURCE B2C */
	public static final String DOC_SOURCE_B2C = "03";
	
	/** ERP DOCSOURCE C2M */
	public static final String DOC_SOURCE_C2M = "04";
	
	/** ERP DOCSOURCE C2B */
	public static final String DOC_SOURCE_C2B = "05";
	
	/**
	 * 获取本机IP
	 * @return
	 */
	public static String getLocalhostIp() {
		if (StringUtil.isEmpty(ip)) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				ip = addr.getHostAddress();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ip;
	}
	
	/**
	 * 判断是否为全流通订单
	 * 
	 * @param source
	 * @return boolean
	 */
	public static boolean isPosOrder(Integer source) {
		if (source == null) {
			return false;
		}
		return source.intValue() == 1;
	}

	/**
	 * 是否云货架订单
	 * 
	 * @param referer
	 * @return
	 */
	public static boolean isYHJOrder(Integer source) {
		if (source == null)
			return false;
		return source.intValue() == 2;
	}

	/**
	 * 获取ERP docsource 单据类型(01:全流通，02:云货架，03:B2C)
	 * 
	 * @param referer
	 * @return
	 */
	public static String getErpOrderSource(Integer source) {
		if (isPosOrder(source))
			return DOC_SOURCE_POS;
		if (isYHJOrder(source)) {
			return DOC_SOURCE_YHJ;
		}
		// OMS 来源类型前加0拼接后的值为ERPdocsource 单据类型
		if (source != null && source.intValue() != 0) {
			return "0" + source;
		}
		return DOC_SOURCE_B2C;
	}

	/**
	 * 判断是否已经将订单下发过ERP
	 * 
	 * @param orderInfo
	 * @param isrelive  复活标示 1为复活
	 * @return true 已经下发过erp false 没有下发过erp
	 */
	public static boolean doERP(OrderDistribute distribute,int isrelive) {
		
		boolean isErp=false;
		if(distribute==null)
			 isErp=false;
		switch(distribute.getOrderType()){
			case 3 :
				isErp=false;
			case 4 :
				isErp=false;
			default :
				if(distribute.getReferer().trim().indexOf("淘宝品牌特卖")!=-1 
					&& distribute.getOrderType() == Constant.OI_ORDER_TYPE_NORMAL_ORDER){
					isErp=false;
				}
				isErp=true;
		}
		if(isErp){
			// 全流通订单 同步ERP
			/*if (isPosOrder(distribute.getSource()) && isrelive == 0) {
				isErp = true;
			} else*/
			if(distribute.getLastUpdateTime() != null && isrelive == 0){ //已经下发过ERP且没有复活
				isErp = true;
			}else{
				isErp = false;
			}
		}
		return isErp;
	}
	
	/**
	 * 2012-07-23 淘宝品牌特卖判定 ，只做判断
	 * @param orderType 订单类型       3，平台币虚拟商品 4 免邮卡。
	 * @return true 可以下发erp false 不下发erp
	 */
	public static boolean canErp(MasterOrderInfo distribute){
		if(distribute==null)
			return true;
		switch(distribute.getOrderType()){
			case 3 :
				return false;
			case 4 :
				return false;
			default :
				if(distribute.getReferer().trim().indexOf("淘宝品牌特卖")!=-1 
					&& distribute.getOrderType() == Constant.OR_ORDER_TYPE_NO){
					return false;
				}
				 return true;
		}
	}
	/**
	 * 对于下发的标志isUpdate/updateType是否修改做判断
	 * 
	 * (1)条件:  ((款到发货 || 担保交易) && 已付款) || 货到付款  结果：true
	 * 
	 * 
	 * @param orderInfo
	 * @return true 修改; false 不修改
	 */
	public static boolean isUpdateModifyFlag(Integer transType, Integer payStatus) {
		if(((transType == Constant.OI_TRANS_TYPE_PREPAY 
				|| transType == Constant.OI_TRANS_TYPE_GUARANTEE) 
			&& payStatus == Constant.OI_PAY_STATUS_PAYED)
				|| transType == Constant.OI_TRANS_TYPE_PRESHIP) {
			return true;
		}
		return false;
	}
	
	
	public static boolean isNowDistribute(Integer source, Integer isGroup) {
		boolean configAllFlag = readPosAllNowConfig();
		if (!configAllFlag) {
			return false;
		}
		// 需要立即分发分.判断团购需不需要立即分发分配
		boolean flag = OrderAttributeUtil.isPosOrder(source);
		if (!flag) {
			return false;
		}
		// 判断是否开启团购订单立即分配
		boolean configFlag = readerConfig();
		// 如果是POS并且团购需要立即分配
		if (flag && configFlag)
			return true;
		return isGroup == 0;
	}
	
	/**
	 * 团购订单是否立即分配 true团购立即分配 false团购不立即分配
	 * 
	 * @return
	 */
	public static boolean readerConfig() {
		String flag = ConfigCenter.getProperty("order.pos.group.now");
		return flag.equals("true");
//		return Boolean.valueOf(ConfigCenter.getPropertyItem("order.pos.group.now", "true").toString());
	}
	
	public static boolean readPosAllNowConfig() {
		// 是否开启立即下发
		return Boolean.valueOf(ConfigCenter.getPropertyItem("order.pos.all.now", "true").toString());
	}
}