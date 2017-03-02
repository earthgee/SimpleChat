package com.earthgee.simplechat.net;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by earthgee on 17/3/1.
 * 心跳维持
 */
public class KeepAliveSevice extends IntentService{

    public static int KEEP_ALIVE_INTERVAL=3000;
    public static int NETWORK_CONNECTION_TIME_OUT=10000;

    private static long lastResponseKeepAliveTime=0;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public KeepAliveSevice(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestSender.getInstance().sendKeepAlive();

        if(System.currentTimeMillis()-lastResponseKeepAliveTime>=NETWORK_CONNECTION_TIME_OUT){
            //服务端挂了?
            stopPollingService(getBaseContext());
        }

    }

    public static void startPollingService(Context context){
        AlarmManager manager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,KeepAliveSevice.class);
        PendingIntent pendingIntent=PendingIntent.getService(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        long startTime= SystemClock.elapsedRealtime();

        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, startTime, KEEP_ALIVE_INTERVAL, pendingIntent);
    }

    public static void stopPollingService(Context context){
        AlarmManager manager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,KeepAliveSevice.class);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.cancel(pendingIntent);
    }

    public static void updateKeepAliveResponseServerTimeStamp(){
        lastResponseKeepAliveTime=System.currentTimeMillis();
    }

}
