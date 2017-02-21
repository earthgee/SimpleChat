package com.example;


import org.apache.mina.core.session.ExpiringSessionRecycler;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by earthgee on 17/2/14.
 */
public abstract class ServerLauncher {

    public static int PORT=7901;
    public static int SESSION_RECYCLER_EXPIRE=1000*60;

    protected ServerCoreHandler serverCoreHandler;
    private NioDatagramAcceptor acceptor;

    protected abstract void initListeners();

    public void startUp() throws IOException{
        this.serverCoreHandler=initServerCoreHandler();
        initListeners();
        acceptor=initAcceptor();
        initSessionConfig(acceptor);
        acceptor.bind(new InetSocketAddress(PORT));
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

    protected void initSessionConfig(NioDatagramAcceptor acceptor){
        DatagramSessionConfig dcfg=acceptor.getSessionConfig();
        dcfg.setReuseAddress(true);
        dcfg.setReceiveBufferSize(1024);
        dcfg.setSendBufferSize(1024);
    }

}
