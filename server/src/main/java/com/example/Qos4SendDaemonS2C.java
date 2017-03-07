package com.example;


import com.example.protocal.Protocol;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.Timer;

/**
 * Created by earthgee on 17/3/7.
 * qos发送线程
 */
public class Qos4SendDaemonS2C {

    public static int CHECK_INTERVAL=5000;
    //延迟发送的时间
    public static int SEND_DELAY_TIME=2000;
    public static int QOS_TRY_COUNT=1;

    private static Qos4SendDaemonS2C instance=null;
    private Timer timer=null;

    private boolean running=false;
    private boolean executing=false;

    private ConcurrentHashMap<String,Protocol> sendMessages=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Long> sendMessagesTimestamp=new ConcurrentHashMap<>();

    private Qos4SendDaemonS2C(){
        init();
    }

    public static Qos4SendDaemonS2C getInstance(){
        if(instance==null){
            instance=new Qos4SendDaemonS2C();
        }
        return instance;
    }

    private void init(){
        this.timer=new Timer(CHECK_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Qos4SendDaemonS2C.this.run();
            }
        });
    }

    public Qos4SendDaemonS2C startUp(){
        stop();

        this.timer.setInitialDelay(0);
        this.timer.start();
        this.running=true;

        return this;
    }

    public void stop(){
        if(this.timer!=null){
            this.timer.stop();
        }

        this.running=false;
    }

    public void run(){
        if(!this.executing){
            ArrayList<Protocol> lostMessages=new ArrayList<>();
            this.executing=true;
            for(String key:sendMessages.keySet()){
                Protocol p=sendMessages.get(key);
                if(p!=null&&p.isQos()){
                    if(p.getRetryCount()>=QOS_TRY_COUNT){
                        lostMessages.add(p.clone());
                        remove(p.getFp());
                    }else{
                        long delay=System.currentTimeMillis()-sendMessagesTimestamp.get(key);
                        if(delay<=SEND_DELAY_TIME){

                        }else{
                            try {
                                boolean sendOk=ServerCoreHandler.sendData(p);
                                p.increaseRetryCount();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean exist(String fingerPrint){
        return this.sendMessages.get(fingerPrint)!=null;
    }

    public void put(Protocol p){
        sendMessages.put(p.getFp(),p);
        sendMessagesTimestamp.put(p.getFp(),System.currentTimeMillis());
    }

    public void remove(String fingerPrint){
        sendMessagesTimestamp.remove(fingerPrint);
        sendMessages.remove(fingerPrint);
    }

}














