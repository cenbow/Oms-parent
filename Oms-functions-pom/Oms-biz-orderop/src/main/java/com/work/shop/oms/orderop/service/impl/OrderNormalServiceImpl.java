package com.work.shop.oms.orderop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.work.shop.oms.bean.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bimonitor.service.BIMonitorService;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.DistributeQuestionDelMapper;
import com.work.shop.oms.dao.DistributeQuestionMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderQuestionDelMapper;
import com.work.shop.oms.dao.MasterOrderQuestionMapper;
import com.work.shop.oms.dao.OrderCustomDefineMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderQuestionLackSkuNewDelMapper;
import com.work.shop.oms.dao.OrderQuestionLackSkuNewMapper;
import com.work.shop.oms.dao.define.OrderDistributeDefineMapper;
import com.work.shop.oms.distribute.service.DistributeSupplierService;
import com.work.shop.oms.erp.service.ErpInterfaceProxy;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.PurchaseOrderService;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.orderop.service.OrderNormalService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单正常服务接口处理
 * @author QuYachu
 */
@Service("orderNormalService")
public class OrderNormalServiceImpl implements OrderNormalService {

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
	private DistributeQuestionMapper distributeQuestionMapper;
	@Resource
	DistributeActionService distributeActionService;
	@Resource
	BIMonitorService biMonitorService;
	@Resource
	private ErpInterfaceProxy erpInterfaceProxy;
	@Resource
	private OrderQuestionLackSkuNewMapper orderQuestionLackSkuNewMapper;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private DistributeQuestionDelMapper distributeQuestionDelMapper;
	@Resource
	private OrderQuestionLackSkuNewDelMapper orderQuestionLackSkuNewDelMapper;
	@Resource
	private DistributeSupplierService distributeSupplierService;
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;
	@Resource(name = "orderNormalProviderJmsTemplate")
	private JmsTemplate orderNormalJmsTemplate;
	@Resource
	private OrderConfirmService orderConfirmService;
	@Resource
	private MasterOrderQuestionDelMapper masterOrderQuestionDelMapper;

	@Resource(name = "orderDistributeProducerJmsTemplate")
	private JmsTemplate orderDistributeJmsTemplate;
	@Resource
	private PurchaseOrderService purchaseOrderService;
	
	private String shopCode;
	
	private List<String> customCodes;

    /**
     * 主订单返回正常单
     * @param master 订单信息
     * @param distributes 交货单列表
     * @param orderStatus 订单状态
     * @param orderType 订单类型 0订单、1交货单
     * @return ReturnInfo
     */
	private ReturnInfo normalOrder(MasterOrderInfo master, List<OrderDistribute> distributes, OrderStatus orderStatus, String orderType) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if(master == null && distributes == null) {
			logger.warn("[masterOrderInfo]或[distribute]不能都为空！");
			info.setMessage("[masterOrderInfo]或[distribute]不能都为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单返回正常单操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单返回正常单操作！");
			return info;
		}
		if (ConstantValues.METHOD_SOURCE_TYPE.POS.equals(orderStatus.getSource())) {
			orderStatus.setMessage("POS返回正常单：" + orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.FRONT.equals(orderStatus.getSource())) {
			orderStatus.setMessage("前台返回正常单：" + orderStatus.getMessage());
		} else if (ConstantValues.METHOD_SOURCE_TYPE.ERP.equals(orderStatus.getSource())) {
			orderStatus.setMessage("ERP返回正常单：" + orderStatus.getMessage());
		} else {
			orderStatus.setMessage("返回正常单：" + orderStatus.getMessage());
		}
		String masterOrderSn = master.getMasterOrderSn();
		logger.debug("订单返回正常单：masterOrderSn=" + masterOrderSn + ";orderStatus=" + JSON.toJSONString(orderStatus));
		MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		if (extend == null) {
			info.setMessage("订单[" + masterOrderSn + "]扩展表不存在，不能进行返回正常单操作！");
			logger.error("订单[" + masterOrderSn + "]扩展表不存在，不能进行返回正常单操作！");
			return info;
		}
		// 有子订单时：子订单转正常单
		if (StringUtil.isListNotNull(distributes)) {
			info = normalOrderByMbDistribute(getOrderSns(distributes), distributes, orderStatus);
			if (info.getIsOk() == Constant.OS_NO) {
				return info;
			}
		}
		// 主订单返回正常单
		if (Constant.order_type_master.equals(orderType)) {
			info = normalOrderByMaster(master.getMasterOrderSn(), master, orderStatus);
		} else if (Constant.order_type_distribute.equals(orderType)) {
			// 子订单返回正常单
			info = judgeMasterOrderNormal(masterOrderSn, master, orderType);
		}
		// 库存预售订单关闭时，需要自动确认
		/*if (orderStatus.getSource() == ConstantValues.METHOD_SOURCE_TYPE.ERP) {
			orderStatus.setMessage("预售关闭转正常单自动确认：");
			orderConfirmService.confirmOrderByMasterSn(masterOrderSn, orderStatus);
		}*/
		return info;
	}

    /**
     * 根据订单编码获取问题单列表
     * @param masterOrderSn
     * @return List<MasterOrderQuestion>
     */
	private List<MasterOrderQuestion> getMasterOrderQuestionByMasterOrderSn(String masterOrderSn) {
        // 问题单列表
        MasterOrderQuestionExample queryExample = new MasterOrderQuestionExample();
        queryExample.or().andMasterOrderSnEqualTo(masterOrderSn);
        List<MasterOrderQuestion> allQuestions = masterOrderQuestionMapper.selectByExample(queryExample);
        return allQuestions;
    }

    /**
     * 订单返回正常单
     * @param masterOrderSn
     * @param master
     * @param orderStatus
     * @return ReturnInfo
     */
	private ReturnInfo normalOrderByMaster(String masterOrderSn, MasterOrderInfo master, OrderStatus orderStatus) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		logger.debug("返回正常单:masterOrderSn=" + masterOrderSn + ";orderStatus=" + orderStatus.toString());
		if (StringUtil.isListNull(orderStatus.getQuestionTypes())) {
			logger.error(masterOrderSn + "传入操作类型为空，不能进行返回正常单操作！");
			ri.setMessage("传入操作类型为空，不能进行返回正常单操作！");
			return ri;
		}
		
		// 问题单列表
		List<MasterOrderQuestion> allQuestions = getMasterOrderQuestionByMasterOrderSn(masterOrderSn);
		
		String msg = null;
		try {
			if (CollectionUtils.isEmpty(allQuestions)) {
				processMasterOrderQuestion(masterOrderSn);
                ri.setIsOk(Constant.OS_YES);
                ri.setMessage("返回正常单成功");
				return ri;
			}

            // 问题单判断
            Map<Integer, Integer> questionTypeMap = getQuestionTypeMap(orderStatus.getQuestionTypes());
			// 普通问题单
			List<MasterOrderQuestion> normalItems = new ArrayList<>();
			// 缺货问题单
			List<MasterOrderQuestion> lackItems = new ArrayList<>();
			// 审核问题单
			List<MasterOrderQuestion> reviewItems = new ArrayList<>();
			// 签章问题单
			List<MasterOrderQuestion> signItems = new ArrayList<>();

            QuestionOrderTypeBean questionOrderTypeBean = getQuestionOrderTypeBean(questionTypeMap, allQuestions, normalItems, lackItems, reviewItems, signItems);

			// 问题单处理
            processOrderQuestion(masterOrderSn, questionTypeMap, normalItems, lackItems, reviewItems, signItems);

			if (questionOrderTypeBean.isNormalFlg() && questionOrderTypeBean.isLackFlg()
                    && questionOrderTypeBean.isReviewFlg() && questionOrderTypeBean.isSignFlag()) {
				processMasterOrderQuestion(masterOrderSn);
				msg = "返回正常单成功";
				logger.info("-2020061111-ERP--"+ JSON.toJSONString(master));
				//返回正常单后续操作（包含创建采购单）
                processMasterOrderNormalFollow(master, orderStatus);
			} else {
				msg = "<font style=color:red;>返回正常单失败：存在其他类型问题单</font>";
			}
			ri.setIsOk(Constant.OS_YES);
			ri.setMessage("返回正常单操作成功!");
		} catch (Exception e) {
			String errorMsg = e.getMessage() == null ? "" : e.getMessage();
			logger.error("订单[" + masterOrderSn + "]返回正常单异常：" + errorMsg, e);
			ri.setMessage("返回正常单异常：" + errorMsg);
			msg = "<font style=color:red;>返回正常单失败：" + ri.getMessage() + "</font>";
		} finally {
			// 记录操作日志异常信息
            saveMasterOrderAction(master, orderStatus, msg);
			logger.info("返回正常单" + JSON.toJSONString(ri));
		}
		return ri;
	}

    /**
     * 获取问题单对应类型情况
     * @param questionTypeMap
     * @param allQuestions
     * @param normalItems
     * @param lackItems
     * @param reviewItems
     * @param signItems
     * @return
     */
	private QuestionOrderTypeBean getQuestionOrderTypeBean(Map<Integer, Integer> questionTypeMap, List<MasterOrderQuestion> allQuestions,
                                                           List<MasterOrderQuestion> normalItems, List<MasterOrderQuestion> lackItems,
                                                           List<MasterOrderQuestion> reviewItems, List<MasterOrderQuestion> signItems) {

        QuestionOrderTypeBean questionOrderTypeBean = new QuestionOrderTypeBean();
        for (MasterOrderQuestion item : allQuestions) {
            int questionType = item.getQuestionType();
            if (questionType == Constant.QUESTION_TYPE_NORMAL) {
                normalItems.add(item);
                if (questionTypeMap.containsKey(item.getQuestionType())) {
                    questionOrderTypeBean.setNormalFlg(true);
                }
            } else if (questionType == Constant.QUESTION_TYPE_LACK) {
                lackItems.add(item);
                if (questionTypeMap.containsKey(item.getQuestionType())) {
                    questionOrderTypeBean.setLackFlg(true);
                }
            } else if (questionType == Constant.QUESTION_TYPE_REVIEW) {
                reviewItems.add(item);
                if (questionTypeMap.containsKey(item.getQuestionType())) {
                    questionOrderTypeBean.setReviewFlg(true);
                }
            } else if (questionType == Constant.QUESTION_TYPE_SIGN) {
                signItems.add(item);
                if (questionTypeMap.containsKey(item.getQuestionType())) {
                    questionOrderTypeBean.setSignFlag(true);
                }
            }
        }
        if (StringUtil.isListNull(normalItems)) {
            questionOrderTypeBean.setNormalFlg(true);
        }
        if (StringUtil.isListNull(lackItems)) {
            questionOrderTypeBean.setLackFlg(true);
        }
        if (StringUtil.isListNull(reviewItems)) {
            questionOrderTypeBean.setReviewFlg(true);
        }
        if (StringUtil.isListNull(signItems)) {
            questionOrderTypeBean.setSignFlag(true);
        }

        return questionOrderTypeBean;
    }

    /**
     * 保存订单日志
     * @param master
     * @param orderStatus
     * @param msg
     */
	private void saveMasterOrderAction(MasterOrderInfo master, OrderStatus orderStatus, String msg) {
        // 记录操作日志异常信息
        MasterOrderAction orderAction = masterOrderActionService.createOrderAction(master);
        orderAction.setActionNote(msg);
        orderAction.setActionUser(orderStatus.getAdminUser());
        masterOrderActionService.insertOrderActionByObj(orderAction);
    }

    /**
     * 处理订单问题单
     * @param masterOrderSn
     * @param questionTypeMap
     * @param normalItems
     * @param lackItems
     * @param reviewItems
     * @param signItems
     */
	private void processOrderQuestion(String masterOrderSn, Map<Integer, Integer> questionTypeMap,
                                      List<MasterOrderQuestion> normalItems,
                                      List<MasterOrderQuestion> lackItems,
                                      List<MasterOrderQuestion> reviewItems,
                                      List<MasterOrderQuestion> signItems) {
        // 普通问题单处理
        if (questionTypeMap.containsKey(Constant.QUESTION_TYPE_NORMAL) && StringUtil.isNotNullForList(normalItems)) {
            processOrderQuestion(masterOrderSn, normalItems);
        }
        // 缺货问题单处理
        if (questionTypeMap.containsKey(Constant.QUESTION_TYPE_LACK) && StringUtil.isNotNullForList(lackItems)) {
            processOrderQuestion(masterOrderSn, lackItems);
        }
        // 待审核问题单处理
        if (questionTypeMap.containsKey(Constant.QUESTION_TYPE_REVIEW) && StringUtil.isNotNullForList(reviewItems)) {
            processOrderQuestion(masterOrderSn, reviewItems);
        }
        // 待签章问题单处理
        if (questionTypeMap.containsKey(Constant.QUESTION_TYPE_SIGN) && StringUtil.isNotNullForList(signItems)) {
            processOrderQuestion(masterOrderSn, signItems);
        }
    }

    /**
     * 处理订单问题单
     * @param masterOrderSn
     * @param omsQuestions
     */
	private void processOrderQuestion(String masterOrderSn, List<MasterOrderQuestion> omsQuestions) {
        for (MasterOrderQuestion question : omsQuestions) {
            MasterOrderQuestionKey questionKey = new MasterOrderQuestionKey();
            questionKey.setMasterOrderSn(masterOrderSn);
            questionKey.setQuestionCode(question.getQuestionCode());
            masterOrderQuestionMapper.deleteByPrimaryKey(questionKey);

            MasterOrderQuestionDel questionDel = new MasterOrderQuestionDel();
            questionDel.setAddTime(question.getAddTime());
            questionDel.setMasterOrderSn(masterOrderSn);
            questionDel.setQuestionCode(question.getQuestionCode());
            questionDel.setQuestionDesc(question.getQuestionDesc());
            questionDel.setQuestionType(question.getQuestionType());
            masterOrderQuestionDelMapper.insertSelective(questionDel);
        }
    }
	
	/**
	 * 判断主单问题单状态
	 * @param masterOrderSn
	 * @param master
	 * @param type
	 * @return
	 */
	private ReturnInfo judgeMasterOrderNormal(String masterOrderSn, MasterOrderInfo master, String type) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO, "处理失败");
		try {
			if (Constant.order_type_distribute.equals(type)) {
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
					if (orderDistribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED
							|| orderDistribute.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
						count ++;
						continue;
					}
				}
				if (count != distributes.size()) {
					info.setMessage("主订单[" + masterOrderSn + "] 下子订单状态不一致！");
					return info;
				}
				deleteMasterOrderQuestion(masterOrderSn);
			}
			processMasterOrderQuestion(masterOrderSn);
			info.setIsOk(Constant.OS_YES);
			info.setMessage("返回正常单成功");
		} catch (Exception e) {
			info.setMessage("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage());
			logger.error("主订单[" + masterOrderSn + "] 操作异常" + e.getMessage(), e);
		}
		return info;
	}

	/**
	 * 返回正常单
	 * @param masterOrderSn 主订单号
	 * @param orderStatus adminUser:操作人;message:备注;type:返回类型0:一般问题单1:缺货问题单;2:全部转正常单;switchFlag:库存占用释放开关（true:开;false:关）
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo normalOrderByMasterSn(String masterOrderSn, OrderStatus orderStatus) {
		logger.info("订单返回正常单：masterOrderSn=" + masterOrderSn + ";orderStatus=" + JSON.toJSONString(orderStatus));
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			logger.error("[masterOrderSn]不能为空！");
			info.setMessage("[masterOrderSn]不能为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空！");
			info.setMessage("[orderStatus]传入参数为空！");
			return info;
		}
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		List<OrderDistribute> distributes = null;
		if (master.getSplitStatus().byteValue() != Constant.SPLIT_STATUS_UNSPLITED.byteValue()) {
			// 订单下的所有符合条件的交货单
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria mbCriteria = distributeExample.or();
			mbCriteria.andMasterOrderSnEqualTo(master.getMasterOrderSn());
			mbCriteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
			distributes = this.orderDistributeMapper.selectByExample(distributeExample);
			if (StringUtil.isListNotNull(distributes)) {
				ReturnInfo<List<OrderDistribute>> checkInfo = checkDistributeListOrderNormal(distributes);
				distributes = checkInfo.getData();
			}
		}
		info = normalOrder(master, distributes, orderStatus, Constant.order_type_master);
		return info;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo normalOrderByOrderSn(String orderSn,
			OrderStatus orderStatus) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(orderSn) && StringUtil.isTrimEmpty(orderSn)) {
			logger.error("[orderSn]不能为空！");
			info.setMessage("[orderSn]不能为空！");
			return info;
		}
		if (orderStatus == null) {
			logger.error("[orderStatus]传入参数为空，不能进行订单返回正常单操作！");
			info.setMessage("[orderStatus]传入参数为空，不能进行订单返回正常单操作！");
			return info;
		}
		logger.debug("订单返回正常单：orderSn=" + orderSn + ";orderStatus=" + orderStatus);
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);;
		if (distribute == null) {
			logger.error("订单[" + orderSn + "]查询结果为空，不能进行订单返回正常单操作！");
			info.setMessage("订单[" + orderSn + "]查询结果为空，不能进行订单返回正常单操作！");
			return info;
		}
		if (distribute.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
			logger.error("订单[" + orderSn + "]是正常单，不能进行订单返回正常单操作！");
			info.setMessage("订单[" + orderSn + "]是正常单，不能进行订单返回正常单操作！");
			return info;
		}
		MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (master == null) {
			logger.error("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单确认操作！");
			info.setMessage("订单主订单[" + distribute.getMasterOrderSn() + "]查询结果为空，不能进行订单确认操作！");
			return info;
		}
		List<OrderDistribute> distributes = new ArrayList<OrderDistribute>();
		distributes.add(distribute);
		info = normalOrder(master, distributes, orderStatus, Constant.order_type_distribute);
		return info;
	}

    /**
     * 供应商交货单返回正常
     * @param orderSns 交货单号列表
     * @param distributes 交货单列表
     * @param orderStatus 订单状态
     * @return ReturnInfo
     */
	private ReturnInfo normalOrderByMbDistribute(List<String> orderSns, List<OrderDistribute> distributes, OrderStatus orderStatus) {
		logger.info("订单返回正常单 orderSns=" + orderSns + ";orderStatus=" + JSON.toJSONString(orderStatus));
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isListNull(distributes)) {
			logger.warn("[" + orderSns + "]订单不存在，无法返回正常单");
			info.setMessage("[" + orderSns + "]订单不存在，无法返回正常单");
			return info;
		}
		List<OrderDistribute> updateDistributes = getOrderDistributeSn(distributes);
		if (StringUtil.isListNull(updateDistributes)) {
			info.setIsOk(Constant.OS_YES);
			return info;
		}
		Map<Integer, Integer> questionTypeMap = getQuestionTypeMap(orderStatus.getQuestionTypes());
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();
			// 问题单判断
			DistributeQuestionExample queryExample = new DistributeQuestionExample();
			queryExample.or().andOrderSnEqualTo(orderSn);
			// OS问题单
			List<DistributeQuestion> allQuestions = distributeQuestionMapper.selectByExample(queryExample);
			if (CollectionUtils.isEmpty(allQuestions)) {
				processOrderDistributeQuestion(orderSn);
				info.setIsOk(Constant.OS_YES);
				info.setMessage("订单["+orderSns+"]订单返回正常单成功");
				continue;
			}

			// OS问题单处理标识
			boolean normalFlg = false;
			// 物流问题单处理标识
			boolean lackFlg = false;
			// 审核问题单
			boolean reviewFlg = false;
			// 签章问题单
            boolean signFlag = false;
			// 普通问题单
			List<DistributeQuestion> normalItems = new ArrayList<>();
			// 缺货问题单
			List<DistributeQuestion> lackItems = new ArrayList<>();
			// 审核问题单
			List<DistributeQuestion> reviewItems = new ArrayList<>();
            // 签章问题单
            List<DistributeQuestion> signItems = new ArrayList<>();

			for (DistributeQuestion item : allQuestions) {
                switch (item.getQuestionType()) {
                    case Constant.QUESTION_TYPE_NORMAL:
                        normalItems.add(item);
                        if (questionTypeMap.containsKey(item.getQuestionType())) {
                            normalFlg = true;
                        }
                        break;
                    case Constant.QUESTION_TYPE_LACK:
                        lackItems.add(item);
                        if (questionTypeMap.containsKey(item.getQuestionType())) {
                            lackFlg = true;
                        }
                        break;
                    case Constant.QUESTION_TYPE_REVIEW:
                        reviewItems.add(item);
                        if (questionTypeMap.containsKey(item.getQuestionType())) {
                            reviewFlg = true;
                        }
                        break;
                    case Constant.QUESTION_TYPE_SIGN:
                        signItems.add(item);
                        if (questionTypeMap.containsKey(item.getQuestionType())) {
                            signFlag = true;
                        }
                        break;
                    default:
                        break;
                }
			}
			if (StringUtil.isListNull(normalItems)) {
				normalFlg = true;
			}
			if (StringUtil.isListNull(lackItems)) {
				lackFlg = true;
			}
			if (StringUtil.isListNull(reviewItems)) {
				reviewFlg = true;
			}
            if (StringUtil.isListNull(signItems)) {
                signFlag = true;
            }
			StringBuffer noteSb = new StringBuffer(orderStatus.getMessage());
			try {
				// 三种问题单类型都没有订单设置正常单类型
				if (normalFlg && lackFlg && reviewFlg && signFlag) {
					// 如果需要通知其他系统返回正常单在此通知
					// 返回正常单
					processOrderDistributeQuestion(orderSn);
				}
				// 普通问题单处理
				if (questionTypeMap.containsKey(Constant.QUESTION_TYPE_NORMAL) && StringUtil.isNotNullForList(normalItems)) {
					// 问题单信息备份到orderQuestionDel中
					deleteOrderQuestions(orderSn, normalItems, "普通问题单返回正常单");
					noteSb.append(" 普通问题单返回正常单处理; ");
				}
				// 缺货问题单处理
				if (questionTypeMap.containsKey(Constant.QUESTION_TYPE_LACK) && StringUtil.isNotNullForList(lackItems)) {
					// 问题单信息备份到orderQuestionDel中
					deleteOrderQuestions(orderSn, lackItems, "缺货问题单返回正常单");
					noteSb.append(" 缺货问题单返回正常单处理; ");
				}
				// 待审核问题单处理
				if (questionTypeMap.containsKey(Constant.QUESTION_TYPE_REVIEW) && StringUtil.isNotNullForList(reviewItems)) {
					// 问题单信息备份到orderQuestionDel中
					deleteOrderQuestions(orderSn, reviewItems, "待审核问题单返回正常单");
					noteSb.append(" 待审核问题单返回正常单处理; ");
				}
                // 待签章问题单处理
                if (questionTypeMap.containsKey(Constant.QUESTION_TYPE_SIGN) && StringUtil.isNotNullForList(signItems)) {
                    // 问题单信息备份到orderQuestionDel中
                    deleteOrderQuestions(orderSn, signItems, "待签章问题单返回正常单");
                    noteSb.append(" 待签章问题单返回正常单处理; ");
                }
				info.setIsOk(Constant.OS_YES);
				info.setMessage("订单["+orderSns+"]订单返回正常单成功");
			} catch (Exception e) {
				String errorMsg = "订单[" + orderSn + "]订单返回正常单异常：" + e.getMessage();
				logger.error(errorMsg, e);
				info.setMessage(errorMsg);
				noteSb = new StringBuffer("<font style=color:red;>" + errorMsg + "</font>");
			} finally {
				distributeActionService.addOrderAction(orderSn, noteSb.toString(), orderStatus.getAdminUser());
				logger.debug("订单["+orderSns+"]订单返回正常单" + JSON.toJSONString(info));
			}
		}
		return info;
	}

    /**
     * 获取问题交货单列表
     * @param distributes
     * @return List<OrderDistribute>
     */
	private List<OrderDistribute> getOrderDistributeSn(List<OrderDistribute> distributes) {
        List<OrderDistribute> updateDistributes = new ArrayList<OrderDistribute>();
        for (OrderDistribute distribute : distributes) {
            if (distribute.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
                continue;
            }
            updateDistributes.add(distribute);
        }
        return updateDistributes;
    }

    /**
     * 处理交货单为正常单
     * @param orderSn
     */
	private void processOrderDistributeQuestion(String orderSn) {
        OrderDistribute updateDistribute = new OrderDistribute();
        // 订单返回正常单原因保存
        updateDistribute.setQuestionStatus(Constant.OI_QUESTION_STATUS_NORMAL);
        updateDistribute.setUpdateTime(new Date());
        updateDistribute.setOrderSn(orderSn);
        orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
    }
	
    /**
     * 处理订单返回正常单
     * @param masterOrderSn
     */
	private void processMasterOrderQuestion(String masterOrderSn) {
		// 返回正常单
		MasterOrderInfo updateMaster = new MasterOrderInfo();
		updateMaster.setQuestionStatus(Constant.OI_QUESTION_STATUS_NORMAL);
		updateMaster.setUpdateTime(new Date());
		updateMaster.setMasterOrderSn(masterOrderSn);
		// 更新订单状态
		masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
    }

    /**
     * 处理订单返回正常单后续处理
     * @param master
     * @param orderStatus
     */
    private void processMasterOrderNormalFollow(MasterOrderInfo master, OrderStatus orderStatus) {
	    String masterOrderSn = master.getMasterOrderSn();
        // 订单操作日志记录
        if (master.getSplitStatus().equals(Constant.SPLIT_STATUS_UNSPLITED)) {
            // 拆单处理
            orderDistributeJmsTemplate.send(new TextMessageCreator(masterOrderSn));
        } else if (master.getSplitStatus().equals(Constant.SPLIT_STATUS_SPLITED)) {
			if (master.getNeedSign() == 1 && master.getSignStatus() == 0) {
				// 需要等待签章
			} else {
				// 创建采购单
				OrderManagementRequest request = new OrderManagementRequest();
				request.setMasterOrderSn(masterOrderSn);
				request.setMessage("返回正常单-创建采购单");
				request.setActionUser(orderStatus.getAdminUser());
				request.setActionUserId(orderStatus.getAdminUserId());
				request.setErpOrderNo(master.getErpOrderNo());
				purchaseOrderService.purchaseOrderCreateByMaster(request);
			}
        }
    }

	/*@Override
	public ReturnInfo returnNormal(String orderSn, OrderStatus orderStatus) {
		logger.debug("returnNormal Method begin: orderSn=" + orderSn + ";orderStatus = " + orderStatus);
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (orderSn == null || orderSn.trim().isEmpty()) {
			logger.error("传入订单编号参数为空，不能进行返回正常单操作！");
			ri.setMessage("传入订单编号参数为空，不能进行返回正常单操作！");
			return ri;
		}
		String type = orderStatus.getType();
		if (StringUtil.isTrimEmpty(type)) {
			logger.error(orderSn + "传入操作类型为空，不能进行返回正常单操作！");
			ri.setMessage("传入操作类型为空，不能进行返回正常单操作！");
			return ri;
		}
		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if (null == distribute) {
			logger.error("没有取得订单" + orderSn + "信息!不能进行设为正常单操作!");
			ri.setMessage("没有取得订单" + orderSn + "信息!不能进行设为正常单操作!");
			return ri;
		}
		if (distribute.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
			logger.error("订单" + orderSn + "已处于正常单状态，不能进行设为正常单操作!");
			ri.setMessage("订单" + orderSn + "已处于正常单状态，不能进行设为正常单操作!");
			return ri;
		}
		MasterOrderInfo masterOrderInfo = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (null == masterOrderInfo) {
			logger.error("没有取得订单" + distribute.getMasterOrderSn() + "信息!不能进行设为正常单操作!");
			ri.setMessage("没有取得订单" + distribute.getMasterOrderSn() + "信息!不能进行设为正常单操作!");
			return ri;
		}
		boolean osFlg = false; // OS问题单处理标识
		boolean lackFlg = false; // 物流问题单处理标识
		List<OrderQuestionNew> orderQuestions = null;						// OS问题单
		List<OrderQuestionNew> lackOrderQuestions = null;					// 物流问题单列表
		List<OrderQuestionNew> depotQuestions = new ArrayList<OrderQuestionNew>();
		// 问题单判断
		OrderQuestionNewExample osExample = new OrderQuestionNewExample();
		osExample.or().andOrderSnEqualTo(orderSn).andQuestionTypeEqualTo(0);
		orderQuestions = distributeQuestionMapper.selectByExample(osExample);
		if (!StringUtil.isNotNullForList(orderQuestions)) {
			osFlg = true;
		}
		// 物流问题单判断
		OrderQuestionNewExample example = new OrderQuestionNewExample();
		example.or().andOrderSnEqualTo(orderSn).andQuestionTypeEqualTo(1);
		lackOrderQuestions = distributeQuestionMapper.selectByExample(example);

		// 物流问题单列表为空 同时 旧数据物流问题单列表为空
		if (!StringUtil.isNotNullForList(lackOrderQuestions)) {
			lackFlg = true;
		}
		Map<String, Object> monitorMap = new HashMap<String, Object>();
		monitorMap.put("orderId", orderSn);
		try {
			StringBuffer noteSb = new StringBuffer();
			 OS问题单处理 
			if ("0".equals(type) || "2".equals(type)) {
				osFlg = true;
			}
			 物流问题单处理 
			if ("1".equals(type) || "2".equals(type)) {
				lackFlg = true;
			}
			// 同步ERP订单状态
			MasterOrderInfoExtend extend = this.masterOrderInfoExtendMapper.selectByPrimaryKey(orderSn);
			if (osFlg && lackFlg) {
				if (OrderAttributeUtil.doERP(distribute, extend.getReviveStt())) {// 下发erp 废弃
					logger.debug("通知ERP订单转正常单" + orderSn);
					// 同步至ERP
					ErpWebserviceResultBean res = new ErpWebserviceResultBean(-1, "初始化");
					res = erpInterfaceProxy.UpdateIdtToQnOrToR(orderSn, 2, null);
					if (res.getCode() == 4) {
						logger.info(res.toString());
					} else if (res.getCode() == 0 || res.getCode() > 1) {
						// ERP返回信息:OS单号141217405721已出库,不允许转正常单
						String msg = "OS单号"+orderSn+"已出库";
						logger.error(res.toString());
						if (res.getMessage().indexOf(msg) == -1) {
							throw new Exception("同步至ERP" + res.toString());
						}
					}
					logger.debug(res.toString());
				}
			}
			 OS问题单处理 
			if ("0".equals(type) || "2".equals(type)) {
				depotQuestions.addAll(orderQuestions);
				// 问题单信息备份到orderQuestionDel中
				if (StringUtil.isNotNullForList(orderQuestions)) {
					deleteOrderQuestions(orderSn, orderQuestions, orderStatus.getMessage());
				}
				noteSb.append(" OS问题单返回正常单处理; ");
			}
			 物流问题单处理 
			if ("1".equals(type) || "2".equals(type)) {
				depotQuestions.addAll(lackOrderQuestions);
				if (StringUtil.isNotNullForList(lackOrderQuestions)) {
					deleteOrderQuestions(orderSn, lackOrderQuestions, orderStatus.getMessage());
				}
				noteSb.append(" 物流问题单返回正常单处理; ");
			}
			// 只有既不是物流问题单也不是OS问题单时 通知ERP
			if (osFlg && lackFlg) {
				distribute.setQuestionStatus(Constant.OI_QUESTION_STATUS_NORMAL);
				distribute.setQuestionTime(null);
				distribute.setUpdateTime(new Date());
				orderDistributeMapper.updateByPrimaryKey(distribute);
			}
			// 订单操作日志记录
			OrderAction action = orderActionService.createQrderAction(distribute);
			action.setActionNote(orderStatus.getMessage() + noteSb.toString());
			action.setActionUser(orderStatus.getAdminUser());
			orderActionService.saveOrderAction(action);
			ri.setIsOk(Constant.OS_YES);
			ri.setMessage("返回正常单操作成功!");
			logger.debug("返回正常单操作成功!");
			//统一监控记录接口
			
			biMonitorService.sendMonitorMessage(Constant.BUSINESS_MONITOR_ORDER_NORMAL, JSON.toJSONString(monitorMap));
			// 占用库存
			boolean erpFlag = OrderAttributeUtil.canErp(masterOrderInfo); // 下发erp，false不下发
			if (erpFlag) {
				// 占库存状态判断
				if ("0".equals(type) || "2".equals(type)) {
					 占库存 
					if (orderStatus.isSwitchFlag() && !OrderAttributeUtil.isPosOrder(distribute.getSource())) {
						String msg = "";
						try {
							// 调用库存占用处理模块   异常时不做处理
							StockResultBean resultBean = stockCenterService.occupy(orderSn, "0");
							if (resultBean.getCode() != 1) { // 占用库存出现异常或者失败
								msg = "返回正常单： 占用库存" + resultBean.getMsg();
								logger.error("返回正常单： 占用库存" + resultBean.getMsg());
							} else {
								msg = "返回正常单,占用库存成功!";
								logger.debug(msg);
							}
						} catch (Exception e) {
							msg = "返回正常单:占用库存异常," + e.getMessage();
							logger.error(msg, e);
						} finally {
							// 订单操作日志记录
							OrderAction orderAction  = orderActionService.createQrderAction(distribute);
							action.setActionNote(msg);
							action.setActionUser(orderStatus.getAdminUser());
							orderActionService.saveOrderAction(orderAction);
						}
					}
				}
			}
			// 未下发ERP时调用下发ERP方法 
			if (!OrderAttributeUtil.doERP(distribute, extend.getReviveStt())) {
				distributeSupplierService.distribute(orderSn);
			} else if (OrderAttributeUtil.doERP(distribute, extend.getReviveStt())
					&& distribute.getDepotStatus() == Constant.OI_DEPOT_STATUS_UNDEPOTED) {
				// 已下发未分仓订单，调用分配接口
				//针对部分缺货问题单和部分缺货问题单，在返回正常单的时候，调用ERP立即分配接口
				callERPToDepot(orderSn, orderStatus.getAdminUser(), distribute.getQuestionCodes());
			}
		} catch (Exception e) {
			logger.error("返回正常单异常：", e);
			String errorMsg = e.getMessage() == null ? "" : e.getMessage();
			ri.setMessage("返回正常单异常：" + errorMsg);
			// 记录操作日志异常信息
			OrderAction orderAction  = orderActionService.createQrderAction(distribute);
			orderAction.setActionNote("<font style=color:red;>返回正常单：异常信息" + errorMsg + "</font>");
			orderAction.setActionUser(orderStatus.getAdminUser());
			orderActionService.saveOrderAction(orderAction);
			// 业务监控异常监控
			monitorMap.put("orderException", errorMsg);
			biMonitorService.sendMonitorMessage(Constant.BUSINESS_MONITOR_ORDER_EXCEPTION, JSON.toJSONString(monitorMap));
		}
		
		return null;
	}*/

    /**
     * 删除交货单问题单
     * @param orderSn 交货单号
     * @return ReturnInfo
     */
	@Override
	public ReturnInfo deleteOrderQuestion(String orderSn) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		DistributeQuestionExample osExample = new DistributeQuestionExample();
		osExample.or().andOrderSnEqualTo(orderSn);
		List<DistributeQuestion> orderQuestions = distributeQuestionMapper.selectByExample(osExample);
		// 物流问题单列表为空 同时 旧数据物流问题单列表为空
		if (StringUtil.isListNull(orderQuestions)) {
			info.setMessage("订单问题单列表为空！");
			info.setIsOk(Constant.OS_YES);
			return info;
		}
		deleteOrderQuestions(orderSn, orderQuestions, Constant.OS_STRING_SYSTEM);
		info.setMessage("删除问题单成功！");
		info.setIsOk(Constant.OS_YES);
		return info;
	}

	/**
	 * 根据订单号删除问题单
	 * @param masterOrderSn 订单编码
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo deleteMasterOrderQuestion(String masterOrderSn) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		MasterOrderQuestionExample example = new MasterOrderQuestionExample();
		example.or().andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderQuestion> orderQuestions = masterOrderQuestionMapper.selectByExample(example);

		if (StringUtil.isListNull(orderQuestions)) {
			info.setMessage("订单问题单列表为空！");
			info.setIsOk(Constant.OS_YES);
			return info;
		}
		try {
			deleteMasterOrderQuestions(masterOrderSn, orderQuestions);
		} catch (Exception e) {
			logger.error("订单[" + masterOrderSn + "]删除问题单异常：" + e.getMessage(), e);
			info.setMessage("订单[" + masterOrderSn + "]删除问题单异常：" + e.getMessage());
			return info;
		}
		info.setMessage("删除问题单成功！");
		info.setIsOk(Constant.OS_YES);
		return info;
	}

	@Override
	public ReturnInfo advanceSaleClose(String shopCode, List<String> customCodes) {
		logger.info("订单店铺["+shopCode+"]customCodes:" + JSON.toJSONString(customCodes) + "预售关闭 begin");
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(shopCode)) {
			info.setMessage("[shopCode] 不能为空");
			return info;
		}
		if (StringUtil.isListNull(customCodes)) {
			info.setMessage("[customCodes] 不能为空");
			return info;
		}
		this.shopCode = shopCode;
		this.customCodes = customCodes;
		AdvanceSaleCloseThread closeThread = new AdvanceSaleCloseThread();
		closeThread.start();
		logger.info("订单店铺[" + shopCode + "]customCodes:" + JSON.toJSONString(customCodes) + "预售关闭 end");
		info.setIsOk(Constant.OS_YES);
		info.setMessage("订单预售关闭成功");
		return info;
	}

	class AdvanceSaleCloseThread extends Thread{
		@Override
		public void run() {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("shopCode", shopCode);
			param.put("list", customCodes);
			List<String> orderSns = orderDistributeDefineMapper.selectQuestionOrder(param);
			if (StringUtil.isListNull(orderSns)) {
				logger.info("订单店铺[" + shopCode + "]customCodes:" + JSON.toJSONString(customCodes) + "预售商品订单为空");
				return ;
			}
			for (String masterOrderSn : orderSns) {
				try {
					OrderStatus orderStatus = new OrderStatus(masterOrderSn, "预售关闭返回正常单", "ERP", "");
					orderStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.ERP);
					orderStatus.setType("0");
					orderNormalJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(orderStatus)));
				} catch (Exception e) {
					logger.error("订单[" + masterOrderSn + "]预售关闭写入MQ异常" + e.getMessage(), e);
					return ;
				}
			}
		}
	}

	/**
	 * 针对部分缺货问题单和部分缺货问题单，在返回正常单的时候，调用ERP立即分配接口
	 * @param orderSn
	 */
	private ReturnInfo callERPToDepot(String orderSn,String actionUser, List<String> depotQuestions){
		logger.debug("订单"+orderSn+"返回正常单时，调用ERP立即分配接口>>>>>>>begin");
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isListNull(depotQuestions)) {
			info.setMessage("返回正常单失败，问题单列表为空！");
			return info;
		}
		int count = 0;
		for (String code : depotQuestions) {
			if(StringUtil.equalsIgnoreCase("996", code)
					|| StringUtil.equalsIgnoreCase("995", code)
					|| StringUtil.equalsIgnoreCase("9951", code)
					|| StringUtil.equalsIgnoreCase("9952", code)
					|| StringUtil.equalsIgnoreCase("9953", code)){
				count ++;
			}
		}
		logger.debug("订单"+orderSn+"返回正常单时，调用ERP立即分配接口>>>>>>>Run.count:"+count);
		//调用ERP接口立即分配接口
		if(count > 0){
			String note = "返回正常单时，调用ERP立即分配接口成功.";
			String sfIdtCode = StringUtil.EMPTY;
			/*try {
				sfIdtCode = noticeDistributeService.getSfIdtCodeByOrderSn(orderSn);
			} catch (Exception e) {
				note = "返回正常单时，获取ERP编码失败.";
				logger.error("订单"+orderSn+"返回正常单时，调用ERP立即分配接口之前，获取ERP编码失败!"+e.getMessage(),e);
				info.setMessage("订单"+orderSn+"返回正常单时，调用ERP立即分配接口之前，获取ERP编码失败!"+e.getMessage());
				return info;
			}
			logger.debug("订单"+orderSn+"返回正常单时，调用ERP立即分配接口>>>>>>>Run.count:"+count+",sfIdtCode:"+sfIdtCode);
			try {
				noticeDistributeService.toErpShip(orderSn, sfIdtCode);
			} catch (Exception e) {
				note = "返回正常单时，调用ERP立即分配接口失败.";
				logger.error("订单"+orderSn+"返回正常单时，调用ERP立即分配接口失败!"+e.getMessage(),e);
				info.setMessage("订单"+orderSn+"返回正常单时，调用ERP立即分配接口失败!"+e.getMessage());
				return info;
			}
			distributeActionService.addOrderAction(orderSn, note, actionUser);*/
		}
		logger.debug("订单"+orderSn+"返回正常单时，调用ERP立即分配接口>>>>>>>end. ");
		info.setIsOk(Constant.OS_YES);
		info.setMessage("调用ERP立即分配接口成功！");
		return info;
	}

	/**
	 * 删除问题单列表（先备份问题单、问题单商品），然后移除
	 * @param orderSn
	 * @param questionNews
	 * @param message
	 */
	private void deleteOrderQuestions(String orderSn, List<DistributeQuestion> questionNews, String message) {

	    if (StringUtil.isListNull(questionNews)) {
	        return;
        }

        for (DistributeQuestion question : questionNews) {
            // 通知数据商品组转正常单
            DistributeQuestionDel del = new DistributeQuestionDel();
            del.setOrderSn(orderSn);
            del.setQuestionCode(question.getQuestionCode());
            del.setQuestionDesc(question.getQuestionDesc());
            del.setAddTime(question.getAddTime());
            del.setQuestionDesc(question.getQuestionDesc());
            del.setQuestionType(question.getQuestionType());
            del.setSupplierOrderSn(question.getSupplierOrderSn());
            this.distributeQuestionDelMapper.insertSelective(del);
            // 分配缺货问题单备份缺货商品数据
            if (question.getQuestionType() == Constant.QUESTION_TYPE_LACK) {
                OrderQuestionLackSkuNewExample lackSkuExample = new OrderQuestionLackSkuNewExample();
                lackSkuExample.or().andOrderSnEqualTo(orderSn).andQuestionCodeEqualTo(question.getQuestionCode());
                List<OrderQuestionLackSkuNew> lackSkus = orderQuestionLackSkuNewMapper.selectByExample(lackSkuExample);
                if (StringUtil.isListNotNull(lackSkus)) {
                    for (OrderQuestionLackSkuNew lackSku :lackSkus) {
                        OrderQuestionLackSkuNewDel lackSkuDel = colneOrderQuestionLackSku(lackSku);
                        orderQuestionLackSkuNewDelMapper.insertSelective(lackSkuDel);
                    }
                }
                OrderQuestionLackSkuNewExample skuNewExample = new OrderQuestionLackSkuNewExample();
                skuNewExample.or().andOrderSnEqualTo(orderSn).andQuestionCodeEqualTo(question.getQuestionCode());
                orderQuestionLackSkuNewMapper.deleteByExample(skuNewExample);
            }
            // 删除相应问题单信息
            DistributeQuestionKey newKey = new DistributeQuestionKey();
            newKey.setOrderSn(orderSn);
            newKey.setQuestionCode(question.getQuestionCode());
            distributeQuestionMapper.deleteByPrimaryKey(newKey);
        }

	}


	/**
	 * 删除问题单列表（先备份问题单、问题单商品），然后移除
	 * @param masterOrderSn 订单号
	 * @param orderQuestions 问题单列表
	 */
	private void deleteMasterOrderQuestions(String masterOrderSn, List<MasterOrderQuestion> orderQuestions) {
		if (orderQuestions == null || orderQuestions.size() == 0) {
			return;
		}

		for (MasterOrderQuestion question : orderQuestions) {
			// 删除相应问题单信息
			MasterOrderQuestionKey key = new MasterOrderQuestionKey();
			key.setMasterOrderSn(masterOrderSn);
			key.setQuestionCode(question.getQuestionCode());
			masterOrderQuestionMapper.deleteByPrimaryKey(key);
		}

	}
	
	private OrderQuestionLackSkuNewDel colneOrderQuestionLackSku(OrderQuestionLackSkuNew lackSku) {
		OrderQuestionLackSkuNewDel lackSkuDel = new OrderQuestionLackSkuNewDel();
		try {
			BeanUtils.copyProperties(lackSkuDel, lackSku);
		} catch (Exception e) {
			logger.error("复制OrderQuestionLackSku信息异常", e);
		}
		return lackSkuDel;
	}

    /**
     * 获取交货单号列表
     * @param distributes
     * @return List<String>
     */
	private List<String> getOrderSns(List<OrderDistribute> distributes) {
		List<String> orderSns = new ArrayList<String>();
		for (OrderDistribute distribute : distributes) {
			orderSns.add(distribute.getOrderSn());
		}
		return orderSns;
	}

	/**
	 * 判断交货单,获取问题单交货单列表
	 * @param distributes
	 * @return ReturnInfo<List<OrderDistribute>>
	 */
	private ReturnInfo<List<OrderDistribute>> checkDistributeListOrderNormal(List<OrderDistribute> distributes) {
		ReturnInfo<List<OrderDistribute>> info = new ReturnInfo<List<OrderDistribute>>(Constant.OS_NO);

		List<OrderDistribute> updateDistributes = new ArrayList<OrderDistribute>();
		for (OrderDistribute distribute : distributes) {
			String orderSn = distribute.getOrderSn();

			if (distribute.getQuestionStatus() == Constant.OI_QUESTION_STATUS_NORMAL) {
				info.setMessage(" 订单[" + orderSn + "]要处于正常单状态,不需要重复操作");
				continue;
			}
			updateDistributes.add(distribute);
		}
		info.setData(updateDistributes);
		info.setIsOk(Constant.OS_YES);
		return info;
	}
	
	public Map<Integer, Integer> getQuestionTypeMap(List<Integer> list) {
		Map<Integer, Integer> map = new HashMap<>(3);
		for (Integer integer : list) {
			map.put(integer, integer);
		}
		return map;
	}
}
