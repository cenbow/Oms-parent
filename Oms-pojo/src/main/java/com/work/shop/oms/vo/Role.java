package com.work.shop.oms.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录用户所属角色权限
 * @author wujian
 *	0打印订单												-1
	生成拒收入库单											-1
	//生成退货单				chl_return-add_return_order		425
	生成退单 					return-info-add	
	
	生成换货单												-1
	生成额外退款单			user_account-add				327
	
	5锁定					order-info-lock					279
	解锁						order-info-unlock				280
	确认						order-info-confirm				281
	未确认					order-info-unconfirm			282
	取消						order-info-cancel				283
	挂起						order-info-suspend				284
	解挂						order-info-unsuspend			285
	设为问题单				order-info-question				286
	返回正常单				order-info-unquestion			287
	通知收款					order-info-notice				288
	已付款					order-info-pay					289
	未付款					order-info-unpay				290
	结算						order-info-finish				292
	沟通						order-info-communicate			293
	1：释放库存  2：占用库存									-1
	复活						order-info-revive				629
	
	21编辑收货人信息			order-info-consignee			275
	编辑其它信息				order-info-other				276
	编辑商品信息				order-info-goods				277
	编辑费用信息				order-info-money				278
	
	25编辑配送方式			order-info-shipping				274
	编辑支付方式				order-info-payment				273
	编辑分仓信息				order-info-separate-status		671
	
	********************************************************************
	退单操作权限
	
	生成退单				return-info-add					303
	单张退单查看			return-list-view				304
	单张退单				return-info						305
	
	更新退款方式			return-info-edit-mode 			672
	更新处理方式			return-info-update_process_type	306
	更新预退款/保证金		return-info-edit-refund			673
	更新退货快递单号		return-info-update_invoice_no	307
	更新退货快递公司		return-info-update_express		308
	更新换货新生成订单号	return-info-update_new_order_sn	309
	更新仓库编码			return-info-edit-code			674
	更新退款总金额		return-info-update_total_fee	310
	
	保存			return-info-save				311
	确认			return-info-confirm				312
	未确认		return-info-unconfirm			313
	追单			return-info-chase				314
	未追单		return-info-unchase				315
	收货			return-info-receive				316
	未收货		return-info-unreceive			317
	质检通过		return-info-pass				318
	质检未通过	return-info-unpass				319
	提交处理意见 return-info-process				321
	作废 		return-info-invalid				322
	入库 		return-info-storage				323
	结算 		return-info-finish				325
	沟通			return-info-communicate			326
	回退客服		return-info-reservice			675
	复活			return-info-revive				630
	
	额外退款		user_account-add				327
	
 */

public class Role {
	public static String ORDER_RETURN_ROLE_STRING="";
	public static String ORDER_ROLE_STRING = "";
	public static String ORDER_DETAIL_MODIFY_ROLE_STRING = "";
	public static String BATCH_TASK_ROLE_STRING = "";   //批量任务操作权限
	public static String RULE_STRING = "";
	public static final int ROLE_SUPER = 28;
	public static final int ROLE_ORDER_VIEW = 29;
	public static final int ROLE_ORDER_CONGSIN_VIEW = 37;
	public static final int ROLE_RETURN_ORDER_CONGSIN_VIEW = 29;
	public static final Map<String, Integer> ORDER_LIST_BATCH = initOrderListBatch();
	public static Map<String, Integer> initOrderListBatch(){
		Map<String, Integer> rs = new HashMap<String, Integer>();
		rs.put("confirm", 30);
		rs.put("range", 31);
		rs.put("cancel", 32);
		rs.put("remove", 33);
		rs.put("suspend", 34);
		rs.put("question", 35);
		rs.put("merge", 36);
		rs.put("tonormal", 13);
		
		return rs;
	}
	
	private StringBuilder roleString;
	public Role() {
	}
	public Role(StringBuilder sb) {
		this.roleString = sb;
	}
	public StringBuilder getRoleString() {
		return roleString;
	}
	
	public String getOrderRole(String orderRoleString){
		return this.getRole(orderRoleString);
	}
	
	private String getRole(String role){
		String[] rss = role.split(",");
		StringBuilder result = new StringBuilder("");
		for (int i = 0; i < rss.length; i++) {
			String s = rss[i];
			if (s.equalsIgnoreCase("-1")) {
				result.append("1");
			} else {
				try {
					if (roleString.indexOf(",all,") > -1
							|| roleString.indexOf("," + s + ",") > -1) {
						result.append("1");
					} else {
						result.append("0");
					}
				} catch (Exception e) {
					result.append("0");
				}
			}
		}
		return result.toString();
	}
	
	public String getOrderReturnRole(){
		return this.getRole(ORDER_RETURN_ROLE_STRING);
	}
	public String getRuleRole(){
		return this.getRole(RULE_STRING);
	}
	public String getOrderDetailModifyRole() {
		return this.getRole(ORDER_DETAIL_MODIFY_ROLE_STRING);
	}
	public String getBatchTaskRole() {
		return this.getRole(BATCH_TASK_ROLE_STRING);
	}
	public void setRoleString(StringBuilder roleString) {
		this.roleString = roleString;
	}
	public static void main(String[] args) {
		System.out.println(ORDER_ROLE_STRING.split(",").length);
	}
}
