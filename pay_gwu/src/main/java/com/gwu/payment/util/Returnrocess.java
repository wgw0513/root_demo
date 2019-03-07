package com.gwu.payment.util;

import java.util.HashMap;
import java.util.Map;
/**
 * @author gwu
 *返回状态代码
 */
public class Returnrocess {
	public static Map<String, String> returnCode = new HashMap<String, String>();
	/**支付宝返回状态码对应信息*/
	static {
		returnCode.put("10000", "成功");
		returnCode.put("20000", "服务不可用（稍后重试）");
		returnCode.put("20001", "授权权限不足");
		returnCode.put("40001", "缺少必选参数");
		returnCode.put("40002", "非法的参数");
		returnCode.put("40004", "业务处理失败");
		returnCode.put("40006", "权限不足");
		returnCode.put("5050", "重复支付");
		returnCode.put("0000", "待支付");
		
	}
}
