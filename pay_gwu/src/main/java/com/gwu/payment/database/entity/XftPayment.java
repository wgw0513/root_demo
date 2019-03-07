package com.gwu.payment.database.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  支付基础数据表
 *     
 *  @版本更新列表
 * 	<pre>
 * 	修改版本: 1.0.0
 * 	修改日期：2018年6月5日
 * 	修改人 : gwu
 * 	修改说明：形成初始版本
 * 	</pre>
 * @author gwu
 */
public class XftPayment {
	/**
	 * 主键
	 */
    private Integer id;
    /**
     * 订单号
     */
    private String payNo;
   /**
    * 订单类型
    */
    private String payType;
   /**
    * 订单金额
    */
    private BigDecimal payAmount;
    /**
     * 微信订单号
     */
    private String paymentNo;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 回调成功时间
     */
    private Date returntime;
    /**
     * 微信统一交易号
     */
    private String transactionId;
    /**
     * 状态  00等待支付 01支付成功 02支付失败
     */
    private String status;
    /**
     * 状态标识 
     */
    private Integer flag;
    
    /**
     * 执行业务url
     */
    private String notifyURL;
    
    /**
     * 1标识微信，2标识支付宝
     */
    private String ptType;
    
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo == null ? null : payNo.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo == null ? null : paymentNo.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getReturntime() {
        return returntime;
    }

    public void setReturntime(Date returntime) {
        this.returntime = returntime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

	public String getNotifyURL() {
		return notifyURL;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	public String getPtType() {
		return ptType;
	}

	public void setPtType(String ptType) {
		this.ptType = ptType;
	}

}