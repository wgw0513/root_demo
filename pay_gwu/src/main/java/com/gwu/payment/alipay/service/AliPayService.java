package com.gwu.payment.alipay.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.gwu.payment.Constant.PayStatus;
import com.gwu.payment.Constant.PayType;
import com.gwu.payment.alipay.config.AliConfigProcess;
import com.gwu.payment.alipay.config.AlipayConfig;
import com.gwu.payment.allinpay.constants.Constant;
import com.gwu.payment.allinpay.model.PayEntity;
import com.gwu.payment.allinpay.service.SybPayService;
import com.gwu.payment.allinpay.util.SybConstants;
import com.gwu.payment.allinpay.util.SybUtil;
import com.gwu.payment.database.entity.RouteOrder;
import com.gwu.payment.database.entity.XftPayment;
import com.gwu.payment.service.XftPaymentService;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.service.WechatNativPayService;
import com.gwu.payment.wechat.util.HttpClientUtils;
import com.gwu.payment.wechat.util.PayUtil;
import com.gwu.payment.wechat.util.Tools;
import com.gwu.payment.wechat.util.UuidUtil;



/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-11 10:17:16
 * @version 1.0.0 类功能说明：支付宝支付 Service
 * 
 */
@Service
public class AliPayService {
	private static final Logger logger = LoggerFactory.getLogger(WechatNativPayService.class);
	@Autowired
	XftPaymentService xftPaymentService;
	@Autowired
	WechatNativPayService  wechatNativPayService;
	@Autowired
	SybPayService  sybPayService;
	
	/**
	 * @author gwu
	 * @param orderID
	 *            订单号  必填
	 * @param subject
	 *            订单名称  必填
	 * @param totalFee
	 *            支付金额  必填
	 * @param body
	 *            订单描述     可为空
	 * @return 支付宝电脑端支付接口
	 */
	public String aliPCPay(Map<String ,Object> map) {
		//请求
		try {
			//判断是否是重复支付
			//保存支付表 
			XftPayment xft=xftPaymentService.selectByNo(map.get("orderID").toString(),map.get("payType").toString());
			
			if(Tools.isEmpty(xft)) {
				savePay(map.get("payType").toString(),map.get("orderID").toString(), map.get("totalFee").toString(),map.get("noticeURL").toString());
			}
			else {
				//判断微信
				String wxStr=wechatNativPayService.isPayOverAlready(map.get("orderID").toString(),map.get("payType").toString());
				Map<String, Object> wxMap=JSONUtils.jsonToMap(wxStr);
				//判断支付宝
				String zfbStr =searchOrderByOrderID(map.get("orderID").toString(),map.get("payType").toString());
				Map<String, Object> zfbMap=JSONUtils.jsonToMap(zfbStr);
				
				logger.info("==============scode=电脑端=pagrams wxStr:**********************" + JSON.toJSONString(wxStr));
				logger.info("==============scode==电脑端 ====pagrams zfbMap:**********************" + JSON.toJSONString(zfbMap));
				
				if(null!=wxMap&&"SUCCESS".equals(wxMap.get("code"))) {
					//查下xft的状态是否有修改
					xft.setReturntime(new Date());
					xft.setStatus(PayStatus.PAID);
					xft.setPtType(PayType.WECHATZF);
					xft.setTransactionId(wxMap.get("transactionId").toString());
					xftPaymentService.updatePayment(xft);
					Map<String,String> returnMap=new HashMap<String ,String>();
					returnMap.put("transactionId", wxMap.get("transactionId").toString());
					returnMap.put("orderId", xft.getPayNo());
					returnMap.put("status", PayStatus.PAID);
					returnMap.put("payType", PayType.WECHATZF);
					returnMap.put("msg", "success");
					
					logger.info("wechat-------pagrams map:**********************" + JSON.toJSONString(returnMap));
					HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), null, returnMap);
					//mv.setViewName("/alipay/success.html");
					
					return "{\"code\":\"5050\",\"msg\":\""+map.get("payType").toString()+map.get("orderID").toString()+"该订单已支付成功，请勿重复支付！\"}";
					
				//支付宝判断	
				}else if(null!=zfbMap&&"10000".equals(zfbMap.get("code"))&&"TRADE_SUCCESS".equals(zfbMap.get("trade_status"))) {
					xft.setReturntime(new Date());
					xft.setStatus(PayStatus.PAID);
					xft.setPtType(PayType.ZFBZF);
					xft.setTransactionId(zfbMap.get("trade_no").toString());
					xftPaymentService.updatePayment(xft);
					Map<String,String> returnZfbMap=new HashMap<String ,String>();
					returnZfbMap.put("transactionId", zfbMap.get("trade_no").toString());
					returnZfbMap.put("orderId", xft.getPayNo());
					returnZfbMap.put("payType", PayType.ZFBZF);
					returnZfbMap.put("status", PayStatus.PAID);
					returnZfbMap.put("msg", "success");
					logger.info("zhifubao-------pagrams map:**********************" + JSON.toJSONString(returnZfbMap));
					HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), null, returnZfbMap);
					
					return "{\"code\":\"5050\",\"msg\":\""+map.get("payType").toString()+map.get("orderID").toString()+"该订单已支付成功，请勿重复支付！\"}";
				}
					
			}
			
			//获得初始化的AlipayClient
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, 
					AlipayConfig.appid, AlipayConfig.merchantprivatekey, "json", 
					AlipayConfig.charset, AlipayConfig.alipaypublickey, AlipayConfig.signtype);
			//设置请求参数
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
			alipayRequest.setReturnUrl(AlipayConfig.returnurl);
			alipayRequest.setNotifyUrl(AlipayConfig.notifyurl);
				//商户订单号，商户网站订单系统中唯一订单号，必填
				alipayRequest.setBizContent("{\"out_trade_no\":\""+ map.get("payType").toString()+map.get("orderID").toString() +"\"," 
						+ "\"total_amount\":\""+ map.get("totalFee").toString() +"\"," 
						+ "\"subject\":\""+ map.get("body").toString() +"\"," 
						+ "\"body\":\""+ map.get("body").toString() +"\"," 
						+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
				
					String result = alipayClient.pageExecute(alipayRequest).getBody();
					
					return JSONUtils.stringToJsonByFastjson("sHtmlText", result);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		return null;

	}
	
	/**
	 * @author gwu
	 * @param orderID
	 *            订单号  必填
	 * @param subject
	 *            订单名称  必填
	 * @param totalFee
	 *            支付金额  必填
	 * @param body
	 *            订单描述     可为空
	 * @return 新第三方支付宝扫码支付
	 */
	public String aliPCNewPay(Map<String ,Object> map) {
		String ewm="";
		//请求
		try {
			//判断是否是重复支付
			//获取时时金额
			String totalFeethis="";
			//判断是线路订单则拿订单的金额
			if("WR".equals(map.get("payType").toString())){
				RouteOrder  ro=xftPaymentService.selectRouteOrderByPaymentNo(map.get("orderID").toString());
				totalFeethis=new BigDecimal(ro.getDiscountAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
				//判断是否已经支付
				if(ro.getStatus().equals("01")){
					
					return "{\"code\":\"5050\",\"msg\":\""+map.get("payType").toString()+map.get("orderID").toString()+"该订单已支付成功，请勿重复支付！\"}";
				}
			}else {
				
			    totalFeethis=new BigDecimal(map.get("totalFee").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
			}
			XftPayment xft=xftPaymentService.selectByNo(map.get("orderID").toString(),map.get("payType").toString());
			//判断是否是重复支付
			if(Tools.isEmpty(xft)) {
				savePay(map.get("payType").toString(),map.get("orderID").toString(), map.get("totalFee").toString(),map.get("noticeURL").toString());
			}else {
				XftPayment xftnew=new  XftPayment();
				xftnew.setId(xft.getId());
				xftnew.setPayAmount(new BigDecimal(totalFeethis));
				xftPaymentService.updatePayment(xftnew);
			}				
			
			System.out.println(totalFeethis+":金额$$$$$$$$$$$");
			PayEntity record = new PayEntity();
			record.setTrxamt(PayUtil.getMoney(totalFeethis));
			record.setReqsn(map.get("payType").toString()+map.get("orderID").toString());
			record.setPaytype(Constant.payType.A01);//支付宝扫码支付
			record.setBody(map.get("body").toString());
			record.setRandomstr(SybUtil.getValidatecode(8));
			record.setRemark("支付宝扫码支付");
			record.setAcct("");
			record.setAuthcode("");
			record.setNotify_url(AlipayConfig.notifyurl);
			record.setLimit_pay("");
			record.setIdno("");
			record.setTruename("");
			record.setAsinfo("");
			Map<String,String> mapNew=sybPayService.pay(record);
			System.out.println(JSON.toJSONString(mapNew) +"%%%%%%%%%%%%%%%");
			Map<String, Object> resultMap=com.gwu.payment.util.JSONUtils.jsonToMap(mapNew.get("result")); 
			if(resultMap.get("retcode")!=null&&resultMap.get("retcode").equals("SUCCESS")) {
				ewm=JSONUtils.stringToJsonByFastjson("ewm", resultMap.get("payinfo").toString());	
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ewm;
		
	}
	
	/**
	 * 
	 * @param orderID 订单号
	 * @param subject 订单名称
	 * @param totalFee 订单金额
	 * @param body 订单描述
	 * @param noticeURL 通知url
	 * @param payType 订单类型
	 * @return手机h5页面支付
	 * @throws AlipayApiException
	 */
	public String mobliePay(String orderID, String subject, String totalFee, String body, String noticeURL,String payType) throws AlipayApiException {
		try {	
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, 
				AlipayConfig.appid, AlipayConfig.merchantprivatekey, "json", 
				AlipayConfig.charset, AlipayConfig.alipaypublickey, AlipayConfig.signtype);
			    AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
			    alipayRequest.setReturnUrl(AlipayConfig.returnurl);
				alipayRequest.setNotifyUrl(AlipayConfig.notifyurl);
				
				XftPayment xft=xftPaymentService.selectByNo(orderID,payType);
				//获取时时金额
				String totalFeethis="";
				//判断是线路订单则拿订单的金额
				if("WR".equals(payType)){
					RouteOrder  ro=xftPaymentService.selectRouteOrderByPaymentNo(orderID);
					totalFeethis=new BigDecimal(ro.getDiscountAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
					//判断是否已经支付
					if(ro.getStatus().equals("01")){
						
						return "{\"code\":\"5050\",\"msg\":\""+payType+orderID+"该订单已支付成功，请勿重复支付！\"}";
					}
				
				}else {
				    totalFeethis=new BigDecimal(totalFee).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
				}
				
				if(Tools.isEmpty(xft)) {
					
					savePay(payType,orderID, totalFeethis,noticeURL);
				}else {
					XftPayment xftnew=new  XftPayment();
					xftnew.setId(xft.getId());
					xftnew.setPayAmount(new BigDecimal(totalFeethis));
					xftPaymentService.updatePayment(xftnew);
				}				
			    alipayRequest.setBizContent("{\"out_trade_no\":\""+ payType+orderID +"\"," 
						+ "\"total_amount\":\""+ totalFeethis +"\"," 
						+ "\"subject\":\""+ subject +"\"," 
						+ "\"body\":\""+ body +"\"," 
						+ "\"product_code\":\"QUICK_WAP_WAY\"" +
			    		"}");//填充业务参数
			    
			    String result = alipayClient.pageExecute(alipayRequest).getBody().replace("<script>document.forms[0].submit();</script>", "");//调用SDK生成表单
				
				return JSONUtils.stringToJsonByFastjson("sHtml5Text", result);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	/**
	 * 
	 * @param orderID 订单号
	 * @param subject 订单名称
	 * @param totalFee 订单金额
	 * @param body 订单描述
	 * @param noticeURL 通知url
	 * @param payType 订单类型
	 * @throws Exception 
	 * @return  第三方支付手机h5页面支付
	 */
	public String moblieNEWPay(String orderID, String subject, String totalFee, String body, String noticeURL,String payType) throws Exception {
				XftPayment xft=xftPaymentService.selectByNo(orderID,payType);
				//获取时时金额
				String totalFeethis="";
		try {	
				//判断是线路订单则拿订单的金额
				if("WR".equals(payType)){
					RouteOrder  ro=xftPaymentService.selectRouteOrderByPaymentNo(orderID);
					totalFeethis=new BigDecimal(ro.getDiscountAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
					//判断是否已经支付
					if(ro.getStatus().equals("01")){
						return "{\"code\":\"5050\",\"msg\":\""+payType+orderID+"该订单已支付成功，请勿重复支付！\"}";
					}
				
				}else {
				    totalFeethis=new BigDecimal(totalFee).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
				}
				
				if(Tools.isEmpty(xft)) {
					
					savePay(payType,orderID, totalFeethis,noticeURL);
				}else {
					XftPayment xftnew=new  XftPayment();
					xftnew.setId(xft.getId());
					xftnew.setPayAmount(new BigDecimal(totalFeethis));
					xftPaymentService.updatePayment(xftnew);
				}	
				
				PayEntity record = new PayEntity();
    			record.setTrxamt(PayUtil.getMoney(totalFeethis));
    			record.setReqsn(payType + orderID);
    			record.setPaytype(Constant.payType.A01);//支付宝扫描js支付
    			record.setBody(body);
    			record.setRemark("支付宝js支付");
    			record.setAcct("");
    			record.setAuthcode("");
    			record.setNotify_url(AlipayConfig.notifyurl);
    			record.setLimit_pay("");
    			record.setIdno("");
    			record.setTruename("");
    			record.setAsinfo("");
    			Map<String,String> mapNew=sybPayService.pay(record);
    			Map<String, Object> resultMap=com.gwu.payment.util.JSONUtils.jsonToMap(mapNew.get("result")); 
    			
    			System.out.println(JSON.toJSONString(mapNew) +"%%%%%%%%%%%%%%%");
    			if(resultMap.get("retcode")!=null&&resultMap.get("retcode").equals("SUCCESS")) {
    				return JSONUtils.stringToJsonByFastjson("sHtml5Text", resultMap.get("payinfo").toString());	
    			}
    			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
				return null;
	}


	
	
	/**
	 * 
	 * @param orderID 系统订单号
	 * @param payType 系统订单类型
	 * @return查询支付宝订单付款信息
	 */
	public String  searchOrderByOrderID(String orderID,String payType ){
		//请求
		try {
			//获得初始化的AlipayClient
			AlipayClient queryClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.appid, 
					AlipayConfig.merchantprivatekey, "json", AlipayConfig.charset,
					AlipayConfig.alipaypublickey, AlipayConfig.signtype);
			//设置请求参数
			AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
			XftPayment xft=xftPaymentService.selectByNo(orderID,payType);
			String payNo="";//支付报订单号
			String trade_no=null;//交易单号
			if(!Tools.isEmpty(xft)) {
				payNo=xft.getPaymentNo();
				trade_no=xft.getTransactionId();
			}else {
				
				payNo=payType+orderID;
				
			}
			if(null!=trade_no&&!"".equals(trade_no)) {
				queryRequest.setBizContent("{\"trade_no\":\""+trade_no+"\"}");
			}else {
				queryRequest.setBizContent("{\"out_trade_no\":\""+ payNo +"\"}");
			}
			
			
			AlipayResponse  ac=queryClient.execute(queryRequest);
			Map m= JSONUtils.jsonToMap(ac.getBody()) ;
			return m.get("alipay_trade_query_response").toString();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	

	/***
	 * @author gwu
	 * @param orderID 系统订单号
	 * @param payType 系统订单类型
	 * @param refundFee 退款金额
	 * @param refundReason 退款原因
	 * @return tradeNo支付宝统一交易订单号 跟 orderID二选一，本字段优先
	 */
	public String refund(Map<String ,Object> map) {
				//获得初始化的AlipayClient
		logger.info("======into alirefund========"+map);
				//请求
				try {
					XftPayment xft=xftPaymentService.selectByNo(map.get("orderID").toString(),map.get("payType").toString());
					String trade_no="";//交易单号
					String payNo="";//支付宝交易订单号
					if(!Tools.isEmpty(xft)) {
						 trade_no=xft.getTransactionId();
						 payNo=xft.getPaymentNo();
					}
					AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.appid, 
						AlipayConfig.merchantprivatekey, "json", 
						AlipayConfig.charset, AlipayConfig.alipaypublickey, AlipayConfig.signtype);
					//设置请求参数
					/**
					 * 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
					 */
					String out_request_no=UuidUtil.get32UUID();
					AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
					alipayRequest.setBizContent("{\"out_trade_no\":\""+ payNo +"\"," 
							+ "\"trade_no\":\""+trade_no+"\"," 
							+ "\"refund_amount\":\""+ map.get("refundFee").toString() +"\"," 
							+ "\"refund_reason\":\""+ map.get("refundReason").toString() +"\"," 
							+ "\"out_request_no\":\""+out_request_no+"\"}");
					//返回支付宝代码
					AlipayTradeRefundResponse  ac=alipayClient.execute(alipayRequest);
					String code=ac.getCode();
					String messge=AliConfigProcess.returnCode.get(code);
					logger.info("======into alirefund########========"+out_request_no );
					logger.info("======into alirefund========"+code +"==================="+messge);
					//更改支付表状态
					if(code.equals("1000")) {
						xft.setStatus(PayStatus.YTK);
						xftPaymentService.updatePayment(xft);
					}
					return "{\"code\":\""+code +"\",\"refno\":\""+out_request_no+"\", "+"\"msg\":\""+messge+"\"}"; 
				} catch (AlipayApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "{\"code\":\"error\","+"\"msg\":\""+e.getMessage()+"\"}"; 	
					
				}
	}
	

	/**
	 * 退款--newpay
	 * retcode  SUCCESS
	 * trxstatus  3008商户余额不足
	 * @param map
	 * @return
	 */
	public String refundNew(Map<String ,Object> map) {
		try {
			XftPayment xft=xftPaymentService.selectByNo(map.get("orderID").toString(),map.get("payType").toString());
			String payNo="";//支付报订单号
			String trade_no="";//交易单号
			if(!Tools.isEmpty(xft)) {
				payNo=xft.getPaymentNo();
				trade_no=xft.getTransactionId();
			}
			String trxamt = PayUtil.getMoney(map.get("refundFee").toString());//申请退款金额
			String reqsn = UuidUtil.get32UUID();//退款单号;
			String oldtrxid = trade_no;//平台流水号
			String oldreqsn = map.get("payType").toString() + map.get("orderID").toString();//商户订单号
			Map<String, Object> refundReturnMap = sybPayService.refund(trxamt, reqsn, oldtrxid, oldreqsn);
			String code="";
			if (null != refundReturnMap && "SUCCESS".equals(refundReturnMap.get("retcode")) ) {
					if ("0000".equals(refundReturnMap.get("trxstatus"))) {
						//退款成功
						code="10000";
						xft.setStatus(PayStatus.YTK);
						xftPaymentService.updatePayment(xft);	
						return "{\"code\":\""+code+"\",\"refno\":\""+refundReturnMap.get("trxid").toString()+"\", "+"\"msg\":\""+refundReturnMap.get("retcode").toString()+"\"}";
						
					} else {
						return "{\"code\":\""+refundReturnMap.get("trxstatus").toString()+"\","+"\"msg\":\""+refundReturnMap.get("retmsg").toString()+"\"}";
					}
			} else {
				return "{\"code\":\""+refundReturnMap.get("retcode").toString()+"\","+"\"msg\":\""+refundReturnMap.get("retmsg").toString()+"\"}";
			}
			
			}catch (Exception e) {
				// TODO: handle exception
				logger.error(e.getMessage());
				e.printStackTrace();
				return "{\"code\":\"error\","+"\"msg\":\""+e.getMessage()+"\"}"; 	
			}
		
		}
	
	
	/***
	 * 
	 * @param payType
	 * @param orderID
	 * @param totalFee
	 * 保存支付表
	 */
	public void savePay(String payType,String orderID,String totalFee,String noticeURL) {
		logger.info("执行保存支付表========");
		try {
		XftPayment xtp=new XftPayment();
		xtp.setCreatetime(new Date());
		xtp.setPayType(payType);
		xtp.setPayNo(orderID);
		xtp.setPaymentNo(payType+orderID);
		xtp.setPayAmount(new BigDecimal(totalFee));
		xtp.setNotifyURL(noticeURL);
		xtp.setStatus(PayStatus.UNPAY);
		xtp.setFlag(0);
		xtp.setPtType(PayType.ZFBZF);
		xftPaymentService.save(xtp);
		}catch (Exception e) {
			// TODO: handle exception
			logger.info("=============Exception:"+e.getMessage());
			e.printStackTrace();
		}
	}
	

}
