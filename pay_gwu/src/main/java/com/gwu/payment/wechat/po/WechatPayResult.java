
package com.gwu.payment.wechat.po;

/**
 * description: 微信支付结果返回字段
 * @author 
 * @since 
 * @see
 */
public class WechatPayResult {
	/**
	 * 调用接口提交的公众账号ID
	 */
	private String appid;
	/**
	 * 付款银行类型
	 */
	private String bankType;
	/**
	 * 货币类型
	 */
	private String cashFee;
	/**
	 * 符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型
	 */
	private String feeType;
	/**
	 * 是否已关注公众号
	 */
	private String isSubscribe;
	/**
	 * 商户号
	 */
	private String mchId;
	/**
	 *
	 * 微信返回的随机字符串
	 */
	private String nonceStr;
	/**
	 * 用户id
	 */
	private String openid;
	/**
	 * 订单号
	 */
	private String outTradeNo;
	/**
	 * 业务结果	result_code	是	String(16)	SUCCESS	SUCCESS/FAIL
	 */
	private String resultCode;
	/**
	 * 当return_code为FAIL时返回信息为错误原因 ，例如
		签名失败
		参数格式校验错误
	 */
	private String returnCode;
	/**
	 * 微信返回的签名值，详见签名算法
	 */
	private String sign;
	/**
	 * 时间戳
	 */
	private String timeEnd;
	/**
	 * 订单金额
	 */
	private String totalFee;
	/**
	 * 交易类型	trade_type	是	String(16)	JSAPI	
		JSAPI 公众号支付
		NATIVE 扫码支付
		APP APP支付
		说明详见参数规定
	 */
	private String tradeType;
	/**
	 * 微信交易凭证号
	 */
	private String transactionId;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1227026039888867970L;
	
	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}
	/**
	 * @param appid the appid to set
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}
	/**
	 * @return the bankType
	 */
	public String getBankType() {
		return bankType;
	}
	/**
	 * @param bankType the bankType to set
	 */
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	/**
	 * @return the cashFee
	 */
	public String getCashFee() {
		return cashFee;
	}
	/**
	 * @param cashFee the cashFee to set
	 */
	public void setCashFee(String cashFee) {
		this.cashFee = cashFee;
	}
	/**
	 * @return the feeType
	 */
	public String getFeeType() {
		return feeType;
	}
	/**
	 * @param feeType the feeType to set
	 */
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	/**
	 * @return the isSubscribe
	 */
	public String getIsSubscribe() {
		return isSubscribe;
	}
	/**
	 * @param isSubscribe the isSubscribe to set
	 */
	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
	/**
	 * @return the mchId
	 */
	public String getMchId() {
		return mchId;
	}
	/**
	 * @param mchId the mchId to set
	 */
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	/**
	 * @return the nonceStr
	 */
	public String getNonceStr() {
		return nonceStr;
	}
	/**
	 * @param nonceStr the nonceStr to set
	 */
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}
	/**
	 * @param openid the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/**
	 * @return the outTradeNo
	 */
	public String getOutTradeNo() {
		return outTradeNo;
	}
	/**
	 * @param outTradeNo the outTradeNo to set
	 */
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}
	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}
	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the timeEnd
	 */
	public String getTimeEnd() {
		return timeEnd;
	}
	/**
	 * @param timeEnd the timeEnd to set
	 */
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	/**
	 * @return the totalFee
	 */
	public String getTotalFee() {
		return totalFee;
	}
	/**
	 * @param totalFee the totalFee to set
	 */
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	/**
	 * @return the tradeType
	 */
	public String getTradeType() {
		return tradeType;
	}
	/**
	 * @param tradeType the tradeType to set
	 */
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	
}