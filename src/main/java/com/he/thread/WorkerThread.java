package com.he.thread;

import com.he.equipments.ConcentratorDevice;

public class WorkerThread extends Thread{
	private final Channel channel;
	public WorkerThread(String name,Channel channel){
		super(name);
		this.channel = channel;
	}
	@Override
	public void run(){
		while(true){
			ConcentratorDevice concentratorDevice = channel.takeRequest();
			concentratorDevice.execute();
		}
	}
}
