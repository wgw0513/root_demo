package com.gwu.payment.wechat.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.gwu.payment.database.entity.NoticeUser;
import com.gwu.payment.database.entity.NoticeUserLog;
import com.gwu.payment.service.NoticeUserLogService;
import com.gwu.payment.service.NoticeUserService;
import com.gwu.payment.wechat.util.WechatUtil;

import net.sf.json.JSONObject;

@Service
public class WeChatTemplateService {

	private static final Logger logger = LoggerFactory.getLogger(WeChatTemplateService.class);
	
	@Autowired
	NoticeUserService noticeUserService;
	@Autowired
	NoticeUserLogService noticeUserLogService;
	/**
	 * 
	 * @return
	 * 发送模版消息
	 * type 1报名成功通知，2退款通知，3保险通知提醒
	 * 
	 */
	public String sendModelMesage(Map<String,Object> map,String tmpResourceId,String type) {
		int index=0;
		String code="0";
		String msg="";
		try {
			logger.info("调用发送模版消息！！！！");
			logger.info("闯过来的数据======================！！！！"+JSON.toJSONString(map));
			JSONObject data=WechatUtil.packModelMsg(map,type);
			String uid=map.get("uid")!=null?map.get("uid").toString():"0";
			String url=map.get("url")!=null?map.get("url").toString():null;
			List<NoticeUser>  list=noticeUserService.getIviList(tmpResourceId,Long.parseLong(uid));
			
			int totalcount=0;
			if(list!=null&&list.size()>0) {
				 totalcount=list.size();
				 logger.info("%%%%%%%%%%%%totalcount:::"+totalcount);
				for(int i=0;i<list.size();i++) {
				String	result=WechatUtil.sendWechatmsgToUser(list.get(i).getOpenid() ,tmpResourceId,url, null, data);
					NoticeUserLog  nulog=new NoticeUserLog();
					nulog.setCreatetime(new Date());
					nulog.setTmpContent(JSON.toJSONString(map));
					if("success".equals(result)) {
						nulog.setStatus(1);
					}else {
						index++;
						nulog.setStatus(0);
					}
					nulog.setMemo(result);
					nulog.setTmpId(list.get(i).getNuid());
					noticeUserLogService.save(nulog);
				}	
			}
			if(index>0&&index!=totalcount) {
				msg="部分发送成功！";
				code="1";
			}else if(index==totalcount){
				msg="发送失败！";
				code="2";
			}else {
				msg="发送成功！！";
				code="0";
					
			}
		}catch(Exception e) {
			e.printStackTrace();
			code="2";
			msg="服务出错出错了！";
		}
		 return "{\"code\":\""+ code +"\","+"\"msg\":\""+msg+"\"}"; 	
		
	}
}
