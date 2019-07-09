package com.work.shop.oms.orderop.service;

import java.util.List;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.common.bean.LackSkuParam;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;


/**
 * 子订单问题单、正常单操作
 * @author lemon
 *
 */
public interface OrderQuestionService {
	
	/**
	 * 订单问题单
	 * 
	 * @param masterOrderInfo
	 * @param distribute
	 * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;switchFlag:库存占用释放开关（true:开;false:关）
	 * @return
	 */
//	public ReturnInfo questionOrder(MasterOrderInfo masterOrderInfo, OrderDistribute distribute, OrderStatus orderStatus);

	/**
	 * 主订单问题单
	 * @param masterOrderSn 订单编码
	 * @param orderStatus 订单状态
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> questionOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus);
	
	/**
	 * 子订单问题单
	 * 
	 * @param masterOrderSn 子订单号
	 * @param orderSn
	 * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;switchFlag:库存占用释放开关（true:开;false:关）
	 * @return
	 */
	public  ReturnInfo questionOrderByOrderSn(String orderSn, OrderStatus orderStatus);

	/**
	 * 设置缺货问题单（短拣、短配、缺货问题单等）
	 * 
	 * @param orderSn 配送单号
	 * @param lackSkuParams 缺货商品列表
	 * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;supplierOrderSn:供应商工单ID
	 * @return ReturnInfo
	 */
	ReturnInfo addLackSkuQuestion(String orderSn, List<LackSkuParam> lackSkuParams, OrderStatus orderStatus);

	/**
	 * 提供给ERP，第一次分配前通知OS商品缺货，生成缺货问题单
	 * @param orderSn 订单号
	 * @param lackSkuParams 缺货商品列表
	 * @param shortReason 缺货原因
	 * @return
	 */
	ReturnInfo noticeOsFromErpForShort(String orderSn, List<LackSkuParam> lackSkuParams, String shortReason);
}
