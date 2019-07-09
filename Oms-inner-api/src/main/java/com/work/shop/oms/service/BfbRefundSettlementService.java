package com.work.shop.oms.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;




import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.OrderBillVO;
import com.work.shop.oms.utils.PageHelper;

public interface BfbRefundSettlementService {
	/**
	 * 邦付宝退款结算：邦付宝退款批次列表查询
	 * @param vo
	 * @param helper
	 * @return
	 */
	public Paging getBfbRefundSettlementList(OrderBillVO vo,PageHelper helper);
	
	/**
	 * 邦付宝退款结算：废弃选中的结算批次
	 * @param request
	 * @param ids
	 * @return
	 */
	public Map delSettlement(String ids,HttpServletRequest request);
	
	/**
	 * 邦付宝退款结算：下载指定调整单号的结算清单
	 * @param billNo
	 * @param response
	 * @param request
	 * @return
	 */
	public Map downloadRecord(HttpServletRequest request,HttpServletResponse response,String billNo);
	
	/**
	 * 邦付宝退款结算：获取指定调整单号的结算清单列表
	 * @param billNo
	 * @param helper
	 * @return
	 */
	public Paging getBfbRefSetDetailList(String billNo,PageHelper helper);
	
	/**
	 * 邦付宝退款结算：生成调整单
	 * @param request
	 * @param filepath
	 * @param note
	 * @return
	 */
	public Map doImport(HttpServletRequest request,MultipartFile myfile,String note);
	
	/**
	 * 邦付宝退款结算：执行选中的调整单
	 * @param request
	 * @param ids
	 * @return
	 */
	public Map doExecute(HttpServletRequest request,String ids);

}
