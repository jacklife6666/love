package cn.itcast.jdbc.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils {

    private static String url;
    private static String username;
    private static String password;
    private static String Driver;
    static{

        try {
            Properties pro = new Properties();
            pro.load(new FileReader("D:\\java项目\\itcast\\day04_jdbc\\src\\cn\\itcast\\jdbc\\util\\jdbc.properties"));
            url=pro.getProperty("url");
            username= pro.getProperty("username");
            password=pro.getProperty("password");
            Driver=pro.getProperty("Driver");
            Class.forName(Driver);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

