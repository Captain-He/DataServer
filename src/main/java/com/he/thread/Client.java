package com.he.thread;

import com.he.equipments.*;
import com.he.socket.RegisteredDevContainer;
import com.he.socket.SocketClient;

public class Client extends Thread{
    private Channel channel;
    private RegisteredDevContainer registeredDevContainer;
    public Client(Channel channel,RegisteredDevContainer registeredDevContainer) {
        this.registeredDevContainer = registeredDevContainer;
        this.channel = channel;
    }
    @Override
    public void run() {
        while (!SocketClient.clientExit) {
            System.out.println("client 工作标志");
            for(ConcentratorDevice dev : registeredDevContainer.getDevs()){
                channel.putRequest(dev.getRequestMsg());
            }
        }
    }
}
