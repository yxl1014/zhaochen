package org.simple.util;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yxl17
 * @Package : org.simple.util
 * @Create on : 2024/2/18 22:15
 **/
public class ExcelUtil {
    public static ExcelUtil INSTANCE = new ExcelUtil();

    private ExcelUtil() {
    }

    /**
     * 日志输出
     */
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    /**
     * 定义excel类型
     */
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     *
     * @param inputStream 读取文件的输入流
     * @param fileType    文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     */
    private Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        //用自带的方法新建工作薄
        return WorkbookFactory.create(inputStream);
    }


    /**
     * 将单元格内容转换为字符串
     *
     * @param cell 单元格
     * @return
     */
    private String convertCellValueToString(Cell cell) {
        if (cell == null) {
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            //数字
            case NUMERIC:
                Double doubleValue = cell.getNumericCellValue();
                // 格式化科学计数法，取一位整数，如取小数，值如0.0,取小数点后几位就写几个0
                DecimalFormat df = new DecimalFormat("0");
                returnValue = df.format(doubleValue);
                break;
            //字符串
            case STRING:
                returnValue = cell.getStringCellValue();
                break;
            //布尔
            case BOOLEAN:
                boolean booleanValue = cell.getBooleanCellValue();
                returnValue = Boolean.toString(booleanValue);
                break;
            // 空值
            case BLANK:
                break;
            // 公式
            case FORMULA:
                returnValue = cell.getCellFormula();
                break;
            // 故障
            case ERROR:
                break;
            default:
                break;
        }
        return returnValue;
    }

    /**
     * 处理Excel内容转为List<Map<String,Object>>输出
     * workbook：已连接的工作薄
     * StatrRow：读取的开始行数（默认填0，0开始,传过来是EXcel的行数值默认从1开始，这里已处理减1）
     * EndRow：读取的结束行数（填-1为全部）
     * ExistTop:是否存在头部（如存在则读取数据时会把头部拼接到对应数据，若无则为当前列数）
     */
    private List<Map<String, Object>> handleData(Workbook workbook, int statrRow, int endRow, boolean existTop) {
        //声明返回结果集result
        List<Map<String, Object>> result = new ArrayList<>();
        //声明一个Excel头部函数
        ArrayList<String> top = new ArrayList<>();
        //解析sheet（sheet是Excel脚页）
        /**
         *此处会读取所有脚页的行数据，若只想读取指定页，不要for循环，直接给sheetNum赋值，脚页从0开始（通常情况Excel都只有一页，所以此处未进行进一步处理）
         */
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }
            //如存在头部，处理头部数据
            if (existTop) {
                int firstRowNum = sheet.getFirstRowNum();
                Row firstRow = sheet.getRow(firstRowNum);
                if (null == firstRow) {
                    logger.warn("解析Excel失败，在第一行没有读取到任何数据！");
                }
                for (int i = 0; i < firstRow.getLastCellNum(); i++) {
                    top.add(convertCellValueToString(firstRow.getCell(i)));
                }
            }
            //处理Excel数据内容
            int endRowNum;
            //获取结束行数
            if (endRow == -1) {
                endRowNum = sheet.getPhysicalNumberOfRows();
            } else {
                endRowNum = Math.min(endRow, sheet.getPhysicalNumberOfRows());
            }
            //遍历行数
            for (int i = statrRow - 1; i < endRowNum; i++) {
                Row row = sheet.getRow(i);
                if (null == row) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                //获取所有列数据
                for (int y = 0; y < row.getLastCellNum(); y++) {
                    if (top.size() > 0) {
                        if (top.size() >= y) {
                            map.put(top.get(y), convertCellValueToString(row.getCell(y)));
                        } else {
                            map.put(String.valueOf(y + 1), convertCellValueToString(row.getCell(y)));
                        }
                    } else {
                        map.put(String.valueOf(y + 1), convertCellValueToString(row.getCell(y)));
                    }
                }
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 方法一
     * 根据行数和列数读取Excel
     * fileName:Excel文件路径
     * StatrRow：读取的开始行数（默认填0）
     * EndRow：读取的结束行数（填-1为全部）
     * ExistTop:是否存在头部（如存在则读取数据时会把头部拼接到对应数据，若无则为当前列数）
     * 返回一个List<Map<String,Object>>
     */
    public List<Map<String, Object>> readExcelByRC(String fileName, int statrRow, int endRow, boolean existTop) {
        //判断输入的开始值是否少于等于结束值
        if (statrRow > endRow && endRow != -1) {
            logger.warn("输入的开始行值比结束行值大，请重新输入正确的行数");
            List<Map<String, Object>> error = null;
            return error;
        }
        //声明返回的结果集
        List<Map<String, Object>> result = new ArrayList<>();
        //声明一个工作薄
        Workbook workbook = null;
        //声明一个文件输入流
        FileInputStream inputStream = null;
        try {
            // 获取Excel后缀名，判断文件类型
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 获取Excel文件
            File excelFile = new File(fileName);
            if (!excelFile.exists()) {
                logger.warn("指定的Excel文件不存在！");
                return null;
            }
            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream, fileType);
            //处理Excel内容
            result = handleData(workbook, statrRow, endRow, existTop);
        } catch (Exception e) {
            logger.warn("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.warn("关闭数据流出错！错误信息：" + e.getMessage());
                return null;
            }
        }
        return result;
    }
}
