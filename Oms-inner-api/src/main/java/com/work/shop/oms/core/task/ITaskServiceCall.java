package com.work.shop.oms.core.task;

import java.util.Map;

/**
 * 任务处理
 * @author QuYachu
 */
public interface ITaskServiceCall {

	/**
	 * 任务
	 * @param para 任务参数
	 */
	void processTask(Map<String,String> para);
}
