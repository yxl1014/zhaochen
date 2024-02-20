package org.simple.mapper;

import org.apache.ibatis.annotations.*;
import org.simple.entity.UD;

/**
 * @author yxl17
 * @Package : org.simple.mapper
 * @Create on : 2024/2/20 21:06
 **/

@Mapper
public interface UdMapper {
    /**
     * 添加数据
     * @param ud 数据
     * @return 成功条数
     */
    @Insert("insert into u_d(userId,dataId) " +
            "values(#{userId},#{dataId})")
    int insertUd(UD ud);

    /**
     * 查询数据
     * @param userId 用户id
     * @param dataId 数据id
     * @return 数据
     */
    @Select("select * from u_d where userId = #{userId} and dataId = #{dataId}")
    UD findUdByUserIdAndDataId(@Param("userId") String userId, @Param("dataId") String dataId);

    /**
     * 删除数据
     * @param userId 用户id
     * @param dataId 数据id
     * @return 数据
     */
    @Delete("delete from u_d where userId = #{userId} and dataId = #{dataId}")
    int deleteUdByUserIdAndDataId(@Param("userId") String userId, @Param("dataId") String dataId);
}
