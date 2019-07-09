package com.work.shop.oms.service;

import java.util.List;
import java.util.Set;



import com.work.shop.oms.api.bean.OrderReturnAction;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.ChannelShop;
import com.work.shop.oms.bean.OrderPeriodDetail;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.vo.OrderRefundListVO;
import com.work.shop.oms.vo.OrderReturnListVO;
import com.work.shop.oms.vo.ReturnOrderVO;
/**
 * OMSManager仅仅用与查询,其他操作请调用OMS
 * @author zhaiyongding
 *
 */
public interface OrderReturnInfoService {
	
	/**
	 * 退单列表(分页)
	 * 
	 * @param model
	 * @param helper
	 * @return Paging
	 */
	Paging getOrderReturnPage(OrderReturnListVO model, PageHelper helper) throws Exception;
	
	/**
	 * 退单结算列表(分页)
	 * 
	 * @param model
	 * @param helper
	 * @return Paging
	 */
	Paging getOrderRefundPage(OrderRefundListVO model, PageHelper helper) throws Exception;
	
	/**
	 * 数据导出不分页
	 * @param orderReturnSerach
	 * @return
	 */
	//Paging getOrderInfoPage(OrderReturnListVO model);

	/**
	 * 取周期值
	 * @param periodSeries
	 * @param periodId
	 * @return
	 */
	//OrderPeriodDetail getOrderPeriodDetail(Integer periodSeries, String periodId);

	/**
	 * 加载退货仓库信息
	 * @param orderSn
	 * @return
	 */
//	public List<ChannelShop> getOrderReturnGoodsDepotList(String orderSn);
	
	/**
	 * 退单列表(分页) 导出csv
	 * 
	 * @param model
	 * @param helper
	 * @return Paging
	 */
//	Paging getOrderReturnPageAtExportCsv(OrderReturnListVO model, PageHelper helper) throws Exception;
	
	/**
	 * 查询退单日志
	 * 
	 *  @param returnSn
	 **/
	List<OrderReturnAction> getOrderReturnActionList(String returnSn);
	
	/**
	 * 得到退单信息
	 * @param returnSn
	 * @return
	 */
	//OrderReturn getOrderReturnByReturnSn(String returnSn);
	
	
	/**
	 * 退单详情页面加载
	 * @param returnSn
	 * @param relOrderSn
	 * @param returnType
	 * @return
	 */
//	ReturnOrderVO getOrderReturnDetailVO(String returnSn,String relOrderSn,String returnType);
	
	/**
	 *查询订单状态不是 orderstauts   2.  取消 ，shipstauts  3.已完成,换货单 号不为空。
	 ****/
	//Set<String> selectOrderInfoOfSettlementByPayStatus(Byte orderStatus,Byte shipStaus);
}
