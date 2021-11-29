package top.easyboot.dao.auto.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import top.easyboot.dao.auto.model.Signature;

@Mapper
public interface SignatureMapper {
    @Delete({
        "delete from signature",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into signature (id, app_id, ",
        "secret, create_time, ",
        "update_time)",
        "values (#{id,jdbcType=BIGINT}, #{appId,jdbcType=VARCHAR}, ",
        "#{secret,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(Signature record);

    @InsertProvider(type=SignatureSqlProvider.class, method="insertSelective")
    int insertSelective(Signature record);

    @Select({
        "select",
        "id, app_id, secret, create_time, update_time",
        "from signature",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="app_id", property="appId", jdbcType=JdbcType.VARCHAR),
        @Result(column="secret", property="secret", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    Signature selectByPrimaryKey(Long id);

    @UpdateProvider(type=SignatureSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Signature record);

    @Update({
        "update signature",
        "set app_id = #{appId,jdbcType=VARCHAR},",
          "secret = #{secret,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Signature record);

    @Select({
            "select",
            "id, app_id, secret, create_time, update_time",
            "from signature",
            "where app_id = #{app_id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="app_id", property="appId", jdbcType=JdbcType.VARCHAR),
            @Result(column="secret", property="secret", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    Signature selectByAppId(@Param("app_id") String appId);
}