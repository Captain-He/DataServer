package com.he.equipments;

import com.serotonin.modbus4j.ip.tcp.TcpMaster;

public class PowerMeter extends ConcentratorDevice {

    public PowerMeter(TcpMaster tcpModbusMaster){
        super(tcpModbusMaster);
    }
}

