package com.work.shop.oms.core.service;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.bean.SystemRegionAreaExample;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.service.SystenRegionSynchronize;

/**
 * 定时同步OS地区基础数据到JMS中
 * 
 * @author tony
 * 
 */
@Service
public class SystenRegionSynchronizeImpl implements SystenRegionSynchronize {

	private Logger logger = LoggerFactory.getLogger(SystenRegionSynchronizeImpl.class);

	//@Resource
	SystemRegionAreaMapper systemRegionAreaMapper;
	//@Resource
	JmsTemplate systemRegionJmsTemplate;

	public static int buffer = 10000;

	@Override
	public void publishRegion() {
		logger.debug("地区同步定时任务启动");
		int size = getUnSynchResgionsCount();
		if (size < 1) {
			logger.debug("本次同步没有发现需要同步的地区信息，任务结束");
			return;
		}
		int page = getPageSize(size);
		logger.debug("需要同步的地区信息" + size + ",系统设置的缓冲长度：" + buffer + "需要进行" + page + "次同步");
		for (int i = 0; i < page; i++) {
			logger.debug("正在进行第" + (i + 1) + "次同步");
			List<SystemRegionArea> item = loadUnSynchRegions(0);
			// 推送topic消息到MQ中
			publishQueue(item);
		}
		logger.debug("同步完成，本次任务结束");
	}

	private void publishQueue(List<SystemRegionArea> item) {
		if(item==null || item.isEmpty()) return ;
		for (int i = 0; i < item.size(); i++) {
			try {
				final SystemRegionArea bean = item.get(i);
				systemRegionJmsTemplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						String str = JSON.toJSONString(bean);
						logger.debug(str);
						return session.createTextMessage(str);
					}
				});
				updateStatus(bean);
			} catch (JmsException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void updateStatus(SystemRegionArea bean) {
		bean.setIsUpdate(0);
		systemRegionAreaMapper.updateByPrimaryKeySelective(bean);
	}

	private List<SystemRegionArea> loadUnSynchRegions(int page) {
		SystemRegionAreaExample example = new SystemRegionAreaExample();
		int offset = page * buffer;
		example.or().andIsUpdateEqualTo(1).limit(offset, buffer);
		List<SystemRegionArea> item = systemRegionAreaMapper.selectByExample(example);
		if (item == null || item.isEmpty())
			return null;
		return item;
	}

	private int getPageSize(int allcount) {
		return allcount / buffer + (allcount % buffer == 0 ? 0 : 1);
	}

	private int getUnSynchResgionsCount() {
		logger.debug("正在计算需要同步的地区数量");
		SystemRegionAreaExample example = getUnSynchRegionsExample();
		return getRegionsCount(example);
	}

	private int getRegionsCount(SystemRegionAreaExample example) {
		int size = 0;
		try {
			size = systemRegionAreaMapper.countByExample(example);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return 0;
		}
		return size;
	}

	private SystemRegionAreaExample getUnSynchRegionsExample() {
		SystemRegionAreaExample example = new SystemRegionAreaExample();
		example.or().andIsUpdateEqualTo(1);
		return example;
	}

}
