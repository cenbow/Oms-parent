package com.work.shop.oms.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;




import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.OrderBillVO;
import com.work.shop.oms.utils.PageHelper;

public interface OrderAmountAdjustmentService {
	/**
	 * 订单金额调整：查询调整批次列表
	 * @param vo
	 * @param helper
	 * @return
	 */
	public Paging getOrderAmountAdjustmentList(OrderBillVO vo,PageHelper helper);
	
	/**
	 * 订单金额调整：废除调整单
	 * @param request
	 * @param ids
	 * @return
	 */
	public Map delOrderAmountAdjustment(HttpServletRequest request,String ids);
	
	/**
	 * 订单金额调整：下载指定调整单清单详情
	 * @param request
	 * @param response
	 * @param billNo
	 */
	public void downloadRecord(HttpServletRequest request,HttpServletResponse response,String billNo);
	
	/**
	 * 订单金额调整：查询指定调整单清单列表
	 * @param billNo
	 * @param helper
	 * @return
	 */
	public Paging getOrdAmoAjuDetailList(String billNo,PageHelper helper);
	
	/**
	 * 订单金额调整：生成调整单
	 * @param request
	 * @param myfile
	 * @param note
	 * @return
	 */
	public Map doImport(HttpServletRequest request,MultipartFile myfile,String note);
	
	/**
	 * 订单金额调整：执行调整单
	 * @param request
	 * @param ids
	 * @return
	 */
	public Map doExecute(HttpServletRequest request,String ids);

}
