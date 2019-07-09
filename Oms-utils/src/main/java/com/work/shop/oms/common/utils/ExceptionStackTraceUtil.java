/*
 * 上海坦思计算机系统有限公司
 * 
 * ---------------+-----------+---------------
 *   更新时间	  	更新者		更新内容
 *   2011/05/30	  周军		 创建
 */
package com.work.shop.oms.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 打印 异常 堆栈信息类
 * 
 * @author zhoujun
 * 
 */
public class ExceptionStackTraceUtil {

	/**
	 * Get the stack trace of the exception.
	 * 
	 * @param e
	 *            The exception instance.
	 * @return The full stack trace of the exception.
	 */
	public static String getExceptionTrace(Throwable e) {

		String exceptionTrace = null;
		StringWriter sw = null;
		PrintWriter pw = null;
		if (e != null) {
			try {
				sw = new StringWriter();
				pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				exceptionTrace = sw.toString();
				return exceptionTrace;
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				pw.close();
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return "No Exception";
	}
}