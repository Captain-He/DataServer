package com.he;

public class Channel {
	//private static final int MAX_REQUEST = 100;
	private final Request[] requestQueue;
	private int tail;
	private int head;
	private int count;
	
	private final WorkerThread threadPool[];
	public Channel(int threads,int requestNum){
		this.requestQueue = new Request[requestNum];
		this.head = 0;
		this.tail = 0;
		this.count = 0;
		threadPool = new WorkerThread[threads];
		System.out.format("请求的最大数量%d\n",requestNum);
		for(int i=0;i<threadPool.length;i++){
			threadPool[i] = new WorkerThread("Worker-"+i,this);
		}
	}
	public void startWorkers(){
		for(int i=0;i<threadPool.length;i++){
			threadPool[i].start();
		}
	}
	public synchronized void putRequest(Request request){
		while(count>=requestQueue.length){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		requestQueue[tail] = request;
		tail = (tail + 1)% requestQueue.length;
		count++;
		notifyAll();
	}
	public synchronized Request takeRequest(){
		while(count <=0){
			try{
				wait();
			}catch(InterruptedException e){
			}
		}
		Request request = requestQueue[head];
		head = (head+1)%requestQueue.length;
		count --;
		notifyAll();
		return request;
	}
}
