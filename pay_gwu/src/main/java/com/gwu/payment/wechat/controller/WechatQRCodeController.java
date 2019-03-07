package com.gwu.payment.wechat.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwu.payment.wechat.util.HttpUtils;
import com.gwu.payment.wechat.util.Tools;
/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0
 * 类功能说明： 二维码生成 controller
 * 
 */
@Controller
@RequestMapping(value = "/wechat/qr")
public class WechatQRCodeController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 * @param request
	 * @param response
	 * @return 微信授权登录入口
	 */
	@RequestMapping(value = "/qrCode")
	@ResponseBody
	public String   qrCode(HttpServletRequest request, HttpServletResponse response) {
		//String uid=request.getParameter("uid");	
		Map<String,Object> map=Tools.getData(request);
		
		
		return HttpUtils.getWXPublicQRCode("2", map.get("uid").toString(),"qrcodebd") ;
		
	}
	
	
	
	
}