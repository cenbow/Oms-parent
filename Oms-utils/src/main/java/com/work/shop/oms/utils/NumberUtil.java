package com.work.shop.oms.utils;

import java.math.BigDecimal;
import java.util.Random;

public class NumberUtil {
	 /**  
     * 对double数据进行取精度.  
     * @param value  double数据.  
     * @param scale  精度位数(保留的小数位数).  
     * @param roundingMode  精度取值方式.  
     * @return 精度计算后的数据.  
     */  
    public static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);   
        bd = bd.setScale(scale, roundingMode);   
        double d = bd.doubleValue();   
        bd = null;   
        return d;   
    }   

     /** 
     * double 相加 
     */ 
    public static double sum(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.add(bd2).doubleValue(); 
    } 


    /** 
     * double 相减 
     */ 
    public static double sub(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.subtract(bd2).doubleValue(); 
    } 

    /** 
     * double 乘法 
     */ 
    public static double mul(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.multiply(bd2).doubleValue(); 
    } 


    /** 
     * double 除法 
     * @param scale 四舍五入 小数点位数 
     */ 
    public static double div(double d1,double d2,int scale){ 
        //  当然在此之前，你要判断分母是否为0，   
        //  为0你可以根据实际需求做相应的处理 

        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.divide 
               (bd2,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 
    }
    
    /**
	 * 非正则判断
	 * 判断字符串是不是数值，正负均可
	 * @param str 
	 * 				需要判断的字符串
	 * @return true|false
	 */

	public static boolean isNumberic(String str) {
		boolean flag = true;
		if (str != null && str.trim().length() > 0) {
			char[] arr = str.toCharArray();
			int sign = 0;
			if (str.charAt(0) == '-') {
				sign = 1;
			}
			if (str.indexOf(".") > 0) {
				int dot = str.indexOf(".");
				for (int i = sign; i < arr.length; i++) {
					if (i != dot) {
						if (!Character.isDigit(arr[i])) {
							flag = false;
						}
					}
				}
			} else {
				for (int i = sign; i < arr.length; i++) {
					if (!Character.isDigit(arr[i])) {
						flag = false;
					}
				}
			}
		}else{
			flag = false;
		}
		return flag;
	}

	/**
	 * 随机一个数字字符串，最大10位
	 * @param len
	 * @return
	 */
	public static String randomStrNumber(int len) {
		Random r = new Random();
		
		String temp = "0000000000" + r.nextInt(999999999);
		
		return temp.substring(temp.length() - len);
	}
}
