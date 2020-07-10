package com.he;

import com.he.equipmentsclass.DevLink;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.*;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;
import com.he.equipmentsclass.CommunicationManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;


public class App {
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

    public static ModbusRequest getRequest(int salveId, int start, int readLenth, ModbusMaster tcpMaster) {
        ModbusRequest modbusRequest = null;
        try {
            modbusRequest = new ReadHoldingRegistersRequest(salveId, start, readLenth);
            return modbusRequest;
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            return null;
        }
    }

    //看modbusTcpMaster 是否可用
    public static ByteQueue modbusRTCP(String ip, int port, int salveId, int start, int readLenth) {
        ModbusMaster tcpMaster = getTcpMaster(ip, port, salveId);// 得到tcpMaster
        if (tcpMaster == null) {
            System.out.println("tcpMaster is null ");
            return null;
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


    public static ByteQueue modbusRTCP0(String ip, int port, int salveId, int start, int readLenth, ModbusMaster tcpMaster) {
        if (tcpMaster == null) {
            System.out.println("tcpMaster is null");
            return null;
        }
        tcpMaster = initTcpMaster(tcpMaster);// 初始化tcpmaster

        if (tcpMaster == null) {
            System.out.println("tcpMaster is null");
            return null;
        }
        ModbusRequest modbusRequest = getRequest(salveId, start, readLenth, tcpMaster);// 得到requst 对象

        if (modbusRequest == null) {
            System.out.println("request is null");
            return null;
        }
        ModbusResponse response = getModbusResponse(tcpMaster, modbusRequest);// 发送请求，得到Response

        ByteQueue byteQueue = new ByteQueue(12);
        response.write(byteQueue);
        /*System.out.println("功能" + modbusRequest.getFunctionCode());
        System.out.println("从站地址:" + modbusRequest.getSlaveId());
        System.out.println("收到的响应信息大小" + byteQueue.size());
        System.out.println("收到的响应信息:" + byteQueue);*/
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
    public static String ansisByteQueue(ByteQueue bq) {
        byte[] result = bq.peekAll();
        byte[] temp = null;
        ByteBuffer buffer = ByteBuffer.wrap(result, 3, result.length - 3);//直接获取 data
        String res = "";
        while (buffer.hasRemaining()) {
            temp = new byte[2];
            buffer.get(temp, 0, temp.length);
            res+=Integer.parseInt(bytesToHexString(temp), 16) + " ";
        }
        return res;
    }

    private static CommunicationManager communicationManager = new CommunicationManager();
    //读取dpu_list ，获得通信管理机信息，一行代表一个通信管理机
    private static ArrayList<CommunicationManager> cmList = communicationManager.txtlist();
    private static DevLink devLink = new DevLink();
    private static ArrayList<DevLink> devList = devLink.txtlist();

    public static void main(String[] args) {

        TxtFileReader txtFileReader = new TxtFileReader();
        ArrayList<ArrayList<RequestMsgFromTxt>> RmList = new ArrayList<ArrayList<RequestMsgFromTxt>>();
        for (int i = 0; i < cmList.size(); i++) {
            int requestNnum = 0;
            String[][] temp = cmList.get(i).getMapRelation();//通信管理机i,的j通道COM1/192.168.1.200/502
            for (int j = 0; j < temp.length; j++) {
                String ip = temp[j][1];
                int com = Integer.parseInt(temp[j][2]);
                //j个通道对应j个ClientThread任务分发线程，共用一个channel
                RmList = txtFileReader.RequestMsg(ip, com, cmList.get(i).getId(), j + 1);
                requestNnum += RmList.size();
            }
            Channel channel = new Channel(temp.length, requestNnum);//此处参数为工人线程数量由通道数量决定，一个通信管理机对应一个channel
            // temp.length为一个通信管理机的通道数量
            channel.startWorkers();
            ReceiveChannel receiveChannel = new ReceiveChannel(temp.length);
            receiveChannel.startWorkers();
            for (int j = 0; j < temp.length; j++) {
                String ip = temp[j][1];
                int com = Integer.parseInt(temp[j][2]);
                //j个通道对应j个ClientThread任务分发线程，共用一个channel
                 new TaskDistributeThread(channel, receiveChannel,txtFileReader.RequestMsg(ip, com, cmList.get(i).getId(), j + 1)).start();
            }

        }

    }
}
