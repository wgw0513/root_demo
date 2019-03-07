package com.gwu.payment.allinpay.service;

import java.util.Map;

import com.gwu.payment.allinpay.model.PayEntity;

public interface SybPayService {
	/**
	 * 支付
	 * @param payrecord
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> pay(PayEntity payrecord) throws Exception;
	
	/**
	 * 取消订单
	 * @param trxamt
	 * @param reqsn
	 * @param oldtrxid
	 * @param oldreqsn
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> cancel(String trxamt,String reqsn,String oldtrxid,String oldreqsn) throws Exception;
	
	public Map<String,Object> refund(String trxamt,String reqsn,String oldtrxid,String oldreqsn) throws Exception;
	/**
	 * 查询
	 * @param reqsn
	 * @param trxid
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> query(String reqsn,String trxid) throws Exception;
	
	public String jsZFBPay()throws Exception;
}
