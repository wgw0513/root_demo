package com.gwu.payment.wechat.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * httpClient公共方法
 * 
 * @author Huang Ping
 *
 */
public class HttpClientUtils {
	// 配置日志
	private static Log logger = LogFactory.getLog(HttpClientUtils.class);
	// utf-8字符编码
	public static final String CHARSET_UTF_8 = "utf-8";

	public static final String fileName = "/httpClient.properties";
	// HTTP内容类型。
	public static final String CONTENT_TYPE_TEXT_HTML = "text/xml";
	// HTTP内容类型。相当于form表单的形式，提交数据
	public static final String CONTENT_TYPE_FORM_URL = "application/x-www-form-urlencoded";
	// HTTP内容类型。相当于form表单的形式，提交数据
	public static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";

	public static final String APP_SERVER_URL_KEY = "APP_SERVER_URL";

	// 连接管理器
	private static PoolingHttpClientConnectionManager pool;
	// 请求配置
	private static RequestConfig requestConfig;

//	private static Properties properties = new Properties();

	static {
		try {
			logger.info("---------------------------初始化HttpClientTest start----------------------");
//			InputStream input = HttpClientUtils.class.getResourceAsStream(fileName);
//			properties.load(input);
			// 配置支持 HTTP
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory()).build();
			// 初始化连接管理器
			pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			// 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
			pool.setMaxTotal(200);
			// 设置最大路由
			pool.setDefaultMaxPerRoute(2);
			// 根据默认超时限制初始化requestConfig
			int socketTimeout = 10000;
			int connectTimeout = 10000;
			int connectionRequestTimeout = 10000;
			requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
					.setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

			logger.info("---------------------------初始化HttpClientTest end----------------------");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 设置请求超时时间
		requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000)
				.setConnectionRequestTimeout(50000).build();
	}

	public static CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom()
				// 设置连接池管理
				.setConnectionManager(pool)
				// 设置请求配置
				.setDefaultRequestConfig(requestConfig)
				// 设置重试次数
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();

		return httpClient;
	}

	/**
	 * 发送Post请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpPost(HttpPost httpPost, HttpServletRequest req) {
		logger.info("send post request");
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		// 响应内容
		String responseContent = null;
		try {
			if(null!=req) {
				// 增加客户端Cookie到请求
				StringBuffer cookies = new StringBuffer();
				Cookie[] clicentCookies = req.getCookies();
				if (clicentCookies != null) {
					for (Cookie c : clicentCookies) {
						if (!c.getName().equalsIgnoreCase("JSESSIONID")) {
							cookies.append(c.getName()).append("=").append(c.getValue()).append(";");
						}
					}
				}
				httpPost.addHeader("Cookie", cookies.toString());
				
			}
			// 创建默认的httpClient实例.
			httpClient = getHttpClient();
			// 配置请求信息
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			// 得到响应实例
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
			EntityUtils.consume(entity);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				// 释放资源
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return responseContent;
	}

	/**
	 * 发送Get请求
	 * 
	 * @param httpGet
	 * @return
	 */
	private static String sendHttpGet(HttpGet httpGet) {
		logger.info("send get request");
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		// 响应内容
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = getHttpClient();
			// 配置请求信息
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			// 得到响应实例
			HttpEntity entity = response.getEntity();

			responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
			EntityUtils.consume(entity);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				// 释放资源
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return responseContent;
	}

	/**
	 * 发送 get 请求
	 * 
	 * @param httpUrl
	 *            地址
	 * @param params
	 *            参数(格式:key1=value1&key2=value2)
	 * 
	 * @param urlKey
	 *            服务地址KEY
	 * 
	 */
	public static String sendHttpGet(String httpUrl, String params) {
		
		if (!StringUtils.hasLength(httpUrl)) {
			return null;
		}

		StringBuffer fullUrl = new StringBuffer().append(httpUrl);
		fullUrl.append(httpUrl.indexOf("?") != -1 ? "&" : "?").append(params);

		HttpGet httpGet = new HttpGet(fullUrl.toString());// 创建httpPost
		logger.debug("request url : " + httpGet.getURI().toString());
		return sendHttpGet(httpGet);
	}

	/**
	 * 发送 post请求 发送json数据
	 * 
	 * @param httpUrl
	 *            地址
	 * @param paramsJson
	 *            参数(格式 json)
	 * 
	 * @param urlKey
	 *            服务地址KEY
	 * 
	 */
	public static String sendHttpPostJson(String httpUrl, HttpServletRequest req, Map<String ,String> reqData) {
	

		HttpPost httpPost = new HttpPost(new StringBuffer().append(httpUrl).toString());// 创建httpPost
		logger.debug("request url : " + httpPost.getURI().toString());
		try {
			String paramsJson = JSON.toJSONString(reqData);
			logger.info("request parmas: " + paramsJson);
			// 设置参数
			if (paramsJson != null && paramsJson.trim().length() > 0) {
				StringEntity stringEntity = new StringEntity(paramsJson, "UTF-8");
				stringEntity.setContentType(CONTENT_TYPE_JSON_URL);
				httpPost.setEntity(stringEntity);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sendHttpPost(httpPost, req);
	}

	/** 外部调用 方法 */
	public static String sendPost(String httpUrl, HttpServletRequest req, Map<String ,String> reqData) {
		return sendHttpPostJson(httpUrl, req, reqData);
	}

}