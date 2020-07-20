package com.he.equipments;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;

public class TcpMaster {
    private static ModbusFactory modbusFactory;

    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    public static ModbusMaster getTcpMaster(String ip, int port) {
        IpParameters params = new IpParameters();
        params.setHost(ip);// 设置ip
        if (port == 0) {
            params.setPort(502);// 设置端口，默认为502
        } else {
            params.setPort(port);
        }
        ModbusMaster tcpMaster = modbusFactory.createTcpMaster(params, true);// 获取ModbusMaster对象
        return tcpMaster;
    }

}
