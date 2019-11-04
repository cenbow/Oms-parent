package com.work.shop.oms.order.service;

import java.util.List;

import com.work.shop.oms.bean.MasterOrderInfoBean;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;

/**
 * 支付单操作服务
 * @author lemon
 *
 */
public interface MasterOrderPayService {

	/**
	 * 创建支付单
	 * @param masterOrderSn 订单编码
	 * @param masterPays 支付单列表
	 * @return ServiceReturnInfo<List<String>>
	 */
	ServiceReturnInfo<List<String>> insertMasterOrderPay(String masterOrderSn, List<MasterPay> masterPays);
	
	/**
	 * 编辑订单信息，更新支付单订单
	 * 
	 * @param masterOrderSn
	 * @param master
	 * @param afterChangePayable
	 * @return
	 */
	ReturnInfo<String> editMasterOrderPay(String masterOrderSn, double oldTotalPayable, MasterOrderInfoBean newMaster);

    /**
     * 根据订单号查询主支付单
     * @param masterOrderSn
     * @return
     */
    List<MasterOrderPay> getMasterOrderPayList(String masterOrderSn);

    /**
     * 根据主键更新
     * @param masterOrderPay
     * @return
     */
    int updateByPrimaryKeySelective(MasterOrderPay masterOrderPay);
}
