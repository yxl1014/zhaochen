package org.simple.controller;

import org.simple.entity.ReBody;
import org.simple.entity.ReData;
import org.simple.entity.User;
import org.simple.service.IUserService;
import org.simple.util.ExcelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author yxl17
 * @Package : org.simple.controller
 * @Create on : 2024/2/18 21:44
 **/

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/test")
    public void test() throws FileNotFoundException {
        FileInputStream inputStream = null;
        // 获取Excel文件
        File excelFile = new File("Test.xlsx");
        if (!excelFile.exists()) {
            return;
        }
        // 获取Excel工作簿
        inputStream = new FileInputStream(excelFile);
        ExcelBuilder excelBuilder = new ExcelBuilder(inputStream);
        System.out.println(Arrays.toString(excelBuilder.readExcelByRC(2, -1, true).toArray()));
    }

    @PostMapping("/testDatas")
    public ReBody testDatas(@RequestBody User user) throws FileNotFoundException {
        FileInputStream inputStream = null;
        // 获取Excel文件
        File excelFile = new File("Test.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        // 获取Excel工作簿
        inputStream = new FileInputStream(excelFile);
        ExcelBuilder excelBuilder = new ExcelBuilder(inputStream);

        ReData reData = new ReData();
        HashMap<String, Object[]> map = new HashMap<>();
        map.put("sheetName", excelBuilder.getSheetName("Test").toArray());
        map.put("top", excelBuilder.getTopData().toArray());
        map.put("length", excelBuilder.getLength().toArray());
        reData.setXAxis(map);

        reData.setEntity(Arrays.asList(excelBuilder.getBody().toArray()));
        return new ReBody(reData);
    }

    @GetMapping("/testTop")
    public ReBody testTop() throws FileNotFoundException {
        FileInputStream inputStream = null;
        // 获取Excel文件
        File excelFile = new File("Test.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        // 获取Excel工作簿
        inputStream = new FileInputStream(excelFile);
        ExcelBuilder excelBuilder = new ExcelBuilder(inputStream);
        List<List<Object>> topData = excelBuilder.getTopData();
        System.out.println(Arrays.toString(topData.toArray()));
        return new ReBody(topData);
    }

    @GetMapping("/testLength")
    public ReBody testLength() throws FileNotFoundException {
        FileInputStream inputStream = null;
        // 获取Excel文件
        File excelFile = new File("Test.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        // 获取Excel工作簿
        inputStream = new FileInputStream(excelFile);
        ExcelBuilder excelBuilder = new ExcelBuilder(inputStream);
        List<Integer> topData = excelBuilder.getLength();
        System.out.println(Arrays.toString(topData.toArray()));
        return new ReBody(topData);
    }


    @PostMapping("/register")
    public ReBody register(@RequestBody User user) {
        if (user == null || user.getUserTel().isEmpty() || user.getUserPwd().isEmpty()) {
            return new ReBody(-1, null);
        }
        boolean success = userService.register(user.getUserTel(), user.getUserPwd());
        return new ReBody(success ? 0 : 1, null);
    }

    @PostMapping("/login")
    public ReBody login(@RequestBody User user) {
        if (user == null || user.getUserTel().isEmpty() || user.getUserPwd().isEmpty()) {
            return new ReBody(-1, null);
        }
        String uuid = userService.login(user.getUserTel(), user.getUserPwd());
        if (uuid == null) {
            return new ReBody(1, null);
        }
        return new ReBody(0, uuid);
    }

    /**
     * 上传文件
     *
     * @param uuid 用户id
     */
    @PostMapping("/upload")
    public ReBody uploadSingleFile(@RequestParam("file") MultipartFile file, @RequestParam("uuid") String uuid) throws IOException {
        if (uuid.isEmpty() || file == null) {
            return new ReBody(-1, null);
        }
        // 在这里可以对上传的文件进行操作，比如保存到本地或者数据库等
        InputStream inputStream = file.getInputStream();

        String uid = userService.upload(uuid, inputStream, file.getOriginalFilename());
        if (uid == null) {
            return new ReBody(1, null);
        }
        return new ReBody(0, uid);
    }

    /**
     * 保存数据
     *
     * @param userUuid 用户id
     * @param dataUuid 数据id
     */
    @GetMapping("/saveData")
    public ReBody saveData(@RequestParam("userUuid") String userUuid, @RequestParam("dataUuid") String dataUuid) {
        if (userUuid.isEmpty() || dataUuid.isEmpty()) {
            return new ReBody(-1, null);
        }

        boolean ok = userService.saveData(userUuid, dataUuid);
        return new ReBody(ok ? 0 : 1, null);
    }

    /**
     * 获取用户所有数据
     *
     * @param userUuid 用户id
     */
    @GetMapping("/listUserData")
    public ReBody listUserData(@RequestParam("userUuid") String userUuid) {
        if (userUuid.isEmpty()) {
            return new ReBody(-1, null);
        }

        List<ReData> ok = userService.listUserData(userUuid);
        return new ReBody(0, ok);
    }

    /**
     * 查询用户数据
     *
     * @param userUuid 用户id
     */
    @GetMapping("/findUserData")
    public ReBody findUserData(@RequestParam("userUuid") String userUuid,@RequestParam("name")String name) {
        if (userUuid.isEmpty()) {
            return new ReBody(-1, null);
        }

        List<ReData> ok = userService.findUserData(userUuid,name);
        return new ReBody(0, ok);
    }


    /**
     * 获取公开的所有数据
     */
    @GetMapping("/listAllData")
    public ReBody listAllData() {
        List<ReData> ok = userService.listAllData();
        return new ReBody(0, ok);
    }

    /**
     * 查询公开的所有数据
     */
    @GetMapping("/findAllData")
    public ReBody findAllData(@RequestParam("name")String name) {
        List<ReData> ok = userService.findAllData(name);
        return new ReBody(0, ok);
    }

    /**
     * 修改玩家数据状态
     *
     * @param userUuid 玩家id
     * @param dataUuid 数据id
     * @param state    状态
     */
    @GetMapping("/updateDataState")
    public ReBody updateDataState(@RequestParam("userUuid") String userUuid, @RequestParam("dataUuid") String dataUuid, @RequestParam("state") Integer state) {
        if (userUuid.isEmpty() || dataUuid.isEmpty()) {
            return new ReBody(-1, null);
        }
        boolean ok = userService.updateDataState(userUuid, dataUuid, state);
        return new ReBody(ok ? 0 : 1, null);
    }

    /**
     * 玩家copy大厅数据
     *
     * @param userUuid 玩家id
     * @param dataUuid 数据id
     */
    @GetMapping("/copyData")
    public ReBody copyData(@RequestParam("userUuid") String userUuid, @RequestParam("dataUuid") String dataUuid) {
        if (userUuid.isEmpty() || dataUuid.isEmpty()) {
            return new ReBody(-1, null);
        }
        String uuid = userService.copyData(userUuid, dataUuid);
        return new ReBody(uuid != null ? 0 : 1, uuid);
    }

    /**
     * 玩家删除数据
     *
     * @param userUuid 玩家id
     * @param dataUuid 数据id
     */
    @GetMapping("/deleteData")
    public ReBody deleteData(@RequestParam("userUuid") String userUuid, @RequestParam("dataUuid") String dataUuid) {
        if (userUuid.isEmpty() || dataUuid.isEmpty()) {
            return new ReBody(-1, null);
        }
        boolean ok = userService.deleteData(userUuid, dataUuid);
        return new ReBody(ok ? 0 : 1, null);
    }
}
