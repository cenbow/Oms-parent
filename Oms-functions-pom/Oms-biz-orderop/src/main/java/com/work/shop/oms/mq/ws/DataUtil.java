package com.work.shop.oms.mq.ws;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.work.shop.oms.common.bean.DistributeShipBean;
import com.work.shop.oms.common.bean.OrderToShipProviderBean;
import com.work.shop.oms.common.bean.OrderToShipProviderBeanERP;
import com.work.shop.oms.common.bean.OrderToShippedProviderBeanERP;

public class DataUtil {
	static Logger logger = Logger.getLogger(DataUtil.class);

	/**
	 * 分仓接口ERP传入数据转化
	 * 
	 * @param list
	 * @return
	 */
	public static List<OrderToShipProviderBean> ToOrderToShipProviderBean(List<OrderToShipProviderBeanERP> list) {
		List<OrderToShipProviderBean> retList = new ArrayList<OrderToShipProviderBean>();
		for (int i = 0; i < list.size(); i++) {
			OrderToShipProviderBeanERP beanERP = list.get(i);
			OrderToShipProviderBean bean = new OrderToShipProviderBean();
			bean.setDist_id(beanERP.getDEPOT_ID());
			bean.setOuter_code(beanERP.getORDER_SN());
			bean.setDist_wareh_code(beanERP.getMAJOR_SEND_WAREH_CODE());
			bean.setShip_code(beanERP.getSHIP_CODE());
			bean.setDistrate(beanERP.getDISTRATE());
			bean.setRcv_warehcode(beanERP.getDWARH_CODE());
			bean.setAgdist(beanERP.getAGDIST());
			bean.setGoods_number(beanERP.getDISTQTY());
			bean.setShip_sn(beanERP.getSHIP_SN());
			bean.setCountry(beanERP.getCOUNTRY());
			bean.setCounty(beanERP.getCOUNTY());
			bean.setCity(beanERP.getCITY());
			bean.setProvince(beanERP.getPROVINCE());
			bean.setShip_flag(beanERP.getSHIP_FLAG());
			bean.setShip_type(beanERP.getSTYPE());
			bean.setProd_id(beanERP.getPROD_ID());
			if ("0".equals(bean.getGoods_number()) || "".equals(bean.getGoods_number()))
				continue;
			retList.add(bean);
		}
		return retList;
	}

	public static List<DistributeShipBean> ToOrderToShippedProviderBean(List<OrderToShippedProviderBeanERP> list) {
		List<DistributeShipBean> retList = new ArrayList<DistributeShipBean>();
		for (int i = 0; i < list.size(); i++) {
			try {
				// TODO 数据转换
				OrderToShippedProviderBeanERP beanERP = list.get(i);
				logger.debug("收到发货数据(解析中...)" + beanERP.getORDERSN());
				DistributeShipBean ship = new DistributeShipBean();
				ship.setShipType(Integer.valueOf(beanERP.getSHOPTYPE()));
				ship.setOrderSn(beanERP.getORDERSN());
				ship.setCode(beanERP.getBILLCODE());
				ship.setDistWarehCode(beanERP.getDWARHCODE());
				ship.setShipCode(beanERP.getEXPRESSCODE());
				ship.setTtlQty(Integer.valueOf(beanERP.getGOODSNUMBER()));
				ship.setCsbNum(beanERP.getEXPRESSNUM());
				ship.setCustomCode(beanERP.getSKU());
				ship.setShipDate(beanERP.getSHIPDATE());
//				ship.setGoodsNumber(beanERP.getGOODSNUMBER());
				ship.setMajorSendWarehCode(beanERP.getMAJOR_SEND_WAREH_CODE());
				retList.add(ship);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return retList;
	}

}
