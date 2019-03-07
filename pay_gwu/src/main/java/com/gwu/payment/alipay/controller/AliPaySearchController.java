package com.gwu.payment.alipay.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwu.payment.alipay.service.AliPayService;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0
 * 类功能说明：支付宝订单查询 controller
 * 
 */
@Controller
@RequestMapping(value = "/ali/payment/search")
public class AliPaySearchController {
	private static final Logger logger = LoggerFactory.getLogger(AliPaySearchController.class);
	@Autowired
	AliPayService aliPayService;
	
	/**
	 * @author gwu
	 * @param req
	 * @param resp
	 * @param body 订单内容
	 * @param openID 
	 * @param orderID 订单号
	 * @param totalFee 订单金额
	 * @return 支付宝订单查询
	 */
	@ResponseBody
	@RequestMapping(value = "/alipaySearch")
	public String  alipaySearch(String orderID,String payType) {      
		logger.info("search alipay========");
		//请求
		try {
			String result = aliPayService.searchOrderByOrderID(orderID, payType);
			
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	


}