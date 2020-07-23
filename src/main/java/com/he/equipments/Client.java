package com.he.equipments;

import com.he.thread.RequestChannel;
import com.he.thread.OnceRequestTask;
import com.he.thread.ReceiveChannel;
import com.he.thread.RequestMsg;
import com.serotonin.modbus4j.ip.IpParameters;
import java.util.ArrayList;

public class Client extends Thread{
    private final RequestChannel requestChannel;
    private final ReceiveChannel receiveChannel;
    private final ArrayList<TcpModbusMaster> tcpModbusMasters;

    public Client(RequestChannel requestChannel, ReceiveChannel receiveChannel, ArrayList<ModbusSlave> modbusSlaves) {
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
        this.requestChannel = requestChannel;
        this.receiveChannel = receiveChannel;
    }
    @Override
    public void run() {
        while (true) {
            try {
                for(TcpModbusMaster tcpModbusMaster : tcpModbusMasters){
                    ArrayList<RequestMsg>requestMsgs = tcpModbusMaster.getRequestMsgs();
                    for(int i=0;i<requestMsgs.size();i++){
                        requestChannel.putRequest(new OnceRequestTask(tcpModbusMaster.getTcpMaster(),requestMsgs.get(i),receiveChannel));
                    }
                }
               // System.out.println("\n" + Thread.currentThread().getName() + "******装载Request完毕********");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
