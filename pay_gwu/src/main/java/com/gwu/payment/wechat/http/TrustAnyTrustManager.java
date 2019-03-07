package com.gwu.payment.wechat.http;




import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author gwu
 * 证书管理类
 *
 */
public class TrustAnyTrustManager implements X509TrustManager{
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		
	}
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		
	}
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}