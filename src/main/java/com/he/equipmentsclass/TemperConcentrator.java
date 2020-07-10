package com.he.equipmentsclass;

import com.he.TxtFileReader;

import java.util.ArrayList;

public class TemperConcentrator extends SensingDevices{

	private String deviceType; //设备型号

	public void setDeviceType(String deviceType){
		this.deviceType= deviceType;
	}
	public String getDeviceType(){
		return deviceType;
	}
	public ArrayList<TemperConcentrator> txtlist(){
		TxtFileReader readTxt = new TxtFileReader();
		String b [] =  readTxt.toArrayByFileReader1("D:\\Repository\\ModbusJ\\modbus4j\\src\\main\\java\\com\\he\\txt\\concentrator_list.txt");
		ArrayList<TemperConcentrator> temperConcentrator = new ArrayList<>();
		for(int i=0;i<b.length;i++) { //i表示文件的行
			String temp[] = b[i].split(" "); //对每一行进行空格分割
			TemperConcentrator newDev = new TemperConcentrator();
			if(temp[0].isEmpty()||temp[0]==null) continue;
			if(300000<Integer.parseInt(temp[0])&&Integer.parseInt(temp[0])<400000){
				newDev.setId(Integer.parseInt(temp[0]));
				newDev.setComNum(temp[1]);
				newDev.setSlaveAddress(Integer.parseInt(temp[7]));
				newDev.setReadTimes(Integer.parseInt(temp[8]));
				String[][] readMessage=new String[temp.length-9][4];
				for(int j=9;j<Integer.parseInt(temp[8])+9;j++){
					readMessage[j-9] = ComIpSplit(temp[j]);
				}
				newDev.setMapRelation(readMessage);
				temperConcentrator.add(newDev);
			}
		}
		return temperConcentrator;
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
}
