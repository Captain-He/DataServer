package com.he.equipments;

import com.serotonin.modbus4j.ip.tcp.TcpMaster;

public class TemperConcentrator extends ConcentratorDevice {

	private String deviceType; //设备型号
	public void setDeviceType(String deviceType){
		this.deviceType= deviceType;
	}
	public String getDeviceType(){

		return deviceType;
	}

}
