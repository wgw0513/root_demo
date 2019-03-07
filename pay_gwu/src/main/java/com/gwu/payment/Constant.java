package com.gwu.payment;

/**
 * 
 *  公共类型定义
 *  @author gwu  
 *  @版本更新列表
 * 	<pre>
 * 	修改版本: 1.0.0
 * 	修改日期：2018年6月13日
 * 	修改人 : gwu
 * 	修改说明：形成初始版本
 * 	</pre>
 */
public interface Constant {
	/** 有效 */
	public static int 	VALID = 1;
	/** 无效 */
	public static int INVALID = 0;
	
	/**支付加密key**/
	public final static String AESZF_KEY="gzhuanbokh_xft!@#";
	   
  /**
   * 
   * @author gwu
   *支付类型
   */
    public static interface PayType {	
    	/** 微信  */
		public static String WECHATZF = "1";
		/** 支付宝 */
		public static String ZFBZF ="2";
		/** 网银 */
		public static String WYZF = "3";
	}
    
    /**
     * 支付状态
     *     
     *  @版本更新列表
     * 	<pre>
     *   @author : gwu 
     * 	修改版本: 1.0.0
     * 	修改日期：2018年6月13日
     * 	修改说明：形成初始版本
     * 	</pre>
     */
    public static interface PayStatus {
    	/** 待支付  */
    	public static String UNPAY = "00";
    	/** 已支付  */
    	public static String PAID = "01";
    	/** 支付失败  */
    	public static String FALID = "02";
    	/**已经退款**/
    	public static String YTK = "03";
    	
    	/**关闭的订单**/
    	public static String CLOSE = "09";
    }
    
    

    /**
     * 通知支付状态
     *     
     *  @版本更新列表
     * 	<pre>
     *   @author : gwu 
     * 	修改版本: 1.0.0
     * 	修改日期：2018年6月13日
     * 	修改说明：形成初始版本
     * 	</pre>
     */
    public static interface PayFlag {
    	/** 待支付  */
    	public static int UNPAY = 1;
    	/** 已支付  */
    	public static int PAID = 2;
    	/** 支付失败  */
    	public static int FALID =3;
    	/** 已退款**/
    	public static int YTK =4;

    }
}
