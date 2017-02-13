package com.earthgee.simplechat.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * Created by earthgee on 17/2/5.
 */
public class ClientCore {

    private Context context;
    private boolean mLocalNetAvaiable;

    private int mCurrentUserId;
    private boolean isLogin;

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
            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getActiveNetworkInfo()!=null
                    &&connectivityManager.getActiveNetworkInfo().isAvailable()){
                //目前网络连接
                mLocalNetAvaiable=true;
            }else{
                //目前网络断开
                mLocalNetAvaiable=false;
            }
        }
    };

    public boolean ismLocalNetAvaiable() {
        return mLocalNetAvaiable;
    }

    public void setCurrentUserId(int mCurrentUserId) {
        this.mCurrentUserId = mCurrentUserId;
    }
}
