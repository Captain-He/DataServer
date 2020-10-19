package com.he.writefile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileReadThread extends Thread {
    private String txtPath;
    ConcurrentLinkedQueue<String> sqlQueue ;
   public FileReadThread(String txtPath, ConcurrentLinkedQueue<String> sqlQueue){
        this.txtPath = txtPath;
         this.sqlQueue = sqlQueue;
    }
    @Override
    public void run() {
        FileReader fr = null;
        try {
            fr = new FileReader(txtPath);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            while ((str = bf.readLine()) != null){
                System.out.println("^^^"+str);
                sqlQueue.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
