package com.work.shop.oms.common.utils;

import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.common.bean.ButtonMenu;
import com.work.shop.oms.common.bean.MasterOrderDetail;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.vo.AdminUser;

public class DistributeOrderStatusUtils {
	/**
	 * 注意  交货单的支付状态以主单的支付状态为准  交货单不一定有支付状态
	 */
	private Integer good;//交货单商品编辑
	private Integer sonConfirm;//交货单确认
	private Integer sonUnConfirm;//交货单未确认
	private Integer sonCancel;//交货单取消
	private Integer sonQuestion;//交货单设问题单
	private Integer sonNormal;//交货单返回正常单
	private Integer sonSwdi;//交货单同步分仓发货信息
	private Integer sonAddrefuse;//交货单生成拒收入库单
	private Integer sonAddReturn;//交货单生成退货退款单
	private Integer sonAddExtra;//交货单生成额外退款单
	private Integer sonAddLostReturn;//交货单生成失货退货单
	private Integer sonAllocation;//交货单分配
	private Integer sonAddExchang;//生成换货单
	private ButtonMenu confirmBtn;//页面确认未确认按钮
	private ButtonMenu setQuesBtn;//页面设置问题单返回正常单按钮
	
	//重载构造函数
	public DistributeOrderStatusUtils(MasterOrderDetail masterOrderInfo,OrderDistribute orderDistribute,String orderSn,AdminUser adminUser){
		this.good = good(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		/**
		 * 以下都设置成1 测试用  后期再一个一个设置交货单的按钮状态
		 */
		this.sonConfirm = sonConfirm(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonUnConfirm = sonUnConfirm(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonCancel = sonCancel(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonQuestion = sonQuestion(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonNormal = sonNormal(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonSwdi = sonSwdi(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonAddrefuse = sonAddrefuse(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonAddReturn = sonAddReturn(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonAddExtra = sonAddExtra(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonAddLostReturn = sonAddLostReturn(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonAllocation = sonAllocation(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		this.sonAddExchang = sonAddExchang(masterOrderInfo,orderDistribute,orderSn,adminUser.getUserName(),Integer.parseInt(adminUser.getUserId())).getIsOk();
		
		if (orderDistribute.getOrderStatus() == Constant.OD_ORDER_STATUS_UNCONFIRMED) {
			this.confirmBtn = new ButtonMenu("交货单确认", "sonConfirm", "sonConfirm"); 
		} else {
			this.confirmBtn = new ButtonMenu("交货单未确认", "sonUnConfirm", "sonUnConfirm"); 
		}
		if (orderDistribute.getQuestionStatus() == Constant.OD_QUESTION_STATUS_NO) {
			this.setQuesBtn = new ButtonMenu("交货单设为问题单", "sonQuestion", "sonQuestion"); 
		} else {
			this.setQuesBtn = new ButtonMenu("交货单返回正常单", "sonNormal", "sonNormal"); 
		}
	}
	
	//生成换货单
	public static ReturnInfo sonAddExchang(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !(OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser))) {
			ri.setMessage(" 订单" + standardMasOrdInfo.getMasterOrderSn() + "要处于已锁定状态！");
			return ri;
		}
		if (orderDistribute.getSource() == Constant.OD_SOURCE_POS) {
			ri.setMessage("交货单" + orderSn + "是全流通订单！");
			return ri;
		}
//		if (orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_UNSHIPPED) {
//			ri.setMessage(" 交货单单" + orderSn + "还处于未发货状态");
//			return ri;
//		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//交货单分配
	public static ReturnInfo sonAllocation(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = OrderStatusUtils.checkConditionOfExecution(standardMasOrdInfo, standardMasOrdInfo.getMasterOrderSn(), "分配");
		if (tempRi != null) {
			return tempRi;
		}
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !(OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser))) {
			ri.setMessage(" 订单" + standardMasOrdInfo.getMasterOrderSn() + "要处于已锁定状态！");
			return ri;
		}
		if(orderDistribute.getLastUpdateTime()==null||"".equals(orderDistribute.getLastUpdateTime())){
			ri.setMessage(" 交货单" + orderSn + "要处于已下发状态！");
			return ri;
		}
		if(orderDistribute.getDepotStatus() == Constant.OD_DEPOT_NOTICE){
			ri.setMessage(" 交货单" + orderSn + "已分配！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//生成失货退款单
	public static ReturnInfo sonAddLostReturn(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage("交货单" + orderSn + "的主单要处于已锁定状态！");
			return ri;
		}
		if (orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage("交货单" + orderSn + "还处于未发货状态");
			return ri;
		}
		if (orderDistribute.getSource() == Constant.OD_SOURCE_POS) {
			ri.setMessage("交货单" + orderSn + "是全流通订单！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//生成额外退款单
	public static ReturnInfo sonAddExtra(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage("交货单" + orderSn + "的主单要处于已锁定状态！");
			return ri;
		}
		if (orderDistribute.getSource() == Constant.OD_SOURCE_POS) {
			ri.setMessage("交货单" + orderSn + "是全流通订单！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//生成退货退款单
	public static ReturnInfo sonAddReturn(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage("交货单" + orderSn + "的主单要处于已锁定状态！");
			return ri;
		}
		/*if (orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage("交货单" + orderSn + "还处于未发货状态");
			return ri;
		}*/
		if (standardMasOrdInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_UNCONFIRMED
				|| standardMasOrdInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
			ri.setMessage("交货单" + orderSn + "要处于已确定|已完成状态");
			return ri;
		}
		if (standardMasOrdInfo.getPayStatus() == Constant.OI_PAY_STATUS_PARTPAYED
				|| standardMasOrdInfo.getPayStatus() == Constant.OI_PAY_STATUS_UNPAYED) {
			ri.setMessage("交货单" + orderSn + "要处于已付款|已结算状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//生成拒收入库单
	public static ReturnInfo sonAddrefuse(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage("交货单" + orderSn + "的主单要处于已锁定状态！");
			return ri;
		}
		if (orderDistribute.getTransType() != Constant.OD_TRANS_TYPE_PRESHIP) {
			ri.setMessage("交货单" + orderSn + "交易类型必须是货到付款类型订单");
			return ri;
		}
		if (orderDistribute.getSource() == Constant.OD_SOURCE_POS) {
			ri.setMessage("交货单" + orderSn + "是全流通订单！");
			return ri;
		}
		if (orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_UNSHIPPED||
				orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_PARTSHIPPED) {
			ri.setMessage("交货单" + orderSn + "发货状态不能是未发货.部分发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//同步分仓发货信息
		public static ReturnInfo sonSwdi(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
			ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
			if (standardMasOrdInfo == null) {
				ri.setMessage("订单" + standardMasOrdInfo.getMasterOrderSn() + "在近三个月的记录中，没有取得订单信息!");
				return ri;
			}
			if (orderDistribute.getOrderStatus()==Constant.OD_ORDER_STATUS_CANCLED||
					orderDistribute.getOrderStatus()==Constant.OD_ORDER_STATUS_FINISHED||
					orderDistribute.getShipStatus()==Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED) {
				ri.setMessage("交货单" + orderSn + "状态是已完成或者已发货.客户已收货!");
				return ri;
			}
			ri.setIsOk(Constant.OS_YES);
			return ri;
		}
	
	//返回正常单
	public static ReturnInfo sonNormal(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage("交货单" + orderSn + "的主单要处于已锁定状态！");
			return ri;
		}
		if (orderDistribute.getQuestionStatus() == Constant.OD_QUESTION_STATUS_NO) {
			ri.setMessage("交货单" + orderSn + "已经是正常单！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//设置问题单
	public static ReturnInfo sonQuestion(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage("交货单" + orderSn + "的主单要处于已锁定状态！");
			return ri;
		}
		if(orderDistribute.getOrderStatus()==Constant.OD_ORDER_STATUS_CANCLED||
				orderDistribute.getOrderStatus()==Constant.OD_ORDER_STATUS_FINISHED){
			ri.setMessage("交货单" + orderSn + "处于已取消或已完成状态！");
			return ri;
		}
		if (orderDistribute.getQuestionStatus() == Constant.OD_QUESTION_STATUS_YES) {
			ri.setMessage("交货单" + orderSn + "已经是问题单！");
			return ri;
		}
		if (orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_ALLSHIPED
				|| orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_CUSTOMER_PARTRECEIVED
				|| orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED) {
			ri.setMessage("交货单" + orderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//交货单取消
	public static ReturnInfo sonCancel(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		ReturnInfo tempRi = OrderStatusUtils.checkConditionOfExecution(standardMasOrdInfo, standardMasOrdInfo.getMasterOrderSn(), "取消");
		if (tempRi != null) {
			return tempRi;
		}
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage("交货单" + orderSn + "的主单要处于已锁定状态！");
			return ri;
		}
		if (orderDistribute.getOrderStatus() != Constant.OD_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("交货单" + orderSn + "不能处于未确认状态");
			return ri;
		}
		if (orderDistribute.getShipStatus() != Constant.OD_SHIP_STATUS_UNSHIPPED) {
			ri.setMessage("交货单" + orderSn + "要处于未发货状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//交货单未确认
	public static ReturnInfo sonUnConfirm(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser)) {
			ri.setMessage("交货单" + orderSn + "的主单要处于已锁定状态！");
			return ri;
		}
		// 订单确认状态检查
		if (orderDistribute.getOrderStatus() != Constant.OD_ORDER_STATUS_CONFIRMED) {
			ri.setMessage("交货单" + orderSn + "要处于已确定状态");
			return ri;
		}
		if (orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_ALLSHIPED
				|| orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_CUSTOMER_PARTRECEIVED
				|| orderDistribute.getShipStatus() == Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED) {
			ri.setMessage("交货单" + orderSn + "要处于未发货.部分发货状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//交货单单确认  已付款的正常单
	public static ReturnInfo sonConfirm(MasterOrderDetail standardMasOrdInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		
		if (standardMasOrdInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("交货单" + orderSn + "的主单已经解锁");
			return ri;
		}
		// 未锁定 || 本人锁定 || admin
		if (standardMasOrdInfo.getLockStatus() != Constant.OI_LOCK_STATUS_UNLOCKED
				&& (!OrderStatusUtils.judgeSelfLock(standardMasOrdInfo.getLockStatus(), userId, actionUser))) {
			ri.setMessage("交货单" + orderSn + "的主单已经解锁");
			return ri;
		}
		if (!OrderStatusUtils.isPayed(standardMasOrdInfo.getTransType(), standardMasOrdInfo.getPayStatus())) {
			ri.setMessage("交货单" + orderSn + "的主单要处于已付款状态！否则不能进行确认操作！");
			return ri;
		}
		if (orderDistribute.getOrderStatus() != Constant.OD_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("交货单" + orderSn + "要处于未确定状态");
			return ri;
		}
		if (orderDistribute.getQuestionStatus() == Constant.OD_QUESTION_STATUS_YES) {
			ri.setMessage("交货单" + orderSn + "要处于正常单状态");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		return ri;
	}
	
	//交货单商品编辑
	public static ReturnInfo good(MasterOrderDetail masterOrderInfo,OrderDistribute orderDistribute,String orderSn, String actionUser, Integer userId){
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderInfo.getLockStatus() == Constant.OI_LOCK_STATUS_UNLOCKED
				|| !(OrderStatusUtils.judgeSelfLock(masterOrderInfo.getLockStatus(), userId, actionUser))) {
			ri.setMessage(" 订单" + masterOrderInfo.getMasterOrderSn() + "要处于已锁定状态！");
			return ri;
		}
		ReturnInfo tempRi = OrderStatusUtils.checkConditionOfExecution(masterOrderInfo, masterOrderInfo.getMasterOrderSn(), "编辑商品");
		if (tempRi != null) {
			return tempRi;
		}
		
		if (masterOrderInfo.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED) {
			ri.setMessage("订单" + masterOrderInfo.getMasterOrderSn() + "要处于未确认状态");
			return ri;
		}
		if (masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED
				|| masterOrderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			ri.setMessage(" 订单" + masterOrderInfo.getMasterOrderSn() + "要处于未发货.部分发货状态！");
			return ri;
		}
		// 已经下发ERP的订制订单不能编辑订单商品
		if (masterOrderInfo.getSource() != null && (masterOrderInfo.getSource() == 4 || masterOrderInfo.getSource() == 5)) {
			if (masterOrderInfo.getUpdateTime()!= null) {
				ri.setMessage(" 订单" + masterOrderInfo.getMasterOrderSn() + "属于订制订单且已经下发不能编辑！");
				return ri;
			}
		}
		if(orderDistribute.getShipStatus()==Constant.OD_SHIP_STATUS_ALLSHIPED){
			ri.setMessage(" 交货单" + orderSn + "已发货！");
			return ri;
		}
		if(orderDistribute.getOrderStatus()!=Constant.OD_ORDER_STATUS_UNCONFIRMED){
			ri.setMessage(" 交货单" + orderSn + "要处于未确认状态！");
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		//主单商品可编辑的话  再考虑子单的各种状况
		return ri;
	}
	
	
	
	public Integer getGood() {
		return good;
	}
	public void setGood(Integer good) {
		this.good = good;
	}

	public Integer getSonConfirm() {
		return sonConfirm;
	}

	public void setSonConfirm(Integer sonConfirm) {
		this.sonConfirm = sonConfirm;
	}

	public Integer getSonUnConfirm() {
		return sonUnConfirm;
	}

	public void setSonUnConfirm(Integer sonUnConfirm) {
		this.sonUnConfirm = sonUnConfirm;
	}

	public Integer getSonCancel() {
		return sonCancel;
	}

	public void setSonCancel(Integer sonCancel) {
		this.sonCancel = sonCancel;
	}

	public Integer getSonQuestion() {
		return sonQuestion;
	}

	public void setSonQuestion(Integer sonQuestion) {
		this.sonQuestion = sonQuestion;
	}

	public Integer getSonNormal() {
		return sonNormal;
	}

	public void setSonNormal(Integer sonNormal) {
		this.sonNormal = sonNormal;
	}

	public Integer getSonSwdi() {
		return sonSwdi;
	}

	public void setSonSwdi(Integer sonSwdi) {
		this.sonSwdi = sonSwdi;
	}

	public Integer getSonAddrefuse() {
		return sonAddrefuse;
	}

	public void setSonAddrefuse(Integer sonAddrefuse) {
		this.sonAddrefuse = sonAddrefuse;
	}

	public Integer getSonAddReturn() {
		return sonAddReturn;
	}

	public void setSonAddReturn(Integer sonAddReturn) {
		this.sonAddReturn = sonAddReturn;
	}

	public Integer getSonAddExtra() {
		return sonAddExtra;
	}

	public void setSonAddExtra(Integer sonAddExtra) {
		this.sonAddExtra = sonAddExtra;
	}

	public Integer getSonAddLostReturn() {
		return sonAddLostReturn;
	}

	public void setSonAddLostReturn(Integer sonAddLostReturn) {
		this.sonAddLostReturn = sonAddLostReturn;
	}

	public ButtonMenu getConfirmBtn() {
		return confirmBtn;
	}

	public void setConfirmBtn(ButtonMenu confirmBtn) {
		this.confirmBtn = confirmBtn;
	}

	public ButtonMenu getSetQuesBtn() {
		return setQuesBtn;
	}

	public void setSetQuesBtn(ButtonMenu setQuesBtn) {
		this.setQuesBtn = setQuesBtn;
	}
	public Integer getSonAllocation() {
		return sonAllocation;
	}
	public void setSonAllocation(Integer sonAllocation) {
		this.sonAllocation = sonAllocation;
	}
	public Integer getSonAddExchang() {
		return sonAddExchang;
	}
	public void setSonAddExchang(Integer sonAddExchang) {
		this.sonAddExchang = sonAddExchang;
	}
	
	
	

}
