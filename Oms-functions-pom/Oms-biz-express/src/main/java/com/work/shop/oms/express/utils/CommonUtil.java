package com.work.shop.oms.express.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.express.bean.ExpressContent;
import com.work.shop.oms.express.bean.ExpressInfo;

public class CommonUtil {

	private static Logger logger = Logger.getLogger(CommonUtil.class);

	/**
	 * linux系统获取本机内网地址
	 */
	public static String getLocalIP() {
		String ip = "";
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				if (ni.getName().equals("eth0")) {
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = ips.nextElement().getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return ip;
	}

	protected static Document getDoc(String note) throws DocumentException {
		Document doc = DocumentHelper.parseText(note);
		return doc;
	}

	@SuppressWarnings("rawtypes")
	public static ExpressInfo xmlToStringForYTO(String xml) {
		try {
			if (xml == null || xml.indexOf("remark") < 0) {
				return null;
			}
			Document doc = getDoc(xml);
			Element rp = doc.getRootElement();
			if (rp != null) {
				Element trackNo = rp.element("orders");
				Element order = trackNo.element("order");
				Element sn = order.element("mailNo");
				Element steps = order.element("steps");
				List dataList = steps.elements("step");
				if (dataList.size() > 0) {
					Iterator it = dataList.iterator();
					int i = 0;
					ExpressInfo info = new ExpressInfo();
					List<ExpressContent> contents = new ArrayList<ExpressContent>();
					String invoice = sn.getText();
					while (it.hasNext()) {
						Element e = (Element) it.next();
						if (!e.hasContent()) {
							return null;
						}
						Element date = e.element("acceptTime");
						Element address = e.element("acceptAddress");
						Element remark = e.element("remark");
						if (date == null || address == null || remark == null) {
							if (i == 0) {
								return null;
							}
							continue;
						}
						ExpressContent data = new ExpressContent();
						data.setContext(address.getText() + " "
								+ remark.getText());
						data.setTime(date.getText());
						contents.add(data);
						i++;
					}
					if (contents.isEmpty() || contents.size() < 1) {
						return null;
					} else {
						info.setCodenumber(invoice);
						info.setData(contents);
						info.setCom("yto");
						info.setCondition("F00");
						info.setMessage("ok");
						info.setStatus(1);
						info.setState(0);
						info.setNu(invoice);
						info.setCompanytype("yto");
						return info;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static List<ExpressInfo> xmlToStringForBatchYTO(String xml) {
		List<ExpressInfo> expressInfo = null;
		try {
			if (xml == null || xml.indexOf("remark") < 0) {
				return null;
			}
			Document doc = getDoc(xml);
			Element rp = doc.getRootElement();
			if (rp != null) {
				Element trackNo = rp.element("orders");
				if (trackNo == null) {
					return null;
				}
				List<Element> orderList = trackNo.elements("order");
				if (orderList == null || orderList.isEmpty()) {
					return null;
				}
				expressInfo = new ArrayList<ExpressInfo>();
				for (Element order : orderList) {
					Element sn = order.element("mailNo");
					Element steps = order.element("steps");
					List dataList = steps.elements("step");
					if (dataList.size() > 0) {
						Iterator it = dataList.iterator();
						int i = 0;
						ExpressInfo info = new ExpressInfo();
						List<ExpressContent> contents = new ArrayList<ExpressContent>();
						String invoice = sn.getText();
						while (it.hasNext()) {
							Element e = (Element) it.next();
							if (!e.hasContent()) {
								continue;
							}
							Element date = e.element("acceptTime");
							Element address = e.element("acceptAddress");
							Element remark = e.element("remark");
							if (date == null || address == null
									|| remark == null) {
								continue;
							}
							ExpressContent data = new ExpressContent();
							data.setContext(address.getText() + " "
									+ remark.getText());
							data.setTime(date.getText());
							contents.add(data);
							i++;
						}
						if (contents.isEmpty() || contents.size() < 1) {
							continue;
						} else {
							info.setCodenumber(invoice);
							info.setData(contents);
							info.setCom("yto");
							info.setCondition("F00");
							info.setMessage("ok");
							info.setStatus(1);
							info.setState(0);
							info.setNu(invoice);
							info.setCompanytype("yto");
							expressInfo.add(info);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return expressInfo;
	}

	public static String xmlToJSONForYTO(String xml) {
		ExpressInfo info = xmlToStringForYTO(xml);
		return JSON.toJSONString(info);
	}

	@SuppressWarnings("unchecked")
	public static ExpressInfo xmlBS(String note) {
		try {
			JSONArray bean = (JSONArray) JSON.parse(note);
			JSONObject obj = (JSONObject) bean.get(0);
			ExpressInfo exInfo = new ExpressInfo();
			List<Map<String, String>> info = (List<Map<String, String>>) obj
					.get("info");
			// StringBuffer steps = new StringBuffer();
			List<ExpressContent> contents = new ArrayList<ExpressContent>();
			String invoice = "";
			for (Map<String, String> map : info) {
				String time = map.get("at");
				String address = map.get("current_site") + " "
						+ map.get("status");
				String memo = map.get("employeeName");
				invoice = map.get("invoiceNo");
				ExpressContent data = new ExpressContent();
				data.setContext(address + " " + memo);
				data.setTime(time);
				contents.add(data);
			}
			exInfo.setCodenumber(invoice);
			exInfo.setData(contents);
			exInfo.setCom("bs");
			exInfo.setCondition("F00");
			exInfo.setMessage("ok");
			exInfo.setStatus(1);
			exInfo.setState(0);
			exInfo.setNu(invoice);
			exInfo.setCompanytype("bs");
			return exInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	// 比较日期大小
	public static boolean compareDate(Date d1, Date d2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		long millsecond1 = cal.getTimeInMillis();
		cal.setTime(d2);
		long millsecond2 = cal.getTimeInMillis();
		return millsecond1 < millsecond2;

	}


	public static List<Map<String, OrderExpressTracing>> subExpressbList(
			List<OrderExpressTracing> resultList, int max, int threadCount) {

		int perCount = resultList.size() / threadCount;
		int mod = resultList.size() % threadCount;
		int lastCount = perCount;
		if (mod > 0) {
			lastCount = perCount + mod;
		}
		List<Map<String, OrderExpressTracing>> mapList = new ArrayList<Map<String, OrderExpressTracing>>();
		if (perCount == lastCount) {
			for (int i = 0; i < threadCount; i++) {
				Map<String, OrderExpressTracing> map = new HashMap<String, OrderExpressTracing>();
				for (int j = 0; j < perCount; j++) {
					map.put(resultList.get(i * perCount + j).getTrackno(),
							resultList.get(i * perCount + j));
				}
				mapList.add(map);
			}
		} else {
			int sign = 0;
			for (int i = 0; i < threadCount - 1; i++) {
				Map<String, OrderExpressTracing> map = new HashMap<String, OrderExpressTracing>();
				for (int j = 0; j < perCount; j++) {
					map.put(resultList.get(i * perCount + j).getTrackno(),
							resultList.get(i * perCount + j));
					sign++;
				}
				mapList.add(map);
			}

			Map<String, OrderExpressTracing> map1 = new HashMap<String, OrderExpressTracing>();
			for (int k = sign; k < resultList.size(); k++) {
				map1.put(resultList.get(k).getTrackno(), resultList.get(k));
			}
			mapList.add(map1);

		}
		return mapList;
	}

}
