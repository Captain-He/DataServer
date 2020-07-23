package com.he.thread;

import com.he.DBConnectionPool.ConnectionPoolManager;
import com.he.DBConnectionPool.IConnectionPool;

import java.sql.*;
import java.util.Map;

public class OnceResolveTask {
    private final ResolverMsg resolverMsg;
    private final short[] buffer;
    private final ReceiveChannel receiveChannel;
    private  IConnectionPool pool;
    private  PreparedStatement stmt;
    private  ResultSet rs;


    public OnceResolveTask(ResolverMsg resolverMsg, short[] buffer, ReceiveChannel receiveChannel) throws SQLException {
        this.resolverMsg = resolverMsg;
        this.buffer = buffer;
        this.receiveChannel = receiveChannel;
        this.pool = ConnectionPoolManager.getInstance().getPool("testPool");
        this.stmt = null;
        this.rs = null;
    }

    public void execute() throws SQLException {
        Connection conn = getConnection();

        String[] aimStr = resolve();
        String sql = "insert into ce(value1) values(?)";
        stmt = conn.prepareStatement(sql);

        for (int i = 0; i < aimStr.length; i++){
            stmt.setString(1, aimStr[i]);
            int result =stmt.executeUpdate();// 返回值代表收到影响的行数
            System.out.println("插入成功"+result+"行");
        }

        pool.releaseConn(conn);

    }
    public Connection getConnection(){
        Connection conn = null;
        if(pool != null && pool.isActive()){
            conn = pool.getConnection();
        }
        return conn;
    }
    private String[] resolve() {
        Map<String, String> resolverMap = resolverMsg.resolverMap;
        String aimStr[] = new String[resolverMap.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : resolverMap.entrySet()) {
            String sensorID = entry.getKey();
            String solver[] = ComIpSplit(entry.getValue(), " ");
            // solver[0] 集中器设备ID，slover[1]传感器设备ID，solver[2]数据项数量，从3开始到solver.length都是每个数据项的【】
            aimStr[index] = solver[0] + "-";
            for (int i = 3; i < Integer.parseInt(solver[2]) + 3; i++) { //遍历项解析
                String solverStr[] = ComIpSplit(solver[i], "/");
                aimStr[index] += "item" + solverStr[0] + "-";
                for (int j = Integer.parseInt(solverStr[1]); j < Integer.parseInt(solverStr[1]) + Integer.parseInt(solverStr[2]); j++) {
                    aimStr[index] += buffer[j];
                }
                aimStr[index] += "-";
            }
            aimStr[index++] += "";
        }
        return aimStr;
    }

    //串口编号/IP地址/端口 分割
    private static String[] ComIpSplit(String str, String splitChar) {
        String splitArray[] = {};
        if ((str != null) && (!isEquals(str, "-"))) {
            splitArray = str.replaceAll("  ", " ").split(splitChar);
        }
        return splitArray;
    }

    //判断字符串a 是否与 字符串b 相等
    public static boolean isEquals(String a, String b) {
        return a.replaceAll("\n", "").replaceAll(" ", "").equals(b);
    }
}
