package com.work.shop.oms.orderReturn.service;

import java.util.List;

import com.work.shop.oms.api.bean.ReturnInfoPage;
import com.work.shop.oms.api.param.bean.*;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnMoneyBean;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.bean.SettleOrderInfo;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.CreateReturnVO;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 退单服务
 * @author QuYachu
 */
public interface OrderReturnService {

    /**
     * 根据订单信息生成退单信息，返回退单单号
     * 供手工生成退单调用
     * @param param
     * @return ReturnInfo<String>
     * 发货单、付款单数据不用传递有接口自动计算填充，只用关注退单和商品列表即可
     */
    ReturnInfo<String> createOrderReturn(CreateOrderReturnBean param);
    
    /**
     * 手工更新退单信息
     * @param orderReturnBean
     * @return
     */
    public ReturnInfo<String> updateOrderReturn(CreateOrderReturnBean orderReturnBean);
    
    /**
     * 手工更新退单费用信息
     * @param orderReturnBean
     * @return
     */
    public ReturnInfo<String> updateOrderReturnFee(CreateOrderReturnBean orderReturnBean);
 
    /**
     * 退款单生成 - 提供外部 
     * @param createReturnVO
     * @param groupId 团购id
     * @return ReturnInfo<String>
     * 1.订单删除商品时调用
     * 4.returnSource  ConstantValues.ORDERRETURN_REFUND_SOURCE  无货退款单 来源类型（ 删除商品,订单发货，订单取消）
     */
    ReturnInfo<String> createOrderReturnPay(CreateReturnVO createReturnVO, Integer groupId);
    
    /**
     * 退货相关-短信发送
     * @param mobile
     * @param message
     * @param actionUser
     * @param returnSn
     */
    public void sendReturnMessage(String mobile,String message,String actionUser,String returnSn);
    
    /**
     * 验证订单所关联的退单是否已结算，判定原订单是否可结算
     * @param orderSn 订单号
     * @param settleMoney 应结算金额
     * @return
     */
    public ReturnInfo<String> checkOrderCanSettle(String orderSn,Double settleMoney);
//    public ReturnInfo checkOrderCanSettle(MasterOrderInfo orderInfo,Double settleMoney);
    
    /**
     * 更新退单商品列表
     * @param param
     * @return
     */
    public ReturnInfo<String> updateReturnGoods(CreateOrderReturnBean param);
    
    /**
     * 赠送消费积分(暂时提供给订单结算和换货单结算)
     * @param uid - 用户id
     * @param points - 积分
     */
    public ReturnInfo<String> giveUserPoints(String uid,Integer points,String orderSn);
    
    /**
     * 扣减积分（积分使用）
     * @param uid - 用户id
     * @param points - 积分
     * @param dealCode
     * @return
     */
    public ReturnInfo<String> deductionsPoints(String uid, Integer points,String dealCode);
    
    public ReturnInfo<String> plusAndMinusPoints(SettleOrderInfo settleOrderInfo);
    
    /**
     * 第三方平台退单查询接口
     * @param params
     * @return ApiReturnData
     */
    ApiReturnData<Paging<ReturnInfoPage>> getReturnInfoPageList(ReturnSearchParams params);
    
    
    /**
     * 第三方平台通过供应商查询对应的退单及退单状态数据
     * @param params
     * @return ApiReturnData
     */
    ApiReturnData<List<SellerBean>> getOrderReturnForSeller(SellerParam params);
    
    /**
     * 第三方平台 退单商品分供应商入库
     * @param returnStorageParam
     * @return
     */
    ApiReturnData returnStorageBySeller(ReturnStorageParam returnStorageParam);
    
    /**
     * 更新订单对应所有退单的快递单号和快递公司编码
     * @param returnShipUpdateInfo
     * @return
     */
    public ApiReturnData updateReturnInvoiceNo(ReturnShipUpdateInfo returnShipUpdateInfo );

    /**
     * 获取RF扫描任务(通过快递单号获取WMS入库所需要的信息)
     * @param returnInvoiceNo
     * @return
     */
    public ApiReturnData getRFDataByReturnInvoiceNo(String returnInvoiceNo);
    
    /**
     * RF扫描任务提交(WMS入库)
     * @param wmsData
     * @return
     */
    public ApiReturnData returnStorageFromWMS(WmsData wmsData);
    
    /**
     * 订单物流写入
     * @param orderSn
     * @return
     */
    public ReturnInfo orderExpress(String orderSn);
    
    /**
     * 退单物流写入
     * @param orderReturn
     * @param orderReturnShip
     * @return
     */
    public ReturnInfo orderReturnExpress(OrderReturn orderReturn, OrderReturnShip orderReturnShip);
    
    
    /**
     * 退单完结通知
     * @param orderStatus
     * @return
     */
    public ReturnInfo<String> orderReturnFinish(OrderStatus orderStatus);
    
    
    /**
     * 获取有效退单列表
     * @param masterOrderSn
     * @return
     */
    ReturnInfo<List<OrderReturn>> getEffectiveReturns(String masterOrderSn);

    /**
     * 获取订单关联退单状态信息
     * @param masterOrderSn
     * @return
     */
    ReturnInfo<List<OrderReturnMoneyBean>> getOrderReturnMoneyInfo(String masterOrderSn);
}
