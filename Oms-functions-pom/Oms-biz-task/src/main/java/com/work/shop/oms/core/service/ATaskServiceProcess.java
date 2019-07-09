/*
* 2015-2-28 上午10:50:34
* 吴健 HQ01U8435
*/

package com.work.shop.oms.core.service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import com.work.shop.oms.bean.OrderTaskConfig;
import com.work.shop.oms.bean.OrderTaskConfigExample;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.OrderRunable;
import com.work.shop.oms.dao.OrderTaskConfigMapper;

/**
 * 任务调度通用逻辑封装
 * @author huangl
 *
 */
public abstract class ATaskServiceProcess implements ITaskServiceTemplate {

	private static final Logger logger = LoggerFactory.getLogger(ATaskServiceProcess.class);
	
	private Integer pageNo;
	private Integer pageSize;
	private Integer dataThread;
	
	//调度任务Task
	public String taskName = StringUtils.EMPTY;
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Resource(name = "orderTaskConfigMapper")
	private OrderTaskConfigMapper orderTaskConfigMapper;
	
	/**
	 * 初始化参数配置
	 */
	@Override
	public void initTaskConfig() {
		if (StringUtils.isBlank(this.taskName)) {
			throw new RuntimeException("调度任务TaskName为空");
		} else {
			OrderTaskConfigExample taskParaExample = new OrderTaskConfigExample();
			taskParaExample.or().andShortCodeEqualTo(this.taskName);
			List<OrderTaskConfig> paraList = orderTaskConfigMapper.selectByExample(taskParaExample);
			if (CollectionUtils.isEmpty(paraList)) {
				this.pageNo = 1;
				this.pageSize = 100;
				this.dataThread = 5;
			} else {
				OrderTaskConfig para = paraList.get(0);
				this.pageNo = 1;
				this.pageSize = para.getTradenumperapi();
				this.dataThread = para.getThreadnumbers();
			}
		}
		logger.debug(">>>TaskService.initTaskConfig ,taskName:"+this.taskName+",dataThread:"+dataThread+",pageNo:"+pageNo+",pageSize:"+pageSize);
	}
	
	@Override
	public void executeTaskMore(List<BaseTask> objList) {
		if (CollectionUtils.isNotEmpty(objList)) {
			for (BaseTask baseObj : objList) {
				executeTask(baseObj);
			}
		}
	}
	
	public void execute() {
		//初始化配置参数
		initTaskConfig();
		logger.debug(">>TaskService " + taskName + " execute info begin.." + taskName);
		int countThread = 0;
		int threadNum = 0;
		long currentTime = System.currentTimeMillis();
		try {
			//获取业务数据
			List<BaseTask> queueList = queryServiceData(null, this.pageSize * this.dataThread);
			if (queueList == null) {
				return;
			}
			int size = queueList.size();
			countThread = (size / pageSize) + (size % pageSize > 0 ? 1 : 0);
			threadNum = countThread;
			final CountDownLatch watch = new CountDownLatch(countThread);
			logger.debug(">>>TaskService runable before taskName:"+taskName+",dataThread:"+this.dataThread+",pageSize:"+pageSize+",size:"+size+"pageNo:"+pageNo);
			while(countThread > 0) {
				int first = (pageNo - 1) * pageSize;
				int max = first + pageSize;
				max = Math.min(max, size);
				List<BaseTask> temp = queueList.subList(first, max);
				if (CollectionUtils.isNotEmpty(temp)) {
					logger.debug("TaskService.splitThread.datas.taskName:"+taskName+",execute....countThread:"+countThread+",totalSize:"+size+",from "+first+" to "+(max)+",temp:"+temp.size()+",(pageNo:"+pageNo+",pageSize:"+pageSize+")");
					taskExecutor.execute(new OrderRunable(this, watch).initTaskDatas(temp, ">countThread:" + countThread + ",from " + first + " to " + max + ",totalSize:" + size + ",datas:" + temp.size()));
				}
				pageNo++;
				countThread--;
				logger.debug("TaskService Order runable after ,size:"+size + ",pageSize:"+pageSize+",pageNo:"+pageNo+",((pageNo - 1) * pageSize):"+((pageNo - 1) * pageSize));
			}
			//稍等片刻
			watch.await();
		} catch (Exception e) {
			logger.error("TaskService orderList failed! msg:"+e.getMessage(),e);
		}
		logger.debug(">>TaskService execute "+taskName+" info end,countThread="+threadNum+",cost:"+(System.currentTimeMillis() - currentTime));
	}
}
