package com.he.equipmentsclass;

import com.he.TxtFileReader;

import java.util.ArrayList;

public class CommunicationManager {
	private int id; //通信管理机编号
	private String terminal; //C端/S端
	private int pcId; //PC编号(如是C端)
	private int scNum;//串行通道数量
	private String mapRelation[][]; //总线/IP/端口映射关系

	public void setId(String id){
		this.id = (int)Double.parseDouble(id);
	}
	public int getId(){
		return id;
	}
	public void setTerminal(String terminal){
		this.terminal = terminal;
	}
	public String getTerminal(){
		return terminal;
	}
	public void setPcId(String pcId){
		this.pcId = (int)Double.parseDouble(pcId);
	}
	public int getPcId(){
		return pcId;
	}
	public void setScNum(String scNum){
		this.scNum = (int)Double.parseDouble(scNum);
	}
	public int getScNum(){
		return scNum;
	}
	public void setMapRelation(String mapRelation [][]){
		this.mapRelation = mapRelation;
	}
	public String [][]getMapRelation(){
		return mapRelation;
	}

	public ArrayList<CommunicationManager> txtlist(){
		TxtFileReader readTxt = new TxtFileReader();
		String b [] =  readTxt.toArrayByFileReader1("D:\\Repository\\ModbusJ\\modbus4j\\src\\main\\java\\com\\he\\txt\\dpu_list.txt");
		ArrayList<CommunicationManager> comManager = new ArrayList<>();
		for(int i=0;i<b.length;i++) {
			String temp[] = b[i].split(" ");
			CommunicationManager newDev = new CommunicationManager();
			if(temp[0].isEmpty()||temp[0]==null) continue;
			newDev.setId(temp[0]);
			newDev.setTerminal(temp[1]);
			newDev.setScNum(temp[2]);
			String[][] comIp=new String[temp.length-3][3];
			for(int j=3;j<Integer.parseInt(temp[2])+3;j++){
				comIp[j-3] = ComIpSplit(temp[j]);
			}
			newDev.setMapRelation(comIp);
			comManager.add(newDev);
		}
		return comManager;
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
