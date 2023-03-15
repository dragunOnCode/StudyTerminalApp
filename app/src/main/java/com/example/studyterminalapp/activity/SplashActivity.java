package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.student.StudentHomeActivity;
import com.example.studyterminalapp.activity.teacher.TeacherHomeActivity;
import com.example.studyterminalapp.activity.admin.AdminHomeActivity;
import com.example.studyterminalapp.utils.UserManage;

public class SplashActivity extends AppCompatActivity {

    // todo: https://blog.csdn.net/lb1207087645/article/details/52900657/

    private static final int GO_LOGIN = 0; // 去登录
    private static final int GO_STUDENT_HOME = 1; // 去主页
    private static final int GO_TEACHER_HOME = 2; // 去主页
    private static final int GO_ADMIN_HOME = 3; // 去主页

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
                    Intent intent1 = new Intent(SplashActivity.this, StudentHomeActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                case GO_TEACHER_HOME:
                    Intent intent2 = new Intent(SplashActivity.this, TeacherHomeActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
                case GO_ADMIN_HOME:
                    Intent intent3 = new Intent(SplashActivity.this, AdminHomeActivity.class);
                    startActivity(intent3);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!UserManage.getInstance().hasUserInfo(this))//自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
        {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
        } else {
            switch (UserManage.getInstance().getUserInfo(this).getRole()) {
                case 0:
                    mHandler.sendEmptyMessageAtTime(GO_LOGIN, 2000);
                    break;
                case 1:
                    mHandler.sendEmptyMessageAtTime(GO_STUDENT_HOME, 2000);
                    break;
                case 2:
                    mHandler.sendEmptyMessageAtTime(GO_TEACHER_HOME, 2000);
                    break;
                case 3:
                    mHandler.sendEmptyMessageAtTime(GO_ADMIN_HOME, 2000);
                    break;
            }
        }

    }
}