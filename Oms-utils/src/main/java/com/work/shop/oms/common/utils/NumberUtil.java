package com.work.shop.oms.common.utils;

import java.math.BigDecimal;

/**
 * 金额处理
 * @author QuYachu
 *
 */
public class NumberUtil {

	public static double getDoubleByDecimal(BigDecimal value) {
  		return getDoubleByDecimal(value, 2);
  	}
  	
  	public static double getDoubleByDecimal(BigDecimal value, int scale) {
  		return getDecimalValue(value, scale).doubleValue();
  	}
  	
  	public static BigDecimal getDecimalValue(BigDecimal value, int scale) {
  		return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
  	}
  	
  	public static double getDoubleByValue(Double value) {
  		return getDoubleByValue(value, 2);
  	}
  	
  	public static double getDoubleByValue(Double value, int scale) {
  		BigDecimal bigValue = new BigDecimal(value.toString());
  		return getDoubleByDecimal(bigValue, scale);
  	}
  	
  	public static BigDecimal getBigDecimalByDouble(Double value, int scale) {
  		BigDecimal bigValue = new BigDecimal(value.toString());
  		
  		return getDecimalValue(bigValue, scale);
  	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @param v1
	 * 被减数
	 * @param v2
	 * 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
}
