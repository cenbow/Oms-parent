package com.work.shop.oms.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;





import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.OrderBillVO;
import com.work.shop.oms.service.OrderAmountAdjustmentService;
import com.work.shop.oms.utils.PageHelper;

@Controller
@RequestMapping(value = "orderAmountAdjustment")
public class OrderAmountAdjustmentController extends BaseController {
	@Resource
	private OrderAmountAdjustmentService orderAmountAdjustmentService;
	
	/**
	 * 订单金额调整：查询调整批次列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	@RequestMapping(value="getOrderAmountAdjustmentList")
	@ResponseBody
	public Paging getOrderAmountAdjustmentList(OrderBillVO vo,PageHelper helper){
		return orderAmountAdjustmentService.getOrderAmountAdjustmentList(vo,helper);
	}
	
	/**
	 * 订单金额调整：废除调整单
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="delOrderAmountAdjustment")
	@ResponseBody
	public Map delOrderAmountAdjustment(HttpServletRequest request,String ids){
		return orderAmountAdjustmentService.delOrderAmountAdjustment(request,ids);
	}
	
	/**
	 * 订单金额调整：下载指定调整单清单详情
	 * @param request
	 * @param response
	 * @param billNo
	 */
	@RequestMapping(value="downloadRecord")
	public void downloadRecord(HttpServletRequest request,HttpServletResponse response,String billNo){
		orderAmountAdjustmentService.downloadRecord(request,response,billNo);
	}
	
	/**
	 * 订单金额调整：查询指定调整单清单列表
	 * @param billNo
	 * @param helper
	 * @return
	 */
	@RequestMapping(value="getOrdAmoAjuDetailList")
	@ResponseBody
	public Paging getOrdAmoAjuDetailList(String billNo,PageHelper helper){
		return orderAmountAdjustmentService.getOrdAmoAjuDetailList(billNo,helper);
	}
	
	/**
	 * 订单金额调整：生成调整单
	 * @param request
	 * @param myfile
	 * @param note
	 * @return
	 */
	@RequestMapping(value="doImport.spmvc", headers = "content-type=multipart/*")
	@ResponseBody
	public Map doImport(HttpServletRequest request,@RequestParam MultipartFile myfile,String note){
		return orderAmountAdjustmentService.doImport(request,myfile,note);
	}
	
	/**
	 * 订单金额调整：执行调整单
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="doExecute")
	@ResponseBody
	public Map doExecute(HttpServletRequest request,String ids){
		return orderAmountAdjustmentService.doExecute(request,ids);
	}

}
