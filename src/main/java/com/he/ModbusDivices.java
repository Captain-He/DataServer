package com.he;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

import java.nio.ByteBuffer;

public class ModbusDivices {
    static ModbusFactory modbusFactory;
    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    public static ModbusMaster initTcpMaster(ModbusMaster tcpMaster) {
        if (tcpMaster == null) {
            return null;
        }
        try {
            tcpMaster.init();
            return tcpMaster;
        } catch (ModbusInitException e) {
            return null;
        }
    }


    public static ModbusResponse getModbusResponse(ModbusMaster tcpMaster, ModbusRequest request) {
        ModbusResponse modbusResponse = null;
        try {
            modbusResponse = tcpMaster.send(request);
            return modbusResponse;
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ModbusRequest getRequest(int salveId, int start,int readLenth, ModbusMaster tcpMaster) {
        ModbusRequest modbusRequest = null;
        try {
            modbusRequest = new ReadHoldingRegistersRequest(salveId, start,readLenth);
            return modbusRequest;
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            return null;
        }
    }

    //看modbusTcpMaster 是否可用
    public static ByteQueue modbusRTCP(String ip, int port, int salveId, int start, int readLenth) {
        ModbusMaster tcpMaster = getTcpMaster(ip, port, salveId);// 得到tcpMaster
        if (tcpMaster == null) {System.out.println("tcpMaster is null ");return null;
        }
        return modbusRTCP0(ip, port, salveId, start, readLenth, tcpMaster);
    }

    public static ModbusMaster getTcpMaster(String ip, int port, int salveId) {
        IpParameters params = new IpParameters();
        params.setHost(ip);// 设置ip
        if (port == 0) {
            params.setPort(502);// 设置端口，默认为502
        } else {
            params.setPort(port);
        }
        ModbusMaster tcpMaster = modbusFactory.createTcpMaster(params, true);// 获取ModbusMaster对象
        return tcpMaster;
    }


    public static ByteQueue modbusRTCP0(String ip, int port, int salveId,int start, int readLenth, ModbusMaster tcpMaster) {
        if (tcpMaster == null) {System.out.println("tcpMaster is null");return null;}
        tcpMaster = initTcpMaster(tcpMaster);// 初始化tcpmaster

        if (tcpMaster == null) {System.out.println("tcpMaster is null");return null;
        }
        ModbusRequest modbusRequest = getRequest(salveId, start, readLenth,tcpMaster);// 得到requst 对象

        if (modbusRequest == null) {System.out.println("request is null");return null;}
        ModbusResponse response = getModbusResponse(tcpMaster, modbusRequest);// 发送请求，得到Response

        ByteQueue byteQueue = new ByteQueue(12);
        response.write(byteQueue);
        System.out.println("功能" + modbusRequest.getFunctionCode());
        System.out.println("从站地址:" + modbusRequest.getSlaveId());
        System.out.println("收到的响应信息大小" + byteQueue.size());
        System.out.println("收到的响应信息:" + byteQueue);
        return byteQueue;
    }

    /* *
     * Convert byte[] to hex
     * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * ***************************************************
     * 起始位置15,响应数据：从站|data包含的传感器个数|data length|data*
     * ***************************************************
     *
     * @param bq
     */
    public static void ansisByteQueue(ByteQueue bq) {
        byte[] result = bq.peekAll();
        byte[] temp = null;
        ByteBuffer buffer = ByteBuffer.wrap(result, 3, result.length - 3);//直接获取 data
        while (buffer.hasRemaining()) {
            temp = new byte[2];
            buffer.get(temp, 0, temp.length);
            System.out.print(Integer.parseInt(bytesToHexString(temp), 16)+" ");
        }

    }
}
