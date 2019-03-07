package com.gwu.payment.util;



import java.security.SecureRandom;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/***
 * 
 * @author gwu
 *	AES加密工具类
 */
public class AesUtil {		
	/**支付加密key**/
	public final static String AESZF_KEY="gzhuanbokh_xft!@#";
	
	/**
	 * @author gwu
	 * @param str 加密的字符串
	 * @param key 双方约定的字符串
	 * @return加密
	 */
	public static String  lastenCrypt(String str,String key) {
		byte[] byteRe = null;
		try {
			
			byteRe = enCrypt(str,key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//加密过的二进制数组转化成16进制的字符串		
		return parseByte2HexStr(byteRe);

	}
	
	/**
	 * @author gwu
	 * @param encrytStr加密字符串
	 * key双方约定字符串
	 * @return解密
	 */
	public static String  lastDeCrypt(String encrytStr,String key) {
		byte[] encrytByte=null;
		String deCrypt=null;
		try {
			//加密过的16进制的字符串转化成二进制数组
			encrytByte = parseHexStr2Byte(encrytStr);	
			deCrypt=deCrypt(encrytByte,key);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//加密过的二进制数组转化成16进制的字符串		
		return deCrypt;

	}
	
	
	
	/**
	 * 加密函数
	 * @param content   加密的内容
	 * @param strKey    密钥
	 * @return  		返回二进制字符数组
	 * @throws Exception
	 */
	private  static byte[] enCrypt(String content,String strKey) throws Exception{
		KeyGenerator keygen;		
		SecretKey desKey;
		Cipher c;		
		byte[] cByte;
		String str = content;
		
		keygen = KeyGenerator.getInstance("AES");
		 SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	     random.setSeed(strKey.getBytes());
		keygen.init(128, random);
		desKey = keygen.generateKey();		
		c = Cipher.getInstance("AES");
		
		c.init(Cipher.ENCRYPT_MODE, desKey);
		
		cByte = c.doFinal(str.getBytes("UTF-8"));		
		
		return cByte;
	}
	
	/** 解密函数
	 * @param src   加密过的二进制字符数组
	 * @param strKey  密钥
	 * @return
	 * @throws Exception
	 */
	private  static  String deCrypt (byte[] src,String strKey) throws Exception{
		KeyGenerator keygen;		
		SecretKey desKey;
		Cipher c;		
		byte[] cByte;	
		
		keygen = KeyGenerator.getInstance("AES");
		 SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	     random.setSeed(strKey.getBytes());
		keygen.init(128, random);
		desKey = keygen.generateKey();
		c = Cipher.getInstance("AES");
		
		c.init(Cipher.DECRYPT_MODE, desKey);
		
		
		cByte = c.doFinal(src);	
		
		return new String(cByte,"UTF-8");
	}
	
	
	/**2进制转化成16进制
	 * @param buf
	 * @return
	 */
	private  static  String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
				}
			sb.append(hex.toUpperCase());
			}
		return sb.toString();
		}
	
	
	/**将16进制转换为二进制
	 * @param hexStr
	 * @return
	 */ 	
	private  static  byte[] parseHexStr2Byte(String hexStr) { 
	        if (hexStr.length() < 1) {
	                return null; 
	        }     
	        byte[] result = new byte[hexStr.length()/2]; 
	        for (int i = 0;i< hexStr.length()/2; i++) { 
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16); 
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16); 
	                result[i] = (byte) (high * 16 + low); 
	        } 
	        return result; 
	} 

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String str = "{\'paymentNo\':\'18060714251093310042\',\'openid\':\'olTM3wd2aAPOzTLLOjHd6M1Lw7Fk\',\'totalFee\':\'0.01\',\'body\':\'123\',\'ip\':\'172.20.1.130\',\'noticeURL\':\'123\',\'payType\':\'MG\'}";
	
		Map map=JSONUtils.jsonToMap(str);

		System.out.println("加密后："+lastenCrypt(str, AESZF_KEY));
		System.out.println("解密后："+lastDeCrypt(lastenCrypt(str,AESZF_KEY), AESZF_KEY));

	}
	

}