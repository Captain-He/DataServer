package com.he;

import com.he.equipments.*;
import com.he.thread.ReceiveChannel;
import com.he.thread.RequestChannel;
import com.he.thread.RequestMsg;
import com.he.writefile.PutDataToFile;
import com.he.writefile.WriteToDB;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class

App {
    public static void main(String[] args) {
        FileWriter fw = null;
        BufferedWriter bufw = null;
        try {
            File file = new File(System.getProperty("user.dir") + File.separator + "sql.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file, true);
            bufw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConcurrentLinkedQueue<String> sqlQueue = new ConcurrentLinkedQueue<>();
       // new Thread(new PutDataToFile(sqlQueue, bufw)).start();
        new Thread(new WriteToDB(sqlQueue)).start();
        ArrayList<CommunicationManager> communicationManagers = GetCommunicationManagers();
        for (CommunicationManager communicationManager : communicationManagers) {
            //开始遍历通信管理机，一个通信管理机对应多个ModbusSlave(通道抽象为ModbusSlave，下边连接多个集中器设备),
            // 一个ModbusSlave对应一个ModbusTcpMaster，TcpMaster任务分发
            RequestChannel requestChannel = new RequestChannel(communicationManager.getScNum());//此处参数为工人线程数量由通道数量决定，一个通信管理机对应一个channel
            // temp.length为一个通信管理机的通道数量
            ReceiveChannel receiveChannel = new ReceiveChannel(communicationManager.getScNum());
            requestChannel.startWorkers();
            receiveChannel.startWorkers();
            new Client(requestChannel, receiveChannel, sqlQueue,communicationManager.getModbusSlaves()).start();
        }
    }

    public static ArrayList<CommunicationManager> GetCommunicationManagers() {
        TxtFileReader readTxt = new TxtFileReader();
        String b[] = readTxt.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\dpu_list.txt");
        ArrayList<CommunicationManager> communicationManagers = new ArrayList<>();
        for (int i = 0; i < b.length; i++) {
            String temp[] = b[i].split(" ");
            CommunicationManager newDev = new CommunicationManager();
            if (temp[0].isEmpty() || temp[0] == null) continue;
            newDev.setId(temp[0]);
            newDev.setTerminal(temp[1]);
            newDev.setScNum(temp[2]);
            String[][] comIp = new String[temp.length - 3][3];
            ArrayList<ModbusSlave> modbusSlaves = new ArrayList<>();
            for (int j = 3; j < Integer.parseInt(temp[2]) + 3; j++) {
                comIp[j - 3] = ComIpSplit(temp[j]);
                ModbusSlave modbusSlave = getModbusSlave(temp[0], j - 2, comIp[j - 3][1], Integer.parseInt(comIp[j - 3][2]));
                if (modbusSlave != null) {
                    modbusSlaves.add(modbusSlave);
                }
            }
            newDev.setModbusSlaves(modbusSlaves);
            communicationManagers.add(newDev);
        }
        return communicationManagers;
    }

    public static ModbusSlave getModbusSlave(String communicationManagerID, int id, String communicationManagerComIP, int chuanId) {
        TxtFileReader readTxt = new TxtFileReader();
        String b[] = readTxt.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\dev_link.txt");
        ModbusSlave newDev = new ModbusSlave();
        for (int i = 0; i < b.length; i++) { //i表示文件的行
            String temp[] = b[i].split(" "); //对每一行进行空格分割
            if (temp[0].isEmpty() || temp[0] == null) continue;
            if (isEquals(communicationManagerID, temp[4]) && id == Integer.parseInt(temp[5])) {
                newDev.setId(id);
                newDev.setCommunicationMaganerID(Integer.parseInt(temp[4]));
                newDev.setDevNum(Integer.parseInt(temp[6]));
                newDev.setCommunicationManagerComIP(communicationManagerComIP);
                newDev.setChuanID(chuanId);
                newDev.setPowerMeters(GetPowerMeters(communicationManagerComIP, chuanId, temp));
                newDev.setTemperConcentrator(GetTemperConcentrators(communicationManagerComIP, chuanId, temp));
                break;
            }
        }
        return newDev;
    }
    private static ArrayList<PowerMeter> GetPowerMeters(String communicationManagerComIP, int chuanId, String[] tempMsg) {
        TxtFileReader readTxt = new TxtFileReader();
        String b[] = readTxt.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\concentrator_list.txt");
        String s[] = readTxt.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\sensor_dev_list.txt");
        ArrayList<PowerMeter> powerMeters = new ArrayList<>();
        for (int q = 7; q < tempMsg.length; q++) {
            int ConcentratorDeivceID = Integer.parseInt(tempMsg[q]);
            for (int i = 0; i < b.length; i++) { //i表示文件的行
                String temp[] = b[i].split(" "); //对每一行进行空格分割

                if (temp[0].isEmpty() || temp[0] == null) continue;

                if (100000 < Integer.parseInt(temp[0]) && Integer.parseInt(temp[0]) < 200000 && ConcentratorDeivceID == Integer.parseInt(temp[0])) {
                   // System.out.format("%d###%d", ConcentratorDeivceID, Integer.parseInt(temp[0]));
                    PowerMeter newDev = new PowerMeter();
                    newDev.setId(Integer.parseInt(temp[0]));
                    newDev.setComNum(temp[1]);
                    newDev.setSlaveAddress(Integer.parseInt(temp[7]));
                    newDev.setReadTimes(Integer.parseInt(temp[8]));
                    String[][] readMessage = new String[temp.length - 9][4];
                    for (int j = 9; j < Integer.parseInt(temp[8]) + 9; j++) {
                        readMessage[j - 9] = ComIpSplit(temp[j]);
                    }
                    newDev.setMapRelation(readMessage);
                    RequestMsg requestMsg = new RequestMsg(communicationManagerComIP, chuanId, Integer.parseInt(temp[0]), Integer.parseInt(temp[7]), Integer.parseInt(temp[8]), readMessage, s);
                    newDev.setRequestMsg(requestMsg);
                    powerMeters.add(newDev);
                }
            }
        }

        return powerMeters;
    }

    private static ArrayList<TemperConcentrator> GetTemperConcentrators(String communicationManagerComIP, int chuanId, String[] tempMsg) {
        TxtFileReader readTxt = new TxtFileReader();
        String b[] = readTxt.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\concentrator_list.txt");
        String sensor_dev_list[] = readTxt.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\sensor_dev_list.txt");
        ArrayList<TemperConcentrator> temperConcentrators = new ArrayList<>();
        for (int q = 7; q < tempMsg.length; q++) {
            int ConcentratorDeivceID = Integer.parseInt(tempMsg[q]);
            for (int i = 0; i < b.length; i++) { //i表示文件的行
                String temp[] = b[i].split(" "); //对每一行进行空格分割
                if (temp[0].isEmpty() || temp[0] == null) continue;
                if (300000 < Integer.parseInt(temp[0]) && Integer.parseInt(temp[0]) < 400000 && ConcentratorDeivceID == Integer.parseInt(temp[0])) {
                    TemperConcentrator newDev = new TemperConcentrator();
                    newDev.setId(Integer.parseInt(temp[0]));
                    newDev.setComNum(temp[1]);
                    newDev.setSlaveAddress(Integer.parseInt(temp[7]));
                    newDev.setReadTimes(Integer.parseInt(temp[8]));
                    String[][] readMessage = new String[temp.length - 9][4];
                    for (int j = 9; j < Integer.parseInt(temp[8]) + 9; j++) {
                        readMessage[j - 9] = ComIpSplit(temp[j]);
                    }
                    RequestMsg requestMsg = new RequestMsg(communicationManagerComIP, chuanId, Integer.parseInt(temp[0]), Integer.parseInt(temp[7]), Integer.parseInt(temp[8]), readMessage, sensor_dev_list);
                    newDev.setRequestMsg(requestMsg);
                    newDev.setMapRelation(readMessage);
                    temperConcentrators.add(newDev);
                }
            }
        }
        return temperConcentrators;
    }

    //串口编号/IP地址/端口 分割
    private static String[] ComIpSplit(String str) {
        String splitArray[] = {};
        if ((str != null) && (!isEquals(str, "-"))) {
            splitArray = str.split("/");
        }
        return splitArray;
    }

    //判断字符串a 是否与 字符串b 相等
    public static boolean isEquals(String a, String b) {
        return a.replaceAll("\n", "").replaceAll(" ", "").equals(b);
    }
}

