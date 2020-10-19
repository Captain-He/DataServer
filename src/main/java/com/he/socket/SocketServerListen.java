package com.he.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerListen {

    private ServerSocket serverSocket;

    public SocketServerListen() {
        try {
            serverSocket = new ServerSocket(0);
            System.out.println("ip:"+serverSocket.getInetAddress().getLocalHost().getHostAddress());
            System.out.println("port:"+serverSocket.getLocalPort());
            System.out.println("server starts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void service() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();          //接收客户连接
                Thread thread = new Thread(new SocketClient(socket));   //创建一个工作线程
                thread.start();    //启动工作线程
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   // public static void main(String[] args) {new SocketServerListen().service();}
}