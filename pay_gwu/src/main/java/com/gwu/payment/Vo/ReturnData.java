package com.gwu.payment.Vo;

import java.math.BigDecimal;

/*
 *返回数据 
 */
public class ReturnData {
	//返回编码
	private String code;
	//返回信息
	private String msg;
	
	//微信id
	private String openid;
	
	/**
	 * 支付标题
	 */
	private String body;
	
	/**
	 * 单号
	 */
	private  String payNo;
	
	/**
	 * ip
	 */
	private String ip;
	
	/**
	 * 单类型
	 */
	private String payType;
	
	/**
	 * 支付金额
	 */
	private BigDecimal totalFee;
	/**
	 * 自动跳转url
	 */
	private String noticeURL;
	
	//返回url
	private String returnURL;



	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String getOpenid() {
		return openid;
	}


	public void setOpenid(String openid) {
		this.openid = openid;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public String getPayNo() {
		return payNo;
	}


	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getPayType() {
		return payType;
	}


	public void setPayType(String payType) {
		this.payType = payType;
	}


	public String getNoticeURL() {
		return noticeURL;
	}


	public void setNoticeURL(String noticeURL) {
		this.noticeURL = noticeURL;
	}


	public String getReturnURL() {
		return returnURL;
	}


	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}


	public BigDecimal getTotalFee() {
		return totalFee;
	}


	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	
	

}
