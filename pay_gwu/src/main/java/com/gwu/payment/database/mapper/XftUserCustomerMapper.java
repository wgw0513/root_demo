package com.gwu.payment.database.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.gwu.payment.database.entity.XftUserCustomer;
@Mapper
public interface XftUserCustomerMapper {
    @Delete({
        "delete from xft_user_customer",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into xft_user_customer (id, uid, ",
        "cid, createTime, ",
        "updateTime, status, ",
        "openid)",
        "values (#{id,jdbcType=INTEGER}, #{uid,jdbcType=INTEGER}, ",
        "#{cid,jdbcType=INTEGER}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{status,jdbcType=INTEGER}, ",
        "#{openid,jdbcType=VARCHAR})"
    })
    int insert(XftUserCustomer record);

    @InsertProvider(type=XftUserCustomerSqlProvider.class, method="insertSelective")
    int insertSelective(XftUserCustomer record);

    @Select({
        "select",
        "id, uid, cid, createTime, updateTime, status, openid",
        "from xft_user_customer",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="uid", property="uid", jdbcType=JdbcType.INTEGER),
        @Result(column="cid", property="cid", jdbcType=JdbcType.INTEGER),
        @Result(column="createTime", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updateTime", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="openid", property="openid", jdbcType=JdbcType.VARCHAR)
    })
    XftUserCustomer selectByPrimaryKey(Integer id);
    
    
    
    @Select({
        "select",
        "id, uid, cid, createTime, updateTime, status, openid",
        "from xft_user_customer",
        "where uid = #{uid,jdbcType=INTEGER} and  openid = #{openid,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="uid", property="uid", jdbcType=JdbcType.INTEGER),
        @Result(column="cid", property="cid", jdbcType=JdbcType.INTEGER),
        @Result(column="createTime", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updateTime", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="openid", property="openid", jdbcType=JdbcType.VARCHAR)
    })
    XftUserCustomer selectByuidOpenid(@Param("uid") Integer uid,@Param("openid") String openid);
    
    
    @UpdateProvider(type=XftUserCustomerSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(XftUserCustomer record);

    @Update({
        "update xft_user_customer",
        "set uid = #{uid,jdbcType=INTEGER},",
          "cid = #{cid,jdbcType=INTEGER},",
          "createTime = #{createTime,jdbcType=TIMESTAMP},",
          "updateTime = #{updateTime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=INTEGER},",
          "openid = #{openid,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(XftUserCustomer record);
}