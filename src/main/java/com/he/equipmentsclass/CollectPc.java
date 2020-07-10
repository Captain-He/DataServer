package com.he.equipmentsclass;

public class CollectPc {
	private int id;
	private String type;
	private String ipAdress;
	private int comNum;
	
	public void setId(String id){
		this.id = Integer.parseInt(id);
	}
	public int getId(){
		return id;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return type;
	}
	public void setIpAdress(String ipAdress){
		this.ipAdress = ipAdress;
	}
	public String getIpAdress(){
		return ipAdress;
	} 
	public void SetComNum(String comNum){
		this.comNum = Integer.parseInt(comNum);
	}
	public int getComNum(){
		return comNum;
	}
}
