package com.work.shop.oms.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.order.service.*;
import com.work.shop.oms.utils.CommonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.channel.bean.OfflineStoreInfo;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.OrderManagementResponse;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.orderop.service.OrderDistributeOpService;
import com.work.shop.oms.orderop.service.OrderNormalService;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import com.work.shop.oms.payment.feign.PayService;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单管理服务
 * @author lemon
 */
@Service
public class OrderManagementServiceImpl implements OrderManagementService {
	
	private static final Logger logger = Logger.getLogger(OrderManagementServiceImpl.class);
	
	@Resource
	private MasterOrderInfoDetailMapper masterOrderInfoDetailMapper;
	@Resource
	private MasterOrderGoodsDetailMapper masterOrderGoodsDetailMapper;
	@Resource
	private OrderReturnGoodsDetailMapper orderReturnGoodsDetailMapper;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private OrderDistributeDetailMapper orderDistributeDetailMapper;
	@Resource
	private OrderDepotShipDetailMapper orderDepotShipDetailMapper;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private OrderReturnMapper orderReturnMapper;
	@Resource
	private MasterOrderPayTypeDetailMapper masterOrderPayTypeDetailMapper;
	@Resource
	private MasterOrderActionService masterOrderActionService;
	@Resource
	private MasterOrderActionMapper masterOrderActionMapper;
	@Resource
	private DistributeActionMapper distributeActionMapper;

	@Resource(name = "distributeShipService")
	private DistributeShipService distributeShipService;

	@Resource
	private SystemShippingMapper systemShippingMapper;

	@Resource(name = "orderDistributeOpService")
	private OrderDistributeOpService orderDistributeOpService;

	@Resource(name = "orderQueryServiceImpl")
	private OrderQueryService orderQueryService;
	@Resource
	private OrderNormalService orderNormalService;
	@Resource
	private OrderQuestionService orderQuestionService;

	@Resource
	private PayService payService;

	@Resource
	private OrderConfirmService orderConfirmService;
	@Resource
	private PurchaseOrderService purchaseOrderService;
	@Resource
	private OrderCommonService orderCommonService;

	@Resource
	private MasterOrderInfoExtendService masterOrderInfoExtendService;

    @Resource
    private MasterOrderInfoService masterOrderInfoService;

	/**
	 * 获取订单详情
	 * @param request 查询参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderItemGet(OrderManagementRequest request) {
		logger.info("订单详情查询 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("查询失败");
		String masterOrderSn = request.getMasterOrderSn();
		double returnSettleMoney = 0.00D;
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>(4);
			paramMap.put("masterOrderSn", masterOrderSn);
			paramMap.put("isHistory", 0);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail masterOrderInfo = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if (masterOrderInfo == null) {
				response.setMessage("查询订单数据不存在");
				return response;
			}

            OrderItemDetail itemDetail = new OrderItemDetail();
			cloneGoods(itemDetail, masterOrderInfo);
			response.setItemDetail(itemDetail);
			
			AdminUser adminUser = new AdminUser();
            String actionUserId = request.getActionUserId();
            if (actionUserId.length() > 9) {
                actionUserId = actionUserId.substring(0,9);
            }
            adminUser.setUserId(actionUserId);
			adminUser.setUserName(request.getActionUser());
			//获取配送信息
            List<OrderItemDepotDetail> depotDetails = orderDepotShipDetailMapper.getOrderItemDepotDetail(masterOrderSn);
            masterOrderInfo.setDepotDetails(depotDetails);
			OrderItemStatusUtils orderItemStatusUtils = new OrderItemStatusUtils(masterOrderInfo, adminUser);
			//获取已退货量
			OrderReturnExample orderReturnExample = new OrderReturnExample();
			orderReturnExample.or().andRelatingOrderSnEqualTo(masterOrderSn).andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY);
			List<OrderReturn> orderReturnList = orderReturnMapper.selectByExample(orderReturnExample);
			if (StringUtil.isListNotNull(orderReturnList)) {
				// 判断是退货单还是退款单
				List<String> returnSns = new ArrayList<String>();
				for (OrderReturn item : orderReturnList) {
					returnSns.add(item.getReturnSn());
					/*if (item.getReturnType() == Constant.OR_RETURN_TYPE_RETURN) {
						// 只能创建一次退单
						orderItemStatusUtils.setCreateReturn(0);
					}*/
					if (item.getReturnType() == Constant.OR_RETURN_TYPE_EXREFUND) {
						// 只能创建一次额外退款单
						orderItemStatusUtils.setAddExtra(0);
					}
				}
				response.setReturnSns(returnSns);
			}
			response.setOrderItemStatusUtils(orderItemStatusUtils);
			List<OrderItemGoodsDetail> itemList = masterOrderGoodsDetailMapper.getOrderGoodsDetail(masterOrderSn);
			
			//查询退单列表  用于计算退单金额和填充已退待退数量信息
			List<OrderReturnGoods> returnList = orderReturnGoodsDetailMapper.getReturnNumberByMasOrdSn(paramMap);
			//计算退单总金额
			if (returnList != null && returnList.size() > 0) {
				for (OrderReturnGoods returnBean : returnList) {
					returnSettleMoney = returnSettleMoney + returnBean.getGoodsReturnNumber() * returnBean.getSettlementPrice().doubleValue();
				}
			}
			//填充商品信息
			for (OrderItemGoodsDetail bean : itemList) {
				//给已退和待退数量赋值
				if (returnList != null && returnList.size() > 0) {
					for (OrderReturnGoods returnBean : returnList) {
						// sku, 商品类型, 仓库, 成交价
						if (bean.getCustomCode().equals(returnBean.getCustomCode())
								&& bean.getExtensionCode().equals(returnBean.getExtensionCode())
								&& bean.getDepotCode().equals(returnBean.getOsDepotCode())
								&& bean.getSettlementPrice().doubleValue() == returnBean.getSettlementPrice().doubleValue()) {
							// 0未入库 1已入库 2待入库
							if (returnBean.getCheckinStatus() == 0) {
								// 待退
								bean.setReturnRemainNum(returnBean.getGoodsReturnNumber().toString());
							} else if(returnBean.getCheckinStatus() == 1) {
								// 已退
								bean.setReturnNum(returnBean.getGoodsReturnNumber().toString());
							}
						}
					}
				}
				//给配送状态名称赋值
				bean.setShippingStatusName(getDepotShipStatusName(bean.getShippingStatus()));
            }
			response.setGoodsDetails(itemList);

			//获取配送信息
            List<OrderItemDepotInfo> orderItemDepotInfos = new ArrayList<OrderItemDepotInfo>();
			if (StringUtil.isListNotNull(depotDetails)) {
			    List<String> orderSns = new ArrayList<String>();
                for (OrderItemDepotDetail depotDetail : depotDetails) {
                    orderSns.add(depotDetail.getOrderSn());
                }

                Map<String, List<OrderItemGoodsDetail>> goodsMap = new HashMap<String, List<OrderItemGoodsDetail>>();
                List<OrderItemGoodsDetail> detailByOrder = masterOrderGoodsDetailMapper.getOrderGoodsDetailByOrder(orderSns);
                if (StringUtil.isListNotNull(detailByOrder)) {

                    for (OrderItemGoodsDetail detail : detailByOrder) {
                        String orderSn = detail.getOrderSn();
                        String invoiceNo = detail.getInvoiceNo();
                        String key = orderSn + "_" + invoiceNo;
                        List<OrderItemGoodsDetail> orderItemGoodsDetails = goodsMap.get(key);
                        if (StringUtil.isListNull(orderItemGoodsDetails)) {
                            orderItemGoodsDetails = new ArrayList<OrderItemGoodsDetail>();
                        }
                        orderItemGoodsDetails.add(detail);
                        goodsMap.put(key, orderItemGoodsDetails);
                    }

                }

                //交货单号分组交货单
                for (OrderItemDepotDetail depotDetail : depotDetails) {
                    String orderSn = depotDetail.getOrderSn();
                    String invoiceNo = depotDetail.getInvoiceNo();
                    String key = orderSn + "_" + invoiceNo;
                    depotDetail.setGoodsDetailList(goodsMap.get(key));
                }
                Map<String, List<OrderItemDepotDetail>> depotMap = new HashMap<String, List<OrderItemDepotDetail>>();
                for (OrderItemDepotDetail detail : depotDetails) {
                    String orderSn = detail.getOrderSn();
                    List<OrderItemDepotDetail> orderItemDepotDetails = depotMap.get(orderSn);
                    if (StringUtil.isListNull(orderItemDepotDetails)) {
                        orderItemDepotDetails = new ArrayList<OrderItemDepotDetail>();
                    }
                    orderItemDepotDetails.add(detail);
                    depotMap.put(orderSn, orderItemDepotDetails);
                }

                if (depotMap != null && depotMap.size() > 0) {
                    for (String orderSn : depotMap.keySet()) {
                        OrderItemDepotInfo depotInfo = new OrderItemDepotInfo();
                        depotInfo.setOrderSn(orderSn);
                        depotInfo.setDepotDetails(depotMap.get(orderSn));
                        orderItemDepotInfos.add(depotInfo);
                    }
                }

            }
			response.setDepotDetails(depotDetails);
            response.setOrderItemDepotInfos(orderItemDepotInfos);

			List<OrderItemAction> itemActions = new ArrayList<OrderItemAction>();
			
			MasterOrderActionExample actionExample = new MasterOrderActionExample();
			actionExample.setOrderByClause("action_id desc");
			actionExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<MasterOrderAction> actions = masterOrderActionMapper.selectByExampleWithBLOBs(actionExample);
			if (StringUtil.isListNotNull(actions)) {
				List<OrderItemActionDetail> actionDetails = new ArrayList<OrderItemActionDetail>();
                List<OrderItemActionDetail> commiteActionDetails = new ArrayList<OrderItemActionDetail>();
				for (MasterOrderAction action : actions) {
					OrderItemActionDetail actionDetail = new OrderItemActionDetail();
					cloneGoods(actionDetail, action);
					if (actionDetail.getLogType() == 0) {
                        actionDetails.add(actionDetail);
                    } else {
                        commiteActionDetails.add(actionDetail);
                    }

				}
				OrderItemAction itemAction = new OrderItemAction();
				itemAction.setName("订单日志");
				itemAction.setOrderSn(masterOrderSn);
				itemAction.setActionDetails(actionDetails);
                itemActions.add(itemAction);

                itemAction = new OrderItemAction();
                itemAction.setName("沟通");
                itemAction.setOrderSn(masterOrderSn);
                itemAction.setActionDetails(commiteActionDetails);
				itemActions.add(itemAction);
			}
			response.setItemActions(itemActions);
			List<OrderItemPayDetail> payDetails = masterOrderPayTypeDetailMapper.getOrderItemPayDetail(masterOrderSn);
			response.setPayDetails(payDetails);
            response.setSuccess(true);
            response.setMessage("查询成功");
		} catch (Exception e) {
			logger.error("订单[" + masterOrderSn + "]查询异常" + e.getMessage(), e);
			response.setMessage("订单查询异常" + e.getMessage());
		}
		return response;
	}

	/**
	 * 沟通
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse communicate(OrderManagementRequest request) {
		logger.info("订单沟通 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("订单沟通失败");
		masterOrderActionService.insertOrderActionBySn(request.getMasterOrderSn(), request.getMessage(), request.getActionUser(), 1);
		response.setSuccess(true);
		response.setMessage("订单沟通成功");
		return response;
	}

	/**
	 * 查询订单日志
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse getOrderItemLog(OrderManagementRequest request) {
		logger.info("查询订单日志request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("查询订单日志失败");
		String masterOrderSn = request.getMasterOrderSn();
		try {
			List<OrderItemAction> itemActions = new ArrayList<OrderItemAction>();
			MasterOrderActionExample actionExample = new MasterOrderActionExample();
			actionExample.setOrderByClause("action_id desc");
			actionExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<MasterOrderAction> actions = masterOrderActionMapper.selectByExampleWithBLOBs(actionExample);

            if (StringUtil.isListNotNull(actions)) {
                List<OrderItemActionDetail> actionDetails = new ArrayList<OrderItemActionDetail>();
                List<OrderItemActionDetail> commiteActionDetails = new ArrayList<OrderItemActionDetail>();
                for (MasterOrderAction action : actions) {
                    OrderItemActionDetail actionDetail = new OrderItemActionDetail();
                    cloneGoods(actionDetail, action);
                    if (actionDetail.getLogType() == 0) {
                        actionDetails.add(actionDetail);
                    } else {
                        commiteActionDetails.add(actionDetail);
                    }

                }
                OrderItemAction itemAction = new OrderItemAction();
                itemAction.setName("订单日志");
                itemAction.setOrderSn(masterOrderSn);
                itemAction.setActionType(1);
                itemAction.setActionDetails(actionDetails);
                itemActions.add(itemAction);

                itemAction = new OrderItemAction();
                itemAction.setName("沟通");
                itemAction.setActionType(2);
                itemAction.setOrderSn(masterOrderSn);
                itemAction.setActionDetails(commiteActionDetails);
                itemActions.add(itemAction);
            }

			OrderDistributeExample distributeExample = new OrderDistributeExample();
			distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
			if (StringUtil.isListNotNull(distributes)) {
				for (OrderDistribute distribute : distributes) {
					DistributeActionExample distributeActionExample = new DistributeActionExample();
					distributeActionExample.setOrderByClause("action_id desc");
					distributeActionExample.or().andOrderSnEqualTo(distribute.getOrderSn());
					List<DistributeAction> distributeActions = distributeActionMapper.selectByExampleWithBLOBs(distributeActionExample);
					if (StringUtil.isListNotNull(distributeActions)) {
						List<OrderItemActionDetail> actionDetails = new ArrayList<OrderItemActionDetail>();
						for (DistributeAction action : distributeActions) {
							OrderItemActionDetail actionDetail = new OrderItemActionDetail();
							cloneGoods(actionDetail, action);
							actionDetails.add(actionDetail);
						}
						
						OrderItemAction itemAction = new OrderItemAction();
						itemAction.setName("交货单" + distribute.getOrderSn() + "日志");
                        itemAction.setActionType(3);
						itemAction.setOrderSn(distribute.getOrderSn());
						itemAction.setActionDetails(actionDetails);
						itemActions.add(itemAction);
					}
				}
			}
			response.setItemActions(itemActions);
			response.setSuccess(true);
			response.setMessage("订单沟通成功");
		} catch (Exception e) {
			logger.error(masterOrderSn + "查询订单日志失败：" + e.getMessage(), e);
			response.setMessage("查询订单日志失败：" + e.getMessage());
		}
		return response;
	}
	
	/**
	 * 订单拣货
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderDistributePicking(OrderManagementRequest request) {
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("订单拣货设置失败");

        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		
		String orderSn = request.getMasterOrderSn();
		
		// 交货单号
		String shipSn = request.getShipSn();
		if (StringUtils.isBlank(shipSn)) {
			response.setMessage("订单交货单不能为空");
			return response;
		}
		
		try {
			DistributeShippingBean distributeShipBean = new DistributeShippingBean();
			distributeShipBean.setOrderSn(orderSn);
			distributeShipBean.setShipSn(shipSn);
			distributeShipBean.setDepotCode(request.getDepotCode());
			distributeShipBean.setActionUser(request.getActionUser());
			ReturnInfo<String> info = distributeShipService.processDistributePicking(distributeShipBean);
			if (info == null) {
				response.setMessage("订单拣货设置失败");
				return response;
			}
			if (info.getIsOk() == Constant.OS_NO) {
				response.setMessage("订单拣货设置失败：" + info.getMessage());
				return response;
			}
			response.setMessage("订单[" + orderSn + "]拣货设置成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setMessage("订单拣货设置失败：" + e.getMessage());
			logger.error("订单拣货设置失败：" + e.getMessage(), e);
		}
		return response;
	}
	
	/**
	 * 订单拣货完成
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderDistributePickUp(OrderManagementRequest request) {
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("订单拣货完成设置失败");

        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }

		String orderSn = request.getMasterOrderSn();

		// 交货单号
		String shipSn = request.getShipSn();
		if (StringUtils.isBlank(shipSn)) {
			// 根据订单编码和配货单编码，获取配货单信息
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			distributeExample.or().andMasterOrderSnEqualTo(orderSn);
			List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
			if (StringUtil.isListNull(distributes)) {
				response.setMessage("订单[" + orderSn + "]下没有交货单[" + orderSn + "]数据");
				return response;
			}

			OrderDistribute orderDistribute = distributes.get(0);
			shipSn = orderDistribute.getOrderSn();
		}
		
		try {
			DistributeShippingBean distributeShipBean = new DistributeShippingBean();
			distributeShipBean.setOrderSn(orderSn);
			distributeShipBean.setShipDate(new Date());
			distributeShipBean.setActionUser(request.getActionUser());
			distributeShipBean.setShipSn(shipSn);
			distributeShipBean.setDepotCode(request.getDepotCode());
			ReturnInfo<String> info = distributeShipService.processDistributePickUp(distributeShipBean);
			if (info == null) {
				response.setMessage("订单拣货完成设置失败");
				return response;
			}
			if (info.getIsOk() == Constant.OS_NO) {
				response.setMessage("订单拣货完成设置失败：" + info.getMessage());
				return response;
			}
			response.setMessage("订单[" + orderSn + "]拣货完成设置成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setMessage("订单拣货完成设置失败：" + e.getMessage());
			logger.error("订单拣货完成设置失败：" + e.getMessage(), e);
		}
		return response;
	}

	/**
	 * 订单取消通知接收
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderDistributeCancel(OrderManagementRequest request) {
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("订单取消通知接收失败");

        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }

		String orderSn = request.getMasterOrderSn();

		try {
			DistributeShippingBean distributeShipBean = new DistributeShippingBean();
			distributeShipBean.setOrderSn(orderSn);
			distributeShipBean.setActionUser(request.getActionUser());

			ReturnInfo<String> info = distributeShipService.processOrderDistributeCancel(distributeShipBean);
			if (info == null) {
				response.setMessage("订单取消通知接收失败");
				return response;
			}
			if (info.getIsOk() == Constant.OS_NO) {
				response.setMessage("订单取消通知接收失败：" + info.getMessage());
				return response;
			}
			response.setMessage("订单[" + orderSn + "]取消通知接收成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setMessage("订单取消通知接收失败：" + e.getMessage());
			logger.error("订单取消通知接收失败：" + e.getMessage(), e);
		}
		return response;
	}

	/**
	 * 订单自提码核销
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse gotWriteOff(OrderManagementRequest request) {
		logger.info("订单沟通 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("订单沟通失败");
		String gotCode = request.getGotCode();
		try {
			if (StringUtil.isTrimEmpty(gotCode)) {
				response.setMessage("自提码不能为空");
				return response;
			}
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			distributeExample.or().andGotCodeEqualTo(gotCode).andGotStatusEqualTo(Constant.GOT_STATUS_NO);
			List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
			if (distributes == null || distributes.size() == 0) {
				response.setMessage("自提码" + gotCode + "没有查询到对应订单，请确认后重新提交");
				return response;
			}
			DistributeShippingBean distributeShipBean = new DistributeShippingBean();
			distributeShipBean.setOrderSn(distributes.get(0).getMasterOrderSn());
			distributeShipBean.setShippingCode(Constant.SHIP_CODE_CAC);
			distributeShipBean.setInvoiceNo(gotCode);
			distributeShipBean.setShipDate(new Date());
			distributeShipBean.setActionUser(request.getActionUser());
			ReturnInfo<String> info = distributeShipService.cacWriteOff(distributeShipBean);
			if (info == null) {
				response.setMessage("自提核销失败");
				return response;
			}
			if (info.getIsOk() == Constant.OS_NO) {
				response.setMessage("自提核销失败：" + info.getMessage());
				return response;
			}
			response.setMessage("订单[" + distributes.get(0).getMasterOrderSn() + "]自提码[" + gotCode + "]自提核销成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setMessage("自提核销失败：" + e.getMessage());
			logger.error("自提核销失败：" + e.getMessage(), e);
		}
		return response;
	}

	/**
	 * 承运商列表
	 * @param request 请求参数
	 * @return OmsBaseResponse<List<ShippingInfo>>
	 */
	@Override
	public OmsBaseResponse<List<ShippingInfo>> getSystemShipping(OmsBaseRequest<ShippingInfo> request) {
		logger.info("查询承运商request:" + JSON.toJSONString(request));
		OmsBaseResponse<List<ShippingInfo>> response = new OmsBaseResponse<List<ShippingInfo>>();
		response.setSuccess(false);
		response.setMessage("查询承运商失败");
		List<ShippingInfo> shippingInfoList = new ArrayList<ShippingInfo>();
		try {
			if (request == null) {
				response.setMessage("参数不能为空");
				return response;
			}
			SystemShippingExample shippingExample = new SystemShippingExample();
			SystemShippingExample.Criteria criteria = shippingExample.or();
			if (request.getData() != null) {
				if (StringUtil.isNotEmpty(request.getData().getShippingCode())) {
					criteria.andShippingCodeEqualTo(request.getData().getShippingCode());
				}
				if (request.getData().getShippingId() != null) {
					criteria.andShippingIdEqualTo(request.getData().getShippingId());
				}
			}
			List<SystemShipping> shippingList = systemShippingMapper.selectByExample(shippingExample);
			if (StringUtil.isListNotNull(shippingList)) {
				for (SystemShipping shipping : shippingList) {
					ShippingInfo info = new ShippingInfo();
					cloneGoods(info, shipping);
					shippingInfoList.add(info);
				}
			}
			response.setData(shippingInfoList);
			response.setSuccess(true);
			response.setMessage("查询承运商成功");
		} catch (Exception e) {
			logger.error("查询承运商失败：" + e.getMessage(), e);
			response.setMessage("查询承运商失败：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 发送自提短信
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse sendGotCode(OrderManagementRequest request) {
		logger.info("发送自提短信request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("发送自提短信失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>(4);
			paramMap.put("masterOrderSn", masterOrderSn);
			paramMap.put("isHistory", 0);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if (master == null) {
				response.setMessage("查询订单数据不存在");
				return response;
			}
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(master.getMasterOrderSn());
			orderStatus.setAdminUser(request.getActionUser());
			OmsBaseRequest<OfflineStoreInfo> baseRequest = new OmsBaseRequest<OfflineStoreInfo>();
			OfflineStoreInfo storeInfo = new OfflineStoreInfo();
			storeInfo.setStoreCode(master.getStoreCode());
			baseRequest.setData(storeInfo);
			OmsBaseResponse<OfflineStoreInfo> baseResponse = orderQueryService.offlineStoreManagement(baseRequest);
			if (baseResponse == null || !baseResponse.getSuccess()) {
				response.setMessage("发送自提短信失败：" + (baseResponse == null ? "返回结果为空" : baseResponse.getMessage()));
				return response;
			}
			if (StringUtil.isListNull(baseResponse.getList())) {
				response.setMessage("发送自提短信失败：查到订单店铺" + master.getStoreCode() + "信息为空");
				return response;
			}
			String storeAddress = baseResponse.getList().get(0).getAddress();
			orderStatus.setStoreAddress(storeAddress);
			ReturnInfo<String> info = orderDistributeOpService.sendGotCode(orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("发送自提短信成功");
			} else {
				response.setMessage("发送自提短信失败：" + (info == null ? "返回结果为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error("发送自提短信失败：" + e.getMessage(), e);
			response.setMessage("发送自提短信失败：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 订单结算
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderSettlement(OrderManagementRequest request) {
		logger.info("订单结算request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("订单结算失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>(4);
			paramMap.put("masterOrderSn", masterOrderSn);
			paramMap.put("isHistory", 0);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if (master == null) {
				response.setMessage("查询订单数据不存在");
				return response;
			}
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(master.getMasterOrderSn());
			orderStatus.setAdminUser(request.getActionUser());
			ReturnInfo<String> info = orderDistributeOpService.settleOrder(masterOrderSn, orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单结算成功");
			} else {
				response.setMessage("订单结算失败：" + (info == null ? "返回结果为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单结算失败：" + e.getMessage(), e);
			response.setMessage("订单结算失败：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 设置正常单
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderNormal(OrderManagementRequest request) {
		logger.info("订单返回正常单 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("订单返回正常单失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>(4);
			paramMap.put("masterOrderSn", masterOrderSn);
			paramMap.put("isHistory", 0);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if (master == null) {
				response.setMessage("查询订单数据不存在");
				return response;
			}
			List<Integer> integers = new ArrayList<>();
			integers.add(0);
			integers.add(1);
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(master.getMasterOrderSn());
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setQuestionTypes(integers);
			orderStatus.setMessage(request.getMessage());
			ReturnInfo<String> info = orderNormalService.normalOrderByMasterSn(masterOrderSn, orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
                //返回正常单，订单推送供应链
                logger.info("返回正常单:" + masterOrderSn + "订单推送供应链");
                purchaseOrderService.pushJointPurchasing(masterOrderSn, request.getActionUser(), request.getActionUserId(), null, 0);
				response.setSuccess(true);
				response.setMessage("订单返回正常单成功");
			} else {
				response.setMessage("订单返回正常单失败：" + (info == null ? "返回结果为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单返回正常单失败：" + e.getMessage(), e);
			response.setMessage("订单返回正常单失败：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 设置问题单
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderQuestion(OrderManagementRequest request) {
		logger.info("订单设置问题单 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("设置问题单失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>(4);
			paramMap.put("masterOrderSn", masterOrderSn);
			paramMap.put("isHistory", 0);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if (master == null) {
				response.setMessage("查询订单数据不存在");
				return response;
			}
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(master.getMasterOrderSn());
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setCode(request.getReasonCode());
			orderStatus.setMessage(request.getMessage());
			ReturnInfo<String> info = orderQuestionService.questionOrderByMasterSn(masterOrderSn, orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单设置问题单成功");
			} else {
				response.setMessage("订单设置问题单失败：" + (info == null ? "返回为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单设置问题单失败：" + e.getMessage(), e);
			response.setMessage("订单设置问题单失败：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 订单未付款
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderUnPay(OrderManagementRequest request) {
		logger.info("订单未付款 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("未付款失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setPaySn(request.getPaySn());
			orderStatus.setSource("OMS");
			orderStatus.setMessage("未付款");
			orderStatus.setUserId(Integer.valueOf(request.getActionUserId()));
			ReturnInfo<String> info = payService.unPayStCh(orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单未付款成功");
			} else {
				response.setMessage("订单未付款失败：" + (info == null ? "返回为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单未付款失败：" + e.getMessage(), e);
			response.setMessage("订单未付款失败：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 设置订单已支付
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderPay(OrderManagementRequest request) {
		logger.info("订单已付款 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("已付款失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			//拼装接口入参
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setMasterOrderSn(masterOrderSn);
            orderStatus.setAdminUser(request.getActionUser());
            orderStatus.setPaySn(request.getPaySn());
            orderStatus.setSource("OMS");
            orderStatus.setPayCode(request.getPayCode());
            String message = "已付款";
            if (StringUtils.isNotBlank(request.getMessage())) {
                message = request.getMessage();
            }
            logger.info("订单已付款 payNote:" + message);
            orderStatus.setMessage(message);
            orderStatus.setUserId(Integer.valueOf(request.getActionUserId()));
			ReturnInfo<String> info = payService.payStCh(orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单已付款成功");
			} else {
				response.setMessage("订单已付款失败：" + (info == null ? "返回为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单已付款失败：" + e.getMessage(), e);
			response.setMessage("订单已付款失败：" + e.getMessage());
		}
		return response;
	}

    /**
     * 订单确认
     * @param request 请求参数
     * @return OrderManagementResponse
     */
	@Override
	public OrderManagementResponse orderConfirm(OrderManagementRequest request) {
		logger.info("订单确认 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("确认失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setMessage(request.getMessage());
			ReturnInfo<String> info = orderConfirmService.confirmOrderByMasterSn(masterOrderSn, orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单确认成功");
			} else {
				response.setMessage("订单确认失败：" + (info == null ? "返回为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单确认失败：" + e.getMessage(), e);
			response.setMessage("订单确认失败：" + e.getMessage());
		}
		return response;
	}
	
	@Override
	public OrderManagementResponse orderUnConfirm(OrderManagementRequest request) {
		logger.info("订单未确认 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("未确认失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setMessage(request.getMessage());
			ReturnInfo<String> info = orderConfirmService.unConfirmOrderByMasterSn(masterOrderSn, orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单未确认成功");
			} else {
				response.setMessage("订单未确认失败：" + (info == null ? "返回为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单未确认失败：" + e.getMessage(), e);
			response.setMessage("订单未确认失败：" + e.getMessage());
		}
		return response;
	}

    /**
     * 订单锁定
     * @param request 请求参数
     * @return OrderManagementResponse
     */
	@Override
	public OrderManagementResponse orderLock(OrderManagementRequest request) {
		logger.info("订单锁定 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("锁定失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setMessage("锁定");
			orderStatus.setUserId(Integer.valueOf(request.getActionUserId()));
			ReturnInfo<String> info = orderDistributeOpService.lockOrder(masterOrderSn, orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单锁定成功");
			} else {
				response.setMessage("订单锁定失败：" + (info == null ? "返回为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单锁定失败：" + e.getMessage(), e);
			response.setMessage("订单锁定失败：" + e.getMessage());
		}
		return response;
	}

	@Override
	public OrderManagementResponse orderUnLock(OrderManagementRequest request) {
		logger.info("订单解锁 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("解锁失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			//拼装接口入参
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setMessage("解锁");
			orderStatus.setUserId(Integer.valueOf(request.getActionUserId()));
			ReturnInfo<String> info = orderDistributeOpService.unLockOrder(masterOrderSn, orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单解锁成功");
			} else {
				response.setMessage("订单解锁失败：" + (info == null ? "返回为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单解锁失败：" + e.getMessage(), e);
			response.setMessage("订单解锁失败：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 合同签章完成
	 * @param request 请求参数
	 * @return OrderManagementResponse
	 */
	@Override
	public OrderManagementResponse orderSignCompleted(OrderManagementRequest request) {
		logger.info("合同签章完成 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("合同签章完成失败");
		String msg = checkCommonOrderManagementRequest(request);
		if (StringUtils.isNotBlank(msg)) {
			response.setMessage(msg);
			return response;
		}
		String masterOrderSn = request.getMasterOrderSn();
		try {
			List<Integer> integers = new ArrayList<>();
			// 问题单类型、待审核问题单
			integers.add(3);
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setAdminUserId(request.getActionUserId());
			orderStatus.setQuestionTypes(integers);
			orderStatus.setMessage(request.getMessage());

            ReturnInfo<String> info = null;
            //交货单直接更新采购单签章状态和合同号
            if (masterOrderSn.contains(Constant.ORDER_DISTRIBUTE_BEFORE)) {
                //更新采购单合同号
                info = updatePushSupplyChain(masterOrderSn, request.getContractNo());

            } else {
                //订单号返回正常单
                info = orderNormalService.normalOrderByMasterSn(masterOrderSn, orderStatus);
                if (info != null && Constant.OS_YES == info.getIsOk()) {
                    // 变更合同签章状态为
                    updateSignStatus(masterOrderSn, request.getContractNo());
                }
            }

			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("合同签章完成成功");
			} else {
				response.setMessage("合同签章完成失败：" + (info == null ? "返回结果为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "合同签章完成失败：" + e.getMessage(), e);
			response.setMessage("合同签章完成失败：" + e.getMessage());
		}
		return response;
	}

    /**
     * 采购单更新合同号和签章状态
     * @param masterOrderSn
     * @param contractNo
     * @return
     */
    private ReturnInfo<String> updatePushSupplyChain(String masterOrderSn, String contractNo) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderCode(masterOrderSn);
        purchaseOrder.setSignComplete((byte) 1);
        purchaseOrder.setContractNo(contractNo);
        purchaseOrder.setSignCompleteTime(new Date());
        return purchaseOrderService.updatePushSupplyChain(purchaseOrder);
    }

    /**
     * 判断是否需要审核
     * @param masterOrderInfo
     * @return boolean
     */
	private boolean checkNeedAudit(MasterOrderInfo masterOrderInfo) {
        Integer needAudit = masterOrderInfo.getNeedAudit();
        if (needAudit == null) {
            needAudit = 0;
        }
        if (needAudit == 0) {
            return true;
        }

        if (needAudit == 1) {
            Integer auditStatus = masterOrderInfo.getAuditStatus();
            if (auditStatus == null) {
                auditStatus = 0;
            }
            if (auditStatus == 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * 订单审核通过
     * @param request 请求参数
     * @return
     */
	@Override
	public OrderManagementResponse orderReviewCompleted(OrderManagementRequest request) {
		logger.info("订单审单完成 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("订单审单完成失败");
        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
            MasterOrderInfo masterOrderInfo = masterOrderInfoService.getOrderInfoBySn(masterOrderSn);
            if (checkNeedAudit(masterOrderInfo)) {
                response.setSuccess(true);
                response.setMessage("订单审单完成成功");
                return response;
            }

			List<Integer> integers = new ArrayList<>();
			// 问题单类型、待审核问题单
			integers.add(2);
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setAdminUserId(request.getActionUserId());
			orderStatus.setQuestionTypes(integers);
			orderStatus.setMessage(request.getMessage());
			ReturnInfo<String> info = orderNormalService.normalOrderByMasterSn(masterOrderSn, orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单审单完成成功");
                // 变更审单状态为已审核
                updateAuditStatus(masterOrderSn);
				//账期支付填充最后支付时间
				masterOrderInfoExtendService.fillPayLastDate(masterOrderSn, new Date());
                // 是否账期支付, 0期立即扣款
                masterOrderInfoService.processOrderPayPeriod(masterOrderSn);

                //订单推送供应链
                logger.info("订单审核成功:" + masterOrderSn + "订单推送供应链");
                purchaseOrderService.pushJointPurchasing(masterOrderSn, request.getActionUser(), request.getActionUserId(), null, 0);
                // 需要合同签章的，先设置问题单
                if (masterOrderInfo.getNeedSign() == 1 && masterOrderInfo.getSignStatus() == 0) {
                    orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "待签章问题单", Constant.QUESTION_CODE_SIGN));
                }
			} else {
				response.setMessage("订单审单完成失败：" + (info == null ? "返回结果为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单审单完成失败：" + e.getMessage(), e);
			response.setMessage("订单审单完成失败：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 订单审批驳回
	 * @param request 请求参数
	 * @return
	 */
    @SuppressWarnings("unchecked")
	@Override
	public OrderManagementResponse orderReviewReject(OrderManagementRequest request) {
		logger.info("订单审单驳回 request:" + JSON.toJSONString(request));
		OrderManagementResponse response = new OrderManagementResponse();
		response.setSuccess(false);
		response.setMessage("订单审单驳回失败");

		String msg = checkCommonOrderManagementRequest(request);
		if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }
		String masterOrderSn = request.getMasterOrderSn();
		try {
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setMasterOrderSn(masterOrderSn);
			orderStatus.setAdminUser(request.getActionUser());
			orderStatus.setMessage("订单驳回,备注：" + request.getMessage());
			// 不创建退单
			orderStatus.setType("1");
			// 取消原因
			orderStatus.setCode("8011");
			ReturnInfo<String> info = orderCommonService.cancelOrderByMasterSn(masterOrderSn, orderStatus);
			if (info != null && Constant.OS_YES == info.getIsOk()) {
				response.setSuccess(true);
				response.setMessage("订单审单驳回成功");
				// 取消成功后记录日志
				masterOrderActionService.insertOrderActionBySn(masterOrderSn, "订单驳回取消订单,备注：" + request.getMessage(), request.getActionUser());
			} else {
				response.setMessage("订单审单驳回失败：" + (info == null ? "返回结果为空" : info.getMessage()));
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "订单审单驳回失败：" + e.getMessage(), e);
			response.setMessage("订单审单驳回失败：" + e.getMessage());
		}
		return response;
	}

    /**
     * 账期支付扣款成功
     * @param request
     * @return OrderManagementResponse
     */
    @Override
	public OrderManagementResponse orderPayPeriodSuccess(OrderManagementRequest request) {
        OrderManagementResponse response = new OrderManagementResponse();
        response.setSuccess(false);
        response.setMessage("账期支付扣款成功状态处理失败");

        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }

        String masterOrderSn = request.getMasterOrderSn();

        try {
            MasterOrderInfoExample masterOrderInfoExample = new MasterOrderInfoExample();
            masterOrderInfoExample.or().andMasterOrderSnEqualTo(masterOrderSn);
            List<MasterOrderInfo> masterOrderInfoList = masterOrderInfoMapper.selectByExample(masterOrderInfoExample);
            if (null == masterOrderInfoList || masterOrderInfoList.size() != 1) {
                response.setMessage("订单信息异常！");
                return response;
            }

            List<MasterOrderInfoExtend> infoExtends = masterOrderInfoExtendService.getMasterOrderInfoExtendByOrder(masterOrderSn);
            if (infoExtends == null || infoExtends.size() <1) {
                response.setMessage("订单信息异常");
                return response;
            }

            String message = "更新账期支付扣款成功状态异常";
            // 成功
            boolean payStatus = masterOrderInfoExtendService.updateMasterPayPeriod(masterOrderSn);
            if (payStatus) {
                message = "账期支付扣款成功";
                response.setSuccess(true);
                response.setMessage("操作成功");
            }
            masterOrderActionService.insertOrderActionBySn(masterOrderSn, message, request.getActionUser());
        } catch (Exception e) {
            logger.error(masterOrderSn + "账期支付扣款状态失败：" + e.getMessage(), e);
            response.setMessage("账期支付扣款状态失败：" + e.getMessage());
        }

        return response;
    }

    /**
     * 账期支付扣款失败
     * @param request
     * @return OrderManagementResponse
     */
    @Override
    public OrderManagementResponse orderPayPeriodError(OrderManagementRequest request) {
        OrderManagementResponse response = new OrderManagementResponse();
        response.setSuccess(false);
        response.setMessage("账期支付扣款失败处理失败");

        String msg = checkCommonOrderManagementRequest(request);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
            return response;
        }

        String masterOrderSn = request.getMasterOrderSn();

        try {
            String note = "账期支付扣款失败处理！错误信息:" + request.getMessage();
            masterOrderActionService.insertOrderActionBySn(masterOrderSn, note, Constant.OS_STRING_SYSTEM);
            // 处理失败，设置问题单
            //orderQuestionService.questionOrderByMasterSn(masterOrderSn, new OrderStatus(masterOrderSn, "账期支付扣款异常", "9980"));
            response.setSuccess(true);
            response.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(masterOrderSn + "账期支付扣款失败处理异常：" + e.getMessage(), e);
            response.setMessage("账期支付扣款失败处理异常：" + e.getMessage());
        }

        return response;
    }

    /**
     * 判断公共订单参数
     * @param request
     * @return String
     */
    private String checkCommonOrderManagementRequest(OrderManagementRequest request) {
	    String msg = null;

        if (request == null) {
            msg = "参数不能为空";
            return msg;
        }

        if (StringUtil.isTrimEmpty(request.getMasterOrderSn())) {
            msg = "订单编码为空";
            return msg;
        }

        return msg;
    }

	/**
	 * 获取仓库发货状态
	 * @param shippingStatus 发货状态
	 * @return String
	 */
	private String getDepotShipStatusName(Byte shippingStatus){
		String returnValue = "";
		if (shippingStatus != null) {
			if (shippingStatus == Constant.OS_SHIPPING_STATUS_UNSHIPPED) {
				returnValue = "未发货";
			} else if(shippingStatus == Constant.OS_SHIPPING_STATUS_SHIPPED) {
				returnValue = "已发货";
			} else if(shippingStatus == Constant.OS_SHIPPING_STATUS_RECEIVED) {
				returnValue = "已收货";
			} else if(shippingStatus == Constant.OS_SHIPPING_STATUS_CONFIRM) {
				returnValue = "客户签收";
			}
		}
		return returnValue;
	}
	
	private void cloneGoods(Object destObj, Object origObj) {
		MasterOrderGoods og = new MasterOrderGoods();
		try {
			BeanUtils.copyProperties(destObj, origObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取退单类型
	 * @param returnType
	 * @return
	 */
	private String getReturnTypeName(int returnType) {
		String returnTypeName = "";
		if (returnType == Constant.OR_RETURN_TYPE_RETURN) {
			returnTypeName = "退货单";
		}else if(returnType == Constant.OR_RETURN_TYPE_REJECT) {
			returnTypeName = "拒收入库单";
		}else if(returnType == Constant.OR_RETURN_TYPE_REFUND) {
			returnTypeName = "普通退款单";
		}else if(returnType == Constant.OR_RETURN_TYPE_EXREFUND) {
			returnTypeName = "额外退款单";
		}else if(returnType == Constant.OR_RETURN_TYPE_LOSERETURN) {
			returnTypeName = "失货退货单";
		}
		return returnTypeName;
	}

    /**
     * 更新订单审核状态标志
     * @param masterOrderSn
     */
	private void updateAuditStatus(String masterOrderSn) {
		MasterOrderInfo updateOrder = new MasterOrderInfo();
		updateOrder.setMasterOrderSn(masterOrderSn);
		updateOrder.setUpdateTime(new Date());
		// 已审核
		updateOrder.setAuditStatus(1);
		masterOrderInfoMapper.updateByPrimaryKeySelective(updateOrder);
	}

    /**
     * 更新订单合同签章状态标志
     * @param masterOrderSn
     */
    private void updateSignStatus(String masterOrderSn, String contractNo) {
        MasterOrderInfo updateOrder = new MasterOrderInfo();
        updateOrder.setMasterOrderSn(masterOrderSn);
        updateOrder.setUpdateTime(new Date());
        // 已签章
        updateOrder.setSignStatus(1);
        updateOrder.setSignCompleteTime(new Date());
        updateOrder.setSignContractNum(contractNo);
        masterOrderInfoMapper.updateByPrimaryKeySelective(updateOrder);
    }


}
