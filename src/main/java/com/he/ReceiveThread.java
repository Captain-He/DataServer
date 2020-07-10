package com.he;

public class ReceiveThread extends Thread{
    private final ReceiveChannel channel;
    public ReceiveThread(String name, ReceiveChannel channel){
        super(name);
        this.channel = channel;

    }
    @Override
    public void run(){
        while(true){
            Response response = channel.takeReceive();
            response.execute();
        }
    }
}