package com.work.shop.oms.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
* 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精确的浮点数运算，包括加减乘除和四舍五入。
*/
public class MathOperation {

	/**
	 * 默认除法运算精度
	 */
	private static final int DEF_DIV_SCALE = 10;
	
	/**
	 * 默认乘法运算精度
	 */
	private static final int DEF_MUL_SCALE = 10;

	/**
	 * 默认构造
	 */
	private MathOperation() {
	}

    /**
     * 提供精确的加法运算。
     *
     * @param v1
     * 被加数
     * @param v2
     * 加数
     * @return 两个参数的和
     */
    public static BigDecimal addByBigDecimal(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

	/**
	* 提供精确的加法运算。
	* 
	* @param v1
	* 被加数
	* @param v2
	* 加数
	* @return 两个参数的和
	*/
	public static double add(double v1, double v2) {
		return addByBigDecimal(v1, v2).doubleValue();
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
    public static BigDecimal subByBigDecimal(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
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
		return subByBigDecimal(v1, v2).doubleValue();
	}

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     * 被乘数
     * @param v2
     * 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mulByBigDecimal(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

	/**
	* 提供精确的乘法运算。
	* 
	* @param v1
	* 被乘数
	* @param v2
	* 乘数
	* @return 两个参数的积
	*/
	public static double mul(double v1, double v2) {
		return mulByBigDecimal(v1, v2).doubleValue();
	}

	/**
	* 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	* 
	* @param v1
	* 被除数
	* @param v2
	* 除数
	* @return 两个参数的商
	*/
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	* 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	* 
	* @param v1
	* 被除数
	* @param v2
	* 除数
	* @param scale
	* 表示表示需要精确到小数点以后几位。
	* @return 两个参数的商
	*/
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("请指定小数点精度，不能小于0");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 保留指定小数位的除法
	 * @param b1
	 * @param b2
	 * @param scale
	 * @return
	 */
	public static BigDecimal div(BigDecimal b1, BigDecimal b2, int scale) {
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	* 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	* 
	* @param v1
	* 被除数
	* @param v2
	* 除数
	* @return 两个参数的商
	*/
	public static int divToInt(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2).intValue();
	}

	/**
	* 提供精确的小数位四舍五入处理。
	* 
	* @param v
	* 需要四舍五入的数字
	* @param scale
	* 小数点后保留几位
	* @return 四舍五入后的结果
	*/
	public static double round(double v, int scale) {
		if (scale < 0) {
		throw new IllegalArgumentException("请指定小数点精度，不能小于0");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	* 提供精确的类型转换(Float)
	* 
	* @param v
	* 需要被转换的数字
	* @return 返回转换结果
	*/
	public static float convertsToFloat(double v) {
		BigDecimal b = new BigDecimal(v);
		return b.floatValue();
	}

	/**
	* 提供精确的类型转换(Int)不进行四舍五入
	* 
	* @param v
	* 需要被转换的数字
	* @return 返回转换结果
	*/
	public static int convertsToInt(double v) {
		BigDecimal b = new BigDecimal(v);
		return b.intValue();
	}

	/**
	* 提供精确的类型转换(Long)
	* 
	* @param v
	* 需要被转换的数字
	* @return 返回转换结果
	*/
	public static long convertsToLong(double v) {
		BigDecimal b = new BigDecimal(v);
		return b.longValue();
	}

	/**
	* 返回两个数中大的一个值
	* 
	* @param v1
	* 需要被对比的第一个数
	* @param v2
	* 需要被对比的第二个数
	* @return 返回两个数中大的一个值
	*/
	public static double returnMax(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.max(b2).doubleValue();
	}

	/**
	* 返回两个数中小的一个值
	* 
	* @param v1
	* 需要被对比的第一个数
	* @param v2
	* 需要被对比的第二个数
	* @return 返回两个数中小的一个值
	*/
	public static double returnMin(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.min(b2).doubleValue();
	}

	/**
	* 精确对比两个数字
	* 
	* @param v1
	* 需要被对比的第一个数
	* @param v2
	* 需要被对比的第二个数
	* @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
	*/
	public static int compareTo(double v1, double v2) {
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.compareTo(b2);
	}

	/**
	* 返回相反数 正数变负数，负数变正数
	* 
	* @param v
	* 需要被取反的数
	* @return
	* @author Mask
	* @version May 6, 2011 9:42:50 AM
	*/
	public static double returnNegate(double v) {
		BigDecimal b = new BigDecimal(v);
		return b.negate().doubleValue();
	}

	public static BigDecimal setScale(BigDecimal v, int scale) {
		return v.setScale(scale, RoundingMode.HALF_UP);
	}
	
	/**
	 * 乘法，保留 小数点后 10 位小数，四舍五入
	 * @Title: mul 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param v1
	 * @param v2
	 * @return  参数说明 
	 * @return BigDecimal    返回类型
	 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
		
		return mul(v1, v2, DEF_MUL_SCALE);
	}
	
	/**
	 * Bigdecimal  乘法，保留 小数点后 scale 位小数，四舍五入
	 * @Title: mul 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return  
	 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("请指定小数点精度，不能小于0");
		}
		
		return v1.multiply(v2).setScale(scale, RoundingMode.HALF_UP);
	}
	
	/**
	 * 累减，第一个数为被减数，减数依次按顺序存放.
	 * @param vList	list集合元素大于2个
	 * @return  
	 */
	public static Double totalSub(List<Double> vList) {
		if (vList == null || vList.size() < 3) {
			return null;
		}
		
		double value = sub(vList.get(0), vList.get(1));
		for (int i=2; i<vList.size(); i++) {
			value = sub(value, vList.get(i));
		}
		
		return value;
	}
	
	/*public static void main(String[] args) {
		List<Double> vList = new ArrayList<Double>();
		vList.add(32.2);
		vList.add(5.2);
		vList.add(2.22);
		vList.add(4.21);
		vList.add(3.25);
		System.err.println(totalSub(vList));
	}*/
	
	public static void main(String[] args) {
		BigDecimal big1 = new BigDecimal(0.075);
		BigDecimal big2 = new BigDecimal(100);

		BigDecimal rate = mul(big1, big2);
		System.out.println(rate);
		System.out.println(mul(big1, big2, 2));

		System.out.println(div(rate.doubleValue(), 100, 3));
	}
}
