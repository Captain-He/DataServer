package com.he;

import com.serotonin.modbus4j.sero.util.queue.ByteQueue;
import java.util.ArrayList;

public class Request {
	private int ConcentratorDevideID;
	private int SlaveID;
	private int SegmentNum;//分段数量
	//private ArrayList<SegmentRequest>Segment;
	private final ArrayList<RequestMsgFromTxt> childRequest;
	private final ReceiveChannel receiveChannel;
	public Request(ArrayList<RequestMsgFromTxt> child, ReceiveChannel receiveChannel){
		this.childRequest = child;
		this.receiveChannel = receiveChannel;
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
