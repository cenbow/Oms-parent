package com.work.shop.oms.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.PaymentManagementVO;
import com.work.shop.oms.service.PaymentManagementService;
import com.work.shop.oms.utils.PageHelper;


@Controller
@RequestMapping(value = "paymentManagement")
public class PaymentManagementController extends BaseController{
	@Resource
	private PaymentManagementService paymentManagementService;
	
	/**
	 * 支付方式管理：查询支付方式列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	@RequestMapping(value="getPaymentQueryList")
	@ResponseBody
	public Paging getPaymentQueryList(PaymentManagementVO vo,PageHelper helper){
		return paymentManagementService.getPaymentQueryList(vo,helper);
	}
	
	/**
	 * 支付方式管理：新增支付方式
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="doAddPayment")
	@ResponseBody
	public Map doAddPayment(PaymentManagementVO vo){
		return paymentManagementService.doAddPayment(vo);
	}
	
	/**
	 * 支付方式管理：切换支付方式状态（启用变禁用，禁用变启用）
	 * @param payId
	 * @param enabled
	 * @return
	 */
	@RequestMapping(value="changeStatus")
	@ResponseBody
	public Map changeStatus(String payId,String enabled){
		return paymentManagementService.changeStatus(payId,enabled);
	}
	
	/**
	 * 支付方式管理：保存编辑支付方式信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="doSaveEdit")
	@ResponseBody
	public Map doSaveEdit(PaymentManagementVO vo){
		return paymentManagementService.doSaveEdit(vo);
	}

}
