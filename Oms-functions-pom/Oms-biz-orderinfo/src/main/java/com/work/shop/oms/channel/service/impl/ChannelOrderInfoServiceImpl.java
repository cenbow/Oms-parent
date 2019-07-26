package com.work.shop.oms.channel.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.work.shop.oms.api.bean.*;
import com.work.shop.oms.api.orderinfo.service.BGOrderInfoService;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.order.service.GoodsReturnChangeService;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.SystemRegionAreaExample.Criteria;
import com.work.shop.oms.config.service.OrderPeriodDetailService;
import com.work.shop.oms.dao.define.DefineOrderMapper;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.Constant;

/**
 * 渠道订单服务
 * @author QuYachu
 */
@Service("bgOrderInfoService")
public class ChannelOrderInfoServiceImpl implements BGOrderInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private DefineOrderMapper defineOrderMapper;
	
	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
	
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	
	@Resource
	private MasterOrderActionService masterOrderActionService;
	
	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;
	
	@Resource
	private MergeOrderPayMapper mergeOrderPayMapper;
	
	@Resource
	private OrderCancelService orderCancelService;
	
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;
	
	@Resource
	private OrderPeriodDetailService orderPeriodDetailService;
	
	@Resource
	private RedisClient redisClient;

	@Resource
	private OrderReturnMapper orderReturnMapper;

	@Resource
	private OrderReturnActionMapper orderReturnActionMapper;

	@Resource
	private GoodsReturnChangeMapper goodsReturnChangeMapper;

	@Resource
	private OrderReturnGoodsMapper orderReturnGoodsMapper;

	/**
	 * 判断查询订单列表参数
	 * @param searchParam 查询参数
	 * @return ApiReturnData
	 */
	private ApiReturnData checkOrderPageListParam(PageListParam searchParam) {
		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk(Constant.OS_STR_YES);

		if (StringUtils.isBlank(searchParam.getUserId())) {
			apiReturnData.setMessage("userId不能为空！");
			return apiReturnData;
		}
		if (searchParam.getPageSize() <= 0) {
			apiReturnData.setMessage("PageSize不能为空！");
			return apiReturnData;
		}
		if (searchParam.getPageNum() <= 0) {
			apiReturnData.setMessage("PageNum不能为空！");
			return apiReturnData;
		}
		if (StringUtil.isTrimEmpty(searchParam.getSiteCode())) {
			apiReturnData.setMessage("siteCode不能为空！");
			return apiReturnData;
		}

		return apiReturnData;
	}

	/**
	 * 设置订单列表查询参数
	 * @param searchParam 参数参数
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getOrderPageSearchParam(PageListParam searchParam) {
		Map<String, Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
		params.put("isHistory", searchParam.getIsHistory());
		params.put("userId", searchParam.getUserId());
		params.put("type", searchParam.getRstatus());
		params.put("start", (searchParam.getPageNum()-1) * searchParam.getPageSize());
		params.put("pageSize", searchParam.getPageSize());
		params.put("siteCode", searchParam.getSiteCode());
		if (StringUtil.isNotEmpty(searchParam.getOrderSn())) {
			params.put("orderSn",searchParam.getOrderSn());
		}
		if (StringUtil.isNotEmpty(searchParam.getGoodsSn())) {
			params.put("goodsSn",searchParam.getGoodsSn());
		}
		if(StringUtil.isNotEmpty(searchParam.getShopCode())){
			params.put("shopCode", searchParam.getShopCode());
		}
		params.put("from", searchParam.getFrom());

		//按天数查询
        Integer selectTimeDays = searchParam.getSelectTimeDays();
        if (selectTimeDays != null) {
            String endTime = TimeUtil.getDate(TimeUtil.YYYY_MM_DD);
            params.put("endTime", endTime);
            String startTime = null;
            startTime = TimeUtil.getBeforeDay(endTime, selectTimeDays-1, TimeUtil.YYYY_MM_DD);
            params.put("startTime", startTime);
        } else {
            //按时间查询
            if (searchParam.getStartTime() != null) {
                params.put("startTime", searchParam.getStartTime());
            }
            if (searchParam.getEndTime() != null) {
                params.put("endTime", searchParam.getEndTime());
            }
        }

        if (StringUtil.isListNotNull(searchParam.getOrderSns())) {
            params.put("orderSns", searchParam.getOrderSns());
        }

        // 合同编码
		if (StringUtils.isNotBlank(searchParam.getContractNum())) {
			params.put("contractNum", searchParam.getContractNum());
		}

		// 物料编码
		if (StringUtils.isNotBlank(searchParam.getMaterielNo())) {
			params.put("materielNo", searchParam.getMaterielNo());
		}

		return params;
	}

	/**
	 * 订单列表查询
	 * @param searchParam 查询对象
	 * @return ApiReturnData<Paging<OrderPageInfo>>
	 */
	@Override
	public ApiReturnData<Paging<OrderPageInfo>> orderPageList(PageListParam searchParam) {
		logger.info("查询用户订单列表：searchParam"+JSON.toJSONString(searchParam));
		ApiReturnData<Paging<OrderPageInfo>> apiReturnData = new ApiReturnData<Paging<OrderPageInfo>>();

		try {
			// 参数判断
			apiReturnData = checkOrderPageListParam(searchParam);
			if (Constant.OS_STR_NO.equals(apiReturnData.getIsOk())) {
				return apiReturnData;
			}
			apiReturnData.setIsOk(Constant.OS_STR_NO);

			if (StringUtils.isBlank(searchParam.getIsHistory())) {
				searchParam.setIsHistory("0");
			}

			// 获取参数参数
			Map<String, Object> params = getOrderPageSearchParam(searchParam);

			Paging paging = new Paging();
			paging.setTotalProperty(defineOrderMapper.selectUserOrderInfoCount(params));
			List<OrderPageInfo> pageList = defineOrderMapper.selectUserOrderInfo(params);

			for (OrderPageInfo orderPageInfo : pageList) {
				// 未支付订单
				if (orderPageInfo.getTotalOrderStatus() == 1) {
					String orderCancelTime = getOrderCancelTime(orderPageInfo.getOrderCreateTime());
					orderPageInfo.setOrderCancelTime(orderCancelTime);
				}
				
				Map<String, Object> returnGoodsParams = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
				returnGoodsParams.put("orderSn", orderPageInfo.getOrderSn());
				Integer goodsType = defineOrderMapper.selectGoodsType(returnGoodsParams);
				Integer returnGoodsType = defineOrderMapper.selectReturnGoodsType(returnGoodsParams);
				orderPageInfo.setGoodsType(goodsType == null ? 0 : goodsType);
				orderPageInfo.setReturnGoodsType(returnGoodsType == null ? 0 : returnGoodsType);
				int goodsCount = 0;
				if ("APP".equals(searchParam.getFrom())) {
					Map<String, Object> goodsParams = new HashMap<String, Object>(4);
					goodsParams.put("masterOrderSn", orderPageInfo.getOrderSn());
					goodsParams.put("isHistory", searchParam.getIsHistory());
					List<OrderGoodsInfo> orderGoodsList = defineOrderMapper.selectOrderGoodsInfo(goodsParams);
					for (OrderGoodsInfo orderGoodsInfo : orderGoodsList) {
						goodsCount += orderGoodsInfo.getGoodsNum();
					}
					setGoodsInfo(orderGoodsList);
					orderPageInfo.setOrderGoodsInfo(orderGoodsList);
				} else {
					Map<String, Object> shipParams=new HashMap<String, Object>(4);
					shipParams.put("orderSn", orderPageInfo.getOrderSn());
					shipParams.put("isHistory", searchParam.getIsHistory());
					List<OrderShipInfo> shipList = defineOrderMapper.selectOrderShipInfo(shipParams);
					//未拆单时商品查询
					if (shipList == null || shipList.size() == 0 || shipList.get(0) == null) {
						Map<String, Object> goodsParams = new HashMap<String, Object>(4);
						goodsParams.put("masterOrderSn",orderPageInfo.getOrderSn());
						goodsParams.put("isHistory", searchParam.getIsHistory());
						List<OrderGoodsInfo> orderGoodsList = defineOrderMapper.selectOrderGoodsInfo(goodsParams);
						setGoodsInfo(orderGoodsList);
						for (OrderGoodsInfo orderGoodsInfo : orderGoodsList) {
							goodsCount += orderGoodsInfo.getGoodsNum();
						}
						OrderShipInfo orderShipInfo = new OrderShipInfo();
						orderShipInfo.setOrderGoodsInfo(orderGoodsList);
						shipList.add(orderShipInfo);
					} else {
						//已拆单时商品查询
						for (OrderShipInfo orderShip : shipList) {
							Map<String, Object> goodsParams = new HashMap<String, Object>(6);
							goodsParams.put("orderSn",orderShip.getOrderSn());
							if (StringUtils.isNotBlank(orderShip.getInvoiceNo())) {
                                goodsParams.put("invoiceNo",orderShip.getInvoiceNo());
                            }
							goodsParams.put("depotCode",orderShip.getDepotCode());
							goodsParams.put("isHistory", searchParam.getIsHistory());
							List<OrderGoodsInfo> orderGoodsList = defineOrderMapper.selectOrderGoodsInfo(goodsParams);
							for (OrderGoodsInfo orderGoodsInfo : orderGoodsList) {
								goodsCount += orderGoodsInfo.getGoodsNum();
							}
							setGoodsInfo(orderGoodsList);
							orderShip.setOrderGoodsInfo(orderGoodsList);
						}
						//合并相同快递单号的物流信息
						shipList = mergeSameInvoiceNo(shipList);
					}
					orderPageInfo.setOrderShipInfo(shipList);
				}
				orderPageInfo.setGoodsCount(goodsCount);
			}
			apiReturnData.setIsOk(Constant.OS_STR_YES);
			paging.setRoot(pageList);
			apiReturnData.setData(paging);
		} catch (Exception e) {

			logger.error("平台查询订单列表异常：searchParam=" + searchParam, e);
			apiReturnData.setMessage("平台查询订单列表异常" + e.toString());
			apiReturnData.setIsOk(Constant.OS_STR_NO);
		}
		return apiReturnData;
	}

	/**
	 * 查询订单退单列表
	 * @param searchParam 查询参数
	 * @return ApiReturnData
	 */
	@Override
	public ApiReturnData<Paging<OrderReturnPageInfo>> orderReturnPageList(PageListParam searchParam) {
		logger.info("查询用户退单订单列表：searchParam" + JSON.toJSONString(searchParam));
		ApiReturnData<Paging<OrderReturnPageInfo>> apiReturnData = new ApiReturnData<Paging<OrderReturnPageInfo>>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);

		try {
			if (StringUtils.isBlank(searchParam.getUserId())) {
				apiReturnData.setMessage("userId不能为空！");
				return apiReturnData;
			}
			if (searchParam.getPageSize() <= 0) {
				apiReturnData.setMessage("PageSize不能为空！");
				return apiReturnData;
			}
			if (searchParam.getPageNum() <= 0) {
				apiReturnData.setMessage("PageNum不能为空！");
				return apiReturnData;
			}
			if (StringUtil.isTrimEmpty(searchParam.getSiteCode())) {
				apiReturnData.setMessage("siteCode不能为空！");
				return apiReturnData;
			}

			Map<String, Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
			params.put("userId", searchParam.getUserId());
			params.put("type",searchParam.getRstatus());
			params.put("start",(searchParam.getPageNum()-1) * searchParam.getPageSize());
			params.put("pageSize", searchParam.getPageSize());
			params.put("siteCode", searchParam.getSiteCode());
			if (StringUtil.isNotEmpty(searchParam.getShopCode())) {
				params.put("shopCode", searchParam.getShopCode());
			}
			if (StringUtils.isNotBlank(searchParam.getOrderReturnSn())) {
                params.put("returnSn", searchParam.getOrderReturnSn());
            }
            if (StringUtils.isNotBlank(searchParam.getOrderSn())) {
                params.put("orderSn", searchParam.getOrderSn());
            }

            //校验处理状态（0为待处理；1.退货中；2.退货成功；3.退款成功）
            Integer returnProcessStatus = searchParam.getReturnProcessStatus();
            if (returnProcessStatus != null) {
                params.put("returnProcessStatus", returnProcessStatus);
            }


            List<OrderReturnPageInfo> pageList = defineOrderMapper.selectUserOrderReturnInfo(params);
			Paging<OrderReturnPageInfo> paging = new Paging<OrderReturnPageInfo>();
			paging.setTotalProperty(defineOrderMapper.selectUserOrderReturnInfoCount(params));
			for (OrderReturnPageInfo orderReturnPageInfo : pageList) {
				Map<String, Object> goodsParams = new HashMap<String, Object>(2);
				goodsParams.put("orderReturnSn", orderReturnPageInfo.getOrderReturnSn());
				List<OrderReturnGoodsInfo> orderGoodsList = defineOrderMapper.selectOrderReturnGoodsInfo(goodsParams);
				setReturnGoodsInfo(orderGoodsList);
				orderReturnPageInfo.setOrderGoodsInfo(orderGoodsList);
			}
			apiReturnData.setIsOk(Constant.OS_STR_YES);
			paging.setRoot(pageList);
			apiReturnData.setData(paging);
		}catch (Exception e) {
			logger.info("平台退单列表列表异常：searchParam="+searchParam, e);
			logger.error("平台退单列表列表异常：searchParam="+searchParam, e);
			apiReturnData.setMessage("平台退单列表列表异常"+e.toString());
		}
		return apiReturnData;
	}

	/**
	 * 查询退单详情
	 * @param orderReturnSn 退单编码
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<OrderReturnDetailInfo>
	 */
	@Override
	public ApiReturnData<OrderReturnDetailInfo> orderReturnDetail(String orderReturnSn, String userId, String siteCode) {
		logger.info("平台查询退单详情：orderReturnSn:" + orderReturnSn);
		ApiReturnData<OrderReturnDetailInfo> apiReturnData = new ApiReturnData<OrderReturnDetailInfo>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		try {
			Map<String, Object> params = new HashMap<String, Object>(2);
			params.put("orderReturnSn", orderReturnSn);
			OrderReturnDetailInfo orderReturnDetailInfo = defineOrderMapper.selectOrderReturnDetailInfo(params);
			if (orderReturnDetailInfo == null) {
				apiReturnData.setMessage("没有相应的退单消息！");
				return apiReturnData;
			}
			//流程状态 1 => '申请退货',   2 => '收货', 3 => '商品质检',  4 => '退货完成',
			if (orderReturnDetailInfo.getPayStatus() == 1) {
				orderReturnDetailInfo.setProgressStatus(4);
			} else if (orderReturnDetailInfo.getQualityTime() != null && orderReturnDetailInfo.getReceiveTime() != null) {
				orderReturnDetailInfo.setProgressStatus(3);
			} else if (orderReturnDetailInfo.getQualityTime() == null && orderReturnDetailInfo.getReceiveTime() != null) {
				orderReturnDetailInfo.setProgressStatus(2);
			} else if(orderReturnDetailInfo.getApplyTime() != null && orderReturnDetailInfo.getQualityTime() == null && orderReturnDetailInfo.getReceiveTime() == null) {
				orderReturnDetailInfo.setProgressStatus(1);
			}
			Map<String, Object> orderReturnParams = new HashMap<String, Object>(3);
			orderReturnParams.put("orderReturnSn", orderReturnSn);
			List<OrderReturnGoodsInfo> orderReturnGoodsList = defineOrderMapper.selectOrderReturnGoodsInfo(orderReturnParams);
			setReturnGoodsInfo(orderReturnGoodsList);
			orderReturnDetailInfo.setOrderGoodsInfo(orderReturnGoodsList);

			//添加退单日志
            OrderReturnActionExample example = new OrderReturnActionExample();
            example.or().andReturnSnEqualTo(orderReturnSn);
            List<OrderReturnAction> returnActions = orderReturnActionMapper.selectByExample(example);
            orderReturnDetailInfo.setActions(returnActions);

            apiReturnData.setIsOk(Constant.OS_STR_YES);
			apiReturnData.setData(orderReturnDetailInfo);
		} catch (Exception e) {
			logger.error("平台查询退单详情异常：orderReturnSn"+orderReturnSn, e);
			apiReturnData.setMessage("平台查询退单详情异常"+e.toString());
		}
		return apiReturnData;
	}

	/**
	 * 查询换单详情
	 * @param orderSn 订单编码
	 * @param isHistory 是否历史
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData
	 */
	@Override
	public ApiReturnData<OrderExchangesDetailInfo> orderExchangesDetail(String orderSn, String isHistory, String userId, String siteCode) {
		logger.info("平台前台用户查询换单详情：orderSn=" + orderSn + ",isHistory=" + isHistory);
		ApiReturnData<OrderExchangesDetailInfo> apiReturnData = new ApiReturnData<OrderExchangesDetailInfo>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		if (StringUtils.isBlank(orderSn)) {
			apiReturnData.setMessage("orderSn不能为空！");
			return apiReturnData;
		}
		try {
			//订单数据
			Map<String, Object> params = new HashMap<String, Object>(4);
			params.put("orderSn", orderSn);
			params.put("isHistory", isHistory);
			OrderExchangesDetailInfo orderExchangesDetailInfo = defineOrderMapper.selectOrderExchangesDetailInfo(params);
			if (orderExchangesDetailInfo == null) {
				apiReturnData.setMessage("没有找到该订单！");
				return apiReturnData;
			}
			//包裹及商品数据
			Map<String, Object> shipParams = new HashMap<String, Object>(4);
			shipParams.put("orderSn", orderSn);
			shipParams.put("isHistory", isHistory);
			List<OrderShipInfo> shipList = defineOrderMapper.selectOrderShipInfo(shipParams);
			//未拆单时商品查询
			if (shipList == null || shipList.size() == 0 || shipList.get(0) == null) {
				Map<String, Object> goodsParams = new HashMap<String, Object>(4);
				goodsParams.put("masterOrderSn", orderSn);
				goodsParams.put("isHistory", isHistory);
				List<OrderGoodsInfo>  orderGoodsList = defineOrderMapper.selectOrderGoodsInfo(goodsParams);
				setGoodsInfo(orderGoodsList);
				OrderShipInfo orderShipInfo = new OrderShipInfo();
				orderShipInfo.setOrderGoodsInfo(orderGoodsList);
				shipList = new ArrayList<OrderShipInfo>(Constant.DEFAULT_LIST_SIZE);
				shipList.add(orderShipInfo);
			} else {
				//已拆单时商品查询
				for (OrderShipInfo orderShip:shipList) {
					Map<String, Object> goodsParams = new HashMap<String, Object>(6);
					goodsParams.put("orderSn", orderShip.getOrderSn());
					goodsParams.put("depotCode", orderShip.getDepotCode());
					goodsParams.put("isHistory", isHistory);
					List<OrderGoodsInfo> orderGoodsList = defineOrderMapper.selectOrderGoodsInfo(goodsParams);
					setGoodsInfo(orderGoodsList);
					orderShip.setOrderGoodsInfo(orderGoodsList);
				}
				//合并相同快递单号的物流信息
				shipList = mergeSameInvoiceNo(shipList);
			}
			orderExchangesDetailInfo.setOrderShipInfo(shipList);
			//原退单商品数据
			Map<String, Object> orderReturnParams = new HashMap<String, Object>(2);
			orderReturnParams.put("orderReturnSn", orderExchangesDetailInfo.getReturnSn());
			List<OrderReturnGoodsInfo> orderReturnGoodsList = defineOrderMapper.selectOrderReturnGoodsInfo(orderReturnParams);
			setReturnGoodsInfo(orderReturnGoodsList);
			orderExchangesDetailInfo.setReturnGoodsInfo(orderReturnGoodsList);
			//流程状态	 1 => '等待补款',2 => '补款成功',3 => '收货',4 => '商品质检',5 => '商品出库',6 => '确认收货'
			if (orderExchangesDetailInfo.getTotalPayable() > 0) {
				orderExchangesDetailInfo.setProgressStatus(1);
			} else if (orderExchangesDetailInfo.getTotalPayable() <= 0 && orderExchangesDetailInfo.getShipStatus() < Constant.OI_SHIP_STATUS_ALLSHIPED
					&& orderExchangesDetailInfo.getReceiveTime() == null && orderExchangesDetailInfo.getQualityTime() == null) {
				orderExchangesDetailInfo.setProgressStatus(2);
			}else if (orderExchangesDetailInfo.getReceiveTime() !=null && orderExchangesDetailInfo.getQualityTime() == null
					&& orderExchangesDetailInfo.getShipStatus() < Constant.OI_SHIP_STATUS_ALLSHIPED) {
				orderExchangesDetailInfo.setProgressStatus(3);
			}else if (orderExchangesDetailInfo.getQualityTime() != null && orderExchangesDetailInfo.getReceiveTime() != null
					&& orderExchangesDetailInfo.getShipStatus() < Constant.OI_SHIP_STATUS_ALLSHIPED){
				orderExchangesDetailInfo.setProgressStatus(4);
			}else if (orderExchangesDetailInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
					|| orderExchangesDetailInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED) {
				orderExchangesDetailInfo.setProgressStatus(5);
			} else if (orderExchangesDetailInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
				orderExchangesDetailInfo.setProgressStatus(6);
			}
			apiReturnData.setData(orderExchangesDetailInfo);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
		} catch (Exception e) {
			logger.error("平台查询换单详情异常：orderSn" + orderSn, e);
			apiReturnData.setMessage("平台查询换单详情异常" + e.toString());
		}
		return apiReturnData;
	}

	/**
	 * 设置订单状态
	 * @param orderDetailInfo 订单详情
	 */
	private void setOrderStatus(OrderDetailInfo orderDetailInfo) {
		if (orderDetailInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
			// 订单取消
			orderDetailInfo.setTotalOrderStatus(6);
		} else if (orderDetailInfo.getPayStatus() == Constant.OI_PAY_STATUS_UNPAYED
				|| orderDetailInfo.getPayStatus() == Constant.OI_PAY_STATUS_PARTPAYED) {
			// 待付款
			orderDetailInfo.setTotalOrderStatus(1);
		} else if (orderDetailInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED
				&& orderDetailInfo.getShipStatus() <= Constant.OI_SHIP_STATUS_PARTSHIPPED) {
			// 待发货
			orderDetailInfo.setTotalOrderStatus(2);
		} else if(orderDetailInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLSHIPED
				|| orderDetailInfo.getShipStatus() == Constant.OI_SHIP_STATUS_PARTRECEIVED) {
			// 待收货
			orderDetailInfo.setTotalOrderStatus(3);
		} else if(orderDetailInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
			// 已完成
			orderDetailInfo.setTotalOrderStatus(4);
		}
	}

	/**
	 * 订单详情
	 * @param orderSn 订单编码
	 * @param isHistory 是否历史
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<OrderDetailInfo>
	 */
	@Override
	public ApiReturnData<OrderDetailInfo> orderInfoDetail(String orderSn, String isHistory, String userId, String siteCode) {
		logger.info("平台前台查询订单详情：order=" + orderSn + ",isHistory=" + isHistory);
		ApiReturnData<OrderDetailInfo> apiReturnData = new ApiReturnData<OrderDetailInfo>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		try {
			if (StringUtils.isBlank(orderSn)) {
				apiReturnData.setMessage("orderSn不能为空！");
				return apiReturnData;
			}
			if (StringUtils.isBlank(siteCode)) {
				apiReturnData.setMessage("siteCode不能为空！");
				return apiReturnData;
			}
			if (StringUtils.isBlank(isHistory)) {
				isHistory = "0";
			}
			Map<String, Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
			params.put("orderSn", orderSn);
			params.put("isHistory", isHistory);
			params.put("siteCode", siteCode);
			if (StringUtils.isBlank(userId)) {
				params.put("userId", null);
			} else {
				params.put("userId", userId);
			}
			OrderDetailInfo orderDetailInfo = defineOrderMapper.selectOrderDetailInfo(params);
			if (orderDetailInfo == null) {
				apiReturnData.setMessage("没有找到该订单！");
				return apiReturnData;
			}
			// 设置订单状态
			setOrderStatus(orderDetailInfo);

			//校验是否可以申请退单
            String orderPayTime = orderDetailInfo.getOrderPayTime();
            if (StringUtils.isNotBlank(orderPayTime)) {
                int periodDetailValue = getPeriodDetailValue(1, "6604");
                int timeLimit = DateTimeUtils.dateDiff(orderPayTime, periodDetailValue);
                if (orderDetailInfo.getPayStatus() == Constant.OI_PAY_STATUS_PAYED
						&& orderDetailInfo.getShipStatus() == Constant.OI_SHIP_STATUS_UNSHIPPED && timeLimit != 1) {
                    orderDetailInfo.setRefund(true);
                }
            }

			// 订单未支付, 订单前端可以取消的最后时间
			String orderCancelTime = getOrderCancelTime(orderDetailInfo.getOrderCreateTime());
			orderDetailInfo.setOrderCancelTime(orderCancelTime);

			String country = "";
			String province = "";
			String city = "";
			String district = "";
			String street = "";
			String shippingAddress = "";
			if (orderDetailInfo != null) {
				orderDetailInfo.setEncryptMobile(orderDetailInfo.getMobile().length() > 11 ? orderDetailInfo.getMobile() : orderDetailInfo.getMobile());
				String mobile = orderDetailInfo.getMobile();
				orderDetailInfo.setMobile(encryptionMobile(mobile));
				if (orderDetailInfo.getAddress() != null) {
					SystemRegionArea countryArea = systemRegionAreaMapper.selectByPrimaryKey(orderDetailInfo.getCountry());
					if (countryArea != null) {
						country = countryArea.getRegionName();
					}
				}
				if (orderDetailInfo.getProvince() != null && orderDetailInfo.getProvince().length() > 0) {
					SystemRegionArea provinceArea = systemRegionAreaMapper.selectByPrimaryKey(orderDetailInfo.getProvince());
					if (provinceArea != null) {
						province = provinceArea.getRegionName();
					}
				}
				if (orderDetailInfo.getCity() != null && orderDetailInfo.getCity().length() > 0) {
					SystemRegionArea cityArea = systemRegionAreaMapper.selectByPrimaryKey(orderDetailInfo.getCity());
					if (cityArea != null) {
						city = cityArea.getRegionName();
					}
				}
				if (orderDetailInfo.getDistrict() != null && orderDetailInfo.getDistrict().length() > 0) {
					SystemRegionArea districtArea = systemRegionAreaMapper.selectByPrimaryKey(orderDetailInfo.getDistrict());
					if (districtArea != null) {
						district = districtArea.getRegionName();
					}
				}
				if (orderDetailInfo.getStreet() != null && orderDetailInfo.getStreet().length() > 0) {
					SystemRegionArea streetArea = systemRegionAreaMapper.selectByPrimaryKey(orderDetailInfo.getStreet());
					if (streetArea != null) {
						street = streetArea.getRegionName();
					}
				}
				shippingAddress = province + city + district + street + orderDetailInfo.getAddress();
			}
			orderDetailInfo.setShippingAddress(shippingAddress);
			Map<String, Object> shipParams = new HashMap<String, Object>(4);
			shipParams.put("orderSn", orderSn);
			shipParams.put("isHistory", isHistory);
			List<OrderShipInfo> shipList = defineOrderMapper.selectOrderShipInfo(shipParams);
			//未拆单时商品查询
			if (shipList == null || shipList.size() == 0 || shipList.get(0) == null) {
				Map<String, Object> goodsParams = new HashMap<String, Object>(4);
				goodsParams.put("masterOrderSn", orderSn);
				goodsParams.put("isHistory", isHistory);
				List<OrderGoodsInfo> orderGoodsList = defineOrderMapper.selectOrderGoodsInfo(goodsParams);
				setGoodsInfo(orderGoodsList);
				OrderShipInfo orderShipInfo = new OrderShipInfo();
				orderShipInfo.setOrderGoodsInfo(orderGoodsList);
				shipList = new ArrayList<OrderShipInfo>();
				shipList.add(orderShipInfo);
			} else {
				//已拆单时商品查询
                boolean flag = true;
                String lastDeliveryConfirmTime = "";
				for (OrderShipInfo orderShip : shipList) {
					Map<String, Object> goodsParams = new HashMap<String, Object>();
                    goodsParams.put("orderSn", orderShip.getOrderSn());
                    goodsParams.put("depotCode", orderShip.getDepotCode());
                    goodsParams.put("isHistory", isHistory);
                    if (StringUtils.isNotBlank(orderShip.getInvoiceNo())) {
                        goodsParams.put("invoiceNo", orderShip.getInvoiceNo());
                    }
					List<OrderGoodsInfo> orderGoodsList = defineOrderMapper.selectOrderGoodsInfo(goodsParams);
					setGoodsInfo(orderGoodsList);
					for (OrderGoodsInfo orderGoodsInfo : orderGoodsList) {
						Map<String, Object> returnGoodsParams = new HashMap<String, Object>();
                        returnGoodsParams.put("sku", orderGoodsInfo.getSkuSn());
                        returnGoodsParams.put("subOrderSn", orderGoodsInfo.getSubOrderSn());
						List<ReturnGoodsStatus> returnGoodsStatusList = defineOrderMapper.selectReturnGoodsStatus(returnGoodsParams);
						if (returnGoodsStatusList != null && returnGoodsStatusList.size() > 0) {
							orderGoodsInfo.setGoodsStatus(returnGoodsStatusList.get(0).getReturnGoodsStatus()); 
						}
					}
					orderShip.setOrderGoodsInfo(orderGoodsList);

					//获取最后收货时间,包裹收货时间不为空,只要有未收货包裹，则无整单确认收货时间
                    if (flag && StringUtils.isNotBlank(orderShip.getDeliveryConfirmTime())) {
                        if (StringUtils.isBlank(lastDeliveryConfirmTime) || lastDeliveryConfirmTime.compareTo(orderShip.getDeliveryConfirmTime()) < 0) {
                            lastDeliveryConfirmTime = orderShip.getDeliveryConfirmTime();
                        }
                    } else {
                        flag = false;
                        lastDeliveryConfirmTime = "";
                    }
				}
				//填充订单确认收货时间
                orderDetailInfo.setLastDeliveryConfirmTime(lastDeliveryConfirmTime);
				//合并相同快递单号的物流信息
				shipList = mergeSameInvoiceNo(shipList);
			}
			orderDetailInfo.setOrderShipInfo(shipList);
			setOrderGoodsInfo(orderDetailInfo);
			apiReturnData.setData(orderDetailInfo);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
		} catch (Exception e) {
			logger.error("平台前台查询订单详情异常：order="+orderSn, e);
			apiReturnData.setMessage("平台前台查询订单详情异常"+e.toString());
		}
		return apiReturnData;
	}

	/**
	 * 获取订单商品信息
	 * @param orderSns 订单列表
	 * @param siteCode 站点编码
	 * @return ApiReturnData<List<OrderShipInfo>>
	 */
	@Override
	public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByOrderSn(List<String> orderSns, String siteCode) {
		logger.info("获取商品消息orderSns=" + JSONObject.toJSONString(orderSns));
		ApiReturnData<List<OrderShipInfo>> apiReturnData = new ApiReturnData<List<OrderShipInfo>>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);

		try {
			List<OrderShipInfo> orderGoodsInfoList = new ArrayList<OrderShipInfo>();
			if (CollectionUtils.isEmpty(orderSns)) {
				apiReturnData.setMessage("orderSns不能为空！");
				return apiReturnData;
			}

			Map<String,Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
			for (String orderSn : orderSns) {
				MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
				if (null == masterOrderInfo) {
					apiReturnData.setMessage("没有查到对应订单！");
					return apiReturnData;
				}
				params.put("masterOrderSn", orderSn);
				params.put("isHistory", "0");
				List<OrderGoodsInfo> orderGoodsList = defineOrderMapper.selectOrderGoodsInfo(params);
				setGoodsInfo(orderGoodsList);
				OrderShipInfo shipInfo = new OrderShipInfo();
				shipInfo.setOrderGoodsInfo(orderGoodsList);
				shipInfo.setOrderSn(orderSn);
				shipInfo.setOrderFrom(masterOrderInfo.getOrderFrom());
				orderGoodsInfoList.add(shipInfo);
			}
			apiReturnData.setData(orderGoodsInfoList);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
		} catch (Exception e) {
			logger.error("app获取商品消息异常：orderSns=" + orderSns, e);
			apiReturnData.setMessage("app获取商品消息异常：" + e.toString());
		}
	
		return apiReturnData;
	}
	
	private void setGoodsInfo(List<OrderGoodsInfo>  orderGoodsList){
		if (CollectionUtils.isNotEmpty(orderGoodsList)) {
			for (OrderGoodsInfo orderGoods : orderGoodsList) {
				if (orderGoods.getSkuSn() != null && orderGoods.getSkuSn().length() > 6) {
					orderGoods.setGoodsSn(orderGoods.getSkuSn().substring(0, 6));
				}
			}
		}
	}
	
	private void setReturnGoodsInfo(List<OrderReturnGoodsInfo> orderReturnGoodsList) {
		if (CollectionUtils.isNotEmpty(orderReturnGoodsList)) {
			for (OrderReturnGoodsInfo orderReturnGoods : orderReturnGoodsList) {
				if (orderReturnGoods.getSkuSn() != null && orderReturnGoods.getSkuSn().length() > 6) {
					orderReturnGoods.setGoodsSn(orderReturnGoods.getSkuSn().substring(0, 6));
				}
			}
		}
	}
	
	/**
	 * 设置商品详情商品总数量
	 * @param orderDetailInfo
	 */
	private void setOrderGoodsInfo(OrderDetailInfo orderDetailInfo) {
		//logger.info("orderDetailInfo:" + JSONObject.toJSONString(orderDetailInfo));
		List<OrderShipInfo> list = orderDetailInfo.getOrderShipInfo();
		if (list == null || list.size() == 0) {
			return;
		}

		//获取退单商品信息
        Map<String, OrderReturnGoods> returnGoodsMap = getOrderReturnGoodsMap(orderDetailInfo.getOrderSn());
		if (returnGoodsMap == null) {
            returnGoodsMap = new HashMap<String, OrderReturnGoods>();
        }
		
		int goodsNumber = 0;
		for (OrderShipInfo orderShipInfo : list) {
			if (orderShipInfo == null) {
				continue;
			}
			
			List<OrderGoodsInfo> goodsList = orderShipInfo.getOrderGoodsInfo();
			if (goodsList != null && goodsList.size() > 0) {
				for (OrderGoodsInfo orderGoodsInfo : goodsList) {
					goodsNumber += orderGoodsInfo.getGoodsNum();
					logger.info("orderDetailInfo-->goodsNumber:" + goodsNumber);

					//填充退单数量
                    String key = orderGoodsInfo.getSkuSn() + "_" + orderGoodsInfo.getExtensionCode();
                    OrderReturnGoods returnGoods = returnGoodsMap.get(key);
                    int canChangeNum = 0;
                    if (returnGoods == null) {
                        canChangeNum = orderGoodsInfo.getGoodsNum();
                    } else {
                        canChangeNum = orderGoodsInfo.getGoodsNum() - returnGoods.getGoodsReturnNumber();
                        orderGoodsInfo.setReturnGoodsNum(returnGoods.getGoodsReturnNumber());
                    }
                    orderGoodsInfo.setCanChangeNum(canChangeNum);
				}
			}
		}
		orderDetailInfo.setGoodsNumber(goodsNumber);
		//logger.info("orderDetailInfo:" + JSONObject.toJSONString(orderDetailInfo));
	}

    /**
     * 获取退单信息
     * @param orderSn
     * @return
     */
    private Map<String, OrderReturnGoods> getOrderReturnGoodsMap(String orderSn) {
        OrderReturnExample returnExample = new OrderReturnExample();
        returnExample.or().andRelatingOrderSnEqualTo(orderSn).andIsDelEqualTo(0).andReturnOrderStatusNotEqualTo((byte) 4);
        List<OrderReturn> orderReturnList = orderReturnMapper.selectByExample(returnExample);
        if (StringUtil.isListNull(orderReturnList)) {
            return null;
        }

        List<String> returnSnList = new ArrayList<String>();
        for (OrderReturn orderReturn : orderReturnList) {
            returnSnList.add(orderReturn.getReturnSn());
        }

        OrderReturnGoodsExample goodsExample = new OrderReturnGoodsExample();
        goodsExample.or().andRelatingReturnSnIn(returnSnList);
        List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(goodsExample);
        if (StringUtil.isListNull(orderReturnGoodsList)) {
            return null;
        }

        Map<String, OrderReturnGoods> map = new HashMap<String, OrderReturnGoods>();
        for (OrderReturnGoods returnGoods : orderReturnGoodsList) {
            String key = returnGoods.getCustomCode() + "_" + returnGoods.getExtensionCode();
            OrderReturnGoods goods = map.get(key);
            if (goods != null) {
                Integer returnNum = goods.getGoodsReturnNumber() + returnGoods.getGoodsReturnNumber();
                returnGoods.setGoodsReturnNumber(returnNum.shortValue());
            }

            map.put(key, returnGoods);
        }

        return map;
    }

    private String encryptionMobile(String  mobile) {
		if (mobile.length() == 11) {
			return mobile.substring(0,3) + "****" + mobile.substring(7,11);
		} else {
			return mobile;
		}
	}
	
	//合并订单相同快递单号记录
	private List<OrderShipInfo> mergeSameInvoiceNo(List<OrderShipInfo> shipList) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<OrderShipInfo> returnList = new ArrayList<OrderShipInfo>();
		for (OrderShipInfo orderShipInfo : shipList) {
			String key = orderShipInfo.getInvoiceNo();
			if(!map.containsKey(key)){
				map.put(key, orderShipInfo);
			}else{
				OrderShipInfo shipInfo =  (OrderShipInfo) map.get(key);
				if(CollectionUtils.isEmpty(shipInfo.getOrderGoodsInfo())){
					shipInfo.setOrderGoodsInfo(new ArrayList<OrderGoodsInfo>());
				}
				for(OrderGoodsInfo orderGoodsInfo :orderShipInfo.getOrderGoodsInfo()){
					shipInfo.getOrderGoodsInfo().add(orderGoodsInfo);
				}
				map.put(key, shipInfo);
			}
		}
		
		for(Map.Entry<String,Object> entry:map.entrySet()){
			returnList.add((OrderShipInfo) entry.getValue());
		}
		return returnList;
	}

	/**
	 * 获取用户订单种类数量
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<UserOrderTypeNum>
	 */
	@Override
	public ApiReturnData<UserOrderTypeNum> getUserOrderType(String userId, String siteCode) {
		logger.info("平台查询用户订单数量userId=" + userId+", siteCode=" + siteCode);
		ApiReturnData<UserOrderTypeNum> apiReturnData = new ApiReturnData<UserOrderTypeNum>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);

		try {
			Map<String, Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
			params.put("userId", userId);
			params.put("siteCode", siteCode);
			//用户待付款订单数量
			params.put("type", 1);
			int waitPayNum = defineOrderMapper.selectUserOrderCountByType(params);
			//用户待发货订单数量
			params.put("type", 2);
			int waitShipNum = defineOrderMapper.selectUserOrderCountByType(params);
			//用户待收货订单数量
			params.put("type", 3);
			int waitReceivingNum = defineOrderMapper.selectUserOrderCountByType(params);
			//等待评论订单数量
			params.put("type", 4);
			int waitReviewNum = defineOrderMapper.selectUserOrderCountByType(params);
			//用户已完成订单数量
			params.put("type", 5);
			int completedNum = defineOrderMapper.selectUserOrderCountByType(params);
			//退单数
			params.put("type", 6);
			int orderReturnNum = defineOrderMapper.selectUserOrderCountByType(params);

			UserOrderTypeNum userOrderTypeNum = new UserOrderTypeNum();
			userOrderTypeNum.setWaitPayNum(waitPayNum);
			userOrderTypeNum.setWaitShipNum(waitShipNum);
			userOrderTypeNum.setWaitReceivingNum(waitReceivingNum);
			userOrderTypeNum.setWaitReviewNum(waitReviewNum);
			userOrderTypeNum.setCompletedNum(completedNum);
			userOrderTypeNum.setOrderReturnNum(orderReturnNum);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
			apiReturnData.setData(userOrderTypeNum);
		} catch (Exception e) {
			apiReturnData.setMessage("平台查询用户订单数量异常："+e.toString());
			logger.error("平台查询用户订单数量异常：", e);
		}
		return apiReturnData;
	}

	/**
	 * 查询订单用户列表
	 * @param searchParam 查询参数
	 * @return ApiReturnData<Paging<String>>
	 */
	@Override
	public ApiReturnData<Paging<String>> getOrderInfoUser(SearchParam searchParam) {
		ApiReturnData apiReturnData = new ApiReturnData();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		logger.info("getOrderInfoUser:orderFrom=" + JSONObject.toJSONString(searchParam));
		try {
			Map<String, Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
			if (StringUtils.isNotEmpty(searchParam.getOrderFrom())) {
				params.put("orderFrom",searchParam.getOrderFrom());
			} else {
				apiReturnData.setMessage("orderFrom不能为空！");
				return apiReturnData;
			}
			if (StringUtils.isNotEmpty(searchParam.getOrderStatus())) {
				params.put("orderStatus",searchParam.getOrderStatus());
			}
			if (StringUtils.isNotEmpty(searchParam.getDateStart())) {
				params.put("dateStart", searchParam.getDateStart());
			}
			if (StringUtils.isNotEmpty(searchParam.getDateEnd())) {
				params.put("dateEnd", searchParam.getDateEnd());
			}
			if (searchParam.getMinMoneyPaid() != null) {
				params.put("minMoneyPaid", searchParam.getMinMoneyPaid());
			}
			if (searchParam.getMaxMoneyPaid() != null) {
				params.put("maxMoneyPaid", searchParam.getMaxMoneyPaid());
			}
			int start = (searchParam.getPnum() - 1) * searchParam.getPsize();
			int end = searchParam.getPsize();
			params.put("start",start);
			params.put("num",end);
			Paging<String> paging = new Paging<String>();
			List<String> list = defineOrderMapper.selectUserId(params);
			paging.setRoot(list);
			paging.setTotalProperty(defineOrderMapper.selectUserIdCount(params));
			apiReturnData.setData(paging);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
		} catch (Exception e) {
			logger.error("getOrderInfoUser异常：",e);
			apiReturnData.setMessage("异常："+e.toString());
		}
		return apiReturnData;
	}

	/**
	 * 获取用户订单编码列表
	 * @param activitySearchParam 参数
	 * @return ApiReturnData<Paging<String>>
	 */
	@Override
	public ApiReturnData<Paging<String>> getOrderSnByUser(ActivitySearchParam activitySearchParam) {
		ApiReturnData<Paging<String>> apiReturnData = new ApiReturnData<Paging<String>>();
		logger.info("getOrderSnByUser:" + JSONObject.toJSONString(activitySearchParam));
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		try {
			Map<String, Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
			if (StringUtils.isNotEmpty(activitySearchParam.getOrderFrom())) {
				params.put("orderFrom", activitySearchParam.getOrderFrom());
			} else {
				apiReturnData.setMessage("orderFrom不能为空！");
				return apiReturnData;
			}
			if (StringUtils.isNotEmpty(activitySearchParam.getUserId())) {
				params.put("userId", activitySearchParam.getUserId());
			} else {
				apiReturnData.setMessage("userId不能为空！");
				return apiReturnData;
			}
			if (activitySearchParam.getPayStatus() != null) {
				params.put("payStatus", activitySearchParam.getPayStatus());
			} else {
				apiReturnData.setMessage("payStatus不能为空！");
				return apiReturnData;
			}
			if (StringUtils.isNotEmpty(activitySearchParam.getAddTimeStart())) {
				params.put("addTimeStart", activitySearchParam.getAddTimeStart());
			}
			if (StringUtils.isNotEmpty(activitySearchParam.getAddTimeEnd())){
				params.put("addTimeEnd", activitySearchParam.getAddTimeEnd());
			}
			if (activitySearchParam.getPrizeMin() != null){
				params.put("prizeMin", activitySearchParam.getPrizeMin());
			}
			if (activitySearchParam.getPrizeMax() != null) {
				params.put("prizeMax", activitySearchParam.getPrizeMax());
			}
			List<String> list = defineOrderMapper.selectOrderSnByUser(params);

			Paging<String> paging = new Paging<String>();
			paging.setRoot(list);
			apiReturnData.setData(paging);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
		} catch (Exception e) {
			logger.error("getOrderSnByUser异常：",e);
			apiReturnData.setMessage("异常："+e.toString());
		}
		
		return apiReturnData;
	}

	/**
	 * 用户订单转移
	 * @param oldUser 老用户id
	 * @param newUser 新用户id
	 * @param siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	@Override
	public ReturnInfo<Boolean> changeOrderUser(String oldUser, String newUser, String siteCode) {
		logger.info("修改订单用户:oldUser=" + oldUser+",newUser="+newUser);
		ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
		returnInfo.setIsOk(Constant.OS_NO);
		if (StringUtils.isBlank(oldUser) || StringUtils.isBlank(newUser)) {
			returnInfo.setMessage("参数不能为空！");
			return returnInfo;
		}
		try {
			Map<String, Object> params=new HashMap<String, Object>(4);
			params.put("oldUser", oldUser);
			params.put("newUser", newUser);
			defineOrderMapper.updateOrderUser(params);
			defineOrderMapper.updateHistoryOrderUser(params);
			defineOrderMapper.updateOrderReturn(params);
			defineOrderMapper.updateOrderReturnChange(params);
			defineOrderMapper.updateOrderReturnChangeAction(params);
			returnInfo.setIsOk(Constant.OS_YES);
			returnInfo.setData(true);
		} catch (Exception e) {
			logger.error("修改订单用户异常：oldUser=" + oldUser+",newUser=" + newUser, e);
			returnInfo.setMessage("修改订单用户异常:" + e.toString());
		}
		return returnInfo;
	}

	/**
	 * 设置订单已评论
	 * @param orderSn 订单编码
	 * @param flag true 为已评论
	 * @param siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	@Override
	public ReturnInfo<Boolean> orderReview(String orderSn, boolean flag, String siteCode) {
		ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
		returnInfo.setIsOk(Constant.OS_NO);
		try {
			if (StringUtils.isBlank(orderSn)) {
				returnInfo.setMessage("orderSn不能为空！");
				return  returnInfo;
			}
			MasterOrderInfoExtend orderInfoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(orderSn);
			if (orderInfoExtend == null) {
				returnInfo.setMessage("没有找到相关订单！");
				return  returnInfo;
			}
			if (flag) {
				MasterOrderInfoExtend orderInfoExtendUpdate = new MasterOrderInfoExtend();
				orderInfoExtendUpdate.setMasterOrderSn(orderSn);
				orderInfoExtendUpdate.setIsReview(1);
				masterOrderInfoExtendMapper.updateByPrimaryKeySelective(orderInfoExtendUpdate);
				returnInfo.setIsOk(Constant.OS_YES);
				returnInfo.setData(true);
			}
		} catch (Exception e) {
			logger.error("平台评论接口异常:" + orderSn , e);
			returnInfo.setMessage("平台评论接口异常:"+ orderSn + e.toString());
		}
		
		return returnInfo;
	}

    /**
     * 判断确认收货参数
     * @param orderSn
     * @param actionUser
     * @return
     */
	private String checkConfirmReceipt(String orderSn, String actionUser) {
	    String msg = null;
        if (StringUtils.isBlank(orderSn)) {
            msg = "订单号不能为空";
            return msg;
        }
        if (StringUtils.isBlank(actionUser)) {
            msg = "actionUser不能为空";
            return msg;
        }
        return msg;
    }

	/**
	 * 确认收货
	 * @param orderSn 订单编码
	 * @param invoiceNo 运单号
	 * @param actionUser 操作人
	 * @param siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	@Override
	public ReturnInfo<Boolean> confirmReceipt(String orderSn, String invoiceNo, String actionUser, String siteCode) {
		logger.info("confirmReceipt.orderSn:" + orderSn + ",invoiceNo:" + invoiceNo + ",actionUser:" + actionUser);
		ReturnInfo<Boolean> ri = new ReturnInfo<Boolean>(Constant.OS_NO);
		try {
		    String msg = checkConfirmReceipt(orderSn, actionUser);
			if (StringUtils.isNotBlank(msg)) {
				ri.setMessage(msg);
				return ri;
			}

			MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
			if (orderInfo == null) {
				ri.setMessage(orderSn + "订单不存在");
				return ri;
			}
			if (orderInfo.getShipStatus().intValue() == Constant.OI_SHIP_STATUS_ALLRECEIVED) {
				ri.setMessage(orderSn + "订单已经是已收货状态");
				ri.setIsOk(Constant.OS_YES);
				return ri;
			}
			if (orderInfo.getShipStatus() < Constant.OI_SHIP_STATUS_PARTSHIPPED) {
				ri.setMessage(orderSn + "订单未发货！");
				return ri;
			}
			if (StringUtils.isBlank(invoiceNo)) {
				if (orderInfo.getShipStatus() < Constant.OI_SHIP_STATUS_ALLSHIPED) {
					ri.setMessage(orderSn + "订单未全部发货，不能确认收货！");
					return ri;
				}
				// 设置订单已收货
                setMasterOrderInfoShipReceived(orderSn);

				OrderDistributeExample orderDistributeExample = new OrderDistributeExample();
				orderDistributeExample.or().andMasterOrderSnEqualTo(orderSn);
				List<OrderDistribute> orderDistributeList = orderDistributeMapper.selectByExample(orderDistributeExample);
				if (null == orderDistributeList || orderDistributeList.size() == 0) {
					ri.setMessage("未找到对应的交货单！");
					return ri;
				}
				// 设置交货单已收货
                setOrderDistributeShipReceived(orderDistributeList);
			} else {
				OrderDepotShipExample orderDepotShipExample = new OrderDepotShipExample();
				orderDepotShipExample.or().andInvoiceNoEqualTo(invoiceNo);
				List<OrderDepotShip> orderDepotShipList = orderDepotShipMapper.selectByExample(orderDepotShipExample);
				if (null == orderDepotShipList || orderDepotShipList.size() == 0) {
					ri.setMessage("未找到对应的发货单！");
					return ri;
				}
				String shipSn = null;
				for (OrderDepotShip orderDepotShip : orderDepotShipList) {
					if (orderDepotShip.getOrderSn().indexOf(orderSn) != -1) {
						shipSn = orderDepotShip.getOrderSn();
						orderDepotShip.setUpdateTime(new Date());
						orderDepotShip.setShippingStatus((byte)Constant.OS_SHIPPING_STATUS_CONFIRM);
						orderDepotShip.setDeliveryConfirmTime(new Date());
						orderDepotShipMapper.updateByPrimaryKeySelective(orderDepotShip);
					}
				}
				orderDepotShipExample.clear();
				orderDepotShipExample.or().andOrderSnEqualTo(shipSn).andIsDelNotEqualTo(1);
				List<OrderDepotShip> orderDepotShipUpdatedList =  orderDepotShipMapper.selectByExample(orderDepotShipExample);
				// 获取子单底下所有交货单的状态，并获取最大与最小值
				List<Byte> shippingStatus = new ArrayList<Byte>();
				for (OrderDepotShip ship : orderDepotShipUpdatedList) {
					shippingStatus.add(ship.getShippingStatus());
				}
				byte maxStatus = Collections.max(shippingStatus);
				byte minStatus = Collections.min(shippingStatus);
				OrderDistribute orderDistribute = new OrderDistribute();
				orderDistribute.setOrderSn(orderDepotShipUpdatedList.get(0).getOrderSn());
				/**
				 * 1:子单底下发货单最小状态与最大状态不相等,只有在以下情况下修改交货单状态，其他状态不做修改！！！
				 *   a:最小状态为已发货，则更新子单发货状态为部分收货 4
				 *   b:最小状态为已收货，则更新子单发货状态为客户已收获 5
				 * 2：子单底下发货单最小状态与最大状态相等
				 *   a:更新子单发货状态为客户已收获 5
				 */
				if (minStatus != maxStatus) {
					if (minStatus == Constant.OS_SHIPPING_STATUS_SHIPPED) {
						orderDistribute.setUpdateTime(new Date());
						orderDistribute.setShipStatus((byte)Constant.OD_SHIP_STATUS_CUSTOMER_PARTRECEIVED);
						orderDistributeMapper.updateByPrimaryKeySelective(orderDistribute);
					} else if(minStatus == Constant.OS_SHIPPING_STATUS_RECEIVED) {
						orderDistribute.setUpdateTime(new Date());
						orderDistribute.setShipStatus((byte)Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED);
						orderDistributeMapper.updateByPrimaryKeySelective(orderDistribute);
					}
				} else if(minStatus == maxStatus) {
					orderDistribute.setUpdateTime(new Date());
					orderDistribute.setShipStatus((byte)Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED);
					orderDistributeMapper.updateByPrimaryKeySelective(orderDistribute);
				}
				/**
				 * 更新主单状态,获取主单底下所有有效子单发货的状态，并取其中最大值与最小值，只要在以下状态下修改主单的发货状态！！
				 * 1:最大值与最小值不相等
				 *   a.最大发货状态为客户已收获，最小发货状态为已发货或者部分收货，则修改主单发货状态为部分收货
				 * 2:最大值与最小值相等
				 *   a.子单发货状态为部分发货，更新主单状态为部分发货
				 *   b.子单发货状态为客户已收货，更新主单状态为客户已收获
				 */
				OrderDistributeExample orderDistributeExample = new OrderDistributeExample();
				orderDistributeExample.or().andMasterOrderSnEqualTo(orderSn).andOrderStatusNotEqualTo((byte)2);
				List<OrderDistribute> orderDistributeUpdatedList =  orderDistributeMapper.selectByExample(orderDistributeExample);
				if (null == orderDistributeUpdatedList || orderDistributeUpdatedList.size() == 0) {
					ri.setMessage("未找到对应的交货单！");
					return ri;
				}
				List<Byte> distributeShippingStatus = new ArrayList<Byte>();
				for (OrderDistribute distribute : orderDistributeUpdatedList) {
					distributeShippingStatus.add(distribute.getShipStatus());
				}
				MasterOrderInfo updateOrderInfo = new MasterOrderInfo();
				updateOrderInfo.setMasterOrderSn(orderSn);
				byte distributeMaxStatus = Collections.max(distributeShippingStatus);
				byte distributeMinStatus = Collections.min(distributeShippingStatus);
				if (distributeMaxStatus != distributeMinStatus ) {
					if(distributeMaxStatus == Constant.OI_SHIP_STATUS_ALLRECEIVED && (distributeMinStatus == Constant.OI_SHIP_STATUS_ALLSHIPED || distributeMinStatus == Constant.OI_SHIP_STATUS_PARTRECEIVED ) ){
						updateOrderInfo.setUpdateTime(new Date());
						updateOrderInfo.setShipStatus((byte)Constant.OI_SHIP_STATUS_PARTRECEIVED);
						masterOrderInfoMapper.updateByPrimaryKeySelective(updateOrderInfo);
					}
				}else if(distributeMaxStatus == distributeMinStatus ){
					if(distributeMinStatus == Constant.OI_SHIP_STATUS_PARTRECEIVED ){
						updateOrderInfo.setUpdateTime(new Date());
						updateOrderInfo.setShipStatus((byte)Constant.OI_SHIP_STATUS_PARTRECEIVED);
						masterOrderInfoMapper.updateByPrimaryKeySelective(updateOrderInfo);
					}else if(distributeMinStatus == Constant.OI_SHIP_STATUS_ALLRECEIVED){
						updateOrderInfo.setUpdateTime(new Date());
						updateOrderInfo.setShipStatus((byte)Constant.OI_SHIP_STATUS_ALLRECEIVED);
						masterOrderInfoMapper.updateByPrimaryKeySelective(updateOrderInfo);
					}
				}
			}

			masterOrderActionService.insertOrderActionBySn(orderSn, "客户确认收货！", actionUser);
			ri.setIsOk(ConstantValues.YESORNO_YES);
			ri.setMessage("确认收货更新完成！");
			ri.setData(true);
		} catch (Exception e) {
			logger.error("confirmReceipt.orderSn:" + orderSn , e);
			ri.setMessage("确认收货更新失败:" + e.toString());
		}
		return ri;
	}

	/**
	 * 设置订单已收货
	 * @param masterOrderSn
	 */
	private void setMasterOrderInfoShipReceived(String masterOrderSn) {
		MasterOrderInfo updateOrderInfo = new MasterOrderInfo();
		updateOrderInfo.setUpdateTime(new Date());
		updateOrderInfo.setShipStatus((byte)Constant.OI_SHIP_STATUS_ALLRECEIVED);
		updateOrderInfo.setMasterOrderSn(masterOrderSn);
		masterOrderInfoMapper.updateByPrimaryKeySelective(updateOrderInfo);
	}

    /**
     * 设置交货单已收货
     * @param orderDistributeList
     */
	private void setOrderDistributeShipReceived(List<OrderDistribute> orderDistributeList) {
        for (OrderDistribute orderDistribute : orderDistributeList) {
            if (orderDistribute.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
                continue;
            }
            OrderDistribute updateOrderDistribute = new OrderDistribute();
            updateOrderDistribute.setUpdateTime(new Date());
            updateOrderDistribute.setShipStatus((byte)Constant.OI_SHIP_STATUS_ALLRECEIVED);
            updateOrderDistribute.setOrderSn(orderDistribute.getOrderSn());
            orderDistributeMapper.updateByPrimaryKeySelective(updateOrderDistribute);
            OrderDepotShipExample orderDepotShipExample = new OrderDepotShipExample();
            orderDepotShipExample.or().andOrderSnEqualTo(orderDistribute.getOrderSn());
            List<OrderDepotShip> orderDepotShipList = orderDepotShipMapper.selectByExample(orderDepotShipExample);
            for (OrderDepotShip orderDepotShip : orderDepotShipList) {
                if (orderDepotShip.getIsDel() == 1) {
                    continue;
                }
                orderDepotShip.setUpdateTime(new Date());
                orderDepotShip.setShippingStatus((byte)Constant.OS_SHIPPING_STATUS_CONFIRM);
                orderDepotShip.setDeliveryConfirmTime(new Date());
                orderDepotShipMapper.updateByPrimaryKeySelective(orderDepotShip);
            }
        }
    }

	/**
	 * 通过支付单号获取订单商品
	 * @param paySn 支付单号
	 * @param siteCode 站点编码
	 * @return ApiReturnData<List<OrderShipInfo>>
	 */
	@Override
	public ApiReturnData<List<OrderShipInfo>> getOrderGoodsByPaySn(String paySn, String siteCode) {
		logger.info("根据支付单号查询商品信息：paySn="+paySn);
		ApiReturnData<List<OrderShipInfo>> data = new ApiReturnData<List<OrderShipInfo>>();
		data.setIsOk(Constant.OS_STR_NO);
		try {
			if (StringUtils.isBlank(paySn)) {
				data.setMessage("参数错误！");
				return data;
			} else {
				List<String> orderSns = new ArrayList<String>(Constant.DEFAULT_LIST_SIZE);
				if (paySn.indexOf("MFK") >= 0) {
					MergeOrderPay mergePay = mergeOrderPayMapper.selectByPrimaryKey(paySn);
					String[] masterPaySns = mergePay.getMasterPaySn().split(",");
					for (String masterPaySn : masterPaySns) {
						MasterOrderPay masterPay = masterOrderPayMapper.selectByPrimaryKey(masterPaySn);
						orderSns.add(masterPay.getMasterOrderSn());
					}
				} else {
					MasterOrderPay masterPay = masterOrderPayMapper.selectByPrimaryKey(paySn);
					orderSns.add(masterPay.getMasterOrderSn());
				}
				data = getOrderGoodsByOrderSn(orderSns, siteCode);
				data.setMessage("查询成功！");
			}
		} catch (Exception e) {
			logger.error("查询商品发生异常,paySn:" + paySn, e);
			data.setMessage("查询商品发生异常,paySn:" + paySn);
		}
		return data;
	}

	/**
	 * 通过订单编码取消订单
	 * @param masterOrderSn 订单编码
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<Boolean>
	 */
	@Override
	public ApiReturnData<Boolean> cancelOrderByMasterSn(String masterOrderSn, String userId, String siteCode) {
        PageListParam searchParam = new PageListParam();
        searchParam.setOrderSn(masterOrderSn);
        searchParam.setUserId(userId);
        searchParam.setSiteCode(siteCode);
	    return cancelOrderByMasterSnAndCancel(searchParam);
	}

    /**
     * 通过订单编码取消订单
     * @param searchParam
     * @return ApiReturnData<Boolean>
     */
    @Override
    public ApiReturnData<Boolean> cancelOrderByMasterSnAndCancel(PageListParam searchParam) {
        String masterOrderSn = searchParam.getOrderSn();
        String userId = searchParam.getUserId();
        String siteCode = searchParam.getSiteCode();

        logger.info("前台订单取消 masterOrderSn：" + masterOrderSn + ";userId:" + userId + ";siteCode" + siteCode);
        OrderStatus orderStatus = new OrderStatus();
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        try {
            MasterOrderInfoExample masterOrderInfoExample = new MasterOrderInfoExample();
            masterOrderInfoExample.or().andMasterOrderSnEqualTo(masterOrderSn).andUserIdEqualTo(userId);
            List<MasterOrderInfo> masterOrderInfoList = masterOrderInfoMapper.selectByExample(masterOrderInfoExample);
            if (null == masterOrderInfoList || masterOrderInfoList.size() != 1) {
                apiReturnData.setMessage("订单数据异常，不能取消！");
                return apiReturnData;
            }

            Integer cancelCode = searchParam.getCancelCode();
            if (cancelCode == null) {
                cancelCode = 8011;
            }
            orderStatus.setCode(cancelCode.toString());
            orderStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.FRONT);
            int type = searchParam.getType();
            if (type == 2) {
                orderStatus.setType(ConstantValues.CREATE_RETURN.NO);
            } else {
                orderStatus.setType(ConstantValues.CREATE_RETURN.YES);
            }

            orderStatus.setAdminUser(userId);
            orderStatus.setMessage(searchParam.getRemarks());
            ReturnInfo returnInfo = orderCancelService.cancelOrderByMasterSn(masterOrderSn, orderStatus);
            if (returnInfo.getIsOk() > 0) {
                apiReturnData.setIsOk(Constant.OS_STR_YES);
                apiReturnData.setData(true);
            } else {
                apiReturnData.setMessage(returnInfo.getMessage());
            }
        } catch (Exception e) {
            apiReturnData.setMessage("平台取消订单异常！");
            logger.info("平台取消订单：masterOrderSn=" + masterOrderSn, e);
        }
        logger.info("前台订单取消 masterOrderSn：" + masterOrderSn + "完结");
        return apiReturnData;
    }

	/**
	 * 申请已支付
	 * @param searchParam 申请信息
	 * @return ApiReturnData<Boolean>
	 */
	@Override
	public ApiReturnData<Boolean> applyOrderPay(PageListParam searchParam) {

		ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);

		String masterOrderSn = searchParam.getOrderSn();
		try {
			String userId = searchParam.getUserId();

			MasterOrderInfoExample masterOrderInfoExample = new MasterOrderInfoExample();
			masterOrderInfoExample.or().andMasterOrderSnEqualTo(masterOrderSn).andUserIdEqualTo(userId);
			List<MasterOrderInfo> masterOrderInfoList = masterOrderInfoMapper.selectByExample(masterOrderInfoExample);
			if (null == masterOrderInfoList || masterOrderInfoList.size() != 1) {
				apiReturnData.setMessage("订单数据异常，不能操作！");
				return apiReturnData;
			}

			MasterOrderInfoExtendExample extendExample = new MasterOrderInfoExtendExample();
			extendExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<MasterOrderInfoExtend> infoExtends = masterOrderInfoExtendMapper.selectByExample(extendExample);
			if (infoExtends == null || infoExtends.size() <1) {
				apiReturnData.setMessage("订单信息异常");
				return apiReturnData;
			}

			// 已付款银行卡号
			String userBankNo = searchParam.getRemarks();
			MasterOrderInfoExtend extend = new MasterOrderInfoExtend();
			extend.setUserPayApply(2);
			if (StringUtil.isNotNull(userBankNo)) {
				extend.setUserBankNo(userBankNo);
			}
			masterOrderInfoExtendMapper.updateByExampleSelective(extend, extendExample);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
			apiReturnData.setData(true);
			apiReturnData.setMessage("操作成功");

			masterOrderActionService.insertOrderActionBySn(masterOrderSn, "提交已支付申请", userId);
		} catch (Exception e) {
			apiReturnData.setMessage("订单申请已支付异常！");
			logger.error("订单申请已支付：masterOrderSn=" + masterOrderSn, e);
		}

		return apiReturnData;
	}

    /**
     * 删除订单
     * @param searchParam
     * @return
     */
    @Override
    public ApiReturnData<Boolean> deleteMasterOrder(PageListParam searchParam) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk("0");
        try {
            if (searchParam == null) {
                apiReturnData.setMessage("参数为空");
                return apiReturnData;
            }

            String orderSn = searchParam.getOrderSn();
            if (StringUtils.isBlank(orderSn)) {
                apiReturnData.setMessage("订单号为空");
                return apiReturnData;
            }

            String userId = searchParam.getUserId();
            if (StringUtils.isBlank(userId)) {
                apiReturnData.setMessage("操作人不能为空");
                return apiReturnData;
            }

            String remarks = searchParam.getRemarks();
            if (StringUtils.isBlank(remarks)) {
                remarks = "删除订单";
            }

            MasterOrderInfoExample masterOrderInfoExample = new MasterOrderInfoExample();
            masterOrderInfoExample.or().andMasterOrderSnEqualTo(orderSn);
            List<MasterOrderInfo> masterOrderInfos = masterOrderInfoMapper.selectByExample(masterOrderInfoExample);
            if (masterOrderInfos == null || masterOrderInfos.size() < 1) {
                apiReturnData.setMessage("该订单不存在");
                return apiReturnData;
            }

            MasterOrderInfoExtendExample extendExample = new MasterOrderInfoExtendExample();
            extendExample.or().andMasterOrderSnEqualTo(orderSn);
            List<MasterOrderInfoExtend> infoExtends = masterOrderInfoExtendMapper.selectByExample(extendExample);
            if (infoExtends == null || infoExtends.size() <1) {
                apiReturnData.setMessage("该订单信息异常");
                return apiReturnData;
            }
            MasterOrderInfoExtend extend = new MasterOrderInfoExtend();
            extend.setIsDel(1);
            masterOrderInfoExtendMapper.updateByExampleSelective(extend, extendExample);
            apiReturnData.setIsOk("1");
            apiReturnData.setData(true);

            masterOrderActionService.insertOrderActionBySn(orderSn, remarks, userId);

        } catch (Exception e) {
            logger.error(searchParam.getOrderSn() + "删除失败", e);
            apiReturnData.setMessage("删除失败");
        }

        return apiReturnData;
    }

    /**
     * 删除退单
     * @param searchParam
     * @return
     */
    @Override
    public ApiReturnData<Boolean> deleteReturnOrder(PageListParam searchParam) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk("0");
        try {
            if (searchParam == null) {
                apiReturnData.setMessage("参数为空");
                return apiReturnData;
            }

            String orderSn = searchParam.getOrderSn();
            if (StringUtils.isBlank(orderSn)) {
                apiReturnData.setMessage("订单号为空");
                return apiReturnData;
            }

            String orderReturnSn = searchParam.getOrderReturnSn();
            if (StringUtils.isBlank(orderReturnSn)) {
                apiReturnData.setMessage("退单号为空");
                return apiReturnData;
            }

            String siteCode = searchParam.getSiteCode();
            if (StringUtils.isBlank(siteCode)) {
                apiReturnData.setMessage("站点编码为空");
                return apiReturnData;
            }

            String userId = searchParam.getUserId();
            if (StringUtils.isBlank(userId)) {
                apiReturnData.setMessage("操作人不能为空");
                return apiReturnData;
            }

            String remarks = searchParam.getRemarks();
            if (StringUtils.isBlank(remarks)) {
                remarks = "删除退单";
            }

            MasterOrderInfoExample masterOrderInfoExample = new MasterOrderInfoExample();
            masterOrderInfoExample.or().andMasterOrderSnEqualTo(orderSn);
            List<MasterOrderInfo> masterOrderInfos = masterOrderInfoMapper.selectByExample(masterOrderInfoExample);
            if (masterOrderInfos == null || masterOrderInfos.size() < 1) {
                apiReturnData.setMessage("该订单不存在");
                return apiReturnData;
            }

            OrderReturnExample orderReturnExample = new OrderReturnExample();
            orderReturnExample.or().andReturnSnEqualTo(orderReturnSn).andSiteCodeEqualTo(siteCode);
            OrderReturn orderReturn = new OrderReturn();
            orderReturn.setIsDel(1);
            orderReturn.setUpdateTime(new Date());
            orderReturnMapper.updateByExampleSelective(orderReturn, orderReturnExample);

            apiReturnData.setIsOk("1");
            apiReturnData.setData(true);

            OrderReturnAction orderReturnAction = new OrderReturnAction();
            orderReturnAction.setReturnSn(orderReturnSn);
            orderReturnAction.setActionUser(userId);
            orderReturnAction.setActionNote(remarks);
            orderReturnAction.setLogTime(new Date());
            orderReturnActionMapper.insertSelective(orderReturnAction);

        } catch (Exception e) {
            logger.error(searchParam.getOrderSn() + "删除失败", e);
            apiReturnData.setMessage("删除失败");
        }

        return apiReturnData;
    }

	/**
	 * 通过支付单号，获取订单编码
	 * @param paySn 支付单号
	 * @param siteCode 站点编码
	 * @return ApiReturnData<List<String>>
	 */
	@Override
	public ApiReturnData<List<String>> getMasterOrderSnByPaySn(String paySn, String siteCode) {
		logger.info("根据支付单号查询订单号：paySn="+paySn);
		ApiReturnData<List<String>> apiReturnData = new ApiReturnData<List<String>>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);

		try {
			List<String> masterPaySnList = new ArrayList<String>(Constant.DEFAULT_LIST_SIZE);
			if (paySn.indexOf(Constant.ORDER_PAY_MFK) >= 0) {
				MergeOrderPay mergePay = mergeOrderPayMapper.selectByPrimaryKey(paySn);
				String[] masterPaySns = mergePay.getMasterPaySn().split(",");
				masterPaySnList = Arrays.asList(masterPaySns);
			} else {
				masterPaySnList.add(paySn);
			}
			if (null == masterPaySnList || masterPaySnList.size() == 0) {
				apiReturnData.setMessage("数据异常或没有找到对应的订单号");
				return apiReturnData;
			}
			MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
			masterOrderPayExample.or().andMasterPaySnIn(masterPaySnList);
			List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);

			List<String> masterOrderSnList = new ArrayList<String>(Constant.DEFAULT_LIST_SIZE);
			for (MasterOrderPay masterOrderPay : masterOrderPayList) {
				masterOrderSnList.add(masterOrderPay.getMasterOrderSn());
			}
			apiReturnData.setIsOk(Constant.OS_STR_YES);
			apiReturnData.setData(masterOrderSnList);
		} catch (Exception e) {
			apiReturnData.setMessage("查询异常！");
			logger.info("根据支付单号查询订单号：paySn=" + paySn, e);
		}
		return apiReturnData;
	}

	/**
	 * 根据用户查询订单简单信息
	 * @param userId 用户id
	 * @param siteCode 站点编码
	 * @return ApiReturnData<List<MasterOrderInfo>>
	 */
	@Override
	public ApiReturnData<List<MasterOrderInfo>> getOrderSimpleInfoByUser(String userId, String siteCode) {
		logger.info("查询订单简易消息：userId="+userId);
		ApiReturnData<List<MasterOrderInfo>> apiReturnData = new ApiReturnData<List<MasterOrderInfo>>();
		try {
			apiReturnData.setIsOk(Constant.OS_STR_NO);
			MasterOrderInfoExample masterOrderInfoExample = new MasterOrderInfoExample(); 
			masterOrderInfoExample.or().andChannelCodeEqualTo(siteCode).andUserIdEqualTo(userId).limit(20);
			masterOrderInfoExample.setOrderByClause("master_order_sn desc");
			List<MasterOrderInfo> masterOrderInfoList =  masterOrderInfoMapper.selectByExample(masterOrderInfoExample);
			apiReturnData.setData(masterOrderInfoList);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
		} catch (Exception e) {
			apiReturnData.setMessage("查询异常！");
			logger.info("查询订单简易异常：userId=" + userId, e);
		}
		return apiReturnData;
	}

	/**
	 * 查询退单详情
	 * @param searchParam 参数
	 * 	orderReturnSn 退单编码
	 * 	userId 用户id
	 * 	siteCode 站点编码
	 * @return ApiReturnData<OrderReturnDetailInfo>
	 */
	@Override
	public ApiReturnData<OrderReturnDetailInfo> orderReturnDetailNew(PageListParam searchParam) {
		return orderReturnDetail(searchParam.getOrderReturnSn(), searchParam.getUserId(), searchParam.getSiteCode());
	}

	/**
	 * 查询换单详情
	 * @param searchParam
	 * 	orderSn 订单编码
	 * 	isHistory 是否历史
	 * 	userId 用户id
	 * 	siteCode 站点编码
	 * @return ApiReturnData
	 */
	@Override
	public ApiReturnData orderExchangesDetailNew(PageListParam searchParam) {
		return orderExchangesDetail(searchParam.getOrderSn(), searchParam.getIsHistory(),
				searchParam.getUserId(), searchParam.getSiteCode());
	}

	/**
	 * 查询订单详情
	 * @param searchParam 参数
	 *  orderSn 订单编码
	 *  isHistory 是否历史
	 *  userId 用户id
	 *  siteCode 站点编码
	 * @return ApiReturnData<OrderDetailInfo>
	 */
	@Override
	public ApiReturnData<OrderDetailInfo> orderInfoDetailNew(PageListParam searchParam) {
		return orderInfoDetail(searchParam.getOrderSn(), searchParam.getIsHistory(),
				searchParam.getUserId(), searchParam.getSiteCode());
	}

	@Override
	public ApiReturnData getOrderGoodsByOrderSnNew(PageListParam searchParam) {
		return getOrderGoodsByOrderSn(searchParam.getOrderSns(), searchParam.getSiteCode());
	}

	@Override
	public ApiReturnData getOrderGoodsByPaySnNew(PageListParam searchParam) {
		return getOrderGoodsByPaySn(searchParam.getPaySn(), searchParam.getSiteCode());
	}

	/**
	 * 查询用户订单类型
	 * @param searchParam 参数
	 *  userId 用户id
	 *  siteCode 站点编码
	 * @return ApiReturnData<UserOrderTypeNum>
	 */
	@Override
	public ApiReturnData<UserOrderTypeNum> getUserOrderTypeNew(PageListParam searchParam) {
		return getUserOrderType(searchParam.getUserId(), searchParam.getSiteCode());
	}

	/**
	 * 通过订单编码取消订单
	 * @param searchParam 参数
	 *  orderSn 订单编码
	 *  userId 用户id
	 *  siteCode 站点编码
	 * @return ApiReturnData<Boolean>
	 */
	@Override
	public ApiReturnData<Boolean> cancelOrderByMasterSnNew(PageListParam searchParam) {
	    return  cancelOrderByMasterSnAndCancel(searchParam);
//		return cancelOrderByMasterSn(searchParam.getOrderSn(), searchParam.getUserId(), searchParam.getSiteCode());
	}

	/**
	 * 通过支付单号，获取订单编码
	 * @param searchParam 参数
	 * @return ApiReturnData<List<String>>
	 */
	@Override
	public ApiReturnData getMasterOrderSnByPaySnNew(PageListParam searchParam) {
		return getMasterOrderSnByPaySn(searchParam.getPaySn(), searchParam.getSiteCode());
	}

	/**
	 * 用户订单转移
	 * @param searchParam 参数
	 *  userId 用户id
	 *  newUserId 新用户id
	 *  siteCode 站点编码
	 * @return
	 */
	@Override
	public ReturnInfo<Boolean> changeOrderUserNew(PageListParam searchParam) {
		return changeOrderUser(searchParam.getUserId(), searchParam.getNewUserId(), searchParam.getSiteCode());
	}

	/**
	 * 设置订单已评论
	 * @param searchParam 参数
	 * @return ReturnInfo<Boolean>
	 */
	@Override
	public ReturnInfo<Boolean> orderReviewNew(PageListParam searchParam) {
		return orderReview(searchParam.getOrderSn(), searchParam.isFlag(), searchParam.getSiteCode());
	}

	/**
	 * 确认收货
	 * @param searchParam 参数
	 *  orderSn 订单编码
	 *  invoiceNo 运单号
	 *  userId 用户id
	 *  siteCode 站点编码
	 * @return ReturnInfo<Boolean>
	 */
	@Override
	public ReturnInfo<Boolean> confirmReceiptNew(PageListParam searchParam) {
		return confirmReceipt(searchParam.getOrderSn(), searchParam.getInvoiceNo(),
				searchParam.getUserId(), searchParam.getSiteCode());
	}

	/**
	 * 根据用户查询订单简单信息
	 * @param searchParam 查询参数
	 * @return ApiReturnData<List<MasterOrderInfo>>
	 */
	@Override
	public ApiReturnData<List<MasterOrderInfo>> getOrderSimpleInfoByUserNew(PageListParam searchParam) {
		return getOrderSimpleInfoByUser(searchParam.getUserId(), searchParam.getSiteCode());
	}

	/**
	 * 查询用户未支付订单
	 * @param searchParam 参数
	 * userId 用户id
	 * siteCode 站点编码
	 * @return ApiReturnData<List<String>>
	 */
	@Override
	public ApiReturnData<List<String>> queryNoPayOrder(PageListParam searchParam) {
		logger.info("查询用户未支付订单：searchParam"+JSON.toJSONString(searchParam));
		ApiReturnData<List<String>> apiReturnData = new ApiReturnData<List<String>>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		try {
			if (StringUtils.isBlank(searchParam.getUserId())) {
				apiReturnData.setMessage("userId不能为空！");
				return apiReturnData;
			}
			if (StringUtil.isTrimEmpty(searchParam.getSiteCode())) {
				apiReturnData.setMessage("siteCode不能为空！");
				return apiReturnData;
			}
			if (StringUtils.isBlank(searchParam.getIsHistory())) {
				searchParam.setIsHistory("0");
			}
			MasterOrderInfoExample example = new MasterOrderInfoExample();
			MasterOrderInfoExample.Criteria criteria = example.or();
			criteria.andUserIdEqualTo(searchParam.getUserId());
			criteria.andChannelCodeEqualTo(searchParam.getSiteCode());
			criteria.andOrderStatusEqualTo((byte)Constant.OI_ORDER_STATUS_UNCONFIRMED);
			List<Byte> values = new ArrayList<Byte>(4);
			values.add(Constant.OI_PAY_STATUS_UNPAYED);
			values.add(Constant.OI_PAY_STATUS_PARTPAYED);
			criteria.andPayStatusIn(values);
			List<MasterOrderInfo> list = masterOrderInfoMapper.selectByExample(example);
			if (StringUtil.isListNotNull(list)) {
				List<String> strings = new ArrayList<String>(Constant.DEFAULT_LIST_SIZE);
				for (MasterOrderInfo master : list) {
					strings.add(master.getMasterOrderSn());
				}
				apiReturnData.setData(strings);
			} else {
				apiReturnData.setData(null);
			}
			apiReturnData.setIsOk(Constant.OS_STR_YES);
			apiReturnData.setMessage("success");
		} catch (Exception e) {
			logger.error("平台查询用户未支付订单异常：searchParam="+searchParam, e);
			apiReturnData.setMessage("平台查询订单列表异常:" + e.getMessage());
		}
		return apiReturnData;
	}

	/**
	 * 查询用户限购订单
	 * @param searchParam userId siteCode
	 * @return
	 */
	@Override
	public ApiReturnData<List<OrderGoodsInfo>> queryRestrictionOrder(PageListParam searchParam) {
		logger.info("查询用户限购订单：searchParam"+JSON.toJSONString(searchParam));
		ApiReturnData<List<OrderGoodsInfo>> apiReturnData = new ApiReturnData<List<OrderGoodsInfo>>();
		apiReturnData.setIsOk(Constant.OS_STR_NO);
		try {
			if (StringUtils.isBlank(searchParam.getUserId())) {
				apiReturnData.setMessage("userId不能为空！");
				return apiReturnData;
			}
			if (StringUtil.isTrimEmpty(searchParam.getSiteCode())) {
				apiReturnData.setMessage("siteCode不能为空！");
				return apiReturnData;
			}
			if (StringUtils.isBlank(searchParam.getIsHistory())) {
				searchParam.setIsHistory("0");
			}
			if (com.work.shop.oms.utils.StringUtil.isListNull(searchParam.getSkuSns())) {
				apiReturnData.setMessage("查询商品编码不能为空！");
				return apiReturnData;
			}
			Map<String, Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
			params.put("siteCode", searchParam.getSiteCode());
			params.put("shopCode", searchParam.getShopCode());
			params.put("startTime", TimeUtil.formatDate(searchParam.getStartTime()));
			params.put("endTime", TimeUtil.formatDate(searchParam.getEndTime()));
			params.put("userId", searchParam.getUserId());
			params.put("list", searchParam.getSkuSns());
			List<OrderGoodsInfo> list = defineOrderMapper.selectRestrictionGoodsByWhere(params);
			apiReturnData.setData(list);
			apiReturnData.setIsOk(Constant.OS_STR_YES);
			apiReturnData.setMessage("查询成功");
		} catch (Exception e) {
			logger.error("平台查询用户限购订单异常：searchParam=" + JSONObject.toJSONString(searchParam), e);
			apiReturnData.setMessage("平台查询限购订单列表异常:" + e.getMessage());
		}
		return apiReturnData;
	}

	/**
	 * 查询地区信息
	 * @param searchParam 查询参数
	 * @return OmsBaseResponse<SystemRegionArea>
	 */
	@Override
	public OmsBaseResponse<SystemRegionArea> querySystemRegionArea(SystemRegionArea searchParam) {
		OmsBaseResponse<SystemRegionArea> returnData = new OmsBaseResponse<SystemRegionArea>();
		returnData.setSuccess(false);
		returnData.setMessage("查询失败");
		
		logger.info("查询地区信息:" + JSONObject.toJSONString(searchParam));
		
		try {
			SystemRegionAreaExample systemRegionAreaExample = new SystemRegionAreaExample();
			Criteria criteria = systemRegionAreaExample.or();
			criteria.andParentIdEqualTo(searchParam.getParentId());
			
			systemRegionAreaMapper.selectByExample(systemRegionAreaExample);
		} catch (Exception e) {
			logger.error("查询地区信息异常:" + JSONObject.toJSONString(searchParam), e);
		}
		
		return returnData;
	}
	
	/**
	 * 获取未支付订单, 人工可以取消时间
	 * @param orderCreateTime
	 * @return
	 */
	private String getOrderCancelTime(String orderCreateTime) {
		int periodValue = getPeriodDetailValueByCache(1, Constant.OS_ORDER_CANCLE_FONT_PERIOD);
		if (periodValue == 0) {
			return orderCreateTime;
		}
		
		Date orderCreateDate = TimeUtil.parseDayDate(orderCreateTime, TimeUtil.YYYY_MM_DD_HH_MM_SS);
		Date orderCancelDate = TimeUtil.getBeforeSecond(orderCreateDate, periodValue);
		
		return TimeUtil.format(orderCancelDate, TimeUtil.YYYY_MM_DD_HH_MM_SS);
	}
	
	/**
	 * 获取未支付订单,人工取消周期
	 * @param period
	 * @param type
	 * @return
	 */
	private int getPeriodDetailValueByCache(int period, String type) {
		String secpValue = null;
		try {
			secpValue = redisClient.get(Constant.OS_ORDER_PERIOD + type);
		} catch (Exception e) {
			logger.error("获取" + Constant.OS_ORDER_PERIOD + type + "异常:", e);
		}
		
		if (StringUtils.isNotBlank(secpValue)) {
			return Integer.valueOf(secpValue);
		}
		
		int periodValue = getPeriodDetailValue(period, type);
		try {
			redisClient.set(Constant.OS_ORDER_PERIOD + type, periodValue + "");
		} catch (Throwable e) {
			logger.error("redis is null error ," + e);
		}
		return periodValue;
	}
	
	/**
	 * 获取订单周期配置值
	 * @param period
	 * @param type
	 * @return
	 */
	private int getPeriodDetailValue(int period, String type) {
		OrderPeriodDetail detail = orderPeriodDetailService.selectByPeriodAndType(period, type);
		if (detail != null) {
			return detail.getPeriodValue();
		}
		return 0;
	}
}
