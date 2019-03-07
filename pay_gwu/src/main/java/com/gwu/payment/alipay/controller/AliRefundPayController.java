

package com.gwu.payment.alipay.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwu.payment.alipay.service.AliPayService;
import com.gwu.payment.wechat.util.Tools;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0
 * 类功能说明：支付宝退款处理 controller
 * 
 */
@Controller
@RequestMapping(value = "/ali/payment/refund")
public class AliRefundPayController{
	
private static final Logger logger = LoggerFactory.getLogger(AliRefundPayController.class);
	@Autowired
	AliPayService aliPayService;
	@Value("${PAYMENT_METHOD}")
	private String paymentMethod;//新支付开关  NEW为开启
	/**
	 * @author gwu
	 * @param orderID 订单号
	 * @param refundFee 退款金额
	 * @param refundReason 退款原因
	 * @return 支付宝退款
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/aliRefund")
	public String  aliRefund(HttpServletRequest req) {
		Map<String,Object> map=Tools.getData(req);
		//请求
		String result="";
		try {
			
			if ("NEW".equals(paymentMethod)) {
				result = aliPayService.refundNew(map);
			} else {
				 result=aliPayService.refund(map);
			}
			return result;
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
}