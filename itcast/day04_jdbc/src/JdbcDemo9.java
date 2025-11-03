import cn.itcast.jdbc.util.JDBCUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JdbcDemo9 {

    public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名");
        String username = sc.nextLine();
        System.out.println("请输入密码");
        String password = sc.nextLine();

        Boolean flag= new JdbcDemo9().login(username,password);
        if(flag){
            System.out.println("成功");
        }
        else{
            System.out.println("失败");
        }
    }





    public boolean login(String username,String password) {
        if (username == null || password == null) {
            return false;
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "select * from uuser where username = '" + username + "' and password = '" + password + "'";
            stmt = conn.createStatement();
             rs = stmt.executeQuery(sql);
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            JDBCUtils.close(rs,stmt,conn);

        }

    }
}
