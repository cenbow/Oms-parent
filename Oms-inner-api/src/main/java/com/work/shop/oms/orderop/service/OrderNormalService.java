package com.work.shop.oms.orderop.service;

import java.util.List;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;


/**
 * 订单返回正常单操作
 * @author lemon
 *
 */
public interface OrderNormalService {
	
	
	/**
	 * 订单返回正常单
	 * 
	 * @param masterOrderInfo
	 * @param distribute
	 * @param orderStatus adminUser:操作人;message:备注;type:返回类型0:一般问题单1:缺货问题单;2:全部转正常单;switchFlag:库存占用释放开关（true:开;false:关）
	 * @return
	 */
//	public ReturnInfo normalOrder(MasterOrderInfo masterOrderInfo, OrderDistribute distribute, OrderStatus orderStatus);

	/**
	 * 订单返回正常单
	 * @param masterOrderSn 主订单号
	 * @param orderStatus adminUser:操作人;message:备注;type:返回类型0:一般问题单1:缺货问题单;2:全部转正常单;switchFlag:库存占用释放开关（true:开;false:关）
	 * @return ReturnInfo<String>
	 */
	ReturnInfo<String> normalOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus);
	
	/**
	 * 子订单返回正常单
	 * 
	 * @param orderSn 子订单号
	 * @param orderStatus adminUser:操作人;message:备注;type:返回类型0:一般问题单1:缺货问题单;2:全部转正常单;switchFlag:库存占用释放开关（true:开;false:关）
	 * @return
	 */
	public  ReturnInfo normalOrderByOrderSn(String orderSn, OrderStatus orderStatus);

	/**
	 * 订单返回正常单
	 * 
	 * @param orderSn
	 * @param orderStatus adminUser:操作者;message:备注;switchFlag:是否占用库存 (true:是;false:否);type:操作类型
	 * @return
	 */
	/*public ReturnInfo returnNormal(String orderSn, OrderStatus orderStatus);*/

	/**
	 * 根据订单号删除
	 * @param orderSn
	 * @return
	 */
	public ReturnInfo deleteOrderQuestion(String orderSn);

	/**
	 * 根据订单号删除问题单
	 * @param masterOrderSn 订单编码
	 * @return ReturnInfo
	 */
	ReturnInfo deleteMasterOrderQuestion(String masterOrderSn);
	
	/**
	 * 预售结束缺货尾单立即转单分配
	 * 
	 * @param shopCode 店铺编码
	 * @param customCode 商品编码
	 * @return
	 */
	public ReturnInfo advanceSaleClose(String shopCode, List<String> customCodes);
}
