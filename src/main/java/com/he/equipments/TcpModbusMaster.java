package com.he.equipments;

import com.he.thread.RequestMsg;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;

import java.util.ArrayList;

public class TcpModbusMaster extends com.serotonin.modbus4j.ip.tcp.TcpMaster{
    private final TcpMaster tcpMaster;
    private final ModbusSlave modbusSlave;
    private final ArrayList<RequestMsg> requestMsgs;
    private static ModbusFactory modbusFactory;
    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    public TcpModbusMaster(IpParameters params, boolean keepAlive, ModbusSlave modbusSlave) {
        super(params, keepAlive);
        this.tcpMaster = (TcpMaster) modbusFactory.createTcpMaster(params, true);// 获取ModbusMaster对象
        this.modbusSlave = modbusSlave;
         ArrayList<RequestMsg> requestMsgs = new ArrayList<>();
        if(modbusSlave.getPowerMeters()!=null){
            for (int i = 0; i < modbusSlave.getPowerMeters().size(); i++) {
                requestMsgs.add(modbusSlave.getPowerMeters().get(i).getRequestMsg());
            }
        }
        if(modbusSlave.getTemperConcentrator()!=null){
            for (int i = 0; i < modbusSlave.getTemperConcentrator().size(); i++) {
                requestMsgs.add(modbusSlave.getTemperConcentrator().get(i).getRequestMsg());
            }
        }
        this.requestMsgs = requestMsgs;
    }

    public TcpMaster getTcpMaster() {
        return tcpMaster;
    }

    public ModbusSlave getModbusSlave() {
        return modbusSlave;
    }

    public ArrayList<RequestMsg> getRequestMsgs() {
        return requestMsgs;
    }
}
