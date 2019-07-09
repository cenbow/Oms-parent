package com.work.shop.oms.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	public static String CHARSET_ENCODING = "UTF-8";
	// private static String
	public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:13.0) Gecko/20100101 Firefox/13.0.1";// firefox

	// private static String
	// USER_AGENT="Mozilla/4.0 (compatible; MSIE 8.0; Win32)";//ie8

	/**
	 * 获取DefaultHttpClient对象,此对象是线程安全的
	 * 
	 * @param charset
	 *            字符编码
	 * @return DefaultHttpClient对象
	 */
	public static DefaultHttpClient getDefaultHttpClient(final String charset) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 模拟浏览器，解决一些服务器程序只允许浏览器访问的问题
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
				USER_AGENT);
		httpclient.getParams().setParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		httpclient.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET,
				charset == null ? CHARSET_ENCODING : charset);
		// 浏览器兼容性
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		// 定义重试策略
		httpclient.setHttpRequestRetryHandler(requestRetryHandler);
		return httpclient;
	}

	/**
	 * 访问https的网站
	 * 
	 * @param httpclient
	 */
	private static void enableSSL(DefaultHttpClient httpclient) {
		// 调用ssl
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme https = new Scheme("https", sf, 443);
			httpclient.getConnectionManager().getSchemeRegistry()
					.register(https);
		} catch (Exception e) {
			logger.error(e.getCause()+":"+e.getMessage());
		}
	}

	/**
	 * 重写验证方法，取消检测ssl
	 */
	private static TrustManager truseAllManager = new X509TrustManager() {

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {

		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {

		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	};

	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 设置恢复策略，在发生异常时候将自动重试3次
			if (executionCount >= 3) {
				// 如果连接次数超过了最大值则停止重试
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// 如果服务器连接失败重试
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// 不要重试ssl连接异常
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// 重试，如果请求是考虑幂等
				return true;
			}
			return false;
		}
	};

	/**
	 * 使用ResponseHandler接口处理响应，HttpClient使用ResponseHandler会自动管理连接的释放，解决了对连接的释放管理
	 */
	private static ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
		// 自定义响应处理
		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String charset = EntityUtils.getContentCharSet(entity) == null ? CHARSET_ENCODING
						: EntityUtils.getContentCharSet(entity);
				return new String(EntityUtils.toByteArray(entity), charset);
			} else {
				return null;
			}
		}
	};

	/**
	 * 使用post方法获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @return
	 */
	public static String post(String url, List<NameValuePair> paramsList) {
		return httpRequest(url, paramsList, "POST", null, null);
	}

	/**
	 * 使用post方法获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @return
	 */
	public static String post(String url, List<NameValuePair> paramsList,
			BasicHeader[] headers) {
		return httpRequest(url, paramsList, "POST", null, headers);
	}

	/**
	 * 使用post方法并且通过代理获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @param proxy
	 * @return
	 */
	public static String post(String url, List<NameValuePair> paramsList,
			HttpHost proxy) {
		return httpRequest(url, paramsList, "POST", proxy, null);
	}

	/**
	 * 使用post方法并且通过代理获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @param proxy
	 * @return
	 */
	public static String post(String url, List<NameValuePair> paramsList,
			HttpHost proxy, BasicHeader[] headers) {
		return httpRequest(url, paramsList, "POST", proxy, headers);
	}

	/**
	 * 使用get方法获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @return
	 */
	public static String get(String url, List<NameValuePair> paramsList) {
		return httpRequest(url, paramsList, "GET", null, null);
	}

	/**
	 * 使用get方法并且通过代理获取相关的数据
	 * 
	 * @param url
	 * @param paramsList
	 * @param proxy
	 * @return
	 */
	public static String get(String url, List<NameValuePair> paramsList,
			HttpHost proxy) {
		return httpRequest(url, paramsList, "GET", proxy, null);
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param url
	 * @param paramsList
	 * @param method
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String httpRequest(String url,
			List<NameValuePair> paramsList, String method, HttpHost proxy,
			BasicHeader[] headers) {
		String responseStr = null;
		// 判断输入的值是是否为空
		if (null == url || "".equals(url)) {
			return null;
		}

		// 创建HttpClient实例
		DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);

		// 判断是否是https请求
		if (url.startsWith("https")) {
			enableSSL(httpclient);
		}
		String formatParams = null;
		// 将参数进行utf-8编码
		if (null != paramsList && paramsList.size() > 0) {
			formatParams = URLEncodedUtils.format(paramsList, CHARSET_ENCODING);
		}
		// 如果代理对象不为空则设置代理
		if (null != proxy) {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
		HttpGet hg = null;
		HttpPost hp = null;
		try {
			// 如果方法为Get
			if ("GET".equalsIgnoreCase(method)) {
				if (formatParams != null) {
					url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams)
							: (url.substring(0, url.indexOf("?") + 1) + formatParams);
				}
				hg = new HttpGet(url);
				responseStr = httpclient.execute(hg, responseHandler);
				
				// 如果方法为Post
			} else if ("POST".equalsIgnoreCase(method)) {
				hp = new HttpPost(url);
				if (formatParams != null) {
					StringEntity entity = new StringEntity(formatParams);
					entity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
					hp.setHeaders(headers);
					hp.setEntity(entity);
				}
				responseStr = httpclient.execute(hp, responseHandler);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error(e+" "+url);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e+" "+url);
		} finally {
			if (null != httpclient) { // 关闭
				httpclient.getConnectionManager().shutdown();
			}
		}
		return responseStr;
	}


	/**
	 * 获取网络文件的字节数组
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] getUrlFileBytes(String url)
			throws ClientProtocolException, IOException {

		byte[] bytes = null;
		// 创建HttpClient实例
		DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);
		// 获取url里面的信息
		HttpGet hg = new HttpGet(url);
		HttpResponse hr = httpclient.execute(hg);
		bytes = EntityUtils.toByteArray(hr.getEntity());
		// 转换内容为字节
		return bytes;
	}
	
	public static String postXmlFile1(String url,
			List<NameValuePair> pms, String xmlpostname, String xml) throws IOException {
		
		String postURL = "";
		
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		for(NameValuePair p : pms) {
			sb.append(p.getName()).append("=").append(p.getValue()).append("&");
		}
		
		if(sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		postURL = sb.toString();
		
		postURL = postURL.replaceAll("\\s", "+");
		
		
		URL postUrl = new URL(postURL);
		
//		System.out.println(postURL);
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
		connection.setConnectTimeout(1000000);
		connection.setReadTimeout(1000000);

		connection.addRequestProperty("Content-Type",
				"multipart/form-data; boundary=FoeeWxM0WaLNKwsc5Tj6_W7O5Fd9n7");

//		connection.addRequestProperty("Content-Length",
//				xml.getBytes("GBK").length + "");
		
		xml = "--FoeeWxM0WaLNKwsc5Tj6_W7O5Fd9n7\r\n"
				+ "Content-Disposition: form-data; name=\"" + xmlpostname
				+ "\"; filename=\"sendGoods.xml\"\r\n"
				+ "Content-Type: application/octet-stream\r\n"
				+ "Content-Transfer-Encoding: binary\r\n" + "\r\n" + xml
				+ "\r\n" + "--FoeeWxM0WaLNKwsc5Tj6_W7O5Fd9n7--\r\n";
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.connect();
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());
		out.write(xml.getBytes("GBK"));

		out.flush();
		out.close();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(
//				connection.getInputStream(), "GBK"));
		InputStream in = connection.getInputStream();
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		int b;
		while ((b = in.read()) != -1) {
			out1.write(b);
		}
		connection.disconnect();
		return new String(out1.toByteArray(),"GBK");
	}

    /**
     * 获取参数bean
     * @param name
     * @param value
     * @return
     */
	public static NameValuePair getNameValuePair(String name, String value) {
        return new NameValuePair() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getValue() {
                return value;
            }
        };
    }

    public static String format(List<? extends NameValuePair> parameters) {
        StringBuilder result = new StringBuilder();
        Iterator i$ = parameters.iterator();

        while(i$.hasNext()) {
            NameValuePair parameter = (NameValuePair)i$.next();
            String encodedName = parameter.getName();
            String encodedValue = parameter.getValue();
            if (result.length() > 0) {
                result.append("&");
            }

            result.append(encodedName);
            if (encodedValue != null) {
                result.append("=");
                result.append(encodedValue);
            }
        }

        return result.toString();
    }
}
