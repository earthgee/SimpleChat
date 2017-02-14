package com.example;


import org.apache.mina.core.session.ExpiringSessionRecycler;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by earthgee on 17/2/14.
 */
public abstract class ServerLauncher {

    public static int PORT=7901;
    public static int SESSION_RECYCLER_EXPIRE=10;

    protected ServerCoreHandler serverCoreHandler;
    private NioDatagramAcceptor acceptor;

    protected abstract void initListeners();

    public void startUp() throws IOException{
        this.serverCoreHandler=initServerCoreHandler();
        initListeners();
        acceptor=initAcceptor();
    }

    protected ServerCoreHandler initServerCoreHandler(){
        return new ServerCoreHandler();
    }

    public void setServerEventListener(ServerEventListener serverEventListener){
        serverCoreHandler.setServerEventListener(serverEventListener);
    }

    protected NioDatagramAcceptor initAcceptor(){
        NioDatagramAcceptor acceptor=new NioDatagramAcceptor();
        acceptor.getFilterChain().addLast("threadPool",
                new ExecutorFilter(Executors.newCachedThreadPool()));
        acceptor.setHandler(serverCoreHandler);
        acceptor.setSessionRecycler(new ExpiringSessionRecycler(SESSION_RECYCLER_EXPIRE));
        return acceptor;
    }


}
