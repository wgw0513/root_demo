package com.gwu.payment.allinpay.constants;

/**
 * 
 *  公共类型定义
 *     
 *  @版本更新列表
 * 	<pre>
 * 	修改版本: 1.0.0
 * 	修改日期：2018年11月29日
 * 	修改人 : HP
 * 	修改说明：形成初始版本
 * 	</pre>
 */
public interface Constant {
	/**
	 * 交易方式
	 * @author HP
	 *
	 */
	public static interface payType{
		/**微信扫码支付*/
		public static String W01 = "W01";
		/**微信JS支付*/
		public static String W02 = "W02";
		/**微信APP支付 */
		public static String W03 = "W03";
		/** 微信小程序支付*/
		public static String W06 = "W06";
		/**支付宝扫码支付 */
		public static String A01 = "A01";
		/**支付宝JS支付*/
		public static String A02 = "A02";
		/**手机QQ扫码支付*/
		public static String Q01 = "Q01";
		/**手机QQ JS支付*/
		public static String Q02 = "Q02";
		/**银联扫码支付(CSB)*/
		public static String U01 = "U01";
		/**银联JS支付*/
		public static String U02 = "U02";
	}
}
