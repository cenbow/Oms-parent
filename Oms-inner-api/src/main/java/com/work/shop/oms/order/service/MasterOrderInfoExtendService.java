package com.work.shop.oms.order.service;

import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.MasterOrder;

import java.util.Date;
import java.util.List;

/**
 * 主订单扩展表
 * @author lemon
 *
 */
public interface MasterOrderInfoExtendService {

	/**
	 * 订单扩展表创建
	 * @param masterOrderSn 订单编码
	 * @param masterOrder 订单信息
	 */
	void insertOrderInfoExtend(String masterOrderSn, MasterOrder masterOrder);

	/**
	 * 通过订单编码获取订单扩展信息列表
	 * @param masterOrderSn 订单编码
	 * @return List<MasterOrderInfoExtend>
	 */
	List<MasterOrderInfoExtend> getMasterOrderInfoExtendByOrder(String masterOrderSn);

	/**
	 * 通过订单编码获取订单扩展信息
	 * @param masterOrderSn
	 * @return MasterOrderInfoExtend
	 */
	MasterOrderInfoExtend getMasterOrderInfoExtendById(String masterOrderSn);

	/**
	 * 更新订单账期支付扣款状态
	 * @param masterOrderSn
	 * @return boolean
	 */
	boolean updateMasterPayPeriod(String masterOrderSn);

	/**
	 * 更新订单结算账户结算状态
	 * @param masterOrderSn
	 * @return boolean
	 */
	boolean updateMasterSettlementAccount(String masterOrderSn);

	/**
	 * 订单创建完成
	 * @param masterOrderSn
	 */
	void masterOrderFinished(String masterOrderSn);

    /**
     * 账期支付填充最后支付单时间
     * @param masterOrderSn 订单号
     * @param startDate 开始时间
     */
    void fillPayLastDate(String masterOrderSn, Date startDate);

    /**
     * 校验是否为账期支付
     * @param payId 支付id
     * @return
     */
    ApiReturnData<Boolean> checkAccountPeriodPay(int payId);

    /**
     * 更新订单推送供应链状态
     * @param masterOrderSn
     * @return
     */
    boolean updatePushSupplyChain(String masterOrderSn);

    /**
     * 更新内行扣款成功状态
     * @param masterOrderSn
     * @param payPeriodPayStatus 内行扣款成功状态 （0未扣、1扣款成功、2扣款失败）
     * @return
     */
    boolean updatePayPeriodPayStatus(String masterOrderSn, int payPeriodPayStatus);
}
