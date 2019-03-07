package com.gwu.payment.wechat.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.gwu.payment.Vo.ReturnData;
import com.gwu.payment.allinpay.model.PayEntity;
import com.gwu.payment.allinpay.service.SybPayService;
import com.gwu.payment.database.entity.RouteOrder;
import com.gwu.payment.database.entity.XftPayment;
import com.gwu.payment.service.XftPaymentService;
import com.gwu.payment.util.AesUtil;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.config.WechatConfig;
import com.gwu.payment.wechat.service.WechatNativPayService;
import com.gwu.payment.wechat.util.PayUtil;
import com.gwu.payment.wechat.util.Sha1Util;
import com.gwu.payment.wechat.util.TokenThread;
import com.gwu.payment.wechat.util.Tools;
import com.gwu.payment.wechat.util.WechatUtil;

/**
 * 
 * 
 * @author gwu
 * @email 402920025@qq.com
 * @date 2018-05-08 11:17:16
 * @version 1.0.0
 * 类功能说明：微信扫码支付处理 controller
 * 
 */
@Controller
@RequestMapping(value = "/wechat/payment")
@PropertySource({ "classpath:/httpClient.properties" })
public class WechatNativPayController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WechatNativPayService wechatNativPayService;
	@Autowired
	XftPaymentService  xftPaymentService;
	@Autowired
	SybPayService  sybPayService;
	
	@Value("${PAYMENT_METHOD}")
	private String paymentMethod;//新支付开关  NEW为开启
	/**
	 * 
	 * @param req
	 * @param url
	 * @return微信分享入口
	 */
	@ResponseBody 
	@RequestMapping(value = "/getFen")
	public String  getFen(HttpServletRequest req) {
		logger.info("pagrams signVerified:**********************:获取分享" );
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			Map<String,Object> getmap=Tools.getData(req);
			logger.info("####################################paramsMap:  " + JSON.toJSONString(getmap));
			String url=getmap.get("url")==null?"":getmap.get("url").toString();
			String timeStamp=Sha1Util.getTimeStamp();
			String nonceStr=PayUtil.getNonceStr();
			map.put("appid", WechatConfig.appid);
			map.put("timestamp",timeStamp);
			map.put("nonceStr",nonceStr);
			//组装签名
			SortedMap<String, String> packageParams = new TreeMap<String, String>();			
			packageParams.put("noncestr", nonceStr);
			packageParams.put("jsapi_ticket",TokenThread.jsapiTicket.getTicket());
			packageParams.put("timestamp",  timeStamp);
			packageParams.put("url", (url!=null&&!"".equals(url))?url:WechatConfig.wzym);
			String sign=null;
			sign = Sha1Util.createSHA1Sign(packageParams);
			map.put("signature", sign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		return JSONUtils.beanToJson(map);  
	}
	
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @param body 订单内容
	 * @param openID 
	 * @param orderID 订单号
	 * @param totalFee 订单金额
	 * @return 微信扫码生成支付字符串
	 */
	@ResponseBody
	@RequestMapping(value = "/nativPay")
	public String  nativPay(HttpServletRequest req) {
		logger.info("pagrams signVerified:**********************:进微信扫码支付！！" );
		//判断
		if ("NEW".equals(paymentMethod)) {
			return wechatNativPayService.returmNewPayUrl(req); 
		} else {
			return wechatNativPayService.returmPayUrl(req); 
		}
	}
	
	
	/**
	 * 
	 * @param req
	 * @return分享页面入口
	 */
	@RequestMapping(value = "/fenxian")
	public ModelAndView  fenxian(HttpServletRequest req){

		ModelAndView mv=new ModelAndView();
		mv.setViewName("/wechat/fenxian.html");
		return mv;  
	}
	
	
	/**
	 * 
	 * @param req
	 * @return微信页面支付入口
	 */
	@RequestMapping(value = "/pay")
	public ModelAndView  pay(HttpServletRequest req, String params, String body){
		ModelAndView mv=new ModelAndView();
		try {
			logger.info("#####################pay");
			//通过掉用微信查询接口,支付宝接口 判断是否已经付款
			Map<String, Object> paramsMap = JSONUtils.jsonToMap(AesUtil.lastDeCrypt(params, AesUtil.AESZF_KEY));
			if ("NEW".equals(paymentMethod)) {
				ReturnData returnData=xftPaymentService.isOverPay2( req,  paramsMap,  body);
				mv.addObject("dataMap", returnData);
				mv.setViewName("/newPay/pay.html");
			} else {
				ReturnData returnData=xftPaymentService.isOverPay( req,  paramsMap,  body);
				mv.addObject("dataMap", returnData);
				mv.setViewName("/wechat/pay.html");
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.info("pagrams pay:**********************:"+e.getMessage());
			e.printStackTrace();
		}	
		return mv;  
	}
	
	/**
	 * 
	 * @param req
	 * @param orderID  订单号
	 * @param openid 微信用户id
	 * @param totalFee 支付金额
	 * @param ip 微信客户端id
	 * @param body 支付内容title
	 * @param notifyURL 支付成功通知url
	 * @param payType 订单类型
	 * @return js页面支付
	 */
	@ResponseBody
	@RequestMapping(value = "/jsApiPay")
	public String  jsApiPay(HttpServletRequest req ,String orderID,String openid,String  totalFee,String ip,String body,String notifyURL
			,String payType){
	boolean isweixin = WechatUtil.isWeiXin(req);
    if (isweixin) {
    	try{
    		//判断
    		if ("NEW".equals(paymentMethod)) {
    			
    			return  wechatNativPayService.jsApiNewPay(orderID,openid,totalFee,ip,body,notifyURL,payType);
    		} else {
    			return wechatNativPayService.jsApiPay(orderID,openid,totalFee,ip,body,notifyURL,payType);
    		}	
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
    		logger.error(e.getMessage());
		}
    }else {
    	return "请用微信开打！";
    }
	return "";
  }

	/**
	 * 
	 * @param orderNo
	 * @param payType
	 * @return  自动刷新页面金额
	 */
	@ResponseBody
	@RequestMapping(value = "/refashToalMoney")
	public String  jsApiPay(String orderNo){
		RouteOrder routeOrder=xftPaymentService.selectRouteOrderByPaymentNo(orderNo);
		if(routeOrder!=null) {
			return routeOrder.getDiscountAmt().toString();
		}else {
			return null;
		}
  }

	
	/**
	 * 
	 * @param req
	 * @param orderID  订单号
	 * @param openid 微信用户id
	 * @param totalFee 支付金额
	 * @param ip 微信客户端id
	 * @param body 支付内容title
	 * @param notifyURL 支付成功通知url
	 * @param payType 订单类型
	 * @return js页面支付
	 */
	@ResponseBody
	@RequestMapping(value = "/jsApiPay2")
	public String  jsApiPay2(HttpServletRequest req ,String orderID,String openid,String  totalFee,String ip,String body,String notifyURL
			,String returnURL,String payType){
	boolean isweixin = WechatUtil.isWeiXin(req);
    if (isweixin) {
    	try{
    		Map<String, String> map = new HashMap<String, String>();
			PayEntity record = new PayEntity();
			record.setTrxamt(PayUtil.getMoney(totalFee));
			record.setReqsn(payType + orderID);
			record.setPaytype("W02");//微信js支付
			record.setBody(body);
			record.setRemark("");
			record.setAcct(openid);
			record.setAuthcode("");
			record.setNotify_url(WechatConfig.notifyurl);
			record.setLimit_pay("");
			record.setIdno("");
			record.setTruename("");
			record.setAsinfo("");
			logger.info("pay pagrams : " + JSON.toJSONString(record));
			map = sybPayService.pay(record);
			logger.info("pay return map : " + JSON.toJSONString(map));
			logger.info(JSONUtils.beanToJson(map)+"%%%%%%%%%%%%%%%%%%%%%%%%%%");
			
			String wxorder="";
			XftPayment xft=xftPaymentService.selectByNo(orderID,payType);
			//判断是线路订单则拿订单的金额,	//获取时时金额 20181017
			 String totalFeethis="";
			if("WR".equals(payType)){
				RouteOrder  ro=xftPaymentService.selectRouteOrderByPaymentNo(orderID);
				totalFeethis=new BigDecimal(ro.getDiscountAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
				//判断是否已经支付
				if(ro.getStatus().equals("01")){
					//System.out.println("=====================该订单已支付成功，请勿重复支付！");	
					return "{\"code\":\"5050\",\"msg\":\""+payType+orderID+"该订单已支付成功，请勿重复支付！\"}";
					
				}
			}else {
			    totalFeethis= new BigDecimal(totalFee).setScale(2, BigDecimal.ROUND_HALF_UP).toString();//充值金额
			}
			
			if(Tools.isEmpty(xft)) {
				wxorder=payType+orderID;
			    
				wechatNativPayService.savePay(payType,orderID, totalFeethis,notifyURL);
			}else {
				XftPayment xftnew=new  XftPayment();
				//重新生成微信单号
				wxorder=payType+orderID;
				xftnew.setId(xft.getId());
				xftnew.setPaymentNo(wxorder);
				xftnew.setPayAmount(new BigDecimal(totalFeethis));
				xftPaymentService.updatePayment(xftnew);
			}
			
			return JSONUtils.beanToJson(map);
    	}catch (Exception e) {
    		e.printStackTrace();
    		logger.error(e.getMessage());
		}
    }else {
    	return "请用微信开打！";
    }
	return "";
  }
}