package com.he.thread;

import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OnceRequestTask extends Thread {
    private  TcpMaster tcpMaster;
    private  RequestMsg requestMsg;
    private ReceiveChannel receiveChannel;
    private ConcurrentLinkedQueue<String> sqlQueue;
    public OnceRequestTask(TcpMaster tcpMaster,RequestMsg requestMsg,ReceiveChannel receiveChannel,ConcurrentLinkedQueue<String> sqlQueue ){
        this.tcpMaster = tcpMaster;
        this.requestMsg = requestMsg;
        this.receiveChannel = receiveChannel;
        this.sqlQueue = sqlQueue;
    }
    public void execute() throws InterruptedException {
        Modbus4jReader reader = new Modbus4jReader(tcpMaster);
        int MAX_LENGTH = 0;
        for(int i=0;i<requestMsg.segmentNum;i++){
            int allLengthTemp=Integer.parseInt(requestMsg.requestSegment[i][2])+Integer.parseInt(requestMsg.requestSegment[i][3]);
            if(MAX_LENGTH<allLengthTemp){
                MAX_LENGTH = allLengthTemp;
            }
        }
        short [] buffer =new short[MAX_LENGTH];
        for(int i=0;i<requestMsg.segmentNum;i++){
           // System.out.format("%s,%s,%s\n",requestMsg.slaveID,requestMsg.requestSegment[i][2],requestMsg.requestSegment[i][3]);
            try {
                short[] a = reader.readHoldingRegister(requestMsg.slaveID,Integer.parseInt(requestMsg.requestSegment[i][2]),Integer.parseInt(requestMsg.requestSegment[i][3]));
                int j=0;
                for(int begin=Integer.parseInt(requestMsg.requestSegment[i][2]);begin<Integer.parseInt(requestMsg.requestSegment[i][2])+Integer.parseInt(requestMsg.requestSegment[i][3]);begin++){
                    buffer[begin] = a[j++];
                }
            } catch (ModbusTransportException e) {
                e.printStackTrace();
            } catch (ErrorResponseException e) {
                e.printStackTrace();
            } catch (ModbusInitException e) {
                e.printStackTrace();
            }
        }
        receiveChannel.putOnceResolveTask(new OnceResolveTask(requestMsg.resolverMsg,buffer,receiveChannel,sqlQueue ));
        /*        for(int i=0;i<buffer.length;i++){
            System.out.print(buffer[i]+" ");
        }*/
    }
}
