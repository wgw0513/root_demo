package com.gwu.payment.wechat.po;
/**
 * 
 * 
 * @author gwu
 
 * 通jstickt对象
 */
public class JsapiTicket {
	/**
	 * 返回编码
	 */
	private String errcode;
	
	/**
	 * 返回信息
	 */
	private String  errmsg;
	
	/**
	 * 凭证号
	 */
	private  String ticket;
	/**
	 * 凭证有效期，单位：秒
	 */
	private int expires_in;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	
	
	
	
	
	
}
