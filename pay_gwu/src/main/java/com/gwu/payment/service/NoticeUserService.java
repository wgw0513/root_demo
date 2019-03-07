package com.gwu.payment.service;

import java.util.List;

import com.gwu.payment.database.entity.NoticeUser;
/**
 * 
 * @author gwu
 *微信版定商户提醒表service
 */
public interface NoticeUserService{
	
	/**
	 * 
	 * @param id
	 * @return 根据主键获取微信商户绑定推送表信息
	 */
	NoticeUser get(Integer id);

	int insert(NoticeUser entity);
	/**
	 * 
	 * @param uid 商户号
	 * @param openid 微信商户的openid
	 * @return 查询微信商户绑定推送表信息
	 */
	NoticeUser selectByNo(String openid,Long uid);
	
	
	/**
	 * 
	 * @param status 状态
	 * @param openid 微信商户的openid
	 * @return 查询微信商户绑定推送表信息
	 */
	NoticeUser selectByOpenId(String openid,Integer status);
	
	
	
	/**
	 * 
	 * @param xtp
	 * 	编辑微信商户绑定推送表
	 * @category
	 */
	void updateNoticeUser(NoticeUser xtp);
	
	/**
	 * 
	 * @param entity
	 * @return保存用户
	 */
	int save(NoticeUser entity);
	
	/**
	 * uid商户id
	 * tmpResourceId 模版编码
	 * @return
	 * 获取有效的用户列表
	 */
	 List<NoticeUser> getIviList(String tmpResourceId,Long uid);
	
}
