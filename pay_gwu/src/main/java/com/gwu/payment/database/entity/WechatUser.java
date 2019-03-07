package com.gwu.payment.database.entity;

import java.util.Date;
/**
 * 
 * @author gwu
 *微信用户表
 */
public class WechatUser {
	/**
	 * 主键
	 */
    private Integer id;
    /**
     * 微信用户唯一标识
     */
    private String openid;
    /***
     * 微信用户昵称
     */

    private String nickname;

    /***
     * 微信用户性别
     * 1标识男 2标识女
     */
    private String sex;
    /**
     * 语言
     */
    private String language;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 所在省份
     */

    private String province;
    /**
     * 国家
     */
    private String country;
    /**
     * 头像
     */
    private String headimgurl;

    
    private String remark;
    /***
     * 微信用户所在分组
     */
    private Integer groupid;
    /**
     * 状态
     */
    private Integer status;
    
    /***
     * 创建时间
     */
    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}