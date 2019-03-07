package com.gwu.payment.allinpay.controllor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.gwu.payment.allinpay.model.PayEntity;
import com.gwu.payment.allinpay.service.SybPayService;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.util.Tools;

@Controller
@RequestMapping(value = "/newPay")
public class SybPayControllor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SybPayService sybPayService;

	@RequestMapping(value = "/query")
	public String query(HttpServletRequest req) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String reqsn = String.valueOf(System.currentTimeMillis());
			Map<String, Object> getmap = Tools.getData(req);
			logger.info("####################################query   paramsMap:  " + JSON.toJSONString(getmap));
			map = sybPayService.query(reqsn, getmap.get("trxid").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONUtils.beanToJson(map);
	}

	@RequestMapping(value = "/refund")
	public String refund(HttpServletRequest req) throws Exception {
		String reqsn = String.valueOf(System.currentTimeMillis());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> getmap = Tools.getData(req);
			logger.info("####################################refund   paramsMap:  " + JSON.toJSONString(getmap));
			String trxamt = getmap.get("trxamt").toString();
			String oldtrxid = getmap.get("oldtrxid").toString();
			String oldreqsn = getmap.get("oldreqsn").toString();
			map = sybPayService.refund(trxamt, reqsn, oldtrxid, oldreqsn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONUtils.beanToJson(map);
	}

	@RequestMapping(value = "/cancel")
	public String cancel(HttpServletRequest req) throws Exception {
		String reqsn = String.valueOf(System.currentTimeMillis());
		Map<String, String> map = new HashMap<String, String>();
		try {
			Map<String, Object> getmap = Tools.getData(req);
			logger.info("####################################cancel   paramsMap:  " + JSON.toJSONString(getmap));
			String trxamt = getmap.get("trxamt").toString();
			String oldtrxid = getmap.get("oldtrxid").toString();
			String oldreqsn = getmap.get("oldreqsn").toString();
			map = sybPayService.cancel(trxamt, reqsn, oldtrxid, oldreqsn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONUtils.beanToJson(map);
	}

	@RequestMapping(value = "/pay")
	public String pay(HttpServletRequest req, String orderID, String openid, 
			String totalFee, String ip, String body,
			String notifyURL, String payType,String paymentType) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			PayEntity record = new PayEntity();
			record.setTrxamt(totalFee);
			record.setReqsn(orderID);
			record.setPaytype(paymentType);
			record.setBody(body);
			record.setRemark("");
			record.setAcct(openid);
			record.setAuthcode("");
			record.setNotify_url(notifyURL);
			record.setLimit_pay("");
			record.setIdno("");
			record.setTruename("");
			record.setAsinfo("");
			logger.info("pay pagrams : " + JSON.toJSONString(record));
			map = sybPayService.pay(record);
			logger.info("pay return map : " + JSON.toJSONString(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONUtils.beanToJson(map);
	}
}
