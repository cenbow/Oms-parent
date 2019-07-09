package com.work.shop.oms.mq.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.work.shop.oms.common.bean.DistributeShipBean;
import com.work.shop.oms.common.bean.OrderToShipProviderBean;

public class OrderUtil {

	private static Logger log = Logger.getLogger(OrderUtil.class);


	public Double roundDouble(double val, int precision) {
		Double ret = null;
		try {
			double factor = Math.pow(10, precision);
			ret = Math.floor(val * factor + 0.5) / factor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	// FIXME
	public Map<String, List<OrderToShipProviderBean>> mergeDataByOrderSN(List<OrderToShipProviderBean> obj) {
		Map<String, List<OrderToShipProviderBean>> list1 = new HashMap<String, List<OrderToShipProviderBean>>();
		String ordersn = "";
		// System.out.println(obj.size());
		for (int j = 0; j < obj.size(); j++) {
			OrderToShipProviderBean p = obj.get(j);

			ordersn = p.getOuter_code();
			// System.out.println(ordersn);
			List<OrderToShipProviderBean> kk = list1.get(ordersn);

			if (kk == null) {
				// System.out.println(ordersn);
				List<OrderToShipProviderBean> ll = new ArrayList<OrderToShipProviderBean>();
				ll.add(p);
				list1.put(ordersn, ll);
				// System.out.println(ll.size());
			} else {
				list1.remove(ordersn);
				kk.add(p);
				list1.put(ordersn, kk);
			}
		}
		return list1;
	}

	public Map<String, List<DistributeShipBean>> mergeDataShipByOrderSN(List<DistributeShipBean> obj) {
		Map<String, List<DistributeShipBean>> shipProviderMap = new HashMap<String, List<DistributeShipBean>>();
		String ordersn = "";
		for (int j = 0; j < obj.size(); j++) {
			DistributeShipBean p = (DistributeShipBean) obj.get(j);
			ordersn = p.getOrderSn();
			if (!shipProviderMap.containsKey(ordersn)) {
				List<DistributeShipBean> item = new ArrayList<DistributeShipBean>();
				shipProviderMap.put(ordersn, item);
			}
			List<DistributeShipBean> spbList = shipProviderMap.get(ordersn);
			spbList.add(p);
		}
		return shipProviderMap;
	}

	public List<OrderToShipProviderBean> mergeListObjectOfNum(List<OrderToShipProviderBean> obj) {
		List<OrderToShipProviderBean> list1 = new ArrayList();
		List<OrderToShipProviderBean> obj1 = obj;
		String temp = "";
		for (int i = 0; i < obj.size(); i++) {

			OrderToShipProviderBean p = (OrderToShipProviderBean) obj.get(i);
			String sku = p.getProd_id();
			String wh = p.getRcv_warehcode();
			String num = p.getGoods_number();
			String ship = p.getShip_sn();
			// //System.out.println(wh+sku+"----num---"+num);
			for (int j = i; j < obj1.size(); j++)

			{
				if (i != j) {
					OrderToShipProviderBean p1 = (OrderToShipProviderBean) obj1.get(j);
					String sku1 = p1.getProd_id();
					String wh1 = p1.getRcv_warehcode();
					String ship1 = p1.getShip_sn();

					if (sku1.equals(sku) && wh1.equals(wh) && ship1.equals(ship)) {

						num = "" + (Long.parseLong(num) + Long.parseLong(p1.getGoods_number()));

						// //System.out.println("in"+num);

					}

				}

			}

			if (!temp.contains((sku + wh + ship))) {
				temp += (sku + wh + ship + ",");
				p.setGoods_number(num);
				list1.add(p);
				// //System.out.println( "hi" );
				// //System.out.println( sku+ wh+"--"+p.getGoods_number() );
			}
		}
		return list1;
	}

	/**
	 * ordership是按照最终发货仓 获取最终发货仓一样的记录 并汇总数量 rcv_warehcode 实际出货仓 orderwh是按照实际发货仓
	 * dist_wareh_code 仓库code ordership按照最终发货仓 虚拟仓库相同，累加。
	 * 
	 * @param obj
	 * @return
	 */
	public List<OrderToShipProviderBean> mergeListObjectOfNumByWH(List<OrderToShipProviderBean> obj) {
		List<OrderToShipProviderBean> list1 = new ArrayList<OrderToShipProviderBean>();
		List<OrderToShipProviderBean> obj1 = obj;
		String temp = "";
		for (int i = 0; i < obj.size(); i++) {

			OrderToShipProviderBean p = new OrderToShipProviderBean();
			try {
				BeanUtils.copyProperties(p, obj.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// String wh = p.getRcv_warehcode();
			String wh = p.getDist_wareh_code();
			String sku = p.getProd_id();
			// 虚拟仓和商品都相同，数量累加。
			String num = p.getGoods_number();
			for (int j = i + 1; j < obj1.size(); j++) {
				// if (i != j) {
				OrderToShipProviderBean p1 = obj1.get(j);

				String wh1 = p1.getDist_wareh_code();

				// String sku1 = p1.getProd_id();
				// if (wh1.equals(wh)&&sku1.equals(sku)) {
				if (wh1.equals(wh)) {
					num = "" + (Long.parseLong(num) + Long.parseLong(p1.getGoods_number()));
				}

				// }

			}

			if (!temp.contains((wh))) {
				temp += (wh + ",");
				p.setGoods_number(num);
				list1.add(p);
			}
		}

		/**
		 * 测试代码BEGIN
		 */
		if (list1 == null || list1.size() < 1) {

		} else {
			for (int i = 0; i < list1.size(); i++) {
				OrderToShipProviderBean b = (OrderToShipProviderBean) list1.get(i);
				log.info("list1------" + b.toString());

			}
		}
		/**
		 * 测试代码END
		 */

		/**
		 * 测试代码BEGIN
		 */
		if (obj == null || obj.size() < 1) {

		} else {
			for (int i = 0; i < obj.size(); i++) {
				OrderToShipProviderBean b = (OrderToShipProviderBean) obj.get(i);
				log.info("obj------" + b.toString());

			}
		}
		/**
		 * 测试代码END
		 */

		return list1;
	}
}
