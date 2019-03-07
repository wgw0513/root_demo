package com.gwu.payment.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.gwu.payment.Constant.PayStatus;
import com.gwu.payment.Constant.PayType;
import com.gwu.payment.alipay.service.AliPayService;
import com.gwu.payment.allinpay.service.SybPayService;
import com.gwu.payment.database.entity.PaymentLog;
import com.gwu.payment.database.entity.XftPayment;
import com.gwu.payment.database.mapper.PaymentLogMapper;
import com.gwu.payment.database.mapper.XftPaymentMapper;
import com.gwu.payment.util.ApplicationContextHolder;
import com.gwu.payment.util.JSONUtils;
import com.gwu.payment.wechat.service.WechatNativPayService;
import com.gwu.payment.wechat.util.HttpClientUtils;

/**
 * 定时任务--每天10分钟执行
 * 扫描所有支付表
 * 
 * @author gwu
 *
 */
@Component
@PropertySource({ "classpath:/httpClient.properties" })
public class SchedulePayMementService {
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulePayMementService.class);
	@Autowired
	PaymentLogMapper paymentLogMapper;
	
	@Autowired
	XftPaymentMapper xftPaymentMapper;

	@Autowired
	AliPayService aliPayService;
	
	@Autowired
	WechatNativPayService wechatNativPayService;
	
	@Autowired
	SybPayService sybPayService;
	
	@Value("${PAYMENT_METHOD}")
	private String paymentMethod;//新支付开关  NEW为开启
	
	@Scheduled(cron = "${SCHEDULE_CRON_HALFHOUR}")
	public void run() {
		System.out.println("启动定时器。。。");	
		if ("NEW".equals(paymentMethod)) {
			this.doScheduleNew();
		} else {
			this.doSchedule();
		}

	}
	
	public void doSchedule() {
		try {
			if (null == xftPaymentMapper) {
				xftPaymentMapper = ApplicationContextHolder.getBean(XftPaymentMapper.class);
			}
			if (null == paymentLogMapper) {
				paymentLogMapper = ApplicationContextHolder.getBean(PaymentLogMapper.class);
			}
			
			if (null == aliPayService) {
				aliPayService = ApplicationContextHolder.getBean(AliPayService.class);
			}
			if (null == wechatNativPayService) {
				wechatNativPayService = ApplicationContextHolder.getBean(WechatNativPayService.class);
			}
			
			
			List<XftPayment> xftList = xftPaymentMapper.getTimeOutList(null);
			for (int i = 0; i < xftList.size(); i++) {
				//修改前数据
				String  beforeJson=JSON.toJSONString(xftList.get(i));
				//修改后数据
				String  afterJSON="";
				Map<String, Object> wxMap=null;
				Map<String, Object> zfbMap=null;
					//判断微信
			      	String wxStr=wechatNativPayService.isPayOverAlready(xftList.get(i).getPayNo(), xftList.get(i).getPayType());
					if(null!=wxStr) {
						wxMap=JSONUtils.jsonToMap(wxStr);
					}
					//判断支付宝
					String zfbStr =aliPayService.searchOrderByOrderID(xftList.get(i).getPayNo(), xftList.get(i).getPayType());
					if(null!=zfbStr) {
						zfbMap=JSONUtils.jsonToMap(zfbStr);
					}
					if(null!=wxMap&&"SUCCESS".equals(wxMap.get("code"))) {
						//查下xft的状态是否有修改
						xftList.get(i).setReturntime(new Date());
						xftList.get(i).setStatus(PayStatus.PAID);
						xftList.get(i).setPtType(PayType.WECHATZF);
						xftList.get(i).setTransactionId(wxMap.get("transactionId").toString());
						
						xftPaymentMapper.updatePayment(xftList.get(i));
						//修改后数据
						 afterJSON=JSON.toJSONString(xftList.get(i));
						 
						Map<String,String> returnMap=new HashMap<String ,String>();
						returnMap.put("transactionId", wxMap.get("transactionId").toString());
						returnMap.put("orderId", xftList.get(i).getPayNo());
						returnMap.put("status", PayStatus.PAID);
						returnMap.put("payType", PayType.WECHATZF);
						returnMap.put("msg", "success");
						returnMap.put("payAmount", xftList.get(i).getPayAmount().toString());
						logger.info("pagrams map:**********************" + JSON.toJSONString(returnMap));
						HttpClientUtils.sendHttpPostJson(xftList.get(i).getNotifyURL(), null, returnMap);
						logger.error("excuteSchedule：schedule error(执行异常微信处理================)！");
					//支付宝判断	
					}else if(null!=zfbMap&&"10000".equals(zfbMap.get("code"))&&"TRADE_SUCCESS".equals(zfbMap.get("trade_status"))) {
					
						xftList.get(i).setReturntime(new Date());
						xftList.get(i).setStatus(PayStatus.PAID);
						xftList.get(i).setPtType(PayType.WECHATZF);
						xftList.get(i).setTransactionId(zfbMap.get("transactionId").toString());
						xftPaymentMapper.updatePayment(xftList.get(i));
						//修改后数据
						 afterJSON=JSON.toJSONString(xftList.get(i));
						 
						Map<String,String> returnZfbMap=new HashMap<String ,String>();
						returnZfbMap.put("transactionId", zfbMap.get("trade_no").toString());
						returnZfbMap.put("orderId", xftList.get(i).getPayNo());
						returnZfbMap.put("payType", PayType.ZFBZF);
						returnZfbMap.put("status", PayStatus.PAID);
						returnZfbMap.put("msg", "success");
						returnZfbMap.put("payAmount", xftList.get(i).getPayAmount().toString());
						logger.info("pagrams map:**********************" + JSON.toJSONString(returnZfbMap));
						logger.info("xft:**********************" + JSON.toJSONString(xftList.get(i)));
						HttpClientUtils.sendHttpPostJson(xftList.get(i).getNotifyURL(), null, returnZfbMap);
						logger.error("excuteSchedule：schedule error(执行异常支付宝处理================)！");
					}else {
						xftList.get(i).setStatus(PayStatus.CLOSE);
						xftPaymentMapper.updatePayment(xftList.get(i));
						//修改后数据
						 afterJSON=JSON.toJSONString(xftList.get(i));	
					}					
					//保存操作日志
					savePaymentLog(beforeJson ,afterJSON,xftList.get(i).getPaymentNo());
			}
			
		} catch (Exception e) {
			logger.error("excuteSchedule：schedule error(查询支付定时任务出现异常)！");
			logger.error(e.getLocalizedMessage(),e);
		}
		
	}
	
	public void doScheduleNew() {
		try {
			if (null == xftPaymentMapper) {
				xftPaymentMapper = ApplicationContextHolder.getBean(XftPaymentMapper.class);
			}
			if (null == paymentLogMapper) {
				paymentLogMapper = ApplicationContextHolder.getBean(PaymentLogMapper.class);
			}
			
			if (null == aliPayService) {
				aliPayService = ApplicationContextHolder.getBean(AliPayService.class);
			}
			if (null == wechatNativPayService) {
				wechatNativPayService = ApplicationContextHolder.getBean(WechatNativPayService.class);
			}
			if (null == sybPayService) {
				sybPayService = ApplicationContextHolder.getBean(SybPayService.class);
			}
			List<XftPayment> xftList = xftPaymentMapper.getTimeOutList(null);
			for (int i = 0; i < xftList.size(); i++) {
				//修改前数据
				String  beforeJson=JSON.toJSONString(xftList.get(i));
				//修改后数据
				String  afterJSON="";
				//判断是否支付
				Map<String,Object> payResultMap = sybPayService.query(xftList.get(i).getPayType() + xftList.get(i).getPayNo(),"");
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
						xftList.get(i).setReturntime(new Date());
						xftList.get(i).setStatus(PayStatus.PAID);
						String payType = "";
						if ("VSP501".equals(payResultMap.get("trxcode"))) {
							payType = PayType.WECHATZF;
						} else if ("VSP511".equals(payResultMap.get("trxcode"))) {
							payType = PayType.ZFBZF;
						}
						xftList.get(i).setPtType(payType);
						xftList.get(i).setTransactionId(payResultMap.get("chnltrxid").toString());
						
						xftPaymentMapper.updatePayment(xftList.get(i));
						//修改后数据
						 afterJSON=JSON.toJSONString(xftList.get(i));
						 
						Map<String,String> returnMap=new HashMap<String ,String>();
						returnMap.put("transactionId", payResultMap.get("chnltrxid").toString());
						returnMap.put("orderId", xftList.get(i).getPayNo());
						returnMap.put("status", PayStatus.PAID);
						returnMap.put("payType", payType);
						returnMap.put("msg", "success");
						returnMap.put("payAmount", xftList.get(i).getPayAmount().toString());
						logger.info("pagrams map:**********************" + JSON.toJSONString(returnMap));
						HttpClientUtils.sendHttpPostJson(xftList.get(i).getNotifyURL(), null, returnMap);
						logger.error("excuteSchedule：schedule error(执行异常处理================)！");
					//支付宝判断	
					} else {
						xftList.get(i).setStatus(PayStatus.CLOSE);
						xftPaymentMapper.updatePayment(xftList.get(i));
						//修改后数据
						 afterJSON=JSON.toJSONString(xftList.get(i));	
					}					
					//保存操作日志
					savePaymentLog(beforeJson ,afterJSON,xftList.get(i).getPaymentNo());
			}
			
		} catch (Exception e) {
			logger.error("excuteSchedule：schedule error(查询支付定时任务出现异常)！");
			logger.error(e.getLocalizedMessage(),e);
		}
		
	}
	
	
	/**
	 * 
	 * @param beforeJson 改变前数据
	 * @param afterJSON 改变后数据
	 * @param pno 单号
	 */
	public  void savePaymentLog(String beforeJson,String afterJSON,String pno) {
		PaymentLog plg=new PaymentLog();
		plg.setDatetime(new Date());
		plg.setBeforContent(beforeJson);
		plg.setAfterContent(afterJSON);
		plg.setOperNo(pno);
		paymentLogMapper.insert(plg);	
	}
	
	
}
