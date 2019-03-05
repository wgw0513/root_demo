package com.gwu.comm.util;

import java.util.UUID;

/** 
 * 说明：生成uuid工具类
 * 创建人：gwu Q402920025
 * 修改时间：2019年03月05日
 * @version 1.0
 */
public class UuidUtil {
	/**
	 * 
	 * @return生成32位的uuid字符串
	 * @author gwu
	 */
	public static String get32UUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
		
		
	}
	

}
