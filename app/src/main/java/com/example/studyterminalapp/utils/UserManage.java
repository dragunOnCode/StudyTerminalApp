package com.example.studyterminalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.studyterminalapp.bean.UserInfo;

/**
 * 保存用户信息的管理类
 */
public class UserManage {
    private static UserManage instance;

    private UserManage() {}

    public synchronized static UserManage getInstance() {
        if (instance == null) {
            instance = new UserManage();
        }
        return instance;
    }

    /**
     * 保存自动登录的用户信息
     */
    public void saveUserInfo(Context context, String username, String password, Integer role, String tokenValue) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USERNAME", username);
        editor.putString("PASSWORD", password);
        editor.putInt("ROLE", role);
        editor.putString("TOKENVALUE", tokenValue);
        editor.commit();
    }

    /**
     * 清除保存的用户信息
     */
    public void clearUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("USERNAME");
        editor.remove("PASSWORD");
        editor.remove("ROLE");
        editor.remove("TOKENVALUE");
        editor.commit();
    }


    /**
     * 获取用户信息model
     *
     * @param context
     * @param
     * @param
     */
    public UserInfo getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(sp.getString("USERNAME", ""));
        userInfo.setPassword(sp.getString("PASSWORD", ""));
        userInfo.setRole(sp.getInt("ROLE", 0));
        userInfo.setPassword(sp.getString("TOKENVALUE", ""));
        return userInfo;
    }


    /**
     * userInfo中是否有数据
     */
    public boolean hasUserInfo(Context context) {
        UserInfo userInfo = getUserInfo(context);
        if (userInfo != null) {
            if ((!TextUtils.isEmpty(userInfo.getUsername())) && (!TextUtils.isEmpty(userInfo.getPassword()))) {//有数据
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
