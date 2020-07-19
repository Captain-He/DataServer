package com.he.equipmentsclass;

import com.he.TxtFileReader;

import java.util.ArrayList;

public class ModbusSlave { //一个通信管理机通道COM模拟一个modbusSlave
    private int id;
    private String accessType; //接入方式(以太网/串口)
    private int communicationMaganerID;//通信管理机ID
    private int comNum;//通信管理机通道号
    private int devNum; //设备数量
    private int[] devId; //设备ID
    private String communicationManagerComIP;
    private ArrayList<PowerMeter> powerMeters;
    private ArrayList<TemperConcentrator> temperConcentrator ;


    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
    public String getAccessType() {
        return accessType;
    }
    public void setCommunicationMaganerID(int communicationMaganerID) {
        this.communicationMaganerID = communicationMaganerID;
    }
    public int getCommunicationMaganerID() {
        return communicationMaganerID;
    }
    public void setComNum(int comNum) {
        this.comNum = comNum;
    }
    public int getComNum() {
        return comNum;
    }
    public void setDevNum(int devNum) {
        this.devNum = devNum;
    }
    public int getDevNum() {
        return devNum;
    }
    public void setDevId(int[] devId) {
        this.devId = devId;
    }
    public int[] getDevId() {
        return devId;
    }
    public void setPowerMeters(ArrayList<PowerMeter> powerMeters) {
        this.powerMeters = powerMeters;
    }
    public void setTemperConcentrator(ArrayList<TemperConcentrator> temperConcentrator) {
        this.temperConcentrator = temperConcentrator;
    }
    public ArrayList<PowerMeter> getPowerMeters() {
        return powerMeters;
    }
    public ArrayList<TemperConcentrator> getTemperConcentrator() {
        return temperConcentrator;
    }
    public void setCommunicationManagerComIP(String communicationManagerComIP) {
        this.communicationManagerComIP = communicationManagerComIP;
    }
    public String getCommunicationManagerComIP() {
        return communicationManagerComIP;
    }
    public ModbusSlave getModbusSlave(String communicationManagerID, int comID, String communicationManagerComIP) {
        TxtFileReader readTxt = new TxtFileReader();
        String b[] = readTxt.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\dev_link.txt");
        ModbusSlave newDev = new ModbusSlave();
        for (int i = 0; i < b.length; i++) { //i表示文件的行
            String temp[] = b[i].split(" "); //对每一行进行空格分割
            if (temp[0].isEmpty() || temp[0] == null) continue;
            if(isEquals(communicationManagerID,temp[4])&&comID==Integer.parseInt(temp[5])){
                newDev.setId(Integer.parseInt(temp[0]));
                newDev.setCommunicationMaganerID(Integer.parseInt(temp[4]));
                newDev.setComNum(Integer.parseInt(temp[5]));
                newDev.setDevNum(Integer.parseInt(temp[6]));
                newDev.setCommunicationManagerComIP(communicationManagerComIP);
                newDev.setPowerMeters(GetPowerMeters(temp));
                newDev.setTemperConcentrator(GetTemperConcentrators(temp));
                break;
            }
        }
        return newDev;
    }
    private ArrayList<PowerMeter> GetPowerMeters(String []tempMsg){
        TxtFileReader readTxt = new TxtFileReader();
        String b [] =  readTxt.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\concentrator_list.txt");
        ArrayList<PowerMeter> powerMeters = new ArrayList<>();
        for (int q = 7; q < tempMsg.length; q++) {
           int ConcentratorDeivceID  = Integer.parseInt(tempMsg[q]);
            for(int i=0;i<b.length;i++) { //i表示文件的行
                String temp[] = b[i].split(" "); //对每一行进行空格分割

                if(temp[0].isEmpty()||temp[0]==null) continue;

                if(100000<Integer.parseInt(temp[0])&&Integer.parseInt(temp[0])<200000&&ConcentratorDeivceID ==Integer.parseInt(temp[0]) ){
                    PowerMeter newDev = new PowerMeter();
                    newDev.setId(Integer.parseInt(temp[0]));
                    newDev.setComNum(temp[1]);
                    newDev.setSlaveAddress(Integer.parseInt(temp[7]));
                    newDev.setReadTimes(Integer.parseInt(temp[8]));
                    String[][] readMessage=new String[temp.length-9][4];
                    for(int j=9;j<Integer.parseInt(temp[8])+9;j++){
                        readMessage[j-9] = ComIpSplit(temp[j]);
                    }
                    newDev.setMapRelation(readMessage);
                    powerMeters.add(newDev);
                }
            }
        }

        return powerMeters;
    }
    private ArrayList<TemperConcentrator> GetTemperConcentrators(String []tempMsg){
        TxtFileReader readTxt = new TxtFileReader();
        String b [] =  readTxt.toArrayByFileReader1(".\\src\\main\\java\\com\\he\\txt\\concentrator_list.txt");
        ArrayList<TemperConcentrator> temperConcentrators = new ArrayList<>();
        for (int q = 7; q < tempMsg.length; q++) {
            int ConcentratorDeivceID  = Integer.parseInt(tempMsg[q]);
            for(int i=0;i<b.length;i++) { //i表示文件的行
                String temp[] = b[i].split(" "); //对每一行进行空格分割

                if(temp[0].isEmpty()||temp[0]==null) continue;
                if(300000<Integer.parseInt(temp[0])&&Integer.parseInt(temp[0])<400000&&ConcentratorDeivceID ==Integer.parseInt(temp[0])){
                    TemperConcentrator newDev = new TemperConcentrator();
                    newDev.setId(Integer.parseInt(temp[0]));
                    newDev.setComNum(temp[1]);
                    newDev.setSlaveAddress(Integer.parseInt(temp[7]));
                    newDev.setReadTimes(Integer.parseInt(temp[8]));
                    String[][] readMessage=new String[temp.length-9][4];
                    for(int j=9;j<Integer.parseInt(temp[8])+9;j++){
                        readMessage[j-9] = ComIpSplit(temp[j]);
                    }
                    newDev.setMapRelation(readMessage);
                    temperConcentrators.add(newDev);
                }
            }
        }
        return temperConcentrators;
    }
    //串口编号/IP地址/端口 分割
    private static String[] ComIpSplit(String str){
        String splitArray[] = {};
        if((str != null)&&(!isEquals(str,"-"))){
            splitArray = str.split("/");
        }
        return splitArray;
    }
    //判断字符串a 是否与 字符串b 相等
    public  static boolean isEquals(String a,String b){
        return a.replaceAll("\n", "").replaceAll(" ", "").equals(b);
    }
}
