package com.he;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TaskDistributeThread extends Thread{
	private final Channel channel;
	private final ReceiveChannel receiveChannel;
	 ArrayList<ArrayList<RequestMsgFromTxt>> trMsg;
	private  static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	public TaskDistributeThread(Channel channel, ReceiveChannel receiveChannel, ArrayList<ArrayList<RequestMsgFromTxt>> trMsg){
		this.channel = channel;
		this.receiveChannel = receiveChannel;
		this.trMsg = trMsg;
	}

	@Override
	public void run(){
		while(true){
			try {
				for(int i=0;i<trMsg.size();i++){
					Request request = new Request(trMsg.get(i),receiveChannel);
					channel.putRequest(request);
					System.out.println("\n"+Thread.currentThread().getName()+"******装载Request完毕********");
				}
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	/*	executor.scheduleWithFixedDelay(new Runnable(){
			//模拟耗时任务,耗时是10s以内的任意数
			@Override
			public void run() {
				try {
					for(int i=0;i<trMsg.size();i++){
						Request request = new Request(trMsg.get(i));
						channel.putRequest(request);
						System.out.println(Thread.currentThread().getName()+"******装载Request完毕********");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 5, TimeUnit.SECONDS);//每隔5s*/
	}
}
