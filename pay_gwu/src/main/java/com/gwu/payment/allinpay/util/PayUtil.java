package com.gwu.payment.allinpay.util;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.po.WechatPayDto;
import com.gwu.payment.wechat.util.GetWeChatOrderNo;
import com.gwu.payment.wechat.util.RequestHandler;
import com.gwu.payment.wechat.util.Sha1Util;
import com.gwu.payment.wechat.util.TenpayUtil;

/**
 * @author hp
 * 
 * 第三方 支付工具类
 */
public class PayUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PayUtil.class);
	private final static String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单地址
	private final static String queryOrder = "https://api.mch.weixin.qq.com/pay/orderquery";//查询地址
	
	
	/**
	 * 获取微信扫码支付二维码连接
	 * 
	 */
	public static String getCodeurl(WechatPayDto tpWxPayDto){
		// 1 参数
		// 附加数据 原样返回
		String attach ="";
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = WechatConfig.notifyurl;
		String trade_type = "NATIVE";
		// 商户号
		String mch_id = WechatConfig.parter;
		// 随机字符串
		String nonce_str = getNonceStr();
		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();
		// 商户订单号
		String out_trade_no = tpWxPayDto.getOrderId();
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", WechatConfig.appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(WechatConfig.appid, WechatConfig.appsecret, WechatConfig.partenterkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + WechatConfig.appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
	
		return   GetWeChatOrderNo.getCodeUrl(createOrderURL, xml);
	}
	
	
	
	
	/**
	 * 获取微信后台订单查询
	 */
	public static String getrOderquery(String orderID,String transaction_id){
		// 商户号
		String mch_id = WechatConfig.parter;
		// 随机字符串
		String nonce_str = getNonceStr();
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", WechatConfig.appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", orderID);
		if(null!=transaction_id&&!"".equals(transaction_id)) {
			packageParams.put("transaction_id", transaction_id);
		}
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(WechatConfig.appid, WechatConfig.appsecret, WechatConfig.partenterkey);
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + WechatConfig.appid + "</appid>" + "<mch_id>"+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str+ "</nonce_str>" + "<sign>" + sign + "</sign>"+ "<out_trade_no>" + orderID+"</out_trade_no>";
				if(null!=transaction_id&&!"".equals(transaction_id)) {
					xml+= "<transaction_id >" + transaction_id+"</transaction_id >";
				}
			xml+="</xml>";
			
			logger.info("search ===========wechat:xml:::"+xml);
		
		return   GetWeChatOrderNo.getWechat(queryOrder, xml);
	}
	
	
	
	/**
	 * 获取请求预支付id报文
	 * @return
	 * @throws Exception 
	 */
	public static String getPackage(WechatPayDto tpWxPayDto) throws Exception {	
		String openId = tpWxPayDto.getOpenId();
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach ="";
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = WechatConfig.notifyurl;
		String trade_type = "JSAPI";
		// ---必须参数
		// 商户号
		String mch_id = SybConstants.SYB_CUSID;
		// 随机字符串
		String nonce_str = getNonceStr();
		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();
		// 商户订单号
		String out_trade_no =orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid",  SybConstants.SYB_APPID);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init( SybConstants.SYB_APPID, SybConstants.SYB_APPKEY, WechatConfig.partenterkey);
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + SybConstants.SYB_APPID + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openId + "</openid>"
				+ "</xml>";
		String createOrderURL = SybConstants.SYB_APIURL + "/pay";
		String prepayID =GetWeChatOrderNo.getPayNo(createOrderURL, xml);//获取到的预支付ID支付
		//获取prepay_id后，拼接最后请求支付所需要的package
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id="+prepayID;
		
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL + "/pay");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("version", "11");
		params.put("trxamt", totalFee);
		params.put("reqsn", orderId);
		params.put("paytype", "W02");
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("body", "newPay");
		params.put("remark", "");
		params.put("acct", "oLRkewh_iKfxKTWVCRcyeDjRWgds");
		params.put("authcode", "");
		params.put("notify_url", "http://test.aodoors.com/xft/shangjia/route/changeStatus");
		params.put("limit_pay", "");
		params.put("idno", "");
		params.put("truename", "");
		params.put("asinfo", "");
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys, "UTF-8");
		Map<String, String> map = handleResult(result);
		//要签名
		String finaPackage = "{ \"appId\":\"" +  SybConstants.SYB_APPID + "\",\"timeStamp\":\"" + timestamp
		+ "\", + \"totel_fee\":\"" + totalFee + "\",\"nonceStr\":\"" + nonce_str + "\",\"package\":\""
		+ packages + "\",\"signType\" : \"MD5" + "\",\"paySign\":\""
		+ SybUtil.sign(params,SybConstants.SYB_APPKEY) + "\" }";
		return finaPackage;
	}
	
	public static Map<String,String> handleResult(String result) throws Exception{
		Map<String,String> map = new HashMap<>();
		map.put("result", result);
		return map;
	}

	/**
	 * 随机字符串生成
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

	/**
	 * 元转换成分
	 * @param money
	 * @return
	 */
	public static String getMoney(String amount) {
		if(amount==null){
			return "";
		}
		// 金额转化为分为单位
		String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额  
        int index = currency.indexOf(".");  
        int length = currency.length();  
        Long amLong = 0L;  
        if(index == -1){  
            amLong = Long.valueOf(currency+"00");  
        }else if(length - index >= 3){  
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
        }else if(length - index == 2){  
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
        }else{  
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
        }  
        return amLong.toString(); 
	}

}
