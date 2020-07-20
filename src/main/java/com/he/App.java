package com.he;

import com.he.equipments.CommunicationManager;
import com.he.equipments.ModbusSlave;
import com.he.equipments.PowerMeter;
import com.he.equipments.TemperConcentrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args){
       // CommunicationManager communicationManager = new CommunicationManager();
        ArrayList<CommunicationManager>communicationManagers =  GetCommunicationManagers();
        for(CommunicationManager communicationManager:communicationManagers){
           // System.out.println(communicationManager.getId());
            Map<String,ModbusSlave> modbusSlaves = communicationManager.getModbusSlaves();
            for (Map.Entry<String,ModbusSlave> modbusSlave: modbusSlaves.entrySet()) {

                ArrayList<PowerMeter> powerMeters = modbusSlave.getValue().getPowerMeters();
                ArrayList<TemperConcentrator> temperConcentrators = modbusSlave.getValue().getTemperConcentrator();
                if(modbusSlave.getValue().getId()!=0){
                    System.out.println(modbusSlave.getKey() + "：" + modbusSlave.getValue().getDevNum());
                    for(PowerMeter powerMeter:powerMeters){
                       System.out.println(powerMeter.getId());
                        //toprint(powerMeter.getMapRelation());
                        System.out.print(powerMeter.getRequest().resolver.resolverMap.size()+"@");
                    }
                    for(TemperConcentrator temperConcentrator:temperConcentrators){
                      //  toprint(temperConcentrator.getMapRelation());
                        System.out.print(temperConcentrator.getRequest().resolver.resolverMap.size()+"@@@");
                    }
                }

            }
        }

//        ModbusMaster tcpMaster = TcpMaster.getTcpMaster("127.0.0.1",502);
//        Modbus4jReader reader = new Modbus4jReader(tcpMaster);
//        try {
//            short[] a = reader.readHoldingRegister(1,0,11);
//            for(int i=0;i<a.length;i++){
//                System.out.print(a[i]);
//            }
//            System.out.print("/n");
//            short[] b = reader.readHoldingRegister(2,20,60);
//            for(int i=0;i<b.length;i++){
//                System.out.print(b[i]);
//            }
//        } catch (ModbusTransportException e) {
//            e.printStackTrace();
//        } catch (ErrorResponseException e) {
//            e.printStackTrace();
//        } catch (ModbusInitException e) {
//            e.printStackTrace();
//        }
    }
    public static ArrayList<CommunicationManager> GetCommunicationManagers(){
        TxtFileReader readTxt = new TxtFileReader();
        String b [] =  readTxt.toArrayByFileReader1(".\\src\\App\\java\\com\\he\\txt\\dpu_list.txt");
        ArrayList<CommunicationManager> communicationManagers = new ArrayList<>();
        for(int i=0;i<b.length;i++) {
            String temp[] = b[i].split(" ");
            CommunicationManager newDev = new CommunicationManager();
            if(temp[0].isEmpty()||temp[0]==null) continue;
            newDev.setId(temp[0]);
            newDev.setTerminal(temp[1]);
            newDev.setScNum(temp[2]);
            String[][] comIp=new String[temp.length-3][3];
            Map<String,ModbusSlave> modbusSlaves = new HashMap<>();
            for(int j=3;j<Integer.parseInt(temp[2])+3;j++){
                comIp[j-3] = ComIpSplit(temp[j]);
                ModbusSlave modbusSlave = new ModbusSlave();

                modbusSlaves.put(temp[j],modbusSlave.getModbusSlave(temp[0],j-2,comIp[j-3][1]));
            }
            newDev.setModbusSlaves(modbusSlaves);
            communicationManagers.add(newDev);
        }
        return communicationManagers;
    }
    //串口编号/IP地址/端口 分割
    public static String[] ComIpSplit(String str){
        String splitArray[] = {};
        if((str != null)&&(!isEquals(str,"-"))){
            splitArray = str.split("/");
        }
        return splitArray;
    }
    //判断字符串a 是否与 字符串b 相等
    public static boolean isEquals(String a,String b){
        return a.replaceAll("\n", "").replaceAll(" ", "").equals(b);
    }
    public  static void toprint(String a[][]){
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[i].length;j++){
            System.out.print(a[i][j]+" ");
            }
            System.out.println();
    }
}
