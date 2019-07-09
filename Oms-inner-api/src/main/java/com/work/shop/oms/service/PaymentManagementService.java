package com.work.shop.oms.service;

import java.util.Map;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.PaymentManagementVO;
import com.work.shop.oms.utils.PageHelper;

public interface PaymentManagementService {
	
	/**
	 * 支付方式管理：查询支付方式列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	public Paging getPaymentQueryList(PaymentManagementVO vo,PageHelper helper);
	
	/**
	 * 支付方式管理：新增支付方式
	 * @param vo
	 * @return
	 */
	public Map doAddPayment(PaymentManagementVO vo);
	
	/**
	 * 支付方式管理：切换支付方式状态（启用变禁用，禁用变启用）
	 * @param payId
	 * @param enabled
	 * @return
	 */
	public Map changeStatus(String payId,String enabled);
	
	/**
	 * 支付方式管理：保存编辑支付方式信息
	 * @param vo
	 * @return
	 */
	public Map doSaveEdit(PaymentManagementVO vo);

}
