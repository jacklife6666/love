package druid;

import Utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//使用工具类
public class DruidDemo2 {

    public static void main(String[] args) {
        PreparedStatement pstmt = null;
        Connection conn= null;
        try {
             conn = JDBCUtils.getConnection();
            String sql= "insert into account values(null,?,?)";
             pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,"王五");
            pstmt.setDouble(2,3000);
int  count=pstmt.executeUpdate();
            System.out.println(count);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            JDBCUtils.close(pstmt,null);
        }


    }


}
