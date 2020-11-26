package com.he;

import com.he.writefile.FileReadThread;
import com.he.writefile.WriteFileClient;
import com.he.writefile.WriteToDB;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class test {

   public static  ConcurrentLinkedQueue<String> sqlQueue = new ConcurrentLinkedQueue<String>();
   public static LinkedBlockingQueue bufQueue = new LinkedBlockingQueue(10000);
    public static void main(String[] args) throws IOException {
        new Thread(new mythread(bufQueue)).start();
        new Thread(new WriteFileClient(bufQueue)).start();
       // new Thread (new FileReadThread("D:\\workspace\\DataServer\\sql.txt",sqlQueue)).start();
    }

}