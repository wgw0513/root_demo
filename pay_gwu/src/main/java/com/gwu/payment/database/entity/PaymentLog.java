package com.gwu.payment.database.entity;

import java.util.Date;
/**
 * 
 * @author gwu
 *支付log
 */
public class PaymentLog {
    private Integer id;

    private String operNo;

    private String beforContent;

    private String afterContent;

    private Date datetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(String operNo) {
        this.operNo = operNo == null ? null : operNo.trim();
    }

    public String getBeforContent() {
        return beforContent;
    }

    public void setBeforContent(String beforContent) {
        this.beforContent = beforContent == null ? null : beforContent.trim();
    }

    public String getAfterContent() {
        return afterContent;
    }

    public void setAfterContent(String afterContent) {
        this.afterContent = afterContent == null ? null : afterContent.trim();
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}