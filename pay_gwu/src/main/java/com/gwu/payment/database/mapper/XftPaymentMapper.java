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

import com.gwu.payment.database.entity.XftPayment;
@Mapper
public interface XftPaymentMapper {
    @Delete({
        "delete from xft_payment",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into xft_payment ( payNo, ",
        "payType, payAmount, ",
        "paymentNo, createtime, ",
        "returntime, transactionId, ",
        "status, flag,",
         "notifyURL,ptType ) ",
        "values ( #{payNo,jdbcType=VARCHAR}, ",
        " #{payType,jdbcType=VARCHAR}, #{payAmount,jdbcType=DECIMAL}, ",
        " #{paymentNo,jdbcType=VARCHAR}, #{createtime,jdbcType=TIMESTAMP}, ",
        " #{returntime,jdbcType=TIMESTAMP}, #{transactionId,jdbcType=VARCHAR},",
        " #{status,jdbcType=VARCHAR}, #{flag,jdbcType=TINYINT},",
         " #{notifyURL,jdbcType=VARCHAR},#{ptType,jdbcType=VARCHAR} )"
    }) 
    void insert(XftPayment record);
    
    
    
    
    

    @InsertProvider(type=XftPaymentSqlProvider.class, method="insertSelective")
    int insertSelective(XftPayment record);

    
    @Select({
        "select",
        "id, payNo, payType, payAmount, paymentNo, createtime, returntime, transactionId, ",
        "status, flag,ptType",
        "from xft_payment",
        "where id = #{id,jdbcType=INTEGER}"
    })
    
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="payNo", property="payNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="payType", property="payType", jdbcType=JdbcType.VARCHAR),
        @Result(column="payAmount", property="payAmount", jdbcType=JdbcType.DECIMAL),
        @Result(column="paymentNo", property="paymentNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="createtime", property="createtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="returntime", property="returntime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="transactionId", property="transactionId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="flag", property="flag", jdbcType=JdbcType.TINYINT)
    })
    XftPayment selectByPrimaryKey(Integer id);
    

    @Select({
        "select",
        "id, payNo, payType, payAmount, paymentNo, notifyURL,createtime, returntime, transactionId, ",
        "status, flag,ptType",
        "from xft_payment",
        "where payNo = #{payNo,jdbcType=VARCHAR}  and payType = #{payType,jdbcType=VARCHAR}"
    })
    XftPayment selectByNo( @Param("payNo") String payNo,@Param("payType") String payType);
    
    
    
    @UpdateProvider(type=XftPaymentSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(XftPayment record);

    @Update({
        "update xft_payment",
        "set payNo = #{payNo,jdbcType=VARCHAR},",
          "payType = #{payType,jdbcType=VARCHAR},",
          "payAmount = #{payAmount,jdbcType=DECIMAL},",
          "paymentNo = #{paymentNo,jdbcType=VARCHAR},",
          "createtime = #{createtime,jdbcType=TIMESTAMP},",
          "returntime = #{returntime,jdbcType=TIMESTAMP},",
          "transactionId = #{transactionId,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=TINYINT},",
          "ptType = #{ptType,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(XftPayment record);
    
    
    
    @Update({
        "update xft_payment",
        "set returntime = #{returntime,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=VARCHAR},",
          "flag = #{flag,jdbcType=TINYINT},",
           "transactionId = #{transactionId,jdbcType=VARCHAR},",
           "ptType = #{ptType,jdbcType=VARCHAR}",
           "where paymentNo = #{paymentNo,jdbcType=VARCHAR}"
    })
    
    void updatePayment(XftPayment record);
    
    
    /* 获取超时30分钟的支付信息
    * @param params
    * @return
    */
   @SelectProvider(type=XftPaymentSqlProvider.class, method="getTimeOutList")
   List<XftPayment> getTimeOutList(Map<String, Object> params);
   
    
}