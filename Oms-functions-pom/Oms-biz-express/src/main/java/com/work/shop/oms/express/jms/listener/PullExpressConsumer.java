package com.work.shop.oms.express.jms.listener;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.express.service.OrderExpressTracingService;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.express.bean.ExpressInfo;
import com.work.shop.oms.express.utils.HttpClientUtil;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 快递物流抓取任务
 * @author lemon
 */
public class PullExpressConsumer implements MessageListener{
	
	private final static Logger logger = Logger.getLogger(PullExpressConsumer.class);
	
	@Autowired
	private OrderExpressTracingService orderExpressTracingService;
	
	@Override
	public void onMessage(Message text) {
		TextMessage textMessage = (TextMessage) text;
		String data = null;
		try{
			data = URLDecoder.decode(textMessage.getText() , "UTF-8");
			logger.info("Get Message from pull_express_info :"+data);
			OrderExpressTracing oet  = JSON.parseObject(data, OrderExpressTracing.class);
			// 快递单号
			String trackNo = oet.getTrackno();
			logger.info("Run process orderSn =" +oet.getOrderSn()+"; depotCode="+ oet.getDepotCode()+  ";trackNo=" + trackNo);

			if (oet.getFetchCount() > 126 || (trackNo != null && trackNo.trim().length() < 10)) {
				// 超过126(3个小时一次,七天)
				// 超过设定时限，抛弃
				oet.setFetchFlag((byte)88);
				orderExpressTracingService.updateTracing(oet);
				return;
			}

			// 公司编码
//			String message = "{\"message\":\"ok\",\"status\":\"1\",\"state\":\"3\",\"data\":[{\"time\":\"2012-07-07 13:35:14\",\"context\":\"客户已签收\"},{\"time\":\"2012-07-07 09:10:10\",\"context\":\"离开 [北京石景山营业厅] 派送中，递送员[温]，电话[]\"},{\"time\":\"2012-07-06 19:46:38\",\"context\":\"到达 [北京石景山营业厅]\"},{\"time\":\"2012-07-06 15:22:32\",\"context\":\"离开 [北京石景山营业厅] 派送中，递送员[温]，电话[]\"},{\"time\":\"2012-07-06 15:05:00\",\"context\":\"到达 [北京石景山营业厅]\"},{\"time\":\"2012-07-06 13:37:52\",\"context\":\"离开 [北京_同城中转站] 发往 [北京石景山营业厅]\"},{\"time\":\"2012-07-06 12:54:41\",\"context\":\"到达 [北京_同城中转站]\"},{\"time\":\"2012-07-06 11:11:03\",\"context\":\"离开 [北京运转中心驻站班组] 发往 [北京_同城中转站]\"},{\"time\":\"2012-07-06 10:43:21\",\"context\":\"到达 [北京运转中心驻站班组]\"},{\"time\":\"2012-07-05 21:18:53\",\"context\":\"离开 [福建_厦门支公司] 发往 [北京运转中心_航空]\"},{\"time\":\"2012-07-05 20:07:27\",\"context\":\"已取件，到达 [福建_厦门支公司]\"}]} ";
//			ExpressInfo exInfo = JSON.parseObject(message, ExpressInfo.class);
//			ExpressInfo exInfo = HttpClientUtil.getKuaidi100WithNoVal(oet.getTrackno(), oet.getCompanyCode());
//			ExpressInfo exInfo = HttpClientUtil.getKuaiDi100ByHtmlApi(oet.getCompanyCode(), oet.getTrackno());
			ExpressInfo exInfo = HttpClientUtil.getKuaidi100NEW(oet.getTrackno(), oet.getCompanyCode(), null, null);
			logger.info("trackNo =" + trackNo + " :" + JSON.toJSONString(exInfo) + " FetchCount:" + oet.getFetchCount());
			if (exInfo == null || StringUtil.isListNull(exInfo.getData())) {
				Integer fetchCount = oet.getFetchCount().byteValue() + 1;
				// 数据无效不会插入到表
				oet.setFetchCount(fetchCount.byteValue());
				if (fetchCount.byteValue() > 126) {
					oet.setFetchFlag(new Byte("88"));
				} else {
					oet.setFetchFlag(new Byte("0"));
				}
				orderExpressTracingService.updateTracing(oet);
				return;
			}
			String city = oet.getCityStr();
			String dist = oet.getDistrictStr();
			String province = oet.getProvinceStr();
			/*if (province == null || "".equals(province)) {
				province = systemRegionService.getSystemRegionAreaName(oet.getProvince()).getRegion_name();
			}
			if (city == null || "".equals(city.trim())) {
				city = systemRegionService.getSystemRegionAreaName(oet.getCity()).getRegion_name();
			}
			if (dist == null || "".equals(dist.trim())) {
				dist = systemRegionService.getSystemRegionAreaName(oet.getDistrict()).getRegion_name();
			}*/
			OrderExpressTracing newTracing = newOrderExpressTracing(oet, exInfo, province, city, dist, oet.getCompanyCode());
			int count = orderExpressTracingService.updateTracing(newTracing);
			logger.info("快递100 Update Count"+"*******"+count+"=====");
		}catch (Exception e) {
			logger.error("快递100查询物流数据：" + data + "异常：" + e.getMessage(), e);
		}
	}

	
	public OrderExpressTracing newOrderExpressTracing(OrderExpressTracing old,
			ExpressInfo info, String province, String city, String dist,
			String expressName) throws ParseException {
		if (info == null || info.getData() == null || info.getData().size() < 1) {
			return old;
		}
		old.setNote(JSON.toJSONString(info));
/*		String city_s = "null";
		if (city != null && city.length() > 0) {
			city_s = city.replace("市", "");
		}
		String dist_s = "null";
		if (dist != null && dist.length() > 0) {
			dist_s = dist.replaceFirst("区|县", "");
		}*/
		List<com.work.shop.oms.express.bean.ExpressContent> list = info.getData();
//		int site = 0;
		// 10，快递取件；11，运输中；11.1，运输中(上干线)；11.2，运输中(到达目的地)；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁）
		float expressStatus = 0.0f;
		try {
			expressStatus = info.getState();
			if (expressStatus == Constant.express_status_sign
					|| expressStatus == Constant.express_status_returnSign) {
				old.setExpressTimeSign(setDateStr(list.get(0).getTime()));
			}
			/*for (com.work.shop.express.utils.bean.ExpressContent content : list) {
				String time = content.getTime();
				String text = content.getContext();
				// 揽件时间
				if (site == 0) {
					if (old.getExpressTimeGet() == null) {
						expressStatus = 10.0f;
						old.setExpressTimeGet(setDateStr(time));
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("province", province);
						param.put("city", city);
						param.put("dist", dist);
						param.put("company", expressName);
					}else{
						expressStatus = 10.0f;
					}
				}
				// 上干线时间,默认第二条。
				if (site == 1) {
					if (expressStatus < 11.0f) {
						expressStatus = 11.0f;
					}
					old.setExpressTimeMain(setDateStr(time));
				}
				// 目的地时间
				if (info.getState() != 3 && info.getState() != 4 && (text.contains(city_s) || text.contains(dist_s))) {
					expressStatus = 12.0f;
					old.setExpressTimeDestination(setDateStr(time));
				}
				// 遗失
				if (text.contains("遗失") || text.contains("损失")
						|| text.contains("损坏") || text.contains("损毁")) {
					expressStatus = 15.0f;
				}
				// 拒签
				if (text.contains("拒签") || text.contains("拒收")||text.contains("退签")) {
					expressStatus = 14.0f;
				}
				// 签收
				if(text.contains("签收") || text.contains("草签") || text.contains("已妥投")){
					expressStatus = 13.0f;
					old.setExpressTimeSign(setDateStr(time));
				}
				site++;
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		old.setExpressStatus(expressStatus);
		old.setUpdTime(new Date());
//		old.setFetchFlag(new Byte("1"));
		return old;
	}
	
	public static Date setDateStr(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = date.replaceAll("/", "-");
		return sdf.parse(repalceSpace(date.getBytes()));
	}
	
	public static String repalceSpace(byte[] b) {
		byte[] n = new byte[b.length];
		for (int i = 0; i < b.length; i++) {
			if (b[i] > 0) {
				n[i] = b[i];
			} else {
				n[i] = 32;
			}
		}
		String newstr = new String(n);
		Pattern p = Pattern.compile(" {2,} ");
		Matcher m = p.matcher(newstr);

		return m.replaceAll(" ");
	}

}
