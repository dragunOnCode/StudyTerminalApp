package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.student.StudentHomeActivity;
import com.example.studyterminalapp.activity.teacher.TeacherHomeActivity;
import com.example.studyterminalapp.activity.admin.AdminHomeActivity;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.SaTokenInfo;
import com.example.studyterminalapp.bean.UserInfo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.utils.UserManage;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    // todo: https://blog.csdn.net/lb1207087645/article/details/52900657/

    private static final int GO_LOGIN = 0; // 去登录
    private static final int GO_STUDENT_HOME = 1; // 去主页
    private static final int GO_TEACHER_HOME = 2; // 去主页
    private static final int GO_ADMIN_HOME = 3; // 去主页
    private UserInfo userInfo;

    /**
     * 跳转判断
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_LOGIN:
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case GO_STUDENT_HOME:
//                    Intent intent1 = new Intent(SplashActivity.this, StudentHomeActivity.class);
//                    startActivity(intent1);
//                    finish();
                    break;
                case GO_TEACHER_HOME:
//                    Intent intent2 = new Intent(SplashActivity.this, TeacherHomeActivity.class);
//                    startActivity(intent2);
//                    finish();
                    break;
                case GO_ADMIN_HOME:
//                    Intent intent3 = new Intent(SplashActivity.this, AdminHomeActivity.class);
//                    startActivity(intent3);
//                    finish();
                    break;
            }
            HashMap<String, Object> param = new HashMap<>();
            String username = userInfo.getUsername();
            String password = userInfo.getPassword();
            int loginStatus = userInfo.getRole();
            param.put("username", username);
            param.put("password", password);
            param.put("loginStatus", loginStatus);
            RequestManager request = RequestManager.getInstance();
            try {
                int loginRole = loginStatus;
                request.PostRequest(param, Constants.DO_LOGIN, new RequestManager.ResultCallback() {
                    @Override
                    public void onResponse(String responseCode, String response) {
                        Type dataType = new TypeToken<Result<SaTokenInfo>>(){}.getType();
                        Result<SaTokenInfo> result = JsonParse.getInstance().getResult(response, dataType);
                        Integer code = result.getStatus();
                        switch (code) {
                            case 200:
                                Log.i("Login Response", response);
                                // 跳转
                                SaTokenInfo data = result.getData();
                                String type = data.getLoginId().substring(0, 1);
                                int id = Integer.parseInt(data.getLoginId().substring(1));
                                MyApp.setId(id);
                                Class cls = null;
                                if ("s".equals(type)) {
                                    cls = StudentHomeActivity.class;
                                } else if ("t".equals(type)) {
                                    cls = TeacherHomeActivity.class;
                                } else if ("a".equals(type)) {
                                    cls = AdminHomeActivity.class;
                                } else {
                                    runOnUiThread(() -> Toast.makeText(SplashActivity.this, "没有选择用户身份", Toast.LENGTH_SHORT));
                                    return;
                                }
                                // 保存用户信息到SharedPreferences
                                UserManage.getInstance().saveUserInfo(SplashActivity.this, username, password, loginRole, data.getTokenValue());

                                Intent intent = new Intent(SplashActivity.this, cls);
                                intent.putExtra("id", id);
                                SplashActivity.this.startActivity(intent);
                                break;
                            default:
                                Log.e("Login Response Not 200", response);
                                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("Login Error", msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UserManage.getInstance().clearUserInfo(SplashActivity.this);
                                Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userInfo = UserManage.getInstance().getUserInfo(this);
        if (!UserManage.getInstance().hasUserInfo(this))//自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
        {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
        } else {
            switch (UserManage.getInstance().getUserInfo(this).getRole()) {
                case 0:
                    mHandler.sendEmptyMessageAtTime(GO_STUDENT_HOME, 2000);
                    break;
                case 1:
                    mHandler.sendEmptyMessageAtTime(GO_TEACHER_HOME, 2000);
                    break;
                case 2:
                    mHandler.sendEmptyMessageAtTime(GO_ADMIN_HOME, 2000);
                    break;
            }
        }

    }
}