package com.gwu.payment.allinpay.model;

public class NotifyEntity {
	private String appid;//通联分配的appid    最大长度8
	private String outtrxid;//第三方交易号    最大长度50   暂未启用
	private String trxcode;//交易类型 		 最大长度8  
	private String trxid;//交易流水号 		 最大长度50  通联收银宝交易流水号
	private String trxamt;//金额  		 最大长度20   单位分
	private String trxdate;//交易请求日期  		最大长度8   格式yyyymmdd
	private String paytime;//交易完成时间 		 最大程度14  格式yyyymmddhhmmss
	private String chnltrxid;//渠道流水号  		最大长度64   如支付宝,微信平台订单号
	private String trxstatus;//交易结果码  		最大长度4
	private String cusid;//商户号  		最大长度15
	private String termno;//终端编码  		最大长度8
	private String termbatchid;//终端批次号  		最大长度16
	private String termtraceno;//终端流水		  最大长度6
	private String termauthno;//终端授权码  		最大长度20
	private String termrefnum;//终端参考号 		 最大长度50
	private String trxreserved;//业务关联内容  		最大长度160   交易备注
	private String srctrxid;//原交易流水 		 最大长度50    通联原交易流水,冲正撤销交易本字段不为空
	private String cusorderid;//业务流水  		最大长度32    如订单号，保单号，缴费编号等
	private String acct;//交易帐号   		最大长度64   如果是刷卡交易,则是隐藏的卡号,例如621700****4586
	private String fee;//手续费  		最大长度15   单位：分
	private String signtype;//签名类型  		最大长度3   MD5或RSA。为空默认MD5
	private String sign;//sign校验码  最大长度32
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getOuttrxid() {
		return outtrxid;
	}
	public void setOuttrxid(String outtrxid) {
		this.outtrxid = outtrxid;
	}
	public String getTrxcode() {
		return trxcode;
	}
	public void setTrxcode(String trxcode) {
		this.trxcode = trxcode;
	}
	public String getTrxid() {
		return trxid;
	}
	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}
	public String getTrxamt() {
		return trxamt;
	}
	public void setTrxamt(String trxamt) {
		this.trxamt = trxamt;
	}
	public String getTrxdate() {
		return trxdate;
	}
	public void setTrxdate(String trxdate) {
		this.trxdate = trxdate;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getChnltrxid() {
		return chnltrxid;
	}
	public void setChnltrxid(String chnltrxid) {
		this.chnltrxid = chnltrxid;
	}
	public String getTrxstatus() {
		return trxstatus;
	}
	public void setTrxstatus(String trxstatus) {
		this.trxstatus = trxstatus;
	}
	public String getCusid() {
		return cusid;
	}
	public void setCusid(String cusid) {
		this.cusid = cusid;
	}
	public String getTermno() {
		return termno;
	}
	public void setTermno(String termno) {
		this.termno = termno;
	}
	public String getTermbatchid() {
		return termbatchid;
	}
	public void setTermbatchid(String termbatchid) {
		this.termbatchid = termbatchid;
	}
	public String getTermtraceno() {
		return termtraceno;
	}
	public void setTermtraceno(String termtraceno) {
		this.termtraceno = termtraceno;
	}
	public String getTermauthno() {
		return termauthno;
	}
	public void setTermauthno(String termauthno) {
		this.termauthno = termauthno;
	}
	public String getTermrefnum() {
		return termrefnum;
	}
	public void setTermrefnum(String termrefnum) {
		this.termrefnum = termrefnum;
	}
	public String getTrxreserved() {
		return trxreserved;
	}
	public void setTrxreserved(String trxreserved) {
		this.trxreserved = trxreserved;
	}
	public String getSrctrxid() {
		return srctrxid;
	}
	public void setSrctrxid(String srctrxid) {
		this.srctrxid = srctrxid;
	}
	public String getCusorderid() {
		return cusorderid;
	}
	public void setCusorderid(String cusorderid) {
		this.cusorderid = cusorderid;
	}
	public String getAcct() {
		return acct;
	}
	public void setAcct(String acct) {
		this.acct = acct;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getSigntype() {
		return signtype;
	}
	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
