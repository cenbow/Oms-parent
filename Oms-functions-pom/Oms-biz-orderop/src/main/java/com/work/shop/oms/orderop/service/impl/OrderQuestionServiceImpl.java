package com.work.shop.oms.orderop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.DistributeAction;
import com.work.shop.oms.bean.DistributeQuestion;
import com.work.shop.oms.bean.DistributeQuestionExample;
import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.MasterOrderQuestion;
import com.work.shop.oms.bean.MasterOrderQuestionExample;
import com.work.shop.oms.bean.MasterOrderQuestionKey;
import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.bean.OrderQuestionLackSkuNew;
import com.work.shop.oms.bean.OrderQuestionLackSkuNewExample;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.LackSkuParam;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.DistributeQuestionMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderQuestionMapper;
import com.work.shop.oms.dao.OrderCustomDefineMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderQuestionLackSkuNewMapper;
import com.work.shop.oms.erp.service.ErpInterfaceProxy;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 问题单服务
 * @author QuYachu
 */
@Service("orderQuestionService")
public class OrderQuestionServiceImpl implements OrderQuestionService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	OrderDistributeMapper orderDistributeMapper;
	@Resource
	OrderCustomDefineMapper orderCustomDefineMapper;
	@Resource
	MasterOrderQuestionMapper masterOrderQuestionMapper;
	@Resource(name="masterOrderActionServiceImpl")
	MasterOrderActionService masterOrderActionService;
	@Resource
	MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
	@Resource
	DistributeQuestionMapper distributeQuestionMapper;
	@Resource
	DistributeActionService distributeActionService;
	@Resource
	private ErpInterfaceProxy erpInterfaceProxy;
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	@Resource
	private OrderQuestionLackSkuNewMapper orderQuestionLackSkuNewMapper;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

    /**
     * 设置订单问题单
     * @param master 订单信息
     * @param distributes 交货单列表
     * @param orderStatus 订单状态
     * @return ReturnInfo
     */
	public ReturnInfo questionOrder(MasterOrderInfo master, List<OrderDistribute> distributes, OrderStatus orderStatus) {
		logger.debug("问题单：masterOrderInfo=" + master + ";orderStatus=" + orderStatus);
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(master == null && distributes == null) {
			logger.warn("[masterOrderInfo]或[distribute]不能都为空！");
			info.setMessage("[masterOrderInfo]或[distribute]不能都为空！");
			return info;
		}
		String masterOrderSn = master.getMasterOrderSn();
		if (StringUtil.isTrimEmpty(orderStatus.getCode())) {
			logger.error("指定的订单问题单编码为空！");
			info.setMessage("指定的订单问题单编码为空！");
			return info;
		}
		OrderCustomDefine define = orderCustomDefineMapper.selectByPrimaryKey(orderStatus.getCode());
		if(define == null) {
			logger.error("指定的订单问题单编码[" + orderStatus.getCode() + "]不存在");
			info.setMessage("指定的订单问题单编码[" + orderStatus.getCode() + "]不存在");
			return info;
		}
		if (ConstantValues.METHOD_SOURCE_TYPE.POS.equals(orderStatus.getSource())) {
			orderStatus.setMessage("POS设置问题单：" + orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.FRONT.equals(orderStatus.getSource())) {
			orderStatus.setMessage("前台设置问题单：" + orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(orderStatus.getSource())) {
			orderStatus.setMessage("ERP设置问题单：" + orderStatus.getMessage());
		} else {
			orderStatus.setMessage("设置问题单：" + orderStatus.getMessage());
		}
		logger.debug("订单设置问题单：masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus);
		// 有子订单时：子订单确认
		if (StringUtil.isListNotNull(distributes)) {
			info = questionOrderByMbDistribute(getOrderSns(distributes), distributes, define, orderStatus);
			if (Constant.OS_NO == info.getIsOk()) {
				return info;
			}
		}
		info = questionOrderByMaster(master.getMasterOrderSn(), master, define, orderStatus);
		return info;
	}
	
	@SuppressWarnings("rawtypes")
	private ReturnInfo judgeMasterOrderQuestion(String masterOrderSn, int questionType, OrderCustomDefine define, String type) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		try {
			if (Constant.order_type_distribute.equals(type)) {
				MasterOrderQuestionKey key = new MasterOrderQuestionKey();
				key.setMasterOrderSn(masterOrderSn);
				key.setQuestionCode(define.getCode());
				MasterOrderQuestion orderQuestion = masterOrderQuestionMapper.selectByPrimaryKey(key);
				if (orderQuestion == null) {
					MasterOrderQuestion insertQuestion = new MasterOrderQuestion();
					insertQuestion.setAddTime(new Date());
					insertQuestion.setMasterOrderSn(masterOrderSn);
					insertQuestion.setQuestionCode(define.getCode());
					insertQuestion.setQuestionDesc(define.getName());
					insertQuestion.setQuestionType(questionType);
					masterOrderQuestionMapper.insertSelective(insertQuestion);
				} else {
					if (questionType != orderQuestion.getQuestionType() && questionType == 1) {
						MasterOrderQuestion updateQuestion = new MasterOrderQuestion();
						updateQuestion.setMasterOrderSn(masterOrderSn);
						updateQuestion.setQuestionCode(orderQuestion.getQuestionCode());
						updateQuestion.setQuestionType(1);
						masterOrderQuestionMapper.updateByPrimaryKeySelective(updateQuestion);
					}
				}
				MasterOrderInfo updateMaster = new MasterOrderInfo();
				updateMaster.setMasterOrderSn(masterOrderSn);
				updateMaster.setUpdateTime(new Date());
				updateMaster.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
				masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			}
		} catch (Exception e) {
			info.setMessage("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage());
			logger.error("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage(), e);
		}
		return info;
	}

    /**
     * 订单设置问题单(供应商使用)
     * @param orderSns 交货单编码
     * @param distributes 交货单列表
     * @param define 问题信息
     * @param orderStatus 订单状态
     * @return ReturnInfo
     */
	private ReturnInfo questionOrderByMbDistribute(List<String> orderSns, List<OrderDistribute> distributes, OrderCustomDefine define, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(StringUtil.isListNull(distributes)) {
			logger.warn("[" + orderSns + "]订单不存在，无法确认");
			info.setMessage("[" + orderSns + "]订单不存在，无法确认");
			return info;
		}
		logger.debug("订单设置问题单 orderSns=" + orderSns + ";orderStatus=" + orderStatus);
		List<OrderDistribute> questionDistributes = new ArrayList<OrderDistribute>();
		/* 执行前提检查 */
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			ReturnInfo tInfo = checkOrderQuestion(orderSn, distribute, define, orderStatus);
			if (tInfo.getIsOk() == Constant.OS_NO) {
                return tInfo;
            }
			// 查询是否已经是同一种问题单
			DistributeQuestionExample orderQuestionExample = new DistributeQuestionExample();
			orderQuestionExample.or().andOrderSnEqualTo(orderSn).andQuestionTypeEqualTo(0).andQuestionCodeEqualTo(define.getCode());
			List<DistributeQuestion> orderQuestions = distributeQuestionMapper.selectByExample(orderQuestionExample);
			if (StringUtil.isListNotNull(orderQuestions)) {
				saveAction(distribute, orderStatus.getMessage(), orderStatus.getAdminUser());
				info.setMessage("订单" + orderSn + "已是" + define.getName() + "问题单，不需要重新设置问题单！");
				continue;
			}
			questionDistributes.add(distribute);
		}
		if (StringUtil.isListNull(questionDistributes)) {
			info.setIsOk(Constant.OS_YES);
			info.setMessage("问题单已经设置，不需要重复设置");
			return info;
		}
		logger.debug("订单设问题单 orderSns=" + orderSns + ";orderStatus=" + orderStatus);
		for (OrderDistribute distribute : questionDistributes) {
			String orderSn = distribute.getOrderSn();
			// 订单历史问题单状态
			Integer hisQuestionStatus = distribute.getQuestionStatus();
			try {
				saveQuestion(orderSn, distribute, define, null, orderStatus, "设置问题单成功");
				info.setMessage("设为问题单成功！");
				info.setIsOk(Constant.OS_YES);
				logger.debug(orderSn + "设为问题单成功！");
			} catch (Exception e) {
				logger.error("设为问题单：执行时异常 ", e);
				info.setMessage("设为问题单：执行时异常 " + (e.getMessage() == null ? "" : e.getMessage()));
				// 记录操作日志异常信息
				distribute.setQuestionStatus(hisQuestionStatus);
				saveAction(distribute, "<font style=color:red;>" + info.getMessage() + "</font>", orderStatus.getAdminUser());
			}
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单["+orderSns+"]订单设置问题单成功");
		logger.debug("订单["+orderSns+"]订单设置问题单成功");
		return info;
	}

    /**
     * 订单问题单
     * @param masterOrderSn 订单编码
     * @param master 订单信息
     * @param define 问题对象
     * @param orderStatus 订单状态
     * @return ReturnInfo
     */
	private ReturnInfo questionOrderByMaster(String masterOrderSn, MasterOrderInfo master, OrderCustomDefine define, OrderStatus orderStatus) {
		logger.debug("设为问题单:masterOrderSn=" + masterOrderSn + ";master=" + master + ";orderStatus=" + orderStatus);
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (null == master) {
			ri.setMessage("订单[" + masterOrderSn + "]不存在，不能进行设问题单操作！");
			logger.error("订单[" + masterOrderSn + "]不存在，不能进行设问题单操作！");
			return ri;
		}
		if (null == orderStatus) {
			ri.setMessage("参数[orderStatus]为空，不能进行设问题单操作！");
			logger.error("参数[orderStatus]为空，不能进行设问题单操作！");
			return ri;
		}
		if (StringUtil.isEmpty(orderStatus.getCode())) {
			logger.error("订单[" + masterOrderSn + "]传入设为问题单原因为空！");
			ri.setMessage("订单[" + masterOrderSn + "]传入设为问题单原因为空！");
			return ri;
		}
		// 查询是否已经是同一种问题单
		MasterOrderQuestionExample questionExample = new MasterOrderQuestionExample();
		questionExample.or().andMasterOrderSnEqualTo(masterOrderSn)
			.andQuestionCodeEqualTo(orderStatus.getCode())
			.andQuestionTypeEqualTo(0);
		List<MasterOrderQuestion> orderQuestions = masterOrderQuestionMapper.selectByExample(questionExample);
		if (StringUtil.isListNotNull(orderQuestions)) {
			ri.setMessage("订单[" + masterOrderSn + "]已是问题单，不需要重新设置问题单！");
			logger.error("订单[" + masterOrderSn + "]已是问题单，不需要重新设置问题单！");
			ri.setIsOk(Constant.OS_YES);
			return ri;
		}
		try {
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			updateMaster.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
			Date now = new Date();
			updateMaster.setQuestionTime(now);
			updateMaster.setUpdateTime(now);
			updateMaster.setMasterOrderSn(masterOrderSn);
			// 更新订单状态
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
			// 问题单表记录只设置一次
			MasterOrderQuestion masterOrderQuestion = new MasterOrderQuestion();
			masterOrderQuestion.setAddTime(now);
			masterOrderQuestion.setMasterOrderSn(masterOrderSn);
			masterOrderQuestion.setQuestionCode(define.getCode());
			masterOrderQuestion.setQuestionDesc(define.getName());
			masterOrderQuestion.setQuestionType(0);
			masterOrderQuestionMapper.insertSelective(masterOrderQuestion);
			StringBuffer msg = new StringBuffer(orderStatus.getMessage());
			msg.append("<br/>&nbsp;&nbsp;&nbsp;<font color=\"red\">问题单code：" + define.getCode()
					+ ";问题单原因：" + define.getName() + "</font>");
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionNote(msg.toString());
			orderAction.setActionUser(orderStatus.getAdminUser());
			masterOrderActionService.insertOrderActionByObj(orderAction);
			ri.setMessage("设为问题单成功！");
			ri.setIsOk(Constant.OS_YES);
			logger.debug(masterOrderSn + "设为问题单成功！");
		} catch (Exception e) {
			logger.error("订单[" + masterOrderSn + "]设为问题单：执行时异常 ", e);
			ri.setMessage("订单[" + masterOrderSn + "]设为问题单：执行时异常 " + (e.getMessage() == null ? "" : e.getMessage()));
			// 记录操作日志异常信息
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderAction.setActionNote("<font style=color:red;>" + ri.getMessage() + "</font>");
			masterOrderActionService.insertOrderActionByObj(orderAction);
		}
		return ri;
	}

	/**
	 * 设置订单问题单
	 * @param masterOrderSn 订单编码
	 * @param orderStatus 订单状态
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo questionOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus) {
		logger.info("设置问题单开始:orderSn=" + masterOrderSn + ";orderStatus=" + JSON.toJSONString(orderStatus));
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]不能都为空！");
			info.setMessage("[masterOrderSn]不能都为空！");
			return info;
		}
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (null == master) {
			info.setMessage("订单[" + masterOrderSn + "]不存在，不能进行设问题单操作！");
			return info;
		}
		if (master.getOrderStatus().intValue() == Constant.OI_ORDER_STATUS_CANCLED) {
			info.setMessage("订单[" + masterOrderSn + "]已处于已取消状态，不能进行设问题单操作！");
			return info;
		}
		if (master.getOrderStatus().intValue() == Constant.OI_ORDER_STATUS_FINISHED) {
			info.setMessage("订单[" + masterOrderSn + "]已处于完成状态，不能进行设问题单操作！");
			return info;
		}

		List<OrderDistribute> distributes = null;
		if (master.getSplitStatus() != (byte) 0) {
			OrderDistributeExample mbDistributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria mbCriteria = mbDistributeExample.or();
			mbCriteria.andMasterOrderSnEqualTo(master.getMasterOrderSn());
			mbCriteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
			distributes = this.orderDistributeMapper.selectByExample(mbDistributeExample);
			if (StringUtil.isListNotNull(distributes)) {
				ReturnInfo<List<OrderDistribute>> checkInfo = checkDistributeListOrderQuestion(distributes);
				distributes = checkInfo.getData();
			}
		}
		orderStatus.setType(Constant.order_type_master);
		info = questionOrder(master, distributes, orderStatus);
		return info;
	}

	@Override
	public ReturnInfo questionOrderByOrderSn(String orderSn,
			OrderStatus orderStatus) {
		logger.info("设置问题单开始");
		logger.debug("设为问题单:orderSn=" + orderSn + ";orderStatus=" + orderStatus.toString());
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(orderSn)) {
			logger.error("[orderSn]不能都为空！");
			info.setMessage("[orderSn]不能都为空！");
			return info;
		}
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if (null == distribute) {
			info.setMessage("订单[" + orderSn + "]不存在，不能进行设问题单操作！");
			return info;
		}
		MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (master == null) {
			logger.error("订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行设问题单操作！");
			info.setMessage("订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行设问题单操作！");
			return info;
		}
		if (master.getOrderStatus().intValue() == Constant.OI_ORDER_STATUS_CANCLED) {
			info.setMessage("订单[" + distribute.getMasterOrderSn() + "]已处于已取消状态，不能进行设问题单操作！");
			return info;
		}
		if (master.getOrderStatus().intValue() == Constant.OI_ORDER_STATUS_FINISHED) {
			info.setMessage("订单[" + distribute.getMasterOrderSn() + "]已处于完成状态，不能进行设问题单操作！");
			return info;
		}
		List<OrderDistribute> distributes = new ArrayList<OrderDistribute>();
		distributes.add(distribute);
		info = questionOrder(master, distributes, orderStatus);
		return info;
	}

	/**
	 * 设置缺货问题单信息
	 * @param orderSn 配送单号
	 * @param lackSkuParams 缺货商品列表
	 * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;supplierOrderSn:供应商工单ID
	 * @return ReturnInfo
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo addLackSkuQuestion(String orderSn, List<LackSkuParam> lackSkuParams, OrderStatus orderStatus) {
		logger.info("设置为缺货问题单：orderSn=" + orderSn + ";orderStatus=" + orderStatus
				+ ";lackSkuParams=" + JSON.toJSONString(lackSkuParams));
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		// 数据check
		if (StringUtil.isTrimEmpty(orderSn)) {
			logger.error("传入订单编号参数为空！");
			ri.setMessage("传入订单编号参数为空！");
			return ri;
		}
		if (null == orderStatus) {
			ri.setMessage("orderStatus 设置问题单参数为空，不能进行设问题单操作！");
			logger.error("orderStatus 设置问题单参数为空，不能进行设问题单操作！");
			return ri;
		}
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if (null == distribute) {
			logger.error("订单" + orderSn + "不存在，不能进行设缺货问题单操作！");
			ri.setMessage("订单" + orderSn + "不存在，不能进行设缺货问题单操作！");
			return ri;
		}
		if (distribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
			ri.setMessage("订单" + orderSn + "已经取消,不能进行设缺货问题单操作！");
			return ri;
		}
		if (StringUtil.isEmpty(orderStatus.getCode())) {
			logger.error(orderSn + "传入设为缺货问题单原因为空！");
			ri.setMessage("传入设为缺货问题单原因为空！");
			return ri;
		}
		OrderCustomDefine define = orderCustomDefineMapper.selectByPrimaryKey(orderStatus.getCode());
		if (define == null) {
			logger.error(orderSn + "问题单code：" + orderStatus.getCode() + "不存在");
			ri.setMessage("问题单code：" + orderStatus.getCode() + "不存在");
			return ri;
		}

		// 检查订单状态 
		ReturnInfo returnInfo = checkConditionOfExecution(distribute, orderSn, "设为问题单");
		if (returnInfo != null) {
			if (distribute.getOrderStatus().intValue() == Constant.OI_ORDER_STATUS_CANCLED) {
				saveAction(distribute, define, orderStatus);
			}
			logger.error(returnInfo.getMessage());
			return returnInfo;
		}
		// 已发货订单设问题单拦截只记录日志
		if (distribute.getShipStatus() != null && distribute.getShipStatus() > Constant.OI_SHIP_STATUS_PARTSHIPPED) {
			saveAction(distribute, define, orderStatus);
			ri.setMessage("订单" + orderSn + "的货物已发，不能在进行设为问题单操作！");
			logger.error("订单" + orderSn + "的货物已发，不能在进行设为问题单操作！");
			return ri;
		}
		if (!StringUtil.isListNotNull(lackSkuParams)) {
			logger.error(orderSn + "传入缺货问题单信息为空！");
			ri.setMessage("传入缺货问题单信息为空！");
			return ri;
		}
		MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		// 检查缺货商品是否在订单商品表中存在
//		ReturnInfo checkRi = checkShortageOrderGoods(lackSkuParams, distribute, orderStatus);
		ReturnInfo<List<OrderQuestionLackSkuNew>> checkRi = checkShortageGoods(lackSkuParams, distribute, orderStatus, define);
		if (checkRi != null && checkRi.getIsOk() == Constant.OS_NO) {
			logger.error(checkRi.getMessage());
			return checkRi;
		}
		List<OrderQuestionLackSkuNew> list = checkRi.getData();
		if (StringUtil.isListNull(list)) {
			ri.setMessage("");
			return ri;
		}
		StringBuffer msg = new StringBuffer();
		msg.append(define.getName() + "缺货问题单：" + orderStatus.getMessage() + "</br>");
		try {
			// 已有问题单判断
			DistributeQuestionExample orderQuestionExample = new DistributeQuestionExample();
			orderQuestionExample.or().andOrderSnEqualTo(orderSn)
				.andQuestionTypeEqualTo(1).andQuestionCodeEqualTo(define.getCode());
			List<DistributeQuestion> orderQuestions = distributeQuestionMapper.selectByExample(orderQuestionExample);
			// 创建问题单信息
			if (StringUtil.isListNull(orderQuestions)) {
				DistributeQuestion question = createQuestion(distribute, new Date(), define);
				question.setQuestionType(1);
				if (StringUtil.isNotEmpty(orderStatus.getSupplierOrderSn())) {
					question.setSupplierOrderSn(orderStatus.getSupplierOrderSn());
				}
				// 新增问题单列表
				distributeQuestionMapper.insertSelective(question);
			} else {
				// 供应商工单ID
				if (StringUtil.isNotEmpty(orderStatus.getSupplierOrderSn())) {
					DistributeQuestion question = orderQuestions.get(0);
					if (StringUtil.isNotEmpty(question.getSupplierOrderSn())) {
						question.setSupplierOrderSn(question.getSupplierOrderSn() + "," + orderStatus.getSupplierOrderSn());
					} else {
						question.setSupplierOrderSn(orderStatus.getSupplierOrderSn());
					}
					distributeQuestionMapper.updateByPrimaryKeySelective(question);
				}
			}
			for (OrderQuestionLackSkuNew lackSkuParam : list) {
				logger.debug("设置为缺货问题单：questionParam = " + lackSkuParam.toString());
				// 问题单类型已经存在
				// 新增缺货商品
//				msg.append(saveLackSku(lackSkuParam, orderSn, define));
				msg.append(insertLackSku(lackSkuParam, define, orderSn));
			}
			OrderDistribute updateDistribute = new OrderDistribute();
			Date now = new Date();
			updateDistribute.setOrderSn(orderSn);
			updateDistribute.setQuestionTime(now);
			updateDistribute.setUpdateTime(now);
			updateDistribute.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
			if ("995".equals(define.getCode()) || "996".equals(define.getCode()) || "9951".equals(define.getCode()) || "9952".equals(define.getCode())
					|| "9953".equals(define.getCode())) {
				updateDistribute.setOrderStatus((byte) Constant.OI_ORDER_STATUS_UNCONFIRMED);
			}
			// 更新订单状态
			orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
			// 更新主单状态
			if ("995".equals(define.getCode()) || "996".equals(define.getCode()) || "9951".equals(define.getCode()) || "9952".equals(define.getCode())
					 || "9953".equals(define.getCode())) {
				MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
				if (master != null) {
					judgeMasterOrderStatus(distribute.getMasterOrderSn(), master, (byte) Constant.OI_ORDER_STATUS_UNCONFIRMED);
				}
			}
			orderStatus.setMessage(msg.toString());
			saveAction(distribute, define, orderStatus);
			ri.setIsOk(Constant.OS_YES);
			ri.setMessage("生成缺货问题单成功！");
			logger.debug(orderSn + "生成缺货问题单成功！");
			judgeMasterOrderQuestion(distribute.getMasterOrderSn(), 1, define, Constant.order_type_distribute);
		} catch (Exception e) {
			logger.error(orderSn + "设缺货问题单：执行时异常 ", e);
			ri.setMessage("设缺货问题单：执行时异常 " + (e.getMessage() == null ? "" : e.getMessage()));
			// 记录操作日志异常信息
			saveAction(distribute, "<font style=color:red;>" + ri.getMessage() + "</font>", orderStatus.getAdminUser());
		}
		return ri;
	}
	
	@Override
	public ReturnInfo noticeOsFromErpForShort(String orderSn, List<LackSkuParam> lackSkuParams, String shortReason) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		info.setOrderSn(orderSn);
		logger.debug("ERP分配问题单begin:orderSn:" + orderSn + ",lackSkuParams:" + lackSkuParams + ",shortReason:" + shortReason);
		try {
			if (StringUtil.isTrimEmpty(orderSn)) {
				logger.error("[orderSn]传入参数为空，不能设问题单操作！");
				info.setMessage("[orderSn]传入参数为空，不能设问题单操作！");
				return info;
			}
			OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
			if (null == distribute) {
				logger.error("订单[" + orderSn + "]不存在，不能设问题单操作！");
				info.setMessage("订单[" + orderSn + "]不存在，不能设问题单操作！");
				return info;
			}
			if (distribute.getOrderStatus() != Constant.OI_ORDER_STATUS_UNCONFIRMED 
					&& distribute.getOrderStatus() != Constant.OI_ORDER_STATUS_CONFIRMED) {
				logger.error("订单[" + orderSn + "]不处于未确认或已确认状态,不能设问题单操作！");
				info.setMessage("订单[" + orderSn + "]不处于未确认或已确认状态,不能设问题单操作！");
				return info;
			}
			// 汇总商品的数量信息
			MasterOrderGoodsExample orderGoodsExample = new MasterOrderGoodsExample();
			orderGoodsExample.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
			List<MasterOrderGoods> orderGoodsList = masterOrderGoodsMapper.selectByExample(orderGoodsExample);
			if (StringUtil.isListNull(orderGoodsList)) {
				logger.error("订单[" + orderSn + "]商品列表为空,不能设问题单操作！");
				info.setMessage("订单[" + orderSn + "]商品列表为空,不能设问题单操作！");
				return info;
			}
			
			// 非缺货原因问题拦截
			StringBuilder actionNote = new StringBuilder();
			actionNote.append("<font color=\"red\">ERP通知OS，非缺货问题挂起,设为问题单,请客服沟通处理! </font><br />");
			if(StringUtils.contains(shortReason, ConstantValues.ERP_OS_SHORT_REASON_NO1)){
				// 跨仓个数受限
				actionNote.append("跨仓个数受限");
				info = questionOrderByOrderSn(orderSn, new OrderStatus(null ,orderSn, actionNote.toString(), Constant.OS_STRING_SYSTEM, "9951"));
				logger.debug("[doOrder/noticeOsFromErpForShort]:ERP-notice-OS.orderSn:" + orderSn+",调用问题单接口，retInfo:"+JSON.toJSONString(info));
				return info;
			}else if(StringUtils.contains(shortReason, ConstantValues.ERP_OS_SHORT_REASON_NO2)){
				// 运输线路未维护
				actionNote.append("运输线路未维护");
				info = questionOrderByOrderSn(orderSn, new OrderStatus(null ,orderSn, actionNote.toString(), Constant.OS_STRING_SYSTEM, "9952"));
				logger.debug("[doOrder/noticeOsFromErpForShort]:ERP-notice-OS.orderSn:" + orderSn+",调用问题单接口，retInfo:"+JSON.toJSONString(info));
				return info;
			}
			logger.debug("[doOrder/noticeOsFromErpForShort]:ERP-notice-OS.orderSn:" + orderSn+",设置前拦截通过，正常进入缺货设置逻辑。");
			actionNote = new StringBuilder();
			actionNote.append("<font color=\"red\">ERP通知OS，以下商品缺货：");
			logger.debug("[noticeOsFromErpForShort.htm]:ERP-notice-OS.orderSn:" + orderSn + ",goodsMap:" +",lackSkuParams:"+JSON.toJSONString(lackSkuParams));
			int shortCount = 0;
			if (StringUtil.isListNull(lackSkuParams)) {
				logger.error("订单[" + orderSn + "]缺货商品列表[lackSkuParams]为空,不能设问题单操作！");
				info.setMessage("订单[" + orderSn + "]缺货商品列表[lackSkuParams]为空,不能设问题单操作！");
				return info;
			}

			if (StringUtils.isBlank(shortReason)) {
				actionNote.append("<br/>设为问题单,请客服沟通处理!</font><br />");
			} else {
				actionNote.append("<br/>设为问题单,缺货原因:" + shortReason + ",请客服沟通处理!</font><br />");
			}
			for (LackSkuParam lackSkuParam : lackSkuParams) {
				shortCount += lackSkuParam.getLackNum();
				lackSkuParam.setDepotCode(Constant.DETAILS_DEPOT_CODE);
			}
			int goodsCount = 0;
			for (MasterOrderGoods orderGoods : orderGoodsList) {
				goodsCount += orderGoods.getGoodsNumber();
			}
			logger.debug("[doOrder/noticeOsFromErpForShort]:ERP-notice-OS.orderSn:" + orderSn+",缺货设置逻辑，shortCount:"+shortCount+",lackSkuParams:"+JSON.toJSONString(lackSkuParams));
			// 根据商品缺货量和订单商品总量比较，设置为全缺货|部分缺货问题单
			// 设为问题单成功
			if (StringUtils.isBlank(shortReason)) {
				actionNote.append("<br/>设为问题单,请客服沟通处理!</font><br />");
			} else {
				actionNote.append("<br/>设为问题单,缺货原因:" + shortReason + ",请客服沟通处理!</font><br />");
			}
			if (shortCount == goodsCount) {
				// 全缺货
				actionNote.append(" ; 全缺货问题单 ");
				OrderStatus orderStatus = new OrderStatus(null ,orderSn, "ERP通知OS商品缺货", Constant.OS_STRING_SYSTEM, "9953");
				orderStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.ERP);
				info = addLackSkuQuestion(orderSn, lackSkuParams, orderStatus);
				logger.debug("[doOrder/noticeOsFromErpForShort]:ERP-notice-OS.orderSn:" + orderSn+",调用问题单接口，res:"+JSON.toJSONString(info));
				return info;
			} else {
				// 部分缺货
				actionNote.append(" ; 部分缺货问题单 ");
				OrderStatus orderStatus = new OrderStatus(null ,orderSn, "ERP通知OS商品缺货", Constant.OS_STRING_SYSTEM, "9953");
				orderStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.ERP);
				info = addLackSkuQuestion(orderSn, lackSkuParams, orderStatus);
				logger.debug("[doOrder/noticeOsFromErpForShort]:ERP-notice-OS.orderSn:" + orderSn+",调用问题单接口，res:"+JSON.toJSONString(info));
				return info;
			}
		} catch (Exception e) {
			info.setMessage("订单[" + orderSn + "]设问题单异常：" +e.getMessage());
			logger.error("订单[" + orderSn + "]设问题单异常：" +e.getMessage(), e);
		}
		logger.debug("[noticeOsFromErpForShort]-end.:ERP-notice-OS.orderSn:" + orderSn);
		return info;
	}
	
	/**
	 * 检查缺货问题单中商品数据的准确性
	 * 
	 * @param shortageParams
	 * @param orderSn
	 * @param adminUser
	 * @param message
	 * @param logType
	 * @param code
	 * @return
	 */
	public ReturnInfo checkShortageOrderGoods(List<LackSkuParam> lackSkuParams, OrderDistribute distribute,
			OrderStatus orderStatus) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		List<String> skuList = new ArrayList<String>();
		for (LackSkuParam param : lackSkuParams) {
			if (StringUtil.isEmpty(param.getCustomCode())) {
				continue;
			}
			skuList.add(param.getCustomCode());
		}
		// 问题单中不含有商品
		if (StringUtil.isListNull(skuList)) {
			ri.setMessage("提交缺货商品编码为空,请检查后重新提交");
			return ri;
		}
		MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
		goodsExample.or().andOrderSnEqualTo(distribute.getOrderSn()).andCustomCodeIn(skuList).andIsDelEqualTo(0);
		List<MasterOrderGoods> list = masterOrderGoodsMapper.selectByExample(goodsExample);
		if (!StringUtil.isNotNullForList(list)) {
			// 记录操作日志异常信息
			String errorMsg = "缺货商品:" + JSON.toJSONString(skuList) + ",订单中不存在此缺货条码!";
			saveAction(distribute, "<font style=color:red;>设缺货问题单：" + orderStatus.getMessage()
					+ errorMsg + "</font>", orderStatus.getAdminUser());
			return ri;
		}
		StringBuffer buffer = new StringBuffer();
		// 筛选掉存在商品
		for (String sku : skuList) {
			boolean b = true;
			for (MasterOrderGoods goods : list) {
				if (sku.equals(goods.getCustomCode())) {
					b = false;
				}
			}
			if (b) {
				buffer.append(sku + "|");
			}
		}
		if (StringUtil.isNotEmpty(buffer.toString())) {
			// 记录操作日志异常信息
			String errorMsg = "商品编码：" + buffer + " 数据存在错误,订单中不存在该商品!";
			buffer = new StringBuffer(errorMsg);
			saveAction(distribute, "<font style=color:red;>设缺货问题单：" + orderStatus.getMessage()
					+ errorMsg + "</font>", orderStatus.getAdminUser());
			return ri;
		}
		ri.setIsOk(Constant.OS_YES);
		ri.setMessage("检查结束");
		return ri;
	}
	
	
	public ReturnInfo<List<OrderQuestionLackSkuNew>> checkShortageGoods(List<LackSkuParam> lackSkuParams, OrderDistribute distribute,
			OrderStatus orderStatus, OrderCustomDefine define) {
		ReturnInfo<List<OrderQuestionLackSkuNew>> info = new ReturnInfo<List<OrderQuestionLackSkuNew>>(Constant.OS_NO);
		List<String> skuList = new ArrayList<String>();
		for (LackSkuParam param : lackSkuParams) {
			String msg = "设置缺货问题单：编码=" + define.getCode() + ";原因=" + define.getName() + "; 参数=" + JSON.toJSONString(lackSkuParams);
			if (StringUtil.isEmpty(param.getCustomCode())) {
				saveAction(distribute, msg, orderStatus.getAdminUser());
				return info;
			}
			// 设置缺货问题单：编码=908;原因=缺货问题单; 参数=[{}]
			if (StringUtil.isEmpty(param.getDepotCode())) {
				saveAction(distribute, msg, orderStatus.getAdminUser());
				return info;
			}
			if (StringUtil.isEmpty(param.getCustomCode())) {
				saveAction(distribute, msg, orderStatus.getAdminUser());
				return info;
			}
			if (param.getLackNum() == null || param.getLackNum().intValue() == 0) {
				saveAction(distribute, msg, orderStatus.getAdminUser());
				return info;
			}
		}
		List<OrderQuestionLackSkuNew> lackSku = new ArrayList<OrderQuestionLackSkuNew>();
		String errorMsg = "";
		for (LackSkuParam param : lackSkuParams) {
			// 相同发货仓相同sku多条记录 根据价格最低的有限设置问题单（将商品的属性添加到问题单记录中）
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.setOrderByClause("transaction_price asc");
			MasterOrderGoodsExample.Criteria criteria = goodsExample.or();
			criteria.andOrderSnEqualTo(distribute.getOrderSn());
			criteria.andCustomCodeEqualTo(param.getCustomCode());
			criteria.andDepotCodeEqualTo(param.getDepotCode());
			criteria.andIsDelEqualTo(0);
			List<MasterOrderGoods> searchGoodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
			if (StringUtil.isListNull(searchGoodsList)) {
				errorMsg += "商品编码：" + param.getCustomCode() + ";配货仓：" + param.getDepotCode() + ",商品不存在！</br>";
				continue;
			}
			short lackNum = param.getLackNum();
			for (MasterOrderGoods searchGoods : searchGoodsList) {
				OrderQuestionLackSkuNew lackSkuNew = new OrderQuestionLackSkuNew();
				lackSkuNew.setOrderSn(distribute.getOrderSn());
				lackSkuNew.setDeliverySn(param.getDeliverySn());
				lackSkuNew.setLackReason(param.getLackReason());
				lackSkuNew.setDepotCode(param.getDepotCode());
				lackSkuNew.setQuestionCode(define.getCode());
				lackSkuNew.setCustomCode(param.getCustomCode());
				short goodsNumber = searchGoods.getGoodsNumber();
				if (lackNum <= 0) {
					break ;
				} else if (goodsNumber < lackNum) {
					lackSkuNew.setLackNum(goodsNumber);
					lackNum = (short)(lackNum - goodsNumber);
					lackSkuNew.setExtensionCode(searchGoods.getExtensionCode());
					lackSkuNew.setExtensionId(searchGoods.getExtensionId());
				} else {
					lackSkuNew.setLackNum(lackNum);
					lackSkuNew.setExtensionCode(searchGoods.getExtensionCode());
					lackSkuNew.setExtensionId(searchGoods.getExtensionId());
					lackNum = 0;
				}
				lackSku.add(lackSkuNew);
			}
		}
		if (StringUtil.isNotEmpty(errorMsg)) {
			errorMsg = "设置缺货问题单：<font color=\"red\">编码=" + define.getCode() + ";原因=" + define.getName() + errorMsg + "</font></br>";
			saveAction(distribute, errorMsg, orderStatus.getAdminUser());
		}
		if (StringUtil.isListNotNull(lackSku)) {
			info.setIsOk(Constant.OS_YES);
		}
		info.setData(lackSku);
		info.setMessage("检查结束");
		return info;
	}
	
	/**
	 * 保存问题单缺货问题商品
	 * 
	 * @param questionParam
	 * @param oqPkId
	 * @param orderSn
	 */
	private String saveLackSku(LackSkuParam lackSkuParam, String orderSn, OrderCustomDefine define) throws Exception {
		logger.debug("订单" + orderSn + "商品SKU:" + lackSkuParam.getCustomCode() + " 保存问题单缺货问题商品");
		StringBuffer outStockStr = new StringBuffer();
		OrderQuestionLackSkuNew lackSku = new OrderQuestionLackSkuNew();
		lackSku.setCustomCode(lackSkuParam.getCustomCode());
		lackSku.setOrderSn(orderSn);
		lackSku.setDepotCode(lackSkuParam.getDepotCode());
		lackSku.setQuestionCode(define.getCode());
		lackSku.setLackNum(lackSkuParam.getLackNum());
		lackSku.setDeliverySn(lackSkuParam.getDeliverySn());
		lackSku.setLackReason(lackSkuParam.getLackReason());
		// 缺货商品
		if (StringUtil.isTrimEmpty(lackSkuParam.getCustomCode())) {
			outStockStr.append(insertLackSku(lackSku, define, orderSn));
		} else {
			// 相同发货仓相同sku多条记录 根据价格最低的有限设置问题单（将商品的属性添加到问题单记录中）
			MasterOrderGoodsExample goodsExample2 = new MasterOrderGoodsExample();
			goodsExample2.setOrderByClause("transaction_price asc");
			MasterOrderGoodsExample.Criteria criteria = goodsExample2.or();
			criteria.andOrderSnEqualTo(orderSn);
			criteria.andCustomCodeEqualTo(lackSkuParam.getCustomCode());
			criteria.andDepotCodeEqualTo(lackSkuParam.getDepotCode());
			criteria.andIsDelEqualTo(0);
			List<MasterOrderGoods> searchGoodsList = masterOrderGoodsMapper.selectByExample(goodsExample2);
			if (StringUtil.isListNotNull(searchGoodsList)) {
				short lackNum = lackSkuParam.getLackNum();
				for (MasterOrderGoods searchGoods : searchGoodsList) {
					short goodsNumber = searchGoods.getGoodsNumber();
					if (lackNum <= 0) {
						break ;
					} else if (goodsNumber < lackNum) {
						lackSku.setLackNum(goodsNumber);
						lackNum = (short)(lackNum - goodsNumber);
						lackSku.setExtensionCode(searchGoods.getExtensionCode());
						lackSku.setExtensionId(searchGoods.getExtensionId());
						outStockStr.append(insertLackSku(lackSku, define, orderSn));
					} else {
						lackSku.setLackNum(lackNum);
						lackSku.setExtensionCode(searchGoods.getExtensionCode());
						lackSku.setExtensionId(searchGoods.getExtensionId());
						outStockStr.append(insertLackSku(lackSku, define, orderSn));
						lackNum = 0;
					}
				}
			} else {
				outStockStr.append("商品编码:" + lackSkuParam.getCustomCode() + ";发货仓编码：" + lackSkuParam.getDepotCode()
						+ ",订单商品中不存在!</br>");
				logger.error("订单" + orderSn + "商品编码:" + lackSkuParam.getCustomCode() + ";发货仓编码："
						+ lackSkuParam.getDepotCode() + ",订单商品中不存在!");
//				throw new Exception(outStockStr.toString());
				return outStockStr.toString();
			}
		}
		return outStockStr.toString();
	}
	
	private String insertLackSku(OrderQuestionLackSkuNew lackSku, OrderCustomDefine define, String orderSn) {
		logger.debug("订单" + orderSn + " 保存缺货问题单商品信息");
		// 校验该商品是否已经存在
		OrderQuestionLackSkuNewExample example = new OrderQuestionLackSkuNewExample();
		OrderQuestionLackSkuNewExample.Criteria criteria = example.or();
		criteria.andOrderSnEqualTo(orderSn).andQuestionCodeEqualTo(define.getCode());
		StringBuffer msg = new StringBuffer();
		String deliverySn = lackSku.getDeliverySn();
		String customCode = lackSku.getCustomCode();
		String depotCode = lackSku.getDepotCode();
		if (StringUtil.isNotEmpty(customCode)) {
			msg.append("缺货商品：" + customCode + ";");
			criteria.andCustomCodeEqualTo(customCode);
		}
		if (StringUtil.isNotEmpty(deliverySn)) {
			msg.append("交货单编码：" + deliverySn + ";");
			criteria.andDeliverySnEqualTo(deliverySn);
		}
		if (StringUtil.isNotEmpty(depotCode)) {
			msg.append("发货仓编码：" + depotCode + ";");
			criteria.andDepotCodeEqualTo(depotCode);
		}
		if (StringUtil.isNotEmpty(lackSku.getExtensionCode())) {
			criteria.andExtensionCodeEqualTo(lackSku.getExtensionCode());
		}
		if (StringUtil.isNotEmpty(lackSku.getExtensionId())) {
			criteria.andExtensionIdEqualTo(lackSku.getExtensionId());
		}
		if (lackSku.getLackNum() != null) {
			msg.append("缺货数量：" + lackSku.getLackNum() + ";");
		}
		if (StringUtil.isNotEmpty(lackSku.getLackReason())) {
			msg.append("缺货原因：" + lackSku.getLackReason() + ";</br>");
		}
		List<OrderQuestionLackSkuNew> lackSkus = orderQuestionLackSkuNewMapper.selectByExample(example);
		if (StringUtil.isListNotNull(lackSkus)) {
			msg.append("商品或者交货单编码已存在！</br>");
			// 该问题单类型下已经含有这款缺货商品
			return msg.toString();
		}
		msg.append("</br>");
		OrderQuestionLackSkuNew newLackSku = new OrderQuestionLackSkuNew();
		newLackSku.setOrderSn(orderSn);
		newLackSku.setCustomCode(customCode);
		newLackSku.setDepotCode(depotCode);
		newLackSku.setQuestionCode(define.getCode());
		newLackSku.setLackNum(lackSku.getLackNum());
		newLackSku.setDeliverySn(deliverySn);
		newLackSku.setLackReason(lackSku.getLackReason());
		newLackSku.setExtensionCode(lackSku.getExtensionCode());
		newLackSku.setExtensionId(lackSku.getExtensionId());
		orderQuestionLackSkuNewMapper.insertSelective(newLackSku);
		return msg.toString();
	}
	
	private DistributeQuestion createQuestion(OrderDistribute distribute, Date now, OrderCustomDefine orderCustomDefine) {
		DistributeQuestion orderQuestion = new DistributeQuestion();
		orderQuestion.setAddTime(now);
		orderQuestion.setQuestionDesc(orderCustomDefine.getName());
		orderQuestion.setQuestionCode(orderCustomDefine.getCode());
		orderQuestion.setOrderSn(distribute.getOrderSn());
		orderQuestion.setQuestionType(0);
		return orderQuestion;
	}
	
	
	private void saveAction(OrderDistribute distribute, String message, String actiomUser) {
		DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
		orderAction.setActionUser(actiomUser);
		orderAction.setActionNote(message);
		distributeActionService.saveOrderAction(orderAction);
	}
	
	private void saveAction(OrderDistribute distribute, OrderCustomDefine define, OrderStatus orderStatus) {
		StringBuffer sb = new StringBuffer(orderStatus.getMessage());
		sb.append("&nbsp;&nbsp;&nbsp;<font color=\"red\">问题单code：" + define.getCode() + ";问题单原因：" + define.getName() + "</font>");
		DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
		orderAction.setActionNote(sb.toString());
		orderAction.setActionUser(orderStatus.getAdminUser());
		distributeActionService.saveOrderAction(orderAction);
	}

    /**
     * 获取交货单编码列表
     * @param distributes 交货单列表
     * @return List<String>
     */
	private List<String> getOrderSns(List<OrderDistribute> distributes) {
		List<String> orderSns = new ArrayList<String>();
		for (OrderDistribute distribute : distributes) {
			orderSns.add(distribute.getOrderSn());
		}
		return orderSns;
	}
	
	private boolean equalsCode(String codes, String code) {
		if (StringUtil.isTrimEmpty(codes)) {
			return true;
		}
		String[] arr = codes.split(",");
		boolean flag = false;
		for (String str : arr) {
			if (code.equals(str)) {
				flag = true;
				break ;
			}
		}
		return flag;
	}
	
	
	private ReturnInfo checkOrderQuestion(String orderSn, OrderDistribute distribute, OrderCustomDefine define, OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		logger.debug("订单设置问题单 orderSn=" + orderSn + ";orderStatus=" + orderStatus);
		if (null == orderStatus) {
			info.setMessage("设置问题单参数[orderStatus]为空，不能进行设问题单操作！");
			logger.error("设置问题单参数[orderStatus]为空，不能进行设问题单操作！");
			return info;
		}
		logger.debug("设为问题单:orderSn=" + orderSn + ";orderStatus=" + orderStatus.toString());
		if (StringUtil.isEmpty(orderStatus.getCode())) {
			logger.error("订单[" + orderSn + "]传入设为问题单原因为空！");
			info.setMessage("订单[" + orderSn + "]传入设为问题单原因为空！");
			return info;
		}
		int oStatus = distribute.getOrderStatus();
		if (oStatus == Constant.OI_ORDER_STATUS_CANCLED) {
			info.setMessage("订单[" + orderSn + "]已处于已取消状态，不能进行设问题单操作！");
			return info;
		}
		if (oStatus == Constant.OI_ORDER_STATUS_FINISHED) {
			info.setMessage("订单[" + orderSn + "]已处于完成状态，不能进行设问题单操作！");
			return info;
		}
		// 已发货订单设问题单拦截只记录日志
		if (distribute.getShipStatus() != null && distribute.getShipStatus() > Constant.OI_SHIP_STATUS_PARTSHIPPED) {
			saveAction(distribute, define, orderStatus);
			info.setMessage("订单[" + orderSn + "]的货物已发，不能在进行设为问题单操作！");
			logger.error("订单[" + orderSn + "]的货物已发，不能在进行设为问题单操作！");
			return info;
		}
		info.setIsOk(Constant.OS_YES);
		return info;
	}
	
	private void saveQuestion(String orderSn, OrderDistribute distribute, OrderCustomDefine define,
			String sn, OrderStatus orderStatus, String message) {
		OrderDistribute updateDistribute = new OrderDistribute();
		updateDistribute.setQuestionStatus(Constant.OI_QUESTION_STATUS_QUESTION);
		updateDistribute.setQuestionTime(new Date());
		updateDistribute.setUpdateTime(new Date());
		updateDistribute.setOrderSn(orderSn);
		// 订单状态设置为未确认状态
		if ("995".equals(define.getCode()) || "996".equals(define.getCode()) || "9951".equals(define.getCode()) || "9952".equals(define.getCode())
				 || "9953".equals(define.getCode())) {
			updateDistribute.setOrderStatus((byte) Constant.OI_ORDER_STATUS_UNCONFIRMED);
		}
		orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
		DistributeQuestion question = createQuestion(distribute, new Date(), define);
		if (StringUtil.isNotEmpty(sn)) {
			question.setSupplierOrderSn(sn);
		}
		// 新增问题单列表
		distributeQuestionMapper.insertSelective(question);
		//订单操作记录
		StringBuffer msg = new StringBuffer(orderStatus.getMessage());
		msg.append("<br/>&nbsp;&nbsp;&nbsp;<font color=\"red\">问题单code：" + define.getCode() + ";问题单原因：" + define.getName());
		if (StringUtil.isNotEmpty(message)) {
			msg.append(message);
		}
		msg.append("</font>");
		DistributeAction orderAction = distributeActionService.createQrderAction(distribute);
		orderAction.setActionUser(orderStatus.getAdminUser());
		orderAction.setActionNote(msg.toString());
		distributeActionService.saveOrderAction(orderAction);
	}
	
	
	/**
	 * 检查订单是否可以进行正常订单操作
	 * 返回null表示此订单可以执行正常订单操作操作，否则表示不能执行
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public ReturnInfo checkConditionOfExecution(MasterOrderInfo orderInfo, String masterOrderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		/* 参数检验 */
		if (orderInfo == null) {
			ri.setMessage("订单" + masterOrderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		int orderStatus = orderInfo.getOrderStatus();
		if (orderStatus == Constant.OI_ORDER_STATUS_CANCLED) {
			ri.setMessage("订单" + masterOrderSn + "已处于已取消状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		if (orderStatus == Constant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage("订单" + masterOrderSn + "已处于完成状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		return null;
	}
	
	/**
	 * 检查订单是否可以进行正常订单操作
	 * 返回null表示此订单可以执行正常订单操作操作，否则表示不能执行
	 * @param orderInfo
	 * @param orderSn
	 * @return
	 */
	public ReturnInfo checkConditionOfExecution(OrderDistribute orderInfo, String orderSn, String actionType) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		/* 参数检验 */
		if (orderInfo == null) {
			ri.setMessage("订单" + orderSn + "在近三个月的记录中，没有取得订单信息!");
			return ri;
		}
		int orderStatus = orderInfo.getOrderStatus();
		if (orderStatus == Constant.OI_ORDER_STATUS_CANCLED) {
			ri.setMessage("订单" + orderSn + "已处于已取消状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		if (orderStatus == Constant.OI_ORDER_STATUS_FINISHED) {
			ri.setMessage("订单" + orderSn + "已处于完成状态，不能在进行" + actionType + "操作！");
			return ri;
		}
		return null;
	}
	
	
	private ReturnInfo<List<OrderDistribute>> checkDistributeListOrderQuestion(List<OrderDistribute> distributes) {
		ReturnInfo<List<OrderDistribute>> info = new ReturnInfo<List<OrderDistribute>>(Constant.OS_NO);
		/* 执行前提检查 */
		List<OrderDistribute> updateDistributes = new ArrayList<OrderDistribute>();
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			if (distribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
				info.setMessage("订单" + orderSn + "已处于已取消状态，不能在进行设问题单操作！");
				continue;
			}
			if (distribute.getOrderStatus() == Constant.OI_ORDER_STATUS_FINISHED) {
				info.setMessage("订单" + orderSn + "已处于完成状态，不能在进行设问题单操作！");
				continue;
			}
			updateDistributes.add(distribute);
		}
		info.setData(updateDistributes);
		info.setIsOk(Constant.OS_YES);
		return info;
	}
	
	@SuppressWarnings("rawtypes")
	private ReturnInfo judgeMasterOrderStatus(String masterOrderSn, MasterOrderInfo master, byte orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		OrderDistributeExample distributeExample = new OrderDistributeExample();
		OrderDistributeExample.Criteria criteria = distributeExample.or();
		criteria.andMasterOrderSnEqualTo(masterOrderSn);
		List<OrderDistribute> distributes = this.orderDistributeMapper.selectByExample(distributeExample);
		if (StringUtil.isListNull(distributes)) {
			info.setMessage("主订单[" + masterOrderSn + "] 下子订单列表为空！");
			return info;
		}
		int count = 0;
		for (OrderDistribute orderDistribute : distributes) {
			if (orderDistribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
				count ++;
				continue;
			}
			if (orderDistribute.getOrderStatus() == orderStatus) {
				count ++;
				continue;
			}
		}
		if (count != distributes.size()) {
			info.setMessage("主订单[" + masterOrderSn + "] 下子订单状态不一致！");
			return info;
		}
		try {
			MasterOrderInfo updateMaster = new MasterOrderInfo();
			updateMaster.setMasterOrderSn(masterOrderSn);
			updateMaster.setOrderStatus(orderStatus);
			if (orderStatus == Constant.OI_ORDER_STATUS_CONFIRMED) {
				updateMaster.setConfirmTime(new Date());
			}
			masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
		} catch (Exception e) {
			info.setMessage("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage());
			logger.error("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage(), e);
		}
		return info;
	}
}
