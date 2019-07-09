package com.work.shop.oms.common.utils;

import java.io.Serializable;

/**
 * 关于Object操作方法类
 * @author
 *
 */
public class ObjectUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1481277009574624751L;

	/**
	 * 转换成String
	 * @param obj
	 * @return
	 */
	public String ots(Object obj){
		return (obj != null ? obj.toString().trim() : "");
	}
	/**
	 * 转化成int
	 * @param obj
	 * @return
	 */
	public int oti(Object obj){
		int rs = 0;
		try {
			rs = obj != null && !"".equalsIgnoreCase(obj.toString()) ? Integer.parseInt(obj.toString()) : 0;
		} catch (Exception e) {
			try {
				Boolean rsBool = new Boolean(obj.toString());
				rs = rsBool ? 1 : 0;
			} catch (Exception e1) {
				rs = 0;
			}
		}
		return rs;
	}
	/**
	 * 转化成long
	 * @param obj
	 * @return
	 */
	public long otl(Object obj){
		return (obj != null && !"".equalsIgnoreCase(obj.toString()) ? Long.parseLong(obj.toString()) : 0l);
	}
	/**
	 * 转化成double
	 * @param obj
	 * @return
	 */
	public double otd(Object obj){
		return (obj != null && !"".equalsIgnoreCase(obj.toString()) ? Double.parseDouble(obj.toString()) : 0);
	}
	/**
	 * 转化成float
	 * @param obj
	 * @return
	 */
	public float otf(Object obj){
		return (obj != null && !"".equalsIgnoreCase(obj.toString()) ? Float.parseFloat(obj.toString()) : 0);
	}
	
	public String formatDate(String d){
		if(d.indexOf(".") > -1){
			return d.substring(0, d.indexOf("."));
		}
		return d;
	}
	
	/**
	 * float转String
	 * 
	 * @param f
	 * @return
	 */
	public String fts(float f) {
		return String.valueOf(f);
	}
}
