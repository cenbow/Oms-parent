package com.work.shop.oms.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.work.shop.oms.api.param.bean.Paging;


/**
 * 分页辅助类.
 * 
 * @author
 */
public class PageHelper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6197243579425960338L;

	private int start;
	
	private int limit;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 返回Pagination对象
	 * 
	 * @param start
	 *            开始行数
	 * @param limit
	 *            每页的显示多少数据
	 * @return
	 */
	public static Pagination getPagination(int start, int limit) {
		Pagination pagination = null;
		try {
			int _curpage = start / limit + 1;
			pagination = new Pagination(_curpage, limit);
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
		return pagination;
	}

	/**
	 * 返回Paging对象
	 * 
	 * @param pagination
	 * @return
	 */
	public static Paging getPaging(final Pagination pagination) {
		return new Paging(pagination.getTotalSize(), pagination.getData());
	}

	/**
	 * 把paging转换成json字符串
	 * 
	 * @param paging
	 * @return
	 */
	public static String toJsonString(Paging paging) {
		JSONObject object = toJson(paging);
		return object.toString();
	}

	/**
	 * 把paging转换成JSONObject
	 * 
	 * @param paging
	 * @return
	 */
	public static JSONObject toJson(Paging paging) {
		JsonConfig config = new JsonConfig();

		// 将数据库的时间格式转换为json指定的日期格式显示
		config.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		return JSONObject.fromObject(paging, config);
	}

	/**
	 * 把数据库的列影射为对象.
	 * 
	 * @param list
	 *            List<Map<?, ?>>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertListMapToListObject(List<?> list) {
		List<T> entitys = new ArrayList<T>();
		try {
			if (list != null) {
				T row = null;
				Map<?, ?> map = null;
				Iterator<?> iterator = list.iterator();
				Iterator<?> keyIt;
				while (iterator.hasNext()) {
					map = (Map<?, ?>) iterator.next();
					keyIt = map.keySet().iterator();
					row = (T) row.getClass().newInstance();
					while (keyIt.hasNext()) {
						String key = (String) keyIt.next();
						mappingColValues(key, map.get(key), row);
					}

					entitys.add(row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entitys;
	}

	/**
	 * 反射字段. 效率会比不反射慢一点.
	 * 
	 * @param fieldName
	 * @param infomation
	 */
	private static void mappingColValues(String fieldName, Object fieldValue,
			Object object) {
		if (fieldValue == null)
			return;
		fieldName = fieldName.toLowerCase();
		try {
			Class<?> c = object.getClass();
			Field field = c.getDeclaredField(fieldName);
			field.setAccessible(true);

			if (field.getType() == String.class) {
				field.set(object, String.valueOf(fieldValue));
			} else if (field.getType() == int.class) {
				field.setInt(object, Integer.parseInt(String
						.valueOf(fieldValue)));
			} else if (field.getType() == long.class) {
				field.set(object, Long.parseLong(String.valueOf(fieldValue)));
			} else if (field.getType() == double.class) {
				field.set(object, Double
						.parseDouble(String.valueOf(fieldValue)));
			} else {
				field.set(object, String.valueOf(fieldValue));
			}
		} catch (SecurityException e) {
			// e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		}
	}

	public void oldPageAdapter() {

	}

	@Override
	public String toString() {
		return "PageHelper [start=" + start + ", limit=" + limit + "]";
	}
}

