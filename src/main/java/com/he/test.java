package com.he;

import com.he.writefile.FileReadThread;
import com.he.writefile.WriteToDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class test {



    public static void main(String[] args) {
         ConcurrentLinkedQueue<String> sqlQueue = new ConcurrentLinkedQueue<>();
        new Thread (new FileReadThread("D:\\workspace\\DataServer\\sql.txt",sqlQueue)).start();
        new Thread(new WriteToDB(sqlQueue)).start();
    }
}