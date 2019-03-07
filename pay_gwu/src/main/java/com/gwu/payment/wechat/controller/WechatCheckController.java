package com.gwu.payment.wechat.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gwu.payment.wechat.service.WechatCoreService;
import com.gwu.payment.wechat.util.SignUtil;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0
 * 类功能说明：微信认证 controller
 * 用于微信公众平台后台配置开发验证
 */
@Controller
@RequestMapping(value = "/wechat/payment")
public class WechatCheckController {
	@Autowired
	WechatCoreService wechatCoreService;
	/**
	 * 确认请求来自微信服务器
	 * @throws IOException 
	 */
	@RequestMapping(value = "/check")
	public void check(HttpServletRequest request, HttpServletResponse response) {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		try {
			PrintWriter out = response.getWriter();
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {	
				String respXml =wechatCoreService.handlePublicMsg(request);
				if(respXml==null) {
					out.print(" ");	
				}else {
					out.print(respXml);	
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
