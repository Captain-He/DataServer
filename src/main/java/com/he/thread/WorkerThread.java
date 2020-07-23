package com.he.thread;

public class WorkerThread extends Thread{
	private final RequestChannel requestChannel;
	public WorkerThread(String name,RequestChannel requestChannel){
		super(name);
		this.requestChannel = requestChannel;

	}
	@Override
	public void run(){
		while(true){
			OnceRequestTask onceRequestTask = requestChannel.takeRequest();
			try {
				onceRequestTask.execute();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
