package com.he.thread;

import com.he.equipments.ConcentratorDevice;

public class Channel {
	private static final int MAX_REQUEST = 100;
	private final RequestMsg[] requestMsgQueue;
	private int tail;
	private int head;
	private int count;
	
	private final WorkerThread threadPool[];
	public Channel(int threads){
		this.requestMsgQueue = new RequestMsg[MAX_REQUEST];
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
	public synchronized void putRequest(RequestMsg requestMsg){
		while(count>= requestMsgQueue.length){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		requestMsgQueue[tail] = requestMsg;
		tail = (tail + 1)% requestMsgQueue.length;
		count++;
		notifyAll();
	}
	public synchronized RequestMsg takeRequest(){
		while(count <=0){
			try{
				wait();
			}catch(InterruptedException e){
			}
		}
		RequestMsg requestMsg = requestMsgQueue[head];
		head = (head+1)% requestMsgQueue.length;
		count --;
		notifyAll();
		return requestMsg;
	}
}
