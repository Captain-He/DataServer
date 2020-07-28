package com.he.equipments;

import com.he.thread.Modbus4jReader;
import com.he.thread.RequestMsg;
import com.he.thread.ResolverMsg;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcentratorDevice {
	/*
	 * 传感器设备 类，是电力仪表 和 测温集中器 的 父类
	 * created in 2019 09 15
	 *
	 */

	private int id ;//采集设备唯一ID
	private String accessType; //接入方式(以太网/串口)
	private String modbusType; //通信协议(modbusTCP/modbusRTU)
	private int underNum; //隶属的通信管理机编号
	private String comNum; //串口编号
	private int bpsNum; //波特率
	private int dataBit; //数据位
	private char checkBit; //校验位
	private float stopBit; //停止位
	private char fluidControl;//流控
	private int slaveAddress; //从站地址
	private int readTimes;
	private String mapRelation[][]; //总线/IP/端口映射关系
	private RequestMsg requestMsg;
	private ResolverMsg resolverMsg;
	private short [] buffer; //模拟寄存器片
	private ConcurrentLinkedQueue<String> sqlQueue;
	private final TcpMaster tcpModbusMaster;

	public ConcentratorDevice(TcpMaster tcpModbusMaster){
		this.tcpModbusMaster = tcpModbusMaster;
	}
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return id;
	}
	public void setAccessType(String accessType){
		this.accessType = accessType;
	}
	public String getAccessType(){
		return accessType;
	}
	public void setModbusType(String modbusType){
		this.modbusType = modbusType;
	}
	public String getModbusType(){
		return modbusType;
	}
	public void setUnderNum(int underNum){
		this.underNum = underNum;
	}
	public int getUnderNum(){
		return underNum;
	}
	public void setComNum(String comNum){
		this.comNum = comNum;
	}
	public String getComNum(){
		return comNum;
	}
	public void setBpsNum(int bpsNum){
		this.bpsNum = bpsNum;
	}
	public int getBpsNum(){
		return bpsNum;
	}
	public void setDataBit(int dataBit){
		this.dataBit = dataBit;
	}
	public int getDataBit(){
		return dataBit;
	}
	public void setCheckBit(char checkBit){
		this.checkBit = checkBit;
	}
	public char getCheckBit(){
		return checkBit;
	}
	public void setStopBit(float stopBit){
		this.stopBit = stopBit;
	}
	public float getStopBit(){
		return stopBit;
	}
	public void setFluidControl(char fluidControl){
		this.fluidControl = fluidControl;
	}
	public char getFluidControl(){
		return fluidControl;
	}
	public void setSlaveAddress(int slaveAddress){
		this.slaveAddress= slaveAddress;
	}
	public int getSlaveAddress(){
		return slaveAddress;
	}
	public void setReadTimes(int readTimes){
		this.readTimes = readTimes;
	}
	public int getReadTimes(){
		return readTimes;
	}
	public void setMapRelation(String mapRelation [][]){
		this.mapRelation = mapRelation;
	}
	public String [][]getMapRelation(){
		return mapRelation;
	}
	public void setRequestMsg(RequestMsg requestMsg) {
		this.requestMsg = requestMsg;
	}
	public RequestMsg getRequestMsg() {
		return requestMsg;
	}
	public void setResolverMsg(ResolverMsg resolverMsg) {
		this.resolverMsg = resolverMsg;
	}
	public ResolverMsg getResolverMsg() {
		return resolverMsg;
	}
	public void setSqlQueue(ConcurrentLinkedQueue<String> sqlQueue) {
		this.sqlQueue = sqlQueue;
	}
	public ConcurrentLinkedQueue<String> getSqlQueue() {
		return sqlQueue;
	}

	public void execute(){
		try {
			Request();//请求、接收
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		resolve();//解析、放入队列
	}

	private void Request() throws InterruptedException {
		Modbus4jReader reader = new Modbus4jReader(this.tcpModbusMaster);
		int MAX_LENGTH = 0;
		for(int i=0;i<requestMsg.segmentNum;i++){
			int allLengthTemp=Integer.parseInt(requestMsg.requestSegment[i][2])+Integer.parseInt(requestMsg.requestSegment[i][3]);
			if(MAX_LENGTH<allLengthTemp){
				MAX_LENGTH = allLengthTemp;
			}
		}
		this.buffer =new short[MAX_LENGTH];
		for(int i=0;i<requestMsg.segmentNum;i++){
			try {
				short[] a = reader.readHoldingRegister(requestMsg.slaveID,Integer.parseInt(requestMsg.requestSegment[i][2]),Integer.parseInt(requestMsg.requestSegment[i][3]));
				int j=0;
				for(int begin=Integer.parseInt(requestMsg.requestSegment[i][2]);begin<Integer.parseInt(requestMsg.requestSegment[i][2])+Integer.parseInt(requestMsg.requestSegment[i][3]);begin++){
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
	}
	private void resolve() {
		Map<String, String> resolverMap = this.resolverMsg.resolverMap;
		String aimStr[] = new String[resolverMap.size()];
		int index = 0;
		short sigleAimStr[] = new short[51];
		for (Map.Entry<String, String> entry : resolverMap.entrySet()) {
			String sensorID = entry.getKey();
			String solver[] = ComIpSplit(entry.getValue(), " ");
			// solver[0] 集中器设备ID，slover[1]传感器设备ID，solver[2]数据项数量，从3开始到solver.length都是每个数据项的【】
			aimStr[index] = "INSERT INTO sensorData (SourceAddr,GroupAddr,samplingTime,item1,item2,item3,item4,item5,item6,item7,item8,item9,item10,item11,item12,item13,item14,item15,item16,item17,item18,item19,item20,item21,item22,item23,item24,item25,item26,item27,item28,item29,item30,item31,item32,item33,item34,item35,item36,item37,item38,item39,item40,item41,item42,item43,item44,item45,item46,item47,item48,item49,item50) VALUES ('"+solver[0];
			Arrays.fill(sigleAimStr, (short) -1);
			for (int i = 3; i < Integer.parseInt(solver[2]) + 3; i++) { //遍历项解析
				String solverStr[] = ComIpSplit(solver[i], "/");
				//aimStr[index] += "item" + solverStr[0] + "-";
				short temp = 0 ;
				for (int j = Integer.parseInt(solverStr[1]); j < Integer.parseInt(solverStr[1]) + Integer.parseInt(solverStr[2]); j++) {
					temp += buffer[j];
				}
				sigleAimStr[Integer.parseInt(solverStr[0])] = temp;
			}
			for(int i=1;i<51;i++){
				aimStr[index]+=",";
				if(sigleAimStr[i] == -1){
					aimStr[index]+="0";
				}else{
					aimStr[index]+=sigleAimStr[i];
				}
			}
			aimStr[index]+=")";
			System.out.println(aimStr[index]);
			index++;
		}
		for (int i = 0; i < aimStr.length; i++) {
			sqlQueue.add(aimStr[i]);
		}
	}
	private static String[] ComIpSplit(String str, String splitChar) {	//串口编号/IP地址/端口 分割
		String splitArray[] = {};
		if ((str != null) && (!isEquals(str, "-"))) {
			splitArray = str.replaceAll("  ", " ").split(splitChar);
		}
		return splitArray;
	}
	private static boolean isEquals(String a, String b) {	//判断字符串a 是否与 字符串b 相等
		return a.replaceAll("\n", "").replaceAll(" ", "").equals(b);
	}

}