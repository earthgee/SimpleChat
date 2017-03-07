package com.example.protocal;

import com.google.gson.Gson;

import java.util.UUID;

/**
 * Created by earthgee on 17/2/6.
 */
public class Protocol {

    private ProtocolType type=null;
    private String content=null;
    private int from=-1;
    private int to=-1;
    private boolean qos=false;
    private String fp=null;
    private int retryCount=0;

    public Protocol(ProtocolType type, String content, int from, int to){
        this(type,content,from,to,false,null);
    }

    public String toGsonString(){
        return new Gson().toJson(this);
    }

    public byte[] toBytes(){
        return CharsetUtil.getBytes(toGsonString());
    }

    public ProtocolType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    //加入设置qos的构造方法
    public Protocol(ProtocolType type,String content,int from,int to,
                    boolean qos,String fingerPrint){
        this.type=type;
        this.content=content;
        this.from=from;
        this.to=to;
        this.qos=qos;

        if(qos&&fingerPrint==null){
            this.fp=genFingerPrint();
        }else{
            this.fp=fingerPrint;
        }
    }

    public static String genFingerPrint(){
        return UUID.randomUUID().toString();
    }

    public boolean isQos() {
        return qos;
    }

    public String getFp() {
        return fp;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void increaseRetryCount(){
        this.retryCount++;
    }

    public Protocol clone() {
        Protocol cloneP=new Protocol(getType(),getContent(),getFrom(),getTo(),isQos(),getFp());
        return cloneP;
    }
}










