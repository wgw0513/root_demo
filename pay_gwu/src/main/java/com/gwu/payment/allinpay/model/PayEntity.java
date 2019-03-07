package com.gwu.payment.allinpay.model;

/**
 * 统一支付请求参数
 * @author HP
 *
 */
public class PayEntity {
	/**商户号； 实际交易的商户号； 不可为空；  最大长度15位*/
	private String cusid;
	/**应用ID；平台分配的APPID；不可为空；最大长度8位*/
	private String appid;
	/**版本号；接口版本号；可位空；最大长度2位；默认填11*/
	private String version;
	/**交易金额；单位为分；不可为空；最大长度15位*/
	private String trxamt;
	/**商户交易单号；商户的交易订单号；不可为空；最大长度32；保证商户平台唯一*/
	private String reqsn;
	/**交易方式；详见constant；不可为空；最大长度3位*/
	private String paytype;
	/**随机字符串；商户自行生成的随机字符串；不可为空；最大长度32位*/
	private String randomstr;
	/**订单标题；订单商品名称，为空则以商户名作为商品名称；可为空；最大长度100位（最大100个字节(50个中文字符)）*/
	private String body;
	/**备注；备注信息；可位空；最大长度160位（最大160个字节(80个中文字符)）*/
	private String remark;
	/**有效时间；订单有效时间，以分为单位，不填默认为15分钟；可为空；最大长度2位；最大60分钟*/
	private String validtime;
	/**支付授权码；微信或者支付宝的被扫刷卡支付时,用户的付款二维码；可为空；最大长度32位*/
	private String authcode;
	/**支付平台用户标识；JS支付时使用 ：1微信支付-用户的微信openid 2支付宝支付-用户user_id 2微信小程序-用户小程序的openid；可位空，最大长度32位*/
	private String acct;
	/**交易结果通知地址；接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数；可为空；最大长度256位；因为刷卡支付交易结果实时返回,因此对于刷卡支付，该字段无效*/
	private String notify_url;
	/**支付限制；no_credit--指定不能使用信用卡支付；可为空；最大长度32位；暂时只对微信支付和支付宝有效,仅支持no_credit*/
	private String limit_pay;
	/**微信子appid；微信小程序/微信公众号/APP的appid*/
	private String sub_appid;
	/**门店号；可为空；最大长度4位*/
	private String subbranch;
	/**终端ip；用户下单和调起支付的终端ip地址；可为空；最大长度16位；payType=U02云闪付JS支付不为空*/
	private String cusip;
	/**证件号；实名交易必填.填了此字段就会验证证件号和姓名；可为空；最大长度32位；暂只支持支付宝支付,微信支付(微信支付的刷卡支付除外)*/
	private String idno;
	/**付款人真实姓名；实名交易必填.填了此字段就会验证证件号和姓名；可为空；最大长度32位；暂只支持支付宝支付,微信支付(微信支付的刷卡支付除外)*/
	private String truename;
	/**分账信息；格式: cusid:type:amount;cusid:type:amount…
	 *其中 cusid:接收分账的通联商户号 type分账类型（01：按金额  02：按比率） 如果分账类型为02，则分账比率为0.5表示50%。如果分账类型为01，则分账金额以元为单位表示
	 *可为空；最大长度1024位；开通此业务需开通分账配置*/
	private String asinfo;
	/**签名方式；可为空；最大长度8位；MD5 RSA 不填默认MD5*/
	private String signtype;
	/**签名；不可为空；最大长度32位；详见底部注释备注*/
	private String sign;
	
	public String getCusid() {
		return cusid;
	}
	public void setCusid(String cusid) {
		this.cusid = cusid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTrxamt() {
		return trxamt;
	}
	public void setTrxamt(String trxamt) {
		this.trxamt = trxamt;
	}
	public String getReqsn() {
		return reqsn;
	}
	public void setReqsn(String reqsn) {
		this.reqsn = reqsn;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getRandomstr() {
		return randomstr;
	}
	public void setRandomstr(String randomstr) {
		this.randomstr = randomstr;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getValidtime() {
		return validtime;
	}
	public void setValidtime(String validtime) {
		this.validtime = validtime;
	}
	public String getAuthcode() {
		return authcode;
	}
	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}
	public String getAcct() {
		return acct;
	}
	public void setAcct(String acct) {
		this.acct = acct;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getLimit_pay() {
		return limit_pay;
	}
	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}
	public String getSub_appid() {
		return sub_appid;
	}
	public void setSub_appid(String sub_appid) {
		this.sub_appid = sub_appid;
	}
	public String getSubbranch() {
		return subbranch;
	}
	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
	}
	public String getCusip() {
		return cusip;
	}
	public void setCusip(String cusip) {
		this.cusip = cusip;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getAsinfo() {
		return asinfo;
	}
	public void setAsinfo(String asinfo) {
		this.asinfo = asinfo;
	}
	public String getSigntype() {
		return signtype;
	}
	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	/**
	 * 1、签名算法
签名生成的通用步骤如下：
第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数以及key按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string。
特别注意以下重要规则：
◆ 参数名ASCII码从小到大排序（字典序）；
◆ 如果参数的值为空不参与签名，该参数包括收银宝平台的key；
◆ 参数名区分大小写；
◆ 验证收银宝主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。
◆ 收银宝平台接口可能增加字段，验证签名时必须支持增加的扩展字段
第二步，对string进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。
◆ key设置路径：收银宝商户服务平台(https://vsp.allinpay.com)-->设置-->对接配置-->系统对接参数-->交易密钥
举例：
假设传送的参数如下：

appid=00000000
cusid =990440153996000
paytype =0
trxamt=1
reqsn =1450432107647
randomstr =1450432107647
body=商品名称
remark =备注信息

商户服务平台设置的交易密钥如下：
key=43df939f1e7f5c6909b3f4b63f893994

第一步：对参数按照key=value的格式，并按照参数名ASCII字典序排序，得到string如下：appid=00000000&body=商品名称&cusid=990440153996000&key=43df939f1e7f5c6909b3f4b63f893994&paytype=0&randomstr=1450432107647&remark=备注信息&reqsn=1450432107647&trxamt=1
第二步：对签名数据为sign=md5(string.getbyte("utf-8")).toUpperCase()=”1918CC7DBBD120B1BB130C9400186F79”;
第三步：验证收银宝平台通知的sign字段数据域signValue是否一致，如一致则进行下一步具体业务逻辑处理
 

2、生成随机数算法
网关支付API接口协议中包含字段randomstr，主要保证签名不可预测。我们推荐生成随机数算法如下：调用随机数函数生成，将得到的值转换为字符串。

3、对接API安全
在普通的网络环境下，HTTP请求存在DNS劫持、运营商插入广告、数据被窃取，正常数据被修改等安全风险。商户对接接口使用HTTPS协议可以保证数据传输的安全性。所以收银宝平台建议商户提供给订单支付的各种对接采用HTTPS协议。
	 */
}
