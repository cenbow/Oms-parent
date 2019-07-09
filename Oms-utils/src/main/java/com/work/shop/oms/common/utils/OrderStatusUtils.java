package com.work.shop.oms.common.utils;

import org.apache.commons.lang.StringUtils;

import com.work.shop.oms.common.bean.ButtonMenu;
import com.work.shop.oms.common.bean.MasterOrderDetail;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.vo.AdminUser;

public class OrderStatusUtils {
	
	private Integer lock;//锁定
	private Integer unlock;//解锁
	private Integer consignee;//编辑收货人信息
	private Integer decode;//解密
	private Integer other;//编辑其他信息
	private Integer money;//编辑付款单费用信息
	private Integer goods;//编辑主单商品
	private Integer confirm;//确认
	private Integer unconfirm;//未确认
	private Integer cancel;//取消
	private Integer question;//设为问题单
	private Integer normal;//返回正常单
	private Integer notice;//通知收款
	private Integer settle;//结算
	private Integer occupy;//占用释放库存
	private ButtonMenu conUnConMenu;//页面确认未确认按钮
	private ButtonMenu cancelMenu;//页面取消作废操作按钮
	private ButtonMenu norQuesMenu;//页面正常单问题单操作按钮
	private ButtonMenu occRealMenu;// 页面占用释放操作按钮
	private Integer swdi;// 同步分仓发货信息
	private Integer recent;// 移入近期
	private Integer addExtra;//额外退款单
	private Integer copyorder;// 复制订单
	private Integer communicate;//沟通
	private Integer pay;//已付款
	private Integer unpay;//未付款
	private Integer allocation;//分配
	private Integer relive;//复活
	private Integer onlyReturn;//仅退货
	private Integer getLogs;//日志补偿
	private Integer sendMessage;//发送短信
	private Integer oneKeyQuest;//一键问题单
	
	//重载构造函数
	public OrderStatusUtils(MasterOrderDetail masterOrderInfo,String masterOrderSn,AdminUser adminUser,String role){
		super();
		this.lock = lock(masterOrderInfo,masterOrderSn).getIsOk();
		this.unlock = unlock(masterOrderInfo,masterOrderSn).getIsOk();
		this.consignee = consignee(masterOrderInfo,masterOrderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.decode = 1;
		this.other = other(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.money = money(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.goods = goods(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.confirm = confirm(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.unconfirm = unconfirm(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.cancel = cancel(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.question = question(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.normal = normal(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.notice = notice(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.settle = settle(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.occupy = occupy(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.swdi = swdi(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.recent = 0;
		this.addExtra = addExtra(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.copyorder = copyorder(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.communicate = 1;
		this.pay = pay(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.unpay = unpay(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.allocation = allocation(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.relive = relive(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.onlyReturn = onlyReturn(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.getLogs = 1;
		this.sendMessage =1;
		this.oneKeyQuest = oneKeyQuest(masterOrderInfo,masterOrderSn, adminUser.getUserName(), Integer.parseInt(adminUser.getUserId())).getIsOk();
		
		if (masterOrderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			this.conUnConMenu = new ButtonMenu("订单确认", "confirm", "confirm"); 
		} else {
			this.conUnConMenu = new ButtonMenu("订单未确认", "unconfirm", "unconfirm"); 
		}
		if (masterOrderInfo.getOrderType() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			this.cancelMenu = new ButtonMenu("订单作废", "cancel", "cancel");
		} else {
			this.cancelMenu = new ButtonMenu("订单取消", "cancel", "cancel");
		}
		if (masterOrderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
			this.norQuesMenu = new ButtonMenu("订单设为问题单", "question", "question");
		} else {
			this.norQuesMenu = new ButtonMenu("订单返回正常单", "normal", "normal");
		}
		
	}
	
	public static ReturnInfo onlyReturn(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId){
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "仅退货");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getOrderType() != Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			ri.setMessage(" 订单" + masterOrderSn + "订单类型必须是换货订单");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	public static ReturnInfo relive(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId){
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		boolean flag = true;
		if (masterOrderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
			flag = false;
		}
		if (flag) {
			ri.setMessage(" 订单" + masterOrderSn + "订单状态要处于已取消状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//分配
	public static ReturnInfo allocation(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId){
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "分配");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED
				&& masterOrderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_PARTSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "发货状态必须是未发货.部分发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//未付款
	public static ReturnInfo unpay(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "未付款");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		if (masterOrderInfo.getPayStatus().byteValue() == Constant.OI_PAY_STATUS_UNPAYED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是未付款订单！");
			return ri;
		}
		if (masterOrderInfo.getTransType().intValue() == Constant.OI_TRANS_TYPE_PRESHIP
				&& masterOrderInfo.getOrderStatus().intValue() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,订单状态必须是已确认状态！");
			return ri;
		}
		if (masterOrderInfo.getTransType() != Constant.OI_TRANS_TYPE_PRESHIP
				&& (masterOrderInfo.getOrderStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED ||
						masterOrderInfo.getOrderStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED)) {
			ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,发货状态必须是未发货或部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//已付款
	public static ReturnInfo pay(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "已付款");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		if (masterOrderInfo.getPayStatus()== Constant.OI_PAY_STATUS_PAYED||masterOrderInfo.getPayStatus()== Constant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是已付款或者已结算订单！");
			return ri;
		}
		
		if (masterOrderInfo.getNoticeStatus() == Constant.OI_NOTICE_STATUS_UNNOTICED) {
			ri.setMessage(" 订单" + masterOrderSn + "还是未通知收款状态！");
			return ri;
		}
		if (masterOrderInfo.getTransType().intValue() == Constant.OI_TRANS_TYPE_PRESHIP
				&& masterOrderInfo.getOrderStatus().intValue() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,订单状态必须是已确认状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//复制订单
	public static ReturnInfo copyorder(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderInfo.getSource()==Constant.MO_SOURCE_POS) {
			ri.setMessage(" 订单" + masterOrderSn + "是全流通订单！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//生成额外退款单
	public static ReturnInfo addExtra(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getSource()==Constant.MO_SOURCE_POS) {
			ri.setMessage(" 订单" + masterOrderSn + "是全流通订单！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 全流通订单判断
	 * @param referer
	 * @return
	 */
	public static boolean judgeQLT(String referer) {
		if(StringUtils.equalsIgnoreCase(referer, "POS"))
			return false;
		return true;
	}
	
	//同步分仓发货信息
	public static ReturnInfo swdi(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		if (checkOrderShipStatus(masterOrderInfo)) {
			ri.setMessage("订单" + masterOrderSn + "状态是已完成或者已发货.客户已收货!");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	public static boolean checkOrderShipStatus(MasterOrderDetail masterOrderInfo) {
		return masterOrderInfo == null || masterOrderInfo.getOrderStatus() == 2 // 取消
				|| masterOrderInfo.getOrderStatus() == 3 // 完成
				|| masterOrderInfo.getPayStatus() == 3 // 已结算
				|| masterOrderInfo.getShipStatus() == 5; // 客户已收货
	}
	
	//占用释放库存
	public static ReturnInfo occupy(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//结算
	public static ReturnInfo settle(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "结算");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		// 订单确认状态检查
		// 未结算
		// 已确认 && (已发货.部分收货.客户已收货)
		// 货到付款 && (未付款 || 部分付款) && 未通知
		// 货到付款 && (部分付款) && 已通知
		// 非货到付款单 && 已付款
		if (masterOrderInfo.getPayStatus() == Constant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是已结算状态");
			return ri;
		}
		if (masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已确定状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus().intValue() < Constant.OI_SHIP_STATUS_ALLSHIPED){
			ri.setMessage(" 订单" + masterOrderSn + "发货状态必须是已发货.部分收货.客户已收货状态！");
			return ri;
		}
		if (masterOrderInfo.getTransType().intValue() == Constant.OI_TRANS_TYPE_PRESHIP) {
			if (masterOrderInfo.getNoticeStatus() != Constant.OI_NOTICE_STATUS_UNNOTICED 
					&& (masterOrderInfo.getPayStatus() != Constant.OI_PAY_STATUS_UNPAYED
							|| masterOrderInfo.getPayStatus() != Constant.OI_PAY_STATUS_PARTPAYED)) {
				ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,通知状态必须是未通知状态,支付状态必须是未付款.部分付款状态！");
				return ri;
			} else if (masterOrderInfo.getPayStatus() != Constant.OI_PAY_STATUS_PARTPAYED) {
				ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,通知状态是已通知状态,支付状态必须是部分付款状态！");
			}
		} else if (masterOrderInfo.getPayStatus() != Constant.OI_PAY_STATUS_PAYED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已付款状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//通知收款
	public static ReturnInfo notice(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "通知收款");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getNoticeStatus() == Constant.OI_NOTICE_STATUS_NOTICED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是通知收款状态！");
			return ri;
		}
		if (masterOrderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED
				|| masterOrderInfo.getPayStatus() == Constant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是已付款.已结算订单！");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		if (masterOrderInfo.getTransType() == Constant.OI_TRANS_TYPE_PRESHIP
				&& masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款订单,订单状态要处于已确认状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	
	//转为正常单
	public static ReturnInfo normal(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "返回正常单");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		/*if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}*/
		if (masterOrderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是正常单！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//设为问题单
	public static ReturnInfo question(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "设问题单");
		if (tempRi != null) {
			return tempRi;
		}
		if(masterOrderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED||
				masterOrderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_FINISHED){
			ri.setMessage(" 订单" + masterOrderSn + "已取消或已完成！");
			return ri;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		if (masterOrderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_QUESTION) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是问题单！");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	public static ReturnInfo oneKeyQuest(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId){
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		if(masterOrderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED||
				masterOrderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_FINISHED){
			ri.setMessage(" 订单" + masterOrderSn + "已取消或已完成！");
			return ri;
		}
		/*if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}*/
		if (masterOrderInfo.getPayStatus() == Constant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + masterOrderSn + "已结算！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//取消
	public static ReturnInfo cancel(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "取消");
		if (tempRi != null) {
			return tempRi;
		}
		if(masterOrderInfo.getOrderType()==Constant.OI_ORDER_TYPE_EXCHANGE_ORDER
				&& masterOrderInfo.getPayStatus()>=Constant.OI_PAY_STATUS_PAYED){
			ri.setMessage(" 换货单" + masterOrderSn + "要处于未付款或部分付款状态！");
			return ri;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		/*if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}*/
		/*if (masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "不能处于未确认状态");
			return ri;
		}*/
		if (masterOrderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货状态");
			return ri;
		}
		/*if (masterOrderInfo.getOrderType().intValue() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			ri.setMessage(" 订单" + masterOrderSn + "是换货订单,不允许编辑！");
			return ri;
		}*/
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//主单未确认
	public static ReturnInfo unconfirm(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		// 订单确认状态检查
		if (masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已确定状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//主单确认
	public static ReturnInfo confirm(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("订单" + masterOrderSn + "已经解锁");
			return ri;
		}
		// 未锁定 || 本人锁定 || admin
		if (masterOrderInfo.getLockStatus() != Constant.OI_LOCK_STATUS_UNLOCKED
				&& (!judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser))) {
			ri.setMessage("订单" + masterOrderSn + "已经解锁");
			return ri;
		}
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		if (!isPayed(masterOrderInfo.getTransType(), masterOrderInfo.getPayStatus())) {
			ri.setMessage("订单" + masterOrderSn + "要处于已付款状态！否则不能进行确认操作！");
			return ri;
		}
		if (masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未确定状态");
			return ri;
		}
		if (masterOrderInfo.getQuestionStatus() != Constant.OI_QUESTION_STATUS_NORMAL) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于正常单状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 交易类型 transType 支付状态 payStatus 判断是都已经付款
	 * 
	 * (1)条件:  ((款到发货 || 担保交易) && 已付款) || 货到付款  结果：true
	 * 
	 * 
	 * @param orderInfo
	 * @return true 修改; false 不修改
	 */
	public static boolean isPayed(short transType, short payStatus) {
		if(((transType == Constant.OI_TRANS_TYPE_PREPAY 
				|| transType == Constant.OI_TRANS_TYPE_GUARANTEE) 
			&& payStatus == Constant.OI_PAY_STATUS_PAYED)
				|| transType == Constant.OI_TRANS_TYPE_PRESHIP) {
			return true;
		}
		return false;
	}
	
	//编辑主单商品
	public static ReturnInfo goods(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "编辑商品");
		if (tempRi != null) {
			return tempRi;
		}
		
		if (masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + masterOrderSn + "要处于未确认状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		if (masterOrderInfo.getOrderType().intValue() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			ri.setMessage(" 订单" + masterOrderSn + "是换货订单,不允许编辑！");
			return ri;
		}
		// 已经下发ERP的订制订单不能编辑订单商品
		if (masterOrderInfo.getSource() != null && (masterOrderInfo.getSource() == 4 || masterOrderInfo.getSource() == 5)) {
			if (masterOrderInfo.getUpdateTime()!= null) {
				ri.setMessage(" 订单" + masterOrderSn + "属于订制订单且已经下发不能编辑！");
				return ri;
			}
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//编辑付款单费用信息
	public static ReturnInfo money(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecutionNoCancel(masterOrderInfo, masterOrderSn, "编辑费用信息");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		if (masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + masterOrderSn + "要处于未确认状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "发货状态必须是未发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//编辑其他信息
	public static ReturnInfo other(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		
		ReturnInfo tempRi = checkConditionOfExecutionNoCancel(masterOrderInfo, masterOrderSn, "编辑其他信息");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		if (masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + masterOrderSn + "要处于未确认状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "发货状态必须是未发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//检查订单是否可以进行正常订单操作
	public static ReturnInfo checkConditionOfExecutionNoCancel(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		/* 参数检验 */
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		int orderStatus = masterOrderInfo.getOrderStatus();
		if (orderStatus == Constant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage("订单" + masterOrderSn + "已处于完成状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		return null;
	}
	
	//判断此订单是否被当前操作者锁定
	public static boolean judgeSelfLock(Integer lockStatus, Integer userId, String actionUser) {
		boolean result = false;
		if (lockStatus == Constant.OI_LOCK_STATUS_UNLOCKED) {
			return result;
		}
		if ("admin".equals(actionUser)) {
			return true;
		}
		if(null == userId) {
		} else if (null == lockStatus || lockStatus.intValue() == Constant.OI_LOCK_STATUS_UNLOCKED) {
		} else {
			if(lockStatus.equals(userId)) {
				result = true;
			} else {
			}
		}
		return result;
	}
	
	//检查订单是否可以进行正常订单操作
	public static ReturnInfo checkConditionOfExecutionNoFinished(MasterOrderDetail masterOrderInfo,String masterOrderSn,String actionType) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		/* 参数检验 */
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		int orderStatus = masterOrderInfo.getOrderStatus();
		return null;
	}
	
	//获取锁定按钮状态
	public static ReturnInfo lock(MasterOrderDetail masterOrderInfo,String masterOrderSn){
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if(masterOrderInfo==null){
			ri.setMessage("没有获取订单" + masterOrderInfo + "的信息！不能进行锁定操作！");
			return ri;
		}
		if (masterOrderInfo.getLockStatus() != Constant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经被锁定！");
			return ri;
		}
		ReturnInfo tempRi = checkConditionOfExecutionNoFinished(masterOrderInfo, masterOrderSn, "锁定");
		if (tempRi != null) {
			return tempRi;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//获取解锁按钮状态
	public static ReturnInfo unlock(MasterOrderDetail masterOrderInfo,String masterOrderSn){
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (null == masterOrderInfo) {
			ri.setMessage("没有获取订单" + masterOrderSn + "的信息！不能进行解锁操作！");
			return ri;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("订单" + masterOrderSn + "已经解锁");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//编辑收货人
	public static ReturnInfo consignee(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "编辑收货人");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		/*if (masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "不能处于未确认状态");
			return ri;
		}*/
		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发XOMS状态！");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "发货状态必须是未发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}

	// 检查订单是否可以进行正常订单操作
	public static ReturnInfo checkConditionOfExecution(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		/* 参数检验 */
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		int orderStatus = masterOrderInfo.getOrderStatus();
		if (orderStatus == Constant.OI_ORDER_STATUS_CANCLED) {
			ri.setMessage("订单" + masterOrderSn + "已处于已取消状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		if (orderStatus == Constant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage("订单" + masterOrderSn + "已处于完成状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		return null;
	}
	
	public Integer getLock() {
		return lock;
	}

	public void setLock(Integer lock) {
		this.lock = lock;
	}

	public Integer getUnlock() {
		return unlock;
	}

	public void setUnlock(Integer unlock) {
		this.unlock = unlock;
	}

	public Integer getConsignee() {
		return consignee;
	}

	public void setConsignee(Integer consignee) {
		this.consignee = consignee;
	}

	public Integer getDecode() {
		return decode;
	}

	public void setDecode(Integer decode) {
		this.decode = decode;
	}

	public Integer getOther() {
		return other;
	}

	public void setOther(Integer other) {
		this.other = other;
	}
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getGoods() {
		return goods;
	}

	public void setGoods(Integer goods) {
		this.goods = goods;
	}

	public Integer getConfirm() {
		return confirm;
	}

	public void setConfirm(Integer confirm) {
		this.confirm = confirm;
	}

	public Integer getUnconfirm() {
		return unconfirm;
	}

	public void setUnconfirm(Integer unconfirm) {
		this.unconfirm = unconfirm;
	}
	public ButtonMenu getConUnConMenu() {
		return conUnConMenu;
	}
	public void setConUnConMenu(ButtonMenu conUnConMenu) {
		this.conUnConMenu = conUnConMenu;
	}
	public Integer getCancel() {
		return cancel;
	}
	public void setCancel(Integer cancel) {
		this.cancel = cancel;
	}
	public ButtonMenu getCancelMenu() {
		return cancelMenu;
	}
	public void setCancelMenu(ButtonMenu cancelMenu) {
		this.cancelMenu = cancelMenu;
	}

	public Integer getQuestion() {
		return question;
	}

	public void setQuestion(Integer question) {
		this.question = question;
	}

	public Integer getNormal() {
		return normal;
	}

	public void setNormal(Integer normal) {
		this.normal = normal;
	}

	public ButtonMenu getNorQuesMenu() {
		return norQuesMenu;
	}

	public void setNorQuesMenu(ButtonMenu norQuesMenu) {
		this.norQuesMenu = norQuesMenu;
	}

	public Integer getNotice() {
		return notice;
	}

	public void setNotice(Integer notice) {
		this.notice = notice;
	}

	public Integer getSettle() {
		return settle;
	}

	public void setSettle(Integer settle) {
		this.settle = settle;
	}

	public Integer getOccupy() {
		return occupy;
	}

	public void setOccupy(Integer occupy) {
		this.occupy = occupy;
	}

	public ButtonMenu getOccRealMenu() {
		return occRealMenu;
	}

	public void setOccRealMenu(ButtonMenu occRealMenu) {
		this.occRealMenu = occRealMenu;
	}

	public Integer getSwdi() {
		return swdi;
	}

	public void setSwdi(Integer swdi) {
		this.swdi = swdi;
	}

	public Integer getRecent() {
		return recent;
	}

	public void setRecent(Integer recent) {
		this.recent = recent;
	}

	public Integer getAddExtra() {
		return addExtra;
	}

	public void setAddExtra(Integer addExtra) {
		this.addExtra = addExtra;
	}

	public Integer getCopyorder() {
		return copyorder;
	}

	public void setCopyorder(Integer copyorder) {
		this.copyorder = copyorder;
	}

	public Integer getCommunicate() {
		return communicate;
	}

	public void setCommunicate(Integer communicate) {
		this.communicate = communicate;
	}

	public Integer getPay() {
		return pay;
	}

	public void setPay(Integer pay) {
		this.pay = pay;
	}

	public Integer getUnpay() {
		return unpay;
	}

	public void setUnpay(Integer unpay) {
		this.unpay = unpay;
	}

	public Integer getAllocation() {
		return allocation;
	}

	public void setAllocation(Integer allocation) {
		this.allocation = allocation;
	}
	public Integer getRelive() {
		return relive;
	}
	public void setRelive(Integer relive) {
		this.relive = relive;
	}
	public Integer getOnlyReturn() {
		return onlyReturn;
	}
	public void setOnlyReturn(Integer onlyReturn) {
		this.onlyReturn = onlyReturn;
	}
	public Integer getGetLogs() {
		return getLogs;
	}
	public void setGetLogs(Integer getLogs) {
		this.getLogs = getLogs;
	}

	public Integer getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(Integer sendMessage) {
		this.sendMessage = sendMessage;
	}

	public Integer getOneKeyQuest() {
		return oneKeyQuest;
	}

	public void setOneKeyQuest(Integer oneKeyQuest) {
		this.oneKeyQuest = oneKeyQuest;
	}
	
	

}
