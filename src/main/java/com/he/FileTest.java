package com.he;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileTest {


	public static void main(String[] args) throws IOException {
			//readLineByChannel(getLatestFilePath());
		//long timeS=System.currentTimeMillis();//单位为ms
		while (true)
			readDataByChannel(getLatestFilePath());
		//long timeE1=System.currentTimeMillis();
		//System.out.println("此种方法所耗时间为："+(timeE1-timeS));

	}
	/*public static long getDataFileLineNum(){
		long time = 0;
		try {
			File file5=new File(getLatestFilePath());
			int lines=0;
			long timeS=System.currentTimeMillis();//单位为ms
			if(file5.exists()) {
				long fileLength = file5.length();
				LineNumberReader lineNumberReader =new LineNumberReader(new FileReader(file5));
				lineNumberReader.skip(fileLength);
				lines=lineNumberReader.getLineNumber();
				lineNumberReader.close();
			}
			long timeE=System.currentTimeMillis();
			//System.out.println("此种方法所耗时间为："+(timeE-timeS)+"ms,文件总条数为@："+lines+"条");
			time = timeE-timeS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	public static long getFileLineNum(String filePath) {
		try {
			return Files.lines(Paths.get(filePath)).count();
		} catch (IOException e) {
			return -1;
		}
	}*/
	public static void readDataByChannel(String path) throws IOException {
		System.out.println(path);
		FileInputStream fileIn = new FileInputStream(path);
		FileChannel fileChannel = fileIn.getChannel();
		// 开始按行读取
		//final int PRIMECOUNT = 7;
		//ByteBuffer buffer = ByteBuffer.allocate(6 * PRIMECOUNT+5);
		ByteBuffer buffer = ByteBuffer.allocate(166);
		//时间戳长度166-47=119
		byte b;
		int readTimes = 256*1024*1024/166;
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timeStamp = sf.format(new Date());
		while(fileChannel.read(buffer) > 0)
		{
			buffer.flip();
			byte[] temp = new byte[119];
			for (int i = 0; i < 119 ; i++)//buffer.limit()
			{
				b = buffer.get();
				temp[i]=b;
/*				if(b==10){  // 如果遇到换行
					lineNumber++;
					System.out.println();
				}*/
			}
			//System.out.println(toString(new String(buffer.array())));
			//System.out.println(toString(new String(temp)));
			///保证一个文件只读一次，容量计数，然后输出比当前时间戳大的
			buffer.clear(); // 清空buffer
			if(timeStamp.compareTo(toString(new String(temp)))<=0){
				System.out.println(toString(new String(temp)));
				timeStamp = sf.format(new Date());
			}
		}
		fileChannel.close();
		//System.out.println(lineNumber);
	}
	public static String toString(String binary) {
		String[] tempStr=binary.split(" ");
		char[] tempChar=new char[tempStr.length];
		for(int i=0;i<tempStr.length;i++) {
			tempChar[i]=BinstrToChar(tempStr[i]);
		}
		return String.valueOf(tempChar);
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
	public static String getLatestFilePath(){

		// 定义list，用于存储数据文件的全路径
		List<String> filelist = new ArrayList<String>();
		String dataFileTempDir = "D:\\workspace\\DataServer\\document";
		// 得到返回文件全路径的list集合
		List<String> list = getFiles(dataFileTempDir, filelist);
		String latestDataFileTempPath = null;
		// 数据文件在临时区的路径
		latestDataFileTempPath = list.get(list.size()-1);
		return latestDataFileTempPath;
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
