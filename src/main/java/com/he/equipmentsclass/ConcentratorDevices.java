package com.he.equipmentsclass;

public class ConcentratorDevices {
	/*
	 * 传感器设备 类，是电力仪表 和 测温集中器 的 父类
	 * created in 2019 09 15
	 *
	 */

	private int id ;//采集设备唯一ID
	private String accessType; //接入方式(以太网/串口)
	private String modbusType; //通信协议(modbusTCP/modbusRTU)
	private int underNum; //隶属的通信管理机编号
	private String comNum; //串口编号
	private int bpsNum; //波特率
	private int dataBit; //数据位
	private char checkBit; //校验位
	private float stopBit; //停止位
	private char fluidControl;//流控
	private int slaveAddress; //从站地址
	private int readTimes;
	private String mapRelation[][]; //总线/IP/端口映射关系

	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return id;
	}
	public void setAccessType(String accessType){
		this.accessType = accessType;
	}
	public String getAccessType(){
		return accessType;
	}
	public void setModbusType(String modbusType){
		this.modbusType = modbusType;
	}
	public String getModbusType(){
		return modbusType;
	}

	public void setUnderNum(int underNum){
		this.underNum = underNum;
	}
	public int getUnderNum(){
		return underNum;
	}
	public void setComNum(String comNum){
		this.comNum = comNum;
	}
	public String getComNum(){
		return comNum;
	}
	public void setBpsNum(int bpsNum){
		this.bpsNum = bpsNum;
	}
	public int getBpsNum(){
		return bpsNum;
	}
	public void setDataBit(int dataBit){
		this.dataBit = dataBit;
	}
	public int getDataBit(){
		return dataBit;
	}
	public void setCheckBit(char checkBit){
		this.checkBit = checkBit;
	}
	public char getCheckBit(){
		return checkBit;
	}
	public void setStopBit(float stopBit){
		this.stopBit = stopBit;
	}
	public float getStopBit(){
		return stopBit;
	}
	public void setFluidControl(char fluidControl){
		this.fluidControl = fluidControl;
	}
	public char getFluidControl(){
		return fluidControl;
	}
	public void setSlaveAddress(int slaveAddress){
		this.slaveAddress= slaveAddress;
	}
	public int getSlaveAddress(){
		return slaveAddress;
	}
	public void setReadTimes(int readTimes){
		this.readTimes = readTimes;
	}
	public int getReadTimes(){
		return readTimes;
	}
	public void setMapRelation(String mapRelation [][]){
		this.mapRelation = mapRelation;
	}
	public String [][]getMapRelation(){
		return mapRelation;
	}
}