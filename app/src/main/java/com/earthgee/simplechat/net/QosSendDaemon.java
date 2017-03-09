package com.earthgee.simplechat.net;

import android.os.Handler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by earthgee on 17/3/9.
 */
public class QosSendDaemon {

    public static final int CHECK_INTERVAL=5000;
    public static final int DELAY_SEND_TIME=2000;
    public static final int QOS_TRY_COUNT=3;

    private static QosSendDaemon instance=null;

    private ConcurrentHashMap<String,Protocol> sendMessages=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Long> sendMessageTimeStamp=new ConcurrentHashMap<>();

    private Handler handler;
    private Runnable runnable;

    private boolean running=false;
    private volatile boolean executing=false;

    private QosSendDaemon(){
        init();
    }

    public static QosSendDaemon getInstance(){
        if(instance==null){
            instance=new QosSendDaemon();
        }
        return instance;
    }

    private void init(){
        this.handler=new Handler();
        this.runnable=new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        executing=true;

                        if(!executing){
                            for(String key:sendMessages.keySet()){
                                Protocol p=sendMessages.get(key);
                                if(p.getRetryCount()>=QOS_TRY_COUNT){
                                    remove(p.getFp());
                                }else{
                                    long delta= System.currentTimeMillis()-sendMessageTimeStamp.get(p.getFp());

                                    if(delta<=DELAY_SEND_TIME){

                                    }else{
                                        RequestSender.getInstance().sendDefaultData(p);
                                        p.increaseRetryCount();
                                    }
                                }
                            }

                            executing=false;
                            handler.postDelayed(runnable,CHECK_INTERVAL);
                        }
                    }
                }).start();
            }
        };
    }

    public void stop(){
        handler.removeCallbacks(runnable);
        running=false;
    }

    public void startUp(){
        stop();

        handler.postDelayed(runnable, 0);
        running=true;
    }

    public void remove(final String fingerPrint){
        sendMessageTimeStamp.remove(fingerPrint);
        sendMessages.remove(fingerPrint);
    }

    public boolean exist(String fingerPrint){
        return sendMessages.containsKey(fingerPrint);
    }

    public void put(Protocol p){
        sendMessages.put(p.getFp(),p);
        sendMessageTimeStamp.put(p.getFp(),System.currentTimeMillis());
    }

}
