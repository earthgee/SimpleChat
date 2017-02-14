package com.example;

import java.io.IOException;

/**
 * Created by earthgee on 17/2/14.
 */
public class ServerLauncherImpl extends ServerLauncher{

    private static ServerLauncherImpl instance=null;

    public static ServerLauncherImpl getInstance() throws IOException {
        if(instance==null){
            instance=new ServerLauncherImpl();
        }
        return instance;
    }

    private ServerLauncherImpl() throws IOException{
        super();
    }

    public static void main(String[] args) throws IOException{
        ServerLauncherImpl.getInstance().startUp();
    }

    @Override
    protected void initListeners() {
        setServerEventListener(new ServerEventListenerImpl());

    }

}
