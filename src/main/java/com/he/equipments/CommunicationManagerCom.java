package com.he.equipments;

import java.util.ArrayList;

public class CommunicationManagerCom { //一个通信管理机通道COM模拟一个modbusSlave
    private int id;//com口编号，为INT
    private String accessType; //接入方式(以太网/串口)
    private int communicationMaganerID;//通信管理机ID
    private int devNum; //设备数量
    private int[] devId; //设备ID
    private int chuanID;//串口ID
    private String communicationManagerComIP;
    private ArrayList<PowerMeter> powerMeters;
    private ArrayList<TemperConcentrator> temperConcentrator;


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
        if (this.powerMeters == null)
            return null;
        return powerMeters;
    }
    public ArrayList<TemperConcentrator> getTemperConcentrator() {
        if (temperConcentrator == null)
            return null;
        return temperConcentrator;
    }
    public void setCommunicationManagerComIP(String communicationManagerComIP) {
        this.communicationManagerComIP = communicationManagerComIP;
    }
    public String getCommunicationManagerComIP() {
        return communicationManagerComIP;
    }
    public void setChuanID(int chuanID) {
        this.chuanID = chuanID;
    }
    public int getChuanID() {
        return chuanID;
    }
}
