package com.gwu.payment.database.entity;

public class NoticeTemplate {
    private Integer tmpId;

    private String tmpName;

    private String tmpResourceId;

    private Integer status;

    public Integer getTmpId() {
        return tmpId;
    }

    public void setTmpId(Integer tmpId) {
        this.tmpId = tmpId;
    }

    public String getTmpName() {
        return tmpName;
    }

    public void setTmpName(String tmpName) {
        this.tmpName = tmpName == null ? null : tmpName.trim();
    }

    public String getTmpResourceId() {
        return tmpResourceId;
    }

    public void setTmpResourceId(String tmpResourceId) {
        this.tmpResourceId = tmpResourceId == null ? null : tmpResourceId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}