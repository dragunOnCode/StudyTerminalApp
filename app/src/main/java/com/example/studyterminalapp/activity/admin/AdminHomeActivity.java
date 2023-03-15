package com.example.studyterminalapp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.ProfileAdminVo;
import com.example.studyterminalapp.bean.vo.ProfileTeacherVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class AdminHomeActivity extends AppCompatActivity {
    private int aid;
    private TextView tvUsername, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        //aid = getIntent().getIntExtra("id", 1);
        aid = MyApp.getId();
        initView();
        initData();
    }

    private void initView() {
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvUsername = (TextView) findViewById(R.id.tv_username);
    }

    private void initData() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("aid", aid);
        RequestManager request = RequestManager.getInstance();
        try {
            request.GetRequest(paramsMap, Constants.ADMIN_PROFILE, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String response) {
                    int code = Integer.parseInt(responseCode);
                    switch (code) {
                        case 200:
                            Log.i("Login Response", response);
                            Type dataType = new TypeToken<Result<ProfileAdminVo>>(){}.getType();
                            Result<ProfileAdminVo> result = JsonParse.getInstance().getResult(response, dataType);

                            ProfileAdminVo data = result.getData();
                            if (data != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvUsername.setText(data.getUsername());
                                        tvEmail.setText(data.getEmail());
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