package com.gwu.payment.database.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.gwu.payment.database.entity.PaymentLog;

public class PaymentLogSqlProvider {

    public String insertSelective(PaymentLog record) {
        BEGIN();
        INSERT_INTO("xft_payment_log");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getOperNo() != null) {
            VALUES("operNo", "#{operNo,jdbcType=VARCHAR}");
        }
        
        if (record.getBeforContent() != null) {
            VALUES("beforContent", "#{beforContent,jdbcType=VARCHAR}");
        }
        
        if (record.getAfterContent() != null) {
            VALUES("afterContent", "#{afterContent,jdbcType=VARCHAR}");
        }
        
        if (record.getDatetime() != null) {
            VALUES("datetime", "#{datetime,jdbcType=TIMESTAMP}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(PaymentLog record) {
        BEGIN();
        UPDATE("xft_payment_log");
        
        if (record.getOperNo() != null) {
            SET("operNo = #{operNo,jdbcType=VARCHAR}");
        }
        
        if (record.getBeforContent() != null) {
            SET("beforContent = #{beforContent,jdbcType=VARCHAR}");
        }
        
        if (record.getAfterContent() != null) {
            SET("afterContent = #{afterContent,jdbcType=VARCHAR}");
        }
        
        if (record.getDatetime() != null) {
            SET("datetime = #{datetime,jdbcType=TIMESTAMP}");
        }
        
        WHERE("id = #{id,jdbcType=INTEGER}");
        
        return SQL();
    }
}