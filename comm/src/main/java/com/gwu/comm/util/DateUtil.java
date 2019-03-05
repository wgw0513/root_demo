package com.gwu.comm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * 说明：日期处理
 * 创建人：gwu Q402920025
 * 修改时间：2019年03月05日
 * @version 1.0
 */
public class DateUtil {
	
	private final static SimpleDateFormat  sdfyear=new SimpleDateFormat("yyyy");
	private final static SimpleDateFormat sdfday=new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat sdfdays=new SimpleDateFormat("yyyyMMdd");
	private  final static SimpleDateFormat sdftime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private  final static SimpleDateFormat sdftimes=new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 
	 * @param date 当传null标识当前服务器时间
	 * @return  获取yyyy格式的年份
	 * @author gwu
	 */
	public static String getSdfYear(Date date) {
		if(date==null) {
			date=new Date();
		}
		
		return sdfyear.format(date);
	}
	
	
	/**
	 * 
	 * @param date 当传null标识当前服务器时间
	 * @return  获取yyyy-MM-dd格式的日期
	 * @author gwu
	 */
	public static String getSdfday(Date date) {
		if(date==null) {
			date=new Date();
		}
		
		
		return sdfday.format(date);
	}
	
	/**
	 * 
	 * @param date 当传null标识当前服务器时间
	 * @return  获取yyyyMMdd格式的日期
	 * @author gwu
	 */
	public static String getSdfdays(Date date) {
		if(date==null) {
			date=new Date();
		}
		return sdfdays.format(date);
	}
	
	/**
	 * 
	 * @param date 当传null标识当前服务器时间
	 * @return 获取yyyy-MM-dd HH:mm:ss格式的时间
	 * @author gwu
	 */
	public static String getSdftime(Date date) {
		if(date==null) {
			date=new Date();
		}
		return sdftime.format(date);
	}
	
	/**
	 * 
	 * @param date 当传null标识当前服务器时间
	 * @return 获取yyyyMMddHHmmss格式的时间
	 * @author gwu
	 */
	public static String getSdftimes(Date date) {
		if(date==null) {
			date=new Date();
		}
		return sdftimes.format(date);
	}
	
	
	/**
	 * 
	 * @param date
	 * @return 格式化日期
	 * @author gwu
	 */
	public static Date fomateDate(String date) {
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
		
	}
	
	/**
	 * 
	 * @param b 开始时间
	 * @param e 结束时间
	 * @author wu
	 * @return 比较日期大小  b>==e返回ture 
	 */
	public static  boolean compareDate(String b,String e) {
		if(fomateDate(b)==null||fomateDate(e)==null) {
			return false;
		}	
		return fomateDate(b).getTime()>=fomateDate(e).getTime();
	}
	

}
