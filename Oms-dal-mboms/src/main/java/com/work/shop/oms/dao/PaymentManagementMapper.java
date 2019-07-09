package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.common.bean.PaymentManagementVO;

public interface PaymentManagementMapper {
	
	/**
	 * 支付方式管理：查询支付方式列表数量
	 * @param paramMap
	 * @return
	 */
	public int getPaymentQueryListCount(Map<String,Object> paramMap);
	
	/**
	 * 支付方式管理：查询支付方式列表
	 * @param paramMap
	 * @return
	 */
	public List<PaymentManagementVO> getPaymentQueryList(Map<String,Object> paramMap);
	
	/**
	 * 支付方式管理：检查是否存在同名或同code的记录
	 * @param paramMap
	 * @return
	 */
	public int checkSameRecord(Map<String,Object> paramMap);
	
	/**
	 * 支付方式管理：新增支付方式
	 * @param vo
	 * @return
	 */
	public int doAddPayment(PaymentManagementVO vo);
	
	/**
	 * 支付方式管理：切换支付方式状态（启用变禁用，禁用变启用）
	 * @param paramMap
	 * @return
	 */
	public int changeStatus(Map<String,Object> paramMap);
	
	/**
	 * 支付方式管理：保存编辑支付方式信息
	 * @param vo
	 * @return
	 */
	public int doSaveEdit(PaymentManagementVO vo);
	
}
