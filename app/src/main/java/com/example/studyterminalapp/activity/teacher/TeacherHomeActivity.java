package com.example.studyterminalapp.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.LoginActivity;
import com.example.studyterminalapp.activity.admin.AdminHomeActivity;
import com.example.studyterminalapp.activity.student.StudentHomeActivity;
import com.example.studyterminalapp.activity.student.StudentProfileActivity;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.SaTokenInfo;
import com.example.studyterminalapp.bean.vo.ProfileTeacherVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class TeacherHomeActivity extends AppCompatActivity {

    private int tid;
    private TextView tvUsername, tvEmail, tvSchool, tvTeacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        // tid = getIntent().getIntExtra("id", 1);
        tid = MyApp.getId();

        initView();
        initData();
    }

    private void initView() {
        tvUsername = findViewById(R.id.tv_username);
        tvTeacherName = findViewById(R.id.tv_teacher_name);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvSchool = (TextView) findViewById(R.id.tv_school);
    }

    private void initData() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("tid", tid);
        RequestManager request = RequestManager.getInstance();
        try {
            request.GetRequest(paramsMap, Constants.TEACHER_PROFILE, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String response) {
                    int code = Integer.parseInt(responseCode);
                    switch (code) {
                        case 200:
                            Log.i("Login Response", response);
                            Type dataType = new TypeToken<Result<ProfileTeacherVo>>(){}.getType();
                            Result<ProfileTeacherVo> result = JsonParse.getInstance().getResult(response, dataType);

                            ProfileTeacherVo data = result.getData();
                            if (data != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvTeacherName.setText(data.getNickname());
                                        tvUsername.setText(data.getUsername());
                                        tvEmail.setText(data.getEmail());
                                        tvSchool.setText(data.getSchool());
                                    }
                                });
                            }
                            break;
                        default:
                            Log.e("Login Response Not 200", response);
                            break;
                    }
                }

                @Override
                public void onError(String msg) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}