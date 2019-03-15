package com.gwu.payment.wechat.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gwu.payment.database.entity.NoticeUser;
import com.gwu.payment.service.NoticeUserService;
import com.gwu.payment.wechat.po.WechatUserInfo;
import com.gwu.payment.wechat.util.TokenThread;
import com.gwu.payment.wechat.util.Tools;
import com.gwu.payment.wechat.util.WechatUtil;

/**
 * Created by gwu on 2018/8/6.
 *
 * 微信消息
 */
@Service
public class WechatCoreService {
	private static final Logger logger = LoggerFactory.getLogger(WechatCoreService.class);
	
	@Autowired
	NoticeUserService noticeUserService;
	
	/**
     * 处理微信公众号请求信息
     * @param request
     * @return
     */
    public String handlePublicMsg(HttpServletRequest request) throws Exception {
        // 获得微信端返回的xml数据
        try {
        	// xml请求解析
        	Map<String, String> decryptMap = Tools.parseXml(request);
            // 区分消息类型
            String msgType = decryptMap.get("MsgType");
            // 普通消息
            if ("text".equals(msgType)) { // 文本消息
            	
                // todo 处理文本消息
            } else if ("image".equals(msgType)) { // 图片消息
            	
                // todo 处理图片消息
            } else if ("voice".equals(msgType)) { //语音消息
            	
                // todo 处理语音消息
            } else if ("video".equals(msgType)) { // 视频消息
            	
                // todo 处理视频消息
            } else if ("shortvideo".equals(msgType)) { // 小视频消息
            	
                // todo 处理小视频消息
            } else if ("location".equals(msgType)) { // 地理位置消息
            	
                // todo 处理地理位置消息
            } else if ("link".equals(msgType)) { // 链接消息
            	
                // todo 处理链接消息
            }
            // 事件推送
            else if ("event".equals(msgType)) { // 事件消息
                // 区分事件推送
                String event = decryptMap.get("Event");
            	String opneId=decryptMap.get("FromUserName");
                logger.info("=======================EventKey:"+decryptMap.get("EventKey"));
                logger.info("=======================opneId"+opneId);
                
                if ("subscribe".equals(event)) { // 订阅事件 或 未关注扫描二维码事件
                	 logger.info("=======================EventKey:subscribe ");    
                	 WechatUserInfo	 wc=WechatUtil.getWXUserInfo2(TokenThread.accessToken.getAccessToken(), opneId);
                	 if(null!=decryptMap.get("EventKey")) { 
                		String arr[]= decryptMap.get("EventKey").split("_");
                		 NoticeUser noticeUser=new NoticeUser();
                		 noticeUser.setNickName(wc.getNickname());
                		 noticeUser.setOpenid(opneId);
                		 noticeUser.setUid(Long.parseLong(arr[1].toString()));
                		 noticeUser.setStatus(1);
                		 noticeUserService.insert(noticeUser);
                		 
                	 } 
                	 
//                	 return mes;
                	
                }  else if ("unsubscribe".equals(event)) { // 取消订阅事件	
                	NoticeUser  nc=noticeUserService.selectByOpenId(opneId,1);
                	if(nc!=null) {
	                	nc.setStatus(2);
	                	noticeUserService.updateNoticeUser(nc);
                	}
                // todo 处理取消订阅事件
                } else if ("SCAN".equals(event)) { // 已关注扫描二维码事件
                	logger.info("=======================unsubscribe:SCANSCANSCANSCANSCANSCAN");
                	WechatUserInfo	 wc=WechatUtil.getWXUserInfo2(TokenThread.accessToken.getAccessToken(), opneId);
                	if(null!=decryptMap.get("EventKey")) { 
                		
                		String uid= decryptMap.get("EventKey");
                		NoticeUser noticeUser=new NoticeUser();
	               		 noticeUser.setNickName(wc.getNickname());
	               		 noticeUser.setOpenid(opneId);
	               		 noticeUser.setUid(Long.parseLong(uid));
	               		 noticeUser.setStatus(1);
	               		 noticeUserService.insert(noticeUser);
               		 
               	 } 	
                } else if ("LOCATION".equals(event)) { // 上报地理位置事件
                    // todo 处理上报地理位置事件
                } else if ("CLICK".equals(event)) { // 点击菜单拉取消息时的事件推送事件
                    // todo 处理点击菜单拉取消息时的事件推送事件
                } else if ("VIEW".equals(event)) { // 点击菜单跳转链接时的事件推送
                    // todo 处理点击菜单跳转链接时的事件推送
                }
            }
        } catch (Exception e) {
            logger.error("处理微信公众号请求信息，失败", e);
        } finally {
           
        }
        return null;
    }
 
}
