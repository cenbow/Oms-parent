package com.work.shop.oms.core.beans;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.shop.oms.core.service.ATaskServiceProcess;

/**
 * 调度任务-线程封装
 * 
 * @author huangl
 * 
 */
public class OrderRunable implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(OrderRunable.class);

	private CountDownLatch watch;
	
	private ATaskServiceProcess taskServiceProcess;
	
	private List<BaseTask> taskDatas;
	//线程前缀
	private String threadIndex;
	 
	public OrderRunable(ATaskServiceProcess taskServiceProcess, CountDownLatch watch) {
		super();
		this.watch = watch;
		this.taskServiceProcess = taskServiceProcess;
	}
	
	public OrderRunable initTaskDatas(List<BaseTask> taskDatas, String threadIndex) {
		this.taskDatas = taskDatas;
		this.threadIndex = threadIndex;
		return this;
	}

	@Override
	public void run() {
		try {
			if (CollectionUtils.isNotEmpty(taskDatas)) {
				taskServiceProcess.executeTaskMore(taskDatas);
				logger.debug("taskServiceThread--->."+ Thread.currentThread().getId()+ ",threadIndex:"+threadIndex);
				nullAllForTask();
			}
		} catch(Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		} finally {
			watch.countDown();
		}
	}
	
	public void nullAllForTask() {
		this.taskDatas = null;
		this.taskServiceProcess = null;
		this.threadIndex = StringUtils.EMPTY;
	}
}
