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

import com.gwu.payment.database.entity.NoticeUserSetting;
@Mapper
public interface NoticeUserSettingMapper {
    @Delete({
        "delete from notice_user_setting",
        "where settingId = #{settingId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer settingId);

    @Insert({
        "insert into notice_user_setting (settingId, nuid, ",
        "tmpId, status)",
        "values (#{settingId,jdbcType=INTEGER}, #{nuid,jdbcType=INTEGER}, ",
        "#{tmpId,jdbcType=INTEGER}, #{status,jdbcType=INTEGER})"
    })
    int insert(NoticeUserSetting record);

    @InsertProvider(type=NoticeUserSettingSqlProvider.class, method="insertSelective")
    int insertSelective(NoticeUserSetting record);

    @Select({
        "select",
        "settingId, nuid, tmpId, status",
        "from notice_user_setting",
        "where settingId = #{settingId,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="settingId", property="settingId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="nuid", property="nuid", jdbcType=JdbcType.INTEGER),
        @Result(column="tmpId", property="tmpId", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    NoticeUserSetting selectByPrimaryKey(Integer settingId);

    @UpdateProvider(type=NoticeUserSettingSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(NoticeUserSetting record);

    @Update({
        "update notice_user_setting",
        "set nuid = #{nuid,jdbcType=INTEGER},",
          "tmpId = #{tmpId,jdbcType=INTEGER},",
          "status = #{status,jdbcType=INTEGER}",
        "where settingId = #{settingId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(NoticeUserSetting record);
}