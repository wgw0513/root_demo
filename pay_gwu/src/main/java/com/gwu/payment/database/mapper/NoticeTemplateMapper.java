package com.gwu.payment.database.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.gwu.payment.database.entity.NoticeTemplate;

@Mapper
public interface NoticeTemplateMapper {
    @Delete({
        "delete from notice_template",
        "where tmpId = #{tmpId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer tmpId);

    @Insert({
        "insert into notice_template (tmpId, tmpName, ",
        "tmpResourceId, status)",
        "values (#{tmpId,jdbcType=INTEGER}, #{tmpName,jdbcType=VARCHAR}, ",
        "#{tmpResourceId,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER})"
    })
    int insert(NoticeTemplate record);

    @InsertProvider(type=NoticeTemplateSqlProvider.class, method="insertSelective")
    int insertSelective(NoticeTemplate record);

    @Select({
        "select",
        "tmpId, tmpName, tmpResourceId, status",
        "from notice_template",
        "where tmpId = #{tmpId,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="tmpId", property="tmpId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="tmpName", property="tmpName", jdbcType=JdbcType.VARCHAR),
        @Result(column="tmpResourceId", property="tmpResourceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    NoticeTemplate selectByPrimaryKey(Integer tmpId);

    @UpdateProvider(type=NoticeTemplateSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(NoticeTemplate record);

    @Update({
        "update notice_template",
        "set tmpName = #{tmpName,jdbcType=VARCHAR},",
          "tmpResourceId = #{tmpResourceId,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER}",
        "where tmpId = #{tmpId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(NoticeTemplate record);
    
    
    
    /**
     * 获取所有模版表
     * @param params
     * @return
     */
    @SelectProvider(type=NoticeTemplateSqlProvider.class, method="getList")
    List<NoticeTemplate> getList(Map<String, Object> params);
    
}