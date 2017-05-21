package com.data.sync;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.data.sync.utils.ReadConfig;

@SuppressWarnings("serial")
public class SyncController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        request = new MultipartRequestWrapper(request);
        String tableName = request.getParameter("tableName");
        String fileName = request.getParameter("fileName");
        InputStream is = new FileInputStream(MultipartRequestWrapper.PATH + fileName);
        analyExcel(is, fileName, request, tableName);
    }

    /**
     * 解析Excel
     * 
     * @param is
     *            Excel输入流
     * @param fileName
     *            Excel文件名
     * @throws IOException
     */
    public static void analyExcel(InputStream is, String fileName, HttpServletRequest request, String tableName)
            throws IOException {
        Workbook workbook = null;
        if (fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook(is);
        } else if (fileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(is);
        } else {
            return;
        }
        JSONObject config = JSON.parseObject(ReadConfig.getValue(tableName));
        JSONArray jsonArray = config.getJSONArray("code");
        int num = workbook.getNumberOfSheets();
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < num; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 循环行Row
            Row row = null;
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                StringBuilder sb = new StringBuilder();
                // 判断第一行指标名称是否一致,如果不一致直接退出,返回错误信息
                row = sheet.getRow(rowNum);
                for (int j = 0; j < jsonArray.size(); j++) {
                    Map<String, Object> map = getValue(row.getCell(j));
                    Object value = map.get("value");
                    if (rowNum == 0) {
                        if (!jsonArray.get(j).toString().equalsIgnoreCase((String) value)) {
                            request.setAttribute("error", jsonArray.get(j) + "指标有误,请检查数据格式!");
                            return;
                        }
                    } else if (rowNum >= 2) {
                        if ((int) map.get("type") == 3) {
                            value = "\"" + (String) value + "\"";
                        }
                        sb.append(",").append(value);
                    }
                }
                if (rowNum >= 2) {
                    // 拼接SQL中的数据
                    data.add(sb.toString().substring(1));
                }
            }
        }
        ReadConfig.insertToDB(data, ReadConfig.JSONArrayToString(jsonArray), tableName);
    }

    /**
     * 获取Excel每个单元格的值
     * 
     * @param cell
     * @return
     */
    public static Map<String, Object> getValue(Cell cell) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            map.put("type", 1);
            map.put("value", cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            map.put("type", 2);
            map.put("value", cell.getNumericCellValue());
        } else {
            map.put("type", 3);
            map.put("value", cell.getStringCellValue());
        }
        return map;
    }

}
