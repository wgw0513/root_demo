package com.gwu.payment.wechat.util;

import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.po.WechatPayDto;

/**
 * @author gwu
 * 
 * 微信支付工具类
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
	 */
	public static String getPackage(WechatPayDto tpWxPayDto) {	
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
		String mch_id = WechatConfig.parter;
		// 随机字符串
		String nonce_str = getNonceStr();
		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();
		// 商户订单号
		String out_trade_no =orderId;

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
		packageParams.put("openid", openId);

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
				+ "</trade_type>" + "<openid>" + openId + "</openid>"
				+ "</xml>";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String prepayID =GetWeChatOrderNo.getPayNo(createOrderURL, xml);//获取到的预支付ID支付
		//获取prepay_id后，拼接最后请求支付所需要的package
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id="+prepayID;
		finalpackage.put("appId",  WechatConfig.appid);  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", nonce_str);  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		//要签名
		String finalsign = reqHandler.createSign(finalpackage);
		String finaPackage = "{ \"appId\":\"" +  WechatConfig.appid + "\",\"timeStamp\":\"" + timestamp
		+ "\",\"nonceStr\":\"" + nonce_str + "\",\"package\":\""
		+ packages + "\",\"signType\" : \"MD5" + "\",\"paySign\":\""
		+ finalsign + "\" }";
		return finaPackage;
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
		  
	/**
	 * 发送微信红包
	 * @param mch_billno 商户订单号
	 * @param openId 接受红包的用户openid 
	 * @param send_name 商户名称
	 * @param total_fee 
	 * @param total_num
	 * @param wishing
	 * @param act_name
	 * @param remark
	 * @param ip
	 * @throws Exception
	 *发放规则1.发送频率限制------默认1800/min
	 *2.发送个数上限------按照默认1800/min算
	 *3.金额限制------默认红包金额为1-200元，如有需要，可前往商户平台进行设置和申请
	 *4.其他其他限制吗？------单个用户可领取红包上线为10个/天，如有需要，可前往商户平台进行设置和申请
	 *5.如果量上满足不了我们的需求，如何提高各个上限？------金额上限和用户当天领取次数上限可以在商户平台进行设置
	 *注意-红包金额大于200或者小于1元时，请求参数scene_id必传，参数说明见下文。
	 *注意2-根据监管要求，新申请商户号使用现金红包需要满足两个条件：1、入驻时间超过90天 2、连续正常交易30天。
	 *注意3-移动应用的appid无法使用红包接口。
	 */
//	 public static void sendRedPack(String mch_billno,String openId,String send_name,String total_fee,String total_num,
//			  String wishing,String act_name,String remark,String ip) throws Exception{
//		  RequestHandler reqHandler = new RequestHandler(null, null);
//			reqHandler.init(WechatConfig.APPID, WechatConfig.APPSECRET, WechatConfig.PARTNERKEY);
//			// 随机字符串
//			String nonce_str = getNonceStr();
//			SortedMap<String, String> sepackage = new TreeMap<String, String>();
//			sepackage.put("nonce_str", nonce_str);
//			sepackage.put("mch_billno", mch_billno);
//			sepackage.put("mch_id", WechatConfig.PARTNER);
//			sepackage.put("wxappid", WechatConfig.APPID);
//			sepackage.put("re_openid", openId);
//			sepackage.put("total_amount", total_fee);
//			sepackage.put("total_num", "1");
//			sepackage.put("client_ip", ip);
//			sepackage.put("act_name",act_name);
//			sepackage.put("send_name", send_name);
//			sepackage.put("wishing", wishing);
//			sepackage.put("remark",remark);
//			reqHandler.init(WechatConfig.APPID, WechatConfig.APPSECRET, WechatConfig.PARTNERKEY);
//			String sign = reqHandler.createSign(sepackage);
//			sepackage.put("sign", sign);
//			
//			
//			String reuqestXml = Tools.callMapToXML(sepackage);
//			
//			KeyStore keyStore = KeyStore.getInstance("PKCS12");
//			FileInputStream instream = new FileInputStream(new File("D\\a"));//证书路径
//			try {
//				keyStore.load(instream, WechatConfig.PARTNER);
//			} finally {
//				instream.close();
//			}
//
//			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,
//					WechatConfig.PARTNER).build();
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//					sslcontext, new String[] { "TLSv1" }, null,
//					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//			CloseableHttpClient httpclient = HttpClients.custom()
//					.setSSLSocketFactory(sslsf).build();
//			try {
//
//				HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");// 退款接口
//				
//				httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//				
//				System.out.println("executing request" + httpPost.getRequestLine());
//				//请求的xml需转码为iso8859-1编码，否则易出现签名错误或红包上的文字显示有误
//				StringEntity reqEntity = new StringEntity(new String(reuqestXml.getBytes(), "ISO8859-1"));
//				// 设置类型
//				httpPost.setEntity(reqEntity);
//				CloseableHttpResponse response = httpclient.execute(httpPost);
//				try {
//					HttpEntity entity = response.getEntity();
//
//					System.out.println("----------------------------------------");
//					System.out.println(response.getStatusLine());
//					if (entity != null) {
//						System.out.println("Response content length: "
//								+ entity.getContentLength());
//						BufferedReader bufferedReader = new BufferedReader(
//								new InputStreamReader(entity.getContent(), "UTF-8"));
//						String text;
//						while ((text = bufferedReader.readLine()) != null) {
//							System.out.println(text);
//						}
//
//					}
//					EntityUtils.consume(entity);
//				} finally {
//					response.close();
//				}
//			} finally {
//				httpclient.close();
//			}
//		} 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//微信支付jsApi
		WechatPayDto tpWxPay = new WechatPayDto();
		//tpWxPay.setOpenId(WeixinConfig.OPENID);
		tpWxPay.setBody("商品信息");
		tpWxPay.setOrderId(getNonceStr());
		tpWxPay.setSpbillCreateIp("127.0.0.1");
		tpWxPay.setTotalFee("0.01");
	    getPackage(tpWxPay);
	    
	    //扫码支付
	    WechatPayDto tpWxPay1 = new WechatPayDto();
	    tpWxPay1.setBody("商品信息");
	    tpWxPay1.setOrderId(getNonceStr());
	    tpWxPay1.setSpbillCreateIp("127.0.0.1");
	    tpWxPay1.setTotalFee("0.01");
		getCodeurl(tpWxPay1);

	}

}
