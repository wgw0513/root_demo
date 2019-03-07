package com.gwu.payment.database.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.gwu.payment.database.entity.XftPayment;

public class XftPaymentSqlProvider {

    public String insertSelective(XftPayment record) {
        BEGIN();
        INSERT_INTO("xft_payment");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getPayNo() != null) {
            VALUES("payNo", "#{payNo,jdbcType=VARCHAR}");
        }
        
        if (record.getPayType() != null) {
            VALUES("payType", "#{payType,jdbcType=VARCHAR}");
        }
        
        if (record.getPayAmount() != null) {
            VALUES("payAmount", "#{payAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getPaymentNo() != null) {
            VALUES("paymentNo", "#{paymentNo,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatetime() != null) {
            VALUES("createtime", "#{createtime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getReturntime() != null) {
            VALUES("returntime", "#{returntime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getTransactionId() != null) {
            VALUES("transactionId", "#{transactionId,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=VARCHAR}");
        }
        
        if (record.getFlag() != null) {
            VALUES("flag", "#{flag,jdbcType=TINYINT}");
        }
        
        if (record.getNotifyURL() != null) {
            VALUES("notifyURL", "#{notifyURL,jdbcType=VARCHAR}");
        }
        
        if (record.getPtType() != null) {
            VALUES("ptType", "#{ptType,jdbcType=VARCHAR}");
        }
        
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(XftPayment record) {
        BEGIN();
        UPDATE("xft_payment");
        
        if (record.getPayNo() != null) {
            SET("payNo = #{payNo,jdbcType=VARCHAR}");
        }
        
        if (record.getPayType() != null) {
            SET("payType = #{payType,jdbcType=VARCHAR}");
        }
        
        if (record.getPayAmount() != null) {
            SET("payAmount = #{payAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getPaymentNo() != null) {
            SET("paymentNo = #{paymentNo,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatetime() != null) {
            SET("createtime = #{createtime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getReturntime() != null) {
            SET("returntime = #{returntime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getTransactionId() != null) {
            SET("transactionId = #{transactionId,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            SET("status = #{status,jdbcType=VARCHAR}");
        }
        
        if (record.getFlag() != null) {
            SET("flag = #{flag,jdbcType=TINYINT}");
        }
        
        if (record.getNotifyURL() != null) {
            SET("notifyURL = #{notifyURL,jdbcType=VARCHAR}");
        }
        
        if (record.getPtType() != null) {
            SET("ptType = #{ptType,jdbcType=VARCHAR}");
        }
        
        
        
        WHERE("id = #{id,jdbcType=INTEGER}");
        
        return SQL();
    }
    
    
    
    /**
     * 获取30分钟未支付的表
     * @param params
     * @return
     */
    public String getTimeOutList(Map<String, Object> params){
    	String sql = "SELECT  * FROM  xft_payment  WHERE UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(createtime) >1800   AND  STATUS='00' ";
    	
        return sql;
    }
    
    
    
}