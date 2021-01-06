package com.he.writefile;

import com.he.readToDB.ReadToDbClient;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.concurrent.LinkedBlockingQueue;

public class WriteFileClient extends Thread{
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
    public static final int FILE_NUM = 4;//创建的文件个数
    public static final String FILE_PATH = System.getProperty("user.dir") +"\\document\\";//文件地址
    private LinkedBlockingQueue bufQueue;

    public WriteFileClient(LinkedBlockingQueue bufQueue){
        this.bufQueue = bufQueue;
    }
    @Override
    public void run() {
int t = 0;
           FileOutputStream fos = null;
           FileChannel outChannel = null;
            int i=1;
            while (true) {
                try {
                    if (i <= FILE_NUM) {
                        String dir = getFilePath("DataFile" + i);
                        fos = new FileOutputStream(dir);
                        outChannel = fos.getChannel();
//166 * 1000000 166M级 *3 =300 0000 1个文件存300万条
                        while ((getFileOrFilesSize(dir,1)<124*7*1000 )) {
                            ByteBuffer buf = (ByteBuffer) bufQueue.take();
                            buf.flip();
                            outChannel.write(buf);
                            System.out.println(t++);
                            System.out.println(ReadToDbClient.toString(new String(buf.array())));
                            buf.clear();
                        }
                        i++;
                    } else {
                        i = 1;
                    }
                } catch(IOException | InterruptedException e){
                    e.printStackTrace();
         }finally {
                    if(outChannel != null){
                        try {
                            outChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(fos != null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
    }

    /**
     *首先判断文件夹 是否存在 ，再判断文件是否存在
     * @param fileName 文件名
     * @return 被创建的txt文件地址
     */
    private String getFilePath(String fileName){
        //按照顺序创建4个文件，DataFile1.txt DataFile2.txt DataFile3.txt DataFile4.txt
        //如果不存在,创建文件夹
        File f = new File(FILE_PATH);
        if(!f.exists()){
            f.mkdirs();
        }
        fileName += ".txt";
        File txt = new File(FILE_PATH+fileName);
        if(!txt.exists()){
            OutputStream out = null;
            try {
                out = new FileOutputStream(FILE_PATH + fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            clearInfoForFile(FILE_PATH +fileName);
        }
        System.out.println(fileName);
        return FILE_PATH + fileName;
    }

/**
    private String getFilePath(){
        //按照时间创建文件夹 和 txt 文件，返回字符串类型的文件地址。
        Date date = new Date();
        String path=System.getProperty("user.dir") +"\\document"+ File.separator +new SimpleDateFormat("yyyy\\MM\\").format(date);
        //如果不存在,创建文件夹
        File f = new File(path);
        if(!f.exists()){
            f.mkdirs();
        }

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + ".txt";
        OutputStream out = null;
        try {
            out = new FileOutputStream(path + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(path+fileName);
         return path + fileName;
    }
**/
    /**
            * 获取文件指定文件的指定单位的大小
     *
             * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    private double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }
    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }
    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }
    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
    // 清空已有的文件内容，以便下次重新写入新的内容
    private void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
