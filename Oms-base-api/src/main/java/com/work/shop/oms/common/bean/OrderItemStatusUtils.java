package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.bean.AdminUser;
import com.work.shop.oms.bean.MasterOrderQuestionBean;
import com.work.shop.oms.common.utils.OrderItemConstant;

/**
 * 订单状态工具类
 * @author lemon
 */
public class OrderItemStatusUtils implements Serializable {

	private static final long serialVersionUID = 8060727967397302982L;

	/**
	 * 锁定
	 */
	private Integer lock;

	/**
	 * 解锁
	 */
	private Integer unlock;

	/**
	 * 编辑收货人信息
	 */
	private Integer consignee;

	/**
	 * 编辑主单商品
	 */
	private Integer goods;

	/**
	 * 确认
	 */
	private Integer confirm;

	/**
	 * 未确认
	 */
	private Integer unconfirm;

	/**
	 * 取消
	 */
	private Integer cancel;

	/**
	 * 设为问题单
	 */
	private Integer question;

	/**
	 * 返回正常单
	 */
	private Integer normal;

	/**
	 * 通知收款
	 */
	private Integer notice;

	/**
	 * 结算
	 */
	private Integer settle;

	/**
	 * 页面确认未确认按钮
	 */
	private ButtonMenu conUnConMenu;

	/**
	 * 页面取消作废操作按钮
	 */
	private ButtonMenu cancelMenu;

	/**
	 * 页面正常单问题单操作按钮
	 */
	private ButtonMenu norQuesMenu;

	/**
	 * 复制订单
	 */
	private Integer copyorder;

	/**
	 * 沟通
	 */
	private Integer communicate;

	/**
	 * 已付款
	 */
	private Integer pay;

	/**
	 * 未付款
	 */
	private Integer unpay;

	/**
	 * 仅退货
	 */
	private Integer onlyReturn;

	/**
	 * 交货单生成退货退款单
	 */
	private Integer createReturn;

	/**
	 * 发送自提码短信
	 */
	private Integer sendGotCode;

	/**
	 * 额外退款单
	 */
	private Integer addExtra;

    /**
     * 人工分配
     */
	private Integer artificialDepot;

	/**
	 * 默认构造函数
	 */
	public OrderItemStatusUtils() {
		super();
	}

	/**
	 * 重构函数
	 * @param masterOrderInfo 订单详情
	 * @param adminUser 操作人
	 */
	public OrderItemStatusUtils(MasterOrderDetail masterOrderInfo, AdminUser adminUser) {
		super();
		String masterOrderSn = masterOrderInfo.getMasterOrderSn();
		Integer userId = Integer.parseInt(adminUser.getUserId());
		this.lock = lock(masterOrderInfo,masterOrderSn).getIsOk();
		this.unlock = unlock(masterOrderInfo,masterOrderSn).getIsOk();
		this.consignee = consignee(masterOrderInfo,masterOrderSn,adminUser.getUserName(),userId).getIsOk();
		this.goods = goods(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.confirm = confirm(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.unconfirm = unconfirm(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.cancel = cancel(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.question = question(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.normal = normal(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.notice = notice(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.settle = settle(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.copyorder = copyorder(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.communicate = 1;
		this.pay = pay(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.unpay = unpay(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.onlyReturn = onlyReturn(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.createReturn = createReturn(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		this.addExtra = addExtra(masterOrderInfo, masterOrderSn, adminUser.getUserName(), userId).getIsOk();
		if (masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_ORDER_STATUS_UNCONFIRMED) {
			this.conUnConMenu = new ButtonMenu("订单确认", "confirm", "confirm"); 
		} else {
			this.conUnConMenu = new ButtonMenu("订单未确认", "unconfirm", "unconfirm"); 
		}
		if (masterOrderInfo.getOrderType() == OrderItemConstant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			this.cancelMenu = new ButtonMenu("订单作废", "cancel", "cancel");
		} else {
			this.cancelMenu = new ButtonMenu("订单取消", "cancel", "cancel");
		}
		if (masterOrderInfo.getQuestionStatus() == OrderItemConstant.OI_QUESTION_STATUS_NORMAL) {
			this.norQuesMenu = new ButtonMenu("订单设为问题单", "question", "question");
		} else {
			this.norQuesMenu = new ButtonMenu("订单返回正常单", "normal", "normal");
		}
		this.sendGotCode = sendGotCode(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
        this.artificialDepot = artificialDepot(masterOrderInfo,masterOrderSn, adminUser.getUserName(), userId).getIsOk();
	}

	/**
	 * 是否可以退货
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作用户
	 * @param userId 下单人
	 * @return ReturnInfo
	 */
	public static ReturnInfo onlyReturn(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "仅退货");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getOrderType() != OrderItemConstant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			ri.setMessage(" 订单" + masterOrderSn + "订单类型必须是换货订单");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 复活
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo relive(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		boolean flag = true;
		if (masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_ORDER_STATUS_CANCLED) {
			flag = false;
		}
		if (flag) {
			ri.setMessage(" 订单" + masterOrderSn + "订单状态要处于已取消状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 分配
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo allocation(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "分配");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() != OrderItemConstant.OI_SHIP_STATUS_UNSHIPPED
				&& masterOrderInfo.getShipStatus() != OrderItemConstant.OI_SHIP_STATUS_PARTSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "发货状态必须是未发货.部分发货状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 未付款
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo unpay(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "未付款");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getPayStatus() != null && masterOrderInfo.getPayStatus() == OrderItemConstant.OI_PAY_STATUS_UNPAYED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是未付款订单！");
			return ri;
		}
		if (masterOrderInfo.getTransType().intValue() == OrderItemConstant.OI_TRANS_TYPE_PRESHIP
				&& masterOrderInfo.getOrderStatus().intValue() != OrderItemConstant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,订单状态必须是已确认状态！");
			return ri;
		}

		if (masterOrderInfo.getTransType() != OrderItemConstant.OI_TRANS_TYPE_PRESHIP) {
			if (masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLSHIPED ||
					masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLRECEIVED) {
				ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,发货状态必须是未发货或部分发货状态！");
			}
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 已付款
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo pay(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "已付款");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow() != null && masterOrderInfo.getIsnow() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发状态！");
			return ri;
		}
		if (masterOrderInfo.getPayStatus()== OrderItemConstant.OI_PAY_STATUS_PAYED
				|| masterOrderInfo.getPayStatus()== OrderItemConstant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是已付款或者已结算订单！");
			return ri;
		}
		
		if (masterOrderInfo.getNoticeStatus() == OrderItemConstant.OI_NOTICE_STATUS_UNNOTICED) {
			ri.setMessage(" 订单" + masterOrderSn + "还是未通知收款状态！");
			return ri;
		}
		if (masterOrderInfo.getTransType().intValue() == OrderItemConstant.OI_TRANS_TYPE_PRESHIP
				&& masterOrderInfo.getOrderStatus().intValue() != OrderItemConstant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,订单状态必须是已确认状态！");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 复制订单
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo copyorder(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo.getSource() == OrderItemConstant.MO_SOURCE_POS) {
			ri.setMessage(" 订单" + masterOrderSn + "是全流通订单！");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 生成额外退款单
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo addExtra(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "还处于未发货状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 生成退货退款单
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo createReturn(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage("订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (OrderItemConstant.OD_SHIP_STATUS_UNSHIPPED.equals(masterOrderInfo.getShipStatus())) {
			ri.setMessage("交货单" + masterOrderSn + "还处于未发货状态");
			return ri;
		}
		if (masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_ORDER_STATUS_UNCONFIRMED
				|| masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_ORDER_STATUS_CANCLED) {
			ri.setMessage("订单" + masterOrderSn + "要处于已确定|已完成状态");
			return ri;
		}
		if (masterOrderInfo.getPayStatus() == OrderItemConstant.OI_PAY_STATUS_PARTPAYED
				|| masterOrderInfo.getPayStatus() == OrderItemConstant.OI_PAY_STATUS_UNPAYED) {
			ri.setMessage("交货单" + masterOrderSn + "要处于已付款|已结算状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 同步分仓发货信息
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo swdi(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		if (checkOrderShipStatus(masterOrderInfo)) {
			ri.setMessage("订单" + masterOrderSn + "状态是已完成或者已发货.客户已收货!");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 判断订单发货状态
	 * @param masterOrderInfo 订单详情
	 * @return boolean
	 */
	public static boolean checkOrderShipStatus(MasterOrderDetail masterOrderInfo) {

		// OrderStatus 2取消、3完成
		// PayStatus 3 已结算
		// ShipStatus 5 客户已收货
		return masterOrderInfo == null || masterOrderInfo.getOrderStatus() == 2
				|| masterOrderInfo.getOrderStatus() == 3
				|| masterOrderInfo.getPayStatus() == 3
				|| masterOrderInfo.getShipStatus() == 5;
	}

	/**
	 * 占用释放库存
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo occupy(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 结算
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo settle(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "结算");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
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
		if (masterOrderInfo.getPayStatus() == OrderItemConstant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是已结算状态");
			return ri;
		}
		if (masterOrderInfo.getOrderStatus() != OrderItemConstant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已确定状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus().intValue() < OrderItemConstant.OI_SHIP_STATUS_ALLSHIPED){
			ri.setMessage(" 订单" + masterOrderSn + "发货状态必须是已发货.部分收货.客户已收货状态！");
			return ri;
		}
		if (masterOrderInfo.getTransType().intValue() == OrderItemConstant.OI_TRANS_TYPE_PRESHIP) {
			if (masterOrderInfo.getNoticeStatus() != OrderItemConstant.OI_NOTICE_STATUS_UNNOTICED 
					&& (masterOrderInfo.getPayStatus() != OrderItemConstant.OI_PAY_STATUS_UNPAYED
							|| masterOrderInfo.getPayStatus() != OrderItemConstant.OI_PAY_STATUS_PARTPAYED)) {
				ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,通知状态必须是未通知状态,支付状态必须是未付款.部分付款状态！");
				return ri;
			} else if (masterOrderInfo.getPayStatus() != OrderItemConstant.OI_PAY_STATUS_PARTPAYED) {
				ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款类型订单,通知状态是已通知状态,支付状态必须是部分付款状态！");
			}
		} else if (masterOrderInfo.getPayStatus() != OrderItemConstant.OI_PAY_STATUS_PAYED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已付款状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 通知收款
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo notice(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "通知收款");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getNoticeStatus() == OrderItemConstant.OI_NOTICE_STATUS_NOTICED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是通知收款状态！");
			return ri;
		}
		if (masterOrderInfo.getPayStatus() == OrderItemConstant.OI_PAY_STATUS_PAYED
				|| masterOrderInfo.getPayStatus() == OrderItemConstant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是已付款.已结算订单！");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		if (masterOrderInfo.getTransType() == OrderItemConstant.OI_TRANS_TYPE_PRESHIP
				&& masterOrderInfo.getOrderStatus() != OrderItemConstant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "交易类型是货到付款订单,订单状态要处于已确认状态！");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 转为正常单
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo normal(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "返回正常单");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getQuestionStatus() == OrderItemConstant.OI_QUESTION_STATUS_NORMAL) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是正常单！");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 设为问题单
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo question(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "设问题单");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_ORDER_STATUS_CANCLED ||
				masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage(" 订单" + masterOrderSn + "已取消或已完成！");
			return ri;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow() != null && masterOrderInfo.getIsnow() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发状态！");
			return ri;
		}
		if (masterOrderInfo.getQuestionStatus() == OrderItemConstant.OI_QUESTION_STATUS_QUESTION) {
			ri.setMessage(" 订单" + masterOrderSn + "已经是问题单！");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}
	
	public static ReturnInfo oneKeyQuest(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId){
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		if(masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_ORDER_STATUS_CANCLED||
				masterOrderInfo.getOrderStatus() == OrderItemConstant.OI_ORDER_STATUS_FINISHED){
			ri.setMessage(" 订单" + masterOrderSn + "已取消或已完成！");
			return ri;
		}

		if (masterOrderInfo.getPayStatus() == OrderItemConstant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 订单" + masterOrderSn + "已结算！");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 取消
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 下单人
	 * @return ReturnInfo
	 */
	public static ReturnInfo cancel(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "取消");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getOrderType() == OrderItemConstant.OI_ORDER_TYPE_EXCHANGE_ORDER
				&& masterOrderInfo.getPayStatus()>=OrderItemConstant.OI_PAY_STATUS_PAYED) {
			ri.setMessage(" 换货单" + masterOrderSn + "要处于未付款或部分付款状态！");
			return ri;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() != OrderItemConstant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 发送自提码短信
	 * @param master 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo sendGotCode(MasterOrderDetail master, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecution(master, masterOrderSn, "取消");
		if (tempRi != null) {
			return tempRi;
		}
		if(master.getIsCac() == null || master.getIsCac() == 0){
			ri.setMessage("订单" + masterOrderSn + "不是自提订单！");
			return ri;
		}
		if(master.getPayStatus() < OrderItemConstant.OI_PAY_STATUS_PAYED){
			ri.setMessage("订单" + masterOrderSn + "要处于已付款状态！");
			return ri;
		}
		if (master.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(master.getLockStatus(), userId, actionUser)) {
			ri.setMessage("订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (master.getShipStatus() != OrderItemConstant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 主单未确认
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo unconfirm(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow() != null && masterOrderInfo.getIsnow() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发状态！");
			return ri;
		}
		// 订单确认状态检查
		if (masterOrderInfo.getOrderStatus() != OrderItemConstant.OI_ORDER_STATUS_CONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已确定状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 主单确认
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo confirm(MasterOrderDetail masterOrderInfo,String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("订单" + masterOrderSn + "已经解锁");
			return ri;
		}
		// 未锁定 || 本人锁定 || admin
		if (masterOrderInfo.getLockStatus() != OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				&& (!judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser))) {
			ri.setMessage("订单" + masterOrderSn + "已经解锁");
			return ri;
		}
		/*if (masterOrderInfo.getIsnow() != null && masterOrderInfo.getIsnow() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发状态！");
			return ri;
		}*/
		if (!isPayed(masterOrderInfo.getTransType(), masterOrderInfo.getPayStatus())) {
			ri.setMessage("订单" + masterOrderSn + "要处于已付款状态！否则不能进行确认操作！");
			return ri;
		}
		if (masterOrderInfo.getOrderStatus() != OrderItemConstant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未确定状态");
			return ri;
		}
		if (masterOrderInfo.getQuestionStatus() != OrderItemConstant.OI_QUESTION_STATUS_NORMAL) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于正常单状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}
	
	/**
	 * 交易类型 transType 支付状态 payStatus 判断是都已经付款
	 * 
	 * (1)条件:  ((款到发货 || 担保交易) && 已付款) || 货到付款  结果：true
	 * 
	 * 
	 * @param transType 交易类型
	 * @param payStatus 支付状态
	 * @return true 修改; false 不修改
	 */
	public static boolean isPayed(short transType, short payStatus) {

		boolean payResult = false;

		if (transType == OrderItemConstant.OI_TRANS_TYPE_PRESHIP) {
			// 货到付款
			payResult = true;
		} else if (payStatus == OrderItemConstant.OI_PAY_STATUS_PAYED) {
			// 已付款
			payResult = true;
		}

		return payResult;
	}

	/**
	 * 编辑主单商品
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo goods(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "编辑商品");
		if (tempRi != null) {
			return tempRi;
		}
		
		if (masterOrderInfo.getOrderStatus() != OrderItemConstant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + masterOrderSn + "要处于未确认状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == OrderItemConstant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow() != null && masterOrderInfo.getIsnow() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发状态！");
			return ri;
		}
		if (masterOrderInfo.getOrderType() == OrderItemConstant.OI_ORDER_TYPE_EXCHANGE_ORDER) {
			ri.setMessage(" 订单" + masterOrderSn + "是换货订单,不允许编辑！");
			return ri;
		}

		// 已经下发ERP的订制订单不能编辑订单商品
		if (masterOrderInfo.getSource() != null) {
			if (masterOrderInfo.getSource() == OrderItemConstant.OD_SOURCE_C2M || masterOrderInfo.getSource() == OrderItemConstant.OD_SOURCE_C2B) {
				if (masterOrderInfo.getUpdateTime()!= null) {
					ri.setMessage(" 订单" + masterOrderSn + "属于订制订单且已经下发不能编辑！");
					return ri;
				}
			}
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 编辑付款单费用信息
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 原操作人
	 * @return ReturnInfo
	 */
	public static ReturnInfo money(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		ReturnInfo tempRi = checkConditionOfExecutionNoCancel(masterOrderInfo, masterOrderSn, "编辑费用信息");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}
		if (masterOrderInfo.getIsnow() != null && masterOrderInfo.getIsnow() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发状态！");
			return ri;
		}
		if (masterOrderInfo.getOrderStatus() != OrderItemConstant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + masterOrderSn + "要处于未确认状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() != OrderItemConstant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "发货状态必须是未发货状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 检查订单是否可以进行正常订单操作
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionType 操作类型
	 * @return ReturnInfo
	 */
	public static ReturnInfo checkConditionOfExecutionNoCancel(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		// 参数检验
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		if (masterOrderInfo.getIsnow() != null && masterOrderInfo.getIsnow() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发状态！");
			return ri;
		}
		int orderStatus = masterOrderInfo.getOrderStatus();
		if (orderStatus == OrderItemConstant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage("订单" + masterOrderSn + "已处于完成状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		return null;
	}

	/**
	 * 判断此订单是否被当前操作者锁定
	 * @param lockStatus 锁定状态
	 * @param userId 下单人
	 * @param actionUser 操作人
	 * @return boolean
	 */
	public static boolean judgeSelfLock(Integer lockStatus, Integer userId, String actionUser) {
		boolean result = false;
		if (lockStatus == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
			return result;
		}
		if (OrderItemConstant.ACTION_USER_ADMIN.equals(actionUser)) {
			return true;
		}
		if(null == userId) {
		} else if (null == lockStatus || lockStatus == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
		} else {
			if(lockStatus.equals(userId)) {
				result = true;
			} else {
			}
		}
		return result;
	}

	/**
	 * 检查订单是否可以进行正常订单操作
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionType 操作类型
	 * @return ReturnInfo
	 */
	public static ReturnInfo checkConditionOfExecutionNoFinished(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		// 参数检验
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		int orderStatus = masterOrderInfo.getOrderStatus();
		return null;
	}

	/**
	 * 获取锁定按钮状态
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @return ReturnInfo
	 */
	public static ReturnInfo lock(MasterOrderDetail masterOrderInfo, String masterOrderSn) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (masterOrderInfo == null) {
			ri.setMessage("没有获取订单" + masterOrderInfo + "的信息！不能进行锁定操作！");
			return ri;
		}
		if (masterOrderInfo.getLockStatus() != OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage(" 订单" + masterOrderSn + "已经被锁定！");
			return ri;
		}
		ReturnInfo tempRi = checkConditionOfExecutionNoFinished(masterOrderInfo, masterOrderSn, "锁定");
		if (tempRi != null) {
			return tempRi;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 获取解锁按钮状态
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @return ReturnInfo
	 */
	public static ReturnInfo unlock(MasterOrderDetail masterOrderInfo, String masterOrderSn) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		if (null == masterOrderInfo) {
			ri.setMessage("没有获取订单" + masterOrderSn + "的信息！不能进行解锁操作！");
			return ri;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("订单" + masterOrderSn + "已经解锁");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 编辑收货人
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @param userId 下单人
	 * @return ReturnInfo
	 */
	public static ReturnInfo consignee(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		
		ReturnInfo tempRi = checkConditionOfExecution(masterOrderInfo, masterOrderSn, "编辑收货人");
		if (tempRi != null) {
			return tempRi;
		}
		if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
			return ri;
		}

		if (masterOrderInfo.getIsnow().intValue() == 1) {
			ri.setMessage(" 订单" + masterOrderSn + "要处于未下发状态！");
			return ri;
		}

		if (masterOrderInfo.getShipStatus() != OrderItemConstant.OI_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage(" 订单" + masterOrderSn + "发货状态必须是未发货状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	/**
	 * 检查订单是否可以进行正常订单操作
	 * @param masterOrderInfo 订单详情
	 * @param masterOrderSn 订单编码
	 * @param actionType 操作类型
	 * @return ReturnInfo
	 */
	public static ReturnInfo checkConditionOfExecution(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
		// 参数检验
		if (masterOrderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		int orderStatus = masterOrderInfo.getOrderStatus();
		if (orderStatus == OrderItemConstant.OI_ORDER_STATUS_CANCLED) {
			ri.setMessage("订单" + masterOrderSn + "已处于已取消状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		if (orderStatus == OrderItemConstant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage("订单" + masterOrderSn + "已处于完成状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		return null;
	}

    /**
     * 校验是否可以人工分配
     * @param masterOrderInfo
     * @param masterOrderSn
     * @param actionUser
     * @param userId
     * @return
     */
    public static ReturnInfo artificialDepot(MasterOrderDetail masterOrderInfo, String masterOrderSn, String actionUser, Integer userId) {
        ReturnInfo ri = new ReturnInfo(OrderItemConstant.OS_NO);
        // 参数检验
        if (masterOrderInfo == null) {
            ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
            return ri;
        }

        //校验是否为当前用户锁定
        if (masterOrderInfo.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
            || !judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser)) {
            ri.setMessage(" 订单" + masterOrderSn + "要处于已锁定状态！");
            return ri;
        }

        //校验拆单
        Byte splitStatus = masterOrderInfo.getSplitStatus();
        if (splitStatus == 0) {
            ri.setMessage("订单" + masterOrderSn + "未拆单");
            return ri;
        }

        //校验交货单
        List<OrderItemDepotDetail> depotDetails = masterOrderInfo.getDepotDetails();
        if (depotDetails == null || depotDetails.size() < 1) {
            ri.setMessage("订单" + masterOrderSn + "无有效交货单信息");
            return ri;
        }
        boolean flag = false;
        for (OrderItemDepotDetail depotDetail : depotDetails) {
            int depotStatus = depotDetail.getDepotStatus();
            if (depotStatus == 0) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            ri.setMessage("订单" + masterOrderSn + "已分仓");
            return ri;
        }

        ri.setIsOk(OrderItemConstant.OS_YES);
        return ri;
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

	public Integer getOnlyReturn() {
		return onlyReturn;
	}

	public void setOnlyReturn(Integer onlyReturn) {
		this.onlyReturn = onlyReturn;
	}

	public Integer getCreateReturn() {
		return createReturn;
	}

	public void setCreateReturn(Integer createReturn) {
		this.createReturn = createReturn;
	}

	public Integer getSendGotCode() {
		return sendGotCode;
	}

	public void setSendGotCode(Integer sendGotCode) {
		this.sendGotCode = sendGotCode;
	}

	public Integer getAddExtra() {
		return addExtra;
	}

	public void setAddExtra(Integer addExtra) {
		this.addExtra = addExtra;
	}

    public Integer getArtificialDepot() {
        return artificialDepot;
    }

    public void setArtificialDepot(Integer artificialDepot) {
        this.artificialDepot = artificialDepot;
    }
}
