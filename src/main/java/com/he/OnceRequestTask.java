package com.he;

import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;

public class OnceRequestTask extends Thread {
    private  TcpMaster tcpMaster;
    private  RequestMsg requestMsg;
    public OnceRequestTask(TcpMaster tcpMaster,RequestMsg requestMsg){
        this.tcpMaster = tcpMaster;
        this.requestMsg = requestMsg;
    }
    public void execute() throws InterruptedException {
        Modbus4jReader reader = new Modbus4jReader(tcpMaster);
        for(int i=0;i<requestMsg.segmentNum;i++){
            System.out.format("%s,%s,%s\n",requestMsg.slaveID,requestMsg.requestSegment[i][2],requestMsg.requestSegment[i][3]);
            try {
                short[] a = reader.readHoldingRegister(requestMsg.slaveID,Integer.parseInt(requestMsg.requestSegment[i][2]),Integer.parseInt(requestMsg.requestSegment[i][3]));
                for(int o=0;o<a.length;o++){
                    System.out.print(a[o]+" ");
                }
                System.out.println();
               // Thread.sleep(50000);
            } catch (ModbusTransportException e) {
                e.printStackTrace();
            } catch (ErrorResponseException e) {
                e.printStackTrace();
            } catch (ModbusInitException e) {
                e.printStackTrace();
            }
        }
    }
}
