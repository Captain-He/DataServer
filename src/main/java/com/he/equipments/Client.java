package com.he.equipments;

import com.he.Channel;
import com.he.OnceRequestTask;
import com.he.ReceiveChannel;
import com.he.RequestMsg;
import com.serotonin.modbus4j.ip.IpParameters;
import java.util.ArrayList;

public class Client extends Thread{
    private final Channel channel;
    private final ReceiveChannel receiveChannel;
    private final ArrayList<TcpModbusMaster> tcpModbusMasters;

    public Client(Channel channel, ReceiveChannel receiveChannel,ArrayList<ModbusSlave> modbusSlaves) {
         ArrayList<TcpModbusMaster> tcpModbusMasters = new ArrayList<>();
        for (ModbusSlave modbusSlave: modbusSlaves) {
            //开始遍历modbusSlave
            if(modbusSlave.getId() == 0)continue;
            IpParameters params = new IpParameters();
            params.setHost(modbusSlave.getCommunicationManagerComIP());// 设置ip
            params.setPort(modbusSlave.getChuanID());
            tcpModbusMasters.add(new TcpModbusMaster(params, true,modbusSlave));// 获取ModbusMaster对象
        }
        this.tcpModbusMasters = tcpModbusMasters;
        this.channel = channel;
        this.receiveChannel = receiveChannel;
    }
    @Override
    public void run() {
        while (true) {
            try {
                for(TcpModbusMaster tcpModbusMaster : tcpModbusMasters){
                    ArrayList<RequestMsg>requestMsgs = tcpModbusMaster.getRequestMsgs();
                    for(int i=0;i<requestMsgs.size();i++){
                        channel.putRequest(new OnceRequestTask(tcpModbusMaster.getTcpMaster(),requestMsgs.get(i)));
                    }
                }
               // System.out.println("\n" + Thread.currentThread().getName() + "******装载Request完毕********");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
