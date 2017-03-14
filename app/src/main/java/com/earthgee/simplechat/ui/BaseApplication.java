package com.earthgee.simplechat.ui;

import android.app.Application;
import android.content.Context;

import com.earthgee.simplechat.net.ConnectionManager;

/**
 * Created by earthgee on 17/2/5.
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ConnectionManager.getInstance(getApplicationContext()).init();
    }
}
