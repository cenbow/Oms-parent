package com.work.shop.oms.express.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.oms.api.express.service.OrderExpressTracingService;
import com.work.shop.oms.bean.ErpStatusInfo;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.bean.OrderExpressTracingExample;
import com.work.shop.oms.bean.OrderExpressTracingKey;
import com.work.shop.oms.dao.OrderExpressTracingMapper;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单物流抓取服务
 * @author lemon
 */
@Service
public class OrderExpressTracingServiceImpl implements OrderExpressTracingService {

	@Resource
	private OrderExpressTracingMapper orderExpressTracingMapper;

	@Resource
	SystemRegionAreaMapper systemRegionAreaMapper;

	/**
	 * 查询需要抓取物流信息列表
	 * @param tracing
	 * @return List<OrderExpressTracing>
	 */
	@Override
	public List<OrderExpressTracing> selectExpress(OrderExpressTracing tracing) {
		if (tracing == null) {
			return null;
		}
		OrderExpressTracingExample example = new OrderExpressTracingExample();
		OrderExpressTracingExample.Criteria criteria = example.or();
		criteria.limit(5000);
		List<Float> values = new ArrayList<Float>();
		// 已签收
		values.add(Constant.express_status_sign);
		// 退签
		values.add(Constant.express_status_returnSign);
		criteria.andExpressStatusNotIn(values);
		// 88 永不抓取
		criteria.andFetchFlagNotEqualTo((byte)88);
		Date endTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
		endTime = calendar.getTime();
		Date startTime = new Date();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(startTime);
		calendar2.set(Calendar.DATE, calendar2.get(Calendar.DATE) - 30);
		startTime = calendar2.getTime();
		criteria.andDeliveryTimeBetween(startTime, endTime);
		return orderExpressTracingMapper.selectByExampleWithBLOBs(example);
	}

	public OrderExpressTracing selectByPrimaryKey(OrderExpressTracingKey expressTracingKey) {
		return orderExpressTracingMapper.selectByPrimaryKey(expressTracingKey);
	}

	public Integer insertSelective(OrderExpressTracing orderExpress) {
		return orderExpressTracingMapper.insertSelective(orderExpress);
	}

	@Override
	public List<OrderExpressTracing> getTracingListByExpress(
			String expressCode, String trackno,String depotCode) {
		OrderExpressTracingExample example = new OrderExpressTracingExample();
		if(null == depotCode){
			example.or().andCompanyCodeEqualTo(expressCode).andTracknoEqualTo(trackno);
		}else{
			example.or().andCompanyCodeEqualTo(expressCode).andTracknoEqualTo(trackno).andDepotCodeEqualTo(depotCode);
		}
		return orderExpressTracingMapper.selectByExample(example);
	}
	public void setReceiveTimeExpress(OrderExpressTracing orderExpress) {
		OrderExpressTracingExample expressTracingExample = new OrderExpressTracingExample();
		expressTracingExample.or().andOrderSnEqualTo(orderExpress.getOrderSn());
		OrderExpressTracing updateExpressTracing = new OrderExpressTracing();
		updateExpressTracing.setExpressReceiveTime(new Date());
		orderExpressTracingMapper.updateByExampleSelective(updateExpressTracing, expressTracingExample);;
	}

	public void updateALlFetchStatus() {
		OrderExpressTracingExample expressTracingExample = new OrderExpressTracingExample();
		OrderExpressTracingExample.Criteria criteria = expressTracingExample.or();
		criteria.andFetchFlagEqualTo((byte)1);
		criteria.andExpressStatusLessThan(13F);
		OrderExpressTracing updateExpressTracing = new OrderExpressTracing();
		updateExpressTracing.setUpdTime(new Date());
		updateExpressTracing.setFetchFlag((byte)0);
		orderExpressTracingMapper.updateByExampleSelective(updateExpressTracing, expressTracingExample);
	}

	public void updateExpressFetchStatus(List<OrderExpressTracing> orderExpressTracingList) {
		if (StringUtil.isListNull(orderExpressTracingList)) {
			return ;
		}
		for (OrderExpressTracing expressTracing : orderExpressTracingList) {
			OrderExpressTracingExample expressTracingExample = new OrderExpressTracingExample();
			OrderExpressTracingExample.Criteria criteria = expressTracingExample.or();
			criteria.andOrderSnEqualTo(expressTracing.getOrderSn());
			criteria.andDepotCodeEqualTo(expressTracing.getDepotCode());
			OrderExpressTracing updateExpressTracing = new OrderExpressTracing();
			updateExpressTracing.setUpdTime(new Date());
			updateExpressTracing.setFetchFlag(expressTracing.getFetchFlag());
			orderExpressTracingMapper.updateByExampleSelective(updateExpressTracing, expressTracingExample);
		}
	}

	public int updateTracing(OrderExpressTracing orderExpress) {
		synchronized(orderExpress) {
			return orderExpressTracingMapper.updateByPrimaryKeySelective(orderExpress);
		}
	}

	public List<OrderExpressTracing> getTracingListBySn(String orderSn,String depotCode) {
		OrderExpressTracingExample example = new OrderExpressTracingExample();
		if(null == depotCode){
			example.createCriteria().andOrderSnEqualTo(orderSn);
		}else{
			example.createCriteria().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(depotCode);
		}
		return orderExpressTracingMapper.selectByExampleWithBLOBs(example);
	}

	public List<ErpStatusInfo> queryErpStatusInfoOMS(String orderSn,String depotCode) {
		if (orderSn == null || orderSn.length() == 0||depotCode == null ||depotCode.length() == 0) {
			return null;
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("orderSn", orderSn);
		paramMap.put("depotCode", depotCode);
//		return erpStatusInfoReadMapper.selectByOrderSnOMS(paramMap);
		return null;
	}
	public List<ErpStatusInfo> queryErpStatusInfo(String orderSn) {
		if (orderSn == null || orderSn.length() == 0) {
			return null;
		}
//		return erpStatusInfoMapper.selectByOrderSn(orderSn);
		return null;
	}

	@Override
	public List<OrderExpressTracing> 	selectExpressInTimeFINNAL(Map<String, Object> map) {
//		return orderExpressTracingMapper.selectExpressInTimeFINNAL(map);
		return null;
	}
	public List<OrderExpressTracing> selectExpressInTime(Map<String, Object> map) {
		//orderExpressTracingMapper.updateExpressStatus();
//		return orderExpressTracingMapper.selectExpressInTime(map);
		return null;
	}
	public List<OrderExpressTracing> selectExpressInTimeGap(Map<String, Object> map) {
//		return orderExpressTracingMapper.selectExpressInTimeGap(map);
		return null;
	}
	
	
	public List<OrderExpressTracing> selectExpressInTimeFINNALGap(Map<String, Object> map) {
//		return orderExpressTracingMapper.selectExpressInTimeFINNALGap(map);
		return null;
	}
	
	@Override
	public List<OrderExpressTracing> selectExpressOutTime(
			Map<String, Object> map) {
//		return orderExpressTracingMapper.selectExpressOutTime(map);
		return null;
	}
	
	
	public void updateExpressStatus( ) {
//		  orderExpressTracingMapper.updateExpressStatus( );
	}
	 
	@Override
	public String getMaxOrderSN() {
		 
//		return orderExpressTracingMapper.getMaxOrderSN();
		return null;
	}
	
	
	
	@Override
	public String selectByTrackno(String trackno) {
		 
//		return orderExpressTracingMapper.selectByTrackno(trackno) ;
		return null;
	}
	
	
	public String selectNameByPrimaryKey(String id) {
		 
//		return systemRegionAreaReadMapper.selectNameByPrimaryKey(id);
		return null;
	}
	
	public List<OrderExpressTracing> getShipListOS(String days) {
//		return orderShipMbomsReadMapper.getShipListOS(days) ;
		return null;
	}
	
	public void updateEtl(String shipsn) {
		 
//		orderShipWriteMapper.updateEtl(shipsn) ;
	}

	public String selectByNo(String expressid ,String no) {
		 
//		return orderExpressTracingMapper.selectByNo(  no);
		return null;
	}

	/**
	 * 如果签收 ，返回NOTE 
	 * 如果不签收 ，返回null
	 */
	public String getRouteInfo(String trackno) {
		String ret = null ;
		OrderExpressTracingExample expressTracingExample = new OrderExpressTracingExample();
		expressTracingExample.or().andTracknoEqualTo(trackno);
		List<OrderExpressTracing> list = orderExpressTracingMapper.selectByExample(expressTracingExample);
		if(list!=null&&list.size()>0) {
			OrderExpressTracing ot = (OrderExpressTracing)list.get(0) ;
			if(ot.getExpressTimeSign()!=null) {
				ret = ot.getNote() ;
			}
			
		}
		return ret  ;
	}
	
	
	
	public String getRouteInfoArr(String tracknoarr) {
		String[] trackno  ;
		String ret = null ; 
		String ret1 = null ; 
		int j=0 ; 
		if(tracknoarr!=null) {
			trackno = tracknoarr.split(",") ; 
			for(int i =0 ; i< trackno.length ; i ++) {
				ret = getRouteInfo(trackno[i]) ;
				if(ret != null ) {
					j++ ; 
					if(j==1) ret1 = trackno[i]; 
					else ret1 += ","+trackno[i] ;
				}
			}
		}
		
		return ret1  ;
		 
	}

	@Override
	public List<OrderExpressTracing> selectExpressInTimeEMS(Map<String, Object> map) {
//		return orderExpressTracingMapper.selectExpressInTimeEMS(map);
		return null;
	}
	@Override
	public List<OrderExpressTracing> selectExpressAnyCompany(Map<String, Object> map) {
//		return orderExpressTracingMapper.selectExpressAnyCompany(map);
		return null;
	}
	@Override
	public List<OrderExpressTracing> selectExpressBGandYouFan(Map<String, Object> map) {
//		return orderExpressTracingMapper.selectExpressBGandYouFan(map);
		return null;
	}
	@Override
	public int updateExpressFetchStatusBycompaytrackno(OrderExpressTracing orderExpressTracing) {
//		return orderExpressTracingMapper.updateExpressFetchStatusBycompaytrackno(orderExpressTracing);
		return 1;
		
	}

	@Override
	public List<OrderExpressTracing> selectExpressSign() {
		
//		return orderExpressTracingMapper.selectExpressSign();
		return null;
	}

	@Override
	public String selectByTracknoAndOSsn(Map<String, Object> map) {
		
//		return orderExpressTracingMapper.selectByTracknoAndOSsn(map) ;
		return null;
	}

	@Override
	public void updateByPrimaryKeySelective(OrderExpressTracing orderExpress) {
		orderExpressTracingMapper.updateByPrimaryKeySelective(orderExpress);
		
	}

	@Override
	public List<OrderExpressTracing> selectOrderReturnExpressSign() {
//		return orderExpressTracingMapper.selectOrderReturnExpressSign();
		return null;
	}

	@Override
	public List<OrderExpressTracing> selectExpressByOrderSnAndtrackno(
			String orderSn, String trackNo) {
		if (StringUtil.isTrimEmpty(orderSn)) {
			return null;
		}
		OrderExpressTracingExample example = new OrderExpressTracingExample();
		OrderExpressTracingExample.Criteria criteria = example.or();
		criteria.andOrderSnEqualTo(orderSn);
		if (StringUtil.isNotEmpty(trackNo)) {
			criteria.andTracknoEqualTo(trackNo);
		}
		return orderExpressTracingMapper.selectByExampleWithBLOBs(example);
	}
}
