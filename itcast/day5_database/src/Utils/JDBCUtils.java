package Utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//Druid连接池工具类
public class JDBCUtils {
    private static DataSource ds;
    static{

        try {
            Properties pro= new Properties();
            pro.load(JDBCUtils.class.getClassLoader().getResourceAsStream("dgconfig.properties"));
        //获取DataSource
            ds= DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    //获取连接
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
public static void close( Statement stmt, Connection conn){
close(null,stmt,conn);
}
public static void close(ResultSet rs, Statement stmt, Connection conn){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    if(stmt!=null){
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    if(conn!=null){
        try {
            conn.close();
        } catch (SQLException e) {//归还连接
            throw new RuntimeException(e);
        }
    }

}
public static DataSource getDataSource(){
        return ds;
}

}
