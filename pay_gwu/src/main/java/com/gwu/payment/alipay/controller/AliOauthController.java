package com.gwu.payment.alipay.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gwu.payment.alipay.config.AlipayConfig;
import com.gwu.payment.alipay.util.AlipaySubmit;
@Controller
@RequestMapping(value = "/ali/payment/oauth")
/**
 * 
 * @author gwu
 *支付宝认证
 */
public class AliOauthController {
	/**
     *  跳转到授权界面
     */
    @RequestMapping(value = "/check")
    public String check(HttpServletRequest request,HttpServletResponse response) {
        Map<String,String> maps = new HashMap<String ,String>();
        //页面回调地址 必须与应用中的设置一样
        //回调地址必须经encode
        String return_url = java.net.URLEncoder.encode(AlipayConfig.oauthreturnurl);
        //重定向到授权页面
        return "redirect:"+AlipayConfig.oauthpayurl+"?app_id=" + AlipayConfig.appid + "&scope=auth_user&redirect_uri=" + return_url;
    }
    
    /**
     * 获取用户信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "/returnImf")
    public void returnImf(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == (values.length-1)) ? valueStr + values[i]:valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        String accessToken= AlipaySubmit.buildRequest(params);
        if(accessToken!=null && accessToken!=""){
            String imf  =  AlipaySubmit.get(accessToken);
            System.out.println(imf);
        }
    }
    
    
}
