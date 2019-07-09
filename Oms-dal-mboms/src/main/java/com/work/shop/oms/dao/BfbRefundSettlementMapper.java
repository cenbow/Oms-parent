package com.work.shop.oms.dao;


import java.util.List;
import java.util.Map;

import com.work.shop.oms.common.bean.OrderBillVO;
import com.work.shop.oms.common.bean.OrderSettleBillVO;

public interface BfbRefundSettlementMapper {
	
	/**
	 * 邦付宝退款结算：邦付宝退款批次列表数量查询
	 * @param paramMap
	 * @return
	 */
	public int getBfbRefundSettlementListCount(Map<String,Object> paramMap);
	
	/**
	 * 邦付宝退款结算：邦付宝退款批次列表查询
	 * @param paramMap
	 * @return
	 */
	public List<OrderBillVO> getBfbRefundSettlementList(Map<String,Object> paramMap);
	
	/**
	 * 邦付宝退款结算：废弃选中的结算批次
	 * @param paramList
	 * @return
	 */
	public int delSettlement(Map<String,Object> paramList);
	
	/**
	 * 邦付宝退款结算：获取指定批次邦付宝退款清单供下载
	 * @param billNo
	 * @return
	 */
	public List<OrderSettleBillVO> getBfbRefundDownloadList(String billNo);
	
	/**
	 * 邦付宝退款结算：获取指定批次的邦付宝退款清单列表
	 * @param paramMap
	 * @return
	 */
	public List<OrderSettleBillVO> getBfbRefSetDetailList(Map<String,Object> paramMap);
	/**
	 * 邦付宝退款结算：获取指定批次的邦付宝退款清单数量
	 * @param billNo
	 * @return
	 */
	public int getBfbRefSetDetailListCount(String billNo);
	
	/**
	 * 邦付宝退款结算：插入邦付宝结算批次记录
	 * @param obVO
	 * @return
	 */
	public int insertOrderBillVO(OrderBillVO obVO);
	
	/**
	 * 邦付宝退款结算：插入邦付宝结算清单记录
	 * @param osbVO
	 * @return
	 */
	public int insertOrderSettleBillVO(OrderSettleBillVO osbVO);
	
	/**
	 * 邦付宝退款结算：查询调整单号列表paramList中存在的已同步或已废除调整单数量
	 * @param paramList
	 * @return
	 */
	public int checkUnfitCount(List<String> paramList);

	/**
	 * 邦付宝退款结算：查询是否存在未同步以外状态的调整单
	 * @param paramList
	 * @return
	 */
	public int checkSynCount(List<String> paramList);
	
	/**
	 * 邦付宝退款结算：更新paramList中的调整单批次同步状态
	 * @param paramList
	 * @return
	 */
	public int updateSynStatus(Map<String,Object> paramMap);
	

}
