package com.data.sync.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.alibaba.fastjson.JSONArray;

public class ReadConfig {
    private static Properties prop = new Properties();

    static {
        initConfig();
    }

    public static void initConfig() {
        try {
            prop.load(new InputStreamReader(
                    ReadConfig.class.getClassLoader().getResourceAsStream("tableConfig.properties"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String name) {
        String value = prop.getProperty(name);
        if (value == null) {
            initConfig();
            return prop.getProperty(name);
        } else {
            return value;
        }
    }

    public static Connection getConn() throws ClassNotFoundException, SQLException {
        Class.forName(prop.getProperty("DRIVER"));
        Connection conn = DriverManager.getConnection(prop.getProperty("CONNURL"), prop.getProperty("USERNAME"),
                prop.getProperty("PASSWORD"));
        return conn;
    }

    public static void insertToDB(List<String> datas, String codes, String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        StringBuilder sb = new StringBuilder();
        try {
            conn = getConn();
            conn.setAutoCommit(false);
            sb.append(" insert into  ").append(tableName).append(" ( ").append(codes).append(" ) ").append(" values ");
            for (String data : datas) {
                sb.append(" ( ").append(data).append(" ) ").append(",");
            }
            String sql = sb.toString();
            sql = sql.substring(0, sql.length() - 1);
            sql = new String(sql.getBytes(), Charset.forName("UTF-8"));
            ps = conn.prepareStatement(
                    " insert into  work ( id,name )  values  ( 1.25,'二狗' ) , ( 2.56,'三狗' ) , ( 3.12,'Sioux' ) ");
            ps.execute();
            conn.commit();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("插入失败");
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 去掉JSONArray前后的 [ ]
     * 
     * @param array
     * @return 返回数据为数据表中所有的列名
     */
    public static String JSONArrayToString(JSONArray array) {
        String string = array.toString();
        string = string.substring(1);
        string = string.substring(0, string.length() - 1).replace("\"", "");
        return string;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection conn = getConn();
        System.out.println(conn);

        JSONArray array = new JSONArray();
        array.add("aaa");
        array.add("aaa");
        array.add("aaa");
        System.out.println(JSONArrayToString(array));
    }

}
