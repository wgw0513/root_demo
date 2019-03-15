
package com.gwu.payment.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gwu.payment.database.entity.NoticeUserLog;
import com.gwu.payment.database.mapper.NoticeUserLogMapper;
import com.gwu.payment.service.NoticeUserLogService;


/**
 * 发送模版消息log
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
public class NoticeUserLogImpl implements NoticeUserLogService {
	
	private static final Logger logger = LoggerFactory.getLogger(NoticeUserLogImpl.class);
	 @Autowired
	 NoticeUserLogMapper noticeUserLogMapper;
	
	@Override
	public void save(NoticeUserLog nl) {
		// TODO Auto-generated method stub
		noticeUserLogMapper.insert(nl);
		
	}
	


}
