package com.work.shop.oms.orderop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.work.shop.oms.api.bean.OrderGoodsInfo;
import com.work.shop.oms.bimonitor.service.BIMonitorService;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.DistributeShipBean;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.LackSkuParam;
import com.work.shop.oms.common.bean.OrderInfoUpdateInfo;
import com.work.shop.oms.common.bean.OrderOtherModifyInfo;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.OrderToShippedProviderBeanParam;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.define.DefineOrderMapper;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.orderop.service.OrderDistributeEditService;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.orderop.service.OrderNormalService;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import com.work.shop.oms.ship.request.DistOrderShipRequest;
import com.work.shop.oms.ship.response.DistOrderShipResponse;
import com.work.shop.oms.ship.service.DistributeShipService;

/**
 * 订单公共操作服务
 * @author QuYachu
 */
@Service
public class OrderCommonServiceImpl implements OrderCommonService{

	@Resource(name="orderQuestionService")
	private OrderQuestionService orderQuestionService;

	@Resource(name="orderCancelService")
	private OrderCancelService orderCancelService;

	@Resource
	private DistributeShipService distributeShipService;
	@Resource
	private OrderDistributeEditService orderDistributeEditService;

	@Resource(name="orderConfirmService")
	private OrderConfirmService orderConfirmService;

	@Resource
	private BIMonitorService biMonitorService;
	@Resource
	private DefineOrderMapper defineOrderMapper;

	@Resource(name = "orderDistributeOpService")
	private OrderDistributeOpService orderDistributeOpService;

	/**
	 * 设置主订单问题单
	 * @param orderSn 子订单号
	 * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;switchFlag:库存占用释放开关（true:开;false:关）
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo questionOrderByOrderSn(String orderSn, OrderStatus orderStatus) {
		return orderQuestionService.questionOrderByMasterSn(orderSn, orderStatus);
	}

	@Override
	public ReturnInfo noticeOsFromErpForShort(String orderSn,
			List<LackSkuParam> lackSkuParams, String shortReason) {
		return orderQuestionService.noticeOsFromErpForShort(orderSn, lackSkuParams, shortReason);
	}

	/**
	 * 指定订单编码取消
	 * @param masterOrderSn 主订单号
	 * @param orderStatus code:取消原因code;note:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo cancelOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus) {
		return orderCancelService.cancelOrderByMasterSn(masterOrderSn, orderStatus);
	}

	/**
	 * 指定订单交货单取消
	 * @param orderStatus code:取消原因code;message:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo cancelOrderByOrderSn(OrderStatus orderStatus) {
		return orderCancelService.cancelOrderByOrderSn(orderStatus);
	}

	/**
	 * 交货单是否可以发货确认
	 * @param request 发货请求对象
	 * @return DistOrderShipResponse
	 */
	@Override
	public DistOrderShipResponse shippedConfirm(DistOrderShipRequest request) {
		return distributeShipService.shippedConfirm(request);
	}

	/**
	 * 设置缺货问题单信息
	 * @param orderSn 配送单号
	 * @param lackSkuParams 缺货商品列表
	 * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;supplierOrderSn:供应商工单ID
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo addLackSkuQuestion(String orderSn, List<LackSkuParam> lackSkuParams, OrderStatus orderStatus) {
		return orderQuestionService.addLackSkuQuestion(orderSn, lackSkuParams, orderStatus);
	}

	@Override
	public ReturnInfo acceptShip(
			List<OrderToShippedProviderBeanParam> providerBeanParams) {
		return distributeShipService.acceptShip(providerBeanParams);
	}

	/**
	 * 编辑收货人信息
	 * @param consignInfo
	 * @return
	 */
	@Override
	public ReturnInfo editConsigneeInfoByOrderSn(ConsigneeModifyInfo consignInfo) {
		return orderDistributeEditService.editConsigneeInfoByOrderSn(consignInfo);
	}

	/**
	 * 主订单编辑订单地址
	 *
	 * @param masterOrderSn 主订单号
	 * @param consignInfo 客户信息
	 * @return
	 */
	@Override
	public ReturnInfo editConsigneeInfoByMasterSn(String masterOrderSn, ConsigneeModifyInfo consignInfo) {
		return orderDistributeEditService.editConsigneeInfoByMasterSn(masterOrderSn, consignInfo);
	}

	/**
	 * 编辑承运商
	 * @param modifyInfo 更新信息
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo editShippingType(ConsigneeModifyInfo modifyInfo) {
		return orderDistributeEditService.editShippingType(modifyInfo);
	}

	@Override
	public ReturnInfo posConfirmOrder(ConsigneeModifyInfo modifyInfo) {
		return orderConfirmService.posConfirmOrder(modifyInfo);
	}

	@Override
	public ReturnInfo editShippingFee(String masterOrderSn, String actionUser,
			Double shippingFee) {
		return orderDistributeEditService.editShippingFee(masterOrderSn, actionUser, shippingFee);
	}

	@Override
	public ReturnInfo editOrderOther(String masterOrderSn, String actionUser,
			OrderOtherModifyInfo otherModify) {
		return orderDistributeEditService.editOrderOther(masterOrderSn, actionUser, otherModify);
	}

	@Override
	public ReturnInfo editGoodsByMasterSn(String masterOrderSn,
			OrderInfoUpdateInfo infoUpdateInfo, String actionUser) {
		return orderDistributeEditService.editGoodsByMasterSn(masterOrderSn, infoUpdateInfo, actionUser);
	}

	@Override
	public ReturnInfo editGoodsByOrderSn(String orderSn,
			OrderInfoUpdateInfo infoUpdateInfo, String actionUser) {
		return orderDistributeEditService.editGoodsByOrderSn(orderSn, infoUpdateInfo, actionUser);
	}

	@Override
	public void sendMonitorMessage(String queueName, String data) {
		biMonitorService.sendMonitorMessage(queueName, data);
	}

	@Override
	public void reShipped(String orderSn) {
		distributeShipService.reShipped(orderSn);
	}

	@Override
	public ReturnInfo processShip(String orderSn,
			List<DistributeShipBean> shipProviderList, boolean isSystem) {
		return distributeShipService.processShip(orderSn, shipProviderList, isSystem);
	}

	@Override
	public ReturnInfo getOrderGoodsByOrderSnAndCustomCode(String orderSn,
			String customCode) {
		ReturnInfo returnInfo=new ReturnInfo();
		returnInfo.setIsOk(0);
		List<OrderGoodsInfo> list=new ArrayList<OrderGoodsInfo>();
		try {
			if(StringUtils.isBlank(orderSn)||StringUtils.isBlank(customCode)){
				returnInfo.setMessage("orderSn或customCode不能为空！");
			}else{
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("orderSn",orderSn);
				params.put("customCode",customCode);
				list=defineOrderMapper.selectByWhere(params);
			}
			returnInfo.setIsOk(1);
			returnInfo.setMessage("查询成功！");
			returnInfo.setData(list);
		} catch (Exception e) {
			returnInfo.setMessage("根据订单号和11位码查询商品发生异常："+e);
		}
		return returnInfo;
	}

	/**
	 * 系统自动确认收货
	 * @param masterOrderSn 订单编码
	 * @param actionUser 操作人
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo confirmReceipt(String masterOrderSn, String actionUser) {
		return distributeShipService.confirmReceipt(masterOrderSn, actionUser);
	}

	/**
	 * 订单自提核销
	 * @param distributeShipBean 订单配送信息
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> cacWriteOff(DistributeShippingBean distributeShipBean) {
		return distributeShipService.cacWriteOff(distributeShipBean);
	}

	/**
	 * 处理系统仓库发货
	 * @param distributeShipBean 交货单发货信息
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> processShip(DistributeShippingBean distributeShipBean) {
		return distributeShipService.processShip(distributeShipBean, true);
	}

	/**
	 * 订单锁定
	 * @param orderStatus 订单状态信息
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> lockOrder(OrderStatus orderStatus) {
		return orderDistributeOpService.lockOrder(orderStatus.getMasterOrderSn(), orderStatus);
	}

	/**
	 * 订单解锁
	 * @param orderStatus 订单状态信息
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> unLockOrder(OrderStatus orderStatus) {
		return orderDistributeOpService.unLockOrder(orderStatus.getMasterOrderSn(), orderStatus);
	}

	/**
	 * 分配单发货确认
	 * @param distributeShipBean 分配单发货信息
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> distOrderConfirm(DistributeShippingBean distributeShipBean) {
		return distributeShipService.distOrderConfirm(distributeShipBean);
	}
	
	/**
	 * 收货确认(按照快递单号签收，快递单号不填写时按照订单签收)
	 * @param bean
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> confirmationOfReceipt(DistributeShippingBean bean) {
		return distributeShipService.confirmationOfReceipt(bean);
	}
}