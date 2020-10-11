package com.he.socket;

import com.he.App;
import com.he.equipments.*;
import com.he.thread.Channel;
import com.he.thread.Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClient implements Runnable {     //负责与单个客户端的通信

    private Socket socket;
    private RegisteredDevContainer registeredDevContainer;
    public static volatile boolean clientExit;
    ArrayList<CommunicationManager> communicationManagers;

    public SocketClient(Socket socket) {
        this.socket = socket;
        communicationManagers = App.GetCommunicationManagers();
        registeredDevContainer = new RegisteredDevContainer();
        clientExit = false;

    }

    private PrintWriter getWriter(Socket socket) {
        if (socket != null) {
            try {
                OutputStream outputSteam = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(outputSteam, true);
                return pw;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private BufferedReader getReader(Socket socket) {
        InputStream inputStream;
        try {
            inputStream = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            return br;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String echo(String msg) {
        return "Hello:" + msg;
    }

    @Override

    public void run() {
        try {
            System.out.println("New Connection accepted,and the address is" + socket.getInetAddress() + ", and the port is " + socket.getLocalPort());
            BufferedReader br = getReader(socket);
            PrintWriter pw = getWriter(socket);
            String msg = null;
            while ((msg = br.readLine()) != null) {            //接收和发送数据，知道通信结束
                System.out.println(msg);
                for (CommunicationManager communicationManager : communicationManagers) {
                    for (CommunicationManagerCom communicationManagerCom : communicationManager.getCommunicationManagerComs()) {
                        if (communicationManagerCom.getDevNum() == 0) continue;
                        if (isEquals(msg, communicationManagerCom.getCommunicationManagerComIP()) && registeredDevContainer.isRegistered(communicationManagerCom.getCommunicationManagerComIP())) {
                            if (registeredDevContainer.getDevs().size() == 0 && !clientExit) {
                                Channel channel = new Channel(communicationManager.getScNum());//此处参数为工人线程数量由通道数量决定，一个通信管理机对应一个channel
                                channel.startWorkers();
                                Client client = new Client(channel,registeredDevContainer); //一个端口 对应一个代理线程
                                client.start();
                            }
                            System.out.println("注册总线ip: " + msg);
                            ArrayList<ConcentratorDevice> devList = new ArrayList<>();
                            for (PowerMeter powerMeter : communicationManagerCom.getPowerMeters()) {
                                devList.add(powerMeter);
                            }
                            for (TemperConcentrator temperConcentrator : communicationManagerCom.getTemperConcentrator()) {
                                devList.add(temperConcentrator);
                            }
                            registeredDevContainer.putDev(msg, devList);
                        }
                    }
                }

            }
            clientExit = true;
            socket.close();                //断开连接
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();                //断开连接
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //判断字符串a 是否与 字符串b 相等
    private  static boolean isEquals(String a, String b) {
        return a.replaceAll("\n", "").replaceAll(" ", "").equals(b);
    }
}