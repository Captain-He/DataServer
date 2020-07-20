package com.he.equipments;

public class TemperComparative {

	private int wenId;
	private int jiId;
	private int address;
	
	public void setWenId(String wenId){
		this.wenId = Integer.parseInt(wenId);
	}
	
	public int getWenId(){
		return wenId; 
	}
	
	public void setJiId(String jiId){
		this.jiId = Integer.parseInt(jiId);
	}
	public int getJiId(){
		return jiId;
	}
	public void setAddress(String address){
		this.address = Integer.parseInt(address);
	}
	public int getAddress(){
		return address;
	}
	

}
