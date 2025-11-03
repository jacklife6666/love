package cn.itcast.domin;
//jdbc工具类
import cn.itcast.jdbc.util.JDBCUtils;
import cn.itcast.domin.emp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDemo8 {
    public static void main(String[] args) {
        List<emp> list = new JdbcDemo8().findAll2();
        System.out.println(list);

    }
    public List<emp> findAll2()
    {
        Statement stmt=null;
        ResultSet rs=null;
        Connection conn=null;
        List<emp> list =null;
        try {
       conn= JDBCUtils.getConnection();
           /* Class.forName("com.mysql.jdbc.Driver");
              conn=DriverManager.getConnection("jdbc:mysql:///syh","root","123");*/
            String sql= "select * from cj";
             stmt = conn.createStatement();
             rs = stmt.executeQuery(sql);
            emp emp=null;
             list= new ArrayList<emp>();
            while(rs.next()){
                String xm = rs.getString("xm");
                String xb = rs.getString("xb");
                Date sr = rs.getDate("sr");
                String xh = rs.getString("xh");
                 emp=new emp();
                 emp.setXm(xm);
                 emp.setXb(xb);
                 emp.setSr(sr);
                 emp.setXh(xh);
                 list.add(emp);
            }
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            //释放资源
/*if(rs!=null) {
    try {
        rs.close();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }*/
           /* if(stmt!=null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }*/

        }
        JDBCUtils.close(rs,stmt,conn);

        return list;

    }
}
