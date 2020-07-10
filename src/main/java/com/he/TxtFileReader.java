package com.he;

import com.he.equipmentsclass.DevLink;
import com.he.equipmentsclass.PowerMeter;
import com.he.equipmentsclass.TemperConcentrator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TxtFileReader {
    public String[] toArrayByFileReader1(String name) {/* 使用ArrayList来存储每行读取到的字符串*/
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(name);
            BufferedReader bf = new BufferedReader(fr);
            String str;/* 按行读取字符串*/
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }/* 对ArrayList中存储的字符串进行处理*/
        int length = arrayList.size();
        String[] array = new String[length];
        for (int i = 0; i < length; i++) {
            String s = arrayList.get(i);
            array[i] = s;
        }/* 返回数组*/
        return array;
    }

    public ArrayList<ArrayList<RequestMsgFromTxt>> RequestMsg(String ip, int com , int dpuId, int wayId){
        DevLink devLink = new DevLink();
        ArrayList<DevLink> devList = devLink.txtlist();
        PowerMeter powerMeter = new PowerMeter();
        ArrayList<PowerMeter>  pwList = powerMeter.txtlist();
        TemperConcentrator temperConcentrator = new TemperConcentrator();
        ArrayList<TemperConcentrator> tcList = temperConcentrator.txtlist();
        //txt中的拼接请求
        ArrayList<ArrayList<RequestMsgFromTxt>> RmList = new ArrayList<ArrayList<RequestMsgFromTxt>>();
        //从dpu_link中 查找通信管理机的此通道下的 对应的设备
        int slaveId;
        int beginId;
        int readLength;
        for(int i = 0;i < devList.size();i++){
            if(devList.get(i).getCmNum() == dpuId&& devList.get(i).getEnterNum() == wayId){
                int[] temp = devList.get(i).getDevId(); //此通信管理机的此通道上的 设备们
                for(int j=0;j<temp.length;j++) //遍历这些设备们
                {
                    int id = temp[j];  //拿到 设备ID，查找此设备具体的请求信息
                    for(int k=0;k<pwList.size();k++){
                        if(pwList.get(k).getId() == id){ //找到此设备
                            ArrayList<RequestMsgFromTxt> RmListChild = new ArrayList<RequestMsgFromTxt>();
                            slaveId = pwList.get(k).getSlaveAddress();
                            String [][] r = pwList.get(k).getMapRelation();//具体的分段
                            for(int w=0;w<r.length;w++){
                                beginId = Integer.parseInt(r[w][2]);
                                readLength = Integer.parseInt(r[w][3]);
                                RequestMsgFromTxt trMsg = new RequestMsgFromTxt();
                                trMsg.setIp(ip);
                                trMsg.setCom(com);
                                trMsg.setSlaveId(slaveId);
                                trMsg.setBeginId(beginId);
                                trMsg.setReadLength(readLength);
                                //System.out.println(ip+" "+com+" "+slaveId+" "+beginId+" "+readLength);
                                RmListChild.add(trMsg);
                            }
                            RmList.add(RmListChild);
                        }
                    }
                    for(int t=0;t<tcList.size();t++){
                        if(tcList.get(t).getId() == id){ //找到此设备
                            ArrayList<RequestMsgFromTxt> RmListChild = new ArrayList<RequestMsgFromTxt>();
                            slaveId = tcList.get(t).getSlaveAddress();
                            String [][] r = tcList.get(t).getMapRelation();
                            for(int w=0;w<r.length;w++){
                                beginId = Integer.parseInt(r[w][2]);
                                readLength = Integer.parseInt(r[w][3]);
                                RequestMsgFromTxt trMsg = new RequestMsgFromTxt();
                                trMsg.setIp(ip);
                                trMsg.setCom(com);
                                trMsg.setSlaveId(slaveId);
                                trMsg.setBeginId(beginId);
                                trMsg.setReadLength(readLength);
                                RmListChild.add(trMsg); //n{100001 COM8 9600 8 0 11}
                            }
                            RmList.add(RmListChild);  //n{n{100001 COM8 9600 8 0 11}, n{100002 COM8 9600 8 0 11},}
                        }
                    }

                }
            }
        }
        return RmList;
    }
}
