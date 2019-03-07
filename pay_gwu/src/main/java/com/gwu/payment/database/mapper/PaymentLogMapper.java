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

import com.gwu.payment.database.entity.PaymentLog;
@Mapper
public interface PaymentLogMapper {
    @Delete({
        "delete from xft_payment_log",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into xft_payment_log (id, operNo, ",
        "beforContent, afterContent, ",
        "datetime)",
        "values (#{id,jdbcType=INTEGER}, #{operNo,jdbcType=VARCHAR}, ",
        "#{beforContent,jdbcType=VARCHAR}, #{afterContent,jdbcType=VARCHAR}, ",
        "#{datetime,jdbcType=TIMESTAMP})"
    })
    int insert(PaymentLog record);

    @InsertProvider(type=PaymentLogSqlProvider.class, method="insertSelective")
    int insertSelective(PaymentLog record);

    @Select({
        "select",
        "id, operNo, beforContent, afterContent, datetime",
        "from xft_payment_log",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="operNo", property="operNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="beforContent", property="beforContent", jdbcType=JdbcType.VARCHAR),
        @Result(column="afterContent", property="afterContent", jdbcType=JdbcType.VARCHAR),
        @Result(column="datetime", property="datetime", jdbcType=JdbcType.TIMESTAMP)
    })
    PaymentLog selectByPrimaryKey(Integer id);

    @UpdateProvider(type=PaymentLogSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(PaymentLog record);

    @Update({
        "update xft_payment_log",
        "set operNo = #{operNo,jdbcType=VARCHAR},",
          "beforContent = #{beforContent,jdbcType=VARCHAR},",
          "afterContent = #{afterContent,jdbcType=VARCHAR},",
          "datetime = #{datetime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(PaymentLog record);
}