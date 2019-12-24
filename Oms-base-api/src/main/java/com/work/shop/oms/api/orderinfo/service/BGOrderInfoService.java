package com.work.shop.oms.api.orderinfo.service;

import java.util.HashMap;
import java.util.List;

import com.work.shop.oms.api.bean.*;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.order.response.OmsBaseResponse;

/**
 * 订单信息接口
 * @author QuYachu
 */
public interface BGOrderInfoService {

	/**
	 * 平台前台查询用户订单列表
	 * @param searchParam 查询参数
	 * @return ApiReturnData<Paging<OrderPageInfo>>
	 */
	ApiReturnData<Paging<OrderPageInfo>> orderPageList(PageListParam searchParam);
	
	/**
	 * 平台前台查询用户退单列表
	 * @param searchParam 查询参数
	 * @return ApiReturnData<Paging<OrderReturnPageInfo>>
	 */
	public ApiReturnData<Paging<OrderReturnPageInfo>> orderReturnPageList(PageListParam searchParam);
	
	/**
	 * 查询退单详情
	 * @param orderReturnSn 退单编码
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<OrderReturnDetailInfo>
	 */
	public ApiReturnData<OrderReturnDetailInfo> orderReturnDetail(String orderReturnSn, String userId, String siteCode);
	
	/**
	 * 查询退单详情
	 * @param searchParam 参数
	 * 	orderReturnSn 退单编码
	 * 	userId 用户id
	 * 	siteCode 站点编码
	 * @return ApiReturnData<OrderReturnDetailInfo>
	 */
	public ApiReturnData<OrderReturnDetailInfo> orderReturnDetailNew(PageListParam searchParam);
	
	/**
	 * 查询换单详情
	 * @param orderSn 订单编码
	 * @param isHistory 是否历史
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<OrderExchangesDetailInfo>
	 */
	public ApiReturnData<OrderExchangesDetailInfo> orderExchangesDetail(String orderSn, String isHistory, String userId, String siteCode);
	
	/**
	 * 查询换单详情
	 * @param searchParam
	 * 	orderSn 订单编码
	 * 	isHistory 是否历史
	 * 	userId 用户id
	 * 	siteCode 站点编码
	 * @return ApiReturnData<OrderExchangesDetailInfo>
	 */
	public ApiReturnData<OrderExchangesDetailInfo> orderExchangesDetailNew(PageListParam searchParam);

	/**
	 * 平台前台查询用户订单详情
	 * @param orderSn 订单编码
	 * @param isHistory 是否历史
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<OrderDetailInfo>
	 */
	ApiReturnData<OrderDetailInfo> orderInfoDetail(String orderSn, String isHistory, String userId, String siteCode);
	
	/**
	 * 平台前台查询用户订单详情
	 * @param searchParam 参数
	 *  orderSn 订单编码
	 *  isHistory 是否历史
	 *  userId 用户id
	 *  siteCode 站点编码
	 * @return ApiReturnData<OrderDetailInfo>
	 */
	ApiReturnData<OrderDetailInfo> orderInfoDetailNew(PageListParam searchParam);

	/**
	 * 获取订单商品消息（app付款校验使用,平台pc统计商品用）
	 * @param orderSns 订单列表
	 * @param siteCode 站点编码
	 * @return ApiReturnData<List<OrderShipInfo>>
	 */
	public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByOrderSn(List<String> orderSns, String siteCode);
	
	/**
	 * 获取订单商品消息（app付款校验使用,平台pc统计商品用）
	 * @param searchParam 查询参数
	 * 	orderSns 订单列表
	 * 	siteCode 站点编码
	 * @return ApiReturnData<List<OrderShipInfo>>
	 */
	public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByOrderSnNew(PageListParam searchParam);
	
	/**
	 * 获取订单商品消息（app付款校验使用,平台pc统计商品用）
	 * @param paySn 支付单号
	 * @param siteCode 站点编码
	 * @return ApiReturnData<List<OrderShipInfo>>
	 */
	public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByPaySn(String paySn, String siteCode);
	
	/**
	 * 获取订单商品消息（app付款校验使用,平台pc统计商品用）
	 * @param searchParam 参数
	 * 			paySn 支付单号
	 * 			siteCode 站点编码
	 * @return ApiReturnData<List<OrderShipInfo>>
	 */
	public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByPaySnNew(PageListParam searchParam);

	/**
	 * 统计用户订单数量
	 * @param searchParam
	 * @return ApiReturnData<UserOrderTypeNum>
	 */
	public ApiReturnData<UserOrderTypeNum> getUserOrderType(PageListParam searchParam);
	
	/**
	 * 统计用户订单数量
	 * @param searchParam 参数
	 *  userId 用户id
	 *  siteCode 站点编码
	 * @return ApiReturnData<UserOrderTypeNum>
	 */
	public ApiReturnData<UserOrderTypeNum> getUserOrderTypeNew(PageListParam searchParam);
	
	/**
	 * 根据条件查询用户（平台app用户升级调度任务使用）
	 * @param searchParam 参数
	 * @return ApiReturnData<Paging<String>>
	 */
	public ApiReturnData<Paging<String>> getOrderInfoUser(SearchParam searchParam);

	/**
	 * 获取用户订单编码列表
	 * @param activitySearchParam 参数
	 * @return ApiReturnData<Paging<String>>
	 */
	public ApiReturnData<Paging<String>> getOrderSnByUser(ActivitySearchParam activitySearchParam);
	
	/**
	 * 取消订单
	 * @param masterOrderSn 订单编码
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<Boolean>
	 */
	public ApiReturnData<Boolean> cancelOrderByMasterSn(String masterOrderSn, String userId, String siteCode);
	
	/**
	 * 取消订单
	 * @param searchParam 参数
	 *  orderSn 订单编码
	 *  userId 用户id
	 *  siteCode 站点编码
	 * @return ApiReturnData<Boolean>
	 */
	public ApiReturnData<Boolean> cancelOrderByMasterSnNew(PageListParam searchParam);

	/**
	 * 根据支付单号查询主订单号
	 * @param paySn 支付单号
	 * @param siteCode 站点编码
	 * @return ApiReturnData<List<String>>
	 */
	public ApiReturnData<List<String>> getMasterOrderSnByPaySn(String paySn, String siteCode);

	/**
	 * 根据支付单号查询主订单号
	 * @param searchParam 参数
	 * 			paySn
	 * 			siteCode
	 * @return ApiReturnData<List<String>>
	 */
	public ApiReturnData<List<String>> getMasterOrderSnByPaySnNew(PageListParam searchParam);

	/**
	 * 修改订单用户
	 * @param oldUser 用户id
	 * @param newUser 新用户id
	 * @param siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	public ReturnInfo<Boolean> changeOrderUser(String oldUser, String newUser, String siteCode);
	
	/**
	 * 修改订单用户（平台）
	 * @param searchParam 参数
	 *  userId 用户id
	 *  newUserId 新用户id
	 *  siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	public ReturnInfo<Boolean> changeOrderUserNew(PageListParam searchParam);
	
	/**
	 * 订单评论接口（平台）
	 * @param orderSn 订单编码
	 * @param flag true 为已评论
	 * @param siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	public ReturnInfo<Boolean> orderReview(String orderSn, boolean flag, String siteCode);

	/**
	 * 订单评论接口（平台）
	 * @param searchParam 参数
	 *  orderSn 订单编码
	 *  flag 已评论
	 *  siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	public ReturnInfo<Boolean> orderReviewNew(PageListParam searchParam);

	/**
	 * 确认收货接口
	 * @param orderSn 订单编码
	 * @param invoiceNo 运单号
	 * @param actionUser 用户id
	 * @param siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	ReturnInfo<Boolean> confirmReceipt(String orderSn, String invoiceNo, String actionUser, String siteCode);
	
	/**
	 * 确认收货接口
	 * @param searchParam 参数
	 *  orderSn 订单编码
	 *  invoiceNo 运单号
	 *  userId 用户id
	 *  siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	ReturnInfo<Boolean> confirmReceiptNew(PageListParam searchParam);
	
	/**
	 * 获取订单简易信息
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<List<MasterOrderInfo>>
	 */
	public ApiReturnData getOrderSimpleInfoByUser(String userId, String siteCode);

	/**
	 * 根据用户查询订单简单信息
	 * @param searchParam 查询参数
	 * @return ApiReturnData<List<MasterOrderInfo>>
	 */
	public ApiReturnData<List<MasterOrderInfo>> getOrderSimpleInfoByUserNew(PageListParam searchParam);
	
	
	/**
	 * 平台前台查询用户未支付订单
	 * @param searchParam userId siteCode
	 * @return ApiReturnData<List<String>>
	 */
	public ApiReturnData<List<String>> queryNoPayOrder(PageListParam searchParam);
	
	/**
	 * 平台前台查询用户限购订单
	 * @param searchParam 查询条件
	 * @return ApiReturnData<List<OrderGoodsInfo>>
	 */
	public ApiReturnData<List<OrderGoodsInfo>> queryRestrictionOrder(PageListParam searchParam);

	/**
	 * 查询地区信息
	 * @param searchParam 查询参数
	 * @return OmsBaseResponse<SystemRegionArea>
	 */
	public OmsBaseResponse<SystemRegionArea> querySystemRegionArea(SystemRegionArea searchParam);

    /**
     * 通过订单编码取消订单,传取消原因和备注
     * @param searchParam
     * @return ApiReturnData<Boolean>
     */
    public ApiReturnData<Boolean> cancelOrderByMasterSnAndCancel(PageListParam searchParam);

    /**
     * 删除订单
     * @param searchParam
     * @return ApiReturnData<Boolean>
     */
    ApiReturnData<Boolean> deleteMasterOrder(PageListParam searchParam);

    /**
     * 删除退单
     * @param searchParam
     * @return ApiReturnData<Boolean>
     */
    ApiReturnData<Boolean> deleteReturnOrder(PageListParam searchParam);

	/**
	 * 申请已支付
	 * @param searchParam 申请信息
	 * @return ApiReturnData<Boolean>
	 */
	ApiReturnData<Boolean> applyOrderPay(PageListParam searchParam);

    /**
     * 延长收货
     * @param searchParam
     * @return
     */
    ApiReturnData<Boolean> extendedReceipt(PageListParam searchParam);

    /**
     * 发货单延长收货
     * @param searchParam
     * @return
     */
    public ApiReturnData<Boolean> extendedReceiptByInvoiceNo(PageListParam searchParam);

    /**
     * 统计用户退单数量
     * @param searchParam 参数
     *  userId 用户id
     *  siteCode 站点编码
     * @return ApiReturnData<UserOrderTypeNum>
     */
    public ApiReturnData<UserOrderTypeNum> getUserOrderReturnType(PageListParam searchParam);

	//查询订单来源
	HashMap<String,Object> getOrderFromByOrderSN(String orderSN);
}
