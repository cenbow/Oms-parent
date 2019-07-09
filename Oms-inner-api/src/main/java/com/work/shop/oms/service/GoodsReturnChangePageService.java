package com.work.shop.oms.service;

import java.util.List;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.GoodsReturnChange;
import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.common.bean.GoodsReturnChangeBean;
import com.work.shop.oms.common.bean.GoodsReturnChangeInfoVO;
import com.work.shop.oms.common.bean.GoodsReturnChangeVO;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.utils.PageHelper;

public interface GoodsReturnChangePageService {
	
	
	/**
	 * 申请单管理页面(分页)
	 * @param goodsReturnChangeVO
	 * @param helper
	 * @return
	 */
	public Paging getPageList(GoodsReturnChangeVO goodsReturnChangeVO,PageHelper helper);
	
	/**不分页（用于导出）
	 * 
	 */
	public List<GoodsReturnChangeBean> getList(GoodsReturnChangeVO goodsReturnChangeVO);

	/**
	 *创建申请单日志 
	 * @param goodsReturnChangeAction
	 * @return
	 */
//	public ReturnInfo createGoodsReturnAction(GoodsReturnChangeAction goodsReturnChangeAction);
	/**
	 * 根据订单号查找日志
	 * @param orderSn
	 * @return
	 */
//	public List<GoodsReturnChangeAction> findActionByorderSn(String  orderSn);
	/**
	 * 根据申请单号查找日志
	 * @param orderSn
	 * @return
	 */
	public List<GoodsReturnChangeAction> findActionByReturnChangeSn(String  returnChangeSn);
	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public GoodsReturnChange findGoodsReturnChangeById(Integer id);
	/**
	 * 批量修改状态
	 * @param type
	 * @param ids
	 * @param userCode
	 */
	public ReturnInfo updateStatusBatch(Integer type, Integer[] ids, String userCode);
	
	/**
	 * 根据订单号查找申请单
	 * @param orderSn
	 * @return
	 */
	public List<GoodsReturnChange> findGoodsReturnChangeByOrderSn(String orderSn);
	/**
	 * 根据订单号查找申请单信息
	 * @param orderSn
	 * @return
	 */
	public List<GoodsReturnChangeInfoVO> findGoodsReturnChangeBySn(String orderSn);
	

}
