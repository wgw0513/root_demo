package com.gwu.payment.database.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.gwu.payment.database.entity.NoticeTemplate;
import com.gwu.payment.database.entity.NoticeUser;
@Mapper
public interface NoticeUserMapper {
    @Delete({
        "delete from notice_user",
        "where nuid = #{nuid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer nuid);

    @Insert({
        "insert into notice_user (nuid, uid, ",
        "openid, nickName, ",
        "createtime, updatetime, ",
        "status)",
        "values (#{nuid,jdbcType=INTEGER}, #{uid,jdbcType=BIGINT}, ",
        "#{openid,jdbcType=VARCHAR}, #{nickName,jdbcType=VARCHAR}, ",
        "#{createtime,jdbcType=TIMESTAMP}, #{updatetime,jdbcType=TIMESTAMP}, ",
        "#{status,jdbcType=INTEGER})"
    })
    int insert(NoticeUser record);
    
    /**
     * 获取系统自增ID
     * @return
     */
    @Select({ "SELECT @@IDENTITY " })
    int getAutoId();

    
    @InsertProvider(type=NoticeUserSqlProvider.class, method="insertSelective")
    int insertSelective(NoticeUser record);

    @Select({
        "select",
        "nuid, uid, openid, nickName, createtime, updatetime, status",
        "from notice_user",
        "where nuid = #{nuid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="nuid", property="nuid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="uid", property="uid", jdbcType=JdbcType.BIGINT),
        @Result(column="openid", property="openid", jdbcType=JdbcType.VARCHAR),
        @Result(column="nickName", property="nickName", jdbcType=JdbcType.VARCHAR),
        @Result(column="createtime", property="createtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updatetime", property="updatetime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    NoticeUser selectByPrimaryKey(Integer nuid);
    
    @Select({
        "select",
        "nuid, uid, openid, nickName, createtime, updatetime, status",
        "from notice_user",
        "where openid = #{openid,jdbcType=VARCHAR}  and  uid = #{uid,jdbcType=BIGINT}  and status = #{status,jdbcType=INTEGER}"
    })
    NoticeUser selectByNo(@Param("openid") String openid,@Param("uid") Long uid,@Param("status") Integer status);
    

    @Select({
        "select",
        "nuid, uid, openid, nickName, createtime, updatetime, status",
        "from notice_user",
        "where openid = #{openid,jdbcType=VARCHAR}  and status = #{status,jdbcType=INTEGER}"
    })
    NoticeUser selectByOpenId(@Param("openid") String openid,@Param("status") Integer status);
    
    

    
    @UpdateProvider(type=NoticeUserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(NoticeUser record);

    @Update({
        "update notice_user",
        "set uid = #{uid,jdbcType=BIGINT},",
          "openid = #{openid,jdbcType=VARCHAR},",
          "nickName = #{nickName,jdbcType=VARCHAR},",
          "createtime = #{createtime,jdbcType=TIMESTAMP},",
          "updatetime = #{updatetime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=INTEGER}",
        "where nuid = #{nuid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(NoticeUser record);
    
    
    /**
     * 获取有效发送用户
     * @param params
     * @return
     */
    @SelectProvider(type=NoticeUserSqlProvider.class, method="getIviList")
    List<NoticeUser> getIviList(Map<String, Object> params);
    
    
}