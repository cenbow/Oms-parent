package com.work.shop.oms.express.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.work.shop.oms.utils.ConfigCenter;

@Component
public class ExpressConstant {
	private static Logger logger = Logger.getLogger(ExpressConstant.class);
	
	public static final String KUAIDI100_KEY_1 = ConfigCenter.getProperty("KUAIDI100_KEY");

	public static final String KUAIDI100_KEY[] = { KUAIDI100_KEY_1 };
//	public static final String KUAIDI100_KEY[] = { "e43663f9ac75b755",
//	"47b7acf3bdba0d16" };
	// http://api.kuaidi100.com/api?id=47b7acf3bdba0d16&com=zhongtong&nu=718093536455&show=0&muti=1&order=asc
	public static final String EMS_NAME = "中国邮政EMS";

	public static final String EMS_100_NAME = "ems";

	public static final String EMS_COMPANY_ID = "27";

	public static final Integer EMS_FETCH_NUM = 12;

	public static final String YTO_URL = "http://116.228.70.199/ordws/VipOrderServlet";

	public static final String YTO_XML = "<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><clientID>metersbonwe</clientID><orders>%s</orders></BatchQueryRequest>";

	public static final String YTO_XML_ORDER = "<order><mailNo>%s</mailNo></order>";
	public static final String YTO_XML_PREFIX = "MTSBW";

	public static final Integer YTO_COMPANY_ID = 29;

	public static final Integer YTO_FETCH_NUM = 300;
	
	public static final String QF_COMPANY_ID = "44";
	
	public static final Integer QF_FETCH_NUM = 300;
	
	
	public static final String YUNDA_NAME="yunda";
	public static final String YUNDA_NAME_CN="韵达快递";
	public static final String YUNDA_COMPANY_ID = "42";
	public static final Integer YUNDA_FETCH_NUM = 30;
	
	
	public static final String ZJS_NAME="zjs";
	public static final String ZJS_NAME_CN="宅急送";
	public static final String ZJS_100_NAME="zhaijisong";
	public static final Integer ZJS_COMPANY_ID = 24;
	public static final Integer ZJS_FETCH_NUM = 30;
	
	

	public static final String BS_URL = "http://rf.bang-go.com.cn:8081/bsmsgsend/msg/query";

	public static final Integer BS_FETCH_NUM = 80;

	public static final Integer BS_COMPANY_ID = 43;

	public static final String SF_100_NAME = "shunfeng";

	public static final Integer SF_FETCH_NUM = 15;

	public static final String SF_NAME = "sf_express";

	public static final Integer SF_COMPANY_ID = 39;

	// 中通快递公司ID
	public static final Integer ZTO_COMPANY_ID = 41;
	// 中通快递公司名称
	public static final String ZTO_COMPANY_NAME = "zhongtong";
	

	// 汇通快递公司ID
	public static final Integer HUITONG_COMPANY_ID = 25;
	// 汇通快递公司名称
	public static String HUITONG_COMPANY_NAME = "huitongkuaidi";

	// 赛澳递快递公司ID
	public static final Integer SAIODI_COMPANY_ID = 40;
	// 赛澳递快递公司名称
	public static final String SAIAODI_COMPANY_NAME = "saiaodi";

	// 芝麻开门快递公司ID
	public static final Integer ZHIMAKAIMEN_COMPANY_ID = 38;
	// 芝麻开门快递公司名称
	public static final String ZHIMAKAIMEN_COMPANY_NAME = "zhimakaimen";
	
	// 申通快递公司ID
	public static final Integer SHENTONG_COMPANY_ID = 22;
	// 申通快递公司名称
	public static final String SHENTONG_COMPANY_NAME = "shentong";
	
	public static final Integer SHENTONG_FETCH_NUM = 12;

	public static final String JD_COMPANY_ID = "48";
 
	public static final String JD_COMPANY_NAME = "jdkd";
	
	public static final Integer JD_FETCH_NUM = 120;

	public static Date setDateStr(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = date.replaceAll("/", "-");
		return sdf.parse(repalceSpace(date.getBytes()));
	}

	public Date addHour(Date date, int hour) {
		long newDay = date.getTime() + hour * 60 * 60 * 1000;
		return new Date(newDay);
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
