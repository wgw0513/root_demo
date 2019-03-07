/**
 * copyright： Copyright ® 2013 三环国际软件版权所有
 * company： i3ring.com
 */
package com.gwu.payment.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.gwu.payment.Constant.PayStatus;
import com.gwu.payment.Constant.PayType;
import com.gwu.payment.Vo.ReturnData;
import com.gwu.payment.alipay.service.AliPayService;
import com.gwu.payment.allinpay.service.SybPayService;
import com.gwu.payment.database.entity.RouteOrder;
import com.gwu.payment.database.entity.XftPayment;
import com.gwu.payment.database.mapper.RouteOrderMapper;
import com.gwu.payment.database.mapper.XftPaymentMapper;
import com.gwu.payment.service.XftPaymentService;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.service.WechatNativPayService;
import com.gwu.payment.wechat.util.HttpClientUtils;



/**
 * 支付公共表
 * 
 * @版本更新列表
 * @author gwu
 *         <pre>
 *   &#64;
 * 	修改版本: 1.0.0
 * 	修改日期：2018年6月04日
 * 	修改说明：形成初始版本
 * </pre>
 */
@Service
public class XftPaymentServiceImpl implements XftPaymentService {
	private static final Logger logger = LoggerFactory.getLogger(XftPaymentServiceImpl.class);
	@Autowired
	private XftPaymentMapper xftPaymentMapper;
	
	@Autowired
	AliPayService aliPayService;
	
	@Autowired
	WechatNativPayService wechatNativPayService;

	@Autowired
	RouteOrderMapper  routeOrderMapper;
	@Autowired
	SybPayService sybPayService;
	
	@Override
	public XftPayment get(Integer id) {
		// TODO Auto-generated method stub
		return xftPaymentMapper.selectByPrimaryKey(id);
	}

	@Override
	public void save(XftPayment xtp) {
		// TODO Auto-generated method stub
		try {
			logger.info("save..........servicr.....:"+JSONUtils.beanToJson(xtp));	
			xftPaymentMapper.insert(xtp);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public XftPayment selectByNo(String payNo, String payType) {
		// TODO Auto-generated method stub
		return xftPaymentMapper.selectByNo(payNo, payType);
	}

	@Override
	public void updatePayment(XftPayment xtp) {
		// TODO Auto-generated method stub
		xftPaymentMapper.updateByPrimaryKeySelective(xtp);
	}
	
	/**
	 * 
	 */
	@Override
	public ReturnData   isOverPay(HttpServletRequest req, Map<String, Object> paramsMap, String body) {
		// TODO Auto-generated method stub
		ReturnData returnData=new ReturnData();
	try {
		returnData.setOpenid(paramsMap.get("openid").toString());
		returnData.setBody(body);
		returnData.setPayNo(paramsMap.get("paymentNo").toString());
		returnData.setPayType(paramsMap.get("payType").toString());
		returnData.setIp(paramsMap.get("ip").toString());
		returnData.setNoticeURL(paramsMap.get("noticeURL").toString());
		//returnData.setReturnURL(paramsMap.get("returnURL").toString());
		returnData.setReturnURL(paramsMap.get("returnURL")==null?"":paramsMap.get("returnURL").toString());
		returnData.setCode("0000");
		//如果是线路订单就拿订单最新
		if("WR".equals(paramsMap.get("payType").toString())) {
			RouteOrder  ro=routeOrderMapper.selectByPaymentNo(paramsMap.get("paymentNo").toString());
			returnData.setTotalFee(new BigDecimal(ro.getDiscountAmt()).setScale(2, BigDecimal.ROUND_HALF_UP));
		}else {
			returnData.setTotalFee(new BigDecimal(paramsMap.get("payAmount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		logger.info("check_orderId==================================:"+paramsMap.get("paymentNo").toString());
		XftPayment  xft=xftPaymentMapper.selectByNo(paramsMap.get("paymentNo").toString(),  paramsMap.get("payType").toString());
		if(xft!=null&&!xft.getPaymentNo().equals("")) {
			//判断微信
			String wxStr=wechatNativPayService.isPayOverAlready(paramsMap.get("paymentNo").toString(),paramsMap.get("payType").toString());
			Map<String, Object> wxMap=JSONUtils.jsonToMap(wxStr);
			//判断支付宝
			String zfbStr =aliPayService.searchOrderByOrderID(paramsMap.get("paymentNo").toString(),paramsMap.get("payType").toString());
			Map<String, Object> zfbMap=JSONUtils.jsonToMap(zfbStr);
			logger.info("==============1111==pagrams wxStr:**********************" + JSON.toJSONString(wxStr));
			logger.info("==============1111==pagrams zfbMap:**********************" + JSON.toJSONString(zfbMap));
			if(null!=wxMap&&"SUCCESS".equals(wxMap.get("code"))) {
				//查下xft的状态是否有修改
				xft.setReturntime(new Date());
				xft.setStatus(PayStatus.PAID);
				xft.setPtType(PayType.WECHATZF);
				xft.setTransactionId(wxMap.get("transactionId").toString());
				updatePayment(xft);
				
				Map<String,String> returnMap=new HashMap<String ,String>();
				returnMap.put("transactionId", wxMap.get("transactionId").toString());
				returnMap.put("orderId", xft.getPayNo());
				returnMap.put("status", PayStatus.PAID);
				returnMap.put("payType", PayType.WECHATZF);
				returnMap.put("msg", "success");
				returnMap.put("payAmount", xft.getPayAmount().toString());
				
				logger.info("wechat-------pagrams map:**********************" + JSON.toJSONString(returnMap));
				HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), req, returnMap);
				
				returnData.setCode("5050");
				returnData.setReturnURL(paramsMap.get("returnURL").toString());
				returnData.setMsg(xft.getPayNo()+"该订单已支付成功，请勿重复支付");
			//支付宝判断	
			}else if(null!=zfbMap&&"10000".equals(zfbMap.get("code"))&&"TRADE_SUCCESS".equals(zfbMap.get("trade_status"))) {
				xft.setReturntime(new Date());
				xft.setStatus(PayStatus.PAID);
				xft.setPtType(PayType.ZFBZF);
				xft.setTransactionId(zfbMap.get("trade_no").toString());
				updatePayment(xft);
				Map<String,String> returnZfbMap=new HashMap<String ,String>();
				returnZfbMap.put("transactionId", zfbMap.get("trade_no").toString());
				returnZfbMap.put("orderId", xft.getPayNo());
				returnZfbMap.put("payType", PayType.ZFBZF);
				returnZfbMap.put("status", PayStatus.PAID);
				returnZfbMap.put("msg", "success");
				returnZfbMap.put("payAmount", xft.getPayAmount().toString());
				logger.info("zhifubao-------pagrams map:**********************" + JSON.toJSONString(returnZfbMap));
				HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), req, returnZfbMap);
				
				returnData.setCode("5050");
				returnData.setReturnURL(paramsMap.get("returnURL").toString());
				returnData.setMsg(xft.getPayNo()+"该订单已支付成功，请勿重复支付");
			}
			
		}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return returnData;
	}
	
	
	/**
	 * 
	 * @param no
	 * @param payType
	 * @return 查看线路订单信息
	 */
	@Override
	public  RouteOrder selectRouteOrderByPaymentNo(String no) {
		return routeOrderMapper.selectByPaymentNo(no);
		
	}
	

	@Override
	public ReturnData   isOverPay2(HttpServletRequest req, Map<String, Object> paramsMap, String body) {
		// TODO Auto-generated method stub
		ReturnData returnData=new ReturnData();
	try {
		returnData.setOpenid(paramsMap.get("openid").toString());
		returnData.setBody(body);
		returnData.setPayNo(paramsMap.get("paymentNo").toString());
		returnData.setPayType(paramsMap.get("payType").toString());
		returnData.setIp(paramsMap.get("ip").toString());
		returnData.setNoticeURL(paramsMap.get("noticeURL").toString());
		returnData.setReturnURL(paramsMap.get("returnURL")==null?"":paramsMap.get("returnURL").toString());
		returnData.setCode("0000");
		
		logger.info("闯过来参数======"+JSON.toJSONString(returnData));
		//如果是线路订单就拿订单最新
		if("WR".equals(paramsMap.get("payType").toString())) {
			RouteOrder  ro=routeOrderMapper.selectByPaymentNo(paramsMap.get("paymentNo").toString());
			returnData.setTotalFee(new BigDecimal(ro.getDiscountAmt()).setScale(2, BigDecimal.ROUND_HALF_UP));
		}else {
			returnData.setTotalFee(new BigDecimal(paramsMap.get("payAmount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		logger.info("check_orderId==================================:"+paramsMap.get("paymentNo").toString());
		XftPayment  xft=xftPaymentMapper.selectByNo(paramsMap.get("paymentNo").toString(),  paramsMap.get("payType").toString());
		if(xft!=null&&!xft.getPaymentNo().equals("")) {
			//判断是否支付
			 Map<String,Object> payResultMap = sybPayService.query(paramsMap.get("payType").toString() + paramsMap.get("paymentNo").toString(),"");
			boolean isPay = false;
			if (null != payResultMap 
					&& "SUCCESS".equals(payResultMap.get("retcode")) 
					&& "0000".equals(payResultMap.get("trxstatus"))
					&& "2008".equals(payResultMap.get("trxstatus"))
					&& "2000".equals(payResultMap.get("trxstatus"))
					&& "".equals(payResultMap.get("trxstatus"))) {
				//交易成功或交易中
				isPay = true;
			}
			
			
			if(isPay) {
				//查下xft的状态是否有修改
				xft.setReturntime(new Date());
				xft.setStatus(PayStatus.PAID);
				String payType = "";
				if ("VSP501".equals(payResultMap.get("trxcode"))) {
					payType = PayType.WECHATZF;
				} else if ("VSP511".equals(payResultMap.get("trxcode"))) {
					payType = PayType.ZFBZF;
				}
				xft.setPtType(payType);
				xft.setTransactionId(payResultMap.get("chnltrxid").toString());
				updatePayment(xft);
				
				Map<String,String> returnMap=new HashMap<String ,String>();
				returnMap.put("transactionId", payResultMap.get("chnltrxid").toString());
				returnMap.put("orderId", xft.getPayNo());
				returnMap.put("status", PayStatus.PAID);
				returnMap.put("payType", payType);
				returnMap.put("msg", "success");
				returnMap.put("payAmount", xft.getPayAmount().toString());
				logger.info("pagrams map:**********************" + JSON.toJSONString(returnMap));
				HttpClientUtils.sendHttpPostJson(xft.getNotifyURL(), req, returnMap);
				
				returnData.setCode("5050");
				returnData.setReturnURL(paramsMap.get("returnURL").toString());
				returnData.setMsg(xft.getPayNo()+"该订单已支付成功，请勿重复支付");
			}
			
		}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return returnData;
	}
	
}
