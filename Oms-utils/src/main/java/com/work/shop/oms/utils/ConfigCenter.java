package com.work.shop.oms.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


/**
 * 读取properties配置文件
 * 
 * @author tony
 * 
 */
public class ConfigCenter {

	//private static final Logger logger = Logger.getLogger(ConfigCenter.class);
	
	public static final int START_WITH = 1;

	public static final int END_WITH = 2;

	/**
	 * 配置文件名称
	 */
	private static final String P_FILE = "resource.properties";
	/**
	 * 属性类对象
	 */
	private Properties pProps = null;

	/**
	 * 单例对象
	 */
	private static final ConfigCenter configInstance = new ConfigCenter();

	/**
	 * 内部实例化单例方法
	 */
	private ConfigCenter() {
		pProps = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream(P_FILE);
			pProps.load(inputStream);
		} catch (Exception e) {
			//logger.error(e.getMessage(), e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					//logger.error("关闭文件流失败.", e);
				}
			}
		}
	}

	/**
	 * 获取指定名称的属性值
	 * 
	 * @param name
	 *            属性key
	 * @param defaultVal
	 *            该属性默认值
	 * @return
	 */
	public static Object getPropertyItem(String name, Object defaultVal) {

		Object val = configInstance.pProps.get(name);

		return val != null && val.toString().length() > 0 ? val : defaultVal;

	}

	/**
	 * 获取指定名称的属性值
	 * 
	 * @param name
	 *            属性key
	 * @return
	 */
	public static String getProperty(String name) {

		Object val = configInstance.pProps.get(name);

		if (val != null) {
			return val.toString().trim();
		}
		return "";
	}

	/**
	 * 获取指定名称的属性值
	 * 
	 * @param name
	 *            属性key
	 * @return
	 */
	public static String getProperty(String name,String defaultValue) {

		Object val = configInstance.pProps.get(name);

		if (val != null && StringUtils.isEmpty(val.toString())) {
			return val.toString().trim();
		}
		return defaultValue;
	}
	
	/**
	 * 获取属性文件中属性键值对的数量
	 * 
	 * @return
	 */
	public static int getPropertySize() {
		Set<String> keys = getKeysAsSet();
		try {
			return keys != null ? keys.size() : 0;
		} catch (Exception e) {
			//logger.error(e.getMessage(), e);
			return 0;
		}
	}

	/**
	 * 获取属性文件中所有属性的Key，以Set<String>形式返回
	 * 
	 * @return
	 */
	public static Set<String> getKeysAsSet() {
		try {
			Set<String> keys = null;
			keys = configInstance.pProps.stringPropertyNames();
			return keys;
		} catch (Exception e) {
			//logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 前缀取值列表
	 * 
	 * @param startStr
	 *            前缀
	 * @return 值列表
	 */
	public static List<String> getStartWith(String startStr) {
		return getList(startStr, START_WITH);
	}

	/**
	 * 后缀取值列表
	 * 
	 * @param endStr
	 *            后缀
	 * @return 值列表
	 */
	public static List<String> getEndWith(String endStr) {
		return getList(endStr, END_WITH);
	}

	private static List<String> getList(String str, int startOrend) {
		Set<Object> keys = configInstance.pProps.keySet();
		List<String> values = new LinkedList<String>();
		if (keys == null)
			return values;
		for (Iterator<Object> iter = keys.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			if (key == null)
				continue;
			if (startOrend == START_WITH) {
				if (key.startsWith(str)) {
					values.add(getProperty(key));
				}
			} else if (startOrend == END_WITH) {
				if (key.endsWith(str)) {
					values.add(getProperty(key));
				}
			}
		}
		return values;
	}

}
