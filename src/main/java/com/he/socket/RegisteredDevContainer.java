package com.he.socket;

import com.he.equipments.ConcentratorDevice;

import java.util.*;

public class RegisteredDevContainer {
    private Map<String, ArrayList<ConcentratorDevice>>registeredDevContainer ;
    public RegisteredDevContainer(){
        registeredDevContainer = new HashMap<>();
    }
    public void putDev(String ip,  ArrayList<ConcentratorDevice> devObjList){
        registeredDevContainer.put(ip,devObjList);
    }
    private void deleteDev(String ip){
        registeredDevContainer.remove(ip);
    }
    /*判断设备是否已经注册
    如果 容器中有设备 则返回false
    如果 容器中没有设备 则返回true 向容器中添加此设备IP
    * */
    public boolean isRegistered(String ip){
        Set<String> keySet = registeredDevContainer.keySet();
        Iterator<String> it = keySet.iterator();
        while(it.hasNext()){
            String key = it.next();
            if(key.equals(ip)){
                return false;
            }
        }
        return  true;
    }
}
