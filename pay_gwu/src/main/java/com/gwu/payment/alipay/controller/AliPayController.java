package com.gwu.payment.alipay.controller;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.gwu.payment.alipay.service.AliPayService;
import com.gwu.payment.allinpay.service.SybPayService;
import com.gwu.payment.allinpay.util.HttpConnectionUtil;
import com.gwu.payment.allinpay.util.SybConstants;
import com.gwu.payment.allinpay.util.SybUtil;
import com.gwu.payment.service.XftPaymentService;
import com.gwu.payment.wechat.util.Tools;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0
 * 类功能说明：支付宝支付处理 controller
 * 
 */
@Controller
@RequestMapping(value = "/ali/payment/pay")
@PropertySource({ "classpath:/httpClient.properties" })
public class AliPayController {
	
private static final Logger logger = LoggerFactory.getLogger(AliPayController.class);
	@Autowired
	XftPaymentService xftPaymentService;
	@Autowired
	AliPayService aliPayService;
	@Autowired
	SybPayService sybPayService;

	@Value("${PAYMENT_METHOD}")
	private String paymentMethod;//新支付开关  NEW为开启
	
	
	/**
	 * @author gwu
	 * @param req
	 * @param resp
	 * @param body 订单内容
	 * @param openID 
	 * @param orderID 订单号
	 * @param totalFee 订单金额
	 * @return 支付宝支付
	 * rderId:ordrerID,subject:subject,totalFee:totalFee,body:body
	 */
	@ResponseBody
	@RequestMapping(value = "/alipay")
	public String  alipay(HttpServletRequest req) {
		logger.info("into alipay-pay==============");
		
		
		Map<String,Object> map=Tools.getData(req);
		
		if ("NEW".equals(paymentMethod)) {
	
			return aliPayService.aliPCNewPay(map);
		} else {
		
			return aliPayService.aliPCPay(map); 
		}
	}

	/**
	 * @author gwu
	 * @param req
	 * @param resp
	 * @param body 订单内容
	 * @param openID 
	 * @param orderID 订单号
	 * @param totalFee 订单金额
	 * @return 支付宝手机页面支付
	 * rderId:ordrerID,subject:subject,totalFee:totalFee,body:body
	 * @throws AlipayApiException 
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "/aliMobilePay")
	public String  aliMobilePay(HttpServletRequest req, HttpServletResponse resp,String orderID,String totalFee,String body,String noticeURL,String payType){      
		logger.info("into alipay-mobilepay==============");
		try {
			if ("NEW".equals(paymentMethod)) {
				return aliPayService.moblieNEWPay(orderID,  body, totalFee, body,noticeURL,payType);
				
			} else {
				return aliPayService.mobliePay(orderID,  body, totalFee, body,noticeURL,payType);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

		
	}



}