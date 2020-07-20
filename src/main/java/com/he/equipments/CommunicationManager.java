package com.he.equipments;

import java.util.Map;

public class CommunicationManager {
	private int id; //通信管理机编号
	private String terminal; //C端/S端
	private int pcId; //PC编号(如是C端)
	private int scNum;//串行通道数量
	private String mapRelation[][]; //总线/IP/端口映射关系
	private Map<String,ModbusSlave> modbusSlaves;

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
	public void setModbusSlaves(Map<String,ModbusSlave> modbusSlaves) {
		this.modbusSlaves = modbusSlaves;
	}
	public Map<String,ModbusSlave> getModbusSlaves() {
		return modbusSlaves;
	}




}
