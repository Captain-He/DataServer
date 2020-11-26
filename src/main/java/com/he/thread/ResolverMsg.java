package com.he.thread;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ResolverMsg {
public final Map<String,String>resolverMap;
    private ConcurrentLinkedQueue<String> sqlQueue;

    public ResolverMsg(int ConcentratorDevideID, String []resolverFromTxt,ConcurrentLinkedQueue<String> sqlQueue){
        this.sqlQueue = sqlQueue;
        Map<String,String>resolverMap = new HashMap<String, String>();
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
            aimStr[index] = solver[0];
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
            aimStr[index]+=" 0x00";
            aimStr[index]+=" "+getTimestamp(new Date());
            for(int i=1;i<51;i++){
                aimStr[index]+=" ";
                if(sigleAimStr[i] == -1){
                    aimStr[index]+="0x00";
                }else{
                    aimStr[index]+=sigleAimStr[i];
                }
            }
            System.out.println(aimStr[index]);
            index++;
        }
        for (int i = 0; i < aimStr.length; i++) {
            sqlQueue.add(StrToBinstr(aimStr[i]));
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
    /**
     获取精确到毫秒的时间戳
     * @param date
     * @return
     **/
    public static Long getTimestamp(Date date){
        if (null == date) {
            return (long) 0;
        }
        String timestamp = String.valueOf(date.getTime());
        return Long.valueOf(timestamp);
    }
    // 将字符串转换成二进制字符串，以空格相隔
    private String StrToBinstr(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]);
        }
        return result;
    }
}
