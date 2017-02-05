package com.earthgee.simplechat.net;


import android.content.Context;

/**
 * Created by earthgee on 17/2/5.
 * 启动长连接，单例
 */
public class ConnectionManager {

    private Context context;
    private ClientCore clientCore;

    private static ConnectionManager instance;

    private ConnectionManager(Context context){
        this.context=context;
    }

    public static ConnectionManager getInstance(Context context){
        if(instance==null){
            instance=new ConnectionManager(context);
        }
        return instance;
    }

    public void init(){
        clientCore=new ClientCore();

    }

}
