package com.he;

public class TemperConcentratorResolver {
    private int ConcentratorDevideID;
    private int SlaveID;
    private int SegmentNum;//分段数量
    private String [][]Segment;

    public TemperConcentratorResolver(int ConcentratorDevideID, int SlaveID, int SegmentNum, String [][]Segment){
        this.ConcentratorDevideID = ConcentratorDevideID;
        this.SlaveID = SlaveID;
        this.SegmentNum = SegmentNum;//分段数量
        this.Segment = Segment;
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
