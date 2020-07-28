package com.he.thread;

public class RequestMsg {
	public String concentratorDevideIP;
	public int concentratorDevideChuanID;
	public int concentratorDevideID;
	public int slaveID;
	public int segmentNum;//分段数量
	public String [][]requestSegment;
	public RequestMsg(String communicationManagerComIP, int chuanId, int concentratorDevideID, int slaveID, int segmentNum, String[][] requestSegment){
		this.concentratorDevideIP = communicationManagerComIP;
		this.concentratorDevideChuanID = chuanId;
		this.concentratorDevideID = concentratorDevideID;
		this.slaveID = slaveID;
		this.segmentNum = segmentNum;//分段数量
		this.requestSegment = requestSegment;
	}
}
