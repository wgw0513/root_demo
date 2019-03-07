package com.gwu.payment.database.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteOrder {
    private Integer id;

    private String ip;

    private String openid;

    private Date createtime;

    private String paymentNo;
    /**
     * 实际支付金额
     */
    private Double payAmount;

    private Date returntime;

    private String transactionId;

    private String status;

    private Integer orderType;

    private Integer customerId;

    private Integer scheduleId;

    private Integer signUpNum;
    
    private Date closeTime;//订单关闭时间
    
    private Integer refundFlag;//是否可以退款
    
    private Date updateTime;//更新时间
    
    private String memo;
    /**
     * 订单金额
     */
    private   Double  billAmount;
    /**
     * 已退款金额
     */
    private   Double  hasRefundAmt;

    
    private Integer ptType;//支付方式
    private String nickname;//下单用户微信昵称
    private Date departDate;//排期出发时间
    
    /**
     * 线路标题
     */
    private String title;
    
    /**
     * 线路id
     */
    private  Integer routeId;
    
    private Integer uid;
   
    /**
     * 集合地点id
     */
    private Integer locationId;
    
    private String collectTion;//集合地点
    
    
    /**
     * 订单折扣后价格
     */
    private Double  discountAmt;
    
    
    
 
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo == null ? null : paymentNo.trim();
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getSignUpNum() {
        return signUpNum;
    }

    public void setSignUpNum(Integer signUpNum) {
        this.signUpNum = signUpNum;
    }

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Integer getPtType() {
		return ptType;
	}

	public void setPtType(Integer ptType) {
		this.ptType = ptType;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getDepartDate() {
		return departDate;
	}

	public void setDepartDate(Date departDate) {
		this.departDate = departDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Double getHasRefundAmt() {
		return hasRefundAmt;
	}

	public void setHasRefundAmt(Double hasRefundAmt) {
		this.hasRefundAmt = hasRefundAmt;
	}
	
	public Integer getRefundFlag() {
		return refundFlag;
	}

	public void setRefundFlag(Integer refundFlag) {
		this.refundFlag = refundFlag;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getCollectTion() {
		return collectTion;
	}

	public void setCollectTion(String collectTion) {
		this.collectTion = collectTion;
	}

	public Double getDiscountAmt() {
		return discountAmt;
	}

	public void setDiscountAmt(Double discountAmt) {
		this.discountAmt = discountAmt;
	}
    
    
    
}