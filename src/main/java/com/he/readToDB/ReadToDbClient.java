package com.he.readToDB;

import com.he.DBConnectionPool.ConnectionPoolManager;
import com.he.DBConnectionPool.IConnectionPool;
import com.he.DBConnectionPool.ThreadConnection;

import java.io.*;
import java.sql.*;
import java.util.*;

public class ReadToDbClient {

	public static void main(String[] args) throws IOException {
		ThreadConnection c = new ThreadConnection();
		c.run();
		Connection conn = c.getConnection();
		Statement stmt = null;
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while (true)
			readDataByLine(getLatestFilePath(),st);


	}

	/**
	 * 定义文件存入具体 数据条数*每条字节数用于计数读取
	 * @param fR
	 * @throws IOException
	 */
	public static void readDataByLine(File fR,Statement st) throws IOException {
		RandomAccessFile randomFile = new RandomAccessFile(fR,"r");
		long startPoint = randomFile.length();
		while(startPoint <= 122*7*1000){
			randomFile.seek(startPoint);
			byte[] bytes = new byte[(122*7)];
			int byteread = 0;
			// 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
			// 将一次读取的字节数赋给byteread
			while ((byteread = randomFile.read(bytes)) != -1) {
				//System.out.println(toString(new String(bytes))+fR.getName());
				String insertStr = toItem(toString(new String(bytes)));
				try {
					st.execute(insertStr);
					System.out.println(insertStr);
				} catch (SQLException e) {
					e.printStackTrace();
				}
                System.out.println(insertStr);
			}
			startPoint = randomFile.length();
		}
	}

	public static String toString(String binary) {
		String[] tempStr=binary.split(" ");
		char[] tempChar=new char[tempStr.length];
		for(int i=0;i<tempStr.length;i++) {
			tempChar[i]=BinstrToChar(tempStr[i]);
		}
		return String.valueOf(tempChar);
	}
//拼item
    private static String toItem(String str){
	    String string = "insert into sensordata(sourceAddr,groupAddr,samplingTime";
	    for(int i=1;i<51;i++){
	    	string +=",item"+i+"";
	    }
	    string +=") values(";
	    String strItem [] = str.split(" ");
        for(int i=0;i< 52;i++){
             string +="'"+strItem[i]+"',";
        }
       return string +="'"+strItem[52]+"'"+");";
    }

	//将二进制字符串转换成int数组
	public static int[] BinstrToIntArray(String binStr) {
		char[] temp=binStr.toCharArray();
		int[] result=new int[temp.length];
		for(int i=0;i<temp.length;i++) {
			result[i]=temp[i]-48;
		}
		return result;
	}


	//将二进制转换成字符
	public static char BinstrToChar(String binStr){
		int[] temp=BinstrToIntArray(binStr);
		int sum=0;
		for(int i=0; i<temp.length;i++){
			sum +=temp[temp.length-1-i]<<i;
		}
		return (char)sum;
	}
	/**
	 *
	 * @return 获得文件夹中最新修改的文件的路径 只返回一个
	 */
	public static File getLatestFilePath(){

		// 定义list，用于存储数据文件的全路径
		List<String> filelist = new ArrayList<String>();
		String dataFileTempDir = "D:\\workspace\\DataServer\\document";
		// 得到返回文件全路径的list集合
		List<String> list = getFiles(dataFileTempDir, filelist);
		String latestDataFileTempPath = null;
		// 数据文件在临时区的路径
		latestDataFileTempPath = list.get(list.size()-1);
		File latestDataFile = new File(latestDataFileTempPath);
		return latestDataFile;
	}
	/**
	 * 通过递归得到某一路径下所有的文件的全路径,分装到list里面
	 *
	 * @param filePath
	 * @param filelist
	 * @return
	 */
	public static List<String> getFiles(String filePath, List<String> filelist) {

		File root = new File(filePath);
		if (!root.exists()) {

		} else {
			File[] files = root.listFiles();
			Arrays.sort(files, new CompratorByLastModified());
			for (File file : files) {
				if (file.isDirectory()) {
					getFiles(file.getAbsolutePath(), filelist);
				} else {
					//logger.info("目录:" + filePath + "文件全路径:" + file.getAbsolutePath());
					filelist.add(file.getAbsolutePath());
				}
			}
		}
		return filelist;
	}

	//根据文件修改时间进行比较的内部类
	static class CompratorByLastModified implements Comparator<File> {

		public int compare(File f1, File f2) {
			long diff = f1.lastModified() - f2.lastModified();
			if (diff > 0) {
				return 1;
			} else if (diff == 0) {
				return 0;
			} else {
				return -1;
			}
		}
	}

}
