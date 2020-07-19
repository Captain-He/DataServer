package com.he.equipmentsclass;

import com.he.TxtFileReader;

import java.util.ArrayList;

public class TemperConcentrator extends ConcentratorDevices {

	private String deviceType; //设备型号

	public void setDeviceType(String deviceType){
		this.deviceType= deviceType;
	}
	public String getDeviceType(){
		return deviceType;
	}

}
