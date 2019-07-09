package com.work.shop.oms.activemq.listener;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.work.shop.oms.common.bean.WirterMethodDescriptor;
import com.work.shop.oms.utils.StringUtil;

/**
 * 动态JMS队列注册监听工具
 * 
 * @author
 * @date 2016-4-13 下午7:15:34
 */
//@Service
public class DynamicListenerManager implements ApplicationContextAware {

	ApplicationContext							applicationContext;

	private ConnectionFactory					connectionFactory;
	
	@Resource(name = "jmsFactory")
	private PooledConnectionFactory				jmsFactory;
	
	@Resource(name = "putQueueJmsTemplate")
	private JmsTemplate putQueueJmsTemplate;

	Logger logger = Logger.getLogger(DynamicListenerManager.class);

	public static Map<String, DefaultMessageListenerContainer> containers = Maps.newHashMap();

	/**
	 * 注册队列监听
	 * 
	 * @param queueName
	 *			队列名称
	 * @param listeserRef
	 *			监听BEANID
	 * @param concurrency
	 *			eg. 1-4
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer
	 */
	public void registeQueueLintener(String queueName, String listeserRef, String concurrency) {
		MessageListener listener = getBean(listeserRef, MessageListener.class);
		registeQueueLintener(queueName, listener, concurrency);
	}

	/**
	 * 注册队列监听
	 * 
	 * @param queueName
	 *			队列名称
	 * @param listener
	 *			监听器
	 * @param concurrency
	 *			eg. 1-4
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer
	 */
	public void registeQueueLintener(String queueName, MessageListener listener, String concurrency) {
		ActiveMQQueue destination = new ActiveMQQueue(queueName);
		Map<String, Object> param = Maps.newHashMap();
		param.put("destination", destination);
		param.put("connectionFactory", jmsFactory);
		param.put("concurrency", concurrency);
		param.put("messageListener", listener);
		try {
			registeMqLintener(param);
		} catch (IllegalAccessException e) {
			logger.error("registeQueueLintener 注册监听出错:" + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error("registeQueueLintener 注册监听出错:" + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error("registeQueueLintener 注册监听出错:" + e.getMessage(), e);
		} catch (IntrospectionException e) {
			logger.error("registeQueueLintener 注册监听出错:" + e.getMessage(), e);
		}
	}

	/**
	 * 注册监听
	 * 
	 * @param param
	 *			参数
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public void registeMqLintener(Map<String, Object> param) throws IntrospectionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Assert.notEmpty(param, "参数不能为空");
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			WirterMethodDescriptor descriptor = new WirterMethodDescriptor(entry.getKey(), container.getClass());
			descriptor.getWriteMethod().invoke(container, entry.getValue());
		}
		Destination destination = (Destination) param.get("destination");
		String name = getDestinationName(destination);
		if (containers.containsKey(name)) {
			throw new RuntimeException("队列" + name + "已经注册过监听");
		}
		containers.put(name, container);
		container.afterPropertiesSet();
		container.start();
	}
	
	/**
	 * 修改注册监听
	 * 
	 * @param param
	 *			参数
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public void updateRegisteMqLintener(String queueName, String listeserRef, String concurrency) throws IntrospectionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		DefaultMessageListenerContainer container = containers.get(queueName);
		if (container == null) {
			registeQueueLintener(queueName, listeserRef, concurrency);
		} else {
			if (StringUtil.isNotEmpty(listeserRef)) {
				MessageListener messageListener = getBean(listeserRef, MessageListener.class);
				container.setMessageListener(messageListener);
			}
			container.setConcurrency(concurrency);
		}
		containers.put(queueName, container);
		container.afterPropertiesSet();
		container.start();
	}

	/**
	 * 修改注册监听
	 * 
	 * @param param
	 *			参数
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public void updateRegisteMqLintener(String queueName, MessageListener listener, String concurrency) throws IntrospectionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		DefaultMessageListenerContainer container = containers.get(queueName);
		if (container == null) {
			registeQueueLintener(queueName, listener, concurrency);
		} else {
			container.setMessageListener(listener);
			container.setConcurrency(concurrency);
		}
		containers.put(queueName, container);
		container.afterPropertiesSet();
		container.start();
	}
	
	/**
	 * 修改注册监听
	 * 
	 * @param param
	 *			参数
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public void destroyRegisteMqLintener(String queueName) throws IntrospectionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		DefaultMessageListenerContainer container = containers.get(queueName);
		if (container == null) {
			return ;
		}
		containers.remove(queueName);
		container.destroy();
	}
	
	/**
	 * 停止注册监听
	 * 
	 * @param param
	 *			参数
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public void stopRegisteMqLintener(String queueName) throws IntrospectionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		DefaultMessageListenerContainer container = containers.get(queueName);
		if (container == null) {
			return ;
		}
		container.stop();
		containers.put(queueName, container);
	}
	
	/**
	 * 开始注册监听
	 * 
	 * @param param
	 *			参数
	 * @see org.springframework.jms.listener.DefaultMessageListenerContainer
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public void startRegisteMqLintener(String queueName, String listeserRef, String concurrency) throws IntrospectionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		DefaultMessageListenerContainer container = containers.get(queueName);
		if (container == null) {
			registeQueueLintener(queueName, listeserRef, concurrency);
			return ;
		}
		container.start();
		containers.put(queueName, container);
	}
	
	/**
	 * 消息放入队列
	 * 
	 * @param queueName  队列名
	 * @param data 消息内容
	 */
	public void putQueueMessage(String queueName, final String data) {
		try {
			putQueueJmsTemplate.send(queueName, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(data);
				}
			});
			logger.debug(data + ".发送成功");
		} catch (Exception e) {
			logger.error(data, e);
		}
	}
	
	/**
	 * 消息放入队列
	 * 
	 * @param queueName  队列名
	 * @param data 消息内容
	 * @throws JMSException 
	 */
	public void putQueueMessages(String queueName, List messages, long timeToLive, boolean transacted) throws JMSException {
		Session session = null;
		try {
			// 创建没有事务的会话
			session = jmsFactory.createConnection().createSession(transacted, Session.AUTO_ACKNOWLEDGE);
			
			Destination destination = session.createQueue(queueName);
			// 创建消息制作者
			MessageProducer producer = session.createProducer(destination);
			// 设置持久化模式
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			if (timeToLive != 0) {
				producer.setTimeToLive(timeToLive);
			}
			// 批量
			foreachQueueData(messages, session, producer);
			// 提交会话
			session.commit();
			logger.debug(JSON.toJSONString(messages) + ".发送成功");
		} catch (Exception e) {
			logger.error(JSON.toJSONString(messages), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private void foreachQueueData(List messages, Session session, MessageProducer producer) {
		String msg=null;
		try {
			int count = 0;
			for (Object order : messages) {
				count ++;
				msg = JSON.toJSONString(order);
				final String msg1 = msg;
				TextMessage text = session.createTextMessage(msg1);
				producer.send(text);
				logger.debug("foreachQueueData..length:"+CollectionUtils.size(messages)+",count:"+count+",foreach:"+JSON.toJSONString(order));
			}
			logger.debug("foreachQueueData.success.."+ CollectionUtils.size(messages));
		} catch (Exception e) {
			logger.error("批量发送MQ队列异常:"+CollectionUtils.size(messages)+",msg:"+e.getMessage(),e);
		}
	}

	private String getDestinationName(Destination destination) {
		if (destination instanceof ActiveMQQueue) {
			ActiveMQQueue queue = (ActiveMQQueue) destination;
			return queue.getPhysicalName();
		}
		if (destination instanceof ActiveMQTopic) {
			ActiveMQTopic topic = (ActiveMQTopic) destination;
			return topic.getPhysicalName();
		}
		throw new RuntimeException(" destination is not support !" + destination);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public <T> T getBean(String name, Class<T> clazz) {
		return applicationContext.getBean(name, clazz);
	}

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
}
