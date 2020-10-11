package com.he.thread;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ResolverMsg {
public final Map<String,String>resolverMap;
    private ConcurrentLinkedQueue<String> sqlQueue;

    public ResolverMsg(int ConcentratorDevideID, String []resolverFromTxt,ConcurrentLinkedQueue<String> sqlQueue){
        this.sqlQueue = sqlQueue;
        Map<String,String>resolverMap = new HashMap<>();
        if(ConcentratorDevideID>100000&&ConcentratorDevideID<200000){
            for(int i=0;i<resolverFromTxt.length;i++){
                if(resolverFromTxt[i] == null||resolverFromTxt[i] == "")continue;
               String temp[] =  ComIpSplit(resolverFromTxt[i]);
               if(temp.length == 0||temp.length == 1)continue;
                if(ConcentratorDevideID == Integer.parseInt(temp[1])){
                    resolverMap.put(ConcentratorDevideID+"",resolverFromTxt[i]);
                }
            }
        }
        if(ConcentratorDevideID>300000&&ConcentratorDevideID<400000){
            for(int i=0;i<resolverFromTxt.length;i++){
                if(resolverFromTxt[i] == null||resolverFromTxt[i] == "")continue;
                String temp[] =  ComIpSplit(resolverFromTxt[i]);
                if(temp.length == 0||temp.length == 1)continue;
                if(ConcentratorDevideID == Integer.parseInt(temp[1])){
                    resolverMap.put(temp[0],resolverFromTxt[i]);
                }
            }
        }
        this.resolverMap = resolverMap;
    }

    public void resolve(short [] buffer) {
        String aimStr[] = new String[resolverMap.size()];
        int index = 0;
        short sigleAimStr[] = new short[51];
        for (Map.Entry<String, String> entry : resolverMap.entrySet()) {
            String sensorID = entry.getKey();
            String solver[] = ComIpSplit(entry.getValue(), " ");
            // solver[0] 集中器设备ID，slover[1]传感器设备ID，solver[2]数据项数量，从3开始到solver.length都是每个数据项的【】
            aimStr[index] = "INSERT INTO sensorData (SourceAddr,GroupAddr,samplingTime,item1,item2,item3,item4,item5,item6,item7,item8,item9,item10,item11,item12,item13,item14,item15,item16,item17,item18,item19,item20,item21,item22,item23,item24,item25,item26,item27,item28,item29,item30,item31,item32,item33,item34,item35,item36,item37,item38,item39,item40,item41,item42,item43,item44,item45,item46,item47,item48,item49,item50) VALUES ('"+solver[0];
            Arrays.fill(sigleAimStr, (short) -1);
            for (int i = 3; i < Integer.parseInt(solver[2]) + 3; i++) { //遍历项解析
                String solverStr[] = ComIpSplit(solver[i], "/");
                //aimStr[index] += "item" + solverStr[0] + "-";
                short temp = 0 ;
                for (int j = Integer.parseInt(solverStr[1]); j < Integer.parseInt(solverStr[1]) + Integer.parseInt(solverStr[2]); j++) {
                    temp += buffer[j];
                }
                sigleAimStr[Integer.parseInt(solverStr[0])] = temp;
            }
            for(int i=1;i<51;i++){
                aimStr[index]+=",";
                if(sigleAimStr[i] == -1){
                    aimStr[index]+="0";
                }else{
                    aimStr[index]+=sigleAimStr[i];
                }
            }
            aimStr[index]+=")";
            System.out.println(aimStr[index]);
            index++;
        }
        for (int i = 0; i < aimStr.length; i++) {
            sqlQueue.add(aimStr[i]);
        }
    }
    private static String[] ComIpSplit(String str, String splitChar) {	//串口编号/IP地址/端口 分割
        String splitArray[] = {};
        if ((str != null) && (!isEquals(str, "-"))) {
            splitArray = str.replaceAll("  ", " ").split(splitChar);
        }
        return splitArray;
    }
    //串口编号/IP地址/端口 分割
    private static String[] ComIpSplit(String str){
        String splitArray[] = {};
        if((str != null)&&(!isEquals(str,"-"))){
            splitArray = str.split(" ");
        }
        return splitArray;
    }
    //判断字符串a 是否与 字符串b 相等
    private static boolean isEquals(String a,String b){
        return a.replaceAll("\n", "").replaceAll(" ", "").equals(b);
    }
}
