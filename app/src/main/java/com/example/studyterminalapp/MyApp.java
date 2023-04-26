package com.example.studyterminalapp;

import android.app.Application;

import com.example.studyterminalapp.bean.Answer;
import com.xuexiang.xui.XUI;

import java.util.HashMap;

public class MyApp extends Application {
    private static MyApp mContext;
    private static int id;
    private static HashMap<Integer, Answer> answerMap;

    @Override
    public void onCreate() {
        XUI.init(this);
        XUI.debug(true);
        super.onCreate();
        this.mContext = this;
        answerMap = new HashMap<>();
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

    public static HashMap<Integer, Answer> getAnswers() {
        return answerMap;
    }

    public static void addAnswer(int qid, Answer answer) {
        answerMap.put(qid, answer);
    }

    public static void clearAnswers() {
        answerMap.clear();
    }
}
