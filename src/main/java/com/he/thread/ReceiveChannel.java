package com.he.thread;

public class ReceiveChannel {
    private static final int MAX_REQUEST = 100;
    private final OnceResolveTask[] onceResolveTaskQueue;
    private int tail;
    private int head;
    private int count;

    private final ResolverThread threadPool[];
    public ReceiveChannel(int threads){
        this.onceResolveTaskQueue = new OnceResolveTask[MAX_REQUEST];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        threadPool = new ResolverThread[threads];
        for(int i=0;i<threadPool.length;i++){
            threadPool[i] = new ResolverThread("ReceiveThread-"+i,this);
        }
    }
    public void startWorkers(){
        for(int i=0;i<threadPool.length;i++){
            threadPool[i].start();
        }
    }
    public synchronized void putOnceResolveTask(OnceResolveTask onceResolveTask){
        while(count>= onceResolveTaskQueue.length){
            try{
                wait();
            }catch(InterruptedException e){

            }
        }
        onceResolveTaskQueue[tail] = onceResolveTask;
        tail = (tail + 1)% onceResolveTaskQueue.length;
        count++;
        notifyAll();
    }
    public synchronized OnceResolveTask takeOnceResolveTask(){
        while(count <=0){
            try{
                wait();
            }catch(InterruptedException e){
            }
        }
        OnceResolveTask onceResolveTask = onceResolveTaskQueue[head];
        head = (head+1)% onceResolveTaskQueue.length;
        count --;
        notifyAll();
        return onceResolveTask;
    }
}
