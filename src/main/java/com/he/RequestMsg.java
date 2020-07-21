package com.he;

import com.serotonin.modbus4j.ip.tcp.TcpMaster;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;
import java.util.ArrayList;

public class RequestMsg {
	public String concentratorDevideIP;
	public int concentratorDevideChuanID;
	public int concentratorDevideID;
	public int slaveID;
	public int segmentNum;//分段数量
	public String [][]requestSegment;
	public Resolver resolver;//解析器
	public RequestMsg(String communicationManagerComIP, int chuanId, int concentratorDevideID, int slaveID, int segmentNum, String [][]requestSegment, String []sensor_dev_list){
		this.concentratorDevideIP = communicationManagerComIP;
		this.concentratorDevideChuanID = chuanId;
		this.concentratorDevideID = concentratorDevideID;
		this.slaveID = slaveID;
		this.segmentNum = segmentNum;//分段数量
		this.requestSegment = requestSegment;
		this.resolver = new Resolver(concentratorDevideID,sensor_dev_list);
	}
}
