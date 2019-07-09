package com.work.shop.oms.core.service;

import java.util.List;

import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ReturnTask;

/**
 * 服务通用接口
 * @author huangl
 *
 */
public interface ITaskServiceTemplate {
	
	/**
	 * 初始化数据参数
	 */
	public void initTaskConfig();

	/**
	 * 获取待处理的服务数据
	 * @return
	 */
	public List<BaseTask> queryServiceData(List<String> orderIdList,Integer dataLimit);
	
	/**
	 * 执行业务操作
	 * @return
	 */
	public ReturnTask executeTask(BaseTask obj);
	
	/**
	 * 多条业务执行
	 * @param objList
	 */
	public void executeTaskMore(List<BaseTask> objList);
	
	
	
}
