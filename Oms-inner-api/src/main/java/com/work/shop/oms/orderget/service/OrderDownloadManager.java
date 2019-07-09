package com.work.shop.oms.orderget.service;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.bgapidb.OrderDownloadPara;
import com.work.shop.oms.utils.PageHelper;

public interface OrderDownloadManager {
	
	public Paging getOrderDownloadParaList(OrderDownloadPara orderDownloadPara,PageHelper helper);
	
	
	/**
	 * 增加orderdownloadpara列表数据 
	 */
	
	public int addOrderDownloadParaItem(OrderDownloadPara orderDownloadPara);
	
	
	/**
	 * 修改orderdownloadpara列表数据
	 */
	
	public int updateDownloadParaItem(OrderDownloadPara orderDownloadPara);
	
	/**
	 * 删除orderdownloadpara列表数据
	 */
	
	public int deleteDownloadParaItem(OrderDownloadPara orderDownloadPara);
	
	/**
	 * 单条数据信息展示
	 */
	
	public OrderDownloadPara getDownloadParaItemById(OrderDownloadPara orderDownloadPara);
}
