package com.gwu.payment.wechat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpSession;

public class Test {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		HttpSession session=null;	
		 //获取文件的路径  
         
	        String proPath = System.getProperty("user.dir");
	        
	    	String path=Tools.returnWebPath("/apiclient_cert.p12");
	    	System.out.println(path);
	        FileInputStream instream = new FileInputStream(new File(path));//P12文件目录
	     
	        System.out.println(proPath);
		
	}

}
