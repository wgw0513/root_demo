package com.gwu.payment.wechat.po;

/**
 * 
 * @author gwu
 * 报名活动通知
 */
public class ModelMessage2 {
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
	 * 活动名称
	 */
	private String keyword1;
	/**
	 * 活动时间
	 */
	private String keyword2;
	/**
	 * 活动地点
	 */
	private String keyword3;
	/**
	 * 报名详情
	 */
	private String keyword4;
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

	public String getKeyword4() {
		return keyword4;
	}

	public void setKeyword4(String keyword4) {
		this.keyword4 = keyword4;
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
