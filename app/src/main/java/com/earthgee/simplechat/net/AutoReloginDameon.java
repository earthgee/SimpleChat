package com.earthgee.simplechat.net;

import android.net.ConnectivityManager;

/**
 * Created by earthgee on 17/3/6.
 */
public class AutoReloginDameon {

    private static AutoReloginDameon instance=null;

    public static AutoReloginDameon getInstance(){
        if(instance==null){
            instance=new AutoReloginDameon();
        }
        return instance;
    }

    private AutoReloginDameon(){
        init();
    }

    private void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestSender.getInstance().sendReloginRequest(ConnectionManager.getInstance().getClientCore().getCurrentUserId());
            }
        }).start();
    }

}
