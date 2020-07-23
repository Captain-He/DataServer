package com.he.thread;

import java.sql.SQLException;

public class ResolverThread extends Thread {
    private final ReceiveChannel receiveChannel;

    public ResolverThread(String name, ReceiveChannel receiveChannel) {
        super(name);
        this.receiveChannel = receiveChannel;
    }

    @Override
    public void run(){
        while(true){
            OnceResolveTask onceRequestTask = receiveChannel.takeOnceResolveTask();
            try {
                onceRequestTask.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
