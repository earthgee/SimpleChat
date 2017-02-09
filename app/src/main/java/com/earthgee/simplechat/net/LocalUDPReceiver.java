package com.earthgee.simplechat.net;

/**
 * Created by earthgee on 17/2/9.
 */
public class LocalUDPReceiver {

    private static LocalUDPReceiver instance;
    private Thread thread=null;

    private LocalUDPReceiver(){
    }

    public static LocalUDPReceiver getInstance(){
        if(instance==null){
            instance=new LocalUDPReceiver();
        }
        return instance;
    }

    public void startUp(){
        stop();
    }

    public void stop(){
        if(thread!=null){
            thread.interrupt();
            thread=null;
        }

        try{
            thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try{

                    }catch (Exception e){

                    }
                }
            });
            thread.start();
        }catch (Exception e){

        }
    }

    private void p2pListeningImpl(){
        
    }

}
