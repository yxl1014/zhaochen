package org.simple.mapper;

import org.apache.ibatis.annotations.*;
import org.simple.entity.UserData;

import java.util.List;

/**
 * @author yxl17
 * @Package : org.simple.mapper
 * @Create on : 2024/2/20 20:36
 **/

@Mapper
public interface UserDataMapper {

    /**
     * 添加数据
     *
     * @param data 数据
     * @return 成功条数
     */
    @Insert("insert into user_data(uuid,dataName,data,state,createTime) " +
            "values(#{uuid},#{dataName},#{data},#{state},#{createTime})")
    int insertData(UserData data);

    /**
     * 查询数据
     *
     * @param uuid 数据
     * @return 数据
     */
    @Select("select * from user_data where uuid = #{uuid}")
    UserData selectDataByUuid(@Param("uuid") String uuid);

    /**
     * 删除数据
     *
     * @param uuid 数据
     * @return 数据
     */
    @Delete("delete from user_data where uuid = #{uuid}")
    int deleteDataByUuid(@Param("uuid") String uuid);

    /**
     * 修改数据状态
     *
     * @param state 状态
     * @param uuid  数据id
     * @return 成功条数
     */
    @Update("update user_data set state = #{state} where uuid = #{uuid}")
    int updateDataStateByUuid(@Param("state") int state, @Param("uuid") String uuid);

    /**
     * 查询数据
     *
     * @param uuid 数据
     * @return 数据
     */
    @Select("select * from user_data join u_d u on user_data.uuid = u.dataId and u.userId = #{uuid}")
    List<UserData> selectDatasByUuid(@Param("uuid") String uuid);

    /**
     * 查询数据
     *
     * @param uuid 数据
     * @param name 数据
     * @return 数据
     */
    @Select("select * from user_data where state = #{state} and dataName like  concat('%',#{name},'%')")
    List<UserData> selectDatasByUuidAndName(@Param("uuid") String uuid, @Param("name") String name);

    /**
     * 查询数据
     *
     * @param state 数据
     * @return 数据
     */
    @Select("select * from user_data where state = #{state}")
    List<UserData> selectAllDataByState(@Param("state") int state);

    /**
     * 查询数据
     *
     * @param state 数据
     * @param name 数据
     * @return 数据
     */
    @Select("select * from user_data where state = #{state} and dataName like '% #{name}%'")
    List<UserData> selectAllDataByStateAndName(@Param("state") int state, @Param("name") String name);
}
