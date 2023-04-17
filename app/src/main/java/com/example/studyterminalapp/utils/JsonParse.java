package com.example.studyterminalapp.utils;

import com.example.studyterminalapp.bean.ContactBean;
import com.example.studyterminalapp.bean.HomeClassBean;
import com.example.studyterminalapp.bean.OrderBean;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.SaTokenInfo;
import com.example.studyterminalapp.bean.StudentBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 单例
 */
public class JsonParse {
    // 3.定义一个静态对象
    private static JsonParse instance;

    // 1.私有构造函数避免被其他类使用
    private JsonParse(){}

    // 2.提供一个方法获取实例
    public synchronized static JsonParse getInstance(){
        if (instance == null) {
            instance = new JsonParse();
        }
        return instance;
    }

    public List<HomeClassBean> getHomeList(String json) {
        Type listType = new TypeToken<List<HomeClassBean>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public StudentBean getStudent(String json) {
        Type listType = new TypeToken<StudentBean>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public List<OrderBean> getOrderList(String json) {
        Type listType = new TypeToken<List<OrderBean>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public List<ContactBean> getContactList(String json) {
        Type listType = new TypeToken<List<ContactBean>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public <T> Result<T> getResult(String json, Type dataType) {
        return new Gson().fromJson(json, dataType);
    }

    public <T> Result<T> getPageInfoResult(String json, Type dataType) {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create()
                .fromJson(json, dataType);
    }
}
