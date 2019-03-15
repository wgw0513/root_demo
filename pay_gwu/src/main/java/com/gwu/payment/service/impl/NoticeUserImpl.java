
package com.gwu.payment.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwu.payment.database.entity.NoticeTemplate;
import com.gwu.payment.database.entity.NoticeUser;
import com.gwu.payment.database.entity.NoticeUserSetting;
import com.gwu.payment.database.mapper.NoticeTemplateMapper;
import com.gwu.payment.database.mapper.NoticeUserMapper;
import com.gwu.payment.database.mapper.NoticeUserSettingMapper;
import com.gwu.payment.service.NoticeUserService;


/**
 * 微信绑定表
 * 
 * @版本更新列表
 * 
 *@author gwu
 *         <pre>
 *   &#64;
 * 	修改版本: 1.0.0
 * 	修改日期：2018年8月08日
 * 	修改说明：形成初始版本
 *    
 */
@Service
public class NoticeUserImpl implements NoticeUserService {
	
	private static final Logger logger = LoggerFactory.getLogger(NoticeUser.class);
	@Autowired
	private NoticeUserMapper  noticeUserMapper;
	@Autowired
	private  NoticeTemplateMapper noticeTemplateMapper;
	@Autowired
	private NoticeUserSettingMapper noticeUserSettingMapper;

	/***
	 * 保存绑定表，同时设置绑定模版
	 * 同时保存商户与微信关系表
	 */
	@Transactional
	@Override
	public int insert(NoticeUser u) {
		try {	
		NoticeUser	ncu= noticeUserMapper.selectByNo(u.getOpenid(), u.getUid(),1);		
		if(ncu==null) {
			u.setCreatetime(new Date());
			u.setStatus(1);
			u.setNickName(filterEmoji(u.getNickName(),""));
			int id=save(u);//保存
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("status", 1);
			List<NoticeTemplate>  ntmList=noticeTemplateMapper.getList(params);
			if(ntmList!=null&&ntmList.size()>0) {
				for(NoticeTemplate nt:ntmList) {
					//保存关系模版表
					NoticeUserSetting noticeUserSetting=new NoticeUserSetting();
					noticeUserSetting.setNuid(id);
					noticeUserSetting.setTmpId(nt.getTmpId());
					noticeUserSetting.setStatus(1);
					noticeUserSettingMapper.insert(noticeUserSetting);
				}	
			}
		//更新表	
		}else {
			u.setNickName(filterEmoji(u.getNickName(),""));
			u.setNuid(ncu.getNuid());
			u.setUpdatetime(new Date());
			noticeUserMapper.updateByPrimaryKey(u);	
		}
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		return 0;
	}

	
	/**
     * emoji表情替换
     *
     * @param source 原字符串
     * @param slipStr emoji表情替换成的字符串                
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source,String slipStr) {
        if(StringUtils.isNotBlank(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        }else{
            return source;
        }
    }


	@Override
	public NoticeUser get(Integer id) {
		// TODO Auto-generated method stub
		return noticeUserMapper.selectByPrimaryKey(id);
	}

	
	@Override
	public NoticeUser selectByNo(String openid, Long uid) {
		// TODO Auto-generated method stub
		return noticeUserMapper.selectByNo(openid, uid, 1);
	}


	@Override
	public void updateNoticeUser(NoticeUser xtp) {
		// TODO Auto-generated method stub
		
		noticeUserMapper.updateByPrimaryKey(xtp);
		
	}

	@Override
	public int save(NoticeUser entity) {
		noticeUserMapper.insertSelective(entity);
		return noticeUserMapper.getAutoId();
	}


	@Override
	public NoticeUser selectByOpenId(String openid, Integer status) {
		// TODO Auto-generated method stub
		return noticeUserMapper.selectByOpenId(openid, status);
	}


	@Override
	public List<NoticeUser> getIviList(String tmpResourceId,Long uid) {
		// TODO Auto-generated method stub
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("tmpResourceId", tmpResourceId);
		params.put("uid", uid);
		return noticeUserMapper.getIviList(params);
	}

}
