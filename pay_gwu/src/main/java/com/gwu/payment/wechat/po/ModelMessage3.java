package com.gwu.payment.wechat.po;

/**
 * 
 * @author gwu
 * 投保失败通知
 */
public class ModelMessage3 {
	/**
	 * 商户号
	 */
	private Long uid;
	
	/**
	 * 详情url
	 */
	private String url;
	
	/**
	 * 首字说明
	 */
	private String  first;
	
	/**
	 * 保险产品
	 */
	private String keyword1;
	/**
	 * 投保人
	 */
	private String keyword2;
	/**
	 * 投保时间
	 */
	private String keyword3;

	/**
	 * 备注
	 */
	private  String remark;

	
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}

	public String getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}

	public String getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	

}
