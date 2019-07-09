package com.work.shop.oms.core.service.impl;

import org.springframework.stereotype.Service;

import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.core.service.ITaskServiceManagerFactory;

@Service("taskManagerFactory")
public class TaskManagerFactory implements ITaskServiceManagerFactory{

	@Override
	public void processTask(ATaskServiceProcess taskService) {
		taskService.execute();
	}

}
