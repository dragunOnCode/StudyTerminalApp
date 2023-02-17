package com.example.studyterminalapp;

import android.app.Application;

import com.xuexiang.xui.XUI;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        XUI.init(this);
        XUI.debug(true);
        super.onCreate();
    }
}
