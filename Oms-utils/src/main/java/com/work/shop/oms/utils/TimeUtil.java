package com.work.shop.oms.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public class TimeUtil {
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String TIMEFORMAT4POS = "yyyy/MM/dd HH:mm:ss";

	public TimeUtil() {
	}

	public static Date getBeginOfDay(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(11, 0);
			cal.set(12, 0);
			cal.set(13, 0);
			cal.set(14, 0);
			return cal.getTime();
		}
	}

	public static Date getEndOfDay(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(11, 23);
			cal.set(12, 59);
			cal.set(13, 59);
			cal.set(14, 999);
			return cal.getTime();
		}
	}

	public static Date parseDayDate(String day) {
		if (day == null)
			return null;
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = df.parse(day);
		} catch (ParseException e) {
		}
		return date;
	}

	public static Date parseDayDate(String day, String srcFormat) {
		if (day == null)
			return null;
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat(srcFormat);
		try {
			date = df.parse(day);
		} catch (ParseException e) {
		}
		return date;
	}

	/**
	 * parseDayDate("2011-06-08 14:02:43", "yyyy-MM-dd HH:mm:ss",
	 * "yyyyMMdd")-->20110608
	 * 
	 * @param date
	 * @param srcFormat
	 * @param targetFormat
	 * @return
	 */
	public static String parseDayDate(String date, String srcFormat,
			String targetFormat) {
		String target;
		Date srcDate = parseDayDate(date, srcFormat);

		target = format(srcDate, targetFormat);

		return target;
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(date);
		}
	}

	public static String format3Date(Date date) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			return df.format(date);
		}
	}

	public static String format2Date(Date date) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.format(date);
		}
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat(format);
			return df.format(date);
		}
	}

	public static boolean isFinish(Date date) {
		boolean isFinish = false;
		Calendar cal = Calendar.getInstance();
		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);
		cal.set(14, 0);
		long currentDay = cal.getTimeInMillis();
		if (date != null && date.getTime() < currentDay)
			isFinish = true;
		return isFinish;
	}

	public static Date parseString2Date(String dateStr) {
		if (dateStr == null)
			return null;
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getBeginOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(5, 1);
		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);
		cal.set(14, 0);
		return cal.getTime();
	}

	public static Date getEndOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(2, cal.get(2) + 1);
		cal.set(5, 1);
		cal.set(5, cal.get(5) - 1);
		cal.set(10, 23);
		cal.set(12, 59);
		cal.set(13, 59);
		cal.set(14, 999);
		return cal.getTime();
	}

	public static String getDate() {
		return getDate(YYYY_MM_DD_HH_MM_SS);
	}

	public static String getDate(String format) {
		Date date = new Date();
		if (StringUtil.isNull(format)) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat(format);
			return df.format(date);
		}
	}
	
	public static Timestamp getTimestamp(){
		return new Timestamp((new Date()).getTime());
	}
	
	/**
	 * 将时间戳转换成时间 格式 为[yyyy-MM-dd HH:mm:ss]
	 * @param time 时间戳
	 * @param tFormat  时间格式 例  "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String parseDayDate(long time, String tFormat){
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat(tFormat);
		String date = format.format(new Date(time * 1000L));
		return date;
	}

	/**
	 * 将日期时间转换成秒数
	 * @param date
	 * @return long
	 */
	public static long parseDateToNumeric(Date date){
		long timeNum = date.getTime();
		return timeNum/1000;
	}
	
	/**
	 * 获取大于或者小于指定日期的日期
	 * @param date 指定日期
	 * @param day 1表示明天|-1表示昨天
	 * @return 
	 */
	public static Date getBeforeDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		cal.add(Calendar.DATE, day);
		
		return cal.getTime();
	}
	
	/**
	 * 获取大于或者小于当前日期的日期
	 * @param day
	 * @return
	 */
	public static Date getBeforeDay(int day) {
		return getBeforeDay(new Date(), day);
	}

    /**
     * 获取时间戳， 10位
     * @param date
     * @return
     */
	public static String getTimestampByTen(Date date) {
        long timestamp = date.getTime() / 1000;
        return String.valueOf(timestamp);
    }

}
