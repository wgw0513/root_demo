package com.gwu.payment.wechat.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSON;
import com.gwu.payment.Constant.PayStatus;
import com.gwu.payment.Constant.PayType;
import com.gwu.payment.allinpay.util.SybConstants;
import com.gwu.payment.allinpay.util.SybUtil;
import com.gwu.payment.database.entity.XftPayment;
import com.gwu.payment.service.XftPaymentService;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.util.HttpClientUtils;
import com.gwu.payment.wechat.util.Tools;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0 类功能说明：微信支付回调处理 controller
 * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，
 * 但微信不保证通知最终能成功。
 * （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
 */
@Controller
@RequestMapping(value = "/wechat/payment/return")
@PropertySource({ "classpath:/httpClient.properties" })
public  class WechatNotifyController {
	private static final Logger logger = LoggerFactory.getLogger(WechatNotifyController.class);
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
	 * @return 支付成功回调执行业务方法
	 */
	@RequestMapping(value = "/notify")
	public void notify(HttpServletRequest request, HttpServletResponse response) {
		String inputLine;
		String notityXml = "";
		String resXml = "";
		logger.info("weixin zhifu  back========@@@=======================");
		if ("NEW".equals(paymentMethod)) {
			
			logger.info("weixin zhifu  back=======new^^=@@@=======================");
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
					//微信订单号
					String orderArr=params.get("cusorderid");
					String  payType=orderArr.substring(0, 2);
					String  orderID=orderArr.substring(2, orderArr.length());
					////////// 执行自己的业务逻辑////////////////
					logger.info("####orderID: " + orderID + "####payType : " + payType);
					XftPayment xft= xftPaymentService.selectByNo(orderID,payType);
					xft.setReturntime(new Date());
					xft.setStatus(PayStatus.PAID);
					xft.setPtType(PayType.WECHATZF);
					xft.setTransactionId(params.get("trxid"));
					xftPaymentService.updatePayment(xft);
					Map<String,String> map=new HashMap<String ,String>();
					map.put("transactionId",params.get("trxid"));
					map.put("orderId", xft.getPayNo());
					map.put("status", PayStatus.PAID);
					map.put("payType", PayType.WECHATZF);
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
			//原支付微信通知
			
			try {
				while ((inputLine = request.getReader().readLine()) != null) {
					notityXml += inputLine;
				}
				request.getReader().close();
				Map returnMap = Tools.parseXmlToList2(notityXml);			
				if ("SUCCESS".equals(returnMap.get("result_code").toString())) {
					//微信订单号
					String orderArr[]=returnMap.get("out_trade_no").toString().split("-");
					String  payType=orderArr[0].substring(0, 2);
					String  orderID=orderArr[0].substring(2, orderArr[0].length());
					////////// 执行自己的业务逻辑////////////////
					XftPayment xft= xftPaymentService.selectByNo(orderID,payType);
					xft.setReturntime(new Date());
					xft.setStatus(PayStatus.PAID);
					xft.setPtType(PayType.WECHATZF);
					xft.setTransactionId(returnMap.get("transaction_id").toString());
					xftPaymentService.updatePayment(xft);
					Map<String,String> map=new HashMap<String ,String>();
					map.put("transactionId", returnMap.get("transaction_id").toString());
					map.put("orderId", xft.getPayNo());
					map.put("status", PayStatus.PAID);
					map.put("payType", PayType.WECHATZF);
					map.put("msg", "success");
					map.put("payAmount", xft.getPayAmount().toString());
					logger.info("pagrams map:**********************" + JSON.toJSONString(map));
					logger.info("xft:**********************" + JSON.toJSONString(xft));
					HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), request, map);
					// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
					
				} else {
					resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				}
				
				// ------------------------------
				// 处理业务完毕
				// ------------------------------
				
			} catch (Exception e) {
				logger.error("notityXml contet:"+notityXml,e);
			}
			
			try {
				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(resXml.getBytes());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		String orderArr[]="18092814340337479433- 0149e0".split("-");
		String  payType=orderArr[0].substring(0, 2);
		String  orderID=orderArr[0].substring(2, orderArr[0].length());
		System.out.println(payType);
		System.out.println(orderID);
	}
	/***
	 * 
	 * @param request
	 * @param response
	 * @return 退款成功回调执行方法
	 */
	@RequestMapping(value = "/renotify")
	public void renotify(HttpServletRequest request, HttpServletResponse response) {
		String inputLine;
		String notityXml = "";
		String resXml = "";
	
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			request.getReader().close();
			Map returnMap = Tools.parseXmlToList2(notityXml);	
			if ("SUCCESS".equals(returnMap.get("result_code").toString())) {
				////////// 执行自己的业务逻辑////////////////
				//微信订单号
				String orderArr[]=returnMap.get("out_trade_no").toString().split("-");
				String  payType=orderArr[0].substring(0, 2);
				String  orderID=orderArr[0].substring(2, orderArr[0].length());
				XftPayment xft= xftPaymentService.selectByNo(orderID,payType);
				xft.setReturntime(new Date());
				xft.setStatus(PayStatus.YTK);
				xft.setPtType(PayType.WECHATZF);
				xft.setTransactionId(returnMap.get("transaction_id").toString());
				xftPaymentService.updatePayment(xft);
				Map<String,String> map=new HashMap<String ,String>();
				map.put("transactionId", returnMap.get("transaction_id").toString());
				map.put("orderId", xft.getPayNo());
				map.put("status", PayStatus.YTK);
				map.put("msg", "success");
				logger.info("pagrams map:**********************" + JSON.toJSONString(map));
				logger.info("xft:**********************" + JSON.toJSONString(xft));
				//退款成功
				//	HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), request, map);
				// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		
			} else {
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
				+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}
			// ------------------------------
			// 处理业务完毕
			// ------------------------------
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

