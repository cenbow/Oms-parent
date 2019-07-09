package com.work.shop.oms.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.ShippingManagementVO;
import com.work.shop.oms.service.ShippingManagementService;
import com.work.shop.oms.utils.PageHelper;


@Controller
@RequestMapping(value = "shippingManagement")
public class ShippingManagementController extends BaseController {
	@Resource
	private ShippingManagementService shippingManagementService;
	
	/**
	 * 承运商管理：获取承运商查询列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	@RequestMapping(value="getShippingQueryList")
	@ResponseBody
	public Paging getShippingQueryList(ShippingManagementVO vo,PageHelper helper){
		return shippingManagementService.getShippingQueryList(vo,helper);
	}
	
	/**
	 * 承运商管理：新增承运商
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="doAddShipping")
	@ResponseBody
	public Map doAddShipping(ShippingManagementVO vo){
		return shippingManagementService.doAddShipping(vo);
	}
	
	/**
	 * 承运商管理：切换承运商状态（启用变禁用，禁用变启用）
	 * @param shippingId
	 * @param enabled
	 * @return
	 */
	@RequestMapping(value="changeStatus")
	@ResponseBody
	public Map changeStatus(String shippingId,String enabled){
		return shippingManagementService.changeStatus(shippingId,enabled);
	}
	
	/**
	 * 承运商管理：保存编辑承运商信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="doSaveEdit")
	@ResponseBody
	public Map doSaveEdit(ShippingManagementVO vo){
		return shippingManagementService.doSaveEdit(vo);
	}

}
