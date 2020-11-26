package com.he;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class mythread extends Thread{

    private LinkedBlockingQueue bufQueue;

    public mythread(LinkedBlockingQueue bufQueue){
        this.bufQueue = bufQueue;
    }
    public void run(){
        //byte [] a = {'1','3','4','6','6','\n'};
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        while (true){
            String str = sf.format(new Date());
            ByteBuffer buf = ByteBuffer.allocate(166*10);
     /*       byte merger[] = buteMerger(a,str.getBytes());
            while(buf.position()+merger.length < buf.capacity() ){
                buf.put(merger);
            }*/
            //byte merger[] = buteMerger(a,str.getBytes());
            String b = "123456\n";
            byte[] temp=toBinary(str+b).getBytes();
           while(buf.position()+temp.length < buf.capacity() ) {//需按一条容量修改
               buf.put(temp);
           }
            try {
                bufQueue.put(buf);
                try
                {
                    Thread.sleep(50);//单位：毫秒
                } catch (Exception e) {
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 字符串转换为二进制字符串
     * @param str
     * @return
     */
    private static String toBinary(String str){
        //把字符串转成字符数组
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            //toBinaryString(int i)返回变量的二进制表示的字符串
            //toHexString(int i) 八进制
            //toOctalString(int i) 十六进制
            result +=Integer.toBinaryString(strChar[i])+ " ";
        }
        //System.out.println(result);
        return result;
    }
    /**
     * byte数组合并
     * @param bt1
     * @param bt2
     * @return
     */
    private static byte[] buteMerger(byte[]bt1,byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
    // 将字符串转换成二进制字符串，以空格相隔
    private String StrToBinstr(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]);
        }
        return result;
    }
}
