package com.he.thread;

import com.he.equipments.ConcentratorDevice;

public class Channel {
	private static final int MAX_REQUEST = 100;
	private final ConcentratorDevice[] concentratorDeviceQueue;
	private int tail;
	private int head;
	private int count;
	
	private final WorkerThread threadPool[];
	public Channel(int threads){
		this.concentratorDeviceQueue = new ConcentratorDevice[MAX_REQUEST];
		this.head = 0;
		this.tail = 0;
		this.count = 0;
		threadPool = new WorkerThread[threads];
		for(int i=0;i<threadPool.length;i++){
			threadPool[i] = new WorkerThread("Worker-"+i,this);
		}
	}
	public void startWorkers(){
		for(int i=0;i<threadPool.length;i++){
			threadPool[i].start();
		}
	}
	public synchronized void putRequest(ConcentratorDevice concentratorDevice){
		while(count>= concentratorDeviceQueue.length){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		concentratorDeviceQueue[tail] = concentratorDevice;
		tail = (tail + 1)% concentratorDeviceQueue.length;
		count++;
		notifyAll();
	}
	public synchronized ConcentratorDevice takeRequest(){
		while(count <=0){
			try{
				wait();
			}catch(InterruptedException e){
			}
		}
		ConcentratorDevice concentratorDevice = concentratorDeviceQueue[head];
		head = (head+1)% concentratorDeviceQueue.length;
		count --;
		notifyAll();
		return concentratorDevice;
	}
}
