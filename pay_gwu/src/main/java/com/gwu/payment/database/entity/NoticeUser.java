package com.gwu.payment.database.entity;

import java.util.Date;

public class NoticeUser {
    private Integer nuid;

    private Long uid;

    private String openid;

    private String nickName;

    private Date createtime;

    private Date updatetime;

    private Integer status;
    
    private String tmpResourceId;

    public Integer getNuid() {
        return nuid;
    }

    public void setNuid(Integer nuid) {
        this.nuid = nuid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getTmpResourceId() {
		return tmpResourceId;
	}

	public void setTmpResourceId(String tmpResourceId) {
		this.tmpResourceId = tmpResourceId;
	}
    
    
    
}