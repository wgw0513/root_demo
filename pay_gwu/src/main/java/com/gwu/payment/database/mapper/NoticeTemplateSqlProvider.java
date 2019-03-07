package com.gwu.payment.database.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import java.util.Map;

import com.gwu.payment.database.entity.NoticeTemplate;

public class NoticeTemplateSqlProvider {

    public String insertSelective(NoticeTemplate record) {
        BEGIN();
        INSERT_INTO("notice_template");
        
        if (record.getTmpId() != null) {
            VALUES("tmpId", "#{tmpId,jdbcType=INTEGER}");
        }
        
        if (record.getTmpName() != null) {
            VALUES("tmpName", "#{tmpName,jdbcType=VARCHAR}");
        }
        
        if (record.getTmpResourceId() != null) {
            VALUES("tmpResourceId", "#{tmpResourceId,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=INTEGER}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(NoticeTemplate record) {
        BEGIN();
        UPDATE("notice_template");
        
        if (record.getTmpName() != null) {
            SET("tmpName = #{tmpName,jdbcType=VARCHAR}");
        }
        
        if (record.getTmpResourceId() != null) {
            SET("tmpResourceId = #{tmpResourceId,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            SET("status = #{status,jdbcType=INTEGER}");
        }
        
        WHERE("tmpId = #{tmpId,jdbcType=INTEGER}");
        
        return SQL();
    }
    
    /**
     * 获取微信模版消息表
     * @param params
     * @return
     */
    public String getList(Map<String, Object> params){
    	String sql = "SELECT * FROM notice_template WHERE 1=1 ";
    	 if (params.get("status")!=null) {
         	sql+=" AND status=#{status,jdbcType=INTEGER}";
         }
        
    	 sql +=" ORDER BY tmpId asc";
         
        return sql;
    }
    
}