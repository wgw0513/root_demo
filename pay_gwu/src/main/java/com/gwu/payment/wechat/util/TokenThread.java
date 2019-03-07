package com.gwu.payment.wechat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwu.payment.wechat.po.AccessToken;
import com.gwu.payment.wechat.po.JsapiTicket;  
  
/** 
 * 定时获取微信access_token  jsapiTicket的线程 
 *  
 * @author gwu 
 * @date 2018-05-22
 * jsapiTicket用于微信分享签名
 * access_token用于基础框架凭证
 * 每天限制拿20000次
 * 有效时间2小时
 */  
public class TokenThread implements Runnable {  
    private static Logger log = LoggerFactory.getLogger(TokenThread.class);  
    public static AccessToken accessToken = null;  
    public static JsapiTicket jsapiTicket = null;  
    @Override
    public void run() {  
        while (true) {  
            try {  
                accessToken = WechatUtil.getAccessToken();  
               
                if (null != accessToken) {  
                	 jsapiTicket=WechatUtil.getjsapi_ticket(accessToken.getAccessToken());
                	 log.info("获取jsapi_Ticket成功，有效时长{}秒 token:{}", jsapiTicket.getExpires_in(), jsapiTicket.getTicket());  
                    log.info("获取access_token成功，有效时长{}秒 token:{}", accessToken.getExpiresIn(), accessToken.getAccessToken());  
                    // 休眠7000秒  
                    Thread.sleep((accessToken.getExpiresIn() - 200) * 1000);  
                } else {  
                    // 如果access_token为null，60秒后再获取  
                    Thread.sleep(60 * 1000);  
                }  
            } catch (InterruptedException e) {  
                try {  
                    Thread.sleep(60 * 1000);  
                } catch (InterruptedException e1) {  
                    log.error("{}", e1);  
                }  
                log.error("{}", e);  
            }  
        }  
    }  

}  