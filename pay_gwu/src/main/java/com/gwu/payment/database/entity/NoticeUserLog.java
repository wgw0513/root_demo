package com.gwu.payment.database.entity;

import java.util.Date;

public class NoticeUserLog {
    private Integer logId;

    private Integer tmpId;

    private String tmpContent;

    private String memo;

    private Date createtime;

    private Integer status;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getTmpId() {
        return tmpId;
    }

    public void setTmpId(Integer tmpId) {
        this.tmpId = tmpId;
    }

    public String getTmpContent() {
        return tmpContent;
    }

    public void setTmpContent(String tmpContent) {
        this.tmpContent = tmpContent == null ? null : tmpContent.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}