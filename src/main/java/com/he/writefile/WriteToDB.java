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
        Statement st = null;
        try {
            st = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (true) {
            if (!queue.isEmpty()) {

                String temp = queue.poll();
                if (temp != null) {
                    try {
                        st.execute(temp);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
