package com.work.shop.oms.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.cart.api.StoreInfoCartApi;
import com.work.shop.cart.api.bean.StoreInfoBean;
import com.work.shop.cart.api.bean.service.ShopCartRequest;
import com.work.shop.cart.api.bean.service.ShopCartResponse;
import com.work.shop.cart.api.bean.service.StoreInfoRequest;
import com.work.shop.oms.api.bean.OrderContractBean;
import com.work.shop.oms.api.bean.OrderContractRequest;
import com.work.shop.oms.api.param.bean.OrderItemQueryExample;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.channel.bean.OfflineStoreInfo;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.dao.define.*;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.request.CustomDefineQueryRequest;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.order.response.CustomDefineQueryResponse;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.response.OrderQueryResponse;
import com.work.shop.oms.order.service.OrderQueryService;
import com.work.shop.oms.rider.service.RiderDistributeService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.OrderReturnListVO;
import com.work.shop.oms.vo.OrderReturnSearchExample;
import com.work.shop.oms.vo.OrderShipVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 订单退单查询管理
 * @author lemon
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {
	
	private static final Logger logger = Logger.getLogger(OrderQueryServiceImpl.class);

	@Resource
	private OrderInfoSearchMapper orderInfoSearchMapper;

	@Resource
	private OrderCustomDefineMapper orderCustomDefineMapper;

	@Resource
	private OrderReturnSearchMapper orderReturnSearchMapper;

	@Resource
	private GoodsReturnChangePageListMapper goodsReturnChangePageListMapper;

	@Resource
	private GoodsReturnChangeActionMapper goodsReturnChangeActionMapper;

	@Resource
	private GoodsReturnChangeMapper goodsReturnChangeMapper;

	//@Resource
	private StoreInfoCartApi storeInfoCartAPI;
	
	@Resource(name="riderDistributeService")
	private RiderDistributeService riderDistributeService;
	
	@Resource
	private OrderDistributeDefineMapper orderDistributeDefineMapper;
	
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	
	@Resource
	private OrderReturnGoodsDetailMapper orderReturnGoodsDetailMapper;
	
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	
	@Resource
	private DefineOrderMapper defineOrderMapper;
	
	@Resource(name="orderDistributeProcessJmsTemplate")
	private JmsTemplate orderDistributeProcessJmsTemplate;

	@Resource
	private GoodsReturnChangeDetailMapper goodsReturnChangeDetailMapper;
	
	@Resource
	private MasterOrderGoodsDetailMapper masterOrderGoodsDetailMapper;

	@Resource
	private SystemPaymentMapper systemPaymentMapper;

	/**
	 * 订单列表查询
	 * @param request 查询参数
	 * @return OrderQueryResponse
	 */
	@Override
	public OrderQueryResponse orderQuery(OrderQueryRequest request) {

		OrderQueryResponse response = new OrderQueryResponse();
		response.setSuccess(false);
		response.setMessage("订单信息查询失败");
		if (request.getPageNo() == null || request.getPageNo() < 1) {
			response.setMessage("订单信息查询页码不能为空或小于1");
			return response;
		}
		if (request.getPageSize() == null || request.getPageSize() < 1) {
			response.setMessage("订单信息查询每页条目不能为空或小于1");
			return response;
		}
		int start = (request.getPageNo() - 1) * request.getPageSize();
		int limit = request.getPageSize();
		// 编辑SQL查询参数
		OrderItemQueryExample example = new OrderItemQueryExample();
		example.setOrderByClause("oi.add_time desc");
		OrderItemQueryExample.Criteria criteria = example.or();
		criteria.limit(start, limit);
		example.setMainOrderInfo(true);
		boolean condition = true;
		example.setUserOg(true);
		criteria.andOrderAndAddress();
		criteria.andOrderAndExtend();
		criteria.andOrderAndPay();
		
		// 导出数据列表类型 0订单列表、1订单商品列表
		int exportType = request.getExportType();
		if (exportType == 1) {
			criteria.andOrderAndGoods();
		}
		// 订单所属站点
		if (StringUtil.isNotEmpty(request.getChannelCode()) && !request.getChannelCode().equals("-1")) {
			condition = false;
			String[] channelCodes = request.getChannelCode().split(",");
			if (channelCodes.length == 1) {
				criteria.andChannelCodeEqualTo(request.getChannelCode());
			} else {
				criteria.andChannelCodeIn(Arrays.asList(channelCodes));
			}
		}
		// 订单渠道店铺
		if (StringUtil.isNotEmpty(request.getShopCode()) && !Constant.PLEASE_SELECT_STRING.equals(request.getShopCode())) {
			condition = false;
			String[] shopCodeData = request.getShopCode().split(Constant.STRING_SPLIT_COMMA);
			if (shopCodeData.length == 1) {
				criteria.andOrderFromEqualTo(request.getShopCode());
			} else {
				criteria.andOrderFromIn(Arrays.asList(shopCodeData));
			}
		}
		// 订单线下店铺
		if (StringUtil.isNotEmpty(request.getStoreCode()) && !request.getStoreCode().equals("-1")) {
			condition = false;
			String arr[] = request.getStoreCode().split(",");
			if (arr.length == 1) {
				criteria.andStoreCodeEqualTo(request.getStoreCode());
			} else {
				criteria.andStoreCodeIn(Arrays.asList(arr));
			}
		}
		// 主订单号
		if (StringUtil.isNotEmpty(request.getMasterOrderSn())) {
			condition = false;
			criteria.andMasterOrderSnLike("%" + request.getMasterOrderSn().trim() + "%");
		}
		/*** 外部交易号 ***/
		if (StringUtil.isNotNull(request.getOuterOrderSn())) {
			condition = false;
			criteria.andOrderOutSnEqualTo(request.getOuterOrderSn());
		}
		/*** 订单类型 ***/
		if (null != request.getOrderType() && request.getOrderType() >= 0) {
			condition = false;
			criteria.andOrderTypeEqualTo(request.getOrderType());
		}
		/*** 订单来源 ***/
		if (StringUtil.isNotNull(request.getReferer()) && !"-1".equals(request.getReferer())) {
			condition = false;
			criteria.andRefererEqualTo(request.getReferer());
		}
		/*** 订单状态 ***/
		if (null != request.getOrderStatus() && request.getOrderStatus() >= 0) {
			condition = false;
			criteria.andOrderStatusEqualTo(request.getOrderStatus().byteValue());
		}
		if (null != request.getPayStatus() && request.getPayStatus() >= 0) {
			criteria.andPayStatusEqualTo(request.getPayStatus().byteValue());
		}
		if (null != request.getShipStatus() && request.getShipStatus() >= 0) {
			condition = false;
			criteria.andShipStatusEqualTo(request.getShipStatus().byteValue());
		}

		if (request.getTimeType() != null 
				&& (StringUtil.isNotNull(request.getStartTime()) 
					|| StringUtil.isNotNull(request.getEndTime())) ) {
			Date startTime = DateTimeUtils.parseStr(request.getStartTime());
			Date endTime = DateTimeUtils.parseStr(request.getEndTime());
			//下单时间
			if (request.getTimeType() == 0) {
				if (StringUtil.isNotNull(request.getStartTime())
						&& StringUtil.isNotNull(request.getEndTime())) {
					condition = false;
					criteria.andAddTimeBetween(startTime,endTime);
				} else if (StringUtil.isNotNull(request.getStartTime())) {
					condition = false;
					criteria.andAddTimeGreaterThanOrEqualTo(startTime);
				} else if (StringUtil.isNotNull(request.getEndTime())) {
					condition = false;
					criteria.andAddTimeLessThanOrEqualTo(startTime);
				}
			}
			//确认时间
			if (request.getTimeType() == 1) {
				if (StringUtil.isNotNull(request.getStartTime())
						&& StringUtil.isNotNull(request.getEndTime())) {
					condition = false;
					criteria.andConfirmTimeBetween(startTime, endTime);
				} else if (StringUtil.isNotNull(request.getStartTime())) {
					condition = false;
					criteria.andConfirmTimeGreaterThanOrEqualTo(startTime);
				} else if (StringUtil.isNotNull(request.getEndTime())) {
					condition = false;
					criteria.andConfirmTimeLessThanOrEqualTo(endTime);
				}
			}
		}
		
		// 下单人
		if (StringUtil.isNotNull(request.getUserId())) {
			condition = false;
			criteria.andUserNameEqualTo(request.getUserId());
		}
		
		// 收货人
		if (StringUtil.isNotNull(request.getReceiverName())) {
			example.setUserOAI(true);
			condition = false;
			criteria.andConsigneeEqualTo(request.getReceiverName());
		}
		
		// 收货人电话
		if (StringUtil.isNotEmpty(request.getReceiverTel())) {
			example.setUserOAI(true);
			condition = false;
			criteria.andTelEqualTo(request.getReceiverTel());
		}
		
		// 收货人手机
		if (StringUtil.isNotEmpty(request.getReceiverMobile())) {
			example.setUserOAI(true);
			condition = false;
			//criteria.andMobileEqualTo(request.getReceiverMobile());
			criteria.andMobileLike("%" + request.getReceiverMobile() + "%");
		}
		
		// BD编码
		String insteadUserId = request.getInsteadUserId();
		if (StringUtil.isNotEmpty(insteadUserId)) {
			condition = false;
			criteria.andInsteadUserIdEqualTo(insteadUserId);
		}
		
		// 问题单状态
		if (request.getQuestionStatus() != null && request.getQuestionStatus().intValue() != -1) {
			condition = false;
			criteria.andQuestionStatusEqualTo(request.getQuestionStatus());
		}
		
		// 拆单状态
		if (request.getSplitStatus() != null && request.getSplitStatus().intValue() != -1) {
			condition = false;
			criteria.andSplitStatusEqualTo(request.getSplitStatus().byteValue());
		}

		//订单有效、隐藏、全部状态
		if (null != request.getOrderView()) {
			if (request.getOrderView() == 0) {//默认显示有效订单
				criteria.andOrderStatusNotEqualTo((byte)2);
			} else if (2 == request.getOrderView()) {//显示隐藏订单
				condition = false;
				criteria.andOrderStatusEqualTo((byte)2);
			} else if( 1  == request.getOrderView()){//显示全部订单
				condition = false;
			}
		}
		if (condition) {
			//显示一周内订单
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -7);
			Date date = calendar.getTime();
			criteria.andAddTimeGreaterThanOrEqualTo(date);
		}

		//支付方式
        String payId = request.getPayId();
		if (StringUtils.isNotBlank(payId)) {
            criteria.andPayIdEqualTo(payId);
        }

        //公司id
        String companyId = request.getCompanyId();
        if (StringUtils.isNotBlank(companyId)) {
            List<String> companyIdList = Arrays.asList(companyId.split(","));
            criteria.andCompanyCodeIn(companyIdList);
        }

        //公司类型
        Integer companyType = request.getCompanyType();
        if (companyType != null) {
            criteria.andCompanyTypeEqualTo(companyType);
        }

        //支付状态不等于
        Integer payNotStatus = request.getPayNotStatus();
        if (payNotStatus != null && payNotStatus >= 0) {
            criteria.andPayStatusNotEqualTo(payNotStatus.byteValue());
        }

        try {
			int totalProperty = 0;
			if (exportType == 1) {
				// 订单商品列表
				totalProperty = orderInfoSearchMapper.countOrderGoodsByExample(example);
				if (totalProperty > 0) {
					List<OrderGoodsItem> orderGoodsItems = orderInfoSearchMapper.selectOrderGoodsByExample(example);
					response.setOrderGoodsItems(orderGoodsItems);
				}
			} else {
				// 订单列表
				List<OrderItem> orderItems = orderInfoSearchMapper.selectOrderItemByExample(example);
				totalProperty = orderInfoSearchMapper.countOrderItemByExample(example);
				response.setOrderItems(orderItems);
			}
			response.setSuccess(true);
			response.setTotalProperty(totalProperty);
			response.setMessage("查询订单列表成功");
		} catch (Exception e) {
			logger.error("订单查询异常: " + e.getMessage(), e);
			response.setMessage("订单查询失败: " + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单列表查询
	 * @param request 查询参数
	 * @return OmsBaseResponse<OrderReturnListVO>
	 */
	@Override
	public OmsBaseResponse<OrderReturnListVO> orderReturnQuery(OmsBaseRequest<OrderReturnListVO> request) {
		OmsBaseResponse<OrderReturnListVO> response = new OmsBaseResponse<OrderReturnListVO>();
		response.setSuccess(false);
		response.setMessage("退单信息查询失败");
		if (request.getPageNo() == null || request.getPageNo() < 1) {
			response.setMessage("退单信息查询页码不能为空或小于1");
			return response;
		}
		if (request.getPageSize() == null || request.getPageSize() < 1) {
			response.setMessage("退单信息查询每页条目不能为空或小于1");
			return response;
		}
		int start = (request.getPageNo() - 1) * request.getPageSize();
		int limit = request.getPageSize();
		
		OrderReturnListVO model = request.getData();
		
		OrderReturnSearchExample example = new OrderReturnSearchExample();
		OrderReturnSearchExample.Criteria criteria = example.or();
		
		// 默认退单时间  降序
		example.setOrderByClause("orn.add_time desc");
		
		criteria.limit(start, limit);
		boolean condition = true;
		
		if (null != model) {
			model.setSelectTimeType("addTime");
			if (StringUtil.isNotNull(model.getListDataType())) {
				if (model.getListDataType().equals("newDate")) {
					example.setListDataType(true);
				} else if(model.getListDataType().equals("historyDate")) {
					example.setListDataType(false);
				}
			} else {
				example.setListDataType(true);
			}
	
			//退单时间  升序和降序
			if ("addTime".equals(model.getSort())) {
				if ("ASC".equalsIgnoreCase(model.getDir())) {
					example.setOrderByClause("orn.add_time asc");
				} 
			}
			
			if (StringUtil.isNotBlank(model.getMasterOrderSn())) {
				condition = false;
				criteria.andMasterOrderSnEqualTo(model.getMasterOrderSn());
			}
			
			//收件人
			if (StringUtil.isNotBlank(model.getConsignee())) {
				condition = false;
				criteria.andConsigneeLike("%"+model.getConsignee()+"%");
			}
			
			//是否退款：0.无须退款；1:需要退款',
			if (null !=  model.getHaveRefund() && -1 != model.getHaveRefund()) {
				criteria.andHaveRefundEqualTo( model.getHaveRefund());
			}
			
			//退单有效、隐藏、全部状态
			if (null != model.getOrderView()) {
				if (model.getOrderView() == 0) {
					List<Byte> list = new ArrayList<Byte>();
					list.add((byte) 4);
					list.add((byte) 10);
					criteria.andOrderReturnOrderStatusNotIn(list);
                    criteria.andBackBalanceEqualTo((byte) 0);
				} else if (model.getOrderView() == 2) {
					List<Byte> list = new ArrayList<Byte>();
					list.add((byte) 4);
					list.add((byte) 10);
					criteria.andOrderReturnOrderStatusIn(list);
				}
			}
			
			//是否收到货   isGoodReceived
			if (null != model.getIsGoodReceived() && -1 != model.getIsGoodReceived()) {
				condition = false;
				criteria.andIsGoodReceivedEqualTo(model.getIsGoodReceived());
			}
			
			//是否入库  checkinStatus
			if (null != model.getCheckinStatus() && -1 != model.getCheckinStatus()) {
				condition = false;
				criteria.andCheckinStatusEqualTo(model.getCheckinStatus());
			}
			
			//质检状态  	qualityStatus
			if (null != model.getQualityStatus() && -1 != model.getQualityStatus()) {
				condition = false;
				criteria.andQualityStatusEqualTo(model.getQualityStatus());
			}
			
			//退货人手机号
			if (StringUtil.isNotNull(model.getReturnMobile()) && 11 ==model.getReturnMobile().length()) {
				condition = false;
				String encMobile = model.getReturnMobile();
				List<String> mobiles = new ArrayList<String>();
				mobiles.add(model.getReturnMobile());
				mobiles.add(encMobile);
				//criteria.andMobileIn(mobiles);
				criteria.andReturnMobileIn(mobiles);
			}
			
			//退货入库
			if (StringUtil.isNotNull(model.getWarehouseCode()) && ! "-1".equals(model.getWarehouseCode())) {
				condition = false;
				criteria.andWarehouseCodeEqualTo(model.getWarehouseCode());
			}

			if (StringUtil.isNotNull(model.getReturnSn())) {
				condition = false;
				criteria.andReturnSnEqualTo(model.getReturnSn());
			}
			
			if (StringUtil.isNotNull(model.getRelatingOrderSn())) {
				condition = false;
				criteria.andRelatingOrderSnEqualTo(model.getRelatingOrderSn());
			}

			//交货单
			if (StringUtil.isNotNull(model.getOrderSn())) {
				condition = false;
				criteria.andOrderSnEqualTo(model.getOrderSn());
			}
			
			if (StringUtil.isNotNull(model.getRelatingOrderSn())) {
				condition = false;
				criteria.andRelatingOrderSnEqualTo(model.getRelatingOrderSn());
			}
			
			if (StringUtil.isNotNull(model.getOrderOutSn())) {
				condition = false;
				criteria.andOrderOutSnEqualTo(model.getOrderOutSn());
			}
			
			if (null != model.getReturnOrderStatus() && model.getReturnOrderStatus() >= 0) {
				condition = false;
				criteria.andReturnOrderStatusEqualTo(model.getReturnOrderStatus());
			}
			
			if (StringUtil.isNotNull(model.getReferer()) && !Constant.PLEASE_SELECT_STRING.equals(model.getReferer())) {
				condition = false;
				if (!Constant.PLEASE_SELECT_STRING.equals(model.getReferer())) {
					criteria.andRefererLike(model.getReferer());
				}
			}
			
			if (null != model.getReturnPay() && model.getReturnPay() >= 0) {
				condition = false;
				criteria.andReturnPayEqualTo(model.getReturnPay());
			}
			
			if (null != model.getRelatingOrderType() && model.getRelatingOrderType() >= 0) {
				condition = false;
				criteria.andRelatingOrderTypeEqualTo(model.getRelatingOrderType());
			}
			
			if (StringUtil.isNotNull(model.getUserName())) {
				condition = false;
				criteria.andUserNameLike("%" + model.getUserName() + "%");
			}
		
			//订单状态
			if (null != model.getOrderOrderStatus() && -1 != model.getOrderOrderStatus()) {
				condition = false;
				criteria.andOrderOrderStatusEqualTo(model.getOrderOrderStatus());
			}
			
			//订单财务状态
	        if (null != model.getOrderPayStatus() && -1 != model.getOrderPayStatus()) {
	        	condition = false;
	        	criteria.andOrderPayStatusEqualTo(model.getOrderPayStatus());
			}
			
	    	//订单物流状态
	        if (null != model.getOrderShipStatus() && -1 != model.getOrderShipStatus()) {
	        	condition = false;
	        	criteria.andOrderShipStatusEqualTo(model.getOrderShipStatus());
			}
	        //退货11位码 
			if (StringUtil.isNotNull(model.getSkuSn())) {
				condition = false;
			//	example.setUserOg(true);
				if ( 11 == model.getSkuSn().length()) {	
					criteria.andCustomCodeEqualTo(model.getSkuSn());
				} else if(6 == model.getSkuSn().length()) {
					criteria.andCustomCodeLike(model.getSkuSn());
				}
			}
	 
			//退单物流状态
			if (null !=  model.getShipStatus() &&  -1 != model.getShipStatus()) {
				condition = false;
				criteria.andShipStatusEqualTo(model.getShipStatus());
			}
			
			//回退客服
			if (null !=  model.getBackToCs() &&  -1 != model.getBackToCs()) {
				condition = false;
				criteria.andBackToCsEqualTo(model.getBackToCs());
			}

			if (null != model.getStReturnTotalFee() && null != model.getEnReturnTotalFee()) {
				condition = false;
				criteria.andReturnTotalFeeBetween(model.getStReturnTotalFee(), model.getEnReturnTotalFee());
			} else if (null != model.getStReturnTotalFee()) {
				condition = false;
				criteria.andReturnTotalFeeGreaterThanOrEqualTo(model.getStReturnTotalFee());
			} else if(null != model.getEnReturnTotalFee()) {
				condition = false;
				criteria.andReturnTotalFeeLessThanOrEqualTo(model.getStReturnTotalFee());
			}
			
			if (StringUtil.isNotNull(model.getReturnInvoiceNo())) {
				condition = false;
				criteria.andReturnInvoiceNoEqualTo(model.getReturnInvoiceNo());
			}
			
			if (null != model.getPayStatus() && model.getPayStatus() >= 0) {
				condition = false;
				criteria.andPayStatusEqualTo(model.getPayStatus());
			}
			
			//结算时间
			if (null != model.getSelectTimeType() && model.getSelectTimeType().equals("clearTime")) {
				if (StringUtil.isNotNull(model.getStartTime()) && StringUtil.isNotNull(model.getEndTime())) {
					condition = false;
					criteria.andClearTimeBetween(DateTimeUtils.parseStr(model.getStartTime()), DateTimeUtils.parseStr(model.getEndTime()));
				} else if(StringUtil.isNotNull(model.getStartTime())) {
					condition = false;
					criteria.andClearTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(model.getStartTime()));	
				} else if(StringUtil.isNotNull(model.getEndTime())) {
					condition = false;
					criteria.andClearTimeLessThanOrEqualTo(DateTimeUtils.parseStr(model.getEndTime()));	
				}
			}
			
			//生成退单时间
			if (null != model.getSelectTimeType() && model.getSelectTimeType().equals("addTime")) {
				if (StringUtil.isNotNull(model.getStartTime()) && StringUtil.isNotNull(model.getEndTime())) {
					condition = false;
					criteria.andAddTimeBetween(DateTimeUtils.parseStr(model.getStartTime()) ,DateTimeUtils.parseStr(model.getEndTime()));
				} else if(StringUtil.isNotNull(model.getStartTime())) {
					condition = false;
					criteria.andAddTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(model.getStartTime()));	
				} else if(StringUtil.isNotNull(model.getEndTime())) {
					condition = false;
					criteria.andAddTimeLessThanOrEqualTo(DateTimeUtils.parseStr(model.getEndTime()));	
				}
			}
			
			//入库时间
			if (null != model.getSelectTimeType() && model.getSelectTimeType().equals("checkinTime")) {
				if (StringUtil.isNotNull(model.getStartTime()) && StringUtil.isNotNull(model.getEndTime())) {
					condition = false;
					criteria.andCheckinTimeBetween(DateTimeUtils.parseStr(model.getStartTime()) ,DateTimeUtils.parseStr(model.getEndTime()));
				} else if (StringUtil.isNotNull(model.getStartTime())) {
					condition = false;
					criteria.andCheckinTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(model.getStartTime()));	
				} else if (StringUtil.isNotNull(model.getEndTime())) {
					condition = false;
					criteria.andCheckinTimeLessThanOrEqualTo(DateTimeUtils.parseStr(model.getEndTime()));	
				}
			}
			
			//确定时间
			if (null != model.getSelectTimeType() && model.getSelectTimeType().equals("confirm_time")) {
				if (StringUtil.isNotNull(model.getStartTime()) && StringUtil.isNotNull(model.getEndTime())) {
					condition = false;
					criteria.andConfirmTimeBetween(DateTimeUtils.parseStr(model.getStartTime()), DateTimeUtils.parseStr(model.getEndTime()));
				} else if(StringUtil.isNotNull(model.getStartTime())) {
					condition = false;
					criteria.andConfirmTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(model.getStartTime()));	
				} else if(StringUtil.isNotNull(model.getEndTime())) {
					condition = false;
					criteria.andConfirmTimeLessThanOrEqualTo(DateTimeUtils.parseStr(model.getEndTime()));	
				}
			}
			//处理状态 （0无操作，1退货，2修补，3销毁，4换货）',
			if (null != model.getProcessType() && model.getProcessType() >= 0) {
				condition = false;
				criteria.andProcessTypeEqualTo(model.getProcessType());
			}
			
			// 回退客服
			if (null != model.getBackToCs() && model.getBackToCs() >= 0) {
				criteria.andBackToCsEqualTo(model.getBackToCs());
			}
			
			// 退单类型：1退货单、2拒收入库单、3普通退款单 4额外退款单、5失货退货单
			if (null != model.getReturnType() && model.getReturnType() >= 0) {
				// 退单类型
				if (model.getReturnType() > -1) {
					condition = false;
					if (model.getReturnType() == 99) {
						// 额外退款单类型
						criteria.andReturnTypeEqualTo((byte) 4);
					} else {
						criteria.andReturnTypeEqualTo(model.getReturnType());
					}
				}
			}
			
			//'1,预付款，2保证金',
			if (null != model.getReturnSettlementType() && model.getReturnSettlementType() >= 0) {
				condition = false;
				criteria.andReturnSettlementTypeEqualTo(model.getReturnSettlementType());
			}
	
			// 退单原因
			if (StringUtil.isNotNull(model.getReturnReason())) {
				if(!model.getReturnReason().equals("-1")){
					condition = false;
					criteria.andReturnReasonEqualTo(model.getReturnReason());
				}
			}
			// 订单所属站点
			if (StringUtil.isNotEmpty(model.getOrderFromSec()) && !model.getOrderFromSec().equals("-1")) {
				condition = false;
				String arr[] = model.getOrderFromSec().split(",");
				if (arr.length == 1) {
					criteria.andSiteCodeEqualTo(arr[0]);
				} else {
					criteria.andSiteCodeIn(Arrays.asList(arr));
				}
			}
			// 渠道店铺
			if (StringUtil.isNotEmpty(model.getOrderFrom()) && !model.getOrderFrom().equals("-1")) {
				String arr[] = model.getOrderFrom().split(",");
				if (arr.length == 1) {
					criteria.andShopCodeEqualTo(arr[0]);
				} else {
					criteria.andShopCodeIn(Arrays.asList(arr));
				}
			}
			
			// 线下店铺
			if (StringUtil.isNotEmpty(model.getStoreCode()) && !model.getStoreCode().equals("-1")) {
				String arr[] = model.getStoreCode().split(",");
				if (arr.length == 1) {
					criteria.andStoreCodeEqualTo(arr[0]);
				} else {
					criteria.andStoreCodeIn(Arrays.asList(arr));
				}
			}
			if (condition) {
				//显示一周内订单
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_YEAR, -7);
				Date date = calendar.getTime();
				criteria.andAddTimeGreaterThanOrEqualTo(date);
			}
		}
		
		// 查询类型 0退单列表、1退单商品列表
		int searchType = model.getSearchType();
		
		// 总数
		int count = 0;
		// 列表明细
		List<OrderReturnListVO> list = null;
		if (searchType == 0) {
			count = orderReturnSearchMapper.countOrderReturnListByExample(example);
			list = orderReturnSearchMapper.selectOrderReturnListByExample(example);
		} else if (searchType == 1) {
			count = orderReturnSearchMapper.countOrderReturnGoodsListByExample(example);
			logger.info("countOrderReturnGoodsListByExample:" + count);
			list = orderReturnSearchMapper.selectOrderReturnGoodsListByExample(example);
		}

        response.setTotalProperty(count);
        response.setSuccess(true);
        response.setMessage("查询成功");
		if (StringUtil.isListNull(list)) {
            response.setList(list);
            return response;
        }

        Set<Byte> payIdSet = new HashSet<Byte>();
        for (OrderReturnListVO vo : list) {
            Integer returnPay = vo.getReturnPay();
            if (returnPay == null) {
                continue;
            }
            payIdSet.add(returnPay.byteValue());
        }
        List<Byte> payIdList = new ArrayList<Byte>();
        payIdList.addAll(payIdSet);
        Map<String, SystemPayment> systemPaymentMap = getSystemPaymentMap(payIdList);
        if (systemPaymentMap == null) {
            systemPaymentMap = new HashMap<String, SystemPayment>();
        }

        for (OrderReturnListVO vo : list) {
            Integer returnPay = vo.getReturnPay();
            if (returnPay == null) {
                continue;
            }
            SystemPayment systemPayment = systemPaymentMap.get(returnPay.toString());
            String payName = returnPay.toString();
            if (systemPayment != null) {
                payName = systemPayment.getPayName();
            }
            vo.setReturnPayStr(payName);
        }

        response.setList(list);
        return response;
	}

    /**
     * 获取支付方式
     * @param payIdList
     * @return
     */
	private Map<String, SystemPayment> getSystemPaymentMap(List<Byte> payIdList) {
	    if (StringUtil.isListNull(payIdList)) {
	        return null;
        }
        SystemPaymentExample paymentExample = new SystemPaymentExample();
        paymentExample.or().andPayIdIn(payIdList);
        List<SystemPayment> systemPayments = systemPaymentMapper.selectByExample(paymentExample);
        if (StringUtil.isListNull(systemPayments)) {
            return null;
        }

        Map<String, SystemPayment> map = new HashMap<String, SystemPayment>();
        for (SystemPayment payment : systemPayments) {
            map.put(payment.getPayId().toString(), payment);
        }

        return map;
    }

	/**
	 * 自定义编码列表
	 * @param request 查询参数
	 * @return CustomDefineQueryResponse
	 */
	@Override
	public CustomDefineQueryResponse customDefineQuery(CustomDefineQueryRequest request) {
		logger.info("用户自定义编码查询request:" + JSON.toJSONString(request));
		CustomDefineQueryResponse response = new CustomDefineQueryResponse();
		response.setSuccess(false);
		response.setMessage("订单信息查询失败");
		try {
			OrderCustomDefineExample defineExample = new OrderCustomDefineExample();
			OrderCustomDefineExample.Criteria criteria = defineExample.or();
			if (request != null) {
				if (null != request.getType()) {
					criteria.andTypeEqualTo(request.getType().shortValue());
				}
				if (StringUtil.isNotEmpty(request.getCode())) {
					criteria.andCodeEqualTo(request.getCode());
				}
			}
			List<OrderCustomDefine> list = orderCustomDefineMapper.selectByExample(defineExample);
			response.setSuccess(true);
			response.setTotalProperty(0);
			response.setMessage("success");
			if (StringUtil.isListNotNull(list)) {
				List<CustomDefine> defines = new ArrayList<CustomDefine>();
				for (OrderCustomDefine item : list) {
					CustomDefine define = new CustomDefine();
					define.setCode(item.getCode());
					define.setName(item.getName());
					define.setType(item.getType().intValue());
					defines.add(define);
				}
				response.setTotalProperty(list.size());
				response.setCustomDefines(defines);
			}
		} catch (Exception e) {
			logger.error("用户自定义编码查询失败：" + e.getMessage(), e);
			response.setMessage("用户自定义编码查询失败：" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单申请列表查询
	 * @param request 查询参数
	 * @return OmsBaseResponse<GoodsReturnChangeBean>
	 */
	@Override
	public OmsBaseResponse<GoodsReturnChangeBean> orderReturnApplyQuery(OmsBaseRequest<GoodsReturnChangeVO> request) {
		OmsBaseResponse<GoodsReturnChangeBean> response = new OmsBaseResponse<GoodsReturnChangeBean>();
		response.setSuccess(false);
		response.setMessage("订单信息查询失败");
		if (request.getPageNo() == null || request.getPageNo() < 1) {
			response.setMessage("订单信息查询页码不能为空或小于1");
			return response;
		}
		if (request.getPageSize() == null || request.getPageSize() < 1) {
			response.setMessage("订单信息查询每页条目不能为空或小于1");
			return response;
		}
		int start = (request.getPageNo() - 1) * request.getPageSize();
		int limit = request.getPageSize();
		try {
			GoodsReturnChangeVO model = request.getData();
			GoodsReturnChangePageListExample example = queryList(model);
			GoodsReturnChangePageListExample.Criteria criteria = example.or();
			criteria.limit(start, limit);
			List<GoodsReturnChangeBean> list = goodsReturnChangePageListMapper.selectByExample(example);
			int count = goodsReturnChangePageListMapper.countByExample(example);
			response.setList(list);
			response.setTotalProperty(count);
			response.setSuccess(true);
			response.setMessage("查询成功");
		} catch (Exception e) {
			logger.error("查询失败" + e.getMessage(), e);
			response.setMessage("查询失败" + e.getMessage());
		}
		return response;
	}

	/**
	 * 退单申请单列表(新)
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChangeBean>
	 */
    @Override
    public OmsBaseResponse<GoodsReturnChangeBean> orderReturnApplyQueryNew(OmsBaseRequest<GoodsReturnChangeVO> request) {
        OmsBaseResponse<GoodsReturnChangeBean> response = new OmsBaseResponse<GoodsReturnChangeBean>();
        response.setSuccess(false);
        response.setMessage("订单信息查询失败");
        if (request.getPageNo() == null || request.getPageNo() < 1) {
            response.setMessage("订单信息查询页码不能为空或小于1");
            return response;
        }
        if (request.getPageSize() == null || request.getPageSize() < 1) {
            response.setMessage("订单信息查询每页条目不能为空或小于1");
            return response;
        }
        int start = (request.getPageNo() - 1) * request.getPageSize();
        int limit = request.getPageSize();
        try {
            GoodsReturnChangeVO model = request.getData();
            GoodsReturnChangePageListExample example = queryList(model);
            GoodsReturnChangePageListExample.Criteria criteria = example.or();
            criteria.limit(start, limit);
            List<GoodsReturnChangeBean> list = goodsReturnChangePageListMapper.selectByExample(example);
            //填充金额
            if (StringUtil.isListNotNull(list)) {
                List<String> changeSns = new ArrayList<String>();
                for (GoodsReturnChangeBean bean : list) {
                    changeSns.add(bean.getReturnchangeSn());
                }

                Map<String, Double> changeReturnTotalFeeMap = getChangeReturnTotalFeeMap(changeSns);
                if (changeReturnTotalFeeMap == null) {
                    changeReturnTotalFeeMap = new HashMap<String, Double>(Constant.DEFAULT_MAP_SIZE);
                }

                for (GoodsReturnChangeBean bean : list) {
                    String returnChaneSn = bean.getReturnchangeSn();
                    Double returnTotalFee = changeReturnTotalFeeMap.get(returnChaneSn);
                    if (returnTotalFee == null) {
                        returnTotalFee = 0.00;
                    }
                    bean.setTransactionPrice(returnTotalFee);
                }

            }

            int count = goodsReturnChangePageListMapper.countByExample(example);
            response.setList(list);
            response.setTotalProperty(count);
            response.setSuccess(true);
            response.setMessage("查询成功");
        } catch (Exception e) {
            logger.error("查询失败" + e.getMessage(), e);
            response.setMessage("查询失败" + e.getMessage());
        }
        return response;
    }

    /**
     * 获取申请商品退款总额
     * @param changeSnList
     * @return
     */
    private Map<String, Double> getChangeReturnTotalFeeMap(List<String> changeSnList) {
        GoodsReturnChangeDetailExample example = new GoodsReturnChangeDetailExample();
        example.or().andReturnchangeSnIn(changeSnList);
        List<GoodsReturnChangeDetail> details = goodsReturnChangeDetailMapper.selectByExample(example);
        if (StringUtil.isListNull(details)) {
            return null;
        }
        Map<String, List<GoodsReturnChangeDetail>> map = new HashMap<String, List<GoodsReturnChangeDetail>>();
        for (GoodsReturnChangeDetail detail : details) {
            String changeSn = detail.getReturnchangeSn();
            List<GoodsReturnChangeDetail> detailsList = map.get(changeSn);
            if (map.get(changeSn) == null) {
                detailsList = new ArrayList<GoodsReturnChangeDetail>();
            }
            detailsList.add(detail);
            map.put(changeSn, detailsList);
        }

        //获取退货商品总额
        Map<String, Double> priceMap = new HashMap<String, Double>();
        for (Map.Entry<String, List<GoodsReturnChangeDetail>> entry : map.entrySet()) {
            List<GoodsReturnChangeDetail> changeDetails = entry.getValue();
            BigDecimal returnTotalFee = new BigDecimal(0);
            if (StringUtil.isListNotNull(details)) {
                for (GoodsReturnChangeDetail changeDetail : changeDetails) {
                    BigDecimal returnFee = changeDetail.getTransactionPrice().multiply(new BigDecimal(changeDetail.getReturnSum()));
                    returnTotalFee = returnTotalFee.add(returnFee);
                }

                priceMap.put(entry.getKey(), returnTotalFee.doubleValue());
            }
        }

        return priceMap;
    }
	
	public GoodsReturnChangePageListExample queryList(GoodsReturnChangeVO goodsReturnChangeVO){
		GoodsReturnChangePageListExample example = new GoodsReturnChangePageListExample();
		example.setOrderByClause(" grc.create desc ");
		GoodsReturnChangePageListExample.Criteria criteria = example.or();
		if(null!=goodsReturnChangeVO){
			/**
			 * newData三个月以内数据
			 * historyData历史数据
			 */
			if(StringUtil.isNotNull(goodsReturnChangeVO.getListDataType())){
					if(goodsReturnChangeVO.getListDataType().equals("historyData")){
						example.setHistoryData(true);	
					}else{
						example.setHistoryData(false);
					}
			}
			
//			criteria.andSKU();
			if(StringUtil.isNotNull(goodsReturnChangeVO.getChannelType())&&!goodsReturnChangeVO.getChannelType().equals("-1")){
				criteria.andChannelTypeEqualTo(goodsReturnChangeVO.getChannelType());
			}
			
			if(StringUtil.isNotNull(goodsReturnChangeVO.getOrderSn())){
				criteria.andOrderSnEqualTo(goodsReturnChangeVO.getOrderSn());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getReturnchangeSn())){
				criteria.andReturnChangeSnEqualTo(goodsReturnChangeVO.getReturnchangeSn());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getReturnSn())){
				criteria.andReturnSnEqualTo(goodsReturnChangeVO.getReturnSn());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getReturnPaySn())){
				criteria.andReturnPaySnEqualTo(goodsReturnChangeVO.getReturnPaySn());
			}
			
			if(StringUtil.isNotNull(goodsReturnChangeVO.getUserId())){
				criteria.andUserIdEqualTo(goodsReturnChangeVO.getUserId());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getSkuSn())){
				criteria.andSkuSnEqualTo(goodsReturnChangeVO.getSkuSn());
			}
			if(null!=goodsReturnChangeVO.getReturnType()&&goodsReturnChangeVO.getReturnType()>=0){
				criteria.andReturnTypeEqualTo(goodsReturnChangeVO.getReturnType());
			}
			if(null!=goodsReturnChangeVO.getReason()&&goodsReturnChangeVO.getReason()>=0){
				criteria.andReasonEqualTo(goodsReturnChangeVO.getReason());
			}
			if(null!=goodsReturnChangeVO.getRedemption()&&goodsReturnChangeVO.getRedemption()>=0){
				criteria.andRedemptionEqualTo(goodsReturnChangeVO.getRedemption());
			}
			if(null!=goodsReturnChangeVO.getTagType()&&goodsReturnChangeVO.getTagType()>=0){
				criteria.andTagTypeEqualTo(goodsReturnChangeVO.getTagType());
			}
			if(null!=goodsReturnChangeVO.getExteriorType()&&goodsReturnChangeVO.getExteriorType()>=0){
				criteria.andExteriorTypeEqualTo(goodsReturnChangeVO.getExteriorType());
			}
			if(null!=goodsReturnChangeVO.getGiftType()&&goodsReturnChangeVO.getGiftType()>=0){
				criteria.andGiftTypeEqualTo(goodsReturnChangeVO.getGiftType());
			}
			if(null!=goodsReturnChangeVO.getStatus()&&goodsReturnChangeVO.getStatus()>=0){
				criteria.andStatusEqualTo(goodsReturnChangeVO.getStatus());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getContactName())){
				criteria.andContactNameEqualTo(goodsReturnChangeVO.getContactName());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getContactMobile())){
				criteria.andContactMobileEqualTo(goodsReturnChangeVO.getContactMobile());
			}
			if(null!=goodsReturnChangeVO.getStReturnSum()&&null!=goodsReturnChangeVO.getEnReturnSum()){
				criteria.andReturnSumBetween(goodsReturnChangeVO.getStReturnSum(), goodsReturnChangeVO.getEnReturnSum());
			}
			if(null!=goodsReturnChangeVO.getStReturnSum()){
				criteria.andReturnSumGreaterThanOrEqualTo(goodsReturnChangeVO.getStReturnSum());
			}
			if(null!=goodsReturnChangeVO.getEnReturnSum()){
				criteria.andReturnSumLessThanOrEqualTo(goodsReturnChangeVO.getEnReturnSum());
			}
			
			// 订单所属站点
			String siteCode = goodsReturnChangeVO.getSiteCode();
			if (StringUtil.isNotEmpty(siteCode) && !siteCode.equals("-1")) {
				String[] siteCodes = siteCode.split(",");
				if (siteCodes.length > 1) {
					criteria.andSiteCodeIn(Arrays.asList(siteCodes));
				} else {
					criteria.andSiteCodeEqualTo(goodsReturnChangeVO.getSiteCode());
				}
			}/* else {
				criteria.andSiteCodeIn(goodsReturnChangeVO.getSites());
			}*/
			// 订单渠道店铺

			String channelCode = goodsReturnChangeVO.getChannelCode();
			if (StringUtil.isNotEmpty(channelCode) && !channelCode.equals("-1")) {
				String[] channelCodes = channelCode.split(",");
				if (channelCodes.length > 1) {
					criteria.andShopCodeIn(Arrays.asList(channelCodes));
				} else {
					criteria.andShopCodeEqualTo(channelCode);
				}
			}
			// 订单线下店铺
			if (StringUtil.isNotEmpty(goodsReturnChangeVO.getStoreCode())
					&& !goodsReturnChangeVO.getStoreCode().equals("-1")) {
				String arr[] = goodsReturnChangeVO.getStoreCode().split(",");
				if (arr.length == 1) {
					criteria.andStoreCodeEqualTo(arr[0]);
				} else {
					criteria.andStoreCodeIn(Arrays.asList(arr));
				}
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getStartTime())&&StringUtil.isNotNull(goodsReturnChangeVO.getEndTime())){
				criteria.andCreateBetween(DateTimeUtils.parseStr(goodsReturnChangeVO.getStartTime()),DateTimeUtils.parseStr(goodsReturnChangeVO.getEndTime()));
			}else if(StringUtil.isNotNull(goodsReturnChangeVO.getStartTime())){
				criteria.andCreateGreaterThanOrEqualTo(DateTimeUtils.parseStr(goodsReturnChangeVO.getStartTime()));
			}else if(StringUtil.isNotNull(goodsReturnChangeVO.getEndTime())){
				criteria.andCreateLessThanOrEqualTo(DateTimeUtils.parseStr(goodsReturnChangeVO.getEndTime()));
			}
		}
		return example;
	}

	/**
	 * 自提订单列表查询
	 * @param request 查询参数
	 * @return OrderQueryResponse
	 */
	@Override
	public OrderQueryResponse pickUpOrderQuery(OrderQueryRequest request) {
		
		logger.info("自提订单查询 request:" + JSON.toJSONString(request));
		
		OrderQueryResponse response = new OrderQueryResponse();
		response.setSuccess(false);
		response.setMessage("自提订单信息查询失败");
		
		Integer pageNo = request.getPageNo();
		if (pageNo == null || pageNo < 1) {
			response.setMessage("查询页码不能为空或小于1");
			return response;
		}
		
		Integer pageSize = request.getPageSize();
		if (pageSize == null || pageSize < 1) {
			response.setMessage("查询每页记录不能为空或小于1");
			return response;
		} else if (pageSize > 5000) {
			response.setMessage("查询每页记录不能大于5000");
			return response;
		}
		int start = (pageNo - 1) * pageSize;
		int limit = pageSize;
		
		Map<String, Object> queryMap = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
		
		// 订单站点编码
		String channelCode = request.getChannelCode();
		if (StringUtils.isNotBlank(channelCode)) {
			String[] channelCodes = channelCode.split(Constant.STRING_SPLIT_COMMA);
			if (channelCodes.length > 1) {
				queryMap.put("channelCodeList", Arrays.asList(channelCodes));
			} else {
				queryMap.put("channelCode", channelCode);
			}
		}
		
		// 订单线上店铺渠道编码
		String shopCode = request.getShopCode();
		if (StringUtils.isNotBlank(shopCode)) {
			String[] shopCodes = shopCode.split(Constant.STRING_SPLIT_COMMA);
			if (shopCodes.length > 1) {
				queryMap.put("orderFromList", Arrays.asList(shopCodes));
			} else {
				queryMap.put("orderFrom", shopCode);
			}
		}
		
		// 线下店铺编码
		String storeCode = request.getStoreCode();
		if (StringUtils.isNotBlank(storeCode)) {
			String[] storeCodes = storeCode.split(Constant.STRING_SPLIT_COMMA);
			if (storeCodes.length > 1) {
				queryMap.put("storeCodeList", Arrays.asList(storeCodes));
			} else {
				queryMap.put("storeCode", storeCode);
			}
		}
		
		// 提货状态
		String gotStatus = request.getGotStatus();
		if (StringUtils.isNotBlank(gotStatus)) {
			queryMap.put("gotStatus", gotStatus);
		}
		
		// 自提时间范围
		String startTime = request.getStartTime();
		String endTime = request.getEndTime();
		if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
			Date date = TimeUtil.getBeforeDay(-1);
			String dateTime = TimeUtil.format2Date(date);
			startTime = dateTime + " 00:00:00";
			endTime = dateTime + " 23:59:59";
		}
		
		if (StringUtils.isNotBlank(startTime)) {
			queryMap.put("startTime", startTime);
		}
		
		if (StringUtils.isNotBlank(endTime)) {
			queryMap.put("endTime", endTime);
		}
		
		// 订单号
		String masterOrderSn = request.getMasterOrderSn();
		if (StringUtils.isNotBlank(masterOrderSn)) {
			queryMap.put("masterOrderSn", masterOrderSn);
		}
		
		logger.info("selectPickUpOrder:queryMap:" + JSONObject.toJSONString(queryMap));
		
		try {
			int total = orderInfoSearchMapper.selectPickUpOrderCount(queryMap);
			List<OrderItem> orderItems = null;
			if (total > 0) {
				queryMap.put("start", start);
				queryMap.put("limit", limit);
				orderItems = orderInfoSearchMapper.selectPickUpOrderList(queryMap);
			}
			response.setSuccess(true);
			response.setTotalProperty(total);
			response.setOrderItems(orderItems);
			response.setMessage("自提订单信息查询成功");
		} catch (Exception e) {
			logger.error("自提订单查询异常: " + e.getMessage(), e);
			response.setMessage("自提订单查询失败: " + e.getMessage());
		}
		return response;
	}
	
	/**
	 * 配送订单列表查询
	 * @param request
	 * @return
	 */
	public OrderQueryResponse distributionOrderQuery(OrderQueryRequest request) {
		OrderQueryResponse response = new OrderQueryResponse();
		response.setSuccess(false);
		response.setMessage("配送订单信息查询失败");
		
		Integer pageNo = request.getPageNo();
		if (pageNo == null || pageNo < 1) {
			response.setMessage("查询页码不能为空或小于1");
			return response;
		}
		
		Integer pageSize = request.getPageSize();
		if (pageSize == null || pageSize < 1) {
			response.setMessage("查询每页记录不能为空或小于1");
			return response;
		} else if (pageSize > 5000) {
			response.setMessage("查询每页记录不能大于5000");
			return response;
		}
		int start = (pageNo - 1) * pageSize;
		int limit = pageSize;
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		
		// 订单站点编码
		String channelCode = request.getChannelCode();
		if (StringUtils.isNotBlank(channelCode)) {
			String[] channelCodes = channelCode.split(",");
			if (channelCodes.length > 1) {
				queryMap.put("channelCodeList", Arrays.asList(channelCodes));
			} else {
				queryMap.put("channelCode", channelCode);
			}
		}
		
		// 订单线上店铺渠道编码
		String shopCode = request.getShopCode();
		if (StringUtils.isNotBlank(shopCode)) {
			String[] shopCodes = shopCode.split(",");
			if (shopCodes.length > 1) {
				queryMap.put("orderFromList", Arrays.asList(shopCodes));
			} else {
				queryMap.put("orderFrom", shopCode);
			}
		}
		
		// 线下店铺编码
		String storeCode = request.getStoreCode();
		if (StringUtils.isNotBlank(storeCode)) {
			String[] storeCodes = storeCode.split(",");
			if (storeCodes.length > 1) {
				queryMap.put("storeCodeList", Arrays.asList(storeCodes));
			} else {
				queryMap.put("storeCode", storeCode);
			}
		}
		
		// 提货状态
		String gotStatus = request.getGotStatus();
		if (StringUtils.isNotBlank(gotStatus)) {
			queryMap.put("gotStatus", gotStatus);
		}
		
		// 配送时间范围
		String startTime = request.getStartTime();
		String endTime = request.getEndTime();
		
		if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
			Date date = TimeUtil.getBeforeDay(-1);
			String dateTime = TimeUtil.format2Date(date);
			startTime = dateTime + " 00:00:00";
			endTime = dateTime + " 23:59:59";
		}
		
		if (StringUtils.isNotBlank(startTime)) {
			queryMap.put("startTime", startTime);
		}
		
		if (StringUtils.isNotBlank(endTime)) {
			queryMap.put("endTime", endTime);
		}
		
		try {
			int total = orderInfoSearchMapper.selectPickUpOrderCount(queryMap);
			List<OrderItem> orderItems = null;
			if (total > 0) {
				queryMap.put("start", start);
				queryMap.put("limit", limit);
				orderItems = orderInfoSearchMapper.selectPickUpOrderList(queryMap);
			}
			response.setSuccess(true);
			response.setTotalProperty(total);
			response.setOrderItems(orderItems);
			response.setMessage("自提订单信息查询成功");
		} catch (Exception e) {
			logger.error("自提订单查询异常: " + e.getMessage(), e);
			response.setMessage("自提订单查询失败: " + e.getMessage());
		}
		return response;
	}
	
	/**
	 * 通知骑手配送取消
	 * @param request 请求参数
	 * @return OmsBaseResponse<Boolean>
	 */
	@Override
	public OmsBaseResponse<Boolean> riderOrderCancel(OrderQueryRequest request) {
		OmsBaseResponse<Boolean> response = new OmsBaseResponse<Boolean>();
		response.setSuccess(false);
		response.setMessage("配送订单取消失败");
		
		if (request == null) {
			response.setMessage("参数为空");
			return response;
		}
		
		String masterOrderSn = request.getMasterOrderSn();
		if (StringUtils.isBlank(masterOrderSn)) {
			response.setMessage("订单编码为空");
			return response;
		}
		
		String actionUser = request.getActionUser();
		if (StringUtils.isBlank(masterOrderSn)) {
			response.setMessage("操作用户为空");
			return response;
		}
		
		try {
			OrderRiderDistributeLog logModel = new OrderRiderDistributeLog();
			logModel.setMasterOrderSn(masterOrderSn);
			logModel.setActionUser(actionUser);
			ServiceReturnInfo<Boolean> serviceReturnInfo = riderDistributeService.sendOrderRiderCancel(logModel);
			
			if (serviceReturnInfo == null) {
				response.setMessage("通知配送取消失败");
			} else if (serviceReturnInfo.isIsok()) {
				response.setData(true);
				response.setSuccess(true);
				response.setMessage("通知配送取消成功");
			} else {
				response.setMessage("通知配送取消失败:" + serviceReturnInfo.getMessage());
			}
		} catch (Exception e) {
			logger.error("订单:" + JSONObject.toJSONString(request) + "通知配送取消异常", e);
			response.setMessage("通知配送取消异常:" + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 通知订单下发到骑手平台
	 * @param request 请求参数
	 * @return OmsBaseResponse<Boolean>
	 */
	@Override
	public OmsBaseResponse<Boolean> riderOrderSend(OrderQueryRequest request) {
		OmsBaseResponse<Boolean> response = new OmsBaseResponse<Boolean>();
		response.setSuccess(false);
		response.setMessage("订单下发骑手平台失败");
		
		if (request == null) {
			response.setMessage("参数为空");
			return response;
		}
		
		String masterOrderSn = request.getMasterOrderSn();
		if (StringUtils.isBlank(masterOrderSn)) {
			response.setMessage("订单编码为空");
			return response;
		}
		
		String actionUser = request.getActionUser();
		if (StringUtils.isBlank(masterOrderSn)) {
			response.setMessage("操作用户为空");
			return response;
		}
		
		try {
			OrderRiderDistributeLog logModel = new OrderRiderDistributeLog();
			logModel.setMasterOrderSn(masterOrderSn);
			logModel.setActionUser(actionUser);
			
			ServiceReturnInfo<Boolean> serviceReturnInfo = riderDistributeService.sendRiderDistributeByUser(logModel);
			
			if (serviceReturnInfo == null) {
				response.setMessage("通知订单配送失败");
			} else if (serviceReturnInfo.isIsok()) {
				response.setData(true);
				response.setSuccess(true);
				response.setMessage("通知订单配送成功");
			} else {
				response.setMessage("通知订单配送失败:" + serviceReturnInfo.getMessage());
			}
		} catch (Exception e) {
			logger.error("订单:" + JSONObject.toJSONString(request) + "通知下发骑手平台异常", e);
			response.setMessage("通知订单下发骑手平台异常:" + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 配送订单列表查询
	 * @param request 查询参数
	 * @return OmsBaseResponse<OrderRiderDistributeLog>
	 */
	@Override
	public OmsBaseResponse<OrderRiderDistributeLog> riderOrderQuery(OrderQueryRequest request) {
		OmsBaseResponse<OrderRiderDistributeLog> response = new OmsBaseResponse<OrderRiderDistributeLog>();
		response.setSuccess(false);
		response.setMessage("配送订单信息查询失败");
		
		Integer pageNo = request.getPageNo();
		if (pageNo == null || pageNo < 1) {
			response.setMessage("查询页码不能为空或小于1");
			return response;
		}
		
		Integer pageSize = request.getPageSize();
		if (pageSize == null || pageSize < 1) {
			response.setMessage("查询每页记录不能为空或小于1");
			return response;
		} else if (pageSize > 5000) {
			response.setMessage("查询每页记录不能大于5000");
			return response;
		}
		int start = (pageNo - 1) * pageSize;
		int limit = pageSize;
		
		Map<String, Object> queryMap = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
		
		// 订单站点编码
		String channelCode = request.getChannelCode();
		if (StringUtils.isNotBlank(channelCode)) {
			String[] channelCodes = channelCode.split(Constant.STRING_SPLIT_COMMA);
			if (channelCodes.length > 1) {
				queryMap.put("siteCodeList", Arrays.asList(channelCodes));
			} else {
				queryMap.put("siteCode", channelCode);
			}
		}

		getCommonQueryMap(queryMap, request);
		
		// 配送时间范围
		String startTime = request.getStartTime();
		String endTime = request.getEndTime();
		
		if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
			Date date = new Date();
			String dateTime = TimeUtil.format2Date(date);
			startTime = dateTime + " 00:00:00";
			endTime = dateTime + " 23:59:59";
		}
		
		if (StringUtils.isNotBlank(startTime)) {
			queryMap.put("startTime", startTime);
		}
		
		if (StringUtils.isNotBlank(endTime)) {
			queryMap.put("endTime", endTime);
		}
		
		// 配送状态 0未配送 1配送中 2配送完成、3配送取消、4已接单
		Integer status = request.getOrderStatus();
		if (status != null) {
			queryMap.put("orderStatus", status);
		}
		
		// 订单编码
		String masterOrderSn = request.getMasterOrderSn();
		if (StringUtils.isNotBlank(masterOrderSn)) {
			queryMap.put("masterOrderSn", masterOrderSn);
		}
		
		// 配送编码
		String tmsCode = request.getTmsCode();
		if (StringUtils.isNotBlank(tmsCode)) {
			String[] tmsCodes = tmsCode.split(Constant.STRING_SPLIT_COMMA);
			if (tmsCodes.length > 1) {
				queryMap.put("tmsCodeList", Arrays.asList(tmsCodes));
			} else {
				queryMap.put("tmsCode", tmsCode);
			}
		}
		
		try {
			int total = riderDistributeService.getRiderDistributeCount(queryMap);
			List<OrderRiderDistributeLog> orderItems = null;
			if (total > 0) {
				queryMap.put("start", start);
				queryMap.put("limit", limit);
				orderItems = riderDistributeService.getOrderRiderList(queryMap);
			}
			response.setSuccess(true);
			response.setTotalProperty(total);
			response.setList(orderItems);
			response.setMessage("配送订单信息查询成功");
		} catch (Exception e) {
			logger.error("配送订单查询异常: " + e.getMessage(), e);
			response.setMessage("配送订单查询失败: " + e.getMessage());
		}
		return response;
	}
	
	/**
	 * 获取骑手配送详情
	 * @param request 查询参数
	 * @return OmsBaseResponse<OrderRiderDistributeLog>
	 */
	@Override
	public OmsBaseResponse<OrderRiderDistributeLog> riderOrderDetail(OrderQueryRequest request) {
		OmsBaseResponse<OrderRiderDistributeLog> response = new OmsBaseResponse<OrderRiderDistributeLog>();
		response.setSuccess(false);
		response.setMessage("配送订单信息查询失败");
		
		if (request == null) {
			response.setMessage("参数为空");
			return response;
		}
		String masterOrderSn = request.getMasterOrderSn();
		if (StringUtils.isBlank(masterOrderSn)) {
			response.setMessage("订单编码为空");
			return response;
		}
		
		try {
			OrderRiderDistributeLog logModel = new OrderRiderDistributeLog();
			logModel.setMasterOrderSn(masterOrderSn);
			logModel = riderDistributeService.getModel(logModel);
			if (logModel == null) {
				response.setMessage("订单编码:" + masterOrderSn + "无配送信息");
			} else {
				response.setMessage("查询成功");
				response.setData(logModel);
				response.setSuccess(true);
			}
		} catch (Exception e) {
			logger.error("配送订单查询异常: " + e.getMessage(), e);
			response.setMessage("配送订单查询失败: " + e.getMessage());
		}
		return response;
	}

	/**
	 * 线下店铺列表
	 * @param request 查询参数
	 * @return OmsBaseResponse<OfflineStoreInfo>
	 */
	@Override
	public OmsBaseResponse<OfflineStoreInfo> offlineStoreManagement(OmsBaseRequest<OfflineStoreInfo> request) {
		logger.info("订单线下店铺信息 request:" + JSON.toJSONString(request));
		OmsBaseResponse<OfflineStoreInfo> response = new OmsBaseResponse<OfflineStoreInfo>();
		response.setSuccess(false);
		response.setMessage("订单线下店铺信息失败");
		try {
			ShopCartRequest<StoreInfoRequest> cartRequest = new ShopCartRequest<StoreInfoRequest>();
			StoreInfoRequest infoRequest = new StoreInfoRequest();
			OfflineStoreInfo offlineStoreInfo = request.getData(); 
			if (offlineStoreInfo != null) {
				if (StringUtil.isNotEmpty(offlineStoreInfo.getShopCode())) {
					infoRequest.setChannelCode(offlineStoreInfo.getShopCode());
				}
				if (StringUtil.isNotEmpty(offlineStoreInfo.getStoreCode())) {
					infoRequest.setStoreCode(offlineStoreInfo.getStoreCode());
				}
				if (StringUtil.isListNotNull(offlineStoreInfo.getShopCodeList())) {
					infoRequest.setChannelCodeList(offlineStoreInfo.getShopCodeList());
				}
				if (StringUtil.isListNotNull(offlineStoreInfo.getStoreCodeList())) {
					infoRequest.setStoreCodeList(offlineStoreInfo.getStoreCodeList());
				}
			}
			cartRequest.setRequestBean(infoRequest);
			logger.info("查询渠道店铺信息:request" + JSON.toJSONString(cartRequest));
			ShopCartResponse<List<StoreInfoBean>> cartResponse = storeInfoCartAPI.getStoreInfoList(cartRequest);
			logger.info("查询渠道店铺信息:response" + JSON.toJSONString(cartResponse));
			if (cartResponse == null) {
				logger.error("查询渠道店铺信息失败：返回结果为空");
				response.setMessage("查询渠道店铺信息失败：返回结果为空");
				return response;
			}
			if (StringUtil.isListNotNull(cartResponse.getData())) {
				List<OfflineStoreInfo> offlineStoreInfoList = new ArrayList<OfflineStoreInfo>();
				for (StoreInfoBean infoBean : cartResponse.getData()) {
					OfflineStoreInfo info = new OfflineStoreInfo();
					info.setShopCode(infoBean.getChannelCode());
					info.setStoreCode(infoBean.getStoreCode());
					info.setStoreName(infoBean.getStoreName());
					info.setStoreType(infoBean.getStoreType());
					info.setAddress(infoBean.getAddress());
					offlineStoreInfoList.add(info);
				}
				response.setList(offlineStoreInfoList);
				response.setSuccess(true);
				response.setMessage("订单线下店铺信息成功");
				return response;
			} else {
				logger.error("查询渠道店铺信息失败：店铺数据为空" + cartResponse.getMsg());
				response.setMessage("查询渠道店铺信息失败：店铺数据为空" + cartResponse.getMsg());
				return response;
			}
		} catch (Exception e) {
			logger.error("订单线下店铺信息失败"+ e.getMessage(), e);
			response.setMessage("订单线下店铺信息失败"+ e.getMessage());
		}
		return response;
	}

	/**
	 * 订单待结算列表查询
	 * @param request 请求参数
	 * @return OmsBaseResponse<OrderItem>
	 */
	@Override
	public OmsBaseResponse<OrderItem> waitSettleOrderQuery(OmsBaseRequest<OrderItem> request) {
		logger.info("待结算订单查询 request:" + JSON.toJSONString(request));
		OmsBaseResponse<OrderItem> response = new OmsBaseResponse<OrderItem>();
		response.setSuccess(false);
		response.setMessage("订单信息查询失败");
		try {
			OrderQueryRequest queryRequest = new OrderQueryRequest();
			queryRequest.setPageNo(request.getPageNo());
			queryRequest.setPageSize(request.getPageSize());
			cloneObj(queryRequest, request.getData());
			// 待结算列表
			queryRequest.setQueryType(1);
			// 已确认
			queryRequest.setOrderStatus(1);
			// 已付款
			queryRequest.setPayStatus(2);
			// 已收货
			queryRequest.setShipStatus(5);
			OrderQueryResponse queryResponse = orderQuery(queryRequest);
			if (queryResponse.getSuccess()) {
				response.setList(queryResponse.getOrderItems());
				response.setSuccess(true);
			} else {
				response.setMessage(queryResponse.getMessage());
			}
		} catch (Exception e) {
			logger.error("待结算订单查询失败"+ e.getMessage(), e);
			response.setMessage("待结算订单查询失败"+ e.getMessage());
		}
		return response;
	}

	/**
	 * 退单待结算列表
	 * @param request 请求参数
	 * @return OmsBaseResponse<OrderReturnListVO>
	 */
	@Override
	public OmsBaseResponse<OrderReturnListVO> waitSettleReturnQuery(OmsBaseRequest<OrderReturnListVO> request) {
		logger.info("待结算退单查询 request:" + JSON.toJSONString(request));
		OmsBaseResponse<OrderReturnListVO> response = new OmsBaseResponse<OrderReturnListVO>();
		response.setSuccess(false);
		response.setMessage("待结算退单查询失败");
		try {
			// 固定条件
			// 已确认
			request.getData().setReturnOrderStatus((byte) 1);
			// 待结算
			request.getData().setReturnPayStatus(2);
			// 已入库
			request.getData().setReturnShippingStatus(3);
			response = orderReturnQuery(request);
		} catch (Exception e) {
			logger.error("待结算退单查询失败"+ e.getMessage(), e);
			response.setMessage("待结算退单查询失败"+ e.getMessage());
		}
		return response;
	}
	
	/**
	 * 查询商品销售记录列表
	 * @param request 请求参数
	 * @return OmsBaseResponse<OrderGoodsSaleBean>
	 */
	@Override
	public OmsBaseResponse<OrderGoodsSaleBean> orderGoodsSaleQuery(OmsBaseRequest<OrderGoodsQuery> request) {
		
		OmsBaseResponse<OrderGoodsSaleBean> response = new OmsBaseResponse<OrderGoodsSaleBean>();
		response.setSuccess(false);
		response.setMessage("商品销售查询失败");
		
		if (request == null) {
			response.setMessage("参数为空");
			return response;
		}
		logger.info("商品销售查询 request:" + JSON.toJSONString(request));
		
		OrderGoodsQuery orderGoodsQuery = request.getData();
		if (orderGoodsQuery == null) {
			response.setMessage("参数为空");
			return response;
		}
		String beginTime = orderGoodsQuery.getBeginTime();
		String endTime = orderGoodsQuery.getEndTime();
		
		if (StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
			response.setMessage("查询时间为空");
			return response;
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("beginTime", beginTime);
			param.put("endTime", endTime);
			
			Integer total = orderInfoSearchMapper.selectOrderGoodsSaleCount(param);
			response.setTotalProperty(total);
			
			int start = orderGoodsQuery.getStart();
			int limit = orderGoodsQuery.getLimit();
			if (limit > 0) {
				param.put("start", start);
				param.put("limit", limit);
			}
			List<OrderGoodsSaleBean> list = orderInfoSearchMapper.selectOrderGoodsSaleList(param);
			
			response.setSuccess(true);
			response.setMessage("查询成功");
			response.setList(list);
		} catch (Exception e) {
			logger.error("商品销售查询失败" + e.getMessage(), e);
			response.setMessage("商品销售查询失败" + e.getMessage());
		}
		return response;
	}
	
	/**
	 * 查询订单仓库配送列表
	 * @param request 请求参数
	 * @return OrderQueryResponse
	 */
	@Override
	public OrderQueryResponse orderDepotShipQuery(OrderQueryRequest request) {
		logger.info("订单仓库配送查询 request:" + JSON.toJSONString(request));
		OrderQueryResponse response = new OrderQueryResponse();
		response.setSuccess(false);
		response.setMessage("订单仓库配送查询失败");
		
		if (request.getPageNo() == null || request.getPageNo() < 1) {
			response.setMessage("页码不能为空或小于1");
			return response;
		}
		if (request.getPageSize() == null || request.getPageSize() < 1) {
			response.setMessage("每页条目不能为空或小于1");
			return response;
		}
		int start = (request.getPageNo() - 1) * request.getPageSize();
		int limit = request.getPageSize();
		
		Map<String, Object> queryMap = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
		
		// 订单编码
		String masterOrderSn = request.getMasterOrderSn();
		if (StringUtils.isNotBlank(masterOrderSn)) {
			queryMap.put("masterOrderSn", masterOrderSn);
		}
		// 处理状态
		String gotStatus = request.getGotStatus();
		if (StringUtils.isNotBlank(gotStatus)) {
			queryMap.put("gotStatus", gotStatus);
		}
		// 仓库编码
		String depotCode = request.getDepotCode();
		if (StringUtils.isNotBlank(depotCode)) {
			String[] depotCodes = depotCode.split(Constant.STRING_SPLIT_COMMA);
			if (depotCodes.length == 1) {
				queryMap.put("depotCode", depotCode);
			} else {
				queryMap.put("depotCodes", Arrays.asList(depotCodes));
			}
		}
		// 站点编码
		String channelCode = request.getChannelCode();
		if (StringUtils.isNotBlank(channelCode)) {
			String[] channelCodes = channelCode.split(Constant.STRING_SPLIT_COMMA);
			if (channelCodes.length == 1) {
				queryMap.put("siteCode", channelCode);
			} else {
				queryMap.put("siteCodes", Arrays.asList(channelCodes));
			}
		}
		// 渠道店铺编码
		String shopCode = request.getShopCode();
		if (StringUtils.isNotBlank(shopCode)) {
			String[] shopCodes = shopCode.split(Constant.STRING_SPLIT_COMMA);
			if (shopCodes.length == 1) {
				queryMap.put("shopCode", shopCode);
			} else {
				queryMap.put("shopCodes", Arrays.asList(shopCodes));
			}
		}
		
		// 配送时间
		String startTime = request.getStartTime();
		if (StringUtils.isNotBlank(startTime)) {
			if (startTime.length() > 10) {
				startTime = startTime.substring(0, 10);
			}
			queryMap.put("startTime", startTime);
		}
		
		String endTime = request.getEndTime();
		if (StringUtils.isNotBlank(endTime)) {
			if (endTime.length() > 10) {
				endTime = endTime.substring(0, 10);
			}
			queryMap.put("endTime", endTime);
		}
		
		// 发货状态
		Integer shipStatus = request.getShipStatus();
		if (shipStatus != null) {
			queryMap.put("shipStatus", shipStatus);
		}
		
		// 收货人手机号码
		String mobile = request.getReceiverMobile();
		if (StringUtils.isNotBlank(mobile)) {
			queryMap.put("mobile", mobile);
		}
		
		int count = 0;
		
		try {
			
			/**
			 * 查询类型 0 订单列表、 1订单商品列表
			 */
			int exportType = request.getExportType();
			if (exportType == 0) {
				count = orderDistributeDefineMapper.getOrderDistributeOutCount(queryMap);
				List<OrderItem> orderList = new ArrayList<OrderItem>();
				if (count > 0) {
					queryMap.put("start", start);
					queryMap.put("limit", limit);
					orderList = orderDistributeDefineMapper.getOrderDistributeOutList(queryMap);
				}
				response.setOrderItems(orderList);
			} else if (exportType == 1) {
				count = orderDistributeDefineMapper.getOrderDistributeOutGoodsCount(queryMap);
				List<OrderShipGoodsItem> orderShipGoodsList = new ArrayList<OrderShipGoodsItem>();
				if (count > 0) {
					queryMap.put("start", start);
					queryMap.put("limit", limit);
					orderShipGoodsList = orderDistributeDefineMapper.getOrderDistributeOutGoodsList(queryMap);
				}
				response.setOrderShipGoodsItems(orderShipGoodsList);
			}
			response.setSuccess(true);
			response.setTotalProperty(count);
			response.setMessage("查询成功");
		} catch (Exception e) {
			logger.error("订单仓库配送查询失败" + e.getMessage(), e);
			response.setMessage("订单仓库配送查询失败" + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 查询订单仓库配送单详情
	 * @param request 请求参数
	 * @return OmsBaseResponse<OrderShipVO>
	 */
	@Override
	public OmsBaseResponse<OrderShipVO> queryOrderDepotShipDetail(OrderQueryRequest request) {
		
		OmsBaseResponse<OrderShipVO> response = new OmsBaseResponse<OrderShipVO>();
		response.setSuccess(false);
		response.setMessage("查询失败");
		
		if (request == null) {
			response.setMessage("查询参数为空");
			return response;
		}
		
		logger.info("订单仓库配送详情 request:" + JSON.toJSONString(request));
		Map<String, Object> queryMap = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
		
		// 订单编码
		String masterOrderSn = request.getMasterOrderSn();
		if (StringUtils.isBlank(masterOrderSn)) {
			response.setMessage("订单编码为空");
			return response;
		}
		
		// 仓库编码
		String depotCode = request.getDepotCode();
		if (StringUtils.isBlank(depotCode)) {
			response.setMessage("仓库编码为空");
			return response;
		}
		
		// 订单配送单
		String orderSn = request.getOuterOrderSn();
		if (StringUtils.isBlank(orderSn)) {
			response.setMessage("配送单编码为空");
			return response;
		}
		
		queryMap.put("masterOrderSn", masterOrderSn);
		queryMap.put("depotCode", depotCode);
		queryMap.put("orderSn", orderSn);
		
		try {
			List<OrderItem> orderList = orderDistributeDefineMapper.getOrderDistributeOutList(queryMap);
			
			if (orderList == null || orderList.size() == 0) {
				response.setMessage("查询无对应配送单信息");
				return response;
			}
			
			OrderShipVO orderShipVO = new OrderShipVO();
			OrderItem orderItem = orderList.get(0);
			orderShipVO.setOrderItem(orderItem);
			
			MasterOrderGoods queryModel = new MasterOrderGoods();
			queryModel.setMasterOrderSn(masterOrderSn);
			queryModel.setOrderSn(orderSn);
			queryModel.setDepotCode(depotCode);
			List<MasterOrderGoods> goodsList = masterOrderGoodsDetailMapper.getMasterOrderGoodsByDepot(queryModel);
			
			if (goodsList != null && goodsList.size() > 0) {
				
				Map<String, Object> paramMap = new HashMap<String, Object>(4);
				paramMap.put("masterOrderSn", masterOrderSn);
				paramMap.put("isHistory", 0);
				// 查询退单列表  用于计算已退待退数量信息
				List<OrderReturnGoods> returnList = orderReturnGoodsDetailMapper.getReturnNumberByMasOrdSn(paramMap);
				if (StringUtil.isListNotNull(returnList)) {
					
					for (Iterator<MasterOrderGoods> goodsIt = goodsList.iterator(); goodsIt.hasNext();) {
						MasterOrderGoods orderBean = goodsIt.next();
						int goodsNumber = orderBean.getGoodsNumber().intValue();
						for (OrderReturnGoods returnBean : returnList) {
							// sku, 商品类型, 仓库, 成交价
							if (orderBean.getCustomCode().equals(returnBean.getCustomCode())
									&& orderBean.getDepotCode().equals(returnBean.getOsDepotCode())) {
								// 退货数据
								int goodsReturnNumber = returnBean.getGoodsReturnNumber().intValue();
								goodsNumber = goodsNumber - goodsReturnNumber;
							}
						}
						
						orderBean.setGoodsNumber((short)goodsNumber);
						if (goodsNumber <= 0) {
							goodsIt.remove();
						}
						
					}
				}
			}
			orderShipVO.setGoodsList(goodsList);
			response.setData(orderShipVO);
			response.setSuccess(true);
			response.setMessage("查询成功");
		} catch (Exception e) {
			logger.error("订单仓库配送单详情查询失败" + e.getMessage(), e);
			response.setMessage("订单仓库配送单详情查询失败" + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 请求处理仓库发送配送
	 * @param param 请求参数
	 */
	@Override
	public void getOrderDistributeOut(Map<String, Object> param) {
		
		if (param == null) {
			logger.info("处理仓库发送配送参数为空");
			return;
		}
		
		if (param.get("siteCode") == null) {
			logger.info("站点编码为空");
			return;
		}
		
		if (param.get("depotCode") == null) {
			logger.info("仓库编码为空");
			return;
		}
		
		String siteCode = String.valueOf(param.get("siteCode"));
		String depotCode = String.valueOf(param.get("depotCode"));
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("siteCode", siteCode);
		queryMap.put("depotCode", depotCode);
		
		String distTime = TimeUtil.format2Date(new Date());
		queryMap.put("distTime", distTime);
		// 未发货
		queryMap.put("shipStatus", 0);
		// 未处理
		queryMap.put("gotStatus", 0);
		int count = orderDistributeDefineMapper.getOrderDistributeOutCount(queryMap);
		
		if (count == 0) {
			return;
		}
		
		Map<String, String> addressMap = new HashMap<String, String>(Constant.DEFAULT_MAP_SIZE);
		int limit = 1000;
		int start = 0;
		while (start < count) {
			queryMap.put("start", start);
			queryMap.put("limit", limit);
			List<OrderItem> orderList = orderDistributeDefineMapper.getOrderDistributeOutList(queryMap);
			
			if (orderList != null && orderList.size() > 0) {
				for (OrderItem orderItem : orderList) {
					processOrderDistributeOut(orderItem, addressMap);
				}
			}
			
			start += limit;
		}
	}

    /**
     * 订单合同列表查询
     * @param request 查询参数
     * @return OrderQueryResponse
     */
    @Override
    public OrderQueryResponse orderContractQuery(OrderContractRequest request) {
        logger.info("订单合同列表查询 request:" + JSON.toJSONString(request));
        OrderQueryResponse response = new OrderQueryResponse();
        response.setSuccess(false);
        response.setMessage("订单合同信息查询失败");
        if (request.getPageNo() == null || request.getPageNo() < 1) {
            response.setMessage("订单合同信息查询页码不能为空或小于1");
            return response;
        }
        if (request.getPageSize() == null || request.getPageSize() < 1) {
            response.setMessage("订单合同信息查询每页条目不能为空或小于1");
            return response;
        }
        int start = (request.getPageNo() - 1) * request.getPageSize();
        int limit = request.getPageSize();
        request.setStart(start);
        request.setLimit(limit);

        try {
            int count = orderInfoSearchMapper.countOrderContractList(request);
            response.setTotalProperty(count);

            if (count > 0) {
                List<OrderContractBean> orderContractBeanList = orderInfoSearchMapper.selectOrderContractList(request);
                response.setOrderContractBeans(orderContractBeanList);
            }
            response.setSuccess(true);
            response.setMessage("查询订单合同列表成功");

        } catch (Exception e) {
            logger.error("订单合同查询异常: " + e.getMessage(), e);
            response.setMessage("订单合同查询失败: " + e.getMessage());
        }
        return response;
    }

    /**
     * 订单列表导出查询
     * @param request 查询参数
     * @return OrderQueryResponse
     */
    @Override
    public OrderQueryResponse orderQueryByExport(OrderQueryRequest request) {
        logger.info("订单列表导出查询 request:" + JSON.toJSONString(request));
        OrderQueryResponse response = new OrderQueryResponse();
        response.setSuccess(false);
        response.setMessage("订单列表导出查询失败");
        if (request.getPageNo() == null || request.getPageNo() < 1) {
            response.setMessage("订单信息查询页码不能为空或小于1");
            return response;
        }

        int start = (request.getPageNo() - 1) * request.getPageSize();
        int limit = request.getPageSize();
        // 编辑SQL查询参数
        Map<String, Object> paramMap = new HashMap<>(5);
        paramMap.put("start", start);
        paramMap.put("limit", limit);

        String startTime = request.getStartTime();
        if (StringUtils.isNotBlank(startTime)) {
            paramMap.put("startTime", startTime);
        }

        String endTime = request.getEndTime();
        if (StringUtils.isNotBlank(endTime)) {
            paramMap.put("endTime", endTime);
        }

        //公司id
        String companyId = request.getCompanyId();
        if (StringUtils.isNotBlank(companyId)) {
            List<String> companyIdList = Arrays.asList(companyId.split(","));
            paramMap.put("companyIdList", companyIdList);
        }

        //公司类型
        Integer companyType = request.getCompanyType();
        if (companyType != null) {
            paramMap.put("companyType", companyType);
        }

        // 订单类型 0正常订单，1联采订单
		Integer orderType = request.getOrderType();
		if (orderType != null) {
			paramMap.put("orderType", orderType);
		}

		// 渠道店铺
		String shopCode = request.getShopCode();
		if (StringUtils.isNotBlank(shopCode)) {
			paramMap.put("shopCode", shopCode);
		}

        try {
            // 订单列表
            List<OrderQueryExportResult> resultList = orderInfoSearchMapper.getOrderQueryExportList(paramMap);
            int exportList = orderInfoSearchMapper.countOrderQueryExportList(paramMap);
            response.setTotalProperty(exportList);
            response.setOrderQueryExportResults(resultList);
            response.setSuccess(true);
            response.setMessage("订单列表导出查询成功");
        } catch (Exception e) {
            logger.error("订单列表导出查询异常: " + e.getMessage(), e);
            response.setMessage("订单列表导出查询失败: " + e.getMessage());
        }
        return response;
    }
	
	/**
	 * 处理订单配送单出库
	 * @param orderItem
	 */
	private void processOrderDistributeOut(OrderItem orderItem, Map<String, String> addressMap) {
		
		if (orderItem == null) {
			return;
		}
		
		// 配送单编码
		String orderSn = orderItem.getOuterOrderSn();
		
		try {
			OrderOutBean orderOutBean = new OrderOutBean();
			orderOutBean.setSiteCode(orderItem.getChannelCode());
			orderOutBean.setDepotCode(orderItem.getDepotCode());
			orderOutBean.setOrderSn(orderItem.getMasterOrderSn());
			orderOutBean.setOrderDistribution(orderSn);
			orderOutBean.setRepName(orderItem.getReceiverName());
			orderOutBean.setMobile(orderItem.getReceiverMobile());
			orderOutBean.setAddress(orderItem.getAddress());
			
			String province = orderItem.getProvince();
			if (StringUtils.isNotBlank(province)) {
				String provinceName = addressMap.get(province);
					if (StringUtils.isBlank(provinceName)) {
					// 省、市
					SystemRegionArea provinceArea = systemRegionAreaMapper.selectByPrimaryKey(province);
					if (provinceArea != null) {
						provinceName = provinceArea.getRegionName();
						addressMap.put(province, provinceName);
					}
				}
				orderOutBean.setProvince(provinceName);
			}
			
			String city = orderItem.getCity();
			if (StringUtils.isNotBlank(city)) {
				String cityName = addressMap.get(city);
					if (StringUtils.isBlank(cityName)) {
					// 省、市
					SystemRegionArea cityArea = systemRegionAreaMapper.selectByPrimaryKey(city);
					if (cityArea != null) {
						cityName = cityArea.getRegionName();
						addressMap.put(city, cityName);
					}
				}
				orderOutBean.setCity(cityName);
			}
			
			// 获取配送商品列表
			Map<String, Object> queryMap = new HashMap<String, Object>(6);
			queryMap.put("masterOrderSn", orderItem.getMasterOrderSn());
			queryMap.put("orderSn", orderSn);
			String depotCode = orderItem.getDepotCode();
			if (StringUtils.isNotBlank(depotCode)) {
				queryMap.put("depotCode", depotCode);
			}
			List<MasterOrderGoods> goodsList = defineOrderMapper.getOrderDistributeGoods(queryMap);
			if (goodsList == null || goodsList.size() == 0) {
				return;
			}
			
			List<OutGoods> outGoodsList = new ArrayList<OutGoods>();
			for (MasterOrderGoods masterOrderGoods : goodsList) {
				OutGoods outGoods = new OutGoods();
				outGoods.setSku(masterOrderGoods.getCustomCode());
				outGoods.setAmount(masterOrderGoods.getGoodsNumber().intValue());
				outGoods.setBoxGauge(masterOrderGoods.getBoxGauge() == null ? null : masterOrderGoods.getBoxGauge().intValue());
				outGoodsList.add(outGoods);
			}
			orderOutBean.setOutGoodsList(outGoodsList);
			
			// 下发MQ
			orderDistributeProcessJmsTemplate.send(new TextMessageCreator(JSONObject.toJSONString(orderOutBean)));
			
			// 更新下发状态
			updateOrderDistribute(orderSn);
		} catch (Exception e) {
			logger.error("订单配送下发异常:" + JSONObject.toJSONString(orderItem), e);
		}
	}
	
	/**
	 * 更新配送状态出库下发
	 * @param orderSn
	 */
	private void updateOrderDistribute(String orderSn) {
		if (StringUtils.isBlank(orderSn)) {
			return;
		}
		OrderDistribute updateDistribute = new OrderDistribute();
		updateDistribute.setGotStatus(Constant.GOT_STATUS_YES);
		updateDistribute.setOrderSn(orderSn);
		updateDistribute.setUpdateTime(new Date());
		orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
	}

	/**
	 * 获取公共查询参数
	 * @param queryMap 查询对象
	 * @param request 请求参数
	 */
	private void getCommonQueryMap(Map<String, Object> queryMap, OrderQueryRequest request) {
		// 订单线上店铺渠道编码
		String shopCode = request.getShopCode();
		if (StringUtils.isNotBlank(shopCode)) {
			String[] shopCodes = shopCode.split(Constant.STRING_SPLIT_COMMA);
			if (shopCodes.length > 1) {
				queryMap.put("channelCodeList", Arrays.asList(shopCodes));
			} else {
				queryMap.put("channelCode", shopCode);
			}
		}

		// 线下店铺编码
		String storeCode = request.getStoreCode();
		if (StringUtils.isNotBlank(storeCode)) {
			String[] storeCodes = storeCode.split(Constant.STRING_SPLIT_COMMA);
			if (storeCodes.length > 1) {
				queryMap.put("storeCodeList", Arrays.asList(storeCodes));
			} else {
				queryMap.put("storeCode", storeCode);
			}
		}
	}

	private void cloneObj(Object destObj, Object origObj) {
		try {
			BeanUtils.copyProperties(destObj, origObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
