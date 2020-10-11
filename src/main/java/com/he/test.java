package com.he;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class test {

    private ServerSocket serverSocket;

    public test() {
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
        while(true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();          //接收客户连接
                Thread thread = new Thread(new Handler(socket));   //创建一个工作线程
                thread.start();    //启动工作线程
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Handler implements Runnable {     //负责与单个客户端的通信

        private Socket socket;
        public  Handler(Socket socket) {
            this.socket = socket;
        }

        private PrintWriter getWriter(Socket socket) {
            if(socket != null) {
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
                while((msg = br.readLine()) != null) {            //接收和发送数据，知道通信结束
                    System.out.println(msg);
                    pw.println(echo(msg));
                    if(msg.equals("bye")) {
                        break;
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                if(socket != null) {
                    try {
                        socket.close();                //断开连接
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {new test().service();}}