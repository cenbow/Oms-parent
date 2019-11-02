package com.work.shop.oms.common.bean;

import java.io.Serializable;

import com.work.shop.oms.bean.AdminUser;
import com.work.shop.oms.common.utils.OrderItemConstant;
import com.work.shop.oms.vo.ReturnCommonVO;

public class ReturnItemStatusUtils implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8060727967397302982L;
	private Integer lock;//锁定
	private Integer unlock;//解锁
	private Integer confirm;//确认
	private Integer unconfirm;//未确认
	private Integer cancel;//取消
	private Integer settle;//结算
	private Integer communicate;//沟通
    private Integer downRefund;//线下退款

    public  ReturnItemStatusUtils() {

    }

	//重载构造函数
	public ReturnItemStatusUtils(ReturnCommonVO returnCommon, AdminUser adminUser){
		super();
		String returnSn = returnCommon.getReturnSn();
		Integer userId = Integer.parseInt(adminUser.getUserId());
		this.lock = lock(returnCommon, returnSn).getIsOk();
		this.unlock = unlock(returnCommon, returnSn).getIsOk();
		this.confirm = confirm(returnCommon, returnSn, adminUser.getUserName(), userId).getIsOk();
		this.unconfirm = unconfirm(returnCommon, returnSn, adminUser.getUserName(), userId).getIsOk();
		this.cancel = cancel(returnCommon, returnSn, adminUser.getUserName(), userId).getIsOk();
		this.settle = settle(returnCommon, returnSn, adminUser.getUserName(), userId).getIsOk();
        this.downRefund = downRefund(returnCommon, returnSn, adminUser.getUserName(), userId).getIsOk();
		this.communicate = 1;
	}

    /**
     * 线下退款
     * @param returnCommon
     * @param returnSn
     * @param userName
     * @param userId
     * @return
     */
    private static ReturnInfo<String> downRefund(ReturnCommonVO returnCommon, String returnSn, String userName, Integer userId) {
        ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
        ReturnInfo<String> tempRi = checkConditionOfExecution(returnCommon, returnSn, "线下退款");
        if (tempRi != null) {
            return tempRi;
        }
        if (returnCommon.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
            || !judgeSelfLock(returnCommon.getLockStatus(), userId, userName)) {
            ri.setMessage(" 退单" + returnSn + "要处于已锁定状态！");
            return ri;
        }
        // 未结算
        // 已确认
        if (returnCommon.getPayStatus() == OrderItemConstant.OI_PAY_STATUS_SETTLED) {
            ri.setMessage(" 退单" + returnSn + "已经是已结算状态");
            return ri;
        }
        if (returnCommon.getReturnOrderStatus().intValue() != ConstantValues.ORDERRETURN_STATUS.CONFIRMED) {
            ri.setMessage(" 退单" + returnSn + "要处于已确定状态");
            return ri;
        }
        if (returnCommon.getBackBalance() == ConstantValues.ORDERRETURN_STATUS.CONFIRMED) {
            ri.setMessage(" 退单" + returnSn + "已退款");
            return ri;
        }

        ri.setIsOk(OrderItemConstant.OS_YES);
        return ri;
    }


    public static boolean checkOrderShipStatus(MasterOrderDetail masterOrderInfo) {
		return masterOrderInfo == null || masterOrderInfo.getOrderStatus() == 2 // 取消
				|| masterOrderInfo.getOrderStatus() == 3 // 完成
				|| masterOrderInfo.getPayStatus() == 3 // 已结算
				|| masterOrderInfo.getShipStatus() == 5; // 客户已收货
	}
	
	//结算
	public static ReturnInfo<String> settle(ReturnCommonVO returnCommon,String returnSn, String actionUser, Integer userId) {
		ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
		ReturnInfo<String> tempRi = checkConditionOfExecution(returnCommon, returnSn, "结算");
		if (tempRi != null) {
			return tempRi;
		}
		if (returnCommon.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(returnCommon.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 退单" + returnSn + "要处于已锁定状态！");
			return ri;
		}
		// 未结算
		// 已确认
		if (returnCommon.getPayStatus() == OrderItemConstant.OI_PAY_STATUS_SETTLED) {
			ri.setMessage(" 退单" + returnSn + "已经是已结算状态");
			return ri;
		}
		if (returnCommon.getReturnOrderStatus().intValue() != ConstantValues.ORDERRETURN_STATUS.CONFIRMED) {
			ri.setMessage(" 退单" + returnSn + "要处于已确定状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}
	
	//取消
	public static ReturnInfo<String> cancel(ReturnCommonVO returnCommon,String returnSn, String actionUser, Integer userId) {
		ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
		ReturnInfo<String> tempRi = checkConditionOfExecution(returnCommon, returnSn, "取消");
		if (tempRi != null) {
			return tempRi;
		}
		if (returnCommon.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(returnCommon.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 退单" + returnSn + "要处于已锁定状态！");
			return ri;
		}
		if (returnCommon.getCheckinStatus() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE
				|| returnCommon.getCheckinStatus() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE) {
			ri.setMessage(" 退单" + returnSn + "要处于未入库状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}
	
	//主单未确认
	public static ReturnInfo<String> unconfirm(ReturnCommonVO returnCommon,String returnSn, String actionUser, Integer userId) {
		ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
		if (returnCommon.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(returnCommon.getLockStatus(), userId, actionUser)) {
			ri.setMessage(" 退单" + returnSn + "要处于已锁定状态！");
			return ri;
		}
		// 退单确认状态检查
		if (returnCommon.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.CONFIRMED) {
			ri.setMessage(" 退单" + returnSn + "要处于已确定状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}
	
	//主单确认
	public static ReturnInfo<String> confirm(ReturnCommonVO returnCommon,String returnSn, String actionUser, Integer userId) {
		ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
		if (returnCommon.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("退单" + returnSn + "已经解锁");
			return ri;
		}
		// 未锁定 || 本人锁定 || admin
		if (returnCommon.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(returnCommon.getLockStatus(), userId, actionUser)) {
			ri.setMessage("退单" + returnSn + "已经解锁");
			return ri;
		}
		
		if (returnCommon.getReturnOrderStatus().intValue() != ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM) {
			ri.setMessage(" 退单" + returnSn + "要处于未确定状态");
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
	 * @param orderInfo
	 * @return true 修改; false 不修改
	 */
	public static boolean isPayed(short transType, short payStatus) {
		if(((transType == OrderItemConstant.OI_TRANS_TYPE_PREPAY 
				|| transType == OrderItemConstant.OI_TRANS_TYPE_GUARANTEE) 
			&& payStatus == OrderItemConstant.OI_PAY_STATUS_PAYED)
				|| transType == OrderItemConstant.OI_TRANS_TYPE_PRESHIP) {
			return true;
		}
		return false;
	}

	//编辑付款单费用信息
	public static ReturnInfo<String> money(ReturnCommonVO returnCommon,String returnSn, String actionUser, Integer userId) {
		ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
		ReturnInfo<String> tempRi = checkConditionOfExecutionNoCancel(returnCommon, returnSn, "编辑费用信息");
		if (tempRi != null) {
			return tempRi;
		}
		if (returnCommon.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED
				|| !judgeSelfLock(returnCommon.getLockStatus(), userId, actionUser)) {
			ri.setMessage("退单" + returnSn + "要处于已锁定状态！");
			return ri;
		}
		if (returnCommon.getReturnOrderStatus().intValue() != ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM) {
			ri.setMessage("退单" + returnSn + "要处于未确认状态");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}
	
	//检查退单是否可以进行正常退单操作
	public static ReturnInfo<String> checkConditionOfExecutionNoCancel(ReturnCommonVO returnCommon,String returnSn, String actionType) {
		ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
		/* 参数检验 */
		if (returnCommon == null) {
			ri.setMessage("退单" + returnSn + "不存在!");
			return ri;
		}
		if (returnCommon.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE) {
			ri.setMessage("退单" + returnSn + "已处于完成状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		return null;
	}
	
	//判断此退单是否被当前操作者锁定
	public static boolean judgeSelfLock(Integer lockStatus, Integer userId, String actionUser) {
		boolean result = false;
		if (lockStatus == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
			return result;
		}
		if ("admin".equals(actionUser)) {
			return true;
		}
		if(null == userId) {
		} else if (null == lockStatus || lockStatus.intValue() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
		} else {
			if(lockStatus.equals(userId)) {
				result = true;
			} else {
			}
		}
		return result;
	}
	
	//获取锁定按钮状态
	public static ReturnInfo<String> lock(ReturnCommonVO returnCommon,String returnSn){
		ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
		if(returnCommon==null){
			ri.setMessage("没有获取退单" + returnSn + "的信息！不能进行锁定操作！");
			return ri;
		}
		if (returnCommon.getLockStatus() != OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage(" 退单" + returnSn + "已经被锁定！");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}
	
	//获取解锁按钮状态
	public static ReturnInfo<String> unlock(ReturnCommonVO returnCommon,String masterOrderSn){
		ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
		if (null == returnCommon) {
			ri.setMessage("没有获取退单" + masterOrderSn + "的信息！不能进行解锁操作！");
			return ri;
		}
		if (returnCommon.getLockStatus() == OrderItemConstant.OI_LOCK_STATUS_UNLOCKED) {
			ri.setMessage("退单" + masterOrderSn + "已经解锁");
			return ri;
		}
		ri.setIsOk(OrderItemConstant.OS_YES);
		return ri;
	}

	// 检查退单是否可以进行正常退单操作
	public static ReturnInfo<String> checkConditionOfExecution(ReturnCommonVO returnCommon,String returnSn, String actionType) {
		ReturnInfo<String> ri = new ReturnInfo<String>(OrderItemConstant.OS_NO);
		/* 参数检验 */
		if (returnCommon == null) {
			ri.setMessage("退单" + returnSn + "不存在!");
			return ri;
		}
		if (returnCommon.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY) {
			ri.setMessage("退单" + returnSn + "已处于已取消状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		if (returnCommon.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE) {
			ri.setMessage("退单" + returnSn + "已处于完成状态，不能在进行" + actionType + "操作！");
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

	public Integer getSettle() {
		return settle;
	}

	public void setSettle(Integer settle) {
		this.settle = settle;
	}

	public Integer getCommunicate() {
		return communicate;
	}

	public void setCommunicate(Integer communicate) {
		this.communicate = communicate;
	}

    public Integer getDownRefund() {
        return downRefund;
    }

    public void setDownRefund(Integer downRefund) {
        this.downRefund = downRefund;
    }
}
