/**
 * copyright： Copyright ® 2013 三环国际软件版权所有
 * company： i3ring.com
 */
package com.gwu.payment.service.impl;

import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwu.payment.database.entity.WechatUser;
import com.gwu.payment.database.entity.XftUserCustomer;
import com.gwu.payment.database.mapper.WechatUserMapper;
import com.gwu.payment.database.mapper.XftUserCustomerMapper;
import com.gwu.payment.service.WechatUserService;
import com.gwu.payment.wechat.po.WechatUserInfo;

/**
 * 微信用户表
 * 
 * @版本更新列表
 * 
 *@author gwu
 *         <pre>
 *   &#64;
 * 	修改版本: 1.0.0
 * 	修改日期：2018年6月04日
 * 	修改说明：形成初始版本
 *    
 */

@Service
public class WechatUserImpl implements WechatUserService {
	
	private static final Logger logger = LoggerFactory.getLogger(WechatUserImpl.class);
	
	@Autowired
	private WechatUserMapper mapper;
	@Autowired
	private XftUserCustomerMapper xftUserCustomerMapper;
	
	/***
	 * 保存微信用户
	 * 同时保存商户与微信关系表
	 */
	@Transactional
	@Override
	public void setWechatUserInfo(WechatUserInfo u) {
		try {
			WechatUser  we=new WechatUser();
			we.setCreatetime(new Date());
			we.setOpenid(u.getOpenId());
			we.setNickname(filterEmoji(u.getNickname(),""));
			we.setSex(String.valueOf(u.getSex()));
			we.setLanguage(u.getLanguage());
			we.setCity(u.getCity());
			we.setProvince(u.getProvince());
			we.setCountry(u.getCountry());
			we.setHeadimgurl(u.getHeadImgUrl());
			//查询openid是否存在
			if (null != we.getOpenid()) {
				WechatUser selectwu =mapper.selectByOpenId(we.getOpenid());
				if (null != selectwu) {
					//已存在
					we.setId(selectwu.getId());
					mapper.updateByPrimaryKey(we);
				} else {
					mapper.insert(we);
				}
			}
			//保存商户与微信关联表
//			XftUserCustomer xuc=xftUserCustomerMapper.selectByuidOpenid(u.getUid(), u.getOpenId());
//			if (null != xuc) {
//				//已存在
//				xuc.setId(xuc.getId());
//				xuc.setUpdateTime(new Date());
//				xftUserCustomerMapper.updateByPrimaryKey(xuc);
//			} else {
//				XftUserCustomer xupo=new XftUserCustomer();
//				xupo.setOpenid(u.getOpenId());
//				xupo.setCreateTime(new Date());
//				xupo.setStatus(1);
//				xupo.setUid(u.getUid());
//				xftUserCustomerMapper.insert(xupo);
//			}
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
	}

	/**
     * emoji表情替换
     *
     * @param source 原字符串
     * @param slipStr emoji表情替换成的字符串                
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source,String slipStr) {
//        if(StringUtils.isNotBlank(source)){
//            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
//        }else{
            return source;
//        }
    }

}
