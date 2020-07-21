package com.he;

public class WorkerThread extends Thread{
	private final Channel channel;
	public WorkerThread(String name,Channel channel){
		super(name);
		this.channel = channel;

	}
	@Override
	public void run(){
		while(true){
			OnceRequestTask onceRequestTask = channel.takeRequest();
			try {
				onceRequestTask.execute();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
