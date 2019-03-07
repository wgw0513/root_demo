package com.gwu.payment.database.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.gwu.payment.database.entity.NoticeUserLog;

public class NoticeUserLogSqlProvider {

    public String insertSelective(NoticeUserLog record) {
        BEGIN();
        INSERT_INTO("notice_user_log");
        
        if (record.getLogId() != null) {
            VALUES("logId", "#{logId,jdbcType=INTEGER}");
        }
        
        if (record.getTmpId() != null) {
            VALUES("tmpId", "#{tmpId,jdbcType=INTEGER}");
        }
        
        if (record.getTmpContent() != null) {
            VALUES("tmpContent", "#{tmpContent,jdbcType=VARCHAR}");
        }
        
        if (record.getMemo() != null) {
            VALUES("memo", "#{memo,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatetime() != null) {
            VALUES("createtime", "#{createtime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=INTEGER}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(NoticeUserLog record) {
        BEGIN();
        UPDATE("notice_user_log");
        
        if (record.getTmpId() != null) {
            SET("tmpId = #{tmpId,jdbcType=INTEGER}");
        }
        
        if (record.getTmpContent() != null) {
            SET("tmpContent = #{tmpContent,jdbcType=VARCHAR}");
        }
        
        if (record.getMemo() != null) {
            SET("memo = #{memo,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatetime() != null) {
            SET("createtime = #{createtime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getStatus() != null) {
            SET("status = #{status,jdbcType=INTEGER}");
        }
        
        WHERE("logId = #{logId,jdbcType=INTEGER}");
        
        return SQL();
    }
}