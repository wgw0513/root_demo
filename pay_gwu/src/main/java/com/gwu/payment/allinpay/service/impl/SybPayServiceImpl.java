package com.gwu.payment.allinpay.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.gwu.payment.allinpay.model.PayEntity;
import com.gwu.payment.allinpay.service.SybPayService;
import com.gwu.payment.allinpay.util.HttpConnectionUtil;
import com.gwu.payment.allinpay.util.SybConstants;
import com.gwu.payment.allinpay.util.SybUtil;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.util.PayUtil;

/**
 * 
 * @author HP
 *
 */
@Service
public class SybPayServiceImpl implements SybPayService {
	
	@Override
	public Map<String,String> pay(PayEntity record) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/pay");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("version", "11");
		params.put("trxamt", record.getTrxamt());
		params.put("reqsn", record.getReqsn());
		params.put("paytype", record.getPaytype());
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("body", record.getBody());
		params.put("remark", record.getRemark());
		params.put("acct", record.getAcct());
		params.put("authcode", record.getAuthcode());
		params.put("notify_url", record.getNotify_url());
		params.put("limit_pay", record.getLimit_pay());
		params.put("idno", record.getIdno());
		params.put("truename", record.getTruename());
		params.put("asinfo", record.getAsinfo());
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result);
		return map;
		
	}
	
	@Override
	public Map<String,String> cancel(String trxamt,String reqsn,String oldtrxid,String oldreqsn) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/cancel");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("version", "11");
		params.put("trxamt", trxamt);
		params.put("reqsn", reqsn);
		params.put("oldtrxid", oldtrxid);
		params.put("oldreqsn", oldreqsn);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result);
		return map;
	}
	
	@Override
	public Map<String,Object> refund(String trxamt,String reqsn,String oldtrxid,String oldreqsn) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/refund");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("version", "11");
		params.put("trxamt", trxamt);
		params.put("reqsn", reqsn);
		params.put("oldreqsn", oldreqsn);
		params.put("oldtrxid", oldtrxid);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result);
		if (!StringUtils.isEmpty(map.get("result"))) {
			Map<String, Object> map1 = JSONUtils.jsonToMap(map.get("result"));
			return map1;
		} else {
			return null;
		}
	}
	
	@Override
	public String jsZFBPay() {
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/pay");
		try {
			http.init();
			TreeMap<String,String> params = new TreeMap<String,String>();
			params.put("cusid", SybConstants.SYB_CUSID);
			params.put("appid", SybConstants.SYB_APPID);
			params.put("version", "11");
			params.put("randomstr", SybUtil.getValidatecode(8));
			params.put("trxamt", "1");
			params.put("reqsn", SybUtil.getValidatecode(32));		
			params.put("body", "测试");
			params.put("paytype","A02");
			params.put("authcode", "");
			
			params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY)); 	
			byte[] bys = http.postParams(params, true);
			String result = new String(bys,"UTF-8");
			Map<String,String> map = handleResult(result);
			System.out.println(JSON.toJSONString(map) );
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public static void main(String[] args) throws Exception{
//		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/refund");
//		http.init();
//		TreeMap<String,String> params = new TreeMap<String,String>();
//		params.put("cusid", SybConstants.SYB_CUSID);
//		params.put("appid", SybConstants.SYB_APPID);
//		params.put("version", "11");
//		params.put("trxamt", "1");
//		params.put("reqsn", SybUtil.getValidatecode(32));
//		params.put("oldreqsn", "");
//		params.put("oldtrxid", "111855300000448587");
//		params.put("randomstr", SybUtil.getValidatecode(8));
//		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY)); 	
//		byte[] bys = http.postParams(params, true);
//		String result = new String(bys,"UTF-8");
//		Map<String,String> map = handleResult(result);
//		System.out.println(JSON.toJSONString(map));
		
//		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/query");
//		http.init();
//		TreeMap<String,String> params = new TreeMap<String,String>();
//		params.put("cusid", SybConstants.SYB_CUSID);
//		params.put("appid", SybConstants.SYB_APPID);
//		params.put("version", "11");
//		params.put("reqsn", "WR18121315130612747352");
//		params.put("trxid", "");
//		params.put("randomstr", SybUtil.getValidatecode(8));
//		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
//		byte[] bys = http.postParams(params, true);
//		String result = new String(bys,"UTF-8");
//		Map<String,String> map = handleResult(result);
//		if (!StringUtils.isEmpty(map.get("result"))) {
//			Map<String, Object> map1 = JSONUtils.jsonToMap(map.get("result"));
//			System.out.println(JSON.toJSONString(map1));
//		}
		
		
		//微信扫码支付
//		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/pay");
//		http.init();
//		TreeMap<String,String> params = new TreeMap<String,String>();
//		params.put("cusid", SybConstants.SYB_CUSID);
//		params.put("appid", SybConstants.SYB_APPID);
//		params.put("version", "11");
//		params.put("randomstr", SybUtil.getValidatecode(8));
//		params.put("trxamt", "300");
//		params.put("reqsn", SybUtil.getValidatecode(32));		
//		params.put("body", "测试");
//		params.put("paytype","W01");
//		params.put("authcode", "");
//		
//		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY)); 	
//		
	
		
//		
//		byte[] bys = http.postParams(params, true);
//		String result = new String(bys,"UTF-8");
//		Map<String,String> map = handleResult(result);
//		System.out.println(JSON.toJSONString(map) );
//		System.out.println(JSONUtils.jsonToMap(map.get("result")).get("payinfo"));
//		
		
		
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/pay");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("version", "11");
		params.put("trxamt", "1");
		params.put("reqsn", "MG18121811074613232160");
		params.put("paytype", "U02");
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("body", "");
		params.put("remark", "");
		params.put("acct", "");
		params.put("authcode", "");
		params.put("notify_url", "");
		params.put("limit_pay", "");
		params.put("idno", "");
		params.put("truename", "");
		params.put("asinfo", "");
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result);
		System.out.println(JSON.toJSONString(map) );
		
//	
		
		//支付宝扫码支付
//		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/pay");
//		http.init();
//		TreeMap<String,String> params = new TreeMap<String,String>();
//		params.put("cusid", SybConstants.SYB_CUSID);
//		params.put("appid", SybConstants.SYB_APPID);
//		params.put("version", "11");
//		params.put("randomstr", SybUtil.getValidatecode(8));
//		params.put("trxamt", "1");
//		params.put("reqsn", SybUtil.getValidatecode(32));		
//		params.put("body", "测试");
//		params.put("paytype","A01");
//		params.put("authcode", "");
//		
//		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY)); 	
//		byte[] bys = http.postParams(params, true);
//		String result = new String(bys,"UTF-8");
//		Map<String,String> map = handleResult(result);
//		
//		System.out.println(JSON.toJSONString(map));
//		System.out.println(JSONUtils.jsonToMap(map.get("result")).get("payinfo"));
////		
		
		
		//支付宝js支付
//		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/pay");
//		http.init();
//		TreeMap<String,String> params = new TreeMap<String,String>();
//		params.put("cusid", SybConstants.SYB_CUSID);
//		params.put("appid", SybConstants.SYB_APPID);
//		params.put("version", "11");
//		params.put("randomstr", SybUtil.getValidatecode(8));
//		params.put("trxamt", "1");
//		params.put("reqsn", SybUtil.getValidatecode(32));		
//		params.put("body", "测试");
//		params.put("paytype","A02");
//		params.put("authcode", "");
//		
//		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY)); 	
//		byte[] bys = http.postParams(params, true);
//		String result = new String(bys,"UTF-8");
//		Map<String,String> map = handleResult(result);
//		System.out.println(JSON.toJSONString(map) );
		
		
		
		
		
		
		
		
	}
	
	@Override
	public Map<String,Object> query(String reqsn,String trxid) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/query");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("version", "11");
		params.put("reqsn", reqsn);
		params.put("trxid", trxid);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result);
		if (!StringUtils.isEmpty(map.get("result"))) {
			Map<String, Object> map1 = JSONUtils.jsonToMap(map.get("result"));
			return map1;
		} else {
			return null;
		}
	}
	
	public static Map<String,String> handleResult(String result) throws Exception{
		Map<String,String> map = new HashMap<>();
		map.put("result", result);
		return map;
	}

}
