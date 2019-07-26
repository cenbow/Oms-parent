package com.work.shop.oms.order.service;

import com.work.shop.oms.bean.OrderReturnBean;
import com.work.shop.oms.order.request.ReturnManagementRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.ReturnManagementResponse;
import com.work.shop.oms.vo.ReturnGoodsVO;

/**
 * 订单管理
 * 
 * @author lemon
 *
 */
public interface ReturnManagementService {

	/**
	 * 退单详情
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemGet(ReturnManagementRequest request);

	/**
	 * 退单创建初始化
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemCreateInit(ReturnManagementRequest request);

	/**
	 * 退单沟通
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemCommunicate(ReturnManagementRequest request);

	/**
	 * 查询退单日志
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse getReturnItemLog(ReturnManagementRequest request);

	/**
	 * 退单创建
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemCreate(ReturnManagementRequest request);

	/**
	 * 退单锁定
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemLock(ReturnManagementRequest request);

	/**
	 * 退单解锁
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemUnLock(ReturnManagementRequest request);

	/**
	 * 退单确认
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemConfirm(ReturnManagementRequest request);

	/**
	 * 退单作废
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemCancel(ReturnManagementRequest request);

	/**
	 * 退单完成
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemCompleted(ReturnManagementRequest request);

	/**
	 * 退单入库
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemStorage(ReturnManagementRequest request);

	/**
	 * 退单结算
	 * @param request 请求参数
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnItemSettlement(ReturnManagementRequest request);

	/**
	 * 退单待入库明细
	 * @param request 请求参数
	 * @return OmsBaseResponse<ReturnGoodsVO>
	 */
	OmsBaseResponse<ReturnGoodsVO> returnWaitStorageItem(ReturnManagementRequest request);

	/**
	 * 退单退款完成
	 * @param request 请求参数
	 * 退单退款完成  退单号、实际退款金额、操作人
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnRefundCompleted(ReturnManagementRequest request);

	/**
	 * 退单对账单已生成
	 * @param request
	 * @return ReturnManagementResponse
	 */
	ReturnManagementResponse returnOrderBillCompleted(ReturnManagementRequest request);

    /**
     * 手动退款
     * @param request
     * @return
     */
    public ReturnManagementResponse manualRefund(ReturnManagementRequest request);

    /**
     * 订单退款操作
     * @param orderReturnBean
     */
    void doOrderReturnMoneyByCommon(OrderReturnBean orderReturnBean);
}
