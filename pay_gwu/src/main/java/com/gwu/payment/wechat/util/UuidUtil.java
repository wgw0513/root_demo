package com.gwu.payment.wechat.util;

import java.util.UUID;
/**
 *
 * @author gwu
 * 获取uuid 工具类
 */
public class UuidUtil {

    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }
    
    /***
     * 
     * @return生成6位uuid
     */
    public static String get6UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "").substring(0, 6);
        return uuid;
    }
    
    
    public static void main(String[] args) {
        System.out.println(get32UUID());
    }
}