package com.gwu.payment.wechat.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwu.payment.database.entity.NoticeUser;
import com.gwu.payment.service.NoticeUserService;
import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.service.WeChatTemplateService;
import com.gwu.payment.wechat.util.Tools;
import com.gwu.payment.wechat.util.WechatUtil;

import net.sf.json.JSONObject;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-08-08 11:17:16
 * @version 1.0.0
 * 类功能说明：发送模版消息接口
 * type 1报名成功通知，2退款通知，3保险通知提醒
 */

@Controller
@RequestMapping(value = "/wechat/payment/send")
public class SendWeChatModelMsgController {
	private static final Logger logger = LoggerFactory.getLogger(WechatPaySearchController.class);
	@Autowired
	WeChatTemplateService weChatTemplateService;
	@Autowired
	NoticeUserService noticeUserService;
	/**
	 * 
	 * @param request
	 * 报名成功通知
	 */
	@ResponseBody 
	@RequestMapping(value = "/sendWxMsg1")
	public String sendWxMsg1(HttpServletRequest request) {
		Map<String,Object> map=Tools.getData(request);
//		String tmpResourceId="tV5FIFtYV_ODDB4WLhvPzwHgw23NaDNpuCCTkLIUfDs";
		//String tmpResourceId="3XUlqbVPd7paJk3qV4IGFEwc2ixP4483EmSztFNDYD8";
		return weChatTemplateService.sendModelMesage(map,WechatConfig.modelMesage1,"1");
		
     
    }
	/***
	 * 
	 * @param request
	 * 申请退款通知
	 */
	@ResponseBody 
	@RequestMapping(value = "/sendWxMsg2")
	public String sendWxMsg2(HttpServletRequest request) {
		Map<String,Object> map=Tools.getData(request);
		//String tmpResourceId="lWS6WsC0CeAXoamsw6-0LYWmPtBv_KoAHUOYU8IwJV0";
//		String tmpResourceId="oNOVvIm3gECu8IH2lq2uj5HgZB4wn6aQfoBi_zcmb9I";
		
		return weChatTemplateService.sendModelMesage(map,WechatConfig.modelMesage2,"2");

    }
	
	/**
	 * 
	 * @param request
	 * 
	 * 投保失败通知
	 */
	@ResponseBody 
	@RequestMapping(value = "/sendWxMsg3")
	public String sendWxMsg3(HttpServletRequest request) {
		Map<String,Object> map=Tools.getData(request);
		//String tmpResourceId="MusWnVaRiuoCmlOkMrWaBFyuOciHdQcemeaziMdJd6U";
//		String tmpResourceId="9c48zfBYhyyJMGNBATf-g9v0QzeJhghklUEppsIV6o4";
		return weChatTemplateService.sendModelMesage(map,WechatConfig.modelMesage3,"3");
     
    }
	
	
	/**
	 * 
	 * @param request
	 * 充值成功通知
	 */
	@ResponseBody 
	@RequestMapping(value = "/sendWxMsg4")
	public String sendWxMsg4(HttpServletRequest request) {
		Map<String,Object> map=Tools.getData(request);
			String tmpResourceId="MQc3i8PzUUaX8C_0b24NllQVt2sKnpESMmDcUD-A9RY";
		 		JSONObject json = new JSONObject();
	            JSONObject jsonFirst = new JSONObject();
	            jsonFirst.put("value", "您购买的流量包充值成功!!");
	            jsonFirst.put("color", "#173177");
	            json.put("first",jsonFirst);
	            
	            JSONObject keyword1 = new JSONObject();
	            keyword1.put("value", "2000M");
	            keyword1.put("color", "#173177");
	            json.put("keyword1", keyword1);
	            
	            JSONObject keyword2 = new JSONObject();
	            keyword2.put("value", "18076488805");
	            keyword2.put("color", "#173177");
	            json.put("keyword2", keyword2);
	            
	            JSONObject keyword3 = new JSONObject();
	            keyword3.put("value", "2019年9月1日 18:36");
	            keyword3.put("color", "#173177");
	            json.put("keyword3", keyword3);

	            JSONObject jsonRemark = new JSONObject();
	            jsonRemark.put("value", "您可在充值手机号码所属运营商微信公众号中查询");
	            jsonRemark.put("color", "#173177");
	            json.put("remark", jsonRemark); 
	            json.put("uid", request.getParameter("uid"));
	            System.out.println("====="+request.getParameter("uid"));
	        	List<NoticeUser>  list=noticeUserService.getIviList(null,Long.parseLong(request.getParameter("uid")));
	        	if(list!=null&&list.size()>0) {
					for(int i=0;i<list.size();i++) {
						WechatUtil.sendWechatmsgToUser(list.get(i).getOpenid() ,tmpResourceId,null, null, json);
					}
	        	}
	            return null;
     
    }

}

