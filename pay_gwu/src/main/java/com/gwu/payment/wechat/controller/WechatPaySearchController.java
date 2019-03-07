package com.gwu.payment.wechat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwu.payment.wechat.service.WechatNativPayService;

/**
 * 
 * @author gwu
 *微信查询接口
 */
@Controller
@RequestMapping(value = "wechat/payment/search")
public class WechatPaySearchController {
	private static final Logger logger = LoggerFactory.getLogger(WechatPaySearchController.class);
	@Autowired
	WechatNativPayService wechatNativPayService;
	/***
	 * 
	 * @param orderID 订单号
	 * @param totalFee 订单金额
	 * @param refundFee 退款金额
	 * @param transactionID 微信订单号
	 * @return 退款操作
	 * http://wx.ringbet.com.cn/wechat/payment/search/isPayOverAlready?orderID=18082109480214436209&payType=WR
	 */
	@ResponseBody
	@RequestMapping(value = "/isPayOverAlready")
	public String isPayOverAlready(String orderID,String payType) {
		logger.info("执行微信查询接口：refund===================");
		String result = wechatNativPayService.isPayOverAlready(orderID, payType);
		return result;

	}
	
	/***
	 * 
	 * @param orderID 订单号
	 * @param totalFee 订单金额
	 * @param refundFee 退款金额
	 * @param transactionID 微信订单号
	 * @return 查询订单信息
	 */
	@ResponseBody
	@RequestMapping(value = "/selctPayOrder")
	public String selctPayOrder(String orderID,String payType) {
		logger.info("执行微信查询接口：refund===================");
		String result = wechatNativPayService.isPayOverAlready(orderID, payType);
		return result;
		
	}
	
	/**
	 * 
	 * @param orderID
	 * @param payType
	 * @return 退款查询操作
	 */
	@ResponseBody
	@RequestMapping(value = "/refundQty")
	public String refundQty(String orderID,String payType) {
		logger.info("执行微信退款查询接口：refundQty===================");
		String result = wechatNativPayService.refundQty(orderID, payType);
		return result;
		
	}


}
