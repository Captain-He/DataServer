package com.he;

public class Response {
    private final String receiveMsg;
    public Response(String receiveMsg){
        this.receiveMsg = receiveMsg;
    }
    public void execute(){
        System.out.format("输出的结果为:%s\n",receiveMsg);
    }
}
