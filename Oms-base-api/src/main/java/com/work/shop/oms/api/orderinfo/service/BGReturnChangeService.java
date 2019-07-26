package com.work.shop.oms.api.orderinfo.service;


import com.work.shop.oms.api.bean.*;
import com.work.shop.oms.api.param.bean.CreateGoodsReturnChange;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.api.param.bean.ReturnChangeGoodsBean;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 申请退换服务
 * @author QuYachu
 */
public interface BGReturnChangeService {
	
	/**
	 * 创建订单商品退换货信息
	 * @param actionUser 操作人
	 * @param actionNote 操作备注helper
	 * @param content 申请内容
	 * @param siteCode 站点编码
	 * @return ReturnInfo
	 */
	public ReturnInfo<Boolean> createGoodsReturnChange(String actionUser, String actionNote, String content, String siteCode);

	/**
	 * 取消申请单
	 * @param channelCode 店铺编码
	 * @param returnChangeSn 申请单号
	 * @param siteCode 站点编码
	 * @return GoodsReturnChangeReturnInfo
	 */
	public GoodsReturnChangeReturnInfo cancelGoodsReturnChange(String channelCode, String returnChangeSn, String siteCode);

	/**
	 * 退货回寄快递消息获取接口
	 * @param returnSn 退单编码
	 * @param returnExpress 退货快递
	 * @param returnInvoiceNo 退货单号
	 * @param returnExpressImg 退货图片
	 * @param siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	public ReturnInfo<Boolean> setOrderReturnShipInfo(String returnSn, String returnExpress,
			String returnInvoiceNo, String returnExpressImg, String siteCode);
	
	/**
	 * 前台退换货申请单列表获得数据接口
	 * @param searchParam
	 * @param siteCode
	 * @return ApiReturnData<Paging<GoodsReturnPageInfo>>
	 */
	public ApiReturnData<Paging<GoodsReturnPageInfo>> getBGGoodsReturnPageList(String searchParam, String siteCode);
	
	/**
	 * 平台前台退换货申请单详情获得数据接口
	 * @param orderSn 订单编码
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<GoodsReturnDetailInfo>
	 */
	public ApiReturnData<GoodsReturnDetailInfo> getBGGoodsReturnInfo(String orderSn, String userId, String siteCode);
	
	/**
     * 平台手机端查询申请单详细
     * @param mobileParams 查询条件
     * @return ApiReturnData
     */
    public ApiReturnData<GoodsReturnChangeMobile> getGoodsReturnChange(MobileParams mobileParams);
    
    /**
     * 平台手机端查询退单列表
     * @param mobileParams 查询条件
     * @return ApiReturnData
     */
    public ApiReturnData<Paging<ReturnMobile>> getOrderReturnList(MobileParams mobileParams);
    
    /**
     * 平台手机端查询换单列表
     * @param mobileParams 查询条件
     * @return ApiReturnData
     */
    public ApiReturnData<Paging<OrderMobile>> getExchangeOrderList(MobileParams mobileParams);

    /**
     * 创建订单商品退换货信息
     * @param createGoodsReturnChange 退换信息
     * @return ReturnInfo
     */
    public ReturnInfo<Boolean> createGoodsReturnChangeNew(CreateGoodsReturnChange createGoodsReturnChange);

    /**
     * 平台手机端根据退换申请单号查询明细
     * @param returnChangeGoodsBean 查询条件
     * @return ApiReturnData<ReturnChangeDetailInfo>
     */
    public ApiReturnData<ReturnChangeDetailInfo> getGoodsReturnChangeDetailList(ReturnChangeGoodsBean returnChangeGoodsBean);

    /**
     * 删除申请单
     * @param returnChangeGoodsBean
     * @return
     */
    public ApiReturnData<Boolean> deleteGoodsReturnChange(ReturnChangeGoodsBean returnChangeGoodsBean);

    /**
     * 售后申请单沟通
     * @param returnChangeGoodsBean
     * @return
     */
    public ApiReturnData<Boolean> communicationChange(ReturnChangeGoodsBean returnChangeGoodsBean);

    /**
     * 申请单列表页获取申请单类型数量
     * @param param
     * @return
     */
    public ApiReturnData<UserOrderTypeNum> getChangeOrderTypeNum(PageListParam param);

    /**
     * 申请单审核
     * @param returnChangeGoodsBean
     * @return
     */
    public ApiReturnData<Boolean> examinationPassed(CreateGoodsReturnChange returnChangeGoodsBean);

    /**
     * 申请单驳回
     * @param channelCode 店铺编码
     * @param returnChangeSn 申请单号
     * @param siteCode 站点编码
     * @return GoodsReturnChangeReturnInfo
     */
    public GoodsReturnChangeReturnInfo rejectGoodsReturnChange(String channelCode, String returnChangeSn, String siteCode);
}
