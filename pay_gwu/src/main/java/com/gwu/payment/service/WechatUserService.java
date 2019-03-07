package com.gwu.payment.service;

import com.gwu.payment.wechat.po.WechatUserInfo;
/**
 * 
 * @author gwu
 *微信用户表service
 */
public interface WechatUserService{
	/**
	 * 
	 * @param u
	 * 保存微信用户表信息
	 */
	 void setWechatUserInfo(WechatUserInfo u);

}
