package com.he.socket;

import com.he.equipments.ConcentratorDevice;

import java.util.*;

public class RegisteredDevContainer {
    private Map<String, ArrayList<ConcentratorDevice>>bottle ;
    public RegisteredDevContainer(){
        this.bottle = new HashMap<String, ArrayList<ConcentratorDevice>>();
    }
    public void putDev(String ip,  ArrayList<ConcentratorDevice> devObjList){
        bottle.put(ip,devObjList);
    }
    public void deleteDev(String ip){
        bottle.remove(ip);
    }
    /*判断设备是否已经注册
    如果 容器中有设备 则返回false
    如果 容器中没有设备 则返回true 向容器中添加此设备IP
    * */
    public boolean isRegistered(String ip){
        Set<String> keySet = bottle.keySet();
        Iterator<String> it = keySet.iterator();
        while(it.hasNext()){
            String key = it.next();
            if(key.equals(ip)){
                return false;
            }
        }
        return  true;
    }
    public ArrayList<ConcentratorDevice> getDevs(){
        ArrayList<ConcentratorDevice> devlist = new ArrayList<ConcentratorDevice>();
        for(ArrayList<ConcentratorDevice> devs:bottle.values()){
            for(ConcentratorDevice dev:devs){
                devlist.add(dev);
            }
        }
        return devlist;
    }
}
