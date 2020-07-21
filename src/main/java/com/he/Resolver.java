package com.he;

import java.util.HashMap;
import java.util.Map;

public class Resolver {
public final Map<String,String>resolverMap;
    public Resolver(int ConcentratorDevideID, String []resolverFromTxt){
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
    //串口编号/IP地址/端口 分割
    private static String[] ComIpSplit(String str){
        String splitArray[] = {};
        if((str != null)&&(!isEquals(str,"-"))){
            splitArray = str.split(" ");
        }
        return splitArray;
    }
    //判断字符串a 是否与 字符串b 相等
    public  static boolean isEquals(String a,String b){
        return a.replaceAll("\n", "").replaceAll(" ", "").equals(b);
    }
}
