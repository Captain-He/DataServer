package com.he.thread;

public class RequestChannel {
	private static final int MAX_REQUEST = 10000;
	private final OnceRequestTask[] onceRequestTaskQueue;
	private int tail;
	private int head;
	private int count;
	
	private final WorkerThread threadPool[];
	public RequestChannel(int threads){
		this.onceRequestTaskQueue = new OnceRequestTask[MAX_REQUEST];
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
	public synchronized void putRequest(OnceRequestTask onceRequestTask){
		while(count>= onceRequestTaskQueue.length){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		onceRequestTaskQueue[tail] = onceRequestTask;
		tail = (tail + 1)% onceRequestTaskQueue.length;
		count++;
		notifyAll();
	}
	public synchronized OnceRequestTask takeRequest(){
		while(count <=0){
			try{
				wait();
			}catch(InterruptedException e){
			}
		}
		OnceRequestTask onceRequestTask = onceRequestTaskQueue[head];
		head = (head+1)% onceRequestTaskQueue.length;
		count --;
		notifyAll();
		return onceRequestTask;
	}
}
