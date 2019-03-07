package com.gwu.payment.wechat.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.gwu.payment.Constant.PayStatus;
import com.gwu.payment.Constant.PayType;
import com.gwu.payment.alipay.service.AliPayService;
import com.gwu.payment.allinpay.constants.Constant;
import com.gwu.payment.allinpay.model.PayEntity;
import com.gwu.payment.allinpay.service.SybPayService;
import com.gwu.payment.database.entity.PaymentLog;
import com.gwu.payment.database.entity.RouteOrder;
import com.gwu.payment.database.entity.XftPayment;
import com.gwu.payment.database.mapper.PaymentLogMapper;
import com.gwu.payment.service.XftPaymentService;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.po.WechatPayDto;
import com.gwu.payment.wechat.po.WechatRefundDto;
import com.gwu.payment.wechat.util.HttpClientUtils;
import com.gwu.payment.wechat.util.PayUtil;
import com.gwu.payment.wechat.util.Tools;
import com.gwu.payment.wechat.util.UuidUtil;
import com.gwu.payment.wechat.util.WechatPayRefundUtil;
/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-10 16:17:16
 * @version 1.0.0 类功能说明：微信扫码支付 Service 
 * 
 */
@Service
public class WechatNativPayService {
	private static final Logger logger = LoggerFactory.getLogger(WechatNativPayService.class);
	@Autowired
	XftPaymentService xftPaymentService;
	
	@Autowired
	PaymentLogMapper paymentLogMapper;
	@Autowired
	AliPayService  aliPayService;
	@Autowired
	SybPayService sybPayService;
	/**
	 * 
	 * @param body 付款描述
	 * @param orderID 订单号
	 * @param Ip 客户端IP
	 * @param totalFee 金额
	 * @return 微信扫码返回支付连接
	 */
	public String  returmPayUrl(HttpServletRequest req) {
		String ewm="";
		try {
			Map<String,Object> map=Tools.getData(req);
			String  wechatCode=map.get("payType").toString()+map.get("orderID").toString();
			XftPayment xft=xftPaymentService.selectByNo(map.get("orderID").toString(),map.get("payType").toString());
		     String  beforeJson=JSON.toJSONString(xft);
		     String  afterJSON="";
			if(Tools.isEmpty(xft)) {
				savePay(map.get("payType").toString(), map.get("orderID").toString(), map.get("totalFee").toString(),
					map.get("noticeURL").toString());
			}else {
				//判断微信
				String wxStr=isPayOverAlready(map.get("orderID").toString(),map.get("payType").toString());
				Map<String, Object> wxMap=JSONUtils.jsonToMap(wxStr);
				//判断支付宝
				String zfbStr =aliPayService.searchOrderByOrderID(map.get("orderID").toString(),map.get("payType").toString());
				Map<String, Object> zfbMap=JSONUtils.jsonToMap(zfbStr);
				logger.info("==============scode==pagrams wxStr:**********************" + JSON.toJSONString(wxStr));
				logger.info("==============scode==pagrams zfbMap:**********************" + JSON.toJSONString(zfbMap));
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
					returnMap.put("payAmount", xft.getPayAmount().toString());
					logger.info("wechat-------pagrams map:**********************" + JSON.toJSONString(returnMap));
					HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), req, returnMap);
					//mv.setViewName("/alipay/success.html");
					
					return "该订单微信已支付请勿重复支付！！！";
					
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
					returnZfbMap.put("payAmount", xft.getPayAmount().toString());
					logger.info("zhifubao-------pagrams map:**********************" + JSON.toJSONString(returnZfbMap));
					HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), req, returnZfbMap);
					//mv.setViewName("/alipay/success.html");
					return "该订单支付宝已支付请勿重复支付！！！";
				}
				else {
					wechatCode=map.get("payType").toString()+map.get("orderID").toString();
					xft.setPaymentNo(wechatCode);
					xft.setPayAmount(new BigDecimal(map.get("totalFee").toString()).setScale(2, BigDecimal.ROUND_HALF_UP) );
					xftPaymentService.updatePayment(xft);
					//修改后数据
					 afterJSON=JSON.toJSONString(xft);
					 //记录日志
					 savePaymentLog(beforeJson ,afterJSON,xft.getPaymentNo()); 
				}
			}
			WechatPayDto tpWxPay = new WechatPayDto();
			tpWxPay.setBody(map.get("body").toString());//
			tpWxPay.setOrderId(wechatCode);
			tpWxPay.setSpbillCreateIp(map.get("ip").toString());
			tpWxPay.setTotalFee(map.get("totalFee").toString());
			ewm=JSONUtils.stringToJsonByFastjson("ewm", PayUtil.getCodeurl(tpWxPay));	
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
			
		}
		return ewm; 
	}	
	
	/**
	 * 
	 * @param body 付款描述
	 * @param orderID 订单号
	 * @param Ip 客户端IP
	 * @param totalFee 金额
	 * @return 第三方微信扫码返回支付连接
	 */
	public String  returmNewPayUrl(HttpServletRequest req) {
		String ewm="";
		try {
			Map<String,Object> map=Tools.getData(req);
			String  wechatCode=map.get("payType").toString()+map.get("orderID").toString();
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
			Map<String, String> mapNew = new HashMap<String, String>();
			PayEntity record = new PayEntity();
			record.setTrxamt(PayUtil.getMoney(totalFeethis));
			record.setReqsn(wechatCode);
			record.setPaytype(Constant.payType.W01);//微信扫码支付
			record.setBody(map.get("body").toString());
			record.setRemark("微信扫码支付");
			record.setAcct("");
			record.setAuthcode("");
			//支付成功回调地址
			record.setNotify_url(WechatConfig.notifyurl);
			record.setLimit_pay("");
			record.setIdno("");
			record.setTruename("");
			record.setAsinfo("");
			logger.info("pay pagrams : " + JSON.toJSONString(record));
			mapNew = sybPayService.pay(record);
			Map<String, Object> resultMap=com.gwu.payment.util.JSONUtils.jsonToMap(mapNew.get("result")); 
			if(resultMap.get("retcode")!=null&&resultMap.get("retcode").equals("SUCCESS")) {
				ewm=JSONUtils.stringToJsonByFastjson("ewm", resultMap.get("payinfo").toString());	
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
			
		}
		return ewm; 
	}	
	
	
	
	
	
	
	/**
	 * @param req 请求
	 * @param openID 微信ID
	 * @param body 付款描述
	 * @param orderID 订单号
	 * @param Ip 客户端IP
	 * @param totalFee 金额
	 * @return 微信js支付连接
	 */
	public String  jsApiPay(String orderID,String openid,String  totalFee,String ip,String body,String notifyURL,String payType) {
		//微信支付jsApi
		String packageData="";
		try {	//微信订单号
			 	String wxorder="";
				XftPayment xft=xftPaymentService.selectByNo(orderID,payType);
				//判断是线路订单则拿订单的金额,	//获取时时金额 20181017
				 String totalFeethis="";
				if("WR".equals(payType)){
					RouteOrder  ro=xftPaymentService.selectRouteOrderByPaymentNo(orderID);
					totalFeethis=new BigDecimal(ro.getDiscountAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
					//判断是否已经支付
					if(ro.getStatus().equals("01")){
						//System.out.println("=====================该订单已支付成功，请勿重复支付！");	
						return "{\"code\":\"5050\",\"msg\":\""+payType+orderID+"该订单已支付成功，请勿重复支付！\"}";
						
					}
				}else {
				    totalFeethis= new BigDecimal(totalFee).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
				}
				
				if(Tools.isEmpty(xft)) {
					wxorder=payType+orderID;
				    
					savePay(payType,orderID, totalFeethis,notifyURL);
				}else {
					XftPayment xftnew=new  XftPayment();
					//重新生成微信单号
					wxorder=payType+orderID;
					xftnew.setId(xft.getId());
					xftnew.setPaymentNo(wxorder);
					xftnew.setPayAmount(new BigDecimal(totalFeethis));
					xftPaymentService.updatePayment(xftnew);
				}
				WechatPayDto tpWxPay = new WechatPayDto();
				tpWxPay.setTotalFee(totalFeethis);
				tpWxPay.setOpenId(openid);//"olTM3wcxWcri2b9-rD73Kbn_TQ7M"
				tpWxPay.setBody(body);
				tpWxPay.setOrderId(wxorder);
				tpWxPay.setSpbillCreateIp(ip);
				packageData=JSONUtils.stringToJsonByFastjson("packageData", PayUtil.getPackage(tpWxPay));
				//packageData=JSONUtils.stringToJsonByFastjson("packageData", com.gwu.payment.allinpay.util.PayUtil.getPackage(tpWxPay));
				
		   logger.info("##########################" + packageData);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return packageData;
	    
	}
	
	
	
	
	/**
	 * @param req 请求
	 * @param openID 微信ID
	 * @param body 付款描述
	 * @param orderID 订单号
	 * @param Ip 客户端IP
	 * @param totalFee 金额
	 * @return 微信js支付连接
	 */
	public String  jsApiNewPay(String orderID,String openid,String  totalFee,String ip,String body,String notifyURL,String payType) {
		//微信支付jsApi
		String packageData="";
		try {	//微信订单号
			 	String wxorder="";
				XftPayment xft=xftPaymentService.selectByNo(orderID,payType);
				//判断是线路订单则拿订单的金额,	//获取时时金额 20181017
				 String totalFeethis="";
				if("WR".equals(payType)){
					RouteOrder  ro=xftPaymentService.selectRouteOrderByPaymentNo(orderID);
					totalFeethis=new BigDecimal(ro.getDiscountAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
					//判断是否已经支付
					if(ro.getStatus().equals("01")){
						//System.out.println("=====================该订单已支付成功，请勿重复支付！");	
						return "{\"code\":\"5050\",\"msg\":\""+payType+orderID+"该订单已支付成功，请勿重复支付！\"}";
					}
				}else {
					
				    totalFeethis= new BigDecimal(totalFee).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
				}
				
				if(Tools.isEmpty(xft)) {
					wxorder=payType+orderID;
				    
					savePay(payType,orderID, totalFeethis,notifyURL);
				}else {
					XftPayment xftnew=new  XftPayment();
					//重新生成微信单号
					wxorder=payType+orderID;
					xftnew.setId(xft.getId());
					xftnew.setPaymentNo(wxorder);
					xftnew.setPayAmount(new BigDecimal(totalFeethis));
					xftPaymentService.updatePayment(xftnew);
				}
				
				logger.info("get new weixin pay =========================================");
    			PayEntity record = new PayEntity();
    			record.setTrxamt(PayUtil.getMoney(totalFeethis));
    			record.setReqsn(payType + orderID);
    			record.setPaytype(Constant.payType.W02);//微信js支付
    			record.setBody(body);
    			record.setRemark("");
    			record.setAcct(openid);
    			record.setAuthcode("");
    			record.setNotify_url(WechatConfig.notifyurl);
    			record.setLimit_pay("");
    			record.setIdno("");
    			record.setTruename("");
    			record.setAsinfo("");
    			Map<String,String> mapNew=sybPayService.pay(record);
    			Map<String, Object> resultMap=com.gwu.payment.util.JSONUtils.jsonToMap(mapNew.get("result")); 
    			if(resultMap.get("retcode")!=null&&resultMap.get("retcode").equals("SUCCESS")) {
    				return  JSONUtils.stringToJsonByFastjson("packageData",resultMap.get("payinfo").toString()); 
    			}
				
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return packageData;
	    
	}
	
	/**
	 * 
	 * @param orderID
	 * @param payType
	 * @return 根据单号查找是否订单已经完成付款。
	 */
	public String   isPayOverAlready(String orderID ,String payType){
		try {
		XftPayment xft=xftPaymentService.selectByNo(orderID,payType);
		String payNo="";//支付报订单号
		String trade_no="";//交易单号
		if(!Tools.isEmpty(xft)) {
			payNo=xft.getPaymentNo();
			trade_no=xft.getTransactionId();
		}
		String xml=PayUtil.getrOderquery(payNo,trade_no);
		logger.info("search wechat order=================== ::"+JSON.toJSONString(xml));
		Map map = Tools.parseXmlToList2(xml);
		String return_code=map.get("return_code").toString();
			if("SUCCESS".equals(return_code)) {
				String result_code=map.get("result_code").toString();
				//交易状态
				/** 
				  SUCCESS—支付成功
					REFUND—转入退款
					NOTPAY—未支付
					CLOSED—已关闭
					REVOKED—已撤销（刷卡支付）
					USERPAYING--用户支付中
					PAYERROR--支付失败(其他原因，如银行返回失败)
				 */
				if("SUCCESS".equals(result_code)) {
					String trade_state=map.get("trade_state").toString();
					logger.info("search wechat trade_state============================== ::"+trade_state);
					if("SUCCESS".equals(trade_state)) {
						return "{\"code\":\""+ map.get("trade_state").toString() +"\","+"\"msg\":\""+map.get("return_msg").toString()+"\","+"\"transactionId\":\""+map.get("transaction_id").toString()+"\"}"; 
					}else {
						return "{\"code\":\""+ map.get("trade_state").toString() +"\","+"\"msg\":\""+map.get("trade_state").toString()+"\"}";
					}
				}	
			}
		
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return null;
	}

	
	
	/***
	 * @author gwu
	 * @param orderID 订单号
	 * @param totalFee 订单金额
	 * @param refundFee 退款金额
	 * @param transactionID 微信订单号  可以不填 和订单号二选一
	 * @return 退款操作
	 * * 修改说明：当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
	 * 	微信支付将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
	 * 	注意：
	 * 1、交易时间超过一年的订单无法提交退款；
	 * 2、微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。
	 * 总退款金额不能超过用户实际支付金额。 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号。
	 * 退款资金来源	refund_account	否	String(30)	REFUND_SOURCE_RECHARGE_FUNDS	
		仅针对老资金流商户使用
		REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
		REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
	 * 
	 */
	public String refund(Map<String ,Object> map) {
	try {
		XftPayment xft=xftPaymentService.selectByNo(map.get("orderID").toString(),map.get("payType").toString());
		String payNo="";//支付报订单号
		String trade_no="";//交易单号
		if(!Tools.isEmpty(xft)) {
			payNo=xft.getPaymentNo();
			trade_no=xft.getTransactionId();
		}
		WechatRefundDto wd = new WechatRefundDto();
		wd.setOp_user_id(WechatConfig.parter);// 操作人
		wd.setOut_trade_no(payNo);// 订单号
		wd.setTotal_fee(map.get("totalFee").toString());// 订单金额
		wd.setRefund_fee(map.get("refundFee").toString());// 退款金额
		wd.setTransaction_id(trade_no);// 微信订单号
		wd.setRefund_account("REFUND_SOURCE_UNSETTLED_FUNDS");
		wd.setNotify_url(WechatConfig.renotifyurl);
		String result = WechatPayRefundUtil.wechatRefund(wd);
		Map resultMap = Tools.parseXmlToList2(result);
		//判断退款更新状态 err_code
		String code="";
		if(resultMap.get("return_code").toString().equals("SUCCESS")) {
			if(resultMap.get("result_code").toString().equals("SUCCESS")) {
				code="10000";
				xft.setStatus(PayStatus.YTK);
				xftPaymentService.updatePayment(xft);	
				return "{\"code\":\""+code+"\",\"refno\":\""+resultMap.get("out_refund_no").toString()+"\", "+"\"msg\":\""+resultMap.get("return_msg").toString()+"\"}"; 
					
			}else {
				return "{\"code\":\""+resultMap.get("err_code").toString()+"\","+"\"msg\":\""+resultMap.get("err_code_des").toString()+"\"}"; 		
			}
			
		}else {
			return "{\"code\":\""+resultMap.get("return_code").toString()+"\","+"\"msg\":\""+resultMap.get("return_msg").toString()+"\"}"; 	
		}
		
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			e.printStackTrace();
			return "{\"code\":\"error\","+"\"msg\":\""+e.getMessage()+"\"}"; 	
		}
	
	}
	
	/**
	 * 
	 * @param orderID
	 * @param payType
	 * @return 退款查询
	 */
	public String refundQty(String orderID ,String payType) {	
		XftPayment xft=xftPaymentService.selectByNo(orderID,payType);
		String payNo="";//支付报订单号
		String trade_no="";//交易单号
		if(!Tools.isEmpty(xft)) {
			payNo=xft.getPaymentNo();
			trade_no=xft.getTransactionId();
		}
		String result = WechatPayRefundUtil.querywechatRefund(payNo,trade_no);
		return result;
		
	}

	/***
	 * 
	 * @param payType
	 * @param orderID
	 * @param totalFee
	 * 保存支付表
	 */
	public void savePay(String payType,String orderID,String totalFee,String noticeURL) {
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
			xtp.setPtType(PayType.WECHATZF);
			xftPaymentService.save(xtp);
		}catch (Exception e) {
			// TODO: handle exception
			logger.info("=============Exception:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param beforeJson 改变前数据
	 * @param afterJSON 改变后数据
	 * @param pno 单号
	 */
	public  void savePaymentLog(String beforeJson,String afterJSON,String pno) {
		PaymentLog plg=new PaymentLog();
		plg.setDatetime(new Date());
		plg.setBeforContent(beforeJson);
		plg.setAfterContent(afterJSON);
		plg.setOperNo(pno);
		paymentLogMapper.insert(plg);	
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
	
	
}
