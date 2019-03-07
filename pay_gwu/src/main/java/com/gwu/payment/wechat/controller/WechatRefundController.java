package com.gwu.payment.wechat.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwu.payment.wechat.service.WechatNativPayService;
import com.gwu.payment.wechat.util.Tools;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 15:17:16
 * @version 1.0.0 
 * 类功能说明：微信支付退款处理 controller
 * 修改说明：当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
 * 	微信支付将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
 * 	注意：
 * 1、交易时间超过一年的订单无法提交退款；
 * 2、微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。
 * 总退款金额不能超过用户实际支付金额。 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号。
 */
@Controller
@RequestMapping(value = "wechat/payment/back")
@PropertySource({ "classpath:/httpClient.properties" })
public class WechatRefundController {
	private static final Logger logger = LoggerFactory.getLogger(WechatRefundController.class);
	@Autowired
	WechatNativPayService wechatNativPayService;
	
	@Value("${PAYMENT_METHOD}")
	private String paymentMethod;//新支付开关  NEW为开启
	/***
	 * 
	 * @param orderID 订单号
	 * @param totalFee 订单金额
	 * @param refundFee 退款金额
	 * @return 退款操作
	 */
	@ResponseBody
	@RequestMapping(value = "/refund")
	public String refund(HttpServletRequest req) {
		logger.info("执行微信退款接口：refund===================");
		Map<String,Object> map=Tools.getData(req);
		String result = "";
		if ("NEW".equals(paymentMethod)) {
			result = wechatNativPayService.refundNew(map);
		} else {
			result = wechatNativPayService.refund(map);
		}
		return result;

	}

	

}
