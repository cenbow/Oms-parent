package com.work.shop.oms.dao.define;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.oms.api.bean.OrderContractBean;
import com.work.shop.oms.api.bean.OrderContractRequest;
import com.work.shop.oms.api.param.bean.OrderInfoSearchExample;
import com.work.shop.oms.api.param.bean.OrderItemQueryExample;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.Common;
import com.work.shop.oms.common.bean.OrderGoodsSaleBean;
import com.work.shop.oms.common.bean.OrderQuestionSearchResultVO;
import com.work.shop.oms.common.bean.OrderQuestionSearchVO;

import java.util.List;
import java.util.Map;

public interface OrderInfoSearchMapper {

	@ReadOnly
	List<Common> selectCommonByExample(OrderInfoSearchExample example);

	@ReadOnly
	int countCommonByExample(OrderInfoSearchExample example);
	
	@ReadOnly
	List<Common> selectCommonByOrderSn(OrderInfoSearchExample example);

	@ReadOnly
	int countCommonByOrdersn(OrderInfoSearchExample example);
	
	/***
	 * 获取问题单列表
	 ***/
	@ReadOnly
	List<OrderQuestionSearchResultVO> getOrderQuestionVOBySearch(OrderQuestionSearchVO orderQuestionSearchVO);
	
	/***
	 * 获取为题单列表数量
	 ***/
	@ReadOnly
	int getOrderQuestionVOBySearchCounts(OrderQuestionSearchVO orderQuestionSearchVO);

	
	/**
	 *获取物流问题单列表 
	 ***/
	@ReadOnly
	List<ShortageQuestion> getShortageQuestionList(OrderQuestionSearchVO searchVO);

	/***
	 * 订单结算列表 
	 ***/
	@ReadOnly
	List<OrderBillListVo> getOrderBillList(OrderBillList orderSettleBill);
	
	/***
	 * 订单结算列表数量 
	 ***/
	@ReadOnly
	int countOrderBillList(OrderBillList orderSettleBill);

	
	/***
	 * 保证金列表 
	 ***/
	@ReadOnly
	List<OrderBillListVo> getOrderDepositList(OrderBillList orderSettleBill);
	
	
	/***
	 * 保证金列表数量 
	 ***/
	@ReadOnly
	int countOrderDepositList(OrderBillList orderSettleBill);
	
	
	/***
	 * 订单退单调整日志列表 
	 ***/
	@ReadOnly
	List<OrderBillListVo> getOrderInfoOrOrderReturnLogListList(OrderBillList orderSettleBill);
	
	
	/***
	 * 订单退单调整日志数量 
	 ***/
	@ReadOnly
	int countOrderInfoOrOrderReturnLogListList(OrderBillList orderSettleBill);
	
	
	/***
	 * 获取子问题单列表
	 ***/
	@ReadOnly
	List<OrderQuestionSearchResultVO> getOrderChildQuestionVOBySearch(OrderQuestionSearchVO orderQuestionSearchVO);
	
	/***
	 * 获取子问题单列表数量
	 ***/
	@ReadOnly
	int getOrderChildQuestionVOBySearchCounts(OrderQuestionSearchVO orderQuestionSearchVO);
	
	
	
	/***
	 * 导出交货问题单列表
	 ***/
	@ReadOnly
	List<OrderQuestionSearchResultVO> getOrderChildQuestionVOByexport(OrderQuestionSearchVO orderQuestionSearchVO);
	
	/***
	 * 导出交货问题单列表数量
	 ***/
	@ReadOnly
	int countOrderChildQuestionVOByexport(OrderQuestionSearchVO orderQuestionSearchVO);
	
	@ReadOnly
	List<Common> selectCommonGoodsByExample(OrderInfoSearchExample example);

	@ReadOnly
	int countCommonGoodsByExample(OrderInfoSearchExample example);
	
	@ReadOnly
	List<OrderItem> selectOrderItemByExample(OrderItemQueryExample example);

	@ReadOnly
	List<OrderItem> selectOrderInfo(OrderItemQueryExample example);

	@ReadOnly
	int countOrderItemByExample(OrderItemQueryExample example);
	
	/**
	 * 获取自提订单总数
	 * @param param
	 * @return
	 */
	int selectPickUpOrderCount(Map<String, Object> param);
	
	/**
	 * 获取自提订单列表
	 * @param param
	 * @return
	 */
	List<OrderItem> selectPickUpOrderList(Map<String, Object> param);
	
	/**
	 * 获取商品销售记录总数
	 * @param param
	 * @return
	 */
	Integer selectOrderGoodsSaleCount(Map<String, Object> param);
	
	/**
	 * 获取商品销售记录
	 * @param param
	 * @return
	 */
	List<OrderGoodsSaleBean> selectOrderGoodsSaleList(Map<String, Object> param);
	
	/**
	 * 获取订单商品记录列表
	 * @param example
	 * @return
	 */
	List<OrderGoodsItem> selectOrderGoodsByExample(OrderItemQueryExample example);
	
	/**
	 * 获取订单商品记录总数
	 * @param example
	 * @return
	 */
	int countOrderGoodsByExample(OrderItemQueryExample example);

	/**
	 * 获取订单到期账期支付信息列表
	 * @param query
	 * @return List<OrderAccountPeriod>
	 */
	List<OrderAccountPeriod> selectCompanyPayPeriodList(Map<String, Object> query);

	/**
	 * 获取订单发货单到期账期支付信息列表
	 * @param query 查询条件
	 * @return List<OrderAccountPeriod>
	 */
	List<OrderAccountPeriod> selectCompanyPayPeriodListByOrderDepotShip(Map<String, Object> query);

    /**
     * 订单合同列表
     * @param request
     * @return
     */
    List<OrderContractBean> selectOrderContractList(OrderContractRequest request);

    /**
     * 订单合同总数
     * @param request
     * @return
     */
    int countOrderContractList(OrderContractRequest request);

    /**
     * 获取公司订单导出数据列表
     * @param paramMap
     * @return
     */
    List<OrderQueryExportResult> getOrderQueryExportList(Map<String, Object> paramMap);

    /**
     * 获取公司订单导出数据列表总数
     * @param paramMap
     * @return
     */
    int countOrderQueryExportList(Map<String, Object> paramMap);

}
