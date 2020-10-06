package com.he.socket;

import com.he.equipments.*;
import com.he.thread.Channel;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @description: 服务端客户消息处理线程类
 **/
public class SocketServerClientHandler extends Thread{
    private final Channel channel;
    private final CommunicationManager communicationManager;
    //每个消息通过Socket进行传输
    private Socket clientConnectSocket;
    private RegisteredDevContainer registeredDevContainer;
    public SocketServerClientHandler(Socket clientConnectSocket,Channel channel,CommunicationManager communicationManager){
        this.clientConnectSocket = clientConnectSocket;
        this.communicationManager = communicationManager;
        this.channel = channel;
        registeredDevContainer = new RegisteredDevContainer();
    }

   @Override
    public void run(){
        try {
            InputStream inputStream = clientConnectSocket.getInputStream();
            while (true) {
                byte[] data = new byte[100];
                int len;
                while ((len = inputStream.read(data)) != -1) {
                    String message = new String(data, 0, len);
                    for(CommunicationManagerCom communicationManagerCom : communicationManager.getCommunicationManagerComs()){
                        if(communicationManagerCom.getDevNum() == 0||message.equals(communicationManagerCom.getCommunicationManagerComIP())||registeredDevContainer.isRegistered(message))continue;
                        ArrayList<ConcentratorDevice> devList = new ArrayList<>();
                        for(PowerMeter powerMeter :communicationManagerCom.getPowerMeters()){
                           devList.add(powerMeter);
                        }
                        for(TemperConcentrator temperConcentrator :communicationManagerCom.getTemperConcentrator()){
                            devList.add(temperConcentrator);
                        }
                        registeredDevContainer.putDev(message,devList);
                    }
                    System.out.println("客户端传来消息: " + message);
                   // clientConnectSocket.getOutputStream().write(data);
                }
                try{
                    clientConnectSocket.sendUrgentData(0xFF);
                }catch (Exception e){
                    System.out.println("客户端已经断开。。。");

                    stop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
