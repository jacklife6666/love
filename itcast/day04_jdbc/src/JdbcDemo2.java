

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcDemo2 {
    public static void main(String[] args) {
        Statement stmt=null;
        Connection conn=null;
//.1.注册驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
       //2.定义sql
            String sql="insert into cj values('张三','男','2024-5-02','008')";
            //3.获取connection对象
             conn = DriverManager.getConnection("jdbc:mysql:///syh", "root", "123");
       //4.获取执行sql的对象
           stmt = conn.createStatement();
            //5.执行sql
            int  count = stmt.executeUpdate(sql);//影响的行数
            //6。处理结果
            System.out.println(count);
            if(count>0)
            {
                System.out.println("添加成功");
            }
            else
            {
                System.out.println("添加失败");
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }finally{
            if(stmt!=null )
            {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null )
            {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
