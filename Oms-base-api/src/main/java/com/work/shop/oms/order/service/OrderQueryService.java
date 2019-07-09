package com.work.shop.oms.order.service;

import java.util.Map;

import com.work.shop.oms.bean.OrderItem;
import com.work.shop.oms.bean.OrderRiderDistributeLog;
import com.work.shop.oms.channel.bean.OfflineStoreInfo;
import com.work.shop.oms.common.bean.GoodsReturnChangeBean;
import com.work.shop.oms.common.bean.GoodsReturnChangeVO;
import com.work.shop.oms.common.bean.OrderGoodsQuery;
import com.work.shop.oms.common.bean.OrderGoodsSaleBean;
import com.work.shop.oms.order.request.CustomDefineQueryRequest;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.order.response.CustomDefineQueryResponse;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.OrderQueryResponse;
import com.work.shop.oms.vo.OrderReturnListVO;
import com.work.shop.oms.vo.OrderShipVO;

/**
 * 订单、退单查询管理
 * 
 * @author lemon
 *
 */
public interface OrderQueryService {
	
	/**
	 * 订单列表查询
	 * @param request 查询参数
	 * @return OrderQueryResponse
	 */
	OrderQueryResponse orderQuery(OrderQueryRequest request);
	
	/**
	 * 退单列表查询
	 * @param request 查询参数
	 * @return OmsBaseResponse<OrderReturnListVO>
	 */
	OmsBaseResponse<OrderReturnListVO> orderReturnQuery(OmsBaseRequest<OrderReturnListVO> request);

	/**
	 * 退单申请列表查询
	 * @param request 查询参数
	 * @return OmsBaseResponse<GoodsReturnChangeBean>
	 */
	OmsBaseResponse<GoodsReturnChangeBean> orderReturnApplyQuery(OmsBaseRequest<GoodsReturnChangeVO> request);

	/**
	 * 退单申请单列表(新)
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChangeBean>
	 */
    OmsBaseResponse<GoodsReturnChangeBean> orderReturnApplyQueryNew(OmsBaseRequest<GoodsReturnChangeVO> request);

	/**
	 * 自定义编码列表
	 * @param request 查询参数
	 * @return CustomDefineQueryResponse
	 */
	CustomDefineQueryResponse customDefineQuery(CustomDefineQueryRequest request);

	/**
	 * 自提订单列表查询
	 * @param request 查询参数
	 * @return OrderQueryResponse
	 */
	OrderQueryResponse pickUpOrderQuery(OrderQueryRequest request);

	/**
	 * 线下店铺列表
	 * @param request 查询参数
	 * @return OmsBaseResponse<OfflineStoreInfo>
	 */
	OmsBaseResponse<OfflineStoreInfo> offlineStoreManagement(OmsBaseRequest<OfflineStoreInfo> request);
	
	/**
	 * 配送订单列表查询
	 * @param request 查询参数
	 * @return OmsBaseResponse<OrderRiderDistributeLog>
	 */
	OmsBaseResponse<OrderRiderDistributeLog> riderOrderQuery(OrderQueryRequest request);

	/**
	 * 获取骑手配送详情
	 * @param request 查询参数
	 * @return OmsBaseResponse<OrderRiderDistributeLog>
	 */
	OmsBaseResponse<OrderRiderDistributeLog> riderOrderDetail(OrderQueryRequest request);

	/**
	 * 通知骑手配送取消
	 * @param request 请求参数
	 * @return OmsBaseResponse<Boolean>
	 */
	OmsBaseResponse<Boolean> riderOrderCancel(OrderQueryRequest request);

	/**
	 * 通知订单下发到骑手平台
	 * @param request 请求参数
	 * @return OmsBaseResponse<Boolean>
	 */
	OmsBaseResponse<Boolean> riderOrderSend(OrderQueryRequest request);

	/**
	 * 订单待结算列表查询
	 * @param request 请求参数
	 * @return OmsBaseResponse<OrderItem>
	 */
	OmsBaseResponse<OrderItem> waitSettleOrderQuery(OmsBaseRequest<OrderItem> request);

	/**
	 * 退单待结算列表
	 * @param request 请求参数
	 * @return OmsBaseResponse<OrderReturnListVO>
	 */
	OmsBaseResponse<OrderReturnListVO> waitSettleReturnQuery(OmsBaseRequest<OrderReturnListVO> request);

	/**
	 * 查询商品销售记录列表
	 * @param request 请求参数
	 * @return OmsBaseResponse<OrderGoodsSaleBean>
	 */
	OmsBaseResponse<OrderGoodsSaleBean> orderGoodsSaleQuery(OmsBaseRequest<OrderGoodsQuery> request);

	/**
	 * 查询订单仓库配送列表
	 * @param request 请求参数
	 * @return OrderQueryResponse
	 */
	OrderQueryResponse orderDepotShipQuery(OrderQueryRequest request);

	/**
	 * 查询订单仓库配送单详情
	 * @param request 请求参数
	 * @return OmsBaseResponse<OrderShipVO>
	 */
	OmsBaseResponse<OrderShipVO> queryOrderDepotShipDetail(OrderQueryRequest request);

	/**
	 * 请求处理仓库发送配送
	 * @param param 请求参数
	 */
	void getOrderDistributeOut(Map<String, Object> param);
}
