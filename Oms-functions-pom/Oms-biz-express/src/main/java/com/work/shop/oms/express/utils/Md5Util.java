/**
 *
 * FileName：MD5Util.java
 *
 * Description：MD5校验码生成工具
 */

package com.work.shop.oms.express.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import sun.misc.BASE64Encoder;

/**
 * MD5校验码生成工具
 * 
 */
public class Md5Util {
	/**
	 * 生成md5校验码
	 * 
	 * @param srcContent
	 *            需要加密的数据
	 * @return 加密后的md5校验码。出错则返回null。
	 */
	public static String makeMd5Sum(byte[] srcContent) {
		if (srcContent == null) {
			return null;
		}

		String strDes = null;

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(srcContent);
			strDes = bytes2Hex(md5.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}
	 /**
     * MD5
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }
	private static String bytes2Hex(byte[] byteArray) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (byteArray[i] >= 0 && byteArray[i] < 16) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(byteArray[i] & 0xFF));
		}
		return strBuf.toString();
	}

	/**
	 * 新的md5签名，首尾放secret。
	 * 
	 * @param params
	 *            传给服务器的参数
	 * 
	 * @param secret
	 *            分配给您的APP_SECRET
	 */
	public static String md5Signature(TreeMap<String, String> params,
			String secret) {
		String result = null;
		StringBuffer orgin = getBeforeSign(params, new StringBuffer(secret));
		if (orgin == null)
			return result;
		orgin.append(secret);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = bytes2Hex(md.digest(orgin.toString().getBytes("utf-8")));
		} catch (Exception e) {
			throw new java.lang.RuntimeException("sign error !");
		}
		return result;
	}

	/**
	 * 添加参数的封装方法
	 * 
	 * @param params
	 * @param orgin
	 * @return
	 */
	private static StringBuffer getBeforeSign(TreeMap<String, String> params,
			StringBuffer orgin) {
		if (params == null)
			return null;
		Map<String, String> treeMap = new TreeMap<String, String>();
		treeMap.putAll(params);
		Iterator<String> iter = treeMap.keySet().iterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			orgin.append(name).append(params.get(name));
		}
		return orgin;
	}

	//顺丰专用
	public static String md5EncryptAndBase64(String str) {
		return encodeBase64(md5Encrypt(str));
	}
	//顺丰专用
	private static byte[] md5Encrypt(String encryptStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(encryptStr.getBytes("utf8"));
			return md5.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	//顺丰专用
	private static String encodeBase64(byte[] b) {
		sun.misc.BASE64Encoder base64Encode = new BASE64Encoder();
		String str = base64Encode.encode(b);
		return str;
	}
	 /**
     * base64
     * @param md5
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] md5) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(md5);
    }
    
  

    /**
     * 摘要生成
     * @param data 请求数据
     * @param sign 签名秘钥(key或者parternID)
     * @param charset 编码格式
     * @return 摘要
     * @throws Exception
     */
    public static String digest(String data,String sign,String charset) throws Exception {
    	 String t = encryptBASE64(encryptMD5((data+sign).getBytes(charset)));
    	// BASE64编码之后默认有一个回车换行符，这个符号在windows上 与 linux不同
    	// 中通后台是按照 Windows 换行符校验的，如果调用者是linux 系统要做个转换
     	if (System.getProperty("line.separator").equals("\n")) {
     	    String t2 = t.replaceAll("\\n", "\r\n");
     	    return t2;
     	}else{
     	 return t;
     	}
    }
   
}
