package com.work.shop.oms.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.work.shop.oms.common.bean.SystemInfo;

/**
 * 工具类 
 * 
 * 1.获取本机系统信息
 * 2.获取本机IP信息
 * 3.获取远程调用者的IP信息
 *
 * 常量NULL_IP_ADDRESS ： 空的IP地址
 * 
 * @author lemon
 *
 */
public final class CommonUtils {

	private static final String NULL_IP_ADDRESS = "0.0.0.0";

	private static volatile String localIp = null;
	
	/**
	 * 获取当前系统基础信息
	 * @param request httprequest
	 * @return 系统基础信息
	 */
	public static SystemInfo getSystemInfo(HttpServletRequest request) {
		Date date = new Date();
		if(localIp == null) {
			localIp = getLocalIP();
		}
		SystemInfo systeminfo = new SystemInfo(date, localIp, getIP(request), date);
		return systeminfo;
	}

	/**
	 * linux系统获取本机内网地址
	 */
/*	public static String getLocalIP() {
		String ip = "";
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				if (ni.getName().equals("eth0")) {
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = ips.nextElement().getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			return NULL_IP_ADDRESS;
		}
		return ip;
	}*/
	/**
	 * 复制bean public属性
	 * @param newbean 新copy对象
	 * @param Beans 原对象
	 * @return
	 */
	public static void copyProperties(Object newbean,Object Beans){
		try {
			BeanUtils.copyProperties(newbean,Beans);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 复制bean
     * @param source
     * @param target
     */
	public static void copyPropertiesNew(Object source,Object target) {
	    try {
            BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopier.copy(source, target, null);
        } catch (Exception e) {
	        e.printStackTrace();
        }
    }
	
	  public static String YikeSignRequest(String appId,String timestamp, String token){
		  StringBuilder sb = new StringBuilder("");
		  sb.append("AppId=").append(appId).append("&Timestamp=").append(timestamp).append("&Token=").append(token);
//		  DigestUtils.shaHex("AppId=EZP&Timestamp=20150701093010&Token=9cd8c0ed38a9e113");
		  
		  return DigestUtils.shaHex(sb.toString());//DigestUtils.md5Hex(sb.toString())
	  }
	
//	
//	  public static String signRequest(Map<String, String> params, String appSecret, String signMethod)
//			    throws Exception
//			  {
//			    StringBuilder sb = new StringBuilder(appSecret);
//
//			    String[] keys = (String[])params.keySet().toArray(new String[0]);
//			    Arrays.sort(keys);
//
//			    for (String key : keys) {
//			      String value = (String)params.get(key);
//			      if (areNotBlank(new String[] { key, value })) {
//			        sb.append(key).append(value);
//			      }
//			    }
//			    sb.append(appSecret);
//			//    LoggerUtil.info(sb.toString());
//			    if ("md5".equals(signMethod)) {
//			      return DigestUtils.md5Hex(sb.toString());
//			
//			    
//			    }
//			    throw new Exception("md5加蜜错：" + signMethod);
//	  }
	  
		public static boolean areNotBlank(String[] args) {
			boolean result = true;
			if ((args == null) || (args.length == 0)) {
				result = false;
			} else {
				String[] arrayOfString = args;
				int j = args.length;
				for (int i = 0; i < j; i++) {
					String str = arrayOfString[i];
					result &= !isBlank(str);
				}
			}
			return result;
		}
	/**
	 * 获取IP地址
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return IP地址
	 */
	public static String getIP(HttpServletRequest request) {
		// x-forwarded-for:10.5.15.104
		if(request == null) return NULL_IP_ADDRESS;
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress != null) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < ipAddress.length(); i++) {
				char ch = ipAddress.charAt(i);
				if (ch != ' ') buf.append(ch);
			}
			ipAddress = buf.toString();
		}
		if (ipAddress != null) {
			if (ipAddress.length() > 0 && !ipAddress.startsWith("10.25.")) {
				int tmpIndex = ipAddress.indexOf(",");
				if (tmpIndex > 0) {
					ipAddress = ipAddress.substring(0, tmpIndex);
				}
				return ipAddress;
			}
		}
		ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (ipAddress != null) {
			int index = ipAddress.indexOf(',');
			if (index > 0) {
				ipAddress = ipAddress.substring(0, index);
			}
			return ipAddress;
		}
		ipAddress = request.getHeader("CLIENT_IP");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		int index = ipAddress.indexOf(',');
		if (index > 0) {
			ipAddress = ipAddress.substring(0, index);
		}
		return StringUtils.isBlank(ipAddress) ? NULL_IP_ADDRESS : ipAddress;
	}

    /** 
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为 但是字符串为空格不为空	 
     *  
     * @param obj 
     * @return 
     */  
    public static boolean isNullOrEmpty(Object obj) {  
        if (obj == null)  
            return true;  
  
        if (obj instanceof String)  
            return ((CharSequence) obj).length() == 0;  
        
        if (obj instanceof CharSequence)  
            return ((String) obj).length() == 0;  
        
        if (obj instanceof Collection)  
            return ((Collection) obj).isEmpty();  
  
        if (obj instanceof Map)  
            return ((Map) obj).isEmpty();  
  
        if (obj instanceof Object[]) {  
            Object[] object = (Object[]) obj;  
            if (object.length == 0) {  
                return true;  
            }  
            boolean empty = true;  
            for (int i = 0; i < object.length; i++) {  
                if (!isNullOrEmpty(object[i])) {  
                    empty = false;  
                    break;  
                }  
            }  
            return empty;  
        }  
        return false;  
    }
    public static boolean isNotNullOrEmpty(Object obj) {  
    	return ! isNullOrEmpty(obj);
    }
    /**
     * 判断字符串是否为空或者空格
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0))
			return true;
		for (int i = 0; i < strLen; ++i) {
			if (!(Character.isWhitespace(str.charAt(i)))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将判断是否被当前用户绑定的状态转变成信息
	 * @param judgeFlag
	 * @return
	 */
	public static String getJudgeLockStatusMeg(int judgeFlag) {
		switch (judgeFlag) {
		case -2:
			return "没有此用户名的信息";
		case -1:
			return "当期数据表中没有找到此订单";
		case 0:
			return "此订单未绑定";
		case 2:
			return "此订单被此操作者锁定";
		case 3:
			return "判断此订单是否被操作者锁定时异常";
		default:
			return "没有被此用户绑定,返回此订单被绑定用户的ID为:" + judgeFlag;
		}
	}

	 /**
     * 将内容写入到文件中
     * @param content
     * @param file
     * @return
     * @throws Exception
     */
    public static boolean writeTxtFile(String content, File file)
			throws Exception {
		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			if(file.exists()){
				file.delete();
			}
			o = new FileOutputStream(file);
			o.write(content.getBytes("UTF-8"));
			o.close();
			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (mm != null) {
				mm.close();
			}
		}
		return flag;
	}

	/**
	 * 验证字符串中是否包含中文
	 * @param input
	 * @return
	 */
	public static boolean checkChinaStr(String input) {
		if(StringUtils.isBlank(input)) return true;
		
		input = new String(input.getBytes());// 用GBK编码
		String pattern = "[\u4e00-\u9fa5]+";
		Pattern p = Pattern.compile(pattern);
		Matcher result = p.matcher(input);
		return result.find(); // 是否含有中文字符
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

	/**
	 * OS结算单据编码-日期+随机编码
	 */
	private static int systemCode = 0;
	public synchronized static String getSettleBillCode() {
		String str = "";
		systemCode += 1;
		if (systemCode > 0 && systemCode < 10) {
			str = "000" + systemCode;
		}
		if (systemCode >= 10 && systemCode < 100) {
			str = "00" + systemCode;
		}
		if (systemCode >= 100 && systemCode < 1000) {
			str = "0" + systemCode;
		}
		if (systemCode >= 1000 && systemCode < 10000) {
			str = "" + systemCode;
		}
		if (systemCode >= 10000) {
			systemCode = 0;
			str = "0001";
		}
		if (StringUtils.isNotBlank(str)) {
			str = TimeUtil.format3Date(new Date()) + str;
		}
		return str;
	}
	/**
	 * double 四舍五入 默认精确到两位小数
	 * @param val
	 * @return
	 */
	public static Double roundDoubleDefault(double val) {
		return roundDouble(val, 2);
	}
	/**
	 * 发货Response数据长度格式化：2000
	 * @param response
	 * @return
	 */
	public static String formatResponse(String response){
		int limit = 1950;
		if(StringUtils.isBlank(response)){
			return StringUtils.EMPTY;
		}
		if(StringUtils.length(response) > limit){
			return StringUtils.left(response, limit);
		}
		return response;
	}
	
	/**
	 * 判断当前系统是否windows 
	 * 
	 * @return
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	/**
	 * 取当前系统站点本地地址 linux下 和 window下可用 add by RWW
	 * 
	 * @return
	 */
	public static String getLocalIP() {
		String sIP = "";
		InetAddress ip = null;
		try {
			// 如果是Windows操作系统
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			} else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
							bFindIP = true;
							break;
						}
						if (ip.isSiteLocalAddress()
								&& !ip.isLoopbackAddress()
								&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
							sIP = ip.getHostAddress();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}
	
	public static BigDecimal newDecimal() {
		return new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal newIntegerDecimal(Integer value) {
		return new BigDecimal(value).setScale(3, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal newStringDecimal(String value) {
		return new BigDecimal(value).setScale(3, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal newDoubleDecimal(Double value) {
		return new BigDecimal(value).setScale(3, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal intoTwoDecimal(String value) {
		return new BigDecimal(value).divide( new BigDecimal(100));
	}
	
	public static BigDecimal intoTwoDecimal(Long value) {
		return new BigDecimal(value).divide( new BigDecimal(100));
	}
	
	public static double BigDecimalTODouble(BigDecimal value) {
		if(null==value){
			return 0D;
		}else{
			return value.doubleValue();
		}
	}

	/**
	 * 取当前系统站点本地地址 linux下 和 window下可用 add by RWW
	 * 
	 * @return
	 */
	public static String getNetIP() {
		String netIP = "";
		InetAddress ip = null;
		try {
			// 如果是Windows操作系统
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			} else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
							bFindIP = true;
							break;
						}
						// 外网IP
						if (!ip.isSiteLocalAddress()
								&& !ip.isLoopbackAddress()
								&& ip.getHostAddress().indexOf(":") == -1) {
							netIP = ip.getHostAddress();
							bFindIP = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (null != ip) {
			netIP = ip.getHostAddress();
		}
		return netIP;
	}
}
