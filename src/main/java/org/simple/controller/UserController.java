package org.simple.controller;

import org.simple.entity.ReBody;
import org.simple.entity.ReData;
import org.simple.entity.User;
import org.simple.entity.UserData;
import org.simple.service.IUserService;
import org.simple.util.ExcelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

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

    @PostMapping("/upload")
    public String uploadSingleFile(@RequestParam("file") MultipartFile file, @RequestParam("uuid") String uuid, @RequestParam("name") String name) throws IOException {
        // 在这里可以对上传的文件进行操作，比如保存到本地或者数据库等
        InputStream inputStream = file.getInputStream();
        ExcelBuilder excelBuilder = new ExcelBuilder(inputStream);
        String string = Arrays.toString(excelBuilder.readExcelByRC(2, -1, true).toArray());
        System.out.println(name + "\t" + string);
        return string;
    }

    @PostMapping("/login")
    public ReBody login(@RequestBody User user) throws FileNotFoundException {
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
        map.put("sheetName", excelBuilder.getSheetName().toArray());
        map.put("top",excelBuilder.getTopData().toArray());
        map.put("length",excelBuilder.getLength().toArray());
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

}
