package com.he.equipments;

public class GeneralMap {
	private int id;
	private String devType;
	private String devModel;
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return id;
	}
	public void setDevType(String devType){
		this.devType = devType;
	}
	public String getDevType(){
		return devType;
	}
	public void setDevModel(String devModel){
		this.devModel = devModel;
	}
	public String getDevModel(){
		return devModel;
	}
}
