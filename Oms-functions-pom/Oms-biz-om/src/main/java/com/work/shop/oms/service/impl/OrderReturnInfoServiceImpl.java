package com.work.shop.oms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.bean.OrderReturnAction;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.OrderPayExample;
import com.work.shop.oms.bean.OrderReturnActionExample;
import com.work.shop.oms.bean.SystemPayment;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.config.service.SystemPaymentService;
import com.work.shop.oms.dao.OrderRefundMapper;
import com.work.shop.oms.dao.OrderRefundSearchMapper;
import com.work.shop.oms.dao.OrderReturnActionMapper;
import com.work.shop.oms.dao.OrderReturnGoodsMapper;
import com.work.shop.oms.dao.define.OrderReturnSearchMapper;
import com.work.shop.oms.service.OrderInfoService;
import com.work.shop.oms.service.OrderReturnInfoService;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.vo.OrderRefundListVO;
import com.work.shop.oms.vo.OrderReturnListVO;
import com.work.shop.oms.vo.OrderReturnSearchExample;

@Service
public class OrderReturnInfoServiceImpl implements OrderReturnInfoService{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private OrderReturnSearchMapper orderReturnSearchMapper;
	
	@Resource
	private OrderInfoService orderInfoService;
	
	@Autowired
	private OrderRefundMapper orderRefundMapper;
	
	@Autowired
	private OrderRefundSearchMapper orderRefundSearchMapper;
	
	@Resource
	private SystemPaymentService systemPaymentService;
	
	@Autowired
	private OrderReturnGoodsMapper orderReturnGoodsMapper;
	@Autowired
	private OrderReturnActionMapper orderReturnActionMapper;

	
	public Paging getOrderReturnPage(OrderReturnListVO model, PageHelper helper)
			throws Exception {
		
		
		OrderReturnSearchExample example = new OrderReturnSearchExample();
		OrderReturnSearchExample.Criteria criteria = example.or();
		
		//默认退单时间  降序
		example.setOrderByClause("orn.add_time desc");
		
		criteria.limit(helper.getStart(), helper.getLimit());
		boolean condition = true;
		
		if (null != model) {
			if(StringUtil.isNotNull(model.getListDataType())){
				if(model.getListDataType().equals("newDate")){
					example.setListDataType(true);
				}else if(model.getListDataType().equals("historyDate")){
					example.setListDataType(false);
				}
			}else{
				example.setListDataType(true);
			}
	
			//退单时间  升序和降序
			if("addTime".equals(model.getSort())){
				if("ASC".equalsIgnoreCase(model.getDir())){
					example.setOrderByClause("orn.add_time asc");
				} 
			}
			
			//入库时间  升序和降序
			if("checkinTime".equals(model.getSort())){
				if("ASC".equalsIgnoreCase(model.getDir())){
					example.setOrderByClause("orn.checkin_Time asc");
					
				} else  if( "DESC".equalsIgnoreCase(model.getDir())){
					example.setOrderByClause("orn.checkin_Time desc");
				}
			}
			
			//待结算退单:不在此list中的数据
			if(StringUtil.isNotNullForList(model.getAddList())){
				criteria.andReturnSnNotIn(model.getAddList());
			}
			
			//结算时间  升序和降序
			if("clearTime".equals(model.getSort())){
				if("ASC".equalsIgnoreCase(model.getDir())){
					example.setOrderByClause("orn.clear_Time asc");
				} else  if( "DESC".equalsIgnoreCase(model.getDir())){
					example.setOrderByClause("orn.clear_Time desc");
				}
			}
			
			if(StringUtil.isNotBlank(model.getMasterOrderSn())){
				condition = false;
				criteria.andMasterOrderSnEqualTo(model.getMasterOrderSn());
			}
			
			
			//收件人
			if(StringUtil.isNotBlank(model.getConsignee())){
				condition = false;
				criteria.andConsigneeLike("%"+model.getConsignee()+"%");
			}
			
			//是否退款：0.无须退款；1:需要退款',
			if(null !=  model.getHaveRefund() && -1 != model.getHaveRefund()){
				criteria.andHaveRefundEqualTo( model.getHaveRefund());
			}
			
			//不是全流通，在待结算退单中使用
			if(model.getIsNotQuanQiuTong()){
				criteria.andRefererNotEqualTo("全流通");
			}
			
//			criteria.andRefundTypeNotEqualTo((byte)1);
			
			//退单有效、隐藏、全部状态
			if (null != model.getOrderView()) {
				if (model.getOrderView() == 0) {
					List<Byte> list = new ArrayList<Byte>();
					list.add((byte) 4);
					list.add((byte) 10);
					criteria.andOrderReturnOrderStatusNotIn(list);
				} else if (model.getOrderView() == 2) {
					List<Byte> list = new ArrayList<Byte>();
					list.add((byte) 4);
					list.add((byte) 10);
					criteria.andOrderReturnOrderStatusIn(list);
				}
			}
			
			//是否收到货   isGoodReceived
			if(null != model.getIsGoodReceived() && -1 != model.getIsGoodReceived() ){
				condition = false;
				criteria.andIsGoodReceivedEqualTo(model.getIsGoodReceived());
			}
			
			//是否入库  checkinStatus
			if(null != model.getCheckinStatus()  && -1 != model.getCheckinStatus()){
				condition = false;
				criteria.andCheckinStatusEqualTo(model.getCheckinStatus());
			}
			
			//质检状态  	qualityStatus
			if(null != model.getQualityStatus()  && -1 != model.getQualityStatus()){
				condition = false;
				criteria.andQualityStatusEqualTo(model.getQualityStatus());
			}
			
			//退货人手机号
			if(StringUtil.isNotNull(model.getReturnMobile()) && 11 ==model.getReturnMobile().length() ){
				condition = false;
				String encMobile = model.getReturnMobile();
				List<String> mobiles = new ArrayList<String>();
				mobiles.add(model.getReturnMobile());
				mobiles.add(encMobile);
				//criteria.andMobileIn(mobiles);
				criteria.andReturnMobileIn(mobiles);
			}
			
			//退货入库
			if(StringUtil.isNotNull(model.getWarehouseCode()) && ! "-1".equals(model.getWarehouseCode())){
				condition = false;
				criteria.andWarehouseCodeEqualTo(model.getWarehouseCode());
			}

			if(StringUtil.isNotNull(model.getReturnSn())){
				condition = false;
				criteria.andReturnSnEqualTo(model.getReturnSn());
			}
			
		   if(StringUtil.isNotNull(model.getRelatingOrderSn())){
				condition = false;
				criteria.andRelatingOrderSnEqualTo(model.getRelatingOrderSn());
			}
			
			
			if(StringUtil.isNotNull(model.getOrderSn())){
				condition = false;
				criteria.andOrderSnEqualTo(model.getOrderSn()); //交货单
			}
			
			if(StringUtil.isNotNull(model.getRelatingOrderSn())){
				condition = false;
				criteria.andRelatingOrderSnEqualTo(model.getRelatingOrderSn());
			}
			
			if(StringUtil.isNotNull(model.getOrderOutSn())){
				condition = false;
				criteria.andOrderOutSnEqualTo(model.getOrderOutSn());
			}
			
			if(null!=model.getReturnOrderStatus()&&model.getReturnOrderStatus()>=0){
				condition = false;
				criteria.andReturnOrderStatusEqualTo(model.getReturnOrderStatus());
			}
			
			if(StringUtil.isNotNull(model.getReferer()) && !"-1".equals(model.getReferer())){
				condition = false;
				if(!model.getReferer().equals("-1"))
				criteria.andRefererLike(model.getReferer());
			}
			
			if(null!=model.getReturnPay()&&model.getReturnPay()>=0){
				condition = false;
				criteria.andReturnPayEqualTo(model.getReturnPay());
			}
			
			if (null != model.getRelatingOrderType()&&model.getRelatingOrderType()>=0) {
				condition = false;
				criteria.andRelatingOrderTypeEqualTo(model.getRelatingOrderType());
			}
			
			if(StringUtil.isNotNull(model.getUserName())){
				condition = false;
				criteria.andUserNameLike("%"+model.getUserName()+"%");
			}
		
			//订单状态
			if(null != model.getOrderOrderStatus() && -1 != model.getOrderOrderStatus()){
				condition = false;
				criteria.andOrderOrderStatusEqualTo(model.getOrderOrderStatus());
			}
			
			//订单财务状态
	        if(null != model.getOrderPayStatus() && -1 != model.getOrderPayStatus()){
	        	condition = false;
	        	criteria.andOrderPayStatusEqualTo(model.getOrderPayStatus());
			}
			
	    	//订单物流状态
	        if(null != model.getOrderShipStatus() && -1 != model.getOrderShipStatus()){
	        	condition = false;
	        	criteria.andOrderShipStatusEqualTo(model.getOrderShipStatus());
			}
	
	//		criteria.andOrderRefundAndorderReturnByReturnSn();
		//	criteria.andOrderReturnAndOrderInfoByOrderSn();
		//	criteria.andOrderReturnShipAndOrderReturnByReturnSn();
		//	criteria.andChannelInfoAndOrderInfoByReturnSn();
		
	        //退货11位码 
	        
			if (StringUtil.isNotNull(model.getSkuSn())) {
				condition = false;
			//	example.setUserOg(true);
				if ( 11 ==  model.getSkuSn().length() ) {	
					criteria.andCustomCodeEqualTo(model.getSkuSn());
				} else if(6 ==   model.getSkuSn().length() ) {
					criteria.andCustomCodeLike(model.getSkuSn());
				}
			}
	        
	        
	  /*  	if(StringUtil.isNotNull(model.getSkuSn())){
				if(11==model.getSkuSn().length()){	
					condition = false;
			
					OrderReturnGoodsExample  orderReturnGoodsExample = new OrderReturnGoodsExample();
					
					orderReturnGoodsExample.or().andCustomCodeEqualTo(model.getSkuSn());
					List<OrderReturnGoods> productBarcodeList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
					String 	skuSn = "";
					if(StringUtil.isListNotNull(productBarcodeList)){
						skuSn = productBarcodeList.get(0).getCustomCode();
						criteria.andCustomCodeEqualTo(skuSn);	
						example.setSkuSn(model.getSkuSn());
					} else{
						logger.error("查询订单退货11位码失败！");
						Paging paging = new Paging(0, null);
						paging.setMessage("查询订退货11位码失败！");
						return paging;
					}
					
				} else if  (6==model.getSkuSn().length()){//退货6位码 
				
					condition = false;	
					OrderReturnGoodsExample  orderReturnGoodsExample = new OrderReturnGoodsExample();
					
					orderReturnGoodsExample.or().andGoodsSnEqualTo(model.getSkuSn());
					List<OrderReturnGoods> productBarcodeList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
					String 	skuSn = "";
					if(StringUtil.isListNotNull(productBarcodeList)){
						skuSn = productBarcodeList.get(0).getGoodsSn();
						//criteria.andskuSnEqualTo(skuSn);
						criteria.andGoodsSnEqualTo(skuSn);
						example.setSkuSn(model.getSkuSn());
					} else{
						logger.error("查询订单退货6位码失败！");
						Paging paging = new Paging(0, null);
						paging.setMessage("查询订退货6位码失败！");
						return paging;
					}
				
				}else{
					logger.error("查询订单退货编码码失败！");
					Paging paging = new Paging(0, null);
					paging.setMessage("查询订退货编码失败！");
					return paging;
				}
			}*/
	 
			//退单物流状态
			if(null !=  model.getShipStatus() &&  -1 != model.getShipStatus()){
				condition = false;
				criteria.andShipStatusEqualTo(model.getShipStatus());
			}
			
			//回退客服
			if(null !=  model.getBackToCs() &&  -1 != model.getBackToCs()){
				condition = false;
				criteria.andBackToCsEqualTo(model.getBackToCs());
			}

			if(null!=model.getStReturnTotalFee()&&null!=model.getEnReturnTotalFee()){
				condition = false;
				criteria.andReturnTotalFeeBetween(model.getStReturnTotalFee(), model.getEnReturnTotalFee());
			}else if(null!=model.getStReturnTotalFee()){
				condition = false;
				criteria.andReturnTotalFeeGreaterThanOrEqualTo(model.getStReturnTotalFee());
			}else if(null!=model.getEnReturnTotalFee()){
				condition = false;
				criteria.andReturnTotalFeeLessThanOrEqualTo(model.getStReturnTotalFee());
			}
			
			if(StringUtil.isNotNull(model.getReturnInvoiceNo())){
				condition = false;
				criteria.andReturnInvoiceNoEqualTo(model.getReturnInvoiceNo());
			}
			
			if(null != model.getPayStatus() && model.getPayStatus() >= 0){
				condition = false;
				criteria.andPayStatusEqualTo(model.getPayStatus());
			}
			
			//结算时间
			if (null!=model.getSelectTimeType()&&model.getSelectTimeType().equals("clearTime")) {
				
				if(StringUtil.isNotNull(model.getStartTime())&&StringUtil.isNotNull(model.getEndTime())){
					condition = false;
					criteria.andClearTimeBetween(DateTimeUtils.parseStr(model.getStartTime()) ,DateTimeUtils.parseStr(model.getEndTime()));
				}else if(StringUtil.isNotNull(model.getStartTime())){
					condition = false;
					criteria.andClearTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(model.getStartTime()));	
				}else if(StringUtil.isNotNull(model.getEndTime())){
					condition = false;
					criteria.andClearTimeLessThanOrEqualTo(DateTimeUtils.parseStr(model.getEndTime()));	
				}
			}
			
			//生成退单时间
			if (null!=model.getSelectTimeType()&&model.getSelectTimeType().equals("addTime")) {
			
				if(StringUtil.isNotNull(model.getStartTime())&&StringUtil.isNotNull(model.getEndTime())){
					condition = false;
					criteria.andAddTimeBetween(DateTimeUtils.parseStr(model.getStartTime()) ,DateTimeUtils.parseStr(model.getEndTime()));
				} else if(StringUtil.isNotNull(model.getStartTime())){
					condition = false;
					criteria.andAddTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(model.getStartTime()));	
				} else if(StringUtil.isNotNull(model.getEndTime())){
					condition = false;
					criteria.andAddTimeLessThanOrEqualTo(DateTimeUtils.parseStr(model.getEndTime()));	
				}
			}
			
			//入库时间
			if (null!=model.getSelectTimeType()&&model.getSelectTimeType().equals("checkinTime")) {
				
				if(StringUtil.isNotNull(model.getStartTime())&&StringUtil.isNotNull(model.getEndTime())){
					condition = false;
					criteria.andCheckinTimeBetween(DateTimeUtils.parseStr(model.getStartTime()) ,DateTimeUtils.parseStr(model.getEndTime()));
				} else if(StringUtil.isNotNull(model.getStartTime())){
					condition = false;
					criteria.andCheckinTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(model.getStartTime()));	
				} else if(StringUtil.isNotNull(model.getEndTime())){
					condition = false;
					criteria.andCheckinTimeLessThanOrEqualTo(DateTimeUtils.parseStr(model.getEndTime()));	
				}
			}
			
			//确定时间
			if (null!=model.getSelectTimeType()&&model.getSelectTimeType().equals("confirm_time")) {
				
				if(StringUtil.isNotNull(model.getStartTime())&&StringUtil.isNotNull(model.getEndTime())){
					condition = false;
					criteria.andConfirmTimeBetween(DateTimeUtils.parseStr(model.getStartTime()) ,DateTimeUtils.parseStr(model.getEndTime()));
				}else if(StringUtil.isNotNull(model.getStartTime())){
					condition = false;
					criteria.andConfirmTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(model.getStartTime()));	
				}else if(StringUtil.isNotNull(model.getEndTime())){
					condition = false;
					criteria.andConfirmTimeLessThanOrEqualTo(DateTimeUtils.parseStr(model.getEndTime()));	
				}
			}
			
			//处理状态 （0无操作，1退货，2修补，3销毁，4换货）',
			if(null != model.getProcessType() && model.getProcessType() >= 0){
				condition = false;
				criteria.andProcessTypeEqualTo(model.getProcessType());
			}
			
			if(null != model.getBackToCs() && model.getBackToCs() >= 0){
				criteria.andBackToCsEqualTo(model.getBackToCs());
			}
			
			if(null!=model.getReturnType()&&model.getReturnType()>=0){
				
				// 退单类型
				if(model.getReturnType() > -1){
					condition = false;
					if(model.getReturnType() == 99) { // 额外退款单类型
						criteria.andReturnTypeEqualTo((byte) 4);
					//	criteria.andOrderTypeEqualTo((byte) 2);
					} /* else if(3  ==  model.getReturnType()){
					
						criteria.andReturnTypeEqualTo((byte) 3);
						
						List<Byte> list = new ArrayList<Byte>();
						list.add((byte)2);
						list.add((byte)3);
						criteria.andRefundTypeIn(list); //退款类型
					}*/
					else {
						criteria.andReturnTypeEqualTo(model.getReturnType());
					}
				}
			}
			
			//'1,预付款，2保证金',
			if(null!=model.getReturnSettlementType()&&model.getReturnSettlementType()>=0){
				condition = false;
				criteria.andReturnSettlementTypeEqualTo(model.getReturnSettlementType());
			}
	
			if(StringUtil.isNotNull(model.getReturnReason())){
				if(!model.getReturnReason().equals("-1")){
					condition = false;
					criteria.andReturnReasonEqualTo(model.getReturnReason());
				}
			}
			// 订单所属店铺
			/*if (StringUtil.isNotEmpty(model.getOrderFromFirst()) && !model.getOrderFromFirst().equals("-1")) {
				List<String> orderForms = orderInfoService.getOrderForms(model.getOrderFromFirst(), model.getOrderFromSec(),
						model.getOrderFroms());
				if (StringUtil.isListNotNull(orderForms)) {
					condition = false;
					criteria.andOrderFromIn(orderForms);
				} else {
					logger.error("查询订单来源列表失败！");
					Paging paging = new Paging(0, null);
					paging.setMessage("查询订单来源列表失败！");
					return paging;
				}
			}*/
			// 订单所属站点
			if (StringUtil.isNotEmpty(model.getOrderFromSec()) && !model.getOrderFromSec().equals("-1")) {
				condition = false;
				criteria.andSiteCodeEqualTo(model.getOrderFromSec());
			} else {
				condition = false;
				criteria.andSiteCodeIn(model.getSites());
			}
			// 订单所属站点
			if (StringUtil.isArrayNotNull(model.getOrderFroms())) {
				if (model.getOrderFroms().length > 1) {
					condition = false;
					criteria.andChannelCodeIn(Arrays.asList(model.getOrderFroms()));
				} else {
					condition = false;
					criteria.andChannelCodeEqualTo(model.getOrderFroms()[0]);
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
		
		List<OrderReturnListVO> list = new ArrayList<OrderReturnListVO>();
		int count = 0;
		if("1".equals(model.getExportTemplateType())){//财务模版
			list = orderReturnSearchMapper.selectOrderReturnListByFinancialExample(example);
			count = orderReturnSearchMapper.countOrderReturnListByFinancialExample(example);
		} else if("2".equals(model.getExportTemplateType())){ //物流模版
			list = orderReturnSearchMapper.selectOrderReturnListByLogisticsExample(example);
			count = orderReturnSearchMapper.countOrderReturnListByLogisticsExample(example);
		}else{//默认
			list = orderReturnSearchMapper.selectOrderReturnListByExample(example);
			count = orderReturnSearchMapper.countOrderReturnListByExample(example);
		}
		
		Paging paging = new Paging(count, list);
		return paging;
	
	}


	@Override
	public List<OrderReturnAction> getOrderReturnActionList(String returnSn) {
		OrderReturnActionExample orderReturnActionExample = new OrderReturnActionExample();
		orderReturnActionExample.setOrderByClause(" log_time desc ");
		orderReturnActionExample.or().andReturnSnEqualTo(returnSn);
		List<OrderReturnAction> returnActionList = orderReturnActionMapper.selectByExample(orderReturnActionExample);
		return returnActionList;
	}

	@Override
	public Paging getOrderRefundPage(OrderRefundListVO model, PageHelper helper)
			throws Exception {
		Map<String,Object>paramMap=new HashMap<String, Object>();
		
		//默认退单时间  降序
		paramMap.put("orderByClause","temp.add_time desc");
		paramMap.put("limitStart",helper.getStart());
		paramMap.put("limitNum",helper.getLimit());
		
		if (null != model) {
			
			//是否历史订单
			if(null!=model.getIsHistory()&&model.getIsHistory()==1){
				paramMap.put("listDataType", false);
			}else {
				paramMap.put("listDataType", true);
			}
			
			//关联退单号
			if(StringUtil.isNotNull(model.getRelatingReturnSn())){
				paramMap.put("relatingReturnSn", model.getRelatingReturnSn());
			}
			
			//关联订单号
			if(StringUtil.isNotNull(model.getRelatingOrderSn())){
				paramMap.put("relatingOrderSn", model.getRelatingOrderSn());
			}
			
			//关联外部交易号
			if(StringUtil.isNotNull(model.getOrderOutSn())){
				paramMap.put("orderOutSn", model.getOrderOutSn());
			}
			
			
			//是否需要退款
			if(null != model.getHaveRefund() ){
				paramMap.put("haveRefund", model.getHaveRefund());
			}
			
			//退单财务状态
			if(null != model.getReturnPayStatus()){
				paramMap.put("returnPayStatus", model.getReturnPayStatus());
			}
			
			//支付方式
			if(null!=model.getReturnPay()&&model.getReturnPay()>=0){
				paramMap.put("returnPay", model.getReturnPay());
			}
			
			//退余额状态：0未退，1已退
			if(null!=model.getBackbalance()){
				paramMap.put("backBalance", model.getBackbalance());
			}
			
			// 退单类型
			if(null!=model.getReturnType()&&model.getReturnType()>=0){
				if(model.getReturnType() > -1){
					if(model.getReturnType() == 99) { // 额外退款单类型
						paramMap.put("returnType", 4);
					} else {
						paramMap.put("returnType", model.getReturnType());
					}
				}
			}
			
			// 订单所属店铺
			if (StringUtil.isNotEmpty(model.getOrderFromFirst()) && !model.getOrderFromFirst().equals("-1")) {
				List<String> orderForms = orderInfoService.getOrderForms(model.getOrderFromFirst(), model.getOrderFromSec(),
						model.getOrderFroms());
				if (StringUtil.isListNotNull(orderForms)) {
					paramMap.put("orderForms", orderForms);
				} else {
					logger.error("查询订单来源列表失败！");
					Paging paging = new Paging(0, null);
					paging.setMessage("查询订单来源列表失败！");
					return paging;
				}
			}
		}
		List<OrderRefundListVO> list = orderRefundSearchMapper.selectOrderRefundListByExample(paramMap);
		List<SystemPayment>paymentList=systemPaymentService.getSystemPaymentList();
		
		//支付方式名称
		for(OrderRefundListVO orderRefundListVO:list){
			for(SystemPayment systemPayment:paymentList){
				if(orderRefundListVO.getReturnPay()==systemPayment.getPayId().intValue()){
					orderRefundListVO.setPayName(systemPayment.getPayName());
				}
			}
			
			//支付日志（默认取第一条）
			OrderPayExample orderPayExample = new OrderPayExample();
			orderPayExample.or().andPayIdEqualTo(orderRefundListVO.getReturnPay().byteValue()).andOrderSnEqualTo(orderRefundListVO.getRelatingOrderSn());
			/*List<OrderPay> payList = orderPayMapper.selectByExample(orderPayExample);
			if(CollectionUtils.isNotEmpty(payList)){
				orderRefundListVO.setPayNote(payList.get(0).getPayNote());
			}*/
		}
		
		int count = orderRefundSearchMapper.countOrderRefundListByExample(paramMap);
		Paging paging = new Paging(count, list);
		return paging;
	}




}
