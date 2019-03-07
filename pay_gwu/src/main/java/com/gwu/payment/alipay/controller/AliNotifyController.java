package com.gwu.payment.alipay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.gwu.payment.Constant.PayStatus;
import com.gwu.payment.Constant.PayType;
import com.gwu.payment.alipay.config.AlipayConfig;
import com.gwu.payment.allinpay.util.SybConstants;
import com.gwu.payment.allinpay.util.SybUtil;
import com.gwu.payment.database.entity.XftPayment;
import com.gwu.payment.service.XftPaymentService;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.util.HttpClientUtils;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0 类功能说明：支付宝服务器异步通知 controller
 *          *************************页面功能说明*************************
 *          创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
 *          如果没有收到该页面返回的 success 建议该页面只做支付成功的业务逻辑处理，退款的处理请以调用退款查询接口的结果为准。
 */
 @Controller
 @RequestMapping(value = "/ali/payment/return")
public  class AliNotifyController {
		
	private static final Logger logger = LoggerFactory.getLogger(AliNotifyController.class);
 	@Autowired
	XftPaymentService xftPaymentService;
 	
	@Value("${PAYMENT_METHOD}")
	private String paymentMethod;//新支付开关  NEW为开启
	
	

	/**
	 * 动态遍历获取所有收到的参数,此步非常关键,因为收银宝以后可能会加字段,动态获取可以兼容由于收银宝加字段而引起的签名异常
	 * @param request
	 * @return
	 */
	private TreeMap<String, String> getParams(HttpServletRequest request){
		TreeMap<String, String> map = new TreeMap<String, String>();
		Map reqMap = request.getParameterMap();
		for(Object key:reqMap.keySet()){
			String value = ((String[])reqMap.get(key))[0];
			map.put(key.toString(),value);
		}
		return map;
	}
	
	/***
	 * 
	 * @param request
	 * @param response
	 * @return 支付宝支付成功回调select
	 * @throws IOException
	 * 每当交易状态改变时，服务器异步通知页面就会收到支付宝发来的处理结果通知，程序执行完后必须打印输出success。
	 * 如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。
	 * 一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）。 
	 *其中，若退款期限是3个月可退款，支付成功后，支付宝会发送trade_success的支付成功状态的异步通知，
	 *在3个月后支付宝会再次发送trade_finished的交易结束状态的异步通知，表示交易结束不允许退款。 
	 */
	 @RequestMapping(value = "/notify")
	public String notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取支付宝POST过来反馈信息
		 	String inputLine;
			String notityXml = "";
			String resXml = "";
		try {
			if ("NEW".equals(paymentMethod)) {
				
				logger.info("zhifubao zhifu  back=======new^^=@@@=======================");
				//新支付通知
				try {
					logger.info("接收到通知");
					request.setCharacterEncoding("gbk");//通知传输的编码为GBK
					response.setCharacterEncoding("gbk");
					TreeMap<String,String> params = getParams(request);//动态遍历获取所有收到的参数,此步非常关键,因为收银宝以后可能会加字段,动态获取可以兼容
					logger.info("返回参数： " + JSONUtils.beanToJson(params));
					boolean isSign = SybUtil.validSign(params, SybConstants.SYB_APPKEY);// 接受到推送通知,首先验签
					logger.info("验签结果:"+isSign);
					//验签完毕进行业务处理
					if (isSign) {
						//处理业务逻辑
						//支付宝订单号
						String orderArr=params.get("cusorderid");
						String  payType=orderArr.substring(0, 2);
						String  orderID=orderArr.substring(2, orderArr.length());
						////////// 执行自己的业务逻辑////////////////
						logger.info("####orderID: " + orderID + "####payType : " + payType);
						XftPayment xft= xftPaymentService.selectByNo(orderID,payType);
						xft.setReturntime(new Date());
						xft.setStatus(PayStatus.PAID);
						xft.setPtType(PayType.ZFBZF);
						xft.setTransactionId(params.get("trxid"));
						xftPaymentService.updatePayment(xft);
						Map<String,String> map=new HashMap<String ,String>();
						map.put("transactionId",params.get("trxid"));
						map.put("orderId", xft.getPayNo());
						map.put("status", PayStatus.PAID);
						map.put("payType", PayType.ZFBZF);
						map.put("msg", "success");
						map.put("payAmount", xft.getPayAmount().toString());
						logger.info("pagrams map:**********************" + JSON.toJSONString(map));
						logger.info("xft:**********************" + JSON.toJSONString(xft));
						HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), request, map);
						// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
						resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
								+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
						
					} else {
						logger.error("验签不通过！");
						resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
								+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
					}
				} catch (Exception e) {//处理异常
					// TODO: handle exception
					e.printStackTrace();
				}
				finally{//收到通知,返回success
					try {
						response.getOutputStream().write("success".getBytes());
						response.flushBuffer();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {				
			logger.info("pagrams signVerified:**********************:进入支付宝回调！" );
			PrintWriter out = response.getWriter();
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			/*
			 * 实际验证过程建议商户务必添加以下校验： 1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
			 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）， 3、校验通知中的seller_id（或者seller_email)
			 * 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
			 * 4、验证app_id是否为该商户本身。
			 */
			boolean  signVerified= AlipaySignature.rsaCheckV1(params, AlipayConfig.alipaypublickey,
					AlipayConfig.charset, AlipayConfig.signtype); // 调用SDK验证签名
				logger.info("pagrams signVerified:**********************" + signVerified);
			if(signVerified) {
					//TRADE_SUCCESS
					String tradeStatus =params.get("trade_status");
					// 商户订单号
					String outTradeNo =params.get("out_trade_no");
					String orderArr[]=outTradeNo.split("-");
					String  payType=orderArr[0].substring(0, 2);
					String  orderID=orderArr[0].substring(2, orderArr[0].length());
					String tradeNo =params.get("trade_no");
					////////// 执行自己的业务逻辑////////////////
					// 支付宝交易号
				if("TRADE_SUCCESS".equals(tradeStatus)) {
					////////// 执行自己的业务逻辑////////////////
					XftPayment xft= xftPaymentService.selectByNo(orderID, payType);
					xft.setReturntime(new Date());
					xft.setStatus(PayStatus.PAID);
					xft.setPtType(PayType.ZFBZF);
					xft.setTransactionId(tradeNo);
					xftPaymentService.updatePayment(xft);
					Map<String,String> map=new HashMap<String ,String>();
					map.put("transactionId", tradeNo);
					map.put("orderId", xft.getPayNo());
					map.put("payType", PayType.ZFBZF);
					map.put("status", PayStatus.PAID);
					map.put("msg", "success");
					map.put("payAmount", xft.getPayAmount().toString());
					logger.info("pagrams map:**********************" + JSON.toJSONString(map));
					logger.info("xft:**********************" + JSON.toJSONString(xft));
					HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), request, map);
					out.println("success");		
				}
				
				
				
					
		}
			
			}	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.info("Exception e:**********************" + e.getMessage());
		}
		
	
		return null;
	}

		//支付成功返回页面
		@RequestMapping(value = "/success")
	public ModelAndView  success(HttpServletRequest req, HttpServletResponse resp) {
			////////////////////////////////////请求参数//////////////////////////////////////
		//	Map<String,Object> map=new HashMap<String,Object>();
			ModelAndView mv=new ModelAndView();
			mv.setViewName("/alipay/success.html");
			return mv;
			
		}

}
