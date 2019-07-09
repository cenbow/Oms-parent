package com.work.shop.oms.express.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
 
public class HttpRequestProxy {
 
    // 连接超时
    private static int connectTimeOut = 5000;
 
    // 读取数据超时
    private static int readTimeOut = 10000;
 
    // 请求编码
    private static String requestEncoding = "UTF-8";
 
    /**
     * <pre>
     * 发送带参数的GET的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @param parameters
     *            参数映射表
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, Map parameters) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), HttpRequestProxy.requestEncoding));
                params.append("&");
            }
 
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
 
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            // System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(getConnectTimeOut()); //（单位：毫秒）jdk 1.5换成这个,连接超时
            url_con.setReadTimeout(getReadTimeOut());       //（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
 
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, getRequestEncoding()));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            System.err.println("网络故障");
            e.printStackTrace();
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
 
        return responseContent;
    }
 
    /**
     * <pre>
     * 发送不带参数的GET的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
 
            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            // System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(getConnectTimeOut());     //（单位：毫秒）jdk 1.5换成这个,连接超时
            url_con.setReadTimeout(getReadTimeOut());           //（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, getRequestEncoding()));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            System.err.println("网络故障");
            e.printStackTrace();
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
 
        return responseContent;
    }
 
    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @param parameters
     *            参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPost(String reqUrl, Map parameters, String verifyString) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                        HttpRequestProxy.requestEncoding));
                params.append("&");
            }
 
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
 
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestProperty("Authorization", verifyString);
            url_con.setRequestMethod("POST");
            // System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.connectTimeOut)); // （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(getConnectTimeOut()); // （单位：毫秒）jdk1.5换成这个,连接超时
            url_con.setReadTimeout(getReadTimeOut());       // （单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
 
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, getRequestEncoding()));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            System.err.println("网络故障");
            e.printStackTrace();
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }
 
    public static String doPost(String reqUrl, Map parameters) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), HttpRequestProxy.requestEncoding));
                params.append("&");
            }
 
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
 
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            // System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.connectTimeOut)); // （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(getConnectTimeOut());     // （单位：毫秒）jdk1.5换成这个,连接超时
            url_con.setReadTimeout(getConnectTimeOut());            // （单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
 
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, getRequestEncoding()));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            System.err.println("网络故障");
            e.printStackTrace();
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }
 
    /**
     * @return 连接超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
     */
    public static int getConnectTimeOut() {
        return HttpRequestProxy.connectTimeOut;
    }
 
    /**
     * @return 读取数据超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
     */
    public static int getReadTimeOut() {
        return HttpRequestProxy.readTimeOut;
    }
 
    /**
     * @return 请求编码
     * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
     */
    public static String getRequestEncoding() {
        return requestEncoding;
    }
 
    /**
     * @param connectTimeOut
     *            连接超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
     */
    public static void setConnectTimeOut(int connectTimeOut) {
        HttpRequestProxy.connectTimeOut = connectTimeOut;
    }
 
    /**
     * @param readTimeOut
     *            读取数据超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
     */
    public static void setReadTimeOut(int readTimeOut) {
        HttpRequestProxy.readTimeOut = readTimeOut;
    }
 
    /**
     * @param requestEncoding
     *            请求编码
     * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
     */
    public static void setRequestEncoding(String requestEncoding) {
        HttpRequestProxy.requestEncoding = requestEncoding;
    }
}
