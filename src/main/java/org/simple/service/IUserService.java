package org.simple.service;

import org.simple.entity.ReData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author yxl17
 * @Package : org.simple.service
 * @Create on : 2024/2/19 20:34
 **/
public interface IUserService {
    /**
     * 注册
     *
     * @param userTel 用户电话
     * @param userPwd 用户密码
     * @return 成功与否
     */
    boolean register(String userTel, String userPwd);


    /**
     * 登录
     *
     * @param userTel 用户电话
     * @param userPwd 用户密码
     * @return uuid
     */
    String login(String userTel, String userPwd);

    /**
     * 上传文件
     *
     * @param uuid        uuid
     * @param inputStream 文件流
     * @param filename    文件名
     * @return uuid
     * @throws IOException io
     */
    String upload(String uuid, InputStream inputStream, String filename) throws IOException;

    /**
     * 保存数据
     *
     * @param userUuid 用户uid
     * @param dataUuid 数据uid
     * @return 成功失败
     */
    boolean saveData(String userUuid, String dataUuid);

    /**
     * 获取用户全部数据
     *
     * @param userUuid userid
     * @return 数据集
     */
    List<ReData> listUserData(String userUuid);

    /**
     * 获取所有公开数据
     *
     * @return 数据集
     */
    List<ReData> listAllData();


    /**
     * 修改数据状态
     *
     * @param userUuid 用户uid
     * @param dataUuid 数据uid
     * @param state 状态
     * @return 成功
     */
    boolean updateDataState(String userUuid, String dataUuid, Integer state);


    /**
     * copy数据
     *
     * @param userUuid 用户uid
     * @param dataUuid 数据uid
     * @return uuid
     */
    String copyData(String userUuid, String dataUuid);


    /**
     * 删除数据
     *
     * @param userUuid 用户uid
     * @param dataUuid 数据uid
     * @return 成功
     */
    boolean deleteData(String userUuid, String dataUuid);

    /**
     * 查询
     * @param userUuid uuid
     * @param name name
     * @return 数据
     */
    List<ReData> findUserData(String userUuid, String name);

    /**
     * 查询
     * @param name name
     * @return 数据
     */
    List<ReData> findAllData(String name);
}
