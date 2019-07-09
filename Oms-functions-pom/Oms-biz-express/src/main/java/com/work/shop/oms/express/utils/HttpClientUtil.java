package com.work.shop.oms.express.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.express.bean.ExpressContent;
import com.work.shop.oms.express.bean.ExpressInfo;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.StringUtil;

/**
 * @className:HttpClientUtil.java
 * @classDescription:HttpClient工具类
 * @author:fdq
 * @createTime:2012-7-11
 */

public class HttpClientUtil {

	private static Logger logger = Logger.getLogger(HttpClientUtil.class);

	public static String CHARSET_ENCODING = "UTF-8";
	
	public static final String kuaidi100_url = ConfigCenter.getProperty("KUAIDI100_URL");
	public static final String kuaidi100_key = ConfigCenter.getProperty("KUAIDI100_KEY");
	public static final String kuaidi100_customer = ConfigCenter.getProperty("KUAIDI100_CUSTOMER");
	// private static String
	// USER_AGENT="Mozilla/4.0 (compatible; MSIE 6.0; Win32)";//ie6
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
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
		httpclient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset == null ? CHARSET_ENCODING : charset);
		// 浏览器兼容性
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		// 定义重试策略
		httpclient.setHttpRequestRetryHandler(requestRetryHandler);
		return httpclient;
	}

	/**
	 * 访问https的网站
	 * 
	 * @param httpclient
	 */
	@SuppressWarnings("deprecation")
	private static void enableSSL(DefaultHttpClient httpclient) {
		// 调用ssl
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme https = new Scheme("https", sf, 443);
			httpclient.getConnectionManager().getSchemeRegistry().register(https);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 重写验证方法，取消检测ssl
	 */
	private static TrustManager truseAllManager = new X509TrustManager() {
		public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
		}
		public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
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
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
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
			HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
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
		@SuppressWarnings("deprecation")
		public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String charset = EntityUtils.getContentCharSet(entity) == null ? CHARSET_ENCODING : EntityUtils.getContentCharSet(entity);
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
	public static String post(String url, List<NameValuePair> paramsList, BasicHeader[] headers) {
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
	public static String post(String url, List<NameValuePair> paramsList, HttpHost proxy) {
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
	public static String post(String url, List<NameValuePair> paramsList, HttpHost proxy, BasicHeader[] headers) {
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
	public static String get(String url, List<NameValuePair> paramsList, HttpHost proxy) {
		return httpRequest(url, paramsList, "GET", proxy, null);
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param url
	 * @param params
	 * @param authenticated
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String httpRequest(String url, List<NameValuePair> paramsList, String method, HttpHost proxy, BasicHeader[] headers) {
		String responseStr = null;
		// 判断输入的值是是否为空
		if (null == url || "".equals(url)) {
			return null;
		}

		// 创建HttpClient实例
		DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);

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
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		try {
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000); 
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
			// 如果方法为Get
			if ("GET".equalsIgnoreCase(method)) {
				if (formatParams != null) {
					url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams) : (url.substring(0, url.indexOf("?") + 1) + formatParams);
				}
				HttpGet hg = new HttpGet(url);
				responseStr = httpclient.execute(hg, responseHandler); 
				// 如果方法为Post
			} else if ("POST".equalsIgnoreCase(method)) {
				String ck = URLDecoder
						.decode("JSESSIONID=0DCFD0CEDC92E4EF6EB00D0BB3F34D8B; OSCP_ID=42912d2e5ccd67333b35480408b72e3413529727; user_userName=hq01u8817; user_empNo=hq01u8817; osPermissions=%2213%2C14%2C15%2C16%2C17%2C21%2C23%2C24%2C25%2C26%2C28%2C29%2C30%2C31%2C31%2C32%2C33%2C35%2C36%2C37%2C38%2C39%2C40%2C41%2C42%2C43%2C44%2C45%2C46%2C47%2C48%2C49%2C50%2C51%2C52%2C53%2C54%2C55%2C56%2C57%2C59%2C61%2C62%2C63%2C64%2C68%2C69%2C83%2C84%2C85%2C87%2C88%2C89%2C90%2C91%2C92%2C94%2C95%2C96%2C97%2C99%2C105%2C106%2C107%2C108%2C109%2C110%2C111%2C112%2C113%2C114%2C116%2C117%2C118%2C119%2C121%2C122%2C123%2C124%2C125%2C126%2C132%2C133%2C134%2C135%2C136%2C137%2C138%2C139%2C140%2C141%2C142%2C143%2C144%2C145%2C146%2C147%2C148%2C149%2C150%2C151%2C152%2C153%2C155%2C156%2C157%2C158%2C159%2C160%2C161%2C162%2C163%2C164%2C165%2C166%2C167%2C168%2C169%2C170%2C171%2C172%2C173%2C174%2C175%2C176%2C177%2C178%2C179%2C180%2C181%2C182%2C183%2C184%2C185%2C186%2C187%2C188%2C189%2C190%2C191%2C192%2C193%2C194%2C195%2C196%2C197%2C198%2C199%2C200%2C201%2C202%2C203%2C204%2C205%2C206%2C207%2C208%2C209%2C210%2C211%2C212%2C213%2C214%2C215%2C216%2C217%2C218%2C219%2C220%2C221%2C222%2C223%2C224%2C225%2C226%2C227%2C228%2C229%2C230%2C231%2C232%2C233%2C234%2C235%2C236%2C237%2C238%2C239%2C240%2C241%2C242%2C243%2C244%2C245%2C246%2C247%2C248%2C249%2C250%2C251%2C252%2C253%2C254%2C255%2C256%2C257%2C258%2C259%2C260%2C261%2C262%2C263%2C264%2C265%2C266%2C267%2C268%2C269%2C270%2C271%2C272%2C273%2C274%2C275%2C276%2C277%2C279%2C280%2C281%2C282%2C283%2C284%2C285%2C286%2C287%2C288%2C293%2C294%2C295%2C296%2C298%2C299%2C300%2C303%2C304%2C305%2C306%2C307%2C308%2C309%2C310%2C311%2C312%2C313%2C314%2C315%2C316%2C317%2C318%2C319%2C320%2C321%2C322%2C323%2C324%2C326%2C327%2C328%2C329%2C330%2C331%2C332%2C334%2C335%2C336%2C337%2C339%2C343%2C344%2C345%2C346%2C347%2C349%2C351%2C354%2C355%2C356%2C358%2C359%2C360%2C361%2C362%2C363%2C364%2C365%2C366%2C367%2C368%2C369%2C370%2C371%2C372%2C373%2C374%2C375%2C376%2C377%2C378%2C379%2C380%2C381%2C382%2C383%2C384%2C385%2C386%2C387%2C388%2C389%2C390%2C391%2C392%2C393%2C394%2C395%2C396%2C397%2C398%2C399%2C400%2C401%2C402%2C403%2C404%2C405%2C406%2C407%2C408%2C409%2C410%2C411%2C412%2C413%2C414%2C415%2C416%2C417%2C418%2C419%2C420%2C421%2C422%2C423%2C424%2C425%2C426%2C427%2C428%2C429%2C430%2C431%2C432%2C433%2C434%2C435%2C436%2C437%2C438%2C439%2C440%2C441%2C442%2C443%2C444%2C445%2C446%2C447%2C448%2C449%2C450%2C451%2C452%2C453%2C454%2C455%2C456%2C457%2C458%2C459%2C460%2C461%2C462%2C463%2C464%2C465%2C466%2C467%2C468%2C494%2C495%2C496%2C497%2C498%2C499%2C508%2C513%2C514%2C515%2C516%2C517%2C518%2C519%2C526%2C527%2C528%2C532%2C533%2C534%2C535%2C536%2C562%2C564%2C566%2C567%2C568%2C569%2C570%2C593%2C594%2C595%2C597%2C598%2C599%2C600%2C601%2C602%2C604%2C605%2C606%2C607%2C608%2C609%2C611%2C614%2C615%2C616%2C617%2C618%2C619%2C620%2C621%2C622%2C623%2C625%2C627%2C628%2C629%2C630%2C631%2C632%2C633%2C634%2C635%2C636%2C637%2C638%2C639%2C661%2C666%2C667%2C668%2C669%2C670%2C671%2C672%2C673%2C674%2C675%2C676%2C677%2C678%2C680%2C681%2C682%2C683%2C684%2C685%2C686%2C687%2C688%2C689%2C690%2C691%2C695%2C696%2C697%2C698%2C699%2C700%2C701%2C702%2C703%2C704%2C705%2C706%2C707%2C708%2C709%2C710%2C711%2C713%2C714%2C715%2C716%2C717%2C718%2C721%2C722%2C732%2C733%2C737%2C738%2C740%2C741%2C742%2C745%2C755%2C758%2C759%2C760%2C765%2C766%2C767%2C769%2C770%2C771%2C772%2C773%2C774%2C775%2C776%2C777%2C779%2C780%2C781%2C782%2C783%2C786%2C787%2C788%2C801%2C816%2C817%2C818%2C819%2C820%2C822%22; interfaceAdminName=%22hq01u8817%22; LOGIN_ADMIN_ID=8B5DEECDD20087443BBB129F5CC9BAE718E0EF2D234AAA4E2DC114EE053708FEFBD41A9C2A792CAA6EA316D6418C70E3124ee7d2840e",
								"UTF-8");
				HttpPost hp = new HttpPost(url);
				if (formatParams != null) {
					StringEntity entity = new StringEntity(formatParams);
					entity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
					hp.addHeader("Cookie", ck);
					hp.addHeader("Host", "order.bang-go.com.cn");
					hp.addHeader("Referer", "http://order.bang-go.com.cn/BGOrderManager/manager/orderShips?sn=130103401007&isHitory=0");
					hp.setHeaders(headers);
					hp.setEntity(entity);
				}
				responseStr = httpclient.execute(hp, responseHandler);

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseStr;
	}
	
	/**
	 * 提交数据到服务器
	 * 
	 * @param url
	 * @param params
	 * @param authenticated
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String httpFileRequest(String url, Map<String, String> fileMap, Map<String, String> stringMap, int type, HttpHost proxy) {
		String responseStr = null;
		// 判断输入的值是是否为空
		if (null == url || "".equals(url)) {
			return null;
		}
		// 创建HttpClient实例
		DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);

		// 判断是否是https请求
		if (url.startsWith("https")) {
			enableSSL(httpclient);
		}

		// 如果代理对象不为空则设置代理
		if (null != proxy) {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		// 发送文件
		HttpPost hp = new HttpPost(url);
		MultipartEntity multiEntity = new MultipartEntity();
		try {
			// type=0是本地路径，否则是网络路径
			if (type == 0) {
				for (String key : fileMap.keySet()) {
					multiEntity.addPart(key, new FileBody(new File(fileMap.get(key))));
				}
			} else {
				for (String key : fileMap.keySet()) {
					multiEntity.addPart(key, new ByteArrayBody(getUrlFileBytes(fileMap.get(key)), key));
				}
			}
			// 加入相关参数 默认编码为utf-8
			for (String key : stringMap.keySet()) {
				multiEntity.addPart(key, new StringBody(stringMap.get(key), Charset.forName(CHARSET_ENCODING)));
			}
			hp.setEntity(multiEntity);
			responseStr = httpclient.execute(hp, responseHandler);
		} catch (Exception e) {
			logger.error(e);
		}
		return responseStr;
	}

	/**
	 * 将相关文件和参数提交到相关服务器
	 * 
	 * @param url
	 * @param fileMap
	 * @param StringMap
	 * @return
	 */
	public static String postFile(String url, Map<String, String> fileMap, Map<String, String> stringMap) {
		return httpFileRequest(url, fileMap, stringMap, 0, null);
	}

	/**
	 * 将相关文件和参数提交到相关服务器
	 * 
	 * @param url
	 * @param fileMap
	 * @param StringMap
	 * @return
	 */
	public static String postUrlFile(String url, Map<String, String> urlMap, Map<String, String> stringMap) {
		return httpFileRequest(url, urlMap, stringMap, 1, null);
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
	public static byte[] getUrlFileBytes(String url) throws ClientProtocolException, IOException {

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

	/**
	 * 获取图片的字节数组
	 * 
	 * @createTime 2011-11-24
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] getImg(String url) throws ClientProtocolException, IOException {
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

	/**
	 * 将字节数组转为输出流
	 * 
	 * @param b
	 * @return
	 */
	public static InputStream getInputStreamFromBytes(byte[] b) {
		InputStream ret = null;
		try {
			if (b == null || b.length <= 0) {
				return ret;
			}
			ret = new ByteArrayInputStream(b);
		} catch (Exception e) {
			logger.error(e);
		}
		return ret;
	}

	public static List<ExpressContent> postCOD(String host, String imgUrl, String action, List<NameValuePair> pairs, String codeName, int type) {
		HttpGet get = null;
		HttpGet img = null;
		HttpPost submit = null;
		try {
			HttpClient cilent = HttpClientUtil.getDefaultHttpClient("UTF-8");
			get = new HttpGet(host);
			cilent.execute(get, responseHandler);
			Thread.sleep(1000);
			img = new HttpGet(imgUrl + "?" + new Random().nextFloat());
			cilent.execute(img, responseHandler);
			Thread.sleep(1000);
			submit = new HttpPost(action);
			submit.setEntity(new UrlEncodedFormEntity(pairs, CHARSET_ENCODING));
			submit.addHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
			submit.addHeader("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)");
			submit.addHeader("Connection", "Keep-Alive");
			String back = cilent.execute(submit, responseHandler);
			Document doc1 = Jsoup.parse(back);
			Elements els1 = doc1.getElementsByTag("script");
			Element script = els1.last();
			String table = script.html();
			doc1 = Jsoup.parse(table);
			Elements tabs = doc1.getElementsByTag("table");
			if (tabs.size() > 1) {
				Element result = tabs.last();
				List<ExpressContent> expressContents = new ArrayList<ExpressContent>();
				Element tbody = result.child(1);
				Elements trs = tbody.getElementsByTag("tr");
				String dateStr = "";
				if (trs.size() > 2) {
					for (Element tr : trs) {
						Elements tds = tr.getElementsByTag("td");
						if (tds.size() > 3) {
							Element date = tds.get(0);
							Element time = tds.get(1);
							Element act = tds.get(2);
							if (!"".equals(date.text())) {
								dateStr = date.text();
							}
							String dstr = dateStr + " " + time.text() + ":00";

							ExpressContent content = new ExpressContent();
							content.setContext(act.text());
							content.setTime(dstr);
							expressContents.add(content);
						}
					}
				}
				return expressContents;
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			if (get != null) {
				get.releaseConnection();
			}
			if (img != null) {
				img.releaseConnection();
			}
			if (submit != null) {
				submit.releaseConnection();
			}
		}
		return null;
	}

	public static List<ExpressInfo> getZMKMBySite(String invoice) {
		HttpPost pt = null;
		try {
			String zmURL = "http://211.155.229.58:8085/query.aspx?jobno=" + invoice;
			HttpClient c = HttpClientUtil.getDefaultHttpClient("UTF-8");
			pt = new HttpPost(zmURL);
			pt.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			pt.addHeader("Connection", "keep-alive");
			pt.addHeader("Accept-Encoding", "gzip, deflate");
			pt.addHeader("Host", "211.155.229.58:8085");
			pt.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:13.0) Gecko/20100101 Firefox/13.0.1");
			// if(hr.getStatusLine().getStatusCode()!=200){
			// return null;
			// }
			String html = c.execute(pt, responseHandler);
			Document doc = Jsoup.parse(html);
			List<ExpressInfo> list = new ArrayList<ExpressInfo>();
			int i = 1;
			Elements tables = doc.getElementsByTag("table");
			if (tables != null && tables.size() > 0) {
				ExpressInfo info = null;
				for (Element table : tables) {
					if (i % 4 == 1) {
						// 取快递单号
						info = new ExpressInfo();
						Elements trs = table.getElementsByTag("tr");
						if (trs != null && trs.size() > 0) {
							Element tr = trs.get(trs.size() - 1);
							// 获取快递单号
							String _inv = tr.getElementsByTag("td").get(0).text();
							if (StringUtils.isBlank(_inv)) {
								return null;
							}
							info.setCodenumber(_inv);
							info.setNu(_inv);
							info.setCom("zmkm");
						}
					}
					if (i % 4 == 3) {
						// 取快递信息
						Elements trs = table.getElementsByTag("tr");
						if (trs != null && trs.size() > 0) {
							List<ExpressContent> contents = new ArrayList<ExpressContent>();
							for (int k = 2; k < trs.size(); k++) {
								Element tr = trs.get(k);
								Elements tds = tr.getElementsByTag("td");
								String content = tds.get(tds.size() - 1).text();
								String date = tds.get(0).text();
								String time = tds.get(1).text();
								ExpressContent cx = new ExpressContent();
								cx.setContext(content);
								cx.setTime(date + " " + time);
								contents.add(cx);
							}
							info.setData(contents);
							list.add(info);
						}
					}
					if (i % 4 == 0) {
						info = null;
					}
					i++;
				}

			}

			return list;
		} catch (Exception e) {
			logger.error(e);
		} finally {
		}
		return null;
	}

	public static String getRandomIp() {
		String ip = "222.66.";
		return ip + new Random().nextInt(222) + "." + new Random(999999).nextInt(109);
	}

	public static String getInfoBy100WithCode(String invoice, String com, int codeSize) {
		HttpGet hg = null;
		try {
			String key = ExpressConstant.KUAIDI100_KEY[new Random().nextLong() % 2 == 0 ? 0 : 1];
			String imgURL = "http://api.kuaidi100.com/verifyCode?id=" + key + "&com=" + com;
			String queryUrl = "http://api.kuaidi100.com/api?id=" + key + "&com=" + com + "&nu=%s&valicode=%s&show=0&muti=1&order=asc";
			HttpClient c = HttpClientUtil.getDefaultHttpClient("UTF-8");
			hg = new HttpGet(imgURL);
			HttpResponse hr = c.execute(hg);
			if (hr.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			String code = OCRUtil.recognizeValidation(getInputStreamFromBytes(EntityUtils.toByteArray(hr.getEntity())));
			if (code != null && code.trim().length() == codeSize) {
				queryUrl = String.format(queryUrl, invoice, code);
				hg = new HttpGet(queryUrl);
				hr = c.execute(hg);
				if (hr.getStatusLine().getStatusCode() != 200) {
					return null;
				}
				return EntityUtils.toString(hr.getEntity());
			}
		} catch (RuntimeException e) {
			logger.error("scan the image error");
			logger.error(e);
		} catch (Exception re) {
			logger.error(re);
		} finally {
			if (hg != null) {
				hg.releaseConnection();
			}
		}
		return null;
	}

	/**
	 * 快递100无验证API example：
	 * http://api.kuaidi100.com/api?id=[]&com=[]&nu=[]&valicode
	 * =[]&show=[0|1|2|3]&muti=[0|1]&order=[desc|asc]
	 * 
	 * @param invoice
	 * @return
	 */
	public static ExpressInfo getKuaidi100WithNoVal(String invoice, String com) {
		HttpGet hg = null;
		try {
			HttpClient c = HttpClientUtil.getDefaultHttpClient("UTF-8");
//			String key = ExpressConstant.KUAIDI100_KEY[new Random().nextLong() % 2 == 0 ? 0 : 1];
			String queryUrl = "http://api.kuaidi100.com/api?id=" + kuaidi100_key + "&com=%s&nu=%s&show=0&muti=1&order=asc";
			// System.out.println("queryUrl="+queryUrl);
			queryUrl = String.format(queryUrl, com, invoice);
			// System.out.println("queryUrl="+queryUrl);
			logger.info("请求快递100接口 queryUrl：" + queryUrl);
			hg = new HttpGet(queryUrl);
			HttpResponse hr = c.execute(hg);
			if (hr.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			String result = EntityUtils.toString(hr.getEntity());
			logger.info("请求快递100接口返回结果：" + result);
			ExpressInfo info = JSON.parseObject(result, ExpressInfo.class);
			if (!info.getData().isEmpty()) {
				return info;
			}
			return info;
		} catch (RuntimeException re) {
			logger.error("请求快递100接口异常" + re.getMessage(), re);
		} catch (Exception e) {
			logger.error("请求快递100接口异常" + e.getMessage(), e);
		} finally {
			if (hg != null) {
				hg.releaseConnection();
			}
		}
		return null;
	}
	
	

	/**
	 * 快递100有验证api
	 */
	public static String getKuaidi100WithVal(String invoice, String com) {
		HttpGet hg = null;
		try {
			HttpClient c = HttpClientUtil.getDefaultHttpClient("UTF-8");
			String key = ExpressConstant.KUAIDI100_KEY[new Random().nextLong() % 2 == 0 ? 0 : 1];
			String imgURL = "http://api.kuaidi100.com/verifyCode?id=" + key + "&com=ems";
			hg = new HttpGet(imgURL);
			HttpResponse hr = c.execute(hg);
			if (hr.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			String code = OCRUtil.recognizeValidation(getInputStreamFromBytes(EntityUtils.toByteArray(hr.getEntity())));

			String queryUrl = "http://api.kuaidi100.com/api?id=" + key + "&com=%s&nu=%s&valicode=%s&show=0&muti=1&order=asc";

			if (code != null && code.trim().length() > 4) {
				queryUrl = String.format(queryUrl, com, invoice, code);
				hg = new HttpGet(queryUrl);
				hr = c.execute(hg);
				if (hr.getStatusLine().getStatusCode() != 200) {
					return null;
				}
				return EntityUtils.toString(hr.getEntity());
			}
		} catch (RuntimeException e) {
			logger.error("scan the image error");
			logger.error(e);
		} catch (Exception re) {
			logger.error(re);
		} finally {
			if (hg != null) {
				hg.releaseConnection();
			}
		}
		return null;
	}

	/**
	 * 根据网站Url得到Docement对象 Post请求
	 **/
	public static Document getDocumentBySite(String formSubmitUrl, List<NameValuePair> nameValuePairList) {
		HttpClient httpClient = HttpClientUtil.getDefaultHttpClient("UTF-8");
		return getDocumentBySite(httpClient, formSubmitUrl, nameValuePairList);
	}

	/**
	 * 根据网站Url得到Docement对象 Post请求
	 **/
	public static Document getDocumentBySite(HttpClient httpClient, String formSubmitUrl, List<NameValuePair> nameValuePairList) {
		HttpPost httpPost = null;
		HttpResponse httpResponse = null;
		try {
			httpPost = new HttpPost(formSubmitUrl);

			List<Cookie> lists = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
			Cookie ck = null;
			if (lists != null && lists.size() > 1) {
				ck = lists.get(1);
				// String[] strs = ck.getValue().split("=");
				// if(strs.length == 2) verifyCode = strs[1];
			}

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, CHARSET_ENCODING));
			httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:13.0) Gecko/20100101 Firefox/13.0.1");
			httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpPost.addHeader("Accept-Language", "zh-cn,zh;q=0.5");
			httpPost.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			httpPost.addHeader("Content", "text/html,charset=UTF-8");
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			// httpPost.addHeader("X_FORWARD_FOR", getRandomIp());
			httpPost.addHeader("Connection", "keep-alive");
			if (ck != null)
				httpPost.addHeader("Cookie", ck.getName() + "=" + ck.getValue());

			httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			String html = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

			return Jsoup.parse(html);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
		return null;
	}

	/**
	 * 根据网站Url得到Docement对象 Get请求
	 **/
	public static Document getDocumentBySite(String httpGetUrl) {
		HttpGet httpGet = null;
		Document doc = null;
		try {
			HttpClient httpClient = HttpClientUtil.getDefaultHttpClient("UTF-8");
			httpGet = new HttpGet(httpGetUrl);
			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			String html = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			doc = Jsoup.parse(html);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
		}
		return doc;
	}

	public static String pUrl = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";
	public static String checkword = "V1ssuq59UDRlEf0zDDkwEuiEXcJWdtrK";

	public static ExpressInfo queryBS(String trackNo) {
		try {
			List<NameValuePair> nv = new ArrayList<NameValuePair>();
			String param = "{'invoiceList':[%s]}";
			String pr = String.format(param, "'" + trackNo + "'");
			NameValuePair p1 = new BasicNameValuePair("invoices", pr);
			nv.add(p1);
			String xml = URLDecoder.decode(HttpClientUtil.post(ExpressConstant.BS_URL, nv), "UTF-8");
			if (xml.equals("[]")) {
				return null;
			}
			ExpressInfo exInfo = CommonUtil.xmlBS(xml);
			return exInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static ExpressInfo queryYTO(String trackNo) {
		try {
			List<NameValuePair> nv = new ArrayList<NameValuePair>();
			String order = String.format(ExpressConstant.YTO_XML_ORDER, trackNo);
			String str1 = String.format(ExpressConstant.YTO_XML, order);
			String val = str1 + ExpressConstant.YTO_XML_PREFIX;
			NameValuePair p1 = new BasicNameValuePair("logistics_interface", str1);
			NameValuePair p2 = new BasicNameValuePair("data_digest", Encoder.getEncodeByMd5(val));
			nv.add(p1);
			nv.add(p2);
			String xml = HttpClientUtil.post(ExpressConstant.YTO_URL, nv);
			ExpressInfo exInfo = CommonUtil.xmlToStringForYTO(xml);
			return exInfo;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static List<ExpressInfo> queryBatchYTO(Collection<String> trackNo) {
		try {
			List<NameValuePair> nv = new ArrayList<NameValuePair>();
			StringBuffer buffer = new StringBuffer();
			for (String no : trackNo) {
				buffer.append(String.format(ExpressConstant.YTO_XML_ORDER, no));
			}
			String str1 = String.format(ExpressConstant.YTO_XML, buffer.toString());
			String val = str1 + ExpressConstant.YTO_XML_PREFIX;
			NameValuePair p1 = new BasicNameValuePair("logistics_interface", str1);
			NameValuePair p2 = new BasicNameValuePair("data_digest", Encoder.getEncodeByMd5(val));
			nv.add(p1);
			nv.add(p2);
			String xml = HttpClientUtil.post(ExpressConstant.YTO_URL, nv);
			// System.out.println("圆通"+xml);
			return CommonUtil.xmlToStringForBatchYTO(xml);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static ExpressInfo getKuaiDi100ByHtmlApi(String company, String trackNo) {
		try {
//			String key = ExpressConstant.KUAIDI100_KEY[new Random().nextLong() % 2 == 0 ? 0 : 1];
			NameValuePair key100 = new BasicNameValuePair("key", kuaidi100_key);
			NameValuePair com = new BasicNameValuePair("com", company);
			NameValuePair track = new BasicNameValuePair("nu", trackNo);
			String url = "http://www.kuaidi100.com/applyurl";
			List<NameValuePair> pair = new ArrayList<NameValuePair>();
			pair.add(key100);
			pair.add(com);
			pair.add(track);
			post(url, pair);
			pair.clear();
			String coreUrl = "http://www.kuaidi100.com/query";
			pair.add(new BasicNameValuePair("id", "1"));
			pair.add(new BasicNameValuePair("type", company));
			pair.add(new BasicNameValuePair("postid", trackNo));
			pair.add(new BasicNameValuePair("valicode", ""));
			pair.add(new BasicNameValuePair("temp", new Random().nextFloat() + ""));
			String back = post(coreUrl, pair);
			ExpressInfo info = JSON.parseObject(back, ExpressInfo.class);
			if (info.getData() != null && info.getData().size() > 1) {
				List<ExpressContent> list = info.getData();
				ExpressContent c1 = list.get(0);
				ExpressContent c2 = list.get(list.size() - 1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				boolean f = CommonUtil.compareDate(sdf.parse(c1.getTime()), sdf.parse(c2.getTime()));
				if (!f) {
					Collections.reverse(list);
					info.setData(list);
				}
			}

			return info;
		} catch (Exception e) {
			logger.error(e + "companyName:" + company + " and invocieNo is：" + trackNo);
		}
		return null;
	}

	/**
	 * 
	 * @param invoice
	 * @param com
	 * @param from
	 * @param to
	 * @return
	 */
	public static ExpressInfo getKuaidi100NEW(String invoice, String com, String from, String to) {
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("com", com);
			param.put("num", invoice);
			param.put("from", from);
			param.put("to", to);
			String paramString = JSON.toJSONString(param);
			String sign = getSign(paramString, kuaidi100_key, kuaidi100_customer);
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("customer", kuaidi100_customer));
			valuePairs.add(new BasicNameValuePair("sign", sign));
			valuePairs.add(new BasicNameValuePair("param", paramString));
			logger.info("请求快递100接口 queryUrl：" + kuaidi100_url + "; param=" + JSON.toJSONString(valuePairs));
			String result=HttpClientUtil.post(kuaidi100_url, valuePairs);
			logger.info("请求快递100接口返回结果：" + result);
			Map<String, String> resultMap = JSON.parseObject(result, Map.class);
			if (resultMap != null) {
				String returnCode = resultMap.get("resultMap");
				if (StringUtil.isNotEmpty(returnCode)) {
					return null;
				}
			}
			ExpressInfo info = new ExpressInfo();
			try {
				info = JSON.parseObject(result, ExpressInfo.class);
			} catch (Exception e) {
				logger.error("请求快递100接口返回结果" + result + "，转JSON对象异常" + e.getMessage(), e);
				return null;
			}
			if (!info.getData().isEmpty()) {
				return info;
			}
			return info;
		} catch (RuntimeException re) {
			logger.error("请求快递100接口异常" + re.getMessage(), re);
		} catch (Exception e) {
			logger.error("请求快递100接口异常" + e.getMessage(), e);
		}
		return null;
	}
	
	private static String getSign(String paramString, String key, String customer) {
		String rs = paramString + key + customer;
		String md5String = MD5(rs);
		return md5String;
	}

	public static ExpressInfo getKuaidi100WithNoValOverrider(String invoice, String com) {
		HttpGet hg = null;
		try {

			URI u = new URI("http://api.kuaidi100.com/api?id=e43663f9ac75b755&com=" + com + "&nu=" + invoice + "&show=0&muti=1&order=asc");
			java.net.URL url = u.toURL();
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(8000);
			connection.setReadTimeout(5000);
			connection.connect();
			ExpressInfo info = JSON.parseObject(new String(readStream(connection.getInputStream())), ExpressInfo.class);
			if (!info.getData().isEmpty()) {
				info.setStatus(1);//表示查询成功
				return info;
			}
		} catch (RuntimeException e) {
			logger.error(e);
		} catch (Exception re) {
			logger.error(re);
		} finally {
			if (hg != null) {
				hg.releaseConnection();
			}
		}
		return null;
	}

	private static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			logger.error("MD5异常",e);
			return null;
		}
	}
	
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
}
