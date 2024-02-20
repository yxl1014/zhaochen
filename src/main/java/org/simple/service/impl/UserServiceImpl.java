package org.simple.service.impl;

import com.google.gson.Gson;
import org.simple.entity.ReData;
import org.simple.entity.UD;
import org.simple.entity.User;
import org.simple.entity.UserData;
import org.simple.mapper.UdMapper;
import org.simple.mapper.UserDataMapper;
import org.simple.mapper.UserMapper;
import org.simple.service.IUserService;
import org.simple.util.ExcelBuilder;
import org.simple.util.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author yxl17
 * @Package : org.simple.service.impl
 * @Create on : 2024/2/19 20:34
 **/

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDataMapper userDataMapper;

    @Autowired
    private UdMapper udMapper;

    private Gson gson = new Gson();

    @Override
    public boolean register(String userTel, String userPwd) {
        User user = userMapper.findUserByTel(userTel);
        if (user != null) {
            return false;
        }

        User newUser = new User(UuidGenerator.getCustomUuid(), userTel, userPwd);

        int ok = userMapper.insertUser(newUser);

        return ok == 1;
    }

    @Override
    public String login(String userTel, String userPwd) {
        User user = userMapper.findUserByTelAndPwd(userTel, userPwd);
        if (user == null) {
            return null;
        }
        return user.getUuid();
    }

    @Override
    public String upload(String uuid, InputStream inputStream, String filename) throws IOException {
        User user = userMapper.findUserByUuid(uuid);
        if (user == null) {
            return null;
        }
        // 提取数据
        ExcelBuilder excelBuilder = new ExcelBuilder(inputStream);

        ReData reData = new ReData();
        HashMap<String, Object[]> map = new HashMap<>();
        map.put("sheetName", excelBuilder.getSheetName().toArray());
        map.put("top", excelBuilder.getTopData().toArray());
        map.put("length", excelBuilder.getLength().toArray());
        reData.setXAxis(map);

        reData.setEntity(Arrays.asList(excelBuilder.getBody().toArray()));

        String dataJson = gson.toJson(reData);
        // 提取xml数据
        byte[] data = dataJson.getBytes(StandardCharsets.UTF_8);

        // 获取文件名
        System.out.println("文件名称：" + filename);

        // 生成数据uuid
        String dataUuid = UuidGenerator.getCustomUuid();

        int ok1 = udMapper.insertUd(new UD(uuid, dataUuid));
        if (ok1 == 0) {
            return null;
        }

        UserData userData = new UserData(dataUuid, filename.split("\\.")[0], data, 0);
        int ok = userDataMapper.insertData(userData);
        return ok == 1 ? dataUuid : null;
    }

    @Override
    public boolean saveData(String userUuid, String dataUuid) {
        User user = userMapper.findUserByUuid(userUuid);
        if (user == null) {
            return false;
        }
        UD ud = udMapper.findUdByUserIdAndDataId(userUuid, dataUuid);
        if (ud == null) {
            return false;
        }
        UserData userData = userDataMapper.selectDataByUuid(dataUuid);
        if (userData == null) {
            return false;
        }
        int ok = userDataMapper.updateDataStateByUuid(1, dataUuid);
        return ok == 1;
    }

    @Override
    public List<ReData> listUserData(String userUuid) {
        User user = userMapper.findUserByUuid(userUuid);
        if (user == null) {
            return null;
        }

        List<UserData> userData = userDataMapper.selectDatasByUuid(userUuid);
        List<ReData> reDataList = new ArrayList<>();
        for (UserData data : userData) {
            String s = new String(data.getData(), StandardCharsets.UTF_8);
            ReData reData = gson.fromJson(s, ReData.class);
            reData.setState(data.getState());
            reDataList.add(reData);
        }

        return reDataList;
    }

    @Override
    public List<ReData> listAllData() {
        List<UserData> userData = userDataMapper.selectAllDataByState(2);
        List<ReData> reDataList = new ArrayList<>();
        for (UserData data : userData) {
            String s = new String(data.getData(), StandardCharsets.UTF_8);
            ReData reData = gson.fromJson(s, ReData.class);
            reDataList.add(reData);
        }

        return reDataList;
    }

    @Override
    public boolean updateDataState(String userUuid, String dataUuid, Integer state) {
        User user = userMapper.findUserByUuid(userUuid);
        if (user == null) {
            return false;
        }
        UD ud = udMapper.findUdByUserIdAndDataId(userUuid, dataUuid);
        if (ud == null) {
            return false;
        }
        UserData userData = userDataMapper.selectDataByUuid(dataUuid);
        if (userData == null) {
            return false;
        }
        if (state == 2 && userData.getState() == 3) {
            return false;
        }
        int ok = userDataMapper.updateDataStateByUuid(state, dataUuid);
        return ok == 1;
    }

    @Override
    public String copyData(String userUuid, String dataUuid) {
        User user = userMapper.findUserByUuid(userUuid);
        if (user == null) {
            return null;
        }
        UserData userData = userDataMapper.selectDataByUuid(dataUuid);
        if (userData == null) {
            return null;
        }
        String dataId = UuidGenerator.getCustomUuid();
        userData.setUuid(dataId);
        userData.setState(3);
        udMapper.insertUd(new UD(userUuid, dataId));
        userDataMapper.insertData(userData);
        return null;
    }

    @Override
    public boolean deleteData(String userUuid, String dataUuid) {
        User user = userMapper.findUserByUuid(userUuid);
        if (user == null) {
            return false;
        }
        UD ud = udMapper.findUdByUserIdAndDataId(userUuid, dataUuid);
        if (ud == null) {
            return false;
        }
        UserData userData = userDataMapper.selectDataByUuid(dataUuid);
        if (userData == null) {
            return false;
        }

        int ok = udMapper.deleteUdByUserIdAndDataId(userUuid, dataUuid);
        if (ok != 1) {
            return false;
        }

        return userDataMapper.deleteDataByUuid(dataUuid) == 1;
    }
}
