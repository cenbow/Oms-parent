package com.work.shop.oms.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.OrderSku;
import com.work.shop.oms.service.AbnormalSKUMaintainService;
import com.work.shop.oms.utils.PageHelper;


@Controller
@RequestMapping(value = "abnormalSKUMaintain")
public class AbnormalSKUMaintainController extends BaseController {
	@Resource
	private AbnormalSKUMaintainService abnormalSKUMaintainService;
	
	/**
	 * 异常SKU调整：查询待调整列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	@RequestMapping(value="getAbnormalSKUList")
	@ResponseBody
	public Paging getAbnormalSKUList(OrderSku vo,PageHelper helper){
		return abnormalSKUMaintainService.getAbnormalSKUList(vo,helper);
	}
	
	/**
	 * 异常SKU调整：保存调整单
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="doSaveEdit")
	@ResponseBody
	public Map doSaveEdit(OrderSku vo){
		return abnormalSKUMaintainService.doSaveEdit(vo);
	}
	
	/**
	 * 异常SKU调整：根据sku查询颜色和尺寸
	 * @param skuSn
	 * @return
	 */
	@RequestMapping(value="selectColorAndSizeBySKU")
	@ResponseBody
	public Map selectColorAndSizeBySKU(String skuSn){
		return abnormalSKUMaintainService.selectColorAndSizeBySKU(skuSn);
	}
	

}
