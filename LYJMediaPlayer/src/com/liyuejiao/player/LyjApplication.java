package com.liyuejiao.player;

import android.app.Application;

public class LyjApplication extends Application {
    public static LyjApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
