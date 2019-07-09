package com.work.shop.oms.order.service;

import java.util.List;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.OcpbStatus;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.common.bean.SystemInfo;
import com.work.shop.oms.common.bean.ValidateOrder;

/**
 * 订单参数校验&异常处理
 * @author lemon
 *
 */
public interface OrderValidateService {

	
	/**
	 * 验证传入的参数是否正确
	 * @param masterOrder 订单信息
	 * @return ServiceReturnInfo<MasterOrder>
	 */
	ServiceReturnInfo<MasterOrder> validate(MasterOrder masterOrder);
	
	/**
	 * 验证传入的参数是否正确
	 * @param masterOrder
	 * @return
	 */
	ServiceReturnInfo<List<MasterOrder>> validateOrders(List<MasterOrder> masterOrder);

	/**
	 * 反序列化订单
	 * @param orderInfoStr
	 * @return
	 */
	ServiceReturnInfo<MasterOrder> orderFormat(String orderInfoStr);
	
	/**
	 * 反序列化订单
	 * @param orderInfoStr
	 * @return
	 */
	ServiceReturnInfo<List<MasterOrder>> orderListFormat(String orderInfoStr);
	
	/**
	 * 订单生成后验证订单(平台币扣减，积分扣减，问题单设置)
	 * @param masterOrderSn
	 * @param validateOrder
	 * @param ocpbStatus
	 * @param qt
	 * @return
	 */
	ReturnInfo validateOrderInfo(String masterOrderSn, ValidateOrder validateOrder, OcpbStatus ocpbStatus, int qt);

	/**
	 * 创建订单异常处理
	 * @param systemInfo
	 * @param returninfo
	 * @return
	 */
	String errorMessage(SystemInfo systemInfo, ServiceReturnInfo<?> returninfo);
	
	/**
	 * 创建订单成功处理
	 * @param systemInfo
	 * @param returninfo
	 * @return
	 */
	String successMessage(SystemInfo systemInfo, ServiceReturnInfo<?> returninfo);

	/**
	 * 将多件一行商品拆分到一件一行
	 * @param masterOrder 订单信息
	 * @return ServiceReturnInfo<MasterOrder>
	 */
	ServiceReturnInfo<MasterOrder> splitGoods(MasterOrder masterOrder);
}
