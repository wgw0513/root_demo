package com.gwu.payment.wechat.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.po.AccessToken;
import com.gwu.payment.wechat.po.JsapiTicket;
import com.gwu.payment.wechat.po.WechatOauth2Token;
import com.gwu.payment.wechat.po.WechatUserInfo;

import net.sf.json.JSONObject;

 
/**
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0
 * 类功能说明：公众平台通用接口工具类 
 */
public class WechatUtil {
	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);  
    /** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {  
        JSONObject jsonObject = null;  
        StringBuffer buffer = new StringBuffer();  
        try {  
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
  
            URL url = new URL(requestUrl);  
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf);  
  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);  
  
            if ("GET".equalsIgnoreCase(requestMethod)) {  
                httpUrlConn.connect();  
            }
            // 当有数据需要提交时  
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
            jsonObject = JSONObject.fromObject(buffer.toString());  
        } catch (ConnectException ce) {  
            log.error("Weixin server connection timed out.");  
        } catch (Exception e) {  
            log.error("https request error:{}", e);  
        }  
        return jsonObject;  
    }  
    
    /**
	 * 获取网页授权凭证
	 * 
	 * @param appId
	 *            公众账号的唯一标识
	 * @param appSecret
	 *            公众账号的密钥
	 * @param code
	 * @return WeixinAouth2Token
	 */
	public static WechatOauth2Token getOauth2AccessToken(String appId,
			String appSecret, String code) {
		WechatOauth2Token wat = null;
		// 拼接请求地址snsapi_userinfo
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		requestUrl = requestUrl.replace("APPID", appId);
		requestUrl = requestUrl.replace("SECRET", appSecret);
		requestUrl = requestUrl.replace("CODE", code);
		// 获取网页授权凭证
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject) {
			try {
				wat = new WechatOauth2Token();
				wat.setAccessToken(jsonObject.getString("access_token"));
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
				wat.setRefreshToken(jsonObject.getString("refresh_token"));
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setScope(jsonObject.getString("scope"));
			} catch (Exception e) {
				wat = null;
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode,
						errorMsg);
			}
		}
		return wat;
	}
    
	/**
	 * 授权获取用户信息
	 * 
	 * @param accessToken
	 *            接口访问凭证
	 * @param openId
	 *            用户标识
	 * @return WeixinUserInfo
	 */
	public static WechatUserInfo getWXUserInfo(String accessToken, String openId) {
		WechatUserInfo wechatUserInfo = null;
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
//		String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace(
				"OPENID", openId);
		// 获取用户信息
		JSONObject jsonObject = CommonUtil
				.httpsRequest(requestUrl, "GET", null);
		log.info("获取用户信息:"+jsonObject);
		if (null != jsonObject) {
			try {
				wechatUserInfo = new WechatUserInfo();
				// 用户的标识
				wechatUserInfo.setOpenId(jsonObject.getString("openid"));
//				// 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
//				wechatUserInfo.setSubscribe(jsonObject.getInt("subscribe"));
				// 用户关注时间
//				wechatUserInfo.setSubscribeTime(jsonObject
//						.getString("subscribe_time"));
				// 昵称
				wechatUserInfo.setNickname(jsonObject.getString("nickname"));
				// 用户的性别（1是男性，2是女性，0是未知）
				wechatUserInfo.setSex(jsonObject.getInt("sex"));
				// 用户所在国家
				wechatUserInfo.setCountry(jsonObject.getString("country"));
				// 用户所在省份
				wechatUserInfo.setProvince(jsonObject.getString("province"));
				// 用户所在城市
				wechatUserInfo.setCity(jsonObject.getString("city"));
				// 用户的语言，简体中文为zh_CN
				wechatUserInfo.setLanguage(jsonObject.getString("language"));
				// 用户头像
				wechatUserInfo
						.setHeadImgUrl(jsonObject.getString("headimgurl"));
			} catch (Exception e) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode,
						errorMsg);
			}
		}
		return wechatUserInfo;
	}
	
	
	
	/**
	 * 直接获取用户信息
	 * 
	 * @param accessToken
	 *            接口访问凭证
	 * @param openId
	 *            用户标识
	 * @return WeixinUserInfo
	 */
	public static WechatUserInfo getWXUserInfo2(String accessToken, String openId) {
		WechatUserInfo wechatUserInfo = null;
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace(
				"OPENID", openId);
		// 获取用户信息
		JSONObject jsonObject = CommonUtil
				.httpsRequest(requestUrl, "GET", null);
		log.info("jsonObjectjsonObjectjsonObjectjsonObjectjsonObject:"+jsonObject);
		if (null != jsonObject) {
			try {
				wechatUserInfo = new WechatUserInfo();
				// 用户的标识
				wechatUserInfo.setOpenId(jsonObject.getString("openid"));
//				// 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
				wechatUserInfo.setSubscribe(jsonObject.getInt("subscribe"));
				// 用户关注时间
//				wechatUserInfo.setSubscribeTime(jsonObject
//						.getString("subscribe_time"));
				// 昵称
				wechatUserInfo.setNickname(jsonObject.getString("nickname"));
				// 用户的性别（1是男性，2是女性，0是未知）
				wechatUserInfo.setSex(jsonObject.getInt("sex"));
				// 用户所在国家
				wechatUserInfo.setCountry(jsonObject.getString("country"));
				// 用户所在省份
				wechatUserInfo.setProvince(jsonObject.getString("province"));
				// 用户所在城市
				wechatUserInfo.setCity(jsonObject.getString("city"));
				// 用户的语言，简体中文为zh_CN
				wechatUserInfo.setLanguage(jsonObject.getString("language"));
				// 用户头像
				wechatUserInfo
						.setHeadImgUrl(jsonObject.getString("headimgurl"));
			} catch (Exception e) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode,
						errorMsg);
			}
		}
		return wechatUserInfo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
 // 获取access_token的接口地址（GET） 限200（次/天）
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 凭证获取（GET）
	public final static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	

  
    /**
     * 获取access_token
     * 
     * @param appid 凭证
     * @param appsecret 密钥
     * @return
     */
    public static AccessToken getAccessToken() {
    	AccessToken accessToken = null;
    	//判断数据库的时间和现在时间
    	String requestUrl = access_token_url.replace("APPID", WechatConfig.appid).replace("APPSECRET", WechatConfig.appsecret);
    	JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
    	log.info("jsonObject=====:"+jsonObject.toString());
    	// 如果请求成功
    	if (null != jsonObject) {
    		try {
    			accessToken = new AccessToken();
    			accessToken.setAccessToken(jsonObject.getString("access_token"));
    			accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
    		} catch (JSONException e) {
    			accessToken = null;
    			// 获取token失败
    			log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
    		}
    	}
    	return accessToken;
    }
    
    
    /**
     * 获取jsapi_ticket
     * 
     * @param appid 凭证
     * @param appsecret 密钥
     * @return
     */
    public static JsapiTicket getjsapi_ticket(String ACCESS_TOKEN) {
    	JsapiTicket jsapiTicket = null;
    	//判断数据库的时间和现在时间
    	String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", ACCESS_TOKEN);
    	JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
    	// 如果请求成功
    	if (null != jsonObject) {
    		try {
    			jsapiTicket = new JsapiTicket();
    			jsapiTicket.setTicket(jsonObject.getString("ticket"));
    			jsapiTicket.setExpires_in(jsonObject.getInt("expires_in"));
    		} catch (JSONException e) {
    			jsapiTicket = null;
    			// 获取token失败
    			log.error("获取jsapiTicket失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
    		}
    	}
    	return jsapiTicket;
    }
    
    
    // 菜单创建（POST） 限100（次/天）
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 创建菜单
     * 
     * @param menu 菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(String menu, String accessToken) {
    	int result = 0;

    	// 拼装创建菜单的url
    	String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
    	// 将菜单对象转换成json字符串
    	String jsonMenu =menu;
    	// 调用接口创建菜单
    	JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

    	if (null != jsonObject) {
    		if (0 != jsonObject.getInt("errcode")) {
    			result = jsonObject.getInt("errcode");
    			log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
    		}
    	}

    	return result;
    }
    

    /**
     * @method sendWechatmsgToUser
     * @描述: TODO(发送模板信息给用户) 
     * @参数@param touser  用户的openid
     * @参数@param templat_id  信息模板id
     * @参数@param url  用户点击详情时跳转的url
     * @参数@param topcolor  模板字体的颜色
     * @参数@param data  模板详情变量 Json格式
     * @参数@return
     * @返回类型：String
     * @添加时间 2018-8-8上午10:38:45
     * @作者：gwu
     */
    public static String sendWechatmsgToUser(String touser, String templat_id, String clickurl, String topcolor, JSONObject data){
       
    	log.info("%%%%%%%%%%%%touser:::"+touser);
    	String tmpurl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        String token = TokenThread.accessToken.getAccessToken();  //微信凭证，access_token
        String url = tmpurl.replace("ACCESS_TOKEN", token);
        JSONObject json = new JSONObject();
        try {
            json.put("touser", touser);
            json.put("template_id", templat_id);
            json.put("url", clickurl);
            json.put("topcolor", topcolor);
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       
        try {
        	 JSONObject resultJson = httpRequest(url, "POST", json.toString());
        	 log.info("%%%%%%%%%%%%resultJson======================:::"+JSON.toJSONString(resultJson));
            String errmsg = (String) resultJson.get("errmsg");
            if(!"ok".equals(errmsg)){  //如果为errmsg为ok，则代表发送成功，公众号推送信息给用户了。
                return "error";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "success";
    }
    
   
    /**
     * 
     * @param first
     * @param orderMoneySum
     * @param orderProductName
     * @param remark
     * @return 组装数据
     * type 1报名成功通知，2退款通知，3保险通知提醒
     */
    public static JSONObject packModelMsg(Map<String,Object> map,String type){
        JSONObject json = new JSONObject();
        try {
        	
            JSONObject jsonFirst = new JSONObject();
            jsonFirst.put("value", map.get("first")!=null?map.get("first").toString():"");
            jsonFirst.put("color", "#173177");
            json.put("first",jsonFirst);
            
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", map.get("keyword1")!=null?map.get("keyword1").toString():"");
            keyword1.put("color", "#173177");
            json.put("keyword1", keyword1);
            
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", map.get("keyword2")!=null?map.get("keyword2").toString():"");
            keyword2.put("color", "#173177");
            json.put("keyword2", keyword2);
            
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", map.get("keyword3")!=null?map.get("keyword3").toString():"");
            keyword3.put("color", "#173177");
            json.put("keyword3", keyword3);
            
            if(!"3".endsWith(type)) {
	            JSONObject keyword4 = new JSONObject();
	            keyword4.put("value", map.get("keyword4")!=null?map.get("keyword4").toString():"");
	            keyword4.put("color", "#173177");
	            json.put("keyword4", keyword4);
            }
            
            if("3".endsWith(type)) {
            	 JSONObject jsonRemark = new JSONObject();
                 jsonRemark.put("value",map.get("keyword4")!=null?map.get("keyword4").toString():"");
                 jsonRemark.put("color", "#173177");
                 json.put("remark", jsonRemark);
            	
            }else {
            	 JSONObject jsonRemark = new JSONObject();
                 jsonRemark.put("value",map.get("remark")!=null?map.get("remark").toString():"");
                 jsonRemark.put("color", "#173177");
                 json.put("remark", jsonRemark);
            }
            
           
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    

    
     
    /**
     * 判断是否来自微信, 5.0 之后的支持微信支付
     *
     * @param request
     * @return 判断是否来自微信, 5.0 之后的支持微信支付

     */
    public static boolean isWeiXin(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ptStr="MicroMessenger/(\\d+).+";
        Pattern   pt = Pattern.compile(ptStr);
        if (StringUtils.isNotBlank(userAgent)) {
            Matcher matcherPo = pt.matcher(userAgent);
            String version = null;
            if (matcherPo.find()) {
                version = matcherPo.group(1);
            }
            return (null != version && NumberUtils.toInt(version) >= 5);
        }
        return false;
    }
}
