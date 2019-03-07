package com.gwu.payment.database.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import java.util.Map;

import com.gwu.payment.database.entity.NoticeUserSetting;

public class NoticeUserSettingSqlProvider {

    public String insertSelective(NoticeUserSetting record) {
        BEGIN();
        INSERT_INTO("notice_user_setting");
        
        if (record.getSettingId() != null) {
            VALUES("settingId", "#{settingId,jdbcType=INTEGER}");
        }
        
        if (record.getNuid() != null) {
            VALUES("nuid", "#{nuid,jdbcType=INTEGER}");
        }
        
        if (record.getTmpId() != null) {
            VALUES("tmpId", "#{tmpId,jdbcType=INTEGER}");
        }
        
        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=INTEGER}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(NoticeUserSetting record) {
        BEGIN();
        UPDATE("notice_user_setting");
        
        if (record.getNuid() != null) {
            SET("nuid = #{nuid,jdbcType=INTEGER}");
        }
        
        if (record.getTmpId() != null) {
            SET("tmpId = #{tmpId,jdbcType=INTEGER}");
        }
        
        if (record.getStatus() != null) {
            SET("status = #{status,jdbcType=INTEGER}");
        }
        
        WHERE("settingId = #{settingId,jdbcType=INTEGER}");
        
        return SQL();
    }
    
    
    
    
    
 
    
}