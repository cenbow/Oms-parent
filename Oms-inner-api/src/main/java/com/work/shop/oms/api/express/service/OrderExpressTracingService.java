package com.work.shop.oms.api.express.service;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.bean.ErpStatusInfo;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.bean.OrderExpressTracingKey;

public interface OrderExpressTracingService {
	
	public OrderExpressTracing selectByPrimaryKey(OrderExpressTracingKey expressTracingKey);
	
	public Integer insertSelective(OrderExpressTracing orderExpress);
	
	public List<OrderExpressTracing> selectExpressInTimeGap(Map<String, Object> map);
	
	public List<OrderExpressTracing> getTracingListBySn(String orderSn,String depotCode);
	
	public List<OrderExpressTracing> getTracingListByExpress(String expressCode,String trackno,String depotCode);
	
	public void updateALlFetchStatus();
	public void setReceiveTimeExpress(OrderExpressTracing orderExpress);
	
	public void updateExpressFetchStatus(List<OrderExpressTracing> orderExpressTracingList);
	
	public int updateExpressFetchStatusBycompaytrackno(OrderExpressTracing orderExpressTracing);
//	
	public  int  updateTracing(OrderExpressTracing orderExpress);
	
	public List<ErpStatusInfo> queryErpStatusInfoOMS(String orderSn,String depotCode);
	
	public List<ErpStatusInfo> queryErpStatusInfo(String orderSn);
	
	public List<OrderExpressTracing> selectExpressInTime(Map<String, Object> map);
	
	public List<OrderExpressTracing> selectExpressOutTime(Map<String, Object> map);

	List<OrderExpressTracing> selectExpressInTimeFINNAL(Map<String, Object> map);
	List<OrderExpressTracing> selectExpressInTimeEMS(Map<String, Object> map);
	
	String getMaxOrderSN();
	public void updateEtl(String shipsn)  ;

		
	public List<OrderExpressTracing> getShipListOS(String days);

	public String selectNameByPrimaryKey(String regionId) ;

	String selectByTrackno(String trackno); 
	String selectByTracknoAndOSsn(Map<String, Object> map);
	String getRouteInfo(String trackno); 
	String getRouteInfoArr(String trackno); 
	 
	public List<OrderExpressTracing> selectExpressInTimeFINNALGap(Map<String, Object> map);
	
	public void updateExpressStatus( );
	public String selectByNo(String expressid ,String no);
	/**
	 * 查询近30天指定公司的未完成且失败不超过指定次数快递信息,
	 * @param map
	 * @return
	 */
	List<OrderExpressTracing> selectExpressAnyCompany(Map<String, Object> map);
	
	/**
	 * 查询近30天指定公司的未完成且失败不超过指定次数快递信息,(邦购 有范)
	 * @param map
	 * @return
	 */
	List<OrderExpressTracing> selectExpressBGandYouFan(Map<String, Object> map);
	/**
	 * 查询快递已签收的物流
	 * @return
	 */
	List<OrderExpressTracing> selectExpressSign();
	
	/**
	 * 查询退单快递已签收的物流
	 * @return
	 */
	List<OrderExpressTracing> selectOrderReturnExpressSign();
	
	void updateByPrimaryKeySelective(OrderExpressTracing orderExpress);

	/**
	 * 查询需要抓取物流列表
	 * @param tracing
	 * @return List<OrderExpressTracing>
	 */
	List<OrderExpressTracing> selectExpress(OrderExpressTracing tracing);

	/**
	 * 
	 * @param orderSn
	 * @param trackno
	 * @return List<OrderExpressTracing>
	 */
	List<OrderExpressTracing> selectExpressByOrderSnAndtrackno(String orderSn, String trackNo);
}
