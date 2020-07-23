package com.he.DBConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        // 初始化连接池

        ThreadConnection a = new ThreadConnection();
        ThreadConnection b = new ThreadConnection();
        ThreadConnection c = new ThreadConnection();

        a.run();
        b.run();
        c.run();
        Connection conn = a.getConnection();
        Statement stmt = null;
        try {
            stmt =  conn.createStatement();
            String sql;
            ResultSet rs;
            sql = "SELECT Host, User, Password FROM user";
            rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                String name = rs.getString("Host");
                String id = rs.getString("User");
                String url = rs.getString("Password");

                // 输出数据
                System.out.print("ID: " + id);
                System.out.print(", 站点名称: " + name);
                System.out.print(", 站点 URL: " + url);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // 设置优先级，先让初始化执行，模拟 线程池 先启动
        // 这里仅仅表面控制了，因为即使t 线程先启动，也不能保证pool 初始化完成，为了简单模拟，这里先这样写了


        System.out.println("线程A-> " + a.getConnection());
        System.out.println("线程B-> " + b.getConnection());
        System.out.println("线程C-> " + c.getConnection());
    }


}