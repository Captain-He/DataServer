package com.he;

import com.he.equipments.*;
import com.he.socket.SocketServerListen;
import com.he.thread.RequestMsg;
import com.he.thread.ResolverMsg;
import com.he.txt.TxtFileReader;
import com.he.writefile.WriteFileClient;


import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class App {

    public static LinkedBlockingQueue bufQueue = new LinkedBlockingQueue(10000);
    public static String sensor_dev_list[] = TxtFileReader.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\sensor_dev_list.txt");
    public static String dpu_list[] = TxtFileReader.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\dpu_list.txt");
    public static String dev_link[] = TxtFileReader.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\dev_link.txt");
    public static String concentrator_list[] = TxtFileReader.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\concentrator_list.txt");

    public static void main(String[] args) {

        //new Thread(new WriteToDB(sqlQueue)).start();
       // new WriteFileClient().service();
        new Thread(new WriteFileClient(bufQueue)).start();
        new SocketServerListen().service();

    }

    public  static ArrayList<CommunicationManager> GetCommunicationManagers() {
        ArrayList<CommunicationManager> communicationManagers = new ArrayList<CommunicationManager>();
        for (int i = 0; i < dpu_list.length; i++) {
            String temp[] = dpu_list[i].split(" ");
            CommunicationManager newDev = new CommunicationManager();
            if (temp[0].isEmpty() || temp[0] == null) continue;
            newDev.setId(temp[0]);
            newDev.setTerminal(temp[1]);
            newDev.setScNum(temp[2]);
            String[][] comIp = new String[temp.length - 3][3];
            ArrayList<CommunicationManagerCom> communicationManagerComs = new ArrayList<CommunicationManagerCom>();
            for (int j = 3; j < Integer.parseInt(temp[2]) + 3; j++) {
                comIp[j - 3] = ComIpSplit(temp[j]);
                CommunicationManagerCom communicationManagerCom = getCManagerCom(temp[0], j - 2, comIp[j - 3][1], Integer.parseInt(comIp[j - 3][2]));
                if (communicationManagerCom != null) {
                    communicationManagerComs.add(communicationManagerCom);
                }
            }
            newDev.setCommunicationManagerComs(communicationManagerComs);
            communicationManagers.add(newDev);
        }
        return communicationManagers;
    }
    public  static CommunicationManagerCom getCManagerCom(String communicationManagerID, int id, String communicationManagerComIP, int chuanId) {
        CommunicationManagerCom newDev = new CommunicationManagerCom();
        for (int i = 0; i < dev_link.length; i++) { //i表示文件的行
            String temp[] = dev_link[i].split(" "); //对每一行进行空格分割
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
    private  static ArrayList<PowerMeter> GetPowerMeters(String communicationManagerComIP, int chuanId, String[] tempMsg) {
        ArrayList<PowerMeter> powerMeters = new ArrayList<PowerMeter>();
        for (int q = 7; q < tempMsg.length; q++) {
            int ConcentratorDeivceID = Integer.parseInt(tempMsg[q]);
            for (int i = 0; i < concentrator_list.length; i++) { //i表示文件的行
                String temp[] = concentrator_list[i].split(" "); //对每一行进行空格分割
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
                    RequestMsg requestMsg = new RequestMsg(communicationManagerComIP, chuanId, Integer.parseInt(temp[0]), Integer.parseInt(temp[7]), Integer.parseInt(temp[8]), readMessage,new ResolverMsg(Integer.parseInt(temp[0]),sensor_dev_list,bufQueue));
                    newDev.setRequestMsg(requestMsg);
                    powerMeters.add(newDev);
                }
            }
        }

        return powerMeters;
    }
    private static ArrayList<TemperConcentrator> GetTemperConcentrators(String communicationManagerComIP, int chuanId, String[] tempMsg) {
        ArrayList<TemperConcentrator> temperConcentrators = new ArrayList<TemperConcentrator>();
        for (int q = 7; q < tempMsg.length; q++) {
            int ConcentratorDeivceID = Integer.parseInt(tempMsg[q]);
            for (int i = 0; i < concentrator_list.length; i++) { //i表示文件的行
                String temp[] = concentrator_list[i].split(" "); //对每一行进行空格分割
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
                    RequestMsg requestMsg = new RequestMsg(communicationManagerComIP, chuanId, Integer.parseInt(temp[0]), Integer.parseInt(temp[7]), Integer.parseInt(temp[8]), readMessage,new ResolverMsg(Integer.parseInt(temp[0]),sensor_dev_list,bufQueue));
                    newDev.setRequestMsg(requestMsg);
                    newDev.setMapRelation(readMessage);
                    temperConcentrators.add(newDev);
                }
            }
        }
        return temperConcentrators;
    }

    //串口编号/IP地址/端口 分割
    private static  String[] ComIpSplit(String str) {
        String splitArray[] = {};
        if ((str != null) && (!isEquals(str, "-"))) {
            splitArray = str.split("/");
        }
        return splitArray;
    }

    //判断字符串a 是否与 字符串b 相等
    private  static boolean isEquals(String a, String b) {
        return a.replaceAll("\n", "").replaceAll(" ", "").equals(b);
    }
}

