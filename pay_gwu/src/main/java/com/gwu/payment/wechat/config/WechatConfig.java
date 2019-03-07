
package com.gwu.payment.wechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0 
 * 类功能说明：微信参数配置 用于微信公众平台后台配置
 */
@Configuration
@ConfigurationProperties(prefix = "wechatconfig", ignoreUnknownFields = false)
@PropertySource("classpath:wechatConfig.properties")
@Component
public class WechatConfig {
	/**
	 *  微信支付商户开通后 微信会提供appid和appsecret和商户号partner
	 */
	public static String appid;
	/**
	 * appsecret密钥
	 */
	public static String appsecret;
	/**
	 * 商户id
	 */
	public static String parter;
	/**
	 * 这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	 */
	public static String partenterkey;
	/**
	 * 微信支付成功后通知地址 必须要求80端口并且地址不能带参数
	 */
	public static String notifyurl;
	/**
	 * 与接口配置信息中的Token要一致
	 */
	public static String token;
	/**
	 * 网站微信后台配置域名
	 */
	public static String wzym;
	/**
	 * 微信授权地址// Key
	 */
	public static String authorizeurl;
	/**
	 *  微信授权全地址
	 */
	public static String accesstokenurl;
	/**
	 * scope
	 */
	public static String scope; 
	/**
	 * grant_type
	 */
	public static String granttype;
	/**
	 * 微信退款通知地址
	 */
	public static String renotifyurl;
	
	
	/***
	 * 报名成功通知模版
	 */
	public static String  modelMesage1;
	
	/**
	 * 申请退款通知模版
	 */
	public static String  modelMesage2;
	
	/**
	 * 投保失败通知模版
	 */
	public static String  modelMesage3;

	
	@Value("${wechatconfig.appid}")
	public void setAppid(String appid) {
		WechatConfig.appid = appid;
	}

	@Value("${wechatconfig.appsecret}")
	public void setAppsecret(String appsecret) {
		WechatConfig.appsecret = appsecret;
	}

	@Value("${wechatconfig.parter}")
	public void setParter(String parter) {
		WechatConfig.parter = parter;
	}

	@Value("${wechatconfig.partenterkey}")
	public void setPartenterkey(String partenterkey) {
		WechatConfig.partenterkey = partenterkey;
	}

	@Value("${wechatconfig.notifyurl}")
	public void setNotifyurl(String notifyurl) {
		WechatConfig.notifyurl = notifyurl;
	}

	@Value("${wechatconfig.token}")
	public void setToken(String token) {
		WechatConfig.token = token;
	}

	@Value("${wechatconfig.wzym}")
	public void setWzym(String wzym) {
		WechatConfig.wzym = wzym;
	}

	@Value("${wechatconfig.authorizeurl}")
	public void setAuthorizeurl(String authorizeurl) {
		WechatConfig.authorizeurl = authorizeurl;
	}

	@Value("${wechatconfig.accesstokenurl}")
	public static void setAccesstokenurl(String accesstokenurl) {
		WechatConfig.accesstokenurl = accesstokenurl;
	}
	
	@Value("${wechatconfig.scope}")
	public void setScope(String scope) {
		WechatConfig.scope = scope;
	}

	@Value("${wechatconfig.granttype}")
	public void setGranttype(String granttype) {
		WechatConfig.granttype = granttype;
	}
	
	@Value("${wechatconfig.renotifyurl}")
	public void setRenotifyurl(String renotifyurl) {
		WechatConfig.renotifyurl = renotifyurl;
	}

	@Value("${wechatconfig.modelMesage1}")
	public static void setModelMesage1(String modelMesage1) {
		
		WechatConfig.modelMesage1 = modelMesage1;
	}

	@Value("${wechatconfig.modelMesage2}")
	public static void setModelMesage2(String modelMesage2) {
		WechatConfig.modelMesage2 = modelMesage2;
	}
	
	@Value("${wechatconfig.modelMesage3}")
	public static void setModelMesage3(String modelMesage3) {
		WechatConfig.modelMesage3 = modelMesage3;
	}
	
	
	
	
	
}
