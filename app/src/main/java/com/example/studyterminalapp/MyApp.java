package com.example.studyterminalapp;

import android.app.Application;

import com.xuexiang.xui.XUI;

public class MyApp extends Application {
    private static MyApp mContext;
    private static int id;

    @Override
    public void onCreate() {
        XUI.init(this);
        XUI.debug(true);
        super.onCreate();
        this.mContext = this;
    }

    public static MyApp getApplication(){
        return mContext;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        MyApp.id = id;
    }
}
