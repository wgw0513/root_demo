package com.gwu.payment.alipay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * 
 * @author gwu
 *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置支付宝帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 */
@Component
@PropertySource("classpath:alipayconfig.properties")
public class AlipayConfig {
	/**
	 * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	 */
	public static String appid;
	/**
	 *  商户私钥，您的PKCS8格式RSA2私钥
	 */
    public static String merchantprivatekey;
	/**
	 * 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
	 */
	public static String alipaypublickey;
	/**
	 * 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	 */
	public static String notifyurl;
	/**
	 * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问,支付成功返回的页面 
	 */
	public static String returnurl;
	/**
	 * 签名方式
	 */
	public static String signtype;
	/**
	 * 字符编码格式 
	 */
	public static String charset;
	/**
	 * 支付宝网关
	 */
	public static String gatewayUrl;
	/**
	 * 商户号
	 */
	public static String partner;
	/**
	 * 授权url
	 */
    public static   String oauthpayurl; 
	/**
	 * 授权返回地址url
	 */
	public static   String oauthreturnurl; 
	
	
	@Value("${alipayconfig.appid}") 
	public  void setAppid(String appid) {
		AlipayConfig.appid = appid;
	}
	
	@Value("${alipayconfig.merchantprivatekey}")
	public  void setMerchantprivatekey(String merchantprivatekey) {
		AlipayConfig.merchantprivatekey = merchantprivatekey;
	}
	
	@Value("${alipayconfig.alipaypublickey}")
	public  void setAlipaypublickey(String alipaypublickey) {
		AlipayConfig.alipaypublickey = alipaypublickey;
	}
	
	@Value("${alipayconfig.notifyurl}")
	public  void setNotifyurl(String notifyurl) {
		AlipayConfig.notifyurl = notifyurl;
	}
	
	@Value("${alipayconfig.returnurl}")
	public  void setReturnurl(String returnurl) {
		AlipayConfig.returnurl = returnurl;
	}
	
	@Value("${alipayconfig.signtype}")
	public  void setSigntype(String signtype) {
		AlipayConfig.signtype = signtype;
	}
	
	@Value("${alipayconfig.charset}")
	public  void setCharset(String charset) {
		AlipayConfig.charset = charset;
	}
	
	@Value("${alipayconfig.gatewayUrl}")
	public  void setGatewayUrl(String gatewayUrl) {
		AlipayConfig.gatewayUrl = gatewayUrl;
	}
	
	@Value("${alipayconfig.partner}")
	public  void setPartner(String partner) {
		AlipayConfig.partner = partner;
	}
	
	@Value("${alipayconfig.oauthpayurl}")
	public  void setOauthpayurl(String oauthpayurl) {
		AlipayConfig.oauthpayurl = oauthpayurl;
	}
	
	@Value("${alipayconfig.oauthreturnurl}")
	public  void setOauthreturnurl(String oauthreturnurl) {
		AlipayConfig.oauthreturnurl = oauthreturnurl;
	}

}

