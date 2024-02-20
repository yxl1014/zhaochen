package org.simple.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.simple.entity.User;

/**
 * @author yxl17
 * @Package : org.simple.mapper
 * @Create on : 2024/2/20 20:05
 **/

@Mapper
public interface UserMapper {

    /**
     * 通过uuid查询用户
     * @param uuid uuid
     * @return user
     */
    @Select("select * from user where uuid = #{uuid}")
    User findUserByUuid(@Param("uuid") String uuid);

    /**
     * 通过uuid查询用户
     * @param tel tel
     * @return user
     */
    @Select("select * from user where userTel = #{tel}")
    User findUserByTel(@Param("tel") String tel);


    /**
     * 通过电话密码查询用户
     * @param tel tel
     * @param pwd pwd
     * @return user
     */
    @Select("select * from user where userTel = #{tel} and userPwd = #{pwd}")
    User findUserByTelAndPwd(@Param("tel") String tel,@Param("pwd") String pwd);

    /**
     * 添加用户
     * @param user 用户
     * @return 成功条数
     */
    @Insert("insert into user(uuid,userTel,userPwd) " +
            "values(#{uuid},#{userTel},#{userPwd})")
    int insertUser(User user);
}
