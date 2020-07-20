package com.he;

import com.serotonin.modbus4j.sero.util.queue.ByteQueue;
import java.util.ArrayList;

public class Request {
	private int ConcentratorDevideID;
	private int SlaveID;
	private int SegmentNum;//分段数量
	private String [][]RequestSegment;
	public Resolver resolver;//解析器
	public Request( int ConcentratorDevideID,int SlaveID,int SegmentNum,String [][]RequestSegment,String []relolverFromTxt){
		this.ConcentratorDevideID = ConcentratorDevideID;
		this.SlaveID = SlaveID;
		this.SegmentNum = SegmentNum;//分段数量
		this.RequestSegment = RequestSegment;
		this.resolver = new Resolver(ConcentratorDevideID,relolverFromTxt);
	}
	public void execute(){
		/*String res = "";
		for (int i=0;i<childRequest.size();i++){
			ByteQueue result = App.modbusRTCP(childRequest.get(i).getIp(), childRequest.get(i).getCom(),
					childRequest.get(i).getSlaveId(),childRequest.get(i).getBeginId(), childRequest.get(i).getReadLength());
			res+="["+App.ansisByteQueue(result)+"]"+"\n";
		}
		Response responseMsg = new Response(res);
		receiveChannel.putReceive(responseMsg);
		System.out.println(Thread.currentThread().getName()+"******执行Request完毕********");*/
	}
}
