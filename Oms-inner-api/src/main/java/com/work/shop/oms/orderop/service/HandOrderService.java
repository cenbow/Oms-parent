package com.work.shop.oms.orderop.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.HandOrderBatch;
import com.work.shop.oms.common.bean.HandOrderBatchVo;
import com.work.shop.oms.common.bean.HandOrderInfoVo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.utils.PageHelper;


/**
 * 子订单问题单、正常单操作
 * @author lemon
 *
 */
public interface HandOrderService {

	/**
	 * 手工打单列表(分页)
	 * 
	 * @param model
	 * @param helper
	 * @return Paging
	 */
	Paging getHardOrderPage(HandOrderBatchVo model, PageHelper helper);
	
	/**
	 * 创建手工打单批次
	 * 
	 * @param orderBatch
	 * @param helper
	 * @return ReturnInfo
	 */
	ReturnInfo<String> createHandOrderBatch(HandOrderBatch orderBatch);
	
	/**
	 * 变更处理状态  
	 * 
	 * @param batchNo
	 * @param processStatus 处理状态 0:未审核; 1:已审核; 2:已打单
	 * @param userName
	 * @return ReturnInfo
	 */
	ReturnInfo<String> changeProcessStatus(String batchNo, int processStatus, String userName);
	
	/**
	 * 获取手工打单指定批次清单列表
	 * @param billNo
	 * @return
	 */
	public Paging getThirdPartyOrderDetailList(String billNo,PageHelper helper);
	
	/**
	 * 从excel读取批量导入数据
	 * @param myfile
	 * @param note
	 * @param channelCode
	 * @param channelName
	 * @return
	 */
	public ReturnInfo<List<HandOrderInfoVo>> doImport(MultipartFile myfile,String note,String channelCode,String channelName, Integer sourceType);
	
	/**
	 * 提交导入手工打单数据
	 * @param infoVos
	 * @param actionUser
	 * @return
	 */
	public ReturnInfo<String> submitImport(List<HandOrderInfoVo> infoVos, String actionUser);
	
	
	/**
	 * 执行手工打单
	 * 
	 * @param batchNo
	 * @param userName
	 * @return ReturnInfo
	 */
	ReturnInfo<String> doHandOrder(String batchNo, String userName);
	
	/**
	 * 执行手工打单
	 * 
	 * @param batchNo
	 * @param userName
	 * @return ReturnInfo
	 */
	ReturnInfo<String> doCreateOrder(String orderId);
}
