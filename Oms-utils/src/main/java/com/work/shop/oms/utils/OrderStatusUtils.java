package com.work.shop.oms.utils;

import org.apache.commons.lang.StringUtils;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.bean.ReturnInfo;



public class OrderStatusUtils {
//	private static Logger logger = Logger.getLogger(OrderStatusUtils.class);
	
	private Integer lock;								//锁定
	
	private Integer unlock;								//解锁
	
	private Integer confirm;							//确认
	
	private Integer unconfirm;							//未确认
	
	private Integer cancel;								//取消
	
	private Integer question;							//设为问题单
	
	private Integer normal;								//返回正常单
	
	private Integer notice;								//通知收款
	
	private Integer pay;								//已付款
	
	private Integer unpay;								//未付款
	
	private Integer settle;								//结算
	
	private Integer occupy;								//占用释放库存
	
	private Integer relive;								//复活
	
	private Integer isPrint;							//打印订单
	
	private Integer addrefuse;							//生成拒收入库单
	
	private Integer addReturn;							//生成退货单
	
	private Integer addExchang;							//生成换货单
	
	private Integer addExtra;							//额外退款单
	
	private Integer consignee;							//编辑收货人
	
	private Integer goods;								//编辑商品
	
	private Integer other;								//编辑其他信息
	
	private Integer shipping;							//编辑配送方式
	
	private Integer payment;							//编辑支付方式
	
	private Integer money;								//编辑费用信息
	
	private Integer allocation;							//分配
	
	private Integer communicate;						//沟通
	
//	private ButtonMenu norQuesMenu;					// 页面正常单问题单操作按钮
//	
//	private ButtonMenu conUnConMenu;				// 页面确认未确认操作按钮
//	
//	private ButtonMenu occRealMenu;					// 页面占用释放操作按钮
//	
//	private ButtonMenu cancelMenu;					// 页面取消作废操作按钮

	private Integer copyorder;							// 复制订单
	
	private Integer onlyReturn;							// 仅退货
	
	private Integer swdi;								// 同步分仓发货信息
	
	private Integer recent;								// 移入近期
	
	private Integer decode;								// 解密

	public OrderStatusUtils(MasterOrderInfo orderInfo, String masterOrderSn, String actionUser, Integer userId, String role) {
		super();
		this.lock = lock(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.unlock = unlock(orderInfo, masterOrderSn, actionUser, userId, role).getIsOk();
		this.confirm = confirm(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.unconfirm = unconfirm(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.cancel = cancel(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.question = question(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.normal = normal(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.notice = notice(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.pay = pay(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.unpay = unpay(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.settle = settle(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.occupy = occupy(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.relive = relive(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
//		this.isPrint = isPrint;
		this.addrefuse = addrefuse(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.addReturn = addReturn(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.addExchang = addExchang(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.addExtra = addExtra(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.goods = goods(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.consignee = consignee(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.other = other(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.shipping = shipping(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.payment = payment(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.money = money(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.allocation = allocation(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.onlyReturn = onlyReturn(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.communicate = 1;
		this.copyorder = copyorder(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		/*if (orderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
			this.norQuesMenu = new ButtonMenu("设为问题单", "question", "question");
		} else {
			this.norQuesMenu = new ButtonMenu("返回正常单", "normal", "normal");
		}
		if (orderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			this.conUnConMenu = new ButtonMenu("确认", "confirm", "confirm"); 
		} else {
			this.conUnConMenu = new ButtonMenu("未确认", "unconfirm", "unconfirm"); 
		}
		if (orderInfo.getOrderType() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			this.cancelMenu = new ButtonMenu("作废", "cancel", "cancel");
		} else {
			this.cancelMenu = new ButtonMenu("取消", "cancel", "cancel");
		}*/
		this.swdi = swdi(orderInfo, masterOrderSn, actionUser, userId).getIsOk();
		this.recent = 0;
		this.decode = 1;
	}
	
	/**
	 * 所有操作按钮需要先锁定  未确认 在操作
	 * 当订单处于锁定状态时，判断订单时候是本人锁定，是本人锁定，按锁定状态处理， 不是本人锁定需解锁才能做相关操作
	 */
	/**
	 * 锁定
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo lock(MasterOrderInfo orderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (null == orderInfo) {
			ri.setMessage("没有获取订单" + masterOrderSn + "的信息！不能进行锁定操作！");
			return ri;
		}
		// 解锁状态 && 不是本人锁定 可使用
		// 锁定状态 || 本人锁定 不可使用
		// && judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)
		if (orderInfo.getLockStatus() != Constant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经被锁定！");
			return ri;
		}
		ReturnInfo tempRi = checkConditionOfExecutionNoFinished(orderInfo, masterOrderSn, "锁定");
		if (tempRi != null) {
			return tempRi;
		}
		// 执行前提检查
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	public static void main(String[] args) {
		MasterOrderInfo orderInfo = new MasterOrderInfo();
		orderInfo.setOrderStatus((byte)Constant.OI_ORDER_STATUS_UNCONFIRMED);
		orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
//		orderInfo.setLockStatus(Constant.OI_LOCK_STATUS_UNLOCKED);
		orderInfo.setLockStatus(2183);
		orderInfo.setShipStatus((byte)Constant.OI_SHIP_STATUS_ALLRECEIVED);
//		ReturnInfo info =  OrderStatusUtils.unlock(orderInfo, "1234567890", "HQ01UC771", 2183);
		ReturnInfo info =  OrderStatusUtils.lock(orderInfo, "1234567890", "sss", 2183);
		System.out.println("lock: " + info.getIsOk());
		ReturnInfo unlockinfo =  OrderStatusUtils.unlock(orderInfo, "1234567890", "sss", 2183, null);
		System.out.println("unlock: " + unlockinfo.getIsOk());
		
//		ReturnInfo confirminfo =  OrderStatusUtils.confirm(orderInfo, "1234567890", "ssss", 2183);
//		System.out.println("confirm: " + confirminfo.getIsOk());
//		
//		
//		System.out.println(judgeSelfLock(orderInfo.getLockStatus(), 2183, "asd"));
		
	}
	/**
	 * 解锁
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo unlock(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId, String role) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (null == orderInfo) {
			ri.setMessage("没有获取订单" + orderSn + "的信息！不能进行解锁操作！");
			return ri;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("订单" + orderSn + "已经解锁");
			return ri;
		}
		// 锁定状态 && 不是本人锁定 可使用
		// 解锁 || 本人锁定 不可使用
		// 已锁定 && 不是本人锁定
		
//		if (orderInfo.getLockStatus() != Constant.OI_LOCK_STATUS_UNLOCKED) {
//			// 判断是否是管理员角色
//			if (!judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
////				ri.setMessage("订单" + orderSn + "解锁,角色不是管理员并且不是本人锁定不能解锁！");
//				ri.setMessage("订单" + orderSn + ",是本人锁定不能解锁！");
//				return ri;
//			}
//		}
		
		// 未锁定 || 本人锁定 || admin
//		if (judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
//			ri.setMessage("订单" + orderSn + "已经解锁");
//			return ri;
//		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	/**
	 * 确认
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo confirm(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("订单" + orderSn + "已经解锁");
			return ri;
		}
		// 未锁定 || 本人锁定 || admin
		if (orderInfo.getLockStatus() != Constant.OI_LOCK_STATUS_UNLOCKED
				&& (!judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser))) {
			ri.setMessage("订单" + orderSn + "已经解锁");
			return ri;
		}
		// 订单确认状态检查
//		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
//				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
//			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
//			return ri;
//		}
		if (!isPayed(orderInfo.getTransType(), orderInfo.getPayStatus())) {
			ri.setMessage("订单" + orderSn + "要处于已付款状态！否则不能进行确认操作！");
			return ri;
		}
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage(" 订单" + orderSn + "要处于未确定状态");
			return ri;
		}
		if (orderInfo.getOrderStatus() != Constant.OI_QUESTION_STATUS_NORMAL) {
			ri.setMessage(" 订单" + orderSn + "要处于正常单状态");
			return ri;
		}
		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 未确认
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo unconfirm(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
//		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
//			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
//			return ri;
//		}
		// 订单确认状态检查
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + orderSn + "要处于已确定状态");
			return ri;
		}
//		if (orderInfo.getQuestionStatus() != Constant.OI_QUESTION_STATUS_NORMAL) {
//			ri.setMessage(" 订单" + orderSn + "要处于正常单状态");
//			return ri;
//		}
		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	
	/**
	 * 取消
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo cancel(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "取消");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
//		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
//			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
//			return ri;
//		}
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage(" 订单" + orderSn + "不能处于未确认状态");
			return ri;
		}
//		if (orderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_CONFIRMED
//				&& orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED) {
//			ri.setMessage(" 订单" + orderSn + "不能同时处于已确认.已付款状态");
//		}
		if (orderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + orderSn + "要处于未发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 正常单问题单按钮显示
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
//	public static ButtonMenu menu(MasterOrderInfo orderInfo, String orderSn) {
//		if (orderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_QUESTION) {
//			ButtonMenu.NORMAL.setName("设为问题单");
//			ButtonMenu.NORMAL.setAction("question");
//		}
//	}
	
	
	/**
	 * 问题单
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo question(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "设问题单");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
//		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
//			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
//			return ri;
//		}
		if (orderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_QUESTION) {
			ri.setMessage(" 订单" + orderSn + "已经是问题单！");
			return ri;
		}
		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 返回正常单
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo normal(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "返回正常单");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
//		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
//			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
//			return ri;
//		}
		if (orderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
			ri.setMessage(" 订单" + orderSn + "已经是正常单！");
			return ri;
		}
		
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 通知收款
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo notice(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "通知收款");
		if (tempRi != null) {
			return tempRi;
		}
//		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
//			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
//			return ri;
//		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getNoticeStatus() == Constant.OI_NOTICE_STATUS_NOTICED) {
			ri.setMessage(" 订单" + orderSn + "已经是通知收款状态！");
			return ri;
		}
		if (orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED
				|| orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + orderSn + "已经是已付款.已结算订单！");
			return ri;
		}
		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		if (orderInfo.getTransType() == Constant.OI_TRANS_TYPE_PRESHIP
				&& orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + orderSn + "交易类型是货到付款订单,订单状态要处于已确认状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	
	/**
	 * 已付款
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo pay(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "已付款");
		if (tempRi != null) {
			return tempRi;
		}
//		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
//			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
//			return ri;
//		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getPayStatus().byteValue() >= Constant.OI_PAY_STATUS_PAYED) {
			ri.setMessage(" 订单" + orderSn + "已经是已付款或者已结算订单！");
			return ri;
		}
		
		if (orderInfo.getNoticeStatus() == Constant.OI_NOTICE_STATUS_UNNOTICED) {
			ri.setMessage(" 订单" + orderSn + "还是未通知收款状态！");
			return ri;
		}
		if (orderInfo.getTransType().intValue() == Constant.OI_TRANS_TYPE_PRESHIP
				&& orderInfo.getOrderStatus().intValue() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + orderSn + "交易类型是货到付款类型订单,订单状态必须是已确认状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	
	/**
	 * 未付款
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo unpay(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "未付款");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getPayStatus().byteValue() == Constant.OI_PAY_STATUS_UNPAYED) {
			ri.setMessage(" 订单" + orderSn + "已经是未付款订单！");
			return ri;
		}
		if (orderInfo.getTransType().intValue() == Constant.OI_TRANS_TYPE_PRESHIP
				&& orderInfo.getOrderStatus().intValue() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + orderSn + "交易类型是货到付款类型订单,订单状态必须是已确认状态！");
			return ri;
		}
		if (orderInfo.getTransType() != Constant.OI_TRANS_TYPE_PRESHIP
				&& (orderInfo.getOrderStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED ||
						orderInfo.getOrderStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED)) {
			ri.setMessage(" 订单" + orderSn + "交易类型是货到付款类型订单,发货状态必须是未发货或部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	
	/**
	 * 结算
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo settle(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "结算");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		// 订单确认状态检查
		// 未结算
		// 已确认 && (已发货.部分收货.客户已收货)
		// 货到付款 && (未付款 || 部分付款) && 未通知
		// 货到付款 && (部分付款) && 已通知
		// 非货到付款单 && 已付款
		if (orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + orderSn + "已经是已结算状态");
			return ri;
		}
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + orderSn + "要处于已确定状态");
			return ri;
		}
		if (orderInfo.getShipStatus().intValue() < Constant.OI_SHIP_STATUS_ALLSHIPED){
			ri.setMessage(" 订单" + orderSn + "发货状态必须是已发货.部分收货.客户已收货状态！");
			return ri;
		}
		if (orderInfo.getTransType().intValue() == Constant.OI_TRANS_TYPE_PRESHIP) {
			if (orderInfo.getNoticeStatus() != Constant.OI_NOTICE_STATUS_UNNOTICED 
					&& (orderInfo.getPayStatus() != Constant.OI_PAY_STATUS_UNPAYED
							|| orderInfo.getPayStatus() != Constant.OI_PAY_STATUS_PARTPAYED)) {
				ri.setMessage(" 订单" + orderSn + "交易类型是货到付款类型订单,通知状态必须是未通知状态,支付状态必须是未付款.部分付款状态！");
				return ri;
			} else if (orderInfo.getPayStatus() != Constant.OI_PAY_STATUS_PARTPAYED) {
				ri.setMessage(" 订单" + orderSn + "交易类型是货到付款类型订单,通知状态是已通知状态,支付状态必须是部分付款状态！");
			}
		} else if (orderInfo.getPayStatus() != Constant.OI_PAY_STATUS_PAYED) {
			ri.setMessage(" 订单" + orderSn + "要处于已付款状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	

	/**
	 * 占用释放库存
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo occupy(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}

	/**
	 * 复活
	 * @param orderInfo
	 * @param orderSn
	 * @param actionUser
	 * @param userId
	 * @return
	 */
	public static ReturnInfo relive(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		boolean flag = true;
		if (orderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_FINISHED) {
			flag = false;
		} else if (orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_SETTLED) {
			flag = false;
		} else if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			flag = false;
		} else if (orderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
			flag = false;
		}
		if (flag) {
			ri.setMessage(" 订单" + orderSn + "订单状态要处于已完成.已关闭.已取消状态||支付状态要处于已结算状态||发货状态要处于已发货.客户已收货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 生成拒收入库单
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo addrefuse(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
//		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "生成拒收入库单");
//		if (tempRi != null) {
//			return tempRi;
//		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getTransType() != Constant.OI_TRANS_TYPE_PRESHIP) {
			ri.setMessage(" 订单" + orderSn + "交易类型必须是货到付款类型订单");
			return ri;
		}
		if (!judgeQLT(orderInfo.getReferer())) {
			ri.setMessage(" 订单" + orderSn + "是全流通订单！");
			return ri;
		}
		if (orderInfo.getShipStatus() <= Constant.OI_SHIP_STATUS_PARTSHIPPED) {
			ri.setMessage(" 订单" + orderSn + "发货状态不能是未发货.部分发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	
	/**
	 * 生成退单
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo addReturn(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
//		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "生成退单");
//		if (tempRi != null) {
//			return tempRi;
//		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + orderSn + "还处于未发货状态");
			return ri;
		}
		if (!judgeQLT(orderInfo.getReferer())) {
			ri.setMessage(" 订单" + orderSn + "是全流通订单！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 生成额外退款单
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo addExtra(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (!judgeQLT(orderInfo.getReferer())) {
			ri.setMessage(" 订单" + orderSn + "是全流通订单！");
			return ri;
		}
//		ReturnInfo tempRi = checkConditionOfExecutionNoCancel(orderInfo, orderSn, "生成额外退款单");
//		if (tempRi != null) {
//			return tempRi;
//		}
//		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_UNSHIPPED) {
//			ri.setMessage(" 订单" + orderSn + "还处于未发货状态");
//			return ri;
//		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 生成换货单
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo addExchang(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
//		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "生成换货单");
//		if (tempRi != null) {
//			return tempRi;
//		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + orderSn + "还处于未发货状态");
			return ri;
		}
		if (!judgeQLT(orderInfo.getReferer())) {
			ri.setMessage(" 订单" + orderSn + "是全流通订单！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 编辑商品
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo goods(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "编辑商品");
		if (tempRi != null) {
			return tempRi;
		}
		
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + orderSn + "要处于未确认状态");
			return ri;
		}
		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		// 已经下发ERP的订制订单不能编辑订单商品
		if (orderInfo.getSource() != null && (orderInfo.getSource() == 4 || orderInfo.getSource() == 5)) {
			// TODO
//			if (orderInfo.getLastUpdateTime() != null) {
//				ri.setMessage(" 订单" + orderSn + "属于订制订单且已经下发不能编辑！");
//				return ri;
//			}
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 编辑收货人
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo consignee(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "编辑收货人");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage(" 订单" + orderSn + "不能处于未确认状态");
			return ri;
		}
		if (orderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + orderSn + "发货状态必须是未发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 编辑其他信息
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo other(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		
		ReturnInfo tempRi = checkConditionOfExecutionNoCancel(orderInfo, orderSn, "编辑其他信息");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + orderSn + "要处于未确认状态");
			return ri;
		}
		if (orderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + orderSn + "发货状态必须是未发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 编辑承运方式
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo shipping(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecutionNoCancel(orderInfo, orderSn, "编辑承运方式");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + orderSn + "要处于未确认状态");
			return ri;
		}
		if (orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + orderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	
	/**
	 * 编辑支付方式
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo payment(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecutionNoCancel(orderInfo, orderSn, "编辑支付方式");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + orderSn + "要处于未确认状态");
			return ri;
		}
		if (orderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + orderSn + "发货状态必须是未发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 编辑费用信息
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo money(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecutionNoCancel(orderInfo, orderSn, "编辑费用信息");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + orderSn + "要处于未确认状态");
			return ri;
		}
		if (orderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + orderSn + "发货状态必须是未发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}

	/**
	 * 分配
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo allocation(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "分配");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED
				&& orderInfo.getShipStatus() != Constant.OI_SHIP_STATUS_PARTSHIPPED) {
			ri.setMessage(" 订单" + orderSn + "发货状态必须是未发货.部分发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 仅退货
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo onlyReturn(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(orderInfo, orderSn, "仅退货");
		if (tempRi != null) {
			return tempRi;
		}
		if (orderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(orderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + orderSn + "要处于已锁定状态！");
			return ri;
		}
		if (orderInfo.getOrderType() != Constant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			ri.setMessage(" 订单" + orderSn + "订单类型必须是换货订单");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}

	/**
	 * 复制订单
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo copyorder(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (!judgeQLT(orderInfo.getReferer())) {
			ri.setMessage(" 订单" + orderSn + "是全流通订单！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	/**
	 * 同步分仓发货信息
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo swdi(MasterOrderInfo orderInfo, String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (orderInfo == null) {
			ri.setMessage("订单" + orderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		if (checkOrderShipStatus(orderInfo)) {
			ri.setMessage("订单" + orderSn + "状态是已完成或者已发货.客户已收货!");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	public static boolean checkOrderShipStatus(MasterOrderInfo orderInfo) {
		return orderInfo == null || orderInfo.getOrderStatus() == 2 // 取消
				|| orderInfo.getOrderStatus() == 3 // 无效
				|| orderInfo.getOrderStatus() == 7 // 完成
				|| orderInfo.getPayStatus() == 3 // 已结算
				|| orderInfo.getShipStatus() == 5; // 客户已收货
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
	
	/**
	 * 检查订单是否可以进行正常订单操作
	 * 返回null表示此订单可以执行正常订单操作操作，否则表示不能执行
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo checkConditionOfExecutionNoCancel(MasterOrderInfo orderInfo, String masterOrderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		/* 参数检验 */
		if (orderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		int orderStatus = orderInfo.getOrderStatus();
		if (orderStatus == Constant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage("订单" + masterOrderSn + "已处于完成状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		return null;
	}

	/**
	 * 检查订单是否可以进行正常订单操作
	 * 返回null表示此订单可以执行正常订单操作操作，否则表示不能执行
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public static ReturnInfo checkConditionOfExecutionNoFinished(MasterOrderInfo orderInfo, String masterOrderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		/* 参数检验 */
		if (orderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		int orderStatus = orderInfo.getOrderStatus();
		if (orderStatus == Constant.OI_ORDER_STATUS_CANCLED) {
			ri.setMessage("订单" + masterOrderSn + "已处于已取消状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		return null;
	}
	
	
	/**
	 * 检查订单是否可以进行正常订单操作
	 * 返回null表示此订单可以执行正常订单操作操作，否则表示不能执行
	 * @param orderInfo
	 * @param masterOrderSn
	 * @param actionType
	 * @return ReturnInfo
	 */
	public static ReturnInfo checkConditionOfExecution(MasterOrderInfo orderInfo, String masterOrderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		/* 参数检验 */
		if (orderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		int orderStatus = orderInfo.getOrderStatus();
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
	
	/**
	 * 判断此订单是否被当前操作者锁定
	 * @param lockStatus
	 * @param userId
	 * @return String
	 */
	public static boolean judgeSelfLock(Integer lockStatus, Integer userId, String actionUser) {
		boolean result = false;
		if (lockStatus == Constant.OI_LOCK_STATUS_UNLOCKED) {
			return result;
		}
		if ("admin".equals(actionUser)) {
			return true;
		}
		if(null == userId) {
//			logger.error("没有此用户名的信息");
		} else if (null == lockStatus || lockStatus.intValue() == Constant.OI_LOCK_STATUS_UNLOCKED) {
//			logger.error("此订单未被锁定");
		} else {
			if(lockStatus.equals(userId)) {
				result = true;
			} else {
//				logger.error("没有被此用户绑定,返回此订单被绑定用户的ID为:"+lockStatus);
			}
		}
		return result;
	}

	/**
	 * 全流通订单判断
	 * @param referer
	 * @return
	 */
	private static boolean judgeQLT(String referer) {
		if(StringUtils.equalsIgnoreCase(referer, "仓库发货")
				|| StringUtils.equalsIgnoreCase(referer, "门店发货")
				|| StringUtils.equalsIgnoreCase(referer, "全流通"))
			return false;
		return true;
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

	public Integer getCancel() {
		return cancel;
	}

	public void setCancel(Integer cancel) {
		this.cancel = cancel;
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

	public Integer getNotice() {
		return notice;
	}

	public void setNotice(Integer notice) {
		this.notice = notice;
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

	public Integer getRelive() {
		return relive;
	}

	public void setRelive(Integer relive) {
		this.relive = relive;
	}

	public Integer getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(Integer isPrint) {
		this.isPrint = isPrint;
	}

	public Integer getAddrefuse() {
		return addrefuse;
	}

	public void setAddrefuse(Integer addrefuse) {
		this.addrefuse = addrefuse;
	}

	public Integer getAddReturn() {
		return addReturn;
	}

	public void setAddReturn(Integer addReturn) {
		this.addReturn = addReturn;
	}

	public Integer getAddExchang() {
		return addExchang;
	}

	public void setAddExchang(Integer addExchang) {
		this.addExchang = addExchang;
	}

	public Integer getAddExtra() {
		return addExtra;
	}

	public void setAddExtra(Integer addExtra) {
		this.addExtra = addExtra;
	}

	public Integer getConsignee() {
		return consignee;
	}

	public void setConsignee(Integer consignee) {
		this.consignee = consignee;
	}

	public Integer getGoods() {
		return goods;
	}

	public void setGoods(Integer goods) {
		this.goods = goods;
	}

	public Integer getOther() {
		return other;
	}

	public void setOther(Integer other) {
		this.other = other;
	}

	public Integer getShipping() {
		return shipping;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public Integer getPayment() {
		return payment;
	}

	public void setPayment(Integer payment) {
		this.payment = payment;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getAllocation() {
		return allocation;
	}

	public void setAllocation(Integer allocation) {
		this.allocation = allocation;
	}

	public Integer getCommunicate() {
		return communicate;
	}

	public void setCommunicate(Integer communicate) {
		this.communicate = communicate;
	}

	/*public ButtonMenu getNorQuesMenu() {
		return norQuesMenu;
	}

	public void setNorQuesMenu(ButtonMenu norQuesMenu) {
		this.norQuesMenu = norQuesMenu;
	}

	public ButtonMenu getConUnConMenu() {
		return conUnConMenu;
	}

	public void setConUnConMenu(ButtonMenu conUnConMenu) {
		this.conUnConMenu = conUnConMenu;
	}

	public ButtonMenu getOccRealMenu() {
		return occRealMenu;
	}

	public void setOccRealMenu(ButtonMenu occRealMenu) {
		this.occRealMenu = occRealMenu;
	}

	public ButtonMenu getCancelMenu() {
		return cancelMenu;
	}

	public void setCancelMenu(ButtonMenu cancelMenu) {
		this.cancelMenu = cancelMenu;
	}*/

	public Integer getCopyorder() {
		return copyorder;
	}

	public void setCopyorder(Integer copyorder) {
		this.copyorder = copyorder;
	}

	public Integer getOnlyReturn() {
		return onlyReturn;
	}

	public void setOnlyReturn(Integer onlyReturn) {
		this.onlyReturn = onlyReturn;
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

	public Integer getDecode() {
		return decode;
	}

	public void setDecode(Integer decode) {
		this.decode = decode;
	}
}
