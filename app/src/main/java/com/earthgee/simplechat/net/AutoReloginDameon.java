package com.earthgee.simplechat.net;

import android.content.Context;
import android.os.Handler;

/**
 * Created by earthgee on 17/3/6.
 */
public class AutoReloginDameon {

    private static AutoReloginDameon instance=null;

    public static int RELOGIN_INTERVAL=5000;

    private Handler handler=null;
    private Runnable runnable=null;

    private static Context contextReal;

    public static AutoReloginDameon getInstance(){
        if(instance==null){
            instance=new AutoReloginDameon();
        }
        return instance;
    }

    public static AutoReloginDameon getInstance(Context context){
        if(instance==null){
            instance=new AutoReloginDameon();
        }
        contextReal=context;
        return instance;
    }

    private AutoReloginDameon(){
        init();
    }

    private void init(){
        this.handler=new Handler();
        this.runnable=new Runnable() {
            @Override
            public void run() {
                RequestSender.getInstance().sendLoginRequest(ConnectionManager.getInstance().getUserName()
                        , ConnectionManager.getInstance().getPassword(), contextReal);

                handler.postDelayed(runnable,RELOGIN_INTERVAL);
            }
        };
    }

    public void stop(){
        handler.removeCallbacks(runnable);
    }

    public void start(){
        stop();

        handler.postDelayed(runnable,0);
    }


}
