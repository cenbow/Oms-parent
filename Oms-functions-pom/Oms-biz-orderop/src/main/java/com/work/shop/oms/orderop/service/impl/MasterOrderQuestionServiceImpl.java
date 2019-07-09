package com.work.shop.oms.orderop.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderQuestion;
import com.work.shop.oms.bean.MasterOrderQuestionExample;
import com.work.shop.oms.bean.MasterOrderQuestionKey;
import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderQuestionMapper;
import com.work.shop.oms.dao.OrderCustomDefineMapper;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.orderop.service.MasterOrderQuestionService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

@Service
public class MasterOrderQuestionServiceImpl implements MasterOrderQuestionService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	OrderCustomDefineMapper orderCustomDefineMapper;
	@Resource
	MasterOrderQuestionMapper masterOrderQuestionMapper;
	@Resource(name="masterOrderActionServiceImpl")
	MasterOrderActionService masterOrderActionService;
	
	@Override
	public ReturnInfo addQuestionOrder(OrderStatus orderStatus) {
		ReturnInfo returnInfo = new ReturnInfo(Constant.OS_NO);
		logger.info("设置问题单开始: " + orderStatus.toString());
		String masterOrderSn = orderStatus.getMasterOrderSn();
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderSn == null || masterOrderSn.trim().isEmpty()) {
			ri.setMessage("addQuestionOrderForOM： 传入订单编号参数为空！");
			return ri;
		}
		MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (null == orderInfo) {
			ri.setMessage("订单" + masterOrderSn + "不存在，不能进行设问题单操作！");
			return ri;
		}
		returnInfo = addCommonQuestionOrder(orderInfo, orderStatus);
		logger.info(masterOrderSn + "设置问题单结束");
		return returnInfo;
	}

	@Override
	public ReturnInfo addCommonQuestionOrder(MasterOrderInfo orderInfo,
			OrderStatus orderStatus) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (null == orderInfo) {
			ri.setMessage("订单不存在，不能进行设问题单操作！");
			logger.error("订单不存在，不能进行设问题单操作！");
			return ri;
		}
		if (null == orderStatus) {
			ri.setMessage("传参为空，不能进行设问题单操作！");
			logger.error("传参为空，不能进行设问题单操作！");
			return ri;
		}
		String masterOrderSn = orderInfo.getMasterOrderSn();
		logger.debug("设为问题单:masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus.toString());
		if (StringUtil.isEmpty(orderStatus.getCode())) {
			logger.error(masterOrderSn + "传入设为问题单原因为空！");
			ri.setMessage("传入设为问题单原因为空！");
			return ri;
		}

		OrderCustomDefine define = orderCustomDefineMapper.selectByPrimaryKey(orderStatus.getCode());
		if (define == null) {
			logger.error(masterOrderSn + "不能从商家自定义信息配置表中没有取得code为：" + orderStatus.getCode() + "的详细信息！");
			ri.setMessage("不能从商家自定义信息配置表中没有取得code为：" + orderStatus.getCode() + "的详细信息！");
			return ri;
		}
		// 查询是否已经是同一种问题单
		MasterOrderQuestionExample questionExample = new MasterOrderQuestionExample();
		questionExample.or().andMasterOrderSnEqualTo(masterOrderSn).andQuestionCodeEqualTo(orderStatus.getCode());
		List<MasterOrderQuestion> orderQuestions = masterOrderQuestionMapper.selectByExample(questionExample);
		if (StringUtil.isListNotNull(orderQuestions)) {
//			saveAction(orderInfo, define, orderStatus.getMessage(), orderStatus.getAdminUser());
			ri.setMessage("订单" + masterOrderSn + "已是问题单，不需要重新设置问题单！");
			logger.error("订单" + masterOrderSn + "已是问题单，不需要重新设置问题单！");
			ri.setIsOk(Constant.OS_YES);
			return ri;
		}
		Map<String, Object> monitorMap = new HashMap<String, Object>();
		monitorMap.put("orderId", masterOrderSn);
		try {
			orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
			Date now = new Date();
			orderInfo.setQuestionTime(now);
			orderInfo.setUpdateTime(now);
			// 更新订单状态
			masterOrderInfoMapper.updateByPrimaryKey(orderInfo);
			// 问题单表记录只设置一次
			MasterOrderQuestion masterOrderQuestion = new MasterOrderQuestion();
			masterOrderQuestion.setAddTime(now);
			masterOrderQuestion.setMasterOrderSn(masterOrderSn);
			masterOrderQuestion.setQuestionCode(define.getCode());
			masterOrderQuestion.setQuestionDesc(define.getName());
//			masterOrderQuestion.setQuestionType(0);
			masterOrderQuestionMapper.insertSelective(masterOrderQuestion);
			StringBuffer msg = new StringBuffer(orderStatus.getMessage());
			msg.append("<br/>&nbsp;&nbsp;&nbsp;<font color=\"red\">问题单code：" + define.getCode()
					+ ";问题单原因：" + define.getName() + "</font>");
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(orderInfo);
			orderAction.setActionNote(msg.toString());
			orderAction.setActionUser(orderStatus.getAdminUser());
			masterOrderActionService.insertOrderActionByObj(orderAction);
			ri.setMessage("设为问题单成功！");
			ri.setIsOk(Constant.OS_YES);
			logger.debug(masterOrderSn + "设为问题单成功！");
			monitorMap.put("problemReason", define.getName());
			// 业务监控
//			businessMonitorService.sendMonitorMessage(Constant.BUSINESS_MONITOR_ORDER_PROBLEM, JSON.toJSONString(monitorMap));
		} catch (Exception e) {
			logger.error("设为问题单：执行时异常 ", e);
			ri.setMessage("设为问题单：执行时异常 " + (e.getMessage() == null ? "" : e.getMessage()));
			// 记录操作日志异常信息
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(orderInfo);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote("<font style=color:red;>" + ri.getMessage() + "</font>");
			masterOrderActionService.insertOrderActionByObj(orderAction);
			// 业务监控异常监控
			monitorMap.put("orderException", ri.getMessage());
//			businessMonitorService.sendMonitorMessage(Constant.BUSINESS_MONITOR_ORDER_EXCEPTION, JSON.toJSONString(monitorMap));
		}
		return ri;
	}

	@Override
	public ReturnInfo returnNormal(OrderStatus orderStatus) {
		ReturnInfo returnInfo = new ReturnInfo(Constant.OS_NO);
		logger.info("返回正常单开始: " + orderStatus.toString());
		String masterOrderSn = orderStatus.getMasterOrderSn();
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (masterOrderSn == null || masterOrderSn.trim().isEmpty()) {
			ri.setMessage("addQuestionOrderForOM： 传入订单编号参数为空！");
			return ri;
		}
		MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (null == orderInfo) {
			ri.setMessage("订单" + masterOrderSn + "不存在，不能进行返回正常单操作！");
			return ri;
		}
		returnInfo = addCommonQuestionOrder(orderInfo, orderStatus);
		logger.info(masterOrderSn + "返回正常单结束");
		return returnInfo;
	}

	@Override
	public ReturnInfo returnNormalCommon(MasterOrderInfo orderInfo,
			OrderStatus orderStatus) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (null == orderInfo) {
			ri.setMessage("订单不存在，不能进行返回正常单操作！");
			logger.error("订单不存在，不能进行返回正常单操作！");
			return ri;
		}
		if (null == orderStatus) {
			ri.setMessage("传参为空，不能进行返回正常单操作！");
			logger.error("传参为空，不能进行返回正常单操作！");
			return ri;
		}
		String masterOrderSn = orderInfo.getMasterOrderSn();
		logger.debug("设为问题单:masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus.toString());
		if (StringUtil.isEmpty(orderStatus.getType())) {
			logger.error(masterOrderSn + "传入操作类型为空，不能进行返回正常单操作！");
			ri.setMessage("传入操作类型为空，不能进行返回正常单操作！");
			return ri;
		}
		if (orderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
			logger.error("订单" + masterOrderSn + "已处于正常单状态，不能进行设为正常单操作!");
			ri.setMessage("订单" + masterOrderSn + "已处于正常单状态，不能进行设为正常单操作!");
			return ri;
		}
		List<MasterOrderQuestion> orderQuestions = null;						// 问题单列表
		// 问题单判断
		MasterOrderQuestionExample osExample = new MasterOrderQuestionExample();
		osExample.or().andMasterOrderSnEqualTo(masterOrderSn);
		orderQuestions = masterOrderQuestionMapper.selectByExample(osExample);
		Map<String, Object> monitorMap = new HashMap<String, Object>();
		monitorMap.put("orderId", masterOrderSn);
		try {
			for (MasterOrderQuestion question : orderQuestions) {
				MasterOrderQuestionKey questionKey = new MasterOrderQuestionKey();
				questionKey.setMasterOrderSn(masterOrderSn);
				questionKey.setQuestionCode(question.getQuestionCode());
				masterOrderQuestionMapper.deleteByPrimaryKey(questionKey);
			}
			orderInfo.setQuestionStatus(Constant.OI_QUESTION_STATUS_NORMAL);
			orderInfo.setQuestionTime(null);
			orderInfo.setUpdateTime(new Date());
			// 更新订单状态
			masterOrderInfoMapper.updateByPrimaryKey(orderInfo);
			// 订单操作日志记录
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(orderInfo);
			orderAction.setActionNote("返回正常单：" +orderStatus.getMessage());
			orderAction.setActionUser(orderStatus.getAdminUser());
			masterOrderActionService.insertOrderActionByObj(orderAction);
			ri.setIsOk(Constant.OS_YES);
			ri.setMessage("返回正常单操作成功!");
			logger.debug("返回正常单操作成功!");
			//统一监控记录接口
//			businessMonitorService.sendMonitorMessage(Constant.BUSINESS_MONITOR_ORDER_NORMAL, JSON.toJSONString(monitorMap));
		} catch (Exception e) {
			logger.error("返回正常单异常：", e);
			String errorMsg = e.getMessage() == null ? "" : e.getMessage();
			ri.setMessage("返回正常单异常：" + errorMsg);
			// 记录操作日志异常信息
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(orderInfo);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote("<font style=color:red;>" + ri.getMessage() + "</font>");
			masterOrderActionService.insertOrderActionByObj(orderAction);
			// 业务监控异常监控
			monitorMap.put("orderException", errorMsg);
//			businessMonitorService.sendMonitorMessage(Constant.BUSINESS_MONITOR_ORDER_EXCEPTION, JSON.toJSONString(monitorMap));
		}
		return ri;
	}
}
