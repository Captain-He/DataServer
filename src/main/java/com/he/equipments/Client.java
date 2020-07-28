package com.he.equipments;

import com.he.thread.Channel;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;

public class Client extends Thread{
    private final Channel channel;
    private final CommunicationManager communicationManager;
    public Client(Channel channel,CommunicationManager communicationManager) {
        this.communicationManager = communicationManager;
        this.channel = channel;
    }
    @Override
    public void run() {
        while (true) {
            try {
                for(CommunicationManagerCom communicationManagerCom : communicationManager.getCommunicationManagerComs()){
                    if(communicationManagerCom.getDevNum() == 0)continue;
                    for(PowerMeter powerMeter :communicationManagerCom.getPowerMeters()){
                        channel.putRequest(powerMeter);
                    }
                    for(TemperConcentrator temperConcentrator :communicationManagerCom.getTemperConcentrator()){
                        channel.putRequest(temperConcentrator );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
