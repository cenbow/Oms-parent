package com.work.shop.oms.utils;

 

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

public class FieldUtil {
	 
    /**
     * map类型转换成list类型
     * @param <E> key
     * @param <T> entity
     * @param map map对象
     * @return
     */
    public static <E extends Serializable, T> List<T> mapToList(Map<E, T> map) {
    	List<T> list = new ArrayList<T>(10);
    	Set<E> sets = map.keySet();
    	Iterator<E> it = sets.iterator();
    	try {
			while(it.hasNext()){
				list.add(map.get(it.next()));
			}
		} catch (Exception e) {
			return new ArrayList<T>(0);
		}
		return list;
    }
	
	public static String getTableName(String tb, int isHistory){
		try {
			if(isHistory < 1){
				return tb + " ";
			}else{
				return tb + "_history ";
			}
		} catch (Exception e) {
			return tb + " ";
		}
	}
	
	/**
	 * Object 转换为 String
	 * @param obj
	 * @return
	 */
	public static String obj2Str(Object obj){

		return obj == null ? StringUtils.EMPTY : StringUtils.trimToEmpty(obj.toString());
	}
    
	
	/**
	 * Object 转换为 Integer
	 * @param obj
	 * @return
	 */
	public static Integer obj2Int(Object obj){

		return obj == null||StringUtils.isBlank(obj.toString()) ? Integer.valueOf(0) : Integer.valueOf(obj.toString());
	}
	
	
	/**
	 * Object 转换为 Double
	 * @param obj
	 * @return
	 */
	public static Double obj2Dbe(Object obj){
		
		return obj == null||StringUtils.isBlank(obj.toString()) ? Double.valueOf(0.0d) : Double.valueOf(obj.toString());
	}
	
	/**
	 * Object 转换为 Date
	 * @param obj
	 * @return
	 * @throws ParseException 
	 */
	public static Date obj2Date(Object obj) throws ParseException{

		return obj == null||StringUtils.isBlank(obj.toString()) ? null : DateUtils.parseDate(StringUtils.trim(obj.toString()),new String[]{ "yyyy-MM-dd HH:mm:ss"});
	}
	
	/**
	 * 将Object数组转换为字符串数据   {a,b,c,d} = > 'a','b','c','d'
	 * @param strList
	 * @return
	 */
	public static String list2StrForSql(List<Object> objList){
		StringBuilder sbStr = new StringBuilder();
		if(CollectionUtils.isNotEmpty(objList)){
			for (Object obj : objList) {
				sbStr.append("'").append(obj2Str(obj)).append("',");
			}
			if(sbStr.length() > 0){
				sbStr.deleteCharAt(sbStr.length() - 1);
			}
		}
		
		return sbStr.toString();
	}
	
	/**
	 * 将指定字符串转换为字符串List
	 * @param strList
	 * @return
	 */
	public static List<String> str2List(String str,String flag){
		String[] list = str.split(flag);
		return Arrays.asList(list);
	}
	/**
	 * 将double数据按照指定精度四舍五入
	 * @param val
	 * @param precision
	 * @return
	 */
	public static Double roundDouble(double val, int precision) {
		Double ret = null;
		try {
			double factor = Math.pow(10, precision);
			ret = Math.floor(val * factor + 0.5) / factor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	 
}
