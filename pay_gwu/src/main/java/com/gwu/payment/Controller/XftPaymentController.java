package com.gwu.payment.Controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.gwu.payment.service.XftPaymentService;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.service.WechatNativPayService;
import com.gwu.payment.wechat.util.Tools;

/**
 * 
 * 支付基础表信息controller
 * 
 * @版本更新列表
 * 
 *         <pre>
 *   &#64
 * 	修改版本: 1.0.0
 * 	修改日期：2018年6月04日
 * 	修改说明：形成初始版本
 *         </pre>
 *    @author gwu     
 */
@RestController
public class XftPaymentController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	XftPaymentService xftPaymentService;
	@Autowired
	WechatNativPayService wechatNativPayService;
	
	/**
	 * 
	 * @param payNo
	 * @param payType
	 * @return查询状态码
	 * @author gwu
	 */
	@RequestMapping(value = "/getPo")
	public String getPo(HttpServletRequest req) {
		Map<String,Object> map=Tools.getData(req);
		logger.info("pagrams map:**********************" + JSON.toJSONString(map));
		return JSONUtils.beanToJson(xftPaymentService.selectByNo(map.get("payNo").toString() ,map.get("payType").toString()));
	}
	
	
	/**
	 * 
	 * @param payNo
	 * @param payType
	 * @return通过支付宝或者微信判断是否已经重复支付
	 * @author gwu
	 */
	@RequestMapping(value = "/isOverPay")
	public String isOverPay(HttpServletRequest req) {
		Map<String,Object> map=Tools.getData(req);
		logger.info("pagrams map:**********************" + JSON.toJSONString(map));
		return JSONUtils.beanToJson(xftPaymentService.selectByNo(map.get("payNo").toString(),map.get("payType").toString()));
	}
	
	
	

}
