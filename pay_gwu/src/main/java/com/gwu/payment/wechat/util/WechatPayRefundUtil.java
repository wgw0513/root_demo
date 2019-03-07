package com.gwu.payment.wechat.util;

import java.util.SortedMap;
import java.util.TreeMap;

import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.http.ClientCustomSSL;
import com.gwu.payment.wechat.po.WechatRefundDto;

/** 
 * @author: gwu
 * @version:
 * qwy.wx.wxpay.util.WXPayRefundUtil.java
 * @Desc  微信退款 申请以及退款
 * //api地址：http://mch.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
 */
public class WechatPayRefundUtil {
	/**
	 * @date 201-04-13
	 * @Des:退款
	 */
	public static String wechatRefund(WechatRefundDto ref) {
		// 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
		String out_refund_no =UuidUtil.get32UUID();
		String out_trade_no =ref.getOut_trade_no();// 商户侧传给微信的订单号
		String total_fee = PayUtil.getMoney(ref.getTotal_fee());// 总金额
		String refund_fee = PayUtil.getMoney(ref.getRefund_fee());// 退款金额
		String nonce_str = getNonceStr();// 随机字符串
		String op_user_id=ref.getOp_user_id();//操作员id
		
		String  transaction_id=ref.getTransaction_id();
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", WechatConfig.appid);
		packageParams.put("mch_id", WechatConfig.parter);
		packageParams.put("nonce_str", nonce_str);
		//packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("out_refund_no", out_refund_no);
		packageParams.put("total_fee", total_fee);
		packageParams.put("refund_fee", refund_fee);
		packageParams.put("op_user_id", ref.getOp_user_id());
		packageParams.put("transaction_id", transaction_id);

		RequestHandler reqHandler = new RequestHandler(
				null, null);
		reqHandler.init(WechatConfig.appid, WechatConfig.appsecret, WechatConfig.partenterkey);
		
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + 
				 "<appid>" + WechatConfig.appid + "</appid>" +
				 "<mch_id>"+  WechatConfig.parter + "</mch_id>" + 
				 "<nonce_str>" + nonce_str+ "</nonce_str>" + 
				 "<sign><![CDATA[" + sign + "]]></sign>"+
				 "<out_refund_no>" + out_refund_no + "</out_refund_no>"+
				 "<total_fee>" + total_fee + "</total_fee>"+ 
				 "<refund_fee>" + refund_fee + "</refund_fee>"+
				 "<transaction_id>" + transaction_id + "</transaction_id>"+
				 "<op_user_id>" + op_user_id + "</op_user_id>" + 
				 "</xml>";
		
		String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		
		String resultXml="";
		try {
			 resultXml= ClientCustomSSL.doRefund(createOrderURL, xml);
			 System.out.println("resultXml:"+resultXml);
			
			//改变支付数据库中的是否退款
			//新增退款数据库数据
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultXml;
	}
	
	/**
	 * @date 201-04-13
	 * @Des:退款查询
	 */
	public static String querywechatRefund(String oderId,String transaction_id) {
		// 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
		String nonce_str = getNonceStr();// 随机字符串
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", WechatConfig.appid);
		packageParams.put("mch_id",  WechatConfig.parter);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("transaction_id", transaction_id);
		RequestHandler reqHandler = new RequestHandler(
				null, null);
		reqHandler.init(WechatConfig.appid, WechatConfig.appsecret, WechatConfig.partenterkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + 
				"<appid>" + WechatConfig.appid + "</appid>" + 
				"<mch_id>"+  WechatConfig.parter + "</mch_id>" + 
				"<nonce_str>" + nonce_str+ "</nonce_str>" + 
				"<sign><![CDATA[" + sign + "]]></sign>"+
				"<transaction_id>" + transaction_id + "</transaction_id>"+ 
				 "</xml>";
		
		String createOrderURL ="https://api.mch.weixin.qq.com/pay/refundquery";
		String resultXml="";
		try {
			 resultXml= ClientCustomSSL.doRefund(createOrderURL, xml);
			System.out.println("s:"+resultXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultXml;
	}
	
	
	/**
	 * 获取随机传
	 * @return
	 */
	public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(2) + "";
		// 10位序列号,可以自行调整。
		return currTime + strRandom;
	}

	
	
	public static void main(String[] args) {
		
		
	}
	
	

}