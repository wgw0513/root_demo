package com.gwu.payment.wechat.controller;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gwu.payment.service.WechatUserService;
import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.po.WechatOauth2Token;
import com.gwu.payment.wechat.po.WechatUserInfo;
import com.gwu.payment.wechat.util.WechatUtil;
/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0
 * 类功能说明：授权登录 controller
 * 
 */
@Controller
@RequestMapping(value = "/wechat/oauth")
public class OauthWechatController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	WechatUserService wechatUserService;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return 微信授权登录入口
	 */
	@RequestMapping(value = "/checkOauth")
	public String   checkOauth(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			String returnUrl = request.getParameter("returnUrl");
			String uid = request.getParameter("uid");
			String code = request.getParameter("code");
	        boolean isValidCode = true;  
	        logger.info("uid="+uid);
	        String serviceUrl = URLEncoder.encode(WechatConfig.wzym +"/wechat/oauth/checkOauth?returnUrl="+returnUrl, "utf-8");  
	        logger.info("*******************serviceUrl" + serviceUrl);
	        //检查是否已验证或者验证是否通过  
	        if (code == null || code.equals("authdeny")) {  
	            isValidCode = false;  
	        }  
	        //如果session未空或者取消授权，重定向到授权页面  
	        if ((!isValidCode)) {  
	            StringBuilder oauth_url = new StringBuilder();  
	            oauth_url.append(WechatConfig.authorizeurl);  
	            oauth_url.append("?appid=").append(WechatConfig.appid);  
	            oauth_url.append("&redirect_uri=").append(serviceUrl);  
	            oauth_url.append("&response_type=code");  
	            oauth_url.append("&scope=snsapi_userinfo");  
	            oauth_url.append("&state=STATE&connect_redirect=1#wechat_redirect");  
	            logger.info("*******************oauth_url" + oauth_url.toString());
	            response.sendRedirect(oauth_url.toString());   
	        }  
	        //如果用户同意授权并且，用户session不存在，通过OAUTH接口调用获取用户信息  
	        if (isValidCode) {  
	        	WechatOauth2Token weixinOauth2Token = WechatUtil.getOauth2AccessToken(WechatConfig.appid,
						WechatConfig.appsecret, code);
				String accessToken = weixinOauth2Token.getAccessToken();
				String openID = weixinOauth2Token.getOpenId();
				//获取用户信息
				WechatUserInfo snsUserInfo = WechatUtil.getWXUserInfo(accessToken, openID);
//				if(uid!=null) {
//						snsUserInfo.setUid(Integer.parseInt(uid));
//				}
				//报讯微信用户表信息
				wechatUserService.setWechatUserInfo(snsUserInfo);
				 response.sendRedirect(returnUrl.toString()+"?openId="+openID);   
				// return "redirct:";
	        }  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
}