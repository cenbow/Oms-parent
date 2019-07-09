package com.work.shop.oms.distribute.service;

import java.util.List;

import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.common.bean.OrderDepotResult;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 订单分发供应商服务
 * @author lemon
 *
 */
public interface DistributeSupplierService {

	
	/**
	 * 执行订单分发给供应商系统
	 * 
	 * @param distributes
	 * @param isRePush 是否重新分发标识;true：是;false：否
	 */
	public OrderDepotResult executeDistribute(OrderDistribute distribute, boolean isRePush) throws Exception;
	
	/**
	 * 执行主订单分发给供应商系统
	 * 
	 * @param masterOrderSn
	 * @param isRePush 是否重新分发标识;true：是;false：否
	 */
	public OrderDepotResult executeMaster(String masterOrderSn, boolean isRePush) throws Exception;
	
	/**
	 * 根据供应商不同批量分发
	 * @param distributes
	 * @param isRePush
	 * @return
	 * @throws Exception
	 */
	public OrderDepotResult executeDistributes(String orderSn, boolean isRePush) throws Exception;
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public List<MasterOrderGoods> groupByOrderGoods(List<MasterOrderGoods> list) throws Exception;

	/**
	 * 执行订单分发给供应商系统
	 * 
	 * @param orderSn
	 * @return
	 */
	public OrderDepotResult executeDistributeByMq(String orderSn) throws Exception;
	
	/**
	 * 执行订单分发给供应商系统
	 * 
	 * @param masterOrderSn
	 * @return
	 */
	public OrderDepotResult executeMasterByMq(String masterOrderSn);
	
	/**
	 * 订单分发给供应商系统
	 * 
	 * @param orderSn
	 * @param isRePush 是否重新分发标识;true：是;false：否
	 */
	public OrderDepotResult distribute(String orderSn) throws Exception;
	
	/**
	 * 判断是否符合分发供应商条件 1.订单确认状态 2.付款状态 3.货到付款先不考虑 4.要考虑全流通切换的问题 5.修改承运商
	 * 
	 * @param distribute 发货单信息
	 * @return ReturnInfo 返回结果0：执行不成功；1：成功
	 */
	public ReturnInfo isSatisfyDistribute(OrderDistribute distribute, MasterOrderInfo master);
}
