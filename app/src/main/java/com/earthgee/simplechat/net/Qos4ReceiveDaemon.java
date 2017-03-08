package com.earthgee.simplechat.net;

import android.os.Handler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by earthgee on 17/3/7.
 *
 * qos接收线程
 *
 */
public class Qos4ReceiveDaemon {

    public static final int CHECK_INTERVAL=300000;
    public static final int MESSAGES_VALID_TIME=600000;

    private static Qos4ReceiveDaemon instance=null;

    private Handler handler;
    private Runnable runnable;

    private boolean running=false;

    private ConcurrentHashMap<String,Long> receivedMessages=new ConcurrentHashMap<>();

    private Qos4ReceiveDaemon(){
        init();
    }

    public static Qos4ReceiveDaemon getInstance(){
        if(instance==null){
            instance=new Qos4ReceiveDaemon();
        }
        return instance;
    }

    private void init(){
        this.handler=new Handler();
        this.runnable=new Runnable() {
            @Override
            public void run() {
                for(String key:receivedMessages.keySet()){
                    long delta=System.currentTimeMillis()-receivedMessages.get(key);
                    if(delta<MESSAGES_VALID_TIME){
                        continue;
                    }
                    receivedMessages.remove(key);
                }

                handler.postDelayed(runnable,CHECK_INTERVAL);
            }
        };
    }

    public void stop(){
        this.handler.removeCallbacks(this.runnable);
        this.running=false;
    }

    public void startUp(){
        handler.postDelayed(runnable, 0);
        running=true;
    }

    public boolean hasReceived(String fingerPrintOfProtocal){
        return receivedMessages.containsKey(fingerPrintOfProtocal);
    }

    public void addReceived(Protocol p){
        addReceived(p.getFp());
    }

    public void addReceived(String fingerPrintOfProtocal){
        receivedMessages.put(fingerPrintOfProtocal,System.currentTimeMillis());
    }

}










