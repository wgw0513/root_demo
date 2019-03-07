package com.gwu.payment.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.gwu.payment.Vo.ReturnData;
import com.gwu.payment.database.entity.RouteOrder;
import com.gwu.payment.database.entity.XftPayment;
/**
 * 
 * @author gwu
 *支付表service
 */
public interface XftPaymentService{
	/**
	 * 
	 * @param id
	 * @return 根据主键获取支付表信息
	 */
	 XftPayment get(Integer id);
	 /**
	  * 
	  * @param xtp
	  * 保存支付表信息
	  */
	void save(XftPayment xtp);
	
	/**
	 * 
	 * @param payNo 系统订单号
	 * @param payType 系统订单类型
	 * @return 查询支付表信息
	 */
	XftPayment selectByNo(String payNo,String payType);
	/**
	 * 
	 * @param xtp
	 * 	编辑支付表
	 * @category
	 */
	void updatePayment(XftPayment xtp);
	
	/**
	 * 
	 * @param payNo 系统订单号
	 * @param payType 系统订单类型
	 * @return 判断是已经支付过
	 */
	ReturnData isOverPay(HttpServletRequest req, Map<String, Object> paramsMap, String body);
	
	/**
	 * 
	 * @param payNo 系统订单号
	 * @param payType 系统订单类型
	 * @return 判断是已经支付过
	 */
	ReturnData isOverPay2(HttpServletRequest req, Map<String, Object> paramsMap, String body);
	/**
	 * 
	 * @param no
	 * @param payType
	 * @return 查看线路订单 信息
	 */
	RouteOrder selectRouteOrderByPaymentNo(String no);
	
}
