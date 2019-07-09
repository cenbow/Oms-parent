package com.work.shop.oms.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;


/**
 * Json 转换数据库日期格式
 * @author Administrator
 *
 */
public class DateJsonValueProcessor implements JsonValueProcessor {
	
	public static final String Default_DATE_PATTERN = "yyyy-MM-dd";
	private DateFormat dateFormat;

	public DateJsonValueProcessor(String datePattern) {
		try {
			dateFormat = new SimpleDateFormat(datePattern);

		} catch (Exception e) {
			dateFormat = new SimpleDateFormat(Default_DATE_PATTERN);

		}

	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		if (null == value) {
			return null;
		}
		String format = "";
		if (value instanceof Date) {
			format = dateFormat.format((Date) value);
		}else if(value instanceof Timestamp)
		{
			format = dateFormat.format((Timestamp) value);
		}
		
		return format;

	}
}
