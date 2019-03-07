package com.gwu.payment.wechat.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import net.sf.json.JSONObject;

public  class HttpUtils{
	
private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

/**
 * 发送https请求
 * @param requestUrl 请求地址
 * @param requestMethod 请求方式（GET、POST）
 * @param data 提交的数据
 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
 */
public static JSONObject httpsRequest(String requestUrl, String requestMethod, String data) {
    JSONObject jsonObject = null;
    InputStream inputStream = null;
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;
    try {
        URL url = new URL(requestUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        // 设置请求方式（GET/POST）
        conn.setRequestMethod(requestMethod);
        conn.connect();
        // 当data不为null时向输出流写数据
        if (null != data) {
            // getOutputStream方法隐藏了connect()方法
            OutputStream outputStream = conn.getOutputStream();
            // 注意编码格式
            outputStream.write(data.getBytes("UTF-8"));
            outputStream.close();
        }
        // 从输入流读取返回内容
        inputStream = conn.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        bufferedReader = new BufferedReader(inputStreamReader);
        String str = null;
        StringBuffer buffer = new StringBuffer();
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        conn.disconnect();
        jsonObject = JSONObject.fromObject(buffer.toString());
        return jsonObject;
    } catch (Exception e) {
        logger.error("发送https请求失败，失败", e);
        return null;
    } finally {
        // 释放资源
        try {
            if(null != inputStream) {
                inputStream.close();
            }
            if(null != inputStreamReader) {
                inputStreamReader.close();
            }
            if(null != bufferedReader) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            logger.error("释放资源失败，失败", e);
        }
    }
}

/**
 * 获取微信公众号二维码
 * @param codeType 二维码类型 "1": 临时二维码  "2": 永久二维码
 * @param sceneId 场景值ID
 */
public static String getWXPublicQRCode(String codeType, String sceneId,String scene_str) {
	 String lastQRCode="";
	try {
		String wxAccessToken = TokenThread.accessToken.getAccessToken();
	    Map<String, Object> map = new HashMap<>();
	    if ("1".equals(codeType)) { // 临时二维码
	        map.put("expire_seconds", 604800);
	        map.put("action_name", "QR_SCENE");
	        Map<String, Object> sceneMap = new HashMap<>();
	        Map<String, Object> sceneIdMap = new HashMap<>();
	       sceneIdMap.put("scene_id", sceneId);
	        sceneIdMap.put("scene_str", scene_str);
	        sceneMap.put("scene", sceneIdMap);
	        map.put("action_info", sceneMap);
	    } else if ("2".equals(codeType)) { // 永久二维码
	        map.put("action_name", "QR_LIMIT_SCENE");
	        Map<String, Object> sceneMap = new HashMap<>();
	        Map<String, Object> sceneIdMap = new HashMap<>();
	        sceneIdMap.put("scene_id", sceneId);
	        sceneIdMap.put("scene_str", scene_str);
	        sceneMap.put("scene", sceneIdMap);
	        map.put("action_info", sceneMap);
	    }
	    String data = JSON.toJSONString(map);
	    // 得到ticket票据,用于换取二维码图片
	    JSONObject jsonObject = HttpUtils.httpsRequest("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + wxAccessToken, "POST", data);
	    String ticket = (String) jsonObject.get("ticket");
	     lastQRCode="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(ticket);
   }catch (Exception e) {
	// TODO: handle exception
	   e.printStackTrace();
}
	return lastQRCode;
  
}

}