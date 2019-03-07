package com.gwu.payment.database.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.gwu.payment.database.entity.NoticeUserLog;
@Mapper
public interface NoticeUserLogMapper {
    @Delete({
        "delete from notice_user_log",
        "where logId = #{logId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer logId);

    @Insert({
        "insert into notice_user_log (logId, tmpId, ",
        "tmpContent, memo, ",
        "createtime, status)",
        "values (#{logId,jdbcType=INTEGER}, #{tmpId,jdbcType=INTEGER}, ",
        "#{tmpContent,jdbcType=VARCHAR}, #{memo,jdbcType=VARCHAR}, ",
        "#{createtime,jdbcType=TIMESTAMP}, #{status,jdbcType=INTEGER})"
    })
    int insert(NoticeUserLog record);

    @InsertProvider(type=NoticeUserLogSqlProvider.class, method="insertSelective")
    int insertSelective(NoticeUserLog record);

    @Select({
        "select",
        "logId, tmpId, tmpContent, memo, createtime, status",
        "from notice_user_log",
        "where logId = #{logId,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="logId", property="logId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="tmpId", property="tmpId", jdbcType=JdbcType.INTEGER),
        @Result(column="tmpContent", property="tmpContent", jdbcType=JdbcType.VARCHAR),
        @Result(column="memo", property="memo", jdbcType=JdbcType.VARCHAR),
        @Result(column="createtime", property="createtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    NoticeUserLog selectByPrimaryKey(Integer logId);

    @UpdateProvider(type=NoticeUserLogSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(NoticeUserLog record);

    @Update({
        "update notice_user_log",
        "set tmpId = #{tmpId,jdbcType=INTEGER},",
          "tmpContent = #{tmpContent,jdbcType=VARCHAR},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "createtime = #{createtime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=INTEGER}",
        "where logId = #{logId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(NoticeUserLog record);
}