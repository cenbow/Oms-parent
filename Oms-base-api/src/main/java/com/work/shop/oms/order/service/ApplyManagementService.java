package com.work.shop.oms.order.service;

import com.work.shop.oms.bean.ApplyItem;
import com.work.shop.oms.bean.GoodsReturnChange;
import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.common.bean.GoodsReturnChangeInfoVO;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;

/**
 * 订单管理
 * 
 * @author lemon
 *
 */
public interface ApplyManagementService {

	/**
	 * 根据退换货申请单号, 获取申请日志
	 * @param returnChangeSn 申请单号
	 * @return OmsBaseResponse<GoodsReturnChangeAction>
	 */
	OmsBaseResponse<GoodsReturnChangeAction> findActionByReturnChangeSn(String returnChangeSn);

	/**
	 * 根据申请单id,获取申请单详情
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChange>
	 */
	OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeById(OmsBaseRequest<ApplyItem> request);

	/**
	 * 修改申请单状态
	 * @param request 请求参数
	 * @return OmsBaseResponse<String>
	 */
	OmsBaseResponse<String> updateStatusBatch(OmsBaseRequest<ApplyItem> request);

	/**
	 * 根据订单号，查询退换货申请单
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChange>
	 */
	OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeByOrderSn(OmsBaseRequest<ApplyItem> request);

	/**
	 * 根据订单号，获取退换货申请单详情
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChangeInfoVO>
	 */
	OmsBaseResponse<GoodsReturnChangeInfoVO> findGoodsReturnChangeBySn(OmsBaseRequest<ApplyItem> request);
}