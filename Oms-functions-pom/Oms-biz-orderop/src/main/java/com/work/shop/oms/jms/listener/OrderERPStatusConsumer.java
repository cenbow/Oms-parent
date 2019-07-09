package com.work.shop.oms.jms.listener;

import java.net.URLDecoder;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.ErpStatusInfo;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.common.bean.ErpStatusInfoTemp;
import com.work.shop.oms.dao.ErpStatusInfoMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.utils.CommonUtils;

/**
 * ERP仓内作业信息同步,并将淘宝信息推送至
 * @author
 *
 */
public class OrderERPStatusConsumer extends Consumer {
	private final static Logger logger = Logger.getLogger(OrderERPStatusConsumer.class);
	
	@Resource
	private ErpStatusInfoMapper erpStatusInfoMapper;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;
	
	@Override
	public String getDATA(String text) {
		try {
			String data = URLDecoder.decode(text , "UTF-8");
			logger.info("Get Message from QUORDERERPSTATUSINFO :"+data);
			ErpStatusInfoTemp erpStatusInfo  = JSON.parseObject(data, ErpStatusInfoTemp.class);
			processObject(erpStatusInfo);
//			List<ErpStatusInfo> erpStatusInfoList  = JSON.parseArray(data, ErpStatusInfo.class);
//			if(CommonUtils.isNotNullOrEmpty(erpStatusInfo.getFromSystem())){
//				for(ErpStatusInfo  erpInfo:erpStatusInfoList){
//					processObject(erpInfo);
//				}
//			}
		} catch (Exception e) {
			logger.error("processObject :"+e.getMessage(), e);
		}
		return "";
	}

	private void processObject(ErpStatusInfoTemp erpStatusInfo) {
		//插入信息到数据库
		//忽略字段 fromSystem
		ErpStatusInfo statusInfo = new ErpStatusInfo();
		try {
			BeanUtils.copyProperties(statusInfo, erpStatusInfo);
		} catch (Exception e) {
			logger.error("", e);
		}
		statusInfo.setDepotCode(erpStatusInfo.getMajorWarehouseCode());
		erpStatusInfoMapper.insert(statusInfo);
		//针对揽件日期操作插入到ordership表中
		//statusInfoMapNew.put("DEG", "524288", "已发货");
		//statusInfoMapNew.put("GDN", "524288", "已发货");
		if(CommonUtils.isNotNullOrEmpty(erpStatusInfo.getMajorWarehouseCode())
				&& "524288".equals(erpStatusInfo.getOpState())
				&&("GDN".equals(erpStatusInfo.getDocType())||"DEG".equals(erpStatusInfo.getDocType()))){
			OrderDepotShip oShip = new OrderDepotShip();
			OrderDepotShipExample shipExample=new OrderDepotShipExample();
			oShip.setPickupDate(erpStatusInfo.getOpDate());
			oShip.setOrderSn(erpStatusInfo.getOsOrderCode());
			oShip.setDepotCode(erpStatusInfo.getMajorWarehouseCode());
			OrderDepotShipExample.Criteria cri = shipExample.or() ;
			cri.andOrderSnEqualTo(erpStatusInfo.getOsOrderCode());
			cri.andDepotCodeEqualTo(erpStatusInfo.getMajorWarehouseCode());
			orderDepotShipMapper.updateByExampleSelective(oShip, shipExample);
			logger.info("insert processObject order Ship :"+JSON.toJSONString(oShip));
		}
		if(CommonUtils.isNullOrEmpty(erpStatusInfo.getFromSystem())){
			erpStatusInfo.setFromSystem(orderDistributeMapper.selectByPrimaryKey(erpStatusInfo.getOsOrderCode()).getOrderFrom());
		}
	}
	
	public static MultiKeyMap statusInfoMap = new MultiKeyMap();
	static {
		//订单已审核----已转单 ； 订单配货中----已通知配货  ； 订单已配货 -----待配货； 交货单分拣中 ---- 已打拣货单；    
		//交货单分拣完成-----已打包；  交货单已出库--已出库；出库单已托运---已发货
		// 订货单
		statusInfoMap.put("IDT", "2", TaobaoTradeTraces.TO_SYSTEM);
		statusInfoMap.put("IDT", "65536", TaobaoTradeTraces.ALLOCATION_NOTIFIED);
		statusInfoMap.put("IDT", "131072", TaobaoTradeTraces.WAIT_ALLOCATION);
		// 交货单
		statusInfoMap.put("DGN", "65536", TaobaoTradeTraces.SEND_PRINTED);
		statusInfoMap.put("DGN", "131072", TaobaoTradeTraces.PACKAGED);
		// 出库单
		statusInfoMap.put("GDN", "65536", TaobaoTradeTraces.OUT_WAREHOUSE);
		//O2O出库
		statusInfoMap.put("DEG", "131072", TaobaoTradeTraces.OUT_WAREHOUSE);
	}
	/**
	 * 淘宝发货单状态
	 */
	class TaobaoTradeTraces {
		/**
		 * 系统已接单
		 */
		public static final String TO_SYSTEM = "X_TO_SYSTEM";
		/**
		 * 已客审
		 */
		public static final String SERVICE_AUDITED = "X_SERVICE_AUDITED";
		/**
		 * 已通知配货
		 */
		public static final String ALLOCATION_NOTIFIED = "X_ALLOCATION_NOTIFIED";
		/**
		 * 待配货
		 */
		public static final String WAIT_ALLOCATION = "X_WAIT_ALLOCATION";
		/**
		 * 已打发货单
		 */
		public static final String SEND_PRINTED = "X_SEND_PRINTED";
		/**
		 * 已打物流单
		 */
		public static final String LOGISTICS_PRINTED = "X_LOGISTICS_PRINTED";
		/**
		 * 已打包
		 */
		public static final String PACKAGED = "X_PACKAGED";
		/**
		 * 已出库
		 */
		public static final String OUT_WAREHOUSE = "X_OUT_WAREHOUSE";
	}
}
