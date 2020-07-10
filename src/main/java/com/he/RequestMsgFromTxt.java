package com.he;

public class RequestMsgFromTxt {
    //ip+" "+com+" "+slaveId+" "+beginId+" "+readLength
    private String ip;
    private int com;
    private int slaveId;
    private int beginId;
    private int readLength;

    public void setIp(String  ip){
        this.ip = ip;
    }
    public String  getIp(){
        return ip;
    }
    public void setCom(int com){
        this.com = com;
    }
    public int getCom(){
        return com;
    }
    public void setSlaveId(int slaveId){
        this.slaveId = slaveId;
    }
    public int getSlaveId(){
        return this.slaveId;
    }

    public void setBeginId(int beginId) {
        this.beginId = beginId;
    }

    public int getBeginId() {
        return beginId;
    }

    public void setReadLength(int readLength) {
        this.readLength = readLength;
    }

    public int getReadLength() {
        return readLength;
    }
}
