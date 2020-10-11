package com.he.thread;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;

public class RequestMsg {
	public String concentratorDevideIP;
	public int concentratorDevideChuanID;
	public int concentratorDevideID;
	public int slaveID;
	public int segmentNum;//分段数量
	public String [][]requestSegment;
	private TcpMaster tcpModbusMaster;
	public ResolverMsg resolverMsg;
	private short [] buffer; //模拟寄存器片
	private static ModbusFactory modbusFcactory =  new ModbusFactory();
	public RequestMsg(String communicationManagerComIP, int chuanId, int concentratorDevideID, int slaveID, int segmentNum, String[][] requestSegment,ResolverMsg resolverMsg){
		this.concentratorDevideIP = communicationManagerComIP;
		this.concentratorDevideChuanID = chuanId;
		this.concentratorDevideID = concentratorDevideID;
		this.slaveID = slaveID;
		this.segmentNum = segmentNum;//分段数量
		this.requestSegment = requestSegment;
		this.resolverMsg = resolverMsg;
		IpParameters params = new IpParameters();
		params.setHost(communicationManagerComIP);// 设置ip
		params.setPort(chuanId);
		tcpModbusMaster= (TcpMaster)modbusFcactory.createTcpMaster(params, true);
	}

	public short [] Request() throws InterruptedException {
		Modbus4jReader reader = new Modbus4jReader(this.tcpModbusMaster);
		int MAX_LENGTH = 0;
		for(int i=0;i<segmentNum;i++){
			int allLengthTemp=Integer.parseInt(requestSegment[i][2])+Integer.parseInt(requestSegment[i][3]);
			if(MAX_LENGTH<allLengthTemp){
				MAX_LENGTH = allLengthTemp;
			}
		}
		this.buffer =new short[MAX_LENGTH];
		for(int i=0;i<segmentNum;i++){
			try {
				short[] a = reader.readHoldingRegister(slaveID,Integer.parseInt(requestSegment[i][2]),Integer.parseInt(requestSegment[i][3]));
				int j=0;
				for(int begin=Integer.parseInt(requestSegment[i][2]);begin<Integer.parseInt(requestSegment[i][2])+Integer.parseInt(requestSegment[i][3]);begin++){
					this.buffer[begin] = a[j++];
				}
			} catch (ModbusTransportException e) {
				e.printStackTrace();
			} catch (ErrorResponseException e) {
				e.printStackTrace();
			} catch (ModbusInitException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}
}
