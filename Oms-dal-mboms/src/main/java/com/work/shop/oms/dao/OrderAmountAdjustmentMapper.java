package com.work.shop.oms.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.common.bean.OrderBillVO;
import com.work.shop.oms.common.bean.OrderSettleBillVO;

public interface OrderAmountAdjustmentMapper {
	/**
	 * 订单金额调整：查询调整批次列表数量
	 * @param paramMap
	 * @return
	 */
	public int getOrderAmountAdjustmentListCount(Map<String,Object> paramMap);
	
	/**
	 * 订单金额调整：查询调整批次列表
	 * @param paramMap
	 * @return
	 */
	public List<OrderBillVO> getOrderAmountAdjustmentList(Map<String,Object> paramMap);
	
	/**
	 * 订单金额调整：废除调整单
	 * @param paramMap
	 * @return
	 */
	public int delOrderAmountAdjustment(Map<String,Object> paramMap);
	
	/**
	 * 订单金额调整：查询是否存在未同步以外状态的调整单
	 * @param list
	 * @return
	 */
	public int checkSynCount(List<String> list);
	
	/**
	 * 订单金额调整：查询指定调整单清单详情
	 * @param billNo
	 * @return
	 */
	public List<OrderSettleBillVO> getOrderAmountAjustDownloadList(String billNo);
	
	/**
	 * 订单金额调整：查询指定调整单清单列表数量
	 * @param billNo
	 * @return
	 */
	public int getOrdAmoAjuDetailListCount(String billNo);
	
	/**
	 * 订单金额调整：查询指定调整单清单列表
	 * @param paramMap
	 * @return
	 */
	public List<OrderSettleBillVO> getOrdAmoAjuDetailList(Map<String,Object> paramMap);
	
	/**
	 * 订单金额调整：生成调整单
	 * @param obVO
	 * @return
	 */
	public int insertOrderBillVO(OrderBillVO obVO);
	
	/**
	 * 订单金额调整：生成调整单清单
	 * @param osbVO
	 * @return
	 */
	public int insertOrderSettleBillVO(OrderSettleBillVO osbVO);
	
	/**
	 * 订单金额调整：查询 已同步或已废除的调整单数量
	 * @param paramList
	 * @return
	 */
	public int checkUnfitCount(List<String> paramList);
	
	/**
	 * 订单金额调整：更新paramList中的调整单批次同步状态
	 * @param paramMap
	 * @return
	 */
	public int updateSynStatus(Map<String,Object> paramMap);
	
	

}
