package com.he.writefile;

import com.he.DBConnectionPool.ConnectionPoolManager;
import com.he.DBConnectionPool.IConnectionPool;

import java.sql.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WriteToDB implements Runnable {
    private ConcurrentLinkedQueue<String> queue;
    private IConnectionPool pool;
    private PreparedStatement stmt;
    private ResultSet rs;
    private Connection conn;

    public WriteToDB(ConcurrentLinkedQueue<String> queue) {
        this.queue = queue;
        this.pool = ConnectionPoolManager.getInstance().getPool("testPool");
        this.stmt = null;
        this.rs = null;
        this.conn = getConnection();
    }

    public void run() {
        //设置批量处理的数量
        int batchSize = 100;
        try {
            stmt = conn.prepareStatement("insert into ce (value1) "
                    + "values (?)");
            // 关闭事务自动提交 ,这一行必须加上
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            if (!queue.isEmpty()) {
                int i = 0;
                try {

                    for (int j = 0; j < 101; j++) {
                        String temp = queue.poll();
                        if( temp == null){
                            j++;
                        }else{
                            ++i;
                            stmt.setString(1, temp);
                            stmt.addBatch();
                            if (i % batchSize == 0) {
                                stmt.executeBatch();
                                conn.commit();
                            }
                        }
                    }
                    if (i % batchSize != 0) {
                        stmt.executeBatch();
                        conn.commit();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Connection getConnection() {
        Connection conn = null;
        if (pool != null && pool.isActive()) {
            conn = pool.getConnection();
        }
        return conn;
    }
}
