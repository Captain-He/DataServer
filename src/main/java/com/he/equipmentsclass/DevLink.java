package com.he.equipmentsclass;

import com.he.TxtFileReader;

import java.util.ArrayList;

public class DevLink {
    private int id;
    private String accessType; //接入方式(以太网/串口)
    private int cmNum;//通信管理机序号
    private int enterNum;//通信管理机通道号
    private int devNum; //设备数量
    private int[] devId; //设备ID

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

    public void setCmNum(int cmNum) {
        this.cmNum = cmNum;
    }

    public int getCmNum() {
        return cmNum;
    }

    public void setEnterNum(int enterNum) {
        this.enterNum = enterNum;
    }

    public int getEnterNum() {
        return enterNum;
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

    public ArrayList<DevLink> txtlist() {
        TxtFileReader readTxt = new TxtFileReader();
        String b[] = readTxt.toArrayByFileReader1("D:\\Repository\\ModbusJ\\modbus4j\\src\\main\\java\\com\\he\\txt\\dev_link.txt");
        ArrayList<DevLink> devLinks = new ArrayList<>();
        for (int i = 0; i < b.length; i++) { //i表示文件的行
            String temp[] = b[i].split(" "); //对每一行进行空格分割
            DevLink newDev = new DevLink();
            if (temp[0].isEmpty() || temp[0] == null) continue;
            newDev.setId(Integer.parseInt(temp[0]));
            newDev.setCmNum(Integer.parseInt(temp[4]));
            newDev.setEnterNum(Integer.parseInt(temp[5]));
            newDev.setDevNum(Integer.parseInt(temp[6]));
            int[] readMessage = new int[temp.length - 7];
            for (int j = 7; j < temp.length; j++) {
                readMessage[j - 7] = Integer.parseInt(temp[j]);
            }
            newDev.setDevId(readMessage);
            devLinks.add(newDev);
        }
        return devLinks;
    }
}
