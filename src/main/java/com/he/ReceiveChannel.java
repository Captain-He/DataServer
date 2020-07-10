package com.he;

public class ReceiveChannel {
    private static final int MAX_REQUEST = 100;
    private final Response[] responseQueue;
    private int tail;
    private int head;
    private int count;

    private final ReceiveThread threadPool[];
    public ReceiveChannel(int threads){
        this.responseQueue = new Response[MAX_REQUEST];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        threadPool = new ReceiveThread[threads];
        //System.out.format("请求的最大数量%d\n",requestNum);
        for(int i=0;i<threadPool.length;i++){
            threadPool[i] = new ReceiveThread("ReceiveThread-"+i,this);
        }
    }
    public void startWorkers(){
        for(int i=0;i<threadPool.length;i++){
            threadPool[i].start();
        }
    }
    public synchronized void putReceive(Response response){
        while(count>= responseQueue.length){
            try{
                wait();
            }catch(InterruptedException e){

            }
        }
        responseQueue[tail] = response;
        tail = (tail + 1)% responseQueue.length;
        count++;
        notifyAll();
    }
    public synchronized Response takeReceive(){
        while(count <=0){
            try{
                wait();
            }catch(InterruptedException e){
            }
        }
        Response response = responseQueue[head];
        head = (head+1)% responseQueue.length;
        count --;
        notifyAll();
        return response;
    }
}
