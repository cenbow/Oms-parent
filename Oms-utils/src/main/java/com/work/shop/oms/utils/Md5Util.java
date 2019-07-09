package com.work.shop.oms.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	
	/**
	 * 用来将字节转换成 16 进制表示的字符
	 */
	private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};

	/**
	 * MD5加密
	 * @param message
	 * @return
	 */
	public static String encrypt(String message) {
		String s = null;
	
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
		    md.update(message.getBytes("UTF-8"));
		    
		    // MD5 的计算结果是一个 128 位的长整数，
		    byte tmp[] = md.digest();
		    // 用字节表示就是 16 个字节
		    // 每个字节用 16 进制表示的话，使用两个字符，
		    // 所以表示成 16 进制需要 32 个字符
		    char str[] = new char[16 * 2];
		    
		    // 表示转换结果中对应的字符位置
		    int k = 0;
		    
		    // 从第一个字节开始，对 MD5 的每一个字节
		    for (int i = 0; i < 16; i++) {
		    	// 转换成 16 进制字符的转换
		    	// 取第 i 个字节
		    	byte byte0 = tmp[i]; 
		    	
		    	// 取字节中高 4 位的数字转换,
		    	// >>> 为逻辑右移，将符号位一起右移
		    	str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];   
		        
		    	// 取字节中低 4 位的数字转换
		    	str[k++] = HEX_DIGITS[byte0 & 0xf];         
		    }
		 // 换后的结果转换为字符串
		    s = new String(str); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return s;
	}
	
	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);

		return md5.digest();

	}
	
	/** 
	 * BASE64解密 
	 *  
	 * @param key 
	 * @return 
	 * @throws Exception 
	 */  
	public static byte[] decryptBASE64(String key) throws Exception {  
	    return (new BASE64Decoder()).decodeBuffer(key);  
	}  
	  
	/** 
	 * BASE64加密 
	 *  
	 * @param key 
	 * @return 
	 * @throws Exception 
	 */  
	public static String encryptBASE64(byte[] key) throws Exception {  
	    String base64 = (new BASE64Encoder()).encodeBuffer(key);
	    base64 = base64.replaceAll("[\\s*\t\n\r]", "");
	    return base64;
	}  
	


/**
     * 获取MD5加密后的字符串
     * @param str 明文
     * @return 加密后的字符串
     * @throws Exception 
     */
    public static String getMD5(String str) throws Exception {
        /** 创建MD5加密对象 */
        MessageDigest md5 = MessageDigest.getInstance("MD5"); 
        /** 进行加密 */
        md5.update(str.getBytes());
        /** 获取加密后的字节数组 */
        byte[] md5Bytes = md5.digest();
        String res = "";
        for (int i = 0; i < md5Bytes.length; i++){
            int temp = md5Bytes[i] & 0xFF;
            if (temp <= 0XF) { 
            	// 转化成十六进制不够两位，前面加零
                res += "0";
            }
            res += Integer.toHexString(temp);
        }
        return res;
    }

    public static String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("UTF-8"));
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return re_md5;
    }


    public static void main(String[] args) {
		
		String encrypt = "key=hla_mt&requestTime=20190424151756&secret=31e9647242dc869787bcea691379501dhlaqweasd&version=1.0.0&serviceType=order.detail.yixiaoshida.add&data={\"add_time\":1551777247,\"address\":\"上海市青浦区双浜路188号(58号)\",\"buy_msg\":\"\",\"city_name\":\"上海市\",\"consignee\":\"刘志鸿\",\"district_name\":\"青浦区\",\"items\":[{\"goods_number\":1,\"goods_price\":\"0.010000\",\"is_gift\":0,\"market_price\":\"0.010000\",\"sku\":\"20011300002_common\"},{\"goods_number\":1,\"goods_price\":\"0.010000\",\"is_gift\":0,\"market_price\":\"0.010000\",\"sku\":\"10006600009_common\"}],\"mobile\":\"15578693258\",\"order_amount\":\"0.02\",\"order_sn\":\"1903051714084487\",\"order_status\":1,\"other_discount_fee\":\"0.00\",\"pay_code\":\"weixin\",\"pay_sn\":\"4200000254201903057975683199\",\"pay_status\":2,\"pay_time\":1551777260,\"pos_code\":\"W0000001\",\"province_name\":\"上海\",\"shipping_code\":\"QSKD\",\"shipping_fee\":\"0.00\",\"user_name\":\"wx680092\",\"vip_no\":0}";
		try {
			encrypt = encrypt(encrypt);
            System.out.println(encrypt);
            encrypt = encryption(encrypt);
            System.out.println(encrypt);
            encrypt = getMD5(encrypt);
            System.out.println(encrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(encrypt);
	}
}
