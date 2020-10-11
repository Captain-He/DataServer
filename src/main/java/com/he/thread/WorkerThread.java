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
			System.out.println("工作线程 标志");
			RequestMsg requestMsg = channel.takeRequest();
			try {
				requestMsg.resolverMsg.resolve(requestMsg.Request());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
