package com.gwu.payment.database.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import java.util.Map;

import com.gwu.payment.database.entity.NoticeUser;

public class NoticeUserSqlProvider {

    public String insertSelective(NoticeUser record) {
        BEGIN();
        INSERT_INTO("notice_user");
        
        if (record.getNuid() != null) {
            VALUES("nuid", "#{nuid,jdbcType=INTEGER}");
        }
        
        if (record.getUid() != null) {
            VALUES("uid", "#{uid,jdbcType=BIGINT}");
        }
        
        if (record.getOpenid() != null) {
            VALUES("openid", "#{openid,jdbcType=VARCHAR}");
        }
        
        if (record.getNickName() != null) {
            VALUES("nickName", "#{nickName,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatetime() != null) {
            VALUES("createtime", "#{createtime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdatetime() != null) {
            VALUES("updatetime", "#{updatetime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=INTEGER}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(NoticeUser record) {
        BEGIN();
        UPDATE("notice_user");
        
        if (record.getUid() != null) {
            SET("uid = #{uid,jdbcType=BIGINT}");
        }
        
        if (record.getOpenid() != null) {
            SET("openid = #{openid,jdbcType=VARCHAR}");
        }
        
        if (record.getNickName() != null) {
            SET("nickName = #{nickName,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatetime() != null) {
            SET("createtime = #{createtime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdatetime() != null) {
            SET("updatetime = #{updatetime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getStatus() != null) {
            SET("status = #{status,jdbcType=INTEGER}");
        }
        
        WHERE("nuid = #{nuid,jdbcType=INTEGER}");
        
        return SQL();
    }
    
   /* 
    * 获取有效的发送微信用户，并且是模版消息可用的用户
    * @param params
    * @return
    */
   public String getIviList(Map<String, Object> params){
   	String sql = "SELECT  DISTINCT aa.* FROM  notice_user  aa ,(SELECT  b.*  ,c.tmpResourceId   FROM notice_user_setting  b,notice_template c WHERE  b.tmpId=c.tmpId  AND b.status=1 AND c.status=1 ) bb ";
   	sql+=	"   WHERE aa.nuid=bb.nuid  and aa.status=1 ";
   	
   	 if (params.get("tmpResourceId")!=null) {
        	sql+=" AND bb.tmpResourceId=#{tmpResourceId,jdbcType=VARCHAR}";
        }
   	 if (params.get("uid")!=null) {
         	sql+=" AND aa.uid=#{uid,jdbcType=BIGINT}";
         }
        
       return sql;
   }
    
    
}