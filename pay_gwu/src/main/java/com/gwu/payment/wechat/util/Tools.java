package com.gwu.payment.wechat.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;

import com.gwu.payment.util.JSONUtils;





/**
 * 
 * @author gwu
 *读取文件内容工具类
 */
public class Tools {
	private static final Logger logger = LoggerFactory.getLogger(Tools.class);
	protected static HttpServletRequest request;

	public static final String inputStream2String(InputStream in)
			throws java.io.IOException {
		if (in == null) {
			return "";
		}	
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n, "UTF-8"));
		}
		return out.toString();
	}
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {  
        // 将解析结果存储在HashMap中  
        Map<String, String> map = new HashMap<String, String>();  
  
        // 从request中取得输入流  
        InputStream inputStream = request.getInputStream();  
        // 读取输入流  
        SAXReader reader = new SAXReader();  
        Document document = reader.read(inputStream);  
        // 得到xml根元素  
        Element root = document.getRootElement();  
        // 得到根元素的所有子节点  
        List<Element> elementList = root.elements();  
  
        // 遍历所有子节点  
        for (Element e : elementList)  
            map.put(e.getName(), e.getText());  
  
        // 释放资源  
        inputStream.close();  
        inputStream = null;  
  
        return map;  
    }  

	// 修改xml里面属性值
	
	public static  int updateXml(String root,String filePath,Map<String, String> map) {
		try {
			String strResult = "";
			String projectPath = System.getProperty("user.dir").replaceAll(
					"\\\\", "/");
			projectPath = splitString(projectPath, "/bin");
			strResult = projectPath + "/webapps/web/";
			strResult = strResult.replaceAll("file:/", "");
			strResult = strResult.replaceAll("%20", " ");
			strResult = strResult.trim() + filePath.trim();
			if (strResult.indexOf(":") != 1) {
				strResult = File.separator + strResult;
			}
			
			Document doc = new SAXReader().read(new File(strResult));
			 Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			  while (it.hasNext()) {
			   Map.Entry<String, String> entry = it.next();
			   Node node = doc.selectSingleNode(root+"/"+entry.getKey());
				node.setText(entry.getValue());
			  }
			  
			// 指定文件输出的位置
			FileOutputStream out = new FileOutputStream(strResult);
			// 指定文本的写出的格式：
			OutputFormat format = OutputFormat.createPrettyPrint(); // 漂亮格式：有空格换行
			format.setEncoding("UTF-8");
			// 1.创建写出对象
			XMLWriter writer = new XMLWriter(out, format);
			// 2.写出Document对象
			writer.write(doc);
			// 3.关闭流
			writer.close();
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	//读取xml属性
	public static String redXml(String filePath){
		String strResult = "";
		String projectPath = System.getProperty("user.dir").replaceAll(
				"\\\\", "/");
		projectPath = splitString(projectPath, "/bin");
		strResult = projectPath + "/webapps/web/";
		strResult = strResult.replaceAll("file:/", "");
		strResult = strResult.replaceAll("%20", " ");
		strResult = strResult.trim() + filePath.trim();
		if (strResult.indexOf(":") != 1) {

			strResult = File.separator + strResult;
		}
		
		Map<String,String> retMap=new HashMap<String, String>();
		try{
		 org.dom4j.Document document =new SAXReader().read(new File(strResult));
		  // 获取根结点对象
       Element rootElement = document.getRootElement();
       // 循环根节点，获取其子节点
       for (Iterator iter = rootElement.elementIterator(); iter.hasNext();) {
           Element element = (Element) iter.next(); // 获取标签对象
           // 循环第一层节点，获取其子节点
           // 获取标签对象
           // 获取该标签对象的名称
           String tagName = element.getName();
           // 获取该标签对象的内容
           String tagContent = element.getTextTrim();
           retMap.put(tagName, tagContent);
       }

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
       String jsonData = JSONUtils.beanToJson(retMap);
		return jsonData;
	}
	
		//读取xml属性2
		public static Map<String,String> redXmlMap(String filePath){
			String strResult = "";
			String projectPath = System.getProperty("user.dir").replaceAll(
					"\\\\", "/");
			projectPath = splitString(projectPath, "/bin");
			strResult = projectPath + "/webapps/web/";
			strResult = strResult.replaceAll("file:/", "");
			strResult = strResult.replaceAll("%20", " ");
			strResult = strResult.trim() + filePath.trim();
			if (strResult.indexOf(":") != 1) {

				strResult = File.separator + strResult;
			}
			
			Map<String,String> retMap=new HashMap<String, String>();
			try{
			 org.dom4j.Document document =new SAXReader().read(new File(strResult));
			  // 获取根结点对象
	       Element rootElement = document.getRootElement();
	       // 循环根节点，获取其子节点
	       for (Iterator iter = rootElement.elementIterator(); iter.hasNext();) {
	           Element element = (Element) iter.next(); // 获取标签对象
	           // 循环第一层节点，获取其子节点
	           // 获取标签对象
	           // 获取该标签对象的名称
	           String tagName = element.getName();
	           // 获取该标签对象的内容
	           String tagContent = element.getTextTrim();
	           retMap.put(tagName, tagContent);
	       }

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return retMap;
		}
		
	
	/**
	 * 读取txt里的单行内容
	 * 
	 * @param filePath
	 *            文件路径 by gwu 2016-08 15
	 */
	public static String readTxtFile(String fileP) {
		try {

			String strResult = "";
			String projectPath = System.getProperty("user.dir").replaceAll(
					"\\\\", "/");
			projectPath = splitString(projectPath, "/bin");
			strResult = projectPath + "/webapps/web/";
			strResult = strResult.replaceAll("file:/", "");
			strResult = strResult.replaceAll("%20", " ");
			strResult = strResult.trim() + fileP.trim();
			if (strResult.indexOf(":") != 1) {

				strResult = File.separator + strResult;
			}
			String encoding = "utf-8";
			File file = new File(strResult);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				String re = "";
				while ((lineTxt = bufferedReader.readLine()) != null) {
					re += lineTxt;
				}
				read.close();
				return re;
			} else {
				System.out.println("找不到指定的文件,查看此路径是否正确:" + strResult);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return "";
	}
	
	public static String returnWebPath(String fileP){
		String strResult = "";
		String projectPath = System.getProperty("user.dir").replaceAll(
				"\\\\", "/");
		projectPath = splitString(projectPath, "/bin");
		strResult = projectPath;
		strResult = strResult.replaceAll("file:/", "");
		strResult = strResult.replaceAll("%20", " ");
		strResult = strResult.trim() + fileP.trim();
		if (strResult.indexOf(":") != 1) {

			strResult = File.separator + strResult;
		}
		
		return strResult;
	}

	private static String splitString(String str, String param) {
		String result = str;

		if (str.contains(param)) {
			int start = str.indexOf(param);
			result = str.substring(0, start);
		}

		return result;
	}
	
	/**
	 * description: 解析微信通知xml
	 * 
	 * @param xml
	 * @return
	 * @author gwu
	 * @see
	 */
	public static Map parseXmlToList2(String xml) {
		logger.info("################################noyify params :" + xml);
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			org.jdom2.Document doc = (org.jdom2.Document) sb.build(source);
			org.jdom2.Element root = doc.getRootElement();// 指向根节点
			List<org.jdom2.Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (org.jdom2.Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}
	
	
	public static String getIp(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		String ip = request.getHeader("X-Requested-For");
		
		if (null!=request.getHeader("X-Forwarded-For")&&request.getHeader("X-Forwarded-For").contains(",")) {
			ip = request.getHeader("X-Forwarded-For").split(",")[0];
		} else {
			ip = request.getHeader("X-Forwarded-For");
		}
		if ((StringUtils.isEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((StringUtils.isEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((StringUtils.isEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if ((StringUtils.isEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if ((StringUtils.isEmpty(ip)) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 获取http post请求参数
	 * @param req
	 * @return
	 */
	public static Map<String, Object> getData(HttpServletRequest req) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			req.setCharacterEncoding("UTF-8");
			reader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return JSONUtils.jsonToMap(buffer.toString());
	}
	
	
	/**
	 * 获取http post请求参数
	 * @param req
	 * @return
	 */
	public static String getStringData(HttpServletRequest req) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			req.setCharacterEncoding("UTF-8");
			reader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}
	
	
	 @SuppressWarnings("rawtypes")  
	    public static boolean isEmpty(Object obj)  
	    {  
	        if (obj == null)  
	        {  
	            return true;  
	        }  
	        if ((obj instanceof List))  
	        {  
	            return ((List) obj).size() == 0;  
	        }  
	        if ((obj instanceof String))  
	        {  
	            return ((String) obj).trim().equals("");  
	        }  
	        return false;  
	    }  
}
