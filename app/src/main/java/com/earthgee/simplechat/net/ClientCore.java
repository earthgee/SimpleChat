package com.earthgee.simplechat.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * Created by earthgee on 17/2/5.
 * 长连接核心启动
 */
public class ClientCore {

    private Context context;

    public void init(Context context){
        this.context=context;

        //注册sdk连接变化广播
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkListenReceiver,intentFilter);
    }

    private final BroadcastReceiver networkListenReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

}
