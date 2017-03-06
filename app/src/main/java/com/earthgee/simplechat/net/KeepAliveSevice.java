package com.earthgee.simplechat.net;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by earthgee on 17/3/1.
 * 心跳维持
 */
public class KeepAliveSevice extends IntentService{

    public static int KEEP_ALIVE_INTERVAL=60*1000;
    public static int NETWORK_CONNECTION_TIME_OUT=121*1000;

    private static long lastResponseKeepAliveTime=0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public KeepAliveSevice() {
        super("KeepAliveSevice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestSender.getInstance().sendKeepAlive();

        if(lastResponseKeepAliveTime!=0&&
                System.currentTimeMillis()-lastResponseKeepAliveTime>=NETWORK_CONNECTION_TIME_OUT){
            //服务端挂了?
            stopPollingService(getBaseContext());
        }

    }

    public static void startPollingService(Context context){
        AlarmManager manager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,KeepAliveSevice.class);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intent, 0);
        //PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), KEEP_ALIVE_INTERVAL, pendingIntent);
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
