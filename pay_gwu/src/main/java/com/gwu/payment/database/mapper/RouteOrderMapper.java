package com.gwu.payment.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.gwu.payment.database.entity.RouteOrder;


@Mapper
public interface RouteOrderMapper {

    @Select({
        "select * ",
        "from web_route_order",
        "where paymentNo = #{paymentNo,jdbcType=VARCHAR}"
    })
    RouteOrder selectByPaymentNo(String paymentNo);
    
   
}