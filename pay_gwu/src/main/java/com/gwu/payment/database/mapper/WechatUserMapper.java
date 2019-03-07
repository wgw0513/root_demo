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

import com.gwu.payment.database.entity.WechatUser;
@Mapper
public interface WechatUserMapper {
    @Delete({
        "delete from xft_wechat_user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into xft_wechat_user (id, openid, ",
        "nickname, sex, language, ",
        "city, province, ",
        "country, headimgurl, ",
        "remark, groupid, ",
        "status, createtime)",
        "values (#{id,jdbcType=INTEGER}, #{openid,jdbcType=VARCHAR}, ",
        "#{nickname,jdbcType=VARCHAR}, #{sex,jdbcType=VARCHAR}, #{language,jdbcType=VARCHAR}, ",
        "#{city,jdbcType=VARCHAR}, #{province,jdbcType=VARCHAR}, ",
        "#{country,jdbcType=VARCHAR}, #{headimgurl,jdbcType=VARCHAR}, ",
        "#{remark,jdbcType=VARCHAR}, #{groupid,jdbcType=INTEGER}, ",
        "#{status,jdbcType=INTEGER}, #{createtime,jdbcType=TIMESTAMP})"
    })
    int insert(WechatUser record);

    @InsertProvider(type=WechatUserSqlProvider.class, method="insertSelective")
    int insertSelective(WechatUser record);

    @Select({
        "select",
        "id, openid, nickname, sex, language, city, province, country, headimgurl, remark, ",
        "groupid, status, createtime",
        "from xft_wechat_user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="openid", property="openid", jdbcType=JdbcType.VARCHAR),
        @Result(column="nickname", property="nickname", jdbcType=JdbcType.VARCHAR),
        @Result(column="sex", property="sex", jdbcType=JdbcType.VARCHAR),
        @Result(column="language", property="language", jdbcType=JdbcType.VARCHAR),
        @Result(column="city", property="city", jdbcType=JdbcType.VARCHAR),
        @Result(column="province", property="province", jdbcType=JdbcType.VARCHAR),
        @Result(column="country", property="country", jdbcType=JdbcType.VARCHAR),
        @Result(column="headimgurl", property="headimgurl", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="groupid", property="groupid", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="createtime", property="createtime", jdbcType=JdbcType.TIMESTAMP)
    })
    WechatUser selectByPrimaryKey(Integer id);
    
    @Select({
        "select",
        "id, openid, nickname, sex, language, city, province, country, headimgurl, remark, ",
        "groupid, status, createtime",
        "from xft_wechat_user",
        "where openid = #{openid,jdbcType=VARCHAR}"
    })
    WechatUser selectByOpenId(String openId);

    @UpdateProvider(type=WechatUserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(WechatUser record);

    @Update({
        "update xft_wechat_user",
        "set openid = #{openid,jdbcType=VARCHAR},",
          "nickname = #{nickname,jdbcType=VARCHAR},",
          "sex = #{sex,jdbcType=VARCHAR},",
          "language = #{language,jdbcType=VARCHAR},",
          "city = #{city,jdbcType=VARCHAR},",
          "province = #{province,jdbcType=VARCHAR},",
          "country = #{country,jdbcType=VARCHAR},",
          "headimgurl = #{headimgurl,jdbcType=VARCHAR},",
          "remark = #{remark,jdbcType=VARCHAR},",
          "groupid = #{groupid,jdbcType=INTEGER},",
          "status = #{status,jdbcType=INTEGER},",
          "createtime = #{createtime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WechatUser record);
}