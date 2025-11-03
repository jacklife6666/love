package cn.itcast.jdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//事务操作
public class JdbcDemo10 {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql1 = "update account set q=q-? where id=?";
            String sql2 = "update account set q=q+? where id=?";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt2 = conn.prepareStatement(sql2);
            //设置参数
            pstmt1.setDouble(1, 500);
            pstmt1.setInt(2, 1);
            pstmt2.setDouble(1, 500);
            pstmt2.setInt(2, 2);
            pstmt1.executeUpdate();
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (pstmt1 != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    if (pstmt2 != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }


            }
        }
    }
}