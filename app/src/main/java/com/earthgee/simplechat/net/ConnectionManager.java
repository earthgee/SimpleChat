package com.earthgee.simplechat.net;


import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by earthgee on 17/2/5.
 * 启动长连接，单例
 */
public class ConnectionManager {

    private Context context;
    private ClientCore clientCore;
    private LocalUDPProvider localUDPProvider;

    private String userName;
    private String password;

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

    public static ConnectionManager getInstance(){
        return instance;
    }

    public void init(){
        clientCore=new ClientCore();
        localUDPProvider=new LocalUDPProvider();
    }

    public boolean isNetAvaiable(){
        return clientCore.ismLocalNetAvaiable();
    }

    public LocalUDPProvider getLocalUDPProvider() {
        return localUDPProvider;
    }

    public ClientCore getClientCore() {
        return clientCore;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
